package cn.lime.mall.controller;

import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.BaseResponse;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ResultUtils;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.constant.YesNoEnum;
import cn.lime.core.module.dto.EmptyDto;
import cn.lime.mall.model.dto.producttag.ProductTagAddDto;
import cn.lime.mall.model.entity.ProductTag;
import cn.lime.mall.model.vo.CartVo;
import cn.lime.mall.service.db.ProductTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: TagController
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/19 16:22
 */
@RestController
@RequestMapping("/producttag")
@Tag(name = "商品标签相关接口")
@CrossOrigin(origins = "*")
@RequestLog
public class TagController {
    @Resource
    private ProductTagService tagService;

    @PostMapping("/list")
    @Operation(summary = "查询所有商品标签")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.TOURIST)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<List<ProductTag>> addTag(@RequestBody @Valid EmptyDto dto, BindingResult result) {
        return ResultUtils.success(tagService.listTags(YesNoEnum.YES.getVal()));
    }
}
