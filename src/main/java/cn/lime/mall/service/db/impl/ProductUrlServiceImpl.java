package cn.lime.mall.service.db.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.lime.mall.constant.ProductUrlTypeEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.ProductUrl;
import cn.lime.mall.service.db.ProductUrlService;
import cn.lime.mall.mapper.ProductUrlMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
* @author riang
* @description 针对表【Product_Url】的数据库操作Service实现
* @createDate 2024-07-31 15:03:53
*/
@Service
public class ProductUrlServiceImpl extends ServiceImpl<ProductUrlMapper, ProductUrl>
    implements ProductUrlService{
    @Resource
    private SnowflakeGenerator ids;
    @Override
    public boolean addMainPicUrl(Long productUrl, String url) {
        ProductUrl bean = new ProductUrl();
        bean.setUrlId(ids.next());
        bean.setUrl(url);
        bean.setProductId(productUrl);
        bean.setUrlType(ProductUrlTypeEnum.MAIN.getVal());
        return save(bean);
    }

    @Override
    @Transactional
    public boolean addRoundPicUrl(Long productUrl, List<String> urls) {
        List<ProductUrl> beans = new LinkedList<>();
        for (String url : urls) {
            ProductUrl bean = new ProductUrl();
            bean.setUrlId(ids.next());
            bean.setUrl(url);
            bean.setProductId(productUrl);
            bean.setUrlType(ProductUrlTypeEnum.MAIN.getVal());
            beans.add(bean);
        }
        return saveBatch(beans);
    }

    @Override
    public boolean delUrl(Long urlId) {
        return lambdaUpdate().eq(ProductUrl::getUrlId,urlId).remove();
    }
}




