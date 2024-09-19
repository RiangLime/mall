package cn.lime.mall.service.db.impl;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.mall.model.vo.ProductLevelTagVo;
import cn.lime.mall.model.vo.ProductTagVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.ProductTag;
import cn.lime.mall.service.db.ProductTagService;
import cn.lime.mall.mapper.ProductTagMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (lambdaQuery().eq(ProductTag::getParentTagId, tagId).exists()) {
            ThrowUtils.throwIf(!lambdaUpdate().eq(ProductTag::getParentTagId, tagId).remove(),
                    ErrorCode.DELETE_ERROR, "删除子标签异常");
        }
        ThrowUtils.throwIf(!lambdaUpdate().eq(ProductTag::getTagId, tagId).remove(),
                ErrorCode.DELETE_ERROR, "删除标签异常");
        return true;
    }

    @Override
    public boolean updateTag(Long tagId, Long parentId, String tag, String tagUrl, Integer sort, Integer state) {
        if (ObjectUtils.isEmpty(parentId) && ObjectUtils.isEmpty(tag) && ObjectUtils.isEmpty(tagUrl)
                && ObjectUtils.isEmpty(sort) && ObjectUtils.isEmpty(state)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无更新参数");
        }
        LambdaUpdateWrapper<ProductTag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ProductTag::getTagId, tagId);
        if (ObjectUtils.isNotEmpty(parentId)) {
            ProductTag bean = getById(parentId);
            ThrowUtils.throwIf(ObjectUtils.isEmpty(bean),ErrorCode.NOT_FOUND_ERROR,"无该父标签");
            wrapper.set(ProductTag::getParentTagId, parentId);
        }
        if (StringUtils.isNotEmpty(tag)) wrapper.set(ProductTag::getTagName, tag);
        if (StringUtils.isNotEmpty(tagUrl)) wrapper.set(ProductTag::getTagUrl, tagUrl);
        if (ObjectUtils.isNotEmpty(sort)) wrapper.set(ProductTag::getTagSort, sort);
        if (ObjectUtils.isNotEmpty(state)) wrapper.set(ProductTag::getTagState, state);
        return update(wrapper);
    }

    @Override
    public List<ProductTagVo> listTags(Integer state) {
        if (ObjectUtils.isNotEmpty(state)) {
            return lambdaQuery().eq(ProductTag::getTagState, state).list().stream().sorted(new Comparator<ProductTag>() {
                @Override
                public int compare(ProductTag o1, ProductTag o2) {
                    return o1.getTagSort() - o2.getTagSort();
                }
            }).map(ProductTagVo::fromBean).toList();
        } else {
            return list().stream().sorted(new Comparator<ProductTag>() {
                @Override
                public int compare(ProductTag o1, ProductTag o2) {
                    return o1.getTagSort() - o2.getTagSort();
                }
            }).map(ProductTagVo::fromBean).toList();
        }
    }

    @Override
    @Transactional
    public List<ProductLevelTagVo> listLeveledTags(Integer state) {
        List<ProductLevelTagVo> topTags = listTopTags(state);
        topTags.forEach(tagTag -> tagTag.setChildren(listChildrenTags(state, tagTag.getTagId())));
        return topTags;
    }

    @Override
    @Transactional
    public List<ProductLevelTagVo> listChildrenTags(Integer state, Long parentId) {
        List<ProductLevelTagVo> children = null;
        if (ObjectUtils.isNotEmpty(state)) {
            children = lambdaQuery()
                    .eq(ProductTag::getTagState, state)
                    .eq(ProductTag::getParentTagId, parentId)
                    .list()
                    .stream()
                    .sorted(new Comparator<ProductTag>() {
                        @Override
                        public int compare(ProductTag o1, ProductTag o2) {
                            return o1.getTagSort() - o2.getTagSort();
                        }
                    })
                    .map(ProductLevelTagVo::fromBean)
                    .toList();
        } else {
            children = lambdaQuery()
                    .eq(ProductTag::getParentTagId, parentId)
                    .list()
                    .stream()
                    .sorted(new Comparator<ProductTag>() {
                        @Override
                        public int compare(ProductTag o1, ProductTag o2) {
                            return o1.getTagSort() - o2.getTagSort();
                        }
                    })
                    .map(ProductLevelTagVo::fromBean)
                    .toList();
        }
        return children;
    }

    @Override
    public List<ProductLevelTagVo> listTopTags(Integer state) {
        List<ProductLevelTagVo> top = null;
        if (ObjectUtils.isNotEmpty(state)) {
            top = lambdaQuery()
                    .eq(ProductTag::getTagState, state)
                    .eq(ProductTag::getParentTagId, 0L)
                    .list()
                    .stream()
                    .sorted(new Comparator<ProductTag>() {
                        @Override
                        public int compare(ProductTag o1, ProductTag o2) {
                            return o1.getTagSort() - o2.getTagSort();
                        }
                    })
                    .map(ProductLevelTagVo::fromBean)
                    .toList();
        } else {
            top = lambdaQuery()
                    .eq(ProductTag::getParentTagId, 0L)
                    .list()
                    .stream()
                    .sorted(new Comparator<ProductTag>() {
                        @Override
                        public int compare(ProductTag o1, ProductTag o2) {
                            return o1.getTagSort() - o2.getTagSort();
                        }
                    })
                    .map(ProductLevelTagVo::fromBean)
                    .toList();
        }
        return top;
    }

    @Override
    public List<ProductLevelTagVo> listAllChildrenTags(Integer state) {
        List<ProductLevelTagVo> top = null;
        if (ObjectUtils.isNotEmpty(state)) {
            top = lambdaQuery()
                    .eq(ProductTag::getTagState, state)
                    .ne(ProductTag::getParentTagId, 0L)
                    .list()
                    .stream()
                    .sorted(new Comparator<ProductTag>() {
                        @Override
                        public int compare(ProductTag o1, ProductTag o2) {
                            return o1.getTagSort() - o2.getTagSort();
                        }
                    })
                    .map(ProductLevelTagVo::fromBean)
                    .toList();
        } else {
            top = lambdaQuery()
                    .ne(ProductTag::getParentTagId, 0L)
                    .list()
                    .stream()
                    .sorted(new Comparator<ProductTag>() {
                        @Override
                        public int compare(ProductTag o1, ProductTag o2) {
                            return o1.getTagSort() - o2.getTagSort();
                        }
                    })
                    .map(ProductLevelTagVo::fromBean)
                    .toList();
        }
        return top;
    }
}




