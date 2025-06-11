[[toc]]

# 第五节 字符串命令：awk

一个强大的文本分析工具，把文件逐行的读入，以空格为默认分隔符将每行切片，切开的部分再进行分析处理。



## 1、基本用法

awk [选项参数] ‘pattern1{action1} pattern2{action2}...’ filename<br/>

pattern：表示AWK在数据中查找的内容，就是匹配模式<br/>

action：在找到匹配内容时所执行的一系列命令<br/>

使用-F参数指定分隔符。<br/>

awk命令的内置变量包括：

| 变量名   | 说明                                   |
| -------- | -------------------------------------- |
| FILENAME | 文件名                                 |
| NR       | 已读取的记录                           |
| NF       | 浏览记录的域的个数（切割后，列的个数） |



## 2、测试

```shell
# 准备数据
[root@hadoop101 datas]$ sudo cp /etc/passwd ./

# 搜索passwd文件以root关键字开头的所有行，并输出该行的第7列。
[root@hadoop101 datas]$ awk -F: '/^root/{print $7}' passwd 
/bin/bash

# 搜索passwd文件以root关键字开头的所有行，并输出该行的第1列和第7列，中间以“，”号分割。
[root@hadoop101 datas]$ awk -F: '/^root/{print $1","$7}' passwd 
root,/bin/bash

# 只显示/etc/passwd的第一列和第七列，以逗号分割，且在所有行前面添加列名user，shell在最后一行添加"dahaige，/bin/zuishuai"。
[root@hadoop101 datas]$ awk -F : 'BEGIN{print "user, shell"} {print $1","$7} END{print "dahaige,/bin/zuishuai"}' passwd
user, shell
root,/bin/bash
bin,/sbin/nologin
。。。
atguigu,/bin/bash
dahaige,/bin/zuishuai

# 将passwd文件中的用户id增加数值1并输出
[root@hadoop101 datas]$ awk -v i=1 -F: '{print $3+i}' passwd
1
2
3
4

# 统计 passwd 文件名，每行的行号，每行的列数
[root@hadoop101 datas]$ awk -F: '{print "filename:"  FILENAME ", linenumber:" NR  ",columns:" NF}' passwd 
filename:passwd, linenumber:1,columns:7
filename:passwd, linenumber:2,columns:7
filename:passwd, linenumber:3,columns:7

# 切割IP
[root@apple workspace]# ifconfig | awk -F " " '/netmask/{print $2}'
192.168.41.100
127.0.0.1
192.168.122.1

# 查询sed.txt中空行所在的行号
[root@hadoop101 datas]$ awk '/^$/{print NR}' sed.txt 
5
```



PS：如果命令很长，可以使用反斜杠换行输入

```shell
[root@apple workspace]# awk -F : \
> 'BEGIN{print "user\t\tshell"} {print $1"\t\t"$7} END{print "dahaige\t\t/bin/zuishuai"}' \
> passwd
```

[上一条](verse05-04-cut.html) [回目录](verse05-00-index.html) [下一条](verse05-06-sort.html)

