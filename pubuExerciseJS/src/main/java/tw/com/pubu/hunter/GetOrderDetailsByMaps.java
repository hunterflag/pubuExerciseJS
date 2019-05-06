package tw.com.pubu.hunter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="doGetOrderDetailsByMaps", urlPatterns= {"/GetOrderDetailsByMaps.do"})
public class GetOrderDetailsByMaps extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
//		out.append("Served at: ").append(request.getContextPath());

		//未登入就不用做了
		HttpSession session = request.getSession();
		System.out.println(session.getAttribute("loginId"));
		if (session.getAttribute("loginId") == null) {
			out.write("尚未登入");
			return;
		}

		//1.從前端取得訂單編號
		String str_od_id = request.getParameter("od_id");
		int od_id = Integer.valueOf(str_od_id==null ? "0" : str_od_id);
		
		System.out.printf("od_id=%d\n", od_id);
		
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();

			//2.依據訂單編號, 從資料庫取得訂單明細
			String qryStmt = "SELECT oddt.oddt_id, od.od_id, ctm.ctm_account, pd.pd_name, pd.pd_price, oddt.oddt_price, oddt.oddt_number "
						   + "FROM order_details AS oddt " 
				  		   + "JOIN products AS pd ON oddt.pd_id = pd.pd_id "
				  		   + "JOIN orders AS od ON od.od_id = oddt.od_id " 
				  		   + "JOIN customers AS ctm ON od.ctm_id = ctm.ctm_id " 
				  		   + "WHERE oddt.od_id=? "
				  	   	   + "ORDER BY pd.pd_name;";
			PreparedStatement pstmt = conn.prepareStatement(qryStmt);
			pstmt.setInt(1, od_id);
			ResultSet rs = pstmt.executeQuery();

			//3.將訂單明細, 轉換成 Map物件
			ArrayList<Map<String, String>> oddts = new ArrayList<>();
			int total_price=0;
			while (rs.next()) {
				Map<String, String> oddt = new HashMap<String, String>();
				oddt.put("oddt_id", rs.getString("oddt.oddt_id"));
				oddt.put("od_id", rs.getString("od.od_id"));
				oddt.put("ctm_account", rs.getString("ctm.ctm_account"));
				oddt.put("pd_name", rs.getString("pd.pd_name"));
				oddt.put("pd_price", rs.getString("pd.pd_price"));
				oddt.put("oddt_price", rs.getString("oddt.oddt_price"));
				oddt.put("oddt_number", rs.getString("oddt.oddt_number"));
				int sub_total_price = rs.getInt("oddt.oddt_price")*rs.getInt("oddt.oddt_number"); 
				oddt.put("sub_total_price", String.valueOf(sub_total_price));
				total_price += sub_total_price;
				oddts.add(oddt);
			}
			
			//4.存進request中、 待用 (Session 不適合, LifeCycle太長、佔記憶體)
			request.setAttribute("oddts", oddts);
			request.setAttribute("od_price", total_price);
			System.out.println(oddts.toString());
	
			//5.forward
			RequestDispatcher dispatcher = request.getRequestDispatcher("/showOrderDetailsByMaps.jsp"); 
			dispatcher.forward(request, response);
			
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

}
