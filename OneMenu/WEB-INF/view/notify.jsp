<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>推送数据接口测试</title>
<style type="text/css">

table.listTable{
	BORDER: red 1px solid; 
}
/*
table.listTable tr
{
	height:30px;
	b
}

table.listTable tr td
{
	height:30px;
	border-bottom: #696969 1px solid;
	border-right: red 1px solid;
	
}

table.listTable tr th
{
	height:30px;
	border-bottom: red 1px solid;
	border-right: red 1px solid;
}*/

/* 表头   */
table tr.head {
    background-color:#696969;
    color: white;
    width: 100%;
    border-bottom: 0px;
	height:28px;
	font-weight: normal;
}

input[type="text"]
{
	height:24px;
	border: solid 1px #919191;
}

input[type="submit"]
{
	height:30px;
	width: 150px;
}

</style>

</head>

<body>

<form action="push" method="post">
<fieldset>
<legend>推送测试</legend>
<table>
 <tr>
  <td align="right">
   <label >deviceToken：</label>
  </td>
  <td>
    <input width="150px" type="text" name="deviceToken" value="">
  </td>
 </tr>
 <tr>
  <td align="right">
   <label>message：</label>
  </td>
  <td>
    <input width="150px" type="text" name="message" value="">
  </td>
 </tr>
 
 <tr align="center">
   <td align="center"><input type="submit" value="推送"></td>
 </tr>
</table>
</fieldset>
</form>

</body>
</html>