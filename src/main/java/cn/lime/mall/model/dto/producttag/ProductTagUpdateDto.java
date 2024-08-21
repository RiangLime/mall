package cn.lime.mall.model.dto.producttag;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: ProductTagUpdateDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/19 16:18
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductTagUpdateDto implements Serializable {
    @Schema(description = "标签ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tagId;
    @Schema(description = "标签名")
    private String tagName;
    @Schema(description = "标签展示URL")
    private String tagUrl;
    @Schema(description = "标签排序字段")
    private Integer tagSort;
    @Schema(description = "标签状态是否可见")
    private Integer tagState;
}
