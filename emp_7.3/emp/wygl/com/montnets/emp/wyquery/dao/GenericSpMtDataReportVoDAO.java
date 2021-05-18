package com.montnets.emp.wyquery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wyquery.vo.WyReportVo;
/**
 * 网优统计报表
 * @功能概要：
 * @项目名称： p_wygl
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-5-6 上午10:41:55
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class GenericSpMtDataReportVoDAO extends SuperDAO{

	/**
	 * 网优统计报表--日报表（不分页）
	 * @param wyrptvo
	 * @return
	 * @throws Exception
	 */
	public List<WyReportVo> findWyRptListByVo(
			WyReportVo wyrptvo)
			throws Exception {
		//sql
		String sql = new GenericSpMtDataReportVoSQL().getWyReportSql(wyrptvo);
		//排序
		String orderBySql = " ORDER BY mtdatareport.spgate,u.GATENAME DESC ";
		String datasql = sql+orderBySql;
		List<WyReportVo> returnList = findVoListBySQL(
				WyReportVo.class, datasql, StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	
	/**
	 * 网优详情查询
	 * @description    
	 * @param wyrptvo
	 * @param reporttype
	 * @param pageInfo
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-5-6 上午11:41:04
	 */
	public List<DynaBean> findWyDetailByDate(
			WyReportVo wyrptvo,String reporttype, PageInfo pageInfo) throws Exception {
		//sql
		String sql = new GenericSpMtDataReportVoSQL().getWyDetailReportSql(wyrptvo, reporttype);
		String oderbystr="";
		if("2".equals(reporttype)){
			oderbystr=" Y DESC,IMONTH DESC ";
		}else{
			oderbystr=" IYMD DESC ";
		}
		//排序
		String orderBySql = " ORDER BY "+oderbystr+",mtdatareport.spgate,u.GATENAME DESC ";
		String datasql = sql+orderBySql;
		String countSql = new StringBuffer("select count(*) totalcount from ("+sql+") A ").toString();
		List<DynaBean>  returnList=null;
		if(pageInfo!=null){
	 	     returnList=new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(datasql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	 	}else{
	 		 returnList=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(datasql);
	 	}
		return returnList;
	}
	
	
	/**
	 * 网优统计报表--日报表（分页）
	 * @param wyrptvo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<WyReportVo> findWyRptListByVoPage(
			WyReportVo wyrptvo, PageInfo pageInfo) throws Exception {
		//sql
		String sql = new GenericSpMtDataReportVoSQL().getWyReportSql(wyrptvo);
		//排序
		String orderBySql = " ORDER BY mtdatareport.spgate,u.GATENAME DESC ";
		String datasql = sql+orderBySql;
		String countSql = new StringBuffer("select count(*) totalcount from ("+sql+") A ").toString();
		
		List<WyReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				WyReportVo.class, datasql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}
	



	/**
	 * 网优统计报表--月/年报表（共用合计）
	 */
	public long[] findSumCount(WyReportVo wyrptvo) throws Exception {
		//提交总数
		 long icount=0L;
		//接受成功数
		 long rsucc=0L;
		//发送失败数
		 long rfail1=0L;
		//接收失败数
		 long rfail2=0L;
		//未返数
		 long rnret=0L;
		//查询字段
		String fieldSql = "SELECT SUM(ICOUNT) ICOUNT,SUM(RSUCC) RSUCC,SUM(RFAIL1) RFAIL1,SUM(RFAIL2) RFAIL2,SUM(RNRET) RNRET FROM  ";
		//获取sql
		String sql1 =new GenericSpMtDataReportVoSQL().getWyReportSql(wyrptvo);
		//分页总sql语句
		String sql =" ("+sql1+" ) MDREPORT";

		//总sql语句   
	    sql = fieldSql+sql;
		//定义连接
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try {
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//获取操作类
			ps = conn.prepareStatement(sql);
			//获取结果集
			rs = ps.executeQuery();
			//遍历
			if (rs.next()) {
				icount = rs.getLong("ICOUNT");
				rsucc = rs.getLong("RSUCC");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网优统计报表查询总数目失败！");
			throw e;
		} finally {
			//关闭连接
			close(rs, ps, conn);
		}
		long[] returnLong = new long[5];
		returnLong[0] = icount;
		returnLong[1] = rsucc;
		returnLong[2] = rfail1;
		returnLong[3] = rfail2;
		returnLong[4] = rnret;
		return returnLong;
	}

}
