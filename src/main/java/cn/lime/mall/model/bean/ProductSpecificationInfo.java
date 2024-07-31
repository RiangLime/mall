package cn.lime.mall.model.bean;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: ProductSpecificationInfo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/7/31 16:34
 */
@Data
public class ProductSpecificationInfo {
    private String groupName;
    private List<String> values;
}
