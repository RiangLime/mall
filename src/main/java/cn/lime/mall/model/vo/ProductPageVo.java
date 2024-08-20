package cn.lime.mall.model.vo;

import lombok.Data;

/**
 * @ClassName: ProductPageVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/1 10:48
 */
@Data
public class ProductPageVo {
    private Long productId;
    private String productCode;
    private String productName;
    private String productBrand;
    private Integer priceRangeStart;
    private Integer priceRangeEnd;
    private Integer productTotalStock;
    private Integer productSales;
    private Integer productViews;
    private Integer productState;
    private String productType;
    private Integer productSort;
    private Long createTime;
    private Boolean multiSku;
}
