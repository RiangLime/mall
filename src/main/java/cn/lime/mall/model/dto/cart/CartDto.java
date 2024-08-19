package cn.lime.mall.model.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: CartAddDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/19 16:38
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDto implements Serializable {
    private Long productId;
    private Long skuId;
    private Integer number;
}
