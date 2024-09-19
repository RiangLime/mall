package cn.lime.mall.controller.admin;

import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.*;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.constant.YesNoEnum;
import cn.lime.mall.model.dto.product.*;
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
 * @ClassName: ProductAdminController
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/6 14:22
 */
@RestController
@RequestMapping("/admin/product")
@Tag(name = "商品相关接口[管理员]")
@CrossOrigin(origins = "*")
@RequestLog
public class ProductAdminController {

    @Resource
    private ProductService productService;

    @PostMapping("/page")
    @Operation(summary = "用户查询商品分页")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<PageResult<ProductPageVo>> getProductPage(@RequestBody @Valid ProductPageAdminDto dto, BindingResult result) {
        return ResultUtils.success(productService.getProductPage(
                dto.getProductName(),dto.getTagIds(),dto.getProductType(), dto.getProductState(),null,
                dto.getCurrent(), dto.getPageSize(),dto.getSortField(),dto.getSortOrder()));
    }

    @PostMapping("/detail")
    @Operation(summary = "用户查询商品详情页")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<ProductDetailVo> getProductDetail(@RequestBody @Valid ProductIdDto dto, BindingResult result) {
        return ResultUtils.success(productService.getProductDetail(dto.getProductId(),null));
    }

    @PostMapping("/add")
    @Operation(summary = "管理员添加商品")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> addNewProduct(@RequestBody @Valid ProductAddDto dto,BindingResult result){
        ThrowUtils.throwIf(!productService.addProduct(dto.getProductCode(),dto.getProductName(),dto.getProductDescription(),
                dto.getProductType1(), dto.getProductType2(),dto.getVisible(),dto.getMainPicUrl(),
                dto.getRoundUrls(),dto.getProductBrand(),dto.getSkuInfos(),dto.getProductTagIds(),dto.getProductState(),
                dto.getProductSubTitle()), ErrorCode.INSERT_ERROR);
        return ResultUtils.success(null);
    }

    @PostMapping("/delete")
    @Operation(summary = "管理员删除商品")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public  BaseResponse<Void> deleteProduct(@RequestBody@Valid ProductDeleteDto dto, BindingResult result){
        ThrowUtils.throwIf(!productService.deleteProducts(dto.getProductIds()),ErrorCode.DELETE_ERROR);
        return ResultUtils.success(null);
    }

    @PostMapping("/update/state")
    @Operation(summary = "管理员上下架商品")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    @Deprecated
    public BaseResponse<Void> updateProductState(@RequestBody@Valid ProductUpdateStateDto dto,BindingResult result){
        if (dto.getState().equals(YesNoEnum.YES.getVal())){
            ThrowUtils.throwIf(!productService.stateUpProducts(dto.getProductIds()),ErrorCode.UPDATE_ERROR);
        }else if (dto.getState().equals(YesNoEnum.NO.getVal())){
            ThrowUtils.throwIf(!productService.stateDownProducts(dto.getProductIds()),ErrorCode.UPDATE_ERROR);
        }else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(null);
    }

    @PostMapping("/update/addtag")
    @Operation(summary = "管理员给商品添加标签")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    @Deprecated
    public BaseResponse<Void> addTagForProduct(@RequestBody@Valid ProductTagDto dto, BindingResult result){
        ThrowUtils.throwIf(!productService.addProductTag(dto.getProductId(),dto.getTagId()),ErrorCode.UPDATE_ERROR);
        return ResultUtils.success(null);
    }

    @PostMapping("/update/deletetag")
    @Operation(summary = "管理员给商品删除标签")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    @Deprecated
    public BaseResponse<Void> deleteTagForProduct(@RequestBody@Valid ProductTagDto dto,BindingResult result){
        ThrowUtils.throwIf(!productService.removeProductTag(dto.getProductId(),dto.getTagId()),ErrorCode.UPDATE_ERROR);
        return ResultUtils.success(null);
    }

    @PostMapping("/update/deletesku")
    @Operation(summary = "管理员给商品删除sku")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> deleteSkusForProduct(@RequestBody@Valid ProductIdDto dto,BindingResult result){
        ThrowUtils.throwIf(!productService.deleteSkus(dto.getProductId()),ErrorCode.DELETE_ERROR);
        return ResultUtils.success(null);
    }

    @PostMapping("/update/reformsku")
    @Operation(summary = "管理员重置商品sku")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    @Deprecated
    public BaseResponse<Void> reformSkusForProduct(@RequestBody@Valid ProductSkuReformDto dto,BindingResult result){
        ThrowUtils.throwIf(!productService.reformatSkus(dto.getProductId(),dto.getSkuInfos()),ErrorCode.DELETE_ERROR);
        return ResultUtils.success(null);
    }

    @PostMapping("/update")
    @Operation(summary = "管理员修改商品基本信息")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> updateProduct(@RequestBody@Valid ProductUpdateDto dto,BindingResult result){
        productService.updateProduct(dto.getProductId(),dto.getProductCode(),dto.getProductName(),
                dto.getProductDescription(), dto.getProductType1(),dto.getProductType2(),dto.getVisible(),
                dto.getMainPicUrl(), dto.getRoundUrls(),dto.getProductBrand(),dto.getSkuInfos(),
                dto.getProductTagIds(),dto.getProductState(),dto.getProductSubTitle());
        return ResultUtils.success(null);
    }

}
