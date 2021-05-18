/**
 * 
 */
package com.montnets.emp.entity.keywords;


/**
 * TableLfKeywords对应的实体类
 * 关键字
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 上午10:53:53
 * @description 
 */

public class KeywordBlack implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4638723059025040841L;
	//主键
	private Long id; 
	//关键字名称
	private String keyWord; 
	private Integer keyType; 
	//关键字级别(一级:1，二级:2，三级:3)，暂时没用
	private Integer keyLevel; 
	//状态（1.启用;0.停用）
	private Integer opType; 
	
	public KeywordBlack(){}

	public String getKeyWord()
	{
		return keyWord;
	}

	public void setKeyWord(String keyWord)
	{
		this.keyWord = keyWord;
	}


	public Integer getKeyLevel() {
		return keyLevel;
	}

	public void setKeyLevel(Integer keyLevel) {
		this.keyLevel = keyLevel;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getKeyType() {
		return keyType;
	}

	public void setKeyType(Integer keyType) {
		this.keyType = keyType;
	}

	public Integer getOpType() {
		return opType;
	}

	public void setOpType(Integer opType) {
		this.opType = opType;
	}

}
