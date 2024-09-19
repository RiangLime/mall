package cn.lime.mall.model.dto.producttag;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductParentIdDto implements Serializable {

    @Schema(description = "父标签ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;
}
