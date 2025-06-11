`尚硅谷_JavaEE_MySQL8高级`                                                       										让天下没有难学的技术

------



#  MySQL主从同步

## 1、MySQL主从同步原理

![img](assets/image-20220714133617856.png)

**基本原理：**

slave会从master读取binlog来进行数据同步

**具体步骤：**

- `step1：`master将数据改变记录到`二进制日志（binary log）`中。这些记录过程叫做二进制日志事件（binary log events）。
- `step2：`当slave连接master时，master会创建一个 `log dump 线程`，用于发送 binlog 的内容。在读取 binlog 的内容的操作中，会对主节点上的 binlog 加锁，当读取完成并发送给从服务器后解锁。
- `step3：` 当slave上执行 `start slave` 命令之后，slave会创建一个 `IO 线程`用来连接master，请求master中的binlog。IO 线程接收主节点 binlog dump 进程发来的更新之后，保存到 `relay log` 中。
- `step4：`slave的`SQL线程`，读取relay log日志，并解析成具体操作，从而实现主从操作一致，最终数据一致。



## 2、一主多从配置

服务器规划：使用`docker`方式创建，`主从服务器IP一致，端口号不一致`

![image-20220807183231101](assets/image-20220807183231101.png)

- 主服务器：容器名`atguigu-mysql-master`，端口`3306`
- 从服务器：容器名`atguigu-mysql-slave1`，端口`3307`
- 从服务器：容器名`atguigu-mysql-slave2`，端口`3308`

**注意：**如果此时防火墙是开启的，`则先关闭防火墙，并重启docker`，否则后续安装的MySQL无法启动

```shell
#关闭docker
systemctl stop docker
#关闭防火墙
systemctl stop firewalld
#启动docker
systemctl start docker
```



### 2.1、准备主服务器

- **step1：在docker中创建并启动MySQL主服务器：**`端口3306`

```shell
docker run -d \
-p 4306:3306 \
-v /atguigu/mysql/master/conf:/etc/mysql/conf.d \
-v /atguigu/mysql/master/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=123456 \
--name atguigu-mysql-master \
mysql:8.0.29
```



- **step2：创建MySQL主服务器配置文件：** 

默认情况下MySQL的binlog日志是自动开启的，可以通过如下配置定义一些可选配置

```shell
vim /atguigu/mysql/master/conf/my.cnf
```

配置如下内容

```properties
[mysqld]
# 服务器唯一id，默认值1
server-id=1
# 设置日志格式，默认值ROW
binlog_format=STATEMENT
# 二进制日志名，默认binlog
# log-bin=binlog
# 设置需要复制的数据库，默认复制全部数据库
binlog-do-db=mydb2
# 设置不需要复制的数据库
binlog-ignore-db=mysql
#binlog-ignore-db=infomation_schema
```

重启MySQL容器

```shell
docker restart atguigu-mysql-master
```



`binlog格式说明：`

- binlog_format=STATEMENT：日志记录的是主机数据库的`写指令`，性能高，但是now()之类的函数以及获取系统参数的操作会出现主从数据不同步的问题。
- binlog_format=ROW（默认）：日志记录的是主机数据库的`写后的数据`，批量操作时性能较差，解决now()或者  user()或者  @@hostname 等操作在主从机器上不一致的问题。
- binlog_format=MIXED：是以上两种level的混合使用，有函数用ROW，没函数用STATEMENT，但是无法识别系统变量



`binlog-ignore-db和binlog-do-db的优先级问题：`

![img](assets/0.08703112216569037.png)





- **step3：使用命令行登录MySQL主服务器：**

```shell
#进入容器：env LANG=C.UTF-8 避免容器中显示中文乱码
docker exec -it atguigu-mysql-master env LANG=C.UTF-8 /bin/bash
#进入容器内的mysql命令行
mysql -uroot -p
#修改默认密码插件
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
```



- **step4：主机中创建slave用户：**

```sql
-- 创建slave用户
CREATE USER 'atguigu_slave'@'%';
-- 设置密码
ALTER USER 'atguigu_slave'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
-- 授予复制权限
GRANT REPLICATION SLAVE ON *.* TO 'atguigu_slave'@'%';
-- 刷新权限
FLUSH PRIVILEGES;
```



- **step5：主机中查询master状态：**

执行完此步骤后`不要再操作主服务器MYSQL`，防止主服务器状态值变化

```sql
SHOW MASTER STATUS;
```

记下`File`和`Position`的值。执行完此步骤后不要再操作主服务器MYSQL，防止主服务器状态值变化。

