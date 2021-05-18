package com.montnets.emp.qyll.vo;
/**
 * 操作员统计报表实体类
 * @project p_qyll
 * @author pengyc
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2017-10-23 下午02:40:32
 * @description
 */
public class LlOperatorReportVo {
	//流量套餐(产品名称)
	private String productName;
	//流量套餐编号(产品编号)
	private String productId;
	//运营商
	private String isp;
	//报表类型
	private String reportType;
	//日报表初始时间
	private String sendtime;
	//日报表结束时间
	private String recvtime;
	//年、月表时间
	private String statisticsTime;
	//提交号码数
	private Integer sunMitNum;
	//订购成功数
	private Integer succNum;
	//订购失败数
	private Integer faildNum;
	//发起成功数
	private Integer sendSuccNum;
	//提交号码总数
	private Long sunMitNumSum;
	//订购成功总数
	private Long succNumSum;
	//订购失败总数
	private Long faildNumSum;
	//时间
	private String showTime;
	//详情
	private String isDel;
	// 是否第一次访问
	private	String isFirstEnter;
	//订购时间
	private	String reportDate;
	//操作员ID组合字符串
	private String userName;
	//机构
	private String depNam;
	//操作员
	private String UName;
	// 部门ID组合字符串
	private String depids;
	//企业编码
	private Integer ecid;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getIsp() {
		return isp;
	}
	public void setIsp(String isp) {
		this.isp = isp;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public String getRecvtime() {
		return recvtime;
	}
	public void setRecvtime(String recvtime) {
		this.recvtime = recvtime;
	}
	public String getStatisticsTime() {
		return statisticsTime;
	}
	public void setStatisticsTime(String statisticsTime) {
		this.statisticsTime = statisticsTime;
	}
	public Integer getSunMitNum() {
		return sunMitNum;
	}
	public void setSunMitNum(Integer sunMitNum) {
		this.sunMitNum = sunMitNum;
	}
	public Integer getSuccNum() {
		return succNum;
	}
	public void setSuccNum(Integer succNum) {
		this.succNum = succNum;
	}
	public Integer getFaildNum() {
		return faildNum;
	}
	public void setFaildNum(Integer faildNum) {
		this.faildNum = faildNum;
	}
	public Integer getSendSuccNum() {
		return sendSuccNum;
	}
	public void setSendSuccNum(Integer sendSuccNum) {
		this.sendSuccNum = sendSuccNum;
	}
	public Long getSunMitNumSum() {
		return sunMitNumSum;
	}
	public void setSunMitNumSum(Long sunMitNumSum) {
		this.sunMitNumSum = sunMitNumSum;
	}
	public Long getSuccNumSum() {
		return succNumSum;
	}
	public void setSuccNumSum(Long succNumSum) {
		this.succNumSum = succNumSum;
	}
	public Long getFaildNumSum() {
		return faildNumSum;
	}
	public void setFaildNumSum(Long faildNumSum) {
		this.faildNumSum = faildNumSum;
	}
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	public String getIsFirstEnter() {
		return isFirstEnter;
	}
	public void setIsFirstEnter(String isFirstEnter) {
		this.isFirstEnter = isFirstEnter;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDepNam() {
		return depNam;
	}
	public void setDepNam(String depNam) {
		this.depNam = depNam;
	}
	public String getUName() {
		return UName;
	}
	public void setUName(String uName) {
		UName = uName;
	}
	public String getDepids() {
		return depids;
	}
	public void setDepids(String depids) {
		this.depids = depids;
	}
	public Integer getEcid() {
		return ecid;
	}
	public void setEcid(Integer ecid) {
		this.ecid = ecid;
	}
	
}
