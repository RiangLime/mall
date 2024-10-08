package cn.lime.mall.service.wx.payment;


import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.RedisDb;
import cn.lime.core.constant.RedisKeyName;
import cn.lime.core.constant.Symbol;
import cn.lime.mall.model.entity.Order;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.core.utils.HttpUtils;
import cn.lime.mall.config.MallParams;
import cn.lime.mall.constant.OrderStatus;
import cn.lime.mall.model.entity.OrderItem;
import cn.lime.mall.model.entity.Product;
import cn.lime.mall.model.vo.OrderPayVo;
import cn.lime.mall.service.db.OrderItemService;
import cn.lime.mall.service.db.OrderService;
import cn.lime.mall.service.db.ProductService;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.h5.H5Service;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BaseWxPayServiceImpl
 * @Description: 微信支付实现类
 * @Author: Lime
 * @Date: 2023/9/28 11:33
 */
@Service(value = "baseWxPayServiceImpl")
@Slf4j
public class BaseWxPayServiceImpl implements WxPayService, InitializingBean {

    private boolean initSuccess;
    @Resource
    protected MallParams mallParams;
    @Resource
    protected Map<Integer, StringRedisTemplate> redisTemplateMap;
    @Resource
    protected OrderService orderService;
    @Resource
    protected OrderItemService orderItemService;
    @Resource
    protected ProductService productService;
    protected NativePayService nativeService;
    protected JsapiServiceExtension jsapiService;
    protected H5Service h5Service;
    protected RefundService refundService;
    protected NotificationConfig notificationConfig;
    @Resource
    protected SnowFlakeGenerator ids;

    @Override
    public void afterPropertiesSet() throws Exception {
        initSuccess = initSuccess();
        if (!initSuccess) {
            log.warn("[Init Wx Pay] 未检测到正确的微信支付相关配置");
            return;
        }

        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        RSAAutoCertificateConfig rsaAutoCertificateConfig =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(mallParams.getWxPayMerchantId())
                        .privateKeyFromPath(mallParams.getWxPayPrivateKeyPath())
                        .merchantSerialNumber(mallParams.getWxPayMerchantSerialNumber())
                        .apiV3Key(mallParams.getWxPayApiV3Key())
                        .build();
        nativeService = new NativePayService.Builder().config(rsaAutoCertificateConfig).build();
        jsapiService = new JsapiServiceExtension.Builder().config(rsaAutoCertificateConfig).build();
        h5Service = new H5Service.Builder().config(rsaAutoCertificateConfig).build();
        refundService = new RefundService.Builder().config(rsaAutoCertificateConfig).build();
        notificationConfig = rsaAutoCertificateConfig;
    }

    @Override
    public boolean isInitSuccess() {
        return initSuccess;
    }

    /**
     * 有具体实现类实现
     */
    @Override
    public OrderPayVo prepay(Long orderId, Integer price, String notifyUrl, String openId) {
        return null;
    }

    /**
     * 由具体实现类实现
     */
    @Override
    public Transaction queryOrderById(Long orderId) {
        return null;
    }

