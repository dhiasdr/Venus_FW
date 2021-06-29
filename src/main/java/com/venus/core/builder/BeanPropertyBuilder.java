package com.venus.core.builder;

import com.venus.core.BeanProperty;

public final class BeanPropertyBuilder {
	private BeanProperty beanProperty;

	public BeanPropertyBuilder() {
		this.beanProperty = new BeanProperty();
	}

	public BeanPropertyBuilder(BeanProperty beanProperty) {
		super();
		this.beanProperty = beanProperty;
	}

	public BeanPropertyBuilder setName(String name) {
		this.beanProperty.setName(name);
		return this;
	}

	public BeanPropertyBuilder setRef(String ref) {
		this.beanProperty.setRef(ref);
		return this;

	}

	public BeanPropertyBuilder setType(String type) {
		this.beanProperty.setType(type);
		return this;

	}

	public BeanPropertyBuilder setValue(String value) {
		this.beanProperty.setValue(value);
		return this;

	}

	public BeanProperty finish() {
		return beanProperty;
	}
}
