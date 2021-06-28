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

		Seif seif = context.getBean("seif", Seif.class);
		Test3 test3 = context.getBean("test3", Test3.class);
		System.out.println(seif.getTest3().getAge());
		System.out.println(test3.getSeif().getAge());
	}

}
