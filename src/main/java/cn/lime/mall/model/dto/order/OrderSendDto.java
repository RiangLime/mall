package cn.lime.mall.model.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderSendDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 17:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSendDto implements Serializable {
    @Schema(description = "订单ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;
    @Schema(description = "快递公司")
    private String deliverCompany;
    @Schema(description = "快递单号")
    private String deliverId;
}
