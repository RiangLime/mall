package cn.lime.mall.mapper;

import cn.lime.mall.model.entity.Discount;
import cn.lime.mall.model.vo.discount.DiscountVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
* @author riang
* @description 针对表【Discount(折扣表)】的数据库操作Mapper
* @createDate 2024-10-05 19:53:53
* @Entity cn.lime.mall.model.entity.Discount
*/
public interface DiscountMapper extends BaseMapper<Discount> {
    DiscountVo getDetail(Long id);
    Page<Long> getPage(Integer type, Long ownerId, Integer discountPriceRangeStart,
                       Integer discountPriceEnd, List<Long> productId, Integer isAvailable, Page<?> page);
}




