/**
 * 
 */
package com.montnets.emp.netnews.vo;

/**
 * 页面传值实体
 * @author Administrator
 *
 */
public class PageNameVo {
	private Long id;
	private String pageName;
	private int visitcount;
	private int visitpep;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public int getVisitcount() {
		return visitcount;
	}
	public void setVisitcount(int visitcount) {
		this.visitcount = visitcount;
	}
	public int getVisitpep() {
		return visitpep;
	}
	public void setVisitpep(int visitpep) {
		this.visitpep = visitpep;
	}
}
