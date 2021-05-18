package com.montnets.emp.zxkf.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONObject;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.online.LfOnlMsgHis;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.entity.wxsysuser.LfSysUser;
import com.montnets.emp.table.online.TableLfOnlMsgHis;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.wxgl.TableLfWeiAccount;
import com.montnets.emp.table.wxgl.TableLfWeiUser2Acc;
import com.montnets.emp.table.wxsysuser.TableLfSysUser;
import com.montnets.emp.util.PageInfo;

public class CustomerDao extends SuperDAO
{
    /**
     * 获取在线客服列表
     * 
     * @param corpcode
     * @param condition
     * @param pageInfo
     * @return
     */
    public List<DynaBean> findCustomerList(String corpCode, LinkedHashMap<String,String> conditionMap, PageInfo pageInfo)
    {
        // filedSql
        String fieldSql = "select s.name,s.user_name, " +
        		(StaticValue.DBTYPE==StaticValue.ORACLE_DBTYPE?"to_char(s.reg_time,'yyyy-mm-dd hh24:mi:ss') as reg_time":"s.reg_time")
        		+",s.user_state,s.user_id,a.A_ID,c.NAME as acctname  ";
        // tableSql
        String tableSql = "from LF_SYSUSER s "+StaticValue.getWITHNOLOCK()+" LEFT JOIN LF_WEI_USER2ACC  a on s.USER_ID=a.USER_ID LEFT JOIN LF_WEI_ACCOUNT c on c.A_ID = a.A_id";
        // conSql(查询条件)
        String conSql = " where s.IS_CUSTOME =1 ";
        if(conditionMap != null && !conditionMap.entrySet().isEmpty())
        {
            if(conditionMap.get("AId") != null && !"".equals(conditionMap.get("AId")))
            {
            	if("0".equals(conditionMap.get("AId")))
            	{
            		conSql += " and a." + TableLfWeiUser2Acc.A_ID +" is null ";
            	}else
            	{
            		conSql += (" and a." + TableLfWeiUser2Acc.A_ID + " = " + conditionMap.get("AId"));
            	}
            }
            if(conditionMap.get("name") != null && !"".equals(conditionMap.get("name")))
            {
                conSql += (" and s." + TableLfSysuser.NAME + " like '%" + conditionMap.get("name") + "%' ");
            }
        }
        // 排序
        String orderbySql = " order by s.REG_TIME DESC";
        // 返回结果
        String sql = fieldSql + tableSql + conSql + orderbySql;
        String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
        return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
    }

    /**
     * 查询系统所有客服
     * 
     * @description
     * @param pageInfo
     * @return
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-17 下午06:23:29
     */
    public List<DynaBean> findCustomerList()
    {
        // filedSql
        String fieldSql = "SELECT sysUser.USER_ID,sysUser.NAME,sysUser.USER_NAME,sysUser.PERMISSION_TYPE as ptype,sysUser.USER_STATE ,sysUser.REG_TIME,account.CORP_CODE as corp_code,account.A_ID as a_id,account.NAME as acctName";
        // tableSql
        StringBuffer tableSql = new StringBuffer();
        tableSql.append(" FROM ").append(TableLfSysUser.TABLE_NAME).append(" sysUser "+StaticValue.getWITHNOLOCK());
        tableSql.append(" LEFT JOIN ").append(TableLfWeiAccount.TABLE_NAME).append(" account ");
        tableSql.append(" ON ").append("sysUser.A_ID = ").append(" account.A_ID ");
        tableSql.append(" WHERE ");
        // conSql(查询条件)
        StringBuffer conSql = new StringBuffer();
        conSql.append("  sysUser.").append(TableLfSysUser.PERMISSION_TYPE).append(" in (").append("'2','3'").append(")");

        // 排序
        String orderbySql = " order by sysUser.REG_TIME DESC";
        // 返回结果
        String sql = fieldSql + tableSql.toString() + conSql.toString() + orderbySql;
        return getListDynaBeanBySql(sql);
    }

