package cn.lime.mall.model.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: AddressDeleteDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/20 11:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDeleteDto implements Serializable {
    private Integer addressId;
}
