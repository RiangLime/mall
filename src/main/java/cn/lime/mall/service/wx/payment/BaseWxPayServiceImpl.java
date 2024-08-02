package cn.lime.mall.service.wx.payment;


import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.RedisDb;
import cn.lime.core.constant.RedisKeyName;
import cn.lime.mall.model.entity.Order;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.core.utils.HttpUtils;
import cn.lime.mall.config.MallParams;
import cn.lime.mall.constant.OrderStatus;
import cn.lime.mall.model.vo.OrderPayVo;
import cn.lime.mall.service.db.OrderService;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    protected MallParams coreParams;
    @Resource
    protected Map<Integer, StringRedisTemplate> redisTemplateMap;
    @Resource
    protected OrderService orderService;
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
        if (!initSuccess){
            log.warn("[Init Wx Pay] 未检测到正确的微信支付相关配置");
            return;
        }

        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        RSAAutoCertificateConfig rsaAutoCertificateConfig =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(coreParams.getWxPayMerchantId())
                        .privateKeyFromPath(coreParams.getWxPayPrivateKeyPath())
                        .merchantSerialNumber(coreParams.getWxPayMerchantSerialNumber())
                        .apiV3Key(coreParams.getWxPayApiV3Key())
                        .build();
        nativeService = new NativePayService.Builder().config(rsaAutoCertificateConfig).build();
        jsapiService = new JsapiServiceExtension.Builder().config(rsaAutoCertificateConfig).build();
        h5Service = new H5Service.Builder().config(rsaAutoCertificateConfig).build();
        refundService = new RefundService.Builder().config(rsaAutoCertificateConfig).build();
        notificationConfig = rsaAutoCertificateConfig;
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
            // 根据支付通知解析回调结果，并更新订单状态
            orderService.doOrderCallback(transaction);
            // 更新订单状态
            removeOrder(Long.parseLong(transaction.getOutTradeNo()));
        } else if (clazz == RefundNotification.class) {
            RefundNotification notification = parser.parse(requestParam, RefundNotification.class);
            // 根据退款通知解析回调结果，并更新订单状态
            orderService.doRefundCallback(notification);
        } else {
            throw new BusinessException(ErrorCode.UNSUPPORTED_METHOD, "无效的回调实体类");
        }
    }

    @Override
    @Transactional
    public void refund(Long orderId, Integer refundPrice, String notifyUrl) {
        Order order = orderService.getOrder(orderId);
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
        FundsFromItem fundsFromItem = new FundsFromItem();
        fundsFromItem.setAmount(Long.valueOf(refundPrice));
        fundsFromItem.setAccount(Account.AVAILABLE);
        amountReq.setFrom(List.of(fundsFromItem));
        request.setAmount(amountReq);
        // 拼接完成
        Refund refund = refundService.create(request);
        orderService.doRefundCallback(refund);
    }

    @Override
    public Refund queryRefundById(Long orderId) {
        Order order = orderService.getOrder(orderId);
        QueryByOutRefundNoRequest request = new QueryByOutRefundNoRequest();
        request.setOutRefundNo(String.valueOf(order.getRefundId()));
        return refundService.queryByOutRefundNo(request);
    }

    protected void cacheOrder(Long orderId) {
        redisTemplateMap.get(RedisDb.PAYMENT_EXPIRE_DB.getVal()).opsForHash()
                .put(RedisKeyName.PAY_EXPIRE.getVal(), String.valueOf(orderId),
                        String.valueOf(System.currentTimeMillis()));

    }

    protected void removeOrder(Long orderId) {
        redisTemplateMap.get(RedisDb.PAYMENT_EXPIRE_DB.getVal()).opsForHash()
                .delete(RedisKeyName.PAY_EXPIRE.getVal(), String.valueOf(orderId));

    }

    public boolean initSuccess(){
        return StringUtils.isNotBlank(coreParams.getWxPayApId())
                && StringUtils.isNotEmpty(coreParams.getWxPayMerchantId())
                && StringUtils.isNotEmpty(coreParams.getWxPayPrivateKeyPath())
                && StringUtils.isNotEmpty(coreParams.getWxPayCertificatePath())
                && StringUtils.isNotEmpty(coreParams.getWxPayApiV3Key())
                && StringUtils.isNotEmpty(coreParams.getWxPayMerchantSerialNumber())
                && StringUtils.isNotEmpty(coreParams.getWxPayNotifyUrlPrefix());
    }

}
