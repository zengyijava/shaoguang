package com.montnets.emp.jfcx.dao;

import java.util.Calendar;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.jfcx.vo.CrmBillQueryVo;
import com.montnets.emp.table.ydyw.TableLfDeductionsDisp;
import com.montnets.emp.util.PageInfo;

/**
 * 客户计费查询报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 下午07:19:58
 * @description
 */
public class GenericCrmBillQueryVoDAO extends SuperDAO{

	/**
	 * 根据当前操作员机构ID查询机构报表信息
	 * @param curUserId
	 * @param depid
	 * @param crmbillqueryvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<CrmBillQueryVo> getCrmBillQueryVoList(Long curUserId,String depid,  CrmBillQueryVo crmbillqueryvo,String corpCode, PageInfo pageInfo) throws Exception {
		// 获取操作员报表用的sql语句
		String sql="";
		//判断短彩类型获取不同的查询语句
		sql= this.getCrmBillQuerySql(curUserId,depid, crmbillqueryvo, corpCode);
		//加上排序
		String dataSql = sql + " order by " + TableLfDeductionsDisp.ID+ " desc ";
		//总条数语句
		String countSql = "select count(*) totalcount FROM (";
		countSql += sql;
		countSql += " ) A";
		List<CrmBillQueryVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				CrmBillQueryVo.class, dataSql, countSql, pageInfo,
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
	 * @param crmbillqueryvo
	 * 
	 * @param corpCode
	 *            企业编码
	 * 
	 * @return
	 * 
	 * 
	 * @throws Exception
	 */
	public List<CrmBillQueryVo> getCrmBillQueryVoDepUnPage(Long curUserId,String depIds, CrmBillQueryVo crmbillqueryvo,String corpCode) throws Exception {

		// String sql = this.getSql(curUserId, depIds, userIds, mtDataReportVo,
		// 获取操作员报表用的sql语句
		String sql= this.getCrmBillQuerySql(curUserId,depIds, crmbillqueryvo, corpCode);
		String dataSql = sql + " order by " + TableLfDeductionsDisp.ID
				+ " desc ";
		//分页语句
		String countSql = "select count(*) totalcount FROM (";
		countSql += sql;
		countSql += " ) A";
		List<CrmBillQueryVo> returnList = findVoListBySQL(CrmBillQueryVo.class,
				dataSql, StaticValue.EMP_POOLNAME);

		return returnList;
	}

