package com.venus.exception;

public class VenusPropertyNotFound extends Exception {
	public VenusPropertyNotFound() {
	}

	public VenusPropertyNotFound(String message) {
		super(message);
	}

	public VenusPropertyNotFound(Exception e) {
		super(e.getMessage());
	}
}
