package com.venus.core.behaviour;

public interface BehaviourAware {
	/**
	 * Defines the name of the bean passed as a parameter after instantiating the
	 * bean and defining its properties
	 * 
	 * @param beanName bean's name
	 */
	public void setBeanName(String beanName);
}
