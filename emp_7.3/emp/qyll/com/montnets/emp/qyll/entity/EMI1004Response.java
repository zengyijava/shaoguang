package com.montnets.emp.qyll.entity;

/**
 * 用于解析余额查询的回应报文
 * @author xiebk
 *
 */
public class EMI1004Response {
	private String CorpCode;
	private String Balance;
	public String getCorpCode() {
		return CorpCode;
	}
	public void setCorpCode(String corpCode) {
		CorpCode = corpCode;
	}
	public String getBalance() {
		return Balance;
	}
	public void setBalance(String balance) {
		Balance = balance;
	}
	@Override
	public String toString() {
		return "EMI1004Response [CorpCode=" + CorpCode + ", Balance=" + Balance
				+ "]";
	}

	
	
}
