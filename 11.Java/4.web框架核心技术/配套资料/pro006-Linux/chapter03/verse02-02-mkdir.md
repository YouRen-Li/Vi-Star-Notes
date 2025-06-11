# 第二节 文件和目录相关命令：创建目录

命令：mkdir

对应单词：make directory

作用：创建目录

格式：mkdir [OPTION]... DIRECTORY...

创建单层目录：mkdir 新目录的路径

::: tip

单层目录说明：

mkdir aaa/bbb/ccc

其中aaa/bbb是存在的目录，要创建的仅仅是ccc

:::

创建多层目录：mkdir -p 新目录的路径

::: tip

多层目录说明：

mkdir -p aaa/bbb/ccc/ddd

其中bbb/ccc/ddd都不存在，现在想一次性把这些目录都建出来

:::

[上一条](verse02-01-hotkey.html) [回目录](verse02-00-index.html) [下一条](verse02-03-cd.html)