package cn.lime.mall.model.dto.producttag;

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
    private Long tagId;
    private String tagName;
    private String tagUrl;
    private Integer tagSort;
    private Integer tagState;
}
