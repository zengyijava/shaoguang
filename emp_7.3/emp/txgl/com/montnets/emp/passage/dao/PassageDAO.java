package com.montnets.emp.passage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.servmodule.txgl.entity.XtGateQueue;
import com.montnets.emp.table.pasroute.TableGtPortUsed;
import com.montnets.emp.table.pasroute.TableLfMmsAccbind;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.table.passage.TableXtGateQueue;
import com.montnets.emp.util.PageInfo;

/**
 * 通道管理dao
 * @description 
 * @project p_txgl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-10-21 上午11:17:44
 */
public class PassageDAO extends SuperDAO{
	
	/**
	 * 通道管理列表信息find查询方法
	 * @description    
	 * @param corp 企业编码
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息
	 * @return 结果集
	 * @throws Exception       			 
	 * @author zhangmin <ljr0300@163.com>
	 * @datetime 2013-10-21 上午11:18:12
	 */
	public List<XtGateQueue> findGateInfoByCorp(String corp,
			LinkedHashMap<String,String> conditionMap, PageInfo pageInfo)
			throws Exception {
		//拼接sql
		String sql = "select distinct xt.* from " + TableXtGateQueue.TABLE_NAME
				+ " xt join " + TableGtPortUsed.TABLE_NAME + " gt on " + "xt." + TableXtGateQueue.SPGATE
				+ "=gt." + TableGtPortUsed.SPGATE + " where (gt." + TableGtPortUsed.USER_ID
				+ " in (select sp.spuser from " + TableLfSpDepBind.TABLE_NAME
				+ " sp where sp." + TableLfSpDepBind.CORP_CODE + "='" + corp + "')"
				+ " or gt."+ TableGtPortUsed.USER_ID 
				+ " in (select "+TableLfMmsAccbind.MMS_USER+" from "+TableLfMmsAccbind.TABLE_NAME
				+" where "+TableLfMmsAccbind.CORP_CODE+" = '"+corp+"') )";
				;
		//查询条件拼接
		StringBuffer conditionStr = new StringBuffer("");
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{

			String spisuncm = conditionMap.get("spisuncm");
			String gateType = conditionMap.get("gateType");
			String gateName = conditionMap.get("gateName");
			String signstr = conditionMap.get("signstr");
			String spgate = conditionMap.get("spgate");
			
			if(spisuncm!=null && !"".equals(spisuncm))
			{
				conditionStr.append(" and xt.spisuncm=").append(spisuncm);
			}
			if(gateType!=null && !"".equals(gateType))
			{
				conditionStr.append(" and xt.gatetype=").append(gateType);
			}
			if(gateName!=null && !"".equals(gateName))
			{
				conditionStr.append(" and xt.gatename like '%").append(gateName).append("%'");
			}
			if(signstr!=null && !"".equals(signstr))
			{
				conditionStr.append(" and xt.signstr like '%").append(signstr).append("%'");
			}
			if(spgate!=null && !"".equals(spgate))
			{
				conditionStr.append(" and xt.spgate like '%").append(spgate).append("%'");
			}
			sql=sql+conditionStr.toString();
		}
		
		if (pageInfo == null) {
			return findEntityListBySQL(XtGateQueue.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")derivedtbl_1").toString();
//			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(XtGateQueue.class,
//							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(XtGateQueue.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	/**
	 * @description  通道管理列表信息find查询方法  
	 * @param conditionMap
	 * @param pageInfo
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @throws Exception 
	 * @datetime 2015-4-22 下午03:42:10
	 */
	public List<XtGateQueue> getByCondition(LinkedHashMap<String, String> conditionMap , PageInfo pageInfo) throws Exception{
		//拼接sql
		String sql = "select * from " + TableXtGateQueue.TABLE_NAME;
		//查询条件拼接
		StringBuffer conditionStr = new StringBuffer("");
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{

			String spisuncm = conditionMap.get("spisuncm");
			String gateType = conditionMap.get("gateType");
			String gateName = conditionMap.get("gateName");
			String signstr = conditionMap.get("signstr");
			String spgate = conditionMap.get("spgate");
			
			if(spisuncm!=null && !"".equals(spisuncm))
			{
				conditionStr.append(" and spisuncm =").append(spisuncm);
			}
			if(gateType!=null && !"".equals(gateType))
			{
				conditionStr.append(" and gatetype =").append(gateType);
			}
			if(gateName!=null && !"".equals(gateName))
			{
				conditionStr.append(" and gatename like '%").append(gateName).append("%'");
			}
			if(signstr!=null && !"".equals(signstr))
			{
				conditionStr.append(" and (signstr like '%").append(signstr).append("%'")
				.append(" or ensignstr like '%").append(signstr).append("%')");
			}
			if(spgate!=null && !"".equals(spgate))
			{
				conditionStr.append(" and spgate like '%").append(spgate).append("%'");
			}
			sql=sql+conditionStr.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		String orderSql = " order by id desc";
		if (pageInfo == null) {
			sql += orderSql;
			return findEntityListBySQL(XtGateQueue.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")derivedtbl_1").toString();
			sql += orderSql;
//			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(XtGateQueue.class,
//							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(XtGateQueue.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}

}
