package cn.lime.mall.service.db;

import cn.lime.mall.model.entity.ProductViewLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author riang
* @description 针对表【Product_View_Log】的数据库操作Service
* @createDate 2024-08-01 15:14:26
*/
public interface ProductViewLogService extends IService<ProductViewLog> {
    void append(Long userId,Long productId);
}
