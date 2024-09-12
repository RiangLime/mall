package cn.lime.mall.model.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto implements Serializable {
    @Schema(description = "商品ID 序列化为string")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;
    @Schema(description = "商品编码")
    private String productCode;
    @Schema(description = "商品名称")
    private String productName;
    @Schema(description = "商品品牌")
    private String brand;
    @Schema(description = "商品详情页")
    private String productDescription;
    @Schema(description = "商品类型1")
    private String productType1;
    @Schema(description = "商品类型2")
    private String productType2;
    @Schema(description = "商品是否可见")
    private Integer isVisible;
    @Schema(description = "商品主图")
    private String mainPicUrl;
    @Schema(description = "商品轮播图 将删除已有轮播图并进行覆盖")
    private List<String> roundUrls;

}
