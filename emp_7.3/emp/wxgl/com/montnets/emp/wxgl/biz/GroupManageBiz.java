/**
 * @description  
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午03:59:19
 */
package com.montnets.emp.wxgl.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiGroup;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxgl.dao.GroupManageDao;

/**
 * @description 群组管理
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午03:59:19
 */
public class GroupManageBiz extends SuperBiz
{
    /**
     * 获取群组信息列表
     * @description    
     * @param corpCode
     * @param conditionMap
     * @param pageInfo
     * @return       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-3 下午05:20:33
     */
    public List<DynaBean> getGroupInfoList(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
    {
        List<DynaBean> beans = null;
        try
        {
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            beans = new GroupManageDao().getGroupInfoList(corpCode, conditionMap, orderbyMap, pageInfo);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"获取群组信息列表失败！");
        }
        return beans;
    }
    
    /**
     * 获取当前组的所有成员(如果组的otWeiGroup的wgId=0，则为“未分组”)
     * @description    
     * @param aId
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-2-25 上午10:56:04
     */
    public List<LfWeiUserInfo> getUnGroupMembers(String aId,LfWeiGroup otWeiGroup)
    {
        try
        {
            List<LfWeiUserInfo> userInfos = null;
               
            userInfos = new GroupManageDao().getUnGroupMembers(aId,otWeiGroup);
            return userInfos;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"获取未分组的关注列表失败！");
            return null;
        }
    }
    
    /**
     * 获取群组信息，封装json
     * @description    
     * @param AId
     * @param gpId
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-2-26 上午10:29:42
     */
    @SuppressWarnings("unchecked")
    public String getGroups(List<LfWeiGroup> groups)
    {
        JSONArray resultArray = new JSONArray();
        try
        {
            if(groups == null || groups.size() == 0)
            {
                // 返回空数组对象，值为“[]”
                return resultArray.toString();
            }
            for(LfWeiGroup group : groups)
            {
                JSONObject json = new JSONObject();
                json.put("name", group.getName());
                json.put("wgid", group.getWGId());
                json.put("gpid", group.getGId());
                
                resultArray.add(json);
            }
            return resultArray.toString();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"获取群组列表失败！");
            resultArray.clear();
            // 返回空数组对象，值为“[]”
            return resultArray.toString();
        }
    }
    
    /**
     * 获取公众帐号群组(不包含指定的群组)
     * @description    
     * @param AId
     * @param gpId
     * @return       			 
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月30日 下午1:01:19
     */
    public List<LfWeiGroup>  getGroupList(String AId,String gpId){
        List<LfWeiGroup> groups = null;
        if(AId != null && !"".equals(AId))
        {
            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
            LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
            conditionMap.put("GId&<>", gpId);
            conditionMap.put("AId", AId);
            orderbyMap.put("WGId", "ASC");
            try
            {
                groups = empDao.findListBySymbolsCondition( LfWeiGroup.class, conditionMap, orderbyMap);
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e,"获取公众帐号群组(不包含指定的群组)失败！-GroupManageBiz#getGroupList");
            }
        }
        return groups;
    }
    
    @SuppressWarnings("unchecked")
    public String getGroupMemberJson(List<LfWeiUserInfo> userInfos)
    {
        JSONArray resultArray = new JSONArray();
        try
        {
            if(userInfos == null || userInfos.size() == 0)
            {
                // 返回空数组对象，值为“[]”
                return resultArray.toString();
            }
            for(LfWeiUserInfo userInfo : userInfos)
            {
                JSONObject json = new JSONObject();
                json.put("name", userInfo.getNickName());
                json.put("gpid", userInfo.getGId());
                json.put("uid", userInfo.getWcId());
                
                resultArray.add(json);
            }
            return resultArray.toString();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"获取未分组的关注列表失败！");
            resultArray.clear();
            // 返回空数组对象，值为“[]”
            return resultArray.toString();
        }
    }
        
    /**
     * 更新群组数量
     * @description    
     * @param aId
     * @param gpIds
     * @return       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-2-25 下午05:52:23
     */
    public boolean updateGroupCount(Connection conn,String gpIds)
    {
        boolean result = false;
        try
        {
            result = new GroupManageDao().updateGroupCount(conn, gpIds);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"更新群组数量失败！");
        }
        return result;
    }
}
