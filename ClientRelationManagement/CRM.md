## CRM

### 系统设置模块

    用户模块：登录操作
    涉及到数据字典模块信息的查询

### 工作台（核心业务）workbench

#### 市场活动模块：activity

    点击创建按钮，打开模态窗口；
    添加操作；
    展现市场活动信息列表（结合条件查询+分页查询）；
    全选和反选操作；
    执行删除操作（可批量删除）；
    点击修改按钮，打开修改操作的模态窗口；
    执行修改操作；
    点击市场活动名称，跳转到详情页面，展示详细信息。
    展现备注信息（详细信息之后）
    备注的增删改查

#### 线索模块：clue

    创建线索（创建按钮+执行添加的模态窗口。窗口中对于下拉列表的处理在服务器缓存中的数据字典来填充）；
    点击线索名称，进入详细信息页面；
    在页面加载完毕后，展示关联的市场活动列表；
    解除关联操作；
    关联市场活动操作；
    点击转换按钮，跳转到线索转换页面；
    执行线索转换的操作（可同时添加交易）；

#### 交易模块：transaction

    点击创建按钮，跳转到交易添加页；
    点击交易名称，进入到交易详细信息页；
    在页面加载完毕后，展示交易历史列表；
    动态展现交易阶段内容及图标；
    点击阶段图标，更改交易阶段；

#### 统计图表模块

    交易阶段的统计图ECharts
