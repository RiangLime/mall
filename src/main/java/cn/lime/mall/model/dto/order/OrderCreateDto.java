package cn.lime.mall.model.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "地址ID")
    private Integer addressId;
    @Schema(description = "购买商品信息")
    private List<OrderItemDto> orderItems;
    @Schema(description = "用户备注")
    private String remark;
}
