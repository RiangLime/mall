package cn.lime.mall.constant;

/**
 * @ClassName: PayCallBackUrl
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/3/15 14:48
 */
public interface PayCallBackUrl {
    String ORDER_CALLBACK_URL_WX = "/notice/order/wx";
    String ORDER_CALLBACK_URL_STRIPE = "/notice/order/stripe";
    String ORDER_REFUND_CALLBACK_URL = "/notice/order/wx/refund";
}
