package com.venus.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.venus.core.builder.BeanConstructorArgumentBuilder;
import com.venus.core.builder.BeanDefinitionBuilder;
import com.venus.core.builder.BeanPropertyBuilder;
import com.venus.exception.VenusConfigurationException;

public class BeansUtility {
	private static final String BASE_XML_PATH = "src/com/venus/config/";
	private static final String XSD_PATH = "src/com/venus/config/BeansConfiguration.xsd";

	private static void ValidateBeanConfigurationXML(String fileName) throws VenusConfigurationException {
		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(XSD_PATH));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(BASE_XML_PATH+fileName)));
		} catch (IOException | SAXException e) {
			throw new VenusConfigurationException(e);
		}
	}
	/**
	 * Checks the XML configuration file and builds all the beans definition by mapping all the configured
	 * data to beans definition instances
	 * 
	 * @param fileName the beans configuration file's name
	 * @return list of all beans definition
	 */
	public static ArrayList<BeanDefinition> parseBeanConfigurationXML(String fileName) throws ParserConfigurationException, SAXException,
	IOException, VenusConfigurationException{
		ValidateBeanConfigurationXML(fileName);
        ArrayList<BeanDefinition> beansDefinition = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(BASE_XML_PATH+fileName));
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
             Node node = nodeList.item(i);
             if (node.getNodeType() == Node.ELEMENT_NODE) {
                  Element elem = (Element) node;
                  ArrayList<BeanProperty> properties= new ArrayList<>();
                  ArrayList<BeanConstructorArgument> beanConstructorArguments= new ArrayList<>();
				if (elem.getChildNodes().getLength() > 0) {
					for (int j = 0; j < elem.getChildNodes().getLength(); j++) {
						Node deepNode = elem.getChildNodes().item(j);
						if (deepNode.getNodeType() == Node.ELEMENT_NODE) {
							Element deepElem = (Element) deepNode;
							if (deepElem.getNodeName().equals("property")) {
								properties
										.add(mapElementToBeanProperty(deepElem));
							} else if (deepElem.getNodeName().equals(
									"constructor-arg")) {
								beanConstructorArguments
										.add(mapElementToBeanConstructorArgs(deepElem));
							}
						}
					}
				}
                  beansDefinition.add(mapElementToBeanDefinition(elem, properties,beanConstructorArguments));
             }
        }
        return beansDefinition;
	}
	private static BeanConstructorArgument mapElementToBeanConstructorArgs(
			Element element) {
		   BeanConstructorArgumentBuilder beanConstructorArgumentBuilder= new BeanConstructorArgumentBuilder(); 
	  	   return beanConstructorArgumentBuilder
	          	  .setType(element.getAttribute("type")!=null&&!element.getAttribute("type").isEmpty()?element.getAttribute("type"):null)
	          	  .setValue(element.getAttribute("value")!=null&&!element.getAttribute("value").isEmpty()?(element.getAttribute("value").equals("null")?null:element.getAttribute("value")):null)
	          	  .finish();
	}
	private static BeanProperty mapElementToBeanProperty(Element element){
	   BeanPropertyBuilder beanPropertyBuilder= new BeanPropertyBuilder(); 
  	   return beanPropertyBuilder.setName(element.getAttribute("name")!=null&&!element.getAttribute("name").isEmpty()?element.getAttribute("name"):null)
          	  .setRef(element.getAttribute("ref")!=null&&!element.getAttribute("ref").isEmpty()?element.getAttribute("ref"):null)
          	  .setType(element.getAttribute("type")!=null&&!element.getAttribute("type").isEmpty()?element.getAttribute("type"):null)
          	  .setValue(element.getAttribute("value")!=null&&!element.getAttribute("value").isEmpty()?(element.getAttribute("value").equals("null")?null:element.getAttribute("value")):null)
          	  .finish();
	}
	private static BeanDefinition mapElementToBeanDefinition(Element element,ArrayList<BeanProperty> properties, ArrayList<BeanConstructorArgument> beanConstructorArguments){
		BeanDefinitionBuilder beanDefinitionBuilder = new BeanDefinitionBuilder();
        return beanDefinitionBuilder
        		.setClassName(element.getAttribute("class")!=null&&!element.getAttribute("class").isEmpty()? element.getAttribute("class") : null)
				.setId(element.getAttribute("id") != null && !element.getAttribute("id").isEmpty() ? element.getAttribute("id") : null)
				.setProperties(properties)
				.setConstructorArgs(beanConstructorArguments)
				.setScope(element.getAttribute("scope") != null&& !element.getAttribute("scope").isEmpty() ? element.getAttribute("scope") : null)
				.setIsSingleton(element.getAttribute("singleton") != null&& !element.getAttribute("singleton").isEmpty() ? Boolean.parseBoolean(element.getAttribute("singleton")): true)
				.setFactoryMethod(element.getAttribute("factory-method") != null&& !element.getAttribute("factory-method").isEmpty() ? element.getAttribute("factory-method") : null)
				.setFactoryBean(element.getAttribute("factory-bean") != null&& !element.getAttribute("factory-bean").isEmpty() ? element.getAttribute("factory-bean") : null)
				.setInitMethod(element.getAttribute("init-method") != null&& !element.getAttribute("init-method").isEmpty() ? element.getAttribute("init-method") : null)
				.setDestroyMethod(element.getAttribute("destroy-method") != null&& !element.getAttribute("destroy-method").isEmpty() ? element.getAttribute("destroy-method") : null)
				.setAspect(element.getAttribute("aspect") != null&& !element.getAttribute("aspect").isEmpty() ? Boolean.parseBoolean(element.getAttribute("aspect")): false)
				.finish();
	}
}
