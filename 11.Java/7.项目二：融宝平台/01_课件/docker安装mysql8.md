docker安装mysql8

# 1、下载mysql8镜像创建容器启动

```
docker run -d \
-p 3396:3306 \
-v /atguigu/mysql/conf:/etc/mysql/conf.d \
-v /atguigu/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=123456 \
--name atguigu-mysql-8 \
--restart=always \
mysql:8.0.29
```

# 2、修改mysql容器密码

mysql8安装后 默认密码加密的插件不是mysql_native_password，所以之前老版本的mysql客户端工具不能直接使用密码连接必须升级到最新版本，或者修改mysql8密码加密的插件为mysql_native_password

```sh
#进入容器：env LANG=C.UTF-8 避免容器中显示中文乱码
docker exec -it atguigu-mysql-8 env LANG=C.UTF-8 /bin/bash
#进入容器内的mysql命令行
mysql -uroot -p
#使用mysql_native_password密码插件创建新的账户root % 并使用它加密密码
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
```



# 3、如果报错IpV4...解决方案如下

docker启动mysql容器时报如下错误：

WARNING: IPv4 forwarding is disabled. Networking will not work.

```
# 修改配置文件：
vim /usr/lib/sysctl.d/00-system.conf
# 添加
net.ipv4.ip_forward=1
# 保存退出 重启网络
```

# 4、重启网络和容器

```sh
systemctl restart network
docker restart atguigu-mysql-8
```

# 5、远程连接测试