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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;

	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});

		// 页面加载完毕后，展现该市场活动关联的备注信息表
		showRemarkList();

		// 实现备注信息后面图标的动态效果
		$("#remarkBody").on("mouseover", ".remarkDiv", function () {
			$(this).children("div").children("div").show();
		})
		$("#remarkBody").on("mouseout", ".remarkDiv", function () {
			$(this).children("div").children("div").hide();
		})

		// 为保存按钮提供绑定事件,执行备注信息的添加操作
		$("#saveRemarkBtn").click(function () {
			$.ajax({
				url : "workbench/activity/saveRemark.do",
				data : {
					"noteContent" : $.trim($("#remark").val()),
					"activityId" : "${activity.id}"
				},
				type : "post",
				dataType : "json",
				success : function (data) {

					/*
					* data的格式：
					* 	{"success":true/false, "ar":{备注}}
					*
					* */
                    // 先清空备注信息
                    $("#remark").val("");
					if (data.success) {
						// 添加成功，在备注信息列表中新增一条备注信息
						var html = "";

						html += '<div id="'+ data.ar.id +'" class="remarkDiv" style="height: 60px;">';
						html += '<img title="'+ data.ar.createBy +'" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
						html += '<div style="position: relative; top: -40px; left: 40px;" >';
						html += '<h5 id="e'+ data.ar.id +'">' + data.ar.noteContent + '</h5>';
						html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;" id="s'+ data.ar.id +'">' + data.ar.createTime + '由' + data.ar.createBy + '</small>';
						html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
						html += '<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+ data.ar.id +'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>';
						html += '&nbsp;&nbsp;&nbsp;&nbsp;';
						html += '<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+ data.ar.id +'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
						html += '</div>';
						html += '</div>';
						html += '</div>';
                        // 从下往上的传值方式
                        $("#remarkDiv").before(html);
					} else  {
						alert("添加备注失败");
					}
				}
			})
		})
        /*
        * 修改模态窗口中备注信息的相关操作
        * updateRemark();
        * */
        $("#updateRemarkBtn").click(function () {
            // 将获取备注id的操作写在ajax请求的外面，方便回调函数再次使用该值
            var remarkId = $("#remarkId").val();
            $.ajax({
                url : "workbench/activity/updateRemark.do",
                data : {
                    "remarkId" : remarkId,
                    "noteContent" : $.trim($("#noteContent").val())
                },
                type : "post",
                dataType : "json",
                success : function (data) {
                    /*
                        data的结构：
                            {"success":true/false, "ar":{备注}}
                     */
                    if (data.success) {
                        // 修改备注成功
                        // 需要更新：noteContent, editTime, editBy
                        $("#e" + remarkId).html(data.ar.noteContent);
                        $("#s" + remarkId).html(data.ar.editTime + " 由 " + data.ar.editBy);
                        // 更新内容后，关闭模态窗口
                        $("#editRemarkModal").modal("hide");
                    } else
                        alert("修改备注失败");
                }
            })
        })

        /*
        * 在市场活动详情的模态窗口中，
        * 实现修改当前市场活动的信息
        * */
        // 为编辑市场活动按钮绑定事件，打开市场活动信息的模态窗口
        $("#editActivityBtn").click(function () {
            /*
            * 通过ajax请求获取后台现有的市场活动信息，展示在市场活动模态窗口中
            * 首先对所有者下拉框中填值
            * */

            $.ajax({
                url : "workbench/activity/getUserListAndActivity.do",
                data : {
                    "id" : "${activity.id}"
                },
                type : "get",
                dataType : "json",
                success : function (data) {
                    /*
                    * data的结构：
                    *   {"userList":[{user}, ...], "activity": {市场活动信息}}
                    * */
                    // 拼接下拉框
                    /*
                      <option>zhangsan</option>
                      <option>lisi</option>
                      <option>wangwu</option>
                    */
                    var html = "<option></option>";
                    $.each(data.userList, function (i, n) {
                        html += "<option value='" + n.id + "'>" + n.name + "</option>";
                    })
                    // 将从后台获取的用户名列表传给所有者下拉框
                    $("#edit-owner").html(html);
                    // 处理从后台传来的单条市场活动信息，展示给前台
                    $("#edit-id").val(data.activity.id);
                    $("#edit-owner").val(data.activity.owner);
                    $("#edit-name").val(data.activity.name);
                    $("#edit-startDate").val(data.activity.startDate);
                    $("#edit-endDate").val(data.activity.endDate);
                    $("#edit-cost").val(data.activity.cost);
                    $("#edit-description").val(data.activity.description);

                    // 填好值后，才展示修改市场活动的模态窗口
                    $("#editActivityModal").modal("show");
                }
            })

        })

        // 展示完模态窗口后，为更新按钮绑定更新事件，完成修改市场活动操作
        // 向后台传值的操作，ajax请求实现
        $("#updateBtn").click(function () {
            $.ajax({
                url : "workbench/activity/update.do",
                data : {
                    "id" : $("#edit-id").val(),
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
                        pageList($("#activityPage").bs_pagination('getOption', 'currentPage'), $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                        // 将修改模态窗口中的值展示在市场活动详细信息页面
                        $("#owner").val($.trim($("#edit-owner").val())),
                        $("#name").val($.trim($("#edit-name").val())),
                            $("#startDate").val($.trim($("#edit-startDate").val())),
                            $("#endDate").val($.trim($("#edit-endDate").val())),
                            $("#cost").val($.trim($("#edit-cost").val())),
                            $("#createTime").val(activity.creatTime),

                        // 关闭模态窗口
                        $("#editActivityModal").modal("hide");
                        // window.parent.location.reload("workbench/activity/detail.jsp");
                    } else
                        alert("更新市场活动信息失败");
                }
            })
        })

	});

	/*
	* 通过市场活动id，展现该市场活动关联的备注信息表
	* */
	function showRemarkList() {
		$.ajax({
			url : "workbench/activity/getRemarkListByAid.do",
			data : {
				"activityId" : "${activity.id}"
			},
			type : "get",
			dataType : "json",
			success : function (data) {
				/*
				* data的结构：[{备注1}, {备注2}, {备注3}, ...]
				* */
				var html = "";
				$.each(data, function (i, n) {
					html += '<div id="'+ n.id +'" class="remarkDiv" style="height: 60px;">';
					html += '<img title="'+ (n.editFlag==0?n.createBy : n.editBy) +'" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
					html += '<div style="position: relative; top: -40px; left: 40px;" >';
					html += '<h5 id="e' + n.id +'">' + n.noteContent + '</h5>';
					html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;" id="s'+ n.id +'">' + (n.editFlag==0?n.createTime : n.editTime) + '由' + (n.editFlag==0?n.createBy : n.editBy) + '</small>';
					html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
					html += '<a class="myHref" href="javascript:void(0);" onclick="editRemark(\'' + n.id + '\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>';
					html += '&nbsp;&nbsp;&nbsp;&nbsp;';
					html += '<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\'' + n.id + '\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
					html += '</div>';
					html += '</div>';
					html += '</div>';
				})
				// 从下往上的传值方式
				$("#remarkDiv").before(html);
			}
		})
	}



	/*
	* 修改当前市场活动信息
	* */

	/*
	* 删除备注信息
	* */
	deleteRemark = function (remarkId) {
		// 后台取数据，ajax
		$.ajax({
			url : "workbench/activity/deleteRemark.do",
			data: {
				"remarkId" : remarkId
			},
			type: "post",
			dataType: "json",
			success: function (data) {
				if (data.success) {
					// 删除成功，刷新备注信息列表
					// 这种方法刷新出问题：每一次
					// showRemarkList();

					$("#" + remarkId).remove();

				} else {
					alert("删除备注失败");
				}
			}
		})
	}

    /**
     * 修改备注信息操作
     */
    // 首先打开模态窗口的相关操作：先对模态窗口中的隐藏域id值赋值；在把已有的备注信息赋给备注框；最后展示修改备注的模态窗口
    editRemark = function (remarkId) {
        // // test
        // alert("123");
        // 将模态窗口中隐藏域中的id进行赋值，材质到是修改的哪一条备注
        $("#remarkId").val(remarkId);
        /**
         * 从前端直接获取当前备注的信息是什么
         */
        var noteContent = $("#e" + remarkId).html();
        // 将前端备注信息noteContent赋给当前的修改备注模态窗口中文本框
        $("#noteContent").val(noteContent);
        // 展示备注修改模态窗口
        $("#editRemarkModal").modal("show");
    }

</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 修改市场活动的模态窗口 -->
    <div class="modal fade" id="editActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改市场活动</h4>
                </div>
                <div class="modal-body">

                    <form class="form-horizontal" role="form">
                        <%--执行修改操作的id值--%>
                        <input type="hidden" id="edit-id"/>
                        <div class="form-group">
                            <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-owner">
                                    <%--<option>zhangsan</option>
                                    <option>lisi</option>
                                    <option>wangwu</option>--%>
                                </select>
                            </div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-startDate">
                            </div>
                            <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-endDate">
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
                                <textarea class="form-control" rows="3" id="edit-description"></textarea>
                            </div>
                        </div>

                    </form>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal" id="updateBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;" id="showDetail">
		<div class="page-header">
			<h3>市场活动-${activity.name} <small>${activity.startDate} ~ ${activity.endDate}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal" id="editActivityBtn"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div id="detailBody" style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;" id="owner"><b></b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;" id="name"><b></b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;" id="startDate"><b></b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;" id="endDate"><b></b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;" id="cost"><b></b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;" id="createBy"><b>&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;" id="createTime"></small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;" id="editBy"><b>&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;" id="editTime"></small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;" id="description">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>

				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkBody" style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<%--<!-- 备注1 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<!-- 备注2 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>