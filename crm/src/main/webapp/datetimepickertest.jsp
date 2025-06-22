<!--<!DOCTYPE html>-->
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
    <%-- 1.引入开发包  JQuery-->bootstrap-->datetimepicker --%>
    <%-- JQuery --%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <%-- bootstrap,日历插件是基于bootstrap框架的插件，因此在使用日历插件之间必须引入bootstrap框架内 --%>
    <link rel="stylesheet" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <%--datetimepicker,日历插件--%>
    <%--rel=Stylesheet -- 定义一个外部加载的样式表,这个属性必须有--%>
    <link rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" />
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <title>演示日历插件</title>
<script>
    /* 3.当所有的页面加载完成后，执行入口函数 */
    $(function () {
        // 当容器加载完成后，对容器调用工具函数
        $("#myDate").datetimepicker({
            language:'zh-CN',
            format: 'yyyy-mm-dd',
            minView:'month',        // 可以选择的最小视图
            initialDate:new Date(), // 在选择完日期后自动关闭
            autoclose:true,         // 设置选择完日期或时间以后，是否自动关闭
            todayBtn:true,          // 显示今天按钮
            clearBtn:true           // 清空按钮
        });


    })
</script>
</head>
<body>
    <%-- 2.创建容器 --%>
    <input type="text" id="myDate" readonly="true">
</body>
</html>
