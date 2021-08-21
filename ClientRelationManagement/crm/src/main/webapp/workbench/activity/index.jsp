<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

	<script type="text/javascript">

	$(function(){

		// 创建按钮绑定事件，实现打开添加操作的模态窗口
		$("#addBtn").click(function () {
			// 修改时间控件的操作。前提的引入bootstrap支持时间修改相关的js插件包
			$(".time").datetimepicker({
				minView : "month",
				language : 'zh-CN',
				format : 'yyyy-mm-dd',
				todayBtn : true,
				autoclose : true,
				pickerPosition : "bottom-left"
			});


			/*
				操作模态窗口的方式：
					需要操作的模态窗口的jQuery对象，调用modal方法，为该方法床底参数 show:打开模态窗口， hide:关闭模态窗口
			 */

			// 通过后台，取数据，获得市场活动中创建模态窗口的用户信息列表，组成下拉列表框
			$.ajax({
				url : "workbench/activity/getUserList.do",   // 访问地址
				type : "get",  // 通过url的请求方式
				dataType : "json",   // 数据文件的类型
				success : function (data) {

                    /**
                     * data的形式：
                     *  [{user1}, {user2}, {user3}, ...]
                     *  {user1} --完全形式为--> {"id":?, "name":?, ...}
                     * @type {string}
                     */
                    // 字符串
                    var html = "<option></option>";

                    // 遍历出每一个u对象
                    $.each(data, function (i, u) {
                        // 使用用户的id展示用户的姓名
                        html += "<option value='" + u.id + "'>" + u.name + "</option>";
                    })

                    // 修改<select class="form-control" id="create-owner">  修改处  </select>
                    $("#create-owner").html(html);

					/*
						将当前登录设置为下拉框的默认选项.jQuery提供的简单方式如下：
						(1) 取得当前登录的用户id赋值给一个变量  var userId = "${user.id}";
						(2) 在js代码中使用el表达式，el表达式嵌套在字符串中  $("#create-owner").val(userId);
					 */
					var userId = "${user.id}";
					$("#create-owner").val(userId);

					// 打开模态窗口，放在下拉框实现之后
					$("#createActivityModal").modal("show");
				}
			})

		})


		// 为创建市场活动的模态窗口保存按钮，绑定事件操作
		$("#saveBtn").click(function () {
			$.ajax({
				url : "workbench/activity/save.do",
				type : "post",
				dataType : "json",
				data : {
					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val())
				},
				success : function (data) {
					// 如果数据添加成功，需要的操作
					if (data.success) {
						pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						// 刷新市场活动信息列表（局部刷新）
						var flush = $("#createActivityModal");
						// 清空模态窗口中的文本内容
						/*
							虽然jQuery对象没有为我们提供reset()方法，但是原生js为我们提供了reset()方法。
							所以需要我们将jQuery对象转换成原生的js dom 对象.
							两者的转换方式：
							 jQuery --> dom
								jQuery对象[下标]
							 dom --> jQuery
							 	$(dom对象)
						 */
						$("#activityAddForm")[0].reset();

						// 关闭添加操作的模态窗口
						flush.modal("hide");
					} else
						alert("添加市场活动失败！");
				}
			})
		})

		// 页面加载完毕后，触发分页操作的方法
		// 默认展开列表的第一页，每页展示两条记录
		/*
		* $("#activityPage").bs_pagination('getOption', 'currentPage');
		* 	操作后停留在当前页面
		* $("#activityPage").bs_pagination('getOption', 'rowsPerPage');
		*	操作后维持已经设置好的每页展现的记录数
		*
		* */
		// 回到第1页,维持每页展现的记录数
		pageList(1, 2);
		// 为查询按钮绑定事件，触发pageList()方法
		$("#searchBtn").click(function () {
			/*
			* 点击查询按钮的时候，应该将搜索框中的信息保存到隐藏域中
			*
			* */

			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			// 回到第1页,维持每页展现的记录数
			pageList(1, 2);
		})

		//	通过selectAll全选或者全不选select
		// 实现全选的复选框绑定事件
		$("#selectAll").click(function () {
			$("input[name=select]").prop("checked", this.checked);
		})

		// 动态生成的元素，不能够以普通绑定事件的形式来进行操作
		/*
		* $("input[name=select]").click(function () {
			alert("111")
		})
		*
		* 动态生成的元素，需要以on方法的形式来进行操作
		* 语法：
		* 	$(需要绑定元素的有效外层元素).on(绑定事件的方式, 需要绑定的元素的jquery对象, 回调函数)
		* */
		$("#activityBody").on("click",$("input[name=select]"),function (){
			// 如果一页上面的单项选择达到全选的要求，就把全选复选框也打上勾
			$("#selectAll").prop("checked",$("input[name=select]").length==$("input[name=select]:checked").length);
		})
		/*
		* 删除按钮绑定事件
		* */
		$("#deleteBtn").click(function () {
			// 找出复选框中所有打√的复选框的jquery对象
			var $select = $("input[name=select]:checked");
			if ($select.length == 0) {
				alert("请选择要删除的活动项");
			} else {
				if (confirm("确定要删除所选记录吗？")) {
					// 传递的url形式：workbench/activity/delete.do?id=xxx&id=xxx&...
					// 拼接需要传递的参数
					var params = "";
					// 将$select的每一个dom对象便利出来，取其value值，相当于取得了需要删除记录的id值
					for (var i = 0; i < $select.length; i++) {
						params += "id=" + $($select[i]).val();
						// 如果不是最后一个元素，需要在每一个id的后面加一个“$”
						// 注意：这个问题在实际开发中很重要！！！
						if (i < $select.length - 1) {
							params += "&";
						}
					}
					// 测试
					// alert(params)
					// 使用ajax请求，实现在不改变url的情况下，局部刷新改动的页面
					$.ajax({
						url : "workbench/activity/delete.do",
						data : params,
						type : "post",
						dataType : "json",
						success : function (data) {
							if (data.success) {
								// 回到第1页维持每页展现的记录数
								pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							} else {
								alert("删除记录失败");
							}
						}
					})
				}
			}
		})

		/*
		* 修改市场活动信息的步骤：
		* 	首先需要从数据库中找出想要修改的市场活动信息；
		* 	然后在做修改操作；
		* 	修改结束，返回给数据库。
		* */
		// 为修改按钮绑定修改事件，打开修改操作的模态窗口
		$("#editBtn").click(function () {
			// 先选择想要修改哪一条记录
			// 为了区分jquery对象和原生dom对象，在对象名前加$
			var $select = $("input[name=select]:checked");
			if ($select.length == 0) {
				alert("请选择想要修改的活动项");
			} else if ($select.length > 1) {
				alert("一次只能修改一个活动项，请选择要修改的某一条活动项");
			} else {
				// 获取需要修改的那条记录
				var id = $select.val();
				$.ajax({
					url : "workbench/activity/getUserListAndActivity.do",
					data : {"id" : id},
					type : "get",
					dataType : "json",
					success : function (data) {
						/*
						* data组成：
						* 	用户列表
						* 	市场活动对象
						* data形式：
						* 	{"userList" : [{user1}, {user2}, ...], "activity" : {市场活动}}
						* */
						var html = "<option></option>";
						$.each(data.userList, function (i, n) {
							html += "<option value='" + n.id + "'>" + n.name + "</option>";
						})
						$("#edit-owner").html(html);
						// 处理单条activity
						$("#edit-id").val(data.activity.id);
						$("#edit-owner").val(data.activity.owner);
						$("#edit-name").val(data.activity.name);
						$("#edit-startDate").val(data.activity.startDate);
						$("#edit-endDate").val(data.activity.endDate);
						$("#edit-cost").val(data.activity.cost);
						$("#edit-description").val(data.activity.description);

						// 填好值之后，打开修改操作的模态窗口
						$("#editActivityModal").modal("show");

					}
				})
			}
		})

		// 为修改的模态窗口中的更新按钮绑定事件，执行市场活动的修改操作
		/*
		* 在实际项目开发中，一定是按照先做添加，在做修改的顺序，
		* 为了节省开发时间，一般更新操作都是copy添加操作的代码
		* */
		$("#updateBtn").click(function () {
			$.ajax({
				url : "workbench/activity/update.do",
				data : {
					"id" : $.trim($("#edit-id").val()),
					"owner" : $.trim($("#edit-owner").val()),
					"name" : $.trim($("#edit-name").val()),
					"startDate" : $.trim($("#edit-startDate").val()),
					"endDate" : $.trim($("#edit-endDate").val()),
					"cost" : $.trim($("#edit-cost").val()),
					"description" : $.trim($("#edit-description").val())
				},
				type : "post",
				dataType : "json",
				success : function (data) {
					if (data.success) {
						// 先局部刷新市场活动列表
						// 修改后，展现当前页面信息，维持每页展现的记录数
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage'), $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						// 关闭修改操作的模态窗口
						$("#editActivityModal").modal("hide");

					} else {
						alert("更新市场活动信息失败");
					}
				}
			})
		})

	});
	/*
		pageList()方法的作用：发出ajax请求到后台，从后台取出最新的市场活动信息列表数据，
			通过响应返回的数据，局部刷新市场活动的信息列表。
		什么时候需要用到pageList()方法：
			(1) 点击左侧菜单栏的"市场活动"超链接；
			(2) 添加，修改，删除后，需要刷新市场活动列表
			(3) 点击查询按钮
			(4) 点击分页组件时
	 */
	function pageList(pageNo,pageSize) {

		// 每次刷新页面的时候，取消全选框
		$("#selectAll").prop("checked", false);
		// 查询前，将隐藏域中的信息取出来，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({

			url : "workbench/activity/pageList.do",
			data : {

				"pageNo" : pageNo,
				"pageSize" : pageSize,
				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val())

			},
			type : "get",
			dataType : "json",
			success : function (data) {
				/*
					需要的数据形式：
					(1) 市场活动信息列表
						[{}, {}, {}, ...]
					(2) 分页插件需要：查询出来的总记录数
						{"total" : 数字}
					拼接得出：
						{"total" : 数字, "dataList" : [{}, {}, {}, ...]}
				 */
				var html = "";
				// 每一个n代表一个市场活动对象
				$.each(data.dataList, function(i,n) {

					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="select" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';

				})
				// 赋值
				$("#activityBody").html(html);

				// 计算总页数
				// var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
				var totalPages = Math.ceil(data.total / pageSize);
				// 进行分页操作
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数时在，点击分页组件的时候触发的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}
		})
	}
	
