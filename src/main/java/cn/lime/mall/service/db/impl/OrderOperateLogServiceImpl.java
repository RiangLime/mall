package cn.lime.mall.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.mall.mapper.AddressMapper;
import cn.lime.mall.mapper.OrderOperateLogMapper;
import cn.lime.mall.model.entity.Address;
import cn.lime.mall.model.entity.OrderOperateLog;
import cn.lime.mall.service.db.AddressService;
import cn.lime.mall.service.db.OrderOperateLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @ClassName: OrderOperateLogServiceImpl
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 15:06
 */
@Service
public class OrderOperateLogServiceImpl extends ServiceImpl<OrderOperateLogMapper, OrderOperateLog>
        implements OrderOperateLogService {
    @Resource
    private SnowFlakeGenerator ids;
    @Override
    public void log(Long orderId, Long userId, String operation) {
        OrderOperateLog bean = new OrderOperateLog();
        bean.setId(ids.nextId());
        bean.setOrderId(orderId);
        bean.setUserId(userId);
        bean.setOperate(operation);
        ThrowUtils.throwIf(!save(bean), ErrorCode.INSERT_ERROR);
    }
}
