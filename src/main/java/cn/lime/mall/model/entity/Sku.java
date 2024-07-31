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
 * @TableName Sku
 */
@TableName(value ="Sku")
@Data
public class Sku implements Serializable {
    /**
     * 库存单元ID
     */
    @TableId(value = "sku_id")
    private Long skuId;

    /**
     * 商品ID
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * sku编码
     */
    @TableField(value = "sku_code")
    private String skuCode;

    /**
     * sku描述
     */
    @TableField(value = "sku_description")
    private String skuDescription;

    /**
     * SKU价格
     */
    @TableField(value = "price")
    private Integer price;

    /**
     * 库存
     */
    @TableField(value = "stock")
    private Integer stock;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private Date gmtCreated;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}