package com.montnets.emp.passage.biz;

import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.XtGateQueue;

/**
 * 通道管理biz
 * @project p_txgl
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-5-13 下午03:31:11
 * @description
 */
public class GateBiz extends SuperBiz{
	ErrorLoger errorLoger = new ErrorLoger();
	/**
	 * 修改通道签名以及单条短信字数
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void changeSinglen(String spgate,String signstr,String singleLen,String signlen,String signmode,String maxword,String spisuncm) throws Exception
	{
		Connection conn = empTransDao.getConnection();
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> gateMap = new LinkedHashMap<String, String>();
			signstr = signstr == null || "".equals(signstr) ? " " : signstr;
			//短信签名
			gateMap.put("signstr", signstr);
			//单条短信字数
			gateMap.put("singleLen", singleLen);
			String smultilen1="";
			String smultilen2="";
			//短信签名长度
			if("1".equals(signmode)||"2".equals(signmode)){
				Integer multilen1=Integer.parseInt(singleLen)-3;
				Integer multilen2=multilen1-signstr.length();
				smultilen1=multilen1+"";
				smultilen2=multilen2+"";
			}else{
				Integer multilen1=Integer.parseInt(singleLen)-3;
				Integer multilen2=multilen1-Integer.parseInt(signlen);
				smultilen1=multilen1+"";
				smultilen2=multilen2+"";
			}
			gateMap.put("multilen1", smultilen1);
			gateMap.put("multilen2", smultilen2);
			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("gateType", "1");
			empTransDao.beginTransaction(conn);
			//更新通道表
			empTransDao.update(conn, XtGateQueue.class,
					gateMap, conditionMap);
			gateMap.remove("singleLen");
			gateMap.put("singlelen", singleLen);
			//更新路由绑定表
			empTransDao.update(conn, GtPortUsed.class,
					gateMap, conditionMap);
			empTransDao.commitTransaction(conn);
		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"修改通道签名以及单条短信字数异常！"));
			throw e;
		} finally
		{
			empTransDao.closeConnection(conn);
		}
	}
	
	
	
	/**
	 * 修改通道签名以及单条短信字数
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Boolean updatespgate(XtGateQueue gate,String spgate,String spisuncm,String gatetype) throws Exception
	{
		boolean result=true;
		Connection conn = empTransDao.getConnection();
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> gateMap = new LinkedHashMap<String, String>();
			if("1".equals(gatetype)){
				gateMap.put("multilen1", gate.getMultilen1()!=null?gate.getMultilen1().toString():null);
				gateMap.put("enmultilen1", gate.getEnmultilen1()!=null?gate.getEnmultilen1().toString():null);
				if(StaticValue.getCORPTYPE() ==0){
					gateMap.put("multilen2", gate.getMultilen2()!=null?gate.getMultilen2().toString():null);
					gateMap.put("enmultilen2", gate.getEnmultilen2()!=null?gate.getEnmultilen2().toString():null);
				}else if(StaticValue.getCORPTYPE() ==1&&gate.getSignType()==1){
					gateMap.put("multilen2", gate.getMultilen2()!=null?gate.getMultilen2().toString():null);
					gateMap.put("enmultilen2", gate.getEnmultilen2()!=null?gate.getEnmultilen2().toString():null);
				}
				gateMap.put("signlen", gate.getSignlen()!=null?gate.getSignlen().toString():null);
				gateMap.put("ensignlen", gate.getEnsignlen()!=null?gate.getEnsignlen().toString():null);
				gateMap.put("maxwords", gate.getMaxWords()!=null?gate.getMaxWords().toString():null);
				gateMap.put("enmaxwords", gate.getEnmaxwords()!=null?gate.getEnmaxwords().toString():null);
				//单条短信字数
				gateMap.put("singlelen", gate.getSingleLen()!=null?gate.getSingleLen().toString():null);
				gateMap.put("ensinglelen", gate.getEnsinglelen()!=null?gate.getEnsinglelen().toString():null);
			}
			if(StaticValue.getCORPTYPE() ==0){
				//短信签名
				gateMap.put("signstr", gate.getSignstr());
				gateMap.put("ensignstr", gate.getEnsignstr());
			}
			
			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("gateType", gatetype);

			empTransDao.beginTransaction(conn);
			empTransDao.update(conn, gate);
			if(gateMap!=null&&gateMap.size()>0){
				//更新路由绑定表
				empTransDao.update(conn, GtPortUsed.class,
						gateMap, conditionMap);
				if(StaticValue.getCORPTYPE() ==1&&gate.getSignType()==0){
					String lenFunName = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? "LENGTH" : "LEN";
					String sql ="UPDATE GT_PORT_USED SET MULTILEN2=MULTILEN1-"+lenFunName+"(SIGNSTR),ENMULTILEN2=ENMULTILEN1-"+lenFunName+"(ENSIGNSTR) WHERE SPGATE='"
						+spgate+"' AND SPISUNCM="+spisuncm+" AND GATETYPE="+gatetype;
					executeSql(conn, sql);
				}
			}
			empTransDao.commitTransaction(conn);
			result=true;
		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"修改通道号失败"));
			result=false;
			throw e;
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		
		return result;
	}
	
	
	
	public void executeSql(Connection conn,String sql)throws Exception{
		Statement ps = null;
		try {
			ps = conn.createStatement();
			ps.executeUpdate(sql);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"修改路由绑定表失败"));
			throw e;
		} finally {
			if(ps!=null){
				ps.close();
			}
		}
	}
	
	
	
	
	/**
	 * 
	 * @param spgate
	 * @param spisuncm
	 * @return
	 * @throws Exception
	 */
	public boolean isGateExist(String spgate, String spisuncm,String gatetype,String id) throws Exception
	{
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("gateType", gatetype);
			conditionMap.put("id&<>", id);
			List<XtGateQueue> gatesList = empDao.findListBySymbolsCondition(
					XtGateQueue.class, conditionMap, null);
			if (gatesList == null || gatesList.size() == 0)
			{
				result = false;
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通道是否存在查询异常！"));
			throw e;
		}
		return result;
	}
	
	/**
	 * 通道是否存在
	 * @param spgate 通道号
	 * @param spisuncm 运营商
	 * @param gatetype 通道类型
	 * @return
	 * @throws Exception
	 */
	public boolean isGateExist(String spgate, String spisuncm,String gatetype) throws Exception
	{
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("gateType", gatetype);
			List<XtGateQueue> gatesList = empDao.findListBySymbolsCondition(
					XtGateQueue.class, conditionMap, null);
			if (gatesList == null || gatesList.size() == 0)
			{
				result = false;
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通道是否存在查询异常！"));
			throw e;
		}
		return result;
	}
	
	/**
	 * 
	 * @param gateName
	 * @return
	 * @throws Exception
	 */
	public boolean isGateNameExist(String gateName,String gatetype,String id) throws Exception
	{
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
			conditionMap.put("gateName", gateName);
			conditionMap.put("gateType", gatetype);
			conditionMap.put("id&<>", id);
			List<XtGateQueue> gatesList = empDao.findListBySymbolsCondition(
					XtGateQueue.class, conditionMap, null);
			if (gatesList == null || gatesList.size() == 0)
			{
				result = false;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通道判断重复异常！"));
			throw e;
		}
		return result;
	}
	
	

}
