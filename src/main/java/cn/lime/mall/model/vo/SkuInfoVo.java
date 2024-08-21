package cn.lime.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName: SkuVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/1 15:55
 */
@Data
public class SkuInfoVo {

    @Schema(description = "SKU ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuId;
    @Schema(description = "SKU编码")
    private String skuCode;
    @Schema(description = "SKU详情")
    private String skuDescription;
    @Schema(description = "SKU单价")
    private Integer price;
    @Schema(description = "SKU库存")
    private Integer stock;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "创建时间 秒级时间戳 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createTime;
    @Schema(description = "SKU包含的属性信息 键值对存在 {color:red,size:L}")
    private Map<String,String> attributes;

}
