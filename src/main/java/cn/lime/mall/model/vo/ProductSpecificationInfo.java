package cn.lime.mall.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: SkuAttributeInfo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/1 15:52
 */
@Data
public class ProductSpecificationInfo {
    @Schema(description = "规格")
    private String specificationKey;
    @Schema(description = "当前规格下所有值")
    private List<String> specificationValues;
}
