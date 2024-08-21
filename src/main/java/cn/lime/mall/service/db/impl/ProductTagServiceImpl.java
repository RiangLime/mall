package cn.lime.mall.service.db.impl;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.ProductTag;
import cn.lime.mall.service.db.ProductTagService;
import cn.lime.mall.mapper.ProductTagMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @author riang
 * @description 针对表【Product_Tag】的数据库操作Service实现
 * @createDate 2024-07-31 15:03:53
 */
@Service
public class ProductTagServiceImpl extends ServiceImpl<ProductTagMapper, ProductTag>
        implements ProductTagService {
    @Resource
    private SnowFlakeGenerator ids;

    @Override
    public boolean addTag(Long parentTagId, String tagName, String tagUrl, Integer sort) {
        ProductTag tag = new ProductTag();
        tag.setTagId(ids.nextId());
        tag.setParentTagId(ObjectUtils.isEmpty(parentTagId) ? 0 : parentTagId);
        tag.setTagName(tagName);
        tag.setTagUrl(tagUrl);
        tag.setTagSort(sort);
        return save(tag);
    }

    @Override
    public boolean deleteTag(Long tagId) {
        return lambdaUpdate().eq(ProductTag::getTagId, tagId).remove();
    }

    @Override
    public boolean updateTag(Long tagId, Long parentId, String tag, String tagUrl, Integer sort, Integer state) {
        if (ObjectUtils.isEmpty(parentId) && ObjectUtils.isEmpty(tag) && ObjectUtils.isEmpty(tagUrl)
                && ObjectUtils.isEmpty(sort) && ObjectUtils.isEmpty(state)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"无更新参数");
        }
        LambdaUpdateWrapper<ProductTag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ProductTag::getTagId,tagId);
        if (ObjectUtils.isNotEmpty(parentId)) wrapper.set(ProductTag::getParentTagId,parentId);
        if (StringUtils.isNotEmpty(tag)) wrapper.set(ProductTag::getTagName,tag);
        if (StringUtils.isNotEmpty(tagUrl)) wrapper.set(ProductTag::getTagUrl,tagUrl);
        if (ObjectUtils.isNotEmpty(sort)) wrapper.set(ProductTag::getTagSort,sort);
        if (ObjectUtils.isNotEmpty(state)) wrapper.set(ProductTag::getTagState,state);
        return update(wrapper);
    }

    @Override
    public List<ProductTag> listTags() {
        return list().stream().sorted(new Comparator<ProductTag>() {
            @Override
            public int compare(ProductTag o1, ProductTag o2) {
                return o1.getTagSort()-o2.getTagSort();
            }
        }).toList();
    }
}




