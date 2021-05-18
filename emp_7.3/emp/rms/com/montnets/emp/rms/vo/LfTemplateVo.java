package com.montnets.emp.rms.vo;

import java.sql.Timestamp;

/**
 *  模板对象Vo
 * @author admin
 * @date 2011-6-30 下午02:38:16
 */

public class LfTemplateVo implements java.io.Serializable {
	private static final long serialVersionUID = 1083784840930893940L;
	/**
	 * 自增id
	 */
	private Long id;
	/**
	 * 共享类型
	 */
	private Integer shareType;
	/**
	 * 自增Id
	 */
	private Long tmid;
	/**
	 * 操作员id
	 */
    private Long userId;
	/**
	 * 模板名称
	 */
	private String tmName;
	/**
	 * 模板内容
	 */
	private String tmMsg;
	/**
	 * 类型 0表示静态 1表示动态
	 */
	private Long dsflag;
	/**
	 * 状态
	 */
	private Long tmState;
	/**
	 * 新增时间
	 */
	private Timestamp addtime;

	private Integer isPass;
	/**
	 * 类型 11表示富信
	 */
	private Integer tmpType;
	/**
	 * 姓名
	 */
    private String name;
	/**
	 * 登录名称
	 */
	private String userName;
	/**
	 * 状态
	 */
	private Integer userState;
	/**
	 * 部门名字
	 */
    private String depName;
	/**
	 * 模板参数个数
	 */
	private Integer paramcnt;
	/**
	 * 网关模板ID
	 */
	private Long sptemplid;
	/**
	 * 网关审批状态 1表示审核通过
	 */
	private Integer auditstatus;
	/**
	 * 网关审批错误信息
	 */
	private Integer errorcode;
	/**
	 * 网关提交状态
	 */
	private Integer submitstatus;
	/**
	 * 模块编码
	 */
	private String tmCode;
	/**
	 * 对应审核流程id
	 */
	private Long flowID;
	/**
	 * 创建开始时间
	 */
	private String addStartm;
	/**
	 * 创建结束时间
	 */
	private String addEndtm;
	/**
	 * 档位
	 */
	private Integer degree;
	/**
	 * 容量
	 */
	private Long degreeSize;
	/**
	 * 企业编码
	 */
	private String corpCode;
	/**
	 * 是否是公共模板 0为不是 1为是
	 */
	private Integer isPublic;
	/**
	 * 行业ID
	 */
	private Integer industryid;
	/**
	 * 用途ID
	 */
	private Integer useid;
	/**
	 * 模板使用次数
	 */
	private Long usecount;
	/**
	 * 是否已设置为我的快捷模板(0:未设置，1:已设置)
	 */
	private Integer isShortTemp;
	/**
	 * 模板版本 目前只有V1.0 与 V2.0
	 */
	private String ver;
	/**
	 * 是否有预览错误 1表示有 0表示无(默认)
	 */
	private Integer previewError = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getShareType() {
		return shareType;
	}

	public void setShareType(Integer shareType) {
		this.shareType = shareType;
	}

	public Long getTmid() {
		return tmid;
	}

	public void setTmid(Long tmid) {
		this.tmid = tmid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTmName() {
		return tmName;
	}

	public void setTmName(String tmName) {
		this.tmName = tmName;
	}

	public String getTmMsg() {
		return tmMsg;
	}

	public void setTmMsg(String tmMsg) {
		this.tmMsg = tmMsg;
	}

	public Long getDsflag() {
		return dsflag;
	}

	public void setDsflag(Long dsflag) {
		this.dsflag = dsflag;
	}

	public Long getTmState() {
		return tmState;
	}

	public void setTmState(Long tmState) {
		this.tmState = tmState;
	}

	public Timestamp getAddtime() {
		return addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

	public Integer getIsPass() {
		return isPass;
	}

	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}

	public Integer getTmpType() {
		return tmpType;
	}

	public void setTmpType(Integer tmpType) {
		this.tmpType = tmpType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
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

	public Integer getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(Integer errorcode) {
		this.errorcode = errorcode;
	}

	public Integer getSubmitstatus() {
		return submitstatus;
	}

	public void setSubmitstatus(Integer submitstatus) {
		this.submitstatus = submitstatus;
	}

	public String getTmCode() {
		return tmCode;
	}

	public void setTmCode(String tmCode) {
		this.tmCode = tmCode;
	}

	public Long getFlowID() {
		return flowID;
	}

	public void setFlowID(Long flowID) {
		this.flowID = flowID;
	}

	public String getAddStartm() {
		return addStartm;
	}

	public void setAddStartm(String addStartm) {
		this.addStartm = addStartm;
	}

	public String getAddEndtm() {
		return addEndtm;
	}

	public void setAddEndtm(String addEndtm) {
		this.addEndtm = addEndtm;
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

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
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

	public Long getUsecount() {
		return usecount;
	}

	public void setUsecount(Long usecount) {
		this.usecount = usecount;
	}

	public Integer getIsShortTemp() {
		return isShortTemp;
	}

	public void setIsShortTemp(Integer isShortTemp) {
		this.isShortTemp = isShortTemp;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public Integer getPreviewError() {
		return previewError;
	}

	public void setPreviewError(Integer previewError) {
		this.previewError = previewError;
	}
}
