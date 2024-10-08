-- 用户收货地址
create table Address
(
    address_id        INT           NOT NULL auto_increment primary key comment '地址ID 自增',
    user_id           BIGINT        NOT NULL comment '收货人对应用户ID',
    receiver_name     NVARCHAR(64)  NOT NULL comment '收货人姓名',
    receiver_position NVARCHAR(128) not null comment '收货地区',
    receiver_address  NVARCHAR(512) NOT NULL comment '收货地址',
    receiver_phone    NVARCHAR(64)  NOT NULL comment '收货人手机号',
    is_DEFAULT        tinyINT       NOT NULL DEFAULT 0 comment '是否为默认地址',
    gmt_created       TIMESTAMP              DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified      TIMESTAMP              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) comment '收货地址表' collate = utf8mb4_unicode_ci;;
ALTER TABLE Address
    ADD CONSTRAINT fk_address_user_id
        FOREIGN KEY (user_id) REFERENCES User (user_id) on delete cascade;

-- 商品
create table Product
(
    product_id          BIGINT        NOT NULL primary key comment '商品ID',
    product_code        NVARCHAR(64) comment '商品编码',
    product_name        NVARCHAR(256) NOT NULL comment '商品名称',
    product_description text          NULL comment '商品详情',
    product_state       tinyINT       NOT NULL DEFAULT 1 comment '商品状态 1上架 0下架',
    visible             tinyint       NOT NULL default 1 comment '商品是否可见',
    product_sort        INT           NOT NULL DEFAULT 1 comment '排序字段',
    product_type_1      NVARCHAR(64)  NULL comment '商品类型1',
    product_type_2      NVARCHAR(64)  NULL comment '商品类型2',
    reserve_str_a       nvarchar(512) null comment '保留字段 字符串A',
    reserve_str_b       nvarchar(512) null comment '保留字段 字符串B',
    reserve_int_a       int           null comment '保留字段 intA',
    reserve_int_b       int           null comment '保留字段 intB',
    gmt_created         TIMESTAMP              DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified        TIMESTAMP              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
) comment '商品表' collate = utf8mb4_unicode_ci;;

-- 商品URL
create table Product_Url
(
    url_id       BIGINT         NOT NULL primary key comment 'url id',
    product_id   BIGINT         NOT NULL comment '商品ID',
    url          nvarchar(1024) not null comment 'URL',
    url_type     tinyINT        NOT NULL comment 'URL类型 1主图 2轮播图',
    url_sort     INT            NOT NULL DEFAULT 1 comment '轮播图序号',
    gmt_created  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified TIMESTAMP               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
) comment '商品URL表' collate = utf8mb4_unicode_ci;;
ALTER TABLE Product_Url
    ADD CONSTRAINT fk_product_url_product_id
        FOREIGN KEY (product_id) REFERENCES Product (product_id) on delete cascade;


-- sku
create table Sku
(
    sku_id          BIGINT        NOT NULL primary key comment '库存单元ID',
    product_id      BIGINT        NOT NULL comment '商品ID',
    sku_code        NVARCHAR(512) NOT NULL comment 'sku编码',
    sku_description NVARCHAR(512) NULL comment 'sku描述',
    price           INT           NOT NULL comment 'SKU价格',
    stock           INT           NOT NULL comment '库存',
    remark          NVARCHAR(512) NULL comment '备注',
    gmt_created     TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
) comment 'SKU表' collate = utf8mb4_unicode_ci;;
ALTER TABLE Sku
    ADD CONSTRAINT fk_sku_product_id
        FOREIGN KEY (product_id) REFERENCES Product (product_id) on delete cascade;

create table SkuAttribute
(
    id              BIGINT        NOT NULL primary key comment '主键',
    sku_id          BIGINT        NOT NULL comment 'SKU ID',
    attribute_name  NVARCHAR(128) NOT NULL comment '分组名称',
    attribute_value NVARCHAR(128) NOT NULL comment '分组值',
    gmt_created     TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
) comment 'SKU属性表' collate = utf8mb4_unicode_ci;;
ALTER TABLE SkuAttribute
    ADD CONSTRAINT fk_sku_attribute_sku_id
        FOREIGN KEY (sku_id) REFERENCES Sku (sku_id) on delete cascade;


create table Product_Tag
(
    tag_id        BIGINT        NOT NULL primary key comment '商品分类ID',
    parent_tag_id BIGINT        NOT NULL DEFAULT 0 comment '父级分类ID',
    tag_name      NVARCHAR(128) NOT NULL comment '分类名',
    tag_url       NVARCHAR(512) NULL comment '分类图标',
    tag_sort      INT           NOT NULL DEFAULT 1 comment '排序字段',
    tag_state     tinyINT       NOT NULL DEFAULT 1 comment '分类状态 是否展示',
    gmt_created   TIMESTAMP              DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified  TIMESTAMP              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
) comment '商品标签表' collate = utf8mb4_unicode_ci;;

