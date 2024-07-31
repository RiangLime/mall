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
 * @TableName Product_Url
 */
@TableName(value ="Product_Url")
@Data
public class ProductUrl implements Serializable {
    /**
     * url id
     */
    @TableId(value = "url_id")
    private Long urlId;

    /**
     * 商品ID
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * URL类型 1主图 2轮播图
     */
    @TableField(value = "url_type")
    private Integer urlType;

    /**
     * 轮播图序号
     */
    @TableField(value = "url_sort")
    private Integer urlSort;

    /**
     * url
     */
    @TableField(value = "url")
    private String url;

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