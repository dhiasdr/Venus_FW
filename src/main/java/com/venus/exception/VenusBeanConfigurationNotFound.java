package com.venus.exception;

public class VenusBeanConfigurationNotFound extends Exception{
	public VenusBeanConfigurationNotFound(){}
	public VenusBeanConfigurationNotFound(String message){
		super(message);
	}
	public VenusBeanConfigurationNotFound(Exception e){
		super(e.getMessage());
	}
}
