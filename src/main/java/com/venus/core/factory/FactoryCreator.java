package com.venus.core.factory;

public class FactoryCreator {
	/**
	 * Creates and returns an instance of the factory requested by its name passed
	 * as a parameter
	 * 
	 * @param name factory name
	 * @return factory object
	 */
	public static AbstractFactory getFactory(String name) {
		if (name.equalsIgnoreCase("Bean")) {
			return new BeanFactory();
		}
		return null;
	}
}
