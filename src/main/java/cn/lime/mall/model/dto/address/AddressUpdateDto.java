package cn.lime.mall.model.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: AddressUpdateDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/20 11:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressUpdateDto implements Serializable {
    private Integer addressId;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 收货地址
     */
    private String receiverAddress;
    /**
     * 收货人手机号
     */
    private String receiverPhone;
    /**
     * 是否为默认地址
     */
    private Integer isDefault;
}
