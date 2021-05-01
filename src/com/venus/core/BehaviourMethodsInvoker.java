package com.venus.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.venus.core.behaviour.BehaviourAware;
import com.venus.core.behaviour.BehaviourDestroy;
import com.venus.core.behaviour.BehaviourInit;
import com.venus.core.behaviour.BehaviourPostProcessors;

public class BehaviourMethodsInvoker {
	public static void invokeFor(Object bean, String beanName, Object postProcessorBean, String initMethodName) {
		Class<?>[] postProcessorParameterTypes = { Object.class, String.class };
		Object[] postProcessorParameterValues = { bean, beanName };
		List<Class<?>> interfaces = Arrays.asList(bean.getClass().getInterfaces());
		Method method;
		if (interfaces.contains(BehaviourAware.class)) {
			try {
				method = BehaviourAware.class.getMethod("setBeanName", String.class);
				method.invoke(bean, beanName);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if (postProcessorBean != null) {
			try {
				method = BehaviourPostProcessors.class.getMethod("operationBeforeInitialization",
						postProcessorParameterTypes);
				method.invoke(postProcessorBean, postProcessorParameterValues);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
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
		if (postProcessorBean != null) {
			try {
				method = BehaviourPostProcessors.class.getMethod("operationAfterInitialization",
						postProcessorParameterTypes);
				method.invoke(postProcessorBean, postProcessorParameterValues);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}
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
}
