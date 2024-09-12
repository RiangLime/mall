package cn.lime.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleTagVo implements Serializable {
    @Schema(description = "标签ID 序列化为string")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tagId;
    @Schema(description = "标签名")
    private String tagName;
}
