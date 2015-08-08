<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计数据</title>
<style type="text/css">

table.listTable{
	BORDER: red 1px solid; 
}

/* 表头   */
table tr.head {
    background-color:#696969;
    color: white;
    width: 100%;
    border-bottom: 0px;
	height:28px;
	font-weight: normal;
}

input[type="submit"]
{
	height:30px;
	width: 150px;
	border: solid 1px #919191;
}

</style>

</head>

<body>
<form action="">
<fieldset><legend>统计数据</legend>
<table class="listTable">
 <tr class="head"><th width="300px">注册用户数</th><th width="300px">订单数量</th></tr>
  <tr><td width="300px" align="center"><c:out value="${accountCount}"/></td><td width="300px" align="center"><c:out value="${orderCount}"/></td></tr>
</table>
</fieldset>
</form>

<form action="data" method="post">
<div align="center" style="width: 600px">
<input type="submit" value="刷新">
</div>
</form>

</body>
</html>