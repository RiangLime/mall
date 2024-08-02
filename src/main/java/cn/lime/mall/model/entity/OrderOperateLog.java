package cn.lime.mall.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单操作日志表
 * @TableName Order_Operate_Log
 */
@TableName(value ="Order_Operate_Log")
@Data
public class OrderOperateLog implements Serializable {
    /**
     * 日志ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 订单ID
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 操作用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 操作事件
     */
    @TableField(value = "operate")
    private String operate;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private Date gmtCreated;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}