package cn.lime.mall.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.ProductViewLog;
import cn.lime.mall.service.db.ProductViewLogService;
import cn.lime.mall.mapper.ProductViewLogMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author riang
* @description 针对表【Product_View_Log】的数据库操作Service实现
* @createDate 2024-08-01 15:14:26
*/
@Service
public class ProductViewLogServiceImpl extends ServiceImpl<ProductViewLogMapper, ProductViewLog>
    implements ProductViewLogService{
    @Resource
    private SnowFlakeGenerator ids;
    @Override
    public void append(Long userId, Long productId) {
        ProductViewLog bean = new ProductViewLog();
        bean.setProductId(productId);
        bean.setUserId(userId);
        bean.setId(ids.nextId());
        ThrowUtils.throwIf(!save(bean), ErrorCode.INSERT_ERROR);
    }
}




