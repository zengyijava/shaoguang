package com.montnets.emp.qyll.biz;

import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.dao.LlProductDao;
import com.montnets.emp.qyll.entity.LlProduct;
import com.montnets.emp.qyll.entity.Products;
import com.montnets.emp.util.PageInfo;

public class LlProductBiz extends SuperBiz{
		protected LlProductDao llProductDao;
		//构造函数
		public LlProductBiz(){
			llProductDao = new LlProductDao();	
		}
		/*
		 * 查询LL_PRODUCT中所有数据，返回List<Bean>
		 * 
		 */
		public List<LlProduct> getLlProductList(LlProduct llProduct,
				PageInfo pageInfo) {
			//定义运营商统计报表集合
			List<LlProduct> returnList = null;
			try {
				returnList = llProductDao.getLlProductSql(llProduct,pageInfo);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"通过流量套餐对象获取流量套餐数据异常");
			}
			//返回数据集合
			return returnList;
		}
		public List<LlProduct> getAllLlProductList() {
			List<LlProduct> returnList = null;
			try {
				returnList = llProductDao.getAllLlProductList();
			} catch (Exception e) {
				EmpExecutionContext.error(e,"通过流量套餐对象获取流量套餐数据异常");
			}
			return returnList;
		}
		/*
		 * 新增数据
		 * products：接口返回数据  ecId：企业ID  operatorid：操纵员ID
		 */
		public boolean addProducts(Products products, String ecId , String operatorid) {
			return llProductDao.addProducts(products, ecId , operatorid);
		}
		/*
		 * 获取LL_PRODUCT表中存在的数据 
		 * 返回：Map<productId,Bean>
		 */
		public Map<String, Products> getLlProductMap(LlProduct llProduct) {
			return llProductDao.getLlProductMap(llProduct);
		}
		/*
		 * 修改
		 * productId：产品编号 lguserid：当前操作员
		 */
		public boolean upDateProducts(String productId,String lguserid,LlProduct llProduct) {
			return llProductDao.upDateProducts(productId,lguserid,llProduct);
		}
		/*
		 * 修改数据库存在，接口不存在的状态
		 * pros：产品编号组，以逗号隔开 ls:10010,10086
		 */
		public boolean updateProduct(String pros,LlProduct llProduct, String lguserid) {
			return llProductDao.updateProduct(pros,llProduct,lguserid);
		}
}
