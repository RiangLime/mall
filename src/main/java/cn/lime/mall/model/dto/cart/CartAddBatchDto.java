package cn.lime.mall.model.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartAddBatchDto implements Serializable {

    @Schema(description = "批量添加DTO")
    @NotNull(message = "批量添加购物车列表不可为空")
    private List<CartDto> dtoList;
}
