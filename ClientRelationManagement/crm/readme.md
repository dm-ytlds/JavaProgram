### 注意事项
 #### 在web.xml文件中做一些操作关键字约定，方便管理

      add/create：跳转到添加页，或者打开添加操作的模态窗口；
      save：执行添加操作；
      edit：跳转到修改页，或者打开修改操作的模态窗口；
      update：执行修改操作；
      get：执行查询操作；

 #### 搭建业务的两种方式
     (1) 从前端到后端：
        控制层(controller) --> 服务层(service) --> 数据层(dao)。 
        这种方式的好处：先写前端，用户需要什么，后端就提供什么数据。
     (2) 从后端到前端：
        和(1)的方式反着来，先处理好用户想要什么数据，在写前端返回给用户想要的数据。

### 验证登录模块
 #### 实现可否登录成功的验证条件

    1. 前提：用户名和用户密码是否有效；
    2. 验证登录时间是否失效； expireTime.compareTo(当前时间)是否大于0来判断是否失效 
    3. 验证账号状态是否锁定； 0 锁定 | 1 激活
    4. 验证Ip地址是否访问受限。 ipPools.contains(ip)? 未受限 : 受限;

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
    (1) 实现窗口中所有者下拉框的获取方式为后台数据库中自动获取，且标注下拉框的默认用户是当前登录用户(通过当前会话获取(User)req.getSession().getAttribute("user").getName();)。
        a.首先创建市场活动控制类crm/workbench/web/controller/ActivityController；
        b.在webapp/WEB-INF/web.xml文件中创建<servlet>和<servlet-mapping>构建市场活动控制器；
        c.通过<servlet-mapping>中的映射地址<url-pattern>，在ActivityController类中实现用户信息获取；
        d.实现用户信息的获取方式为service调用dao层实现的用户数据获取类，在userDao层的映射文件UserDao.xml中创建sql语句实现数据库查询的操作；
        e.将得到的数据返回给前端，前端通过编写webapp/workbench/activity/index.jsp文件中的js代码实现信息的获取。
    (2) 实现日期控件，引入bootstrap相关的js插件资源包。注意，其中多次用到了日期填写的操作，所以需要用的是class属性，而不是id。
        对于class属性已经有值的情况下，可以打空格，在后面添加想要命名的属性值，实现属性的多重赋值操作。例如：class="AAA BBB CCC"，说明该class属性有三个属性值。
    (3) 执行市场活动添加操作，即将用户添加的数据保存到数据库中。注意：这里是从前端到后端的操作。
        对于ActivityController类中的save方法需要注意的是：用户id使用创建好的UUIDUtil工具类自动生成，创建时间就是当前的系统时间，创建人就是当前的登录用户，需要通过请求当前会话来获取用户姓名。
        其他的参数都是从前端js代码中ajax的json文件data中传过来的。
    (4) 将创建的市场活动信息展示在市场活动列表栏中。通过创建的pageList()方法实现。这里展示需要从后台获取所有的市场活动信息workbench/activity/pageList.do。

 #### 删除市场活动信息
    (1) 对复选框做一个定位，看用户是想要删除多少条信息，通过用户想要删除的活动编号activityId，定位到后台数据库做delete操作；
    (2) 注意：在删除操作时，会关联到市场活动标注表activity_remark，其中对备注信息做了相应的记录，
        在删除市场活动的时候，也需要删除相应的activity_remark表中的记录；
    (3) 实现删除操作的步骤(ActivityServiceImpl)：
        先查出欲删除的市场活动关联的所有remark记录：activityRemarkDao.getCountByIds(ids);，
        在通过查询到的remark记录删除：activityRemarkDao.deleteByIds(ids);
        最后删除市场活动表中的记录：activityDao.delete(ids);

 #### 修改市场活动信息模态窗口
    (1) 想要修改信息，就得先从后端将需要修改的市场活动信息展示在前端的模态窗口中，涉及到一个信息获取的过程workbench/activity/getUserListAndActivity.do；
    (2) 将修改后的信息，重新保存到对应的市场活动中，通过activityId定位统一市场活动，设计到更新实现活动信息的操作workbench/activity/update.do；
    (3) 同时，还需要在前端展示中，刷新对应的市场活动信息的展示pageList($("#activityPage").bs_pagination('getOption', 'currentPage'), $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
        注意，修改后，展现当前页面信息，维持每页展现的记录数。
 #### 市场活动的详情模态窗口
    (1) 展示市场活动相关的信息workbench/activity/detail.do，以及和该市场活动相关的remark信息（showRemarkList()方法实现）；
    (2) 删除remark信息，deleteRemark()方法动态的实现，在表单元素中添加onClick(remarkId)方法来实现动态删除操作；
    (3) 添加备注信息，只需要关注添加的备注内容是什么，其他的都是后台获取。
        注意：这里需要手动给editFlag赋值为0，以及通过UUIDUtil工具类生成一个备注id；
    (4) 修改备注信息。打开修改备注模态窗口之前，将之前的备注内容（从前端页面直接获取）获取到，以及修改后提交给后端数据库保存updateRemark()


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


### 线索模块、客户模块、联系人模块以及交易模块
    线索，即潜在客户。
    线索模块包括的数据库表有：线索表tbl_clue、线索备注表tbl_clue_remark、线索市场活动关系表tbl_clue_activity_relation
    客户模块包括的数据库表有：客户表tbl_customer、客户备注表tbl_customer_remark
    联系人模块包括的数据库表有：联系人表tbl_contacts、联系人备注表tbl_contacts_remark、联系人市场活动关系表tbl_contacts_activity_relation
    交易模块包括的数据库表有：交易表tbl_tran、交易备注表tbl_tran_remark、交易历史表tbl_tran_history
    数据字典包括的数据库表有：字典类型表tbl_dic_type、字典值表tbl_dic_value

#### 数据字典的实现
    1. 创建监听器（web/listener/SysInitListener）完成上下文作用域对象的监听事件，从而将获取到的数据保存到服务器缓存中。
    2. 在setting软件包下创建相应的数据字典获取文件：DicType和DicValue相关的：
        domain文件（DicType, DicValue）、dao文件（DicTypeDao, DicValueDao）、service文件（DicService）
#### 创建线索
    1. 大体上来说，还是和之前一样，先铺值，在展示窗口。这里的先铺值，主要说的是几个下拉框的值。
       对于所有者下拉框来说，需要从用户表中查出所有的用户列表，对于这种容易变动的数据（用户信息数据），主要还是采用实时的从后端获取的方式；
       而对于像称呼appellation、职称等信息基本上没有变动的信息，主要还是用数据字典来实现：即将其放在服务器缓存中，和服务器共存亡（服务器不关，信息就不一直在缓存中，且不会改变）。
    2. 打开模态窗口后，将填写好的数据post到数据库表中。
#### 修改线索、删除线索
    和之前的市场活动模块操作一样，略过。
#### 跳转线索详情页面
    1. 通过重定向页面，实现线索信息的展示detail.do。
    2. 通过后台获取，展示当前线索已关联的市场活动信息showActivityList()。
    3. 实现解除关联的操作unBund()。通过当前的线索id，从而删除线索和市场活动关联的关系表tbl_clue_activity_relation来实现。
    4. 关联市场活动。注意：在写SQL 语句的时候，由于service实现层是将内容封装到了ClueActivityRelation对象里，
       所以SQL语句中的values语句中应该和实例对象中的属性同名，即#{所提暗属性和ClueActivityRelation对象中的属性名对应相同}。
    5. 实现线索详情页面的 转换 操作。也就是说，如果该线索（潜在客户）成为了我们的客户，需要对该线索进行一次转换，即在客户表、联系人表以及交易表中都需要添加相关的信息。
       <font color="#FF0000">*****这属于该项目的重中之重。*****</font>
        对于数据如何转换的考虑1：我们采用的是传统请求方式来给数据库传递数据，因为该网页在转换后，没有必要ajax进行局部刷新操作，转换结束，直接关闭该网页即可。
        对于数据如何转换的考虑2：如果没有交易信息产生，直接在链接地址后面挂参数就可以了，因为此时只需要将线索id传给后台即可；但是如果有交易信息产生，就需要考虑到可能会传递大量的参数问题，所以，我们采用提交表单的形式来提交数据。





 #### 知识点解释
    1. 解析CRM中的多对多关系
        在实际开发中，基本上半数以上的需求，都是一对多，或者多对一的关系，存在一种特殊的关系：一对一（如：人和身份证的关系）。
        对于上面三种关系，采用的方式：在其中的一张表中建立外键（多）来维持表与表之间的关系。
     多对多的关系，不应该在任何一张表中建立外键关联，我们应该建立起第三张表：关联关系表，由此表来维护两表之间的关系。
    2. cache（缓存机制）解决数据字典存储问题：避免每次都从数据库取数据，省去了连接数据库的操作，节省时间，节省内存
        但是，不是所有的数据都适合放在缓存里面，一般来说，只是建议把那些长期不变的数据，放在缓存里面。
        (1) 缓存：针对内存中的数据而言。
         该项目要做的是一种服务器缓存机制，相当于要将数据保存到服务器的内存中，如果服务器开启，就一直能够从该缓存中获取数据。
        (2) application（全局作用域，也叫做上下文作用域）：在服务器启动阶段，将数据保存在服务器的缓存中，服务器启动多久，数据就存在多久。
         将数据保存到服务器缓存中的方法：application.setAttribute();
         将数据从服务器换粗哪种取出的方法：application.getAttribute();
    3. 数据字典是什么
        指的是在应用程序中，做表单元素选择内容用的相关的数据。比如：下拉框、单选框、复选框。
       数据字典普遍被用在下拉框中。
    4. 如何做到让application在服务器启动阶段，就将数据字典保存起来？
        监听器的使用：监听器作为Web服务三大组件之一，只能监听域对象（request域、session域、application域的创建与销毁）以及域对象属性的变化（添加、删除、修改）
        本项目监听器的作用：监听上下文域(application域)对象的创建
    5. 对于数据字典，需要分类来保存。本项目中按照typecode来分类

    6. JSP中的内置对象，在JSP页面未声明就可以直接使用。在<%%>里面直接使用即可。在<%=得到的参数名%>里面直接调用得到的参数即可。有9大内置对象：
        JSP内置对象(9个内置对象):
        (1) PageContext javax.servlet.jsp.PageContext JSP的页面容器
        (2) request javax.servlet.http.HttpServletrequest 获取用户的请求信息
        (3) response javax.servlet.http.HttpServletResponse 服务器向客户端的回应信息
        (4) session javax.servlet.http.HttpSession 用来保存每一个用户的信息
        (5) application javax.servlet.ServletContext 表示所有用户的共享信息
        (6) config javax.servlet.ServletConfig 服务器配置信息，可以取得初始化参数
        (7) out javax.servlet.jsp.jspWriter 页面输出
        (8) page java.lang.object)
        (9) exception java.lang.Throwable
       四种属性范围:
        page(pageContext):只在一个页面中保存属性。跳转之后无效。
        request:只在一次请求中有效，服务器跳转之后有效。客户端跳转之后无效
        session:在一次会话中有效。服务器跳转、客户端跳转都有效。网页关闭重新打开无效
        application:在整个服务器上保存，所有用户都可使用。重启服务器后无效
    7. el表达式为我们提供了N多个隐含对象
		只有xxxScope系列的隐含对象可以省略掉
		其他所有的隐含对象都不能省略（如果省略掉，会变成从域对象中取值）

