# 第五节 字符串命令：basename

返回路径字符串中的资源（文件或目录本身）部分

```shell
[root@apple w]# basename /aa/bb/cc/dd
dd
```



如果指定了后缀，basename会帮我们把后缀部分也去掉

```shell
[root@apple workspace]# basename /aa/bb/cc/dd.txt .txt
dd
```



[上一条](verse05-01-regular-expression.html) [回目录](verse05-00-index.html) [下一条](verse05-03-dirname.html)