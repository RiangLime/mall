package cn.lime.mall.service.db;

import cn.lime.core.module.dto.OrderPayDto;
import cn.lime.core.module.entity.Order;
import cn.lime.core.module.vo.OrderPayVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.RefundNotification;

/**
* @author riang
* @description 针对表【Order(订单表)】的数据库操作Service
* @createDate 2024-03-15 14:29:53
*/
public interface OrderService extends IService<Order> {
    Order getOrder(Long orderId);
    Order createOrder(Long orderId,Long userId,Long productId,Integer orderPrice,String remark);
    Boolean cancelOrder(Long orderId);
    OrderPayVo payOrder(OrderPayDto dto);
    // 待支付 -> 支付中
    Boolean updateOrderStatusFromWaitingPayToPaying(Long orderId);
    // 待支付 -> 关闭
    Boolean updateOrderStatusFromWaitingPayToClose(Long orderId);
    Boolean updateOrderStatusFromPayingToPayed(Long orderId);

    Boolean updateOrderStatusFromPayedToFinish(Long orderId);
    // 支付中 -> 待支付
    Boolean updateOrderStatusFromPayingToWaitingPay(Long orderId);
    // 支付中 -> 关闭
    Boolean updateOrderStatusFromPayingToClose(Long orderId);
    // 支付中 -> 订单成功
    Boolean updateOrderStatusFromPayingToFinish(Long orderId);

    Integer updateTimeoutWaitingPayOrder(Integer timeout);
    void doOrderCallback(Transaction transaction);
    void doRefundCallback(Refund refund);
    void doRefundCallback(RefundNotification refundNotification);
    void doOrderCallback(PaymentIntent paymentIntent);
    void doOrderCallback(Session stripeSession);
}
