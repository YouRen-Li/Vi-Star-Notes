# 第四节 辅助命令：反斜杠

符号：\

作用：如果一个命令特别长，那么可以使用反斜杠表示到下一行继续输入

示例：

::: tip

./configure \\<br/>
--prefix=/usr/local/nginx \\<br/>
--pid-path=/var/run/nginx/nginx.pid \\<br/>
--lock-path=/var/lock/nginx.lock \\<br/>
--error-log-path=/var/log/nginx/error.log \\<br/>
--http-log-path=/var/log/nginx/access.log \\<br/>
--with-http_gzip_static_module \\<br/>
--http-client-body-temp-path=/var/temp/nginx/client \\<br/>
--http-proxy-temp-path=/var/temp/nginx/proxy \\<br/>
--http-fastcgi-temp-path=/var/temp/nginx/fastcgi \\<br/>
--http-uwsgi-temp-path=/var/temp/nginx/uwsgi \\<br/>
--http-scgi-temp-path=/var/temp/nginx/scgi

:::



[上一条](verse04-04-shutdown.html) [回目录](verse04-00-index.html) [下一条](verse04-06-curl.html)