# 第7章-CompletableFuture异步编排

**学习目标：**

-   CompletableFuture异步任务应用场景
-   掌握CompletableFuture相关API的应用
-   基于CompletableFuture+自定义线程池实现优化商品数据接口调用
-   基于CompletableFuture实现首页商品分类

# 1、CompletableFuture异步编排

问题：查询商品详情页的逻辑非常复杂，数据的获取都需要远程调用，必然需要花费更多的时间。

假如商品详情页的每个查询，需要如下标注的时间才能完成

1.  获取sku的基本信息+sku的图片信息	1s
  2.  获取商品所属三级分类0.5s
  3.  获取spu的所有销售属性1s
  4.  商品sku价格0.5s
2.  获取商品海报列表 0.5s
3.  获取商品Sku平台属性以及值 0.5s
4.  ......

那么，用户需要4s后才能看到商品详情页的内容。很显然是不能接受的。如果有多个线程同时完成这4步操作，也许只需要1.5s即可完成响应。

## 1.1 CompletableFuture介绍

Future是Java 5添加的接口，用来描述一个异步计算的结果。你可以使用isDone方法检查计算是否完成，或者使用get阻塞住调用线程，直到计算完成返回结果，你也可以使用cancel方法停止任务的执行。

在Java 8中, 新增加了一个包含50个方法左右的类: CompletableFuture，提供了非常强大的Future的扩展功能，可以帮助我们简化异步编程的复杂性，提供了函数式编程的能力，可以通过回调的方式处理计算结果，并且提供了转换和组合CompletableFuture的方法。

CompletableFuture类实现了Future接口，所以你还是可以像以前一样通过get方法阻塞或者轮询的方式获得结果，但是这种方式不推荐使用。

CompletableFuture和FutureTask同属于Future接口的实现类，都可以获取线程的执行结果。

![](image/wps1_-XFjobsDfG.jpg)

## 1.2 创建异步对象

CompletableFuture 提供了四个静态方法来创建一个异步操作。

![](image/wps2_AwgeboMrfy.jpg)

没有指定Executor的方法会使用ForkJoinPool.commonPool() 作为它的线程池执行异步代码。如果指定线程池，则使用指定的线程池运行。以下所有的方法都类同。

-   runAsync方法不支持返回值。
-   supplyAsync可以支持返回值。

## 1.3 计算完成时回调方法

当CompletableFuture的计算结果完成，或者抛出异常的时候，可以执行特定的Action。主要是下面的方法：

![](image/wps3_t_ZnvfdhfT.jpg)

-   whenComplete可以处理正常或异常的计算结果
-   exceptionally处理异常情况。BiConsumer\<? super T,? super Throwable>可以定义处理业务

**whenComplete 和 whenCompleteAsync 的区别**：

whenComplete：是执行当前任务的线程执行继续执行 whenComplete 的任务。

whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行。

方法不以Async结尾，意味着Action使用相同的线程执行，而Async可能会使用其他线程执行（如果是使用相同的线程池，也可能会被同一个线程选中执行）

代码示例：

```java
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture future = CompletableFuture.supplyAsync(new Supplier<Object>() {
            @Override
            public Object get() {
                System.out.println(Thread.currentThread().getName() + "\t completableFuture");
                int i = 10 / 0;
                return 1024;
            }
        }).whenComplete(new BiConsumer<Object, Throwable>() {
            @Override
            public void accept(Object o, Throwable throwable) {
                System.out.println("-------o=" + o.toString());
                System.out.println("-------throwable=" + throwable);
            }
        }).exceptionally(new Function<Throwable, Object>() {
            @Override
            public Object apply(Throwable throwable) {
                System.out.println("throwable=" + throwable);
                return 6666;
            }
        });
        System.out.println(future.get());
    }
}
```

## 1.4 线程串行化与并行化方法

thenApply 方法：当一个线程依赖另一个线程时，获取上一个任务返回的结果，并返回当前任务的返回值。

![](image/wps4_izygHg9iO-.jpg)

