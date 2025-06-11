[[toc]]

# 实验六 切面的优先级

## 1、概念

相同目标方法上同时存在多个切面时，切面的优先级控制切面的<span style="color:blue;font-weight:bold;">内外嵌套</span>顺序。

- 优先级高的切面：外面
- 优先级低的切面：里面



使用@Order注解可以控制切面的优先级：

- @Order(较小的数)：优先级高
- @Order(较大的数)：优先级低

![images](../images/img012.png)





## 2、实际意义

实际开发时，如果有多个切面嵌套的情况，要慎重考虑。例如：如果事务切面优先级高，那么在缓存中命中数据的情况下，事务切面的操作都浪费了。

![images](../images/img013.png)



此时应该将缓存切面的优先级提高，在事务操作之前先检查缓存中是否存在目标数据。

![images](../images/img014.png)



[上一个实验](experiment05.html) [回目录](../verse05.html) [下一个实验](experiment07.html)