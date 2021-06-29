package com.venus.test;

import com.venus.core.annotation.Autowired;
import com.venus.core.annotation.Bean;
import com.venus.core.behaviour.BehaviourAware;
import com.venus.core.behaviour.BehaviourDestroy;
import com.venus.core.behaviour.BehaviourInit;
import com.venus.test.dao.IClassA;

@Bean(factoryBean = "seifFactory", factoryMethod = "getInstance", initMethod="init", destroyMethod="destroy")
public class Seif implements SeifInterface,BehaviourAware, BehaviourInit, BehaviourDestroy{
	private String name;
	private Double age = 19.0;
	private Test3 test3;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getAge() {
		return age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	public Seif() {
		super();

	}

	public Test3 getTest3() {
		return test3;
	}

	@Autowired
	public void setTest3(Test3 test) {
		this.test3 = test;
	}

	@Override
	public void aspectTest() {
		System.out.println("aspectTest");
	}

	@Override
	public void onDestroy() {
		System.out.println("destroy**");
		
	}

	@Override
	public void initialize() {
		System.out.println("init**");
		
	}

	@Override
	public void setBeanName(String beanName) {
		System.out.println("bean:" +beanName);
		
	}

	public void init() {
		System.out.println("init method exexuted");
	}

	@Override
	public void destroy() {
		System.out.println("Destroy bean by destroy-method");
	}
}
