![img](assets/timg.jpg)

# 1. 概述

## 1.1.   现实问题

一款产品从开发到上线，从操作系统到运行环境，再到应用配置。作为开发+运维之间的协作我们需要关心很多东西，这也是很多互联网公司都不得不面对的问题，特别是各种版本的迭代之后，不同版本**环境的兼容**，对运维人员都是考验

之前在服务器配置一个应用的运行环境，要安装各种软件，就拿实际做过的项目的环境来说吧，Java/Tomcat/MySQL/JDBC驱动包等。安装和配置这些东西有多麻烦就不说了，它还不能跨平台。假如我们是在 Windows 上安装的这些环境，到了 Linux 又得重新装。况且就算不跨操作系统，换另一台同样操作系统的服务器，要移植应用也是非常麻烦的。

传统上认为，软件编码开发/测试结束后，所产出的成果即是程序或是能够编译执行的二进制字节码等(java为例)。而为了让这些程序可以顺利执行，开发团队也得准备完整的部署文件，让维运团队得以部署应用程式，**开发需要清楚的告诉运维部署团队，用的全部配置文件+所有软件环境。**不过，即便如此，仍然常常发生部署失败的状况。

环境配置如此麻烦，换一台机器，就要重来一次，旷日费时。很多人想到，能不能从根本上解决问题，软件可以带环境安装？也就是说，安装的时候，把原始环境一模一样地复制过来。



## 1.2.   虚拟机技术

虚拟机（virtual machine）就是带环境安装的一种解决方案。
它可以在一种操作系统里面运行另一种操作系统，比如在Windows 系统里面运行Linux 系统。应用程序对此毫无感知，因为虚拟机看上去跟真实系统一模一样，而对于底层系统来说，虚拟机就是一个普通文件，不需要了就删掉，对其他部分毫无影响。这类虚拟机完美的运行了另一套系统，能够使应用程序，操作系统和硬件三者之间的逻辑不变。  

![1563086518552](assets/1563086518552.png)

虚拟机的缺点：
1    资源占用多               2    冗余步骤多                 3    启动慢



## 1.3.   Linux 容器

由于前面虚拟机存在这些缺点，Linux 发展出了另一种虚拟化技术：Linux 容器（Linux Containers，缩写为 LXC）。
**Linux 容器不是模拟一个完整的操作系统，而是对进程进行隔离。**有了容器，就可以**将软件运行所需的所有资源打包到一个隔离的容器中**。容器与虚拟机不同，不需要捆绑一整套操作系统，只需要软件工作所需的库资源和设置。系统因此而变得高效轻量并保证部署在任何环境中的软件都能始终如一地运行。

![img](assets/1100338-20181010205426157-1788702025.png)

由于容器是进程级别的，相比虚拟机有很多优势。

**（1）启动快**

传统虚拟机技术是虚拟出一套硬件后，在其上运行一个完整操作系统，在该系统上再运行所需应用进程；而**容器内的应用进程直接运行于宿主的内核**，容器内没有自己的内核，而且也没有进行硬件虚拟。因此容器要比传统虚拟机更为轻便。所以，启动容器相当于启动本机的一个进程，而不是启动一个操作系统，速度就快很多。

**（2）资源占用少**

容器只占用需要的资源，不占用那些没有用到的资源；虚拟机由于是完整的操作系统，不可避免要占用所有资源。另外，**多个容器可以共享资源，虚拟机都是独享资源**。

**（3）体积小**

容器只要包含用到的组件即可，而虚拟机是整个操作系统的打包，所以容器文件比虚拟机文件要小很多。

总之，容器有点像轻量级的虚拟机，能够提供虚拟化的环境，但是成本开销小得多。



## 1.4.   docker介绍

**Docker 属于 Linux 容器的一种封装，提供简单易用的容器使用接口。**它是目前最流行的 Linux 容器解决方案。

![1563089349712](assets/1563089349712.png)

Docker是基于Go语言实现的云开源项目。
Docker的主要目标是“Build，Ship and Run Any App,Anywhere”，也就是通过对应用组件的封装、分发、部署、运行等生命周期的管理，使用户的APP（可以是一个WEB应用或数据库应用等等）及其运行环境能够做到“**一次封装，到处运行**”。

![1563089390838](assets/1563089390838.png)

Docker镜像的设计，使得Docker得以打破过去「程序即应用」的观念。**通过镜像(images)将系统核心之外，应用程序运作所需要的系统环境，由下而上打包，达到应用程式跨平台间的无缝接轨运作。**

Docker 将应用程序与该程序的依赖，打包在一个文件里面。运行这个文件，就会生成一个虚拟容器。程序在这个虚拟容器里运行，就好像在真实的物理机上运行一样。有了 Docker，就不用担心环境问题，这就实现了跨平台、跨服务器。**只需要一次配置好环境，换到别的机子上就可以一键部署好，大大简化了操作**



## 1.5.   docker的优势

符合企业级开发中的devops思想，可以做到一次构建、随处运行：

