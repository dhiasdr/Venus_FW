package com.venus.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.venus.core.behaviour.BehaviourAware;
import com.venus.core.behaviour.BehaviourDestroy;
import com.venus.core.behaviour.BehaviourInit;
import com.venus.core.behaviour.BehaviourPostProcessors;
import com.venus.core.behaviour.Ordered;

public class BehaviourMethodsInvoker {
	private static ArrayList<Object> containerPostProcessorBeans;

	/**
	 * Checks the life cycle of the object passed as a parameter before being ready
	 * for use by invoking the behavior interfaces if they are implemented by this
	 * object and the init-method also if it has been configured and mapped to the
	 * bean definition
	 * 
	 * @param bean               object to be treated
	 * @param beanName           bean's name
	 * @param postProcessorBeans postProcessorBean object
	 * @param initMethodName     init-method's name of the related bean
	 */
	public static void invokeFor(Object bean, String beanName, ArrayList<Object> postProcessorBeans,
			String initMethodName) {
		Class<?>[] postProcessorParameterTypes = { Object.class, String.class };
		Object[] postProcessorParameterValues = { bean, beanName };
		List<Class<?>> interfaces = Arrays.asList(bean.getClass().getInterfaces());
		Method method;
		setPostProcessorBeans(postProcessorBeans);
		if (interfaces.contains(BehaviourAware.class)) {
			try {
				method = BehaviourAware.class.getMethod("setBeanName", String.class);
				method.invoke(bean, beanName);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if (containerPostProcessorBeans != null) {
			invokePostProcessorBehaviourMethods(containerPostProcessorBeans, true, postProcessorParameterTypes,
					postProcessorParameterValues);
		}
		if (initMethodName != null) {
			try {
				method = bean.getClass().getMethod(initMethodName);
				method.invoke(bean);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if (interfaces.contains(BehaviourInit.class)) {
			try {
				method = BehaviourInit.class.getMethod("initialize");
				method.invoke(bean);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if (containerPostProcessorBeans != null) {
			invokePostProcessorBehaviourMethods(containerPostProcessorBeans, false, postProcessorParameterTypes,
					postProcessorParameterValues);
		}

	}

	/**
	 * Checks the life cycle of the object passed as a parameter before destroying
	 * all the beans by invoking the behavior interface(BehaviourDestroy)'s method
	 * if it's implemented by this object and the destroy-method also if it has been
	 * configured and mapped to the bean definition
	 * 
	 * @param bean              object to be treated
	 * @param beanName          bean's name
	 * @param destroyMethodName destroy-method's name of the related bean
	 */
	public static void invokeOnShutDownContainerFor(Object bean, String beanName, String destroyMethodName) {
		List<Class<?>> interfaces = Arrays.asList(bean.getClass().getInterfaces());
		Method method;
		if (destroyMethodName != null) {
			try {
				method = bean.getClass().getMethod(destroyMethodName);
				method.invoke(bean);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if (interfaces.contains(BehaviourDestroy.class)) {
			try {
				method = BehaviourDestroy.class.getMethod("onDestroy");
				method.invoke(bean);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Invokes postProcessors methods(operationBeforeInitialization or
	 * operationAfterInitialization) for the bean which is contained in
	 * postProcessorParameterValues passed in parameter
	 * 
	 * @param postProcessorBeans           list of all postProcessor beans presented
	 *                                     in the container
	 * @param beforeInitialization         boolean value reflecting the method to be
	 *                                     executed
	 * @param postProcessorParameterTypes  array of the bean and the bean's name
	 *                                     class
	 * @param postProcessorParameterValues array of object contains the bean and the
	 *                                     bean's name
	 */
	private static void invokePostProcessorBehaviourMethods(ArrayList<Object> postProcessorBeans,
			boolean beforeInitialization, Class<?>[] postProcessorParameterTypes,
			Object[] postProcessorParameterValues) {
		postProcessorBeans.stream().forEach(postProccesorBean -> {
			Method method;
			try {
				method = BehaviourPostProcessors.class.getMethod(
						beforeInitialization ? "operationBeforeInitialization" : "operationAfterInitialization",
						postProcessorParameterTypes);
				method.invoke(postProccesorBean, postProcessorParameterValues);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		});

	}

	/**
	 * Orders postProcessor beans by their order number after invoking the Ordered
	 * interface method (order ()) if it is implemented by the postProcessor classes
	 * 
	 * @param postProcessorBeans list of all postProcessor beans presented in the
	 *                           container
	 */
	private static void orderPostProcessorBeans(ArrayList<Object> postProcessorBeans) {
		Collections.sort(postProcessorBeans, new Comparator<Object>() {
			@Override
			public int compare(Object postProcc1, Object postProcc2) {
				Method postProcc1Method;
				int postProcc1Order = 0;
				Method postProcc2Method;
				int postProcc2Order = 0;
				if (Arrays.asList(postProcc1.getClass().getInterfaces()).contains(Ordered.class)) {
					try {
						postProcc1Method = Ordered.class.getMethod("order");
						postProcc1Order = (int) postProcc1Method.invoke(postProcc1);
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				if (Arrays.asList(postProcc2.getClass().getInterfaces()).contains(Ordered.class)) {
					try {
						postProcc2Method = Ordered.class.getMethod("order");
						postProcc2Order = (int) postProcc2Method.invoke(postProcc2);
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				return postProcc1Order - postProcc2Order;
			}
		});
	}

	/**
	 * Fills and sorts the containerPostProcessorBeans list if it's null
	 */
	private static void setPostProcessorBeans(ArrayList<Object> postProcessorBeans) {
		if (containerPostProcessorBeans == null) {
			containerPostProcessorBeans = postProcessorBeans;
			orderPostProcessorBeans(containerPostProcessorBeans);
		}
	}
}
