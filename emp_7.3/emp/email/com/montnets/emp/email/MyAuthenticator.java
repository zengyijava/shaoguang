package com.montnets.emp.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 向邮件服务器提交认证信息
 */ 
public class MyAuthenticator extends Authenticator {
	private String username ;

	private String password ;

	public MyAuthenticator() {
		super();
	}

	public MyAuthenticator(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {

		return new PasswordAuthentication(username, password);
	}
}
