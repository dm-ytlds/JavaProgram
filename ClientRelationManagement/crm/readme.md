### 注意事项
 #### 在web.xml文件中做一些操作关键字约定，方便管理

      add/create：跳转到添加页，或者打开添加操作的模态窗口；
      save：执行添加操作；
      edit：跳转到修改页，或者打开修改操作的模态窗口；
      update：执行修改操作；
      get：执行查询操作；

 #### 搭建业务的两种方式
     (1) 从前端到后端：
        控制层(controller) --> 服务层(service) --> 数据层(dao)。 这种方式的好处：先写前端，用户需要什么，后端就提供给什么数据。
     (2) 从后端到前端：
        和(1)的方式反着来，先处理好用户想要什么数据，在写前端返回给用户想要的数据。

### 验证登录模块
 #### 实现可否登录成功的验证条件

    1. 前提：用户名和用户密码是否有效；
    2. 验证登录时间是否失效； expireTime.compareTo(当前时间)是否大于0来判断是否失效 
    3. 验证账号状态是否锁定；
    4. 验证Ip地址是否访问受限。

 #### 遇到的问题

    保持MySQL连接器的版本一致。
        pom.xml文件中mysql-connecter-java的版本最好更新到和本机安装的MySQL应用的版本一致。
    可能会出现的错误：“Authentication plugin 'caching_sha2_password' cannot be loaded: ”
        解决方案：首先登录mysql在进行下面的操作。
        mysql> use mysql;
        mysql> select user, plugin from user;
        可以看到root用户的加密方式为caching_sha2_password。
        这样的话有两种办法可以解决问题：
            (1) 升级客户端支持caching_sha2_password方式，没有采用。
            我使用第二种方法：
            (2) 修改密码加密方式，改成mysql_native_password
            ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
            然后修改密码：
            SET PASSWORD FOR 'root'@'localhost' = PASSWORD('newpassword');
            或者执行命令flush privileges使权限配置项立即生效。

### 市场活动模块

 #### 创建市场活动模态窗口
    (1) 修改窗口中所有者下拉框的获取方式为后台数据库中自动获取，且标注下拉框的默认用户是当前登录用户。
        a.首先创建市场活动控制类crm/workbench/web/controller/ActivityController；
        b.在webapp/WEB-INF/web.xml文件中创建<servlet>和<servlet-mapping>构建市场活动控制器；
        c.通过<servlet-mapping>中的映射地址<url-pattern>，在ActivityController类中实现用户信息获取；
        d.实现用户信息的获取方式为service调用dao层实现的用户数据获取类，在userDao层的映射文件UserDao.xml中创建sql语句实现数据库查询的操作；
        e.将得到的数据返回给前端，前段通过编写webapp/workbench/activity/index.jsp文件中的js代码实现信息的获取。
    (2) 



 #### 遇到的问题
    1. workbench/activity/index.jsp中，对于模态窗口的操作，最好是改成用JS代码来操作。
    详细使用方式见workbench/activity/index.jsp中的JavaScript代码。
    	
           