![image-20220804191852164](assets/image-20220804191852164.png)



### 2.2、准备从服务器

可以配置多台从机slave1、slave2...，这里以配置slave1为例

- **step1：在docker中创建并启动MySQL从服务器：**`端口3307`

```shell
docker run -d \
-p 4307:3306 \
-v /atguigu/mysql/slave1/conf:/etc/mysql/conf.d \
-v /atguigu/mysql/slave1/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=123456 \
--name atguigu-mysql-slave1 \
mysql:8.0.29
```



- **step2：创建MySQL从服务器配置文件：** 

```shell
vim /atguigu/mysql/slave1/conf/my.cnf
```

配置如下内容：

```properties
[mysqld]
# 服务器唯一id，每台服务器的id必须不同，如果配置其他从机，注意修改id
server-id=2
# 中继日志名，默认xxxxxxxxxxxx-relay-bin
#relay-log=relay-bin
```

重启MySQL容器

```shell
docker restart atguigu-mysql-slave1
```



- **step3：使用命令行登录MySQL从服务器：**

```shell
#进入容器：
docker exec -it atguigu-mysql-slave1 env LANG=C.UTF-8 /bin/bash
#进入容器内的mysql命令行
mysql -uroot -p
#修改默认密码插件
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
```



- **step4：在从机上配置主从关系：**

在**从机**上执行以下SQL操作

```sql
CHANGE MASTER TO MASTER_HOST='192.168.100.201', 
MASTER_USER='atguigu_slave',MASTER_PASSWORD='123456', MASTER_PORT=3306,
MASTER_LOG_FILE='binlog.000003',MASTER_LOG_POS=1357; 
```



### 2.3、启动主从同步

启动从机的复制功能，执行SQL：

```sql
START SLAVE;
-- 查看状态（不需要分号）
SHOW SLAVE STATUS\G
```



**两个关键进程：**下面两个参数都是Yes，则说明主从配置成功！

![img](assets/image-20220715000533951.png)



### 2.4、实现主从同步

在主机中执行以下SQL，在从机中查看数据库、表和数据是否已经被同步

```sql
CREATE DATABASE mydb2;
USE mydb2;
CREATE TABLE t_user (
 id BIGINT AUTO_INCREMENT,
 uname VARCHAR(30),
 PRIMARY KEY (id)
);
INSERT INTO t_user(uname) VALUES('zhang3');
INSERT INTO t_user(uname) VALUES(@@hostname);
```



### 2.5、停止和重置

需要的时候，可以使用如下SQL语句

```sql
-- 在从机上执行。功能说明：停止I/O 线程和SQL线程的操作。
stop slave; 

-- 在从机上执行。功能说明：用于删除SLAVE数据库的relaylog日志文件，并重新启用新的relaylog文件。
reset slave;

-- 在主机上执行。功能说明：删除所有的binglog日志文件，并将日志索引文件清空，重新开始所有新的日志文件。
-- 用于第一次进行搭建主从库时，进行主库binlog初始化工作；
reset master;
```



### **2.6、常见问题**

#### 问题1

启动主从同步后，常见错误是`Slave_IO_Running： No 或者 Connecting` 的情况，此时查看下方的 `Last_IO_ERROR`错误日志，根据日志中显示的错误信息在网上搜索解决方案即可

![img](assets/image-20220714235426120.png)



**典型的错误例如：**`Last_IO_Error: Got fatal error 1236 from master when reading data from binary log: 'Client requested master to start replication from position > file size'`

**解决方案：**

```sql
-- 在从机停止slave
SLAVE STOP;

-- 在主机查看mater状态
SHOW MASTER STATUS;
-- 在主机刷新日志
FLUSH LOGS;
-- 再次在主机查看mater状态（会发现File和Position发生了变化）
SHOW MASTER STATUS;
-- 修改从机连接主机的SQL，并重新连接即可
```



#### 问题2

启动docker容器后提示 `WARNING: IPv4 forwarding is disabled. Networking will not work.`

![img](assets/image-20220715004850504.png)

此错误，虽然不影响主从同步的搭建，但是如果想从远程客户端通过以下方式连接docker中的MySQL则没法连接

```shell
C:\Users\administrator>mysql -h 192.168.100.201 -P 3306 -u root -p
```

**解决方案：**

```shell
#修改配置文件：
vim /usr/lib/sysctl.d/00-system.conf
#追加
net.ipv4.ip_forward=1
#接着重启网络
systemctl restart network
```


