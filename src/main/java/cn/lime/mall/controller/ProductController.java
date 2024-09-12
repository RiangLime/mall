package cn.lime.mall.controller;

import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.BaseResponse;
import cn.lime.core.common.PageResult;
import cn.lime.core.common.ResultUtils;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.constant.YesNoEnum;
import cn.lime.mall.model.dto.product.ProductIdDto;
import cn.lime.mall.model.dto.product.ProductPageUserDto;
import cn.lime.mall.model.vo.ProductDetailVo;
import cn.lime.mall.model.vo.ProductPageVo;
import cn.lime.mall.service.db.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: ProductController
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 14:00
 */
@RestController
@RequestMapping("/product")
@Tag(name = "商品相关接口")
@CrossOrigin(origins = "*")
@RequestLog
public class ProductController {

    @Resource
    private ProductService productService;

    @PostMapping("/page")
    @Operation(summary = "用户查询商品分页")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.TOURIST)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<PageResult<ProductPageVo>> getProductPage(@RequestBody @Valid ProductPageUserDto dto, BindingResult result) {
        return ResultUtils.success(productService.getProductPage(
                dto.getProductName(),dto.getTagIds(),dto.getProductType(), YesNoEnum.YES.getVal(),
                dto.getCurrent(), dto.getPageSize(),dto.getSortField(),dto.getSortOrder()));
    }

    @PostMapping("/detail")
    @Operation(summary = "用户查询商品详情页")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.TOURIST)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<ProductDetailVo> getProductDetail(@RequestBody @Valid ProductIdDto dto, BindingResult result) {
        return ResultUtils.success(productService.getProductDetail(dto.getProductId(),YesNoEnum.YES.getVal()));
    }



}
