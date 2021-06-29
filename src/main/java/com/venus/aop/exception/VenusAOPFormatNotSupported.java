package com.venus.aop.exception;

public class VenusAOPFormatNotSupported extends Exception {
	public VenusAOPFormatNotSupported() {
	}

	public VenusAOPFormatNotSupported(String message) {
		super(message);
	}

	public VenusAOPFormatNotSupported(Exception e) {
		super(e.getMessage());
	}
}
