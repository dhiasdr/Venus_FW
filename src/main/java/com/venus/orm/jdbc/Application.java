package com.venus.orm.jdbc;



public class Application {

	public static void main(String[] args) {
		DataSource dataSource = new DataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int res = (Integer)jdbcTemplate.update("INSERT INTO personne (prenom, nom) VALUES (?, ?)", new String[] {"Free", "Palestine"});
	}
}
