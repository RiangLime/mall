package cn.lime.mall.service.db;

import cn.lime.core.common.PageResult;
import cn.lime.mall.model.bean.SkuInfo;
import cn.lime.mall.model.entity.Product;
import cn.lime.mall.model.vo.ProductDetailVo;
import cn.lime.mall.model.vo.ProductMallHomePageVo;
import cn.lime.mall.model.vo.ProductPageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author riang
 * @description 针对表【Product】的数据库操作Service
 * @createDate 2024-07-31 15:03:53
 */
public interface ProductService extends IService<Product> {
    boolean addProduct(String productCode, String productName, String productDescription, String realVirtualType,
                       String detectNormalType, Integer isVisible, String mainPicUrl, List<String> roundUrls, String brand,
                       List<SkuInfo> skuInfos, List<Long> productTagIds, Integer productState,String productSubTitle);

    boolean updateProduct(Long productId, String productCode, String productName, String productDescription,
                          String realVirtualType, String detectNormalType, Integer isVisible, String mainPicUrl,
                          List<String> roundUrls, String brand, List<SkuInfo> skuInfos, List<Long> productTagIds,
                          Integer productState,String productSubTitle);

    boolean deleteSkus(Long productId);

    boolean reformatSkus(Long productId, List<SkuInfo> skuInfos);

    boolean deleteProduct(Long productId);

    boolean deleteProducts(List<Long> productIds);

    boolean updateProductState(Long productId, Integer state);

    boolean stateUpProducts(List<Long> productIds);

    boolean stateDownProducts(List<Long> productIds);

    PageResult<ProductPageVo> getProductPage(String productName, List<Long> tagIds, String productType, Integer productState,Integer visible,
                                             Integer current, Integer pageSize, String sortField, String sortOrder);

    ProductDetailVo getProductDetail(Long productId, Integer state);

    boolean addProductTag(Long productId, Long tagId);

    boolean removeProductTag(Long productId, Long tagId);
    ProductMallHomePageVo getMallHomePage();
}
