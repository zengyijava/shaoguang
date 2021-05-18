/**
 * Program  : app_homeeditDao.java
 * Author   : zousy
 * Create   : 2014-6-13 上午08:40:09
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.appmage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.appmage.TableLfAppTcMomsg;
import com.montnets.emp.util.PageInfo;


/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-13 上午08:40:09
 */
public class app_morequeryDao extends SuperDAO
{
	public List<DynaBean> getMoList(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception{
		List<DynaBean> beanList = null;
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		String sql = "select "+TableLfAppTcMomsg.FROMUSER+","+TableLfAppTcMomsg.FROMNAME+","
		+TableLfAppTcMomsg.MSG_TYPE+","+TableLfAppTcMomsg.MSG_ID+","+TableLfAppTcMomsg.MSG_TEXT+","
		+TableLfAppTcMomsg.MSG_JSON+","+TableLfAppTcMomsg.CREATETIME
		+" from "+TableLfAppTcMomsg.TABLE_NAME;
		String countSql  = "select count(*) totalcount from "+TableLfAppTcMomsg.TABLE_NAME;
		String orderSql = " order by "+TableLfAppTcMomsg.CREATETIME+" desc";
		
		//是否有where，默认为false即没where
		boolean hasWhere = false;
		StringBuffer sqlCon = new StringBuffer();
		//账号
		String fromuser = conditionMap.get("fromuser");
		if(StringUtils.isNotBlank(fromuser)){
			hasWhere = addWhereOrAnd(sqlCon, hasWhere);
			sqlCon.append(TableLfAppTcMomsg.FROMUSER+" = '"+fromuser+"'" );
		}
		
		//APP用户账号
		String fromname = conditionMap.get("fromname");
		if(StringUtils.isNotBlank(fromname)){
			hasWhere = addWhereOrAnd(sqlCon, hasWhere);
			sqlCon.append(" ("+TableLfAppTcMomsg.FROMUSER+" like '%"+ fromname+"%'")
			.append(" or "+TableLfAppTcMomsg.FROMNAME+" like '%"+ fromname+"%')");
		}
		
		//消息类型
		String msgtype = conditionMap.get("msgtype");
		if(StringUtils.isNotBlank(msgtype)){
			hasWhere = addWhereOrAnd(sqlCon, hasWhere);
			sqlCon.append(TableLfAppTcMomsg.MSG_TYPE+" = "+ msgtype);
		}
		//消息内容
		String msgtext = conditionMap.get("msgtext");
		if(StringUtils.isNotBlank(msgtext)){
			hasWhere = addWhereOrAnd(sqlCon, hasWhere);
			sqlCon.append(TableLfAppTcMomsg.MSG_TEXT+" like '%"+ msgtext+"%'");
		}
		//起始时间
		String starttime = conditionMap.get("starttime");
		if(StringUtils.isNotBlank(starttime)){
			hasWhere = addWhereOrAnd(sqlCon, hasWhere);
			sqlCon.append(TableLfAppTcMomsg.CREATETIME+" >="+ genericDao.getTimeCondition(starttime));
		}
		//结束时间
		String endtime = conditionMap.get("endtime");
		if(StringUtils.isNotBlank(endtime)){
			hasWhere = addWhereOrAnd(sqlCon, hasWhere);
			sqlCon.append(TableLfAppTcMomsg.CREATETIME+" <="+genericDao.getTimeCondition(endtime));
		}
		
//		sqlCon.append(" and mo."+TableLfAppTcMomsg.CORP_CODE+" = '"+conditionMap.get("lgcorpcode")+"'");
		sql = sql + sqlCon.toString() + orderSql;
		if(pageInfo == null){
			beanList = getListDynaBeanBySql(sql);
		}else{
			countSql = countSql + sqlCon;
			beanList = genericDao.findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		return beanList;
	}
	
	/**
	 * 按情况拼接where或and
	 * @param sql
	 * @param hasWhere false为没where关键词
	 * @return 第一次拼接后将会有where，直接返回true
	 */
	private boolean addWhereOrAnd(StringBuffer sql, boolean hasWhere)
	{
		if(!hasWhere)
		{
			sql.append(" where ");
		}
		else
		{
			sql.append(" and ");
		}
		return true;
	}
}

