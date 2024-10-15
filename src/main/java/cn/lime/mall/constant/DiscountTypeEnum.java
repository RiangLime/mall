package cn.lime.mall.constant;

public enum DiscountTypeEnum {
    NEW_USER_DISCOUNT(1),
    USER_DISCOUNT(2),
    CD_KEY(3)

    ;

    private final int val;

    DiscountTypeEnum(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }
}
