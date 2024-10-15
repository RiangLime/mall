package cn.lime.mall.model.dto.discount;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DiscountAddBatchDto extends DiscountAddDto{

    @Schema(description = "优惠嘛数量")
    @NotNull(message = "优惠码数量不可为空")
    private Integer number;

}
