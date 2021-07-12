package com.venus.core.factory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import com.venus.aop.AOPUtility;
import com.venus.aop.AspectActivator;
import com.venus.core.BeanConstructorArgument;
import com.venus.core.BeanDefinition;
import com.venus.core.BeanProperty;
import com.venus.core.BeansDefinitionApplication;
import com.venus.core.BehaviourMethodsInvoker;
import com.venus.core.behaviour.BehaviourDestroy;
import com.venus.exception.VenusBeanConfigurationNotFound;
import com.venus.exception.VenusPropertyNotFound;

public class BeanFactory implements IBeanFactory {
	private final String PROTOYPE_SCOPE = "prototype";
	private final String DEFAULT_TYPE = "java.lang.String";
	private HashMap<String, Object> beans = new HashMap<>();

	public BeanFactory() {
		beansInstantiationResult();
	}

	@Override
	public <T> T getBean(String name, Class<?> type) {
		if (beans != null && beans.size() > 0) {
			checkBeanForPrototypeScope(name);
		}
		Object bean = this.beans.get(name);
		return (T) type.cast(bean);
	}

	@Override
	public <T> T getBean(String name) {
		if (beans != null && beans.size() > 0) {
			checkBeanForPrototypeScope(name);
		}
		Object bean = this.beans.get(name);
		return (T) bean;
	}

	@Override
	public boolean beanExists(String name) {
		return this.beans.containsKey(name);
	}

