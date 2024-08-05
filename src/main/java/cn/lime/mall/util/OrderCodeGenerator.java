package cn.lime.mall.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: OrderCodeGenerator
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/5 10:46
 */
public class OrderCodeGenerator {

    public static String get() {
        // 获取当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        return date + System.currentTimeMillis() / 1000;
    }

}
