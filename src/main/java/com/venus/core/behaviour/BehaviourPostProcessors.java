package com.venus.core.behaviour;

public interface BehaviourPostProcessors {
	/**
	 * Defines the processing to be performed before the initialization phase
	 * 
	 * @param bean object which will suffer the operation
	 * @param name bean's name
	 * @return the object after the treatment
	 */
	default Object operationBeforeInitialization(Object bean, String name) {
		return bean;
	}

	/**
	 * Defines the processing to be performed after the initialization phase
	 * 
	 * @param bean object which will suffer the operation
	 * @param name bean's name
	 * @return the object after the treatment
	 */
	default Object operationAfterInitialization(Object bean, String name) {
		return bean;
	}
}
