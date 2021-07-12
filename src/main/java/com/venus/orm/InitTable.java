package com.venus.orm;

import com.venus.orm.jdbc.DataSource;
import com.venus.orm.jdbc.JdbcTemplate;

public class InitTable {

	public static void initTablesCreation(String Querries) {
		JdbcTemplate jdbc = new JdbcTemplate(new DataSource());
		jdbc.executeTablesCreationScript(Querries);
	}
}
