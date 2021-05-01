package com.venus.test.dao;

import com.venus.core.behaviour.BehaviourDestroy;

public class ClassB implements BehaviourDestroy {
	private int age;
	private String name;
	private ClassA classA;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public ClassB(int age, String name) {
		super();
		this.age = age;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ClassA getClassA() {
		return classA;
	}

	public void setClassA(ClassA classA) {
		this.classA = classA;
	}

	@Override
	public String toString() {
		return "ClassB [age=" + age + ", name=" + name + "]";
	}

	@Override
	public void onDestroy() {
		System.out.println("Destroy !!");
	}

}
