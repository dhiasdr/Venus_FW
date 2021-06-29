package com.venus.aop.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyFactory {
	private InvocationHandler invocationHandler;
	private Object target;
	
	public void setInvocationHandler(InvocationHandler invocationHandler) {
		this.invocationHandler = invocationHandler;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
    public Object createProxy() {
    return Proxy.newProxyInstance(this.target.getClass().getClassLoader(),
    		                      this.target.getClass().getInterfaces(),
    	                          this.invocationHandler);
    }
	

}
