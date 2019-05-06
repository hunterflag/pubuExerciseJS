<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@ page import="java.sql.*, javax.sql.*, javax.servlet.http.*, tw.idv.hunter.*, org.json.*" %> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="js/DevTools.js"></script>
<title>訂單明細</title>
</head>
<body>
<c:choose>
	<c:when test="${empty loginName}">
		請先登入!! (稍後自動關閉)
		<script type='text/javascript'>
			setTimeout('window.close()', 2000);
		</script>
	</c:when>
	<c:otherwise>
		${loginName}查詢的訂購明細：	${oddts.size()} 筆<br/>
		<table border='1'>
		<TR>
		<TH>單號<TH>訂單號<TH>客戶<TH>品名<TH>定價<TH>售價<TH>數量<TH>小計
		<c:if test="${oddts.size()>0}">
			<c:forEach items="${oddts }" var="oddt" >
				<TR>
				<TD>${oddt.oddt_id}
				<TD>${oddt.od_id}
				<TD>${oddt.ctm_account}
				<TD>${oddt.pd_name}
				<TD>${oddt.pd_price}
				<TD>${oddt.oddt_price}
				<TD>${oddt.oddt_number}
				<TD>${oddt.sub_total_price}
			</c:forEach>
		</c:if>
		<TR><TH colspan=4>總價 <TD colspan=4> ${od_price}
		</table>
	</c:otherwise>
</c:choose>
</body>
</html>