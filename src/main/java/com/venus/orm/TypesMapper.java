package com.venus.orm;

public class TypesMapper {
	
	public static String getSqlType(String javaType){
		
		switch(javaType){
		case "java.lang.String": return "VARCHAR(255)";
		case "int": return "INTEGER";
		case "double":return "Float";
		case "java.util.Date": 
			 return "DOUBLE";
		case "float":return "REAL";
		default: return "VARCHAR(255)";
		}
	}


}
