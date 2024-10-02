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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        sku.setSkuCode(String.valueOf(Math.abs(skuInfo.getAttributes().hashCode())));
        sku.setProductId(productId);
        sku.setPrice(skuInfo.getSkuPrice());
        sku.setStock(skuInfo.getSkuStock());
        sku.setSkuDescription(skuInfo.getSkuDescription());
        ThrowUtils.throwIf(!save(sku), ErrorCode.INSERT_ERROR);
        return sku;
    }

    @Override
    @Transactional
    public List<SkuInfoVo> getProductSkuInfos(Long productId) {
        List<SkuInfoVo> skuInfos = baseMapper.getBaseSkuInfo(productId);
        for (SkuInfoVo skuInfo : skuInfos) {
            List<Map<String,String>> listMap = baseMapper.getAttributesBySkuId(skuInfo.getSkuId());
            Map<String,String> attrMap = new HashMap<>();
            for (Map<String, String> stringStringMap : listMap) {
                attrMap.put(stringStringMap.get("attribute_name"),stringStringMap.get("attribute_value"));
            }
            skuInfo.setAttributes(attrMap);

        }
        return skuInfos;
    }

    @Override
    public boolean updateSkuPriceStock(Long skuId,String skuCode, Integer price, Integer stock,String skuDescription,String remark) {
        ThrowUtils.throwIf(ObjectUtils.isEmpty(price) && ObjectUtils.isNotEmpty(stock)
                && StringUtils.isEmpty(skuCode) && StringUtils.isEmpty(skuDescription)
                && StringUtils.isEmpty(remark), ErrorCode.PARAMS_ERROR);
        LambdaUpdateWrapper<Sku> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Sku::getSkuId,skuId);
        if (ObjectUtils.isNotEmpty(price)) wrapper.set(Sku::getPrice,price);
        if (ObjectUtils.isNotEmpty(stock)) wrapper.set(Sku::getStock,stock);
        if (StringUtils.isNotEmpty(skuCode)) wrapper.set(Sku::getSkuCode,skuCode);
        if (StringUtils.isNotEmpty(skuDescription)) wrapper.set(Sku::getSkuDescription,skuDescription);
        if (StringUtils.isNotEmpty(remark)) wrapper.set(Sku::getRemark,remark);
        return update(wrapper);
    }

    @Override
    public boolean deleteProductSkus(Long productId) {
        if (lambdaQuery().eq(Sku::getProductId,productId).exists()) {
            return lambdaUpdate().eq(Sku::getProductId, productId).remove();
        }else {
            return true;
        }
    }
}




