# day11 书城项目第四阶段

## 第一章 添加商品进购物车

### 1 创建购物车模型

![images](images/img007.png)

#### 1.1 购物项详情类

![images](images/img008.png)

public class CartItem {

```java
package com.atguigu.bean;

/**
 * 包名:com.atguigu.bean
 *
 * @author Leevi
 * 日期2021-05-17  09:06
 */
public class CartItem {
    private Integer bookId;
    private String bookName;
    private String imgPath;
    /**
     * 商品的单价
     */
    private Double price;
    private Integer count;
    /**
     * 购物项的金额
     */
    private Double amount;

    public CartItem() {
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", amount=" + amount +
                '}';
    }

    public CartItem(Integer bookId, String bookName, String imgPath, Double price, Integer count, Double amount) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.imgPath = imgPath;
        this.price = price;
        this.count = count;
        this.amount = amount;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 这个方法获取总价:要通过计算才能获取
     * @return
     */
    public Double getAmount() {
        return this.price*this.count;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * 数量+1
     */
    public void countIncrease(){
        this.count ++;
    }

    /**
     * 数量-1
     */
    public void countDecrease(){
        this.count --;
    }
}
```
#### 1.2 购物车类：Cart

```java
package com.atguigu.bean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 包名:com.atguigu.bean
 *
 * @author Leevi
 * 日期2021-05-17  09:09
 */
public class Cart {
    /**
     * 存储购物车中的购物项
     * 以购物项中的书的ID作为key，以购物项作为value
     */
    private Map<Integer,CartItem> cartItemMap = new HashMap<>();

    /**
     * 将书添加进购物车
     * @param book
     */
    public void addBookToCart(Book book){
        //1. 判断购物车中是否已经有这件商品
        if (cartItemMap.containsKey(book.getBookId())) {
            //2. 如果购物车中已经有这件商品了，那么就对其数量+1
            itemCountIncrease(book.getBookId());
        }else {
            //3. 如果购物车中还没有这件商品，那么我们就新增一个购物项
            //第一次添加商品进购物车，那么数量count肯定是1，那么总价就是单价
            CartItem cartItem = new CartItem(book.getBookId(),book.getBookName(),book.getImgPath(),book.getPrice(),1,book.getPrice());
            cartItemMap.put(cartItem.getBookId(),cartItem);
        }
    }

    @Override
    public String toString() {
        return "Cart{" +
            "cartItemMap=" + cartItemMap +
            '}';
    }

    /**
     * 显示购物车信息,为了在Thymeleaf中便于使用OGNL表达式获取购物车的信息，
     * 我们的方法名要叫作getCartItemMap(),那么我们在Thymeleaf中就可以使用 .cartItemMap获取
     * @return
     */
    public Map<Integer,CartItem> getCartItemMap(){
        return cartItemMap;
    }

    /**
     * 将购物车中的某个购物项的数量+1
     * @param bookId
     */
    public void itemCountIncrease(Integer bookId){
        cartItemMap.get(bookId).countIncrease();
    }

    /**
     * 将购物车中的某个购物项的数量-1
     * @param bookId
     */
    public void itemCountDecrease(Integer bookId){
        CartItem cartItem = cartItemMap.get(bookId);
        //1.先将当前购物项的数量-1
        cartItem.countDecrease();
        //2.判断当前购物项的count是否等于0
        if (cartItem.getCount() == 0) {
            //说明要将当前购物项从购物车中移除
            removeCartItem(bookId);
        }
    }

    /**
     * 从购物车中移除购物项
     * @param bookId
     */
    public void removeCartItem(Integer bookId){
        cartItemMap.remove(bookId);
    }

    /**
     * 修改某个购物项的数量
     * @param bookId
     */
    public void updateItemCount(Integer bookId,Integer newCount){
        cartItemMap.get(bookId).setCount(newCount);
    }

    /**
     * 获取购物车中的购物项中的商品的总数
     * 就是将所有购物项的count进行累加
     * @return
     */
    public Integer getTotalCount(){
        //遍历出每一个CartItem的count然后累加
        //方式一: 采用JDK1.8的新特性
        /*AtomicReference<Integer> totalCount = new AtomicReference<>(0);
        cartItemMap.forEach((k,cartItem) -> {
            totalCount.updateAndGet(v -> v + cartItem.getCount());
        });

        return totalCount.get();*/

        //方式二: 使用原生的entrySet遍历Map
        Integer totalCount = 0;
        for (Map.Entry<Integer, CartItem> cartItemEntry : cartItemMap.entrySet()) {
            totalCount += cartItemEntry.getValue().getCount();
        }
        return totalCount;
    }

    /**
     * 获取总金额
     * 就是遍历出每个购物项的金额再累加
     * @return
     */
    public Double getTotalAmount(){
        //解决精度问题的核心: 就是将要进行运算的数据转成BigDecimal类型之后再计算
        //声明一个总金额
        Double totalAmount = 0.0;
        for (Map.Entry<Integer, CartItem> cartItemEntry : cartItemMap.entrySet()) {
            //cartItemEntry.getValue().getAmount()是获取每一个购物项的金额

            //使用总金额加上遍历出来的购物项的金额
            totalAmount += cartItemEntry.getValue().getAmount();
        }
        return totalAmount;
    }
}
```

### 2. 目标

在首页点击『加入购物车』将该条图书加入『购物车』

### 3. 思路

首页→加入购物车→CartServlet.addCartItem()→执行添加操作→回到首页

### 4. 代码实现

#### 4.1 创建CartServlet

![images](images/img009.png)

```java
public class CartServlet extends ModelBaseServlet {
    private BookService bookService = new BookServiceImpl();
    /**
     * 添加商品进购物车
     * @param request
     * @param response
     */
    public void addCartItem(HttpServletRequest request, HttpServletResponse response){
        try {
            //1. 获取请求参数id的值
            Integer id = Integer.valueOf(request.getParameter("id"));
            //2. 调用bookService的方法根据id查询book信息
            Book book = bookService.getBookById(id);
            //3. 尝试从会话域中获取购物车
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            //4. 判断之前是否已经添加过购物车了
            if (cart == null) {
                //说明这是第一次添加购物车
                //那么就要新创建一个购物车对象
                cart = new Cart();
                //然后将当前book加入到这个购物车
                cart.addBookToCart(book);
                //然后将cart存入到session
                session.setAttribute("cart",cart);
            }else {
                //说明不是第一次添加购物车
                //那么就直接用之前的购物车，添加当前book就行
                cart.addBookToCart(book);
            }
			
            //跳转到首页
            response.sendRedirect(request.getContextPath()+"/index.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 4.2 index.html页面

购物车数量显示：

```html
<!--登录前的风格-->
<div class="topbar-right" th:if="${session.loginUser == null}">
    <a href="user?method=toLoginPage" class="login">登录</a>
    <a href="user?method=toRegisterPage" class="register">注册</a>
    <a
       href="#"
       class="cart iconfont icon-gouwuche
              "
       >
        购物车
        <div class="cart-num" th:if="${session.cart != null}" th:text="${session.cart.totalCount}">3</div>
    </a>
    <a href="admin?method=toManagerPage" class="admin">后台管理</a>
</div>
<!--登录后风格-->
<div class="topbar-right" th:unless="${session.loginUser == null}">
    <span>欢迎你<b th:text="${session.loginUser.username}">张总</b></span>
    <a href="user?method=logout" class="register">注销</a>
    <a
       href="#"
       class="cart iconfont icon-gouwuche
              ">
        购物车
        <div class="cart-num" th:if="${session.cart != null}" th:text="${session.cart.totalCount}">3</div>
    </a>
    <a href="admin?method=toManagerPage" class="admin">后台管理</a>
</div>
```

加入购物车：

```html
<a th:href="@{/cart(method='addCartItem',id=${book.bookId})}">加入购物车</a>
```

#### 4.3 CSS样式

![images](images/img010.png)

```css
.books-list .list-content .list-item a {
  display: block;
  width: 80px;
  height: 30px;
  border: none;
  line-height: 30px;
  background-color: #39987c;
  margin-top: 5px;
  outline: none;
  color: #fff;
  cursor:pointer;
  font-size:12px;
  text-align:center;
}
```

如果修改完成后页面效果没有改变，可以使用Ctrl+F5强制刷新。

## 第二章 显示购物车页面

### 1目标

把购物车信息在专门的页面显示出来

### 2思路

首页→购物车超链接→CartServlet.showCart()→cart.html

### 3代码实现

#### 3.1 购物车超链接

登录状态和未登录状态

```html
<div class="topbar-right" th:if="${session.loginUser == null}">
    <a href="user?method=toLoginPage" class="login">登录</a>
    <a href="user?method=toRegisterPage" class="register">注册</a>
    <a
       href="cart?method=toCartPage"
       class="cart iconfont icon-gouwuche
              "
       >
        购物车
        <div class="cart-num" th:if="${session.cart != null}" th:text="${session.cart.totalCount}">3</div>
    </a>
    <a href="admin?method=toManagerPage" class="admin">后台管理</a>
</div>
<!--登录后风格-->
<div class="topbar-right" th:unless="${session.loginUser == null}">
    <span>欢迎你<b th:text="${session.loginUser.username}">张总</b></span>
    <a href="user?method=logout" class="register">注销</a>
    <a
       href="cart?method=toCartPage"
       class="cart iconfont icon-gouwuche
              ">
        购物车
        <div class="cart-num" th:if="${session.cart != null}" th:text="${session.cart.totalCount}">3</div>
    </a>
    <a href="admin?method=toManagerPage" class="admin">后台管理</a>
</div>
```

#### 3.2 CartServlet

```java
/**
     * 跳转到显示购物车列表的页面
     * @param request
     * @param response
     */
public void toCartPage(HttpServletRequest request,HttpServletResponse response) throws IOException {
    processTemplate("cart/cart",request,response);
}
```

#### 3.3 cart.html

```html
<table>
    <thead>
        <tr>
            <th>图片</th>
            <th>商品名称</th>

