package cn.lime.mall.model.dto.product;

import cn.lime.core.common.LongListToStringSerializer;
import cn.lime.mall.model.bean.SkuInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

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
    @NotNull
    private String productCode;
    @Schema(description = "商品名称")
    @NotNull
    private String productName;
    @Schema(description = "商品副标题")
    private String productSubTitle;
    @Schema(description = "商品品牌")
    private String productBrand;
    @Schema(description = "商品内容")
    private String productDescription;
    @Schema(description = "产品状态 0下架1上架 默认1上架")
    @Nullable
    private Integer productState = 1;
    @Schema(description = "产品类型1 待定")
    private String productType1;
    @Schema(description = "产品类型2 待定")
    private String productType2;
    @Schema(description = "用户是否可见 1可见 0不可见")
    @NotNull
    @Range(min = 0,max = 1)
    private Integer visible = 1;
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
