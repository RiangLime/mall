package cn.lime.mall.service.db;

import cn.lime.mall.model.entity.Sku;
import cn.lime.mall.model.entity.Skuattribute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author riang
* @description 针对表【SkuAttribute】的数据库操作Service
* @createDate 2024-07-31 15:03:53
*/
public interface SkuattributeService extends IService<Skuattribute> {
    boolean addAttributes(Sku sku, Map<String,String> attributeMap);
}
