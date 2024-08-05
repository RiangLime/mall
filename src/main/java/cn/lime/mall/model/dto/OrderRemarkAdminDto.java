package cn.lime.mall.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderRemarkDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 16:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRemarkAdminDto implements Serializable {
    private Long orderId;
    private String merchantRemark;
}
