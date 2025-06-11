[[toc]]

# 生命周期

## 1、作用

为了让构建过程自动化完成，Maven设定了三个生命周期，生命周期中的每一个环节对应构建过程中的一个操作。

## 2、三个生命周期

| 生命周期名称    | 作用         | 各个环节                                                     |
| --------------- | ------------ | ------------------------------------------------------------ |
| Clean生命周期   | 清理操作相关 | pre-clean<br />clean<br />post-clean                         |
| Site生命周期    | 生成站点相关 | pre-site<br />site<br />post-site<br />deploy-site           |
| Default生命周期 | 主要构建过程 | validate<br/>generate-sources<br/>process-sources<br/>generate-resources<br/>process-resources 复制并处理资源文件，至目标目录，准备打包。<br/>compile 编译项目的源代码。<br/>process-classes<br/>generate-test-sources<br/>process-test-sources<br/>generate-test-resources<br/>process-test-resources 复制并处理资源文件，至目标测试目录。<br/>test-compile 编译测试源代码。<br/>process-test-classes<br/>test 使用合适的单元测试框架运行测试。这些测试代码不会被打包或部署。<br/>prepare-package<br/>package 接受编译好的代码，打包成可发布的格式，如JAR。<br/>pre-integration-test<br/>integration-test<br/>post-integration-test<br/>verify<br/>install将包安装至本地仓库，以让其它项目依赖。<br/>deploy将最终的包复制到远程的仓库，以让其它开发人员与项目共享或部署到服务器上运行。 |

## 3、特点

- 前面三个生命周期彼此是独立的。
- 在任何一个生命周期内部，执行任何一个具体环节的操作，都是从本周期最初的位置开始执行，直到指定的地方。（本节记住这句话就行了，其他的都不需要记）

Maven之所以这么设计其实就是为了提高构建过程的自动化程度：让使用者只关心最终要干的即可，过程中的各个环节是自动执行的。



[上一节](concept-life-cycle.html) [回目录](index.html) [下一节](concept-plugin-goal.html)