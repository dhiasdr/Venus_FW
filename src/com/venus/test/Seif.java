package com.venus.test;

import com.venus.core.annotation.Autowired;
import com.venus.core.annotation.Bean;

@Bean(factoryBean = "seifFactory", factoryMethod = "getInstance")
public class Seif {
	private String name;
	private Double age=19.0;
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


}
