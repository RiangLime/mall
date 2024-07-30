package cn.lime.mall.config;

import lombok.Data;
import org.springframework.stereotype.Component;

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

}
