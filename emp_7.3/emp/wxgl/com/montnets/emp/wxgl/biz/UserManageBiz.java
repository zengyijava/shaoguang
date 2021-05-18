/**
 * @description 用户管理BIZ
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午03:22:49
 */
package com.montnets.emp.wxgl.biz;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxgl.dao.UserManageDao;

/**
 * @description 用户管理
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午03:22:49
 */
public class UserManageBiz extends SuperBiz
{
    
    /**
     * @description 默认构造函数          			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-29 下午03:22:49
     */
    public UserManageBiz()
    {
        super();
    }
    
    /**
     * 获取关注用户列表
     * @description    
     * @param corpCode 集团编码
     * @param conditionMap 查询条件
     * @param pageInfo 分页对象
     * @return  动态bean数据集合     			  
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-6 下午01:09:07
     */
    public List<DynaBean> getUserInfoList(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
    {
        List<DynaBean> beans = null;
        try
        {
            beans = new UserManageDao().getUserInfoList(corpCode, conditionMap, null, pageInfo);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"获取关注者用户列表失败！");
        }
        return beans;
    }
}
