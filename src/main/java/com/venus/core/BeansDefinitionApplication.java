package com.venus.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.venus.core.behaviour.BehaviourPostProcessors;
import com.venus.exception.ProcessException;
import com.venus.exception.VenusConfigurationException;

public class BeansDefinitionApplication {
	private static String beansConfigurationFileName;
	private static ArrayList<BeanDefinition> beansDefinition;
	private static ArrayList<String> postProcessorBeansNames = new ArrayList<>();
	private static ArrayList<String> aspectBeansNames = new ArrayList<>();
	private static ArrayList<String> technicalBeansNames = new ArrayList<>();

	/**
	 * Calls the method createBeansByConfiguration() to build all the beans
	 * definition
	 * 
	 * @param configurationFileName the beans configuration file's name
	 */
	public static void createBeansApplicationByConfiguration(String configurationFileName) {
		beansConfigurationFileName = configurationFileName;
		createBeansByConfiguration();
	}

	/**
	 * Calls the method createBeansApplicationByAnnotation() to build all the beans
	 * definition
	 * 
	 */
	public static void createBeansApplicationByAnnotation() {
		createBeansByAnnotation();
	}

	private static void createBeansByAnnotation() {

		beansDefinition = BeansUtility.getBeansDefinitionWithAnnotation();
		prioritizeTechnicalBeans();

	}

	/**
	 * Returns all the beans definition for all beans configured in the file
	 * 
	 * @return list of beans definition
	 */
	public static ArrayList<BeanDefinition> getBeansDefinitionApplication() {
		if (beansDefinition == null && beansConfigurationFileName != null && !beansConfigurationFileName.isEmpty())
			createBeansByConfiguration();
		else if (beansDefinition == null)
			createBeansByAnnotation();
		return beansDefinition;
	}

	private static void createBeansByConfiguration() {
		try {
			beansDefinition = BeansUtility.parseBeanConfigurationXML(beansConfigurationFileName);
			prioritizeTechnicalBeans();
		} catch (ParserConfigurationException | SAXException | IOException | VenusConfigurationException e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * Changes the value of instantiated to true for the bean definition passed in
	 * parameter
	 * 
	 * @param beanDefinition the bean definition to be marked as instantiated
	 */
	public static void markBeanAsInstantiated(BeanDefinition beanDefinition) {
		beanDefinition.setInstantiated(true);
		int index = beansDefinition.indexOf(beanDefinition);
		beansDefinition.set(index, beanDefinition);
	}

	private static void prioritizeTechnicalBeans() {
		ArrayList<String> factoriesBeansName = new ArrayList<>();
		ArrayList<BeanDefinition> priortizedBeans = new ArrayList<>();
		for (Iterator<?> beansDefinitionItr = beansDefinition.iterator(); beansDefinitionItr.hasNext();) {
			BeanDefinition beanDefinition = (BeanDefinition) beansDefinitionItr.next();
			// treat the case of aspect bean
			if (beanDefinition.isAspect()) {
				aspectBeansNames.add(beanDefinition.getId());
				priortizedBeans.add(beanDefinition);
			}
		}
		for (Iterator<?> beansDefinitionItr = beansDefinition.iterator(); beansDefinitionItr.hasNext();) {
			BeanDefinition beanDefinition = (BeanDefinition) beansDefinitionItr.next();
			if (isPostProcessorBean(beanDefinition)) {
				postProcessorBeansNames.add(beanDefinition.getId());
				priortizedBeans.add(beanDefinition);
			}
		}
		technicalBeansNames.addAll(aspectBeansNames);
		technicalBeansNames.addAll(postProcessorBeansNames);
		for (Iterator<?> beansDefinitionItr = beansDefinition.iterator(); beansDefinitionItr.hasNext();) {
			BeanDefinition beanDefinition = (BeanDefinition) beansDefinitionItr.next();
			if (beanDefinition.getFactoryBean() != null) {
				factoriesBeansName.add(beanDefinition.getFactoryBean());
			}
		}
		for (Iterator<?> beansDefinitionItr = beansDefinition.iterator(); beansDefinitionItr.hasNext();) {
			BeanDefinition beanDefinition = (BeanDefinition) beansDefinitionItr.next();
			if (factoriesBeansName.contains(beanDefinition.getId())) {
				technicalBeansNames.add(beanDefinition.getId());
				priortizedBeans.add(beanDefinition);
			}
		}
		if (priortizedBeans.size() > 0) {
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
			throw new ProcessException(e);
		}

		return false;
	}

	/**
	 * Returns all postProcessor beans names existing in the container
	 * 
	 * @return list of all postProcessor beans
	 */
	public static ArrayList<String> getPostProcessorBeansNames() {
		return postProcessorBeansNames;
	}

	/**
	 * Returns all aspect beans names existing in the container
	 * 
	 * @return list of all aspect beans
	 */
	public static ArrayList<String> getAspectBeansNames() {
		return aspectBeansNames;
	}

	/**
	 * Returns all technical beans names existing in the container
	 * 
	 * @return list of all technical beans
	 */
	public static ArrayList<String> getTechnicalBeansNames() {
		return technicalBeansNames;
	}
}
