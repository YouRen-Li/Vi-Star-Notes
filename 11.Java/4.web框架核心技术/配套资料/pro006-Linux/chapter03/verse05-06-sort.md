# 第五节 字符串命令：sort

sort命令是在Linux里非常有用，它将文件进行排序，并将排序结果标准输出。

| 参数名 | 作用                     |
| ------ | ------------------------ |
| -n     | 依照数值大小排序         |
| -r     | 相反顺序排序             |
| -t     | 设置排序时使用的分隔字符 |
| -k     | 指定需要排序的列         |

```shell
# 准备数据
[root@hadoop101 datas]$ touch sort.sh
[root@hadoop101 datas]$ vim sort.sh 
bb:40:5.4
bd:20:4.2
xz:50:2.3
cls:10:3.5
ss:30:1.6
```

```shell
# 测试
[root@hadoop101 datas]$ sort -t : -nrk 3  sort.sh 
bb:40:5.4
bd:20:4.2
cls:10:3.5
xz:50:2.3
ss:30:1.6

[root@apple workspace]# sort -nrt : -k 3 sort.txt 
bb:40:5.4
bd:20:4.2
cls:10:3.5
xz:50:2.3
ss:30:1.6
```

[上一条](verse05-05-awk.html) [回目录](verse05-00-index.html) [下一条](verse05-07-xargs.html)