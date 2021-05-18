package com.montnets.emp.entity.template;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短彩模板实体类
 * @author zm
 *
 */
public class LfTemplate {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6395344328891027683L;

	/**
	 * 
	 */
	// private static final long serialVersionUID = -6980913232678879079L;
	//主键
	private Long tmid;  

	 //创建人(外键)lf_sysuser.user_id
	private Long userId;  

	//模板名称
	private String tmName;   

	//模板内容
	private String tmMsg;   

	//模板类型(1.通用动态模块;0.通用静态模块;2.智能抓取模块;3.移动财务模块)
	private Long dsflag;  

	//模板状态(1.启用，0.禁用,2.草稿)
	private Long tmState;   

	//创建时间
	private Timestamp addtime;   

	//审批状态（-1.未审批；0.无需审批；1.审批通过；2.审批未通过）
	private Integer isPass;

	//模板（3-短信模板;4-彩信模板）
	private Integer tmpType;
	
	//业务编码
	private String bizCode;  
	
	//企业编号（lf_sysuser.corpCode）
	private String corpCode;  
	//彩信平台模板ID，值为0时表示未上传
	private Long sptemplid;
	//网关审核状态，（－1.未审批；0.无需审批；1.审批通过；2.审批未通过；3审核中；4禁用;5-删除）
	private Integer auditstatus;
	//关联彩信模板表MMS_TEMPLATE的Id字段
	private Long mmstmplid;
	//网关彩信模板状态（0：正常，可用	1：锁定，暂时不可用	2：永久锁定，永远不可用）
	private Integer tmplstatus;
	//模板参数个数
	private Integer paramcnt;
	//提交状态 	0：未提交	1：提交成功	2：提交失败	3；提交中
	private Integer submitstatus;
	//EMP系统模块ID
	private String emptemplid;
	//网关错误编码
	private Integer errorcode;
	//模块编码
	private String tmCode;
	
	//档位
	private Integer degree = 0;
	
	//容量
	private Long degreeSize = 0L;
	//行业id
	private Integer industryid = -1;
	//用途id
	private Integer useid = -2;
	
	//是否是公共模板 0为不是 1为是
	private Integer isPublic = 0;
	//模板使用次数
	private Long usecount = 0L;
	
	//动态模板生成excel的表头json
	private String exlJson = "";
	
	// 富信模板版本
	private String ver = "";

	//富信发送接口中参数个数
	private Integer paramsnum = 0;

	//是否是快捷场景  0为否 1为是
	private Integer isShortTemp = 0;

	//模板来源 1-托管版EMP,2-标准版EMP,3-RCOS ,4-富信模板接口,5-其它
	private Integer source = 0;

	//是否RCOS 同步来的公共素材 0-否，1-是
	private Integer isMaterial = 0;

	//RCOS 平台同步过来的启用禁用状态 0 -禁用，1-启用
	private Integer rcosTmpState = 1;



	public String toString() {
		//增加content字段，用于页面处理
		String json="{tmid:%d,tmname:'%s'}";
		
		return String.format(json,this.getTmid(), this.getTmName());
	}

	public Integer getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(Integer errorcode) {
		this.errorcode = errorcode;
	}

	public LfTemplate()
	{
		addtime = new Timestamp(System.currentTimeMillis());
	}
	
	public String getEmptemplid() {
		return emptemplid;
	}

	public void setEmptemplid(String emptemplid) {
		this.emptemplid = emptemplid;
	}

	public Integer getSubmitstatus() {
		return submitstatus;
	}

	public void setSubmitstatus(Integer submitstatus) {
		this.submitstatus = submitstatus;
	}

	public Integer getParamcnt() {
		return paramcnt;
	}

	public void setParamcnt(Integer paramcnt) {
		this.paramcnt = paramcnt;
	}

	public Long getSptemplid() {
		return sptemplid;
	}
	public void setSptemplid(Long sptemplid) {
		this.sptemplid = sptemplid;
	}


	public Integer getAuditstatus() {
		return auditstatus;
	}
	public void setAuditstatus(Integer auditstatus) {
		this.auditstatus = auditstatus;
	}

