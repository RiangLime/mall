package cn.lime.mall.service.db.impl;

import cn.lime.core.common.*;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.constant.YesNoEnum;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.core.threadlocal.ReqThreadLocal;
import cn.lime.mall.constant.DiscountTypeEnum;
import cn.lime.mall.model.entity.DiscountAvailableProduct;
import cn.lime.mall.model.vo.OrderDetailVo;
import cn.lime.mall.model.vo.OrderProductSkuVo;
import cn.lime.mall.model.vo.discount.DiscountVo;
import cn.lime.mall.model.vo.discount.ProductTitleVo;
import cn.lime.mall.service.db.DiscountAvailableProductService;
import cn.lime.mall.service.db.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.Discount;
import cn.lime.mall.service.db.DiscountService;
import cn.lime.mall.mapper.DiscountMapper;
import jakarta.annotation.Resource;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author riang
 * @description 针对表【Discount(折扣表)】的数据库操作Service实现
 * @createDate 2024-10-05 19:53:53
 */
@Service
public class DiscountServiceImpl extends ServiceImpl<DiscountMapper, Discount>
        implements DiscountService {
    @Resource
    private SnowFlakeGenerator ids;
    @Resource
    private DiscountAvailableProductService availableProductService;
    @Resource
    private OrderService orderService;

    @Override
    @Transactional
    public DiscountVo addDiscount(Integer type, Long ownerId, Integer minPrice, Integer discountPrice, List<Long> productIds) {
        Discount discount = new Discount();
        discount.setId(ids.nextId());
        discount.setType(type);
        discount.setOwnerId(ownerId);
        discount.setMinPrice(minPrice);
        discount.setDiscountPrice(discountPrice);
        ThrowUtils.throwIf(!save(discount), ErrorCode.INSERT_ERROR,"新增折扣项异常");
        List<DiscountAvailableProduct> availableProducts = new LinkedList<>();
        for (Long productId : productIds) {
            DiscountAvailableProduct bean = new DiscountAvailableProduct();
            bean.setDiscountId(discount.getId());
            bean.setProductId(productId);
            availableProducts.add(bean);
        }
        ThrowUtils.throwIf(!availableProductService.saveBatch(availableProducts),
                ErrorCode.INSERT_ERROR,"新增折扣券可用商品异常");
        return getVoById(discount.getId());
    }

    @Override
    public DiscountVo getVoById(Long id) {
        return baseMapper.getDetail(id);
    }

    @Override
    public void deleteDiscount(Long id) {
        ThrowUtils.throwIf(!lambdaUpdate().eq(Discount::getId,id).remove(),ErrorCode.DELETE_ERROR);
    }

    @Override
    public PageResult<DiscountVo> pageDiscount(Integer type, Long ownerId, Integer discountPriceRangeStart, Integer discountPriceEnd,
                                               List<Long> productId,Integer isAvailable, Integer current, Integer pageSize) {
        Page<?> page = PageUtils.build(current,pageSize,null,null);
        Page<Long> discountIds = baseMapper.getPage(type,ownerId,discountPriceRangeStart,discountPriceEnd,productId,isAvailable,page);
        if (CollectionUtils.isEmpty(discountIds.getRecords())){
            return new PageResult<>(discountIds, Collections.emptyList());
        }
        List<DiscountVo> list = new LinkedList<>();
        for (Long discountId : discountIds.getRecords()) {
            list.add(getVoById(discountId));
        }
        return new PageResult<>(discountIds,list);
    }

    @Override
    public Integer useDiscount(Long id,Long orderId) {
        DiscountVo discount = getVoById(id);
        ThrowUtils.throwIf(!ObjectUtils.isEmpty(discount),ErrorCode.NOT_FOUND_ERROR,"无该折扣项");
        ThrowUtils.throwIf(discount.getIsAvailable().equals(YesNoEnum.NO.getVal()),ErrorCode.PARAMS_ERROR,"该折扣项不可用");
        // 查订单信息 找商品信息
        OrderDetailVo orderDetailVo = orderService.getOrderDetail(orderId);
        // 最低使用价的判断
        ThrowUtils.throwIf(discount.getMinPrice()>orderDetailVo.getRealOrderPrice(),
                ErrorCode.SYSTEM_ERROR, "订单价格未达到折扣使用最低价");
        // 用户使用者的判断
        if (ObjectUtils.isNotEmpty(discount.getOwnerId())){
            ThrowUtils.throwIf(!discount.getOwnerId().equals(ReqThreadLocal.getInfo().getUserId()) &&
                            ReqThreadLocal.getInfo().getAuthLevel()< AuthLevel.ADMIN.getVal()
                    ,ErrorCode.AUTH_FAIL,"您无权使用该折扣券");
        }
        // 获取订单的所有商品ID
        List<Long> orderProducts = orderDetailVo.getOrderSkuList().stream().map(OrderProductSkuVo::getProductId).toList();
        ThrowUtils.throwIf(CollectionUtils.isEmpty(discount.getAvailableProductList()),ErrorCode.SYSTEM_ERROR,"该折扣享无可用商品");
        // 判断订单中的商品是否全部包含在折扣券可买商品内
        if (!new HashSet<>(discount.getAvailableProductList().stream().map(ProductTitleVo::getProductId).toList()).containsAll(orderProducts)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"订单中存在不可使用该优惠券的商品");
        }
        ThrowUtils.throwIf(!lambdaUpdate().eq(Discount::getId,id).set(Discount::getIsAvailable,YesNoEnum.NO.getVal()).update(),
                ErrorCode.UPDATE_ERROR,"更新折扣状态异常");
        return discount.getDiscountPrice();
    }

    @Override
    @Transactional
    public DiscountVo updateNewUserDiscount(Integer minPrice, Integer discountPrice, List<Long> productIds) {
        Optional<Discount> discountOpt = lambdaQuery().eq(Discount::getType, DiscountTypeEnum.NEW_USER_DISCOUNT.getVal()).oneOpt();
        if (discountOpt.isPresent()){
            ThrowUtils.throwIf(!lambdaUpdate().eq(Discount::getType,DiscountTypeEnum.NEW_USER_DISCOUNT.getVal())
                    .set(Discount::getMinPrice,minPrice)
                    .set(Discount::getDiscountPrice,discountPrice).update(), ErrorCode.UPDATE_ERROR);
            return getVoById(discountOpt.get().getId());

        }else {
            return addDiscount(DiscountTypeEnum.NEW_USER_DISCOUNT.getVal(),null,minPrice,discountPrice,productIds);
        }
    }

}




