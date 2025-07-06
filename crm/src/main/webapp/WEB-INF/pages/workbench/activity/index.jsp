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

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<!--  PAGINATION plugin -->
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.min.js"></script>
<title>演示 bs_paginationt 插件</title>
<script type="text/javascript">

	$(function(){
        // 给“创建”按钮添加单击事件
        $("#createActivityButton").click(function () {
            // 初始化工作，相较于 data-toggle="modal" data-target="#xxx" 这种方式可以进行初始化，完成更多的操作
            // 重置表单，JQreqry拿到dom对象，使用dom对象中的重置方法
            $("#createActivityForm").get(0).reset();

            // 弹出创建市场活动的模态窗口
            $("#createActivityModal").modal("show");
        });

        // 给“保存”按钮添加单击事件
        $("#saveCreateActivityBtn").click(function () {
            /**
             * 1.收集表单参数
             * create-marketActivityOwner 下拉列表中的值
             * create-marketActivityName  名称输入框中的值
             * create-startDate			  开始日期中的值
             * create-endDate			  结束日期中的值
             * create-cost				  成本
             * create-description		  描述
             */

            var owner = $("#create-marketActivityOwner").val();
            var name = $.trim($("#create-marketActivityName").val());
            var startDate = $("#create-startDate").val();
            var endDate = $("#create-endDate").val();
            var cost = $.trim($("#create-cost").val());
            var description = $.trim($("#create-description").val());

            /**
             * 2.表单验证
             * 所有者和名称不能为空
             * 如果开始日期和结束日期都不为空,则结束日期不能比开始日期小
             * 成本只能为非负整数
             */
            if (owner==""){
                alert("所有者不能为空");
                return;
            }
            if (name==""){
                alert("名称不能为空");
                return;
            }
            if (startDate!=""&&endDate!=""){
                // 日期比较大小的方式 1：转为 Date类型，获取到1970的毫秒数，两者进行比较
                // 日期比较大小的方式 2：使用字符串的大小代替日期的大小
                if(endDate<startDate){
                    alert("结束日期不能够比开始日期小！");
                    return;
                }
            }
            /**
             * 使用正则表达式来验证 “非负整数” 等验证情况
             * 正则表达式：
             * 	1.//：在JS中定义一个正则表达式		var regExp = /xxxxx/
             * 	2.^:  匹配字符串的开头位置
             * 	  $:  匹配字符串的结尾
             * 	3.[]: 表示匹配[]内字符集的一位字符	var regExp = /[abc]/
             * 	4.{}：匹配次数 						var regExp = /^[abc]{5}$/
             * 		{m} 	匹配m词
             * 		{m,n} 	匹配m次到n次
             * 		{m,}	匹配m次或者更多次
             * 	5.特殊符号
             * 	  \d:匹配一位数字 [0-9]
             * 	  \D:匹配一位非数字
             * 	  \w:匹配所有字符，汉字，——，数字等等
             * 	  \W:匹配非字符
             *
             * 	  *:匹配0次或者多次	{0，}
             * 	  +:匹配1次或者多次，{1，}
             * 	  ?:匹配1次或者0次，{0，1}
             */
            var refExp = /^(([1-9]\d*)|0)$/;
            if(!refExp.test(cost)){
                alert("成本只能是非负整数");
                return;
            }
            //3.ajax 发送请求
            $.ajax({
                url:'workbench/activity/saveCreateActivity.do',
                data:{
                    owner:owner, /* 参数名：参数值，参数名要与controlle中实体类的属性名一致 */
                    name:name,
                    startDate:startDate,
                    endDate:endDate,
                    cost:cost,
                    description:description
                },
                type:'post',
                dataType:'json',
                success:function (data) { //data:接收后台返回的响应信息
                    if(data.code=="1"){
                        //成功：关闭模态窗口
                        $("#createActivityModal").modal('hide');
                        // 刷新市场活动列，显示第一页数据，保持每页显示条目不变
                        queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
                    }else{
                        // 失败：提示信息，模态窗口不关闭
                        alert(data.message);
                        $("#createActivityModal").modal('show');
                    }

                }
            })

        });

        // 3.当容器加载完成后，对容器调用工具函数，使用class选择器选中，修改页面中的开始结束日期
        // 方法2：$("input[name='xxx']").datetimepicker
        $(".mydate").datetimepicker({
            language:'zh-CN',
            format: 'yyyy-mm-dd',
            minView:'month',        // 可以选择的最小视图
            initialDate:new Date(), // 在选择完日期后自动关闭
            autoclose:true,         // 设置选择完日期或时间以后，是否自动关闭
            todayBtn:true,          // 显示今天按钮
            clearBtn:true           // 清空按钮
        });

        /**
         * 查询市场活动数据
         * 执行查询的三种情况：
         3. 当用户切换以及每页显示条数和页号，查询所有指定每页显示条数和页号的数据以及所有符合条数数据的总条数
         */
        // 市场活动查询 case1. 当市场活动主页面加载完成以后，查询所有数据的第一页，以及所有数据的总条数
        queryActivityByConditionForPage(1,10);
        // 市场活动查询 case2. 当用户输入条件后，点击查询按钮，查询所有符合条件数据的第一页以及所有符合条件数据的总条数
        // 给“查询”按钮添加单机事件
        $("#queryActivityBtn").click(function () {
            // getOption 固定参数，rowsPerPage，想要获取选项栏对象的属性值
            // 每当用户点击查询以后，可以实现保持原来的每页显示条数。
            queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'));
        });

        /**
         * 给“全选”按钮添加单击事件
         */

        $("#chckAll").click(function () {
            // case1.如果“全选”按钮是选中状态，则列表中的所有checkbox按钮都是选中状态,this 表示当前正在发生这个事件dom对象
            // 写法1
            /* if(this.checked==true){
                // 空格选择器，表示选择tBody的所有input子标签，>父子选择器，只能获得子这一层的标签
                $("#tBody input[type='checkbox']").prop("checked",true);
            }else{
                $("#tBody input[type='checkbox']").prop("checked",false);
            }*/
            // 写法2
            $("#tBody input[type='checkbox']").prop("checked",this.checked);
        })

        // case2：有列表中的一个复选框没选中，表头取消“全选"按钮选中状态
        // 表示列表中的任何一个复选框触发了单机事件，都会执行的事件
        /*
        * $("#tBody input[type='checkbox']").click(function () {} 仅仅固有元素添加事件
        */
        /*$("#tBody input[type='checkbox']").click(function () {
            // 如果列表中的所有 checkbox都处于选中状态，则表头“全选”按钮处于选中状态
            // 思路：拿到数组中所有的checkbox和所有选中状态的checkbox进行比较
           if( $("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
               $("#chckAll").prop("checkbox",true);
           }else{
               $("#chckAll").prop("checkbox",false);
           };
        })*/
        // 使用：父选择器.on("事件类型",子选择器,function(){
        $("#tBody").on("click","input[type='checkbox']",function () {
            // 如果列表中的所有 checkbox都处于选中状态，则表头“全选”按钮处于选中状态
            // 思路：拿到数组中所有的checkbox和所有选中状态的checkbox进行比较
            if( $("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
                $("#chckAll").prop("checked",true);
            }else{
                $("#chckAll").prop("checked",false);
            };
        })

        /**
         * 删除市场活动数据
         */
        // 给删除按钮添加单机事件
        $("#deleteActivityButton").click(function () {
            // 收集参数（选中的市场活动id）
            // 获取列表中所有被选中的checkbox
            var checkedIds = $("#tBody input[type='checkbox']:checked");
            if(checkedIds.size()==0){
                alert("请选择要删除的市场活动！");
                return;
            }

            if(window.confirm("确定删除嘛？")){
                //$.each(checkedIds,function (index,obj)  .each() 遍历函数，checkedIds：所要进行遍历的数组,function (index 数组下标,obj每个数组下标的元素变量)
                var ids="";  //"id=xxx&id=xxx&id=xxx&id=xxx& 向后台发送拼成的数据
                $.each(checkedIds,function (index,obj) {
                    ids+="id="+this.value+"&";
                });
                ids =  ids.substr(0,ids.length-1);
                /*
                   data 传参：
                   方式1，data:{K:v} 局限性在于只能提交一个id对应一个value,最后一个id值覆盖所有的值
                   方式2：data:k1=v1&k1=v1,可以使用同一个参数变量对应多个参数值，操作较为繁琐
                   方式3：formdata对象，上传文件二进制数据
                 */
                alert("查看ids字符串是否拼接成功"+ids);
                $.ajax({
                    url:'workbench/activity/deleteActivityIds.do',
                    data:ids,
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        if(data.code=="1"){
                            // 刷新市场活动列表，保持市场活动条目不变
                            queryActivityByConditionForPage(1,$("#demo_pag1").bs_pagination('getOption','rowsPerPage'))

                        }else{
                            // 提示信息
                            alert(data.message);
                        }
                    }

                });
                alert("ajax 参数执行完成！");
            }

        })

    });

	// 入口函数外封装查询市场活动函数
    function queryActivityByConditionForPage(pageNo,pageSize) {
        // 收集参数
        var name = $("#query-name").val();
        var owner = $("#query-owner").val();
        var startDate = $("#query-startDate").val();
        var endDate = $("#query-endDate").val();
        // var pageNo = 1;    	// 查询第一页
        // var pageSize = 10;	// 每页显示的条数
        // 发送请求
        $.ajax({
            url:'workbench/activity/queryActivityByConditionForPage.do',
            data:{
                // data中参数名要与后台接受请求的参数名一致
                name:name,
                owner:owner,
                startDate:startDate,
                endDate:endDate,
                pageNo:pageNo,
                pageSize:pageSize,
            },
            type:'post',
            dataType:'json',
            success:function (data) { // data：接受后台返回的响应信息
                // .text() 更改选项框内容
                // 显示总条数
               // $("#totalRowsB").text(data.totalRows);
                // 显示市场活动列表
                // 遍历activityList集合，拼接所有的行数据，JSTL 标签用于遍历作用域中的标签，$.each() 遍历ajax中的data数据
                // function (index,obj),intdex是activityList的数据下标，obj从activityList取出数据元素
                // \" 转义双引号
                var htmlStr="";
                $.each(data.activityList,function (index,obj) {
                    htmlStr+="<tr class=\"active\">";
                    htmlStr+="<td><input type=\"checkbox\" value=\""+obj.id+"\"></td>";
                    htmlStr+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='detail.html';\">"+obj.name+"</a></td>";
                    htmlStr+="<td>"+obj.owner+"</td>";
                    htmlStr+="<td>"+obj.startDate+"</td>";
                    htmlStr+="<td>"+obj.endDate+"</td>";
                    htmlStr+="</tr>";
                });

                // .html(JSP页面片段字符串)  覆盖显示，.append(JSP页面片段字符串)  追加显示
                $("#tBody").html(htmlStr);

                //给“全选”按钮添加单击事件 case3：当用户添加执行分页查询以后，从后台新生成的列表，取消表头"全选"复选框
                $("#chckAll").prop("checked",false);

                /**
                 * 分页查询 - 3.对容器调用工具函数，为什么在这里调用工具函数？因为从后端获取到的data函数内包含totalRows（总条数）等相关信息
                 */
                // 计算总的分页页数
                var totalPages = 1;
                if(data.totalRows%pageSize==0){
                    totalPages = data.totalRows/pageSize;
                }else{
                    totalPages = parseInt(data.totalRows/pageSize) + 1; //JS的系统函数，parseInt() 截取小数的整数部分
                }

                $(function (){
                    $("#demo_pag1").bs_pagination({
                        currentPage: pageNo,         // 当前页号，相当于pageNo
                        rowsPerPage: pageSize,       // 每页显示条数，相当于pageSize
                        totalRows: data.totalRows,   // 总条数
                        totalPages:totalPages,       // 总分页页数，必传参数。总条数➗每页条数 = 分页页数

                        visiblePageLinks: 5,         // 设置可以显示的页面下标数

                        showGoToPage: true,          // 是否显示跳转到
                        showRowsPerPage: true,       // 是否显示"每页显示条数"部分，默认显示
                        showRowsInfo: true,          // 是否显示记录的信息
                        /**
                         * 当点击切换页号的后，返回点击切换后的 page_num 和 pageSize
                         * pageObj：这个参数代表了整个翻页对象，内含所有的翻页数据，例如，currentPage: 1,rowsPerPage: 10, 等等
                         */
                        onChangePage:function(event,pageObj) {
                            // 递归调用
                            queryActivityByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
                        },
                    })
                });
            }
        })
    }