            <th>数量</th>
            <th>单价</th>
            <th>金额</th>
            <th>操作</th>
        </tr>
    </thead>
    <tbody th:if="${session.cart == null}">
        <tr>
            <td th:colspan="6">
                <a href="index.html">购物车空空如也，请抓紧购物吧!!!!</a>
            </td>
        </tr>
    </tbody>
    <tbody th:if="${session.cart != null}">
        <tr th:each="cartItemEntry : ${session.cart.cartItemMap}">
            <td>
                <img th:src="${cartItemEntry.value.imgPath}" alt="" />
            </td>
            <td th:text="${cartItemEntry.value.bookName}">活着</td>
            <td>
                <a class="count" th:href="#">-</a>
                <input class="count-num" type="text" th:value="${cartItemEntry.value.count}" value="1" />
                <input type="hidden" th:value="${cartItemEntry.value.bookId}"/>
                <a class="count" th:href="#">+</a>
            </td>
            <td th:text="${cartItemEntry.value.price}">36.8</td>
            <td th:text="${cartItemEntry.value.amount}">36.8</td>
            <td><a th:href="#">删除</a></td>
        </tr>
    </tbody>
</table>
<div>
    <div class="footer" th:if="${session.cart != null}">
        <div class="footer-left">
            <a href="#" class="clear-cart">清空购物车</a>
            <a href="#">继续购物</a>
        </div>
        <div class="footer-right">
            <div>共<span th:text="${session.cart.totalCount}">3</span>件商品</div>
            <div class="total-price">总金额<span th:text="${session.cart.totalAmount}">99.9</span>元</div>
            <a class="pay" href="pages/cart/checkout.html">去结账</a>
        </div>
    </div>
</div>
```

## 第三章 清空购物车

### 1 目标

当用户确定点击清空购物车，将Session域中的Cart对象移除。

### 2 思路

cart.html→清空购物车超链接→绑定单击响应函数→confirm()确认→确定→CartServlet.clearCart()→从Session域移除Cart对象

### 3 代码实现

#### 3.1 清空购物车超链接

```html
<!--在head里面引入vue-->
<script src="static/script/vue.js" type="text/javascript" charset="utf-8"></script>

<!--修改清空购物车的a标签的访问路径，并且绑定点击事件-->
<a href="cart?method=cleanCart" @click="cleanCart" class="clear-cart">清空购物车</a>

<script>
    /**
    * 注意设置table的外层的div的id为app
    * <div class="list" id="app">
    *  <div class="w">
    *    <table>
    */
    var vue = new Vue({
        "el":"#app",
        "methods":{
            cleanCart(){
                //弹出确认框
                if (!confirm("你确定要清空购物车吗?")) {
                    //不需要清空购物车，阻止a标签跳转
                    event.preventDefault()
                }
            }
        }
    });
</script>
```

#### 3.2 CartServlet.cleanCart()

```java
/**
     * 清空购物车
     * @param request
     * @param response
     * @throws IOException
     */
public void cleanCart(HttpServletRequest request,HttpServletResponse response) throws IOException {
    //就是移除掉session中的cart就行了
    request.getSession().removeAttribute("cart");
    //跳转到购物车展示页面
    processTemplate("cart/cart",request,response);
}
```

## 第四章 减号

### 1. 目标

- 在大于1的数值基础上-1：执行-1的逻辑
- 在1的基础上-1：执行删除item的逻辑

### 2. 思路

减号a标签绑定单击响应函数→获取文本框中的数据：当前数量→检查数量是否等于1，如果它等于1的话，则弹出一个确认框，询问其是否想删除?如果不想删除，则阻止a标签的事件

### 3. 后端代码

CartServlet.countDecrease()方法

```java
/**
     * 购物车中某个一个购物项的数量-1
     * @param request
     * @param response
     * @throws IOException
     */
public void countDecrease(HttpServletRequest request,HttpServletResponse response) throws IOException {
    //1. 获取到要-1的书的id
    Integer id = Integer.valueOf(request.getParameter("id"));
    //2. 从session中获取购物车信息
    Cart cart = (Cart) request.getSession().getAttribute("cart");
    //3. 调用购物车的-1方法
    cart.itemCountDecrease(id);
    //4. 跳转回到购物车页面
    processTemplate("cart/cart",request,response);
}
```

### 4. 前端代码

HTML代码给“减号”设置访问路径以及绑定点击事件：

需要注意:只能使用onclick绑定不能使用Vue绑定(通过实践得出的)

```html
<a onclick="cartItemCountDecrease()" class="count" th:href="@{/cart(method='countDecrease',id=${cartItemEntry.value.bookId})}">-</a>
```

js代码：

```javascript
<script>
function cartItemCountDecrease(){
    //判断:输入框的内容是否是1
    //输入框就是当前点击的a标签的下一个兄弟: event.target.nextElementSubling
    if (event.target.nextElementSibling.value == '1') {
        var bookName = event.target.parentElement.parentElement.getElementsByTagName("td")[1].innerText;
        //则弹出提示框问你是否要删除
        if (!confirm(bookName + "的数量已经是1了，你确定还要减少吗?")) {
            //阻止事件发生
            event.preventDefault()
        }
    }
}
.....
</script>
```

## 第四章 删除

### 1. 目标

点击删除超链接后，把对应的CartItem从Cart中删除

### 2. 思路

cart.html→删除超链接→CartServlet.removeCartItem()→回到cart.html

需要注意：删除之前要确认

### 3. 代码实现

#### 3.1 后端代码

CartServlet.removeCartItem()

```java
/**
     * 删除购物项
     * @param request
     * @param response
     * @throws IOException
     */
public void removeCartItem(HttpServletRequest request,HttpServletResponse response) throws IOException {
    //1. 获取要删除的购物项的书的id
    Integer id = Integer.valueOf(request.getParameter("id"));
    //2.从session中获取购物车
    Cart cart = (Cart) request.getSession().getAttribute("cart");
    //3. 调用cart的删除购物项的方法
    cart.removeCartItem(id);
    //4. 跳转回到购物车页面
    processTemplate("cart/cart",request,response);
}
```

#### 3.2 前端代码

HTML代码：

```html
<td><a th:href="@{/cart(method='removeCartItem',id=${cartItemEntry.value.bookId})}" @click="deleteCartItem">删除</a></td>
```

Vue代码：

```javascript
var vue = new Vue({
    "el":"#app",
    "methods":{
       
        deleteCartItem(){
            //想办法使用JavaScript获取要删除的购物项的书名
            //event.target就表示获取当前事件的标签
            var bookName = event.target.parentElement.parentElement.getElementsByTagName("td")[1].innerText;
            if (!confirm("你确定要删除这个"+bookName+"吗?")) {
                event.preventDefault()
            }
        }
    }
});
```

## 第五章 文本框修改

### 1. 目标

用户在文本框输入新数据后，根据用户输入在Session中的Cart中修改CartItem中的count

### 2. 思路

cart.html→前端数据校验→CartServlet.updateCartItemCount()→回到cart.html

### 3. 代码实现

#### 3.1 后端代码

CartServlet.updateCartItemCount()

```java
/**
     * 修改购物项的数量
     * @param request
     * @param response
     * @throws IOException
     */
public void updateCartItemCount(HttpServletRequest request,HttpServletResponse response) throws IOException {
    //1. 获取请求参数:id,newCount
    Integer id = Integer.valueOf(request.getParameter("id"));
    Integer newCount = Integer.valueOf(request.getParameter("newCount"));
    //2. 从session中获取购物车信息
    Cart cart = (Cart) request.getSession().getAttribute("cart");
    //3. 调用cart中跟新数量的方法
    cart.updateItemCount(id,newCount);

    //4. 跳转到购物车页面
    processTemplate("cart/cart",request,response);
}
```

#### 3.2 前端代码

HTML代码：

```html
<input @change="updateCartItemCount" class="count-num" type="text" th:value="${cartItemEntry.value.count}" value="1" />
<input type="hidden" th:value="${cartItemEntry.value.bookId}"/>
```

Vue代码：

```javascript
var vue = new Vue({
    "el":"#app",
    "methods":{
        updateCartItemCount(){
            //获取bookId
            var bookId = event.target.nextElementSibling.value;
            //获取newCount
            var newCount = event.target.value;

            //校验newCount的格式是否正确
            var reg = /^[1-9][0-9]*$/

            if (reg.test(newCount)) {
                //发送请求携带请求参数
                location.href = "cart?method=updateCartItemCount&id="+bookId+"&newCount="+newCount

            }else {
                alert("请输入正确的数量")
            }
        }
    }
});
```

## 第六章 加号

### 1. 目标

告诉Servlet将Session域中Cart对象里面对应的CartItem执行count+1操作

### 2. 思路

加号span绑定单击响应函数→获取文本框中的数据：当前数量→CartServlet.countIncrease()→回到cart.html

### 3. 代码实现

#### 3.1 后端代码

CartServlet.countIncrease()

```java
/**
     * 购物车中某个一个购物项的数量+1
     * @param request
     * @param response
     * @throws IOException
     */
public void countIncrease(HttpServletRequest request,HttpServletResponse response) throws IOException {
    //1. 获取到要-+1的书的id
    Integer id = Integer.valueOf(request.getParameter("id"));
    //2. 从session中获取购物车信息
    Cart cart = (Cart) request.getSession().getAttribute("cart");
    //3. 调用购物车的+1方法
    cart.itemCountIncrease(id);
    //4. 跳转回到购物车页面
    processTemplate("cart/cart",request,response);
}
```

#### 3.2 前端代码

HTML代码：

```html
<a class="count" th:href="@{/cart(method='countIncrease',id=${cartItemEntry.value.bookId})}">+</a>
```

## 第七章 Double数据运算过程中精度调整

### 1. 问题现象

![images](images/img011.png)

### 2. 解决方案

- 使用BigDecimal类型来进行Double类型数据运算
- 创建BigDecimal类型对象时将Double类型的数据转换为字符串

Cart类：

```java
/**
     * 获取总金额
     * @return
     */
public Double getTotalAmount(){
    //解决精度问题的核心: 就是将要进行运算的数据转成BigDecimal类型之后再计算
    //声明一个总金额
    BigDecimal bigDecimalTotalAmount = new BigDecimal("0.0");
    for (Map.Entry<Integer, CartItem> cartItemEntry : cartItemMap.entrySet()) {
        //cartItemEntry.getValue().getAmount()是获取每一个购物项的金额

        //使用总金额加上遍历出来的购物项的金额
        bigDecimalTotalAmount = bigDecimalTotalAmount.add(new BigDecimal(cartItemEntry.getValue().getAmount() + ""));
    }
    return bigDecimalTotalAmount.doubleValue();
}
```

CartItem类：

```java
/**
     * 这个方法获取总价:要通过计算才能获取
     * @return
     */
public Double getAmount() {
    //1. 将price和count封装成BigDecimal类型
    BigDecimal bigDecimalPrice = new BigDecimal(price + "");
    BigDecimal bigDecimalCount = new BigDecimal(count + "");

    //2. 使用bigDecimal的方法进行乘法
    return bigDecimalCount.multiply(bigDecimalPrice).doubleValue();
}
```
