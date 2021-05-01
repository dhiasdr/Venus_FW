package com.venus.core;

import com.venus.core.factory.IBeanFactory;

public interface ApplicationContext extends IBeanFactory {
	public void shutDown();
}