thenAccept方法：消费处理结果。接收任务的处理结果，并消费处理，无返回结果。

![](image/wps5_b8qp166lYW.jpg)

thenRun方法：只要上面的任务执行完成，就开始执行thenRun，只是处理完任务后，执行 thenRun的后续操作

![](image/wps6_f3lg1jPoyK.jpg)

带有Async默认是异步执行的。这里所谓的异步指的是不在当前线程内执行。

Function\<? super T,? extends U>

T：上一个任务返回结果的类型

U：当前任务的返回值类型

代码演示：

```java
package com.atguigu.gmall.product;

import java.util.concurrent.*;

public class CompletableFutureDemo1 {



    public static void main(String[] args) {

        // 线程1执行返回的结果：hello
        CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> "hello");

        // 线程2 获取到线程1执行的结果
        CompletableFuture<Void> futureB = futureA.thenAcceptAsync((s) -> {
            delaySec(1);
            printCurrTime(s + " 第一个线程");
        });

        CompletableFuture<Void> futureC = futureA.thenAcceptAsync((s) -> {
            delaySec(2);
            printCurrTime(s + " 第二个线程");
        });

        threadPoolExecutor.shutdown();
    }


    private static void printCurrTime(String str) {
        System.out.println(str);
    }

    private static void delaySec(int i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

## 1.5  多任务组合

```java
public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs);
public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs);
```

-   allOf：等待所有任务完成
-   anyOf：只要有一个任务完成

```java
package com.atguigu.gmall;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 异步任务 串行，并行
 * @author: atguigu
 * @create: 2023-01-03 15:36
 */
public class CompletableFutureDemo1 {

    /**
     * A 线程有计算结果
     * B 线程依赖A线程计算结果，执行B线程任务
     * C 线程依赖A线程计算结果，执行C线程任务
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
       //1.创建异步任务对象 CompletableFuture  A任务需要返回值
        CompletableFuture<Long> futureA = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("A任务执行");
            return 29L;
        });

        //2.基于上面对象 构建 B任务对象
        CompletableFuture<String> futureB = futureA.thenApplyAsync((aResult) -> {
            try {
                Thread.sleep(5000);
                System.out.println("B任务执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "skuID为" + aResult + "的销售属性";
        });


        //3.基于上面对象 构建 C任务对象
        CompletableFuture<String> futureC = futureA.thenApplyAsync((aResult) -> {
            try {
                Thread.sleep(11);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("C任务执行");
            return "skuID为" + aResult + "的海报列表";
        });

        //futureA.join();
        //futureB.join();
        //futureC.join();

        //Long a = futureA.get();
        //System.out.println(a);
        //String b = futureB.get();
        //System.out.println(b);
        //String c = futureC.get();
        //System.out.println(c);


        CompletableFuture.anyOf(futureA, futureB, futureC).join();
        CompletableFuture.allOf(futureA, futureB, futureC).join();
        System.out.println("执行后续业务代码");
    }
}
```

## 1.6  优化商品详情页

### 1.6.1. 自定义线程池

在`service-item`模块中新建包名：com.atguigu.gmall.item.config 新增线程池配置类：ThreadPoolConfig

```java
package com.atguigu.gmall.item.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 全局自定义线程池配置
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        //动态获取服务器核数
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                processors+1, // 核心线程个数 io:2n ,cpu: n+1  n:内核数据
                processors+1,
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        //  返回线程池对象
        return threadPoolExecutor;
    }
}
```

### 1.6.2. 优化商品详情数据

对`service-item`模块中`ItemServiceImpl`类中的getBySkuId方法进行优化

```java
@Autowired
private ThreadPoolExecutor threadPoolExecutor;

/**
 * 远程调用商品微服务：根据skuID汇总sku商品详情页所有数据
 *
 * @param skuId
 * @return
 */
@Override
public Map<String, Object> getBySkuId(Long skuId) {
    //0.创建响应结果Map
    Map<String, Object> result = new HashMap<>();

    //远程调用商品微服务7个接口之前 提前知道用户访问商品SKUID是否存在与布隆过滤器
    RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER);
    if (!bloomFilter.contains(skuId)) {
        log.error("用户查询商品sku不存在：{}", skuId);
        return result;
    }

