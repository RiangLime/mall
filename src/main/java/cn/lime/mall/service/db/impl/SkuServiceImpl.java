package cn.lime.mall.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.mall.model.bean.SkuInfo;
import cn.lime.mall.model.vo.SkuInfoVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.Sku;
import cn.lime.mall.service.db.SkuService;
import cn.lime.mall.mapper.SkuMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author riang
 * @description 针对表【Sku】的数据库操作Service实现
 * @createDate 2024-07-31 15:03:53
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku>
        implements SkuService {
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

    @Override
    @Transactional
    public List<SkuInfoVo> getProductSkuInfos(Long productId) {
        List<SkuInfoVo> skuInfos = baseMapper.getBaseSkuInfo(productId);
        for (SkuInfoVo skuInfo : skuInfos) {
            skuInfo.setAttributes(baseMapper.getAttributesBySkuId(skuInfo.getSkuId()));
        }
        return skuInfos;
    }

    @Override
    public boolean updateSkuPriceStock(Long skuId, Integer price, Integer stock) {
        ThrowUtils.throwIf(ObjectUtils.isEmpty(price) && ObjectUtils.isNotEmpty(stock), ErrorCode.PARAMS_ERROR);
        LambdaUpdateWrapper<Sku> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Sku::getSkuId,skuId);
        if (ObjectUtils.isNotEmpty(price)) wrapper.set(Sku::getPrice,price);
        if (ObjectUtils.isNotEmpty(stock)) wrapper.set(Sku::getStock,stock);
        return update(wrapper);
    }

    @Override
    public boolean deleteProductSkus(Long productId) {
        return lambdaUpdate().eq(Sku::getProductId,productId).remove();
    }
}




