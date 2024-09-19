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
public class ProductMallHomePageVo implements Serializable {
    @Schema(description = "全量商品信息")
    private List<ProductPageVo> products;
    @Schema(description = "商品按标签分组信息 每一个实体为一个一级标签")
    private List<ProductGroupVo> groupProductVos;
}
