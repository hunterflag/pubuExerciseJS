package tw.com.pubu.hunter;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name="doLogin", urlPatterns="/Login.do")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		
		if(request.getMethod().equals("GET")) {
			System.out.println("get");
			out.write("請勿竄改網頁!" 
					+ "<button type='button' onclick='window.close()' >關閉</button>"
					+ "<script type='text/javascript'>"
					+ "self.opener.location.reload();"
					+ "setTimeout('window.close()', 2000);"
					+ "</script>");
			return;
		}
		

		//取得網站傳入值
		String loginAcc = request.getParameter("loginAcc");
		String loginPwd = request.getParameter("loginPwd");
		
		//取得資料庫相同帳號
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			String qryStmt = "SELECT * FROM customers WHERE ctm_account = ?;";
			PreparedStatement stmt = conn.prepareStatement(qryStmt);
			stmt.setString(1, loginAcc);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {//有此帳號
				if (loginPwd.equals(rs.getString("ctm_password"))) {
					out.write(loginAcc + "登入成功! " + "<br/>");
					out.write("<button type='button' onclick='window.close()' >關閉</button>"
							+ "<script type='text/javascript'>"
							+ "self.opener.location.reload();"
							+ "setTimeout('window.close()', 2000);"
							+ "</script>");
					HttpSession session = request.getSession();
					session.setAttribute("loginName", loginAcc);
					session.setAttribute("loginId", rs.getInt("ctm_id"));
				}else {
					out.write("<script type='text/javascript'>"
							+ "alert('" + loginAcc + " 登入失敗!" + loginPwd + ", 重新輸入! 登記: " + rs.getString("ctm_password") + "');"
							+ "window.open('login.jsp', '_self');"
							+ "</script>");
				}
			} else {	//無此帳號
				out.write("<script type='text/javascript'>"
						+ "alert('沒有帳號: " + loginAcc + " , 重新輸入!');"
						+ "window.open('login.jsp', '_self');"
						+ "</script>");
				out.write(" " + loginAcc + "<br/>");
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

}
