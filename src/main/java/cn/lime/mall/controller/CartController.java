package cn.lime.mall.controller;

import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.BaseResponse;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ResultUtils;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.module.dto.EmptyDto;
import cn.lime.core.threadlocal.ReqThreadLocal;
import cn.lime.mall.model.dto.cart.CartDto;
import cn.lime.mall.model.dto.cart.UpdateCartNumberDto;
import cn.lime.mall.model.vo.CartVo;
import cn.lime.mall.service.db.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: CartController
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/19 16:28
 */
@RestController
@RequestMapping("/cart")
@Tag(name = "购物车相关接口")
@CrossOrigin(origins = "*")
@RequestLog
public class CartController {

    @Resource
    private CartService cartService;

    @PostMapping("/list")
    @Operation(summary = "查询用户购物车信息")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<List<CartVo>> listCart(@RequestBody @Valid EmptyDto dto, BindingResult result) {
        return ResultUtils.success(cartService.listUserCart(ReqThreadLocal.getInfo().getUserId()));
    }

    @PostMapping("/add")
    @Operation(summary = "添加购物车")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> addCart(@RequestBody @Valid CartDto dto, BindingResult result) {
        ThrowUtils.throwIf(dto.getNumber()==0, ErrorCode.INSERT_ERROR,"添加购物车商品数量最小值为1");
        cartService.addCart(ReqThreadLocal.getInfo().getUserId(), dto.getProductId(),dto.getSkuId(),dto.getNumber());
        return ResultUtils.success(null);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除购物车")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> deleteCart(@RequestBody @Valid CartDto dto, BindingResult result) {
        ThrowUtils.throwIf(dto.getNumber()==0, ErrorCode.DELETE_ERROR,"删除购物车商品数量最小值为1");
        cartService.deleteCart(ReqThreadLocal.getInfo().getUserId(), dto.getProductId(),dto.getSkuId(),dto.getNumber());
        return ResultUtils.success(null);
    }

    @PostMapping("/updatenumber")
    @Operation(summary = "删除购物车")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> updateCartNumber(@RequestBody @Valid CartDto dto, BindingResult result) {
        cartService.updateCartNumber(ReqThreadLocal.getInfo().getUserId(), dto.getProductId(),dto.getSkuId(),dto.getNumber());
        return ResultUtils.success(null);
    }

}
