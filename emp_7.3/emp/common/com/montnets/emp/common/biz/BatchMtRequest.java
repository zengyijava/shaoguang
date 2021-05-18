package com.montnets.emp.common.biz;

/**
 * 
 * @author Administrator
 *
 */
public class BatchMtRequest {
    //发送调用接口名
	private String command = "BATCH_MT_REQUEST";	
	//发送账号
	private String spid;  
	//发送账号密码
	private String sppassword;  
	//服务代码
	private String spsc;	
	//源地址
	private String sa;   
	//目标地址
	private String da;   
	//相同内容批量下行多个手机号连起来的字符串
	private String das;		
	//不同内容批量下行多个手机号码加不同下行内容连起来的字符串
	private String dasm;	
	//文件群发类型
	private String bmttype;		
	//任务id
	private String taskid;	
	//任务标题
	private String title;	
	//发送内容
	private String content;	
	//消息内容
	private String sm;		
	//发送文件路径
	private String url;		
	//消息编码格式
	private String dc = "15";	
	//优先级
	private String priority;	
	 //定时发送时间
	private String attime;
	//有效时间
	private String validtime;	
	//8字节验证码
	private String verifycode;
	//业务类型
	private String svrtype; 
	//自定义参数1
	private String param1;
	//自定义参数2
	private String param2;
	//自定义参数3
	private String param3;
	//自定义参数4
	private String param4;
    //自定义消息ID
	private String msgid;
    //模块ID
	private String moduleid;
    //是否需要状态报告
	private String rptflag;
	
	/**
	 * 自定义参数1get方法
	 * @return
	 */
	public String getParam1() {
		return param1;
	}
	
	/**
	 * 自定义参数1set方法
	 * @return
	 */
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	
	/**
	 * 消息内容get方法
	 * @return
	 */
	public String getSm()
	{
		return sm;
	}
	
	/**
	 * 消息内容set方法
	 * @return
	 */
	public void setSm(String sm)
	{
		this.sm = sm;
	}
	
	/**
	 * 发送调用接口名get方法
	 * @return
	 */
	public String getCommand() {
		return command;
	}
	
	/**
	 * 发送调用接口名set方法
	 * @return
	 */
	public void setCommand(String command)
	{
		this.command = command;
	}
	
	/**
	 * 发送账号get方法
	 * @return
	 */
	public String getSpid() {
		return spid;
	}
	
	/**
	 * 发送账号set方法
	 * @return
	 */
	public void setSpid(String spid) {
		this.spid = spid;
	}
	
	/**
	 * 发送账号密码get方法
	 * @return
	 */
	public String getSppassword() {
		return sppassword;
	}
	
	/**
	 * 发送账号密码set方法
	 * @return
	 */
	public void setSppassword(String sppassword) {
		this.sppassword = sppassword;
	}
	
	/**
	 * 服务代码get方法
	 * @return
	 */
	public String getSpsc() {
		return spsc;
	}
	
	/**
	 * 服务代码set方法
	 * @return
	 */
	public void setSpsc(String spsc) {
		this.spsc = spsc;
	}
	public String getBmttype() {
		return bmttype;
	}
	public void setBmttype(String bmttype) {
		this.bmttype = bmttype;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDc() {
		return dc;
	}
	public void setDc(String dc) {
		this.dc = dc;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getAttime() {
		return attime;
	}
	public void setAttime(String attime) {
		this.attime = attime;
	}
	public String getValidtime() {
		return validtime;
	}
	public void setValidtime(String validtime) {
		this.validtime = validtime;
	}
	public String getSa()
	{
		return sa;
	}
	public void setSa(String sa)
	{
		this.sa = sa;
	}
	public String getDa()
	{
		return da;
	}
	public void setDa(String da)
	{
		this.da = da;
	}
	public String getDas()
	{
		return das;
	}
	public void setDas(String das)
	{
		this.das = das;
	}
	public String getDasm()
	{
		return dasm;
	}
	public void setDasm(String dasm)
	{
		this.dasm = dasm;
	}
	public String getVerifycode()
	{
		return verifycode;
	}
	public void setVerifycode(String verifycode)
	{
		this.verifycode = verifycode;
	}
	public String getSvrtype() {
		return svrtype;
	}
	public void setSvrtype(String svrtype) {
		this.svrtype = svrtype;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	public String getParam4() {
		return param4;
	}
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	//msgid
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	//模块id
	public String getModuleid() {
		return moduleid;
	}
	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}
	//是否需要状态报告
	public String getRptflag() {
		return rptflag;
	}
	public void setRptflag(String rptflag) {
		this.rptflag = rptflag;
	}
	
	
	
}
