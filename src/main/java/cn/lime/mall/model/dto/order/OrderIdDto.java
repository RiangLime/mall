package cn.lime.mall.model.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderCancelDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 14:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderIdDto implements Serializable {
    @Schema(description = "订单ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long orderId;
}
