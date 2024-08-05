package cn.lime.mall.service.wx.payment;

import cn.lime.mall.model.vo.OrderPayVo;
import cn.lime.core.threadlocal.ReqThreadLocal;
import com.wechat.pay.java.service.payments.h5.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import org.springframework.stereotype.Service;


/**
 * @ClassName: H5PayServiceImpl
 * @Description: 微信H5支付服务
 * @Author: Lime
 * @Date: 2023/12/1 14:26
 */
@Service
public class H5PayServiceImpl extends BaseWxPayServiceImpl {

    @Override
    public OrderPayVo prepay(Long orderId, Integer price, String notifyUrl, String openId) {
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setPayerClientIp(ReqThreadLocal.getInfo().getIp());
        amount.setTotal(price);
        request.setAmount(amount);
        request.setMchid(mallParams.getWxPayMerchantId());
        request.setAppid(mallParams.getWxPayAppId());
        request.setNotifyUrl(mallParams.getWxPayNotifyUrlPrefix() + notifyUrl);
        request.setOutTradeNo(String.valueOf(orderId));
        request.setDescription("ApplyEasy Order " + orderId);
        request.setSceneInfo(sceneInfo);
        // 预下单
        PrepayResponse response = h5Service.prepay(request);
        cacheOrder(orderId);
        OrderPayVo res = new OrderPayVo();
        res.setUrlCode(response.getH5Url());
        return res;
    }

    @Override
    public Transaction queryOrderById(Long orderId) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(String.valueOf(orderId));
        request.setMchid(String.valueOf(mallParams.getWxPayMerchantId()));
        return h5Service.queryOrderByOutTradeNo(request);
    }

}
