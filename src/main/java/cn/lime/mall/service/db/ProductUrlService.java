package cn.lime.mall.service.db;

import cn.lime.mall.model.entity.ProductUrl;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author riang
* @description 针对表【Product_Url】的数据库操作Service
* @createDate 2024-07-31 15:03:53
*/
public interface ProductUrlService extends IService<ProductUrl> {
    boolean addMainPicUrl(Long productUrl,String url);
    boolean addRoundPicUrl(Long productUrl, List<String> urls);
    boolean delUrl(Long urlId);
    boolean delRoundUrl(Long productId);
}
