package com.venus.orm.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceUtils {

	private static Connection conn;
	private static DataSource dataSource;
	private DataSourceUtils() throws SQLException{
		try {
			Class.forName(dataSource.getDriverClassName());
			
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		conn = DriverManager.getConnection(dataSource.getUrl(),
				dataSource.getPassword(),dataSource.getUsername());
			
				
	}
	public static Connection getConnection(DataSource dSource) {
		
		try {
			if(conn == null || conn.isClosed()) {
				
				try {
					dataSource = dSource ;
					new DataSourceUtils();
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
}
