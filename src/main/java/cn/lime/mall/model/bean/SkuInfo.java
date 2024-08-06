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
    @Schema(description = "")
    private Integer skuPrice;
    @Schema(description = "")
    private Integer skuStock;
    @Schema(description = "")
    private String skuUrl;
    @Schema(description = "")
    private Map<String,String> attributes;

}
