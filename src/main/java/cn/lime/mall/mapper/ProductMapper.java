package cn.lime.mall.mapper;

import cn.lime.core.common.PageResult;
import cn.lime.mall.model.entity.Product;
import cn.lime.mall.model.entity.ProductUrl;
import cn.lime.mall.model.vo.ProductDetailVo;
import cn.lime.mall.model.vo.ProductPageVo;
import cn.lime.mall.model.vo.ProductSpecificationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author riang
 * @description 针对表【Product】的数据库操作Mapper
 * @createDate 2024-07-31 15:11:27
 * @Entity cn.lime.mall.model.entity.Product
 */
public interface ProductMapper extends BaseMapper<Product> {
    Page<ProductPageVo> pageProduct(String productName, List<Long> tagIds, String productType, Integer state, Page<?> page);

    ProductDetailVo getProductBasicDetail(Long productId,Integer state);

    List<ProductSpecificationInfo> getProductSpecification(Long productId);

    @Select("select * from Product_Url where product_id=#{productId}")
    List<ProductUrl> getProductUrls(Long productId);
}