1. **更快速的应用交付和部署**

   传统的应用开发完成后，需要提供一堆安装程序和配置说明文档，安装部署后需根据配置文档进行繁杂的配置才能正常运行。Docker化之后**只需要交付少量容器镜像文件，在正式生产环境加载镜像并运行即可**，应用安装配置在镜像里已经内置好，大大节省部署配置和测试验证时间。

2. **更便捷的升级和扩缩容**

   随着微服务架构和Docker的发展，大量的应用会通过微服务方式架构，应用的开发构建将变成搭乐高积木一样，每个Docker容器将变成一块“积木”，应用的升级将变得非常容易。当现有的容器不足以支撑业务处理时，**可通过镜像运行新的容器进行快速扩容，使应用系统的扩容从原先的天级变成分钟级甚至秒级。**

3. **更简单的系统运维**

   应用容器化运行后，**生产环境运行的应用可与开发、测试环境的应用高度一致**，容器会将应用程序相关的环境和状态完全封装起来，不会因为底层基础架构和操作系统的不一致性给应用带来影响，产生新的BUG。当出现程序异常时，也可以通过测试环境的相同容器进行快速定位和修复。

4. **更高效的计算资源利用**

   **Docker是内核级虚拟化，其不像传统的虚拟化技术一样需要额外的Hypervisor支持**，所以在一台物理机上可以运行很多个容器实例，可大大提升物理服务器的CPU和内存的利用率。

从下面这张表格很清楚地看到容器相比于传统虚拟机的特性的优势所在：

| 特性       | 容器               | 虚拟机       |
| :--------- | :----------------- | :----------- |
| 启动       | 秒级               | 分钟级       |
| 硬盘使用   | 一般为MB           | 一般为GB     |
| 性能       | 接近原生           | 弱于         |
| 系统支持量 | 单机支持上千个容器 | 一般是几十个 |



## 1.6.   docker企业级实践

docker自2013年诞生起，便迅速在全球范围内火爆起来，当前很多互联网型公司都在使用docker。例如：阿里巴巴、网易、新浪、美团、蘑菇街等等

### 1.6.1.    新浪

![1563090927162](assets/1563090927162.png)

![1563090972450](assets/1563090972450.png)

![1563090983776](assets/1563090983776.png)

![1563091010473](assets/1563091010473.png)



### 1.6.2.    美团

![1563091055819](assets/1563091055819.png)

![1563091065694](assets/1563091065694.png)

### 1.6.3.    蘑菇街

![1563091101890](assets/1563091101890.png)

![1563091109442](assets/1563091109442.png)



# 2. 下载及安装

docker官网：http://www.docker.com

Docker Hub仓库官网: https://hub.docker.com/

Docker 是一个开源的商业产品，有两个版本：社区版（Community Edition，缩写为 CE）和企业版（Enterprise Edition，缩写为 EE）。企业版包含了一些收费服务，个人开发者一般用不到。下面的介绍都针对社区版。

这里我们安装linux centOS版本

 ![1563092315537](assets/1563092315537.png)



## 2.1.   环境要求

1. CentOS Docker支持以下的CentOS版本：

- CentOS 7 (64-bit)

- CentOS 6.5 (64-bit) 或更高的版本



2. 前提条件

目前，CentOS 仅发行版本中的内核支持 Docker。

Docker 运行在 CentOS 7 上，要求系统为64位、系统内核版本为 3.10 以上。

Docker 运行在 CentOS-6.5 或更高的版本的 CentOS 上，要求系统为64位、系统内核版本为 2.6.32-431 或者更高版本。



查看自己的内核
uname命令用于打印当前系统相关信息（内核版本号、硬件架构、主机名称和操作系统类型等）。

```shell
uname -r
cat /etc/redhat-release
```

![1563092628058](assets/1563092628058.png)



## 2.2.   centos7安装Docker

可以参考官方文档：https://docs.docker.com/install/linux/docker-ce/centos/

官网中文安装手册：https://docs.docker-cn.com/engine/installation/linux/docker-ce/centos/#prerequisites

确保系统是centOS7系统及以上版本，并且网络连接正常。



1. 如果已经安装过，卸载旧版本：

```shell
yum -y remove docker \
    docker-client \
    docker-client-latest \
    docker-common \
    docker-latest \
    docker-latest-logrotate \
    docker-logrotate \
    docker-engine
```



2. yum安装gcc相关

```shell
yum -y install gcc
yum -y install gcc-c++
```



3. 安装需要的软件包

```shell
yum install -y yum-utils device-mapper-persistent-data lvm2
```



4. 设置yum仓库

