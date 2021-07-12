package com.venus.orm;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.venus.orm.annotation.Column;
import com.venus.orm.annotation.Entity;
import com.venus.orm.annotation.GeneratedValue;
import com.venus.orm.annotation.Id;
import com.venus.orm.annotation.Table;
import com.venus.orm.enum_.GenerationType;
import com.venus.orm.exception.FieldsMapperException;

import java.sql.Timestamp;

public class FieldsMapper {

	public FieldsMapper() {

	}

	public static HashMap<String, FieldDescription> insertintoFieldDescription(Class<?> cls)
			throws FieldsMapperException {
		HashMap<String, FieldDescription> Fields = new HashMap<String, FieldDescription>();

		Field[] listAttributs = cls.getDeclaredFields();
		for (Field f : listAttributs) {
			FieldDescription fieldDescription = new FieldDescription();

			f.setAccessible(true);
			String fieldType = f.getType().getName();
			String fieldName = f.getName();
			fieldDescription.setType(fieldType);
			if (f.isAnnotationPresent(Id.class)) {
				if (f.isAnnotationPresent(GeneratedValue.class)
						&& f.getAnnotation(GeneratedValue.class).strategy() != null
						&& f.getAnnotation(GeneratedValue.class).strategy().equals(GenerationType.IDENTITY)) {
					fieldDescription.setPrimaryKey(true);
					fieldDescription.setAutoIncrement(true);

				}

			}

			if (f.getAnnotation(Column.class) != null) {
				if (f.getAnnotation(Column.class).notNull())
					fieldDescription.setNotNull(true);
				if (f.getAnnotation(Column.class).name() != null && !f.getAnnotation(Column.class).name().isEmpty())
					fieldName = f.getAnnotation(Column.class).name();
				if (f.getAnnotation(Column.class).unique())
					fieldDescription.setUnique(true);
			}

			Fields.put(fieldName, fieldDescription);

		}

		return Fields;

	}

}
