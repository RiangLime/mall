package cn.lime.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: OrderVo
private Integer orderSource;
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/2 14:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo implements Serializable {
    @Schema(description = "订单ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;
    @Schema(description = "订单编码")
    private String orderCode;
    @Schema(description = "订单来源")
    private Integer orderSource;
    @Schema(description = "订单实际价格")
    private Integer realOrderPrice;
    @Schema(description = "订单状态 0待支付、1支付中、2待发货、3待收货、4待评价、5已完成、8关闭订单、9退/换货")
    private Integer orderState;
    @Schema(description = "订单创建时间 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderCreateTime;
    @Schema(description = "订单支付方式 ")
    private Integer orderPayMethod;
    @Schema(description = "订单支付时间 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderPayTime;
    @Schema(description = "订单呢结束时间 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderFinishTime;
    @Schema(description = "用户备注")
    private String userRemark;
    @Schema(description = "商家备注")
    private String merchantRemark;

    @Schema(description = "用户ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @Schema(description = "用户昵称")
    private String userName;
    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "用户收货地址ID")
    private Integer addressId;
    @Schema(description = "收货人姓名")
    private String receiverName;
    @Schema(description = "收货省市区")
    private String receiverPosition;
    @Schema(description = "收货地址")
    private String receiverAddress;
    @Schema(description = "收货人手机号")
    private String receiverPhone;
    @Schema(description = "快递公司")
    private String deliverCompany;
    @Schema(description = "快递单号")
    private String deliverId;
    @Schema(description = "发货时间 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deliverTime;

    @Schema(description = "订单包含的商品信息")
    private List<OrderProductSkuVo> orderSkuList;
    @Schema(description = "订单历史操作信息")
    private List<OrderOperateLogVo> logs;

}
