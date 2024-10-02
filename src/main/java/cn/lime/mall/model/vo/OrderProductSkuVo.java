package cn.lime.mall.model.vo;

import cn.lime.mall.model.entity.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "订单商品是否依旧存在")
    private Integer productIsExist;
    @Schema(description = "订单SKU是否依旧存在")
    private Integer skuIsExist;
    @Schema(description = "商品ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;
    @Schema(description = "商品编码")
    private String productCode;
    @Schema(description = "商品名称")
    private String productName;
    @Schema(description = "商品描述 --格式待定")
    private String productDescription;
    @Schema(description = "商品状态 0下架1上架")
    private Integer productState;
    @Schema(description = "商品类型1")
    private String productType1;
    @Schema(description = "商品类型2")
    private String productType2;
    @Schema(description = "商品主图")
    private String productMainUrl;
    @Schema(description = "SKU ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuId;
    @Schema(description = "SKU编码")
    private String skuCode;
    @Schema(description = "SKU单价")
    private Integer skuPrice;
    @Schema(description = "购买数量")
    private Integer buyNumber;
    @Schema(description = "SKU属性信息")
    List<SkuAttributeVo> skuAttributes;

    public void fillProductInfo(Product product,String mainUrl){
        setProductId(product.getProductId());
        setProductCode(product.getProductCode());
        setProductName(product.getProductName());
        setProductDescription(product.getProductDescription());
        setProductState(product.getProductState());
        setProductType1(product.getProductType1());
        setProductType2(product.getProductType2());
        setProductMainUrl(mainUrl);
    }

}
