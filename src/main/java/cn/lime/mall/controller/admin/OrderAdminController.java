package cn.lime.mall.controller.admin;

import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.*;
import cn.lime.core.constant.AuthLevel;
import cn.lime.mall.model.dto.OrderIdDto;
import cn.lime.mall.model.dto.OrderPageUserDto;
import cn.lime.mall.model.dto.OrderRemarkDto;
import cn.lime.mall.model.vo.OrderDetailVo;
import cn.lime.mall.model.vo.OrderPageVo;
import cn.lime.mall.service.db.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: OrderAdminController
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 15:44
 */
@RestController
@RequestMapping("/admin/order")
@Tag(name = "订单相关接口[管理员]")
@CrossOrigin(origins = "*")
@RequestLog
public class OrderAdminController {
    @Resource
    private OrderService orderService;

    @PostMapping("/page")
    @Operation(summary = "管理员查询订单列表")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<PageResult<OrderPageVo>> pageOrder(@RequestBody @Valid OrderPageUserDto dto, BindingResult result){
        return ResultUtils.success(orderService.getOrderPage(dto.getOrderCode(), dto.getUserName(),dto.getProductName(),
                null, dto.getOrderState(),null,dto.getOrderStartTime(),dto.getOrderEndTime(),
                dto.getCurrent(),dto.getPageSize(),dto.getSortField(),dto.getSortOrder()));
    }

    @PostMapping("/detail")
    @Operation(summary = "管理员查询订单详情")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<OrderDetailVo> getOrderDetail(@RequestBody@Valid OrderIdDto dto, BindingResult result){
        return ResultUtils.success(orderService.getOrderDetail(dto.getOrderId()));
    }

    @PostMapping("/addremark")
    @Operation(summary = "管理员添加订单备注")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> addRemark(@RequestBody@Valid OrderRemarkDto dto,BindingResult result){
        orderService.addRemark(dto.getOrderId(),dto.getUserRemark(),dto.getMerchantRemark());
        return ResultUtils.success(null);
    }

    @PostMapping("/send")
    @Operation(summary = "管理员发货")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> orderSendStaff(@RequestBody@Valid OrderIdDto dto,BindingResult result){
        ThrowUtils.throwIf(!orderService.updateOrderStatusFromWaitingSendToWaitingReceive(dto.getOrderId()),
                ErrorCode.UPDATE_ERROR,"更新订单状态异常");
        return ResultUtils.success(null);
    }

}
