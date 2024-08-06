package cn.lime.mall.model.dto.order;

import cn.lime.core.common.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderPageUserDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 15:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderPageUserDto extends PageRequest implements Serializable {
    private String orderCode;
    private String userName;
    private String productName;
    private Integer orderState;
    private Long orderStartTime;
    private Long orderEndTime;


}
