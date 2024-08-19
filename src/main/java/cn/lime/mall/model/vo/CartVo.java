package cn.lime.mall.model.vo;

import cn.lime.mall.model.entity.Cart;
import cn.lime.mall.model.entity.ProductTag;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: CartVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/19 16:31
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartVo implements Serializable {

    Long cartId;
    String productCode;
    String productName;
    String productMainUrl;
    String skuCode;
    String skuDescription;
    Integer skuPrice;
    List<SkuAttributeVo> attributeVos;
    Integer number;




}
