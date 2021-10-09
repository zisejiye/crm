<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/7/30 0030
  Time: 22:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
</head>
<body>
    $.ajax({
    url: "",
    data:{},
    type:"",
    datatype:"json",
    success:function (data) {

    }
    })


    //创建时间，当前系统时间
    String createTime = DateTimeUtil.getSysTime();
    //创建人，当前系统用户
    String createBy = ((User)request.getSession().getAttribute("user")).getName();

    $(".time").datetimepicker({
    minView: "month",
    language:  'zh-CN',
    format: 'yyyy-mm-dd',
    autoclose: true,
    todayBtn: true,
    pickerPosition: "bottom-left"
    });


    function showActivityList(){
    $.ajax({
    url: "workbench/clue/getActivityListByClueId.do",
    data:{
    "ClueId": "${c.id}",
    },
    type:"get",
    datatype:"json",
    success:function (data) {
    var data = JSON.parse(data);
    /*data
    [{市场活动}，{市场活动2}.。]
    * */
    var html = "";
    $.each(data,function(i,n){
    html += '<tr>';
        html += '<td>'+n.name+'</td>';
        html += '<td>'+n.startDate+'</td>';
        html += '<td>'+n.endDate+'</td>';
        html += '<td>'+n.owner+'</td>';
        html += '<td><a href="javascript:void(0);" onclick="unbund(\''+n.id+'\')" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>';
        html += '</tr>';
    })
    ${"#activityBody"}.html(html);
    }

    })
    function unbund(id){

    }
    }

</body>
</html>
