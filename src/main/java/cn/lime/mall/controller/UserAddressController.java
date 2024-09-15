package cn.lime.mall.controller;

import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.BaseResponse;
import cn.lime.core.common.ResultUtils;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.module.dto.EmptyDto;
import cn.lime.core.threadlocal.ReqThreadLocal;
import cn.lime.mall.model.dto.address.AddressAddDto;
import cn.lime.mall.model.dto.address.AddressDeleteDto;
import cn.lime.mall.model.dto.address.AddressUpdateDto;
import cn.lime.mall.model.dto.cart.CartDto;
import cn.lime.mall.model.vo.AddressVo;
import cn.lime.mall.model.vo.CartVo;
import cn.lime.mall.service.db.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: UserAddressController
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/20 10:53
 */
@RestController
@RequestMapping("/address")
@Tag(name = "收货地址相关接口")
@CrossOrigin(origins = "*")
@RequestLog
public class UserAddressController {
    @Resource
    private AddressService addressService;

    @PostMapping("/list")
    @Operation(summary = "查询用户所有收货地址")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<List<AddressVo>> listAddress(@RequestBody @Valid EmptyDto dto, BindingResult result) {
        return ResultUtils.success(addressService.listUserAddress(ReqThreadLocal.getInfo().getUserId()));
    }

    @PostMapping("/add")
    @Operation(summary = "添加用户收货地址")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> addAddress(@RequestBody @Valid AddressAddDto dto, BindingResult result) {
        addressService.addAddress(ReqThreadLocal.getInfo().getUserId(), dto.getReceiverName(),
                dto.getReceiverPosition(),dto.getReceiverAddress(), dto.getReceiverPhone(), dto.getIsDefault());
        return ResultUtils.success(null);
    }

    @PostMapping("/update")
    @Operation(summary = "用户修改收货地址")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> updateAddress(@RequestBody @Valid AddressUpdateDto dto, BindingResult result) {
        addressService.updateAddress(dto.getAddressId(), dto.getReceiverName(),dto.getReceiverPosition(),
                dto.getReceiverAddress(), dto.getReceiverPhone(), dto.getIsDefault());
        return ResultUtils.success(null);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除用户收货地址")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> deleteAddress(@RequestBody @Valid AddressDeleteDto dto, BindingResult result) {
        addressService.deleteAddress(dto.getAddressId());
        return ResultUtils.success(null);
    }
}
