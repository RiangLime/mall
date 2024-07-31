package cn.lime.mall.service.db;

import cn.lime.mall.model.bean.SkuInfo;
import cn.lime.mall.model.entity.Sku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author riang
 * @description 针对表【Sku】的数据库操作Service
 * @createDate 2024-07-31 15:03:53
 */
public interface SkuService extends IService<Sku> {
    Sku addSku(SkuInfo skuInfo, Long productId);
}
