
### 用户管理模块的实现 2021-07-29
1. 添加util文件夹，实现用户数据的初始化（InitDataUtil类）。向磁盘写入文件的过程。

        (1) 初始化文件夹和文件；
        (2) 创建对象输出流ObjectOutputStream oos；
        (3) 如果没有文件夹或文件，则创建，如果文件中没有数据，则添加
        (4) 写入文件ois中（oos.writeObject(数据/列表);）
        (5) 要注意异常的捕获，以及流的关闭。
2. 添加dao（Data Access Object，数据接入对象。将数据写入文件中，实现数据持久化。）文件夹，创建UserDao接口及其实现类UserDaoImpl，实现从硬盘中的读取数据，以及将数据写入硬盘文件中。
   
       (1) 用对象输入流ObjectInputStream实现对 对象输出流的文件读取；
       (2) 将读取结果集合，作为返回值返回。
       (3) 注意异常捕获，如果读取文件时出现异常，则返回空集合（new ArrayList();）。
   
3. 在service文件夹下，创建UserService服务接口以及实现类UserServiceImpl来实现用户服务，由于查询结果和dao中是一样的，
   所以直接将UserDaoImpl类中的查询结果传过来返回即可。
   注意：需要先在UserServiceImpl类中对UserDaoImpl类进行实例化。
   
4. 修改module/user/UserViewCtrl类中的手动传参方式。

        (1) 实例化UserServiceImpl对象；UserService userService = new UserServiceImpl();
        (2) 调用方法select()得到查询结果；List<User> userList = userService.select();
        (3) 将查询结果传给ObservableList<User> users = FXCollections.observableArrayList();的users（users.addAll(userList)）。

5. 实现对用户的添加add(User user)。module/user/ --> service --> dao

        (1) 在dao/UserDao接口中添加抽象方法add()，在UserDaoImpl类中实现该方法；
        (2) 在service/UserService接口中同样添加抽象方法add()，在UserServiceImpl类中同样实现该方法。可以直接调用dao层写好的方法；
        (3) 更新UserHandleViewCtrl类中的用户添加操作，实现将用户数据持久化到文件中，反馈给用户管理界面。

6. 修改用户数据update(User user)。步骤和添加基本一样，参考第5步 即可。

7. 删除用户delete(int id)。
   
8. 修改用户的状态frozen(int id)。
### 遇到的问题
1. 如果修改User类的属性值个数（增加或者删除），都会出现InvalidClassFoundation (发现无效类异常)。
    
        原因是：User类实现了Serializable接口，它的版本序列号serialVersionUID还是之前的没变，导致找不到当前的User类。
        所以，在类实现了Serializable接口后，需要手动设置序列号，让其保持不变。
        private static final long serialVersionUID = 一个long型数;

2. 对于不常修改的数据（比如：文件路径等字符串常量），一般都创建一个常量类来保存，方便再次使用。

3. 当删除用户的时候，最好是将持久化的操作放在删除展示页面的前面。因为最终的目的是要删除文件中的用户（持久化文件中的用户），而不是删除内存中的临时用户（用来展示删除成功）。
   
4. 为了防止捕获不到异常，需要在UserDaoImpl实现类中涉及到用户数据修改的所有方法（add() update() delete()）体内将异常手动抛出。throw new RuntimeException();

### 图书管理模块 2021-07-30
1. 在dao层创建BookDao接口及其实现类，完成图书管理模块的数据持久化操作。
   注意：由于这里的初始化操作，和用户的初始化操作基本一致，所以可以写一个统一的初始化方法initData();，让代码更简洁。
   
2. 图书条件查询功能的实现query(Book book)。在BookDao接口以及其实现类BookDaoImpl中实现，同时，在BookService接口服务以及其实现类BookServiceImpl中调用dao层的实现。

3. 添加图书add(Book book)。

4. 删除图书delete(int id)。

5. 修改图书信息delete(Book book)。注意：其中在给原属性替换值的过程中，可能胡出现很多需要修改的属性值，这时候就需要我们写一个反射类（BeanUtil）来串门处理这一问题。


### 知识点总结
1. 对代码结构类似或者相同的代码，尽量抽离成一个方法，实现解耦合的操作。
2. 在一次性需要对多个属性值做修改时，可以考虑用反射机制来实现该功能。
   比如：本系统中图书管理模块的图书信息修改操作中就有用到（BookDaoImpl中的BeanUtil就是一个属性反射类）

### 遇到的问题
1. 在做条件查询时，要注意的是，当同时输入两个条件进行查询的时候，如果单独列两个if判断语句来进行数据查询，会出现查询结果无效的情况。
比如：本系统在做图书管理模块时，需要对图书名称和图书ISBN编号进行结果查询，当我将两本图书的ISBN设置为相同时，无论我输入的图书名称是两本中的那一本，结果输出都是两本，明显不符合预期。 
   所以需要做改进。
   
       (1) 先对两个条件同时输入做一个查询操作；
       (2) 在分别对单一条件查询操作做判断。

### 借阅管理模块
1. 借阅查询query(Lend lend)。整体上和图书信息查询一致，所以不过多赘述。

2. 添加借阅记录，因为借阅记录是从图书借阅(图书管理模块)和借阅用户(用户管理模块)这两个模块产生的，所以不需要单独在借阅管理模块做添加按钮的操作，直接在图书管理界面做一个借阅操作，然后关联到用户管理即可实现借阅记录的创建。

       (1) 实现可借书用户的查询操作selectUserToLend()。用户借书是有限制的，比如一个用户只能借一本书，所以在用户已经借过书后，就不能再借书，所以在展示可借书用户的时候，可以适当的做一些剪枝操作。
            在bean的用户类中设置一个是否已借书的（参数）属性isLend。
       (2) 在LendDao接口中创建添加借阅记录操作以及在其实现类中实现。
       (3) 在LendService接口中创建添加借阅记录操作以及在其实现类中实现。这里不同于之前，之前的所有service层只是简单地将dao层数据传入即可。这里需要在service层写相关的图书和用户信息（哪个用户想要借哪本书），以及修改相应信息（借完后，需要修改图书状态以及用户是否已经借过书的状态isLend）的持久化操作（调用对应的dao层的update()方法实现）。
       (4) 修改展示页面的数据。需要注意的是：需要在BookLendViewCtrl类中的添加方法add()中调用借阅管理服务实现类中的add()方法实现添加成功的展示，当然，关联的图书状态和用户是否已借书的状态也要展示。同时，这两个状态数据的修改，需要刷新页面才能看见。
            刷新页面的实现：需要找到相应的展示页面属性bookTableView，实现属性的set和get方法，在BookViewCtrl类中的initLendStage()方法里将bookTableView属性传入(controller.setBookTableView(bookTableView);)。

3. 还书操作，即删除借阅记录。
   
       (1) 在dao层直接定义delete()方法，和之前两个模块的删除方法的语句块一样，
       (2) 然而在service层定义方法名为图书归还returnBook()，并且还要实现相应的界面展示修改，图书管理界面的图书状态修改为“入库”，在选择是否已借书用户的展示界面的状态改为false（isLend = false）。
       (3) 还需要计算是否逾期还书，在LendViewCtrl类的initialize方法中实现。

4. 续借管理

### 知识点总结
1. 借阅书籍管理，不同于用户管理和图书管理，借阅数据可能会很多，如果还是用之前的int整型自增的话，随着时间的增加容易整型越界，所以，需要将借阅编号采用String类型来管理。
   借阅编号最好不要让用户手动输入，容易出错，这里引入一种自动生成编号的方式：UUID。使用方式：UUID.randomUUID().toString();
   