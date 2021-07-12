package com.venus.orm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.venus.orm.jdbc.DataSource;
import com.venus.orm.jdbc.JdbcTemplate;

public class Orm {

	public static void generateAndExecuteTableCreationScript() {
		StringBuilder querries = new StringBuilder();
		ArrayList<Class<?>> listofEntity = new ArrayList<Class<?>>();
		ArrayList<EntityDescription> entities = new ArrayList<EntityDescription>();
		listofEntity = OrmUtils.listOfClassesWithAnotationEntity();
		entities = EntityMapper.listOfEntity(listofEntity);

		for (EntityDescription pn : entities) {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(new DataSource());

			if (!jdbcTemplate.isTableExist(pn.getName())) {

				HashMap<String, FieldDescription> columns = new HashMap<String, FieldDescription>();
				FieldDescription field = new FieldDescription();
				ArrayList<String> uniqueList = new ArrayList<String>();
				StringBuilder query = new StringBuilder("CREATE TABLE ");
				String pkName = null;

				query.append(pn.getName());
				query.append("(");
				columns = pn.getColumns();
				Iterator it = columns.entrySet().iterator();
				while (it.hasNext()) {

					Map.Entry pair = (Map.Entry) it.next();

					query.append(pair.getKey());
					field = (FieldDescription) pair.getValue();
					query.append(" " + TypesMapper.getSqlType(field.getType()));
					if (field.isNotNull())
						query.append(" NOT NULL ");

					if (field.isUnique()) {
						uniqueList.add((String) pair.getKey());
					}
					if (field.isPrimaryKey() && field.isAutoIncrement()) {

						query.append(" AUTO_INCREMENT ");

						pkName = (String) pair.getKey();

					}
					query.append(",");

				}
				String uniqueFieldName = null;
				Iterator iter = uniqueList.iterator();

				while (iter.hasNext()) {
					uniqueFieldName = (String) iter.next();
					query.append(" CONSTRAINT ");
					query.append(pn.getName());
					query.append("_");
					query.append(uniqueFieldName.toString());

					query.append(" UNIQUE (");

					query.append(uniqueFieldName.toString());
					query.append("),");

				}
				if (pkName != null) {
					query.append(" PRIMARY KEY (");
					query.append(pkName);
					query.append("),");
				}
				query.replace(query.length() - 1, query.length(), " )");
				querries.append(query.toString().toUpperCase() + ";");

			}
		}
		if (querries != null && !querries.toString().isEmpty()) {
			System.out.println(querries.toString());
			InitTable.initTablesCreation(querries.toString());
		}
	}

}
