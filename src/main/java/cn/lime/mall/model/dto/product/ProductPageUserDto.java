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
 * @ClassName: ProductPageDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 14:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageUserDto extends PageRequest implements Serializable {
    @Schema(description = "商品名称")
    private String productName;
    @Schema(description = "商品标签ID 序列化为String")
    @JsonSerialize(using = LongListToStringSerializer.class)
    private List<Long> tagIds;
    @Schema(description = "商品类型")
    private String productType;
}
