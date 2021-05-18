package com.montnets.emp.bustaocan.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;


public class BusTaoCanDAO extends SuperDAO {
	
	public List<DynaBean> getTaoCanRecord(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception{
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		String sql ="select taocan.taocan_name,taocan.taocan_code,charge.buckle_type,charge.buckle_date,taocan.taocan_type,taocan.taocan_money,taocan.start_date,taocan.end_date,taocan.state,taocan.create_time,taocan.update_time,lfuser.user_name,lfuser.name,lfdep.dep_name  from "+getTableSql();
		
		String corpCode = conditionMap.get("corpCode");
		
		String conditionSql=" where  lfuser."+TableLfSysuser.CORP_CODE +" ='" +corpCode +"'  and ";
		
		String userIdStr = conditionMap.get("userId");
		//机构权限
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userIdStr);
		String dominationSql = new StringBuffer("(lfuser.").append(
				TableLfSysuser.USER_ID).append("=").append(userIdStr).append(
				" or lfuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(domination).append("))").append(" and lfuser.").append(TableLfSysuser.USER_ID).append("<>1").toString();
		conditionSql= conditionSql+dominationSql;
		
		//套餐名称
		if(conditionMap.get("taocan_name")!=null&&!"".equals(conditionMap.get("taocan_name")))
		{
			conditionSql = conditionSql + " and taocan.taocan_name like '%" + conditionMap.get("taocan_name") +"%'"; 
		}
		//套餐编号
		if(conditionMap.get("taocan_code")!=null&&!"".equals(conditionMap.get("taocan_code")))
		{
			conditionSql = conditionSql + " and taocan.taocan_code like '%" + conditionMap.get("taocan_code") +"%'"; 
		}
		//状态
		if(conditionMap.get("state")!=null&&!"".equals(conditionMap.get("state")))
		{
			conditionSql = conditionSql + " and taocan.state =" + conditionMap.get("state") +""; 
		}
		//计费类型
		if(conditionMap.get("freeType")!=null&&!"".equals(conditionMap.get("freeType")))
		{
			conditionSql = conditionSql + " and taocan.taocan_type =" + conditionMap.get("freeType") +""; 
		}
		
		//业务包编号 
		if(conditionMap.get("pageckcode")!=null&&!"".equals(conditionMap.get("pageckcode")))
		{
			conditionSql = conditionSql + " and taocan.taocan_code in ( select taocan_code from   lf_buspkgetaocan   where associate_type=0 and  package_code like '%"+conditionMap.get("pageckcode")+"%')";
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
					LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(deptid));
//					if(lfDep!=null&&lfDep.getDepLevel().intValue()==1){
//						conditionSql= conditionSql +" and lfuser.dep_id  ='"+deptid+"'";
//					}else{
						String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(deptid),TableLfDep.DEP_ID);
						conditionSql= conditionSql +" and lfuser."+depid;
//					}
			}else{
				 //不包含子机构
				conditionSql= conditionSql +" and lfuser.dep_id  = "+deptid+"";

			}

		}
		}
		//开始时间
		if(conditionMap.get("startSubmitTime")!=null&&!"".equals(conditionMap.get("startSubmitTime")))
		{
			conditionSql = conditionSql + " and taocan.create_time >="+genericDao.getTimeCondition(conditionMap.get("startSubmitTime"));
		}
		
		//结束时间
		if(conditionMap.get("endSubmitTime")!=null&&!"".equals(conditionMap.get("endSubmitTime")))
		{
			conditionSql = conditionSql + " and taocan.create_time <="+genericDao.getTimeCondition(conditionMap.get("endSubmitTime"));
		}
		//创建人
		if(conditionMap.get("user")!=null&&!"".equals(conditionMap.get("user")))
		{
			conditionSql = conditionSql + " and lfuser.name like '%" + conditionMap.get("user") +"%'"; 
		}
		
		String orderby=" order by taocan.create_time desc";
		sql=sql+conditionSql+orderby;
		String countSql = "select count(*) totalcount from "+getTableSql()+conditionSql;

		List<DynaBean> returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return returnList;
	}
	
	/**
	 *  获得表名称
	 * @return
	 */
	public String getTableSql(){
		return " LF_BUS_TAOCAN  taocan left join LF_SYSUSER lfuser on lfuser.user_id=taocan.user_id left join lf_dep lfdep on lfdep.dep_id=lfuser.dep_id  left join lf_procharges charge on  taocan.taocan_code=charge.taocan_code" ; 
	}
	
	/***
	 * 获得套餐列表
	 * @return
	 */
	public List<DynaBean> getTCList(String name){
		List<DynaBean> list=null;
		if(name==null){
		 list=this.getListDynaBeanBySql("select package_id,package_code,package_name from	lf_bus_package  where lf_bus_package.package_state=0");
		}else {
			list=this.getListDynaBeanBySql("select package_id,package_code,package_name from	lf_bus_package  where lf_bus_package.package_state=0 and package_name like '%"+name+"%'");
		}
		
		return list;
	}
	/***
	 * 通过code查询关联关系，然后查询出已经绑定的套餐
	 * @param code
	 * @return
	 */
	public List<DynaBean> getListByCode(String code){
		String Sql=" select pack.package_id,pack.package_code,pack.package_name,pack.package_state from   lf_buspkgetaocan taocan left join lf_bus_package pack on taocan.package_code=pack.package_code  where  taocan.associate_type=0    and taocan.taocan_code='"+code+"'";
		return this.getListDynaBeanBySql(Sql);
	}
	
}