package com.montnets.emp.netnews.biz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.netnews.dao.TrustDataDao;
import com.montnets.emp.netnews.entity.LfWXData;
import com.montnets.emp.netnews.entity.LfWXDataChos;
import com.montnets.emp.netnews.entity.LfWXTrustCols;
import com.montnets.emp.util.PageInfo;
public class TrustDataBiz extends SuperBiz{
	
	/*
	 * 查询互动项数据
	 */
	public List<DynaBean> getData(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception {
		return new TrustDataDao().getData(conditionMap, pageInfo);
	}
	/**
	 * 动态生成业务数据表
	 * 
	 * @param tableName
	 *            数据表名
	 * @param colList
	 *            业务数据字段List
	 * @return boolean
	 */
	public boolean trustDataTable(String tableName, List<LfWXTrustCols> colList,
			String trustType) throws SQLException {
		return new TrustDataDao().trustDataTable(tableName, colList, trustType);
	}
	
	/**
	 * 动态创建表信息
	 * @param tableName
	 *            数据表名
	 * @param colList
	 *            插入信息
	 * @param trustType
	 *            插入类型
	 */
	public boolean createDataTable(String tableName, List<LfWXData> colList,
			String trustType) throws SQLException {
		return new TrustDataDao().createDataTable(tableName, colList, trustType);
	}
	

	/**
	 * 删除动态的表
	 * 
	 * @param id
	 *            表名
	 * @return boolean
	 */
	public boolean delTrustTable(String tableName) throws Exception{
		return new TrustDataDao().delTrustTable(tableName);
	}
	
	/**
	 * 通过ID删除互动项
	 * 
	 * @param id
	 *            表的ID
	 * @return boolean
	 */
	public boolean delete(String id){
		boolean result = false;
		Connection conn = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("dId", id);
		try {
			conn =   empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			empTransDao.delete(conn,LfWXDataChos.class,conditionMap);
			empTransDao.delete(conn, LfWXData.class, id);
			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"删除互动项");
			empTransDao.rollBackTransaction(conn);
		}finally{
			empTransDao.closeConnection(conn);
		}
		
		return result;
		
	}
	/**
	 * 获取所有手机号码HashSet
	 * 
	 * @param tableName
	 *            业务数据表名
	 * @return HashSet
	 * @throws Exception
	 */
	public HashSet<String> getTrustDataMobile(String tableName) throws Exception  {
		return new TrustDataDao().getTrustDataMobile(tableName);
	}
	
	public boolean trustDataImport(String tableName, List<List<String>> trustList)
	throws SQLException {
		return new TrustDataDao().trustDataImport(tableName, trustList);
	}
	
	public List<List<String>> getTrustDataView(String tableName,int colNum,PageInfo pageInfo) throws Exception  {
		return new TrustDataDao().getTrustDataView(tableName, colNum,pageInfo);
	}
	
	/**
	 * 删除业务数据记录
	 * 
	 * @param tableName
	 *            业务数据表名
	 * @param ID
	 *            自动编号
	 * @return int
	 * @throws Exception
	 */
	public int trustDataDelView(String tableName, String ID)
	throws Exception {
		return new TrustDataDao().trustDataDelView(tableName, ID);
	}
	
	/**
	 * 编辑业务数据
	 * 
	 * @param tableName
	 *            业务数据表名
	 * @param ID
	 *            自动编号
	 * @return List
	 * @throws Exception
	 */
	public List<String> trustDataViewEdit(String tableName, String id)
	throws Exception {
		return new TrustDataDao().trustDataViewEdit(tableName, id);
	}
	
	/**
	 * 判断手机号码是否存在
	 * 
	 * @return boolean true：存在；false：不存在
	 */
	public boolean checkTrustDataPhone(String tableName, String phone)
			throws SQLException {
		return new TrustDataDao().checkTrustDataPhone(tableName, phone);
	}
	
	public boolean trustDataUpdate(String tableName, String ID, List<LfWXTrustCols> colList,
			List<String> dataList) throws Exception {
		return new TrustDataDao().trustDataUpdate(tableName, ID, colList, dataList);
	}
	
