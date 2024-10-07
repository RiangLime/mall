package cn.lime.mall.service.db.impl;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.mall.model.entity.Sku;
import cn.lime.mall.model.vo.SkuAttributeVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.Skuattribute;
import cn.lime.mall.service.db.SkuattributeService;
import cn.lime.mall.mapper.SkuattributeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* @author riang
* @description 针对表【SkuAttribute】的数据库操作Service实现
* @createDate 2024-07-31 15:03:53
*/
@Service
public class SkuattributeServiceImpl extends ServiceImpl<SkuattributeMapper, Skuattribute>
    implements SkuattributeService{
    @Resource
    private SnowFlakeGenerator ids;

    @Override
    @Transactional
    public boolean addAttributes(Sku sku, Map<String, String> attributeMap) {
        List<Skuattribute> beans = new ArrayList<>();
        for (Map.Entry<String,String> entry : attributeMap.entrySet()){
            Skuattribute skuattribute = new Skuattribute();
            skuattribute.setId(ids.nextId());
            skuattribute.setSkuId(sku.getSkuId());
            skuattribute.setAttributeName(entry.getKey());
            skuattribute.setAttributeValue(entry.getValue());
            beans.add(skuattribute);
        }
        return saveBatch(beans);
    }

    @Override
    public List<SkuAttributeVo> getSkuAttributeVos(Long skuId) {
        if (!lambdaQuery().eq(Skuattribute::getSkuId,skuId).exists()){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"无该SKU");
        }
        return lambdaQuery()
                .eq(Skuattribute::getSkuId,skuId)
                .list()
                .stream()
                .map(SkuAttributeVo::fromBean)
                .toList();
    }
}




