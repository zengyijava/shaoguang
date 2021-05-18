package com.montnets.emp.exception;

public class UnKnowSequenceException extends Exception
{
	private static final long serialVersionUID = -1740982868337929942L;
	/**
	 * 没有序列
	 * @param sequence
	 */
	public UnKnowSequenceException(String sequence){
		super(" unknow sequence : "+sequence);
	}
}
