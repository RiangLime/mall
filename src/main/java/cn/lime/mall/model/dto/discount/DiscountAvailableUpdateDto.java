package cn.lime.mall.model.dto.discount;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Data
public class DiscountAvailableUpdateDto implements Serializable {
    @Schema(description = "折扣ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long discountId;
    @Schema(description = "是否可用 0不可用1可用")
    @Range(min = 0,max = 1,message = "0不可用 1可用")
    private Integer isAvailable;
}
