package com.montnets.emp.corpmanage.biz;

import java.sql.Connection;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.corpmanage.dao.GenericLfSpCorpBindVoDAO;
import com.montnets.emp.corpmanage.vo.LfSpCorpBindVo;
import com.montnets.emp.entity.corp.GwUserproperty;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.util.PageInfo;


/**
 * 
 * @author Administrator
 *
 */
public class CorpSpUserBindBiz extends SuperBiz 
{
	/**
	 * 
	 * @param lfSpBusBindVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfSpCorpBindVo> getSpCorpBindVos(LfSpCorpBindVo lfSpBusBindVo,
			PageInfo pageInfo) throws Exception
	{
		return new GenericLfSpCorpBindVoDAO().findLfSpCorpBindVos(
				lfSpBusBindVo, pageInfo);
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GtPortUsed> getUnBindEmpUserID() throws Exception
	{
		return new GenericLfSpCorpBindVoDAO().findUnBindEmpUserID();
	}
	
	/**
	 * 获取EMP账号
	 * @description    
	 * @param spType 1：EMP应用账号；2：EMP接入账号
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-7-18 下午07:17:54
	 */
	public List<GtPortUsed> getUnBindEmpUserID(int spType) throws Exception
	{
		return new GenericLfSpCorpBindVoDAO().findUnBindEmpUserID(spType);
	}
	
	/**
	 * 获取未绑定彩信账号
	 * @return
	 * @throws Exception
	 */
	public List<GtPortUsed> getUnBindEmpMMsUserID() throws Exception
	{
		return new GenericLfSpCorpBindVoDAO().findUnBindEmpMMsUserID();
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GtPortUsed> getUnBindDBUserID() throws Exception
	{
		return new GenericLfSpCorpBindVoDAO().findUnBindDBUserID();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GtPortUsed> findAllEmpUserID() throws Exception
	{
		return new GenericLfSpCorpBindVoDAO().findAllEmpUserID();
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<GtPortUsed> findAllDBServerUserID() throws Exception
	{
		return new GenericLfSpCorpBindVoDAO().findAllDBServerUserID();
	}
	
    /**
     * 托管版EMP短信SP账号绑定，保存绑定关系和GwUserproperty
     * @param user
     * @param ipMac
     * @param corpCode
     * @return
     */
	public int addLfSpDepBindList(List<LfSpDepBind> idList,List<GwUserproperty> gwUserpropertyList)
	{
		// 获取连接
		Connection conn = null;
		//返回值
		int resultRs = -1;
		
		try
		{
			//获取连接
			conn = empTransDao.getConnection();
			//开启事务
			empTransDao.beginTransaction(conn);
			resultRs=empTransDao.save(conn, idList, LfSpDepBind.class);
			resultRs=empTransDao.save(conn, gwUserpropertyList, GwUserproperty.class);
			empTransDao.commitTransaction(conn);
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "托管版EMP短信SP账号绑定，保存绑定关系和GwUserproperty失败！");
			resultRs = -1;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return resultRs;
	}
}
