package cn.lime.mall.service.db;

import cn.lime.core.common.PageResult;
import cn.lime.mall.model.dto.order.OrderItemDto;
import cn.lime.mall.model.dto.order.OrderPayDto;
import cn.lime.mall.model.entity.Order;
import cn.lime.mall.model.vo.OrderDetailVo;
import cn.lime.mall.model.vo.OrderPageVo;
import cn.lime.mall.model.vo.OrderPayVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.Refund;
import com.wechat.pay.java.service.refund.model.RefundNotification;

import java.util.List;

/**
* @author riang
* @description 针对表【Order(订单表)】的数据库操作Service
* @createDate 2024-03-15 14:29:53
*/
public interface OrderService extends IService<Order> {
    Order createOrder(Long userId, Integer addressId, List<OrderItemDto> orderItems, String remark);
    Boolean cancelOrder(Long orderId);
    OrderPayVo payOrder(OrderPayDto dto);
    OrderDetailVo getOrderDetail(Long orderId);
    PageResult<OrderPageVo> getOrderPage(String orderCode, String userName, String productName,String receiverName,
                                         Integer orderState,Long orderUserId, Long orderStartTime,Long orderEndTime,
                                         Integer current, Integer pageSize, String sortField, String sortOrder);
    Boolean applyRefund(Long orderId);
    Boolean refund(Long orderId);
    void receive(Long orderId);
    void comment(Long orderId,String comment);
    void orderUpdateByAdmin(Long orderId, String merchantRemark, Integer changedPrice);
    void orderSend(Long orderId,String deliverCompany,String deliverId);


    /* 订单状态转换 */

    // 待支付 -> 支付中
    Boolean updateOrderStatusFromWaitingPayToPaying(Long orderId);
    // 待支付 -> 关闭
    Boolean updateOrderStatusFromWaitingPayToClose(Long orderId);
    Boolean updateOrderStatusFromPayingToPayed(Long orderId);
    Boolean updateOrderStatusFromPayedToWaitingSend(Long orderId);
    Boolean updateOrderStatusFromWaitingSendToWaitingReceive(Long orderId);
    Boolean updateOrderStatusFromWaitingReceiveToWaitingComment(Long orderId);
    Boolean updateOrderStatusFromWaitingCommentToFinish(Long orderId);
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
