<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
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
		//为创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function () {
		    //为模态窗口绑定时间控件
            $(".time").datetimepicker({
                minView: "month",
                language:  'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });
			/*操作模态窗口的方式：
				需要操作的模态窗口的jquery对象，调用modal方法，为该方法传递参数 show：打开模态窗口. hide：关闭模态窗口*/
            $.ajax({
                url: "workbench/Activity/getUserList.do",
                type:"get",
                datatype:"json",
                success:function (data) {

                var data =JSON.parse(data);
                 var html ="<option></option>";
                $.each(data,function (i,n) {
                    html +="<option value='"+n.id+"'>"+n.name+"</option>";
                })
                    $("#create-owner").html(html);
                //取得当前用户的id,将用户设置为系统登录用户
					var  id= "${user.id}"
					$("#create-owner").val(id);

                    $("#createActivityModal").modal("show");
                }
            })

		})


        //为保存按钮绑定事件，执行添加操作
		$("#saveBtn").click(function () {
			$.ajax({
				url: "workbench/Activity/save.do",
				data:{
					"owner":$.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate": $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val())
				},
				type:"post",
				datatype:"json",
				success:function (data) {

					var datal = JSON.parse(data);
					/*
                    data{"success":true/false}
                     */
					if(datal.success){
						//添加成功后
						//刷新市场活动信息列表（局部刷新）
                       /* pageList(1,2)*/
                        pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						//清空添加操作模态窗口中的数据,jquery对象没有为我们提供reset方法，但原生js为我们提供了reset方法
						$("#activityAddForm")[0].reset();
						//关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");
					}else{
						alert("添加市场活动失败")
					}

				}
			})

		})
		//1）点击左侧才当中的“市场活动”超链接，刷新市场活动列表
		pageList(1,2);
       //2）为查询按钮提供刷新入口
		$("#search-Btn").click(function () {
			//在点击查询按钮时，将搜索框文本中的值，保存在文本的隐藏域中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			pageList(1,2);

		})

		//为全选的复选框绑定事件
		$("#selectAll").click(function () {
			$("input[name=selectcheckbox]").prop("checked",this.checked)
		})

		//动态生成的元素，我们要以on的方式来触发事件
		/*语法：(checkbox 最外层的有效元素为activityBody)
		$(需要绑定元素的有效外层元素).on(绑定事件的方式，需要绑定元素的jquery对象，回调函数)*/
		$("#activityBody").on("click",$("input[name=selectcheckbox]"),function () {
			$("#selectAll").prop("checked",$("input[name=selectcheckbox]").length==$("input[name=selectcheckbox]:checked").length)
		})

		//为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function () {

			//找到复选框中所有打√的复选框的jquery对象
            var $xz = $("input[name=selectcheckbox]:checked")

            if($xz.length==0){
                alert("请选择需要删除的记录")
                //选择了，可能一条，可能多条
            }else{
                //url:workbench/Activity/delete.do?id=xxx&id=xxx&id=xxx
                //将$xz对象中的每个dom对象遍历出来，取其value值，就相当于与取得了需要删除的id
                var param="";
                for(var i =0;i<$xz.length;i++){
                    param +=  "id=" + $($xz[i]).val()
                    if (i<$xz.length-1) {
                        param += "&";
                    }
                }
				if(confirm("确认删除所选记录吗？记录将清空")){
					$.ajax({
						url: "workbench/Activity/delete.do",
						data: param,
						type:"post",
						datatype:"json",
						success:function (data) {
							var data =JSON.parse(data)
							/*data {"success":true/false}*/
							if(data.success){
								//删除成功后
								// pageList(1,2)
                                pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							}else{
								alert("删除市场活动失败")
							}
						}
					})

				}
            }

		})

		//为修改按钮绑定事件，打开修改操作的窗口
		$("#editBtn").click(function () {
			//取得列表√的对象
			var $xz = $("input[name=selectcheckbox]:checked")
			if($xz.length==0){
				alert("请选择要修改的对象")
			}else if($xz.length>1){
				alert("最多选择一个记录进行修改")
				//这时选择的对像一定只有一个
			}else{
				var id =$xz.val();

				$.ajax({
					url: "workbench/Activity/getUserListAndActivity.do",
					data:{
						"id": id
					},
					type:"get",
					datatype:"json",
					success:function (data) {
						var data = JSON.parse(data);
						//从后台取得信息显示在模态窗口
						/* data： 用户列表
						          市场活动对象
						{"uList":[{用户1}，{用户2}]，"a":{市场活动}}*/
						var html = "<option></option>";
						$.each(data.uList,function (i,n) {
							html += "<option  value='"+n.id+"'>"+n.name+"</option>"

						})
						//将用户信息列表铺到文本框中
						$("#edit-owner").html(html);

						var  id= "${user.id}"
						$("#edit-owner").val(id);
						//将活动信息列表铺到其他文本框中
						$("#edit-id").val(data.a.id);
						$("#edit-name").val(data.a.name);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);

						//展现修改的模态窗口
						$("#editActivityModal").modal("show")

					}
				})

			}

		})

        //为修改更新按钮绑定事件
        $("#updateBtn").click(function(){
            $.ajax({
                url: "workbench/Activity/update.do",
                data:{
                    "id":$.trim($("#edit-id").val()),
                    "owner":$.trim($("#edit-owner").val()),
                    "name" : $.trim($("#edit-name").val()),
                    "startDate" : $.trim($("#edit-startDate").val()),
                    "endDate": $.trim($("#edit-endDate").val()),
                    "cost" : $.trim($("#edit-cost").val()),
                    "description":$.trim($("#edit-description").val())
                },
                type:"post",
                datatype:"json",
                success:function (data) {
                    var datal = JSON.parse(data);
                    /*
                    data{"success":true/false}
                     */
                    if(datal.success){
                        //添加成功后
                        //刷新市场活动信息列表（局部刷新）
                        /*pageList(1,2)*/
                        pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                            ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                        //关闭修改操作的模态窗口
                        $("#editActivityModal").modal("hide");
                    }else{
                        alert("修改市场活动失败")
                    }

                }
            })

        })


	});

    	//pageNo:页码
		//pageSize:每页展现的记录数
	/*pageList()方法就是向后台发送ajax请求到后台，从后台去的最新的市场活动信息列表数据
			通过想响应回来的数据，局部刷新市场活动信息列表。
	我们在下列的情况下，需要调用pageList方法（刷新市场活动列表）
	1）点击左侧才当中的“市场活动”超链接，需要刷新市场活动列表
	2）添加、修改、删除、需要刷新市场活动列表
	3）点击查询按钮的时候，需要刷新市场活动列表
	4）点击分页组件的时候*/
    function pageList(pageNo,pageSize){

        //当进行列表刷新时将总复选框取消
        $("#selectAll").prop("checked",false)

		//查询前将隐藏域中保存的信息取出来，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url: "workbench/Activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startDate").val()),
				"endDate":$.trim($("#search-endDate").val())

			},
			type:"get",
			datatype:"json",
			success:function (data) {
			/*	data
				需要的数据是：
				dataList：市场活动信息列表 List<Activity> aList
				total:分页查询需要的总条数  int total
				{"total":100,"dataList":[{市场活动1},{2},{3}]}*/
			var data = JSON.parse(data);

			var html="";
			$.each(data.dataList,function (i,n) {
				html += '<tr class="active">';
				html += '<td><input type="checkbox" name="selectcheckbox" value="'+n.id+'"/></td>';
				html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/Activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
				html += '<td>'+n.owner+'</td>';//目前展现的为32字符，需在SQL语句中与user表关联获得所有者姓名
				html += '<td>'+n.startDate+'</td>';
				html += '<td>'+n.endDate+'</td>';
				html += '</tr>';
				
			})
				$("#activityBody").html(html);
				//计算总页数
				var totalPages=data.total%pageSize ==0 ? data.total/pageSize:parseInt(data.total/pageSize)+1;
				//分页组件
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
					//该回调函数是在，点击分页组件的时候触发的。
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
<%--//查询文本的隐藏域--%>
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
						<input type="hidden"  id="edit-id"/>
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
							<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name" >
							</div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" >
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<%--文本域textarea:1)以标签对形式出现，中间没有空格。2）它属于表单元素，用val()进行取值和赋值--%>
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
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
				
					<form   id="activityAddForm"  class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-markActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
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
                    data-dismiss="modal:关闭模态窗口
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
				  
				  <button type="button" id="search-Btn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				<%--未来的实际开发中，对于触发模态窗口的操作。一定不要写死在元素当中，应该由我们自己写js代码来操作：--%>
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"  id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
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
					<tbody  id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>--%>
                        </tr>
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