```shell
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

注意：这里不要使用官方仓库，使用阿里仓库，否则后续yum安装可能报错。

报错：
	1   [Errno 14] curl#35 - TCP connection reset by peer

​	2   [Errno 12] curl#35 - Timeout



5. 更新yum软件包索引（速度可能较慢）

```shell
yum makecache fast
```



6. 安装DOCKER CE（容器管理工具）

```shell
yum -y install docker-ce
```

如果报错，重新执行安装一次

7. 启动docker

```shell
systemctl start docker
```



8. 测试

```shell
docker version
# 或者
docker run hello-world
```

![1563097940990](assets/1563097940990.png)



9. 卸载

```shell
systemctl stop docker 
yum -y remove docker-ce
rm -rf /var/lib/docker
```



## 2.3.   基本概念

`Docker` 中包括三个基本的概念：

- `Image`（镜像）
- `Container`（容器）
- `Repository`（仓库）

镜像是 `Docker` 运行容器的前提，仓库是存放镜像的场所，可见镜像更是 `Docker` 的核心。



**镜像（Image）**

​		Docker 镜像（Image）就是一个只读的模板。镜像可以用来创建 Docker 容器，一个镜像可以创建很多容器。

​		`Docker` 镜像可以看作是一个特殊的文件系统，除了提供容器运行时所需的程序、库、资源、配置等文件外，还包含了一些为运行时准备的一些配置参数（如匿名卷、环境变量、用户等）。镜像不包含任何动态数据，其内容在构建之后也不会被改变。



**容器（Container）**

​		Docker 利用容器（Container）独立运行的一个或一组应用。容器是用镜像创建的运行实例。

​		它可以被启动、开始、停止、删除。每个容器都是相互隔离的、保证安全的平台。

​		可以把容器看做是一个简易版的 Linux 环境（包括root用户权限、进程空间、用户空间和网络空间等）和运行在其中的应用程序。

​		容器的定义和镜像几乎一模一样，也是一堆层的统一视角，唯一区别在于容器的最上面那一层是可读可写的。

![1563097536554](assets/1563097536554.png)



**仓库（repository）**

​		仓库（Repository）是**集中存放镜像文件的场所。**

​		**仓库**(Repository)和**仓库注册服务器**（Registry）是有区别的。**仓库注册服务器上往往存放着多个仓库，每个仓库中又包含了多个镜像，每个镜像有不同的标签（tag）。标签就是对应软件的各个版本**

​		仓库分为公开仓库（Public）和私有仓库（Private）两种形式。

​		最大的公开仓库是 Docker Hub(https://hub.docker.com/)，存放了数量庞大的镜像供用户下载。国内的公开仓库包括阿里云 、网易云 等



总结：

Docker 本身是一个**容器运行载体或称之为管理引擎**。我们把应用程序和配置依赖打包好形成一个可交付的运行环境，这个打包好的运行环境就是 image镜像文件。只有通过这个镜像文件才能生成 Docker 容器。image 文件可以看作是容器的模板。Docker 根据 image 文件生成容器的实例。同一个 image 文件，可以生成多个同时运行的容器实例。

*  image 文件可以生成容器实例，本身也是一个文件，称为镜像文件。

*  一个容器运行一种服务，当我们需要的时候，就可以通过docker客户端创建一个对应的运行实例，也就是我们的容器

* 至于仓库，就是放了一堆镜像的地方，我们可以把镜像发布到仓库中，需要的时候从仓库中拉下来就可以了。



## 2.4.   配置镜像加速

鉴于国内网络问题，后续拉取 Docker 镜像十分缓慢，我们可以需要配置加速器来解决：

1. 阿里云镜像
2. 网易镜像

### 2.4.1.   获取阿里镜像地址

登陆阿里云开发者平台，找到“容器镜像服务”：

![1591437026549](assets/1591437026549.png)

![1591437073434](assets/1591437073434.png)

镜像加速器地址，每个人都不一样，拷贝加速器地址

![1591444615256](assets/1591444615256.png)





### 2.4.2.   centos7配置镜像地址

```shell
mkdir -p /etc/docker
vim /etc/docker/daemon.json
systemctl daemon-reload
systemctl restart docker
```

daemon.json文件的内容如下：

```
{"registry-mirrors": ["http://hub-mirror.c.163.com"] }
{
  "registry-mirrors": ["自己的阿里云加速器地址"]
}

```

查看服务状态：能正常启动，说明配置成功了

如果运行失败，执行dockerd命令查看错误

![1563100966409](assets/1563100966409.png)



## 2.5.   docker run hello-world

![1563101442152](assets/1563101442152.png)

运行流程如下：

![1563101480076](assets/1563101480076.png)



## 2.6.   再谈底层原理

**Docker是怎么工作的？**

Docker是一个Client-Server结构的系统，Docker守护进程运行在主机上， 然后通过Socket连接从客户端访问，守护进程从客户端接受命令并管理运行在主机上的容器。 **容器，是一个运行时环境，就是我们前面说到的集装箱**。

![1563101911652](assets/1563101911652.png)



**为什么Docker比较比VM快？**

(1)docker有着比虚拟机更少的抽象层。由于docker不需要Hypervisor实现硬件资源虚拟化,运行在docker容器上的程序直接使用的都是实际物理机的硬件资源。因此在CPU、内存利用率上docker将会在效率上有明显优势。

(2)docker利用的是宿主机的内核,而不需要Guest OS。因此,当新建一个容器时,docker不需要和虚拟机一样重新加载一个操作系统内核。仍而避免引寻、加载操作系统内核返个比较费时费资源的过程,当新建一个虚拟机时,虚拟机软件需要加载Guest OS,返个新建过程是分钟级别的。而docker由于直接利用宿主机的操作系统,则省略了返个过程,因此新建一个docker容器只需要几秒钟。

![1563102000953](assets/1563102000953.png)



# 3. 常用命令

## 3.1.   基础命令

```shell
docker version # 查看docker版本信息
docker info # 查看docker及环境信息
docker help # 查看帮助文档
```



## 3.2.   镜像命令

```shell
docker images  # 列出本地主机上的镜像
docker search [options] <某个XXX镜像名字>  # 搜索镜像  一般在dockerhub网站中搜索
docker pull <某个XXX镜像名字>  # 下载镜像
docker rmi [options] <某个XXX镜像名字ID>  # 删除镜像
```



### 3.2.1.    查询本机镜像

```shell
docker images  # 列出本地主机上的镜像
	-a #列出本地所有的镜像（含中间映像层）
	-q #只显示镜像ID。
	--digests #显示镜像的摘要信息
	--no-trunc #显示完整的镜像信息
