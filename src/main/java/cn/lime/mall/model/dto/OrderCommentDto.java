package cn.lime.mall.model.dto;

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
    private Long orderId;
    private String comment;
}
