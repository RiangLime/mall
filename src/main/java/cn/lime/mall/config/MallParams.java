package cn.lime.mall.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author riang
 */
@Component
@Data
public class MallParams {

    // wx pay
    @Value("${mall.pay.wx.merchant-id:}")
    private String wxPayMerchantId;
    @Value("${mall.pay.wx.private-key-path:}")
    private String wxPayPrivateKeyPath;
    @Value("${mall.pay.wx.merchant-serial-number:}")
    private String wxPayMerchantSerialNumber;
    @Value("${mall.pay.wx.api-v3-key:}")
    private String wxPayApiV3Key;
    @Value("${mall.pay.wx.app-id:}")
    private String wxPayAppId;
    @Value("${mall.pay.wx.certificate-path:}")
    private String wxPayCertificatePath;
    @Value("${mall.pay.wx.notice-url-prefix:}")
    private String wxPayNotifyUrlPrefix;

    @Value("${mall.pay.timeout: 60000}")
    private Long paymentTimeout;
    /**
     * 单位Hour 3小时
     */
    @Value("${mall.order.timeout: 12}")
    private Integer orderTimeout;


    @Value("${mall.pay.stripe.key:}")
    private String stripeApiKey;
    @Value("${mall.pay.stripe.secret:}")
    private String stripeSecret;
    @Value("${mall.pay.stripe.complete-endpoint-url:}")
    private String successEndpointSecret;

}
