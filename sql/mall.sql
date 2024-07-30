-- 用户收货地址
create table Address
(
    address_id       int           not null auto_increment primary key comment '地址ID 自增',
    receiver_name    nvarchar(64)  not null comment '收货人姓名',
    receiver_address nvarchar(512) not null comment '收货地址',
    receiver_phone   nvarchar(64)  not null comment '收货人手机号',
    user_id          bigint        not null comment '收货人对应用户ID',
    is_default       tinyint       not null default 0 comment '是否为默认地址',
    gmt_created      bigint        null comment '创建时间',
    gmt_modified     bigint        null comment '更新时间'
);

-- 商品
create table Product
(
    product_id               bigint        not null primary key comment '商品ID',
    product_number           int auto_increment comment '自增ID',
    product_main_picture_url nvarchar(512) null comment '商品主图URL',
    product_name             nvarchar(256) not null comment '商品名称',
    product_state            tinyint       not null default 1 comment '商品状态 1上架 0下架',
    product_sort             int           not null default 1 comment '排序字段',
    product_type             int           not null default 1 comment '商品类型 单规格/多规格',
    gmt_created              bigint        null comment '创建时间',
    gmt_modified             bigint        null comment '更新时间'
);

create table Product_Url
(
    url_id       bigint  not null primary key comment 'url id',
    product_id   bigint  not null comment '商品ID',
    url_type     tinyint not null comment 'URL类型 1主图 2轮播图',
    url_sort     int     not null default 1 comment '轮播图序号',
    gmt_created  bigint  null comment '创建时间',
    gmt_modified bigint  null comment '更新时间'
);

-- 规格

-- sku
create table Sku
(
    sku_id     bigint not null primary key comment '库存单元ID',
    product_id bigint not null comment '商品ID',
    sku

)

