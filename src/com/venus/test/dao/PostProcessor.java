package com.venus.test.dao;

import com.venus.core.behaviour.BehaviourPostProcessors;

public class PostProcessor implements BehaviourPostProcessors {
	@Override
	public Object operationBeforeInitialization(Object bean, String name) {
		if (bean instanceof ClassA) {
			System.out.println("beforeIntialization executed for " + name);
		}
		return bean;
	}
	@Override
	public Object operationAfterInitialization(Object bean, String name) {
		System.out.println("after Intialization executed");
		return BehaviourPostProcessors.super.operationAfterInitialization(bean, name);
	}
}
