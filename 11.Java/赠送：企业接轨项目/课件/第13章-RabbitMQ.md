# 第13章-RabbitMQ

**学习目标：**

-   能够说出Rabbitmq应用场景
-   能够说出Rabbitmq消息不丢失解决方案
-   掌握Rabbitmq实现普通消息的收发
-   掌握Rabbitmq实现延迟消息
-   基于Rabbitmq消息队列实现商品数据同步
-   基于Rabbitmq消息队列实现订单延迟关闭

# 1、目前存在的问题

## 1.1 搜索与商品服务的问题

我们思考一下，是否存在问题？

-   **商品的原始数据保存在数据库中，增删改查都在数据库中完成**
-   **搜索服务数据来源是索引库，如果数据库商品发生变化，索引库数据不能及时更新**

如果我们在后台修改了商品的价格，搜索页面依然是旧的价格，这样显然不对。该如何解决？

这里有两种解决方案：

-   **方案1**：每当后台对商品做增删改操作，同时要修改索引库数据
-   **方案2**：搜索服务对外提供操作接口，后台在商品增删改后，调用接口

以上两种方式都有同一个严重问题：就是代码耦合，后台服务中需要嵌入搜索和商品页面服务，违背了微服务的独立原则。

所以，我们会通过另外一种方式来解决这个问题：**消息队列**

## 1.2 订单服务取消订单问题

用户下单后，如果2个小时未支付，我们该如何取消订单

-   方案1：定时任务，定时扫描未支付订单，超过2小时自动关闭
-   方案2：使用延迟队列关闭订单

## 1.3 分布式事务问题

如：用户支付订单，我们如何保证更新订单状态与扣减库存 ，三个服务数据最终一致！

# 2、消息队列解决什么问题

消息队列都解决了什么问题？

## 2.1 异步

![](image/wps1_eVFxUO9fWR.jpg)

## 2.2 解耦

​&#x9;

![](image/wps2_B0SJ7U51qE.jpg)

## 2.3 并行

![](image/wps3_FPh2pc_byj.jpg)

## 2.4 排队

![](image/wps4_mBlRcwOGua.jpg)

# 3、消息队列工具 RabbitMQ

## 3.1 常见MQ产品

-   ActiveMQ：基于JMS（Java Message Service）协议，java语言，jdk
-   RabbitMQ：基于AMQP协议，erlang语言开发，稳定性好
-   RocketMQ：基于JMS，阿里巴巴产品，目前交由Apache基金会
-   Kafka：分布式消息系统，高吞吐量

## 3.2 RabbitMQ基础概念

![](image/wps5_eMVe5UphrZ.png)

Broker：简单来说就是消息队列服务器实体

Exchange：消息交换机，它指定消息按什么规则，路由到哪个队列

Queue：消息队列载体，每个消息都会被投入到一个或多个队列

Binding：绑定，它的作用就是把 exchange和 queue按照路由规则绑定起来

Routing Key：路由关键字， exchange根据这个关键字进行消息投递

vhost：虚拟主机，一个 broker里可以开设多个 vhost，用作不同用户的权限分离

producer：消息生产者，就是投递消息的程序

consumer：消息消费者，就是接受消息的程序

channel：消息通道，在客户端的每个连接里，可建立多个 channel，每个 channel代表一个会话任务

## 3.3 安装RabbitMQ

看电商软件环境安装.doc