	public Long getMmstmplid() {
		return mmstmplid;
	}

	public void setMmstmplid(Long mmstmplid) {
		this.mmstmplid = mmstmplid;
	}

	public Integer getTmplstatus() {
		return tmplstatus;
	}

	public void setTmplstatus(Integer tmplstatus) {
		this.tmplstatus = tmplstatus;
	}

	//主键
	public Long getTmid()
	{
		return tmid;
	}

	public void setTmid(Long tmid)
	{
		this.tmid = tmid;
	}

	//创建人(外键)lf_sysuser.user_id
	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	//模板名称
	public String getTmName()
	{
		return tmName;
	}

	public void setTmName(String tmName)
	{
		this.tmName = tmName;
	}

	//模板内容
	public String getTmMsg()
	{
		return tmMsg;
	}

	public void setTmMsg(String tmMsg)
	{
		this.tmMsg = tmMsg;
	}

	//模板类型(1.通用动态模块;0.通用静态模块;2.智能抓取模块;3.移动财务模块)
	public Long getDsflag()
	{
		return dsflag;
	}

	public void setDsflag(Long dsflag)
	{
		this.dsflag = dsflag;
	}

	//模板状态(1.启用，0.禁用)
	public Long getTmState()
	{
		return tmState;
	}

	public void setTmState(Long tmState)
	{
		this.tmState = tmState;
	}

	//创建时间
	public Timestamp getAddtime()
	{
		return addtime;
	}

	public void setAddtime(Timestamp addtime)
	{
		this.addtime = addtime;
	}

	
	//审批状态（-1.未审批；0.无需审批；1.审批通过；2.审批未通过）
	public Integer getIsPass()
	{
		return isPass;
	}

	public void setIsPass(Integer isPass)
	{
		this.isPass = isPass;
	}

	//模板（3-短信模板;4-彩信模板）
	public Integer getTmpType()
	{
		return tmpType;
	}

	public void setTmpType(Integer tmpType)
	{
		this.tmpType = tmpType;
	}

	//业务编码
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getBizCode() {
		return bizCode;
	}

	public String replaceParam(String str)
	{
		str = str.replaceAll("<", "&lt;");
		String patternStr = "-?#P\\_(\\d+)#-?";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(str);
		String text = matcher
				.replaceAll("<div name='-#P-$1#-' class='PraDiv' disable=true>&nbsp;����$1&nbsp;</div>");
		return text;
	}

	//企业编号（lf_sysuser.corpCode）
	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getTmCode() {
		return tmCode;
	}

	public void setTmCode(String tmCode) {
		this.tmCode = tmCode;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public Long getDegreeSize() {
		return degreeSize;
	}

	public void setDegreeSize(Long degreeSize) {
		this.degreeSize = degreeSize;
	}

	public Integer getIndustryid() {
		return industryid;
	}

	public void setIndustryid(Integer industryid) {
		this.industryid = industryid;
	}

	public Integer getUseid() {
		return useid;
	}

	public void setUseid(Integer useid) {
		this.useid = useid;
	}

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public Long getUsecount() {
		return usecount;
	}

	public void setUsecount(Long usecount) {
		this.usecount = usecount;
	}

	public String getExlJson() {
		return exlJson;
	}

	public void setExlJson(String exlJson) {
		this.exlJson = exlJson;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public Integer getParamsnum() {
		return paramsnum;
	}

	public void setParamsnum(Integer paramsnum) {
		this.paramsnum = paramsnum;
	}


	public Integer getIsShortTemp() {
		return isShortTemp;
	}

	public void setIsShortTemp(Integer isShortTemp) {
		this.isShortTemp = isShortTemp;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getIsMaterial() {
		return isMaterial;
	}

	public void setIsMaterial(Integer isMaterial) {
		this.isMaterial = isMaterial;
	}

	public Integer getRcosTmpState() {
		return rcosTmpState;
	}

	public void setRcosTmpState(Integer rcosTmpState) {
		this.rcosTmpState = rcosTmpState;
	}
}
