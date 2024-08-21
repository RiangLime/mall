package cn.lime.mall.model.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName: ProductTagTagDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 14:48
 */
@Data
public class ProductTagDto {
    @Schema(description = "商品ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long productId;
    @Schema(description = "标签ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long tagId;
}
