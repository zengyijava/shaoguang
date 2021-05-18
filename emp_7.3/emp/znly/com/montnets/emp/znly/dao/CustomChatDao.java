package com.montnets.emp.znly.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.online.LfOnlGroup;
import com.montnets.emp.entity.online.LfOnlMsgHis;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.wxgl.TableLfWeiUserInfo;

public class CustomChatDao extends SuperDAO
{
    /**
     * 获取客服群组
     * @description    通过传入的客服人员Id查找该客服所属的客服群组
     * @param userId    客服人员Id
     * @return      客服群组集合           
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-11 下午02:59:51
     */
    public List<LfOnlGroup> getCustomeGroupList(String userId) throws Exception
    {
        String sql = "select * from lf_onl_group "+StaticValue.getWITHNOLOCK()+" where GP_ID in" +
        		" (select GP_ID from lf_onl_gpmem where GM_USER="+userId+" and gm_state = 1)";

        List<LfOnlGroup> returnList = findEntityListBySQL(LfOnlGroup.class, sql, StaticValue.EMP_POOLNAME);
        return returnList;
    }
    
    /**
     * 通过openid获取昵称
     * @param openId 微信号标识
     * @throws Exception 
     */
    public String getNickNameByOpenId(String openId) throws Exception
    {
        String sql = "select "+TableLfWeiUserInfo.NICK_NAME+" from "
            + TableLfWeiUserInfo.TABLE_NAME + " "+StaticValue.getWITHNOLOCK()+" where "
            + TableLfWeiUserInfo.OPEN_ID + "='"+openId+"'";
        return this.getString(TableLfWeiUserInfo.NICK_NAME,sql,StaticValue.EMP_POOLNAME);
    }
    
    /**
     * 获取客服未读取的群组消息集合
     * @description    
     * @param id        客服id
     * @param groupKeys 群组Id
     * @return
     * @throws Exception       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-23 下午04:05:59
     */
    public List<LfOnlMsgHis> getGroupMsg(String id,String groupKeys,String maxId,Long readedId) throws Exception
    {
        String groupKeyCondition = "'"+groupKeys.replace(",", "','")+"'";
        String sql = "select * from lf_onl_msg_his "+StaticValue.getWITHNOLOCK()+" where M_ID <= "+maxId+
        " and M_ID > "+readedId +
        " and to_user in ("+groupKeyCondition+") and push_type = 4";
        List<LfOnlMsgHis> returnList = findEntityListBySQL(LfOnlMsgHis.class, sql, StaticValue.EMP_POOLNAME);
        return returnList;
    }
    
    /**
     * 获取群组人员列表
     * @description    根据群组Id获取群组人员的列表集合
     * @param groupId   群组id
     * @return
     * @throws Exception       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-27 上午10:25:13
     */
    public List<DynaBean> getGroupMem(String groupId,String userid) throws Exception
    {
        String sql = "select u.user_name,u.name,u.user_id,a.a_id,r.mark_name from lf_sysuser u  "+StaticValue.getWITHNOLOCK()+
        	" left join lf_onl_remark r "+StaticValue.getWITHNOLOCK()+" on (r.mark_id = u.user_id and r.user_id ="+userid+")" +
        	" left join lf_wei_user2acc a "+StaticValue.getWITHNOLOCK()+
        	" on a.user_id = u.user_id  where u.user_id in " +
        		"(select gm_user from lf_onl_gpmem "+StaticValue.getWITHNOLOCK()+" where gm_state =1 and GP_ID ="+groupId+" )";
        List<DynaBean> returnList = getListDynaBeanBySql(sql);
        return returnList;
    }
    
    /**
     * 获取客服人员已读的群组消息ID
     * @description    
     * @param customeId
     * @return
     * @throws Exception       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-2 下午03:03:01
     */
    public Long getGpMsgId(String customeId) throws Exception
    {
        String sql = " select msg_Id as totalcount from lf_onl_gpmsgid "+StaticValue.getWITHNOLOCK()+" where gm_user="+customeId;
        return Long.valueOf(findCountBySQL(sql));
    }
    
