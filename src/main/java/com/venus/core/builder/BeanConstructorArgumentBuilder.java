package com.venus.core.builder;

import com.venus.core.BeanConstructorArgument;

public class BeanConstructorArgumentBuilder {
	private BeanConstructorArgument beanConstructorArgument;

	public BeanConstructorArgumentBuilder() {
		beanConstructorArgument = new BeanConstructorArgument();
	}

	public BeanConstructorArgumentBuilder setType(String type) {
		this.beanConstructorArgument.setType(type);
		return this;
	}

	public BeanConstructorArgumentBuilder setValue(String value) {
		this.beanConstructorArgument.setValue(value);
		return this;
	}
	
	public BeanConstructorArgument finish(){
		return beanConstructorArgument;
	}
}
