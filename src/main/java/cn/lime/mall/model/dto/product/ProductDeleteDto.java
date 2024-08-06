package cn.lime.mall.model.dto.product;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ProductDeleteDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 14:36
 */
@Data
public class ProductDeleteDto implements Serializable {
    private List<Long> productIds;
}