    //1.根据skuId查询商品Sku信息包含商品图片 得到SkuInfo 创建带返回值异步对象
    CompletableFuture<SkuInfo> skuInfoCompletableFuture = CompletableFuture.supplyAsync(() -> {
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if (skuInfo != null) {
            result.put("skuInfo", skuInfo);
        }
        return skuInfo;
    }, threadPoolExecutor);


    //2.根据商品所属三级分类Id查询分类对象信息 创建任务该任务获取前面sku信息任务返回结果，当前任务不需要返回值
    CompletableFuture<Void> categoryViewCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync((skuInfo -> {
        BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
        if (categoryView != null) {
            result.put("categoryView", categoryView);
        }
    }), threadPoolExecutor);

    //3.根据商品skuId查询商品价格
    CompletableFuture<Void> priceCompletableFuture = CompletableFuture.runAsync(() -> {
        BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
        if (skuPrice != null) {
            result.put("price", skuPrice);
        }
    }, threadPoolExecutor);

    //4.根据spuId查询商品海报图片列表
    CompletableFuture<Void> spuPosterListCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync((skuInfo -> {
        List<SpuPoster> spuPosterList = productFeignClient.findSpuPosterBySpuId(skuInfo.getSpuId());
        if (!CollectionUtils.isEmpty(spuPosterList)) {
            result.put("spuPosterList", spuPosterList);
        }
    }), threadPoolExecutor);


    //5.根据skuId查询平台属性以及平台属性值
    CompletableFuture<Void> attrListCompletableFuture = CompletableFuture.runAsync(() -> {
        List<BaseAttrInfo> attrList = productFeignClient.getAttrList(skuId);
        if (!CollectionUtils.isEmpty(attrList)) {
            //5.1 遍历平台属性集合
            List<Map<String, String>> skuAttrList = attrList.stream().map(baseAttrInfo -> {
                //5.2 封装List<Map<String, String>> 得到平台属性名称 以及 平台属性名称 attrName 平台属性名称值 attrValue
                Map<String, String> attMap = new HashMap<>();
                String attrName = baseAttrInfo.getAttrName();
                String attrValue = baseAttrInfo.getAttrValueList().get(0).getValueName();
                attMap.put("attrName", attrName);
                attMap.put("attrValue", attrValue);
                return attMap;
            }).collect(Collectors.toList());
            result.put("skuAttrList", skuAttrList);
        }
    }, threadPoolExecutor);

    //6.根据spuID,skuID查询所有销售属性，以及当前sku选中销售属性
    CompletableFuture<Void> spuSaleAttrListCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(skuInfo -> {
        List<SpuSaleAttr> attrListCheckBySku = productFeignClient.getSpuSaleAttrListCheckBySku(skuId, skuInfo.getSpuId());
        if (!CollectionUtils.isEmpty(attrListCheckBySku)) {
            result.put("spuSaleAttrList", attrListCheckBySku);
        }
    }, threadPoolExecutor);

    //7.根据spuID查询销售属性属性值对应sku信息Map {"销售属性1|销售属性2":"skuId"} TODO 注意要将map转为JSON
    CompletableFuture<Void> valuesSkuJsonCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync(skuInfo -> {
        Map map = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
        if (!CollectionUtils.isEmpty(map)) {
            //将map转为JSON字符串
            result.put("valuesSkuJson", JSON.toJSONString(map));
        }
    }, threadPoolExecutor);

    //8.将以上七个任务全部并行执行，执行完所有任务才返回
    CompletableFuture.allOf(skuInfoCompletableFuture,
            categoryViewCompletableFuture,
            priceCompletableFuture,
            spuPosterListCompletableFuture,
            attrListCompletableFuture,
            spuSaleAttrListCompletableFuture,
            valuesSkuJsonCompletableFuture).join();

