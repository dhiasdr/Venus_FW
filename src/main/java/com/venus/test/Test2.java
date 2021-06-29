package com.venus.test;


import com.venus.core.ApplicationContext;
import com.venus.core.ApplicationContextXMLConfiguration;
import com.venus.test.dao.ClassIndepCir;
import com.venus.test.dao.ClassIndepCir1;

public class Test2 {
	public static void main(String[] args) {

	ApplicationContext context = new ApplicationContextXMLConfiguration(
			"BeansConfiguration.xml");
	
	TestDouble testD = context.getBean("testD",
			TestDouble.class);
	System.out.println(testD.getD());
	ClassIndepCir cir= context.getBean("classIndepCir", ClassIndepCir.class);
	ClassIndepCir1 cir1= context.getBean("classIndepCir1", ClassIndepCir1.class);
	
	System.out.println(cir.getClassIndepCir1().getB());
	System.out.println(cir1.getClassIndepCir().getA());


}
}