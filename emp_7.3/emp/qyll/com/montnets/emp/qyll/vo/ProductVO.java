package com.montnets.emp.qyll.vo;

/**
 * @author Jason Huang
 * @date 2017年11月1日 下午2:52:10
 */

public class ProductVO {
	private String productId;
	private String productName;
	private String isp;
	private String count;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

}