	/**
	 * 
	 * 个人权限用
	 * 
	 * @param curUserId
	 * @param crmbillqueryvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<CrmBillQueryVo> getCrmBillQueryVoPersonal(Long curUserId,
			CrmBillQueryVo crmbillqueryvo, String corpCode, PageInfo pageInfo)
			throws Exception {
		return this.getCrmBillQueryVoList(curUserId, null,crmbillqueryvo, corpCode, pageInfo);
	}

	/**
	 * 获取客户计费的SQL语句
	 * 
	 * @param depId
	 * @param crmbillqueryvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getCrmBillQuerySql(Long curUserId,String depId, CrmBillQueryVo crmbillqueryvo,String corpCode) throws Exception {
		StringBuffer sql = new StringBuffer("SELECT D.MOBILE," +
													"D.CUSTOM_NAME," +
													"D.CONTRACT_ID," +
													"D.ACCT_NO," +
													"D.DEBITACCOUNT," +
													"D.TAOCAN_NAME," +
													"D.TAOCAN_TYPE," +
													"D.TAOCAN_CODE," +
													"D.CONTRACT_STATE," +
													"D.DEDUCTIONS_MONEY," +
													"D.TAOCAN_MONEY," +
													"D.OPR_TIME," +
													"D.OPR_STATE," +
													"P.DEP_NAME " +
													"FROM LF_DEDUCTIONS_DISP D LEFT JOIN LF_DEP P " +
													"ON D.CONTRACT_DEP = P.DEP_ID WHERE");
		//判断条件对象是否为空
		if(crmbillqueryvo==null)
		{
			crmbillqueryvo=new CrmBillQueryVo();
		}
		//扣费状态
		if(crmbillqueryvo.getOprstate()!=null)
		{
			sql.append(" OPR_STATE = ").append(crmbillqueryvo.getOprstate());
		}
		else
		{
			sql.append(" OPR_STATE in (0,1,2,-1) ");
		}
		//手机号
		if(crmbillqueryvo.getMobile()!=null&&!"".equals(crmbillqueryvo.getMobile())){
			sql.append(" AND MOBILE LIKE '%").append(crmbillqueryvo.getMobile().trim()).append("%' ");
		}
		
		//姓名
		if(crmbillqueryvo.getCustomname()!=null&&!"".equals(crmbillqueryvo.getCustomname())){
			sql.append(" AND CUSTOM_NAME LIKE '%").append(crmbillqueryvo.getCustomname().trim()).append("%' ");
		}
		
		//签约账号
		if(crmbillqueryvo.getAcctno()!=null&&!"".equals(crmbillqueryvo.getAcctno())){
			sql.append(" AND ACCT_NO LIKE '%").append(crmbillqueryvo.getAcctno().trim()).append("%' ");
		}
		
		//套餐计费类型
		if(crmbillqueryvo.getTaocantype()!=null){
			sql.append(" AND TAOCAN_TYPE = ").append(crmbillqueryvo.getTaocantype());
		}
		
		//签约状态
		if(crmbillqueryvo.getContractstate()!=null){
			sql.append(" AND CONTRACT_STATE = ").append(crmbillqueryvo.getContractstate());
		}
		
		//扣费账号
		if(crmbillqueryvo.getDebitaccount()!=null&&!"".equals(crmbillqueryvo.getDebitaccount())){
			sql.append(" AND DEBITACCOUNT LIKE '%").append(crmbillqueryvo.getDebitaccount().trim()).append("%' ");
		}
		//签约套餐
		if(crmbillqueryvo.getTaocanname()!=null&&!"".equals(crmbillqueryvo.getTaocanname())){
			sql.append(" AND TAOCAN_NAME LIKE '%").append(crmbillqueryvo.getTaocanname().trim()).append("%' ");
		}
		//机构
		if(depId != null && !"".equals(depId))
		{
			if(crmbillqueryvo.getIsContainsSun() == null || (crmbillqueryvo.getIsContainsSun() != null && !"".equals(crmbillqueryvo.getIsContainsSun()) && "1".equals(crmbillqueryvo.getIsContainsSun())))
			{
				// 根据机构组装下级机构
				LfDep lfDep = new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(depId));
				if(lfDep != null && lfDep.getDepLevel().intValue() == 1)
				{
					crmbillqueryvo.setDepids("");
					crmbillqueryvo.setCorpcode(lfDep.getCorpCode());
				}
				else
				{
					String depid = new DepDAO().getChildUserDepByParentID(Long.parseLong(depId), TableLfDeductionsDisp.CONTRACT_DEP);
					crmbillqueryvo.setDepids(depid);
				}
			}
			// 不包含子机构
			else
			{
				String depIdCondition = TableLfDeductionsDisp.CONTRACT_DEP + "=" + depId;
				crmbillqueryvo.setDepids(depIdCondition);
			}
		}
		//查询个人权限
		else
		{
			String userIdCondition = TableLfDeductionsDisp.CONTRACT_USER + "=" + curUserId;
			crmbillqueryvo.setDepids(userIdCondition);
		}
		//机构
		if(crmbillqueryvo.getDepids()!=null&&!"".equals(crmbillqueryvo.getDepids()))
		{
			sql.append(" AND ").append(crmbillqueryvo.getDepids());
		}
		
		//扣费时间
		if(crmbillqueryvo.getSendTime()!=null&&!"".equals(crmbillqueryvo.getSendTime()))
		{
			sql.append(" AND OPR_TIME >= ").append(new DataAccessDriver().getGenericDAO().getTimeCondition(crmbillqueryvo.getSendTime()));
		}
		if(crmbillqueryvo.getEndTime()!=null&&!"".equals(crmbillqueryvo.getEndTime()))
		{
			sql.append(" AND OPR_TIME <= ").append(new DataAccessDriver().getGenericDAO().getTimeCondition(crmbillqueryvo.getEndTime()));
		}
		
		String totalsql=sql.toString();
		return totalsql;
	}
	
	
	
//	private String getTaocanCodeByTypeAndName(String taocantype,String taocanname){
//		
//		String sql="SELECT "
//		
//	}
	
	
	
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
			EmpExecutionContext.error(e,"客户计费查询报表获取当前年份和月份数组异常");
		}

		return datetime;
	}

}
