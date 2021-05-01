package com.venus.core.behaviour;

public interface BehaviourPostProcessors {
	default Object operationBeforeInitialization(Object bean, String name) {
		return bean;
	}

	default Object operationAfterInitialization(Object bean, String name) {
		return bean;
	}
}
