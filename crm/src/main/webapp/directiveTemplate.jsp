<%--在将html转义为jsp的时候，要添加这一行--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    /**
     * 动态获取相关地址信息
     * request.getScheme()      获取客户端请求的协议类型（如 http 或 https）
     * request.getServerName()  返回处理请求的服务器主机名（域名或 IP 地址）
     * request.getServerPort()  获取服务器处理请求时使用的端口号，整数（如 8080、443）
     * request.getContextPath() 返回当前 Web 应用的上下文路径（即项目根路径）
     **/
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <%-- base 表示所有的路径连接都要在前面补充上 base 中 href 的路径前缀 --%>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <%-- base 表示所有的路径连接都要在前面补充上 base 中 href 的路径前缀 --%>


</head>
<body>

</body>
</html>
