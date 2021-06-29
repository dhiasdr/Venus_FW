package com.venus.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

import com.venus.aop.annotation.After;
import com.venus.aop.annotation.AfterReturning;
import com.venus.aop.annotation.AfterThrowing;
import com.venus.aop.annotation.Around;
import com.venus.aop.annotation.Before;

public class MethodsInvocationInterceptor implements InvocationHandler {
	private Object target;
	private ArrayList<Object> aspects;

	public MethodsInvocationInterceptor(Object target) {
		this.target = target;
	}

	public void setAspects(ArrayList<Object> aspects) {
		this.aspects = aspects;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		Method realMethod = getMethodOfRealClass(proxy, method);
		invokeAspectMethodForProxy(proxy, realMethod, aspects, Before.class, null);
		invokeAspectMethodForProxy(proxy, realMethod, aspects, Around.class, null);
		try {
			result = method.invoke(target, args);
		} catch (Exception exception) {
			exception.printStackTrace();
			invokeAspectMethodForProxy(proxy, realMethod, aspects, AfterThrowing.class, exception.getCause());
		}
		invokeAspectMethodForProxy(proxy, realMethod, aspects, AfterReturning.class, result);
		invokeAspectMethodForProxy(proxy, realMethod, aspects, Around.class, null);
		invokeAspectMethodForProxy(proxy, realMethod, aspects, After.class, null);
		return result;
	}

	private Method getMethodOfRealClass(Object proxy, Method method) {
		Class<?> realCls = ((MethodsInvocationInterceptor) (Proxy.getInvocationHandler(proxy))).target.getClass();
		for (Method realMethod : realCls.getDeclaredMethods()) {
			if (method.getName().equals(realMethod.getName()) && (Arrays.asList(method.getParameterTypes())
					.equals(Arrays.asList(realMethod.getParameterTypes())))) {
				return realMethod;
			}
		}
		return method;
	}

	private void invokeAspectMethodForProxy(Object proxy, Method method, ArrayList<Object> aspects,
			Class<? extends Annotation> annotationCls, Object returnResult) throws Throwable {
		for (Object aspect : this.aspects) {
			for (Method m : aspect.getClass().getDeclaredMethods()) {
				if (m.isAnnotationPresent(annotationCls)) {
					if (AOPUtility.checkExpressionMatching(method, getExpressionValue(m, annotationCls))) {
						if (annotationCls.equals(AfterReturning.class) || annotationCls.equals(AfterThrowing.class)) {
							m.invoke(aspect, returnResult);
						} else
							m.invoke(aspect);
					}
				}
			}
		}
	}

	private String getExpressionValue(Method method, Class<? extends Annotation> annotationCls) {
		String expressionValue = null;
		if (annotationCls.equals(Before.class)) {
			expressionValue = ((Before) method.getAnnotation(annotationCls)).value();
		} else if (annotationCls.equals(After.class)) {
			expressionValue = ((After) method.getAnnotation(annotationCls)).value();
		} else if (annotationCls.equals(Around.class)) {
			expressionValue = ((Around) method.getAnnotation(annotationCls)).value();
		} else if (annotationCls.equals(AfterReturning.class)) {
			expressionValue = ((AfterReturning) method.getAnnotation(annotationCls)).value();
		} else if (annotationCls.equals(AfterThrowing.class)) {
			expressionValue = ((AfterThrowing) method.getAnnotation(annotationCls)).value();
		}
		return expressionValue;
	}
}
