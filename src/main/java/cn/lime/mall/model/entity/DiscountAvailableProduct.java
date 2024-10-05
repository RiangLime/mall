package cn.lime.mall.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 折扣可用商品表
 * @TableName Discount_Available_Product
 */
@TableName(value ="Discount_Available_Product")
@Data
public class DiscountAvailableProduct implements Serializable {
    /**
     * 折扣项ID
     */
    @TableId(value = "id")
    private Long discountId;

    /**
     * 折扣项可用商品ID
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private Date gmtCreated;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}