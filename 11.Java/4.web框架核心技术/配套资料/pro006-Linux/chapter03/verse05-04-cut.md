# 第五节 字符串命令：cut

根据指定符号拆分字符串并提取。默认根据 \t 拆分。

- -f 参数：指定要提取的列
- -d 参数：指定拆分依据的字符

准备测试数据：

```shell
[root@hadoop101 datas]$ touch cut.txt
[root@hadoop101 datas]$ vim cut.txt
dong shen
guan zhen
wo  wo
lai  lai
le  le
```

切割提取第一列：

```shell
[root@hadoop101 datas]$ cut -d " " -f 1 cut.txt 
dong
guan
wo
lai
le
```

切割提取第二、第三列：

```shell
[root@apple w]# cut -d " " -f 2,3 cut.txt 
shen
zhen
 wo
 lai
 la
```

在cut.txt中切出guan

```shell
[root@hadoop101 datas]$ cat cut.txt | grep "guan" | cut -d " " -f 1
guan
```

选取系统 PATH 变量值，第2个 “:” 开始后的所有路径：

```shell
[root@hadoop101 datas]$ echo $PATH
/usr/lib64/qt-3.3/bin:/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/sbin:/home/atguigu/bin

[root@hadoop101 datas]$ echo $PATH | cut -d : -f 2-
/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/sbin:/home/atguigu/bin
```

切割ifconfig 后打印的IP地址：

```shell
[root@apple w]# ifconfig | grep "netmask" | cut -d "i" -f 2 | cut -d " " -f 2
192.168.41.100
127.0.0.1
192.168.122.1
```

另一种做法：

```shell
[root@apple workspace]# ifconfig | grep netmask | cut -d " " -f 10
192.168.41.100
127.0.0.1
192.168.122.1
```

[上一条](verse05-03-dirname.html) [回目录](verse05-00-index.html) [下一条](verse05-05-awk.html)