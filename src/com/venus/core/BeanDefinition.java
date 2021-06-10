package com.venus.core;

import java.util.ArrayList;

public class BeanDefinition {

	private String id;
	private String className;
	private ArrayList<BeanProperty> properties;
	private ArrayList<BeanConstructorArgument> constructorArguments;
	private String scope;
	private boolean isSingleton;
	private String factoryBean;
	private String factoryMethod;
	private String initMethod;
	private String destroyMethod;
    private boolean instantiated;
    private boolean isAspect;
	public BeanDefinition() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public ArrayList<BeanProperty> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<BeanProperty> properties) {
		this.properties = properties;
	}

	public ArrayList<BeanConstructorArgument> getConstructorArguments() {
		return constructorArguments;
	}

	public void setConstructorArguments(
			ArrayList<BeanConstructorArgument> constructorArguments) {
		this.constructorArguments = constructorArguments;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public boolean isSingleton() {
		return isSingleton;
	}

	public void setSingleton(boolean isSingleton) {
		this.isSingleton = isSingleton;
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

	public boolean isInstantiated() {
		return instantiated;
	}

	public void setInstantiated(boolean instantiated) {
		this.instantiated = instantiated;
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

	public boolean isAspect() {
		return isAspect;
	}

	public void setAspect(boolean isAspect) {
		this.isAspect = isAspect;
	}

	@Override
	public String toString() {
		return "BeanDefinition [id=" + id + ", className=" + className + ", properties=" + properties
				+ ", constructorArguments=" + constructorArguments + ", scope=" + scope + ", isSingleton=" + isSingleton
				+ ", factoryBean=" + factoryBean + ", factoryMethod=" + factoryMethod + ", initMethod=" + initMethod
				+ ", destroyMethod=" + destroyMethod + ", instantiated=" + instantiated + ", isAspect=" + isAspect
				+ "]";
	}



}
