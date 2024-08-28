package cn.lime.mall.model.vo;

import cn.lime.mall.model.entity.Cart;
import cn.lime.mall.model.entity.ProductTag;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "购物车内容ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long cartId;
    @Schema(description = "商品ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;
    @Schema(description = "商品编码")
    private String productCode;
    @Schema(description = "商品名称")
    private String productName;
    @Schema(description = "商品主图")
    private String productMainUrl;
    @Schema(description = "skuID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuId;
    @Schema(description = "SKU编码")
    private String skuCode;
    @Schema(description = "SKU描述")
    private String skuDescription;
    @Schema(description = "SKU单价")
    private Integer skuPrice;
    @Schema(description = "SKU属性信息")
    private List<SkuAttributeVo> attributeVos;
    @Schema(description = "数量")
    private Integer number;




}
