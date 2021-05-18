package com.montnets.emp.ywgl.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @todo TODO
 * @project	emp
 * @author WANGRUBIN
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-13 下午03:16:17
 * @description
 */
public class YwglDao extends SuperDAO{
	
	IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();

	/**
	 * 查所所有启用的业务列表
	 * @author WANGRUBIN
	 * @datatime 2015-1-13 下午03:16:28
	 * @description TODO 
	 * @param searStr 查询条件
	 * @return
	 */
	public List<DynaBean> getBusList(String searStr){
		
		List<DynaBean> list=null;
		
		if(searStr==null || "".equals(searStr)){
			list=this.getListDynaBeanBySql("select * from lf_busmanager where state = 0");
		}else{
			list=this.getListDynaBeanBySql("select * from lf_busmanager where  state = 0 and bus_name like '%"+searStr.trim()+"%' or bus_code like '%"+searStr.trim()+"%' ");
		}
		return list;
	}
	
	/**
	 * 业务查询
	 * @author WANGRUBIN
	 * @datatime 2015-1-15 上午11:56:04
	 * @description TODO 
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public List<DynaBean> getPkgList(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws NumberFormatException, Exception{
		
		List<DynaBean> list=null;
		String conditionSql = "";//" and c.bus_code like '%CSY%'";
		
		if(null!=conditionMap.get("packageName") && !"".equals(conditionMap.get("packageName"))){
			conditionSql += " and pkg.package_name like '%"+conditionMap.get("packageName").trim()+"%' ";
		}
		if(null!=conditionMap.get("packageCode") && !"".equals(conditionMap.get("packageCode"))){
			conditionSql += " and pkg.package_code like '%"+conditionMap.get("packageCode").trim()+"%' ";
		}
		if(null!=conditionMap.get("packageState") && !"".equals(conditionMap.get("packageState"))){
			conditionSql += " and pkg.package_state = "+conditionMap.get("packageState");
		}
		if(null!=conditionMap.get("busName") && !"".equals(conditionMap.get("busName"))){
			conditionSql += " and c.bus_name like '%"+conditionMap.get("busName").trim()+"%' ";
		}
		if(null!=conditionMap.get("busCode") && !"".equals(conditionMap.get("busCode"))){
			conditionSql += " and c.bus_code like '%"+conditionMap.get("busCode").trim()+"%' ";
		}
		if(null!=conditionMap.get("userName") && !"".equals(conditionMap.get("userName"))){
			conditionSql += " and u.name like '%"+conditionMap.get("userName").trim()+"%' ";
		}
		String deptid = conditionMap.get("deptString");
		//个人权限只能查自己
		if(null!=conditionMap.get("permissionType") && "1".equals(conditionMap.get("permissionType"))){
			if(null!=conditionMap.get("permissionUserName") && !"".equals(conditionMap.get("permissionUserName"))){
				conditionSql += " and u.user_name = '"+conditionMap.get("permissionUserName")+"' ";				
			}
		}else{//机构权限
			if(null!=deptid && !"".equals(deptid)){
				//包含子机构
				if("1".equals(conditionMap.get("isContainsSun"))){
				//	LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(deptid));
					String depid=new DepDAO().getChildUserDepByParentID(Long.parseLong(deptid),TableLfDep.DEP_ID);
					conditionSql += " and pkg."+depid;
				}else{
					 //不包含子机构
					conditionSql += " and pkg.dep_id = "+conditionMap.get("deptString")+" ";
				}			
			}else{
				if(null!=conditionMap.get("opDepId") && !"".equals(conditionMap.get("opDepId"))){
					String opDepId = conditionMap.get("opDepId");
					String opdepid=new DepDAO().getChildUserDepByParentID(Long.parseLong(opDepId),TableLfDep.DEP_ID);
					conditionSql += " and pkg."+opdepid;
				}
			}
		}

		if(null!=conditionMap.get("begintime") && !"".equals(conditionMap.get("begintime"))){
			conditionSql += " and pkg.create_time >= "+genericDao.getTimeCondition(conditionMap.get("begintime"));
		}
		if(null!=conditionMap.get("endtime") && !"".equals(conditionMap.get("endtime"))){
			conditionSql += " and pkg.create_time <= "+genericDao.getTimeCondition(conditionMap.get("endtime")+"");
		}
		
		//String sql = "select pkg.package_id,pkg.package_code,pkg.package_name,pkg.package_state,pkg.package_des,pkg.corp_code,pkg.create_time,pkg.update_time,pkg.dep_id,pkg.user_id ,u.user_name,u.name,d.dep_name from lf_bus_package pkg left join lf_sysuser u on pkg.user_id=u.user_id left join lf_dep d on pkg.dep_id = d.dep_id where pkg.package_code in (select package_code  from  lf_buspkgetaocan b  inner join  lf_busmanager c on b.bus_code=c.bus_code  where b.associate_type=1 and  b.package_code = pkg.package_code " + conditionSql + ") order by pkg.package_id desc";
		//2015-2-9 update by dingzx
		String sql = "select pkg.package_id,pkg.package_code,pkg.package_name,pkg.package_state,pkg.package_des,pkg.corp_code,pkg.create_time,pkg.update_time,pkg.dep_id,pkg.user_id ,u.user_name,u.name,d.dep_name from lf_bus_package pkg left join lf_sysuser u on pkg.user_id=u.user_id left join lf_dep d on pkg.dep_id = d.dep_id where pkg.package_code in (select package_code  from  lf_buspkgetaocan b  inner join  lf_busmanager c on b.bus_code=c.bus_code  where b.associate_type=1 and  b.package_code = pkg.package_code "+conditionSql+") order by pkg.package_id desc";
		
		String countSql = "select count(*) totalcount from lf_bus_package pkg left join lf_sysuser u on pkg.user_id=u.user_id left join lf_dep d on pkg.dep_id = d.dep_id  where  pkg.package_code in (select package_code  from  lf_buspkgetaocan b  inner join  lf_busmanager c on b.bus_code=c.bus_code  where b.associate_type=1 and b.package_code = pkg.package_code " + conditionSql + ")";
		
		list = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		
		return list;
	}

	/**
	 * 业务信息查询
	 * @author WANGRUBIN
	 * @datatime 2015-1-15 上午11:56:08
	 * @description TODO 
	 * @param packCode 业务包编码
	 * @return
	 */
	public List<DynaBean> getBusInfoList(String packCode){
		
		List<DynaBean> list=null;
		
		String sql = "select c.bus_name,c.state,b.bus_code,b.package_code  from  lf_buspkgetaocan b  inner join  lf_busmanager c on b.bus_code=c.bus_code  where  b.associate_type=1 and  b.package_code in ("+packCode+")";
		
		list=this.getListDynaBeanBySql(sql);
		
		return list;
	}

}
