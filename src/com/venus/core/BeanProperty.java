package com.venus.core;

public class BeanProperty {
	private String name;
	private String ref;
	private String type;
	private String value;
	public BeanProperty(){}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "BeanProperty [name=" + name + ", ref=" + ref + ", type=" + type
				+ ", value=" + value + "]";
	}
	
}
