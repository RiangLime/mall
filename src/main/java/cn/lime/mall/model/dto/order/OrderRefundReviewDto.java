package cn.lime.mall.model.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @ClassName: OrderRefundReviewDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/22 17:40
 */
@Data
public class OrderRefundReviewDto implements Serializable {
    @Schema(description = "订单ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long orderId;

    @Schema(description = "是否同意退款")
    @Range(min = 0,max = 1,message = "该字段仅可为0或1")
    private Integer isApprove;
}
