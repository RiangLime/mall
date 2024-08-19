package cn.lime.mall.mapper;

import cn.lime.mall.model.entity.Sku;
import cn.lime.mall.model.vo.SkuInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author riang
* @description 针对表【Sku】的数据库操作Mapper
* @createDate 2024-07-31 15:11:27
* @Entity cn.lime.mall.model.entity.Sku
*/
public interface SkuMapper extends BaseMapper<Sku> {
    List<SkuInfoVo> getBaseSkuInfo(Long productId);
    @Select("SELECT attribute_name, attribute_value FROM SkuAttribute WHERE sku_id = #{skuId}")
    List<Map<String, String>> getAttributesBySkuId(Long skuId);
}




