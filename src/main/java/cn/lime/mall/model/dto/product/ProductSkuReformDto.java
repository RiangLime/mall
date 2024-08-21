package cn.lime.mall.model.dto.product;

import cn.lime.mall.model.bean.SkuInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ProductSkuReformDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 15:02
 */
@Data
public class ProductSkuReformDto implements Serializable {
    @Schema(description = "商品ID 序列化为String")
    private Long productId;
    @Schema(description = "SKU信息")
    private List<SkuInfo> skuInfos;
}
