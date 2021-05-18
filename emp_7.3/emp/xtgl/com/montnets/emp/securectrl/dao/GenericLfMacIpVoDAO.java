package com.montnets.emp.securectrl.dao;

import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.securectrl.vo.LfMacIpVo;
import com.montnets.emp.table.securectrl.TableLfMacIp;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

public class GenericLfMacIpVoDAO extends SuperDAO
{
	/**
	 * 获取IP-MAC-动态口令绑定集合
	 * @param lfSysuser
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfMacIpVo> findLfMacIpVo(LfSysuser lfSysuser, Map<String,String> conditionMap,PageInfo pageInfo)
			throws Exception
	{
		//拼接查询sql
		String sql = "select lfdep." + TableLfDep.DEP_NAME +",sysuser."+TableLfSysuser.GUID+",sysuser."
		+TableLfSysuser.NAME+ ",sysuser."+ TableLfSysuser.USER_NAME 
		+",lfmacip."+TableLfMacIp.LMIID+ ",lfmacip." + TableLfMacIp.IP_ADDR+",lfmacip."
		+TableLfMacIp.MAC_ADDR+",lfmacip."+TableLfMacIp.TYPE
		+ ",lfmacip." + TableLfMacIp.CREATOR_NAME + ",lfmacip."
		+ TableLfMacIp.CREAT_TIME +",lfmacip."+TableLfMacIp.DT_PASS+ " from "+TableLfSysuser.TABLE_NAME +" sysuser left join "
		+TableLfMacIp.TABLE_NAME +" lfmacip on lfmacip."+TableLfMacIp.GUID +"=sysuser."
		+TableLfSysuser.GUID  ;
		
		//查询条件==绑定类型
		if(conditionMap.get("type") != null && !"".equals(conditionMap.get("type"))){
			sql += " and lfmacip."+TableLfMacIp.TYPE+"="+conditionMap.get("type");
		}
		sql += " left join "+TableLfDep.TABLE_NAME+" lfdep on sysuser."
		+TableLfSysuser.DEP_ID+"=lfdep."+TableLfDep.DEP_ID ;
		StringBuffer conditionSql = new StringBuffer(" where sysuser.").append(TableLfSysuser.CORP_CODE)
		.append("='").append(conditionMap.get("corpCode")).append("'").append(" and sysuser.")
		.append(TableLfSysuser.USER_STATE).append("=1");
		
		//查询条件---机构id
		if(conditionMap.get("depId") != null && !"".equals(conditionMap.get("depId"))){
			String depIdCon = new DepDAO().getChildUserDepByParentID(Long.parseLong(conditionMap.get("depId")),"lfdep."+TableLfDep.DEP_ID);
			conditionSql.append(" and ").append(depIdCon).append(" and sysuser.").append(TableLfSysuser.DEP_ID)
			.append(" in(").append(" select ").append(TableLfSysuser.DEP_ID).append(" from ")
			.append(TableLfDomination.TABLE_NAME).append(" domina where domina.").append(TableLfSysuser.USER_ID)
			.append(" = ").append(lfSysuser.getUserId()).append(")");
		}
		
		//查询条件---操作员id
		if(conditionMap.get("userName") != null && !"".equals(conditionMap.get("userName"))){
			conditionSql.append(" and sysuser.").append(TableLfSysuser.USER_NAME).append(" like'%")
			.append(conditionMap.get("userName")).append("%'");
		}
		//查询条件---操作员名称
		if(conditionMap.get("name") != null && !"".equals(conditionMap.get("name"))){
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME).append(" like'%")
			.append(conditionMap.get("name")).append("%'");
		}
		//查询条件---IP地址
		if(conditionMap.get("ipaddr") != null && !"".equals(conditionMap.get("ipaddr"))){
			conditionSql.append(" and lfmacip.").append(TableLfMacIp.IP_ADDR).append(" like'%")
			.append(conditionMap.get("ipaddr")).append("%'");
		}
		//查询条件----MAC地址
		if(conditionMap.get("macaddr") != null && !"".equals(conditionMap.get("macaddr"))){
			conditionSql.append(" and lfmacip.").append(TableLfMacIp.MAC_ADDR).append(" like'%")
			.append(conditionMap.get("macaddr")).append("%'");
		}
		//查询条件----是否绑定MAC地址
		if(conditionMap.get("isBindMac") != null && !"".equals(conditionMap.get("isBindMac"))){
			if("1".equals(conditionMap.get("isBindMac"))){
				//绑定
				if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
					//适用oracle数据库
					conditionSql.append(" and lfmacip.").append(TableLfMacIp.MAC_ADDR).append(" is not null ");
				}else{
					//适用sqlservr、mysql、db2数据库
					conditionSql.append(" and (lfmacip.").append(TableLfMacIp.MAC_ADDR).append(" is not null and lfmacip.").append(TableLfMacIp.MAC_ADDR).append("<>'')");
				}
			}else {
				//未绑定
				conditionSql.append(" and (lfmacip.").append(TableLfMacIp.MAC_ADDR).append(" is null or lfmacip.").append(TableLfMacIp.MAC_ADDR).append("='')");
			}
		}
		//查询条件----是否绑定IP地址
		if(conditionMap.get("isBindIp") != null && !"".equals(conditionMap.get("isBindIp"))){
			if("1".equals(conditionMap.get("isBindIp"))){
				//绑定
				if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
					//适用oracle数据库
					conditionSql.append(" and lfmacip.").append(TableLfMacIp.IP_ADDR).append(" is not null ");
				}else{
					//适用sqlservr、mysql、db2数据库
					conditionSql.append(" and (lfmacip.").append(TableLfMacIp.IP_ADDR).append(" is not null and lfmacip.").append(TableLfMacIp.IP_ADDR).append("<>'')");
				}
			}else {
				//未绑定
				conditionSql.append(" and (lfmacip.").append(TableLfMacIp.IP_ADDR).append(" is null or lfmacip.").append(TableLfMacIp.IP_ADDR).append("='')");
			}
		}
		//查询条件----是否启用动态口令
		if(conditionMap.get("isBindPwd") != null && !"".equals(conditionMap.get("isBindPwd"))){
			if("1".equals(conditionMap.get("isBindPwd"))){
				//启用
				conditionSql.append(" and lfmacip.").append(TableLfMacIp.DT_PASS).append("=1");
			}else {
				//未启用
				conditionSql.append(" and (lfmacip.").append(TableLfMacIp.DT_PASS).append("=0 or ")
				.append(" lfmacip.").append(TableLfMacIp.DT_PASS).append(" is null)");
			}
		}
		
		sql+=conditionSql;
		String cSql = sql;
		String order="";
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			order = " asc";
		}else{
			order = " desc";
		}
		//拼接排序条件
		String orderBySql = " order by lfmacip."+TableLfMacIp.DT_PASS+",sysuser.GUID" + order;
		sql += orderBySql;
		//计数sql
		String countSql = "select count(*) totalcount from(";
		countSql = countSql + cSql +")temp_t";
		//执行查询
		List<LfMacIpVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
				LfMacIpVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
	}

	
}
