package com.montnets.emp.exception;


public class InvalidVersionNumber extends RuntimeException
{

	private static final long serialVersionUID = -4428837300631842037L;
	
	public InvalidVersionNumber(String message){
		super(message);
	}
	

}
