package cn.lime.mall.model.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderItemDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 10:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto implements Serializable {
    @Schema(description = "商品ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long productId;
    @Schema(description = "SKU ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long skuId;
    @Schema(description = "购买数量")
    @NotNull
    @Min(value = 1,message = "数量必须大于等于1")
    private Integer number;
}
