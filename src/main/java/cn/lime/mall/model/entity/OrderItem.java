package cn.lime.mall.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单物品表
 * @TableName Order_Item
 */
@TableName(value ="Order_Item")
@Data
public class OrderItem implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 订单ID
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 商品ID
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * SKU ID
     */
    @TableField(value = "sku_id")
    private Long skuId;

    /**
     * 购买数量
     */
    @TableField(value = "number")
    private Integer number;


    @TableField(value = "item_price")
    private Integer itemPrice;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private Date gmtCreated;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}