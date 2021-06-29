package com.venus.test;

import com.venus.core.annotation.Autowired;
import com.venus.core.annotation.Bean;

@Bean(isSingleton = false)
public class Test3 {
	private String name;
	private int age=15;
	private SeifInterface seif;
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public SeifInterface getSeif() {
		return seif;
	}
    @Autowired
	public void setSeif(SeifInterface seif) {
		this.seif = seif;
	}


}
