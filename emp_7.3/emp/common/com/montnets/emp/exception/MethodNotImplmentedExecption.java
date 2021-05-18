package com.montnets.emp.exception;

public class MethodNotImplmentedExecption extends RuntimeException
{

	private static final long serialVersionUID = 7276199077832691747L;
	
	public MethodNotImplmentedExecption()
	{
		this("子类需重写此方法");
	}
	
	/**
	 * 找不到方法
	 * @param string
	 */
	public MethodNotImplmentedExecption(String message)
	{
		super(message);
	}

	

}
