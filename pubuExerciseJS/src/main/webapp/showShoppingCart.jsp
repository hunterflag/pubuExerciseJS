<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, javax.sql.*, javax.servlet.http.*, tw.com.pubu.hunter.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="js/showShoppingCart.js"></script>
<script type="text/javascript" src="js/DevTools.js"></script>
<title>購物車</title>
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
		購物車(已選購商品):
		<table border='1'>
		<TR>
		<TH><TH>客戶<TH>品名<TH>售價<TH>數量<TH>小計
		<%
			int ctm_id = 3;
		 	ctm_id = Integer.valueOf(session.getAttribute("loginId").toString());
		
			Connection conn = ConnectionFactory.getConnection();
			
			String qryStmt = "SELECT  ctm.ctm_account, pd.pd_name, sc.sc_price, sc.sc_number, pd.pd_id "
						   + "FROM shopping_carts AS sc " 
				  		   + "JOIN customers AS ctm ON sc.ctm_id = ctm.ctm_id " 
				  		   + "JOIN products AS pd ON sc.pd_id = pd.pd_id "
				  		   + "WHERE ctm.ctm_id=? "
				  	   	   + "ORDER BY pd.pd_name;";
			PreparedStatement pstmt = conn.prepareStatement(qryStmt);
			pstmt.setInt(1, ctm_id);
			ResultSet rs = pstmt.executeQuery();
			
			int total_price=0;
			while (rs.next()) {
				out.println("<TR>");
				out.println("<TD>" + "<button type='button' onclick='removeFromShoppingCart(" + rs.getString("pd.pd_id") + ")'>移除</button>");
				out.println("<TD><span id='ctm_id' value='" + ctm_id + "'/>" + rs.getString("ctm.ctm_account") + "</span>");
				out.println("<TD><span id='pd_id' value='" + rs.getString("pd.pd_id") + "'/>" + rs.getString("pd.pd_name") + "</span>");
				out.println("<TD>" + rs.getString("sc.sc_price"));
				out.println("<TD><input id='sc_number' type='number' min=1  value=" + rs.getString("sc.sc_number") +" />"
						   +"<button type='button' onclick='updateShoppingCart(" + ctm_id + ", "
							   													 + rs.getString("pd.pd_id")
							   												 	 + ")'>修改</button>");
				int sub_total_price = rs.getInt("sc.sc_price")*rs.getInt("sc.sc_number"); 
				out.println("<TD>" + sub_total_price );
				out.println("</TR>");
		
				total_price = total_price + sub_total_price;
			}
			out.println("<TR><TH colspan=4>總價 <TD colspan=2>" +total_price );
		%>
		</table>
		
		<button id="btnClear">全部清除</button>
		<button id="btnOrder">確認結帳</button>
	</c:otherwise>
</c:choose>
</body>
</html>