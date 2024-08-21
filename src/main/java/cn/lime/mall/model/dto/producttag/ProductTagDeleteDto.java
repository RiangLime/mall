package cn.lime.mall.model.dto.producttag;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: ProductTagDeleteDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/19 16:21
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductTagDeleteDto implements Serializable {
    @Schema(description = "标签ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tagId;
}
