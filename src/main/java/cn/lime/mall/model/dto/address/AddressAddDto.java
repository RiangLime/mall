package cn.lime.mall.model.dto.address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
    @Schema(description = "收货人姓名")
    @NotNull
    private String receiverName;
    /**
     * 收货地址
     */
    @Schema(description = "收货人地址")
    @NotNull
    private String receiverAddress;
    /**
     * 收货人手机号
     */
    @Schema(description = "收货人手机号")
    @NotNull
    private String receiverPhone;
    /**
     * 是否为默认地址
     */
    @Schema(description = "是否为默认地址 1默认地址 0不是默认地址")
    private Integer isDefault = 0;
}
