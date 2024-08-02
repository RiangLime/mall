package cn.lime.mall.model.vo;

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
    private Long id;
    private Long userId;
    private String operation;
    private Long operateTime;
}
