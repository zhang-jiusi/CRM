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

            // 动态的鼠标悬停事件
            $("#remarkDivList").on("mouseover",".remarkDiv",function () {
                $(this).children("div").children("div").show();
            });

            // 动态鼠标移除事件
            $("#remarkDivList").on("mouseout",".remarkDiv",function () {
                $(this).children("div").children("div").hide();
            });
            // css
            $("#remarkDivList").on("mouseover",".myHref",function () {
                $(this).children("span").css("color","red");
            });
            $("#remarkDivList").on("mouseout",".myHref",function () {
                $(this).children("span").css("color","#E6E6E6");

            });

            /**
             * 给“保存”按钮添加单击事件
             *
             *  为什么'$"{activity.id}' 为什么加单引号？（多加了一个"是为了让他成为注释）
             * 	因为：$"{} EL 表达式取值会把值当作未被定义的变量名，没有被定义的变量名会使得activityId为空值
             *
             */
            $("#saveCreateRemarkBtn").click(function () {
                // 收集参数
                var noteContent	 = $.trim($("#remark").val());
                var activityId = '${activity.id}';				//EL 表达式可以应用于jsp页面的任何地方
                console.log($.trim($("#remark").val()));
                console.log('${activity.id}');

                // 表单验证
                if (noteContent==""){
                    alert("输入值不能为空");
                    return;
                }

                // 发送请求
                $.ajax({
                    url:'workbench/activity/saveCreateActivityRemark.do',
                    data:{
                        noteContent:noteContent,
                        activityId:activityId
                    },
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        if(data.code=="1"){
                            // 清空用户的输入内容
                            $("#remark").val("");
                            //刷新备注列表
                            // id=\"div_"+data.retData.id+"\"
                            var htmlStr="";
                            htmlStr+="<div id=\"div_"+data.retData.id+"\"  class=\"remarkDiv\" style=\"height: 60px;\">";
                            htmlStr+="<img title=\"${sessionScope.sessionUser.name}\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
                            htmlStr+="<div style=\"position: relative; top: -40px; left: 40px;\" >";
                            htmlStr+="<h5>"+data.retData.noteContent+"</h5>";
                            htmlStr+="<font color=\"gray\">市场活动</font> <font color=\"gray\">-</font> <b>${activity.name}</b> <small style=\"color: gray;\"> "+data.retData.createTime+" 由${sessionScope.sessionUser.name}创建</small>";
                            htmlStr+="<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
                            htmlStr+="<a class=\"myHref\" name=\"editA\" remarkId=\""+data.retData.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
                            htmlStr+="&nbsp;&nbsp;&nbsp;&nbsp;";
                            htmlStr+="<a class=\"myHref\" name=\"deleteA\" remarkId=\""+data.retData.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
                            htmlStr+="</div>";
                            htmlStr+="</div>";
                            htmlStr+="</div>";
                            $("#remarkDiv").before(htmlStr);
                        }
                    }
                })

            });//$("#saveCreateRemarkBtn")给“保存”按钮添加单击事件

            /**
             * 给“删除”按钮添加单击事件
             */
            $("#remarkDivList").on("click","a[name='deleteA']",function () {
                // 收集参数
                var id =  $(this).attr("remarkId");
                // 发送请求
                $.ajax({
                    url:'workbench/activity/deleteActivityRemarkById.do',
                    data:{
                        id:id
                    },
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        if(data.code=="1"){
                            // 刷新备注列表
                            $("#div_"+id).remove();
                        }else{
                            // 提示信息
                            alert(data.message);
                        }
                    }
                });

            });// $("#remarkDivList") 给“删除”按钮添加单击事件

            /**
             * 给备注后面的"修改"按钮添加单击事件
             */
            $("#remarkDivList").on("click","a[name='editA']",function () {

                // 获取备注的id和noteCOntent
                // 收集参数
                var id =  $(this).attr("remarkId");
                /**
                 * 现在拿到<a>,通过<a>标签拿到父标签div的兄弟标签h5
                 * 方式1：（给“删除”按钮添加单击事件采用的）
                 *      在<h5>标签中设定，id=\"h5_"+data.retData.id+"\"，
                 *      通过$("#h5_"+id)拿到div的id
                 * 方式2：（现在采用的）
                 *      现在可以拿到<a>标签，通过 div标签中的 id=\"div_"+data.retData.id+"\"，拿到div标签，空格h5,空格子选择器拿到h5标签
                 *      空格选择器可以选择指定元素下的所有子元素。
                 *      var noteConten = $("div_"+id+" h5").text;
                 */
                var noteConten = $("#div_"+id+" h5").text();
                // 将活动的id，备注信息填到到模态窗口的隐藏域中
                $("#edit-id").val(id);
                $("#edit-noteContent").val(noteConten);

                // 获取到模态窗口的id，弹出模态窗口
                $("#editRemarkModal").modal("show");

            });//$("#remarkDivList")  给备注后面的"修改"按钮添加单击事件

            /**
             * 给模态窗口中的“保存”按钮添加单击事件updateRemarkBtn
             */
            $("#updateRemarkBtn").click(function () {
                // 收集参数
                var id = $("#edit-id").val(); // 谁给这个隐藏域赋值的？
                var noteContent  = $.trim($("#edit-noteContent").val());

                // 表单验证
                if(noteContent==""){
                    alert("提交修改内容信息不为空");
                    return ;
                }
                // 发送请求
                $.ajax({
                    url:'workbench/activity/saveEditActivityRemark.do',
                    data:{
                        id:id,
                        noteContent:noteContent
                    },
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        if(data.code=="1"){
                            // 关闭模态窗口
                            $("#editRemarkModal").modal("hide");

                            // 将从数据库中返回的，备注信息，修改事件，修改者写入到 detail.jsp 的备注div中
                            // 刷新活动列表
                            /*$("#div_"+data.retData.id+" h5").text(data.retData.noteContent);
                            $("#div_"+data.retData.id+" samll").text(" "+data.retData.editTime+" 由${sessionScope.sessionUser.name}修改");*/
                            $("#div_"+data.retData.id+" h5").text(data.retData.noteContent);
                            $("#div_"+data.retData.id+" small").text(" "+data.retData.editTime+" 由${sessionScope.sessionUser.name}修改");
                            console.log(data.retData.id);
                            console.log(data.retData.editTime);
                        }else {
                            // 提示信息
                            alert(data.message);
                            // 模态窗口不关闭
                            $("#editRemarkModal").modal("show");
                        }
                    }

                });
            });// $("#updateRemarkBtn") ,给模态窗口中的“保存”按钮添加单击事件updateRemarkBtn


        });

    </script>

