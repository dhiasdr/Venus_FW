package com.venus.core.factory;

public interface IBeanFactory extends AbstractFactory {
   public <T> T getBean(String name, Class<?> type);
   public <T> T getBean(String name);
   public boolean beanExists(String name);
   public boolean isSingleton(String name);
}
