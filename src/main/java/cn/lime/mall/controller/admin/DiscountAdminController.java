package cn.lime.mall.controller.admin;

import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.BaseResponse;
import cn.lime.core.common.PageResult;
import cn.lime.core.common.ResultUtils;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.threadlocal.ReqThreadLocal;
import cn.lime.mall.constant.DiscountTypeEnum;
import cn.lime.mall.model.dto.discount.*;
import cn.lime.mall.model.vo.discount.DiscountVo;
import cn.lime.mall.service.db.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/discount")
@Tag(name = "折扣券相关接口[管理员]")
@CrossOrigin(origins = "*")
@RequestLog
public class DiscountAdminController {

    @Resource
    private DiscountService discountService;

    @PostMapping("/add")
    @Operation(summary = "管理员新增折扣券")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<DiscountVo> addDiscount(@RequestBody @Valid DiscountAddDto dto, BindingResult result) {
        return ResultUtils.success(discountService.addDiscount(ObjectUtils.isEmpty(dto.getOwnerId()) ?
                        DiscountTypeEnum.CD_KEY.getVal() : DiscountTypeEnum.USER_DISCOUNT.getVal(),
                dto.getOwnerId(), dto.getMinPrice(), dto.getDiscountPrice(), dto.getAvailableProductList()));
    }

    @PostMapping("/add/all")
    @Operation(summary = "管理员新增折扣券 全体发放")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> addDiscountAll(@RequestBody @Valid DiscountAddDto dto, BindingResult result) {
        discountService.addDiscountAll(DiscountTypeEnum.USER_DISCOUNT.getVal(), dto.getMinPrice(),
                dto.getDiscountPrice(), dto.getAvailableProductList());
        return ResultUtils.success(null);
    }

    @PostMapping("/add/batch")
    @Operation(summary = "管理员新增折扣券 批量")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    @Transactional
    public BaseResponse<Void> addBatchDiscount(@RequestBody @Valid DiscountAddBatchDto dto, BindingResult result) {
        for (int i = 0; i < dto.getNumber(); i++) {
            discountService.addDiscount(ObjectUtils.isEmpty(dto.getOwnerId()) ?
                            DiscountTypeEnum.CD_KEY.getVal() : DiscountTypeEnum.USER_DISCOUNT.getVal(),
                    dto.getOwnerId(), dto.getMinPrice(), dto.getDiscountPrice(), dto.getAvailableProductList());
        }
        return ResultUtils.success(null);
    }

    @PostMapping("/delete")
    @Operation(summary = "管理员删除折扣券")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> deleteDiscount(@RequestBody @Valid DiscountIdDto dto, BindingResult result) {
        discountService.deleteDiscount(dto.getDiscountId());
        return ResultUtils.success(null);
    }

    @PostMapping("/udpateavailable")
    @Operation(summary = "管理员设置折扣券是否可用")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> updateDiscountAvailable(@RequestBody @Valid DiscountAvailableUpdateDto dto, BindingResult result) {
        discountService.updateAvailable(dto.getDiscountId(),dto.getIsAvailable());
        return ResultUtils.success(null);
    }

    @PostMapping("/page")
    @Operation(summary = "管理员查询折扣券 分页")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<PageResult<DiscountVo>> pageDiscount(@RequestBody@Valid DiscountAdminPageDto dto,BindingResult result){
        return ResultUtils.success(discountService.pageDiscount(dto.getType(),dto.getOwnerId(),dto.getDiscountPriceRangeStart(),
                dto.getDiscountPriceEnd(),dto.getProductId(),dto.getIsAvailable(),dto.getCurrent(),dto.getPageSize()));
    }

    @PostMapping("/updatenewuserdiscount")
    @Operation(summary = "管理员更新新用户折扣")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<DiscountVo> updateNewUserDiscount(@RequestBody@Valid NewUserDiscountUpdateDto dto,BindingResult result){
        return ResultUtils.success(discountService.updateNewUserDiscount(dto.getMinPrice(),dto.getDiscountPrice(),dto.getAvailableProductList()));
    }

    @PostMapping("/bind")
    @Operation(summary = "管理员为用户绑定cdkey")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> bind(@RequestBody @Valid DiscountIdBindAdminDto dto, BindingResult result) {
        discountService.cdKeyBind(dto.getDiscountId(), dto.getUserId());
        return ResultUtils.success(null);
    }

}
