package cn.lime.mall.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.mall.model.bean.SkuInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.Sku;
import cn.lime.mall.service.db.SkuService;
import cn.lime.mall.mapper.SkuMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author riang
* @description 针对表【Sku】的数据库操作Service实现
* @createDate 2024-07-31 15:03:53
*/
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku>
    implements SkuService{
    @Resource
    private SnowFlakeGenerator ids;
    @Override
    public Sku addSku(SkuInfo skuInfo, Long productId) {
        Sku sku = new Sku();
        sku.setSkuId(ids.nextId());
        sku.setSkuCode(String.valueOf(skuInfo.getAttributes().hashCode()));
        sku.setPrice(sku.getPrice());
        sku.setStock(sku.getStock());
        ThrowUtils.throwIf(!save(sku), ErrorCode.INSERT_ERROR);
        return sku;
    }
}




