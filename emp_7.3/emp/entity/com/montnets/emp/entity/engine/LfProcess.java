/**
 * 
 */
package com.montnets.emp.entity.engine;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 下午01:51:03
 * @description
 */

public class LfProcess implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1424857094063571956L;

	/**
	 * 
	 */
	// private static final long serialVersionUID = -2147825554988884160L;
	private Long prId;
	// 库连接ID
	private Long dbId;
	// 服务业务id
	private Long serId;

	private Long sgId;
	// 步骤说明
	private String prName;
	// 描述
	private String comments;
	// 1增,2删,3改，4是查询5是回复
	private Integer prType;
	// 是否（0否，1是）为最后处理步骤
	private Integer finalState;

	private Integer grState;
	// 处理结果（成功/失败）
	private Integer prResult;

	private String tableName;
	// SQL语句
	private String sql;
	// 下一步ID（OR步骤序号）
	private Long nextPrId;

	private String sysError;

	private String dbError;
	// 步骤序号
	private Integer prNo;
	// 上一步ID
	private Long prePrId;
	//指令代码
	private String proCode;
	//消息分隔符
	private String msgSeparated;
	//短信类型 0为下行短信，1为上行短信
	private Integer msgType;
	//短信内容
	private String msgContent;
	//模板ID
	private Long templateId;
	
	public LfProcess()
	{
	}

	public Long getPrId()
	{
		return prId;
	}

	public void setPrId(Long prId)
	{
		this.prId = prId;
	}

	public Long getDbId()
	{
		return dbId;
	}

	public void setDbId(Long dbId)
	{
		this.dbId = dbId;
	}

	public Long getSerId()
	{
		return serId;
	}

	public void setSerId(Long serId)
	{
		this.serId = serId;
	}

	public Long getSgId()
	{
		return sgId;
	}

	public void setSgId(Long sgId)
	{
		this.sgId = sgId;
	}

	public String getPrName()
	{
		return prName;
	}

	public void setPrName(String prName)
	{
		this.prName = prName;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public Integer getPrType()
	{
		return prType;
	}

	public void setPrType(Integer prType)
	{
		this.prType = prType;
	}

	public Integer getFinalState()
	{
		return finalState;
	}

	public void setFinalState(Integer finalState)
	{
		this.finalState = finalState;
	}

	public Integer getGrState()
	{
		return grState;
	}

	public void setGrState(Integer grState)
	{
		this.grState = grState;
	}

	public Integer getPrResult()
	{
		return prResult;
	}

	public void setPrResult(Integer prResult)
	{
		this.prResult = prResult;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getSql()
	{
		return sql;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	public Long getNextPrId()
	{
		return nextPrId;
	}

	public void setNextPrId(Long nextPrId)
	{
		this.nextPrId = nextPrId;
	}

	public String getSysError()
	{
		return sysError;
	}

	public void setSysError(String sysError)
	{
		this.sysError = sysError;
	}

	public String getDbError()
	{
		return dbError;
	}

	public void setDbError(String dbError)
	{
		this.dbError = dbError;
	}

	public Integer getPrNo()
	{
		return prNo;
	}

	public void setPrNo(Integer prNo)
	{
		this.prNo = prNo;
	}

	public Long getPrePrId()
	{
		return prePrId;
	}

	public void setPrePrId(Long prePrId)
	{
		this.prePrId = prePrId;
	}

	public String getProCode()
	{
		return proCode;
	}

	public void setProCode(String proCode)
	{
		this.proCode = proCode;
	}

	public String getMsgSeparated()
	{
		return msgSeparated;
	}

	public void setMsgSeparated(String msgSeparated)
	{
		this.msgSeparated = msgSeparated;
	}

	public Integer getMsgType()
	{
		return msgType;
	}

	public void setMsgType(Integer msgType)
	{
		this.msgType = msgType;
	}

	public String getMsgContent()
	{
		return msgContent;
	}

	public void setMsgContent(String msgContent)
	{
		this.msgContent = msgContent;
	}

	public Long getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(Long templateId)
	{
		this.templateId = templateId;
	}
	

}
