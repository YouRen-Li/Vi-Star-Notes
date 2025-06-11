# 第17章-XXL-JOB

# 1、XXL\_JOB 简介

## 1.1 简介

XXL-JOB是一个分布式任务调度平台，其核心设计目标是开发迅速、学习简单、轻量级、易扩展。现已开放源代码并接入多家公司线上产品线，开箱即用。

## 1.2 特性

-   1、简单：支持通过Web页面对任务进行CRUD操作，操作简单，一分钟上手；
-   2、动态：支持动态修改任务状态、启动/停止任务，以及终止运行中任务，即时生效；
-   3、调度中心HA（中心式）：调度采用中心式设计，“调度中心”自研调度组件并支持集群部署，可保证调度中心HA；
-   4、执行器HA（分布式）：任务分布式执行，任务”执行器”支持集群部署，可保证任务执行HA；
-   5、注册中心: 执行器会周期性自动注册任务, 调度中心将会自动发现注册的任务并触发执行。同时，也支持手动录入执行器地址；
-   6、弹性扩容缩容：一旦有新执行器机器上线或者下线，下次调度时将会重新分配任务；
-   7、触发策略：提供丰富的任务触发策略，包括：Cron触发、固定间隔触发、固定延时触发、API（事件）触发、人工触发、父子任务触发；
-   8、调度过期策略：调度中心错过调度时间的补偿处理策略，包括：忽略、立即补偿触发一次等；
-   9、阻塞处理策略：调度过于密集执行器来不及处理时的处理策略，策略包括：单机串行（默认）、丢弃后续调度、覆盖之前调度；
-   10、任务超时控制：支持自定义任务超时时间，任务运行超时将会主动中断任务；
-   11、任务失败重试：支持自定义任务失败重试次数，当任务失败时将会按照预设的失败重试次数主动进行重试；其中分片任务支持分片粒度的失败重试；
-   12、任务失败告警；默认提供邮件方式失败告警，同时预留扩展接口，可方便的扩展短信、钉钉等告警方式；
-   13、路由策略：执行器集群部署时提供丰富的路由策略，包括：第一个、最后一个、轮询、随机、一致性HASH、最不经常使用、最近最久未使用、故障转移、忙碌转移等；
-   14、分片广播任务：执行器集群部署时，任务路由策略选择”分片广播”情况下，一次任务调度将会广播触发集群中所有执行器执行一次任务，可根据分片参数开发分片任务；
-   15、动态分片：分片广播任务以执行器为维度进行分片，支持动态扩容执行器集群从而动态增加分片数量，协同进行业务处理；在进行大数据量业务操作时可显著提升任务处理能力和速度。
-   16、故障转移：任务路由策略选择”故障转移”情况下，如果执行器集群中某一台机器故障，将会自动Failover切换到一台正常的执行器发送调度请求。
-   17、任务进度监控：支持实时监控任务进度；
-   18、Rolling实时日志：支持在线查看调度结果，并且支持以Rolling方式实时查看执行器输出的完整的执行日志；
-   19、GLUE：提供Web IDE，支持在线开发任务逻辑代码，动态发布，实时编译生效，省略部署上线的过程。支持30个版本的历史版本回溯。
-   20、脚本任务：支持以GLUE模式开发和运行脚本任务，包括Shell、Python、NodeJS、PHP、PowerShell等类型脚本;
-   21、命令行任务：原生提供通用命令行任务Handler（Bean任务，”CommandJobHandler”）；业务方只需要提供命令行即可；
-   22、任务依赖：支持配置子任务依赖，当父任务执行结束且执行成功后将会主动触发一次子任务的执行, 多个子任务用逗号分隔；
-   23、一致性：“调度中心”通过DB锁保证集群分布式调度的一致性, 一次任务调度只会触发一次执行；
-   24、自定义任务参数：支持在线配置调度任务入参，即时生效；
-   25、调度线程池：调度系统多线程触发调度运行，确保调度精确执行，不被堵塞；
-   26、数据加密：调度中心和执行器之间的通讯进行数据加密，提升调度信息安全性；
-   27、邮件报警：任务失败时支持邮件报警，支持配置多邮件地址群发报警邮件；
-   28、推送maven中央仓库: 将会把最新稳定版推送到maven中央仓库, 方便用户接入和使用;
-   29、运行报表：支持实时查看运行数据，如任务数量、调度次数、执行器数量等；以及调度报表，如调度日期分布图，调度成功分布图等；
-   30、全异步：任务调度流程全异步化设计实现，如异步调度、异步运行、异步回调等，有效对密集调度进行流量削峰，理论上支持任意时长任务的运行；
-   31、跨语言：调度中心与执行器提供语言无关的 RESTful API 服务，第三方任意语言可据此对接调度中心或者实现执行器。除此之外，还提供了 “多任务模式”和“httpJobHandler”等其他跨语言方案；
-   32、国际化：调度中心支持国际化设置，提供中文、英文两种可选语言，默认为中文；
-   33、容器化：提供官方docker镜像，并实时更新推送dockerhub，进一步实现产品开箱即用；
-   34、线程池隔离：调度线程池进行隔离拆分，慢任务自动降级进入”Slow”线程池，避免耗尽调度线程，提高系统稳定性；
-   35、用户管理：支持在线管理系统用户，存在管理员、普通用户两种角色；
-   36、权限控制：执行器维度进行权限控制，管理员拥有全量权限，普通用户需要分配执行器权限后才允许相关操作；

