package cn.lime.mall.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.mall.model.bean.SkuInfo;
import cn.lime.mall.model.entity.Sku;
import cn.lime.mall.service.db.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.Product;
import cn.lime.mall.mapper.ProductMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author riang
* @description 针对表【Product】的数据库操作Service实现
* @createDate 2024-07-31 15:03:53
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{
    @Resource
    private SnowFlakeGenerator ids;
    @Resource
    private ProductUrlService productUrlService;
    @Resource
    private ProductHaveTagService productHaveTagService;
    @Resource
    private SkuService skuService;
    @Resource
    private SkuattributeService skuattributeService;
    @Override
    @Transactional
    public boolean addProduct(String productCode, String productName, String productDescription, String realVirtualType,
                              String detectNormalType, String mainPicUrl, List<String> roundUrls, List<SkuInfo> skuInfos,
                              List<Long> productTagIds) {
        // 新增商品
        Product product = new Product();
        product.setProductId(ids.nextId());
        product.setProductCode(productCode);
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductType1(realVirtualType);
        product.setProductType2(detectNormalType);
        ThrowUtils.throwIf(!save(product), ErrorCode.INSERT_ERROR,"新增商品信息失败");
        // 新增商品图片
        boolean res = productUrlService.addMainPicUrl(product.getProductId(),mainPicUrl);
        res &= productUrlService.addRoundPicUrl(product.getProductId(), roundUrls);
        ThrowUtils.throwIf(!res,ErrorCode.INSERT_ERROR,"新增商品URL失败");
        // 新增商品类别
        res = productHaveTagService.addRelation(product.getProductId(),productTagIds);
        ThrowUtils.throwIf(!res,ErrorCode.INSERT_ERROR,"新增商品分类失败");
        // 新增SKU
        for (SkuInfo skuInfo : skuInfos) {
            Sku sku = skuService.addSku(skuInfo,product.getProductId());
            // 添加属性
            res = skuattributeService.addAttributes(sku,skuInfo.getAttributes());
            ThrowUtils.throwIf(!res,ErrorCode.INSERT_ERROR,"新增商品库存单元失败");
        }
        return true;
    }

    @Override
    public boolean deleteProduct(Long productId) {
        return lambdaUpdate().eq(Product::getProductId,productId).remove();
    }

    @Override
    public boolean updateProductState(Long productId, Integer state) {
        return lambdaUpdate().eq(Product::getProductId,productId).set(Product::getProductState,state).update();
    }
}




