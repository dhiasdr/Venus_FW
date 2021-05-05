package com.venus.test.dao;

import com.venus.core.behaviour.BehaviourPostProcessors;
import com.venus.core.behaviour.Ordered;

public class PostProcessor implements BehaviourPostProcessors, Ordered {
	@Override
	public Object operationBeforeInitialization(Object bean, String name) {
		if (bean instanceof ClassA) {
			System.out.println("PostProcessor:// beforeIntialization executed for " + name);
		}
		return bean;
	}

	@Override
	public Object operationAfterInitialization(Object bean, String name) {
		if (bean instanceof ClassA) {
			System.out.println("PostProcessor:// after Intialization executed");
		}
		return bean;
	}

	@Override
	public int order() {
		return 6;
	}
}