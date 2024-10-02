package cn.lime.mall.service.db;

import cn.lime.core.common.PageResult;
import cn.lime.mall.model.dto.cart.CartAddBatchDto;
import cn.lime.mall.model.dto.cart.CartDto;
import cn.lime.mall.model.entity.Cart;
import cn.lime.mall.model.vo.CartVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author riang
* @description 针对表【Cart】的数据库操作Service
* @createDate 2024-07-31 15:03:53
*/
public interface CartService extends IService<Cart> {
    boolean addCart(Long userId,Long proId,Long skuId,Integer number);
    boolean addCartBatch(Long userId, List<CartDto> dtoList);
    boolean deleteCart(Long userId,Long proId,Long skuId,Integer number);
    void batchDelete(Long userId, List<Long> cartIds);
    void updateCartNumber(Long userId,Long proId,Long skuId,Integer number);
    List<CartVo> listUserCart(Long userId);
}
