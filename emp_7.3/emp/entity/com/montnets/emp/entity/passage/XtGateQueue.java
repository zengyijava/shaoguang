package com.montnets.emp.entity.passage;


 
/**
 * TableXtGateQueue对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:21:50
 * @description 
 */
public class XtGateQueue implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5340274530504595699L;
	//标识 自增ID
	private Long id;
	//主端口
	private String spgate;
	//状态
	private Integer status;
	//运营商（0 移动 1 联通 21 电信）
	private Integer spisuncm;
	//通道类型
	private Integer gateType;
	//通道名称
	private String gateName;
	//端口类型
	private Integer portType;
	//单条短信长度
	private Integer singleLen;
	//企业签名
	private String signstr;
	//SP通道优先级（0~9）
	private Integer riseLevel;
	//速率
	private Long speed;
	//是否支持长短信0支持  1 不支持
	private Long longSms;
	//短信最大长度 
	private Integer maxWords;
	//最大SP扩展位数
	private Integer sublen;
	//扣费类型(1:预付费,2:后付费)
	private Integer feeFlag;
	//短信签名长度(有自动计算与固定长度，为固定长度时有1位至8位选择，
	//并且短信签名长度不能大于固定长度，为自动计算时自动计算输入的签名长度值，但最大不能大于8个字符)
	private Integer signlen;
	//费率
	private Double fee;

	//private String gateInfo;
	//截取短信规则
	private Integer splitRule;
	
	//private Integer wordLen;
	
	//private Integer showFlag;
	
	//private Integer sortId;
	
	private Integer multilen1;
	
	private Integer multilen2;
	
	//private Integer gateNo;
	
	private Integer signType;
	
	private Integer signFixLen;
	
	//private Integer preFixLen;
	
	//private Integer maxLongMsgEq;
	
	private Integer endSplit;
	
	//private Integer gateSeq;
	
	private Integer signDropType;
	
	//private String gateArea;
	
	private Integer areaType;
	
	private Integer eachSign;
	//用位进行表示:	第一位：无需过滤黑名单	第二位：支持0编码短信
	//第三位：前置签名   1:前置  0:后置，默认后置即该位为0。
	//其他位待定。
	private Integer gateprivilege;
	//英文签名
	private String ensignstr;
	//英文签名长度
	private Integer ensignlen;
	//英文长短信头字节数
	private Integer enprefixlen;
	//英文短信最大字数
	private Integer enmaxwords;
	//单条英文短信字数
	private Integer ensinglelen;
	//英文长短信第一条至N-1 条短信内容长度
	private Integer enmultilen1;
	//英文长短信最后一条短信内容长度
	private Integer enmultilen2;
	//构建函数
	public XtGateQueue() {
		//初始化变量
		this.spgate=" ";
		this.status=new Integer(0);
//		this.gateType=2;
		this.gateName=" ";
		this.portType=new Integer(0);
		this.singleLen=70;
		this.signstr=" ";
		this.riseLevel = new Integer(0);
		this.speed = new Long(60);
		this.longSms = new Long(0);
		this.maxWords = new Integer(0);
		this.sublen = new Integer(0);
		this.feeFlag = new Integer(2);
		this.signlen= new Integer(8);
		this.fee =new Double(0.00);
		//this.gateInfo = " ";
		this.splitRule = new Integer(1);
		//this.wordLen = new Integer(70);
		//this.showFlag = new Integer(1);
		//this.sortId = new Integer(1);
		this.multilen1 = new Integer(67);
		this.multilen2 = new Integer(67);
		//this.gateNo = new Integer(0);
		this.signType = new Integer(0);
		this.signFixLen = new Integer(10);
		//this.preFixLen = new Integer(3);
		//this.maxLongMsgEq = new Integer(0);
		this.endSplit = new Integer(1);
		//this.gateSeq = new Integer(0);
		this.signDropType = new Integer(1);
		//this.gateArea = " ";
		this.areaType = new Integer(0);
		this.eachSign = new Integer(0);
		this.gateprivilege=new Integer(0);
		this.ensignstr = " ";
		this.ensignlen = 10;
		this.enprefixlen = 6;
		this.enmaxwords = 2000;
		this.ensinglelen = 160;
		this.enmultilen1 = 153;
		this.enmultilen2 = 143;
	}
 
	public Integer getGateprivilege()
	{
		return gateprivilege;
	}

	public void setGateprivilege(Integer gateprivilege)
	{
		this.gateprivilege = gateprivilege;
	}

	//获取标识
	public Long getId()
	{
		return id;
	}

	//设置标识
	public void setId(Long id)
	{
		this.id = id;
	}

	//获取主端口号
	public String getSpgate() {
		return spgate;
	}

	//设置主端口号
	public void setSpgate(String spgate) {
		this.spgate = spgate;
	}

 
	//获取状态
	public Integer getStatus()
	{
		return status;
	}

	//设置状态
	public void setStatus(Integer status)
	{
		this.status = status;
	}
//获取运营商
	public Integer getSpisuncm() {
		return spisuncm;
	}

	//设置运营商
	public void setSpisuncm(Integer spisuncm) {
		this.spisuncm = spisuncm;
	}

 
	//获取通道名
	public String getGateName() {
		return gateName;
	}
	//设置通道名称
	public void setGateName(String gateName) {
		this.gateName = gateName;
	}
	//获取企业签名
	public String getSignstr() {
		return signstr;
	}
	//设置企业签名
	public void setSignstr(String signstr) {
		this.signstr = signstr;
	}
	//获取发送速率
	public Long getSpeed() {
		return speed;
	}
	//设置发送速率
	public void setSpeed(Long speed) {
		this.speed = speed;
	}
	//获取短信长度
	public Long getLongSms() {
		return longSms;
	}
	//设置短信长度
	public void setLongSms(Long longSms) {
		this.longSms = longSms;
	}
	//设计费率
	public Integer getFeeFlag() {
		return feeFlag;
	}

	public void setFeeFlag(Integer feeFlag) {
		this.feeFlag = feeFlag;
	}
