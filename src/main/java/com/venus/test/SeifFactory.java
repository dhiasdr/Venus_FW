package com.venus.test;

import com.venus.core.annotation.Bean;


@Bean
public class SeifFactory {
 public Seif getInstance(){
	 return new Seif();
 }
}
