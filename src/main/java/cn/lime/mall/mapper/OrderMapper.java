package cn.lime.mall.mapper;

import cn.lime.mall.model.entity.Order;
import cn.lime.mall.model.vo.OrderDetailVo;
import cn.lime.mall.model.vo.OrderPageVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
* @author riang
* @description 针对表【Order(订单表)】的数据库操作Mapper
* @createDate 2024-08-02 14:01:40
* @Entity cn.lime.mall.model.entity.Order
*/
public interface OrderMapper extends BaseMapper<Order> {
    Integer updateTimeoutWaitingOrder(Integer orderTimeoutHour);

    Page<OrderPageVo> pageOrder(String orderCode, String userName, String productName,String receiverName,
                                Integer orderState,Long orderUserId, Long orderStartTime,Long orderEndTime,Page<?> page);

    OrderDetailVo getOrderDetail(Long orderId);
}