</script>
</head>
<body>

	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>


	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<%--执行修改操作的id值--%>
						<input type="hidden" id="edit-id"/>

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">



								</select>
							</div>
							<label for="edit-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<!--

									关于文本域textarea：
										（1）一定是要以标签对的形式来呈现,正常状态下标签对要紧紧的挨着
										（2）textarea虽然是以标签对的形式来呈现的，但是它也是属于表单元素范畴
											我们所有的对于textarea的取值和赋值操作，应该统一使用val()方法（而不是html()方法）

								-->
								<textarea class="form-control" rows="3" id="edit-description">123</textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">

					<form id="activityAddForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">



								</select>
							</div>
							<label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-name">
							</div>
						</div>

						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
						<div class="form-group">

							<label for="create-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-cost">
							</div>
						</div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<!--

						data-dismiss="modal"
							表示关闭模态窗口

					-->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	

	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表123</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--

						点击创建按钮，观察两个属性和属性值

						data-toggle="modal"：
							表示触发该按钮，将要打开一个模态窗口

						data-target="#createActivityModal"：
							表示要打开哪个模态窗口，通过#id的形式找到该窗口


						现在我们是以属性和属性值的方式写在了button元素中，用来打开模态窗口
						但是这样做是有问题的：
							问题在于没有办法对按钮的功能进行扩充

						所以未来的实际项目开发，对于触发模态窗口的操作，一定不要写死在元素当中，
						应该由我们自己写js代码来操作



					-->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>