package com.montnets.emp.jfcx.dao;

import java.util.Calendar;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.jfcx.vo.CrmRfMgrVo;
import com.montnets.emp.table.ydyw.TableLfDeductionsList;
import com.montnets.emp.util.PageInfo;

/**
 * 客户退费管理报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 下午07:19:58
 * @description
 */
public class GenericCrmRfMgrVoDAO extends SuperDAO {

	/**
	 * 根据当前操作员机构ID查询机构报表信息
	 * @param curUserId
	 * @param depid
	 * @param crmrfmgrvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<CrmRfMgrVo> getCrmRfMgrVoList(Long curUserId,String depid,  CrmRfMgrVo crmrfmgrvo,String corpCode, PageInfo pageInfo) throws Exception {
		String sql = this.getCrmRfMgrSql(curUserId,depid, crmrfmgrvo, corpCode);
		//加上排序
		String dataSql = sql + " order by " + TableLfDeductionsList.ID+ " desc ";
		//总条数语句
		String countSql = "select count(*) totalcount FROM (";
		countSql += sql;
		countSql += " ) A";
		List<CrmRfMgrVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				CrmRfMgrVo.class, dataSql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}
	

	/**
	 * 
	 * 根据企业编码，或者对应的操作员统计报表信息 （如果企业编码为空，则显示所有）(不带分页的查询）
	 * 
	 * @param curUserId
	 *            当期登录的操作员ID
	 * 
	 * @param userIds
	 *            管辖范围内的操作员ID
	 * 
	 * @param depIds
	 *            所管辖的机构ids
	 * @param crmrfmgrvo
	 * 
	 * @param corpCode
	 *            企业编码
	 * 
	 * @return
	 * 
	 * 
	 * @throws Exception
	 */
	public List<CrmRfMgrVo> getCrmRfMgrVoDepUnPage(Long curUserId,String depIds, CrmRfMgrVo crmrfmgrvo,String corpCode) throws Exception {

		// String sql = this.getSql(curUserId, depIds, userIds, mtDataReportVo,
		// 获取操作员报表用的sql语句
		String sql= this.getCrmRfMgrSql(curUserId,depIds, crmrfmgrvo, corpCode);
		String dataSql = sql + " order by " + TableLfDeductionsList.ID
				+ " desc ";
		//分页语句
		String countSql = "select count(*) totalcount FROM (";
		countSql += sql;
		countSql += " ) A";

		List<CrmRfMgrVo> returnList = findVoListBySQL(CrmRfMgrVo.class,
				dataSql, StaticValue.EMP_POOLNAME);

		return returnList;
	}

