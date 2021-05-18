package com.montnets.emp.util;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;

/**
 * 分页
 * @author Administrator
 *
 */
public class PageInfo implements java.io.Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//总页数
	 private int totalPage = 1;
     //上一页
	 private int prePage = 1;
     //下一页
	 private int nextPage = 1;
     //总记录数
	 private int totalRec = 0;
     //默认一页条数
	 private final int defaultPageSize = Integer.parseInt(SystemGlobals.getValue(StaticValue.EMP_PAGEINFO_DEFAULTPAGESIZE));
     //一页数
	 private int pageSize = defaultPageSize;
     //当前页
	 private int pageIndex = 1;

	 private int[] pageNumbers;
	 
	 //是否需要新分页信息。1-需要；2-不需要
	 private int needNewData = 1;
	 
	/**
	  * 是否需要新分页信息。1-需要；2-不需要
	  * @return
	  */
	public int getNeedNewData()
	{
		return needNewData > 0 ? needNewData : 1;
	}

	/**
	  * 是否需要新分页信息。1-需要；2-不需要
	  * @return
	  */
	public void setNeedNewData(int needNewData)
	{
		this.needNewData = needNewData > 0 ? needNewData : 1;
	}

	 public int getPageIndex() {
	  return pageIndex;
	 }

	 public void setPageIndex(int pageIndex) {
	  this.pageIndex = pageIndex > 0 ? pageIndex : 1;
	 }

	 public int getNextPage() {
	  return nextPage;
	 }

	 public void setNextPage(int nextPage) {
	  this.nextPage = nextPage > this.totalPage ? this.totalPage : nextPage;
	 }

	 public int getPageSize() {
	  return pageSize;
	 }

	 public void setPageSize(int pageSize) {
	  this.pageSize = pageSize > 0 ? pageSize : defaultPageSize;
	 }

	 public int getPrePage() {
	  return prePage;
	 }

	 public void setPrePage(int prePage) {
	  this.prePage = prePage < 1 ? 1 : prePage;
	 }

	 public int getTotalPage() {
	  return totalPage;
	 }

	 public void setTotalPage(int totalPage) {
	  this.totalPage = totalPage > 0 ? totalPage : 1;
	 }

	 public int getTotalRec() {
	  return totalRec;
	 }

	 public void setTotalRec(int totalRec) {
	  this.totalRec = totalRec > -1 ? totalRec : 0;
	 }

	 public int[] getPageNumbers() {
	  return pageNumbers;
	 }

	 public void setPageNumbers(int[] pageNumbers) {
	  this.pageNumbers = pageNumbers;
	 }
}
