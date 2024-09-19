package cn.lime.mall.controller.admin;

import cn.lime.core.annotation.AuthCheck;
import cn.lime.core.annotation.DtoCheck;
import cn.lime.core.annotation.RequestLog;
import cn.lime.core.common.*;
import cn.lime.core.constant.AuthLevel;
import cn.lime.core.constant.YesNoEnum;
import cn.lime.core.module.dto.EmptyDto;
import cn.lime.mall.model.dto.producttag.ProductParentIdDto;
import cn.lime.mall.model.dto.producttag.ProductTagAddDto;
import cn.lime.mall.model.dto.producttag.ProductTagDeleteDto;
import cn.lime.mall.model.dto.producttag.ProductTagUpdateDto;
import cn.lime.mall.model.vo.ProductLevelTagVo;
import cn.lime.mall.model.vo.ProductTagVo;
import cn.lime.mall.service.db.ProductTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: TagAdminController
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/19 16:12
 */
@RestController
@RequestMapping("/admin/producttag")
@Tag(name = "商品标签相关接口[管理员]")
@CrossOrigin(origins = "*")
@RequestLog
public class TagAdminController {

    @Resource
    private ProductTagService tagService;

    @PostMapping("/add")
    @Operation(summary = "管理员添加商品标签")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> addTag(@RequestBody @Valid ProductTagAddDto dto, BindingResult result) {
        ThrowUtils.throwIf(!tagService.addTag(dto.getParentId(), dto.getTag(), dto.getTagUrl(), 0), ErrorCode.INSERT_ERROR);
        return ResultUtils.success(null);
    }

    @PostMapping("/update")
    @Operation(summary = "管理员修改商品标签")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> updateTag(@RequestBody @Valid ProductTagUpdateDto dto, BindingResult result) {
        ThrowUtils.throwIf(!tagService.updateTag(dto.getTagId(), dto.getParentId(), dto.getTagName(), dto.getTagUrl(), dto.getTagSort(), dto.getTagState()), ErrorCode.INSERT_ERROR);
        return ResultUtils.success(null);
    }

    @PostMapping("/delete")
    @Operation(summary = "管理员删除商品标签 如果是一级标签会自动删除子标签")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<Void> deleteTag(@RequestBody @Valid ProductTagDeleteDto dto, BindingResult result) {
        ThrowUtils.throwIf(!tagService.deleteTag(dto.getTagId()), ErrorCode.INSERT_ERROR);
        return ResultUtils.success(null);
    }

    @PostMapping("/list")
    @Operation(summary = "查询所有商品标签")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<List<ProductTagVo>> listTag(@RequestBody @Valid EmptyDto dto, BindingResult result) {
        return ResultUtils.success(tagService.listTags(null));
    }

    @PostMapping("/list/leveltag")
    @Operation(summary = "查询所有商品标签 以树状结构返回")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<List<ProductLevelTagVo>> listTopTags(@RequestBody @Valid EmptyDto dto, BindingResult result){
        return ResultUtils.success(tagService.listLeveledTags(null));
    }

    @PostMapping("/list/toptag")
    @Operation(summary = "查询所有一级标签")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<List<ProductLevelTagVo>> listTopTags(@RequestBody @Valid ProductParentIdDto dto, BindingResult result){
        return ResultUtils.success(tagService.listChildrenTags(null,dto.getParentId()));
    }

    @PostMapping("/list/childrentag")
    @Operation(summary = "查询某父标签下的所有子标签")
    @AuthCheck(needToken = true, needPlatform = true, authLevel = AuthLevel.ADMIN)
    @DtoCheck(checkBindResult = true)
    public BaseResponse<List<ProductLevelTagVo>> listChildrenTags(@RequestBody @Valid ProductParentIdDto dto, BindingResult result){
        return ResultUtils.success(tagService.listChildrenTags(null,dto.getParentId()));
    }

}
