package cn.lime.mall.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.mall.model.vo.CartVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.Cart;
import cn.lime.mall.service.db.CartService;
import cn.lime.mall.mapper.CartMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author riang
 * @description 针对表【Cart】的数据库操作Service实现
 * @createDate 2024-07-31 15:03:53
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
        implements CartService {
    @Resource
    private SnowFlakeGenerator ids;
    @Override
    public boolean addCart(Long userId, Long proId, Long skuId, Integer number) {
        // 判断是否已经存在
        Optional<Cart> cartOptional = lambdaQuery().eq(Cart::getUserId,userId).eq(Cart::getProductId,proId)
                .eq(Cart::getSkuId,skuId).oneOpt();
        if (cartOptional.isPresent()){
            Cart cart = cartOptional.get();
            cart.setNumber(cart.getNumber()+number);
            ThrowUtils.throwIf(!updateById(cart),ErrorCode.UPDATE_ERROR);
        }else {
            Cart cart = new Cart();
            cart.setId(ids.nextId());
            cart.setUserId(userId);
            cart.setProductId(proId);
            cart.setSkuId(skuId);
            cart.setNumber(number);
            ThrowUtils.throwIf(!save(cart),ErrorCode.INSERT_ERROR);
        }
        return true;
    }

    @Override
    public boolean deleteCart(Long userId, Long proId, Long skuId, Integer number) {
        Optional<Cart> optionalCart = lambdaQuery().eq(Cart::getUserId,userId).eq(Cart::getProductId,proId).eq(Cart::getSkuId,skuId).oneOpt();
        ThrowUtils.throwIf(optionalCart.isEmpty(), ErrorCode.PARAMS_ERROR,"购物车无该项");
        if (number>=optionalCart.get().getNumber()){
            return lambdaUpdate().eq(Cart::getId,optionalCart.get().getId()).remove();
        }else {
            return lambdaUpdate().eq(Cart::getId,optionalCart.get().getId()).set(Cart::getNumber,optionalCart.get().getNumber()-number).update();
        }

    }

    @Override
    public void updateCartNumber(Long userId,Long proId,Long skuId, Integer number) {
        Optional<Cart> optionalCart = lambdaQuery().eq(Cart::getUserId,userId).eq(Cart::getProductId,proId).eq(Cart::getSkuId,skuId).oneOpt();
        // 空就新增
        if (optionalCart.isEmpty()){
            ThrowUtils.throwIf(number==0,ErrorCode.INSERT_ERROR,"购物车无该项,新增number不可为0");
            addCart(userId,proId,skuId,number);
        }
        else {
            // number是0就删除
            if (number == 0) {
                ThrowUtils.throwIf(!lambdaUpdate().eq(Cart::getId,optionalCart.get().getId()).remove(),ErrorCode.DELETE_ERROR);
            }else {
                Cart bean = optionalCart.get();
                bean.setNumber(number);
                ThrowUtils.throwIf(!updateById(bean),ErrorCode.UPDATE_ERROR);
            }
        }
    }

    @Override
    public List<CartVo> listUserCart(Long userId) {
        return baseMapper.listUserCart(userId);
    }
}




