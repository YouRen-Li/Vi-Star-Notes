[[toc]]

# 第七节 文件下载

## 1、初始形态

使用链接地址指向要下载的文件。此时浏览器会尽可能解析对应的文件，只要是能够在浏览器窗口展示的，就都会直接显示，而不是提示下载。

```html
<a href="download/hello.atguigu">下载</a><br/>
<a href="download/tank.jpg">下载</a><br/>
<a href="download/chapter04.zip">下载</a><br/>
```

上面例子中，只有 chapter04.zip 文件是直接提示下载的，其他两个都是直接显示。



## 2、明确要求浏览器提示下载

```java
@Autowired
private ServletContext servletContext;

@RequestMapping("/download/file")
public ResponseEntity<byte[]> downloadFile() {

    // 1.获取要下载的文件的输入流对象
    // 这里指定的路径以 Web 应用根目录为基准
    InputStream inputStream = servletContext.getResourceAsStream("/images/mi.jpg");

    try {
        // 2.将要下载的文件读取到字节数组中
        // ①获取目标文件的长度
        int len = inputStream.available();

        // ②根据目标文件长度创建字节数组
        byte[] buffer = new byte[len];

        // ③将目标文件读取到字节数组中
        inputStream.read(buffer);

        // 3.封装响应消息头
        // ①创建MultiValueMap接口类型的对象，实现类是HttpHeaders
        MultiValueMap responseHeaderMap = new HttpHeaders();

        // ②存入下载文件所需要的响应消息头
        responseHeaderMap.add("Content-Disposition", "attachment; filename=mi.jpg");

        // ③创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(buffer, responseHeaderMap, HttpStatus.OK);

        // 4.返回responseEntity对象
        return responseEntity;
    } catch (IOException e) {
        e.printStackTrace();
    } finally {

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    return null;
}
```



## 3、典型应用场景举例

我们目前实现的是一个较为简单的下载，可以用在下面的一些场合：

- 零星小文件下载
- 将系统内部的数据导出为 Excel、PDF 等格式，然后以下载的方式返回给用户



[上一节](verse06.html) [回目录](index.html) [下一节](verse08.html)