    return result;
}
```

# 2、首页商品分类实现

![](image/wps7_1GftfHpAEa.jpg)

前面做了商品详情，我们现在来做首页分类，我先看看京东的首页分类效果，我们如何实现类似效果：

![](image/wps8_nG0Kk0VEKr.jpg)

思路：

1，首页属于并发量比较高的访问页面，我看可以采取页面静态化方式实现，或者把数据放在缓存中实现

2，我们把生成的静态文件可以放在nginx访问或者放在web-index模块访问

## 2.1  修改pom.xml

在`web-all`模块中新增商品服务依赖

```xml
<dependency>
    <groupId>com.atguigu.gmall</groupId>
    <artifactId>service-product-client</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## 2.2  封装数据接口

由于商品分类信息在service-product模块，我们在该模块封装数据，数据结构为父子层级

商品分类保存在base\_category1、base\_category2和base\_category3表中，由于需要静态化页面，我们需要一次性加载所有数据，前面我们使用了一个视图base\_category\_view，所有我从视图里面获取数据，然后封装为父子层级

数据结构如下：json 数据结构

```json
[
    {
        "index":1,     #序号
        "categoryName":"图书、音像、电子书刊",   #一级分类名称
        "categoryId":1,                       #一级分类ID
        "categoryChild":[                     #当前一级分类包含的二级分类集合
            {
                "categoryName":"电子书刊",     #二级分类名称
                "categoryId":1,               #二级分类ID
                "categoryChild":[             #当前二级分类包含的三级分类集合
                    {
                        "categoryName":"电子书",#三级分类名称
                        "categoryId":1         #三级分类ID
                    },
                    {
                        "categoryName":"网络原创",
                        "categoryId":2
                    }
                ]
            }
        ]
    },
    {
        "index":2,
        "categoryName":"手机",
        "categoryId":2,
        "categoryChild":[
            {
                "categoryName":"手机通讯",
                "categoryId":13,
                "categoryChild":[
                    {
                        "categoryName":"手机",
                        "categoryId":61
                    }
                ]
            },
            {
                "categoryName":"运营商",
                "categoryId":14
            },
            {
                "categoryName":"手机配件",
                "categoryId":15
            }
        ]
    }
]
```

### 2.2.1  控制器

`service-product`模块中ProductApiController

```java
/**
 * 获取全部分类信息
 *
 * @return
 */
@GetMapping("/inner/getBaseCategoryList")
public List<JSONObject> getBaseCategoryList() {
    List<JSONObject> list = categoryService.getBaseCategoryList();
    return list;
}
```

### 2.2.2 BaseCategoryService接口

```java
/**
 * 获取全部分类信息
 * @return
 */
List<JSONObject> getBaseCategoryList();
```

### 2.2.3 ManageServiceImpl 实现类

