package cn.lime.mall.model.dto.product;

import cn.lime.core.common.LongListToStringSerializer;
import cn.lime.core.common.PageRequest;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @Schema(description = "商品名称")
    private String productName;
    @Schema(description = "商品标签ID 序列化为String")
    @JsonSerialize(using = LongListToStringSerializer.class)
    private List<Long> tagIds;
    @Schema(description = "商品类型1")
    private String productType;
    @Schema(description = "商品状态 0下架 1上架")
    private Integer productState;
    @Schema(description = "商品是否可见")
    private Integer isVisible;
}
