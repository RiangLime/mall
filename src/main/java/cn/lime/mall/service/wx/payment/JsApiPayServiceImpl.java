package cn.lime.mall.service.wx.payment;

import cn.lime.mall.model.vo.OrderPayVo;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.springframework.stereotype.Service;

/**
 * @ClassName: JsApiPayServiceImpl
 * @Description: jsApi支付方式具体实现类 小程序拉起支付
 * @Author: Lime
 * @Date: 2023/9/28 11:49
 */
@Service
public class JsApiPayServiceImpl extends BaseWxPayServiceImpl {

    @Override
    public OrderPayVo prepay(Long orderId, Integer price, String notifyUrl, String openId) {
        Amount amount = new Amount();
        amount.setTotal(price);
        PrepayRequest request = new PrepayRequest();
        request.setAmount(amount);
        request.setAppid(mallParams.getWxPayAppId());
        request.setMchid(mallParams.getWxPayMerchantId());
        request.setNotifyUrl(mallParams.getWxPayNotifyUrlPrefix() + notifyUrl);
        request.setOutTradeNo(String.valueOf(orderId));
        request.setDescription("ApplyEasy Order " + orderId);
        Payer payer = new Payer();
        payer.setOpenid(openId);
        request.setPayer(payer);
        // 预下单
        PrepayWithRequestPaymentResponse response =
                jsapiService.prepayWithRequestPayment(request);
        // 入缓存
        cacheOrder(orderId);
        OrderPayVo res = new OrderPayVo();
        res.setJsapiResponse(response);
        return res;
    }

    @Override
    public Transaction queryOrderById(Long orderId) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(String.valueOf(orderId));
        request.setMchid(String.valueOf(mallParams.getWxPayMerchantId()));
        return jsapiService.queryOrderByOutTradeNo(request);
    }

}
