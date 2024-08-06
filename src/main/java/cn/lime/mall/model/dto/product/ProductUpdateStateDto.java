package cn.lime.mall.model.dto.product;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ProductUpdateStateDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 14:39
 */
@Data
public class ProductUpdateStateDto implements Serializable {
    private List<Long> productIds;
    private Integer state;
}
