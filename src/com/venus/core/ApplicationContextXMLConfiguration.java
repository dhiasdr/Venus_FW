package com.venus.core;

import com.venus.core.factory.BeanFactory;
import com.venus.core.factory.FactoryCreator;
import com.venus.core.factory.IBeanFactory;

public class ApplicationContextXMLConfiguration implements ApplicationContext {
	private IBeanFactory beanFactory;

	/**
	 * Creates an instance of ApplicationContextXMLConfiguration with triggering the
	 * process of building all the beans definition and injecting the beanFactory
	 * 
	 * @param configurationFileName the beans configuration file's name
	 * @return instance of ApplicationContextXMLConfiguration
	 */
	public ApplicationContextXMLConfiguration(String configurationFileName) {
		BeansDefinitionApplication.createBeansApplicationByConfiguration(configurationFileName);
		this.beanFactory = (IBeanFactory) FactoryCreator.getFactory("bean");
	}

	@Override
	public <T> T getBean(String beanName, Class<?> beanType) {
		return beanFactory.getBean(beanName, beanType);
	}

	@Override
	public void shutDown() {
		((BeanFactory) this.beanFactory).destroyBeans();
	}

	@Override
	public <T> T getBean(String beanName) {
		return beanFactory.getBean(beanName);
	}

	@Override
	public boolean beanExists(String name) {
		return beanFactory.beanExists(name);
	}

	@Override
	public boolean isSingleton(String name) {
		return beanFactory.isSingleton(name);
	}

}