    /**
     * 获取最大的群组消息ID
     * @description    
     * @return
     * @throws Exception                 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-2 下午05:03:11
     */
    public long getMaxMsgId() throws Exception
    {
        String sql = " select max(m_id) totalcount from lf_onl_msg_his "+StaticValue.getWITHNOLOCK();
        return Long.valueOf(findCountBySQL(sql));
    }
    
    /**
     * 加载历史记录
     * @description   
     * @param count             每批查询数量 
     * @param serverNum
     * @param pushtype
     * @param anotherServerNum
     * @param msgId       			 
     * @author linzhihan <zhihanking@163.com>
     * @throws Exception 
     * @datetime 2014-1-15 下午02:08:58
     */
    public List<LfOnlMsgHis> loadMsgHis(String count,String serverNum,String anotherServerNum,String msgId,String pushType) throws Exception
    {
    	String sql = "";
    	int dbType = StaticValue.DBTYPE;
    	String ontInType = "'zjkf','tips','tcqz','newqz'";
    	String whereStr = " where (SERVER_NUM = '" +serverNum + "'";
    	if("7".equals(pushType))
    	{
    		whereStr = " where ((from_user = '"+serverNum+"' and to_user = '"
    			+anotherServerNum+"') or (from_user = '"+anotherServerNum+"' and to_user = '"
    			+serverNum+"')) and (push_type=6 or push_type = 7)";
    	}else
    	{
	    	if(anotherServerNum != null)
	        {
	    		whereStr += " or SERVER_NUM = '" + anotherServerNum + "'";
	        }
	    	whereStr += ")";
    	}
        
        if(msgId != null && !"".equals(msgId))
        {
        	whereStr += " and M_ID < " + msgId;
        }
    	if(dbType == StaticValue.DB2_DBTYPE)
    	{
    		sql = "select * from lf_onl_msg_his ";
	        sql += whereStr;
	        sql += " and msg_type not in ("+ontInType+") order by M_ID desc fetch first "+count+" rows only";
    	}
    	else if(dbType == StaticValue.SQLSERVER_DBTYPE)
    	{
    		sql = "select top "+count+" * from lf_onl_msg_his "+StaticValue.getWITHNOLOCK();
        	sql += whereStr;
	        sql += " and msg_type not in  ("+ontInType+")  order by M_ID desc";
    	}
    	else if(dbType == StaticValue.ORACLE_DBTYPE)
    	{
    		sql = "SELECT * FROM  (select * from lf_onl_msg_his ";
    		sql += whereStr;
	        sql += " and msg_type not in  ("+ontInType+")  order by M_ID desc) WHERE ROWNUM <= "
	        	+count+" ORDER BY ROWNUM ASC  ";
    	}
    	else if(dbType == StaticValue.MYSQL_DBTYPE)
    	{
    		sql = "select * from lf_onl_msg_his ";
	        sql += whereStr;
	        sql += " and msg_type not in ("+ontInType+") order by M_ID desc limit 0,"+count;
    	}
        return findEntityListBySQL(LfOnlMsgHis.class, sql, StaticValue.EMP_POOLNAME);
    }
    
    /**
     * 加载通讯过的客户列表
     * @description    
     * @param userinfos 当前接入的客户
     * @param aId 公众号id
     * @param customeId 客服人员id
     * @param timeCondition 时间条件
     * @return
     * @throws Exception       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-2-19 下午05:26:04
     */
    public List<DynaBean> getChatUserInfos(String userinfos,String aId,String customeId,String timeCondition,String sertype) throws Exception
    {
        if(userinfos != null)
        {
            userinfos = "'"+userinfos.replace(",", "','")+"'";
        }
        String groupSql = "select from_user,max(create_time) ctime from lf_onl_server "+StaticValue.getWITHNOLOCK()+" where custome_id="+customeId+" "+
            (userinfos!=null?" and from_user not in ("+userinfos+")":"") +
            " and sertype="+sertype+" group by from_user,custome_id";
        if(customeId == null)
        {
            groupSql = "select from_user,max(create_time) ctime from lf_onl_server "+StaticValue.getWITHNOLOCK()+" where a_id="+aId+" "+
            " group by from_user,a_id";
        }
        String sql = "select  ser.from_user,ser.create_time,ser.ser_num,userinfo.nick_name " +
    		"from lf_onl_server ser "+StaticValue.getWITHNOLOCK()+" inner join "+
            "("+groupSql+") gser "+
            "on (gser.from_user = ser.from_user and gser.ctime = ser.create_time) "+
    		"inner join lf_wei_userinfo userinfo "+StaticValue.getWITHNOLOCK()+" on ser.from_user = userinfo.open_id " +
    		"where ser.a_id = " + aId +
    		"  and userinfo.a_id= " + aId;
        if(timeCondition != null)
        {
            sql += " and ser.create_time > '"+ timeCondition + "'";
        }
        return getListDynaBeanBySql(sql);
    }
    
