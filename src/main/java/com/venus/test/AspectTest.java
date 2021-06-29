package com.venus.test;

import com.venus.aop.annotation.After;
import com.venus.aop.annotation.AfterReturning;
import com.venus.aop.annotation.AfterThrowing;
import com.venus.aop.annotation.Around;
import com.venus.aop.annotation.Before;
import com.venus.core.annotation.Aspect;
import com.venus.core.annotation.Bean;
@Bean
@Aspect
public class AspectTest {
	@Before("* * *.Seif.aspectTest()")
	public void beforeTreatement(){
		System.out.println("Traitement Before");
	}
	/*@After("* * *.ObjectDAOImpl.aspectTest() && @annotation(Override)")
	public void afterTreatement(){
		System.out.println("Traitement after");
	}*/
	
	@Around("* * *.Seif.aspectTest() && @annotation(Override)")
	public void aroundTreatement(){
		System.out.println("Traitement around");
	}
	/*@AfterReturning("* * *.ObjectDAOImpl.aspectTest() && @annotation(Override)")
	public void afterReturningTreatement(Object obj){
		System.out.println("Traitement after returning " + obj);
	}
	@AfterThrowing("* * *.ObjectDAOImpl.aspectTest() && @annotation(Override)")
	public void afterThrowingTreatement(Exception e){
		System.out.println("Traitement after throwing " + e);
	}
	
	@Around("* * com.venus.*.*.ClassA.test()")
	public void aroundClassATreatement(){
		System.out.println("Traitement around classA");
	}*/
}
