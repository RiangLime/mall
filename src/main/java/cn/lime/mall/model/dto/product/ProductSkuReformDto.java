package cn.lime.mall.model.dto.product;

import cn.lime.mall.model.bean.SkuInfo;
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
    private Long productId;
    private List<SkuInfo> skuInfos;
}
