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

@WebServlet(name = "doShoppingCartConfirmOrder", urlPatterns = { "/ShoppingCartConfirmOrder.do" })
public class ShoppingCartConfirmOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("doShoppingCartConfirmOrder Served at: ")
							.append(request.getContextPath());
		//取出客戶Id
		HttpSession session = request.getSession();
		int ctm_id = Integer.valueOf(session.getAttribute("loginId").toString());
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			//建立訂單 1: 先建立訂單編號、取得新增訂單的 oid
			int od_id=0;
			String insStmt = "INSERT INTO orders(ctm_id) VALUES (?);"; 
			PreparedStatement ins_stmt = conn.prepareStatement(insStmt, PreparedStatement.RETURN_GENERATED_KEYS);
			ins_stmt.setInt(1, ctm_id);
			ins_stmt.executeUpdate();
			ResultSet rs_od = ins_stmt.getGeneratedKeys();
			if(rs_od.next()) {
				od_id = rs_od.getInt(1);
			}

			//取出購物車內容
			String qryStmt = "SELECT * FROM shopping_carts WHERE ctm_id=?;"; 
			PreparedStatement pstmt = conn.prepareStatement(qryStmt);
			pstmt.setInt(1, ctm_id);
			ResultSet rs_sc = pstmt.executeQuery();

			//依購物車內容, 建立訂購明細表 & 計算總價
			String oddtInsStmt = "INSERT INTO order_details(pd_id, oddt_price, oddt_number, od_id) " 
								+ "VALUES (?, ?, ?, ?);";
			PreparedStatement oddt_insStmt = conn.prepareStatement(oddtInsStmt);
			
			int total_price=0;
			while (rs_sc.next()) {
				total_price += rs_sc.getInt("sc_price") * rs_sc.getInt("sc_number");
				oddt_insStmt.setInt(1, rs_sc.getInt("pd_id"));
				oddt_insStmt.setInt(2, rs_sc.getInt("sc_price"));
				oddt_insStmt.setInt(3, rs_sc.getInt("sc_number"));
				oddt_insStmt.setInt(4, od_id);
				oddt_insStmt.executeUpdate();
			}
			System.out.println(total_price);
			
			//建立訂單2：更新總價
			String oddtUpdStmt = "UPDATE orders "
								+"SET od_total_price=?, od_state=? " 
								+" WHERE od_id=? ;"; 
			PreparedStatement oddt_updStmt = conn.prepareStatement(oddtUpdStmt);
			oddt_updStmt.setInt(1, total_price);
			oddt_updStmt.setString(2, "close");
			oddt_updStmt.setInt(3, od_id);
			oddt_updStmt.executeUpdate();
			
			//移除購物車內容
			String delStmt = "DELETE FROM shopping_carts WHERE ctm_id=?;"; 
			PreparedStatement del_Stmt = conn.prepareStatement(delStmt);
			del_Stmt.setInt(1, ctm_id);
			del_Stmt.executeUpdate();
			
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
