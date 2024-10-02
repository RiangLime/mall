package cn.lime.mall.service.db.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.mall.constant.ProductUrlType;
import cn.lime.mall.constant.ProductUrlTypeEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.ProductUrl;
import cn.lime.mall.service.db.ProductUrlService;
import cn.lime.mall.mapper.ProductUrlMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
* @author riang
* @description 针对表【Product_Url】的数据库操作Service实现
* @createDate 2024-07-31 15:03:53
*/
@Service
public class ProductUrlServiceImpl extends ServiceImpl<ProductUrlMapper, ProductUrl>
    implements ProductUrlService{
    @Resource
    private SnowFlakeGenerator ids;
    @Override
    @Transactional
    public boolean addMainPicUrl(Long productUrl, String url) {
        if (lambdaQuery().eq(ProductUrl::getProductId,productUrl)
                .eq(ProductUrl::getUrlType,ProductUrlType.MAIN.getVal()).exists()){
            ThrowUtils.throwIf(!lambdaUpdate().eq(ProductUrl::getProductId,productUrl)
                    .eq(ProductUrl::getUrlType,ProductUrlType.MAIN.getVal())
                    .remove(), ErrorCode.DELETE_ERROR,"删除已有主图失败");
        }
        ProductUrl bean = new ProductUrl();
        bean.setUrlId(ids.nextId());
        bean.setUrl(url);
        bean.setProductId(productUrl);
        bean.setUrlType(ProductUrlTypeEnum.MAIN.getVal());
        return save(bean);
    }

    @Override
    @Transactional
    public boolean addRoundPicUrl(Long productUrl, List<String> urls) {
        if (CollectionUtils.isEmpty(urls)) return true;
        List<ProductUrl> beans = new LinkedList<>();
        for (String url : urls) {
            ProductUrl bean = new ProductUrl();
            bean.setUrlId(ids.nextId());
            bean.setUrl(url);
            bean.setProductId(productUrl);
            bean.setUrlType(ProductUrlTypeEnum.ROUND.getVal());
            beans.add(bean);
        }
        return saveBatch(beans);
    }

    @Override
    public String getMainPic(Long productId) {
        Optional<ProductUrl> productUrl = lambdaQuery().eq(ProductUrl::getProductId,productId).eq(ProductUrl::getUrlType,ProductUrlTypeEnum.MAIN.getVal()).oneOpt();
        return productUrl.map(ProductUrl::getUrl).orElse(null);
    }

    @Override
    public boolean delUrl(Long urlId) {
        return lambdaUpdate().eq(ProductUrl::getUrlId,urlId).remove();
    }

    @Override
    public boolean delRoundUrl(Long productId) {
        return lambdaUpdate().eq(ProductUrl::getProductId,productId).eq(ProductUrl::getUrlType, ProductUrlType.ROUND.getVal()).remove();
    }
}




