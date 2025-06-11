[[toc]]

# 第六节 案例

## 1、准备工作

### ①创建实体类

```java
public class Movie {
    
    private String movieId;
    private String movieName;
    private Double moviePrice;
    ……
```



### ②创建Service

#### [1]接口

```java
public interface MovieService {
    
    List<Movie> getAll();
    
    Movie getMovieById(String movieId);
    
    void saveMovie(Movie movie);
    
    void updateMovie(Movie movie);
    
    void removeMovieById(String movieId);
    
}
```



#### [2]接口的实现类

```java
@Service
public class MovieServiceImpl implements MovieService {
    
    private static Map<String ,Movie> movieMap;
    
    static {
    
        movieMap = new HashMap<>();
    
        String movieId = null;
        Movie movie = null;
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "肖申克救赎", 10.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "泰坦尼克号", 20.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "审死官", 30.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "大话西游之大圣娶亲", 40.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "大话西游之仙履奇缘", 50.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "功夫", 60.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "大内密探凌凌漆", 70.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "食神", 80.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "西游降魔篇", 90.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "西游伏妖篇", 11.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "三傻大闹宝莱坞", 12.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "唐人街探案", 13.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "一个人的武林", 14.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "罗马假日", 15.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "花季雨季", 16.0);
        movieMap.put(movieId, movie);
    
        movieId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        movie = new Movie(movieId, "夏洛特烦恼", 17.0);
        movieMap.put(movieId, movie);
    }
    
    @Override
    public List<Movie> getAll() {
        return new ArrayList<>(movieMap.values());
    }
    
    @Override
    public Movie getMovieById(String movieId) {
        return movieMap.get(movieId);
    }
    
    @Override
    public void saveMovie(Movie movie) {
        String movieId = UUID.randomUUID().toString().replace("-", "");
    
        movie.setMovieId(movieId);
    
        movieMap.put(movieId, movie);
    }
    
    @Override
    public void updateMovie(Movie movie) {
    
        String movieId = movie.getMovieId();
    
        movieMap.put(movieId, movie);
    
    }
    
    @Override
    public void removeMovieById(String movieId) {
        movieMap.remove(movieId);
    }
}
```



### ③测试Service

```java
@SpringJUnitConfig(locations = {"classpath:spring-mvc.xml"})
public class MovieTest {
        
    @Autowired
    private MovieService movieService;
        
    @Test
    public void testServiceGetAll() {
        List<Movie> list = movieService.getAll();
        for (Movie movie : list) {
            System.out.println("movie = " + movie);
        }
    }
    
    @Test
    public void testServiceGetById() {
        List<Movie> movieList = movieService.getAll();
        for (Movie movie : movieList) {
            String movieId = movie.getMovieId();
            Movie movieById = movieService.getMovieById(movieId);
            System.out.println("movieById = " + movieById);
        }
    }
    
    @Test
    public void testGetOne() {
        Movie movie = movieService.getMovieById("178E6B0B0DA14DC59141E06FFA620673");
        System.out.println("movie = " + movie);
    }
    
    @Test
    public void testServiceRemoveById() {
        List<Movie> movieList = movieService.getAll();
        for (Movie movie : movieList) {
            String movieId = movie.getMovieId();
            movieService.removeMovieById(movieId);
        }
    
        movieList = movieService.getAll();
        for (Movie movie : movieList) {
            System.out.println("movie = " + movie);
        }
    }
    
    @Test
    public void testServiceSave() {
        movieService.saveMovie(new Movie(null, "aa", 111.11));
    
        List<Movie> all = movieService.getAll();
        for (Movie movie : all) {
            System.out.println("movie = " + movie);
        }
    }
    
    @Test
    public void testServiceUpdate() {
        List<Movie> all = movieService.getAll();
        for (Movie movie : all) {
            String movieId = movie.getMovieId();
            String movieName = movie.getMovieName() + "~";
    
            Movie movieNew = new Movie(movieId, movieName, movie.getMoviePrice());
            movieService.updateMovie(movieNew);
        }
    
        List<Movie> movieList = movieService.getAll();
        for (Movie movie : movieList) {
            System.out.println("movie = " + movie);
        }
    }
    
}
```



## 2、搭建环境

### ①引入依赖

