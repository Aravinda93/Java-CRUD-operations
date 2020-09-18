package echobot1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

	private static String dbUrl = "jdbc:mysql://localhost:3306/thinkport";
	private static String dbUsername = "root";
	private static String dbPassword = "";
	String url = dbUrl + "user=" + dbUsername + "&password=" + dbPassword;
	static Connection connection = null;

	public static Connection getConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			System.out.println("Unable to load the driver");
			System.err.println(e);
		}

		try {
			connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			return connection;
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		return connection;
	}

	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