	/**
	 * 
	 * 个人权限用
	 * 
	 * @param curUserId
	 * @param crmrfmgrvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<CrmRfMgrVo> getCrmRfMgrVoPersonal(Long curUserId,
			CrmRfMgrVo crmrfmgrvo, String corpCode, PageInfo pageInfo)
			throws Exception {
		return this.getCrmRfMgrVoList(curUserId, null,crmrfmgrvo, corpCode, pageInfo);
	}

	/**
	 * 获取客户计费的SQL语句
	 * 
	 * @param depId
	 * @param crmrfmgrvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getCrmRfMgrSql(Long curUserId,String depId, CrmRfMgrVo crmRfMgrVo,String corpCode) throws Exception {
		StringBuffer sql = new StringBuffer("SELECT L.MOBILE," +
													"L.ID," +
													"L.CUSTOM_NAME," +
													"L.CONTRACT_ID," +
													"L.ACCT_NO," +
													"L.DEBITACCOUNT," +
													"L.TAOCAN_TYPE," +
													"L.TAOCAN_NAME," +
													"L.TAOCAN_CODE," +
													"L.TAOCAN_MONEY," +
													"L.DEDUCTIONS_TYPE," +
													"L.CONTRACT_STATE," +
													"P.DEP_ID," +
													"P.DEP_NAME," +
													"L.UDPATE_TIME," +
													"L.BUCKLEFEE_TIME," +
													"L.BUCKLEUPFEE_TIME," +
													"L.BUPSUM_MONEY," +
													"S.USER_NAME," +
													"S.NAME "+
													"FROM LF_DEDUCTIONS_LIST L " +
													"LEFT JOIN LF_SYSUSER S " +
													"ON L.USER_ID = S.USER_ID " +
													"LEFT JOIN LF_DEP P " +
													"ON L.DEP_ID = P.DEP_ID " +
													" WHERE L.CORP_CODE = '"+corpCode+"' ");
		//判断条件对象是否为空
		if(crmRfMgrVo==null)
		{
			crmRfMgrVo = new CrmRfMgrVo();
		}
		//手机号
		if(crmRfMgrVo.getMobile()!=null&&!"".equals(crmRfMgrVo.getMobile())){
			sql.append(" AND L.MOBILE LIKE '%").append(crmRfMgrVo.getMobile().trim()).append("%' ");
		}
		//姓名
		if(crmRfMgrVo.getCustomname()!=null&&!"".equals(crmRfMgrVo.getCustomname())){
			sql.append(" AND CUSTOM_NAME LIKE '%").append(crmRfMgrVo.getCustomname().trim()).append("%' ");
		}
		//签约账号
		if(crmRfMgrVo.getAcctno()!=null&&!"".equals(crmRfMgrVo.getAcctno())){
			sql.append(" AND ACCT_NO LIKE '%").append(crmRfMgrVo.getAcctno().trim()).append("%' ");
		}
		//套餐计费类型
		if(crmRfMgrVo.getTaocantype()!=null){
			sql.append(" AND TAOCAN_TYPE = ").append(crmRfMgrVo.getTaocantype());
		}
		//签约状态
		if(crmRfMgrVo.getContractstate()!=null){
			sql.append(" AND CONTRACT_STATE = ").append(crmRfMgrVo.getContractstate());
		}
		//扣费状态
		if(crmRfMgrVo.getDeductionstype()!=null)
		{
			sql.append(" AND DEDUCTIONS_TYPE = ").append(crmRfMgrVo.getDeductionstype());
		}
		//扣费账号
		if(crmRfMgrVo.getDebitaccount()!=null&&!"".equals(crmRfMgrVo.getDebitaccount())){
			sql.append(" AND DEBITACCOUNT LIKE '%").append(crmRfMgrVo.getDebitaccount().trim()).append("%' ");
		}
		//签约套餐
		if(crmRfMgrVo.getTaocanname()!=null&&!"".equals(crmRfMgrVo.getTaocanname())){
			sql.append(" AND TAOCAN_NAME LIKE '%").append(crmRfMgrVo.getTaocanname().trim()).append("%' ");
		}
		//操作员
		if(crmRfMgrVo.getUsername()!=null&&!"".equals(crmRfMgrVo.getUsername())){
			sql.append(" AND NAME LIKE '%").append(crmRfMgrVo.getUsername().trim()).append("%' ");
		}
		//机构
		if(depId != null && !"".equals(depId))
		{
			if(crmRfMgrVo.getIsContainsSun() == null || (crmRfMgrVo.getIsContainsSun() != null && !"".equals(crmRfMgrVo.getIsContainsSun()) && "1".equals(crmRfMgrVo.getIsContainsSun())))
			{
				// 根据机构组装下级机构
				LfDep lfDep = new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(depId));
				if(lfDep != null && lfDep.getDepLevel().intValue() == 1)
				{
					crmRfMgrVo.setDepids("");
					crmRfMgrVo.setCorpcode(lfDep.getCorpCode());
				}
				else
				{
					String depid = new DepDAO().getChildUserDepByParentID(Long.parseLong(depId),TableLfDeductionsList.CONTRACT_DEP);
					crmRfMgrVo.setDepids(depid);
				}
			}
			// 不包含子机构
			else
			{
				String depIdCondition = TableLfDeductionsList.CONTRACT_DEP + "=" + depId;
				crmRfMgrVo.setDepids(depIdCondition);
			}
		}
		else
		{
			//查询个人权限
			String userIdCondition = "L."+TableLfDeductionsList.CONTRACT_USER + "=" + curUserId;
			crmRfMgrVo.setDepids(userIdCondition);
		}
		//机构权限查询
		if(crmRfMgrVo.getDepids()!=null&&!"".equals(crmRfMgrVo.getDepids()))
		{
			sql.append(" AND ").append(crmRfMgrVo.getDepids());
		}
		//扣费时间
		if(crmRfMgrVo.getSendTime()!=null&&!"".equals(crmRfMgrVo.getSendTime()))
		{
			sql.append(" AND BUCKLEFEE_TIME >= ").append(new DataAccessDriver().getGenericDAO().getTimeCondition(crmRfMgrVo.getSendTime()));
		}
		if(crmRfMgrVo.getEndTime()!=null&&!"".equals(crmRfMgrVo.getEndTime()))
		{
			sql.append(" AND BUCKLEFEE_TIME <= ").append(new DataAccessDriver().getGenericDAO().getTimeCondition(crmRfMgrVo.getEndTime()));
		}
		String totalsql=sql.toString();
		return totalsql;
	}
	
	/**
	 * 
	 * 获取当前的年份和月份的数组，用于限制查询当前月份的操作员报表
	 * 
	 * @return
	 */
	public String[] getYearAndMonth() {
		//时间和月份的数组
		String[] datetime = new String[2];

		try {
			//定义一个系统时间对象
			Calendar cal = Calendar.getInstance();
			//获取月份
			Integer month = cal.get(Calendar.MONTH);
			month += 1;
			datetime[0] = String.valueOf(cal.get(Calendar.YEAR));
			datetime[1] = String.valueOf(month);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"客户退费管理报表获取当前年份和月份数组异常");
		}

		return datetime;
	}

}
