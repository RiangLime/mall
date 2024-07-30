package cn.lime.mall.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单表
 * @TableName Order
 */
@TableName(value ="`Order`")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order implements Serializable {
    /**
     * 订单ID
     */
    @TableId(value = "order_id")
    private Long orderId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 商品ID
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * 付款方式
     */
    @TableField(value = "order_pay_method")
    private Integer orderPayMethod;

    /**
     * 订单状态 订单状态 0待支付、1支付中、2待发货、3待收货、4待评价、5已完成、8关闭订单、9退/换货
     */
    @TableField(value = "order_status")
    private Integer orderStatus;

    /**
     * 订单价格
     */
    @TableField(value = "order_price")
    private Integer orderPrice;

    /**
     * 微信回调函数是否被处理过 0没有 1处理过
     */
    @TableField(value = "order_is_deal")
    private Integer orderIsDeal;

    /**
     * stripe付款ID
     */
    @TableField(value = "third_payment_id")
    private String thirdPaymentId;

    /**
     * stripe支付方式
     */
    @TableField(value = "third_payment_method")
    private Integer thirdPaymentMethod;

    @TableField(value = "refund_id")
    private Long refundId;

    @TableField(value = "refund_status")
    private Integer refundStatus;

    @TableField(value = "refund_price")
    private Integer refundPrice;

    @TableField(value = "refund_is_deal")
    private Integer refundIsDeal;
    /**
     * 备注信息
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_created")
    private String gmtCreated;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified")
    private String gmtModified;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}