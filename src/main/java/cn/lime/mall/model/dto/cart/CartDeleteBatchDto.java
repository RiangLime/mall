package cn.lime.mall.model.dto.cart;

import cn.lime.core.common.LongListToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDeleteBatchDto implements Serializable {

    @Schema(description = "购物车ID列表 list<String>")
    @NotNull
    @JsonSerialize(using = LongListToStringSerializer.class)
    private List<Long> cartIds;
}
