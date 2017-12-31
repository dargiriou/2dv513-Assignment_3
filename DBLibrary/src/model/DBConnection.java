package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;

public class DBConnection {

	static String filename = "LibraryDB.db";
	public static String fileurl = "jdbc:sqlite:" + filename;
	
	public static Connection getConnection() throws SQLException
	{
		
		try {
			Class.forName("org.sqlite.JDBC");
			SQLiteConfig config = new SQLiteConfig();  
		    config.enforceForeignKeys(true);
			return DriverManager.getConnection(fileurl);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
