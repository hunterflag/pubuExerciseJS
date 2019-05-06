package tw.com.pubu.hunter;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "doAddShoppingCart", urlPatterns = { "/AddShoppingCart.do" })
public class AddShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.setContentType("text/html;charset=UTF-8");
//		PrintWriter out = response.getWriter();
//		out.append("doAddShoppingCart Served at: ").append(request.getContextPath());
//		System.out.println("Hello");
		
		//未登入就不用做了
		HttpSession session = request.getSession();
		if (session.getAttribute("loginId") == null) return;
		
		//輸入資料檢查
		String str_pd_id = request.getParameter("pd_id");
		int pd_id = Integer.valueOf(str_pd_id==null ? "0" : str_pd_id);
		
		String str_pd_price = request.getParameter("pd_price");
		int pd_price = Integer.valueOf(str_pd_price==null ? "0" : str_pd_price);
		
		int ctm_id = Integer.valueOf(session.getAttribute("loginId").toString());

		//取得 購物車
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();

			//檢查要加的商品是否已經在該員的購物車內? 若否, 則加入; 若是, 則不再加入!
			String qryStmt = "SELECT * FROM shopping_carts WHERE ctm_id=? AND pd_id=?;";
			PreparedStatement stmt = conn.prepareStatement(qryStmt);
			stmt.setInt(1, ctm_id);
			stmt.setInt(2, pd_id);
			ResultSet rs = stmt.executeQuery();
			
			if (!rs.next()) {
				String insStmt = "INSERT INTO shopping_carts(ctm_id, pd_id, sc_price, sc_number) "
								+"VALUES (?, ?, ?, 1);";
				PreparedStatement pstmt = conn.prepareStatement(insStmt);
				pstmt.setInt(1, ctm_id);
				pstmt.setInt(2, pd_id);
				pstmt.setInt(3, (pd_price/100)*100);
				pstmt.executeUpdate();
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn!=null) {
				try {
					conn.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