访问IP地址：[http://192.168.200.128:15672](http://192.168.200.128:15672 "http://192.168.200.128:15672")

-   用户名：admin
-   密码：admin

## 3.4 五种消息模型

RabbitMQ提供了6种消息模型，但是第6种其实是RPC，并不是MQ，因此不予学习。那么也就剩下5种。

但是其实3、4、5这三种都属于订阅模型，只不过进行路由的方式不同。

![](image/wps6_2OiaOvtigu.jpg)

-   基本消息模型：生产者–>队列–>消费者
-   work消息模型：生产者–>队列–>多个消费者竞争消费
-   订阅模型-Fanout：广播模式，将消息交给所有绑定到交换机的队列，每个消费者都会收到同一条消息
-   **订阅模型-Direct：定向，把消息交给符合指定 rotingKey 的队列**
-   订阅模型-Topic 主题模式：通配符，把消息交给符合routing pattern（路由模式） 的队列

我们项目使用的是第四种!

## 3.5 搭建mq测试环境service-mq

### 3.5.1 搭建service-mq服务

在`gmall_service`模块下新建模块：service-mq

![](image/image-20221208224406411_V-l9xxTYmL.png)

### 3.5.2 启动类

```java
package com.atguigu.gmall;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源自动配置
@EnableDiscoveryClient
public class MqDemoApp {

   public static void main(String[] args) {
      SpringApplication.run(MqDemoApp.class, args);
   }
}
```

### 3.5.3 添加配置文件

在`resources`目录下新建配置文件：bootstrap.properties

```.properties
spring.application.name=service-mq
spring.profiles.active=dev
spring.cloud.nacos.discovery.server-addr=192.168.200.128:8848
spring.cloud.nacos.config.server-addr=192.168.200.128:8848
spring.cloud.nacos.config.prefix=${spring.application.name}
spring.cloud.nacos.config.file-extension=yaml
spring.cloud.nacos.config.shared-configs[0].data-id=common.yaml
```

Nacos配置中心提供`common.yaml`配置文件，说明：**rabbitmq默认端口**5672

# 4、消息不丢失

消息的不丢失，在MQ角度考虑，一般有三种途径：

1.  生产者不丢数据
2.  MQ服务器不丢数据
3.  消费者不丢数据

保证消息不丢失有两种实现方式：

-   开启事务模式
-   消息息确认模式（生产者，消费者）

说明：开启事务会大幅降低消息发送及接收效率，使用的相对较少，因此我们生产环境一般都采取消息确认模式，以下我们只是讲解消息确认模式

## 4.1 消息确认

### 4.1.1 消息持久化

如果希望RabbitMQ重启之后消息不丢失，那么需要对以下3种实体均配置持久化

**Exchange**

声明exchange时设置持久化（durable = true）并且不自动删除(autoDelete = false)

**Queue**

声明queue时设置持久化（durable = true）并且不自动删除(autoDelete = false)

**message**

发送消息时通过设置deliveryMode=2持久化消息

### 4.1.2 发送确认

有时，业务处理成功，消息也发了，但是我们并不知道消息是否成功到达了rabbitmq，如果由于网络等原因导致业务成功而消息发送失败，那么发送方将出现不一致的问题，此时可以使用rabbitmq的发送确认功能，即要求rabbitmq显式告知我们消息是否已成功发送。

### 4.1.3 手动消费确认

有时，消息被正确投递到消费方，但是消费方处理失败，那么便会出现消费方的不一致问题。比如:订单已创建的消息发送到用户积分子系统中用于增加用户积分，但是积分消费方处理却都失败了，用户就会问：我购买了东西为什么积分并没有增加呢？

要解决这个问题，需要引入消费方确认，即只有消息被成功处理之后才告知rabbitmq以ack，否则告知rabbitmq以nack

## 4.2 消息确认业务封装

### 4.2.1 service-mq修改配置

开启rabbitmq消息确认配置,在common的配置文件中都已经配置好了！

```yaml
spring:
  rabbitmq:
    host: 192.168.200.128
    port: 5672
    username: admin
    password: admin
    publisher-confirm-type: correlated  #交换机的确认 异步回调ConfirmCallback
    publisher-returns: true  # 队列的确认 异步回调ReturnCallback
    listener:
      simple:
        acknowledge-mode: manual #默认情况下消息消费者是自动确认消息的，如果要手动确认消息则需要修改确认模式为manual
        prefetch: 1 # 消费者每次从队列获取的消息数量。此属性当不设置时为：轮询分发，设置为1为：公平分发
```

### 4.2.2 搭建rabbit-util模块

由于消息队列是公共模块，我们把mq的相关代码（生产者）封装到该模块，其他service微服务模块都可能使用，因此我们把他封装到一个单独的模块，需要使用mq的模块直接引用该模块即可

1.  在`gmall-common`模块下新增模块：rabbit-util  。搭建方式如common-util

![](image/image-20221208225353770_zpvK6EWB24.png)

1.  pom.xml
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <parent>
            <artifactId>gmall-common</artifactId>
            <groupId>com.atguigu.gmall</groupId>
            <version>1.0</version>
        </parent>
        <modelVersion>4.0.0</modelVersion>

        <artifactId>rabbit-util</artifactId>

        <dependencies>
            <!--AMQP依赖，包含RabbitMQ-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-amqp</artifactId>
            </dependency>
        </dependencies>
    </project>
    ```
2.  在`rabbit-util`中提供常量类 MqConst
    ```java
    package com.atguigu.gmall.common.constant;

    public class MqConst {
        /**
         * 消息补偿
         */
        public static final String MQ_KEY_PREFIX = "mq:list";
        public static final int RETRY_COUNT = 3;
        /**
         * 商品上下架
         */
        public static final String EXCHANGE_DIRECT_GOODS = "exchange.direct.goods";
        public static final String ROUTING_GOODS_UPPER = "goods.upper";
        public static final String ROUTING_GOODS_LOWER = "goods.lower";
        //队列
        public static final String QUEUE_GOODS_UPPER  = "queue.goods.upper";
        public static final String QUEUE_GOODS_LOWER  = "queue.goods.lower";
        /**
         * 取消订单，发送延迟队列
         */
        public static final String EXCHANGE_DIRECT_ORDER_CANCEL = "exchange.direct.order.cancel";//"exchange.direct.order.create" test_exchange;
        public static final String ROUTING_ORDER_CANCEL = "order.create";
        //延迟取消订单队列
        public static final String QUEUE_ORDER_CANCEL  = "queue.order.cancel";
        //取消订单 延迟时间 单位：秒 真实业务
        public static final int DELAY_TIME  = 24*60*60;
        //  测试取消订单
        // public static final int DELAY_TIME  = 3;
        /**
         * 订单支付
         */
        public static final String EXCHANGE_DIRECT_PAYMENT_PAY = "exchange.direct.payment.pay";
        public static final String ROUTING_PAYMENT_PAY = "payment.pay";
        //队列
        public static final String QUEUE_PAYMENT_PAY  = "queue.payment.pay";

        /**
         * 减库存
         */
        public static final String EXCHANGE_DIRECT_WARE_STOCK = "exchange.direct.ware.stock";
        public static final String ROUTING_WARE_STOCK = "ware.stock";
        //队列
        public static final String QUEUE_WARE_STOCK  = "queue.ware.stock";
        /**
         * 减库存成功，更新订单状态
         */
        public static final String EXCHANGE_DIRECT_WARE_ORDER = "exchange.direct.ware.order";
        public static final String ROUTING_WARE_ORDER = "ware.order";
        //队列
        public static final String QUEUE_WARE_ORDER  = "queue.ware.order";

        /**
         * 关闭交易
         */
        public static final String EXCHANGE_DIRECT_PAYMENT_CLOSE = "exchange.direct.payment.close";
        public static final String ROUTING_PAYMENT_CLOSE = "payment.close";
        //队列
        public static final String QUEUE_PAYMENT_CLOSE  = "queue.payment.close";
        /**
         * 定时任务
         */
        public static final String EXCHANGE_DIRECT_TASK = "exchange.direct.task";
        public static final String ROUTING_TASK_1 = "seckill.task.1";
        //队列
        public static final String QUEUE_TASK_1  = "queue.task.1";
        /**
         * 秒杀
         */
        public static final String EXCHANGE_DIRECT_SECKILL_USER = "exchange.direct.seckill.user";
        public static final String ROUTING_SECKILL_USER = "seckill.user";
        //队列
        public static final String QUEUE_SECKILL_USER  = "queue.seckill.user";

        /**
         * 定时任务
         */

        public static final String ROUTING_TASK_18 = "seckill.task.18";
        //队列
        public static final String QUEUE_TASK_18  = "queue.task.18";


    }
    ```

### 4.2.3 封装发送端消息确认

在`rabbit-util`中添加类

```java
package com.atguigu.gmall.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description 消息发送确认-确保生产者消息不丢失
 * <p>
 * ConfirmCallback  只确认消息是否正确到达 Exchange 中
 * ReturnCallback   消息没有正确到达队列时触发回调，如果正确到达队列不执行
 * <p>
 * 1. 如果消息没有到exchange,则confirm回调,ack=false
 * 2. 如果消息到达exchange,则confirm回调,ack=true
 * 3. exchange到queue成功,则不回调return
 * 4. exchange到queue失败,则回调return
 */
@Slf4j
@Component
public class MQProducerAckConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 应用启动后触发一次
     */
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 只确认消息是否正确到达 Exchange 中,成功与否都会回调
     *
     * @param correlationData 相关数据  非消息本身业务数据
     * @param ack             应答结果
     * @param cause           如果发送消息到交换器失败，错误原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            //消息到交换器成功
            log.info("消息发送到Exchange成功：{}", correlationData);
        } else {
            //消息到交换器失败
            log.error("消息发送到Exchange失败：{}", cause);
        }
    }


    /**
     * 消息没有正确到达队列时触发回调，如果正确到达队列不执行
     * @param message 消息对象
     * @param replyCode 应答码
     * @param replyText 应答提示信息
     * @param exchange 交换器
     * @param routingKey 路由键
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息路由queue失败，应答码={}，原因={}，交换机={}，路由键={}，消息={}",
                replyCode, replyText, exchange, routingKey, message.toString());
    }
}
```

### 4.2.4 封装消息发送

在`rabbit-util` 中添加类

```java
package com.atguigu.gmall.common.service;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     *  发送消息
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息
     */
    public boolean sendMessage(String exchange, String routingKey, Object message) {

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }

}
```

### 4.2.5 发送确认消息测试

在`service-mq`引入`rabbit-util`模块依赖

```xml
<dependencies>
    <dependency>
        <groupId>com.atguigu.gmall</groupId>
        <artifactId>rabbit-util</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

