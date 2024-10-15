package cn.lime.mall.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 折扣表
 * @TableName Discount
 */
@TableName(value ="Discount")
@Data
public class Discount implements Serializable {
    /**
     * 折扣项ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 折扣类型 1新用户免费送券 2指定用户类型优惠券 3CdKey
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 拥有者用户ID 如果是cdKey就为空
     */
    @TableField(value = "owner_id")
    private Long ownerId;

    /**
     * 可用时最小订单价格
     */
    @TableField(value = "min_price")
    private Integer minPrice;

    /**
     * 优惠减少价格
     */
    @TableField(value = "discount_price")
    private Integer discountPrice;

    /**
     * 是否可用 优惠仅可用一次 用完后变为0
     */
    @TableField(value = "is_available")
    private Integer isAvailable;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private Date gmtCreated;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}