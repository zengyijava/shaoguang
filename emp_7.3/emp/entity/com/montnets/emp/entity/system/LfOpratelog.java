package com.montnets.emp.entity.system;


import java.sql.Timestamp;

/**
 * TableLfOpratelog对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:24:15
 * @description 
 */
public class LfOpratelog  implements java.io.Serializable {
 
     /**
	 * 
	 */
	private static final long serialVersionUID = 7987766343580250333L;
	/**
	 * 
	 */
	////private static final long serialVersionUID = -5614764771064232405L;
	private Long logId;
     private Timestamp opTime;
     private String opUser;
     private String opModule;
     private String opAction;
     private Integer opResult;
 	 private String opContent;
 	private String corpCode;
 
    public LfOpratelog() {}

    public String getOpContent() {
		return opContent;
	}


	public void setOpContent(String opContent) {
		this.opContent = opContent;
	}


	public Long getLogId() {
        return this.logId;
    }
    
    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Timestamp getOpTime() {
        return this.opTime;
    }
    
    public void setOpTime(Timestamp opTime) {
        this.opTime = opTime;
    }

    public String getOpUser() {
        return this.opUser;
    }
    
    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public String getOpModule() {
        return this.opModule;
    }
    
    public void setOpModule(String opModule) {
        this.opModule = opModule;
    }

    public String getOpAction() {
        return this.opAction;
    }
    
    public void setOpAction(String opAction) {
        this.opAction = opAction;
    }

	public Integer getOpResult()
	{
		return opResult;
	}

	public void setOpResult(Integer opResult)
	{
		this.opResult = opResult;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
    
	








}