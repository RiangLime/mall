package cn.lime.mall.model.vo;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName: SkuVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/1 15:55
 */
@Data
public class SkuInfoVo {

    private Long skuId;
    private String skuCode;
    private String skuDescription;
    private Integer price;
    private Integer stock;
    private String remark;
    private Long createTime;
    private Map<String,String> attributes;

}
