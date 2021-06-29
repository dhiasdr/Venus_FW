package com.venus.core;

import com.venus.core.factory.IBeanFactory;

public interface ApplicationContext extends IBeanFactory {
	/**
	 * This method will be overridden to implement the processing that must be done
	 * when the context is stopped
	 */
	public void shutDown();
}
