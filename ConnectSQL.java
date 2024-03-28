package LibraryManagement;

import java.sql.*;

public class ConnectSQL {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/library_database";
	private static final String DB_USER = "diem";
	private static final String DB_PASSWORD = "1234";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

}