```xml
<dependencies>
    <!-- SpringMVC -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.3.1</version>
    </dependency>
    
    <!-- 日志 -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.3</version>
    </dependency>
    
    <!-- ServletAPI -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- Spring5和Thymeleaf整合包 -->
    <dependency>
        <groupId>org.thymeleaf</groupId>
        <artifactId>thymeleaf-spring5</artifactId>
        <version>3.0.12.RELEASE</version>
    </dependency>
    
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.7.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>5.3.1</version>
    </dependency>
</dependencies>
```



### ②加入配置文件

#### [1]web.xml

```xml
<servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring-mvc.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
    
<filter>
    <filter-name>characterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
        <param-name>forceRequestEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
    <init-param>
        <param-name>forceResponseEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```



#### [2]日志配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">
    <!-- 指定日志输出的位置 -->
    <appender name="STDOUT"
              class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <!-- 日志输出的格式 -->
            <!-- 按照顺序分别是：时间、日志级别、线程名称、打印日志的类、日志主体内容、换行 -->
            <pattern>[%d{HH:mm:ss.SSS}] [%-5level] [%thread] [%logger] [%msg]%n</pattern>
        </encoder>
    </appender>
    
    <!-- 设置全局日志级别。日志级别按顺序分别是：DEBUG、INFO、WARN、ERROR -->
    <!-- 指定任何一个日志级别都只打印当前级别和后面级别的日志。 -->
    <root level="INFO">
        <!-- 指定打印日志的appender，这里通过“STDOUT”引用了前面配置的appender -->
        <appender-ref ref="STDOUT" />
    </root>
    
    <!-- 专门给某一个包指定日志级别 -->
    <logger name="com.atguigu" level="DEBUG" additivity="false">
        <appender-ref ref="STDOUT" />
    </logger>

    <logger name="org.springframework.web.servlet" level="DEBUG" additivity="false">
        <appender-ref ref="STDOUT" />
    </logger>
    
</configuration>
```



#### [3]SpringMVC 配置文件

```xml
<!-- 自动扫描的包 -->
<context:component-scan base-package="com.atguigu.demo"/>
    
<!-- 视图解析器 -->
<bean id="thymeleafViewResolver" class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
    <property name="order" value="1"/>
    <property name="characterEncoding" value="UTF-8"/>
    <property name="templateEngine">
        <bean class="org.thymeleaf.spring5.SpringTemplateEngine">
            <property name="templateResolver">
                <bean class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
                    <property name="prefix" value="/WEB-INF/templates/"/>
                    <property name="suffix" value=".html"/>
                    <property name="characterEncoding" value="UTF-8"/>
                    <property name="templateMode" value="HTML5"/>
                </bean>
            </property>
        </bean>
    </property>
</bean>
    
<!-- SpringMVC 标配：注解驱动 -->
<mvc:annotation-driven/>
    
<!-- 对于没有 @RequestMapping 的请求直接放行 -->
<mvc:default-servlet-handler/>
```



## 3、功能清单

[显示<span style="color:blue;font-weight:bold;">首页</span>](verse06/feature01.html)
[在<span style="color:blue;font-weight:bold;">首页</span>点击<span style="color:red;font-weight:bold;">超链接显示全部数据</span>](verse06/feature02.html)
[在<span style="color:blue;font-weight:bold;">列表页面</span>点击<span style="color:red;font-weight:bold;">删除超链接</span>执行删除操作](verse06/feature03.html)
[在<span style="color:blue;font-weight:bold;">列表页面</span>点击<span style="color:red;font-weight:bold;">添加新数据超链接</span>跳转到<span style="color:blue;font-weight:bold;">添加数据的表单页面</span>](verse06/feature04.html)
[在<span style="color:blue;font-weight:bold;">添加数据的表单页面</span>点击<span style="color:red;font-weight:bold;">提交按钮执行保存</span>操作](verse06/feature05.html)
[在<span style="color:blue;font-weight:bold;">列表页面</span>点击<span style="color:red;font-weight:bold;">更新超链接</span>跳转到<span style="color:blue;font-weight:bold;">更新数据的表单页面</span>并回显表单](verse06/feature06.html)
[在<span style="color:blue;font-weight:bold;">更新数据的表单页面</span>点击<span style="color:red;font-weight:bold;">提交按钮执行更新</span>操作](verse06/feature07.html)



[上一节](verse05.html) [回目录](index.html)