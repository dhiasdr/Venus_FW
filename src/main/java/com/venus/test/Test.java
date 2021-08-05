package com.venus.test;


import com.venus.core.ApplicationContext;
import com.venus.core.ApplicationContextXMLConfiguration;
import com.venus.test.dao.ClassB;
import com.venus.test.dao.IClassA;
import com.venus.test.dao.IObjectDAO;
import com.venus.test.dao.ObjectDAOFactory;
import com.venus.test.dao.ObjectDAOImpl;
import com.venus.test.service.IObjectService;
import com.venus.test.service.ObjectServiceImpl;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ApplicationContextXMLConfiguration(
				"BeansConfiguration.xml");
       
		IObjectService service = context.getBean("objectService",
				IObjectService.class);
		((ObjectServiceImpl)service).getObjectDAO().work();
		IObjectDAO objDao=context.getBean("objectDAO", IObjectDAO.class);
		objDao.work();
        
		//Aspect
	    objDao.aspectTest();
	    context.shutDown();
	}
	
}
