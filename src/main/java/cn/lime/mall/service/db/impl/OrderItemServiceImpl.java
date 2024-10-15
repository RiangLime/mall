package cn.lime.mall.service.db.impl;

import cn.lime.core.constant.YesNoEnum;
import cn.lime.mall.model.entity.Product;
import cn.lime.mall.model.entity.Sku;
import cn.lime.mall.model.entity.Skuattribute;
import cn.lime.mall.model.vo.OrderProductSkuVo;
import cn.lime.mall.model.vo.SkuAttributeVo;
import cn.lime.mall.service.db.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.mall.model.entity.OrderItem;
import cn.lime.mall.mapper.OrderItemMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
* @author riang
* @description 针对表【Order_Item(订单物品表)】的数据库操作Service实现
* @createDate 2024-08-02 15:59:55
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{
    @Resource
    private ProductService productService;
    @Resource
    private ProductUrlService productUrlService;
    @Resource
    private SkuService skuService;
    @Resource
    private SkuattributeService skuattributeService;
    @Override
    public List<OrderProductSkuVo> getItemsByOrderId(Long orderId) {
        List<OrderItem> orderItems = lambdaQuery().eq(OrderItem::getOrderId,orderId).list();
        List<OrderProductSkuVo> vos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderProductSkuVo singleVo = new OrderProductSkuVo();
            // 获取product信息
            Product product = productService.getById(orderItem.getProductId());
            if (ObjectUtils.isEmpty(product)){
                singleVo.setProductIsExist(YesNoEnum.NO.getVal());
                singleVo.setProductName(orderItem.getProductName());
                singleVo.setProductMainUrl(orderItem.getProductMainPic());
            }else {
                singleVo.setProductIsExist(YesNoEnum.YES.getVal());
                String mainUrl = productUrlService.getMainPic(product.getProductId());
                singleVo.fillProductInfo(product,mainUrl);
                // 获取sku信息
                singleVo.setSkuId(orderItem.getSkuId());
                singleVo.setSkuPrice(orderItem.getItemPrice()/orderItem.getNumber());
                singleVo.setBuyNumber(orderItem.getNumber());
                Optional<Sku> sku = skuService.lambdaQuery().eq(Sku::getSkuId,orderItem.getSkuId()).oneOpt();
                singleVo.setSkuIsExist(sku.isPresent()?YesNoEnum.YES.getVal():YesNoEnum.NO.getVal());
                if (sku.isPresent()){
                    singleVo.setSkuCode(sku.get().getSkuCode());
                    // 获取attribute
                    List<SkuAttributeVo> skuAttributeVos = skuattributeService.getSkuAttributeVos(orderItem.getSkuId());
                    singleVo.setSkuAttributes(skuAttributeVos);
                }else {
                    List<SkuAttributeVo> skuAttributeVos = JSON.parseArray(orderItem.getSkuAttribute(),SkuAttributeVo.class);
                    singleVo.setSkuAttributes(skuAttributeVos);
                }
                vos.add(singleVo);
            }
        }
        return vos;
    }
}




