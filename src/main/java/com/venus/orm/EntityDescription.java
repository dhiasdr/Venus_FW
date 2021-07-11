package com.venus.orm;

import java.util.HashMap;

public class EntityDescription {
	
	private String Name;
	private HashMap<String, FieldDescription> Columns;
	public EntityDescription() {
		
	}
	public EntityDescription(String name, HashMap<String, FieldDescription> columns) {
		
		Name = name;
		Columns = columns;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public HashMap<String, FieldDescription> getColumns() {
		return Columns;
	}
	public void setColumns(HashMap<String, FieldDescription> columns) {
		Columns = columns;
	}
	@Override
	public String toString() {
		return "EntityDescription [Name=" + Name + ", Columns=" + Columns + "]";
	}
	
}
