package com.montnets.emp.tailmanage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

public class TailMgrDAO extends SuperDAO{
	
	/**
	 * 获得贴尾查询列表信息
	 * @param conditionMap
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public List<DynaBean> getTailRecord(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws NumberFormatException, Exception{
		
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		String sql ="select tail.bustail_name,tail.bustail_id, tail.content,tail.create_time,lfuser.user_name,lfuser.name,lfdep.dep_name from "+getTableSql();
		
		String conditionSql=" where";
		
		String userIdStr = conditionMap.get("userId");
		//机构权限
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userIdStr);
		String dominationSql = new StringBuffer(" (lfuser.").append(
				TableLfSysuser.USER_ID).append("=").append(userIdStr).append(
				" or lfuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(domination).append("))").append(" and lfuser.").append(TableLfSysuser.USER_ID).append("<>1").toString();
		conditionSql= conditionSql+dominationSql;
		
		//创建人
		if(conditionMap.get("userName")!=null&&!"".equals(conditionMap.get("userName")))
		{
			conditionSql = conditionSql + " and lfuser.name like '%" + conditionMap.get("userName") +"%'"; 
		}
		//贴尾名称
		if(conditionMap.get("tailname")!=null&&!"".equals(conditionMap.get("tailname")))
		{
			conditionSql = conditionSql + " and tail.bustail_name like '%" + conditionMap.get("tailname") +"%'"; 
		}
		
		//机构
		String deptid = conditionMap.get("deptid");
		//个人权限只能查自己
		if(null!=conditionMap.get("permissionType") && "1".equals(conditionMap.get("permissionType"))){
			if(null!=conditionMap.get("permissionUserName") && !"".equals(conditionMap.get("permissionUserName"))){
				conditionSql += " and lfuser.user_name = '"+conditionMap.get("permissionUserName")+"' ";				
			}
		}else{//机构权限
			if(deptid!=null&&!"".equals(deptid)){
				//包含子机构
				if("1".equals(conditionMap.get("isContainsSun"))){
							String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(deptid),TableLfDep.DEP_ID);
							conditionSql= conditionSql +" and lfuser."+depid;
	//					}
				}else{
					 //不包含子机构
					conditionSql= conditionSql +" and lfuser.dep_id  = "+deptid+"";
	
				}
	
			}
		}
		//根据业务名称查询 (去掉业务类型：手动)
		if(conditionMap.get("buss")!=null&&!"".equals(conditionMap.get("buss")))
		{
			conditionSql = conditionSql + " and tail.bustail_id in ( select lf_bus_tailtmp.smstail_id from lf_bus_tailtmp,lf_busmanager where (lf_busmanager.bus_type=1 or lf_busmanager.bus_type=2) and lf_bus_tailtmp.bus_id=lf_busmanager.bus_id  and lf_bus_tailtmp.associate_type=0 and lf_busmanager.bus_name like '%"+conditionMap.get("buss")+"%')";
		}
		
		if(conditionMap.get("startSubmitTime")!=null&&!"".equals(conditionMap.get("startSubmitTime")))
		{
			conditionSql = conditionSql + " and tail.create_time >="+genericDao.getTimeCondition(conditionMap.get("startSubmitTime"));
		}
		if(conditionMap.get("endSubmitTime")!=null&&!"".equals(conditionMap.get("endSubmitTime")))
		{
			conditionSql = conditionSql + " and tail.create_time <="+genericDao.getTimeCondition(conditionMap.get("endSubmitTime"));
		}
		String orderby=" order by tail.create_time desc";
		sql=sql+conditionSql+orderby;
		String countSql = "select count(*) totalcount from "+getTableSql()+conditionSql;

		List<DynaBean> returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return returnList;
		
	}
	 /**
	  * 根据ID查询业务集合(去掉业务类型：手动)
	  * @param id sql是数字类型，查询时候不要加单引号(DB兼容)
	  * @return
	  */
	public List<DynaBean>  busList(String id){
		List<DynaBean> list=this.getListDynaBeanBySql("select  LF_BUSMANAGER.BUS_NAME,LF_BUSMANAGER.BUS_CODE,LF_BUSMANAGER.BUS_ID, LF_BUSMANAGER.STATE from	 LF_BUS_TAILTMP,LF_BUSMANAGER where  (LF_BUSMANAGER.BUS_TYPE=1 or LF_BUSMANAGER.BUS_TYPE=2 ) and LF_BUSMANAGER.BUS_ID=LF_BUS_TAILTMP .BUS_ID and LF_BUS_TAILTMP.SMSTAIL_ID="+id+"");
		
		
		return list;
	}
	
