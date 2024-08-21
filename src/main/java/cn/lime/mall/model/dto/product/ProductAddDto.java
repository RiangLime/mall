package cn.lime.mall.model.dto.product;

import cn.lime.core.common.LongListToStringSerializer;
import cn.lime.mall.model.bean.SkuInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @Schema(description = "商品编码")
    private String productCode;
    @Schema(description = "商品名称")
    private String productName;
    @Schema(description = "商品品牌")
    private String productBrand;
    @Schema(description = "商品内容")
    private String productDescription;
    @Schema(description = "产品类型1 待定")
    private String realVirtualType;
    @Schema(description = "产品类型2 待定")
    private String detectNormalType;
    @Schema(description = "用户是否可见")
    private Integer visible;
    @Schema(description = "商品主图")
    private String mainPicUrl;
    @Schema(description = "商品轮播图")
    private List<String> roundUrls;
    @Schema(description = "SKU 信息")
    private List<SkuInfo> skuInfos;
    @Schema(description = "商品标签ID 序列化为String")
    @JsonSerialize(using = LongListToStringSerializer.class)
    private List<Long> productTagIds;
}
