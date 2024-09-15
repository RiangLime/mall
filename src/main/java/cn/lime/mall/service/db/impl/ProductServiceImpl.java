package cn.lime.mall.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.PageResult;
import cn.lime.core.common.PageUtils;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.YesNoEnum;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.core.threadlocal.ReqThreadLocal;
import cn.lime.mall.constant.ProductUrlType;
import cn.lime.mall.model.bean.SkuInfo;
import cn.lime.mall.model.entity.ProductHaveTag;
import cn.lime.mall.model.entity.ProductUrl;
import cn.lime.mall.model.entity.Sku;
import cn.lime.mall.model.vo.ProductDetailVo;
import cn.lime.mall.model.vo.ProductPageVo;
import cn.lime.mall.service.db.*;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.Product;
import cn.lime.mall.mapper.ProductMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        implements ProductService {
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
                              String detectNormalType, Integer isVisible, String mainPicUrl, List<String> roundUrls, String productBrand,
                              List<SkuInfo> skuInfos, List<Long> productTagIds,Integer productState,String productSubTitle) {
        // 新增商品
        Product product = new Product();
        product.setProductId(ids.nextId());
        product.setProductCode(productCode);
        product.setProductName(productName);
        product.setVisible(isVisible);
        product.setReserveStrA(productBrand);
        product.setProductDescription(productDescription);
        product.setProductType1(realVirtualType);
        product.setProductType2(detectNormalType);
        product.setProductState(productState);
        product.setReserveStrB(productSubTitle);
        ThrowUtils.throwIf(!save(product), ErrorCode.INSERT_ERROR, "新增商品信息失败");
        // 新增商品图片
        boolean res = productUrlService.addMainPicUrl(product.getProductId(), mainPicUrl);
        res &= productUrlService.addRoundPicUrl(product.getProductId(), roundUrls);
        ThrowUtils.throwIf(!res, ErrorCode.INSERT_ERROR, "新增商品URL失败");
        // 新增商品类别
        res = productHaveTagService.addRelation(product.getProductId(), productTagIds);
        ThrowUtils.throwIf(!res, ErrorCode.INSERT_ERROR, "新增商品分类失败");
        // 新增SKU
        for (SkuInfo skuInfo : skuInfos) {
            Sku sku = skuService.addSku(skuInfo, product.getProductId());
            // 添加属性
            res = skuattributeService.addAttributes(sku, skuInfo.getAttributes());
            ThrowUtils.throwIf(!res, ErrorCode.INSERT_ERROR, "新增商品库存单元失败");
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateProduct(Long productId, String productCode, String productName, String productDescription,
                                 String realVirtualType, String detectNormalType, Integer isVisible,
                                 String mainPicUrl, List<String> roundUrls, String brand, List<SkuInfo> skuInfos,
                                 List<Long> productTagIds,Integer productState,String productSubTitle) {
        boolean res = true;
        LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Product::getProductId, productId);
        if (StringUtils.isNotEmpty(productCode) || StringUtils.isNotEmpty(productName) ||
                StringUtils.isNotEmpty(productDescription) || StringUtils.isNotEmpty(realVirtualType) ||
                StringUtils.isNotEmpty(detectNormalType) || ObjectUtils.isNotEmpty(isVisible) || StringUtils.isNotEmpty(brand)) {
            if (StringUtils.isNotEmpty(productCode)) wrapper.set(Product::getProductCode, productCode);
            if (StringUtils.isNotEmpty(productName)) wrapper.set(Product::getProductName, productName);
            if (StringUtils.isNotEmpty(productDescription))
                wrapper.set(Product::getProductDescription, productDescription);
            if (StringUtils.isNotEmpty(realVirtualType)) wrapper.set(Product::getProductType1, realVirtualType);
            if (StringUtils.isNotEmpty(detectNormalType)) wrapper.set(Product::getProductType2, detectNormalType);
            if (ObjectUtils.isNotEmpty(isVisible)) wrapper.set(Product::getVisible, isVisible);
            if (StringUtils.isNotEmpty(brand)) wrapper.set(Product::getReserveStrA, brand);
            if (ObjectUtils.isNotEmpty(productState)) wrapper.set(Product::getProductState,productState);
            if (StringUtils.isNotEmpty(productSubTitle)) wrapper.set(Product::getReserveStrB,productSubTitle);
            res = update(wrapper);
        }
        ThrowUtils.throwIf(!res, ErrorCode.UPDATE_ERROR, "更新产品信息异常");

        // sku
        if (!CollectionUtils.isEmpty(skuInfos)) {
            res &= reformatSkus(productId, skuInfos);
        }
        ThrowUtils.throwIf(!res, ErrorCode.UPDATE_ERROR, "更新产品SKU信息异常");

        // tag
        if (CollectionUtils.isEmpty(productTagIds)) {
            productHaveTagService.reformRelation(productId, productTagIds);
        }

        // 图片类
        if (StringUtils.isNotEmpty(mainPicUrl) || !CollectionUtils.isEmpty(roundUrls)) {
            if (StringUtils.isNotEmpty(mainPicUrl)) {
                res &= productUrlService.addMainPicUrl(productId, mainPicUrl);
            }
            if (!CollectionUtils.isEmpty(roundUrls)) {
                res &= productUrlService.delRoundUrl(productId);
                res &= productUrlService.addRoundPicUrl(productId, roundUrls);
            }
        }
        ThrowUtils.throwIf(!res, ErrorCode.UPDATE_ERROR, "更新产品图片信息异常");
        return true;
    }

    @Override
    public boolean deleteProduct(Long productId) {
        return lambdaUpdate().eq(Product::getProductId, productId).remove();
    }

    @Override
    @Transactional
    public boolean deleteProducts(List<Long> productIds) {
        return removeBatchByIds(productIds);
    }

    @Override
    public boolean updateProductState(Long productId, Integer state) {
        return lambdaUpdate().eq(Product::getProductId, productId).set(Product::getProductState, state).update();
    }

    @Override
    public boolean stateUpProducts(List<Long> productIds) {
        return lambdaUpdate().in(Product::getProductId, productIds)
                .set(Product::getProductState, YesNoEnum.YES.getVal()).update();
    }

    @Override
    public boolean stateDownProducts(List<Long> productIds) {
        return lambdaUpdate().in(Product::getProductId, productIds)
                .set(Product::getProductState, YesNoEnum.NO.getVal()).update();
    }

    @Override
    public PageResult<ProductPageVo> getProductPage(String productName, List<Long> tagIds, String productType, Integer productState,
                                                    Integer current, Integer pageSize, String sortField, String sortOrder) {
        Page<?> page = PageUtils.build(current, pageSize, sortField, sortOrder);
        Page<ProductPageVo> vos = baseMapper.pageProduct(productName, tagIds, productType, productState, page);
        return new PageResult<>(vos);
    }

    @Override
    @Transactional
    public ProductDetailVo getProductDetail(Long productId, Integer state) {
        // 基本信息
        ProductDetailVo vo = baseMapper.getProductBasicDetail(productId, state);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(vo), ErrorCode.NOT_FOUND_ERROR);
        // 规格信息
        vo.setSpecificationInfos(baseMapper.getProductSpecification(productId));
        // URL信息
        List<ProductUrl> urls = productUrlService.lambdaQuery().eq(ProductUrl::getProductId, productId).list();
        Map<Integer, List<ProductUrl>> urlMap = urls.stream().collect(Collectors.groupingBy(ProductUrl::getUrlType));
        if (urlMap.containsKey(ProductUrlType.MAIN.getVal())) {
            vo.setMainUrl(urlMap.get(1).get(0).getUrl());
        }
        if (urlMap.containsKey(ProductUrlType.ROUND.getVal())) {
            vo.setRoundUrls(urlMap.get(2).stream().map(ProductUrl::getUrl).collect(Collectors.toList()));
        }
        // sku 信息
        vo.setSkuInfos(skuService.getProductSkuInfos(productId));
        // 留痕
        logService.append(ReqThreadLocal.getInfo().getUserId(), productId);
        return vo;
    }

    @Override
    public boolean deleteSkus(Long productId) {
        return skuService.deleteProductSkus(productId);
    }

    @Override
    public boolean reformatSkus(Long productId, List<SkuInfo> skuInfos) {
        Product product = getById(productId);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(product), ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(!deleteSkus(productId),ErrorCode.DELETE_ERROR,"删除已有SKU失败");
        boolean res = true;
        // 新增SKU
        for (SkuInfo skuInfo : skuInfos) {
            Sku sku = skuService.addSku(skuInfo, product.getProductId());
            // 添加属性
            res = skuattributeService.addAttributes(sku, skuInfo.getAttributes());
            ThrowUtils.throwIf(!res, ErrorCode.INSERT_ERROR, "新增商品库存单元失败");
        }
        return true;
    }

    @Override
    public boolean addProductTag(Long productId, Long tagId) {
        ProductHaveTag bean = new ProductHaveTag();
        bean.setId(ids.nextId());
        bean.setProductId(productId);
        bean.setTagId(tagId);
        return productHaveTagService.save(bean);
    }

    @Override
    public boolean removeProductTag(Long productId, Long tagId) {
        return productHaveTagService.lambdaUpdate()
                .eq(ProductHaveTag::getProductId, productId)
                .eq(ProductHaveTag::getTagId, tagId).remove();
    }
}




