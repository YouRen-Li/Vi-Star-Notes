[[toc]]

# 插件和目标

## 1、插件

Maven的核心程序仅仅负责宏观调度，不做具体工作。具体工作都是由Maven插件完成的。例如：编译就是由maven-compiler-plugin-3.1.jar插件来执行的。

## 2、目标

一个插件可以对应多个目标，而每一个目标都和生命周期中的某一个环节对应。<br/>

Default生命周期中有compile和test-compile两个和编译相关的环节，这两个环节对应compile和test-compile两个目标，而这两个目标都是由maven-compiler-plugin-3.1.jar插件来执行的。



[上一节](concept-coordinate.html) [回目录](index.html) [下一节](concept-dependency.html)