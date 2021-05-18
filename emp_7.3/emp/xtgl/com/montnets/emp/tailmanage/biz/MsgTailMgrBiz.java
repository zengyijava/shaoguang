package com.montnets.emp.tailmanage.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailmanage.GwMsgtail;
import com.montnets.emp.entity.tailmanage.GwTailbind;
import com.montnets.emp.tailmanage.dao.MsgTailMgrDAO;
import com.montnets.emp.util.PageInfo;


public class MsgTailMgrBiz extends SuperBiz{
	
	
	MsgTailMgrDAO dao=new MsgTailMgrDAO();
	  IEmpTransactionDAO empTransactionDAO=new DataAccessDriver().getEmpTransDAO();
	/**
	 * 获得贴尾查询列表信息
	 * @param conditionMap
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public List<DynaBean> getTailRecord(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws NumberFormatException, Exception{
		
		return dao.getTailRecord(conditionMap,pageInfo);
		
	}
	 /**
	  * 根据ID查询业务集合
	  * @param id
	  * @return
	  */
	public LinkedHashMap<String, String>  busList(String id){
		LinkedHashMap<String, String> 		all=new LinkedHashMap<String, String> ();
		List<DynaBean> list = dao.busList(id);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				DynaBean db=list.get(i);
				String bus_name=db.get("bus_name").toString();
				String bus_code=db.get("bus_code").toString();
				all.put(bus_code, bus_name);
			}
		}
		return all;
	}
	
	 /**
	  * 根据ID查询SP账号集合
	  * @param id
	  * @return
	  */
	public LinkedHashMap<String, String> spList(String id) {
		LinkedHashMap<String, String> 		all=new LinkedHashMap<String, String> ();
		List<DynaBean> list = dao.spList(id);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				DynaBean db=list.get(i);
				String userid=db.get("userid").toString();
				String staffname=db.get("staffname").toString();
				all.put(userid, staffname);
			}
		}
		return all;
	}
	
	
	 /**
	  * 根据ID查询业务集合(是否显示禁用)
	  * @param id
	  * @return
	  */
	public String  busStr(String id){
		StringBuffer sb=new StringBuffer("[");
		List<DynaBean> list = dao.busList(id);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				DynaBean db=list.get(i);
				String bus_code=db.get("bus_code").toString();
				String bus_name=db.get("bus_name").toString();
				String bus_id=db.get("bus_id").toString();
				String state=db.get("state").toString();
				sb.append("{bus_code:'"+bus_code+"',bus_name:'"+bus_name+"',bus_id:'"+bus_id+"',state:'"+state+"'}");
				if(i !=list.size()-1){
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	 /**
	  * 根据ID查询SP账号集合  用于修改的 
	  * @param id
	  * @return
	  */
	public String spStr(String id) {
		StringBuffer sb=new StringBuffer("[");
		List<DynaBean> list = dao.spList(id);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				DynaBean db=list.get(i);
				String userid=db.get("userid").toString();
				String staffname=db.get("staffname").toString();
				
				sb.append("{userid:'"+userid+"',staffname:'"+staffname+"'}");
				if(i !=list.size()-1){
					sb.append(",");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	/**
	 *获得业务信息 
	 * @return 业务信息集合
	 */
	public List<DynaBean> getBusiness(String name){
		
			return dao.getBusiness(name);

	}
	
	/**
	 *获得业务信息 
	 * @return 业务信息集合
	 */
	public List<DynaBean> getBusinessV1(String name,String corpcode,Integer corptype){
		
		//多企业
		if(corptype==1){
			return dao.getBusinessV1(name,corpcode);
		}else{
			return dao.getBusiness(name);
		}

	}
	
	/**
	 *  删除主表而且删除关联关系表
	 * @param tailid
	 * @return
	 */
	public String deleteSingle(String tailid){
		  String ret="false";
			Connection conn =null;
			conn= empTransactionDAO.getConnection();
			int size =0;
			try {
				empTransactionDAO.beginTransaction(conn);
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("tailid", tailid);
					 empTransactionDAO.delete(conn, GwMsgtail.class, tailid);
					 //删除关联关系表
					 size=empTransactionDAO.delete(conn, GwTailbind.class, conditionMap);
				empTransactionDAO.commitTransaction(conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"根据ID删除单条记录异常!");
				empTransactionDAO.rollBackTransaction(conn);
				return ret;
			}finally{
				empTransactionDAO.closeConnection(conn);
				if(size>0){
					ret="success";
				}
			}
			return ret;
	}
	
	
	
	/**
	 * 根据ID删除多条记录
	 * @param ids
	 * @return
	 */
	  public String deleteSelect(String ids){

		  String ret="false";
			Connection conn =null;
			conn= empTransactionDAO.getConnection();
			String[] idarr=ids.split(",");
			int size =0;
			try {
				empTransactionDAO.beginTransaction(conn);
				for(int i=0;i<idarr.length;i++){
					if("".equals(idarr[i])){
						continue;
					}
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("tailid", idarr[i]);
					 empTransactionDAO.delete(conn, GwMsgtail.class, conditionMap);
					 LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
					 condition.put("tailid", idarr[i]);
					 //删除关联关系表
					 size=empTransactionDAO.delete(conn, GwTailbind.class, condition);
				}

				empTransactionDAO.commitTransaction(conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"根据ID删除多条记录异常!");
				empTransactionDAO.rollBackTransaction(conn);
				return ret;
			}finally{
				empTransactionDAO.closeConnection(conn);
				if(size>0){
					ret="success";
				}
			}
			return ret;
	  }
	  
	  /**
	   * 保存短息贴尾信息
	   * @param tail
	 * @param tailtype 
	   * @param bussCodes
	   * @return
	   */
	public boolean save(GwMsgtail tail,String bussOrSpsIds, int tailtype){
		boolean add=false;
		int  dep_id=0;
		try {
			LfSysuser user=new BaseBiz().getLfSysuserByUserId(tail.getUserid()+"") ;
			if(user!=null){
				dep_id=user.getDepId().intValue();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取用户信息异常。");
		}
		String[] ids=bussOrSpsIds.split(",");
		Connection conn =null;
		conn= empTransactionDAO.getConnection();
		try {
			empTransactionDAO.beginTransaction(conn);
			//tail.setDep_id(dep_id);
			Long tailid=empTransactionDAO.saveObjectReturnID(conn, tail);
			for(int i=0;i<ids.length;i++){
				if(!"".equals(ids[i])){
					GwTailbind tailbind =new GwTailbind();
					tailbind.setTailid(tailid);
					if(tailtype == 2){
						tailbind.setSpuserid(ids[i]);
						tailbind.setBuscode(" ");
					}
					if(tailtype == 1){
						tailbind.setSpuserid(" ");
						tailbind.setBuscode(ids[i]);
					}
					tailbind.setTailtype(tailtype);
					tailbind.setCreatetime(new Timestamp(System.currentTimeMillis()));
					tailbind.setUpdatetime(new Timestamp(System.currentTimeMillis()));
					tailbind.setCorpcode(tail.getCorpcode());
					tailbind.setUserid(tail.getUserid());
					add=empTransactionDAO.save(conn, tailbind);
				}
			}
			empTransactionDAO.commitTransaction(conn);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"保存短息贴尾信息异常!");
			empTransactionDAO.rollBackTransaction(conn);
			return false;
		}finally{
			empTransactionDAO.closeConnection(conn);
			
		}	
		return add;
	}
	
	/**
	 * 采用先删除后增加的处理方式
	 * @param tail
	 * @param tailtype 
	 * @param bussids
	 * @return
	 */
	public boolean update(GwMsgtail tail,String bussOrSpsIds,String modfid, int tailtype){
		boolean add=false;
		int  dep_id=0;
		try {
			LfSysuser user=new BaseBiz().getLfSysuserByUserId(tail.getUserid()+"") ;
			if(user!=null){
				dep_id=user.getDepId().intValue();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取用户信息异常。");
		}
		String[] ids=bussOrSpsIds.split(",");
		Connection conn =null;
		conn= empTransactionDAO.getConnection();
		try {
			empTransactionDAO.beginTransaction(conn);
			//修改业务贴尾
			empTransactionDAO.update(conn, tail);
			//先删除
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("tailid", modfid);
			empTransactionDAO.delete(conn, GwTailbind.class, conditionMap);
			 
			//tail.setDep_id(dep_id);
			for(int i=0;i<ids.length;i++){
				if(!"".equals(ids[i])){
					GwTailbind tailbind =new GwTailbind();
					tailbind.setCorpcode(tail.getCorpcode());
					tailbind.setCreatetime(new Timestamp(System.currentTimeMillis()));
					tailbind.setUpdatetime(new Timestamp(System.currentTimeMillis()));
					if(tailtype == 2){
						tailbind.setSpuserid(ids[i]);
						tailbind.setBuscode(" ");
					}
					if(tailtype == 1)
					{
						tailbind.setSpuserid(" ");
						tailbind.setBuscode(ids[i]);
					}
					tailbind.setTailtype(tailtype);
					tailbind.setTailid(Long.parseLong(modfid));
					tailbind.setUserid(tail.getUserid());
					
					add=empTransactionDAO.save(conn, tailbind);
				}
			}
			empTransactionDAO.commitTransaction(conn);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"保存短息贴尾信息异常!");
			empTransactionDAO.rollBackTransaction(conn);
			return false;
		}finally{
			empTransactionDAO.closeConnection(conn);
			
		}	
		return add;
	}
	
	
	/**
	 * 获取部门操作员
	 * @param userId 用户id
	 * @param depId 部门id
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getAllSysusersOfSmsTaskRecordByDep(Long userId,Long depId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			if (null != userId)
				lfSysuserList = dao.findDomUserBySysuserIDOfSmsTaskRecordByDep(userId
						.toString(),depId.toString());

		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取部门操作员异常。");
			throw e;
		}
		return lfSysuserList;
	}
	
	/**
	 * 机构树
	 * @return
	 */
	public String getDepartmentJosnData2(Long depId,Long userId,String corpCode){
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			sysuser = empDao.findObjectByID(LfSysuser.class, userId);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发历史或群发任务查询操作员信息异常！");
			tree = new StringBuffer("[]");
		}
//		if(sysuser.getPermissionType()==1)
//		{
//			tree = new StringBuffer("[]");
//		}else
//		{
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			
			try {
				lfDeps = null;
				
				 if ((sysuser != null) && (sysuser.getCorpCode().equals("100000")))
			        {
			          if (depId == null)
			          {
			            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			            LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();

			            conditionMap.put("superiorId", "0");

			            conditionMap.put("depState", "1");

			            orderbyMap.put("depId", "ASC");
			            orderbyMap.put("deppath", "ASC");
			            lfDeps = new BaseBiz().getByCondition(LfDep.class, conditionMap, orderbyMap);
			          }
			          else
			          {
			        	lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			          }

			        }
			        else if (depId == null) {
			          lfDeps = new ArrayList();
			          //LfDep lfDep = (LfDep)depBiz.getAllDeps(userId).get(0);
			          //LfDep lfDep = (LfDep)depBiz.getAllDepByUserIdAndCorpCode(userId,corpCode).get(0);
			          LfDep lfDep = (LfDep)getAllDepByCorpCode(corpCode).get(0);
			          lfDeps.add(lfDep);
			        }
			        else {
			        	//lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			        	lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,corpCode);
			        }
				
				
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
		
			} catch (Exception e) {
				EmpExecutionContext.error(e,"群发历史或群发任务操作员树加载biz层异常！");
				tree = new StringBuffer("[]");
			}
//		}
		return tree.toString();
	}
	
	
	/**
	 * 根据企业编码获取所有机构
	 * @description    
	 * @param corpCode
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-3-4 下午05:02:42
	 */
	public List<LfDep> getAllDepByCorpCode(String corpCode) throws Exception
	{
		List<LfDep> depList = new ArrayList<LfDep>();
		try
		{
			LinkedHashMap<String,String> condition = new LinkedHashMap<String,String>(); 
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>(); 
			condition.put("depState", "1");
			condition.put("corpCode", corpCode);
			orderbyMap.put("depId","asc");
			 
			depList = this.empDao.findListByCondition(LfDep.class, condition, orderbyMap);

		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过企业编码获取其所以管辖机构的机构对象异常。");
		}
		return depList;
	}
	
	
	
	/**
	 * 机构树
	 * @return
	 */
	public String getDepartmentJosnData2(Long depId,Long userId){
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			sysuser = empDao.findObjectByID(LfSysuser.class, userId);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发历史或群发任务查询操作员信息异常！");
			tree = new StringBuffer("[]");
		}
		if(sysuser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			
			try {
				lfDeps = null;
				
				 if ((sysuser != null) && (sysuser.getCorpCode().equals("100000")))
			        {
			          if (depId == null)
			          {
			            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			            LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();

			            conditionMap.put("superiorId", "0");

			            conditionMap.put("depState", "1");

			            orderbyMap.put("depId", "ASC");
			            orderbyMap.put("deppath", "ASC");
			            lfDeps = new BaseBiz().getByCondition(LfDep.class, conditionMap, orderbyMap);
			          }
			          else
			          {
			        	lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			          }

			        }
			        else if (depId == null) {
			          lfDeps = new ArrayList();
			          LfDep lfDep = (LfDep)depBiz.getAllDeps(userId).get(0);
			          lfDeps.add(lfDep);
			        }
			        else {
			        	lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			        }
				
				
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
		
			} catch (Exception e) {
				EmpExecutionContext.error(e,"群发历史或群发任务操作员树加载biz层异常！");
				tree = new StringBuffer("[]");
			}
		}
		return tree.toString();
	}
	
	/**
	 *获得sp信息 
	 * @return 业务信息集合
	 */
	public List<DynaBean> getSp(String name){
		
			return dao.getSp(name);

	}
	
	/**
	 *获得sp信息 
	 * @return 业务信息集合
	 */
	public List<DynaBean> getSpV1(String name,String corpcode,Integer corptype){
		if(corptype==1){
			return dao.getSpV1(name,corpcode);
		}else{
			return dao.getSp(name);
		}

	}
	
	
	

}
