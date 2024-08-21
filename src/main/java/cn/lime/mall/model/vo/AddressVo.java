package cn.lime.mall.model.vo;

import cn.lime.mall.model.entity.Address;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: AddressVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/20 10:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressVo implements Serializable {
    /**
     * 地址ID 自增
     */
    @Schema(description = "收货地址ID")
    private Integer addressId;

    /**
     * 收货人姓名
     */
    @Schema(description = "收货人姓名")
    private String receiverName;

    /**
     * 收货地址
     */
    @Schema(description = "收货地址")
    private String receiverAddress;

    /**
     * 收货人手机号
     */
    @Schema(description = "收货人手机")
    private String receiverPhone;

    /**
     * 是否为默认地址
     */
    @Schema(description = "是否为默认地址")
    private Integer isDefault;

    public static AddressVo fromBean(Address bean){
        AddressVo vo = new AddressVo();
        BeanUtils.copyProperties(bean,vo);
        return vo;
    }

}
