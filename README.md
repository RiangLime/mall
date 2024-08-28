# mall

---

### Intro

mall模块为业务提供了基本的商城功能,业务可以直接引用来实现基本的电商功能,但是此模块依赖core,要使用此模块必须引入core

---

### Configs

你需要配置一些配置项来快速使用,目前支持微信支付和海外stripe支付
```yaml
-- 用户微信支付所需配置的信息
mall.pay.wx.merchant-id
mall.pay.wx.private-key-path
mall.pay.wx.merchant-serial-number
mall.pay.wx.api-v3-key
mall.pay.wx.app-id
mall.pay.wx.certificate-path
mall.pay.wx.notice-url-prefix

-- 用户海外stripe支付所需配置的信息
mall.pay.stripe.key
mall.pay.stripe.secret
mall.pay.stripe.complete-endpoint-url
```

#### 1. 商品管理

提供基本的商品管理能力,商品的基本管理和商品标签管理等

#### 2. 订单管理

提供基本的订单管理能力,用户可以管理自己的订单,创建订单、支付订单、收货、评论等

#### 3. 购物车、购物地址管理