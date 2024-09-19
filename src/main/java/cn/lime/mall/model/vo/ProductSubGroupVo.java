package cn.lime.mall.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSubGroupVo implements Serializable {
    @Schema(description = "分组二级标签信息")
    private ProductLevelTagVo tagVo;
    @Schema(description = "商品列表")
    private List<ProductPageVo> productVos;
}
