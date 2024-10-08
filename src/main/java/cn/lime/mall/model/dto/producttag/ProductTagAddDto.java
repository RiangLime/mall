package cn.lime.mall.model.dto.producttag;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: ProductTagAddDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/19 16:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTagAddDto implements Serializable {
    @Schema(description = "标签父ID 如果为一级标签则为0")
    @NotNull(message = "父标签ID不可为空")
    private Long parentId = 0L;
    @Schema(description = "标签名")
    @NotNull(message = "标签名不可为空")
    private String tag;
    @Schema(description = "标签展示URL")
    private String tagUrl;
}
