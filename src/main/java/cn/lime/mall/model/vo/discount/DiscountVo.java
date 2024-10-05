package cn.lime.mall.model.vo.discount;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class DiscountVo implements Serializable {
    private Long id;
    private Integer type;
    private Long ownerId;
    private Integer minPrice;
    private Integer discountPrice;
    private Integer isAvailable;
    private List<ProductTitleVo> availableProductList;
}
