package com.venus.orm;



public class FieldDescription{
private String type;
private boolean notNull;
private boolean unique;
private Object value;
private boolean primaryKey;
private boolean autoIncrement;
public FieldDescription() {
	
}
public FieldDescription(String type, boolean notNull, boolean unique,
		boolean primaryKey, String mappingMode, String defaultValue,
		boolean autoIncrement, boolean isIndex,
		String referencedTableName, String referencedFieldName, Object value) {

	this.type = type;
	this.notNull = notNull;
    this.primaryKey = primaryKey;
	this.value = value;
	this.unique = unique;
	this.autoIncrement = autoIncrement;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public boolean isNotNull() {
	return notNull;
}
public void setNotNull(boolean notNull) {
	this.notNull = notNull;
}





public boolean isAutoIncrement() {
	return autoIncrement;
}
public void setAutoIncrement(boolean autoIncrement) {
	this.autoIncrement = autoIncrement;
}
public boolean isPrimaryKey() {
	return primaryKey;
}
public void setPrimaryKey(boolean primaryKey) {
	this.primaryKey = primaryKey;
}
public boolean isUnique() {
	return unique;
}
public void setUnique(boolean unique) {
	this.unique = unique;
}
public Object getValue() {
	return value;
}
public void setValue(Object value) {
	this.value = value;
}
@Override
public String toString() {
	return "FieldDescription [type=" + type + ", notNull=" + notNull
			+ ", value="
			+ value + "]";
}



}
