package cn.lime.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: ProductPageVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/1 10:48
 */
@Data
public class ProductPageVo {
    @Schema(description = "商品ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;
    @Schema(description = "商品编码")
    private String productCode;
    @Schema(description = "商品名称")
    private String productName;
    @Schema(description = "商品副标题")
    private String productSubTitle;
    @Schema(description = "商品品牌")
    private String productBrand;
    @Schema(description = "商品主图")
    private String productMainUrl;
    @Schema(description = "商品价格范围 开始")
    private Integer priceRangeStart;
    @Schema(description = "商品范围价格 结束")
    private Integer priceRangeEnd;
    @Schema(description = "商品库存")
    private Integer productTotalStock;
    @Schema(description = "商品销量")
    private Integer productSales;
    @Schema(description = "商品浏览量")
    private Integer productViews;
    @Schema(description = "商品状态")
    private Integer productState;
    @Schema(description = "商品是否可见")
    private Integer productVisible;
    @Schema(description = "商品类型1")
    private String productType;
    @Schema(description = "商品排序字段")
    private Integer productSort;
    @Schema(description = "商品创建时间 秒级时间戳 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createTime;
    @Schema(description = "是否多规格")
    private Boolean multiSku;
}
