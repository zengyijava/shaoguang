package com.montnets.emp.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.report.vo.ChargeStatsRptVo;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.util.PageInfo;

/**
 * 业务套餐统计报表
 * 
 * @project p_ydyw
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-19 上午11:54:35
 * @description
 */
public class GenericChargeStatsRptVoDAO extends SuperDAO
{
	
	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 根据查询条件查询业务套餐统计
	 * 
	 * @param cstatsrptvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<ChargeStatsRptVo> getChargeStatsRptVoList(ChargeStatsRptVo cstatsrptvo, String corpCode, PageInfo pageInfo) throws Exception
	{
		// 获取操作员报表用的sql语句
		String sql = "";
		// 判断短彩类型获取不同的查询语句
 		sql = this.getChargeStatsRptSql(cstatsrptvo, corpCode);
		// 加上排序
		String dataSql = sql + " order by tot.taocan_name desc ";
		// 总条数语句
		String countSql = "select count(*) totalcount FROM (";
		countSql += sql;
		countSql += " ) A";
		List<DynaBean> dynList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(dataSql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		List<ChargeStatsRptVo> returnList = fillChargeStatsRptVoList(dynList, cstatsrptvo, corpCode);
		//Collections.sort(returnList);
		return returnList;
	}

	/**
	 * 不分页 查询 用于导出
	 * 
	 * @param charstatvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<ChargeStatsRptVo> getChargeStatsRptVoUnPage(ChargeStatsRptVo charstatvo, String corpCode) throws Exception
	{
		// 获取操作员报表用的sql语句
		String sql = this.getChargeStatsRptSql(charstatvo, corpCode);
		sql = sql + " ORDER BY tot.TAOCAN_NAME DESC ";
		List<DynaBean> dynList = getListDynaBeanBySql(sql);
		// 填充vo
		List<ChargeStatsRptVo> returnList = fillChargeStatsRptVoList(dynList, charstatvo, corpCode);
		//Collections.sort(returnList);
		return returnList;
	}
	
	
	/**
	 * 合计
	 * @param curUserId
	 * @param depid
	 * @param dbqvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public Long findSumCount(ChargeStatsRptVo dbqv, String corpCode) throws Exception
	{
		String sql = this.getChargeStatsRptSql(dbqv, corpCode);
		List<DynaBean> dynList = getListDynaBeanBySql(sql);
		Long sumtotalmoney = fillTotalMoneyList(dynList, dbqv,corpCode);
		return sumtotalmoney;
	}
	
	/**
	 * 填充合计数量
	 * @param dynList
	 * @param dbqvs
	 * @param corpcode
	 * @return
	 * @throws Exception
	 */
	public Long fillTotalMoneyList(List<DynaBean> dynList, ChargeStatsRptVo dbqvs,String corpcode) throws Exception
	{
		Long sumtotalmoney=0l;
		// 循环填充值
		if(dynList != null && dynList.size() > 0)
		{
			for (int i = 0; i < dynList.size(); i++)
			{
				DynaBean dynb = dynList.get(i);
				if(dynb != null)
				{
					// 数据类型 机构 操作员 未知
					String udtype = dynb.get("udtype") != null ? dynb.get("udtype").toString() : "";
					// 套餐编码
					String taocancode = dynb.get("taocan_code") != null ? dynb.get("taocan_code").toString() : "";
					// 套餐资费
					String strtaocanmoney = dynb.get("taocan_money") != null ? dynb.get("taocan_money").toString() : "";
					Long taocanmoney=strtaocanmoney != null && !"".equals(strtaocanmoney) ? Long.parseLong(strtaocanmoney) : 0l;
					//总收入
					Long totalmoney=0l;
					if(!"".equals(taocancode))
					{
						if(udtype != null && "0".equals(udtype))
						{
							// 业务套餐数据填充
							// 扣费成功数
							Long deductioncount = statisticsCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 1, corpcode);
							// 扣费总金额
							Long deductiontotalcount = deductioncount != null ? deductioncount * taocanmoney : 0l;
							// 退费总金额
							Long backmoney = statisticsCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 3, corpcode);
							if(deductiontotalcount > backmoney)
							{
								totalmoney = deductiontotalcount - backmoney;
							}

						}
						else if(udtype != null && "1".equals(udtype))
						{
							// 未知
							//扣费成功数
							Long deductioncount=statisticsUnKnownCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 1, corpcode);
							//扣费总金额
							Long deductiontotalcount=deductioncount!=null?deductioncount*taocanmoney:0l;
							//退费总金额
							Long backmoney=statisticsUnKnownCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 3, corpcode);
							//总收入
							if(deductiontotalcount>backmoney){
								totalmoney=deductiontotalcount-backmoney;
							}
						}
					}
					else
					{
						//总收入
						totalmoney=0l;
					}
					sumtotalmoney=sumtotalmoney+totalmoney;
				}
			}
		}
		return sumtotalmoney;
	}
	
	

	/**
	 * 填充业务套餐统计报表
	 * 
	 * @param dynList
	 * @param dbqvs
	 * @param corpcode
	 * @return
	 * @throws Exception
	 */
	public List<ChargeStatsRptVo> fillChargeStatsRptVoList(List<DynaBean> dynList, ChargeStatsRptVo dbqvs, String corpcode) throws Exception
	{
		List<ChargeStatsRptVo> returnList = new ArrayList<ChargeStatsRptVo>();
		// 循环填充值
		if(dynList != null && dynList.size() > 0)
		{
			for (int i = 0; i < dynList.size(); i++)
			{
				DynaBean dynb = dynList.get(i);
				if(dynb != null)
				{
					// 业务套餐统计对象
					ChargeStatsRptVo dbqv = new ChargeStatsRptVo();
					// 数据类型 机构 操作员 未知
					String udtype = dynb.get("udtype") != null ? dynb.get("udtype").toString() : "";
					// 套餐编码
					String taocancode = dynb.get("taocan_code") != null ? dynb.get("taocan_code").toString() : "";
					// 套餐名称
					String taocanname = dynb.get("taocan_name") != null ? dynb.get("taocan_name").toString() : "";
					// 套餐类型 包年包月
					String taocantype = dynb.get("taocan_type") != null ? dynb.get("taocan_type").toString() : "";
					// 套餐资费
					String taocanmoney = dynb.get("taocan_money") != null ? dynb.get("taocan_money").toString() : "";
					// 套餐编码
					dbqv.setTaocancode(taocancode);
					// 套餐名称
					dbqv.setTaocanname(taocanname);
					// 套餐类型
					dbqv.setTaocantype(taocantype != null && !"".equals(taocantype) ? Integer.parseInt(taocantype) : null);
					// 套餐资费
					dbqv.setTaocanmoney(taocanmoney != null && !"".equals(taocanmoney) ? Long.parseLong(taocanmoney) : 0l);
					//数据类型  0 套餐  1未知
					dbqv.setUdtype(udtype);
					if(!"".equals(taocancode))
					{
						if(udtype != null && "0".equals(udtype))
						{
							// 签约人数
							Long contractcount = statisticsCount(taocancode,dbqvs.getY(), dbqvs.getImonth(), 0, corpcode);
							dbqv.setContractcount(contractcount);
							// 扣费成功数
							Long deductioncount = statisticsCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 1, corpcode);
							dbqv.setDeductioncount(deductioncount);
							// 扣费失败数
							Long deductionfailcount = statisticsCount(taocancode,  dbqvs.getY(), dbqvs.getImonth(), 2, corpcode);
							dbqv.setDeductionfailcount(deductionfailcount);
							// 扣费总金额
							dbqv.setDeductiontotalcount(dbqv.getDeductioncount() != null ? dbqv.getDeductioncount() * dbqv.getTaocanmoney() : 0l);
							// 退费总金额
							Long backmoney = statisticsCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 3, corpcode);
							dbqv.setBackmoney(backmoney);
							// 总收入
							if(dbqv.getDeductiontotalcount() > dbqv.getBackmoney())
							{
								dbqv.setTotalmoney(dbqv.getDeductiontotalcount() - dbqv.getBackmoney());
							}
							else
							{
								dbqv.setTotalmoney(0l);
							}

						}
						else if(udtype != null && "1".equals(udtype))
						{
							// 未知
							// 签约人数
							Long contractcount = statisticsUnKnownCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 0, corpcode);
							dbqv.setContractcount(contractcount);
							// 扣费成功数
							Long deductioncount = statisticsUnKnownCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 1, corpcode);
							dbqv.setDeductioncount(deductioncount);
							// 扣费失败数
							Long deductionfailcount = statisticsUnKnownCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 2, corpcode);
							dbqv.setDeductionfailcount(deductionfailcount);
							// 扣费总金额
							dbqv.setDeductiontotalcount(dbqv.getDeductioncount() != null ? dbqv.getDeductioncount() * dbqv.getTaocanmoney() : 0l);
							// 退费总金额
							Long backmoney = statisticsUnKnownCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 3, corpcode);
							dbqv.setBackmoney(backmoney);
							// 总收入
							if(dbqv.getDeductiontotalcount() > dbqv.getBackmoney())
							{
								dbqv.setTotalmoney(dbqv.getDeductiontotalcount() - dbqv.getBackmoney());
							}
							else
							{
								dbqv.setTotalmoney(0l);
							}
						}
					}
					else
					{
						// 签约人数
						dbqv.setContractcount(0l);
						// 扣费金额
						dbqv.setDeductioncount(0l);
						// 扣费失败金额
						dbqv.setDeductionfailcount(0l);
						//扣费总金额
						dbqv.setDeductiontotalcount(0l);
						// 退费金额
						dbqv.setBackmoney(0l);
						// 总收入
						dbqv.setTotalmoney(0l);
					}
					returnList.add(dbqv);
				}
			}
		}
		return returnList;
	}

	/**
	 * 业务套餐统计计数 套餐编码计数
	 * 
	 * @param taocancode
	 * @param year
	 * @param month
	 * @param optype
	 * @param corpcode
	 * @return
	 * @throws Exception
	 */
	public Long statisticsCount(String taocancode, String year, String month, Integer optype, String corpcode) throws Exception
	{
		if(taocancode == null || "".equals(taocancode) || optype == null)
		{
			return 0l;
		}
		long result = 0l;
		String sql = "";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			// 签约人数
			if(optype == 0)
			{
				sql = "SELECT COUNT(ID) COUNT FROM LF_CONTRACT_TAOCAN WHERE TAOCAN_CODE='" + taocancode + "' "
				+" AND (IS_VALID='0' OR (IS_VALID IN ('1','2') ";
				//获取月份的天数
				Calendar time = Calendar.getInstance();
				time.set(Calendar.YEAR, Integer.parseInt(year)); // year年
				// 月
				if(month != null && !"".equals(month))
				{
					time.set(Calendar.MONTH, Integer.parseInt(month) - 1);// Calendar对象默认一月为0,month月
				}else{
					time.set(Calendar.MONTH, 11);// Calendar对象默认一月为0,month月
				}
				time.set(Calendar.DAY_OF_MONTH,1);//
				time.set(Calendar.HOUR_OF_DAY, 0);
				time.set(Calendar.MINUTE, 0);
				time.set(Calendar.SECOND, 0);
				IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
				sql =sql+ " AND UPDATE_TIME>="+genericDao.getTimeCondition(df.format(time.getTime()));
				time.set(Calendar.DAY_OF_MONTH, time.getActualMaximum(Calendar.DAY_OF_MONTH));
				sql=sql+")) AND CREATE_TIME<="+genericDao.getTimeCondition(df.format(time.getTime()))+" ";
				// 企业编码
				if(corpcode != null && !"".equals(corpcode) && !"0".equals(corpcode))
				{
					sql = sql + " AND CORP_CODE='" + corpcode + "' ";
				}
			}
			else if(optype == 1 || optype == 2)
			{
				// 扣费成功数
				sql = "SELECT COUNT(ID) COUNT FROM LF_DEDUCTIONS_LIST WHERE TAOCAN_CODE='" + taocancode + "' ";

				// 扣费成功数条件
				if(optype == 1)
				{
					sql = sql + " AND DEDUCTIONS_TYPE IN (1,3,4,5) ";
				}
				else if(optype == 2)
				{
					// 扣费失败数条件
					sql = sql + " AND DEDUCTIONS_TYPE=2 ";
				}
				else
				{
					return 0l;
				}

				// 年
				if(year != null && !"".equals(year))
				{
					sql = sql + " AND IYEAR=" + year.trim() + " ";
				}

				// 月
				if(month != null && !"".equals(month))
				{
					sql = sql + " AND IMONTH=" + month.trim() + " ";
				}

				// 企业编码
				if(corpcode != null && !"".equals(corpcode) && !"0".equals(corpcode))
				{
					sql = sql + " AND CORP_CODE='" + corpcode + "' ";
				}
			}
			else if(optype == 3)
			{
				// 退费金额
				sql = "SELECT SUM(BUPSUM_MONEY) COUNT FROM LF_DEDUCTIONS_LIST WHERE TAOCAN_CODE='" + taocancode + "'   AND DEDUCTIONS_TYPE=3 ";

				// 年
				if(year != null && !"".equals(year))
				{
					sql = sql + " AND IYEAR=" + year.trim() + " ";
				}

				// 月
				if(month != null && !"".equals(month))
				{
					sql = sql + " AND IMONTH=" + month.trim() + " ";
				}

				// 企业编码
				if(corpcode != null && !"".equals(corpcode) && !"0".equals(corpcode))
				{
					sql = sql + " AND CORP_CODE='" + corpcode + "' ";
				}
			}
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next())
			{
				result = rs.getLong("COUNT");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "业务统计报表计数查询异常");
			throw e;
		}
		finally
		{
			close(rs, ps, conn);
		}
		return result;
	}

	
	/**
	 * 未知统计数查询
	 * 
	 * @param userid
	 * @param taocancode
	 * @param optype
	 * @return
	 * @throws Exception
	 */
	public Long statisticsUnKnownCount(String taocancode, String year, String month, Integer optype, String corpcode) throws Exception
	{
		if(taocancode == null || "".equals(taocancode) || optype == null)
		{
			return 0l;
		}
		long result = 0l;
		String sql = "";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			// 签约人数
			if(optype == 0)
			{
				return 0l;
			}
			else if(optype == 1 || optype == 2)
			{
				// 扣费成功数 扣费失败数
				// mySql
				if(StaticValue.DBTYPE == 3)
				{
					sql = "SELECT COUNT(LBT.ID) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE COALESCE(SU.USER_ID,0)=0 AND TAOCAN_CODE='" + taocancode + "' ";
				}
				else if(StaticValue.DBTYPE == 4)
				{
					// DB2
					sql = "SELECT COUNT(LBT.ID) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE COALESCE(SU.USER_ID,0)=0 AND TAOCAN_CODE='" + taocancode + "' ";
				}
				else
				{
					// 其他数据库
					sql = "SELECT COUNT(LBT.ID) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE SU.USER_ID IS NULL AND TAOCAN_CODE='" + taocancode + "' ";
				}

				// 扣费成功数条件
				if(optype == 1)
				{
					sql = sql + " AND DEDUCTIONS_TYPE IN (1,3,4,5) ";
				}
				else if(optype == 2)
				{
					// 扣费失败数条件
					sql = sql + " AND DEDUCTIONS_TYPE=2 ";
				}
				else
				{
					return 0l;
				}

				// 年
				if(year != null && !"".equals(year))
				{
					sql = sql + " AND LBT.IYEAR=" + year.trim() + " ";
				}

				// 月
				if(month != null && !"".equals(month))
				{
					sql = sql + " AND LBT.IMONTH=" + month.trim() + " ";
				}

				// 企业编码
				if(corpcode != null && !"".equals(corpcode) && !"0".equals(corpcode))
				{
					sql = sql + " AND LBT.CORP_CODE='" + corpcode + "' ";
				}
			}
			else if(optype == 3)
			{
				// 退费金额
				// mySql
				if(StaticValue.DBTYPE == 3)
				{
					sql = "SELECT SUM(LBT.BUPSUM_MONEY) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE COALESCE(SU.USER_ID,0)=0 AND TAOCAN_CODE='" + taocancode + "'   AND DEDUCTIONS_TYPE=3 ";
				}
				else if(StaticValue.DBTYPE == 4)
				{
					// DB2
					sql = "SELECT SUM(LBT.BUPSUM_MONEY) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE COALESCE(SU.USER_ID,0)=0 AND TAOCAN_CODE='" + taocancode + "'   AND DEDUCTIONS_TYPE=3 ";
				}
				else
				{
					// 其他数据库
					sql = "SELECT SUM(LBT.BUPSUM_MONEY) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE SU.USER_ID IS NULL AND TAOCAN_CODE='" + taocancode + "'   AND DEDUCTIONS_TYPE=3 ";
				}

				// 年
				if(year != null && !"".equals(year))
				{
					sql = sql + " AND LBT.IYEAR=" + year.trim() + " ";
				}

				// 月
				if(month != null && !"".equals(month))
				{
					sql = sql + " AND LBT.IMONTH=" + month.trim() + " ";
				}

				// 企业编码
				if(corpcode != null && !"".equals(corpcode) && !"0".equals(corpcode))
				{
					sql = sql + " AND LBT.CORP_CODE='" + corpcode + "' ";
				}
			}
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next())
			{
				result = rs.getLong("COUNT");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "未知统计数查询异常");
			throw e;
		}
		finally
		{
			close(rs, ps, conn);
		}
		return result;
	}

	/**
	 * 获取套餐计费统计报表
	 * 
	 * @param depreportvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getChargeStatsRptSql(ChargeStatsRptVo cstatvo, String corpCode) throws Exception
	{
		StringBuffer sql = new StringBuffer("");
		// 数据的连接符号码
		String lianjiefuhao = "||";
		switch (StaticValue.DBTYPE)
		{
			case 1:
				// oracle
				lianjiefuhao = "||";
				break;
			case 2:
				// sqlserver2005
				lianjiefuhao = "+";
				break;
			case 3:
				// MYSQL
				lianjiefuhao = "CONCAT(";
				break;
			case 4:
				// DB2
				lianjiefuhao = "||";
				break;
			default:
				break;
		}

		// 是否多企业的条件条件查询
		String conditionSql = "  ";
		String contractdep = "";
		conditionSql += " AND d." + TableLfDep.CORP_CODE + "='" + corpCode + "'";
		contractdep = " LBT.CORP_CODE='" + corpCode + "'";
		// 套餐相关查询条件
		String taocanCdtionSql = getConditionTaoCanSql(cstatvo, "LBT.");
		String timeCdtionSql = getConditionTimeSql(cstatvo, "LBT.");

		// 业务套餐统计语句
		StringBuffer taocansql = null;
		// 套餐主语句
		taocansql = new StringBuffer(" SELECT LBT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY,'0' UDTYPE FROM LF_DEP_TAOCAN LDT INNER JOIN LF_BUS_TAOCAN LBT ON LDT.TAOCAN_CODE=LBT.TAOCAN_CODE  WHERE" + contractdep + taocanCdtionSql + " GROUP BY LBT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY ");
		// 未知sql
		String nullSql = "";
		// mySql
		if(StaticValue.DBTYPE == 3)
		{
			nullSql = " UNION ALL SELECT LBT.TAOCAN_CODE," + lianjiefuhao + "LBT.TAOCAN_NAME,'(未知)') TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY,'1' UDTYPE FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_DEP_TAOCAN LDT ON LDT.TAOCAN_CODE=LBT.TAOCAN_CODE WHERE COALESCE(LDT.ID,0)=0 AND" + contractdep + taocanCdtionSql + timeCdtionSql + " GROUP BY LBT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY ";
		}
		else if(StaticValue.DBTYPE == 4)
		{
			// DB2
			nullSql = " UNION ALL SELECT LBT.TAOCAN_CODE,LBT.TAOCAN_NAME" + lianjiefuhao + "'(未知)' TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY,'1' UDTYPE FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_DEP_TAOCAN LDT ON LDT.TAOCAN_CODE=LBT.TAOCAN_CODE WHERE COALESCE(LDT.ID,0)=0 AND" + contractdep + taocanCdtionSql + timeCdtionSql + " GROUP BY LBT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY ";
		}
		else
		{
			// 其他数据库
			nullSql = " UNION ALL SELECT LBT.TAOCAN_CODE,LBT.TAOCAN_NAME" + lianjiefuhao + "'(未知)',LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY,'1' UDTYPE FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_DEP_TAOCAN LDT ON LDT.TAOCAN_CODE=LBT.TAOCAN_CODE WHERE LDT.ID IS NULL AND" + contractdep + taocanCdtionSql + timeCdtionSql + " GROUP BY LBT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY ";
		}
		sql.append(taocansql).append(nullSql);
		String totalsql = "select tot.TAOCAN_CODE,tot.TAOCAN_NAME,tot.TAOCAN_TYPE,tot.TAOCAN_MONEY,tot.UDTYPE FROM (" + sql.toString() + ") tot ";
		return totalsql;
	}

	/**
	 * 获取外层查询条件sql
	 * 
	 * @param cstatvo
	 * @return
	 */
	public String getConditionTaoCanSql(ChargeStatsRptVo cstatvo, String bieming)
	{
		String sql = "";

		// 套餐编码
		if(cstatvo.getTaocancode() != null && !"".equals(cstatvo.getTaocancode()))
		{
			sql = sql + " AND " + bieming + "TAOCAN_CODE LIKE '%" + cstatvo.getTaocancode().trim() + "%'";
		}

		// 套餐名称
		if(cstatvo.getTaocanname() != null && !"".equals(cstatvo.getTaocanname()))
		{
			sql = sql + " AND " + bieming + "TAOCAN_NAME LIKE '%" + cstatvo.getTaocanname().trim() + "%'";
		}

		// 套餐类型 包年 包月。。。
		if(cstatvo.getTaocantype() != null)
		{
			sql = sql + " AND " + bieming + "TAOCAN_TYPE=" + cstatvo.getTaocantype() + " ";
		}

		return sql;
	}

	/**
	 * 获取时间查询条件sql
	 * 
	 * @param cstatvo
	 * @param bieming
	 * @return
	 */
	public String getConditionTimeSql(ChargeStatsRptVo cstatvo, String bieming)
	{
		String sql = "";

		// 年
		if(cstatvo.getY() != null)
		{
			sql = sql + " AND " + bieming + "IYEAR=" + cstatvo.getY().trim() + " ";
		}

		// 月
		if(cstatvo.getImonth() != null)
		{
			sql = sql + " AND " + bieming + "IMONTH=" + cstatvo.getImonth().trim() + " ";
		}

		return sql;
	}

}
