package cn.lime.mall.model.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderCommentDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 15:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCommentDto implements Serializable {

    @Schema(description = "订单ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderId;
    @Schema(description = "用户评论内容 --格式如何?")
    private String comment;
}
