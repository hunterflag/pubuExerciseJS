package tw.com.pubu.hunter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
//	private static String connUrl = "jdbc:mysql://192.168.100.250:3306/pubu_exercise"
	private static String connUrl = "jdbc:mysql://localhost:3306/pubu_exercise"
			 	  + "?user=root&password=123456"
//			 	  + "?user=root&password=SuperHunter"
//			 	  + "?user=hunter&password=SuperHunter"
				  + "&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
	private static Connection conn = null;
	
	public static Connection getConnection() throws SQLException {
		conn = DriverManager.getConnection(connUrl);
		return conn;
	}
	
	
	
}