```

![1563114207468](assets/1563114207468.png)

各个选项说明:

```
REPOSITORY：表示镜像的仓库源
TAG：镜像的标签
IMAGE ID：镜像ID
CREATED：镜像创建时间
SIZE：镜像大小
```

 同一仓库源可以有多个 TAG，代表这个仓库源的不同个版本，我们使用 

REPOSITORY:TAG 来定义不同的镜像。

如果你不指定一个镜像的版本标签，例如你只使用 ubuntu，docker 将默认使用 ubuntu:latest 镜像

docker images -qa  :删除时使用

docker  images

### 3.2.2.    搜索镜像

```shell
docker search [options] <某个XXX镜像名字>  # 搜索镜像
	--no-trunc # 显示完整的镜像描述
	--filter=stars=n # 列出收藏数不小于n的镜像。
	--automated # 只列出 automated build类型的镜像
```

镜像的搜索都是到官网搜索（**docker hub**），使用命令搜索的结果和在网站上搜索的结果一致。

Docker Hub(https://hub.docker.com/)

下载可以通过镜像加速下载，搜索都是在官网搜索的。



### 3.2.3.    下载镜像

```shell
docker pull 镜像名字[:TAG] # 默认是最新版本，可以指定版本号下载
```

1. 下载centos

![1563114127390](assets/1563114127390.png)

2. 下载nginx

![1563114163126](assets/1563114163126.png)

3. 下载tomcat

![1563114088264](assets/1563114088264.png)

4、下载java8

```
docker  pull  java:8
```



### 3.2.4.    删除镜像

```shell
docker rmi [options] <某个XXX镜像名字ID>  # 删除镜像
docker rmi 	-f <镜像ID>  # 删除单个
docker rmi 	-f <镜像名1:TAG> <镜像名2:TAG>  # 删除多个
docker rmi  -f $(docker images -qa) # 删除全部
```

![1563115303797](assets/1563115303797.png)



## 3.3.   容器命令

```shell
docker run IMAGE  # 新建启动容器
docker ps  # 查看所有正在运行的容器
exit # 停止并退出容器
ctrl + P + Q  # 不停止退出容器
docker start 容器ID或者容器名 # 启动容器
docker restart 容器ID或者容器名 # 重启容器
docker stop 容器ID或者容器名 # 停止容器
docker kill 容器ID或者容器名 # 强制停止容器
docker rm 容器ID  # 删除已停止的容器

docker logs 容器ID  # 查看容器日志， 
docker top 容器ID # 查看容器内运行的进程
docker exec -it 容器ID /bin/bash  # 在容器中打开新的终端，并且可以启动新的进程
docker attach 容器ID  # 直接进入容器启动命令的终端，不会启动新的进程
docker cp 需要拷贝的文件或目录 容器名称:容器目录 # 拷贝文件到容器指定目录下
docker inspect 容器ID或者容器名 # 查看容器细节
```

**有镜像才能创建容器，这是根本前提(下载一个CentOS镜像测试)**



### 3.3.1.    新建并启动容器

```shell
docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
    --name="容器新名字" # 为容器指定一个名称；
    -d # 后台运行容器，并返回容器ID，也即启动守护式容器；
    -i # 以交互模式运行容器，通常与 -t 同时使用；
    -t # 为容器重新分配一个伪输入终端，通常与 -i 同时使用；
    -P # 随机端口映射，并将容器内部使用的网络端口映射到我们使用的主机上；
    -p # 指定端口映射，有以下四种格式
          ip:hostPort:containerPort
          ip::containerPort
          hostPort:containerPort # 将containerPort映射到主机上的hostPort端口
          containerPort
	-v 主机目录:容器目录 # 挂载 宿主机的目录挂载到容器的指定目录 
