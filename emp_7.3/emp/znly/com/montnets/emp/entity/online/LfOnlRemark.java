package com.montnets.emp.entity.online;

public class LfOnlRemark implements java.io.Serializable
{
    /**
     * @description  
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-06-25 上午11:10:09
     */
    private static final long serialVersionUID = 3001514446817028862L;

    private Long userId;
    
    private Long markId;
    
    private String markName;

    public LfOnlRemark()
    {
    }
    public LfOnlRemark(Long userId,Long markId,String markName)
    {
        this.userId = userId;
        this.markId = markId;
        this.markName = markName;
    }
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getMarkId() {
		return markId;
	}
	public void setMarkId(Long markId) {
		this.markId = markId;
	}
	public String getMarkName() {
		return markName;
	}
	public void setMarkName(String markName) {
		this.markName = markName;
	}
    
    
    
}
