package cn.lime.mall.model.dto.order;

import cn.lime.core.common.PageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderPageUserDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 15:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderPageUserDto extends PageRequest implements Serializable {
    @Schema(description = "订单编号")
    private String orderCode;
    @Schema(description = "商品名称")
    private String productName;
    @Schema(description = "订单状态")
    private Integer orderState;
    @Schema(description = "订单时间 开始时间 （秒级时间戳 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderStartTime;
    @Schema(description = "订单时间 结束时间 （秒级时间戳 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderEndTime;


}
