package cn.lime.mall.model.vo;

import cn.lime.mall.model.entity.Skuattribute;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: OrderPageVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/2 14:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageVo implements Serializable {

    // 订单信息
    @Schema(description = "订单ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;
    @Schema(description = "订单编码")
    private String orderCode;
    @Schema(description = "订单状态")
    private Integer orderState;
    @Schema(description = "订单价格")
    private Integer realOrderPrice;
    // 用户信息
    @Schema(description = "用户ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @Schema(description = "用户昵称")
    private String userName;
    @Schema(description = "用户头像")
    private String userAvatar;
    // 收货地址
    @Schema(description = "收货地址ID")
    private Integer addressId;
    @Schema(description = "收货人姓名")
    private String receiverName;
    @Schema(description = "收货地址")
    private String receiverAddress;
    @Schema(description = "收货人手机号")
    private String receiverPhone;

    @Schema(description = "订单创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "用户付款时间")
    private Long payTime;
    @Schema(description = "发货时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sendTime;
    @Schema(description = "用户收货时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long receiveTime;
    @Schema(description = "订单结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long finishTime;

    @Schema(description = "订单包含的商品信息")
    private List<OrderProductSkuVo> orderSkuList;


}
