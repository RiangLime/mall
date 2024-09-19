package cn.lime.mall.service.db;

import cn.lime.mall.model.entity.ProductTag;
import cn.lime.mall.model.vo.ProductLevelTagVo;
import cn.lime.mall.model.vo.ProductTagVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author riang
* @description 针对表【Product_Tag】的数据库操作Service
* @createDate 2024-07-31 15:03:53
*/
public interface ProductTagService extends IService<ProductTag> {
    boolean addTag(Long parentTagId,String tag,String tagUrl,Integer sort);
    boolean deleteTag(Long tagId);
    boolean updateTag(Long tagId,Long parentId,String tag,String tagUrl,Integer sort,Integer state);
    List<ProductTagVo> listTags(Integer state);
    List<ProductLevelTagVo> listLeveledTags(Integer state);
    List<ProductLevelTagVo> listChildrenTags(Integer state,Long parentId);
    List<ProductLevelTagVo> listTopTags(Integer state);
    List<ProductLevelTagVo> listAllChildrenTags(Integer state);


}
