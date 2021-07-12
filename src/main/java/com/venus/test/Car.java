package com.venus.test;

import com.venus.core.annotation.Autowired;
import com.venus.core.annotation.Bean;

@Bean(isSingleton = false)
public class Car {
	private String ref;
	//private PersonInterface person;
	public String getRef() {
		return ref;
	}
	
	public void setRef(String ref) {
		this.ref = ref;
	}
	/*public PersonInterface getPerson() {
		return person;
	}
    @Autowired
	public void setPerson(PersonInterface person) {
		this.person = person;
	}*/


}
