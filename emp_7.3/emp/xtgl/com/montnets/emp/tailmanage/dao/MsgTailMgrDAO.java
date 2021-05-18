package com.montnets.emp.tailmanage.dao;

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
import org.apache.commons.beanutils.DynaBean;

import java.util.LinkedHashMap;
import java.util.List;

public class MsgTailMgrDAO extends SuperDAO{
	
	/**
	 * 获得贴尾查询列表信息
	 * @param conditionMap
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public List<DynaBean> getTailRecord(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws NumberFormatException, Exception{
		
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		/*会出现重复记录 暂不使用 备份
		String sql ="select tail.TAIL_ID,tail.TAIL_NAME,tbind.TAIL_TYPE,tail.CONTENT,tbind.SPUSERID,tbind.BUS_CODE,tail.CREATE_TIME,lfuser.user_name,lfuser.name,lfdep.dep_name from "+getTableSql();
		*/
		String sql ="select distinct tail.TAIL_ID,tail.TAIL_NAME,tbind.TAIL_TYPE,tail.CONTENT,tail.CREATE_TIME,lfuser.user_name,lfuser.name,lfdep.dep_name from "+getTableSql();
		String conditionSql=" where tail.CORP_CODE='"+conditionMap.get("corpcode")+"'";
		
//		String userIdStr = "2";//conditionMap.get("userId");
//		//机构权限
//		StringBuffer domination = new StringBuffer("select ").append(
//				TableLfDomination.DEP_ID).append(" from ").append(
//				TableLfDomination.TABLE_NAME).append(" where ").append(
//				TableLfDomination.USER_ID).append("=").append(userIdStr);
//		String dominationSql = new StringBuffer(" (lfuser.").append(
//				TableLfSysuser.USER_ID).append("=").append(userIdStr).append(
//				" or lfuser.").append(TableLfSysuser.DEP_ID).append(" in (")
//				.append(domination).append("))").append(" and lfuser.").append(TableLfSysuser.USER_ID).append("<>1").toString();
//		conditionSql= conditionSql+dominationSql;
		
		//创建人
		if(conditionMap.get("userName")!=null&&!"".equals(conditionMap.get("userName")))
		{
			conditionSql = conditionSql + " and lfuser.name like '%" + conditionMap.get("userName") +"%'"; 
		}
		//贴尾名称
		if(conditionMap.get("tailname")!=null&&!"".equals(conditionMap.get("tailname")))
		{
			conditionSql = conditionSql + " and tail.TAIL_NAME like '%" + conditionMap.get("tailname") +"%'"; 
		}
		
