package com.montnets.emp.qyll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.qyll.dao.sql.DataQuerySQLFactory;
import com.montnets.emp.qyll.dao.sql.ReportQuerySql;
import com.montnets.emp.qyll.entity.LlProduct;
import com.montnets.emp.qyll.entity.Products;
import com.montnets.emp.util.PageInfo;

public class LlProductDao extends SuperDAO  {

	public List<LlProduct> getLlProductSql(LlProduct llProduct,
			PageInfo pageInfo) {
		//获取查询sql
		String searchSql = getSearchSql(llProduct);
		if(searchSql == null || searchSql.trim().length() == 0)
		{
			EmpExecutionContext.error("企业流量套餐表,获取查询sql为空。");
			return null;
		}
		String dataSql = searchSql;
		//String dataSql = searchSql + " ORDER BY PRICE";
		List<LlProduct> returnList = null;
		try {
			if(pageInfo != null)
			{
				//总条数语句
				String countSql = "select count(*) totalcount FROM (" + searchSql + " ) A";
				returnList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LlProduct.class, dataSql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
			}else{
				returnList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LlProduct.class, dataSql, null, pageInfo, StaticValue.EMP_POOLNAME);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"企业流量套餐表,获取查询数据异常");
		}
		return returnList;
	}

	private String getSearchSql(LlProduct llProduct) {
		String conditionSql = "";
		//String sql = "SELECT PRODUCTID,PRODUCTNAME,ISP,VOLUME,PRICE FROM LL_PRODUCT WHERE STATUS = 1";
		String sql = "SELECT * FROM LL_PRODUCT WHERE STATUS = 1 AND ECID="+llProduct.getEcid()+" AND PTYPE=0";
		if(llProduct.getProductid() != null && !"".equals(llProduct.getProductid())){
			conditionSql = conditionSql +" AND PRODUCTID='"+llProduct.getProductid()+"'";
		}
		if(llProduct.getProductname()!= null && !"".equals(llProduct.getProductname())){
			conditionSql = conditionSql +" AND PRODUCTNAME LIKE '%"+llProduct.getProductname()+"%'";
		}
		if(!"9999".equals(llProduct.getIsp())){
			conditionSql = conditionSql + " AND ISP ='"+llProduct.getIsp()+"'";
		}
		String totalSql = sql + conditionSql;
		return totalSql;
	}

	public List<LlProduct> getAllLlProductList() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LlProduct> returnList = new ArrayList<LlProduct>();
		try 
		{
			String sql = "SELECT PRODUCTID,PRICE FROM LL_PRODUCT WHERE STATUS = 1";
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) 
			{
				LlProduct  returnBean = new LlProduct();
				returnBean.setProductid(rs.getString("PRODUCTID"));
				returnBean.setPrice(rs.getDouble("PRICE"));
				returnList.add(returnBean);
			}
			return returnList;
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"数据查询异常");
			return null;
		} 
		finally 
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"数据链接关闭异常");
			}
		}
	}

	public boolean addProducts(Products products, String ecId, String operatorid) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean returnFlag = false;
		try {
			// 通过SQL工厂生产对应数据库的SQL实例
			ReportQuerySql reportQuerySql = new DataQuerySQLFactory().getSearchSql(StaticValue.DBTYPE);
			String sql = reportQuerySql.getLlProductInsertSql();
			//String sql = "INSERT INTO LL_PRODUCT (ID,ECID,PRODUCTID,PRODUCTNAME,ISP,VOLUME,PRICE,DISCPRICE,AREA,PTYPE,PMOLD,RTYPE,STATUS,UPDATETM,CREATETM,OPERATORID) VALUES(seq_ll_product.nextval,?,?,?,?,?,?,?,?,?,?,?,1,sysdate,sysdate,?)";
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setInt(1,Integer.parseInt(ecId));
			ps.setString(2,products.getProductId());
			ps.setString(3,products.getProductName());
			ps.setString(4,products.getISP());
			ps.setString(5,products.getVolume());
			ps.setDouble(6,products.getPrice());
			ps.setDouble(7,Double.parseDouble(products.getDiscountPrice()));
			ps.setString(8,products.getArea());
			ps.setInt(9,products.getProductType());
			if(products.getProductType()==0){
				ps.setInt(10,products.getProductMold());
				ps.setInt(11,products.getType());
			}else{
				ps.setInt(10,-1);
				ps.setInt(11,-1);
			}
			ps.setInt(12,Integer.parseInt(operatorid));
			int length = ps.executeUpdate();
			if(length > 0){
				returnFlag = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"数据插入异常");
			return false;
		} finally{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"连接关闭异常");
			}
		}
		return returnFlag;
	}

	public Map<String, Products> getLlProductMap(LlProduct llProduct) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Products  returnBean = null;
		Map<String, Products> returnMap= new HashMap<String, Products>();
		try 
		{
			//String sql = "SELECT PRODUCTID,PRICE FROM LL_PRODUCT WHERE STATUS = 1";
			String sql = "SELECT * FROM LL_PRODUCT WHERE STATUS = 1 AND ECID="+llProduct.getEcid()+" AND PTYPE=0";
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) 
			{
				returnBean = new Products();
				returnBean.setProductId(rs.getString("PRODUCTID"));
				returnBean.setPrice(rs.getDouble("PRICE"));
				returnBean.setDiscountPrice(String.valueOf(rs.getDouble("DISCPRICE")));
				returnMap.put(rs.getString("PRODUCTID"), returnBean);
			}
			return returnMap;
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"数据查询异常");
			return null;
		} 
		finally 
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"数据链接关闭异常");
			}
		}
	}

	public boolean upDateProducts(String productId,String lguserid,LlProduct llProduct) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean returnFlag = false;
		try {
			// 通过SQL工厂生产对应数据库的SQL实例
			ReportQuerySql reportQuerySql = new DataQuerySQLFactory().getSearchSql(StaticValue.DBTYPE);
			String sql = reportQuerySql.getLlProductUpdateSql(llProduct);
			//String sql = "UPDATE LL_PRODUCT SET STATUS=0,OPERATORID=? WHERE PRODUCTID=?"+" AND ECID="+llProduct.getEcid();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setInt(1,Integer.parseInt(lguserid));
			ps.setString(2,productId);
			int length = ps.executeUpdate();
			if(length > 0){
				returnFlag = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"数据更新异常");
			return false;
		} finally{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"连接关闭异常");
			}
		}
		return returnFlag;
	}

	public boolean updateProduct(String pros,LlProduct llProduct,String lguserid) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean returnFlag = false;
		try {
			// 通过SQL工厂生产对应数据库的SQL实例
			ReportQuerySql reportQuerySql = new DataQuerySQLFactory().getSearchSql(StaticValue.DBTYPE);
			String sql = reportQuerySql.getLlProductUpdateInSql(llProduct,pros);
			//String sql = "UPDATE LL_PRODUCT SET STATUS=0,OPERATORID=? WHERE PRODUCTID IN ("+ pros +") AND ECID="+llProduct.getEcid();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setInt(1,Integer.parseInt(lguserid));
			int length = ps.executeUpdate();
			if(length > 0){
				returnFlag = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"数据更新异常");
			return false;
		} finally{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"连接关闭异常");
			}
		}
		return returnFlag;
	}
	
	/**
	 * 根据productID 查询产品表中的自增ID
	 * @param productID
	 * @return
	 */
	public String queryIdByProID(String productID){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String id = "";
		try 
		{
			String sql = "SELECT ID FROM LL_PRODUCT WHERE STATUS = 1 AND PRODUCTID = ? ";
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setString(1, productID);
			rs = ps.executeQuery();
			while (rs.next()) 
			{
				id = rs.getString("ID");
			}
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"数据查询异常");
			return "";
		} 
		finally 
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"数据链接关闭异常");
			}
		}
		return id;
		
	}
	
}
