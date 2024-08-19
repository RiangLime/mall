package cn.lime.mall.model.dto.producttag;

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
    private Long tagId;
}
