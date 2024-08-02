package cn.lime.mall.model.vo;

import cn.lime.mall.model.entity.Skuattribute;
import com.baomidou.mybatisplus.annotation.TableField;
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
    private Long orderId;
    private Integer productNumber;
    private Integer realOrderPrice;

    // 用户信息
    private Long userId;
    private String userName;

    // 收货地址
    private Long addressId;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;

    // 商品信息
    private Long productId;
    private String productName;
    private String productCode;
    private String productMainUrl;
    private Long skuId;
    private String skuCode;
    private List<Skuattribute> skuAttributes;

}
