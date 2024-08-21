package cn.lime.mall.model.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: ProductIdDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 14:58
 */
@Data
public class ProductIdDto implements Serializable {
    @Schema(description = "商品ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;
}
