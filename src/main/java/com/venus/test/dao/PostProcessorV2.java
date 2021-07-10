package com.venus.test.dao;

import com.venus.core.behaviour.BehaviourPostProcessors;
import com.venus.core.behaviour.Ordered;
import com.venus.test.Seif;

public class PostProcessorV2   implements BehaviourPostProcessors, Ordered{
	@Override
	public Object operationBeforeInitialization(Object bean, String name) {
		if (bean instanceof Seif) {
			System.out.println("PostProcessorV2:// beforeIntialization executed for " + name);
		}
		return bean;
	}
	@Override
	public Object operationAfterInitialization(Object bean, String name) {
		if(bean instanceof Seif) {
		System.out.println("PostProcessorV2:// after Intialization executed");
		}
		return bean;
	}
	@Override
	public int order() {
		return 4;
	}
}
