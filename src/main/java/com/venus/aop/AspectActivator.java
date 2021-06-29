package com.venus.aop;

import java.util.ArrayList;

import com.venus.aop.factory.ProxyFactory;

public class AspectActivator {
  
	
	public static Object activateAspect(Object target, ArrayList<Object> aspects) {
		ProxyFactory proxyFactory = new ProxyFactory();
		MethodsInvocationInterceptor invocationInterceptor= new MethodsInvocationInterceptor(target);
		invocationInterceptor.setAspects(aspects);
		proxyFactory.setInvocationHandler(invocationInterceptor);
		proxyFactory.setTarget(target);
		Object proxyObject = proxyFactory.createProxy();
		return proxyObject;
	}
}
