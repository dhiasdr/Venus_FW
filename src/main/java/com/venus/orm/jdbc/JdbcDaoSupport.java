package com.venus.orm.jdbc;

import java.sql.Connection;

public class JdbcDaoSupport {

	private JdbcTemplate jdbcTemplate;
	
	protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	public  void setDataSource(DataSource dataSource) {
		if (this.jdbcTemplate == null || dataSource != this.jdbcTemplate.getDataSource()) {
			this.jdbcTemplate = createJdbcTemplate(dataSource);
			
		}
	}
	void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	
	}
	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}
	
	
	protected final Connection getConnection() {
		DataSource dataSource = getDataSource();
		return DataSourceUtils.getConnection(dataSource);
	}
	
	public final DataSource getDataSource() {
		return (this.jdbcTemplate != null ? this.jdbcTemplate.getDataSource() : null);
	}

}
