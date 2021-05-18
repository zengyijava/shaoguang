package com.montnets.emp.entity.online;



/**
 * 实体类： LfOnlGpmem
 *
 * @project p_onl
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfOnlGpmem implements java.io.Serializable
{
   
    /**
     * @description  
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-11 下午02:42:03
     */
    private static final long serialVersionUID = 5272183070197099123L;

    // 关联群组id
    private Long	gpId;
   
    // 群组人员ID
    private Long	gmUser;
   

   
    // 状态，1-有效，0-无效（当群组人员退出该群组时置为状态无效）
    private Integer	gmState;

    public Long getGpId()
    {
        return gpId;
    }

    public void setGpId(Long gpId)
    {
        this.gpId = gpId;
    }

    public Long getGmUser()
    {
        return gmUser;
    }

    public void setGmUser(Long gmUser)
    {
        this.gmUser = gmUser;
    }

    /*public Long getMessId()
    {
        return messId;
    }

    public void setMessId(Long messId)
    {
        this.messId = messId;
    }*/

    public Integer getGmState()
    {
        return gmState;
    }

    public void setGmState(Integer gmState)
    {
        this.gmState = gmState;
    }

}