```

方式①：以交互方式运行docker，并打开docker内的命令行窗口：`docker run -it centos:7`

![1563149329793](assets/1563149329793.png)

```
//退出并关闭容器
exit
```

方式②：如果要上传文件到docker容器，可以使用-v参数：docker run -itv /opt:/usr/local/opt centos:7

-v可以将宿主机的目录与容器内的目录进行映射，这样我们就可以通过修改宿主机某个目录的文件从而去影响容器(挂载多级目录时可能权限不足，需要添加参数: **--privileged=true**)

![1563150158461](assets/1563150158461.png)

```
ctrl + P + Q  # 不停止退出容器
```

xshell中  ctrl+s可以锁屏，ctrl+q可以解锁

### 3.3.2.    查询容器

**列出当前所有正在运行的容器：**

```shell
docker ps [OPTIONS]
	-a # 列出当前所有正在运行的容器+历史上运行过的
    -l # 显示最近创建的容器。
    -n # 显示最近n个创建的容器。   docker ps -n 3
    -q # 静默模式，只显示容器编号。
    --no-trunc # 不截断输出。
```

![1563150216332](assets/1563150216332.png)



### 3.3.3.    删除容器

```shell
docker rm 容器ID # 删除指定容器
docker rm 容器Name #根据容器名删除容器
docker rm -f $(docker ps -a -q) # 删除所有容器，包括正在运行的容器
docker ps -a -q | xargs docker rm # 删除所有容器，不包括正在运行的容器
```

![1563151931359](assets/1563151931359.png)



### 3.3.4.    守护式容器命令

以后台模式启动一个容器

docker run -d 容器名

例如：`docker run -d centos:7`

然后docker ps -a 进行查看, 会发现容器已经退出

很重要的要说明的一点: Docker容器后台运行，就必须有一个前台进程.

容器运行的命令如果不是那些一直挂起的命令（比如运行top，tail），就是会自动退出的。

这个是docker的机制问题：**容器后台启动后，如果他觉得他没事可做会立即自杀**

![1563153421967](assets/1563153421967.png)

所有要给我的docker容器干点事儿，否则就会自杀。

创建并启动docker容器：

```
docker run -d centos:7 /bin/bash -c "while true;do echo hello zzyy;sleep 2;done"
```



### 3.3.5.    查看docker容器日志

查看容器所有日志：

```shell
docker container logs 容器ID
```

跟随查看容器日志：

```shell
docker logs -f -t --tail n 容器ID  # 查看容器日志 
	-t # 是加入时间戳
	-f # 跟随最新的日志打印
	--tail 数字 # 显示最后多少条
