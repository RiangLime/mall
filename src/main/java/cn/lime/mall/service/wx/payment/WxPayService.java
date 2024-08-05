package cn.lime.mall.service.wx.payment;

import cn.lime.mall.model.vo.OrderPayVo;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.Refund;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: WxPayService
 * @Description: 微信支付接口
 * @Author: Lime
 * @Date: 2023/9/28 11:28
 */
public interface WxPayService {

    boolean isInitSuccess();
    /**
     * 预支付
     * @param orderId 订单ID
     * @param price 订单价格
     * @param notifyUrl 回调URL
     * @param openId 用户OPENID
     * @return 预支付vo
     */
    OrderPayVo prepay(Long orderId, Integer price, String notifyUrl, String openId);

    <T> void dealNotice(HttpServletRequest request, Class<T> clazz);
    /**
     * 查询订单详情
     * @param orderId 外部第三方（本系统 订单ID
     * @return 微信交易信息
     */
    Transaction queryOrderById(Long orderId);

    /**
     *
     * @param orderId
     * @param refundPrice
     * @param notifyUrl
     */
    void refund(Long orderId, Integer refundPrice,String notifyUrl);

    /**
     *
     * @param orderId
     * @return
     */
    Refund queryRefundById(Long orderId);

}
