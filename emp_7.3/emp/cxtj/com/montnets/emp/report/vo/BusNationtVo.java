package com.montnets.emp.report.vo;

/**
 *  业务类型报表实体类
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 下午07:27:42
 * @description
 */
public class BusNationtVo implements java.io.Serializable{

	private static final long serialVersionUID = -6044320532839571992L;

	private Long iymd;
    //年
	private String y;
    //月
	private String imonth;
	//企业编码
	private String corpCode;
	//账号类型
	private Integer mstype;	
	//数据源类型
	private Integer datasourcetype;
	//业务类型编码
	private String busCode;
	//业务类型名称
	private String busName;
    //提交总数
    private Long icount;
    //接收成功数
    private Long rsucc;
    //发送失败数
    private Long rfail1;
    //接收失败数
    private Long rfail2;
    //未返数
    private Long rnret;
	//开始时间
	private String begintime;
	//结束时间 
	private String endtime;
	
	//报表类型
	private String reportType;
	
	//详细显示标志
	private String detailFlag;
	
	//国家类型类型标志
	private String nationFlag;
	//运营商类型（国内，国外）
	private String spisuncm;
	
	//国家代码，做为传入的查询条件
	private String nationcode;
	//国家名称
	private String nationname;
	//通道号码
	private String spgatecode;
	//通道名称
	private String spgatename;
	//帐号类型
	private Integer sptype;
	//发送类型
	private Integer sendtype;	
	
	
	public BusNationtVo(){
		
	}
	
	
	
	
	
	public Integer getSendtype() {
		return sendtype;
	}





	public void setSendtype(Integer sendtype) {
		this.sendtype = sendtype;
	}





	public Integer getSptype() {
		return sptype;
	}



	public void setSptype(Integer sptype) {
		this.sptype = sptype;
	}





	public String getNationcode() {
		return nationcode;
	}





	public void setNationcode(String nationcode) {
		this.nationcode = nationcode;
	}





	public String getNationname() {
		return nationname;
	}





	public void setNationname(String nationname) {
		this.nationname = nationname;
	}





	public String getSpgatecode() {
		return spgatecode;
	}





	public void setSpgatecode(String spgatecode) {
		this.spgatecode = spgatecode;
	}





	public String getSpgatename() {
		return spgatename;
	}





	public void setSpgatename(String spgatename) {
		this.spgatename = spgatename;
	}





	public String getNationFlag() {
		return nationFlag;
	}

	public void setNationFlag(String nationFlag) {
		this.nationFlag = nationFlag;
	}


	public String getSpisuncm() {
		return spisuncm;
	}


	public void setSpisuncm(String spisuncm) {
		this.spisuncm = spisuncm;
	}




	public String getDetailFlag() {
		return detailFlag;
	}


	public void setDetailFlag(String detailFlag) {
		this.detailFlag = detailFlag;
	}

	public String getReportType() {
		return reportType;
	}

	public String getY() {
		return y;
	}


	public void setY(String y) {
		this.y = y;
	}


	public void setReportType(String reportType) {
		this.reportType = reportType;
	}


	public String getBusName()
	{
		return busName;
	}

	public void setBusName(String busName)
	{
		this.busName = busName;
	}

	public Integer getDatasourcetype()
	{
		return datasourcetype;
	}




	public void setDatasourcetype(Integer datasourcetype)
	{
		this.datasourcetype = datasourcetype;
	}




	public String getBegintime()
	{
		return begintime;
	}


	public void setBegintime(String begintime)
	{
		this.begintime = begintime;
	}


	public String getEndtime()
	{
		return endtime;
	}


	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}


	public Long getIymd()
	{
		return iymd;
	}

	public void setIymd(Long iymd)
	{
		this.iymd = iymd;
	}

	public String getImonth()
	{
		return imonth;
	}

	public void setImonth(String imonth)
	{
		this.imonth = imonth;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}

	public Integer getMstype()
	{
		return mstype;
	}

	public void setMstype(Integer mstype)
	{
		this.mstype = mstype;
	}

	public String getBusCode()
	{
		return busCode;
	}

	public void setBusCode(String busCode)
	{
		this.busCode = busCode;
	}

    public Long getIcount() {
        return icount;
    }

    public void setIcount(Long icount) {
        this.icount = icount;
    }

    public Long getRsucc() {
        return rsucc;
    }

    public void setRsucc(Long rsucc) {
        this.rsucc = rsucc;
    }

    public Long getRfail1() {
        return rfail1;
    }

    public void setRfail1(Long rfail1) {
        this.rfail1 = rfail1;
    }

    public Long getRnret() {
        return rnret;
    }

    public void setRnret(Long rnret) {
        this.rnret = rnret;
    }

    public Long getRfail2()
	{
		return rfail2;
	}

	public void setRfail2(Long rfail2)
	{
		this.rfail2 = rfail2;
	}
	

	
}
