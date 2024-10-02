package cn.lime.mall.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 * @TableName Order
 */
@TableName(value ="`Order`")
@Data
public class Order implements Serializable {
    /**
     * 订单ID
     */
    @TableId(value = "order_id")
    private Long orderId;

    /**
     * 订单来源
     */
    @TableField(value = "order_source")
    private Integer orderSource;

    /**
     * 订单编号
     */
    @TableField(value = "order_code")
    private String orderCode;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 用户收货地址ID
     */
    @TableField(value = "address_id")
    private Integer addressId;

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
    @TableField(value = "origin_order_price")
    private Integer originOrderPrice;

    /**
     * 实际订单价格
     */
    @TableField(value = "real_order_price")
    private Integer realOrderPrice;

    @TableField(value = "order_start_pay_time")
    private Long orderStartPayTime;

    /**
     * 订单支付时间
     */
    @TableField(value = "order_pay_time")
    private Date orderPayTime;

    @TableField(value = "order_receive_time")
    private Date orderReceiveTime;

    /**
     * 订单完成时间
     */
    @TableField(value = "order_finish_time")
    private Date orderFinishTime;

    /**
     * 微信回调函数是否被处理过 0没有 1处理过
     */
    @TableField(value = "order_is_deal")
    private Integer orderIsDeal;

    /**
     * 第三方付款ID
     */
    @TableField(value = "third_payment_id")
    private String thirdPaymentId;

    /**
     * 第三方支付方式
     */
    @TableField(value = "third_payment_method")
    private Integer thirdPaymentMethod;

    /**
     * 退款ID
     */
    @TableField(value = "refund_id")
    private Long refundId;

    /**
     * 退款状态 0未退款 1退款中 2退款成功 3退款关闭 4退款异常
     */
    @TableField(value = "refund_status")
    private Integer refundStatus;

    /**
     * 退款金额
     */
    @TableField(value = "refund_price")
    private Integer refundPrice;

    /**
     * 微信回调函数是否被处理过 0没有 1处理过
     */
    @TableField(value = "refund_is_deal")
    private Integer refundIsDeal;

    /**
     * 备注信息
     */
    @TableField(value = "remark1")
    private String remark1;

    /**
     * 备注信息
     */
    @TableField(value = "remark2")
    private String remark2;

    /**
     * 评论
     */
    @TableField(value = "comment")
    private String comment;

    /**
     * 物流公司
     */
    @TableField(value = "deliver_company")
    private String deliverCompany;

    /**
     * 物流单号
     */
    @TableField(value = "deliver_id")
    private String deliverId;

    /**
     * 发货时间
     */
    @TableField(value = "send_deliver_time")
    private Date sendDeliverTime;

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