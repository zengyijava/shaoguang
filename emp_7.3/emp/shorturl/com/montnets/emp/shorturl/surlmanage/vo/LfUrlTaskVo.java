package com.montnets.emp.shorturl.surlmanage.vo;

import java.sql.Timestamp;

/**
 * 短地址群发任务查看
 * @author Administrator
 *
 */
public class LfUrlTaskVo {
	
	/**
	 * 自定id
	 */
	private Long id;
	/**
	 * 创建人员ID
	 */
	private Integer createUid;
	/**
	 * 创建人员名
	 */
	private String name;
	/**
	 * 隶属机构
	 */
	private String depname;
	/**
	 * 隶属机构id
	 */
	private Integer depid;
	
	/**
	 * sp账号
	 */
	private String spUser;
	/**
	 * 发送类型
	 */
	private Integer utype;
	/**
	 * 发送主题
	 */
	private String title;
	/**
	 * 任务批次号
	 */
	private Integer taskId;
	/**
	 * 发送时间
	 */
	private Timestamp sendtm;
	/**
	 * 创建时间
	 */
	private Timestamp createtm;
	/**
	 * 提交个数
	 */
	private Integer subCount;
	/**
	 * 短信内容
	 */
	private String msg;
	/**
	 * 企业长地址
	 */
	private String netUrl;
	/**
	 * 冗余审核表ID
	 * 
	 */
	private Long netUrlId;
	/**
	 * 文件本地路径（唯一的）
	 */
	private String srcfile;
	/**
	 * 文件相对路径
	 */
	private String urlfile;
	
	/**
	 * 处理状态
	 * 10,11：未处理
	   11,21：处理中
	   12,22：处理完成
	   13,23 : 处理失败
	 */
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCreateUid() {
		return createUid;
	}

	public void setCreateUid(Integer createUid) {
		this.createUid = createUid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepname() {
		return depname;
	}

	public void setDepname(String depname) {
		this.depname = depname;
	}

	public Integer getDepid() {
		return depid;
	}

	public void setDepid(Integer depid) {
		this.depid = depid;
	}

	public String getSpUser() {
		return spUser;
	}

	public void setSpUser(String spUser) {
		this.spUser = spUser;
	}

	public Integer getUtype() {
		return utype;
	}

	public void setUtype(Integer utype) {
		this.utype = utype;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Timestamp getSendtm() {
		return sendtm;
	}

	public void setSendtm(Timestamp sendtm) {
		this.sendtm = sendtm;
	}

	public Timestamp getCreatetm() {
		return createtm;
	}

	public void setCreatetm(Timestamp createtm) {
		this.createtm = createtm;
	}

	public Integer getSubCount() {
		return subCount;
	}

	public void setSubCount(Integer subCount) {
		this.subCount = subCount;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getNetUrl() {
		return netUrl;
	}

	public void setNetUrl(String netUrl) {
		this.netUrl = netUrl;
	}

	public Long getNetUrlId() {
		return netUrlId;
	}

	public void setNetUrlId(Long netUrlId) {
		this.netUrlId = netUrlId;
	}

	public String getSrcfile() {
		return srcfile;
	}

	public void setSrcfile(String srcfile) {
		this.srcfile = srcfile;
	}

	public String getUrlfile() {
		return urlfile;
	}

	public void setUrlfile(String urlfile) {
		this.urlfile = urlfile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
}
