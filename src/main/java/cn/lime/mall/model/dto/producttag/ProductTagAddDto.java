package cn.lime.mall.model.dto.producttag;

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
    private String tag;
    private String tagUrl;
}