```

查看打印日志：docker logs -f -t --tail 10 7dd1f44842fc

![1563155252043](assets/1563155252043.png)

ctrl+c  可以退出跟随查看

### 3.3.6.    重新进入docker

```shell
docker exec -it 容器ID /bin/bash  # 在容器中打开新的终端，并且可以启动新的进程
docker attach 容器ID  # 直接进入容器启动命令的终端，不会启动新的进程
docker exec -it 容器ID ls -l /tmp # 在容器外执行docker内命令
```

![1563156824555](assets/1563156824555.png)











### 3.3.7.    tomcat部署

创建并启动docker：

```shell
docker run -d -p 8888:8080 tomcat  # 虚拟机8888端口，对应的是docker的8080端口
```

![1563157909061](assets/1563157909061.png)

通过浏览器访问：

![1563157960553](assets/1563157960553.png)



交互方式启动tomcat：

```shell
docker run -it -p 9999:8080 tomcat
```

![1563158111931](assets/1563158111931.png)



#### 1 发布项目到tomcat：

```shell
docker run -it --name mytomcat -p 6666:8080 tomcat /bin/bash
docker run -it -v 项目目录:/usr/local/tomcat/webapps/ -p 7777:8080 tomcat /bin/bash
```

##### 1.1、通过xftp拷贝web项目到虚拟机的/opt/app/目录下

##### 1.2、docker使用tomcat镜像创建容器并启动时

​	2.1 以交互的方式执行

​	2.2 需要将/opt/app目录下的项目挂载到容器的/usr/local/tomcat/webapps目录下

​	2.3 指定虚拟机和容器的端口号对应关系

```
docker run -it -v /opt/app/:/usr/local/tomcat/webapps/ -p 9999:8080 tomcat /bin/bash
```

##### 1.3、启动tomcat

```
/usr/local/tomcat/bin/startup.sh
```

##### 1.4、不关闭容器退出：ctrl+p+q

```
//访问测试
curl http://192.168.1.166:9999
```



### 3.3.8 nginx部署

#### 1 拉取镜像

```
docker pull nginx
```

#### 2 创建Nginx容器



```
docker run -di --name mynginx -p 80:80 nginx
```

![1591528978853](assets/1591528978853.png)

安装完成后，请求访问nginx

![1591529024025](assets/1591529024025.png)

指定配置文件启动nginx

```
docker run --name nginx -p 80:80 -v /opt/nginx.conf:/etc/nginx/nginx.conf  -d nginx
```

修改配置文件：

```
# 进入容器内部
docker exec -it nginx  /bin/bash
# 拷贝本地nginx配置文件到容器内.
docker cp /usr/local/nginx/conf/nginx.conf nginx:/etc/nginx/
```



### 3.3.9 redis部署

实际开发过程中，有些需求占用内存较大，而且读取数据要求也非常高，项目部署时直接分配较大的docker内存，项目中的数据直接从内存中存取使用

#### 1 拉取镜像

```
docker pull redis
```

#### 2 创建容器

```
docker run -di --name myredis -p 6379:6379 redis
```

创建 redis 容器

![1591529171216](assets/1591529171216.png)

连接测试：

![1591601636957](assets/1591601636957.png)

### 3.3.10 mysql部署

```sh
docker run -p 3316:3306 --name mysql-master \
-v mysql-master-log:/var/log/mysql \
-v mysql-master-data:/var/lib/mysql \
-v mysql-master-conf:/etc/mysql/conf.d \
-e MYSQL_ROOT_PASSWORD=123456 \
--restart=always \
-d mysql:5.7
```

-v 使用相对路径的方式挂载的目录docker会自动创建，路径为：

```
/var/lib/docker/volumes/
```

修改mysql配置：



# 4. 迁移备份

![docker迁移备份](assets/docker迁移备份.jpg)

### 4.1 保存镜像

使用 `docker ps -a` 查看所有的容器

![1591529435777](assets/1591529435777.png)

通过以下命令将容器保存为镜像

```
# 保存nginx容器为镜像
docker commit 容器名称  镜像名称
例如：docker commit mynginx mynginx_img
```

![1591529502335](assets/1591529502335.png)

查看镜像是否保存成功

```
docker images
```

![1591529617456](assets/1591529617456.png)

使用保存的镜像，重新创建一个容器（基于容器保存的镜像中不会携带容器的内容）

```shell
docker run -di --name mynginx3 -p  81:80 mynginx_img
```

![1591530050568](assets/1591530050568.png)

通过docker ps查看正在运行的容器，查找新创建的容器

![1591530099153](assets/1591530099153.png)

### 4.2 镜像备份

可以通过以下命令将镜像保存为tar 文件

```shell
# 命令形式：docker save –o 文件名.tar.gz 镜像名
# 保存镜像为文件 -o：表示output 输出的意思
docker  save -o mynginx.tar.gz mynginx_img
```

通过 save 保存成文件之后，在通过 ls 命令进行查看当前目录是否有 tar文件

![1591531113499](assets/1591531113499.png)

![1591531129817](assets/1591531129817.png)

### 4.3 镜像备份恢复

首先我们先删除掉mynginx_img镜像 然后执行此命令进行恢复

#### 4.3.1 删除镜像前需要先关闭使用镜像的容器：

```shell
# 查看所有镜像
docker images
# 查看运行的容器
docker ps
# 查找占用镜像的容器关闭
docker stop 容器ID或者容器名
```

![1591530848901](assets/1591530848901.png)

#### 4.3.2 删除镜像

```
docker rmi -f  镜像名:TAG
```

![1591531019901](assets/1591531019901.png)

#### 4.3.3 恢复镜像

```
# 命令形式：docker load -i 文件名.tar.gz
docker load -i mynginx.tar.gz
```

-i 表示input输入的文件

执行后再次查看镜像，可以看到镜像已经恢复

![1591531233138](assets/1591531233138.png)

# 5. dockerfile

## 5.1 什么是Dockerfile

前面的课程中已经知道了，要获得镜像，可以从Docker仓库中进行下载。那如果我们想自己开发一个镜像，那该如何做呢？答案是：Dockerfile
Dockerfile其实就是一个文本文件，由一系列命令和参数构成，Docker可以读取Dockerfile文件并根据Dockerfile文件的描述来构建镜像。

1、对于开发人员：可以为开发团队提供一个完全一致的开发环境；
2、对于测试人员：可以直接拿开发时所构建的镜像或者通过Dockerfile文件构建一个新的镜像开始工作了；
3、对于运维人员：在部署时，可以实现应用的无缝移植。

## 5.2 使用脚本创建自定义docker镜像

基于centos7创建启动springboot项目的docker镜像

步骤：

（1）在虚拟机/opt下创建空目录

```
mkdir /opt/myappdocker
```

拷贝课件中提供的jdk压缩包到新创建的目录下

```
 mv /opt/jdk1.8.0_152.tar.gz /opt/myappdocker/
