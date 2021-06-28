package com.venus.core;

public class AnnotationValues {
	private boolean isSingleton;
	private String scope;
	private String factoryBean;
	private String factoryMethod;
	private String initMethod;
	private String destroyMethod;
	public boolean isSingleton() {
		return isSingleton;
	}
	public void setSingleton(boolean isSingleton) {
		this.isSingleton = isSingleton;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getFactoryBean() {
		return factoryBean;
	}
	public void setFactoryBean(String factoryBean) {
		this.factoryBean = factoryBean;
	}
	public String getFactoryMethod() {
		return factoryMethod;
	}
	public void setFactoryMethod(String factoryMethod) {
		this.factoryMethod = factoryMethod;
	}
	public String getInitMethod() {
		return initMethod;
	}
	public void setInitMethod(String initMethod) {
		this.initMethod = initMethod;
	}
	public String getDestroyMethod() {
		return destroyMethod;
	}
	public void setDestroyMethod(String destroyMethod) {
		this.destroyMethod = destroyMethod;
	}
	
}
