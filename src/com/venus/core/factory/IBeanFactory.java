package com.venus.core.factory;

public interface IBeanFactory extends AbstractFactory {
	/**
	 * Returns an instance by the name of the requested bean, with the required type
	 * passed in parameter
	 * 
	 * @param name name of the requested bean
	 * @param type required type of the requested bean
	 * @return an object, the requested bean
	 */
	public <T> T getBean(String name, Class<?> type);

	/**
	 * Returns an instance by the bean's name passed in parameter
	 * 
	 * @param name name of the requested bean
	 * @return an object, the requested bean
	 */
	public <T> T getBean(String name);

	/**
	 * Checks the existing of the requested bean in the container by the bean's name
	 * passed in parameter
	 * 
	 * @param name name of the requested bean
	 * @return Boolean value reflecting the existence of the requested bean
	 */
	public boolean beanExists(String name);

	/**
	 * Checks if the scope of the bean requested by the name of the bean passed as a
	 * parameter is singleton in the container
	 * 
	 * @param name name of the requested bean
	 * @return boolean value indicating whether the requested bean is singleton
	 */
	public boolean isSingleton(String name);
}
