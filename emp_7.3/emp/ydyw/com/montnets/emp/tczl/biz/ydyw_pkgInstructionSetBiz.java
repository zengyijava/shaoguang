package com.montnets.emp.tczl.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.pasgroup.AcmdRoute;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.table.pasgroup.TableAcmdRoute;
import com.montnets.emp.tczl.dao.ydyw_pkgInstructionSetDao;
import com.montnets.emp.tczl.entity.LfTaocanCmd;
import com.montnets.emp.tczl.vo.LfTaocanCmdVo;
import com.montnets.emp.util.PageInfo;

public class ydyw_pkgInstructionSetBiz extends SuperBiz
{
	ErrorLoger errorLoger = new ErrorLoger();
	private ydyw_pkgInstructionSetDao pkgInstructionSetDao;
	private SuperDAO superDao;
	public ydyw_pkgInstructionSetBiz()
	{
		pkgInstructionSetDao = new ydyw_pkgInstructionSetDao();
		superDao = new SuperDAO();
	}
	
	/**
	 * FunName:查询结果集方法
	 * Description:
	 * @param 
	 * @retuen List<LfTaocanCmdVo>
	 */
	public List<LfTaocanCmdVo> getLfTaocanCmdVo(LfTaocanCmdVo taocanVo, PageInfo pageInfo)throws Exception 
	{
		List<LfTaocanCmdVo> taocanVoList = null;
		try
		{
			if (pageInfo == null)
			{
				taocanVoList = pkgInstructionSetDao.findLfTaocanCmdVo(taocanVo);
			} else
			{
				taocanVoList = pkgInstructionSetDao.findLfTaocanCmdVo(taocanVo, pageInfo);
			}
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "业务管理查询异常。");
			//异常处理
			throw e;
		}
		return taocanVoList;
	}
	
	public List<Userdata> findSpUser(LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo) throws Exception{
		List<Userdata> userdatas = null;
		try
		{
			userdatas = new ydyw_pkgInstructionSetDao().findSpUser(conditionMap, orderbyMap,
					pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找账户异常！"));
			throw e;
		}
		return userdatas;
	}
	
	public List<Userdata> findSpUserByCorp(String corp,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<Userdata> xx = null;
		try
		{
			xx = new ydyw_pkgInstructionSetDao().findSpUserByCorp(corp, conditionMap, orderbyMap,
					pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找账户异常！"));
			// TODO: handle exception
			throw e;
		}
		return xx;
	}

    public List<AcmdRoute> findAllStruct()throws Exception{
	  
	  String sql = "SELECT * FROM "+TableAcmdRoute.TABLE_NAME;
	  return superDao.findEntityListBySQL(AcmdRoute.class, sql, StaticValue.EMP_POOLNAME);
    }
    
	  /**
	   * 添加套餐指令设置
	   * @param moTructVo
	   * @return
	   * @throws Exception
	   */
	  public long addMoServiceBind(AcmdRoute moTructorderVo,AcmdRoute moTructexitVo,LfTaocanCmd taocanCmdorder,LfTaocanCmd taocanCmdexit)throws Exception{
		  Long id = 0L;		
			// 获取连接
			Connection conn = empTransDao.getConnection();
			try
			{
				// 开启事务
				empTransDao.beginTransaction(conn);	
				if(moTructorderVo!=null)
				{
					id = empTransDao.saveObjectReturnID(conn, moTructorderVo);
				}
				if(moTructexitVo!=null)
				{
					id = empTransDao.saveObjectReturnID(conn, moTructexitVo);
				}
				if(taocanCmdorder!=null)
				{
					id = empTransDao.saveObjectReturnID(conn, taocanCmdorder);
				}
				if(taocanCmdexit!=null)
				{
					id = empTransDao.saveObjectReturnID(conn, taocanCmdexit);
				}
				// 保存数据库
//				id = empTransDao.saveObjectReturnID(conn, moTructVo);
//				boolean issuccess = false;			
				empTransDao.commitTransaction(conn);
				
			} catch (Exception e)
			{
				// 回滚
				empTransDao.rollBackTransaction(conn);
				id =0L;
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
	   * 修改套餐指令设置
	   * @param moTructVo
	   * @return
	   * @throws Exception
	   */
	  public boolean updateTaocanCmd(AcmdRoute moTructexitVo,LfTaocanCmd taocanCmdorder)throws Exception{
		  boolean flag = false;		
		  // 获取连接
		  Connection conn = empTransDao.getConnection();
		  try
		  {
			  // 开启事务
			  empTransDao.beginTransaction(conn);	
			  if(moTructexitVo!=null)
			  {
				  flag =empTransDao.update(conn, moTructexitVo);
				  if(flag)
				  {
					  if(taocanCmdorder!=null)
					  {
						  flag =empTransDao.update(conn, taocanCmdorder);
						  if (flag)
						  {
							  empTransDao.commitTransaction(conn);
						  }else{
							  empTransDao.rollBackTransaction(conn);
						  }
					  }else{
						  empTransDao.rollBackTransaction(conn);
						  flag =false;
					  }
					  
				  }else{
					  empTransDao.rollBackTransaction(conn);
					  flag =false;
				  }
			  }
			  // 保存数据库
			  
		  } catch (Exception e)
		  {
			  // 回滚
			  empTransDao.rollBackTransaction(conn);
			  flag =false;
			  EmpExecutionContext.error(new ErrorLoger().getErrorLog(e,"修改套餐指令异常！"));
			  throw e;
		  } finally
		  {
			  // 关闭连接
			  empTransDao.closeConnection(conn);
		  }
		  return flag;
	  }
	  
	  /**
	   * 删除套餐指令设置
	   * @param moTructVo
	   * @return
	   * @throws Exception
	   */
	  public boolean deleteTaocanCmd(String cmdId,String aId)throws Exception{
//		  public boolean deleteTaocanCmd(AcmdRoute moTructexitVo,LfTaocanCmd taocanCmdorder)throws Exception{
		  boolean flag =false;
		  int count= 0;		
		  // 获取连接
		  Connection conn = empTransDao.getConnection();
		  try
		  {
			  // 开启事务
			  empTransDao.beginTransaction(conn);	
			  count = empTransDao.delete(conn, AcmdRoute.class, aId);
			  if(count>0)
			  {
				  count =0;
				  count = empTransDao.delete(conn, LfTaocanCmd.class, cmdId);
				  if (count>0)
				  {
					  empTransDao.commitTransaction(conn);
					  flag =true;
				  }else{
					  empTransDao.rollBackTransaction(conn);
				  }
			  }
			  
		  } catch (Exception e)
		  {
			  // 回滚
			  empTransDao.rollBackTransaction(conn);
			  EmpExecutionContext.error(new ErrorLoger().getErrorLog(e,"删除套餐指令异常！"));
			  throw e;
		  } finally
		  {
			  // 关闭连接
			  empTransDao.closeConnection(conn);
		  }
		  return flag;
	  }
	
	  
	  
}
