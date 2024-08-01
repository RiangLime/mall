package cn.lime.mall.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: SkuAttributeInfo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/1 15:52
 */
@Data
public class ProductSpecificationInfo {
    private String specificationKey;
    private List<String> specificationValues;
}
