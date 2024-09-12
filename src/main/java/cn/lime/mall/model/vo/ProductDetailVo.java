package cn.lime.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: ProductDetailVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/1 15:49
 */
@Data
public class ProductDetailVo {

    /**
     * product信息
     */
    @Schema(description = "商品ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;
    @Schema(description = "商品编码")
    private String productCode;
    @Schema(description = "商品名")
    private String productName;
    @Schema(description = "商品详情页")
    private String productDescription;
    @Schema(description = "商品状态")
    private Integer productState;
    @Schema(description = "商品排序字段")
    private Integer productSort;
    @Schema(description = "商品分类1")
    private String productType1;
    @Schema(description = "商品分类2")
    private String productType2;
    @Schema(description = "商品品牌")
    private String productBrand;
    @Schema(description = "商品是否可见")
    private Integer productVisible;
    @Schema(description = "商品创建时间 秒级时间戳 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createTime;
    @Schema(description = "商品最新修改时间 秒级时间戳 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long modifiedTime;

    /*
    * url
    * */
    @Schema(description = "商品主图")
    private String mainUrl;
    @Schema(description = "商品轮播图")
    private List<String> roundUrls;

    /**
     * 标签分类信息
     */
    @Schema(description = "商品相关标签")
    private List<SimpleTagVo> productTags;

    /**
     * 规格信息
     */
    @Schema(description = "商品规格信息")
    List<ProductSpecificationInfo> specificationInfos;
    /**
     * SKU信息
     */
    @Schema(description = "SKU详细信息")
    private List<SkuInfoVo> skuInfos;

}
