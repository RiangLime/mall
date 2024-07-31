package cn.lime.mall.constant;

/**
 * @ClassName: ProductUrlTypeEnum
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/7/31 17:02
 */
public enum ProductUrlTypeEnum {
    MAIN(1),
    ROUND(2)
    ;

    private final int val;

    ProductUrlTypeEnum(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }
}
