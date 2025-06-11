[[toc]]

# 第五节 字符串命令：面试真题

## 1、京东

### ①问题1：

使用 Linux 命令查询 file1 中空行所在的行号

```shell
[root@hadoop101 datas]$ awk '/^$/{print NR}' sed.txt 
5
```



### ②问题2：

有文件 chengji.txt 内容如下：

张三 40

李四 50

王五 60

使用Linux命令计算第二列的和并输出

```shell
[root@hadoop101 datas]$ cat chengji.txt | awk -F " " '{sum+=$2} END{print sum}'
150
```



```shell
[root@apple workspace]# awk '{sum+=$2} {print $1","$2} END{print "总分："sum}' chengji.txt 
张三,50
小红,60
小刚,100
总分：210
```



## 2、新浪

用shell写一个脚本，对文本中无序的一列数字排序

```shell
[root@CentOS6-2 ~]# cat test.txt
9
8
7
6
5
4
3
2
10
1
[root@CentOS6-2 ~]# sort -n test.txt|awk '{a+=$1;print $1}END{print "SUM="a}'
1
2
3
4
5
6
7
8
9
10
SUM=55
```



## 3、金和网络

请用shell脚本写出查找当前文件夹（/home）下所有的文本文件内容中包含有字符”shen”的文件名称

```shell
[root@hadoop101 datas]$ grep -r "shen" /home | cut -d ":" -f 1
/home/atguigu/datas/sed.txt
/home/atguigu/datas/cut.txt
```



```shell
[root@apple workspace]# grep -r "shen" /root/workspace/
/root/workspace/sed.txt:dong shen
/root/workspace/cut.txt:dong shen
[root@apple workspace]# grep -r "shen" /root/workspace/ | awk -F : '{print $1}'
/root/workspace/sed.txt
/root/workspace/cut.txt
[root@apple workspace]# grep -r "shen" /root/workspace/ | awk -F : '{print $1}' | awk -F '/' '{print $4}'
sed.txt
cut.txt
```

[上一条](verse05-07-xargs.html) [回目录](verse05-00-index.html)