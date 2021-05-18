package com.montnets.emp.engine.biz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Testsss
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String msg = "asdfasfa#W_1#sdfasfasfasf";
		int index = msg.indexOf("#W_");
		if(msg.indexOf("#W_") == -1){
			
		}
		String strMsg = msg.substring(index);
		System.out.println(strMsg);
		String[] msgArray = strMsg.split("#");
		System.out.println(msgArray[1]);
		String wxId = msgArray[1].substring(msgArray[1].indexOf("_")+1);
		System.out.println(wxId);
	}

}