	/**
	 * 添加互动项数据
	 * @param wxData
	 * @param dataChos
	 * @return
	 */
	public boolean add(LfWXData wxData,List<LfWXDataChos> dataChos){
		boolean result = false;
		Connection conn = null;
		try {
			conn =   empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			Long id = empTransDao.saveObjProReturnID(conn, wxData);
			wxData.setDId(id);
			if(dataChos != null && dataChos.size()>0){
				for (LfWXDataChos lfWXDataChos : dataChos) {
					lfWXDataChos.setDId(id);
				}
				empTransDao.save(conn, dataChos, LfWXDataChos.class);
			}
			
			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"添加互动项数据异常！");
			empTransDao.rollBackTransaction(conn);
		}finally{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 修改互动项数据
	 * @param wxData
	 * @param dataChos
	 * @return
	 */
	public boolean update(LfWXData wxData,List<LfWXDataChos> dataChos){
		boolean result = false;
		Connection conn = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("dId", wxData.getDId().toString());
		try {
			conn =   empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			//先删除数据选项表里面的数据
			empTransDao.delete(conn, LfWXDataChos.class, conditionMap);
			empTransDao.update(conn, wxData);
			if(dataChos != null && dataChos.size()>0){
				empTransDao.save(conn, dataChos, LfWXDataChos.class);
			}
			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"修改互动项数据异常！");
			empTransDao.rollBackTransaction(conn);
		}finally{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 新增网讯互动数据
	 * @param netId
	 * @param dataId
	 * @return
	 */
	public boolean addDynTable(String tableName, String params,String lgcorpcode,String lguserid)
	{
		try
		{
			if(params == null || params.length() == 0)
			{
				EmpExecutionContext.error("新增移动网讯模板的参数值为空！corpCode："+lgcorpcode+",userid:"+lguserid+",参数表名:"+tableName);
				return false;
			}
			
			List<LfWXTrustCols> colList = new ArrayList<LfWXTrustCols>();
			
			LfWXTrustCols trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("NETID");
			trustDataCol.setColType(1);
			trustDataCol.setColSize(38);
			colList.add(trustDataCol);
			
			trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("TASKID");
			trustDataCol.setColType(1);
			trustDataCol.setColSize(38);
			colList.add(trustDataCol);
			
			trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("PHONE");
			trustDataCol.setColType(0);
			trustDataCol.setColSize(15);
			colList.add(trustDataCol);
			
			trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("DATE_TIME");
			trustDataCol.setColType(2);
			trustDataCol.setColSize(8);
			colList.add(trustDataCol);
			
			String[] paramsArray = params.split(",");
			Set<String> paramsSet=new HashSet<String>();
			for(String par :paramsArray){
				paramsSet.add(par);
			}
			for(String par :paramsSet)
			{
				trustDataCol = new LfWXTrustCols();
				trustDataCol.setColName("P"+par);
				trustDataCol.setColType(0);
				trustDataCol.setColSize(256);
				colList.add(trustDataCol);
			}
			
			boolean optColTable = this.trustDataTable(tableName, colList, "2");
			return optColTable;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"新增移动网讯模板的参数数据异常！,参数表名:"+tableName);
			return false;
		}
		
	}
	
	
	/**
	 * 新增网讯互动数据
	 * @param netId
	 * @param dataId
	 * @return
	 */
	public boolean addDynTable(String tableName, String params)
	{
		try
		{
			if(params == null || params.length() == 0)
			{
				EmpExecutionContext.error("新增移动网讯模板的参数值为空！参数表名:"+tableName);
				return false;
			}
			
			List<LfWXTrustCols> colList = new ArrayList<LfWXTrustCols>();
			
			LfWXTrustCols trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("NETID");
			trustDataCol.setColType(1);
			trustDataCol.setColSize(38);
			colList.add(trustDataCol);
			
			trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("TASKID");
			trustDataCol.setColType(1);
			trustDataCol.setColSize(38);
			colList.add(trustDataCol);
			
			trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("PHONE");
			trustDataCol.setColType(0);
			trustDataCol.setColSize(15);
			colList.add(trustDataCol);
			
			trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("DATE_TIME");
			trustDataCol.setColType(2);
			trustDataCol.setColSize(8);
			colList.add(trustDataCol);
			
			String[] paramsArray = params.split(",");
			Set<String> paramsSet=new HashSet<String>();
			for(String par :paramsArray){
				paramsSet.add(par);
			}
			for(String par :paramsSet)
			{
				trustDataCol = new LfWXTrustCols();
				trustDataCol.setColName("P"+par);
				trustDataCol.setColType(0);
				trustDataCol.setColSize(256);
				colList.add(trustDataCol);
			}
			
			boolean optColTable = this.trustDataTable(tableName, colList, "2");
			return optColTable;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"新增移动网讯模板的参数数据异常！,参数表名:"+tableName);
			return false;
		}
		
	}
	/**
	 * 获取互动项编码
	 * @return
	 */
	public Long getWxCode(){
		long value = empDao.getIdByPro(17,1);
		return value;
	}
}
