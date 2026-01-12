package cn.lime.mall.model.dto.product;

import cn.lime.core.common.LongListToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: GetProductPageVoFromIdsDto
 * @Description: TODO 描述类的功能
 * @Author: riang
 * @Date: 2026/1/10 20:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductPageVoFromIdsDto implements Serializable {

    @Schema(description = "商品ID列表")
    @JsonSerialize(using = LongListToStringSerializer.class)
    private List<Long> ids;
}