## 1.3 架构设计

### 1.3.1 设计思想

将调度行为抽象形成“调度中心”公共平台，而平台自身并不承担业务逻辑，“调度中心”负责发起调度请求。

将任务抽象成分散的JobHandler，交由“执行器”统一管理，“执行器”负责接收调度请求并执行对应的JobHandler中业务逻辑。

因此，“调度”和“任务”两部分可以相互解耦，提高系统整体稳定性和扩展性；

## 1.3.2 系统组成

**调度模块（调度中心）**

负责管理调度信息，按照调度配置发出调度请求，自身不承担业务代码。调度系统与任务解耦，提高了系统可用性和稳定性，同时调度系统性能不再受限于任务模块；
支持可视化、简单且动态的管理调度信息，包括任务新建，更新，删除，GLUE开发和任务报警等，所有上述操作都会实时生效，同时支持监控调度结果以及执行日志，支持执行器Failover。

**执行模块（执行器）**

负责接收调度请求并执行任务逻辑。任务模块专注于任务的执行等操作，开发和维护更加简单和高效； &#x20;
接收“调度中心”的执行请求、终止请求和日志请求等。

## 1.3.3 架构图

![](image/image_22ZP1InE0e.png)

## 1.3.4 下载

文档地址：[https://www.xuxueli.com/xxl-job/](https://www.xuxueli.com/xxl-job/ "https://www.xuxueli.com/xxl-job/")

源码仓库地址：

1、[https://github.com/xuxueli/xxl-job](https://github.com/xuxueli/xxl-job "https://github.com/xuxueli/xxl-job")

2、[http://gitee.com/xuxueli0323/xxl-job](http://gitee.com/xuxueli0323/xxl-job "http://gitee.com/xuxueli0323/xxl-job")

中央仓库地址

```java
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-job-core</artifactId>
    <version>2.3.0</version>
</dependency>
```

# 2 快速入门

## 2.1 初始化项目

![](image/image_pASnDOjvLY.png)

&#x20;

说明：

1.  doc：xxl-job官方文档
2.  xxl-job-admin：调度中心
3.  xxl-job-core：公共依赖
4.  xxl-job-executor-samples：执行器Sample示例（选择合适的版本执行器，可直接使用，也可以参考其并将现有项目改造成执行器）
5.  ：xxl-job-executor-sample-springboot：Springboot版本，通过Springboot管理执行器，推荐这种方式；
6.  ：xxl-job-executor-sample-frameless：无框架版本；

## 2.2 **初始化“调度数据库”**

“调度数据库初始化SQL脚本” 位置为:/xxl-job/doc/db/tables\_xxl\_job.sql，直接执行即可

## **2.3 部署“调度中心”**

1.  调度中心项目：xxl-job-admin
2.  作用：统一管理任务调度平台上调度任务，负责触发调度执行，并且提供任务管理平台。

### **2.3.1更改配置文件**

调整数据库连接：

/xxl-job/xxl-job-admin/src/main/resources/application.properties

### **2.3.2部署项目**

在idea运行项目

调度中心访问地址：[http://localhost:8080/xxl-job-admin](http://localhost:8080/xxl-job-admin "http://localhost:8080/xxl-job-admin")

默认登录账号 “admin/123456”, 登录后运行界面如下图所示

说明：到此处我们已经部署好“调度中心”，接下来我们部署“执行器”，即：需要被调用的任务

## **2.4 配置部署“执行器项目”**

我们直接使用官方示例代码：xxl-job-executor-sample-springboot，只要这个示例任务执行成功，那边我们的项目按照示例代码方式即可集成成功；

接下来我们分析示例代码集成方式：

### **2.4.1引入依赖**

确认pom文件中引入了 “xxl-job-core” 的maven依赖；

### **2.4.2 执行器配置**

执行器配置，配置文件地址：

/xxl-job/xxl-job-executor-samples/xxl-job-executor-sample-springboot/src/main/resources/application.properties

执行器配置，配置内容说明：

```.properties
# web port
server.port=8081

# log config
logging.config=classpath:logback.xml


### 调度中心部署跟地址 [选填]：如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
xxl.job.admin.addresses=http://127.0.0.1:8080/xxl-job-admin

### 执行器通讯TOKEN [选填]：非空时启用；
xxl.job.accessToken=

### 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
xxl.job.executor.appname=xxl-job-executor-sample
### 执行器注册 [选填]：优先使用该配置作为注册地址，为空时使用内嵌服务 ”IP:PORT“ 作为注册地址。从而更灵活的支持容器类型执行器动态IP和动态映射端口问题。
xxl.job.executor.address=
### 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"；
xxl.job.executor.ip=
### 执行器端口号 [选填]：小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
xxl.job.executor.port=9999
### 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
xxl.job.executor.logpath=/data/applogs/xxl-job/jobhandler
### 执行器日志文件保存天数 [选填] ： 过期日志自动清理, 限制值大于等于3时生效; 否则, 如-1, 关闭自动清理功能；
xxl.job.executor.logretentiondays=30
```

### **2.4.3 执行器组件配置**

```java

package com.xxl.job.executor.core.config;

@Configuration
public class XxlJobConfig {
    private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.executor.address}")
    private String address;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobSpringExecutor;
    }

}
```

### **2.4.4 查看任务**

示例：com.xxl.job.executor.service.jobhandler.SampleXxlJob.demoJobHandler()

```java
/**
 * 1、简单任务示例（Bean模式）
 */
@XxlJob("demoJobHandler")
public void demoJobHandler() throws Exception {
    XxlJobHelper.log("XXL-JOB, Hello World.");

    for (int i = 0; i < 5; i++) {
        XxlJobHelper.log("beat at:" + i);
        TimeUnit.SECONDS.sleep(2);
    }
  
```

### **2.4.5 启动部署**

在idea直接启动示例项目

说明：目前执行器任务我们已经部署启动，接下来我们需要在“调用中心”配置任务

## 2.**5** **配置**任务

配置任务分两个步骤：

1，在“调度中心”“执行器管理”新增执行器

2，在“调度中心”“任务管理”新增对应执行器的任务

### 2.5.1配置执行器\*\*

说明：我们初始化sql是默认已添加了一个执行器，我们可以直接使用，也可以删除重新添加，如图

![](image/image_xwWwFkfbqC.png)

添加或编辑执行器

![](image/image_Cr2iw2gPdF.png)

执行器属性说明

1.  AppName: 是每个执行器集群的唯一标示AppName, 执行器会周期性以AppName为对象进行自动注册。可通过该配置自动发现注册成功的执行器, 供任务调度时使用;
2.  名称: 执行器的名称, 因为AppName限制字母数字等组成,可读性不强, 名称为了提高执行器的可读性;
3.  注册方式：调度中心获取执行器地址的方式；
4.  自动注册：执行器自动进行执行器注册，调度中心通过底层注册表可以动态发现执行器机器地址；
5.  手动录入：人工手动录入执行器的地址信息，多地址逗号分隔，供调度中心使用；
6.  机器地址："注册方式"为"手动录入"时有效，支持人工维护执行器的地址信息；

### 2.5.2新建任务

![](image/image_Xbo4d_5WfN.png)

新增

![](image/image_k7B5PFVFg8.png)

配置属性详细说明：

1.  基础配置：

-   执行器：任务的绑定的执行器，任务触发调度时将会自动发现注册成功的执行器, 实现任务自动发现功能; 另一方面也可以方便的进行任务分组。每个任务必须绑定一个执行器, 可在 "执行器管理" 进行设置;
-   任务描述：任务的描述信息，便于任务管理；
-   负责人：任务的负责人；
-   报警邮件：任务调度失败时邮件通知的邮箱地址，支持配置多邮箱地址，配置多个邮箱地址时用逗号分隔；

1.  触发配置：

-   调度类型：

无：该类型不会主动触发调度；

CRON：该类型将会通过CRON，触发任务调度；

固定速度：该类型将会以固定速度，触发任务调度；按照固定的间隔时间，周期性触发；

固定延迟：该类型将会以固定延迟，触发任务调度；按照固定的延迟时间，从上次调度结束后开始计算延迟时间，到达延迟时间后触发下次调度；

-   CRON：触发任务执行的Cron表达式；
-   固定速度：固件速度的时间间隔，单位为秒；
-   固定延迟：固件延迟的时间间隔，单位为秒；

1.  任务配置：

-   运行模式：

BEAN模式：任务以JobHandler方式维护在执行器端；需要结合 "JobHandler" 属性匹配执行器中任务；(我们项目一般使用BEAN模式)

GLUE模式(Java)：任务以源码方式维护在调度中心；该模式的任务实际上是一段继承自IJobHandler的Java类代码并 "groovy" 源码方式维护，它在执行器项目中运行，可使用@Resource/@Autowire注入执行器里中的其他服务；

GLUE模式(Shell)：任务以源码方式维护在调度中心；该模式的任务实际上是一段 "shell" 脚本；

...

-   JobHandler：运行模式为 "BEAN模式" 时生效，对应执行器中新开发的JobHandler类“@JobHandler”注解自定义的value值；
-   执行参数：任务执行所需的参数；

### 2.5.3 BEAN模式开发\*\*

前面我们在“执行器项目”中已经看见一个示例代码，它就是BEAN模式的示例代码。

```java
/**
 * 1、简单任务示例（Bean模式）
 */
@XxlJob("demoJobHandler")
public void demoJobHandler() throws Exception {
    XxlJobHelper.log("XXL-JOB, Hello World.");

    for (int i = 0; i < 5; i++) {
        XxlJobHelper.log("beat at:" + i);
        TimeUnit.SECONDS.sleep(2);
    }
    // default success
}
```

Bean模式任务，支持基于方法的开发方式，每个任务对应一个方法。

· 优点：

· 每个任务只需要开发一个方法，并添加”[@XxlJob](https://github.com/XxlJob "@XxlJob")”注解即可，更加方便、快速。

· 支持自动扫描任务并注入到执行器容器。

· 缺点：要求Spring容器环境；

## 2.6测试

1、xxl-job-executor-sample-springboot项目的demoJobHandler()设置断点

2、任务管理，操作执行一次，如：

![](image/image_F8pSwaGwPN.png)

说明：断点相应，则调试成功，后续我们可以按照corn表达式再继续测试

到次xxl\_job的示例程序我们就联调成功了，步骤很繁琐，其实很简单。

## 2.7总结

如果我们要讲xxl\_job集成到项目中，怎么操作呢？其实就几个步骤：

1、项目引用xxl-job-core依赖

2、添加xxl-job配置

3、添加xxl-job配类：XxlJobConfig.java

4、编写BEAN模式任务类

## **2.8 整合电商项目**

将xxl-job-master 引入到项目中，并在本地创建xxl-job 数据库；

![](image/image_2duYowNgsn.png)

![](image/image_6KIxBV45lg.png)

1.  修改xxl-job-admin一些配置文件中的属性：

```.properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.mail.host=smtp.qq.com
spring.mail.port=25
spring.mail.username=243620922@qq.com
spring.mail.from=243620922@qq.com
```

1.  在service-task 模块中添加xxl-job依赖配置

```xml
<dependency>  
  <groupId>com.xuxueli</groupId>
  <artifactId>xxl-job-core</artifactId>
  <version>2.3.0</version>
</dependency>
```

1.  在service-task 添加配置类以及日志文件

logback.xml 直接从xxl-job-admin 拷贝即可

XxlJobConfig

```java
package com.atguigu.gmall.task.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
public class XxlJobConfig {
    private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.executor.address}")
    private String address;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobSpringExecutor;
    }
}
```

1.  添加控制

@XxlJob("demo") &#x20;

```java
@XxlJob("demo")
public void demo(){
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    System.out.println("hello");
}
```

1.  在service-task 配置文件中添加xxl-job 相关配置

```.properties
# log config
logging.config=classpath:logback.xml
xxl.job.admin.addresses=http://127.0.0.1:8080/xxl-job-admin
xxl.job.accessToken=
xxl.job.executor.appname=xxl-job-executor-sample
xxl.job.executor.address=
xxl.job.executor.ip=
xxl.job.executor.port=9999
xxl.job.executor.logpath=/data/applogs/xxl-job/jobhandler
xxl.job.executor.logretentiondays=30
```

1.  执行器配置信息

AppName 的值是由配置文件中的 xxl.job.executor.appname=xxl-job-executor-sample 规定的！

![](image/image_wlpViF3fzN.png)

1.  任务管理器

![](image/image_MgoDEwabw1.png)

![](image/image_zv4HQMnRw4.png)

执行一次就可以看到结果了！

