package com.venus.test;

import com.venus.core.ApplicationContext;
import com.venus.core.ApplicationContextConfiguration;

public class TestAnnotation {

	public static void main(String[] args) {
		ApplicationContext context = new ApplicationContextConfiguration();
		PersonInterface person = context.getBean("person", PersonInterface.class);
		Car car = context.getBean("car", Car.class);
        car.setRef("ref-car");
		System.out.println(car.getRef());
		person.aspectTest();
		context.shutDown();
	}

}
