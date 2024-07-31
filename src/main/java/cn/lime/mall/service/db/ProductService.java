package cn.lime.mall.service.db;

import cn.lime.mall.model.bean.SkuInfo;
import cn.lime.mall.model.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author riang
* @description 针对表【Product】的数据库操作Service
* @createDate 2024-07-31 15:03:53
*/
public interface ProductService extends IService<Product> {
    boolean addProduct(String productCode, String productName, String productDescription, String realVirtualType,
                       String detectNormalType, String mainPicUrl, List<String> roundUrls, List<SkuInfo> skuInfos,
                       List<Long> productTagIds);
    boolean deleteProduct(Long productId);
    boolean updateProductState(Long productId,Integer state);
}
