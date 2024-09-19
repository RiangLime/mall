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
public class ProductGroupVo implements Serializable {
    @Schema(description = "一级标签信息")
    private ProductLevelTagVo tagVo;
    @Schema(description = "二级列表")
    private List<ProductSubGroupVo> subGroupVos;

}
