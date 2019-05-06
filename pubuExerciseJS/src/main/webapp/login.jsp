<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登入</title>
</head>
<body>
	<form action="Login.do" method="post" target="_parent">
		帳號:<input name="loginAcc" type="text" value="Tester2">
		密碼:<input name="loginPwd" type="password" value="123456">
		<input type="submit" value="登入">
		<input type="reset" value="重填">
	</form>
</body>
</html>