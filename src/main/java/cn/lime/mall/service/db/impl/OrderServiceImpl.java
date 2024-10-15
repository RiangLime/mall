package cn.lime.mall.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.PageResult;
import cn.lime.core.common.PageUtils;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.*;
import cn.lime.core.module.entity.User;
import cn.lime.core.module.entity.Userthirdauthorization;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.mall.config.MallParams;
import cn.lime.mall.constant.OrderStatus;
import cn.lime.mall.constant.PayCallBackUrl;
import cn.lime.mall.constant.PaymentTypeEnum;
import cn.lime.mall.constant.RefundStatus;
import cn.lime.mall.controller.OrderController;
import cn.lime.mall.mapper.OrderMapper;
import cn.lime.mall.model.dto.order.OrderItemDto;
import cn.lime.mall.model.dto.order.OrderPayDto;
import cn.lime.mall.model.entity.*;
import cn.lime.mall.model.vo.*;
import cn.lime.mall.service.db.*;
import cn.lime.core.service.db.UserService;
import cn.lime.core.service.db.UserthirdauthorizationService;
import cn.lime.mall.service.stripe.StripePayService;
import cn.lime.mall.service.wx.payment.WxPayFactory;
import cn.lime.mall.service.wx.payment.WxPayService;
import cn.lime.core.threadlocal.ReqThreadLocal;
import cn.lime.mall.util.OrderCodeGenerator;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.RefundNotification;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static cn.lime.core.constant.RedisDb.PAYMENT_EXPIRE_DB;


