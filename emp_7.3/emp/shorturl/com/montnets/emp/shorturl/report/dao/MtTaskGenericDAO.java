package com.montnets.emp.shorturl.report.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.shorturl.report.vo.ReplyDetailVo;
import com.montnets.emp.shorturl.report.vo.SendDetailMttaskVo;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import java.util.ArrayList;
import java.util.List;

public class MtTaskGenericDAO extends SuperDAO {

	/**
	 * 获取通道号集合
	 * @param corpCode 企业编码
	 * @return 返回通道号动态bean集合
	 */
	public List<DynaBean> findSpgateList(String corpCode) {
		//获取查询sql
		String sql = getSpgateSql(corpCode);
		if(sql == null)
		{
			return null;
		}
		//返回集合
		return getListDynaBeanBySql(sql);
	}

	/**
	 * 获取通道号查询sql
	 * @param corpCode 企业编码
	 * @return 返回查询sql
	 */
	private String getSpgateSql(String corpCode) {
		try
		{
			String sql;
			//单企业
			if(StaticValue.getCORPTYPE() == 0)
			{
				sql = "select SPGATE from XT_GATE_QUEUE "+StaticValue.getWITHNOLOCK()+" where  SPGATE in (select SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" where USERID in (select SPUSER from LF_SP_DEP_BIND "+StaticValue.getWITHNOLOCK()+" where CORP_CODE='" + corpCode + "')) or SPGATE like '200%'";
				return sql;
			}

			//多企业
			//100000企业则查全部
			if("100000".equals(corpCode))
			{
				sql = "select distinct(SPGATE) SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK();
			}
			//其他企业按企业编码查
			else
			{
				sql = "select distinct(SPGATE) SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" where USERID in (select SPUSER from LF_SP_DEP_BIND "+StaticValue.getWITHNOLOCK()+" where CORP_CODE='" + corpCode + "')";
			}
			return sql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，下行记录，获取通道号查询sql，异常。");
			return null;
		}
	}

	/**
	 *
	 * @description 系统下行记录获取错误代码说明
	 * @param errCodes 错误码，格式为：code1,code2,code3.....
	 * @param corpCode 企业编码
	 * @return 错误代码说明动态bean集合
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-19 上午09:02:50
	 */
	public List<DynaBean> getErrCodeDis(String errCodes, String corpCode) {
		if(errCodes == null || errCodes.length() < 1)
		{
			EmpExecutionContext.error("DAO查询，系统下行记录获取错误代码说明，传入的错误代码为空。");
			return null;
		}

		String sql = "select state_code, state_des from lf_statecode where state_code in (" + errCodes + ") and CORP_CODE='"+corpCode+"'";

		List<DynaBean> beanList = getListDynaBeanBySql(sql);
		return beanList;
	}

    public List<SendDetailMttaskVo> findAllSendMttask(SendDetailMttaskVo detailMttaskVo,PageInfo pageInfo) throws Exception {

		List<SendDetailMttaskVo> sendDetailMttaskVos = new ArrayList<SendDetailMttaskVo>();

		String fieldSql = MtRecordDAOSql.getAllSendMttaskFieldSql();

		String tableSql = MtRecordDAOSql.getAllSendMttaskTableSql(detailMttaskVo);

		String connditionSql = MtRecordDAOSql.getAllSendMttaskConnditionSql(detailMttaskVo);

		String sql = fieldSql + tableSql + connditionSql;

		String countSql = "select count(*) totalcount" + " from (" + sql + ") totalcount";

		//调用 查询方法
		IGenericDAO dao = new DataAccessDriver().getGenericDAO();
		if(pageInfo != null) {
			sendDetailMttaskVos = dao.findPageVoListBySQLNoCount(
					SendDetailMttaskVo.class, sql, countSql, pageInfo,
					StaticValue.EMP_POOLNAME);
		}else {
			sendDetailMttaskVos = findVoListBySQL(SendDetailMttaskVo.class,sql,StaticValue.EMP_POOLNAME);
		}
		return sendDetailMttaskVos;
	}

    public List<ReplyDetailVo> findAllReplyDetail(ReplyDetailVo replyDetailVo, PageInfo pageInfo) throws Exception {
		List<ReplyDetailVo> replyDetailVos = new ArrayList<ReplyDetailVo>();

		String fieldSql = MoRecordDAOSql.getAllReplyDetailFieldSql();

		//根据taskId查询下行表，获得通道号与子号
		String spNumber = getSpNumberByTaskId(replyDetailVo);
		replyDetailVo.setSpNumber(spNumber);

		String tableSql = MoRecordDAOSql.getAllReplyDetailTableSql(replyDetailVo);

		String connditionSql = MoRecordDAOSql.getAllReplyDetailConnditionSql(replyDetailVo);

		String sql = "select * from(" + fieldSql + tableSql + ") a2" + connditionSql;

		String countSql = "select count(*) totalcount" + " from (" + sql + ") totalcount";

		//调用 查询方法
		IGenericDAO dao = new DataAccessDriver().getGenericDAO();
		if(pageInfo != null){
			replyDetailVos = dao.findPageVoListBySQLNoCount(
					ReplyDetailVo.class, sql, countSql, pageInfo,
					StaticValue.EMP_POOLNAME);
		}else {
			replyDetailVos = findVoListBySQL(ReplyDetailVo.class,sql,StaticValue.EMP_POOLNAME);
		}
		return replyDetailVos;
    }

	/**
	 * 根据TaskId查询下行表获取通道号与子号
	 * @param replyDetailVo
	 * @return
	 */
	private static String getSpNumberByTaskId(ReplyDetailVo replyDetailVo) {
		String spGate = "";
		String subNo = "";
		String fieldSql = MoRecordDAOSql.getSpNumberFieldSql();
		String tableSql = MoRecordDAOSql.getSpNumberTableSql(replyDetailVo);
		String sql = fieldSql + tableSql;
		IGenericDAO dao = new DataAccessDriver().getGenericDAO();
		List<DynaBean> dynaBeanBySql = dao.findDynaBeanBySql(sql);
		if(dynaBeanBySql != null && dynaBeanBySql.size() > 0){
			DynaBean bean = dynaBeanBySql.get(0);
			spGate = (String) bean.get("spgate");
			subNo = (String) bean.get("cpno");
		}
		return spGate + subNo;
	}
}
