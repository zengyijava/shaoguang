package com.montnets.emp.ottbase.constant;

public class OttException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OttException()
	{
		super();
	}
	
	public OttException(String msg)
	{
		super(msg);
	}
	
	public OttException(Exception e)
	{
		super(e);
	}
	
	public OttException(String msg, Exception e)
	{
		super(msg,e);
	}
}
