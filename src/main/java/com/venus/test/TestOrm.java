package com.venus.test;

import java.lang.reflect.InvocationTargetException;

import com.venus.orm.Orm;

public class TestOrm {

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Orm.generateAndExecuteTableCreationScript();	
	}
}
