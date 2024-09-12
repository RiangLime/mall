package cn.lime.mall.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.ProductHaveTag;
import cn.lime.mall.service.db.ProductHaveTagService;
import cn.lime.mall.mapper.ProductHaveTagMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author riang
 * @description 针对表【Product_Have_Tag】的数据库操作Service实现
 * @createDate 2024-07-31 15:03:53
 */
@Service
public class ProductHaveTagServiceImpl extends ServiceImpl<ProductHaveTagMapper, ProductHaveTag>
        implements ProductHaveTagService {
    @Resource
    private SnowFlakeGenerator ids;

    @Override
    @Transactional
    public boolean addRelation(Long productId, List<Long> tagIds) {
        if (CollectionUtils.isEmpty(tagIds)) {
            return true;
        }
        List<ProductHaveTag> relations = new ArrayList<>();
        for (Long tagId : tagIds) {
            ProductHaveTag bean = new ProductHaveTag();
            bean.setTagId(tagId);
            bean.setProductId(productId);
            bean.setId(ids.nextId());
            relations.add(bean);
        }
        return saveBatch(relations);
    }

    @Override
    public boolean delTag(Long productId, Long tagId) {
        return lambdaUpdate().eq(ProductHaveTag::getProductId, productId).eq(ProductHaveTag::getTagId, tagId).remove();
    }

    @Override
    @Transactional
    public void reformRelation(Long productId, List<Long> tagIds) {
        Set<Long> exist = lambdaQuery().eq(ProductHaveTag::getProductId, productId)
                .list()
                .stream()
                .map(ProductHaveTag::getTagId)
                .collect(Collectors.toSet());
        if (exist.equals(new HashSet<>(tagIds))){
            return;
        }
        ThrowUtils.throwIf(!lambdaUpdate().eq(ProductHaveTag::getProductId,productId).remove(), ErrorCode.DB_DATA_ERROR);
        ThrowUtils.throwIf(!addRelation(productId,tagIds),ErrorCode.UPDATE_ERROR);
    }
}




