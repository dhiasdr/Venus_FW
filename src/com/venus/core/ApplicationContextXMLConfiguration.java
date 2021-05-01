package com.venus.core;


import com.venus.core.factory.BeanFactory;
import com.venus.core.factory.FactoryCreator;
import com.venus.core.factory.IBeanFactory;


public class ApplicationContextXMLConfiguration implements ApplicationContext {
	private IBeanFactory beanFactory;
	public ApplicationContextXMLConfiguration(String configurationFileName) {
		BeansDefinitionApplication.
		configureBeansApplicationByConfiguration(configurationFileName);
		this.beanFactory= (IBeanFactory) FactoryCreator.getFactory("bean");
	}
    @Override
	public <T> T getBean(String beanName, Class<?> beanType) {
		return beanFactory.getBean(beanName, beanType);
	}
    @Override
	public void shutDown() {
		((BeanFactory)this.beanFactory).destroyBeans();
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
