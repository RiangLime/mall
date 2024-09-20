package cn.lime.mall.service.db.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.OrderItem;
import cn.lime.mall.service.db.OrderItemService;
import cn.lime.mall.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author riang
* @description 针对表【Order_Item(订单物品表)】的数据库操作Service实现
* @createDate 2024-08-02 15:59:55
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{
}




