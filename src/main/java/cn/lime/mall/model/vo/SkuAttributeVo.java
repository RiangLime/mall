package cn.lime.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: SkuAttributeVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/2 17:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuAttributeVo implements Serializable {
    @Schema(description = "属性ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @Schema(description = "SKU ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuId;
    @Schema(description = "SKU属性名")
    private String attributeName;
    @Schema(description = "SKU属性值")
    private String attributeValue;
}
