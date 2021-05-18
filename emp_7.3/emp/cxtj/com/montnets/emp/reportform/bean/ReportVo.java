package com.montnets.emp.reportform.bean;

import com.montnets.emp.reportform.annotation.RptExportAnnotation;

import java.io.Serializable;

/**
 * ReportVo
 * @author lianghuageng
 * @date 2018/12/10 15:27
 */
public class ReportVo implements Serializable {
    private static final long serialVersionUID = 7857327178754855656L;
    /**
     * 企业编码
     */
    private String corpCode;
    /**
     * 数据源类型
     */
    private Integer datasourcetype;
    /**
     * 业务类型编码
     */
    private String busCode;
    /**
     * 业务类型名称
     */
    @RptExportAnnotation(module = {"busReport"}, index = {1},thName = "业务类型")
    private String busName;
    /**
     * 年
     */
    private Integer y;
    /**
     * 月
     */
    private Integer imonth;
    /**
     * 区域 (省)
     */
    private String province;
    /**
     * 账户名称
     */
    @RptExportAnnotation(module = {"spMtReport"}, index = {2}, thName = "账号名称")
    private String staffname;
    /**
     * 区域 (市)
     */
    private String city;
    /**
     * 省加市的结果集
     */
    @RptExportAnnotation(module = {"areaReport"},index = {1}, thName = "区域")
    private String provinceAndCity;
    /**
     * 接收失败数
     */
    @RptExportAnnotation(module = {"areaReport", "busReport", "sysUserReport", "spisuncmMtReport", "spMtReport", "sysDepReport", "dynParamReport"},index = {3, 5, 5, 4, 7, 5, 4}, thName = "接收失败数")
    private Long rfail2;
    /**
     * 提交总数
     */
    private Long icount;
    /**
     * 接收成功数
     */
    private Long rsucc;

    /**
     * 发送成功数
     */
    @RptExportAnnotation(module = {"areaReport", "busReport","sysUserReport", "spisuncmMtReport", "spMtReport", "sysDepReport", "dynParamReport"},index = {2, 4, 4, 3, 6, 4, 3}, thName = "发送成功数")
    private Long sendSucc;

    /**
     * 发送失败数
     */
    private Long rfail1;
    /**
     * 未返数
     */
    private Long rnret;
    /**
     * 账户类型 0短信 1彩信
     */
    private Integer mstype;
    /**
     * 是否详情查看
     */
    private boolean isDetail;
    /**
     * 时间
     */
    private Integer iymd;
    /**
     * 操作员
     */
    private String userIdStr;
    /**
     * 操作员的SP账号
     */
    @RptExportAnnotation(module = {"spMtReport"}, index = {1},thName = "sp账号")
    private String spUserId;
    /**
     * 包含子机构
     */
    private boolean containSubDep;
    /**
     * 机构 ID
     */
    private String orgId;
    /**
     * 业务类型
     */
    private String svrType;
    /**
     * 报表类型
     */
    private Integer reportType;
    /**
     * 运营商类型（移动，联通，电信，国外）
     */
    private Integer spisuncm;
    /**
     * 国家代码，做为传入的查询条件
     */
    private String nationcode;
    /**
     * 国家名称
     */
    private String nationname;
    /**
     * 通道号码
     */
    private String spgatecode;
    /**
     * 通道名称
     */
    private String spgatename;
    /**
     * 帐号类型
     */
    private Integer sptype;
    /**
     * 发送类型
     */
    private Integer sendtype;

    /**
     * 查询时间
     */
    private String queryTime;

    /**
     * 区域信息
     */
    private String provinces;

    /**
     * 自定义参数
     */
    private String param;
    /**
     * 参数名称
     */
    @RptExportAnnotation(module = {"dynParamReport"},index = {1}, thName = "参数名称")
    private String paramName;
    /**
     * 参数subNum
     */
    private String paramNum;
    /**
     * 参数值
     */
    @RptExportAnnotation(module = {"dynParamReport"},index = {2}, thName = "参数值")
    private String paramValue;
    /**
     * 页面显示时间
     */
    @RptExportAnnotation(module = {"areaReport", "spisuncmMtReport", "spMtReport", "sysDepReport", "dynParamReport", "busReport", "sysUserReport"},index = {0, 0, 0, 0, 0, 0, 0}, thName = "时间")
    private String queryTimeStr;
    /**
     * 运营商名称
     */
    @RptExportAnnotation(module = {"sysUserReport","spisuncmMtReport", "spMtReport", "busReport", "sysDepReport"},index = {3,2,5,3,3}, thName = "运营商")
    private String operatorName;
    /**
     * 发送类型名称
     */
    @RptExportAnnotation(module = {"spMtReport" , "busReport", "sysDepReport"}, index = {4,2,2}, thName = "发送类型")
    private String sendName;
    /**
     * 运营商ID
     */
    private String spId;
    /**
     * 运营商ID名称
     */
    @RptExportAnnotation(module = {"spisuncmMtReport"},index = {1},thName = "运营商ID")
    private String spIdName;
    /**
     * 用户userid
     */
    private String userId;
    /**
     * 账号类型名称
     */
    @RptExportAnnotation(module = {"spMtReport"}, index = {3}, thName = "账号类型")
    private String spName;
    /**
     * 操作员状态 0禁用 1启用 2 注销 3锁定
     */
    private Integer userState;
    /**
     * 操作员名字
     */
    @RptExportAnnotation(module = {"sysUserReport"},index = {1}, thName = "操作员")
    private String name;
    /**
     * 机构名字
     */
    @RptExportAnnotation(module = {"sysUserReport"},index = {2}, thName = "机构")
    private String depName;
    /**
     * 多个sp账号
     */
    private String spUsers;
    /**
     * 统计来源(1.机构，2.操作员，3.接口)
     */
    private Integer idtype;

