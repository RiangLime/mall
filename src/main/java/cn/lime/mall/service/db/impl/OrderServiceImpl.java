package cn.lime.mall.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.*;
import cn.lime.core.module.dto.OrderPayDto;
import cn.lime.core.module.entity.Order;
import cn.lime.core.module.entity.User;
import cn.lime.core.module.entity.Userthirdauthorization;
import cn.lime.core.module.vo.OrderPayVo;
import cn.lime.mall.constant.OrderStatus;
import cn.lime.mall.constant.PaymentTypeEnum;
import cn.lime.mall.service.db.OrderService;
import cn.lime.core.service.db.UserService;
import cn.lime.core.service.db.UserthirdauthorizationService;
import cn.lime.mall.service.stripe.StripePayService;
import cn.lime.mall.service.wx.payment.WxPayFactory;
import cn.lime.mall.service.wx.payment.WxPayService;
import cn.lime.core.threadlocal.ReqThreadLocal;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.RefundNotification;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static cn.lime.core.constant.RedisDb.PAYMENT_EXPIRE_DB;


/**
* @author riang
* @description 针对表【Order(订单表)】的数据库操作Service实现
* @createDate 2024-03-15 14:29:53
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService {

    @Resource
    private UserService userService;
    @Resource
    private UserthirdauthorizationService openIdService;
    @Resource
    private StripePayService stripeService;
    @Resource
    private Map<Integer, StringRedisTemplate> redisTemplateMap;
    /**
     * 单位毫秒
     */
    @Value("${payment.wx.timeout: 30000}")
    private Long paymentTimeout;
    /**
     * 单位秒 3小时
     */
    @Value("${payment.order.timeout: 10800}")
    private Integer orderTimeout;

    @Override
    public Order getOrder(Long orderId) {
        Order order = lambdaQuery().eq(Order::getOrderId,orderId).one();
        ThrowUtils.throwIf(ObjectUtils.isEmpty(order), ErrorCode.NOT_FOUND_ERROR);
        return order;
    }

    @Override
    public Order createOrder(Long orderId, Long userId, Long productId, Integer orderPrice, String remark) {
        Order order = Order.builder().orderId(orderId).userId(userId).productId(productId).orderPrice(orderPrice).remark(remark).build();
        ThrowUtils.throwIf(!save(order),ErrorCode.INSERT_ERROR);
        return order;
    }

    @Override
    public Boolean cancelOrder(Long orderId) {
        return lambdaUpdate()
                .eq(Order::getOrderId, orderId)
                .eq(Order::getOrderStatus, OrderStatus.WAITING_PAY.getVal())
                .update();
    }

    @Override
    @Transactional
    public OrderPayVo payOrder(OrderPayDto dto) {
        Order order = getOrder(dto.getOrderId());
        User user = userService.getById(ReqThreadLocal.getInfo().getUserId());
        ThrowUtils.throwIf(!order.getUserId().equals(user.getUserId()), ErrorCode.AUTH_FAIL);
        ThrowUtils.throwIf(!updateOrderStatusFromWaitingPayToPaying(dto.getOrderId()), ErrorCode.UPDATE_ERROR, "更新订单状态异常");
        //
        if (dto.getPayMethod().equals(PaymentTypeEnum.STRIPE.getVal())) {
            return doStripePay(order,dto.getSuccessUrl(),dto.getCancelUrl());
        } else {
            return doWxPay(order, dto.getPayMethod());
        }
    }

    private OrderPayVo doStripePay(Order order, String successUrl, String cancelUrl) {
        return stripeService.doStripePay(order,successUrl,cancelUrl);
    }

    private OrderPayVo doWxPay(Order order, Integer paymentType) {
        WxPayService wxPayService = WxPayFactory.get(paymentType);
        String openId = null;
        if (paymentType.equals(PaymentTypeEnum.JS_API.getVal())){
            Userthirdauthorization userthirdauthorization = openIdService.lambdaQuery()
                    .eq(Userthirdauthorization::getPersonnelId,ReqThreadLocal.getInfo().getUserId())
                    .eq(Userthirdauthorization::getAppPlatform,ReqThreadLocal.getInfo().getPlatform())
                    .one();
            ThrowUtils.throwIf(ObjectUtils.isEmpty(userthirdauthorization), ErrorCode.CANNOT_FIND_BY_ID, "查询用户小程序关联OPENID异常");
            openId = userthirdauthorization.getThirdSecondTag();
        }
        return wxPayService.prepay(order.getOrderId(), order.getOrderPrice(), PayCallBackUrl.ORDER_CALLBACK_URL_WX, openId);
    }

    @Override
    public Boolean updateOrderStatusFromWaitingPayToPaying(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .eq(Order::getOrderStatus, OrderStatus.WAITING_PAY.getVal())
                .set(Order::getOrderStatus, OrderStatus.PAYING.getVal())
                .update();
    }

    @Override
    public Boolean updateOrderStatusFromWaitingPayToClose(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .eq(Order::getOrderStatus, OrderStatus.WAITING_PAY.getVal())
                .set(Order::getOrderStatus, OrderStatus.CLOSE.getVal())
                .update();
    }

    @Override
    public Boolean updateOrderStatusFromPayingToWaitingPay(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .eq(Order::getOrderStatus, OrderStatus.PAYING.getVal())
                .set(Order::getOrderStatus, OrderStatus.WAITING_PAY.getVal())
                .update();
    }

    @Override
    public Boolean updateOrderStatusFromPayingToClose(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .set(Order::getOrderStatus, OrderStatus.CLOSE.getVal())
                .update();
    }

    @Override
    public Boolean updateOrderStatusFromPayingToFinish(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .set(Order::getOrderStatus, OrderStatus.FINISH.getVal())
                .update();
    }

    @Override
    public Boolean updateOrderStatusFromPayingToPayed(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .set(Order::getOrderStatus, OrderStatus.WAITING_SEND.getVal())
                .update();
    }

    @Override
    public Boolean updateOrderStatusFromPayedToFinish(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .eq(Order::getOrderStatus,OrderStatus.WAITING_SEND.getVal())
                .set(Order::getOrderStatus, OrderStatus.FINISH.getVal())
                .update();
    }

    @Override
    public Integer updateTimeoutWaitingPayOrder(Integer timeout) {
        return baseMapper.updateTimeoutWaitingOrder(timeout);
    }


    @Override
    @Transactional
    public void doOrderCallback(Transaction transaction) {
        Order order = getById(transaction.getOutTradeNo());
        ThrowUtils.throwIf(ObjectUtils.isEmpty(order), ErrorCode.NOT_FOUND_ERROR);
        // 微信回调函数已经处理过
        if (order.getOrderIsDeal().equals(YesNoEnum.YES.getVal())) {
            return;
        }
        if (ObjectUtils.isEmpty(order.getThirdPaymentId())) {
            // 处理微信回调字段
            ThrowUtils.throwIf(!lambdaUpdate()
                    .eq(Order::getOrderId, order.getOrderId())
                    .set(Order::getThirdPaymentId, transaction.getTransactionId())
                    .update(), ErrorCode.UPDATE_ERROR);
        }
        // 支付成功
        if (transaction.getTradeState().equals(Transaction.TradeStateEnum.SUCCESS)) {
            ThrowUtils.throwIf(!updateOrderStatusFromPayingToPayed(order.getOrderId()), ErrorCode.UPDATE_ERROR);
        }
        // 支付失败
        else if (transaction.getTradeState().equals(Transaction.TradeStateEnum.PAYERROR)
                || transaction.getTradeState().equals(Transaction.TradeStateEnum.CLOSED)) {
            ThrowUtils.throwIf(!updateOrderStatusFromPayingToClose(order.getOrderId()), ErrorCode.UPDATE_ERROR);
        }
        // 处理微信回调字段
        ThrowUtils.throwIf(!lambdaUpdate()
                .eq(Order::getOrderId, order.getOrderId())
                .set(Order::getOrderIsDeal, YesNoEnum.YES.getVal())
                .set(Order::getThirdPaymentId,transaction.getTransactionId())
                .update(), ErrorCode.UPDATE_ERROR);
    }

    @Override
    public void doRefundCallback(Refund refund) {
        RefundStatus refundStatus = null;
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getOrderId, Long.valueOf(refund.getOutTradeNo()));

        switch (refund.getStatus()) {
            case ABNORMAL -> {
                refundStatus = RefundStatus.FAIL;
                wrapper.set(Order::getRefundIsDeal,YesNoEnum.YES.getVal());
            }
            case SUCCESS -> {
                refundStatus = RefundStatus.SUCCESS;
                wrapper.set(Order::getRefundIsDeal,YesNoEnum.YES.getVal());
            }
            case PROCESSING -> refundStatus = RefundStatus.PROCESSING;
            case CLOSED -> {
                refundStatus = RefundStatus.CLOSED;
                wrapper.set(Order::getRefundIsDeal,YesNoEnum.YES.getVal());
            }
        }
        wrapper.set(Order::getRefundStatus, refundStatus.getVal());
        ThrowUtils.throwIf(!update(wrapper), ErrorCode.UPDATE_ERROR);
    }

    @Override
    public void doRefundCallback(RefundNotification refundNotification) {
        RefundStatus refundStatus = null;
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getOrderId, Long.valueOf(refundNotification.getOutTradeNo()));

        switch (refundNotification.getRefundStatus()) {
            case ABNORMAL -> {
                refundStatus = RefundStatus.FAIL;
                wrapper.set(Order::getRefundIsDeal,YesNoEnum.YES.getVal());
            }
            case SUCCESS -> {
                refundStatus = RefundStatus.SUCCESS;
                wrapper.set(Order::getRefundIsDeal,YesNoEnum.YES.getVal());
            }
            case PROCESSING -> refundStatus = RefundStatus.PROCESSING;
            case CLOSED -> {
                refundStatus = RefundStatus.CLOSED;
                wrapper.set(Order::getRefundIsDeal,YesNoEnum.YES.getVal());
            }
        }
        wrapper.set(Order::getRefundStatus, refundStatus.getVal());
        ThrowUtils.throwIf(!update(wrapper), ErrorCode.UPDATE_ERROR);
    }

    @Override
    @Transactional
    public void doOrderCallback(PaymentIntent paymentIntent) {
        Order order = getOrder(Long.valueOf(paymentIntent.getId()));
        doOrderCallbackStripe(order,paymentIntent);
    }

    @Override
    @Transactional
    public void doOrderCallback(Session stripeSession) {
        Order order = getOrder(Long.parseLong(stripeSession.getClientReferenceId()));
        doOrderCallbackStripe(order,stripeSession);

    }

    @Transactional
    public void doOrderCallbackStripe(Order order, Object stripeEvent){
        // 回调函数已经处理过
        if (order.getOrderIsDeal().equals(YesNoEnum.YES.getVal())) {
            return;
        }
        // 支付成功
        if (ObjectUtils.isNotEmpty(stripeEvent)) {
            ThrowUtils.throwIf(!updateOrderStatusFromPayingToFinish(order.getOrderId()), ErrorCode.UPDATE_ERROR);
        }
        // 支付失败
        else {
            ThrowUtils.throwIf(!updateOrderStatusFromPayingToClose(order.getOrderId()), ErrorCode.UPDATE_ERROR);
        }
        // 处理微信回调字段
        ThrowUtils.throwIf(!lambdaUpdate()
                .eq(Order::getOrderId, order.getOrderId())
                .set(Order::getOrderIsDeal, YesNoEnum.YES.getVal())
                .update(), ErrorCode.UPDATE_ERROR);
    }

    /**
     * 支付超时扫描任务
     */
    @Scheduled(cron = "${payment.wx.schedule-scan: 30 * * * * ?}")
    @Transactional
    // 不支持分布式
    public void scheduleClearTimeoutPayment() {
        Map<Object, Object> cache = redisTemplateMap.get(PAYMENT_EXPIRE_DB.getVal())
                .opsForHash().entries(RedisKeyName.PAY_EXPIRE.getVal());
        cache.forEach((k, v) -> {
            Long timestamp = Long.valueOf(v.toString());
            // 超时付款
            if (timestamp + paymentTimeout < System.currentTimeMillis()) {
                Long orderId = Long.valueOf(k.toString());
                Order order = getById(orderId);
                // 如果没被处理 就更新为代付款
                // 如果只是第三方付款有时间差  回调函数会强制改为付款成功
                if (order.getOrderIsDeal() == YesNoEnum.NO.getVal()){
                    updateOrderStatusFromPayingToWaitingPay(orderId);
                }
                // 删除该key
                redisTemplateMap.get(PAYMENT_EXPIRE_DB.getVal()).opsForHash().delete(RedisKeyName.PAY_EXPIRE.getVal(), k.toString());
            }
        });
    }

    /**
     * 支付订单未支付状态超时扫描任务
     */
    @Scheduled(cron = "${payment.wx.schedule-scan: 30 * * * * ?}")
    @Transactional
    public void scheduleClearUnPayedTimeoutPayment() {
        int expireNum = updateTimeoutWaitingPayOrder(orderTimeout);
        if (expireNum != 0) {
            log.warn("[EXPIRE_ORDER] FIND ORDER EXPIRE");
        }
    }
}




