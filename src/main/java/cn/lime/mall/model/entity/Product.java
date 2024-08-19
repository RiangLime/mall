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
 * @TableName Product
 */
@TableName(value ="Product")
@Data
public class Product implements Serializable {
    /**
     * 商品ID
     */
    @TableId(value = "product_id")
    private Long productId;

    /**
     * 商品编码
     */
    @TableField(value = "product_code")
    private String productCode;

    /**
     * 商品名称
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 商品详情
     */
    @TableField(value = "product_description")
    private String productDescription;

    /**
     * 商品状态 1上架 0下架
     */
    @TableField(value = "product_state")
    private Integer productState;

    @TableField(value = "visible")
    private Integer visible;

    /**
     * 排序字段
     */
    @TableField(value = "product_sort")
    private Integer productSort;

    /**
     * 商品类型1
     */
    @TableField(value = "product_type_1")
    private String productType1;

    /**
     * 商品类型2
     */
    @TableField(value = "product_type_2")
    private String productType2;

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