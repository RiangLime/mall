package cn.lime.mall.model.dto.product;

import cn.lime.mall.model.bean.SkuInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ProductAddDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 14:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAddDto implements Serializable {
    @Schema(description = "")
    private String productCode;
    @Schema(description = "")
    private String productName;
    @Schema(description = "")
    private String productBrand;
    @Schema(description = "")
    private String productDescription;
    @Schema(description = "")
    private String realVirtualType;
    @Schema(description = "")
    private String detectNormalType;
    @Schema(description = "")
    private Integer visible;
    @Schema(description = "")
    private String mainPicUrl;
    @Schema(description = "")
    private List<String> roundUrls;
    @Schema(description = "")
    private List<SkuInfo> skuInfos;
    @Schema(description = "")
    private List<Long> productTagIds;
}
