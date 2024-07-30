package cn.lime.mall.model.vo;

import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: PayVo
 * @Description: 订单预支付vo
 * @Author: Lime
 * @Date: 2023/9/27 17:22
 */
@Data
public class OrderPayVo implements Serializable {

    @Schema(description = "支付码二维码")
    private String urlCode;

    @Schema(description = "预支付订单ID")
    private String prePayId;

    @Schema(description = "JSApi支付结果")
    private PrepayWithRequestPaymentResponse jsapiResponse;

    @Schema(description = "stripe支付session")
    private String payUrl;
}
