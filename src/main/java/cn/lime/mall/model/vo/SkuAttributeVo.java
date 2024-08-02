package cn.lime.mall.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: SkuAttributeVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/2 17:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuAttributeVo implements Serializable {
    private Long id;
    private Long skuId;
    private String attributeName;
    private String attributeValue;
}
