# 谷粒商城简介

### 前言

gulimall项目致力于打造一个完整的电商系统，采用现阶段流行技术来实现，采用前后端分离继续编写。

### 项目API接口文档

- 文档地址：[https://easydoc.xyz/s/78237135/ZUqEdvA4/hKJTcbfd]()

### 项目介绍

gulimall（谷粒商城） 项目是一套电商项目，包括前台商城系统以及后台管理系统，基于 SpringCloud + SpringCloudAlibaba + MyBatis-Plus实现，采用 Docker
容器化部署。前台商城系统包括：用户登录、注册、商品搜索、商品详情、购物车、下订单流程、秒杀活动等模块。后台管理系统包括：系统管理、商品系统、优惠营销、库存系统、订单系统、用户系统、内容管理等七大模块。

### 项目演示

#### **前台部分功能演示效果**

![image](https://raw.githubusercontent.com/zhttim/tim_mall/main/image/image1.png)

![image](https://raw.githubusercontent.com/zhttim/tim_mall/main/image/image2.png)

#### 后端登录界面

![image](https://raw.githubusercontent.com/zhttim/tim_mall/main/image/image6.png)

#### 主页面

![image](https://raw.githubusercontent.com/zhttim/tim_mall/main/image/image3.png)

### 组织结构

```
gulimall
├── gulimall-common -- 工具类及通用代码
├── renren-generator -- 人人开源项目的代码生成器
├── gulimall-auth-server -- 认证中心（社交登录、OAuth2.0、单点登录）
├── gulimall-cart -- 购物车服务
├── gulimall-coupon -- 优惠卷服务
├── gulimall-gateway -- 统一配置网关
├── gulimall-order -- 订单服务
├── gulimall-product -- 商品服务
├── gulimall-search -- 检索服务
├── gulimall-seckill -- 秒杀服务
├── gulimall-third-party -- 第三方服务
├── gulimall-ware -- 仓储服务
└── gulimall-member -- 会员服务

```

### 技术选型

**后端技术**

|        技术        |           说明           |                      官网                       |
| :----------------: | :----------------------: | :---------------------------------------------: |
|     SpringBoot     |       容器+MVC框架       |     https://spring.io/projects/spring-boot      |
|    SpringCloud     |        微服务架构        |     https://spring.io/projects/spring-cloud     |
| SpringCloudAlibaba |        一系列组件        | https://spring.io/projects/spring-cloud-alibaba |
|    MyBatis-Plus    |         ORM框架          |             https://mp.baomidou.com             |
|  renren-generator  | 人人开源项目的代码生成器 |   https://gitee.com/renrenio/renren-generator   |
|   Elasticsearch    |         搜索引擎         |    https://github.com/elastic/elasticsearch     |
|      RabbitMQ      |         消息队列         |            https://www.rabbitmq.com             |
|   Springsession    |        分布式缓存        |    https://projects.spring.io/spring-session    |
|      Redisson      |         分布式锁         |      https://github.com/redisson/redisson       |
|       Docker       |       应用容器引擎       |             https://www.docker.com              |
|        OSS         |        对象云存储        |  https://github.com/aliyun/aliyun-oss-java-sdk  |

**前端技术**

|   技术    |    说明    |           官网            |
| :-------: | :--------: | :-----------------------: |
|    Vue    |  前端框架  |     https://vuejs.org     |
|  Element  | 前端UI框架 | https://element.eleme.io  |
| thymeleaf |  模板引擎  | https://www.thymeleaf.org |
|  node.js  | 服务端的js |   https://nodejs.org/en   |

### 架构图

**系统架构图**

![image](https://raw.githubusercontent.com/zhttim/tim_mall/main/image/image4.jpg)

**业务架构图**

![image](https://raw.githubusercontent.com/zhttim/tim_mall/main/image/image5.png)

### 环境搭建

#### 开发工具

|     工具      |        说明         |                      官网                       |
| :-----------: | :-----------------: | :---------------------------------------------: |
|     IDEA      |    开发Java程序     |     https://www.jetbrains.com/idea/download     |
| RedisDesktop  | redis客户端连接工具 |        https://redisdesktop.com/download        |
|  SwitchHosts  |    本地host管理     |       https://oldj.github.io/SwitchHosts        |
|    X-shell    |  Linux远程连接工具  | http://www.netsarang.com/download/software.html |
|    Navicat    |   数据库连接工具    |       http://www.formysql.com/xiazai.html       |
| PowerDesigner |   数据库设计工具    |             http://powerdesigner.de             |
|    Postman    |   API接口调试工具   |             https://www.postman.com             |
|    Jmeter     |    性能压测工具     |            https://jmeter.apache.org            |
|    Typora     |   Markdown编辑器    |                https://typora.io                |

#### 开发环境

|     工具      |  版本号   |                             下载                             |
| :-----------: |:------:| :----------------------------------------------------------: |
|      JDK      |  1.8   | https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html |
|     Mysql     |  5.7   |                    https://www.mysql.com                     |
|     Redis     | 6.2.6  |                  https://redis.io/download                   |
| Elasticsearch | 7.4.2  |               https://www.elastic.co/downloads               |
|    Kibana     | 7.4.2  |               https://www.elastic.co/cn/kibana               |
|   RabbitMQ    | 3.9.11 |            http://www.rabbitmq.com/download.html             |
|     Nginx     | 1.1.0  |              http://nginx.org/en/download.html               |

注意：以上的除了jdk都是采用docker方式进行安装，详细安装步骤可参考百度!!!

#### 搭建步骤

> 环境部署

- 修改本机的host文件，映射域名端口

```
192.168.110.20  gulimall.com
192.168.110.20  search.gulimall.com
192.168.110.20  item.gulimall.com
192.168.110.20  auth.gulimall.com
192.168.110.20  cart.gulimall.com
192.168.110.20  order.gulimall.com
192.168.110.20  member.gulimall.com
192.168.110.20  seckill.gulimall.com
以上端口换成自己Linux的ip地址
```

- 修改Linux中Nginx的配置文件

```
1、在nginx.conf中添加负载均衡的配置    
upstream gulimall{
      server 192.168.110.2:90;
    }
2、在gulimall.conf中添加如下配置
server {
    listen       80;
    server_name *.gulimall.com gulimall.com timhaozi.top;

    #charset koi8-r;
    #access_log  /var/log/nginx/log/host.access.log  main;

    #配置静态资源的动态分离
    location /static/ {
        root   /usr/share/nginx/html;
    }

    #支付异步回调的一个配置
    location /payed/ {
        proxy_set_header Host order.gulimall.com;        #不让请求头丢失
        proxy_pass http://gulimall;
    }

    location / {
        proxy_set_header Host $host;        #不让请求头丢失
        proxy_pass http://gulimall;
    }
```

- 克隆前端项目 `renren-fast-vue` 以 `npm run dev` 方式去运行
- 克隆整个后端项目 `gulimall` ，并导入 IDEA 中完成编译

