package com.venus.test.dao;

import com.venus.core.behaviour.BehaviourAware;
import com.venus.core.behaviour.BehaviourDestroy;
import com.venus.core.behaviour.BehaviourInit;

public class ClassA implements IClassA, BehaviourAware, BehaviourInit, BehaviourDestroy {
	private int age;
	private String name;

	public ClassA() {
	}

	public ClassA(int age, String name) {
		super();
		this.age = age;
		this.name = name;
	}

	@Override
	public void test() {
		System.out.println(age + " hello: my name:" + name);
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public void initialize() {
		System.out.println("Initialize method executed");
	}

	@Override
	public void setBeanName(String beanName) {
		System.out.println(beanName + " :Bean Name");
	}

	public void init() {
		System.out.println("init method exexuted");
	}

	@Override
	public void onDestroy() {
		System.out.println("Destroy bean ClassA");
	}

	public void destroy() {
		System.out.println("Destroy bean ClassA");
	}

}
