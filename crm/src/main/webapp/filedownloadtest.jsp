<%--
  Description:
  User: zk
  DateTime: 2025/7/14 18:30
--%>
<%--在将html转义为jsp的时候，要添加这一行--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    /* 动态获取相关地址信息 */
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <%-- base 表示所有的路径连接都要在前面补充上 base 中 href 的路径前缀 --%>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <title>演示文件下载</title>
    <script type="text/javascript">
        $(function () {
            // 给下载按钮添加单击事件
            $("#fileDownLoadBtn").click(function () {
                // 发送文件下载的请求，所有文件下载的请求只能发送同步请求，不能发送异步请求（ajax只能接受json格式数据）
                /*
                * 从客户端向服务端发送同步请求，采用方式1，地址栏的方式向后台发送同步请求
                *   方式1，地址栏
                *   方式2，超级链接
                *   方式3，form表单
                */
                console.log("正在发送请求");
                window.location.href="workbench/activity/fileDownload.do";
                console.log("已发送请求");
            })
        });
    </script>
</head>
<body>
    <input type="button" id="fileDownLoadBtn" value="下载">
</body>
</html>
