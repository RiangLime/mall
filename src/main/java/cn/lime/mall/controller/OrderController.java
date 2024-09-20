package cn.lime.mall.controller;

import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.*;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.threadlocal.ReqThreadLocal;
import cn.lime.mall.model.dto.order.*;
import cn.lime.mall.model.entity.Order;
import cn.lime.mall.model.vo.OrderDetailVo;
import cn.lime.mall.model.vo.OrderPageVo;
import cn.lime.mall.model.vo.OrderPayVo;
import cn.lime.mall.service.db.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: OrderController
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 14:38
 */
@RestController
@RequestMapping("/order")
@Tag(name = "订单相关接口")
@CrossOrigin(origins = "*")
@RequestLog
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "用户创建订单")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<OrderDetailVo> createOrder(@RequestBody @Valid OrderCreateDto dto, BindingResult result) {
        Order order = orderService.createOrder(ReqThreadLocal.getInfo().getUserId(), dto.getAddressId(), dto.getOrderItems(), dto.getRemark());
        OrderDetailVo vo = orderService.getOrderDetail(order.getOrderId());
        return ResultUtils.success(vo);
    }

    @PostMapping("/cancel")
    @Operation(summary = "用户取消订单")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> cancelOrder(@RequestBody @Valid OrderIdDto dto, BindingResult result) {
        ThrowUtils.throwIf(!orderService.cancelOrder(dto.getOrderId()), ErrorCode.UPDATE_ERROR, "取消订单失败");
        return ResultUtils.success(null);
    }


    @PostMapping("/pay")
    @Operation(summary = "用户支付订单")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<OrderPayVo> payOrder(@RequestBody @Valid OrderPayDto dto, BindingResult result) {
        return ResultUtils.success(orderService.payOrder(dto));
    }

    @PostMapping("/refund")
    @Operation(summary = "用户申请订单退款")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> applyRefund(@RequestBody@Valid OrderIdDto dto, BindingResult result){
        ThrowUtils.throwIf(!orderService.applyRefund(dto.getOrderId()),ErrorCode.UPDATE_ERROR);
        return ResultUtils.success(null);
    }

    @PostMapping("/receive")
    @Operation(summary = "用户订单收货")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> receiveOrder(@RequestBody@Valid OrderIdDto dto, BindingResult result){
        orderService.receive(dto.getOrderId());
        return ResultUtils.success(null);
    }

    @PostMapping("/comment")
    @Operation(summary = "用户订单评论")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> commentOrder(@RequestBody@Valid OrderCommentDto dto, BindingResult result){
        orderService.comment(dto.getOrderId(),dto.getComment());
        return ResultUtils.success(null);
    }

    @PostMapping("/page")
    @Operation(summary = "用户查询订单列表")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<PageResult<OrderPageVo>> pageOrder(@RequestBody@Valid OrderPageUserDto dto, BindingResult result){
        return ResultUtils.success(orderService.getOrderPage(dto.getOrderCode(), null,dto.getProductName(),
                null, dto.getOrderState(),null,dto.getOrderStartTime(),dto.getOrderEndTime(),
                dto.getCurrent(),dto.getPageSize(),dto.getSortField(),dto.getSortOrder()));
    }

    @PostMapping("/detail")
    @Operation(summary = "用户查询订单详情")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.USER)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<OrderDetailVo> getOrderDetail(@RequestBody@Valid OrderIdDto dto, BindingResult result){
        return ResultUtils.success(orderService.getOrderDetail(dto.getOrderId()));
    }



}
