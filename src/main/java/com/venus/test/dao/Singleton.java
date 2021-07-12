package com.venus.test.dao;

public class Singleton {
	// static variable single_instance of type Singleton
	private static Singleton single_instance = null;

	// variable of type String
	public String s;

	// private constructor restricted to this class itself
	private Singleton() {
		s = "Hello I am a string part of Singleton class";
	}

	// static method to create instance of Singleton class
	public static Singleton createInstance() {
		// To ensure only one instance is created
		if (single_instance == null) {
			single_instance = new Singleton();
		}
		return single_instance;
	}
}
