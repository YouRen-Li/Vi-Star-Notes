[[toc]]

# 第二节 Linux系统的服务管理



## 1、服务的概念

操作系统中在后台持续运行的程序，本身并没有操作界面，需要通过端口号访问和操作。CentOS 6和CentOS 7的服务管理有很大区别，我们分别来看。



## 2、CentOS6服务

### ①service命令

启动服务：service  服务名 start

停止服务：service  服务名 stop

重启服务：service  服务名 restart

重新加载服务：service  服务名 reload

查看服务状态：service  服务名 status



### ②chkconfig命令

查看服务列表：chkconfig [--list]

设置具体服务开机自动启动状态：chkconfig 服务名 on/off

::: tip

思考：你能否区分清楚这两种状态呢？

服务现在是否运行

服务是否开机自动运行

:::



### ③运行级别

vim /etc/inittab查看系统配置。CentOS6系统使用0~6这7个数字来控制Linux系统的启动方式。

运行级别0：系统停机状态，系统默认运行级别不能设为0，否则不能正常启动

运行级别1：单用户工作状态，root权限，用于系统维护，禁止远程登陆

运行级别2：多用户状态(没有NFS)，没有网络服务

运行级别3：完全的多用户状态(有NFS)，登录后进入控制台命令行模式

运行级别4：系统未使用，保留

运行级别5：X11表示控制台，进入图形界面

运行级别6：系统正常关闭并重启，默认运行级别不能设为6，否则不能正常启动

 

<span style="color:blue;font-weight:bold;">常用的是3或5</span>。

 

chkconfig 命令使用 --level 参数和一个数值可以控制一个服务在某个运行级别的是否自动启动。



### ④防火墙

防火墙默认会阻止绝大部分端口号的访问，在实际生产环境下，运维工程师需要为服务器设置详细的访问规则。在练习过程中，我们为了方便建议把防火墙直接关闭。由于防火墙服务默认开机自动启动，所以除了<span style="color:blue;font-weight:bold;">停止服务</span>，还要设置为<span style="color:blue;font-weight:bold;">开机不自动启动</span>。

服务名：iptables

停止防火墙：service iptables stop

设置开机不自动启动：chkconfig iptables off



## 3、CentOS7服务

### ①systemctl命令

启动服务：systemctl start 服务名(xxxx.service)

重启服务：systemctl restart 服务名(xxxx.service)

停止服务：systemctl stop 服务名(xxxx.service)

重新加载服务：systemctl reload 服务名(xxxx.service)

查看服务状态：systemctl status 服务名(xxxx.service)



### ②systemctl命令代替chkconfig命令

查看服务状态：systemctl list-unit-files

设置或取消服务开机自动启动：

::: tip

设置开机自动启动：systemctl enable 服务名

取消开机自动启动：systemctl disable 服务名

:::



### ③CentOS7简化了运行级别

cat /etc/inittab

::: tip

\# inittab is no longer used when using systemd.

\#

\# ADDING CONFIGURATION HERE WILL HAVE NO EFFECT ON YOUR SYSTEM.

\#

\# Ctrl-Alt-Delete is handled by /usr/lib/systemd/system/ctrl-alt-del.target

\#

\# systemd uses 'targets' instead of runlevels. By default, there are two main targets:

\#

\# multi-user.target: analogous to runlevel 3

\# graphical.target: analogous to runlevel 5

\#

\# To view current default target, run:

\# systemctl get-default

\#

\# To set a default target, run:

\# systemctl set-default TARGET.target

:::



### ④关闭防火墙

systemctl disable firewalld.service

systemctl stop firewalld.service

请大家记住：<span style="color:blue;font-weight:bold;">斩草要除根</span>。



[上一节](verse01-auth.html) [回目录](index.html) [下一节](verse03-shell.html)