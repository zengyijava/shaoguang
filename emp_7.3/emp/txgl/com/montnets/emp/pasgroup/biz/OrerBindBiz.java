package com.montnets.emp.pasgroup.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.AcmdRoute;
import com.montnets.emp.servmodule.txgl.table.TableAcmdRoute;
import com.montnets.emp.util.PageInfo;

public class OrerBindBiz extends SuperBiz{
	
	  private SuperDAO superDao;
	  public OrerBindBiz(){
		  superDao = new SuperDAO();
	  }

	  /**
	   * 添加业务指令绑定
	   * @param moTructVo
	   * @return
	   * @throws Exception
	   */
	  public long addMoServiceBind(AcmdRoute moTructVo)throws Exception{
		  Long id = 0L;		
			// 获取连接
			Connection conn = empTransDao.getConnection();
			try
			{
				// 开启事务
				empTransDao.beginTransaction(conn);												
				// 保存数据库
				id = empTransDao.saveObjectReturnID(conn, moTructVo);
//				boolean issuccess = false;			
				empTransDao.commitTransaction(conn);
				
			} catch (Exception e)
			{
				// 回滚
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(new ErrorLoger().getErrorLog(e,"添加业务指令绑定biz层异常！"));
				throw e;
			} finally
			{
				// 关闭连接
				empTransDao.closeConnection(conn);
			}
			return id.longValue();
	  }
	  
	  /**
	   * 根据ID删除一条业务指令绑定
	   * @return
	   * @throws Exception 
	   */
	  public int delete(String id) throws Exception{
		  
		  if(id==null||id.equals("")){
			  return 0;
		  }
		  int result =  empDao.delete(AcmdRoute.class, id);

		  return result;
	  }
	  
	  
	  
	  /**
	   * 根据id获取上行业务指令绑定对象
	   * @param bindid
	   * @return
	   * @throws Exception
	   */
	  public AcmdRoute findLfMoTructVoBybindid(String bindid)throws Exception{
		  AcmdRoute lfMoTructVo1 = empDao.findObjectByID(AcmdRoute.class, Long.parseLong(bindid));
		  return lfMoTructVo1;
	  }
	  /**
	   * 根据指令编码获取 指令信息
	   * @param structcode
	   * @return
	   * @throws Exception
	   */
	  public AcmdRoute findLfMotructListByCode(String structcode)throws Exception{
		  AcmdRoute lfmotruct = null;
		  String sql = "Select * from  "+TableAcmdRoute.TABLE_NAME +" WHERE "+ TableAcmdRoute.STRUCTCODE+" = '"+structcode+"' ";
		  List<AcmdRoute> lfmotructs = superDao.findEntityListBySQL(AcmdRoute.class, sql, StaticValue.EMP_POOLNAME);
		  if(lfmotructs!= null && lfmotructs.size() > 0){
			  lfmotruct = lfmotructs.get(0);
		  }
		  return lfmotruct;
	  }
	  
	  /**
	   * 指令和SP帐号绑定  或解绑 
	   * status 01 启用 02 绑定
	   * @param structCode
	   * @return
	   * @throws Exception
	   */
	  public boolean bindingTruct(String structCode,String status)throws Exception{
		  boolean flag = false;
		  if(!"".equals(structCode) && structCode != null){
			  String sql = "UPDATE "+TableAcmdRoute.TABLE_NAME+" SET STATUS ='"+status+"'  WHERE STRUCTCODE= '"+structCode+"' ";
			  flag =  superDao.executeBySQL(sql, StaticValue.EMP_POOLNAME);
		  }
		  return flag;
	  }
	  
	  
	  /**
	   * 判断 指令是否已存在
	   * @param structcode
	   * @return
	   * @throws Exception
	   */
	  public boolean findLfMoTructIsExitsByStruct(String structcode)throws Exception{
		  boolean flag = false;
		  String sql = "SELECT * FROM "+TableAcmdRoute.TABLE_NAME+ " WHERE "+TableAcmdRoute.STRUCTCODE+"= '"+structcode+"' ";
		  List<AcmdRoute> lfmotructs = superDao.findEntityListBySQL(AcmdRoute.class, sql, StaticValue.EMP_POOLNAME);
		  if(lfmotructs!= null && lfmotructs.size() > 0){
				flag = true;
		  }
		  return flag;
	  }
	  
	  /**
	   * 查询所有指令
	   * @param structcode
	   * @return
	   * @throws Exception
	   */
	  public List<AcmdRoute> findAllStruct()throws Exception{
		  
		  String sql = "SELECT * FROM "+TableAcmdRoute.TABLE_NAME;
		  return superDao.findEntityListBySQL(AcmdRoute.class, sql, StaticValue.EMP_POOLNAME);
	  }
	  
	  /**
	   * 获取所有启用 并且未绑定指令集
	   * @param structcode
	   * @return
	   * @throws Exception
	   */
	  public List<AcmdRoute> findBindingTructByCode(String structcode)throws Exception{
		  List<AcmdRoute> tructlist = null;
		  if(!"".equals(structcode)){
			  String sql = "SELECT * FROM  "+TableAcmdRoute.TABLE_NAME+" WHERE ("+TableAcmdRoute.STATUS+" ='01' OR ("+TableAcmdRoute.STATUS+" ='02' AND "+TableAcmdRoute.STRUCTCODE+" ='"+structcode+"')) AND "+TableAcmdRoute.TRUCTTYPE+" = '01' ";
			  tructlist  = superDao.findEntityListBySQL(AcmdRoute.class, sql, StaticValue.EMP_POOLNAME);
		  }
		  return tructlist;
	  }
	  
	  /**
	   * 根据指令代码，业务系统名称查询相关的指令绑定信息
	   * @return
	   * @throws Exception 
	   * @throws NumberFormatException 
	   */
	  public List<AcmdRoute>  getMoTructVo(String spids,String userid,String structcode,String bussysname,PageInfo pageInfo) throws NumberFormatException, Exception{
		  
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		orderMap.put("id", StaticValue.DESC);

		if (structcode != null && !structcode.equals("")) {
			conditionMap.put("structcode&like", structcode);
		}
		if (bussysname != null && !bussysname.equals("")) {
			conditionMap.put("bussysname&like",bussysname);
		}
		
		if(spids!=null&&!"".equals(spids)){
			conditionMap.put("spid&in",spids);
		}
		//只查询类型为01:智能引擎 02:接口接入
		conditionMap.put("tructtype&in", "01,02");
		
//		List<AcmdRoute> list = empDao.findPageListBySymbolsCondition(null,
//				AcmdRoute.class, conditionMap, orderMap, pageInfo);
		List<AcmdRoute> list = empDao.findPageListBySymbolsConditionNoCount(null,
				AcmdRoute.class, conditionMap, orderMap, pageInfo);

		return list;
	    
	  }
	  
}
