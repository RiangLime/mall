package cn.lime.mall.model.vo;

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
    private Long orderId;
    private String orderCode;
    private Integer orderSource;
    private Integer realOrderPrice;
    private Integer orderState;
    private Long orderCreateTime;
    private Integer orderPayMethod;
    private Long orderPayTime;
    private Long orderFinishTime;
    private String userRemark;
    private String merchantRemark;

    private Long userId;
    private String userName;
    private String userAvatar;

    private Long addressId;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private String deliverCompany;
    private String deliverId;
    private Long deliverTime;

    private List<OrderProductSkuVo> orderSkuList;
    private List<OrderOperateLogVo> logs;

}
