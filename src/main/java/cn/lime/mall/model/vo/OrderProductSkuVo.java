package cn.lime.mall.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: OrderProductVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/2 17:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductSkuVo implements Serializable {
    private Long productId;
    private String productCode;
    private String productName;
    private String productDescription;
    private Integer productState;
    private Integer productType1;
    private Integer productType2;
    private String productMainUrl;
    private Long skuId;
    private String skuCode;
    private Integer skuPrice;
    private Integer buyNumber;
    List<SkuAttributeVo> skuAttributes;

}
