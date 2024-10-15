package cn.lime.mall.model.dto.discount;

import cn.lime.core.common.LongListToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DiscountAddDto implements Serializable {
    @Schema(description = "可用用户 不填即为CDKEY所有人可用 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ownerId;
    @Schema(description = "折扣券使用最低价格 默认为0")
    private Integer minPrice = 0;
    @Schema(description = "折扣券减少价格")
    @NotNull
    @Min(value = 1,message = "减少价格必须大于0")
    private Integer discountPrice;
    @Schema(description = "折扣券可用商品ID")
    @JsonSerialize(using = LongListToStringSerializer.class)
    @NotNull
    private List<Long> availableProductList;

}
