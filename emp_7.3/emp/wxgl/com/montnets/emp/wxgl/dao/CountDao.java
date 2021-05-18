/**
 * @description  
 * @author Administrator <foyoto@gmail.com>
 * @datetime 2014-1-20 下午01:14:55
 */
package com.montnets.emp.wxgl.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.wxgl.LfWeiCount;

/**
 * @author fangyt <foyoto@gmail.com>
 * @datetime 2014-1-20 下午01:14:55
 */
public class CountDao extends SuperDAO
{
  //得到当天的统计记录
  public LfWeiCount getTodayCount(Long aId){
      LfWeiCount otWeiCount = new LfWeiCount();
      try{
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String today = df.format(System.currentTimeMillis());
         String sql  = "select * from lf_wei_count where A_ID = '" + String.valueOf(aId) + "' and YEAR(DAYTIME) =YEAR('"+ today +"') and MONTH(DAYTIME) = MONTH('"+today+"') and DAY(DAYTIME) = DAY('"+today+"')";
         List<LfWeiCount> otWeicountList = findEntityListBySQL(LfWeiCount.class, sql, StaticValue.EMP_POOLNAME);
          if(otWeicountList!=null&&otWeicountList.size()>0){
              otWeiCount = otWeicountList.get(0);
          }else {
            return null;
        }
      }catch(Exception e){
          EmpExecutionContext.error(e, "查询当前公众帐号当天的统计记录失败！");
          return null;
      }
      return otWeiCount;
  }
}