</head>
<body>

<!-- 修改市场活动备注的模态窗口 -->
<div class="modal fade" id="editRemarkModal" role="dialog">
    <%-- 当前模态窗口的备注的id --%>
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
                    <%-- 设定表单的隐藏域名，保存活动市场活动的 备注id --%>
                    <input type="hidden" id="edit-id">
                    <div class="form-group">
                        <label for="edit-noteContent" class="col-sm-2 control-label">内容</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-noteContent"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="updateRemarkBtn" type="button" class="btn btn-primary" >更新</button>
            </div>
        </div>
    </div>
</div>



<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>市场活动-${activity.name} <small>${activity.startDate} ~ ${activity.endDate}</small></h3>
    </div>

</div>

<br/>
<br/>
<br/>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.owner}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.name}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>

    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">开始日期</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.startDate}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.endDate}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">成本</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.cost}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.createTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.editTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${activity.description}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div id="remarkDivList" style="position: relative; top: 30px; left: 40px;">
    <div class="page-header">
        <h4>备注</h4>
    </div>

    <!-- 遍历存放在request中的remarkList，显示所有的备注 -->
    <%--
        在 jsp 中遍历集合、数组由两种方式
            方式1：$.each()
            方式2：JSTL 标签库中的 foreach标签，jstl标签与el表达式同时使用，${}进行取值
        选取：
            $.each()，在js代码中遍历
            foreach标签，遍历作用域中的数据
    --%>
    <%--
        items：存储在数据域中的名字，所要遍历的集合对象，使用"${}"
        var:   循环遍历，从items中取出一个要遍历的对象赋值给var

    --%>
    <!--遍历remarkList，显示所有的备注-->
    <c:forEach items="${remarkList}" var="remark">
        <div id="div_${remark.id}" class="remarkDiv" style="height: 60px;">
            <img title="${remark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
            <div style="position: relative; top: -40px; left: 40px;" >
                <h5 >${remark.noteContent}</h5>
                    <%-- 创建时间还是修改时间，写法1：三目运算符 --%>
                    <%-- 创建者还是修改者 --%>
                    <%-- 创建还是修改 --%>
                <font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;"> ${remark.editFlag=='1'?remark.editTime:remark.createTime} 由${remark.editFlag=='1'?remark.editBy:remark.createBy}${remark.editFlag=='1'?'修改':'创建'}</small>
                <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                        <%--  自定义remarkId属性，用于保存备注的id --%>
                    <a class="myHref" name="editA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a class="myHref" name="deleteA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
                </div>
            </div>
        </div>
    </c:forEach>
    <%-- 输入域 --%>
    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button id="saveCreateRemarkBtn" type="button" class="btn btn-primary">保存</button>
            </p>
        </form>
    </div>
</div>
<div style="height: 200px;"></div>
</body>
</html>