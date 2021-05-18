package com.montnets.emp.qyll.entity;

import java.sql.Timestamp;

/**
 * 企业流量套餐信息
 * @author Administrator
 *
 */
public class LlProduct {
	//自增ID
	private Integer id; 
	//企业编号
	private Integer ecid; 
	//产品编号
    private String productid;
    //产品名称
    private String productname; 
    //运营商品牌
    private String isp;
    //套餐大小
    private String volume; 
    //价格，如：10.0
    private Double price; 
    //折扣价，如：9.0
    private Double discprice;
    //可服务区域
    private String area;
    //产品类型
    private Integer ptype;
    //产品属性当ProductType=0的时候才有此字段 0:后向1:前向
    private Integer  pmold; 
    //资源属性 当ProductType=0的时候才有此字段  0-国网包 1-省漫包2-省内包 3-日包 4-三日包 5-周包
    private Integer rtype; 
    //套餐使用情况：0、过期，1、使用中2、禁用
    private Integer status; 
    //更新时间，最后一次更新时间即过期时间
    private Timestamp updatetm; 
    //创建时间
    private Timestamp createtm;
    //操作员ID
    private Integer operatorid;
    //判断是否是第一次进入 0 是 1不是
    private String isFirstEnter;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getEcid() {
		return ecid;
	}
	public void setEcid(Integer ecid) {
		this.ecid = ecid;
	}
	public String getProductid() {
		return productid;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getIsp() {
		return isp;
	}
	public void setIsp(String isp) {
		this.isp = isp;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getDiscprice() {
		return discprice;
	}
	public void setDiscprice(Double discprice) {
		this.discprice = discprice;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Integer getPtype() {
		return ptype;
	}
	public void setPtype(Integer ptype) {
		this.ptype = ptype;
	}
	public Integer getPmold() {
		return pmold;
	}
	public void setPmold(Integer pmold) {
		this.pmold = pmold;
	}
	public Integer getRtype() {
		return rtype;
	}
	public void setRtype(Integer rtype) {
		this.rtype = rtype;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Timestamp getUpdatetm() {
		return updatetm;
	}
	public void setUpdatetm(Timestamp updatetm) {
		this.updatetm = updatetm;
	}
	public Timestamp getCreatetm() {
		return createtm;
	}
	public void setCreatetm(Timestamp createtm) {
		this.createtm = createtm;
	}
	public Integer getOperatorid() {
		return operatorid;
	}
	public void setOperatorid(Integer operatorid) {
		this.operatorid = operatorid;
	}
	public String getIsFirstEnter() {
		return isFirstEnter;
	}
	public void setIsFirstEnter(String isFirstEnter) {
		this.isFirstEnter = isFirstEnter;
	}
	@Override
	public String toString() {
		return "LlProduct [id=" + id + ", ecid=" + ecid + ", productid="
				+ productid + ", productname=" + productname + ", isp=" + isp
				+ ", volume=" + volume + ", price=" + price + ", discprice="
				+ discprice + ", area=" + area + ", ptype=" + ptype
				+ ", pmold=" + pmold + ", rtype=" + rtype + ", status="
				+ status + ", updatetm=" + updatetm + ", createtm=" + createtm
				+ ", operatorid=" + operatorid + ", isFirstEnter="
				+ isFirstEnter + "]";
	}
	
}
