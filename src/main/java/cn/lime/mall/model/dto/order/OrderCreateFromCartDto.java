package cn.lime.mall.model.dto.order;

import cn.lime.core.common.LongListToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderCreateFromCartDto implements Serializable {

    @Schema(description = "地址ID")
    private Integer addressId;
    @Schema(description = "购物车ID")
    @NotNull
    @JsonSerialize(using = LongListToStringSerializer.class)
    private List<Long> cartIds;
    @Schema(description = "用户备注")
    private String remark;

}