在`service-mq`编写测试代码

消息发送端

```java
package com.atguigu.gmall.mq.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mq")
public class MqController {


   @Autowired
   private RabbitService rabbitService;


   /**
    * 消息发送
    */
   //http://localhost:8282/mq/sendConfirm
   @GetMapping("sendConfirm")
   public Result sendConfirm() {
      rabbitService.sendMessage("exchange.confirm", "routing.confirm", "来人了，开始接客吧！");
      return Result.ok();
   }
}

```

消息接收端

在service-mq 中编写

```java
package com.atguigu.gmall.mq.receiver;


import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConfirmReceiver {

    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue.confirm"),
            exchange = @Exchange(value = "exchange.confirm"),
            key = "routing.confirm"))
    public void process(Message message, Channel channel) {
        System.out.println("RabbitListener:" + new String(message.getBody()));

        // false 确认一个消息，true 批量确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
```

测试：[http://localhost:8282/api/mq/sendConfirm](http://localhost:8282/api/mq/sendConfirm "http://localhost:8282/api/mq/sendConfirm")

### 4.2.6 消息发送失败，设置重发机制

实现思路：借助redis来实现重发机制

1.  在`rabbit-util` 模块中添加依赖
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
    </dependency>
    ```
2.  `rabbit-util`模块中自定义一个实体类来接收消息
    ```java
    package com.atguigu.gmall.common.model;

    import lombok.Data;
    import org.springframework.amqp.rabbit.connection.CorrelationData;

    @Data
    public class GmallCorrelationData extends CorrelationData {

        //  默认有一个消息唯一表示的Id   private volatile String id;
        //  消息主体
        private Object message;
        //  交换机
        private String exchange;
        //  路由键
        private String routingKey;
        //  重试次数
        private int retryCount = 0;
        //  消息类型  是否是延迟消息
        private boolean isDelay = false;
        //  延迟时间
        private int delayTime = 10;
    }
    ```
3.  修改`rabbit-util`中`RabbitService`中发送方法：sendMessage() 修改发送方法
    ```java
    package com.atguigu.gmall.common.service;

    import com.alibaba.fastjson.JSON;
    import com.atguigu.gmall.common.model.GmallCorrelationData;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.amqp.rabbit.core.RabbitTemplate;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.stereotype.Component;

    import java.util.UUID;
    import java.util.concurrent.TimeUnit;

    /**
     * 其他模块直接调用该对象中发送消息方法即可，内部确保消息正常发送-确保消息被发送成功
     *
     * @author: atguigu
     * @create: 2023-01-13 11:20
     */
    @Slf4j
    @Component
    public class RabbitService {

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @Autowired
        private RedisTemplate redisTemplate;


        /**
         * 用于其他微服务发送消息工具方法
         *
         * @param exchange
         * @param routingKey
         * @param message
         * @return
         */
        public Boolean sendMessage(String exchange, String routingKey, Object message) {
            //1.创建自定义相关消息对象-包含业务数据本身，交换器名称，路由键，队列类型，延迟时间,重试次数
            GmallCorrelationData correlationData = new GmallCorrelationData();
            String uuid = "mq:" + UUID.randomUUID().toString().replaceAll("-", "");
            correlationData.setId(uuid);
            correlationData.setMessage(message);
            correlationData.setExchange(exchange);
            correlationData.setRoutingKey(routingKey);
            //2.将相关消息封装到发送消息方法中

            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);

            //3.将相关消息存入Redis  Key：UUID  相关消息对象  10 分钟
            redisTemplate.opsForValue().set(uuid, JSON.toJSONString(correlationData), 10, TimeUnit.MINUTES);

            //log.info("生产者发送消息成功：{}，{}，{}", exchange, routingKey, message);
            return true;
        }
    }
    ```
4.  发送失败调用重发方法 MQProducerAckConfig 类中修改confirm方法
    ```java
    package com.atguigu.gmall.common.config;

    import com.alibaba.fastjson.JSON;
    import com.atguigu.gmall.common.model.GmallCorrelationData;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.amqp.core.Message;
    import org.springframework.amqp.rabbit.connection.CorrelationData;
    import org.springframework.amqp.rabbit.core.RabbitTemplate;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.stereotype.Component;

    import javax.annotation.PostConstruct;
    import java.util.concurrent.TimeUnit;

    /**
     * @Description 消息发送确认-确保生产者消息不丢失
     * <p>
     * ConfirmCallback  只确认消息是否正确到达 Exchange 中
     * ReturnCallback   消息没有正确到达队列时触发回调，如果正确到达队列不执行
     * <p>
     * 1. 如果消息没有到exchange,则confirm回调,ack=false
     * 2. 如果消息到达exchange,则confirm回调,ack=true
     * 3. exchange到queue成功,则不回调return
     * 4. exchange到queue失败,则回调return
     */
    @Slf4j
    @Component
    public class MQProducerAckConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @Autowired
        private RedisTemplate redisTemplate;

        /**
         * 应用启动后触发一次
         */
        @PostConstruct
        public void init() {
            rabbitTemplate.setConfirmCallback(this);
            rabbitTemplate.setReturnCallback(this);
        }

        /**
         * 只确认消息是否正确到达 Exchange 中,成功与否都会回调
         *
         * @param correlationData 相关数据  非消息本身业务数据
         * @param ack             应答结果
         * @param cause           如果发送消息到交换器失败，错误原因
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            if (ack) {
                //消息到交换器成功
                log.info("消息发送到Exchange成功：{}", correlationData);
            } else {
                //消息到交换器失败
                log.error("消息发送到Exchange失败：{}", cause);
                //执行消息重发
                this.retrySendMsg(correlationData);
            }
        }


        /**
         * 消息没有正确到达队列时触发回调，如果正确到达队列不执行
         *
         * @param message    消息对象，包含相关对象唯一标识
         * @param replyCode  应答码
         * @param replyText  应答提示信息
         * @param exchange   交换器
         * @param routingKey 路由键
         */
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            log.error("消息路由queue失败，应答码={}，原因={}，交换机={}，路由键={}，消息={}",
                    replyCode, replyText, exchange, routingKey, message.toString());
            //当路由队列失败 也需要重发
            //1.构建相关数据对象
            String redisKey = message.getMessageProperties().getHeader("spring_returned_message_correlation");
            String correlationDataStr = (String) redisTemplate.opsForValue().get(redisKey);
            GmallCorrelationData gmallCorrelationData = JSON.parseObject(correlationDataStr, GmallCorrelationData.class);
            //2.调用消息重发方法
            this.retrySendMsg(gmallCorrelationData);
        }


        /**
         * 消息重新发送
         *
         * @param correlationData
         */
        private void retrySendMsg(CorrelationData correlationData) {
            //获取相关数据
            GmallCorrelationData gmallCorrelationData = (GmallCorrelationData) correlationData;

            //获取redis中存放重试次数
            //先重发，在写会到redis中次数
            int retryCount = gmallCorrelationData.getRetryCount();
            if (retryCount >= 3) {
                //超过最大重试次数
                log.error("生产者超过最大重试次数，将失败的消息存入数据库用人工处理；给管理员发送邮件；给管理员发送短信；");
                return;
            }
            //重发消息
            rabbitTemplate.convertAndSend(gmallCorrelationData.getExchange(), gmallCorrelationData.getRoutingKey(), gmallCorrelationData.getMessage(), gmallCorrelationData);
            //重发次数+1
            retryCount += 1;
            gmallCorrelationData.setRetryCount(retryCount);
            redisTemplate.opsForValue().set(gmallCorrelationData.getId(), JSON.toJSONString(gmallCorrelationData), 10, TimeUnit.MINUTES);
            log.info("进行消息重发！");
        }
    }

    ```

测试： 修改路由键或交换机 -- 完美!

## 4.3 改造商品搜索上下架

### 4.3.1 service-list与service-product引入依赖与配置

分别在`商品微服务`跟`搜索微服务`模块导入以下依赖

```xml
<!--rabbitmq消息队列-->
<dependency>
   <groupId>com.atguigu.gmall</groupId>
   <artifactId>rabbit-util</artifactId>
   <version>1.0</version>
