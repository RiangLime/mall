package cn.lime.mall.constant;

/**
 * @ClassName: OrderStatus
 * @Description: 订单状态 0待支付、1支付中、2待发货、3待收货、4待评价、5已完成、8退款中、9关闭订单、
 * @Author: Lime
 * @Date: 2023/9/27 16:17
 */
public enum OrderStatus {

    WAITING_PAY(0),
    PAYING(1),
    WAITING_SEND(2),
    WAITING_RECEIVE(3),
    WAITING_COMMENT(4),
    FINISH(5),
    REFUNDING(8),
    CLOSE(9)
    ;

    private final int val;

    OrderStatus(int dbVal) {
        this.val = dbVal;
    }

    public int getVal() {
        return val;
    }

}
