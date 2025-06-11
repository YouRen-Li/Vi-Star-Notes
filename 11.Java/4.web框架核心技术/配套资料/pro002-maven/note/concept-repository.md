[[toc]]

# 仓库

- 本地仓库：在当前电脑上，为电脑上所有工程服务
- 远程仓库：需要联网
  - 局域网：我们自己搭建的Maven私服，例如使用Nexus技术。
  - Internet
    - 中央仓库
    - 镜像仓库：内容和中央仓库保持一致，但是能够分担中央仓库的负载，同时让用户能够就近访问提高下载速度，例如：Nexus aliyun

建议：不要中央仓库和阿里云镜像混用，以免发生冲突等问题<br/>

专门搜索Maven依赖信息的网站：https://mvnrepository.com/<br/>



[上一节](concept-polymerization.html) [回目录](index.html)