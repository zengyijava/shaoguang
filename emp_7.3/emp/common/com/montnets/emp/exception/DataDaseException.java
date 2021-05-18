package com.montnets.emp.exception;

public class DataDaseException extends RuntimeException
{

	private static final long serialVersionUID = -5497572870237458411L;
	
	public DataDaseException(String message) {
		super(message);
	}
	
	public DataDaseException(String message, Throwable t) {
		super(message, t);
		this.setStackTrace(t.getStackTrace());
	}
	
	public DataDaseException(Throwable t) {
		super(t);
		this.setStackTrace(t.getStackTrace());
	}

}