    @Transactional
    @Override
    public <T> void dealNotice(HttpServletRequest request, Class<T> clazz) {
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = (request.getHeader("Wechatpay-Nonce"));
        String wechatPayCertificateSerialNumber = (request.getHeader("Wechatpay-Serial"));
        String signature = (request.getHeader("Wechatpay-Signature"));
        RequestParam requestParam = null;
        try {
            // 构造 RequestParam
            requestParam = new RequestParam.Builder()
                    .serialNumber(wechatPayCertificateSerialNumber)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timestamp)
                    .body(HttpUtils.getBody(request))
                    .build();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.IO_ERROR, "解析支付通知报文失败");
        }
        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser(notificationConfig);

        if (clazz == Transaction.class) {
            // 以支付通知回调为例，验签、解密并转换成 Transaction
            Transaction transaction = parser.parse(requestParam, Transaction.class);
            dealTransaction(transaction);
        } else if (clazz == RefundNotification.class) {
            RefundNotification notification = parser.parse(requestParam, RefundNotification.class);
            dealRefundNotification(notification);
        } else {
            throw new BusinessException(ErrorCode.UNSUPPORTED_METHOD, "无效的回调实体类");
        }
    }

    public void dealRefundNotification(RefundNotification refundNotification) {
        // 根据退款通知解析回调结果，并更新订单状态
        orderService.doRefundCallback(refundNotification);
    }

    public void dealRefund(Refund refund) {
        orderService.doRefundCallback(refund);
    }

    public void dealRefund(Long orderId,Exception e){
        orderService.doRefundCallback(orderId,e);
    }

    public void dealTransaction(Transaction transaction) {
        // 根据支付通知解析回调结果，并更新订单状态
        orderService.doOrderCallback(transaction);
        // 更新订单状态
        removeOrder(Long.parseLong(transaction.getOutTradeNo()));
    }

    @Override
    @Transactional
    public void refund(Long orderId, Integer refundPrice, String notifyUrl) {
        Order order = orderService.getById(orderId);
        order.setRefundId(ids.nextId());
        order.setRefundStatus(1);
        order.setRefundPrice(refundPrice);
        order.setOrderStatus(OrderStatus.REFUNDING.getVal());
        ThrowUtils.throwIf(!orderService.updateById(order), ErrorCode.UPDATE_ERROR);
        // 退款参数拼接
        CreateRequest request = new CreateRequest();
        request.setTransactionId(order.getThirdPaymentId());
        request.setOutTradeNo(String.valueOf(order.getOrderId()));
        request.setOutRefundNo(String.valueOf(order.getRefundId()));
        request.setReason("无offer无理由退款");
        request.setNotifyUrl(notifyUrl);
        AmountReq amountReq = new AmountReq();
        amountReq.setRefund(Long.valueOf(refundPrice));
        amountReq.setTotal(Long.valueOf(order.getRealOrderPrice()));
        amountReq.setCurrency("CNY");
//        FundsFromItem fundsFromItem = new FundsFromItem();
//        fundsFromItem.setAmount(Long.valueOf(refundPrice));
//        fundsFromItem.setAccount(Account.AVAILABLE);
//        amountReq.setFrom(List.of(fundsFromItem));
        request.setAmount(amountReq);
        // 拼接完成
        Refund refund = refundService.create(request);
        orderService.doRefundCallback(refund);
        try {
            Thread.sleep(500);
        }catch (Exception ignored){

        }
    }

    @Override
    public Refund queryRefundById(Long orderId) {
        Order order = orderService.getById(orderId);
        QueryByOutRefundNoRequest request = new QueryByOutRefundNoRequest();
        request.setOutRefundNo(String.valueOf(order.getRefundId()));
        return refundService.queryByOutRefundNo(request);
    }

    @Override
    public Refund queryRefundByRefundId(Long refundId) {
        QueryByOutRefundNoRequest request = new QueryByOutRefundNoRequest();
        request.setOutRefundNo(String.valueOf(refundId));
        return refundService.queryByOutRefundNo(request);
    }

    protected void cacheOrder(Long orderId) {
        redisTemplateMap.get(RedisDb.PAYMENT_EXPIRE_DB.getVal()).opsForHash()
                .put(RedisKeyName.PAY_EXPIRE.getVal(), String.valueOf(orderId),
                        String.valueOf(System.currentTimeMillis()));
        ThrowUtils.throwIf(!orderService.lambdaUpdate().eq(Order::getOrderId,orderId)
                .set(Order::getOrderStartPayTime,System.currentTimeMillis()).update(),ErrorCode.UPDATE_ERROR);

    }

    protected void removeOrder(Long orderId) {
        redisTemplateMap.get(RedisDb.PAYMENT_EXPIRE_DB.getVal()).opsForHash()
                .delete(RedisKeyName.PAY_EXPIRE.getVal(), String.valueOf(orderId));

    }

    protected String getOrderDescription(Long orderId){
        List<OrderItem> orderItems = orderItemService.lambdaQuery().eq(OrderItem::getOrderId,orderId).list();
        if (!CollectionUtils.isEmpty(orderItems)){
            List<Long> productIds = orderItems.stream().map(OrderItem::getProductId).toList();
            List<String> productNames = productService.lambdaQuery().in(Product::getProductId,productIds)
                    .list()
                    .stream()
                    .map(Product::getProductName)
                    .toList();
            return String.join(String.valueOf(Symbol.COMMA),productNames);
        }
        return "订单号" + orderId;
    }

    public boolean initSuccess() {
        log.info("""
                                        
                        WxPayAppId={},
                        WxPayMerchantId={},
                        WxPayPrivateKeyPath={},
                        WxPayCertificatePath={},
                        WxPayApiV3Key={},
                        WxPayMerchantSerialNumber={},
                        WxPayNotifyUrlPrefix={}
                        """, mallParams.getWxPayAppId(), mallParams.getWxPayMerchantId(),
                mallParams.getWxPayPrivateKeyPath(), mallParams.getWxPayCertificatePath(),
                mallParams.getWxPayApiV3Key(), mallParams.getWxPayMerchantSerialNumber(),
                mallParams.getWxPayNotifyUrlPrefix());
        return StringUtils.isNotBlank(mallParams.getWxPayAppId())
                && StringUtils.isNotEmpty(mallParams.getWxPayMerchantId())
                && StringUtils.isNotEmpty(mallParams.getWxPayPrivateKeyPath())
                && StringUtils.isNotEmpty(mallParams.getWxPayCertificatePath())
                && StringUtils.isNotEmpty(mallParams.getWxPayApiV3Key())
                && StringUtils.isNotEmpty(mallParams.getWxPayMerchantSerialNumber())
                && StringUtils.isNotEmpty(mallParams.getWxPayNotifyUrlPrefix());
    }

}
