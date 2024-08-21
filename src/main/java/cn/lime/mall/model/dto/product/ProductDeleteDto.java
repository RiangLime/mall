package cn.lime.mall.model.dto.product;

import cn.lime.core.common.LongListToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ProductDeleteDto
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 14:36
 */
@Data
public class ProductDeleteDto implements Serializable {
    @Schema(description = "商品ID 序列化为String")
    @JsonSerialize(using = LongListToStringSerializer.class)
    private List<Long> productIds;
}
