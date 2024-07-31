-- 用户收货地址
create table Address
(
    address_id       INT           NOT NULL auto_increment primary key comment '地址ID 自增',
    user_id          BIGINT        NOT NULL comment '收货人对应用户ID',
    receiver_name    NVARCHAR(64)  NOT NULL comment '收货人姓名',
    receiver_address NVARCHAR(512) NOT NULL comment '收货地址',
    receiver_phone   NVARCHAR(64)  NOT NULL comment '收货人手机号',
    is_DEFAULT       tinyINT       NOT NULL DEFAULT 0 comment '是否为默认地址',
    gmt_created      TIMESTAMP              DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified     TIMESTAMP              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 商品
create table Product
(
    product_id          BIGINT        NOT NULL primary key comment '商品ID',
    product_code        NVARCHAR(64) comment '商品编码',
    product_name        NVARCHAR(256) NOT NULL comment '商品名称',
    product_description text          NULL comment '商品详情',
    product_state       tinyINT       NOT NULL DEFAULT 1 comment '商品状态 1上架 0下架',
    product_sort        INT           NOT NULL DEFAULT 1 comment '排序字段',
    product_type_1      NVARCHAR(64)  NULL comment '商品类型1',
    product_type_2      NVARCHAR(64)  NULL comment '商品类型2',
    gmt_created         TIMESTAMP              DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified        TIMESTAMP              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
);

-- 商品URL
create table Product_Url
(
    url_id       BIGINT  NOT NULL primary key comment 'url id',
    product_id   BIGINT  NOT NULL comment '商品ID',
    url_type     tinyINT NOT NULL comment 'URL类型 1主图 2轮播图',
    url_sort     INT     NOT NULL DEFAULT 1 comment '轮播图序号',
    gmt_created  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
);


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
);

create table SkuAttribute
(
    id              BIGINT        NOT NULL primary key comment '主键',
    sku_id          BIGINT        NOT NULL comment 'SKU ID',
    attribute_name  NVARCHAR(128) NOT NULL comment '分组名称',
    attribute_value NVARCHAR(128) NOT NULL comment '分组值',
    gmt_created     TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间'
);


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
);

-- 商品标签
create table Product_Have_Tag
(
    id          BIGINT NOT NULL primary key comment '主键',
    product_id  BIGINT NOT NULL comment '商品ID',
    tag_id      BIGINT NOT NULL comment '分类ID',
    gmt_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'
);

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
);