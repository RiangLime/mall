package cn.lime.mall.service.db;

import cn.lime.core.common.PageResult;
import cn.lime.mall.model.entity.Discount;
import cn.lime.mall.model.vo.discount.DiscountVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Page;

import java.util.List;

/**
* @author riang
* @description 针对表【Discount(折扣表)】的数据库操作Service
* @createDate 2024-10-05 19:53:53
*/
public interface DiscountService extends IService<Discount> {
    DiscountVo addDiscount(Integer type, Long ownerId, Integer minPrice, Integer discountPrice, List<Long> productIds);
    DiscountVo getVoById(Long id);

    void deleteDiscount(Long id);
    PageResult<DiscountVo> pageDiscount(Integer type, Long ownerId, Integer discountPriceRangeStart, Integer discountPriceEnd,
                                        List<Long> productId, Integer isAvailable, Integer current, Integer pageSize);
    Integer useDiscount(Long id,Long order);
    DiscountVo updateNewUserDiscount(Integer minPrice, Integer discountPrice, List<Long> productIds);
}