```java
/**
 * 获取全部分类信息
 * [{一级分类基本属性,categoryChild:[{二级分类基本属性, categoryChild:[{三级分类属性}]}]},{一级分类基本属性,categoryChild:[{二级分类基本属性, categoryChild:[{三级分类属性}]}]}]
 *
 * @return
 */
@Override
@GmallCache(prefix = "baseCategoryList")
public List<JSONObject> getBaseCategoryList() {
    //0.构建响应结果对象
    List<JSONObject> resultList = new ArrayList<>();
    //1.查询所有分类视图表记录
    List<BaseCategoryView> allCategoryviewList = baseCategoryViewMapper.selectList(null);

    //2.对所有分类根据category1Id属性进行分组，得到所有一级分类（包含二级跟三级）
    if (!CollectionUtils.isEmpty(allCategoryviewList)) {
        //2.1 采用Stream流进行分组 分组后Map中key:getCategory1Id一级分类ID  val:所有一级分类数据
        Map<Long, List<BaseCategoryView>> category1Map = allCategoryviewList.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));
        //2.2 遍历一级分类
        int index = 1;
        for (Map.Entry<Long, List<BaseCategoryView>> entry : category1Map.entrySet()) {
            //2.2.1 获取一级分类ID
            Long category1Id = entry.getKey();
            //2.2.2 获取一级分类名称
            String category1Name = entry.getValue().get(0).getCategory1Name();
            //2.2.3 创建一级分类JSON对象
            JSONObject category1 = new JSONObject();
            category1.put("index", index++);
            category1.put("categoryId", category1Id);
            category1.put("categoryName", category1Name);

            //3.从每个一级分类Map中获取二级分类，处理二级分类数据
            //3.1 声明二级分类List集合
            List<JSONObject> category2List = new ArrayList<>();
            //3.2 从一级分类map中获去所有一级分类数据，根据二级分类ID分组
            Map<Long, List<BaseCategoryView>> category2MapList = entry.getValue().stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
            //3.3 遍历二级分类 构建二级分类JSON对象
            for (Map.Entry<Long, List<BaseCategoryView>> category2Entry : category2MapList.entrySet()) {
                JSONObject category2 = new JSONObject();
                category2.put("categoryId", category2Entry.getKey());
                category2.put("categoryName", category2Entry.getValue().get(0).getCategory2Name());
                category2List.add(category2);

                //4. 从每个二级分类中获取三级分类 只需要从category2Entry Map获取val
                List<BaseCategoryView> category3ArrayList = category2Entry.getValue();
                //4.1 声明三级分类集合
                List<JSONObject> category3List = new ArrayList<>();
                //4.2 遍历集合 构建三级分类对象 将三级分类加入到二级分类对象中
                for (BaseCategoryView baseCategoryView : category3ArrayList) {
                    JSONObject category3 = new JSONObject();
                    category3.put("categoryId", baseCategoryView.getCategory3Id());
                    category3.put("categoryName", baseCategoryView.getCategory3Name());
                    category3List.add(category3);
                }
                //4.3 将三级分类集合加入到二级分类中
                category2.put("categoryChild", category3List);
            }

            //将二级分类集合加入一级分类对象中
            category1.put("categoryChild", category2List);

            //将处理后一级分类加入结果
            resultList.add(category1);
        }
    }
    return resultList;
}
```

## 2.3  service-product-client添加接口

在`service-product-client`模块中ProductFeignClient，提供远程调用FeignAPI接口以及服务降级方法

```java
/**
 * 获取全部分类信息
 * @return
 */
@GetMapping("/api/product/getBaseCategoryList")
Result getBaseCategoryList();
```

```java
@Override
public Result getBaseCategoryList() {
    return null;
}
```

## 2.4  页面渲染

**第一种缓存渲染方式**：

`web-all`模块中编写控制器

```java
package com.atguigu.gmall.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private ProductFeignClient productFeignClient;

    /**
     * 渲染首页
     * @param model
     * @return
     */
    @GetMapping({"/", "/index.html"})
    public String index(Model model) {
        //1.远程获取分类数据
        List<JSONObject> list = productFeignClient.getBaseCategoryList();

        //2.添加数据到模型对象Model
        model.addAttribute("list", list);

        //3.返回模板页面
        return "/index/index.html";
    }
}

```

**第二种方式nginx做静态代理方式：**

1.  生成静态文件

```java
@Autowired
private TemplateEngine templateEngine;

@GetMapping("createIndex")
@ResponseBody
public Result createIndex(){
    //  获取后台存储的数据
    Result result = productFeignClient.getBaseCategoryList();
    //  设置模板显示的内容
    Context context = new Context();
    context.setVariable("list",result.getData());

    //  定义文件输入位置
    FileWriter fileWriter = null;
    try {
        fileWriter = new FileWriter("D:\\index.html");
    } catch (IOException e) {
        e.printStackTrace();
    }
    //  调用process();方法创建模板
    templateEngine.process("index/index.html",context,fileWriter);
    return Result.ok();
}
```

1.  解压课后资料中nginx压缩 不要中文空格
2.  将静态文件拷贝到nginx/html目录下 包含js,css等文件夹
3.  ![](image/image-20230104161748678_6OerIZkoR5.png)




3.  启动Nginx服务
4.  访问首页
5.  ![](image/image-20230104161906057_HoWs6TZHfG.png)