</script>
</head>
<body>

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

                    <%-- 在这个表单中保存了模态窗口中的所有选项栏 --%>
					<form id="createActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<%-- “所有者”下拉列表 --%>
								<select class="form-control" id="create-marketActivityOwner">
									<%-- 使用 jstl 标签库，从 request 作用域中循环读取数据  --%>
									 <c:forEach items="${userList}" var="user">
										 <option value=${user.id}>${user.name}</option>
									 </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
								<%-- 名称输入框 --%>
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<%-- 开始日期输入框 --%>
								<%-- 添加类选择器， mydate，用于在JQ中调用--%>
								<input type="text" class="form-control mydate" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<%-- 结束日期输入框 --%>
								<input type="text" class="form-control mydate" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
								<%-- 成本输入框 --%>
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<%-- 描述输入框 --%>
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<%-- 摒弃 data-dismiss="modal"，相关的调用操作交由js管理--%>
					<button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
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
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<%-- 使用 jstl 标签库，从 request 作用域中循环读取数据  --%>
									<c:forEach items="${userList}" var="user">
										<option value=${user.id}>${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">

								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
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
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<%-- 摒弃data-toggle的控制方式，使用js控制模态窗口的弹出，在入口函数出，统一实现js代码的调用 --%>
				  <button type="button" class="btn btn-primary" id="createActivityButton"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityButton"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="chckAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
					<%-- 表内的数据交由ajax来获取处理 --%>
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>

                <%-- 分页查询 --%>
                <%-- 2.创建div容器 --%>
                <div id="demo_pag1"></div>
			</div>


			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					&lt;%&ndash; id=totalRowsB：标签属性id&ndash;%&gt;
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
	</div>
</body>
</html>