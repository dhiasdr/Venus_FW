package com.venus.core.factory;

public class FactoryCreator {
   public static AbstractFactory getFactory(String name){
	   if(name.equalsIgnoreCase("Bean")){
		   return new BeanFactory();
	   }
	return null;
   }
}
