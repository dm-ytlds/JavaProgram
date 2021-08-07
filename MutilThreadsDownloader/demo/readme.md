### Main  
    主类，用于接收用户输入的下载地址，并调用相应的程序实现下载等功能。
### utils
    HttpUtils 用于连接下载地址的工具类。主要实现了向链接服务器发起连接请求并连接，以及通过下载地址获取文件名称的两个方法。
    LogUtils 日志功能实现类。
    FileUtils 获取本地文件的大小。

### core
    DownLoader 下载文件处理类。通过IO流实现文件的下载与存储，用到多线程下载文件后，需要对文件的切分、合并以及删除临时文件进行处理。
    DownLoaderInfoThread 按线程下载文件。其中将本地已下载的大小和本次累计下载的大小设置成原子类型LongAdder
    DownLoaderTask 分块后，每一块的执行过程类。注意：用到了CountDownLatch类来记录线程相应与否，没响应，就等待awit();，记得在最后需要做一个线程减1操作countDown();

### constant
    Constant 常量类。放一些不常修改的文件名称。

### learn
    学到的一些新知识测试文件夹。

### 知识点总结
1. ScheduledExecutorService。按计划处理执行业务。
2. ThreadPoolExecutor 线程池对象的使用。