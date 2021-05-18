package com.montnets.emp.appmage.httpinterface;


public class AppHttpParam
{
	private String respCommand;
	
	private String errcode = "";
	
	private String mtmsgid = "";
	
	private String params = "";

	
	public String getRespCommand()
	{
		return respCommand;
	}

	public void setRespCommand(String respCommand)
	{
		this.respCommand = respCommand;
	}

	public String getErrcode()
	{
		return errcode;
	}

	public void setErrcode(String errcode)
	{
		this.errcode = errcode;
	}

	public String getMtmsgid()
	{
		return mtmsgid;
	}

	public void setMtmsgid(String mtmsgid)
	{
		this.mtmsgid = mtmsgid;
	}

	public String getParams()
	{
		return params;
	}

	public void setParams(String params)
	{
		this.params = params;
	}

	
}
