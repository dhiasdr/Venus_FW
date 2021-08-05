package com.venus.exception;

public class ProcessException extends RuntimeException {
	public ProcessException(){}
	public ProcessException(String message){
		super(message);
	}
	public ProcessException(Exception e){
		super(e.getMessage());
	}
}
