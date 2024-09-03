package cn.lime.mall.model.vo;

import cn.lime.mall.model.entity.ProductTag;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @ClassName: ProductTagVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/9/2 17:07
 */
@Data
public class ProductTagVo {
    /**
     * 商品分类ID
     */
    @Schema(description = "标签ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tagId;

    /**
     * 父级分类ID
     */
    @Schema(description = "父级标签ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentTagId;

    /**
     * 分类名
     */
    @Schema(description = "标签名")
    private String tagName;

    /**
     * 分类图标
     */
    @Schema(description = "标签Logo Url")
    private String tagUrl;

    /**
     * 排序字段
     */
    @Schema(description = "标签排序字段")
    private Integer tagSort;

    /**
     * 分类状态 是否展示
     */
    @Schema(description = "标签状态 是否展示")
    private Integer tagState;

    @Schema(description = "创建时间 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long createTime;

    @Schema(description = "更新时间 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long updateTime;

    public static ProductTagVo fromBean(ProductTag bean){
        ProductTagVo vo = new ProductTagVo();
        BeanUtils.copyProperties(bean,vo);
        vo.setCreateTime(bean.getGmtCreated().getTime()/1000);
        vo.setUpdateTime(bean.getGmtModified().getTime()/1000);
        return vo;
    }
}
