package cn.lime.mall.service.stripe;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.RedisDb;
import cn.lime.core.constant.RedisKeyName;
import cn.lime.mall.config.MallParams;
import cn.lime.mall.model.entity.Order;
import cn.lime.mall.model.vo.OrderPayVo;
import cn.lime.mall.service.db.OrderService;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionRetrieveParams;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: StripePayService
 * @Description: stripe支付服务
 * @Author: Lime
 * @Date: 2023/12/7 15:01
 */
@Service
@Slf4j
public class StripePayService implements InitializingBean {

    @Resource
    private MallParams params;
    public boolean initSuccess;
    @Resource
    private OrderService orderService;

    @Resource
    protected Map<Integer, StringRedisTemplate> redisTemplateMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        initSuccess = initSuccess();
        if (!initSuccess){
            log.warn("[Init Stripe Pay] 未检测到正确的Stripe支付相关配置");
        }
    }

    public boolean initSuccess(){
        return StringUtils.isNotEmpty(params.getStripeSecret())
                && StringUtils.isNotEmpty(params.getStripeApiKey())
                && StringUtils.isNotEmpty(params.getSuccessEndpointSecret());
    }

    public OrderPayVo doStripePay(Order payorder, String successUrl, String cancelUrl) {
//        try {
//            Stripe.apiKey = stripeSecret;
//            Product product = productService.getById(payorder.getProductId());
//            SessionCreateParams params = null;
//            if (ObjectUtils.isNotEmpty(payorder.getDiscountId())){
//                String discountStripeCode = discountService.getById(payorder.getDiscountId()).getStripeId();
//                params = SessionCreateParams.builder()
//                        .setMode(SessionCreateParams.Mode.PAYMENT)
//                        .setClientReferenceId(payorder.getOrderId().toString())
//                        .setSuccessUrl(successUrl)
//                        .addLineItem(SessionCreateParams.LineItem.builder()
//                                .setQuantity(1L)
//                                .setPrice(product.getStripePriceId())
//                                .build())
//                        .addDiscount(SessionCreateParams.Discount.builder().setCoupon(discountStripeCode).build())
//                        .build();
//            }else {
//                params = SessionCreateParams.builder()
//                        .setMode(SessionCreateParams.Mode.PAYMENT)
//                        .setClientReferenceId(payorder.getOrderId().toString())
//                        .setSuccessUrl(successUrl)
//                        .addLineItem(SessionCreateParams.LineItem.builder()
//                                .setQuantity(1L)
//                                .setPrice(product.getStripePriceId())
//                                .build())
//                        .build();
//            }
//
//            Session session = Session.create(params);
//            // 入缓存
//            cacheOrder(payorder.getOrderId());
//            OrderPayVo vo = new OrderPayVo();
//            vo.setPayUrl(session.getUrl());
//            return vo;
//        } catch (StripeException e) {
//            throw new BusinessException(ErrorCode.STRIPE_INTERFACE_ERROR,e.getMessage());
//        }
        return null;
    }

    @Transactional
    public void dealNotice(HttpServletRequest request) throws StripeException {
        String body = "";
        Stripe.apiKey = params.getStripeSecret();
        try (InputStream is = request.getInputStream()) {
            body = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            log.error("read http request failed.", ex);
        }
        log.info("GET:{}", body);

        String sigHeader = request.getHeader("Stripe-Signature");
        Event event = null;

        try {
            event = Webhook.constructEvent(body, sigHeader, params.getSuccessEndpointSecret());
            dealEvent(event);
        } catch (JsonSyntaxException | SignatureVerificationException e) {
            throw new BusinessException(ErrorCode.STRIPE_INTERFACE_ERROR,e.getMessage());
        }
    }

    @Transactional
    public void dealEvent(Event event) throws StripeException {
        switch (event.getType()) {
            // Handle the checkout.session.completed event
            case "checkout.session.completed" -> {
                {
                    Session sessionEvent = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();
                    SessionRetrieveParams params =
                            SessionRetrieveParams.builder()
                                    .addExpand("line_items")
                                    .build();

                    Session session = Session.retrieve(sessionEvent.getId(), params, null);
                    String paymentId = session.getPaymentIntent();
                    Long orderId = Long.valueOf(session.getClientReferenceId());
                    orderService
                            .lambdaUpdate().eq(Order::getOrderId, orderId)
                            .set(Order::getThirdPaymentId, paymentId)
                            .update();
                    if (Objects.equals(session.getPaymentStatus(), "paid")) {
                        // Fulfill the purchase...
                        doBizNotice(session);
                    } else {
                        log.info("[STRIPE]Session User Unpaid yet, waiting next notification...");
                    }
                }
                ;
            }
            case "checkout.session.async_payment_succeeded" -> {
                {
                    Session sessionEvent = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();
                    SessionRetrieveParams params =
                            SessionRetrieveParams.builder()
                                    .addExpand("line_items")
                                    .build();

                    Session session = Session.retrieve(sessionEvent.getId(), params, null);
                    doBizNotice(session);
                }
                ;
            }
            case "checkout.session.async_payment_failed" -> {
                {
                    Session sessionEvent = (Session) event.getDataObjectDeserializer().getObject().orElseThrow();
                    SessionRetrieveParams params =
                            SessionRetrieveParams.builder()
                                    .addExpand("line_items")
                                    .build();

                    Session session = Session.retrieve(sessionEvent.getId(), params, null);
                    Long orderId = Long.valueOf(session.getClientReferenceId());
                    ThrowUtils.throwIf(!orderService.updateOrderStatusFromPayingToWaitingPay(orderId),
                            ErrorCode.UPDATE_ERROR, "更新订单状态失败 支付中->待支付");
                }
                ;
            }
            case "payment_intent.succeeded" -> {
                {
                    PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow();
                    doBizNotice(paymentIntent);
                }
                ;
            }
        }
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

    @Transactional
    public void doBizNotice(PaymentIntent paymentIntent) {
        Order order = orderService.lambdaQuery().eq(Order::getThirdPaymentId,paymentIntent.getId()).one();
        ThrowUtils.throwIf(ObjectUtils.isEmpty(order),ErrorCode.NOT_FOUND_ERROR);
        // 根据支付通知解析回调结果，并更新订单状态
        orderService.doOrderCallback(paymentIntent);
        // 更新订单状态
        removeOrder(order.getOrderId());
    }

    @Transactional
    public void doBizNotice(Session session) {
        // 根据支付通知解析回调结果，并更新订单状态
        orderService.doOrderCallback(session);
        // 更新订单状态
        removeOrder(Long.parseLong(session.getClientReferenceId()));
    }

}
