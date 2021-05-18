package com.montnets.emp.common.constant;

public class EMPException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EMPException()
	{
		super();
	}
	
	public EMPException(String msg)
	{
		super(msg);
	}
	
	public EMPException(Exception e)
	{
		super(e);
	}
	
	public EMPException(String msg, Exception e)
	{
		super(msg,e);
	}
}
