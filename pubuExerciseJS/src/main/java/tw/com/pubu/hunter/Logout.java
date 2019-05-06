package tw.com.pubu.hunter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "doLogout", urlPatterns = { "/Logout.do" })
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
//		out.append("Served at: ").append(request.getContextPath());

		HttpSession session = request.getSession();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset='UTF-8'>");
		out.println("</head>");
		out.println("<body>");
//		out.println("原sessionID:" + session.getId());
		if(session.getAttribute("loginName")!=null) {
			out.println("<h1>" + request.getSession().getAttribute("loginName") + "已登出, 並更新sessionID </h1><br>");
//	        session.removeAttribute("loginName");
//	        session.removeAttribute("loginId");
	        session.invalidate();
		}else {
			out.println("<h1>尚無人登入, 不需登出!</h1><br>");
		}
//		session = request.getSession();
//		out.println("新sessionID:" + session.getId());
		out.write("<button type='button' onclick='window.close()' >關閉</button>"
				+ "<script type='text/javascript'>"
				+ "self.opener.location.reload();"
				+ "setTimeout('window.close()', 2000);"
				+ "</script>");
		out.println("</body>");
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
