package cn.lime.mall.service.db;

import cn.lime.mall.model.entity.OrderOperateLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: OrderOperateLogService
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 15:06
 */
public interface OrderOperateLogService extends IService<OrderOperateLog> {
    void log(Long productId,Long userId,String operation);
}