```

（2）使用xftp将springboot项目的jar包上传到第一步创建的目录下：

![image-20200830160952762](assets/image-20200830160952762.png)

（3）在/opt/myappdocker目录下创建文件Dockerfile 

```
# Dockerfile 名字不要改
vim Dockerfile
```

Dockerfile中编写以下内容保存退出

```dockerfile
# 基于centos7镜像创建新镜像
FROM centos:7
# 镜像作者
MAINTAINER atguigu
# 镜像内工作目录
WORKDIR /usr
# 镜像内执行命令：新建目录
RUN mkdir /usr/local/java
# 上传本地文件到镜像中并解压缩
ADD jdk1.8.0_152.tar.gz /usr/local/java/
# 声明需要暴露的端口 
EXPOSE 8080
# 配置java环境变量
ENV JAVA_HOME /usr/local/java/jdk1.8.0_152
ENV PATH $JAVA_HOME/bin:$PATH
# 镜像内执行命令
RUN mkdir /usr/local/myapp
# 上传本地项目到镜像指定目录
COPY springboot-hello-online-0.0.1-SNAPSHOT.jar /usr/local/myapp/
# 执行命令运行jar包(也可以使用RUN)
ENTRYPOINT ["nohup" ,"java" ,"-jar" ,"/usr/local/myapp/springboot-hello-online-0.0.1-SNAPSHOT.jar" ,"--server.port=10000" ,"&"]
```

dockerfile命令：

| 命令                               | 作用                                                         |
| ---------------------------------- | ------------------------------------------------------------ |
| FROM image_name:tag                | 定义了使用哪个基础镜像启动构建流程                           |
| MAINTAINER user_name               | 声明镜像的创建者                                             |
| ENV key value                      | 设置环境变量 (可以写多条)                                    |
| RUN command                        | 是Dockerfile的核心部分(可以写多条)                           |
| ADD source_dir/file dest_dir/file  | 将宿主机的文件复制到容器内，如果是一个压缩文件，将会在复制后自动解压 |
| COPY source_dir/file dest_dir/file | 和ADD相似，但是如果有压缩文件并不能解压                      |
| WORKDIR path_dir                   | 设置工作目录                                                 |



（5）执行命令构建镜像

```
docker build -t='myappdockerimg' .
```

注意后边的空格和点，不要省略

![image-20200830162124328](assets/image-20200830162124328.png)

（5）查看镜像是否建立完成

```
docker images
```

![image-20200830162226133](assets/image-20200830162226133.png)

(6) 使用镜像运行创建容器运行访问测试

创建容器以守护的方式运行：

```
docker run -d -p 80:10000 myappdockerimg
```

查看运行的容器：

```
docker ps
```

![image-20200830162403030](assets/image-20200830162403030.png)

浏览器访问测试：

![image-20200830163116242](assets/image-20200830163116242.png)



# 6. 常用中间件安装

## 6.1 rabbitmq

下载镜像：

```
docker pull rabbitmq:management
```

创建实例并启动：

```shell
docker run -d -p 5672:5672 -p 15672:15672 -p 25672:25672 --name rabbitmq rabbitmq:management
```

注：
5672 --client通信端口
15672 -- 管理界面ui端口
25672 -- server间内部通信口

**访问测试：**

​	在web浏览器中输入地址：http://虚拟机ip:15672/

​	输入默认账号: guest   : guest

## 6.2 elasticsearch

安装es和ik分词器：

```dockerfile
# 下载es镜像
docker pull elasticsearch:7.6.2
# 运行es
docker run --name elasticsearch7.6.2 -d -e ES_JAVA_OPTS="-Xms512m -Xmx512m" --net host -e "discovery.type=single-node" -p 9200:9200 -p 9300:9300 elasticsearch:7.6.2
#上述命令执行完成之后，容器创建成功，有的机器需要10分钟左右才能访问成功，请耐心等待
#上传ik分词器并解压：
unzip elasticsearch-analysis-ik-7.6.2.zip -d ik-analyzer
# es安装分词器
docker cp ./ik-analyzer elasticsearch7.6.2:/usr/share/elasticsearch/plugins
# 重启es容器
docker restart elasticsearch7.6.2
```

安装kibana：

```dockerfile
# 下载kibana镜像(版本必须和es一致)
docker pull kibana:7.6.2
# 运行kibana容器
docker run --name kibana7.6.2 -p 5601:5601 -d kibana:7.6.2
# kibana配置连接es：
docker exec -it kibana7.6.2 bash
# 进入kibana容器，执行：
vi /opt/kibana/config/kibana.yml
# 把对应内容修改如下：
elasticsearch.hosts: [ "http://192.168.1.170:9200" ]
# 保存并退出
# 并在exit退出kibana容器后，执行：
docker restart kibana7.6.2
# 需要等待一会儿访问：http://192.168.1.170:5601
```

注意：如果访问kibana显示连接被拒绝或者连接不上，可能是es的地址写错了，也可能是以下问题：

![image-20220702214332472](assets/image-20220702214332472.png)

解决：

```shell
# 修改配置文件：
vim /usr/lib/sysctl.d/00-system.conf
# 添加
net.ipv4.ip_forward=1
# 保存退出 重启网络
```

访问测试：

​	http://虚拟机ip:5601



# 7. Docker私有仓库

Docker官方的Docker hub（https://hub.docker.com）是一个用于管理公共镜像的仓库，我们可以从上面拉取镜像到本地，也可以把我们自己的镜像推送上去。但是，有时候我们的服务器无法访问互联网，或者你不希望将自己的镜像放到公网当中，那么我们就需要搭建自己的私有仓库来存储和管理自己的镜像。

## 7.1 私有仓库搭建与配置

（1）拉取私有仓库镜像

```
docker pull registry
```

（2）启动私有仓库容器

```
docker run -di --name=registry -p 5000:5000 registry
```

（3）访问私有仓库

```
#打开浏览器 输入地址 
http://192.168.1.171:5000/v2/_catalog 
```

看到{"repositories":[]} 表示私有仓库搭建成功并且内容为空

（4）修改daemon.json

```
vi /etc/docker/daemon.json
```

添加以下内容，让容器信任下面的地址,保存退出。

```
"insecure-registries":["192.168.1.171:5000"]
```

（5）重启docker 服务

```
systemctl restart docker
```

## 7.2 镜像上传至私有仓库

### 1. 标记此镜像为私有仓库的镜像

```
# 标记镜像为私有仓库的镜像   
docker tag springboot-demo:1.0.0 192.168.1.171:5000/springboot-demo:1.1.1
```

### 2. 再次启动私服容器

```
# 再次启动私有仓库容器   
docker start registry
```

### 3. 上传标记的镜像

```
#上传标记的镜像   
docker push 192.168.1.171:5000/springboot-demo:1.1.1
```

**上传失败：**

![image-20230316194636601](assets/image-20230316194636601.png)

**解决：**Docker1.3.X之后docker registry交互默认使用的是HTTPS，但是搭建私有镜像默认使用的是HTTP服务，私有镜像上传时出现以上错误。

```
vim /usr/lib/systemd/system/docker.service
```

添加以下内容：

```
# 在ExecStart的行尾添加：
--insecure-registry  192.168.1.171:5000
```

![image-20230316195006496](assets/image-20230316195006496.png)

重启docker服务和私有仓库容器：

```
systemctl daemon-reload 
systemctl restart docker
docker restart registry
```

重新上传标记的镜像

```
#上传标记的镜像   
docker push 192.168.1.171:5000/springboot-demo:1.1.1
```

## 7.3 从私有仓库拉取镜像

```
#执行拉取镜像命令并查看 
docker pull 192.168.1.171:5000/springboot-demo:1.1.1
```



# 8. 总结

![1563159145676](assets/1563159145676.png)

attach    Attach to a running container  # 当前 shell 下 attach 连接指定运行镜像
build     Build an image from a Dockerfile     # 通过 Dockerfile 定制镜像
commit    Create a new image from a container changes   # 提交当前容器为新的镜像
cp     Copy files/folders from the containers filesystem to the host path   #从容器中拷贝指定文件或者目录到宿主机中
create    Create a new container                # 创建一个新的容器，同 run，但不启动容器
diff      Inspect changes on a container's filesystem   # 查看 docker 容器变化
events    Get real time events from the server          # 从 docker 服务获取容器实时事件
exec      Run a command in an existing container        # 在已存在的容器上运行命令
export    Stream the contents of a container as a tar archive # 导出容器的内容流作为一个 tar 归档文件[对应 import]
history   Show the history of an image                  # 展示一个镜像形成历史
images    List images                                   # 列出系统当前镜像
import    Create a new filesystem image from the contents of a tarball # 从tar包中的内容创建一个新的文件系统映像[对应export]
info      Display system-wide information               # 显示系统相关信息
inspect   Return low-level information on a container   # 查看容器详细信息
kill      Kill a running container         # kill 指定 docker 容器
load      Load an image from a tar archive     # 从一个 tar 包中加载一个镜像[对应 save]
login     Register or Login to the docker registry server    # 注册或者登陆一个 docker 源服务器
logout    Log out from a Docker registry server          # 从当前 Docker registry 退出
logs      Fetch the logs of a container                 # 输出当前容器日志信息
port      Lookup the public-facing port which is NAT-ed to PRIVATE_PORT  # 查看映射端口对应的容器内部源端口
pause     Pause all processes within a container        # 暂停容器
ps        List containers                               # 列出容器列表
pull      Pull an image or a repository from the docker registry server # 从docker镜像源服务器拉取指定镜像或者库镜像
push      Push an image or a repository to the docker registry server    # 推送指定镜像或者库镜像至docker源服务器
restart   Restart a running container                   # 重启运行的容器
rm        Remove one or more containers                 # 移除一个或者多个容器
rmi       Remove one or more images             # 移除一个或多个镜像[无容器使用该镜像才可删除，否则需删除相关容器才可继续或 -f 强制删除]
run       Run a command in a new container              # 创建一个新的容器并运行一个命令
save      Save an image to a tar archive                # 保存一个镜像为一个 tar 包[对应 load]
search    Search for an image on the Docker Hub         # 在 docker hub 中搜索镜像
start     Start a stopped containers                    # 启动容器
stop      Stop a running containers                     # 停止容器
tag       Tag an image into a repository                # 给源中镜像打标签
top       Lookup the running processes of a container   # 查看容器中运行的进程信息
unpause   Unpause a paused container                    # 取消暂停容器
version   Show the docker version information           # 查看 docker 版本号
wait      Block until a container stops, then print its exit code   # 截取容器停止时的退出状态值







开发中运维创建docker容器时一般会指定cpu和内存：

​		一般的docker容器配置4c8g：  一个docker绑定4个cpu，8g内存(一般不会独占)

​		特殊情况： 大企业的有些数据读取时需要的延迟非常小，会直接扔到内存中。这样的项目使用docker部署时，直接给docker容器分配非常大的内存，项目从内存中对数据进行CRUD的操作(32c256g)





