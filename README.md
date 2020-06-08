| 项目 | 端口 | 二级域名 | 描述 |
|:----|:----:|:----| :----|
|dubbo-admin|8000|dubbo.chiry.loc/dubbo/| dubbo注册管理 |
|anonyshop-user-service|8011| | 用户服务 |
|anonyshop-user-web|8012|user.anonyshop.tech| 用户web |
|anonyshop-seller-web|8020|manage.anonyshop.tech| 后台管理 |
|anonyshop-product-service|8030|  | 商品管理服务 | 
|anonyshop-search-web|8040|search.anonyshop.tech| 搜索web |
|anonyshop-search-service|8050| | 搜索service |
|anonyshop-item-web|8060|item.anonyshop.tech| 商品详情,首页,分类检索 |
|anonyshop-redission-test|8070|redisson.chiry.test| redisson测试 |
|anonyshop-web-test|8090|anonyshopo.tech| 网站首页 |
|anonyshop-cart-web|8100|cart.anonyshopo.tech| 购物车 |
|anonyshop-cart-service|8101| | 购物车Service |
|anonyshop-passport-web|8110| passport.anonyshop.tech | 认证中心 |
|anonyshop-order-web|8120| order.anonyshop.tech | 订单web |
|anonyshop-order-service|8121|  | 订单service |
|anonyshop-payment|8130| payment.anonyshop.tech | 支付 |
|anonyshop-store-service|8140|  | 店铺Service |
|anonyshop-chat-service|8150|  | 聊天Service |
|anonyshop-ware-service|8160|  | 库存Service |


<html xmlns:th="http://www.thymeleaf.org">

`<script src="https://cdn.jsdelivr
.net/npm/vue/dist/vue.js"></script>`  
`<a th-href="'javascript:fun(\''+${item}+'\')'"></a>`
