package cn.lime.mall.constant;

/**
 * @ClassName: PaymentTypeEnum
 * @Description: 1微信支付-二维码支付 2微信支付-小程序JSapi支付
 * @Author: Lime
 * @Date: 2023/10/19 11:24
 */
public enum PaymentTypeEnum {

    WX_PAY_BASE(0),
    CODE(1),
    JS_API(2),
    H5(3),
    STRIPE(4),

    ;

    private final int val;

    PaymentTypeEnum(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }

}
