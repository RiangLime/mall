package cn.lime.mall.service.db;

import cn.lime.mall.model.entity.ProductHaveTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author riang
* @description 针对表【Product_Have_Tag】的数据库操作Service
* @createDate 2024-07-31 15:03:53
*/
public interface ProductHaveTagService extends IService<ProductHaveTag> {
    boolean addRelation(Long productId, List<Long> tagIds);
    boolean delTag(Long productId,Long tagId);
}
