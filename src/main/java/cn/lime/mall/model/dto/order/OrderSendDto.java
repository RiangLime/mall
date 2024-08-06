package cn.lime.mall.model.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderSendDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 17:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSendDto implements Serializable {
    private Long orderId;
    private String deliverCompany;
    private String deliverId;
}
