package com.venus.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.venus.core.behaviour.BehaviourPostProcessors;
import com.venus.exception.VenusConfigurationException;

public class BeansDefinitionApplication {
	private static String beansConfigurationFileName;
	private static ArrayList<BeanDefinition> beansDefinition;
	private static String postProcessorBeanName;
	public static void configureBeansApplicationByConfiguration(String configurationFileName){
		beansConfigurationFileName =configurationFileName;
		configureBeans();
	}
	public static ArrayList<BeanDefinition> getBeansDefinitionApplication(){
		//for now
		if(beansDefinition==null)configureBeans();
		return beansDefinition;
	}
	private static void configureBeans(){
		try {
			beansDefinition = BeansUtility
					.parseBeanConfigurationXML(beansConfigurationFileName);
			prioritizeFactoriesBeans();
		} catch (ParserConfigurationException | SAXException | IOException
				| VenusConfigurationException e) {
			e.printStackTrace();
		}
	}
	public static void markBeanAsInstanciated(BeanDefinition beanDefinition){
		beanDefinition.setInstanciated(true);
		int index = beansDefinition.indexOf(beanDefinition);
		beansDefinition.set(index, beanDefinition);
	}
	private static void prioritizeFactoriesBeans(){
		ArrayList<String> priortizedBeansName = new ArrayList<>();
		ArrayList<BeanDefinition> priortizedBeans = new ArrayList<>();
		for(Iterator<?> beansDefinitionItr= beansDefinition.iterator(); beansDefinitionItr.hasNext();){
			BeanDefinition beanDefinition = (BeanDefinition) beansDefinitionItr.next();
			if(isPostProcessorBean(beanDefinition)) {
				postProcessorBeanName=beanDefinition.getId();
				priortizedBeans.add(beanDefinition);
				break;
			}
		}
		for(Iterator<?> beansDefinitionItr= beansDefinition.iterator(); beansDefinitionItr.hasNext();){
			BeanDefinition beanDefinition = (BeanDefinition) beansDefinitionItr.next();
			if(beanDefinition.getFactoryBean()!=null){
				priortizedBeansName.add(beanDefinition.getFactoryBean());
			}
		}
		for(Iterator<?> beansDefinitionItr= beansDefinition.iterator(); beansDefinitionItr.hasNext();){
			BeanDefinition beanDefinition = (BeanDefinition) beansDefinitionItr.next();
			if(priortizedBeansName.contains(beanDefinition.getId())){
				priortizedBeans.add(beanDefinition);
			}
		}
		if(priortizedBeans.size()>0) {
			beansDefinition.removeAll(priortizedBeans);
			priortizedBeans.addAll(beansDefinition);
			beansDefinition = priortizedBeans;
		}
		
	}

	private static boolean isPostProcessorBean(BeanDefinition beanDef) {
		try {
			if (Arrays.asList((Class.forName(beanDef.getClassName())).getInterfaces())
					.contains(BehaviourPostProcessors.class)) {
				return true;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}
	public static String getPostProcessorBeanName() {
		return postProcessorBeanName;
	}
}
