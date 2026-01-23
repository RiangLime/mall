package cn.lime.mall.service.wx.payment;

import cn.lime.mall.model.vo.OrderPayVo;
import com.alibaba.fastjson.JSON;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName: JsApiPayServiceImpl
 * @Description: jsApi支付方式具体实现类 小程序拉起支付
 * @Author: Lime
 * @Date: 2023/9/28 11:49
 */
@Service
@Slf4j
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
        request.setOutTradeNo(getOrderOutTradeNo(orderId));
        request.setDescription(getOrderDescription(orderId));
        Payer payer = new Payer();
        payer.setOpenid(openId);
        request.setPayer(payer);
        log.info("微信预支付请求体:" + JSON.toJSONString(request));
        // 预下单
        PrepayWithRequestPaymentResponse response =
                jsapiService.prepayWithRequestPayment(request);
        // 入缓存
        super.cacheOrder(orderId);
        OrderPayVo res = new OrderPayVo();
        res.setJsapiResponse(response);
        return res;
    }

    @Override
    public Transaction queryOrderByOutTradeNo(String outTradeNo) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(outTradeNo);
        request.setMchid(String.valueOf(mallParams.getWxPayMerchantId()));
        return jsapiService.queryOrderByOutTradeNo(request);
    }

}
