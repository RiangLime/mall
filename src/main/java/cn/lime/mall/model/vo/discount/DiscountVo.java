package cn.lime.mall.model.vo.discount;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class DiscountVo implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private Integer type;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long ownerId;
    private Integer minPrice;
    private Integer discountPrice;
    private Integer isAvailable;
    private List<ProductTitleVo> availableProductList;
}
