package com.venus.test.dao;

import com.venus.core.behaviour.BehaviourDestroy;

public class ClassB implements BehaviourDestroy {
	private int age;
	private int salaire;
	private String name;
	private ClassA classA;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public ClassB(int age, String name, int salaire) {
		super();
		this.age = age;
		this.name = name;
		this.salaire=salaire;
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
	public int getSalaire() {
		return salaire;
	}

	public void setSalaire(int salaire) {
		this.salaire = salaire;
	}

	@Override
	public String toString() {
		return "ClassB [age=" + age + ", salaire=" + salaire + ", name=" + name + ", classA=" + classA + "]";
	}

	@Override
	public void onDestroy() {
		System.out.println("Destroy !!");
	}

}
