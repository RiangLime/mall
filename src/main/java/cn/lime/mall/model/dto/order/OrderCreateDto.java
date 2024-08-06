package cn.lime.mall.model.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: OrderCreateDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 14:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto implements Serializable {
    private Long addressId;
    private List<OrderItemDto> orderItems;
    private String remark;
}
