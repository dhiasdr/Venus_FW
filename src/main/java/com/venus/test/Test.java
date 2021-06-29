package com.venus.test;


import com.venus.core.ApplicationContext;
import com.venus.core.ApplicationContextXMLConfiguration;
import com.venus.test.dao.ClassA;
import com.venus.test.dao.ClassB;
import com.venus.test.dao.IClassA;
import com.venus.test.dao.IObjectDAO;
import com.venus.test.dao.ObjectDAOFactory;
import com.venus.test.dao.ObjectDAOImpl;
import com.venus.test.dao.Singleton;
import com.venus.test.service.IObjectService;
import com.venus.test.service.ObjectServiceImpl;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ApplicationContextXMLConfiguration(
				"BeansConfiguration.xml");
        //Test-Prototype

		/*IClassA classA = context.getBean("classA", IClassA.class);
		((ClassA) classA).setName("classA");
		System.out.println("Hello object (helloA)" + "Your name is: "
				+ ((ClassA) classA).getName());
		IClassA classB = context.getBean("classA", IClassA.class);
		((ClassA) classB).setName("classB");
		System.out.println("Hello object (helloB)" + "Your name is: "
				+ ((ClassA) classB).getName());
		System.out.println("'classA' and 'classB'" + "are referring "
				+ "to the same object: " + (classA == classB));

		System.out.println("Address of object classA: " + classA);
		System.out.println("Address of object classB: " + classB);
		System.out.println("----------------------- ");*/

        //Test-Singleton
		ObjectServiceImpl obj1 = context.getBean("objectService",
				ObjectServiceImpl.class);
        ((ObjectDAOImpl)obj1.getObjectDAO()).setA(15);

		IObjectService obj2 = context.getBean("objectService",
				ObjectServiceImpl.class);
        ((ObjectDAOImpl)obj1.getObjectDAO()).setA(30);

		System.out.println(obj1+" ---"+obj2+"====>"+obj1.equals(obj2));
		System.out.println("'obj1' and 'obj2'" + "are referring "
				+ "to the same object: " + (obj1 == obj2));
		System.out.println("Address of object obj1: " + obj1);
		System.out.println("Address of object obj2: " + obj2);
		obj1.serviceWork();
		//IObjectDAO dao1=context.getBean("objectDAO", ObjectDAOImpl.class);
		//IObjectDAO dao2=context.getBean("objectDAO", ObjectDAOImpl.class);
		//dao1.work();
		//System.out.println(dao1+" ---"+dao2+"====>"+dao1.equals(dao2));
		ClassB b = context.getBean("classB", ClassB.class);
		ClassB b1 = context.getBean("classB", ClassB.class);
		//System.out.println(b);
		System.out.println(b+" ---"+b1+"====>"+b.equals(b1));
		b1.getClassA().test();
		System.out.println(" ----------Singleton-FactoryMethod--------------");
		Singleton singleton = context.getBean("singleton", Singleton.class);
		Singleton singleton1 = context.getBean("singleton", Singleton.class);
        System.out.println(context.isSingleton("singleton"));
		System.out.println(singleton+" ---"+singleton1+"====>"+singleton.equals(singleton1));
		ObjectDAOFactory daoFact = context.getBean("daoFactory", ObjectDAOFactory.class);
		//ObjectDAOImpl daoImpl = (ObjectDAOImpl) daoFact.createInstance();
		System.out.println("test daoFactory");
		//daoImpl.work();
        System.out.println("-------------------------------");
		IClassA classA7 = context.getBean("classA", IClassA.class);
		IClassA classA1 = context.getBean("classA",IClassA.class);
		System.out.println(classA7+" ---"+classA1+"====>"+classA7.equals(classA1));

		//classA.test();
		//context.shutDown();
		//ObjectServiceImpl obj1 = context.getBean("objectService",
				//ObjectServiceImpl.class);
		//((ObjectDAOImpl)obj1.getObjectDAO()).setA(10);
		//((ObjectDAOImpl)obj1.getObjectDAO()).getClassC().setB(18);
		//ObjectServiceImpl obj2 = context.getBean("objectService",
				//ObjectServiceImpl.class);
		//((ObjectDAOImpl)obj2.getObjectDAO()).setA(20);
		//((ObjectDAOImpl)obj2.getObjectDAO()).getClassC().setB(28);

		System.out.println(obj1==obj2);
		obj1.serviceWork();
        System.out.println("************************************");
       // ObjectDAOImpl od1=context.getBean("objectDAO", ObjectDAOImpl.class);
        //od1.getClassC().setB(20);
       // ObjectDAOImpl od2=context.getBean("objectDAO", ObjectDAOImpl.class);
       // od2.getClassC().setB(50);
       // od1.work();
        System.out.println("************************************");
        
        
        IObjectDAO objDao=context.getBean("objectDAO", IObjectDAO.class);
        objDao.aspectTest();
        //ClassA clsA=context.getBean("classA", ClassA.class);
		//System.out.println(clsA.getAge());
        IClassA clsA=context.getBean("classA", IClassA.class);
        clsA.test();
	}
	
}
