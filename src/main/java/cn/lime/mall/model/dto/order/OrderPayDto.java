package cn.lime.mall.model.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @ClassName: OrderPayDto
 * @Description: 支付订单DTO
 * @Author: Lime
 * @Date: 2023/10/19 13:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class OrderPayDto implements Serializable {

    @NonNull
    @Schema(description = "订单ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long orderId;

    @Schema(description = "1微信支付-二维码支付 2微信支付-小程序JSapi支付 3 h5 4 Stripe支付")
    @Range(min = 1,max = 4)
    private Integer payMethod;

    @Schema(description = "支付成功跳转URL,stripe支付使用")
    private String successUrl;
    @Schema(description = "支付取消跳转URL,stripe支付使用")
    private String cancelUrl;

}
