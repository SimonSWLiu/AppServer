<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配置数据</title>
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
<form action="">
<fieldset><legend>配置数据</legend>
<table class="listTable">
 <tr class="head"><th width="150px">key</th><th width="600px">value</th></tr>
 <c:forEach items="${list}" var="config">
  <tr><td width="150px" align="center"><c:out value="${config.key}"/></td><td width="600px" align="center"><c:out value="${config.value}"/></td></tr>
 </c:forEach>
</table>
</fieldset>
</form>

<br/>
<c:if test="${appTips}!=null">
 <h1><c:out value="${appTips}"></c:out></h1>
</c:if>
<br/>

<form action="updateAppConfig" method="post">
<fieldset>
<legend>修改 App配置数据</legend>
<table>
 <tr>
  <td align="right">
   <label >appVersion：</label>
  </td>
  <td>
    <input width="150px" type="text" name="appVersion" value="">
  </td>
 </tr>
 <tr>
  <td align="right">
   <label>appVersionDesc：</label>
  </td>
  <td>
    <input width="150px" type="text" name="appVersionDesc" value="">
  </td>
 </tr>
 <tr>
  <td align="right">
   <label>forceUpdateVersion：</label>
  </td>
  <td>
    <input width="150px" type="text" name="forceUpdateVersion" value="">
  </td>
 </tr>
 <tr>
  <td align="right">
   <label >forceUpdate：</label>
  </td>
  <td>
    <input width="150px" type="text" name="forceUpdate" value="">
  </td>
 </tr>
 <tr>
  <td align="right">
   <label>tax：</label>
  </td>
  <td>
    <input width="150px" type="text" name="tax" value="">
  </td>
 </tr>
 <tr align="center">
   <td align="center" colspan="2"><input type="submit" value="保存"></td>
 </tr>
</table>
</fieldset>
<input type="hidden" name="sign" value="${sign}">
</form>

<br/>
<c:if test="${tips}!=null">
 <h1><c:out value="${tips}"></c:out></h1>
</c:if>
<br/>

<form action="save" method="post">
<fieldset>
<legend>新增/修改 配置数据</legend>
<table>
 <tr>
  <td>
   <label>键：</label>
  </td>
  <td>
    <input type="text" width="150px" name="key" value="">
  </td>
 </tr>
 <tr>
  <td>
   <label>值：</label>
  </td>
  <td>
    <input type="text" width="150px" name="value" value="">
  </td>
 </tr>
 <tr align="center">
   <td align="center" colspan="2"><input type="submit" value="保存"></td>
 </tr>
</table>
</fieldset>
<input type="hidden" name="sign" value="${sign}">
</form>

<br/>
<br/>

<form action="delete" method="post">
<fieldset>
<legend>删除配置数据</legend>
<table>
 <tr>
  <td>
   <label>键：</label>
  </td>
  <td>
    <input type="text" width="150px" name="key" value="">
  </td>
 </tr>
 <tr align="center">
   <td align="center" colspan="2"><input type="submit" value="删除"></td>
 </tr>
</table>
</fieldset>
<input type="hidden" name="sign" value="${sign}">
</form>
</body>
</html>