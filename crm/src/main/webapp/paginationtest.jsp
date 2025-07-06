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
    <!--  JQUERY -->
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <!--  BOOTSTRAP -->
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <!--  PAGINATION plugin -->
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.min.js"></script>
    <title>演示 bs_paginationt 插件</title>
    <%-- 3.调用函数 --%>
    <%-- https://blog.csdn.net/Julia2016/article/details/139202340          演示文章教程
         https://html-use.blogspot.com/2014/11/bootstrap-pagination.html    参数列表--%>
    <script type="text/javascript">
        $(function (){
            $("#demo_pag1").bs_pagination({
               currentPage: 1,              // 当前页号，相当于pageNo
               rowsPerPage: 10,             // 每页显示条数，相当于pageSize
               totalRows: 1000,             // 总条数
               totalPages:100,              // 总分页页数，必传参数。总条数➗每页条数 = 分页页数

               visiblePageLinks: 5,         // 设置可以显示的页面下标数

               showGoToPage: true,          // 是否显示跳转到
               showRowsPerPage: true,       // 是否显示"每页显示条数"部分，默认显示
               showRowsInfo: true,          // 是否显示记录的信息
               onChangePage:function(event,pageObj) {     // 当点击切换页号的后，返回点击切换后的 page_num 和 pageSize
                   // pageObj：这个参数代表了整个翻页对象，内含所有的翻页数据，例如，currentPage: 1,rowsPerPage: 10, 等等
                   alert("切换后的页号"+pageObj.currentPage);
                   alert("每页显示条数"+pageObj.rowsPerPage);
            },
            })
        });
    </script>
</head>
<body>
    <%-- 2.创建div容器 --%>
    <div id="demo_pag1"></div>
</body>
</html>
