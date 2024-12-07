package cn.lime.mall.model.dto.discount;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountIdBindAdminDto {
    @Schema(description = "折扣ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long discountId;
    @Schema(description = "用户ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
}
