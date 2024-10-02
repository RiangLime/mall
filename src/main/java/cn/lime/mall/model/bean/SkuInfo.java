package cn.lime.mall.model.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName: SkuInfo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/7/31 16:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuInfo implements Serializable {
    @Schema(description = "SKU价格")
    private Integer skuPrice;
    @Schema(description = "SKU库存")
    private Integer skuStock;
    @Schema(description = "SKU展示图片")
    private String skuUrl;
    @Schema(description = "SKU属性  如{color:red ，size:big}")
    private Map<String,String> attributes;
    @Schema(description = "SKU描述")
    private String skuDescription;

}
