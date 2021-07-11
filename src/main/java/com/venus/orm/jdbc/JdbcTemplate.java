package com.venus.orm.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class JdbcTemplate {
	
	private DataSource dataSource;
	private Connection con = null;
	private boolean rollback = false;

	public JdbcTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	/* methodeTemplate */
	public Object update(String sql, String[] args) {
		Object result = null;
		openConexion();
		beginTransaction ();
		result = execute(sql, args);
		endTransaction();
		closeConnection();
		return result;
	}
    public boolean isTableExist(String tableName) {
    
        DatabaseMetaData meta;
        boolean exist = false;
        openConexion();
		beginTransaction ();
		try {
            ResultSet res = null;
            meta = con.getMetaData();
            res = meta.getTables(null, null, tableName.toUpperCase(), new String[] { "TABLE" });
            while (res.next()) {
                exist = true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }		
		endTransaction();
		closeConnection();
        

 

        return exist;
    }
	
	private void openConexion() {
		con = DataSourceUtils.getConnection(this.dataSource);
	}
	
	private void beginTransaction () {
		try {
			con.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	private Object execute(String sql, String[] args) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        int id = 0;
        try {      	 	
        	pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        	for (int i=0 ; i< args.length; i++)
        	   pstmt.setString((i+1), args[i]);
        	
        	pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next())
            	id = rs.getInt(1);   
        }catch (SQLException ex) {
        	rollback = true;
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
                if(pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return id;
	}
	
	private void endTransaction() {
		if (rollback) {
			try { // rollback the transaction
				if (con != null)
					con.rollback();
				return;
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		try {
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

	}
	private void closeConnection() {
		if(con != null)
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	  public boolean executeTablesCreationScript(String query) {
	        int result = 0;
	        PreparedStatement preparedQuery = null;
	        openConexion();
			beginTransaction ();
	        try {

	            preparedQuery = con.prepareStatement(query.toString());
	            result = preparedQuery.executeUpdate();
	        } catch (SQLException e) {
	            System.out.println(e);
	        }
	        endTransaction();
			closeConnection();
	        return result > 0;

	    }
	
	
}
