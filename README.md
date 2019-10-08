## Android+Java后端（Springboot，Mybatis）开源小商店项目

### 简介

豆腐小店是自己的学习项目，主要实现一个商城的下单、购物车、支付等基本功能，熟悉后端的开发过程，因为自己做Android的，然后想学一些后端的技术，所以Android端和后端(Springboot，Mybatis)一起写了。Android使用基本的MVC模式，尽可能的写得简单，因为主要还是学习后端，后端使用流行的SpringBoot+Mybatis框架，设计一个可扩展分布式架构。

### 代码链接

Android端代码：[https://github.com/luqinmao/storeClient](https://github.com/luqinmao/storeClient)

Java后端代码：[https://github.com/luqinmao/storeServer](https://github.com/luqinmao/storeServer)


### 使用的技术

**storeClient安卓端**

| 技术          | 说明                                                         |
| ------------- | :----------------------------------------------------------- |
| okgo          | 优秀的网络请求框架                                           |
| autosize      | 屏幕适配解决方案                                             |
| rxpermissions | 动态权限框架                                                 |
| alipaySdk     | 支付宝支付SDK                                                |
| 其他          | 如：butterknife、gson、oss、eventbus、BaseRecyclerViewAdapterHelper等 |

**storeServer后端**

| 技术             | 说明                |
| ---------------- | ------------------- |
| SpringBoot       | 容器+MVC框架        |
| MyBatis          | ORM框架             |
| MyBatisGenerator | 数据层代码生成      |
| PageHelper       | MyBatis物理分页插件 |
| Redis            | 分布式缓存          |
| Druid            | 数据库连接池        |
| OSS              | 阿里云对象存储      |
| Lombok           | 简化对象封装工具    |

### 项目运行

本项目启动需要依赖MySql、Redis、navicat内网穿透等服务，数据库中需要导入store.sql脚本，Android端下单支付时使用沙箱版支付宝APP进行付款。安装完相关依赖以后直接启动StoreApplication类的main函数即可。

### 项目包结构
``` lua
src
├── common -- 用于存储通用代码及工具类
├── config -- SpringBoot中的Java配置
├── controller -- 控制器层代码
|    ├── backend -- 后台管理接口
|    └── common  -- 通用未分类接口
|    └── portal  -- Android前端接口
├── dao -- 数据访问层代码
├── pojo -- 实体类
├── mbg -- MyBatisGenerator生成器相关代码
└── service -- 业务层接口代码
|   └── impl -- 业务层接口实现类代码
├── task -- 计时器相关代码
├── util -- 工具类
├── task -- 计时器相关代码
└── vo -- 
```

### 资源文件

``` lua
├── generatorConfig.xml -- MyBatisGenerator生成代码规则配置
├── application.yml     -- SpringBoot的配置文件
├── logback.xml 	    -- logback日志配置文件
├── store.properties    -- 项目的Redis、内网穿透等需要的参数文件
├── zfbinfo.properties  -- 支付宝需要的配置参数文件
└── generatorConfig.xml -- MyBatisGenerator生成代码规则配置
```



### 说明

#### 项目相关

- > 因为我没有支付宝企业账号，所以项目使用的支付宝支付为沙盒环境（测试环境），所以在安卓端试用支付功能时只能用沙箱版支付宝APP进行支付，需要在手机安装沙箱版支付宝APP，请自行下载安装，下载地址：https://openhome.alipay.com/platform/appDaily.htm?tab=tool， 买家测试账号：aemkne6182@sandbox.com  密码：111111

- 启动后端项目之前需要先启动Redis，不然会报错启动不了

- 数据库文件放在doc文件夹下面,即store.sql文件

- Android端的运行时候根据你运行在手机还是模拟器，需要求改base文件夹下AppConst文件的SERVER_ADDRESS请求地址

- 请求接口需要带上请求头login_token，值从登陆接口获得，用户测试账号：admin,密码：admin

- portal模块的controller类，不需要登录拦截的uri配置在LoginPublicUri类里面

- backend模块的controller类，登录与用户管理员权限拦截在AuthorityInterceptor里面

#### 其他相关

- 本项目学习参考了：https://coding.imooc.com/class/chapter/144.html#Anchor

- 因为本人能力有限，项目代码还存在许多不足与不好的代码，请见谅。

  



### 学习时的一些记录

+ > war包 javax/el/ELManager错误解决：错误原因是因为tomcat7中的el-api2.2 版本太低导致
  >
  > 解决方法一:直接下载一个el-api3.x.jar替换tomcat中的el-api2.2.jar
  >
  > 解决方法二,直接下载tomcat8安装解决问题

+ > 过滤器匹配：
  >
  > - 匹配com.lqm.controller包下的所有方法
  >    @Pointcut("execution(public * com.lqm.controller.*.*(..))")
  > - 匹配com.lqm.controller包及其子包下的所有方法
  >   	@Pointcut("execution(* com.lqm.controller..*.*(..))")	

+ > jackson config不生效记录：之前AuthorityInterceptorConfig 我继承WebMvcConfigurationSupport，
  > 然后jackson config  的就失效了，把之前AuthorityInterceptorConfig改为 implements WebMvcConfigurer  就可以了

+  > orderControler报错，  Caused by: java.lang.NoClassDefFoundError: com/alipay/api/AlipayApiException  
    > 解决：https://blog.csdn.net/huanjia_h/article/details/72026757   war 加入alipay相关jar包
   
+  > 启动报错：    Error creating bean with name 'gsonBuilder'
    > 更换gson版本为2.6.2 后解决	

### 项目截图

#### Android前端效果图

![](https://lqmdemo.oss-cn-beijing.aliyuncs.com/store/sp1.png)
![](https://lqmdemo.oss-cn-beijing.aliyuncs.com/store/sp2.png)
![](https://lqmdemo.oss-cn-beijing.aliyuncs.com/store/sp3.png)
![](https://lqmdemo.oss-cn-beijing.aliyuncs.com/store/sp4.png)
![](https://lqmdemo.oss-cn-beijing.aliyuncs.com/store/sp5.png)
![](https://lqmdemo.oss-cn-beijing.aliyuncs.com/store/sp6.png)

#### 项目代码图

![](https://lqmdemo.oss-cn-beijing.aliyuncs.com/store/client1.png)
![](https://lqmdemo.oss-cn-beijing.aliyuncs.com/store/server1.png)
![](https://lqmdemo.oss-cn-beijing.aliyuncs.com/store/sql1.png)
