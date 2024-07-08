<!--<!DOCTYPE html>-->
<%--在将html转义为jsp的时候，要添加这一行--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- 引入 jstl 标签库 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	/* 动态获取相关地址信息 */
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<%-- base 表示所有的路径连接都要在前面补充上 base 中 href 的路径前缀 --%>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<%-- 添加入口函数 --%>
<script type="application/javascript">

	$(function () {
		// 给当前整个浏览器窗口界面添加键盘按下事件，当用户按下回车键的时候，向后台提交表单
		$(window).keydown(function(event){
			// 判断键盘按下的是不是回车键，true 向后台提交表单，false 不做任何操作
			if(event.keyCode==13){
				/**
				 *2，jquery事件函数的用法：
				 *		用法 1：
				 *				选择器.click(function(){//给指定的元素添加事件
				 *					//js代码
				 *				});
				 *
				 *		用法 2：
				 *				选择器.click();//在指定的元素上模拟发生一次事件
				 **/
				$("#loginBtn").click();
			}
		});


		// 给 "登录" 按钮添加单击事件,click 传入一个函数参数，js 是弱脚本语言，表示当触发单击事件的时候，自动执行事件的函数
		$("#loginBtn").click(function () {
			 // 前台要完成的事情 1 --- 4
			 // 1.拿到当前表单中参数, $.trim() 去掉字符串中的前后空格
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());
			/**
			 * JQuery 获取指定元素的指定属性值的值
			 * 	  选择器.attr("属性名")，获取元素值非 true 或 false 类型的值，非布尔型
			 * 	  选择器.prop("属性名")，仅仅用于获取 true 或 false 类型的值，例如：checked，selected，readonly，diableed
			 **/
			var isRemPwd = $("#isRemPwd").prop("checked");

			// 2.验证表单数据的合法性
			// 验证账户是否为空
			if(loginAct==""){
				alert("请输入账户");
				// return 表示结束当前函数的执行
				return;
			}
			// 验证密码是否为空
			if(loginPwd==""){
				alert("请输入密码");
				// return 表示结束当前函数的执行
				return;
			}
			// 3.发送异步请求
			$.ajax({
				// 这里不加 "/" 是因为在前面中使用了 base 标签中有所设置，所有的 url 请求都自动补充 href 路径
				url:'settings/qx/user/login.do',
				data:{
					/**
					 * data 属性中的数据参数名的命名，要与后端接受方法中的参数名一致
					 * public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request){
					 **/
					loginAct:loginAct,
					loginPwd:loginPwd,
					isRemPwd:isRemPwd
				},
				type:'post',
				dataType:'json',
				// 4.处理后端返回来的响应数据，data 接受来自后台的响应信息
				/**
				 * public class ReturnObject {
				 *   private String code;
				 *   private String message;
				 *   private Object retData;
				 *  }
				 **/
				success:function (data) {
					if(data.code=="1"){
						// 登录成功，后台查询数据库，登录密码账户无误，跳转到业务主页面
						// 向后台发送请求的三种方式：1.地址栏 2.href 链接 3.form 表单
						// 这里不加 "/" 是因为在前面中使用了 base 标签中有所设置，所有的 url 请求都自动补充 href 路径
						window.location.href="workbench/index.do";
					}else{
						// 登录失败，将提示信息显示到指定位置
						$("#msg").html(data.message);
					}
				},
				//
				/**
				 * 在执行 ajax 请求之前会先执行本函数，该函数的返回值能够决定 ajax 是否真的向后台发送请求
				 * 		返回 ture， 则 ajax 真的向后台发送请求
				 * 		返回 false，则 ajax 不会向后台发送请求
				 *
				 * 	示例如下：初步验证表单是否合法
				 *
				 * 		beforeSend:function(){
				 *
				 *			if(loginAct==""){
				 *				alert("请输入账户");
				 *				// return 表示结束当前函数的执行
				 *				return;
				 *			}
				 *			// 验证密码是否为空
				 *			if(loginPwd==""){
				 *				alert("请输入密码");
				 *				// return 表示结束当前函数的执行
				 *				return;
				 *			}
				 *		}
				 **/
				beforeSend:function(){
					// 在当向后台提交数据的时候，在页面显示 "正在努力验证......"
					$("#msg").html("正在努力验证......");
					return true;
				}
			});

		});
	});

</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<%-- 使用 EL 表达式从 cookie 中获取到保存到 cookie 中的值 --%>
						<input class="form-control" id="loginAct" value="${cookie.loginAct.value}" type="text" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" id="loginPwd" value="${cookie.loginPwd.value}" type="password" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<%--
								JSTL 表达式判断用户名，密码选项框是否为空
									空：  "十天内免登录" 复选框不选中
									非空： "十天内免登录" 复选框选中

								empty cookie.loginAct.value	：
										empty    判断  cookie.loginAct.value 是否为空
											空：  返回 true
											非空：返回 false

										not empty 判断是否不为空
											空：  返回 false
											非空：返回 true
							--%>
							<c:if test="${not empty cookie.loginAct.value and not empty cookie.loginPwd.value}">
								<input type="checkbox" id="isRemPwd" checked="true">
							</c:if>
							<c:if test="${ empty cookie.loginAct.value or  empty cookie.loginPwd.value}">
								<input type="checkbox" id="isRemPwd" >
							</c:if>
							 十天内免登录
						</label>
						&nbsp;&nbsp;
						<span id="msg"></span>
					</div>
					<%-- 给登录按钮添加单击事件，当用户点击登录按钮的时候，向后台提交数据表单的信息 --%>
					<button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>