</dependency>
```

### 4.3.2 service-product发送消息

我在商品上架与商品添加时发送消息

商品上架业务实现类SkuManageServiceImpl

```java
@Autowired
private RabbitService rabbitService;

@Override
@Transactional
public void onSale(Long skuId) {
    //  数据库发生了变化；则需要保证缓存数据一致！  mysql 与 redis 数据同步！
    //  sku:[21]:info
    String skuKey = RedisConst.SKUKEY_PREFIX+"["+skuId+"]"+RedisConst.SKUKEY_SUFFIX;
    this.redisTemplate.delete(skuKey);
    //  update sku_info set is_sale = 1 where sku_id = ?;
    SkuInfo skuInfo = new SkuInfo();
    skuInfo.setId(skuId);
    skuInfo.setIsSale(1);
    skuInfoMapper.updateById(skuInfo);

    //  睡眠.
    try {
        Thread.sleep(300);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    //  再删除.
    this.redisTemplate.delete(skuKey);
    //  商品上架：
    //  发送的消息内容是谁?  是由消费者决定的！
    this.rabbitService.sendMsg(MqConst.EXCHANGE_DIRECT_GOODS,MqConst.ROUTING_GOODS_UPPER,skuId);
}
```

商品下架

```java
@Override
public void cancelSale(Long skuId) {
    //  组成缓存的key;
    String skuKey = RedisConst.SKUKEY_PREFIX+"["+skuId+"]"+RedisConst.SKUKEY_SUFFIX;
    this.redisTemplate.delete(skuKey);
    //  update sku_info set is_sale = 0 where sku_id = ?;
    SkuInfo skuInfo = new SkuInfo();
    skuInfo.setId(skuId);
    skuInfo.setIsSale(0);
    skuInfoMapper.updateById(skuInfo);
    //  睡眠.
    try {
        Thread.sleep(300);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    //  再删除.
    this.redisTemplate.delete(skuKey);
    //  商品下架：
    //  发送的消息内容是谁?  是由消费者决定的！
    this.rabbitService.sendMsg(MqConst.EXCHANGE_DIRECT_GOODS,MqConst.ROUTING_GOODS_LOWER,skuId);
}
```

### 4.3.4 service-list消费消息

```java
package com.atguigu.gmall.list.receiver;

import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.list.service.SearchService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: atguigu
 * @create: 2023-01-13 14:19
 */
@Slf4j
@Component
public class ListReceiver {

    @Autowired
    private SearchService searchService;

    /**
     * 商品上架监听器
     *
     * @param skuId
     * @param message
     * @param channel
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    exchange = @Exchange(MqConst.EXCHANGE_DIRECT_GOODS),
                    value = @Queue(value = MqConst.QUEUE_GOODS_UPPER, durable = "true"),
                    key = MqConst.ROUTING_GOODS_UPPER
            )
    )
    public void processUpperGoods(Long skuId, Message message, Channel channel) {
        try {
            //1.判断商品ID是否有值
            if (skuId != null) {
                log.info("【检索微服务】监听到商品上架消息：{}", skuId);
                //2.调用业务逻辑完成商品上架
                searchService.upperGoods(skuId);
                //3.手动应答rabbitmq
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("【检索微服务】，商品上架业务异常：{}", e);
        }
    }


    /**
     * 商品下架监听器
     *
     * @param skuId
     * @param message
     * @param channel
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    exchange = @Exchange(MqConst.EXCHANGE_DIRECT_GOODS),
                    value = @Queue(value = MqConst.QUEUE_GOODS_LOWER, durable = "true"),
                    key = MqConst.ROUTING_GOODS_LOWER
            )
    )
    public void processLowerGoods(Long skuId, Message message, Channel channel) {
        try {
            //1.判断商品ID是否有值
            if (skuId != null) {
                log.info("【检索微服务】监听到商品下架消息：{}", skuId);
                //2.调用业务逻辑完成商品上架
                searchService.lowerGoods(skuId);
                //3.手动应答rabbitmq
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("【检索微服务】，商品下架业务异常：{}", e);
        }
    }
}
```

### 4.3.5 测试

启动后台管理页面

[http://localhost:8888/#/product/sku/list](#/product/sku/list "http://localhost:8888/#/product/sku/list")

操作商品的上架，下架。动态更改es中的数据。

可以通过[http://192.168.200.128:5601/app/kibana#/dev\_tools/console](http://192.168.200.128:5601/app/kibana#/dev_tools/console "http://192.168.200.128:5601/app/kibana#/dev_tools/console")[http://192.168.200.128:5601/app/kibana#/dev\_tools/console](http://192.168.200.128:5601/app/kibana#/dev_tools/console "http://192.168.200.128:5601/app/kibana#/dev_tools/console")[?\_ g=()](http://192.168.200.128:5601/app/kibana#/dev_tools/console?_g=\(\) "?_ g=()")[?\_ g=()](http://192.168.200.128:5601/app/kibana#/dev_tools/console?_g=\(\) "?_ g=()") 观察功能是否实现！

# 5、延迟消息

延迟消息有两种实现方案：

1，基于死信队列

2，集成延迟插件

## 5.1 基于死信实现延迟消息

使用RabbitMQ来实现延迟消息必须先了解RabbitMQ的两个概念：消息的TTL和死信Exchange，通过这两者的组合来实现延迟队列

### 5.1.1 消息的TTL（Time To Live）

消息的TTL就是消息的存活时间。RabbitMQ可以对队列和消息分别设置TTL。对队列设置就是队列没有消费者连着的保留时间，也可以对每一个单独的消息做单独的设置。超过了这个时间，我们认为这个消息就死了，称之为死信。

如何设置TTL：

我们创建一个队列queue.temp，在Arguments 中添加x-message-ttl 为5000 （单位是毫秒），那所在压在这个队列的消息在5秒后会消失。

### 5.1.2 死信交换机  Dead Letter Exchanges

一个消息在满足如下条件下，会进死信路由，记住这里是路由而不是队列，一个路由可以对应很多队列。

（1） 一个消息被Consumer拒收了，并且reject方法的参数里requeue是false。也就是说不会被再次放在队列里，被其他消费者使用。

（2）**上面的消息的TTL到了，消息过期了。**

（3）队列的长度限制满了。排在前面的消息会被丢弃或者扔到死信路由上。

Dead Letter Exchange其实就是一种普通的exchange，和创建其他exchange没有两样。只是在某一个设置Dead Letter Exchange的队列中有消息过期了，会自动触发消息的转发，发送到Dead Letter Exchange中去。

![](image/wps7_RzfUtKxf1h.jpg)

我们现在可以测试一下延迟队列。

（1）创建死信队列

（2）创建交换机

（3）建立交换器与队列之间的绑定

（4）创建队列

### 5.1.3 代码实现

#### 5.1.3.1 在service-mq 中添加配置类

```java
package com.atguigu.gmall.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class DeadLetterMqConfig {
    // 声明一些变量

    public static final String exchange_dead = "exchange.dead";
    public static final String routing_dead_1 = "routing.dead.1";
    public static final String routing_dead_2 = "routing.dead.2";
    public static final String queue_dead_1 = "queue.dead.1";
    public static final String queue_dead_2 = "queue.dead.2";

    // 定义交换机
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange_dead, true, false, null);
    }

    @Bean
    public Queue queue1() {
        // 设置如果队列一 出现问题，则通过参数转到exchange_dead，routing_dead_2 上！
        HashMap<String, Object> map = new HashMap<>();
        // 参数绑定 此处的key 固定值，不能随意写
        map.put("x-dead-letter-exchange", exchange_dead);
        map.put("x-dead-letter-routing-key", routing_dead_2);
        // 设置延迟时间
        map.put("x-message-ttl", 10 * 1000);
        // 队列名称，是否持久化，是否独享、排外的【true:只可以在本次连接中访问】，是否自动删除，队列的其他属性参数
        return new Queue(queue_dead_1, true, false, false, map);
    }

    @Bean
    public Binding binding() {
        // 将队列一 通过routing_dead_1 key 绑定到exchange_dead 交换机上
        return BindingBuilder.bind(queue1()).to(exchange()).with(routing_dead_1);
    }

    // 这个队列二就是一个普通队列
    @Bean
    public Queue queue2() {
        return new Queue(queue_dead_2, true, false, false, null);
    }

    // 设置队列二的绑定规则
    @Bean
    public Binding binding2() {
        // 将队列二通过routing_dead_2 key 绑定到exchange_dead交换机上！
        return BindingBuilder.bind(queue2()).to(exchange()).with(routing_dead_2);
    }
}
```

#### 5.1.3.2 配置发送消息

`service-mq`模块 MqController

```java
/**
 * 消息发送延迟消息：基于死信实现
 */
@GetMapping("/sendDeadLetterMsg")
public Result sendDeadLetterMsg() {
    rabbitService.sendMessage(DeadLetterMqConfig.exchange_dead, DeadLetterMqConfig.routing_dead_1, "我是延迟消息");
    log.info("基于死信发送延迟消息成功");
    return Result.ok();
}
```

#### 5.1.3.3 消息接收方

`service-mq`模块

```java
package com.atguigu.gmall.mq.receiver;

import com.atguigu.gmall.mq.config.DeadLetterMqConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: atguigu
 * @create: 2023-01-28 10:58
 */
@Slf4j
@Component
public class DeadLetterReceiver {


    /**
     * 监听延迟消息
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {DeadLetterMqConfig.queue_dead_2})
    public void getDeadLetterMsg(String msg, Message message, Channel channel) {
        try {
            if (StringUtils.isNotBlank(msg)) {
                log.info("死信消费者：{}", msg);
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("[xx服务]监听xxx业务异常：{}", e);
        }
    }
}
```

## 5.2 基于延迟插件实现延迟消息

Rabbitmq实现了一个插件x-delay-message来实现延时队列

### 5.2.1 插件安装

1.  首先我们将刚下载下来的rabbitmq\_delayed\_message\_exchange-3.9.0.ez文件上传到RabbitMQ所在服务器，下载地址：[https://www.rabbitmq.com/community-plugins.html](https://www.rabbitmq.com/community-plugins.html "https://www.rabbitmq.com/community-plugins.html")
2.  切换到插件所在目录，执行命令，将刚插件拷贝到容器内plugins目录下
    ```docker
    docker cp rabbitmq_delayed_message_exchange-3.9.0.ez gmalldocker_rabbitmq_1:/plugins
    ```
3.  执行 docker exec -it gmalldocker\_rabbitmq\_1 /bin/bash 命令进入到容器内部，并 cd plugins 进入plugins目录
4.  执行 ls -l|grep delay  命令查看插件是否copy成功
5.  在容器内plugins目录下，执行 rabbitmq-plugins enable rabbitmq\_delayed\_message\_exchange  命令启用插件
6.  exit命令退出RabbitMQ容器内部，然后执行 docker restart gmalldocker\_rabbitmq\_1 命令重启RabbitMQ容器

### 5.2.2 代码实现

在`service-mq` 中添加类，配置队列

```java
  package com.atguigu.gmall.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayedMqConfig {

    public static final String exchange_delay = "exchange.delay";
    public static final String routing_delay = "routing.delay";
    public static final String queue_delay_1 = "queue.delay.1";

    @Bean
    public Queue delayQeue1() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(queue_delay_1, true);
    }

    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(exchange_delay, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding delayBbinding1() {
        return BindingBuilder.bind(delayQeue1()).to(delayExchange()).with(routing_delay).noargs();
    }
}
```

MqController：发送消息

```java
@Autowired
private RabbitTemplate rabbitTemplate;

/**
 * 消息发送延迟消息：基于延迟插件使用,使用插件后交换机会暂存消息固交换器无法即时路由消息到队列
 */
//@GetMapping("/sendDelayMsg")
//public Result sendDelayMsg() {
//    rabbitTemplate.convertAndSend(DelayedMqConfig.exchange_delay,
//            DelayedMqConfig.routing_delay,
//            "基于延迟插件-我是延迟消息",
//            (message -> {
//                //设置消息ttl
//                message.getMessageProperties().setDelay(10000);
//                return message;
//            })
//    );
//    log.info("基于延迟插件-发送延迟消息成功");
//    return Result.ok();
//}


/**
 * 消息发送延迟消息：基于延迟插件使用
 */
@GetMapping("/sendDelayMsg")
public Result sendDelayMsg() {
    //调用工具方法发送延迟消息
    int delayTime = 10;
    rabbitService.sendDealyMessage(DelayedMqConfig.exchange_delay, DelayedMqConfig.routing_delay, "我是延迟消息", delayTime);
    log.info("基于延迟插件-发送延迟消息成功");
    return Result.ok();
}
```

`rabbit-util`中`RabbitService`中封装发送延迟消息方法,队列确认方法中增加延迟队列判断

```java
/**
 * 发送延迟消息方法
 * @param exchange 交换机
 * @param routingKey 路由键
 * @param message 消息数据
 * @param delayTime 延迟时间，单位为：秒
 */
public boolean sendDealyMessage(String exchange, String routingKey, Object message, int delayTime) {
    //1.创建自定义相关消息对象-包含业务数据本身，交换器名称，路由键，队列类型，延迟时间,重试次数
    GmallCorrelationData correlationData = new GmallCorrelationData();
    String uuid = "mq:" + UUID.randomUUID().toString().replaceAll("-", "");
    correlationData.setId(uuid);
    correlationData.setMessage(message);
    correlationData.setExchange(exchange);
    correlationData.setRoutingKey(routingKey);
    correlationData.setDelay(true);
    correlationData.setDelayTime(delayTime);

    //2.将相关消息封装到发送消息方法中
    rabbitTemplate.convertAndSend(exchange, routingKey, message,message1 -> {
        message1.getMessageProperties().setDelay(delayTime*1000);
        return message1;
    }, correlationData);

    //3.将相关消息存入Redis  Key：UUID  相关消息对象  10 分钟
    redisTemplate.opsForValue().set(uuid, JSON.toJSONString(correlationData), 10, TimeUnit.MINUTES);
    return true;

}
```

`MQProducerAckConfig`队列确认增加延迟消息判断

```java
/**
 * 消息没有正确到达队列时触发回调，如果正确到达队列不执行
 *
 * @param message    消息对象，包含相关对象唯一标识
 * @param replyCode  应答码
 * @param replyText  应答提示信息
 * @param exchange   交换器
 * @param routingKey 路由键
 */
@Override
public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
    log.error("消息路由queue失败，应答码={}，原因={}，交换机={}，路由键={}，消息={}",
            replyCode, replyText, exchange, routingKey, message.toString());
    //当路由队列失败 也需要重发
    //1.构建相关数据对象
    String redisKey = message.getMessageProperties().getHeader("spring_returned_message_correlation");
    String correlationDataStr = (String) redisTemplate.opsForValue().get(redisKey);
    GmallCorrelationData gmallCorrelationData = JSON.parseObject(correlationDataStr, GmallCorrelationData.class);
    //todo 方式一:如果不考虑延迟消息重发 直接返回
    if(gmallCorrelationData.isDelay()){
        return;
    }
    //2.调用消息重发方法
    this.retrySendMsg(gmallCorrelationData);
}


/**
 * 消息重新发送
 *
 * @param correlationData
 */
private void retrySendMsg(CorrelationData correlationData) {
    //获取相关数据
    GmallCorrelationData gmallCorrelationData = (GmallCorrelationData) correlationData;

    //获取redis中存放重试次数
    //先重发，在写会到redis中次数
    int retryCount = gmallCorrelationData.getRetryCount();
    if (retryCount >= 3) {
        //超过最大重试次数
        log.error("生产者超过最大重试次数，将失败的消息存入数据库用人工处理；给管理员发送邮件；给管理员发送短信；");
        return;
    }
    //重发次数+1
    retryCount += 1;
    gmallCorrelationData.setRetryCount(retryCount);
    redisTemplate.opsForValue().set(gmallCorrelationData.getId(), JSON.toJSONString(gmallCorrelationData), 10, TimeUnit.MINUTES);
    log.info("进行消息重发！");
    //重发消息
    //todo 方式二：如果是延迟消息，依然需要设置消息延迟时间
    if (gmallCorrelationData.isDelay()) {
        //延迟消息
        rabbitTemplate.convertAndSend(gmallCorrelationData.getExchange(), gmallCorrelationData.getRoutingKey(), gmallCorrelationData.getMessage(), message -> {
            message.getMessageProperties().setDelay(gmallCorrelationData.getDelayTime() * 1000);
            return message;
        }, gmallCorrelationData);
    } else {
        //普通消息
        rabbitTemplate.convertAndSend(gmallCorrelationData.getExchange(), gmallCorrelationData.getRoutingKey(), gmallCorrelationData.getMessage(), gmallCorrelationData);
    }
}
```

接收消息,消费者端判断是否需要做幂等性处理

```java
package com.atguigu.gmall.mq.receiver;

import com.atguigu.gmall.mq.config.DelayedMqConfig;
import com.mongodb.internal.connection.ConcurrentPool;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author: atguigu
 * @create: 2023-01-28 11:24
 */
@Slf4j
@Component
public class DelayReceiver {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 监听到延迟消息
     *
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = DelayedMqConfig.queue_delay_1)
    public void getDelayMsg(String msg, Message message, Channel channel) {
        String key = "mq:" + msg;
        try {
            //如果业务保证幂等性，基于redis setnx保证
            Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, "", 2, TimeUnit.SECONDS);
            if (!flag) {
                //说明该业务数据以及被执行
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            if (StringUtils.isNotBlank(msg)) {
                log.info("延迟插件监听消息：{}", msg);
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("异常：{}", e);
            redisTemplate.delete(key);
        }
    }
}
```

消费结果会发送三次，也被消费三次！

如何保证消息幂等性？

1.  使用数据库方式
2.  **使用redis setnx 命令解决（推荐）**

## 5.3 基于延迟插件实现取消订单

service-order模块

### 5.3.1 业务配置与接口封装

rabbit-util模块已配置常量MqConst

```java
/**
 * 取消订单，发送延迟队列
 */
public static final String EXCHANGE_DIRECT_ORDER_CANCEL = "exchange.direct.order.cancel";//"exchange.direct.order.create" test_exchange;
public static final String ROUTING_ORDER_CANCEL = "order.create";
//延迟取消订单队列
public static final String QUEUE_ORDER_CANCEL  = "queue.order.cancel";
//取消订单 延迟时间 单位：秒
public static final int DELAY_TIME  = 10;
```

### 5.3.2 改造订单service-order模块

`service-order`模块添加依赖

```xml
<!--rabbitmq工具模块-->
<dependency>
    <groupId>com.atguigu.gmall</groupId>
    <artifactId>rabbit-util</artifactId>
    <version>1.0</version>
</dependency>
```

配置队列

```java
package com.atguigu.gmall.order.receiver;


import com.atguigu.gmall.common.constant.MqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderCanelMqConfig {

    @Bean
    public Queue delayQueue() {
        // 第一个参数是创建的queue的名字，第二个参数是是否支持持久化
        return new Queue(MqConst.QUEUE_ORDER_CANCEL, true);
    }

    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MqConst.EXCHANGE_DIRECT_ORDER_CANCEL, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding bindingDelay() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(MqConst.ROUTING_ORDER_CANCEL).noargs();
    }
}
```

### 5.3.3 发送消息

创建订单时，发送延迟消息

修改保存订单方法

```java
@Autowired
private RabbitService rabbitService;


@Override
@Transactional
public Long saveOrderInfo(OrderInfo orderInfo) {
    .....
    //发送延迟队列，如果定时未支付，取消订单
 rabbitService.sendDelayMsg(MqConst.EXCHANGE_DIRECT_ORDER_CANCEL, MqConst.ROUTING_ORDER_CANCEL, orderInfo.getId(), MqConst.DELAY_TIME);
    // 返回
    return orderInfo.getId();
}
```

### 5.3.4 接收消息

```java
package com.atguigu.gmall.order.receiver;

import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.enums.model.OrderStatus;
import com.atguigu.gmall.order.model.OrderInfo;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: atguigu
 * @create: 2023-01-28 14:23
 */
@Slf4j
@Component
public class OrderReceiver {

    @Autowired
    private OrderInfoService orderInfoService;


    /**
     * 监听关闭订单消息：将订单关闭；todo 关闭本地交易记录，支付宝交易记录
     *
     * @param orderId 订单ID
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {MqConst.QUEUE_ORDER_CANCEL})
    public void closeOrder(Long orderId, Message message, Channel channel) {
        try {
            //1.处理业务
            if (orderId != null) {
                log.info("【订单微服务】关闭订单消息：{}", orderId);
                //1.1 根据订单ID查询订单状态 状态如果是未支付：将订单状态改为关闭
                OrderInfo orderInfo = orderInfoService.getById(orderId);
                if (orderId != null && OrderStatus.UNPAID.name().equals(orderInfo.getOrderStatus()) && OrderStatus.UNPAID.name().equals(orderInfo.getProcessStatus())) {
                    //1.2 修改订单状态
                    orderInfoService.execExpiredOrder(orderId);
                }

            }
            //2.手动应答
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("【订单微服务】关闭订单业务异常：{}", e);
        }
    }
}
```

### 5.3.5 编写取消订单接口与实现类

OrderInfoService

```java
/**
 * 关闭订单
 * @param orderId
 */
void execExpiredOrder(Long orderId);

/**
 * 按照指定状态修改订单
 * @param orderId
 * @param processStatus
 */
void updateOrderStatus(Long orderId, ProcessStatus processStatus);
```

OrderInfoServiceImpl

```java
/**
 * 关闭订单
 *
 * @param orderId
 */
@Override
public void execExpiredOrder(Long orderId) {
    this.updateOrderStatus(orderId, ProcessStatus.CLOSED);
}

/**
 * 修改订单为指定状态
 *
 * @param orderId
 * @param processStatus
 */
@Override
public void updateOrderStatus(Long orderId, ProcessStatus processStatus) {
    OrderInfo orderInfo = new OrderInfo();
    orderInfo.setId(orderId);
    //订单处理状态-工作人员
    orderInfo.setProcessStatus(processStatus.name());
    //订单状态-消费者
    orderInfo.setOrderStatus(processStatus.getOrderStatus().name());
    this.updateById(orderInfo);
}
```
