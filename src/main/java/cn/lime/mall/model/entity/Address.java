package cn.lime.mall.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName Address
 */
@TableName(value ="Address")
@Data
public class Address implements Serializable {
    /**
     * 地址ID 自增
     */
    @TableId(value = "address_id", type = IdType.AUTO)
    private Integer addressId;

    /**
     * 收货人对应用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 收货人姓名
     */
    @TableField(value = "receiver_name")
    private String receiverName;

    @TableField(value = "receiver_position")
    private String receiverPosition;

    /**
     * 收货地址
     */
    @TableField(value = "receiver_address")
    private String receiverAddress;

    /**
     * 收货人手机号
     */
    @TableField(value = "receiver_phone")
    private String receiverPhone;

    /**
     * 是否为默认地址
     */
    @TableField(value = "is_DEFAULT")
    private Integer isDefault;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private Date gmtCreated;

    /**
     * 
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}