    /**
     * 获取相同
     * @description    根据群组Id获取群组人员的列表集合
     * @param groupId   群组id
     * @return
     * @throws Exception       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-27 上午10:25:13
     */
    public List<LfSysuser> getSameAccUser(String userid,String AId) throws Exception
    {
        String sql = "select * from lf_sysuser "+StaticValue.getWITHNOLOCK()+" where user_id in " +
        		"(select user_id from lf_wei_user2Acc "+StaticValue.getWITHNOLOCK()+" where a_id = " + AId +
        		") and user_id <> "+userid;
        List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql, StaticValue.EMP_POOLNAME);
        return returnList;
    }
    
    /**
     * 获取相同
     * @description    根据群组Id获取群组人员的列表集合
     * @param groupId   群组id
     * @param stateReq 是否过滤
     * @return
     * @throws Exception       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-27 上午10:25:13
     */
    public List<DynaBean> getCustomeList(String userid,String aid,int stateReq) throws Exception
    {
    	String sql = "select u.name,u.user_id,u.user_name,a.a_id,r.mark_name from lf_sysuser u "+StaticValue.getWITHNOLOCK()+" " +
    	"left join lf_wei_user2Acc  a "+StaticValue.getWITHNOLOCK()+" on u.user_id = a.user_id " +
			"left join lf_onl_remark r "+StaticValue.getWITHNOLOCK()+" on (r.mark_id = u.user_id and r.user_id = "+userid+") " +
			"where u.is_custome = 1 and  u.user_id <>"+userid;
    	if(null != aid && !"0".equals(aid) && stateReq==1 )
    	{
    		sql += " and a.a_id = "+aid;
    	}
        
        List<DynaBean> returnList = this.getListDynaBeanBySql(sql);
        return returnList;
    }
    
    public int getAidByUserid(String userId) throws Exception
    {
    	String sql = " select A_ID as totalcount from lf_wei_user2Acc "+StaticValue.getWITHNOLOCK()+" where user_id="+userId;
    	return findCountBySQL(sql);
    }
    
    /**
     * 验证群组名称是否存在
     * @param userId
     * @param gpname
     * @return
     * @throws Exception
     */
    public int getGroupIdByName(String userId,String gpname) throws Exception
    {
        String sql = " select gp_id as totalcount from lf_onl_group where gp_name = '"+gpname+"' and gp_id in (select gp_id from LF_ONL_GPMEM where gm_user = "+userId+")";
        return findCountBySQL(sql);
    }
    
    public String getCustomByAppcode(String appcode) throws Exception
    {
    	String sql = " select custome_id from LF_ONL_SERVER where sertype = 6 and from_user ='"+appcode+"' ORDER BY CREATE_time DESC";
    	return getString("custome_id",sql,StaticValue.EMP_POOLNAME);
    }
    
    /**
     * 获取app服务的最晚时间
     * @param userid
     * @return
     * @throws Exception
     */
    public List<DynaBean> getAppSerTime(String userid) throws Exception
    {
    	String sql = "select  ser.from_user,ser.create_time from lf_onl_server ser "+StaticValue.getWITHNOLOCK()+"  "+ 
    		"inner join (select from_user,max(create_time) ctime from lf_onl_server "+StaticValue.getWITHNOLOCK()+" "+  
    		"where  sertype=6 and custome_id = "+userid+" group by from_user,custome_id) gser" +
    				" on (gser.from_user=ser.FROM_USER and gser.ctime = ser.CREATE_TIME)"; 
    	 List<DynaBean> returnList = this.getListDynaBeanBySql(sql);
         return returnList;
    }
}
