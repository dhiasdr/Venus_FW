package com.venus.core.builder;

import java.util.ArrayList;

import com.venus.core.BeanConstructorArgument;
import com.venus.core.BeanDefinition;
import com.venus.core.BeanProperty;

public final class BeanDefinitionBuilder {
	private BeanDefinition beanDefinition;

	public BeanDefinitionBuilder() {
		this.beanDefinition = new BeanDefinition();
	}

	public BeanDefinitionBuilder(BeanDefinition beanDefinition) {
		super();
		this.beanDefinition = beanDefinition;
	}

	public BeanDefinitionBuilder setId(String id) {
		this.beanDefinition.setId(id);
		return this;
	}

	public BeanDefinitionBuilder setClassName(String className) {
		this.beanDefinition.setClassName(className);
		return this;

	}

	public BeanDefinitionBuilder setScope(String scope) {
		this.beanDefinition.setScope(scope);
		return this;

	}

	public BeanDefinitionBuilder setProperties(
			ArrayList<BeanProperty> properties) {
		this.beanDefinition.setProperties(properties);
		return this;

	}
	public BeanDefinitionBuilder setConstructorArgs(
			ArrayList<BeanConstructorArgument> constructorArguments) {
		this.beanDefinition.setConstructorArguments(constructorArguments);
		return this;

	}
	public BeanDefinitionBuilder setIsSingleton(boolean isSingleton) {
		this.beanDefinition.setSingleton(isSingleton);
		return this;

	}

	public BeanDefinitionBuilder setFactoryMethod(String factoryMethod) {
		this.beanDefinition.setFactoryMethod(factoryMethod);
		return this;

	}

	public BeanDefinitionBuilder setFactoryBean(String factoryBean) {
		this.beanDefinition.setFactoryBean(factoryBean);
		return this;

	}
	public BeanDefinitionBuilder setInitMethod(String initMethod) {
		this.beanDefinition.setInitMethod(initMethod);
		return this;

	}
	public BeanDefinitionBuilder setDestroyMethod(String destroyMethod) {
		this.beanDefinition.setDestroyMethod(destroyMethod);
		return this;

	}
	public BeanDefinitionBuilder setAspect(boolean isAspect) {
		this.beanDefinition.setAspect(isAspect);
		return this;

	}
	public BeanDefinition finish() {
		return beanDefinition;

	}

}
