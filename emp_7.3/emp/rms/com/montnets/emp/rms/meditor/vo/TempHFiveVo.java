package com.montnets.emp.rms.meditor.vo;

import java.sql.Timestamp;

/**
 * 用于前端H5接口传递参数以及返回
 * @author dell
 *
 */
public class TempHFiveVo {
	private String hId;
	//标题
	private String title;
	//作者
	private String author;
	//H5内容
	private String content;
	
	//模糊匹配字段
	private String pageFuzzyMatch;
	//状态(0已应用 1应用中 2草稿）（可不传
	private String status;
	//修改时间
	private String updateTimeBeg;
	//修改时间
	private String updateTimeEnd;
	//当前页数
	private String currentPage;
	//每页条数
	private String pageSize;
	
	public String gethId() {
		return hId;
	}
	public void sethId(String hId) {
		this.hId = hId;
	}
	public String getPageFuzzyMatch() {
		return pageFuzzyMatch;
	}
	public void setPageFuzzyMatch(String pageFuzzyMatch) {
		this.pageFuzzyMatch = pageFuzzyMatch;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUpdateTimeBeg() {
		return updateTimeBeg;
	}
	public void setUpdateTimeBeg(String updateTimeBeg) {
		this.updateTimeBeg = updateTimeBeg;
	}
	public String getUpdateTimeEnd() {
		return updateTimeEnd;
	}
	public void setUpdateTimeEnd(String updateTimeEnd) {
		this.updateTimeEnd = updateTimeEnd;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
