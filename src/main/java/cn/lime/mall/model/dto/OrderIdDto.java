package cn.lime.mall.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderCancelDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 14:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderIdDto implements Serializable {
    private Long orderId;
}
