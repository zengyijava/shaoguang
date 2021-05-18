package com.montnets.emp.entity.wxgl;

import java.sql.Timestamp;


/**
 * 实体类： LfWeiGroup 微信用户分组
 *
 * @project p_wei
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
/**
 * @description
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-5 上午11:24:33
 */
public class LfWeiGroup implements java.io.Serializable
{
   
    /**
     * @description  
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-29 下午04:34:11
     */
    private static final long serialVersionUID = -5507538948525942815L;

    // 标识ID
    private Long	GId;

    // 微信同步过来的群组ID
    private Long WGId;
    
    // 分组名称
    private String	name;
   
    // 分组内用户数量
    private String	count;
   
    // 公众帐号的ID
    private Long	AId;
   
    // 企业编号
    private String	corpCode;
   
    // 创建时间
    private Timestamp	createtime;
   
    // 修改时间
    private Timestamp	modifytime;
   
	public Long getGId()
    {
        return GId;
    }

    public void setGId(Long gId)
    {
        GId = gId;
    }

    public Long getWGId()
    {
        return WGId;
    }

    public void setWGId(Long wGId)
    {
        WGId = wGId;
    }

    public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
    
	public String getCount()
	{
		return count;
	}

	public void setCount(String count)
	{
		this.count = count;
	}
    
	public Long getAId()
    {
        return AId;
    }

    public void setAId(Long aId)
    {
        AId = aId;
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
    
	public Timestamp getModifytime()
	{
		return modifytime;
	}

	public void setModifytime(Timestamp modifytime)
	{
		this.modifytime = modifytime;
	}

}