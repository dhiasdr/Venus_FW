package com.venus.exception;

public class VenusConfigurationException extends Exception{
	public VenusConfigurationException(){}
	public VenusConfigurationException(String message){
		super(message);
	}
	public VenusConfigurationException(Exception e){
		super(e.getMessage());
	}
}
