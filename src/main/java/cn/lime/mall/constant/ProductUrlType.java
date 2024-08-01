package cn.lime.mall.constant;

/**
 * @ClassName: ProductUrlType
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/8/1 17:41
 */
public enum ProductUrlType {

    MAIN(1),
    ROUND(2)
    ;

    private final int val;

    ProductUrlType(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }
}
