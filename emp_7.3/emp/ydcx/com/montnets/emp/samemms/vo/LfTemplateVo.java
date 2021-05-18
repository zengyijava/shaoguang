/**
 * 
 */
package com.montnets.emp.samemms.vo;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-6-30 下午02:38:16
 * @description 
 */

public class LfTemplateVo implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1083784840930893940L;
	//自增id
	private Long id;
	private Integer shareType;
	private Long tmid;
	//操作员id
    private Long userId;
    //模板名称
    private String tmName;
    //模板内容
    private String tmMsg;
    //类型
    private Long dsflag;
    //状态
    private Long tmState;
    //新增时间
    private Timestamp addtime;


	private Integer isPass;
	//类型
	private Integer tmpType;
	//姓名
    private String name;
    //登录名称
    private String userName;
    //状态
    private Integer userState;
    
    private String depName;
	//模板参数个数
	private Integer paramcnt;
	
	//网关模板ID
	private Long sptemplid;
	
	//网关审批状态
	private Integer auditstatus;
	
	//网关审批错误信息
	private Integer errorcode;
	//网关提交状态
	private Integer submitstatus;
	
	//模块编码
	private String tmCode;
    //对应审核流程id
    private Long flowID;
	
	public String getTmCode() {
		return tmCode;
	}
	public void setTmCode(String tmCode) {
		this.tmCode = tmCode;
	}
	
	public Integer getSubmitstatus() {
		return submitstatus;
	}
	public void setSubmitstatus(Integer submitstatus) {
		this.submitstatus = submitstatus;
	}
	public Integer getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(Integer errorcode) {
		this.errorcode = errorcode;
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

    
    public LfTemplateVo() 
    {
    	//初始化值
    	addtime =  new Timestamp(System.currentTimeMillis()); 
    }
    //获取id
	public Long getTmid()
	{
		return tmid;
	}
	//设置id
	public void setTmid(Long tmid)
	{
		this.tmid = tmid;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getTmName()
	{
		return tmName;
	}

	public void setTmName(String tmName)
	{
		this.tmName = tmName;
	}

	public String getTmMsg()
	{
		return tmMsg;
	}

	public void setTmMsg(String tmMsg)
	{
		this.tmMsg = tmMsg;
	}

	public Long getDsflag()
	{
		return dsflag;
	}

	public void setDsflag(Long dsflag)
	{
		this.dsflag = dsflag;
	}

	public Long getTmState()
	{
		return tmState;
	}

	public void setTmState(Long tmState)
	{
		this.tmState = tmState;
	}

	public Timestamp getAddtime()
	{
		return addtime;
	}

	public void setAddtime(Timestamp addtime)
	{
		this.addtime = addtime;
	}

	public String getName()
	{
		return name;
	}

	public Integer getIsPass()
	{
		return isPass;
	}

	public void setIsPass(Integer isPass)
	{
		this.isPass = isPass;
	}

	public Integer getTmpType()
	{
		return tmpType;
	}

	public void setTmpType(Integer tmpType)
	{
		this.tmpType = tmpType;
	}

	public void setName(String name)
	{
		this.name = name;
	}
  
	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public String getDepName() {
		return depName;
	}
	
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public Integer getParamcnt() {
		return paramcnt;
	}

	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Integer getShareType()
	{
		return shareType;
	}
	public void setShareType(Integer shareType)
	{
		this.shareType = shareType;
	}
	public void setParamcnt(Integer paramcnt) {
		this.paramcnt = paramcnt;
	}
	public String replaceParam(String str)
    {
    	str = str.replaceAll("<", "&lt;");
    	String patternStr="-?#P\\_(\\d+)#-?";
    	Pattern pattern=Pattern.compile(patternStr);
        Matcher matcher=pattern.matcher(str);
        String text = matcher.replaceAll("<div name='-#P-$1#-' class='PraDiv' disable=true>&nbsp;参数$1&nbsp;</div>");
    	return text;
    }

    public Long getFlowID() {
        return flowID;
    }

    public void setFlowID(Long flowID) {
        this.flowID = flowID;
    }
}
