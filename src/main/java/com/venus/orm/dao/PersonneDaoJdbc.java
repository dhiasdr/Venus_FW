package com.venus.orm.dao;

import com.venus.orm.jdbc.DataSource;
import com.venus.orm.jdbc.JdbcDaoSupport;
import com.venus.orm.jdbc.JdbcTemplate;
import com.venus.test.Personne;

public class PersonneDaoJdbc extends JdbcDaoSupport implements PersonneDao {

	

	@Override
	public Personne save(Personne personne) {
		// TODO Auto-generated method stub
		int num = (int) getJdbcTemplate().update("insert into Personne(prenom,nom) values (?,?)",new String[] {personne.getNom(),personne.getPrenom()});
		personne.setId(num);
		return personne;
	}

}
