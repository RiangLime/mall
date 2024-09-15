package cn.lime.mall.model.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartNumberDto implements Serializable {
    @Schema(description = "购物车ID 序列化为string")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cartId;
    @Schema(description = "修改后的数量")
    private Integer number;
}
