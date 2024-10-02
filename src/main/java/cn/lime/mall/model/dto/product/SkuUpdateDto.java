package cn.lime.mall.model.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuUpdateDto implements Serializable {
    @Schema(description = "SKU ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long skuId;
    @Schema(description = "SKU编码")
    private String skuCode;
    @Schema(description = "SKU描述")
    private String skuDescription;
    @Schema(description = "SKU价格")
    private Integer skuPrice;
    @Schema(description = "SKU库存")
    private Integer skuStock;
    @Schema(description = "备注")
    private String remark;

}
