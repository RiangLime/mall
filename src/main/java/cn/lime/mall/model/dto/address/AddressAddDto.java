package cn.lime.mall.model.dto.address;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: AddressAddDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/20 10:59
 */
@Data
public class AddressAddDto implements Serializable {
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
