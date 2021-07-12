package com.venus.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.venus.core.annotation.Aspect;
import com.venus.core.annotation.Autowired;
import com.venus.core.annotation.Bean;
import com.venus.core.annotation.Component;
import com.venus.core.annotation.Controller;
import com.venus.core.annotation.Service;
import com.venus.core.behaviour.BehaviourPostProcessors;
import com.venus.core.builder.AnnotationValuesBuilder;
import com.venus.core.builder.BeanConstructorArgumentBuilder;
import com.venus.core.builder.BeanDefinitionBuilder;
import com.venus.core.builder.BeanPropertyBuilder;
import com.venus.exception.VenusConfigurationException;

public class BeansUtility {
	private static final String BASE_XML_PATH = "src/main/resources/";
	private static final String XSD_PATH = "src/main/resources/BeansConfiguration.xsd";
	private static final String PROTOYPE_SCOPE = "prototype";
	private static final String SINGLETON_SCOPE = "singleton";
	private static final String PROJECT_RACINE = "src/main/java/";

	private static void ValidateBeanConfigurationXML(String fileName) throws VenusConfigurationException {
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(XSD_PATH));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(BASE_XML_PATH + fileName)));
		} catch (IOException | SAXException e) {
			throw new VenusConfigurationException(e);
		}
	}

	/**
	 * Checks the XML configuration file and builds all the beans definition by
	 * mapping all the configured data to beans definition instances
	 * 
	 * @param fileName the beans configuration file's name
	 * @return list of all beans definition
	 */
	public static ArrayList<BeanDefinition> parseBeanConfigurationXML(String fileName)
			throws ParserConfigurationException, SAXException, IOException, VenusConfigurationException {
		ValidateBeanConfigurationXML(fileName);
		ArrayList<BeanDefinition> beansDefinition = new ArrayList<>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(BASE_XML_PATH + fileName));
		NodeList nodeList = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				ArrayList<BeanProperty> properties = new ArrayList<>();
				ArrayList<BeanConstructorArgument> beanConstructorArguments = new ArrayList<>();
				if (elem.getChildNodes().getLength() > 0) {
					for (int j = 0; j < elem.getChildNodes().getLength(); j++) {
						Node deepNode = elem.getChildNodes().item(j);
						if (deepNode.getNodeType() == Node.ELEMENT_NODE) {
							Element deepElem = (Element) deepNode;
							if (deepElem.getNodeName().equals("property")) {
								properties.add(mapElementToBeanProperty(deepElem));
							} else if (deepElem.getNodeName().equals("constructor-arg")) {
								beanConstructorArguments.add(mapElementToBeanConstructorArgs(deepElem));
							}
						}
					}
				}
				beansDefinition.add(mapElementToBeanDefinition(elem, properties, beanConstructorArguments));
			}
		}
		return beansDefinition;
	}

	private static BeanConstructorArgument mapElementToBeanConstructorArgs(Element element) {
		BeanConstructorArgumentBuilder beanConstructorArgumentBuilder = new BeanConstructorArgumentBuilder();
		return beanConstructorArgumentBuilder
				.setType(element.getAttribute("type") != null && !element.getAttribute("type").isEmpty()
						? element.getAttribute("type")
						: null)
				.setValue(element.getAttribute("value") != null && !element.getAttribute("value").isEmpty()
						? (element.getAttribute("value").equals("null") ? null : element.getAttribute("value"))
						: null)
				.finish();
	}

	private static BeanProperty mapElementToBeanProperty(Element element) {
		BeanPropertyBuilder beanPropertyBuilder = new BeanPropertyBuilder();
		return beanPropertyBuilder
				.setName(element.getAttribute("name") != null && !element.getAttribute("name").isEmpty()
						? element.getAttribute("name")
						: null)
				.setRef(element.getAttribute("ref") != null && !element.getAttribute("ref").isEmpty()
						? element.getAttribute("ref")
						: null)
				.setType(element.getAttribute("type") != null && !element.getAttribute("type").isEmpty()
						? element.getAttribute("type")
						: null)
				.setValue(element.getAttribute("value") != null && !element.getAttribute("value").isEmpty()
						? (element.getAttribute("value").equals("null") ? null : element.getAttribute("value"))
						: null)
				.finish();
	}

	private static BeanDefinition mapElementToBeanDefinition(Element element, ArrayList<BeanProperty> properties,
			ArrayList<BeanConstructorArgument> beanConstructorArguments) {
		BeanDefinitionBuilder beanDefinitionBuilder = new BeanDefinitionBuilder();
		return beanDefinitionBuilder
				.setClassName(element.getAttribute("class") != null && !element.getAttribute("class").isEmpty()
						? element.getAttribute("class")
						: null)
				.setId(element.getAttribute("id") != null && !element.getAttribute("id").isEmpty()
						? element.getAttribute("id")
						: null)
				.setProperties(properties).setConstructorArgs(beanConstructorArguments)
				.setScope(element.getAttribute("scope") != null && !element.getAttribute("scope").isEmpty()
						? element.getAttribute("scope")
						: null)
				.setIsSingleton(
						element.getAttribute("singleton") != null && !element.getAttribute("singleton").isEmpty()
								? Boolean.parseBoolean(element.getAttribute("singleton"))
								: true)
				.setFactoryMethod(element.getAttribute("factory-method") != null
						&& !element.getAttribute("factory-method").isEmpty() ? element.getAttribute("factory-method")
								: null)
				.setFactoryBean(
						element.getAttribute("factory-bean") != null && !element.getAttribute("factory-bean").isEmpty()
								? element.getAttribute("factory-bean")
								: null)
				.setInitMethod(
						element.getAttribute("init-method") != null && !element.getAttribute("init-method").isEmpty()
								? element.getAttribute("init-method")
								: null)
				.setDestroyMethod(element.getAttribute("destroy-method") != null
						&& !element.getAttribute("destroy-method").isEmpty() ? element.getAttribute("destroy-method")
								: null)
				.setAspect(element.getAttribute("aspect") != null && !element.getAttribute("aspect").isEmpty()
						? Boolean.parseBoolean(element.getAttribute("aspect"))
						: false)
				.finish();
	}

	public static void allProjectPackages(String directoryName, Set<String> packs) {
		File directory = new File(directoryName);
		File[] filesList = directory.listFiles();
		for (File file : filesList) {
			if (file.isFile()) {
				String path = file.getPath();
				String packName = path.substring(path.indexOf("src") + 14, path.lastIndexOf('\\'));
				packs.add(packName.replace('\\', '.'));
			} else if (file.isDirectory()) {
				allProjectPackages(file.getAbsolutePath(), packs);
			}
		}

	}

	public static ArrayList<BeanDefinition> getBeansDefinitionWithAnnotation() {
		Set<Class<? extends Object>> classes = new HashSet<>();
		Set<String> packagesNames = new HashSet<>();
		allProjectPackages(PROJECT_RACINE, packagesNames);
		ArrayList<BeanDefinition> beanDefinitionList = new ArrayList<>();
		for (String packageName : packagesNames) {

			Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
			classes.addAll(reflections.getSubTypesOf(Object.class));
		}
		for (Class classs : classes) {
			if (classs.isAnnotationPresent(Bean.class) || classs.isAnnotationPresent(Service.class)
					|| classs.isAnnotationPresent(Controller.class) || classs.isAnnotationPresent(Component.class)
					|| classs.isAnnotationPresent(Aspect.class)
					|| Arrays.asList(classs.getInterfaces()).contains(BehaviourPostProcessors.class)) {

				BeanDefinition beanDefinition = new BeanDefinition();
				String className = classs.getSimpleName();
				beanDefinition.setId(className.substring(0, 1).toLowerCase() + className.substring(1));
				beanDefinition.setClassName(classs.getName());
				AnnotationValuesBuilder builder = new AnnotationValuesBuilder();
				AnnotationValues annotationValues;
				if (classs.isAnnotationPresent(Bean.class)) {
					Bean bean = (Bean) classs.getAnnotation(Bean.class);
					builder.setSingleton(bean.isSingleton())
					.setScope(bean.scope())
					.setDestroyMethod(bean.destroyMethod())
					.setInitMethod(bean.initMethod())
					.setFactoryMethod(bean.factoryMethod())
					.setFactoryBean(bean.factoryBean());

				} else if (classs.isAnnotationPresent(Service.class)) {
					Service service = (Service) classs.getAnnotation(Service.class);
					builder.setSingleton(service.isSingleton())
					.setScope(service.scope())
					.setDestroyMethod(service.destroyMethod())
					.setInitMethod(service.initMethod())
					.setFactoryMethod(service.factoryMethod())
					.setFactoryBean(service.factoryBean());
				} else if (classs.isAnnotationPresent(Controller.class)) {
					Controller controller = (Controller) classs.getAnnotation(Controller.class);
					builder.setSingleton(controller.isSingleton())
					.setScope(controller.scope())
					.setDestroyMethod(controller.destroyMethod())
					.setInitMethod(controller.initMethod())
					.setFactoryMethod(controller.factoryMethod())
					.setFactoryBean(controller.factoryBean());
				} else if (classs.isAnnotationPresent(Aspect.class)
						|| Arrays.asList(classs.getInterfaces()).contains(BehaviourPostProcessors.class)) {
					builder.setScope(SINGLETON_SCOPE)
					.setSingleton(true);
				} else {
					Component component = (Component) classs.getAnnotation(Component.class);
					builder.setSingleton(component.isSingleton())
					.setScope(component.scope())
					.setDestroyMethod(component.destroyMethod())
					.setInitMethod(component.initMethod())
					.setFactoryMethod(component.factoryMethod())
					.setFactoryBean(component.factoryBean());
				}
				annotationValues = builder.finish();
				if (annotationValues.isSingleton()) {
					beanDefinition.setSingleton(true);
					beanDefinition.setScope(SINGLETON_SCOPE);
				} else {
					beanDefinition.setSingleton(false);
					beanDefinition.setScope(PROTOYPE_SCOPE);
				}
				if (annotationValues.getScope() != null && !annotationValues.isSingleton()) {
					beanDefinition.setSingleton(false);
					beanDefinition.setScope(PROTOYPE_SCOPE);
				} else {
					beanDefinition.setSingleton(true);
					beanDefinition.setScope(SINGLETON_SCOPE);
				}

				beanDefinition.setFactoryBean(
						annotationValues.getFactoryBean() != null && !annotationValues.getFactoryBean().isEmpty()
								? annotationValues.getFactoryBean()
								: null);
				beanDefinition.setFactoryMethod(
						annotationValues.getFactoryMethod() != null && !annotationValues.getFactoryMethod().isEmpty()
								? annotationValues.getFactoryMethod()
								: null);
				beanDefinition.setInitMethod(
						annotationValues.getInitMethod() != null && !annotationValues.getInitMethod().isEmpty()
								? annotationValues.getInitMethod()
								: null);
				beanDefinition.setDestroyMethod(
						annotationValues.getDestroyMethod() != null && !annotationValues.getDestroyMethod().isEmpty()
								? annotationValues.getDestroyMethod()
								: null);

				ArrayList<BeanProperty> beanProperties = new ArrayList<>();
				Field[] fields = classs.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					if ((fields[i].getType().isPrimitive())) {
						BeanProperty beanProperty = new BeanProperty();
						beanProperty.setName(fields[i].getName());
						beanProperty.setType(fields[i].getType().toString());
						beanProperties.add(beanProperty);
					} else {
						Class<?>[] complexTypesCls = { String.class, Boolean.class, Double.class, Float.class,
								Character.class, Long.class, Integer.class, Short.class, Byte.class };

						if (Arrays.asList(complexTypesCls).contains(fields[i].getType())) {
							BeanProperty beanProperty = new BeanProperty();
							beanProperty.setName(fields[i].getName());
							beanProperty.setType(fields[i].getType().toString().substring(6));
							beanProperties.add(beanProperty);
						} else {
							String methodName = "set" + fields[i].getName().substring(0, 1).toUpperCase()
									+ fields[i].getName().substring(1);
							for (Method m : classs.getDeclaredMethods()) {
								if (m.getName().equals(methodName) && m.isAnnotationPresent(Autowired.class)) {
									BeanProperty beanProperty = new BeanProperty();
									beanProperty.setName(fields[i].getName());
									beanProperty.setRef(fields[i].getName());
									beanProperties.add(beanProperty);
									break;
								}
							}
						}
					}

				}
				beanDefinition.setProperties(beanProperties);
				if (classs.isAnnotationPresent(Aspect.class))
					beanDefinition.setAspect(true);
				beanDefinitionList.add(beanDefinition);
			}
		}
		return beanDefinitionList;
	}
}
