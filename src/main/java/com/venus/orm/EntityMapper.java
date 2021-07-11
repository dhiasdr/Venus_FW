package com.venus.orm;

import java.util.ArrayList;
import java.util.HashMap;

import com.venus.orm.annotation.Table;
import com.venus.orm.exception.FieldsMapperException;

public class EntityMapper {
	
	
	public static EntityDescription insertIntoEntity(Class<?> cls, HashMap<String, FieldDescription> f) {
		
		EntityDescription ed = new EntityDescription(); 
		ed.setColumns(f);
		if(cls.isAnnotationPresent(Table.class) && !cls.getAnnotation(Table.class).name().isEmpty())
			ed.setName(cls.getAnnotation(Table.class).name());
		else
			ed.setName(cls.getSimpleName());


		return ed;
		
		
	}
	
	
public static  ArrayList<EntityDescription> listOfEntity(ArrayList<Class<?>> list) {
		
		ArrayList<EntityDescription> Entities = new ArrayList<EntityDescription>();
		HashMap<String, FieldDescription> Fields = new HashMap<String, FieldDescription>();
   	 for (Class<?> cls : list) {
 		EntityDescription ed = null;
   		try {
			Fields = FieldsMapper.insertintoFieldDescription(cls);
		} catch (FieldsMapperException e) {
			e.printStackTrace();
		}
   			
   		ed = EntityMapper.insertIntoEntity(cls,Fields);
   			Entities.add(ed);
   	 }
		return Entities;
		
		
	}

}
