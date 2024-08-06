package cn.lime.mall.model.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: ProductDetailDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 14:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDto implements Serializable {
    private Long productId;
}
