package cn.lime.mall.model.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderItemDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 10:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto implements Serializable {
    private Long productId;
    private Long skuId;
    private Integer number;
}
