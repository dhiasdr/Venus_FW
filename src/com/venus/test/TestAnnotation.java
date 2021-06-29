package com.venus.test;

import com.venus.core.ApplicationContext;
import com.venus.core.ApplicationContextConfiguration;
import com.venus.core.ApplicationContextXMLConfiguration;
import com.venus.test.dao.ClassA;
import com.venus.test.dao.IClassA;

public class TestAnnotation {

	public static void main(String[] args) {
		ApplicationContext context = new ApplicationContextConfiguration();
        //Test-Prototype
		SeifInterface seif = context.getBean("seif", SeifInterface.class);
		Test3 test3 = context.getBean("test3", Test3.class);
		seif.aspectTest();
		//System.out.println(seif.getTest3().getAge());
		//System.out.println(test3.getSeif().getAge());
		context.shutDown();
	}

}
