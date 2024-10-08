package cn.lime.mall.service.db;

import cn.lime.mall.model.entity.OrderItem;
import cn.lime.mall.model.vo.OrderProductSkuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author riang
* @description 针对表【Order_Item(订单物品表)】的数据库操作Service
* @createDate 2024-08-02 15:59:55
*/
public interface OrderItemService extends IService<OrderItem> {
    List<OrderProductSkuVo> getItemsByOrderId(Long orderId);
}
