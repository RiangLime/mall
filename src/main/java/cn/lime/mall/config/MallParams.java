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
    private String wxPayMerchantId;
    private String wxPayPrivateKeyPath;
    private String wxPayMerchantSerialNumber;
    private String wxPayApiV3Key;
    private String wxPayApId;
    private String wxPayCertificatePath;
    private String wxPayNotifyUrlPrefix;

    @Value("${payment.wx.timeout: 30000}")
    private Long paymentTimeout;
    /**
     * 单位秒 3小时
     */
    @Value("${payment.order.timeout: 10800}")
    private Integer orderTimeout;

}
