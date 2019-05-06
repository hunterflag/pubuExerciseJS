package tw.com.pubu.hunter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "doUpdateShoppingCart", urlPatterns = { "/UpdateShoppingCart.do" })
public class UpdateShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("doUpdateShoppingCart Served at: ")
							.append(request.getContextPath());

		//取得客戶ID
		HttpSession session = request.getSession();
		int ctm_id = Integer.valueOf(session.getAttribute("loginId").toString());
		
		//取得商品ID & 更新數量
		int pd_id =  Integer.valueOf(request.getParameter("pd_id"));
		int sc_number =  Integer.valueOf(request.getParameter("sc_number"));
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			
			//開啟購物車, 更新紀錄
			String updStmt = "UPDATE shopping_carts SET sc_number=? WHERE ctm_id=? AND pd_id=?;"; 
			PreparedStatement pstmt = conn.prepareStatement(updStmt);
			pstmt.setInt(1, sc_number);
			pstmt.setInt(2, ctm_id);
			pstmt.setInt(3, pd_id);
			int rs = pstmt.executeUpdate();
			System.out.println("updateShoppingCart: " + rs);
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn!=null) {
				try {
					conn.close();
					response.sendRedirect("showShoppingCart.jsp");
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
