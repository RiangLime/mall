package cn.lime.mall.model.dto.product;

import cn.lime.core.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ProductPageAdminDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 14:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageAdminDto extends PageRequest implements Serializable {
    @Schema(description = "")
    private String productName;
    @Schema(description = "")
    private List<Long> tagIds;
    @Schema(description = "")
    private String productType;

    private Integer productState;
}
