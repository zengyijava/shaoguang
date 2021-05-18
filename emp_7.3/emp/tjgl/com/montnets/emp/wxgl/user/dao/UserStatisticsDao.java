/**
 * @description 统计管理 - 用户统计 - 用户属性
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2014-1-10 上午09:17:47
 */
package com.montnets.emp.wxgl.user.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.wxgl.LfWeiCount;

/**
 * @description 统计管理 - 用户统计 - 用户属性
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2014-1-10 上午09:17:47
 */

public class UserStatisticsDao extends SuperDAO
{

	
    /**
     * @description 默认构造函数
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-10 上午09:17:47
     */
    public UserStatisticsDao()
    {
        super();
    }

    /**
     * 查询用户性别统计结果
     * 
     * @param corpCode
     * @param conditionMap
     * @param orderbyMap
     * @param pageInfo
     * @return
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-10 上午09:33:27
     */
    public List<DynaBean> getUserSexDistributionList(String aid, String corpCode)
    {
        String sql = "SELECT SEX as sex,COUNT(SEX) as countsex FROM LF_WEI_USERINFO  "+ StaticValue.getWITHNOLOCK() +" WHERE CORP_CODE = '" + corpCode + "' and A_ID = " + aid + " GROUP BY sex";
        List<DynaBean> beans = this.getListDynaBeanBySql(sql);
        //	new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, null, null, StaticValue.EMP_POOLNAME, null);
        return beans;
    }

    /**
     * 查询用户省份统计结果
     * 
     * @param corpCode
     * @param conditionMap
     * @param orderbyMap
     * @param pageInfo
     * @return
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-10 上午09:33:27
     */
    public List<DynaBean> getUserProvinceDistributionList(String aid, String corpCode)
    {

    	int DBTYPE = Integer.parseInt(SystemGlobals.getValue("DBType"));
    	String sql = "SELECT PROVINCE as province,COUNT(PROVINCE) as countprovince FROM LF_WEI_USERINFO   "+ StaticValue.getWITHNOLOCK() +"  WHERE CORP_CODE = '" + corpCode + "' and A_ID = " + aid + " GROUP BY province";
    	if(1==DBTYPE){
    		 sql = "SELECT PROVINCE as province,COUNT(NVL(PROVINCE,'未知')) as countprovince FROM LF_WEI_USERINFO   "+ StaticValue.getWITHNOLOCK() +"  WHERE CORP_CODE = '" + corpCode + "' and A_ID = " + aid + " GROUP BY province";
    	}
        List<DynaBean> beans = this.getListDynaBeanBySql(sql); 
        	//new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, null, null, StaticValue.EMP_POOLNAME, null);
        return beans;
    }

    /**
     * 查询用户城市统计结果
     * 
     * @param corpCode
     * @param conditionMap
     * @param orderbyMap
     * @param pageInfo
     * @return
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-10 上午09:33:27
     */
    public List<DynaBean> getUserCityDistributionList(String aid, String corpCode)
    {
        String sql = "SELECT CITY as city,COUNT(CITY) as countcity FROM LF_WEI_USERINFO  "+ StaticValue.getWITHNOLOCK() +"  WHERE CORP_CODE = '" + corpCode + "' and A_ID = " + aid + " GROUP BY city";
        List<DynaBean> beans = this.getListDynaBeanBySql(sql);
        //	new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, null, null, StaticValue.EMP_POOLNAME, null);
        return beans;
    }

    /**
     * 查询用户（本日、本周、本月）关注数
     * 
     * @param corpCode
     * @param conditionMap
     * @param orderbyMap
     * @param pageInfo
     * @return 记录总数
     * @author fanglu <fanglu@montnets.com>
     * @throws Exception
     * @datetime 2014-1-10 上午09:33:27
     */
    public int getUserGrowCount(String startTime, String endTime, String aid, String corpCode) throws Exception
    {
        String sql = "SELECT SUM(follow_count) totalcount from LF_WEI_COUNT   "+ StaticValue.getWITHNOLOCK()
	        +"  where CORP_CODE = '" + corpCode + "' and A_ID = " + aid +" and "
	        + appendTimeSql(startTime, endTime);
        return findCountBySQL(sql);
    }

    /**
     * 查询用户（本日、本周、本月）取消关注数
     * 
     * @param startTime
     * @param endTime
     * @param aid
     * @param corpCode
     * @return
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-14 下午12:55:20
     */
    public int getUserUnFollowCount(String startTime, String endTime, String aid, String corpCode) throws Exception
    {
        String sql = "SELECT SUM(unfollow_count) totalcount from LF_WEI_COUNT  "+ StaticValue.getWITHNOLOCK()
        +"  where CORP_CODE = '" + corpCode + "' and A_ID = " + aid + " and "
        + appendTimeSql(startTime, endTime);
        return findCountBySQL(sql);
    }

    /**
     * 查询累积关注用户数
     * 
     * @description
     * @param aid
     * @param corpCode
     * @return
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-14 上午11:47:11
     */
    public int getSubscriberCount(String aid, String corpCode) throws Exception
    {
        String sql = "SELECT count(wc_id) as totalcount from LF_WEI_USERINFO  "
        	+ StaticValue.getWITHNOLOCK() +" where CORP_CODE = '" + corpCode + "' and A_ID = " + aid ;
        return  findCountBySQL(sql);
    }

    /**
     * 根据起至日期查询用户增长信息
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-18 下午06:31:47
     */
    public List<LfWeiCount> getUserCountList(String corpCode,String aid,String startTime, String endTime)
    {
        List<LfWeiCount> otWeiCountList = null;
        String sql = "SELECT * from LF_WEI_COUNT  "+ StaticValue.getWITHNOLOCK() +" WHERE CORP_CODE = '"
	        + corpCode + "' and A_ID = " + aid + " and " + appendTimeSql(startTime, endTime);
        try
        {
            otWeiCountList = findEntityListBySQL(LfWeiCount.class, sql, StaticValue.EMP_POOLNAME);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"获取微信用户统计信息失败!");
            return null;
        }
        return otWeiCountList;
    }
    
    private String appendTimeSql(String startTime, String endTime)
    {
    	 if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
         {
 	        return "DAYTIME >= to_date('" 
 		        + startTime + "','yyyy-MM-dd HH24:MI:SS') and DAYTIME <= to_date('" 
 		        + endTime + "','yyyy-MM-dd HH24:MI:SS') ";
         }else
         {
        	return "DAYTIME >= '" 
 		        + startTime + "' and DAYTIME <= '" 
 		        + endTime + "' ";
         }
    }
}
