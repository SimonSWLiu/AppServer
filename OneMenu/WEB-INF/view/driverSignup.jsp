<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>driver Signup</title>
<style type="text/css">

/* 表头   */
table tr.head {
	background-color: #696969;
	color: white;
	width: 100%;
	border-bottom: 0px;
	height: 28px;
	font-weight: normal;
}

input[type="text"] {
	height: 24px;
	border: solid 1px #919191;
}

input[type="submit"] {
	height: 30px;
	width: 150px;
}
</style>

</head>

<body>
	<form action="signUp" method="post">
		<fieldset>
			<legend>driver Signup</legend>

			<div>
				<font style="color: red">${message}</font>
			</div>

			<table>
				<tr>
					<td align="right"><label>Name：</label></td>
					<td><input width="150px" type="text" name="name" value="">
					</td>
				</tr>
				<tr>
					<td align="right"><label>phone：</label></td>
					<td><input width="150px" type="text" name="phone" value="">
					</td>
				</tr>
				<tr>
					<td align="right"><label>address：</label></td>
					<td><input width="150px" type="text" name="address" value="">
					</td>
				</tr>
				<tr>
					<td align="right"><label>email：</label></td>
					<td><input width="150px" type="text" name="email" value="">
					</td>
				</tr>
				<tr>
					<td align="right"><label>password：</label></td>
					<td><input width="150px" type="text" name="password" value="">
					</td>
				</tr>
				<tr align="center">
					<td align="center" colspan="2"><input type="submit"
						value="signup"></td>
				</tr>
			</table>
		</fieldset>
	</form>
</body>
</html>