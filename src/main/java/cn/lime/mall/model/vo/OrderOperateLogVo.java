package cn.lime.mall.model.vo;

import cn.lime.mall.model.entity.OrderOperateLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderOperateLogVo
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/2 17:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOperateLogVo implements Serializable {
    @Schema(description = "操作ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @Schema(description = "操作用户ID 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @Schema(description = "操作内容")
    private String operation;
    @Schema(description = "操作时间 （秒级时间戳 序列化为String")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long operateTime;

    public static OrderOperateLogVo fromBean(OrderOperateLog bean){
        OrderOperateLogVo vo = new OrderOperateLogVo();
        vo.setId(bean.getId());
        vo.setUserId(bean.getUserId());
        vo.setOperation(bean.getOperate());
        vo.setOperateTime(bean.getGmtCreated().getTime()/1000);
        return vo;
    }
}