    /**
     * 客服查询消息历史记录
     * 
     * @description 客服查询和某个人的历史消息记录
     * @param ownerId
     *        客服人员自己的id
     * @param customId
     *        和客服聊天人的id
     * @param conditionMap
     *        查询条件
     * @param pageInfo
     *        分页对象
     * @return List<LfOnlMsgHis>对象
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-19 下午03:45:58
     */
    public List<LfOnlMsgHis> findHistoryMessage(String ownerId, String customId, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
    {
        String fieldSql = "SELECT otOnlineMesHis.M_ID,otOnlineMesHis.FROM_USER,otOnlineMesHis.TO_USER,otOnlineMesHis.SEND_TIME,otOnlineMesHis.SERVER_NUM,otOnlineMesHis.MSG_TYPE,otOnlineMesHis.MESSAGE,otOnlineMesHis.PUSH_TYPE,otOnlineMesHis.A_ID";

        StringBuffer tableSql = new StringBuffer();
        tableSql.append(" FROM ").append(TableLfOnlMsgHis.TABLE_NAME).append(" as otOnlineMesHis "+StaticValue.getWITHNOLOCK());
        tableSql.append(" WHERE ((FROM_USER = '").append(ownerId).append("'");
        tableSql.append(" AND TO_USER = '").append(customId).append("'");
        tableSql.append(") OR (FROM_USER = '").append(customId).append("'");
        tableSql.append(" AND TO_USER = '").append(ownerId).append("'))");

        // conSql(查询条件)
        String conditionSql = getConditionSql(conditionMap);
        String orderbySql = " order by otOnlineMesHis.SEND_TIME";
        String sql = fieldSql + tableSql.toString() + conditionSql + orderbySql;
        String countSql = "select count(*) totalcount " + tableSql.toString() + conditionSql;
        List<LfOnlMsgHis> returnList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(LfOnlMsgHis.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
        return returnList;
    }

    /**
     * 加载客服所服务人员的名单
     * @description    
     * @param owerId 客服ID
     * @return
     * @throws Exception       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-23 上午09:14:11
     */
    @SuppressWarnings("unchecked")
    public JSONObject findCustomerToClient(String owerId) throws Exception
    {
        JSONObject jsonObject = new JSONObject();
        String fieldSql = "SELECT otOnlineMesHis.M_ID,otOnlineMesHis.FROM_USER,otOnlineMesHis.TO_USER,otOnlineMesHis.SEND_TIME,otOnlineMesHis.SERVER_NUM,otOnlineMesHis.MSG_TYPE,otOnlineMesHis.MESSAGE,otOnlineMesHis.PUSH_TYPE,otOnlineMesHis.A_ID";

        StringBuffer tableSql = new StringBuffer();
        tableSql.append(" FROM ").append(TableLfOnlMsgHis.TABLE_NAME).append(" as otOnlineMesHis "+StaticValue.getWITHNOLOCK());
        tableSql.append(" WHERE ((FROM_USER = '").append(owerId).append("'");
        tableSql.append(") OR (TO_USER = '").append(owerId).append("'))");

        // conSql(查询条件)
        String orderbySql = " order by otOnlineMesHis.SEND_TIME";
        String sql = fieldSql + tableSql.toString() + orderbySql;
        String countSql = "select count(*) totalcount " + tableSql.toString();
        //List<LfOnlMsgHis> returnList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(LfOnlMsgHis.class, sql, countSql, null, StaticValue.EMP_POOLNAME);
        List<LfOnlMsgHis> returnList = findEntityListBySQL(LfOnlMsgHis.class, sql, StaticValue.EMP_POOLNAME);
        if(null != returnList && returnList.size() > 0)
        {
            List<LfSysUser> otSysUsers = new ArrayList<LfSysUser>();
            List<LfWeiUserInfo> otWeiUserInfos = new ArrayList<LfWeiUserInfo>();
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            for (Iterator iterator = returnList.iterator(); iterator.hasNext();)
            {
                conditionMap.clear();
                String openId = "";
                String clientId = "";
                LfOnlMsgHis otOnlMsgHis = (LfOnlMsgHis) iterator.next();
                
                //1-手机to客服，2-客服to手机，3-客服to客服，4群组
                int pushType = otOnlMsgHis.getPushType();
                if(pushType == 1)
                {
                    openId = otOnlMsgHis.getFromUser();
                }
                else if (pushType == 2)
                {
                    openId = otOnlMsgHis.getToUser();
                }
                else if (pushType == 3)
                {
                    clientId = otOnlMsgHis.getFromUser();
                    if(null != clientId && clientId.equals(owerId))
                    {
                        clientId = otOnlMsgHis.getToUser();
                    }
                }
                else if (pushType == 4)
                {
                    
                }                

                if(pushType == 1 || pushType == 2)
                {
                    //聊天对象是客户
                    LfWeiUserInfo otWeiUserInfo = new LfWeiUserInfo();
                    conditionMap.put("openId", openId);
                    List<LfWeiUserInfo> otWeiUserInfoList = new DataAccessDriver().getEmpDAO().findListByCondition(LfWeiUserInfo.class, conditionMap, null);
                    if(null != otWeiUserInfoList && otWeiUserInfoList.size() > 0)
                    {
                        otWeiUserInfo = otWeiUserInfoList.get(0);
                    }
                    otWeiUserInfos.add(otWeiUserInfo);
                }
                else if(pushType == 3)
                {
                    //聊天对象是客服
                    conditionMap.clear();
                    LfSysUser otSysUser = new LfSysUser();
                    conditionMap.put("userId", clientId);
                    List<LfSysUser> otSysUserList = new DataAccessDriver().getEmpDAO().findListByCondition(LfSysUser.class, conditionMap, null);
                    if(null != otSysUserList && otSysUserList.size() > 0)
                    {
                        otSysUser = otSysUserList.get(0);
                    }
                    otSysUsers.add(otSysUser);
                }                
            }
            jsonObject.put("otSysUserObjList", otSysUsers);
            jsonObject.put("otWeiUserInfoObjList", otWeiUserInfos);
        }
        
        return jsonObject;
    }
    
    /**
     * 根据条件进行查询
     * 
     * @description
     * @param conditionMap
     *        查询条件
     * @return
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-20 上午10:21:16
     */
    public String getConditionSql(LinkedHashMap<String, String> conditionMap)
    {
        StringBuffer conditionSql = new StringBuffer();

        if(null != conditionMap.get("beginTime") && !"".equals(conditionMap.get("beginTime")))
        {
            conditionSql.append(" AND otOnlineMesHis." + TableLfOnlMsgHis.SEND_TIME + " >= " + conditionMap.get("beginTime"));
        }
        if(null != conditionMap.get("endTime") && !"".equals(conditionMap.get("endTime")))
        {
            conditionSql.append(" AND otOnlineMesHis." + TableLfOnlMsgHis.SEND_TIME + " <= " + conditionMap.get("endTime"));
        }
        if(null != conditionMap.get("content") && !"".equals(conditionMap.get("content")))
        {
            conditionSql.append(" AND otOnlineMesHis." + TableLfOnlMsgHis.MESSAGE + " like '%" + conditionMap.get("content") + "%'");
        }

        return conditionSql.toString();
    }
}
