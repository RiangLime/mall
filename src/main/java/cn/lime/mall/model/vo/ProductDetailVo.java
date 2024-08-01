package cn.lime.mall.model.vo;

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
    private Long productId;
    private String productCode;
    private String productName;
    private String productDescription;
    private Integer productState;
    private Integer productSort;
    private String productType1;
    private String productType2;
    private Long createTime;
    private Long modifiedTime;

    /*
    * url
    * */
    private String mainUrl;
    private List<String> roundUrls;

    /**
     * 标签分类信息
     */
    private List<String> productTags;

    /**
     * 规格信息
     */
    List<ProductSpecificationInfo> specificationInfos;
    /**
     * SKU信息
     */
    private List<SkuInfoVo> skuInfos;

}
