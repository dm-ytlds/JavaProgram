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

	<script>
		$(function () {

			if (window.top != window) {
				window.top.location = window.location;
			}
			// 页面加载完毕后，将文本框中的内容清空
			// .val() 如果方法里面没有值，则表示取值操作；有值才是存值操作
			$("#loginAct").val("");
			// 页面加载完毕后，让用户的文本框自动获取焦点
			$("#loginAct").focus();
			// 为登录按钮绑定事件，执行登录操作
			$("#submitBtn").click(function () {
				login();
			});
			// 为当前窗口页绑定键盘事件
			$(window).keydown(function (event) {
				if (event.keyCode == 13) {
					login();
				}
			})
		})

		// 普通的function方法 一般放在$(function(){})的外面
		// 登录提示信息方法

		login = function () {
			// alert("登录操作");
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());

			if (loginAct == "" || loginPwd == "") {
				$("#msg").html("账号密码不能为空");
				return false;
			}

			// 后台验证登录相关的操作
			$.ajax({
				url : "settings/user/login.do",
				data : {
					"loginAct" : loginAct,
					"loginPwd" : loginPwd
				},
				type : "post",
				dataType : "json",
				success : function (data) {

					if (data.success) {
						// 跳转到工作台的初始页
						window.location.href = "workbench/index.jsp";

					} else {
						// 如果登录失败
						$("#msg").html(data.msg);
					}
				}
			})

		}
	</script>

</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2021&nbsp;Demi</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名", id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码", id="loginPwd">
					</div>
					<div class="checkbox" style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: #b2dba1"></span>
						
					</div>
					<%--
						按钮设置在form表单中，默认就是提交按钮。
						如果不需要自动提交，需要将按钮的类型(type)改为button。
						这样的话，按钮触发的行为就是手动编写的js代码
					--%>
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>