/**
 * @author riang
 * @description 针对表【Order(订单表)】的数据库操作Service实现
 * @createDate 2024-03-15 14:29:53
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

    @Resource
    private UserService userService;
    @Resource
    private SkuService skuService;
    @Resource
    private SkuattributeService skuattributeService;
    @Resource
    private ProductService productService;
    @Resource
    private UserthirdauthorizationService openIdService;
    @Resource
    private StripePayService stripeService;
    @Resource
    private Map<Integer, StringRedisTemplate> redisTemplateMap;
    @Resource
    private MallParams params;
    @Resource
    private OrderItemService orderItemService;
    @Resource
    private SnowFlakeGenerator ids;
    @Resource
    private AddressService addressService;
    @Resource
    private OrderOperateLogService logService;
    @Resource
    private CartService cartService;
    @Resource
    private DiscountService discountService;

    @Override
    public Order getById(Serializable orderId) {
        Order order = lambdaQuery().eq(Order::getOrderId, orderId).one();
        ThrowUtils.throwIf(ObjectUtils.isEmpty(order), ErrorCode.NOT_FOUND_ERROR);
        return order;
    }

    @Override
    @Transactional
    public Order createOrder(Long userId, Integer addressId, List<OrderItemDto> orderItems, String remark, Long discountId) {
        int price = 0;
        for (OrderItemDto orderItem : orderItems) {
            Sku sku = skuService.getById(orderItem.getSkuId());
            ThrowUtils.throwIf(ObjectUtils.isEmpty(sku), ErrorCode.NOT_FOUND_ERROR);
            ThrowUtils.throwIf(sku.getStock() < orderItem.getNumber(), ErrorCode.PARAMS_ERROR, "库存不足");
            price += sku.getPrice() * orderItem.getNumber();
        }

        int realPrice = price;

        // 默认原价生成订单,后续有优惠政策在业务模块修改订单价格
        Order order = new Order();
        order.setOrderId(ids.nextId());
        order.setOrderCode(OrderCodeGenerator.get());
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setOriginOrderPrice(price);
        order.setRealOrderPrice(realPrice);
        order.setRemark1(remark);
        order.setOrderSource(ReqThreadLocal.getInfo().getPlatform());
        order.setOrderStatus(OrderStatus.WAITING_PAY.getVal());
        ThrowUtils.throwIf(!save(order), ErrorCode.INSERT_ERROR);
//        ThrowUtils.throwIf(!baseMapper.insertOrder(order.getOrderId(),order.getOrderCode(),order.getUserId(),order.getAddressId(),
//                order.getOriginOrderPrice(),order.getRealOrderPrice(),order.getRemark1()), ErrorCode.INSERT_ERROR);
        for (OrderItemDto orderItem : orderItems) {
            OrderItem bean = new OrderItem();
            ProductDetailVo product = productService.getProductDetailWithoutLog(orderItem.getProductId(),null);
            Sku sku = skuService.getById(orderItem.getSkuId());
            ThrowUtils.throwIf(ObjectUtils.isEmpty(sku), ErrorCode.NOT_FOUND_ERROR);
            bean.setOrderId(order.getOrderId());
            bean.setId(ids.nextId());
            bean.setProductId(orderItem.getProductId());
            bean.setSkuId(orderItem.getSkuId());
            bean.setNumber(orderItem.getNumber());
            bean.setProductName(product.getProductName());
            bean.setProductMainPic(product.getMainUrl());
            for (SkuInfoVo skuInfo : product.getSkuInfos()) {
                if (skuInfo.getSkuId().equals(orderItem.getSkuId())){
                    bean.setSkuAttribute(JSON.toJSONString(skuattributeService.getSkuAttributeVos(orderItem.getSkuId())));
                }
            }
            bean.setItemPrice(orderItem.getNumber() * sku.getPrice());
            ThrowUtils.throwIf(!orderItemService.save(bean), ErrorCode.INSERT_ERROR, "生成购买物品失败");
            // 锁库存
            boolean res = skuService.lambdaUpdate()
                    .eq(Sku::getSkuId, sku.getSkuId())
                    .set(Sku::getStock, sku.getStock() - orderItem.getNumber()).update();
            ThrowUtils.throwIf(!res, ErrorCode.UPDATE_ERROR, "锁定库存失败");

        }
        logService.log(order.getOrderId(), userId, "用户创建订单");

        // 折扣码减价格
        if (ObjectUtils.isNotEmpty(discountId)) {
            int newRealPrice = realPrice - discountService.useDiscount(discountId, order.getOrderId());
            ThrowUtils.throwIf(!lambdaUpdate().eq(Order::getOrderId, order.getOrderId()).set(Order::getRealOrderPrice, newRealPrice).update(),
                    ErrorCode.UPDATE_ERROR, "使用折扣券修改价格失败");
            logService.log(order.getOrderId(), userId, "用户使用折扣券 订单价格:" + realPrice + "->" + newRealPrice);
        }
        return order;
    }

    @Override
    @Transactional
    public Order createOrder(Long userId, Integer addressId, String remark, List<Long> cartIds, Long discountId) {
        List<OrderItemDto> orderItems = new ArrayList<>();
        for (Long cartId : cartIds) {
            Cart cart = cartService.getById(cartId);
            ThrowUtils.throwIf(!ObjectUtils.isEmpty(cart), ErrorCode.NOT_FOUND_ERROR, "该购物车项不存在");
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setProductId(cart.getProductId());
            orderItemDto.setSkuId(cart.getSkuId());
            orderItemDto.setNumber(cart.getNumber());
            orderItems.add(orderItemDto);
            ThrowUtils.throwIf(!cartService.lambdaUpdate().eq(Cart::getId, cartId).remove(),
                    ErrorCode.DELETE_ERROR, "删除购物车项失败");
        }
        return createOrder(userId, addressId, orderItems, remark, discountId);
    }

    @Override
    @Transactional
    public Boolean cancelOrder(Long orderId) {
        Order order = getById(orderId);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(order), ErrorCode.NOT_FOUND_ERROR);
        orderOwnerCheck(order, ReqThreadLocal.getInfo().getUserId());
        ThrowUtils.throwIf(!order.getOrderStatus().equals(OrderStatus.WAITING_PAY.getVal()),
                ErrorCode.PARAMS_ERROR, "仅待付款订单可取消,已付款订单请走退款流程");
        // 库存
        List<OrderItem> orderItems = orderItemService.lambdaQuery().eq(OrderItem::getOrderId, orderId).list();
        for (OrderItem orderItem : orderItems) {
            Sku sku = skuService.getById(orderItem.getSkuId());
            ThrowUtils.throwIf(ObjectUtils.isEmpty(sku), ErrorCode.NOT_FOUND_ERROR);
            // 恢复库存
            boolean res = skuService.lambdaUpdate().eq(Sku::getSkuId, sku.getSkuId())
                    .set(Sku::getStock, sku.getStock() + orderItem.getNumber()).update();
            ThrowUtils.throwIf(!res, ErrorCode.UPDATE_ERROR, "恢复库存失败");
        }
        logService.log(order.getOrderId(), ReqThreadLocal.getInfo().getUserId(), "用户取消订单");
        ThrowUtils.throwIf(!updateOrderStatusFromWaitingPayToClose(orderId), ErrorCode.UPDATE_ERROR, "更新订单状态异常");
        return true;
    }

    @Override
    public OrderDetailVo getOrderDetail(Long orderId) {
        orderOwnerCheck(orderId, ReqThreadLocal.getInfo().getUserId());
        OrderDetailVo vo = new OrderDetailVo();
        // 订单信息
        Order order = getById(orderId);
        vo.fillOrderInfo(order);
        // 用户信息
        User user = userService.getById(order.getUserId());
        vo.fillUserInfo(user);
        // 地址信息
        Address address = addressService.getById(order.getAddressId());
        vo.fillAddressInfo(address);
        // 商品SKU信息
        List<OrderProductSkuVo> productSkuVos = orderItemService.getItemsByOrderId(orderId);
        vo.setOrderSkuList(productSkuVos);
        // 日志信息
        List<OrderOperateLogVo> logs = logService.lambdaQuery().eq(OrderOperateLog::getOrderId, orderId).list().stream().map(OrderOperateLogVo::fromBean).toList();
        vo.setLogs(logs);

        return vo;
    }

    @Override
    public Boolean applyRefund(Long orderId) {
        Order order = getById(orderId);
        orderOwnerCheck(order, ReqThreadLocal.getInfo().getUserId());
        logService.log(order.getOrderId(), ReqThreadLocal.getInfo().getUserId(), "用户申请订单退款");
        return lambdaUpdate().eq(Order::getOrderId, orderId).set(Order::getOrderStatus, OrderStatus.REFUNDING.getVal())
                .set(Order::getRefundId, orderId)
                .set(Order::getRefundStatus, RefundStatus.APPLY.getVal())
                .set(Order::getRefundPrice, order.getRealOrderPrice())
                .update();
    }

    @Override
    public void reviewRefund(Long orderId, Integer isApprove) {
        Order order = getById(orderId);
        if (isApprove == YesNoEnum.NO.getVal()) {
            // 退款拒绝
            ThrowUtils.throwIf(!lambdaUpdate().eq(Order::getOrderId, orderId)
                    .set(Order::getRefundStatus, RefundStatus.CLOSED.getVal()).update(), ErrorCode.UPDATE_ERROR);
        } else {
            // 同意退款
            ThrowUtils.throwIf(!lambdaUpdate().eq(Order::getOrderId, orderId)
                    .set(Order::getRefundStatus, RefundStatus.PROCESSING.getVal())
                    .set(Order::getOrderStatus, OrderStatus.REFUNDING.getVal())
                    .update(), ErrorCode.UPDATE_ERROR);
            refund(orderId);
        }
    }

    @Override
    public void refund(Long orderId) {
        Order order = getById(orderId);
        orderOwnerCheck(order, ReqThreadLocal.getInfo().getUserId());
        logService.log(order.getOrderId(), ReqThreadLocal.getInfo().getUserId(), "管理员同意订单退款");
        WxPayFactory.get(PaymentTypeEnum.WX_PAY_BASE.getVal()).refund(orderId, order.getRefundPrice(),
                params.getWxPayNotifyUrlPrefix() + PayCallBackUrl.ORDER_REFUND_CALLBACK_URL);
    }

    @Override
    public void receive(Long orderId) {
        orderOwnerCheck(orderId, ReqThreadLocal.getInfo().getUserId());
        logService.log(orderId, ReqThreadLocal.getInfo().getUserId(), "用户确认收货");
        ThrowUtils.throwIf(!updateOrderStatusFromWaitingReceiveToWaitingComment(orderId),
                ErrorCode.UPDATE_ERROR, "更新用户订单状态异常");
    }

    @Override
    @Transactional
    public void comment(Long orderId, String comment) {
        Order order = getById(orderId);
        orderOwnerCheck(order, ReqThreadLocal.getInfo().getUserId());
        ThrowUtils.throwIf(StringUtils.isNotEmpty(order.getComment()), ErrorCode.PARAMS_ERROR, "您已经评论过该订单");
        boolean commentRes = lambdaUpdate().eq(Order::getOrderId, orderId).set(Order::getComment, comment).update();
        ThrowUtils.throwIf(!commentRes, ErrorCode.UPDATE_ERROR, "评论失败");
        commentRes = updateOrderStatusFromWaitingCommentToFinish(orderId);
        ThrowUtils.throwIf(!commentRes, ErrorCode.UPDATE_ERROR, "评论失败");
        logService.log(order.getOrderId(), ReqThreadLocal.getInfo().getUserId(), "用户评论订单");
    }

    @Override
    public PageResult<OrderPageVo> getOrderPage(String orderCode, String userName, String productName, String receiverName,
                                                Integer orderState, Long orderUserId, Long orderStartTime, Long orderEndTime,
                                                Integer current, Integer pageSize, String sortField, String sortOrder) {
        Page<?> page = PageUtils.build(current, pageSize, sortField, sortOrder);
        Page<OrderPageVo> res = baseMapper.pageOrder(orderCode, userName, productName, receiverName, orderState,
                orderUserId, orderStartTime, orderEndTime, page);
        for (OrderPageVo record : res.getRecords()) {
            record.setOrderSkuList(baseMapper.getProductSkusByOrderId(record.getOrderId()));
        }
        return new PageResult<>(res);
    }

    @Override
    @Transactional
    public void orderUpdateByAdmin(Long orderId, String merchantRemark, Integer changedPrice) {
        Order order = getById(orderId);
        logService.log(orderId, ReqThreadLocal.getInfo().getUserId(), "管理员修改订单信息" + order.getRealOrderPrice()
                + "->" + changedPrice + ",REMARK[" + merchantRemark + "]");
        ThrowUtils.throwIf(merchantRemark == null && ObjectUtils.isEmpty(changedPrice),
                ErrorCode.PARAMS_ERROR, "不可全为空");
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getOrderId, orderId);
        if (merchantRemark != null) {
            wrapper.set(Order::getRemark2, merchantRemark);
        }
        if (!ObjectUtils.isEmpty(changedPrice)) {
            wrapper.set(Order::getRealOrderPrice, changedPrice);
        }
        ThrowUtils.throwIf(!update(wrapper), ErrorCode.UPDATE_ERROR, "添加订单备注/改价异常");
    }

    @Override
    @Transactional
    public void orderSend(Long orderId, String deliverCompany, String deliverId) {
        logService.log(orderId, ReqThreadLocal.getInfo().getUserId(), "管理员确认订单发货");
        ThrowUtils.throwIf(!lambdaUpdate().eq(Order::getOrderId, orderId)
                .set(Order::getDeliverCompany, deliverCompany)
                .set(Order::getDeliverId, deliverId)
                .set(Order::getSendDeliverTime, new Date()).update(), ErrorCode.UPDATE_ERROR);
        Order order = getById(orderId);
        if (order.getOrderStatus().equals(OrderStatus.WAITING_SEND.getVal())) {
            ThrowUtils.throwIf(!updateOrderStatusFromWaitingSendToWaitingReceive(orderId),
                    ErrorCode.UPDATE_ERROR, "更新订单状态异常");
        }
    }

    @Override
//    @Transactional
    public OrderPayVo payOrder(OrderPayDto dto) {
        Order order = getById(dto.getOrderId());
        orderOwnerCheck(order, ReqThreadLocal.getInfo().getUserId());
        User user = userService.getById(ReqThreadLocal.getInfo().getUserId());
        ThrowUtils.throwIf(!order.getUserId().equals(user.getUserId()), ErrorCode.AUTH_FAIL);
        ThrowUtils.throwIf(!order.getOrderStatus().equals(OrderStatus.WAITING_PAY.getVal())
                        && !order.getOrderStatus().equals(OrderStatus.PAYING.getVal()),
                ErrorCode.PARAMS_ERROR, "该订单已支付");
        if (order.getOrderStatus().equals(OrderStatus.WAITING_PAY.getVal())) {
            ThrowUtils.throwIf(!updateOrderStatusFromWaitingPayToPaying(dto.getOrderId()),
                    ErrorCode.UPDATE_ERROR, "更新订单状态异常");
        } else {
            ThrowUtils.throwIf(!updatePayTime(dto.getOrderId()), ErrorCode.UPDATE_ERROR, "更新付款时间异常");
        }
        logService.log(order.getOrderId(), ReqThreadLocal.getInfo().getUserId(), "用户付款");
        // 成功
        if (order.getRealOrderPrice() == 0) {
            ThrowUtils.throwIf(!updateOrderStatusFromPayingToPayed(order.getOrderId()), ErrorCode.UPDATE_ERROR);
            return null;
        }
        //
        OrderPayVo vo = null;
        if (dto.getPayMethod().equals(PaymentTypeEnum.STRIPE.getVal())) {
            vo = doStripePay(order, dto.getSuccessUrl(), dto.getCancelUrl());
        } else {
            vo = doWxPay(order, dto.getPayMethod());
        }
        // 写不进去测试
        redisTemplateMap.get(RedisDb.PAYMENT_EXPIRE_DB.getVal()).opsForHash()
                .put(RedisKeyName.PAY_EXPIRE.getVal(), String.valueOf(order.getOrderId()),
                        String.valueOf(System.currentTimeMillis()));
        return vo;
    }

    private OrderPayVo doStripePay(Order order, String successUrl, String cancelUrl) {
        ThrowUtils.throwIf(!stripeService.initSuccess, ErrorCode.SYSTEM_ERROR, "Stripe支付未正常启动");
        return stripeService.doStripePay(order, successUrl, cancelUrl);
    }

    private OrderPayVo doWxPay(Order order, Integer paymentType) {
        WxPayService wxPayService = WxPayFactory.get(paymentType);
        ThrowUtils.throwIf(!wxPayService.isInitSuccess(), ErrorCode.SYSTEM_ERROR, "wx支付未正常启动");
        String openId = null;
        if (paymentType.equals(PaymentTypeEnum.JS_API.getVal())) {
            Userthirdauthorization userthirdauthorization = openIdService.lambdaQuery()
                    .eq(Userthirdauthorization::getPersonnelId, ReqThreadLocal.getInfo().getUserId())
                    .eq(Userthirdauthorization::getAppPlatform, ReqThreadLocal.getInfo().getPlatform())
                    .one();
            ThrowUtils.throwIf(ObjectUtils.isEmpty(userthirdauthorization), ErrorCode.CANNOT_FIND_BY_ID, "查询用户小程序关联OPENID异常");
            openId = userthirdauthorization.getThirdSecondTag();
        }
        return wxPayService.prepay(order.getOrderId(), order.getRealOrderPrice(), PayCallBackUrl.ORDER_CALLBACK_URL_WX, openId);
    }

    @Override
    public Boolean updateOrderStatusFromWaitingPayToPaying(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .eq(Order::getOrderStatus, OrderStatus.WAITING_PAY.getVal())
                .set(Order::getOrderStatus, OrderStatus.PAYING.getVal())
                .set(Order::getOrderPayTime, new Date())
                .update();
    }

    @Override
    public Boolean updatePayTime(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .set(Order::getOrderPayTime, new Date())
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
        ThrowUtils.throwIf(!lambdaUpdate().eq(Order::getOrderId, orderId)
                .set(Order::getOrderStatus, OrderStatus.WAITING_SEND.getVal())
                .update(), ErrorCode.UPDATE_ERROR);
        noticeOrderPaySuccess(orderId);
        return true;
    }

    @Override
    public Boolean updateOrderStatusFromPayedToWaitingSend(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .set(Order::getOrderStatus, OrderStatus.WAITING_SEND.getVal())
                .update();
    }

    @Override
    public Boolean updateOrderStatusFromWaitingSendToWaitingReceive(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .eq(Order::getOrderStatus, OrderStatus.WAITING_SEND.getVal())
                .set(Order::getOrderStatus, OrderStatus.WAITING_RECEIVE.getVal())
                .update();
    }

    @Override
    public Boolean updateOrderStatusFromWaitingReceiveToWaitingComment(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .eq(Order::getOrderStatus, OrderStatus.WAITING_RECEIVE.getVal())
                .set(Order::getOrderStatus, OrderStatus.WAITING_COMMENT.getVal())
                .update();
    }

    @Override
    public Boolean updateOrderStatusFromWaitingCommentToFinish(Long orderId) {
        ThrowUtils.throwIf(!lambdaUpdate().eq(Order::getOrderId, orderId)
                .eq(Order::getOrderStatus, OrderStatus.WAITING_COMMENT.getVal())
                .set(Order::getOrderStatus, OrderStatus.FINISH.getVal())
                .update(), ErrorCode.UPDATE_ERROR);
        noticeOrderFinishSuccess(orderId);
        return true;
    }

    @Override
    public Boolean updateOrderStatusFromPayedToFinish(Long orderId) {
        return lambdaUpdate().eq(Order::getOrderId, orderId)
                .eq(Order::getOrderStatus, OrderStatus.WAITING_SEND.getVal())
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
        log.info("[SCAN order:{}]", JSON.toJSONString(order));
        // 支付成功
        if (transaction.getTradeState().equals(Transaction.TradeStateEnum.SUCCESS)) {
            ThrowUtils.throwIf(!updateOrderStatusFromPayingToPayed(order.getOrderId()), ErrorCode.UPDATE_ERROR);
            logService.log(order.getOrderId(), null, "微信提醒支付成功");
        }
        if (transaction.getTradeState().equals(Transaction.TradeStateEnum.NOTPAY)
                || transaction.getTradeState().equals(Transaction.TradeStateEnum.USERPAYING)
                || transaction.getTradeState().equals(Transaction.TradeStateEnum.ACCEPT)
                || transaction.getTradeState().equals(Transaction.TradeStateEnum.REVOKED)) {
            return;
        }
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
        // 支付失败
        else if (transaction.getTradeState().equals(Transaction.TradeStateEnum.PAYERROR)
                || transaction.getTradeState().equals(Transaction.TradeStateEnum.CLOSED)) {
            ThrowUtils.throwIf(!updateOrderStatusFromPayingToClose(order.getOrderId()), ErrorCode.UPDATE_ERROR);
        }
        // 处理微信回调字段
        ThrowUtils.throwIf(!lambdaUpdate()
                .eq(Order::getOrderId, order.getOrderId())
                .set(Order::getOrderIsDeal, YesNoEnum.YES.getVal())
                .set(Order::getThirdPaymentId, transaction.getTransactionId())
                .update(), ErrorCode.UPDATE_ERROR);
    }

    @Override
    public void doRefundCallback(Refund refund) {
        logService.log(Long.valueOf(refund.getOutTradeNo()), null, "收到微信退款回调信息");

        RefundStatus refundStatus = null;
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getOrderId, Long.valueOf(refund.getOutTradeNo()));

        switch (refund.getStatus()) {
            case ABNORMAL -> {
                refundStatus = RefundStatus.FAIL;
                wrapper.set(Order::getRefundIsDeal, YesNoEnum.YES.getVal());
            }
            case SUCCESS -> {
                refundStatus = RefundStatus.SUCCESS;
                wrapper.set(Order::getRefundIsDeal, YesNoEnum.YES.getVal());
            }
            case PROCESSING -> refundStatus = RefundStatus.PROCESSING;
            case CLOSED -> {
                refundStatus = RefundStatus.CLOSED;
                wrapper.set(Order::getRefundIsDeal, YesNoEnum.YES.getVal());
            }
        }
        wrapper.set(Order::getRefundStatus, refundStatus.getVal());
        ThrowUtils.throwIf(!update(wrapper), ErrorCode.UPDATE_ERROR);
    }

    @Override
    public void doRefundCallback(Long orderId, Exception e) {
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getOrderId, orderId);
        wrapper.set(Order::getRefundStatus, RefundStatus.FAIL.getVal());
        wrapper.set(Order::getRemark2, e.getMessage());
        ThrowUtils.throwIf(!update(wrapper), ErrorCode.UPDATE_ERROR);
    }

    @Override
    @Transactional
    public void doRefundCallback(RefundNotification refundNotification) {
        logService.log(Long.valueOf(refundNotification.getOutTradeNo()), null, "收到微信退款回调信息");
        RefundStatus refundStatus = null;
        LambdaUpdateWrapper<Order> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Order::getOrderId, Long.valueOf(refundNotification.getOutTradeNo()));

        switch (refundNotification.getRefundStatus()) {
            case ABNORMAL -> {
                refundStatus = RefundStatus.FAIL;
                wrapper.set(Order::getRefundIsDeal, YesNoEnum.YES.getVal());
            }
            case SUCCESS -> {
                refundStatus = RefundStatus.SUCCESS;
                wrapper.set(Order::getRefundIsDeal, YesNoEnum.YES.getVal());
                noticeOrderRefundSuccess(Long.valueOf(refundNotification.getOutTradeNo()));
            }
            case PROCESSING -> refundStatus = RefundStatus.PROCESSING;
            case CLOSED -> {
                refundStatus = RefundStatus.CLOSED;
                wrapper.set(Order::getRefundIsDeal, YesNoEnum.YES.getVal());
            }
        }
        wrapper.set(Order::getRefundStatus, refundStatus.getVal());
        ThrowUtils.throwIf(!update(wrapper), ErrorCode.UPDATE_ERROR);
    }

    @Override
    @Transactional
    public void doOrderCallback(PaymentIntent paymentIntent) {
        logService.log(Long.valueOf(paymentIntent.getId()), null, "收到Stripe退款回调信息");
        Order order = getById(Long.valueOf(paymentIntent.getId()));
        doOrderCallbackStripe(order, paymentIntent);
    }

    @Override
    @Transactional
    public void doOrderCallback(Session stripeSession) {
        logService.log(Long.valueOf(stripeSession.getClientReferenceId()), null, "收到Stripe退款回调信息");
        Order order = getById(Long.parseLong(stripeSession.getClientReferenceId()));
        doOrderCallbackStripe(order, stripeSession);

    }

    @Transactional
    public void doOrderCallbackStripe(Order order, Object stripeEvent) {
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

    @Override
    public void noticeOrderPaySuccess(Long orderId) {
        redisTemplateMap.get(RedisDb.BIZ_DB.getVal()).opsForList().rightPush(
                RedisKeyName.BIZ_NOTICE_SUCCESS_PAID_ORDER.getVal(), String.valueOf(orderId));
    }

    @Override
    public void noticeOrderRefundSuccess(Long orderId) {
        redisTemplateMap.get(RedisDb.BIZ_DB.getVal()).opsForList().rightPush(
                RedisKeyName.BIZ_NOTICE_SUCCESS_REFUND_ORDER.getVal(), String.valueOf(orderId));
    }

    @Override
    public void noticeOrderFinishSuccess(Long orderId) {
        redisTemplateMap.get(RedisDb.BIZ_DB.getVal()).opsForList().rightPush(
                RedisKeyName.BIZ_NOTICE_SUCCESS_FINISH_ORDER.getVal(), String.valueOf(orderId));
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
            if (timestamp + params.getPaymentTimeout() < System.currentTimeMillis()) {
                Long orderId = Long.valueOf(k.toString());
                Order order = getById(orderId);
                // 如果没被处理 就更新为代付款
                // 如果只是第三方付款有时间差  回调函数会强制改为付款成功
                if (order.getOrderIsDeal() == YesNoEnum.NO.getVal()) {
                    updateOrderStatusFromPayingToWaitingPay(orderId);
                }
                // 删除该key
                redisTemplateMap.get(PAYMENT_EXPIRE_DB.getVal()).opsForHash().delete(RedisKeyName.PAY_EXPIRE.getVal(), k.toString());
            }
        });
        List<Order> payingOrders = lambdaQuery().eq(Order::getOrderStatus, OrderStatus.PAYING.getVal()).list();
        for (Order payingOrder : payingOrders) {
            // 超时付款
            if (payingOrder.getOrderStartPayTime() + params.getPaymentTimeout() < System.currentTimeMillis()) {
                // 如果没被处理 就更新为代付款
                // 如果只是第三方付款有时间差  回调函数会强制改为付款成功
                if (payingOrder.getOrderIsDeal() == YesNoEnum.NO.getVal()) {
                    updateOrderStatusFromPayingToWaitingPay(payingOrder.getOrderId());
                }
            }
        }
    }

    /**
     * 支付订单未支付状态超时扫描任务
     */
    @Scheduled(cron = "${payment.wx.schedule-scan: 30 * * * * ?}")
    @Transactional
    public void scheduleClearUnPayedTimeoutPayment() {
        int expireNum = updateTimeoutWaitingPayOrder(params.getOrderTimeout());
        if (expireNum != 0) {
            log.warn("[EXPIRE_ORDER] FIND ORDER EXPIRE");
        }
    }

    /**
     * 七天后将收货的订单更新为订单结束
     */
    @Scheduled(cron = "${payment.wx.schedule-scan: 30 * * * * ?}")
    @Transactional
    public void scheduleUpdateWaitingComment2Finish() {
        List<Long> orderIds = baseMapper.getWaitingComment2FinishOrderIds();
        if (!CollectionUtils.isEmpty(orderIds)) {
            for (Long orderId : orderIds) {
                updateOrderStatusFromWaitingCommentToFinish(orderId);
                log.info("[7 DAYS ORDER] {} -> FINISH STATE", orderId);
            }
        }
    }

    private void orderOwnerCheck(Order order, Long userId) {
        if (ReqThreadLocal.getInfo().getAuthLevel() < AuthLevel.ADMIN.getVal()) {
            ThrowUtils.throwIf(!order.getUserId().equals(userId), ErrorCode.AUTH_FAIL, "您无权操作该订单");
        }
    }

    private void orderOwnerCheck(Long orderId, Long userId) {
        if (ReqThreadLocal.getInfo().getAuthLevel() < AuthLevel.ADMIN.getVal()) {
            Order order = getById(orderId);
            ThrowUtils.throwIf(!order.getUserId().equals(userId), ErrorCode.AUTH_FAIL, "您无权操作该订单");
        }
    }
}