	@Override
	public boolean isSingleton(String name) {
		for (Iterator<?> beansIterator = BeansDefinitionApplication.getBeansDefinitionApplication()
				.iterator(); beansIterator.hasNext();) {
			BeanDefinition beanDefinition = (BeanDefinition) beansIterator.next();
			if (beanDefinition.getId().equals(name) && beanDefinition.isSingleton()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Iterates all the beans definition, creates all the beans and places them all
	 * in the container which's represented by a hashMap named beans
	 */
	private void beansInstantiationResult() {
		Object bean = null;
		for (Iterator<?> beansIterator = BeansDefinitionApplication.getBeansDefinitionApplication()
				.iterator(); beansIterator.hasNext();) {
			BeanDefinition beanDefinition = (BeanDefinition) beansIterator.next();
			if (!beanDefinition.isInstantiated()) {
				bean = instanciateObject(beanDefinition.getClassName(), beanDefinition,null);
				setBeanProperties(bean, beanDefinition);
				BeansDefinitionApplication.markBeanAsInstantiated(beanDefinition);
				//Activate Aspect
				this.beans.put(beanDefinition.getId(), 
						processAspectTreatmentFor(bean, beanDefinition));
			}
		}
		for (Iterator<?> beansIterator = BeansDefinitionApplication.getBeansDefinitionApplication()
				.iterator(); beansIterator.hasNext();) {
			BeanDefinition beanDefinition = (BeanDefinition) beansIterator.next();
			beanDefinition.setInstantiated(false);
		}
	}

	/**
	 * On each request to obtain a bean by the method named getBean (), this method
	 * is called to check if the scope of this object is prototype. If so, another
	 * new instance should be created, injected as a dependency into the associated
	 * beans and placed in the container.
	 * 
	 * @param name name of the requested bean
	 * @return instance of the requested bean
	 */
	private Object checkBeanForPrototypeScope(String prototypeBeanName) {
		Object bean = null;
		BeanDefinition relatedBeanDefinition = null;
		for (Iterator<?> beansIterator = BeansDefinitionApplication.getBeansDefinitionApplication()
				.iterator(); beansIterator.hasNext();) {
			relatedBeanDefinition = (BeanDefinition) beansIterator.next();

			if (relatedBeanDefinition.getId().equals(prototypeBeanName)) {
				if ((relatedBeanDefinition.getScope() != null
						&& relatedBeanDefinition.getScope().equals(PROTOYPE_SCOPE))
						|| !relatedBeanDefinition.isSingleton()) {
					bean = instanciateObject(relatedBeanDefinition.getClassName(), relatedBeanDefinition, null);
                    //
					BeansDefinitionApplication.markBeanAsInstantiated(relatedBeanDefinition);
					//
					setBeanPropertiesForProtoype(bean, relatedBeanDefinition);
					//Activate Aspect
					this.beans.put(prototypeBeanName, 
							processAspectTreatmentFor(bean, relatedBeanDefinition));
				}
				break;
			}
		}
        return bean;

	}

	/**
	 * Sets bean's properties with the scope // prototype //
	 * 
	 * @param bean    an instance of bean
	 * @param beanDef the related bean definition
	 */
	private void setBeanPropertiesForProtoype(Object bean, BeanDefinition beanDef) {
		for (Iterator<?> beanPropertiesIt = beanDef.getProperties().iterator(); beanPropertiesIt.hasNext();) {
			BeanProperty beanProperty = (BeanProperty) beanPropertiesIt.next();
				try {
				if (beanProperty.getRef() != null) {
					if (findBeanDefinitionByPropertyReference(beanProperty.getRef()) != null
							&& (!findBeanDefinitionByPropertyReference(beanProperty.getRef()).isSingleton()
									|| (findBeanDefinitionByPropertyReference(beanProperty.getRef()).getScope() != null
											&& findBeanDefinitionByPropertyReference(beanProperty.getRef()).getScope()
													.equals(PROTOYPE_SCOPE)))) {
						if(findBeanDefinitionByPropertyReference(beanProperty.getRef()).isInstantiated()) {
							Object referencedBean = this.beans.get(beanProperty.getRef());
							setProperty(bean, referencedBean, beanProperty.getName());
						}
						else {
						Object referencedBean = checkBeanForPrototypeScope(beanProperty.getRef());
						setProperty(bean, referencedBean, beanProperty.getName()); 
						}
					}
					Object referencedBean = this.beans.get(beanProperty.getRef());
					setProperty(bean, referencedBean, beanProperty.getName());
					}
				} catch (VenusPropertyNotFound e) {
					e.printStackTrace();
				}
			
			setUpSimpleProperties(bean, beanProperty);
		}
		// BehaviourMethodsInvoker.invokeFor() should not be called for the
		// postProcessor beans
		if (!BeansDefinitionApplication.getPostProcessorBeansNames().contains(beanDef.getId())
				&& !BeansDefinitionApplication.getAspectBeansNames().contains(beanDef.getId())) {
			BehaviourMethodsInvoker.invokeFor(bean, beanDef.getId(), getExistingPostProcessorBeans(),
					beanDef.getInitMethod());
		}
	}

	/**
	 * Sets all beans properties when building the instantiation result, and calls
	 * the method invokeFor() of BehaviourMethodsInvoker to run each bean's life
	 * cycle
	 * 
	 * @param bean    an instance of bean
	 * @param beanDef the related bean definition
	 */
	private void setBeanProperties(Object bean, BeanDefinition beanDef) {
		for (Iterator<?> beanPropertiesIt = beanDef.getProperties().iterator(); beanPropertiesIt.hasNext();) {
			BeanProperty beanProperty = (BeanProperty) beanPropertiesIt.next();
			if (beanProperty.getRef() != null) {
				try {
					BeanDefinition beanDefinition = findBeanDefinitionByPropertyReference(beanProperty.getRef());
					Object referencedBean =null;

					if (beanDefinition != null) {
						if(beanDefinition.isInstantiated()) {
							referencedBean= this.beans.get(beanDefinition.getId());
							 setProperty(bean, referencedBean, beanProperty.getName());
						}
						else {
						referencedBean = instanciateObject(beanDefinition.getClassName(), beanDefinition,null);
						setProperty(bean, referencedBean, beanProperty.getName());
						//Activate Aspect
						this.beans.put(beanDefinition.getId(),processAspectTreatmentFor(referencedBean, beanDefinition));
						BeansDefinitionApplication.markBeanAsInstantiated(beanDefinition);
						setBeanProperties(referencedBean, beanDefinition);
					}
					} else {
						throw new VenusBeanConfigurationNotFound(
								"Bean " + beanProperty.getRef() + " not found in Configuration file");
					}

				} catch (VenusBeanConfigurationNotFound | VenusPropertyNotFound e) {
					e.printStackTrace();
				}
			}
			setUpSimpleProperties(bean, beanProperty);
		}
		// BehaviourMethodsInvoker.invokeFor() should not be called for the
		// postProcessor beans
		if (!BeansDefinitionApplication.getPostProcessorBeansNames().contains(beanDef.getId())
				&& !BeansDefinitionApplication.getAspectBeansNames().contains(beanDef.getId())) {
			BehaviourMethodsInvoker.invokeFor(bean, beanDef.getId(), getExistingPostProcessorBeans(),
					beanDef.getInitMethod());
		}

	}

	/**
	 * Sets bean's properties which have primitive types
	 * 
	 * @param bean         an instance of bean
	 * @param beanProperty the related property of the bean
	 */
	private void setUpSimpleProperties(Object bean, BeanProperty beanProperty) {
		if (beanProperty.getValue() != null) {
			String propertyType;
			if (beanProperty.getType() != null) {
				propertyType = beanProperty.getType();
			} else {
				Class<?> cls = findPropertyDeclarationClass(bean, beanProperty.getName());
				propertyType = cls.getName();
			}
			Object object;
			if (isPrimitive(propertyType)) {
				object = getPropertyValueWithPrmTypes(propertyType, beanProperty.getValue());
			} else {
				object = instanciateObject(propertyType, null, beanProperty.getValue());
			}
			try {
				setProperty(bean, object, beanProperty.getName());
			} catch (VenusPropertyNotFound e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns an object after the instantiation of it's class. It's supports the
	 * instantiation with class constructors, related factories beans and related
	 * factory methods
	 * 
	 * @param className      class name of the object which will be created
	 * @param beanDefinition bean definition, could be null
	 * @return the object, result of instantiation
	 * 
	 */
	private Object instanciateObject(String className, BeanDefinition beanDefinition, String value) {
		Object object = null;
		try {
			if (beanDefinition != null && beanDefinition.getConstructorArguments() != null
					&& beanDefinition.getConstructorArguments().size() > 0) {

				Class[] paramCls = new Class<?>[beanDefinition.getConstructorArguments().size()];
				ArrayList<Object> paramValues = new ArrayList<>();
				for (int i = 0; i < beanDefinition.getConstructorArguments().toArray().length; i++) {
					BeanConstructorArgument constructorArgument = (BeanConstructorArgument) beanDefinition
							.getConstructorArguments().toArray()[i];
					Object paramObject = null;
					if (constructorArgument.getType() == null
							|| (constructorArgument.getType() != null && !isPrimitive(constructorArgument.getType()))) {
						paramCls[i] = Class.forName(
								(constructorArgument.getType() == null) ? DEFAULT_TYPE : constructorArgument.getType());
						if(constructorArgument.getValue()!=null) {
						paramObject = instanciateObject(
								(constructorArgument.getType() == null) ? DEFAULT_TYPE : constructorArgument.getType(),
								null,constructorArgument.getValue());
						}
					} else {
						paramCls[i] = getParamsClass(constructorArgument.getType());
						if(constructorArgument.getValue()!=null) {
						paramObject = getPropertyValueWithPrmTypes(constructorArgument.getType(),
								constructorArgument.getValue());
						}
					}
					paramValues.add(paramObject);
				}
				if (beanDefinition.getFactoryMethod() != null) {
					object = getObjectFromFactory(beanDefinition, paramCls, paramValues);
				} else {
					object = Class.forName(className).getConstructor(paramCls).newInstance(paramValues.toArray());
				}

			} else {
				if (beanDefinition != null && beanDefinition.getFactoryMethod() != null) {
					object = getObjectFromFactory(beanDefinition, null, null);
				} else {
					Class<?>[] clss= {Integer.class, Boolean.class, Double.class, Long.class,
							String.class,Float.class, Byte.class, Short.class};
					if(Arrays.asList(clss).contains(Class.forName(className)) && value!=null) {
						object = instantiateNoPrimitiveTypes(className,value);
					}
					else {
						object = Class.forName(className).getConstructor().newInstance();
					}
				}
			}

		} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}
	
	private Object instantiateNoPrimitiveTypes(String className, String value) {
		Object result=null;
		try {
			Class<?>cls = Class.forName(className);
			if(cls.equals(Double.class)) {
				try {
					result = Double.valueOf(value);
				} catch (IllegalArgumentException
						| SecurityException e) {
					e.printStackTrace();
				}
			}
			else if(cls.equals(String.class)) {
				try {
					result = String.valueOf(value);
				} catch (IllegalArgumentException
						| SecurityException e) {
					e.printStackTrace();
				}
			}
			else if(cls.equals(Integer.class)) {
				try {
					result = Integer.valueOf(value);
				} catch (IllegalArgumentException
						| SecurityException e) {
					e.printStackTrace();
				}
			}
			else if(cls.equals(Long.class)) {
				try {
					result = Long.valueOf(value);
				} catch (IllegalArgumentException
						| SecurityException e) {
					e.printStackTrace();
				}
			}
			else if(cls.equals(Float.class)) {
				try {
					result = Float.valueOf(value);
				} catch (IllegalArgumentException
						| SecurityException e) {
					e.printStackTrace();
				}
			}
			else if(cls.equals(Boolean.class)) {
				try {
					result = Boolean.valueOf(value);
				} catch (IllegalArgumentException
						| SecurityException e) {
					e.printStackTrace();
				}
			}
			else if(cls.equals(Short.class)) {
				try {
					result = Short.valueOf(value);
				} catch (IllegalArgumentException
						| SecurityException e) {
					e.printStackTrace();
				}
			}
			else if(cls.equals(Byte.class)) {
				try {
					result = Byte.valueOf(value);
				} catch (IllegalArgumentException
						| SecurityException e) {
					e.printStackTrace();
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Returns an object after the instantiation with factory-bean or method-bean
	 * 
	 * @param beanDefinition the related bean definition
	 * @param paramCls       constructor parameters class array
	 * @param paramValues    constructor parameters values list
	 * @return the object, result of instantiation by the factory-bean or
	 *         factory-method
	 */
	private Object getObjectFromFactory(BeanDefinition beanDefinition, Class[] paramCls,
			ArrayList<Object> paramValues) {
		Object object = null;
		try {
			if (beanDefinition.getFactoryBean() != null) {
				if (beanExists(beanDefinition.getFactoryBean())) {
					Object factoryBean = this.beans.get(beanDefinition.getFactoryBean());
					Class<?> factoryClass = factoryBean.getClass();
					Method setter;
					if (paramCls != null) {
						setter = factoryClass.getMethod(beanDefinition.getFactoryMethod(), paramCls);
						object = setter.invoke(factoryBean, paramValues);

					} else {
						setter = factoryClass.getMethod(beanDefinition.getFactoryMethod());
						object = setter.invoke(factoryBean);
					}
				}
			} else {
				Class<?> factoryClass = Class.forName(beanDefinition.getClassName());
				Method setter;
				if (paramCls != null) {
					setter = factoryClass.getMethod(beanDefinition.getFactoryMethod(), paramCls);
					object = setter.invoke(null, paramValues);

				} else {
					setter = factoryClass.getMethod(beanDefinition.getFactoryMethod());
					object = setter.invoke(null);

				}

			}

		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return object;

	}

	/**
	 * Sets bean's property
	 * 
	 * @param mainObject   the principal object, in which the property object will
	 *                     be injected
	 * @param property     the property object
	 * @param propertyName the property's name
	 */
	private void setProperty(Object mainObject, Object property, String propertyName) throws VenusPropertyNotFound {
		if (findPropertyDeclarationClass(mainObject, propertyName) != null) {
			try {
				Method setter = mainObject.getClass().getMethod(
						"set" + propertyName.replaceFirst(propertyName.substring(0, 1),
								propertyName.substring(0, 1).toUpperCase()),
						new Class[] { findPropertyDeclarationClass(mainObject, propertyName) });
				setter.invoke(mainObject, new Object[] { property });
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}

		} else {
			throw new VenusPropertyNotFound(
					"Field " + propertyName + " not found in Class: " + mainObject.getClass().getName());
		}
	}

	/**
	 * Finds the class of a property in the main object class
	 * 
	 * @param mainObject   the principal object
	 * @param propertyName property's name (field's name)
	 * @return the class of the property (field's name)
	 */
	private Class<?> findPropertyDeclarationClass(Object mainObject, String propertyName) {
		Class<?> cls = null;
		for (Iterator<?> classFieldsIt = Arrays.asList(mainObject.getClass().getDeclaredFields())
				.iterator(); classFieldsIt.hasNext();) {
			Field field = (Field) classFieldsIt.next();
			if (field.getName().equals(propertyName)) {
				cls = field.getType();
				break;
			}
		}
		return cls;
	}

	/**
	 * Finds the bean definition related to a referenced property
	 * 
	 * @param reference bean definition identifier of the referenced property
	 * @return the bean definition of the property
	 */
	private BeanDefinition findBeanDefinitionByPropertyReference(String reference) {
		for (Iterator<?> beansDefIterator = BeansDefinitionApplication.getBeansDefinitionApplication()
				.iterator(); beansDefIterator.hasNext();) {
			BeanDefinition beanDef = (BeanDefinition) beansDefIterator.next();
			if (beanDef.getId().equals(reference)) {
				return beanDef;
			}
		}
		return null;
	}

	/**
	 * Checks if the type of a property is primitive
	 * 
	 * @param type the type to check
	 * @return boolean value reflecting whether this type is primitive
	 */
	private boolean isPrimitive(String type) {
		if (type.equals("boolean") || type.equals("byte") || type.equals("short") || type.equals("int")
				|| type.equals("long") || type.equals("float") || type.equals("double") || type.equals("char")) {
			return true;
		}
		return false;
	}

	/**
	 * Converts the value in parameter to the appropriate type
	 * 
	 * @param type  the type to which the value will be converted
	 * @param value the value to be converted
	 * @return object, result of conversion
	 */
	private Object getPropertyValueWithPrmTypes(String type, String value) {
		switch (type) {
		case "boolean":
			return Boolean.parseBoolean(value);
		case "byte":
			return Byte.parseByte(value);
		case "short":
			return Short.parseShort(value);
		case "int":
			return Integer.parseInt(value);
		case "long":
			return Long.parseLong(value);
		case "float":
			return Float.parseFloat(value);
		case "double":
			return Double.parseDouble(value);
		case "char":
			char pValue = value.charAt(0);
			return pValue;
		default:
			return value;
		}
	}

	/**
	 * Returns the class of a primitive type in parameter
	 * 
	 * @param type the primitive type
	 * @return class of the related type
	 */
	private Class<?> getParamsClass(String type) {
		switch (type) {
		case "boolean":
			return boolean.class;
		case "byte":
			return byte.class;
		case "short":
			return short.class;
		case "int":
			return int.class;
		case "long":
			return long.class;
		case "float":
			return float.class;
		case "double":
			return double.class;
		case "char":
			return char.class;
		default:
			return String.class;
		}
	}

	/**
	 * Destroys all the beans in the container with running the method
	 * invokeOnShutDownContainerFor() of BehaviourMethodsInvoker to check the beans
	 * life cycle
	 */
	public void destroyBeans() {
		BeansDefinitionApplication.getBeansDefinitionApplication().stream().filter(beanDef -> {
			try {
				return Arrays.asList(Class.forName(beanDef.getClassName()).getInterfaces())
						.contains(BehaviourDestroy.class) || beanDef.getDestroyMethod() != null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return false;
		}).forEach(beanDef -> {
			BehaviourMethodsInvoker.invokeOnShutDownContainerFor(this.beans.get(beanDef.getId()), beanDef.getId(),
					beanDef.getDestroyMethod());
		});
		this.beans.clear();
	}

	/**
	 * Returns all the post processor beans existing in the beans container
	 * 
	 * @return list of all post processor beans
	 */
	private ArrayList<Object> getExistingPostProcessorBeans() {
		ArrayList<Object> postProcessorBeans = new ArrayList<>();
		BeansDefinitionApplication.getPostProcessorBeansNames().stream()
				.forEach(beanName -> postProcessorBeans.add(this.beans.get(beanName)));
		return postProcessorBeans;
	}
	
	/**
	 * Returns all the aspect beans existing in the beans container
	 * 
	 * @return list of all aspect beans
	 */
	private ArrayList<Object> getExistingAspectBeans() {
		ArrayList<Object> aspectBeans = new ArrayList<>();
		BeansDefinitionApplication.getAspectBeansNames().stream()
				.forEach(beanName -> aspectBeans.add(this.beans.get(beanName)));
		return aspectBeans;
	}
	
	private Object processAspectTreatmentFor(Object target, BeanDefinition relatedBeanDefinition) {
		if(BeansDefinitionApplication.getTechnicalBeansNames()
				.contains(relatedBeanDefinition.getId())){
			return target;
		}
		else if(getExistingAspectBeans().size()>0) {
			if(AOPUtility.isEligibleToAspectProcess(target, getExistingAspectBeans())) {
			return 	AspectActivator.activateAspect(target, getExistingAspectBeans());
			}
		}
		return target;
	}
}