	/**
	 *  获得表名称
	 * @return
	 */
	public String getTableSql(){
		return " LF_BUSTAIL tail left join LF_SYSUSER lfuser on lfuser.user_id=tail.user_id left join lf_dep lfdep on lfdep.dep_id=lfuser.dep_id  " ; 
	}
	
	/**
	 *获得业务信息 (显示所有已启用的业务,去掉业务类型：手动)
	 * @return 业务信息集合
	 */
	public List<DynaBean> getBusiness(String name){
		List<DynaBean> list=null;
		//SQLSERVER 排序时候需要单独处理 数据库兼容
		if(name==null||"".equals(name)){
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				list=this.getListDynaBeanBySql("select  distinct  lf_busmanager.bus_code,lf_busmanager.bus_name,lf_busmanager.bus_id, lf_bus_tailtmp.bus_id as tablelink,lf_bustail.bustail_name,lf_busmanager.state from lf_busmanager left join lf_bus_tailtmp on  lf_bus_tailtmp.bus_id=lf_busmanager.bus_id and lf_bus_tailtmp.associate_type=0  left join  lf_bustail on lf_bus_tailtmp.smstail_id=lf_bustail.bustail_id where (lf_busmanager.bus_type=1 or lf_busmanager.bus_type=2)  and lf_busmanager.state=0   order by tablelink ");
			}else{
				list=this.getListDynaBeanBySql("select  distinct  lf_busmanager.bus_code,lf_busmanager.bus_name,lf_busmanager.bus_id, lf_bus_tailtmp.bus_id as tablelink,lf_bustail.bustail_name,lf_busmanager.state from lf_busmanager left join lf_bus_tailtmp on  lf_bus_tailtmp.bus_id=lf_busmanager.bus_id and lf_bus_tailtmp.associate_type=0  left join  lf_bustail on lf_bus_tailtmp.smstail_id=lf_bustail.bustail_id where (lf_busmanager.bus_type=1 or lf_busmanager.bus_type=2)  and lf_busmanager.state=0   order by tablelink desc ");
			}
			
		}else{
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
				
				list=this.getListDynaBeanBySql("select  distinct  lf_busmanager.bus_code,lf_busmanager.bus_name,lf_busmanager.bus_id,lf_bustail.bustail_name, lf_bus_tailtmp.bus_id as tablelink from lf_busmanager left join lf_bus_tailtmp on  lf_bus_tailtmp.bus_id=lf_busmanager.bus_id and lf_bus_tailtmp.associate_type=0  left join  lf_bustail on lf_bus_tailtmp.smstail_id=lf_bustail.bustail_id  where    (lf_busmanager.bus_type=1 or lf_busmanager.bus_type=2) and lf_busmanager.state=0 and  lf_busmanager.bus_name like '%"+name.trim()+"%' order by tablelink");
			}else{
				
				list=this.getListDynaBeanBySql("select  distinct  lf_busmanager.bus_code,lf_busmanager.bus_name,lf_busmanager.bus_id,lf_bustail.bustail_name, lf_bus_tailtmp.bus_id as tablelink from lf_busmanager left join lf_bus_tailtmp on  lf_bus_tailtmp.bus_id=lf_busmanager.bus_id and lf_bus_tailtmp.associate_type=0  left join  lf_bustail on lf_bus_tailtmp.smstail_id=lf_bustail.bustail_id  where    (lf_busmanager.bus_type=1 or lf_busmanager.bus_type=2) and lf_busmanager.state=0 and  lf_busmanager.bus_name like '%"+name.trim()+"%' order by tablelink desc ");
			}
			
			
		}
		return list;
	}
	
	/**
	 *  查询部门相关信息
	 * @param sysuserID
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> findDomUserBySysuserIDOfSmsTaskRecordByDep(String sysuserID,String depId)
    throws Exception {
		//拼接sql
		StringBuffer domination = new StringBuffer("select ").append(
			TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(" where ").append(
							TableLfDomination.USER_ID).append("=").append(sysuserID);
		StringBuffer dominationSql = new StringBuffer(" where (").append(
			TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
			" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
					domination).append(")) and ").append(TableLfSysuser.USER_ID)
					.append("<>1 and ").append(TableLfSysuser.DEP_ID).append("=").append(depId);
		String sql = new StringBuffer("select * from ").append(
			TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
		//排序条件拼接
		sql += " order by " + TableLfSysuser.NAME + " asc";
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
			StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
	}
}