-- 商品标签
create table Product_Have_Tag
(
    id          BIGINT NOT NULL primary key comment '主键',
    product_id  BIGINT NOT NULL comment '商品ID',
    tag_id      BIGINT NOT NULL comment '分类ID',
    gmt_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'
) comment '商品标签关联表' collate = utf8mb4_unicode_ci;;
ALTER TABLE Product_Have_Tag
    ADD CONSTRAINT fk_product_have_tag_product_id
        FOREIGN KEY (product_id) REFERENCES Product (product_id) on delete cascade;
ALTER TABLE Product_Have_Tag
    ADD CONSTRAINT fk_product_have_tag_tag_id
        FOREIGN KEY (tag_id) REFERENCES Product_Tag (tag_id) on delete cascade;

-- 购物车
create table Cart
(
    id           BIGINT NOT NULL primary key comment 'id',
    user_id      BIGINT NOT NULL comment '用户ID',
    product_id   BIGINT NOT NULL comment '商品ID',
    sku_id       BIGINT NOT NULL comment 'SKU ID',
    number       INT    NOT NULL comment '购买数量',
    gmt_created  TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
) comment '购物车表' collate = utf8mb4_unicode_ci;
ALTER TABLE Cart
    ADD CONSTRAINT fk_cart_user_id
        FOREIGN KEY (user_id) REFERENCES User (user_id) on delete cascade;

create table Product_View_Log
(
    id          bigint not null primary key comment 'ID',
    product_id  bigint not null comment '商品ID',
    user_id     bigint null comment '浏览用户ID',
    gmt_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'
) comment '商品详情页浏览日志表' collate = utf8mb4_unicode_ci;;

-- 订单表
create table `Order`
(
    order_id             bigint              not null comment '订单ID' primary key,
    order_source         int                 not null comment '订单来源',
    order_code           nvarchar(64)        not null comment '订单编号',
    user_id              bigint              not null comment '用户ID',
    address_id           bigint              null comment '用户收货地址ID',
    order_pay_method     tinyint             null comment '付款方式',
    order_status         int       default 0 not null comment '订单状态 订单状态 0待支付、1支付中、2待发货、3待收货、4待评价、5已完成、8关闭订单、9退/换货',
    origin_order_price   int       default 0 not null comment '订单价格',
    real_order_price     int       default 0 not null comment '实际订单价格',
    order_start_pay_time bigint              null comment '用户开始支付时间',
    order_pay_time       timestamp           null comment '订单支付时间',
    order_receive_time   timestamp           null comment '订单收货时间',
    order_finish_time    timestamp           null comment '订单完成时间',
    order_is_deal        tinyint   default 0 not null comment '微信回调函数是否被处理过 0没有 1处理过',
    third_payment_id     nvarchar(255)       null comment '第三方付款ID',
    third_payment_method int                 null comment '第三方支付方式',
    refund_id            bigint              null comment '退款ID',
    refund_status        int                 null comment '退款状态 0未退款 1退款中 2退款成功 3退款关闭 4退款异常',
    refund_price         int                 null comment '退款金额',
    refund_is_deal       int       default 0 not null comment '微信回调函数是否被处理过 0没有 1处理过',
    remark1              nvarchar(2048)      null comment '备注信息',
    remark2              nvarchar(2048)      null comment '备注信息',
    comment              nvarchar(2048)      null comment '评论',
    deliver_company      nvarchar(128)       null comment '物流公司',
    deliver_id           nvarchar(64)        null comment '物流单号',
    send_deliver_time    timestamp           null comment '发货时间',
    gmt_created          TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified         TIMESTAMP DEFAULT null ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
) comment '订单表' collate = utf8mb4_unicode_ci;

create table Order_Item
(
    id          bigint not null comment 'id' primary key,
    order_id    bigint not null comment '订单ID',
    product_id  bigint not null comment '商品ID',
    sku_id      bigint not null comment 'SKU ID',
    number      int    not null default 1 comment '购买数量',
    item_price  int    not null comment '该项SKU总价',
    gmt_created TIMESTAMP       DEFAULT CURRENT_TIMESTAMP comment '创建时间'
) comment '订单物品表' collate = utf8mb4_unicode_ci;
ALTER TABLE Order_Item
    ADD CONSTRAINT fk_order_item_order_id
        FOREIGN KEY (order_id) REFERENCES `Order` (order_id) on delete cascade;

create table Order_Operate_Log
(
    id          bigint         not null comment '日志ID' primary key,
    order_id    bigint         not null comment '订单ID',
    user_id     bigint         not null comment '操作用户ID',
    operate     nvarchar(2048) null comment '操作事件',
    gmt_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'
) comment '订单操作日志表' collate = utf8mb4_unicode_ci;