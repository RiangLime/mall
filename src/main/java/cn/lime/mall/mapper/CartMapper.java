package cn.lime.mall.mapper;

import cn.lime.mall.model.entity.Cart;
import cn.lime.mall.model.vo.CartVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author riang
* @description 针对表【Cart】的数据库操作Mapper
* @createDate 2024-07-31 15:11:27
* @Entity cn.lime.mall.model.entity.Cart
*/
public interface CartMapper extends BaseMapper<Cart> {
    List<CartVo> listUserCart(Long userId);
}




