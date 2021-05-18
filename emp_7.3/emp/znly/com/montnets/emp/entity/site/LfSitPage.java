package com.montnets.emp.entity.site;

import java.sql.Timestamp;


/**
 * 实体类： LfSitPage
 *
 * @project p_sit
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfSitPage implements java.io.Serializable
{
   
    /**
     * @description  serialVersionUID
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-25 下午04:58:17
     */
    private static final long serialVersionUID = 3876893153912627860L;

    // 程序自增ID
    private Long	pageId;
   
    // 微站编号
    private Long	sId;
   
    // 唯一标识（访问用）
    private String	pageType;
   
    // 页面的名称
    private String	name;
   
    // 访问地址
    private String	url;
   
    // 排序
    private Integer	seqNum;
   
    // 企业编码（0表示系统默认分类）
    private String	corpCode;
   
    // 创建时间
    private Timestamp	createtime;
   
    // 更新时间
    private Timestamp	moditytime;
   
    
	public Long getPageId()
	{
		return pageId;
	}

	public void setPageId(Long pageId)
	{
		this.pageId = pageId;
	}
    
	public Long getSId()
	{
		return sId;
	}

	public void setSId(Long sId)
	{
		this.sId = sId;
	}
    
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
    
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
    
	public Integer getSeqNum()
	{
		return seqNum;
	}

	public void setSeqNum(Integer seqNum)
	{
		this.seqNum = seqNum;
	}
    
	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
    
	public Timestamp getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(Timestamp createtime)
	{
		this.createtime = createtime;
	}
    
	public Timestamp getModitytime()
	{
		return moditytime;
	}

	public void setModitytime(Timestamp moditytime)
	{
		this.moditytime = moditytime;
	}

    public void setPageType(String pageType)
    {
        this.pageType = pageType;
    }
    
    public String getPageType()
    {
        return pageType;
    }

}