package cn.lime.mall.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName Product_Tag
 */
@TableName(value ="Product_Tag")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTag implements Serializable {
    /**
     * 商品分类ID
     */
    @TableId(value = "tag_id")
    private Long tagId;

    /**
     * 父级分类ID
     */
    @TableField(value = "parent_tag_id")
    private Long parentTagId;

    /**
     * 分类名
     */
    @TableField(value = "tag_name")
    private String tagName;

    /**
     * 分类图标
     */
    @TableField(value = "tag_url")
    private String tagUrl;

    /**
     * 排序字段
     */
    @TableField(value = "tag_sort")
    private Integer tagSort;

    /**
     * 分类状态 是否展示
     */
    @TableField(value = "tag_state")
    private Integer tagState;

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