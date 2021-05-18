package com.montnets.emp.znly.biz;

/**
 * @description  
 * @author Administrator <foyoto@gmail.com>
 * @datetime 2014-1-20 下午03:53:21
 */
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiCount;
import com.montnets.emp.znly.dao.CountDao;

/**
 * @description 
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fangyt <foyoto@gmail.com>
 * @datetime 2014-1-20 下午03:53:21
 */
public class CountBiz extends SuperBiz
{
    CountDao countDao = new CountDao();
    
    /**
     * 得到当天的统计数据
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-20 下午02:28:34
     */
    public LfWeiCount getTodayCount(Long aId){
        return countDao.getTodayCount(aId);
    }
    
    /**
     * @description  创建当前统计数据 
     * @param acct 公众帐号Id
     * @param follow 关注数
     * @param unfollow 取消关注数
     * @param income 净增加数
     * @param amount 总数
     * @return                   
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2014-1-20 下午05:31:58
     */
    public LfWeiCount ceateTodayCount(LfWeiAccount acct,Integer follow,Integer unfollow,Integer income,Integer amount){
        LfWeiCount otWeiCount = new LfWeiCount();
        try{
            otWeiCount.setAId(acct.getAId());
            otWeiCount.setFollowCount(follow == null? 0 : follow);
            otWeiCount.setUnfollowCount(unfollow == null? 0 : unfollow);
            otWeiCount.setIncomeCount(income == null? 0 : income);
            otWeiCount.setAmountCount(amount == null? 0 : amount);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            otWeiCount.setDayTime(Timestamp.valueOf(df.format(System.currentTimeMillis())));
            otWeiCount.setCorpCode(acct.getCorpCode());
            //ottDao.save(otWeiCount);
        }catch(Exception e){
            EmpExecutionContext.error(e, "查询当前公众帐号当天的统计记录失败！");
            return null;
        }
        return otWeiCount;
    }
    
    /**
     * 得到上一次的累计次数
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-20 下午02:28:34
    */
    public Integer getLastAmount(Long aId){
        LfWeiCount otWeiCount = new LfWeiCount();
        int amount = 0;
        try
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("AId", String.valueOf(aId));
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("dayTime", "DESC");
            List<LfWeiCount> otWeiCountList = empDao.findListByCondition(LfWeiCount.class, conditionMap, orderbyMap);
            if(otWeiCountList!=null&&otWeiCountList.size()>0){
                otWeiCount = otWeiCountList.get(0);
                amount = otWeiCount.getAmountCount() == null ? 0 : otWeiCount.getAmountCount();
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "查询用户统计记录失败！");
        }
        return amount;
    }
    
}