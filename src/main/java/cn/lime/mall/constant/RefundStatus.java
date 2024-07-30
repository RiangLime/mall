package cn.lime.mall.constant;

/**
 * @ClassName: RefundStatus
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/3/21 17:54
 */
public enum RefundStatus {

    NO(0),
    PROCESSING(1),
    SUCCESS(2),
    CLOSED(3),
    FAIL(4)
    ;

    private final int val;

    RefundStatus(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }
}
