package cn.lime.mall.model.dto.discount;

import cn.lime.core.common.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class DiscountAdminPageDto extends PageRequest implements Serializable {

    private Integer type;
    private Long ownerId;
    private Integer discountPriceRangeStart;
    private Integer discountPriceEnd;
    private List<Long> productId;
    private Integer isAvailable;

}
