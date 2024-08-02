package cn.lime.mall.model.bean;

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

    private Integer skuPrice;
    private Integer skuStock;
    private String skuUrl;
    private Map<String,String> attributes;

}