//设置单条短信长度
	public Integer getSingleLen() {
		return singleLen;
	}
	//获取单条短信长度
	public void setSingleLen(Integer singleLen) {
		this.singleLen = singleLen;
	}

	public Integer getSignlen() {
		return signlen;
	}

	public void setSignlen(Integer signlen) {
		this.signlen = signlen;
	}

	//获取费率
	public Double getFee() {
		return fee;
	}
	//设置费率
	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Integer getGateType() {
		return gateType;
	}

	public void setGateType(Integer gateType) {
		this.gateType = gateType;
	}

	public Integer getPortType() {
		return portType;
	}

	public void setPortType(Integer portType) {
		this.portType = portType;
	}

	public Integer getRiseLevel() {
		return riseLevel;
	}

	public void setRiseLevel(Integer riseLevel) {
		this.riseLevel = riseLevel;
	}

	public Integer getMaxWords() {
		return maxWords;
	}

	public void setMaxWords(Integer maxWords) {
		this.maxWords = maxWords;
	}

	public Integer getSublen() {
		return sublen;
	}

	public void setSublen(Integer sublen) {
		this.sublen = sublen;
	}
	/*public String getGateInfo() {
		return gateInfo;
	}

	public void setGateInfo(String gateInfo) {
		this.gateInfo = gateInfo;
	}*/

	public Integer getSplitRule() {
		return splitRule;
	}

	public void setSplitRule(Integer splitRule) {
		this.splitRule = splitRule;
	}

	/*public Integer getWordLen() {
		return wordLen;
	}

	public void setWordLen(Integer wordLen) {
		this.wordLen = wordLen;
	}

	public Integer getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
	}

	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}*/

	public Integer getMultilen1() {
		return multilen1;
	}

	public void setMultilen1(Integer multilen1) {
		this.multilen1 = multilen1;
	}

	public Integer getMultilen2() {
		return multilen2;
	}

	public void setMultilen2(Integer multilen2) {
		this.multilen2 = multilen2;
	}

	/*public Integer getGateNo() {
		return gateNo;
	}

	public void setGateNo(Integer gateNo) {
		this.gateNo = gateNo;
	}*/

	public Integer getSignType() {
		return signType;
	}

	public void setSignType(Integer signType) {
		this.signType = signType;
	}

	public Integer getSignFixLen() {
		return signFixLen;
	}

	public void setSignFixLen(Integer signFixLen) {
		this.signFixLen = signFixLen;
	}

	/*public Integer getPreFixLen() {
		return preFixLen;
	}

	public void setPreFixLen(Integer preFixLen) {
		this.preFixLen = preFixLen;
	}

	public Integer getMaxLongMsgEq() {
		return maxLongMsgEq;
	}

	public void setMaxLongMsgEq(Integer maxLongMsgEq) {
		this.maxLongMsgEq = maxLongMsgEq;
	}*/

	public Integer getEndSplit() {
		return endSplit;
	}

	public void setEndSplit(Integer endSplit) {
		this.endSplit = endSplit;
	}

	/*public Integer getGateSeq() {
		return gateSeq;
	}

	public void setGateSeq(Integer gateSeq) {
		this.gateSeq = gateSeq;
	}*/

	public Integer getSignDropType() {
		return signDropType;
	}

	public void setSignDropType(Integer signDropType) {
		this.signDropType = signDropType;
	}

/*	public String getGateArea() {
		return gateArea;
	}

	public void setGateArea(String gateArea) {
		this.gateArea = gateArea;
	}*/

	public Integer getAreaType() {
		return areaType;
	}

	public void setAreaType(Integer areaType) {
		this.areaType = areaType;
	}

	public Integer getEachSign() {
		return eachSign;
	}

	public void setEachSign(Integer eachSign) {
		this.eachSign = eachSign;
	}

	public String getEnsignstr()
	{
		return ensignstr;
	}

	public void setEnsignstr(String ensignstr)
	{
		this.ensignstr = ensignstr;
	}

	public Integer getEnsignlen()
	{
		return ensignlen;
	}

	public void setEnsignlen(Integer ensignlen)
	{
		this.ensignlen = ensignlen;
	}

	public Integer getEnprefixlen()
	{
		return enprefixlen;
	}

	public void setEnprefixlen(Integer enprefixlen)
	{
		this.enprefixlen = enprefixlen;
	}

	public Integer getEnmaxwords()
	{
		return enmaxwords;
	}

	public void setEnmaxwords(Integer enmaxwords)
	{
		this.enmaxwords = enmaxwords;
	}

	public Integer getEnsinglelen()
	{
		return ensinglelen;
	}

	public void setEnsinglelen(Integer ensinglelen)
	{
		this.ensinglelen = ensinglelen;
	}

	public Integer getEnmultilen1()
	{
		return enmultilen1;
	}

	public void setEnmultilen1(Integer enmultilen1)
	{
		this.enmultilen1 = enmultilen1;
	}

	public Integer getEnmultilen2()
	{
		return enmultilen2;
	}

	public void setEnmultilen2(Integer enmultilen2)
	{
		this.enmultilen2 = enmultilen2;
	}
}