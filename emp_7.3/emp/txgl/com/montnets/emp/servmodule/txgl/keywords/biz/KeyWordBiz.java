package com.montnets.emp.servmodule.txgl.keywords.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.keywords.KeywordBlack;
import com.montnets.emp.entity.keywords.LfKeywords;


/**
 * 
 * @author Administrator
 *
 */
public class KeyWordBiz extends SuperBiz{

	/**
	 * 查找当前企业已启用的关键字
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<String> getKwInUsed(String corpCode) throws Exception {

		List<String> strKwsList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		try{
			//查找当前企业及全局的管理
			conditionMap.put("corpCode&in", "100000," + corpCode);
			//启用状态
			conditionMap.put("kwState", "1");
			//按关键字名称排序
			orderByMap.put("keyWord", StaticValue.ASC);
			List<LfKeywords> kwsList = empDao.findListByCondition(LfKeywords.class, conditionMap, null);
			if(kwsList == null || kwsList.size() == 0){
				return null;
			}
			strKwsList = new ArrayList<String>();
			for(int i=0;i<kwsList.size();i++){
				strKwsList.add(kwsList.get(i).getKeyWord().toUpperCase());
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"查询企业启用的关键字列表发生异常！");
			throw e;
		}
		Collections.sort(strKwsList);
		return strKwsList;
	}
	
	public List<String> getKwInUsed() throws Exception {

		List<String> strKwsList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("kwState", "1");
			List<LfKeywords> kwsList = empDao.findListByCondition(LfKeywords.class, conditionMap, null);
			if(kwsList == null || kwsList.size() == 0){
				return null;
			}
			strKwsList = new ArrayList<String>();
			for(int i=0;i<kwsList.size();i++){
				strKwsList.add(kwsList.get(i).getKeyWord().toUpperCase());
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"查询启用的关键字列表发生异常！");
			//异常处理
			throw e;
		}
		Collections.sort(strKwsList);
		return strKwsList;
	}

	/**
	 * 判断此关键字是否已存在
	 * @param keyWord
	 * @return
	 * @throws Exception
	 */
	public boolean isKwExists(String keyWord,String corpcode) throws Exception {
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			//关键字
			conditionMap.put("keyWord", keyWord);
			conditionMap.put("corpCode", corpcode);
			//查询关键字信息
			List<LfKeywords> kwsList = empDao.findListByCondition(LfKeywords.class, conditionMap, null);
			if(kwsList == null || kwsList.size() == 0){
				result = false;
			}
		}catch(Exception e){
			EmpExecutionContext.error(e, "判断关键字是否存在发生异常！");
			throw e;
		}
		return result;
	}
	
	/**
	 * 添加单个关键字
	 * @param keyword 关键字信息
	 * @return 成功个数
	 */
	public Integer addKw(LfKeywords keyword)
	{
		List<LfKeywords> kwList = new ArrayList<LfKeywords>();
		kwList.add(keyword);
		return addKwList(kwList);
	}
	
	/**
	 * 批量添加关键字 
	 * @param kwList 关键字集合
	 * @return 成功添加的个数
	 */
	public Integer addKwList(List<LfKeywords> kwList)
	{
		Integer resultCount = 0;
		
		Connection conn = empTransDao.getConnection();
	
		try
		{
			empTransDao.beginTransaction(conn);
			int corpType = StaticValue.getCORPTYPE();
			resultCount = empTransDao.save(conn, kwList, LfKeywords.class);
			if(resultCount > 0)
			{
				//如果是单企业，则需处理网关的关键字表
				if(corpType == 0)
				{
					Integer addCount = doKeywordBlack(kwList , conn, 1);
					if(addCount - resultCount == 0)
					{
						empTransDao.commitTransaction(conn);
					}else
					{
						empTransDao.rollBackTransaction(conn);
						resultCount = 0;
					}
				}else
				{
					empTransDao.commitTransaction(conn);
				}
			}else
			{
				empTransDao.rollBackTransaction(conn);
				resultCount = 0;
			}
		}catch(Exception e)
		{
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			resultCount = 0;
			EmpExecutionContext.error(e,"批量添加关键字异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		
		return resultCount;
	}

	
	/**
	 * 批量删除关键字
	 * @param ids 关键字id，多个id则以逗号分隔
	 * @return
	 */
	public Integer delKwList(String ids)
	{
		Integer resultCount = 0;
		
		Connection conn = empTransDao.getConnection();
	
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("kwId&in",ids);
		try
		{
			List<LfKeywords> kwList = empDao.findListBySymbolsCondition(LfKeywords.class, conditionMap, null);
			empTransDao.beginTransaction(conn);
			int corpType = StaticValue.getCORPTYPE();
			resultCount = empTransDao.delete(conn,LfKeywords.class,ids);
			if(resultCount > 0)
			{
				//如果是单企业，则需处理网关的关键字表
				if(corpType == 0)
				{
					Integer doCount = doKeywordBlack(kwList , conn, 0);
					if(doCount - resultCount == 0)
					{
						empTransDao.commitTransaction(conn);
					}else
					{
						empTransDao.rollBackTransaction(conn);
						resultCount = 0;
					}
				}else
				{
					empTransDao.commitTransaction(conn);
				}
			}else
			{
				empTransDao.rollBackTransaction(conn);
				resultCount = 0;
			}
		}catch(Exception e)
		{
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			resultCount = 0;
			EmpExecutionContext.error(e,"批量删除关键字发生异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		
		return resultCount;
	}
	
	/**
	 * 修改关键字状态
	 * @param id		关键字id
	 * @param state		关键字修改后的状态
	 * @return
	 */
	public boolean changeState(LfKeywords kw)
	{
		boolean result = false;
		
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			int corpType = StaticValue.getCORPTYPE();
			//先更新EMP的关键字表
			result = empTransDao.update(conn, kw);
			if(result)
			{
				//如果是单企业，则需处理网关的关键字表
				if(corpType == 0)
				{
					List<LfKeywords> kwList = new ArrayList<LfKeywords>();
					kwList.add(kw);
					if(doKeywordBlack(kwList , conn, kw.getKwState()) > 0)
					{
						empTransDao.commitTransaction(conn);
					}else
					{
						empTransDao.rollBackTransaction(conn);
						result = false;
					}
				}else
				{
					empTransDao.commitTransaction(conn);
				}
			}else
			{
				empTransDao.rollBackTransaction(conn);
				result = false;
			}
		}catch(Exception e)
		{
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			result = false;
			EmpExecutionContext.error(e, "修改关键字状态发生异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		
		return result;
	}
	
	/**
	 * 修改关键字
	 * @param kw
	 * @param curKey
	 * @return
	 */
	public boolean editKeyword(LfKeywords kw,String curKey)
	{
		boolean result = false;
		
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			int corpType = StaticValue.getCORPTYPE();
			//先更新EMP的关键字表
			result = empTransDao.update(conn, kw);
			if(result)
			{
				//如果是单企业，则需处理网关的关键字表
				if(corpType == 0)
				{
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("keyWord", curKey+","+kw.getKeyWord());
					//先删除keywordBlack中关键字重复的数据
					if(empTransDao.delete(conn,KeywordBlack.class, conditionMap ) > 0)
					{
						KeywordBlack keywordBlack = new KeywordBlack();
						keywordBlack.setKeyWord(curKey);
						keywordBlack.setKeyType(1);
						keywordBlack.setKeyLevel(1);
						keywordBlack.setOpType(0);
						
						//先添加要删除的关键字记录
						result = empTransDao.save(conn, keywordBlack);
						
						if(result)
						{
							keywordBlack.setKeyWord(kw.getKeyWord());
							keywordBlack.setOpType(1);
							//再添加修改后的关键字记录
							result = empTransDao.save(conn, keywordBlack);
						}
					}
				}
				if(result)
				{	
					empTransDao.commitTransaction(conn);
				}else
				{
					empTransDao.rollBackTransaction(conn);
				}
			}else
			{
				empTransDao.rollBackTransaction(conn);
				result = false;
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"修改关键字异常！");
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			result = false;
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		
		return result;
	}
	
	/**
	 * 操作网关关键字表
	 * @param kwList	EMP关键字集合
	 * @param conn		数据库连接
	 * @param opType	操作类型 0-删除，1-添加
	 * @return			成功添加的数据行数
	 * @throws Exception
	 */
	public Integer doKeywordBlack(List<LfKeywords> kwList,Connection conn,int opType) throws Exception
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		StringBuffer kwBuf = new StringBuffer();
		List<KeywordBlack> kbList = new ArrayList<KeywordBlack>();
		int kwCount = 0;
		for(LfKeywords kw : kwList)
		{
			KeywordBlack keywordBlack = new KeywordBlack();
			keywordBlack.setKeyWord(kw.getKeyWord());
			keywordBlack.setKeyType(1);
			keywordBlack.setKeyLevel(1);
			keywordBlack.setOpType(opType);
			
			kbList.add(keywordBlack);
			kwCount++;
			kwBuf.append(kw.getKeyWord());
			//如果关键字数达到500，则先删除表中存在的关键字 
			if(kwCount == 500)
			{
				conditionMap.put("keyWord", kwBuf.toString());
				empTransDao.delete(conn,KeywordBlack.class, conditionMap );
				kwBuf.setLength(0);
				
				kwCount = 0;
			}else
			{
				kwBuf.append(",");
			}
		}
		if(kwCount > 0)
		{
			kwBuf.deleteCharAt(kwBuf.length()-1);
			conditionMap.put("keyWord", kwBuf.toString());
			empTransDao.delete(conn,KeywordBlack.class, conditionMap );
			kwBuf.setLength(0);
		}
		//添加网关黑名单表
		return empTransDao.save(conn, kbList, KeywordBlack.class);
	}
}
