package cn.lime.mall.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.PageResult;
import cn.lime.core.common.PageUtils;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.core.threadlocal.ReqThreadLocal;
import cn.lime.mall.constant.ProductUrlType;
import cn.lime.mall.model.bean.SkuInfo;
import cn.lime.mall.model.entity.ProductUrl;
import cn.lime.mall.model.entity.Sku;
import cn.lime.mall.model.vo.ProductDetailVo;
import cn.lime.mall.model.vo.ProductPageVo;
import cn.lime.mall.service.db.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.Product;
import cn.lime.mall.mapper.ProductMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Resource
    private ProductViewLogService logService;
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

    @Override
    public PageResult<ProductPageVo> getProductPage(String productName, List<Long> tagIds, String productType,Integer productState,
                                                    Integer current,Integer pageSize, String sortField,String sortOrder) {
        Page<?> page = PageUtils.build(current,pageSize,sortField,sortOrder);
        Page<ProductPageVo> vos = baseMapper.pageProduct(productName,tagIds,productType,page);
        return new PageResult<>(vos);
    }

    @Override
    @Transactional
    public ProductDetailVo getProductDetail(Long productId) {
        // 基本信息
        ProductDetailVo vo = baseMapper.getProductBasicDetail(productId);
        // 规格信息
        vo.setSpecificationInfos(baseMapper.getProductSpecification(productId));
        // URL信息
        List<ProductUrl> urls = baseMapper.getProductUrls(productId);
        Map<Integer,List<ProductUrl>> urlMap = urls.stream().collect(Collectors.groupingBy(ProductUrl::getUrlType));
        if (urlMap.containsKey(ProductUrlType.MAIN.getVal())){
            vo.setMainUrl(urlMap.get(1).get(0).getUrl());
        }
        if (urlMap.containsKey(ProductUrlType.ROUND.getVal())){
            vo.setRoundUrls(urlMap.get(2).stream().map(ProductUrl::getUrl).collect(Collectors.toList()));
        }
        // sku 信息
        vo.setSkuInfos(skuService.getProductSkuInfos(productId));
        // 留痕
        logService.append(ReqThreadLocal.getInfo().getUserId(),productId);
        return vo;
    }
}