		//机构
		String deptid = conditionMap.get("deptid");
//		//个人权限只能查自己
//		if(null!=conditionMap.get("permissionType") && "1".equals(conditionMap.get("permissionType"))){
//			if(null!=conditionMap.get("permissionUserName") && !"".equals(conditionMap.get("permissionUserName"))){
//				conditionSql += " and lfuser.user_name = '"+conditionMap.get("permissionUserName")+"' ";				
//			}
//		}else{//机构权限
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
		//}
		/*备份
		//根据业务名称查询 (去掉业务类型：手动)
		if(conditionMap.get("buss")!=null&&!"".equals(conditionMap.get("buss")))
		{
			conditionSql = conditionSql + " and tail.bustail_id in ( select lf_bus_tailtmp.smstail_id from lf_bus_tailtmp,lf_busmanager where (lf_busmanager.bus_type=1 or lf_busmanager.bus_type=2) and lf_bus_tailtmp.bus_id=lf_busmanager.bus_id  and lf_bus_tailtmp.associate_type=0 and lf_busmanager.bus_name like '%"+conditionMap.get("buss")+"%')";
		}
		*/
		if(conditionMap.get("startSubmitTime")!=null&&!"".equals(conditionMap.get("startSubmitTime")))
		{
			conditionSql = conditionSql + " and tail.create_time >="+genericDao.getTimeCondition(conditionMap.get("startSubmitTime"));
		}
		if(conditionMap.get("endSubmitTime")!=null&&!"".equals(conditionMap.get("endSubmitTime")))
		{
			conditionSql = conditionSql + " and tail.create_time <="+genericDao.getTimeCondition(conditionMap.get("endSubmitTime"));
		}
		/**EMP6.1 标准版 新需求*/
		//贴尾内容
		if(conditionMap.get("content")!=null&&!"".equals(conditionMap.get("content")))
		{
			conditionSql = conditionSql + " and tail.content like '%" + conditionMap.get("content") +"%'"; 
		}
		//贴尾类型
		if(conditionMap.get("tailtype")!=null&&!"".equals(conditionMap.get("tailtype")))
		{
			conditionSql = conditionSql + " and tbind.TAIL_TYPE in (" + conditionMap.get("tailtype") +")"; 
		}
		//SP账号
		if(conditionMap.get("spuserid")!=null&&!"".equals(conditionMap.get("spuserid")))
		{
			conditionSql = conditionSql + " and tbind.SPUSERID like '%" + conditionMap.get("spuserid") +"%'"; 
		}
		//业务编码
		if(conditionMap.get("buscode")!=null&&!"".equals(conditionMap.get("buscode")))
		{
			conditionSql = conditionSql + " and tbind.BUS_CODE like '%" + conditionMap.get("buscode") +"%'"; 
		}
		/**end*/
		String orderby=" order by tail.create_time desc";
		sql=sql+conditionSql+orderby;
		//分页时没有过滤掉重复的，分页数据与实际条数不一致，备份
		//String countSql = "select count(*) totalcount from "+getTableSql()+conditionSql;
		String countSql = "select count(distinct tail.TAIL_ID) totalcount from "+getTableSql()+conditionSql;

		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);

	}
	 /**  备份
	  * 根据ID查询业务集合(去掉业务类型：手动)
	  * @param id sql是数字类型，查询时候不要加单引号(DB兼容)
	  * @return
	  
	public List<DynaBean>  busList(String id){
		List<DynaBean> list=this.getListDynaBeanBySql("select  LF_BUSMANAGER.BUS_NAME,LF_BUSMANAGER.BUS_CODE,LF_BUSMANAGER.BUS_ID, LF_BUSMANAGER.STATE from	 LF_BUS_TAILTMP,LF_BUSMANAGER where  (LF_BUSMANAGER.BUS_TYPE=1 or LF_BUSMANAGER.BUS_TYPE=2 ) and LF_BUSMANAGER.BUS_ID=LF_BUS_TAILTMP .BUS_ID and LF_BUS_TAILTMP.SMSTAIL_ID="+id+"");
		
		
		return list;
	}
	*/
	/** 
	  * 根据ID查询业务集合(去掉业务类型：手动)
	  * @param id sql是数字类型，查询时候不要加单引号(DB兼容)
	  * @return
	  */
	public List<DynaBean>  busList(String id){
		//下面这个是不包括手动的
		//String dbSql = "SELECT LF_BUSMANAGER.BUS_NAME, LF_BUSMANAGER.BUS_CODE, LF_BUSMANAGER.BUS_ID, LF_BUSMANAGER.STATE FROM GW_TAILBIND, LF_BUSMANAGER WHERE ( LF_BUSMANAGER.BUS_TYPE = 1 OR LF_BUSMANAGER.BUS_TYPE = 2 ) AND LF_BUSMANAGER.BUS_CODE = GW_TAILBIND.BUS_CODE AND GW_TAILBIND.TAIL_ID = "+id+"";
		String dbSql = "SELECT LF_BUSMANAGER.BUS_NAME, LF_BUSMANAGER.BUS_CODE, LF_BUSMANAGER.BUS_ID, LF_BUSMANAGER.STATE FROM GW_TAILBIND, LF_BUSMANAGER WHERE LF_BUSMANAGER.BUS_CODE = GW_TAILBIND.BUS_CODE AND GW_TAILBIND.TAIL_ID = "+id+"";
		List<DynaBean> list=this.getListDynaBeanBySql(dbSql);
		//System.out.println(dbSql);
		
		
		return list;
	}
	
	/** 
	  * 根据ID查询SP账号集合
	  * @param id sql是数字类型，查询时候不要加单引号(DB兼容)
	  * @return
	  */
	public List<DynaBean> spList(String id) {
		//在oracle中查询时 下面这行代码加上uid的话 会报错 备份
		//String dbSql = "SELECT USERDATA.UID, USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME FROM GW_TAILBIND, USERDATA WHERE USERDATA.USERID = GW_TAILBIND.SPUSERID AND GW_TAILBIND.TAIL_ID = "+id+"";
		String dbSql = "SELECT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME FROM GW_TAILBIND, USERDATA WHERE USERDATA.USERID = GW_TAILBIND.SPUSERID AND GW_TAILBIND.TAIL_ID = "+id+"";
		List<DynaBean> list=this.getListDynaBeanBySql(dbSql);
		
		
		return list;
	}
	
	
	
	/**
	 *  获得表名称
	 * @return
	 */
	public String getTableSql(){
		return " GW_MSGTAIL tail left join LF_SYSUSER lfuser on lfuser.user_id=tail.user_id left join lf_dep lfdep on lfdep.dep_id=lfuser.dep_id LEFT JOIN GW_TAILBIND tbind ON tbind.TAIL_ID = tail.TAIL_ID " ; 
	}
	
	/**   备份
	 *获得业务信息 (显示所有已启用的业务,去掉业务类型：手动)
	 * @return 业务信息集合
	 
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
	*/
	
	/**EMP6.1 标准版 新需求 pengj*/
	/**
	 *获得业务信息 (显示所有已启用的业务,去掉业务类型：手动)
	 * @return 业务信息集合
	 
	//备份
	public List<DynaBean> getBusiness(String name){
		List<DynaBean> list=null;
		//SQLSERVER 排序时候需要单独处理 数据库兼容
		if(name==null||"".equals(name)){
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_TAILBIND.BUS_CODE AS tablelink, GW_MSGTAIL.TAIL_NAME, lf_busmanager.state FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE ( lf_busmanager.bus_type = 1 OR lf_busmanager.bus_type = 2 ) AND lf_busmanager.state = 0 ORDER BY tablelink ");
			}else{
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_TAILBIND.BUS_CODE AS tablelink, GW_MSGTAIL.TAIL_NAME, lf_busmanager.state FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE ( lf_busmanager.bus_type = 1 OR lf_busmanager.bus_type = 2 ) AND lf_busmanager.state = 0 ORDER BY tablelink DESC ");
			}
			
		}else{
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
				
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_MSGTAIL.TAIL_NAME, GW_TAILBIND.BUS_CODE AS tablelink FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE ( lf_busmanager.bus_type = 1 OR lf_busmanager.bus_type = 2 ) AND lf_busmanager.state = 0 AND lf_busmanager.bus_name LIKE '%"+name.trim()+"%' ORDER BY tablelink");
			}else{
				
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_MSGTAIL.TAIL_NAME, GW_TAILBIND.BUS_CODE AS tablelink FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE ( lf_busmanager.bus_type = 1 OR lf_busmanager.bus_type = 2 ) AND lf_busmanager.state = 0 AND lf_busmanager.bus_name LIKE '%"+name.trim()+"%' ORDER BY tablelink DESC");
			}
		}
		return list;
	}
	
	*/
	/**
	 *获得业务信息 (显示所有已启用的业务)
	 * @return 业务信息集合
	 
	*/
	public List<DynaBean> getBusiness(String name){
		List<DynaBean> list=null;
		//SQLSERVER 排序时候需要单独处理 数据库兼容
		if(name==null||"".equals(name)){
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_TAILBIND.BUS_CODE AS tablelink, GW_MSGTAIL.TAIL_NAME, lf_busmanager.state FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE lf_busmanager.state = 0 ORDER BY tablelink ");
			}else{
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_TAILBIND.BUS_CODE AS tablelink, GW_MSGTAIL.TAIL_NAME, lf_busmanager.state FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE lf_busmanager.state = 0 ORDER BY tablelink DESC ");
			}
			
		}else{
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
				
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_MSGTAIL.TAIL_NAME, GW_TAILBIND.BUS_CODE AS tablelink FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE lf_busmanager.state = 0 AND lf_busmanager.bus_name LIKE '%"+name.trim()+"%' ORDER BY tablelink");
			}else{
				
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_MSGTAIL.TAIL_NAME, GW_TAILBIND.BUS_CODE AS tablelink FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE lf_busmanager.state = 0 AND lf_busmanager.bus_name LIKE '%"+name.trim()+"%' ORDER BY tablelink DESC");
			}
		}
		return list;
	}
	
	
	
	/**
	 *获得业务信息 (显示所有已启用的业务)
	 * @return 业务信息集合
	 
	*/
	public List<DynaBean> getBusinessV1(String name,String corpcode){
		List<DynaBean> list=null;
		//SQLSERVER 排序时候需要单独处理 数据库兼容
		if(name==null||"".equals(name)){
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_TAILBIND.BUS_CODE AS tablelink, GW_MSGTAIL.TAIL_NAME, lf_busmanager.state FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE lf_busmanager.state = 0  AND lf_busmanager.CORP_CODE IN (0,"+corpcode+") ORDER BY tablelink ");
			}else{
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_TAILBIND.BUS_CODE AS tablelink, GW_MSGTAIL.TAIL_NAME, lf_busmanager.state FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE lf_busmanager.state = 0  AND lf_busmanager.CORP_CODE IN (0,"+corpcode+") ORDER BY tablelink DESC ");
			}
			
		}else{
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
				
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_MSGTAIL.TAIL_NAME, GW_TAILBIND.BUS_CODE AS tablelink FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE lf_busmanager.state = 0 AND lf_busmanager.bus_name LIKE '%"+name.trim()+"%' AND lf_busmanager.CORP_CODE IN (0,"+corpcode+")  ORDER BY tablelink");
			}else{
				
				list=this.getListDynaBeanBySql("SELECT DISTINCT lf_busmanager.bus_code, lf_busmanager.bus_name, lf_busmanager.bus_id, GW_MSGTAIL.TAIL_NAME, GW_TAILBIND.BUS_CODE AS tablelink FROM lf_busmanager LEFT JOIN GW_TAILBIND ON GW_TAILBIND.BUS_CODE = lf_busmanager.BUS_CODE LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE lf_busmanager.state = 0 AND lf_busmanager.bus_name LIKE '%"+name.trim()+"%'  AND lf_busmanager.CORP_CODE IN (0,"+corpcode+") ORDER BY tablelink DESC");
			}
		}
		return list;
	}
	
	
	/**
	 *获得SP账号信息
	 * @return SP账号信息集合
	 
	public List<DynaBean> getSp(String name){
		List<DynaBean> list=null;
		//SQLSERVER 排序时候需要单独处理 数据库兼容
		if(name==null||"".equals(name)){
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM USERDATA LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID ORDER BY tablelink ");
			}else{
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM USERDATA LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID ORDER BY tablelink DESC ");
			}
			
		}else{
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
				
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM USERDATA LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE USERDATA.STAFFNAME LIKE '%"+name.trim()+"%' ORDER BY tablelink");
			}else{
				
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM USERDATA LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE USERDATA.STAFFNAME LIKE '%"+name.trim()+"%' ORDER BY tablelink DESC");
			}
		}
		return list;
	}
	*/
	
	/**
	 *获得SP账号信息
	 * @return SP账号信息集合 只包含短信SP 和 前端账号
	 */
	public List<DynaBean> getSp(String name){
		List<DynaBean> list=null;
		//SQLSERVER 排序时候需要单独处理 数据库兼容
		if(name==null||"".equals(name)){
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM USERDATA LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE USERDATA.ACCOUNTTYPE=1 and USERDATA.\"UID\">100001 and USERDATA.USERTYPE=0 ORDER BY tablelink ");
			}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){  //出现了mysql兼容问题，所以这里新增
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM USERDATA LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE USERDATA.ACCOUNTTYPE=1 and USERDATA.UID>100001 and USERDATA.USERTYPE=0 ORDER BY tablelink DESC ");
			}else{
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM USERDATA LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE USERDATA.ACCOUNTTYPE=1 and USERDATA.\"UID\">100001 and USERDATA.USERTYPE=0 ORDER BY tablelink DESC ");
			}
			
		}else{
			if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
				
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM USERDATA LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE USERDATA.STAFFNAME LIKE '%"+name.trim()+"%' AND USERDATA.ACCOUNTTYPE=1 and USERDATA.\"UID\">100001 and USERDATA.USERTYPE=0 ORDER BY tablelink");
			}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){  //出现了mysql兼容问题，所以这里新增
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM USERDATA LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE USERDATA.STAFFNAME LIKE '%"+name.trim()+"%' AND USERDATA.ACCOUNTTYPE=1 and USERDATA.UID>100001 and USERDATA.USERTYPE=0 ORDER BY tablelink DESC");
			}else{
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM USERDATA LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE USERDATA.STAFFNAME LIKE '%"+name.trim()+"%' AND USERDATA.ACCOUNTTYPE=1 and USERDATA.\"UID\">100001 and USERDATA.USERTYPE=0 ORDER BY tablelink DESC");
			}
		}
		return list;
	}
	
	
	/**
	 *获得SP账号信息
	 * @return SP账号信息集合 只包含短信SP 和 前端账号
	 */
	public List<DynaBean> getSpV1(String name,String corpcode){
		List<DynaBean> list=null;
		String uid = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? "UID" : "\"UID\"";
		//SQLSERVER 排序时候需要单独处理 数据库兼容
		if(name==null||"".equals(name)){
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM (SELECT SPDB.SPUSER USERID,UD.STAFFNAME,UD.LOGINID FROM " +
						"(SELECT SPUSER FROM LF_SP_DEP_BIND WHERE CORP_CODE='"+corpcode+"') SPDB LEFT JOIN (SELECT STAFFNAME,USERID,LOGINID FROM USERDATA WHERE ACCOUNTTYPE=1	AND "+ uid +">100001 " +
						"AND USERTYPE=0) UD ON SPDB.SPUSER=UD.USERID) USERDATA  LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = " +
						"USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID ORDER BY tablelink ");
		}else{
				list=this.getListDynaBeanBySql("SELECT DISTINCT USERDATA.USERID, USERDATA.LOGINID, USERDATA.STAFFNAME, GW_TAILBIND.SPUSERID AS tablelink, GW_MSGTAIL.TAIL_NAME FROM " +
						"(SELECT SPDB.SPUSER USERID,UD.STAFFNAME,UD.LOGINID FROM (SELECT SPUSER FROM LF_SP_DEP_BIND WHERE CORP_CODE='"+corpcode+"') SPDB LEFT JOIN (SELECT STAFFNAME,USERID,LOGINID FROM USERDATA WHERE ACCOUNTTYPE=1 AND " + uid + ">100001 " +
								"AND USERTYPE=0) UD ON SPDB.SPUSER=UD.USERID) USERDATA LEFT JOIN GW_TAILBIND ON GW_TAILBIND.SPUSERID = USERDATA.USERID LEFT JOIN GW_MSGTAIL ON GW_TAILBIND.TAIL_ID = GW_MSGTAIL.TAIL_ID WHERE  USERDATA.STAFFNAME LIKE '%"+name.trim()+"%' ORDER BY tablelink");
		}
		return list;
	}
	
	
	/**end*/
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
