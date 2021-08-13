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
    (2) 修改日期控件，引入bootstrap相关的js插件资源包。注意，其中多次用到了日期填写的操作，所以需要用的是class属性，而不是id。
        对于class属性已经有值的情况下，可以打空格，在后面添加想要命名的属性值，实现属性的多重赋值操作。例如：class="AAA BBB CCC"，说明该class属性有三个属性值。
    (3) 执行市场活动添加操作，即将用户添加的数据保存到数据库中。注意：这里是从前端到后端的操作。
        对于ActivityController类中的save方法需要注意的是：用户id使用UUIDUtil工具类自动生成，创建时间就是当前的系统时间，创建人就是当前的登录用户，需要通过请求当前会话来获取用户姓名。
        其他的参数都是从前端js代码中ajax的json文件中传过来的。
    

 #### 知识点储备
#####1. jquery 的 prop()方法的使用
   定义：用来设置或返回被选元素的属性和值。
     
    当该方法用于返回属性值时，则返回第一个匹配的元素值；
    当该方法用于设置属性值时，则为匹配元素集合设置一个或多个属性值（对）。
    
   注意事项：
     
    prop() 方法应该用于检索属性值，例如 DOM 属性（如 selectedIndex, tagName, nodeName, nodeType, ownerDocument, defaultChecked, 和 defaultSelected）；
    如需检索 HTML 属性，请使用 attr() 方法代替；
    如需移除属性，请使用 removeProp() 方法。
   使用方法：
     
    返回属性的值：
    $(selector).prop(property);
    设置属性的的值：
    $(selector).prop(property, value);
    使用函数设置属性和值：
    $(selector).prop(property, function(index, currentvalue));
    设置对个属性和值：
    $(selector).prop({property:value, property:value,...});

 #### 遇到的问题
  1. workbench/activity/index.jsp中，对于模态窗口的操作，最好是改成用JS代码来操作。
    详细使用方式见workbench/activity/index.jsp中的JavaScript代码。
  2. 虽然jQuery对象没有为我们提供reset()方法，但是原生js为我们提供了reset()方法。所以需要我们将jQuery对象转换成原生的js dom 对象.

    两者的转换方式：
		 (1) jQuery --> dom
		  jQuery对象[下标]
		 (2) dom --> jQuery
		  $(dom对象)	
    详细使用方式见workbench/activity/index.jsp中的JavaScript代码。
  3. 对于所有的关系型数据库，做前端的分页操作的基础组件，都涉及两个参数：pageNo和pageSize。
      
    pageNo：页码
    pageSize：每页展现的记录数
   写一个pageList()方法：发送ajax请求到后台，从后台取得最新的列表数据，通过相应返回的数据，局部刷新信息列表。
  
    详细使用方式见workbench/activity/index.jsp中的JavaScript代码。
  4. 作为controller，需要为ajax请求提供多项信息
     
    可以有两种方式来处理：
        (1) 将多项信息打包成map，将map解析为json串
        (2) 创建一个vo。object值对象。通常用于业务层之间的数据传递，和PO一样也是仅仅包含数据而已。
    如何选择用哪种方式：
        如果需要展现的信息将来还会大量用到，就需要创建一个vo类，这样使用更方便；
        如果对于展现的信息只有在这个需求中能够使用，就用map集合
  5. 当Firefox浏览器控制台(Console)错误提示"a is not undefined"，主要排查提示错误那一行是否有变量命名错误。