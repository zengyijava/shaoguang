package com.montnets.emp.apimanage.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.apimanage.dao.wg_apiBaseDAO;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.servmodule.txgl.entity.GwBaseprotocol;
import com.montnets.emp.servmodule.txgl.entity.GwMultiEnterp;
import com.montnets.emp.servmodule.txgl.entity.GwProtomtch;
import com.montnets.emp.servmodule.txgl.entity.GwPushprotomtch;
import com.montnets.emp.util.PageInfo;

public class wg_apiBaseBiz extends SuperBiz{

	wg_apiBaseDAO dao=new wg_apiBaseDAO();
	private SuperDAO superDao = new SuperDAO();
	private IEmpDAO genericBaseDAO= new DataAccessDriver().getEmpDAO();

	
	/**
	 * 个性化接口企业信息
	 */
	public List<DynaBean> getApiList(LinkedHashMap<String, String> conditionMap,PageInfo pageinfo) throws Exception
	{
		
		return dao.getApiList(conditionMap, pageinfo);
	}
	
	/**
	 * 保存企业基本协议
	 * @param gwlist
	 * @return
	 */
	public int add(List<GwBaseprotocol> gwlist){
		Integer result = 0;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			//更新企业表
			result=empTransDao.save(conn, gwlist, GwBaseprotocol.class);
			if(result > 0)
			{
				empTransDao.commitTransaction(conn);
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存企业基本协议异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	
	/**
	 * 获得函数数量
	 */
	public List<GwBaseprotocol> getfunNum(String ecid,String funtype,String type) throws Exception{
		 String sql = "";
			if(type!=null&&!"".equals(type)){
				 sql = "select * from GW_BASEPROTOCOL where ecid='"+ecid+"' and funtype='"+funtype+"' and cmdtype='"+type+"'";
			}else{
				sql = "select * from GW_BASEPROTOCOL where ecid='"+ecid+"' and funtype='"+funtype+"'";
			}
		  List<GwBaseprotocol> list  = superDao.findEntityListBySQL(GwBaseprotocol.class, sql, StaticValue.EMP_POOLNAME);
		  return list;
	}
	//获得企业相关信息
	public GwMultiEnterp getEnterp(String funtype) throws Exception{
		GwMultiEnterp ge=new GwMultiEnterp();
		 String sql = "select * from GW_MULTI_ENTERP where funtype='"+funtype+"'";
		  List<GwMultiEnterp> list  = superDao.findEntityListBySQL(GwMultiEnterp.class, sql, StaticValue.EMP_POOLNAME);
		  if(list!=null&&list.size()>0){
			  ge=list.get(0);
		  }
		  return ge;
	}
	/**
	 * 修改状态
	 * @param map
	 * @param conmap
	 * @return
	 */
	public boolean setStatus(LinkedHashMap<String, String> map,LinkedHashMap<String, String> conmap){
		boolean result = false;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);

			result=empTransDao.update(conn, GwMultiEnterp.class, map, conmap);
			if(result)
			{
				empTransDao.commitTransaction(conn);
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存企业基本协议异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 修改状态
	 * @param map
	 * @param conmap
	 * @return
	 */
	public boolean changeStaute(LinkedHashMap<String, String> map,LinkedHashMap<String, String> conmap){
		boolean result = false;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);

			result=empTransDao.update(conn, GwBaseprotocol.class, map, conmap);
			if(result)
			{
				empTransDao.commitTransaction(conn);
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存企业基本协议异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	
	/**
	 * 修改状态
	 * @param map
	 * @param conmap
	 * @return
	 */
	public boolean updateFun(LinkedHashMap<String, String> condition,LinkedHashMap<String, String> obj,String req_type,String resp_type){
		boolean result = false;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			//更新企业表
			if(req_type!=null&&!"".equals(req_type)){
				condition.put("cmdtype", "1");
				obj.put("rettype", req_type);
				result=empTransDao.update(conn, GwBaseprotocol.class, obj, condition);
			}
			if(result){
				for(int i=2;i<6;i++){
					condition.put("cmdtype", i+"");
					obj.put("rettype", resp_type);
					result=empTransDao.update(conn, GwBaseprotocol.class, obj, condition);
				}
			}
			
			
			if(result)
			{
				empTransDao.commitTransaction(conn);
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存企业基本协议异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	
	/**
	 * 删除方法
	 * @param map
	 * @param conmap
	 * @return
	 */
	public int del(LinkedHashMap<String, String> conmap,LinkedHashMap<String, String> enternMap,LinkedHashMap<String, String> enternContMap){
		int result = 0;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			//更新企业表
			empTransDao.update(conn, GwMultiEnterp.class, enternMap, enternContMap);
			//删除当前这条记录
			 result=empTransDao.delete(conn, GwBaseprotocol.class, conmap);
			 //删除这个条记录的关系映射表
			String funname= conmap.get("funname");
			if("MO".equals(funname.toUpperCase())||"RPT".equals(funname.toUpperCase())){
				LinkedHashMap<String, String> map =new LinkedHashMap<String, String>();
				map.put("ecid", conmap.get("ecid"));
				String Str="1";
				if("MO".equals(funname)){
					Str="1";
				}else{
					Str="2";
				}
				map.put("pushflag", Str);
				empTransDao.delete(conn, GwPushprotomtch.class, map);
			}else{
				empTransDao.delete(conn, GwProtomtch.class, conmap);
			}
			if(result>0)
			{
				empTransDao.commitTransaction(conn);
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}catch(Exception e)
		{
			result=0;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存企业基本协议异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 函数列表信息
	 */
	public List<DynaBean> getFunList(LinkedHashMap<String, String> conditionMap,PageInfo pageinfo) throws Exception
	{
		
		return dao.getFunList(conditionMap, pageinfo);
	}
	
	/**
	 * 获得梦网编码字段
	 */
	public List<DynaBean> getMWCode(String funname,String cmdtype) throws Exception{
		return dao.getMWCode(funname, cmdtype);
	}
	
	/**
	 * 获得解析内容
	 */
	public List<GwBaseprotocol> getParseContext(String funname,String cmdtype) throws Exception{
		  String sql = "select * from GW_BASEPROTOCOL where FUNNAME='"+funname+"' and CMDTYPE='"+cmdtype+"'";
		  List<GwBaseprotocol> list  = superDao.findEntityListBySQL(GwBaseprotocol.class, sql, StaticValue.EMP_POOLNAME);
		  return list;
	}
	
	/**
	 * 获取是否映射信息
	 */
	public List<GwBaseprotocol> getbase(String funname,String funtype,String cmdtype) throws Exception{
		  String sql = "select * from GW_BASEPROTOCOL where FUNNAME='"+funname+"' and FUNTYPE='"+funtype+"' and CMDTYPE='"+cmdtype+"'";
		  List<GwBaseprotocol> list  = superDao.findEntityListBySQL(GwBaseprotocol.class, sql, StaticValue.EMP_POOLNAME);
		  return list;
	}
	
	/**
	 * 获得字段映射关系接口类型
	 */
	public List<GwProtomtch> getInter(String funname,String cmdtype,String funtype) throws Exception{
		  String sql = "select * from GW_PROTOMTCH where FUNNAME='"+funname+"' and CMDTYPE='"+cmdtype+"' and FUNTYPE='"+funtype+"' order by ID";
		  List<GwProtomtch> list  = superDao.findEntityListBySQL(GwProtomtch.class, sql, StaticValue.EMP_POOLNAME);
		  return list;
	}
	
	/**
	 * 获得字段映射关系--MO/RPT   推送类型1-MO   2-RPT
	 */
	public List<GwPushprotomtch> getMORPT(String funname,String cmdtype) throws Exception{
		  String sql = "select * from GW_PUSHPROTOMTCH where PUSHFLAG='"+funname+"' and CMDTYPE='"+cmdtype+"'";
		  List<GwPushprotomtch> list  = superDao.findEntityListBySQL(GwPushprotomtch.class, sql, StaticValue.EMP_POOLNAME);
		  return list;
	}
	
	/**
	 * 新增方法类型 
	 * @param ent
	 * @return
	 * @throws Exception
	 */
	public boolean  addFuntype(GwMultiEnterp ent){
		boolean result=false;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			result=empTransDao.save(conn, ent);
		}catch(Exception e)
		{
			result=false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"新增方法类型异常！");
		}
		return result;
	}
	
	/**
	 * 解析之后保存（一般的，不包含MO，RPT）  
	 * @param map
	 * @param conmap
	 * @return
	 */
	public int saveMapping(LinkedHashMap<String, String> condition,LinkedHashMap<String, String> obj,List<GwProtomtch> add_list,List<GwProtomtch> delete_list){
		int result = 0;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);

			boolean updatesuc=empTransDao.update(conn, GwBaseprotocol.class, obj, condition);
			if(updatesuc){
				for(int k=0;k<delete_list.size();k++){
					GwProtomtch to=delete_list.get(k);
					LinkedHashMap<String, String> map = new LinkedHashMap<String,String>();
					map.put("id", to.getId().toString());
					empTransDao.delete(conn, GwProtomtch.class, map);
				}
				
				if(add_list!=null&&add_list.size()>0){
					for(int k=0;k<add_list.size();k++){
						empTransDao.saveObjectReturnID(conn, add_list.get(k));
						result=result+1;
					}

				}
			}
		}catch(Exception e)
		{
			result=0;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存企业基本协议异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	
	
	
	/**
	 * 解析之后保存（一般的，不包含MO，RPT）  
	 * @param map
	 * @param conmap
	 * @return
	 */
	public int saveMORPTMapping(LinkedHashMap<String, String> condition,LinkedHashMap<String, String> obj,List<GwPushprotomtch> add_list,List<GwPushprotomtch> delete_list){
		int result = 0;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);

			boolean updatesuc=empTransDao.update(conn, GwBaseprotocol.class, obj, condition);
			if(updatesuc){
				for(int k=0;k<delete_list.size();k++){
					GwPushprotomtch to=delete_list.get(k);
					LinkedHashMap<String, String> map = new LinkedHashMap<String,String>();
					map.put("id", to.getId().toString());
					empTransDao.delete(conn, GwPushprotomtch.class, map);
				}
				
				if(add_list!=null&&add_list.size()>0){
					for(int k=0;k<add_list.size();k++){
						empTransDao.saveObjectReturnID(conn, add_list.get(k));
						result=result+1;
					}

				}
			}
		}catch(Exception e)
		{
			result=0;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存企业基本协议异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public List<GwBaseprotocol> isExist(LinkedHashMap<String, String> con){
		List<GwBaseprotocol> list =null;
		try {
			list = genericBaseDAO.findListByCondition(GwBaseprotocol.class, con, null);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询企业基本协议表异常。");
			return null;
		}
		return list;
	}
	
}
