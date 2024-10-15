package cn.lime.mall.controller;

import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.BaseResponse;
import cn.lime.core.common.PageRequest;
import cn.lime.core.common.PageResult;
import cn.lime.core.common.ResultUtils;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.constant.YesNoEnum;
import cn.lime.core.module.dto.EmptyDto;
import cn.lime.core.threadlocal.ReqThreadLocal;
import cn.lime.mall.model.vo.CartVo;
import cn.lime.mall.model.vo.discount.DiscountVo;
import cn.lime.mall.service.db.CartService;
import cn.lime.mall.service.db.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/discount")
@Tag(name = "折扣券相关接口")
@CrossOrigin(origins = "*")
@RequestLog
public class DiscountController {

    @Resource
    private DiscountService discountService;

    @PostMapping("/page")
    @Operation(summary = "查询用户折扣券信息")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<PageResult<DiscountVo>> listCart(@RequestBody @Valid PageRequest dto, BindingResult result) {
        return ResultUtils.success(discountService.pageDiscount(null,ReqThreadLocal.getInfo().getUserId(),
                null,null,null, YesNoEnum.YES.getVal()
                ,dto.getCurrent(),dto.getPageSize()));
    }

}
