<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.sql.*, javax.sql.*, javax.servlet.http.*, tw.com.pubu.hunter.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="js/DevTools.js"></script>
<script type="text/javascript" src="js/showOrders.js"></script>
<link rel="stylesheet" type="text/css" href="css/showOrders.css" />
<title>訂購單</title>
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
		${loginId }號會員${loginName}的訂購紀錄：<br />
		<table border='1'>
		<TR>
		<TH>單號<TH>客戶<TH>時間<TH>金額<TH>狀態
		<%
			//取得客戶ID
		 	int ctm_id = Integer.valueOf(session.getAttribute("loginId").toString());
		 	
			Connection conn = ConnectionFactory.getConnection();
			//取出訂單
			String qryStmt = "SELECT od.od_id, ctm.ctm_account, od.od_time, od.od_total_price, od.od_state, od.ctm_id "
						   + "FROM orders AS od " 
				  		   + "JOIN customers AS ctm ON od.ctm_id = ctm.ctm_id " 
				  		   + "WHERE od.ctm_id=? AND od.od_state='close' "
				  	   	   + "ORDER BY od.od_time DESC;";
			PreparedStatement pstmt = conn.prepareStatement(qryStmt);
			pstmt.setInt(1, ctm_id);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				out.println("<TR onclick='getOrderDetails(" + rs.getString("od.od_id") + ");' "
							+"class='record' title='點選以顯示訂購明細' >");
				out.println("<TD>" + rs.getString("od.od_id"));
				out.println("<TD>" + rs.getString("ctm.ctm_account"));
				out.println("<TD>" + rs.getString("od.od_time"));
				out.println("<TD>" + rs.getString("od.od_total_price"));
				out.println("<TD>" + rs.getString("od.od_state"));
				out.println("</TR>");
			}	
		%>
		</table>
		<hr />
		<div id="areaShowOrderDetails"></div>	
	</c:otherwise>
</c:choose>
</body>
</html>