    private Integer depId;
    @RptExportAnnotation(module ={"sysDepReport"}, index = {1}, thName = "机构/操作员")
    private String depOrUserName;
    /**
     * 当前操作员可以查看的SP账号
     */
    private String currentSpUser;

    public String getCurrentSpUser() {
        return currentSpUser;
    }

    public void setCurrentSpUser(String currentSpUser) {
        this.currentSpUser = currentSpUser;
    }

    public ReportVo() {
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamNum() {
        return paramNum;
    }

    public void setParamNum(String paramNum) {
        this.paramNum = paramNum;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getProvinceAndCity() {
        return provinceAndCity;
    }

    public void setProvinceAndCity(String provinceAndCity) {
        this.provinceAndCity = provinceAndCity;
    }

    public Long getSendSucc() {
        return sendSucc;
    }

    public void setSendSucc(Long sendSucc) {
        this.sendSucc = sendSucc;
    }

    public String getQueryTimeStr() {
        return queryTimeStr;
    }

    public void setQueryTimeStr(String queryTimeStr) {
        this.queryTimeStr = queryTimeStr;
    }

    public String getStaffname() {
        return staffname;
    }

    public void setStaffname(String staffname) {
        this.staffname = staffname;
    }

    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public Integer getDatasourcetype() {
        return datasourcetype;
    }

    public void setDatasourcetype(Integer datasourcetype) {
        this.datasourcetype = datasourcetype;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getImonth() {
        return imonth;
    }

    public void setImonth(Integer imonth) {
        this.imonth = imonth;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getRfail2() {
        return rfail2;
    }

    public void setRfail2(Long rfail2) {
        this.rfail2 = rfail2;
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

    public Integer getMstype() {
        return mstype;
    }

    public void setMstype(Integer mstype) {
        this.mstype = mstype;
    }

    public Integer getIymd() {
        return iymd;
    }

    public void setIymd(Integer iymd) {
        this.iymd = iymd;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getSpUserId() {
        return spUserId;
    }

    public void setSpUserId(String spUserId) {
        this.spUserId = spUserId;
    }

    public boolean isContainSubDep() {
        return containSubDep;
    }

    public void setContainSubDep(boolean containSubDep) {
        this.containSubDep = containSubDep;
    }

    public String getSvrType() {
        return svrType;
    }

    public void setSvrType(String svrType) {
        this.svrType = svrType;
    }

    public Integer getSpisuncm() {
        return spisuncm;
    }

    public void setSpisuncm(Integer spisuncm) {
        this.spisuncm = spisuncm;
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

    public Integer getSptype() {
        return sptype;
    }

    public void setSptype(Integer sptype) {
        this.sptype = sptype;
    }

    public Integer getSendtype() {
        return sendtype;
    }

    public void setSendtype(Integer sendtype) {
        this.sendtype = sendtype;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isDetail() {
        return isDetail;
    }

    public void setDetail(boolean detail) {
        isDetail = detail;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public Integer getUserState() {
        return userState;
    }

    public void setUserState(Integer userState) {
        this.userState = userState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getSpUsers() {
        return spUsers;
    }

    public void setSpUsers(String spUsers) {
        this.spUsers = spUsers;
    }

    public String getSpIdName() {
        return spIdName;
    }

    public void setSpIdName(String spIdName) {
        this.spIdName = spIdName;
    }

    public Integer getIdtype() {
        return idtype;
    }

    public void setIdtype(Integer idtype) {
        this.idtype = idtype;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }

    public String getDepOrUserName() {
        return depOrUserName;
    }

    public void setDepOrUserName(String depOrUserName) {
        this.depOrUserName = depOrUserName;
    }
}
