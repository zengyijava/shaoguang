package com.montnets.emp.znly.biz;

import java.util.HashMap;

import com.montnets.emp.entity.wxgl.LfWeiAccount;

public class CustomThread implements Runnable {

	private LfWeiAccount acct;
	  HashMap<String, String>  paramsXmlMap;
	  public void setAcct(LfWeiAccount acct) 
	  { 
	  this.acct = acct; 
	  } 
	  public void setXmlMap(HashMap<String, String> paramsXmlMap) 
	  { 
	  this.paramsXmlMap = paramsXmlMap; 
	  } 
   public void run(){
	   CustomChatBiz.doPush(acct, paramsXmlMap);
   }

}
