package cn.lime.mall.mapper;

import cn.lime.core.module.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author riang
 * @description 针对表【Order(订单表)】的数据库操作Mapper
 * @createDate 2024-03-15 14:29:53
 * @Entity cn.queertech.applyeasybackend.base.module.entity.Order
 */
public interface OrderMapper extends BaseMapper<Order> {
    Integer updateTimeoutWaitingOrder(Integer timeout);
}