package com.venus.test;

import com.venus.core.annotation.Bean;

@Bean
public class PersonFactory {
	public Person getInstance() {
		return new Person();
	}
}
