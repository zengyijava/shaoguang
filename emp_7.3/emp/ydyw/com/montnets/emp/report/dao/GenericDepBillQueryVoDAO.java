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
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.report.vo.DepBillQueryVo;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 *机构计费统计报表
 * 
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 下午07:19:58
 * @description
 */
public class GenericDepBillQueryVoDAO extends SuperDAO
{

	
	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 根据当前操作员机构ID查询机构计费报表信息
	 * 
	 * @param curUserId
	 * @param depid
	 * @param dbqvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DepBillQueryVo> getDepBillQueryVoList(Long curUserId, String depid, DepBillQueryVo dbqvo, String corpCode, PageInfo pageInfo) throws Exception
	{
		// 获取操作员报表用的sql语句
		String sql = "";
		// 判断短彩类型获取不同的查询语句
		sql = this.getDepBillQuerySql(depid, dbqvo, corpCode);
		// 加上排序
		String dataSql = sql + " order by tot.UDTYPE,tot.DEP_NAME,tot.TAOCAN_NAME ASC ";
		// 总条数语句
		String countSql = "select count(*) totalcount FROM (";
		countSql += sql;
		countSql += " ) A";
		List<DynaBean> dynList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(dataSql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		List<DepBillQueryVo> returnList = fillDepBillQueryVoList(dynList, dbqvo,corpCode);
		//Collections.sort(returnList);
		return returnList;
	}

	
	/**
	 * 根据企业编码，或者对应的操作员统计报表信息 （如果企业编码为空，则显示所有）(不带分页的查询）
	 * 
	 * @param curUserId
	 *        当期登录的操作员ID
	 * @param userIds
	 *        管辖范围内的操作员ID
	 * @param depIds
	 *        所管辖的机构ids
	 * @param dbqv
	 * @param corpCode
	 *        企业编码
	 * @return
	 * @throws Exception
	 */
	public List<DepBillQueryVo> getDepBillQueryVoDepUnPage(Long curUserId, String depIds, DepBillQueryVo dbqv, String corpCode) throws Exception
	{
		String sql = this.getDepBillQuerySql(depIds, dbqv, corpCode);
		sql = sql + " order by tot.UDTYPE,tot.DEP_NAME,tot.TAOCAN_NAME ASC ";
		List<DynaBean> dynList = getListDynaBeanBySql(sql);
		// 填充vo
		List<DepBillQueryVo> returnList = fillDepBillQueryVoList(dynList, dbqv, corpCode);
		//Collections.sort(returnList);
		return returnList;
	}
	
	
	/**
	 * 填充机构计费统计报表
	 * @param dynList
	 * @param dbqvs
	 * @param corpcode
	 * @return
	 * @throws Exception
	 */
	public List<DepBillQueryVo> fillDepBillQueryVoList(List<DynaBean> dynList, DepBillQueryVo dbqvs,String corpcode) throws Exception
	{
		List<DepBillQueryVo> returnList = new ArrayList<DepBillQueryVo>();
		// 循环填充值
		if(dynList != null && dynList.size() > 0)
		{
			for (int i = 0; i < dynList.size(); i++)
			{
				DynaBean dynb = dynList.get(i);
				if(dynb != null)
				{
					//机构计费统计对象
					DepBillQueryVo dbqv = new DepBillQueryVo();
					// 机构ID/操作员ID
					String depid = dynb.get("dep_id") != null ? dynb.get("dep_id").toString() : "";
					// 数据类型 机构 操作员 未知
					String udtype = dynb.get("udtype") != null ? dynb.get("udtype").toString() : "";
					// 机构名称或操作员名称
					String depname = dynb.get("dep_name") != null ? dynb.get("dep_name").toString() : "";
					// 状态
					String userstate = dynb.get("user_state") != null ? dynb.get("user_state").toString() : "";
					// 套餐编码
					String taocancode = dynb.get("taocan_code") != null ? dynb.get("taocan_code").toString() : "";
					// 套餐名称
					String taocanname = dynb.get("taocan_name") != null ? dynb.get("taocan_name").toString() : "";
					// 套餐类型 包年包月
					String taocantype = dynb.get("taocan_type") != null ? dynb.get("taocan_type").toString() : "";
					// 套餐资费
					String taocanmoney = dynb.get("taocan_money") != null ? dynb.get("taocan_money").toString() : "";

					// 机构名称
					dbqv.setDepname(depname);
					// 操作员状态
					dbqv.setUserstate(userstate != null && !"".equals(userstate) ? Integer.parseInt(userstate) : null);
					// 套餐名称
					dbqv.setTaocanname(taocanname);
					// 套餐类型
					dbqv.setTaocantype(taocantype != null && !"".equals(taocantype) ? Integer.parseInt(taocantype) : null);
					// 套餐资费
					dbqv.setTaocanmoney(taocanmoney != null && !"".equals(taocanmoney) ? Long.parseLong(taocanmoney) : 0l);
					//数据类型 机构 操作员  未知
					dbqv.setUdtype(udtype);
					
					if(!"".equals(depid) && !"".equals(taocancode))
					{
						if(udtype != null && "0".equals(udtype))
						{
							// 机构数据填充
							// 获取次级机构的所有下级机构
							String depids = new DepDAO().getChildUserDepByParentID(Long.parseLong(depid), "LD.DEP_ID");
							//签约人数
							Long contractcount=statisticsDepCount(depids, taocancode,dbqvs.getY(), dbqvs.getImonth(), 0,corpcode);
							dbqv.setContractcount(contractcount);
							//扣费成功数
							Long deductioncount=statisticsDepCount(depids, taocancode, dbqvs.getY(), dbqvs.getImonth(), 1, corpcode);
							dbqv.setDeductioncount(deductioncount);
							//扣费失败数
							Long deductionfailcount=statisticsDepCount(depids, taocancode, dbqvs.getY(), dbqvs.getImonth(), 2, corpcode);
							dbqv.setDeductionfailcount(deductionfailcount);
							//扣费总金额
							dbqv.setDeductiontotalcount(dbqv.getDeductioncount()!=null?dbqv.getDeductioncount()*dbqv.getTaocanmoney():0l);
							//退费总金额
							Long backmoney=statisticsDepCount(depids, taocancode, dbqvs.getY(), dbqvs.getImonth(), 3, corpcode);
							dbqv.setBackmoney(backmoney);
							//总收入
							if(dbqv.getDeductiontotalcount()>dbqv.getBackmoney()){
								dbqv.setTotalmoney(dbqv.getDeductiontotalcount()-dbqv.getBackmoney());
							}else{
								dbqv.setTotalmoney(0l);
							}

						}
						else if(udtype != null && "1".equals(udtype))
						{
							// 操作员
							//签约人数
							Long contractcount=statisticsUserCount(depid, taocancode,  dbqvs.getY(), dbqvs.getImonth(), 0,corpcode);
							dbqv.setContractcount(contractcount);
							//扣费成功数
							Long deductioncount=statisticsUserCount(depid, taocancode, dbqvs.getY(), dbqvs.getImonth(), 1, corpcode);
							dbqv.setDeductioncount(deductioncount);
							//扣费失败数
							Long deductionfailcount=statisticsUserCount(depid, taocancode, dbqvs.getY(), dbqvs.getImonth(), 2, corpcode);
							dbqv.setDeductionfailcount(deductionfailcount);
							//扣费总金额
							dbqv.setDeductiontotalcount(dbqv.getDeductioncount()!=null?dbqv.getDeductioncount()*dbqv.getTaocanmoney():0l);
							//退费总金额
							Long backmoney=statisticsUserCount(depid, taocancode, dbqvs.getY(), dbqvs.getImonth(), 3, corpcode);
							dbqv.setBackmoney(backmoney);
							//总收入
							if(dbqv.getDeductiontotalcount()>dbqv.getBackmoney()){
								dbqv.setTotalmoney(dbqv.getDeductiontotalcount()-dbqv.getBackmoney());
							}else{
								dbqv.setTotalmoney(0l);
							}

							
						}
						else if(udtype != null && "2".equals(udtype))
						{
							// 未知
							//签约人数
							Long contractcount = statisticsUnKnownCount(taocancode, null, null, 0, corpcode);
							dbqv.setContractcount(contractcount);
							//扣费成功数
							Long deductioncount=statisticsUnKnownCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 1, corpcode);
							dbqv.setDeductioncount(deductioncount);
							//扣费失败数
							Long deductionfailcount=statisticsUnKnownCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 2, corpcode);
							dbqv.setDeductionfailcount(deductionfailcount);
							//扣费总金额
							dbqv.setDeductiontotalcount(dbqv.getDeductioncount()!=null?dbqv.getDeductioncount()*dbqv.getTaocanmoney():0l);
							//退费总金额
							Long backmoney=statisticsUnKnownCount(taocancode, dbqvs.getY(), dbqvs.getImonth(), 3, corpcode);
							dbqv.setBackmoney(backmoney);
							//总收入
							if(dbqv.getDeductiontotalcount()>dbqv.getBackmoney()){
								dbqv.setTotalmoney(dbqv.getDeductiontotalcount()-dbqv.getBackmoney());
							}else{
								dbqv.setTotalmoney(0l);
							}
						}
					}
					else
					{
						// 签约人数
						dbqv.setContractcount(0l);
						// 扣费人数
						dbqv.setDeductioncount(0l);
						// 扣费失败人数
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
	 * 合计
	 * @param curUserId
	 * @param depIds
	 * @param dbqv
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public Long findSumCount(Long curUserId, String depIds, DepBillQueryVo dbqv, String corpCode) throws Exception
	{
		String sql = this.getDepBillQuerySql(depIds, dbqv, corpCode);
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
	public Long fillTotalMoneyList(List<DynaBean> dynList, DepBillQueryVo dbqvs,String corpcode) throws Exception
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
					// 机构ID/操作员ID
					String depid = dynb.get("dep_id") != null ? dynb.get("dep_id").toString() : "";
					// 数据类型 机构 操作员 未知
					String udtype = dynb.get("udtype") != null ? dynb.get("udtype").toString() : "";
					// 套餐编码
					String taocancode = dynb.get("taocan_code") != null ? dynb.get("taocan_code").toString() : "";
					// 套餐资费
					String strtaocanmoney = dynb.get("taocan_money") != null ? dynb.get("taocan_money").toString() : "";
					Long taocanmoney=strtaocanmoney != null && !"".equals(strtaocanmoney) ? Long.parseLong(strtaocanmoney) : 0l;
					//总收入
					Long totalmoney=0l;
					if(!"".equals(depid) && !"".equals(taocancode))
					{
						if(udtype != null && "0".equals(udtype))
						{
							// 机构数据填充
							// 获取次级机构的所有下级机构
							String depids = new DepDAO().getChildUserDepByParentID(Long.parseLong(depid), "LD.DEP_ID");
							//扣费成功数
							Long deductioncount=statisticsDepCount(depids, taocancode, dbqvs.getY(), dbqvs.getImonth(), 1, corpcode);
							//扣费总金额
							Long deductiontotalcount=deductioncount!=null?deductioncount*taocanmoney:0l;
							//退费总金额
							Long backmoney=statisticsDepCount(depids, taocancode, dbqvs.getY(), dbqvs.getImonth(), 3, corpcode);
							if(deductiontotalcount>backmoney){
								totalmoney=deductiontotalcount-backmoney;
							}

						}
						else if(udtype != null && "1".equals(udtype))
						{
							// 操作员
							//扣费成功数
							Long deductioncount=statisticsUserCount(depid, taocancode, dbqvs.getY(), dbqvs.getImonth(), 1, corpcode);
							//扣费总金额
							Long deductiontotalcount=deductioncount!=null?deductioncount*taocanmoney:0l;
							//退费总金额
							Long backmoney=statisticsUserCount(depid, taocancode, dbqvs.getY(), dbqvs.getImonth(), 3, corpcode);
							//总收入
							if(deductiontotalcount>backmoney){
								totalmoney=deductiontotalcount-backmoney;
							}
						}
						else if(udtype != null && "2".equals(udtype))
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
	 * 机构计费统计数查询
	 * 
	 * @param depids
	 * @param taocancode
	 * @param year
	 * @param month
	 * @param optype
	 * @param corpcode
	 * @return
	 * @throws Exception
	 */
	public Long statisticsDepCount(String depids, String taocancode, String year, String month, Integer optype,String corpcode) throws Exception
	{
		if(depids == null || "".equals(depids) || taocancode == null || "".equals(taocancode) || optype == null)
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
				sql = "SELECT COUNT(ID) COUNT FROM LF_CONTRACT_TAOCAN LCT INNER JOIN LF_SYSUSER LS ON LCT.CONTRACT_USER=LS.USER_ID INNER JOIN LF_DEP LD ON LD.DEP_ID=LS.DEP_ID WHERE TAOCAN_CODE='" + taocancode + "' AND " + depids
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
				time.set(Calendar.DAY_OF_MONTH, 1);//
				time.set(Calendar.HOUR_OF_DAY, 0);
				time.set(Calendar.MINUTE, 0);
				time.set(Calendar.SECOND, 0);
				IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
				sql =sql+ " AND UPDATE_TIME>="+genericDao.getTimeCondition(df.format(time.getTime()));
				time.set(Calendar.DAY_OF_MONTH, time.getActualMaximum(Calendar.DAY_OF_MONTH));
				sql=sql+")) AND CREATE_TIME<="+genericDao.getTimeCondition(df.format(time.getTime()));
				//企业编码
				if(corpcode != null && !"".equals(corpcode)&&!"0".equals(corpcode))
				{
					sql = sql + " AND LS.CORP_CODE='" + corpcode + "' ";
				}
			}
			else if(optype == 1 || optype == 2)
			{
				//扣费成功数 
				sql = "SELECT COUNT(ID) COUNT FROM LF_DEDUCTIONS_LIST LCT INNER JOIN LF_SYSUSER LS ON LCT.CONTRACT_USER=LS.USER_ID INNER JOIN LF_DEP LD ON LD.DEP_ID=LS.DEP_ID  WHERE TAOCAN_CODE='" + taocancode + "' AND  " + depids;
				
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
				
				//年
				if(year != null && !"".equals(year))
				{
					sql = sql + " AND IYEAR=" + year.trim() + " ";
				}
				
				//月
				if(month != null && !"".equals(month))
				{
					sql = sql + " AND IMONTH=" + month.trim() + " ";
				}
				
				//企业编码
				if(corpcode != null && !"".equals(corpcode)&&!"0".equals(corpcode))
				{
					sql = sql + " AND LS.CORP_CODE='" + corpcode + "' ";
				}
			}
			else if(optype == 3)
			{
				// 退费金额
				sql = "SELECT SUM(BUPSUM_MONEY) COUNT FROM LF_DEDUCTIONS_LIST LCT INNER JOIN LF_SYSUSER LS ON LCT.CONTRACT_USER=LS.USER_ID INNER JOIN LF_DEP LD ON LD.DEP_ID=LS.DEP_ID  WHERE TAOCAN_CODE='" + taocancode + "' AND DEDUCTIONS_TYPE=3 AND " + depids;
				
				//年
				if(year != null && !"".equals(year))
				{
					sql = sql + " AND IYEAR=" + year.trim() + " ";
				}
				
				//月
				if(month != null && !"".equals(month))
				{
					sql = sql + " AND IMONTH=" + month.trim() + " ";
				}
				
				//企业编码
				if(corpcode != null && !"".equals(corpcode)&&!"0".equals(corpcode))
				{
					sql = sql + " AND LS.CORP_CODE='" + corpcode + "' ";
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
			EmpExecutionContext.error(e, "机构统计数查询异常");
			throw e;
		}
		finally
		{
			close(rs, ps, conn);
		}
		return result;
	}

	/**
	 * 操作员统计数查询
	 * 
	 * @param userid
	 * @param taocancode
	 * @param optype
	 * @return
	 * @throws Exception
	 */
	public Long statisticsUserCount(String userid, String taocancode, String year, String month, Integer optype,String corpcode) throws Exception
	{
		if(userid == null || "".equals(userid) || taocancode == null || "".equals(taocancode) || optype == null)
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
				sql = "SELECT COUNT(ID) COUNT FROM LF_CONTRACT_TAOCAN WHERE TAOCAN_CODE='" + taocancode + "' AND CONTRACT_USER=" + userid
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
				time.set(Calendar.DAY_OF_MONTH, 1);//
				time.set(Calendar.HOUR_OF_DAY, 0);
				time.set(Calendar.MINUTE, 0);
				time.set(Calendar.SECOND, 0);
				IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
				sql =sql+ " AND UPDATE_TIME>="+genericDao.getTimeCondition(df.format(time.getTime()));
				time.set(Calendar.DAY_OF_MONTH, time.getActualMaximum(Calendar.DAY_OF_MONTH));
				sql=sql+"))  AND CREATE_TIME<="+genericDao.getTimeCondition(df.format(time.getTime()))+" ";
				//企业编码
				if(corpcode != null && !"".equals(corpcode)&&!"0".equals(corpcode))
				{
					sql = sql + " AND CORP_CODE='" + corpcode + "' ";
				}
			}
			else if(optype == 1 || optype == 2)
			{
				// 扣费成功数
				sql = "SELECT COUNT(ID) COUNT FROM LF_DEDUCTIONS_LIST WHERE TAOCAN_CODE='" + taocancode + "' AND CONTRACT_USER=" + userid;
				
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
				
				//年
				if(year != null && !"".equals(year))
				{
					sql = sql + " AND IYEAR=" + year.trim() + " ";
				}
				
				//月
				if(month != null && !"".equals(month))
				{
					sql = sql + " AND IMONTH=" + month.trim() + " ";
				}
				
				//企业编码
				if(corpcode != null && !"".equals(corpcode)&&!"0".equals(corpcode))
				{
					sql = sql + " AND CORP_CODE='" + corpcode + "' ";
				}
			}
			else if(optype == 3)
			{
				// 退费金额
				sql = "SELECT SUM(BUPSUM_MONEY) COUNT FROM LF_DEDUCTIONS_LIST WHERE TAOCAN_CODE='" + taocancode + "' AND DEDUCTIONS_TYPE=3 AND CONTRACT_USER=" + userid;

				//年
				if(year != null && !"".equals(year))
				{
					sql = sql + " AND IYEAR=" + year.trim() + " ";
				}
				
				//月
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
			EmpExecutionContext.error(e, "操作员统计数查询异常");
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
	 * @param taocancode
	 * @param year
	 * @param month
	 * @param optype
	 * @param corpcode
	 * @return
	 * @throws Exception
	 */
	public Long statisticsUnKnownCount(String taocancode, String year, String month, Integer optype,String corpcode) throws Exception
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
				// 扣费成功数   扣费失败数
				// mySql
				if(StaticValue.DBTYPE == 3)
				{
					sql = "SELECT COUNT(LBT.ID) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE " +
							"COALESCE(SU.USER_ID,0)=0 AND TAOCAN_CODE='" + taocancode + "' ";
				}
				else if(StaticValue.DBTYPE == 4)
				{
					// DB2
					sql = "SELECT COUNT(LBT.ID) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE " +
							"COALESCE(SU.USER_ID,0)=0 AND TAOCAN_CODE='" + taocancode + "' ";
				}
				else
				{
					// 其他数据库
					sql = "SELECT COUNT(LBT.ID) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE " +
							"SU.USER_ID IS NULL AND TAOCAN_CODE='" + taocancode + "' ";
				}
				
				//扣费成功数条件
				if(optype == 1)
				{
					sql = sql + " AND DEDUCTIONS_TYPE IN (1,3,4,5) ";
				}else if(optype==2){
					//扣费失败数条件
					sql = sql + " AND DEDUCTIONS_TYPE=2 ";
				}else{
					return 0l;
				}
				
				//年
				if(year != null && !"".equals(year))
				{
					sql = sql + " AND LBT.IYEAR=" + year.trim() + " ";
				}
				
				//月
				if(month != null && !"".equals(month))
				{
					sql = sql + " AND LBT.IMONTH=" + month.trim() + " ";
				}
				
				//企业编码
				if(corpcode != null && !"".equals(corpcode) && !"0".equals(corpcode))
				{
					sql = sql + " AND LBT.CORP_CODE='" + corpcode + "' ";
				}
			}
			else if(optype == 3)
			{
				//退费金额
				// mySql
				if(StaticValue.DBTYPE == 3)
				{
					sql = "SELECT SUM(LBT.BUPSUM_MONEY) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE " +
							"COALESCE(SU.USER_ID,0)=0 AND TAOCAN_CODE='" + taocancode + "' AND DEDUCTIONS_TYPE=3 ";
				}
				else if(StaticValue.DBTYPE == 4)
				{
					// DB2
					sql = "SELECT SUM(LBT.BUPSUM_MONEY) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE " +
							"COALESCE(SU.USER_ID,0)=0 AND TAOCAN_CODE='" + taocancode + "'  AND DEDUCTIONS_TYPE=3 ";
				}
				else
				{
					// 其他数据库
					sql = "SELECT SUM(LBT.BUPSUM_MONEY) COUNT FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU ON SU.USER_ID=LBT.CONTRACT_USER WHERE " +
							"SU.USER_ID IS NULL AND TAOCAN_CODE='" + taocancode + "'  AND DEDUCTIONS_TYPE=3 ";
				}
				
				
				//年
				if(year != null && !"".equals(year))
				{
					sql = sql + " AND LBT.IYEAR=" + year.trim() + " ";
				}
				
				//月
				if(month != null && !"".equals(month))
				{
					sql = sql + " AND LBT.IMONTH=" + month.trim() + " ";
				}
				
				//企业编码
				if(corpcode != null && !"".equals(corpcode)&&!"0".equals(corpcode))
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
	 * 短信机构计费报表sql语句生成
	 * 
	 * @param depId
	 * @param depbillqueryvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public String getDepBillQuerySql(String depId, DepBillQueryVo depbillqueryvo, String corpCode) throws Exception
	{
		if(depId == null)
		{
			depId = "1";
		}
		StringBuffer sql = new StringBuffer("");
		// 数据库的判空函数 默认为oracle的判断NULL函数
		String nullFunction = "NVL";
		// 数据的连接符号码
		String lianjiefuhao = "||";
		switch (StaticValue.DBTYPE)
		{
			case 1:
				// oracle
				nullFunction = "NVL";
				lianjiefuhao = "||";
				break;
			case 2:
				// sqlserver2005
				nullFunction = "ISNULL";
				lianjiefuhao = "+";
				break;
			case 3:
				// MYSQL
				nullFunction = "IFNULL";
				lianjiefuhao = "CONCAT(";
				break;
			case 4:
				// DB2
				nullFunction = "COALESCE";
				lianjiefuhao = "||";
				break;
			default:
				break;
		}

		// 是否多企业的条件条件查询
		String conditionSqldep = "  ";
		// 是否多企业的条件条件查询
		String conditionSqluser = "  ";
		String contractdep="";
		if(null != corpCode && 0 != corpCode.length() && StaticValue.getCORPTYPE() == 1)
		{
			conditionSqldep += " AND d." + TableLfDep.CORP_CODE + "='" + corpCode + "'";
			contractdep = " AND LBT.CORP_CODE='" + corpCode + "'";
			conditionSqluser += " AND sysuser." + TableLfSysuser.CORP_CODE + "='" + corpCode + "'";
		}

		// 套餐相关查询条件
		String taocanCdtionSql = getConditionTaoCanSql(depbillqueryvo, "LBT.");
		String timeCdtionSql = getConditionTimeSql(depbillqueryvo, "LBT.");

		/**
		 * MySql连接
		 */
		// 机构计费语句
		StringBuffer depsql = null;
		if(StaticValue.DBTYPE == 3)
		{
			// mysql
			depsql = new StringBuffer("SELECT " + TableLfDep.DEP_ID + "," + lianjiefuhao + "'[机构]'," + nullFunction + "(" + TableLfDep.DEP_NAME + ",'未知机构')) DEP_NAME,null USERSTATE,TC.TAOCAN_CODE,TC.TAOCAN_NAME,TC.TAOCAN_TYPE,TC.TAOCAN_MONEY,'0' UDTYPE ");
		}
		else
		{
			// 其他数据库
			if(StaticValue.DBTYPE == 4)
			{
				// db2数据库
				depsql = new StringBuffer("SELECT " + TableLfDep.DEP_ID + ",'[机构]'" + lianjiefuhao + nullFunction + "(" + TableLfDep.DEP_NAME + ",'未知机构') DEP_NAME,-1 USERSTATE,TC.TAOCAN_CODE,TC.TAOCAN_NAME,TC.TAOCAN_TYPE,TC.TAOCAN_MONEY,'0' UDTYPE");
			}
			else
			{
				depsql = new StringBuffer("SELECT " + TableLfDep.DEP_ID + ",'[机构]'" + lianjiefuhao + nullFunction + "(" + TableLfDep.DEP_NAME + ",'未知机构') DEP_NAME,null USERSTATE,TC.TAOCAN_CODE,TC.TAOCAN_NAME,TC.TAOCAN_TYPE,TC.TAOCAN_MONEY,'0' UDTYPE");
			}
		}

		// 操作员语句
		StringBuffer usersql = null;
		// 未知sql
		String nullSql = "";

		// mySql
		if(StaticValue.DBTYPE == 3)
		{
			usersql = new StringBuffer("SELECT USER_ID DEP_ID," + lianjiefuhao + "'[操作员]'," + nullFunction + "(" + TableLfSysuser.USER_NAME + ",'未知操作员')) DEP_NAME,USER_STATE USERSTATE,TC.TAOCAN_CODE,TC.TAOCAN_NAME,TC.TAOCAN_TYPE,TC.TAOCAN_MONEY,'1' UDTYPE ");
			nullSql = " UNION ALL SELECT 0 DEP_ID,'未知机构' DEP_NAME,-1 USERSTATE,LBT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY,'2' UDTYPE FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU on SU.USER_ID=LBT.CONTRACT_USER WHERE COALESCE(SU.USER_ID,0)=0 "+ contractdep+ taocanCdtionSql + timeCdtionSql + " GROUP BY LBT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY ";
		}
		else if(StaticValue.DBTYPE == 4)
		{
			// DB2
			usersql = new StringBuffer("SELECT USER_ID DEP_ID,'[操作员]'" + lianjiefuhao + nullFunction + "(" + TableLfSysuser.USER_NAME + ",'未知操作员') DEP_NAME,USER_STATE USERSTATE,TC.TAOCAN_CODE,TC.TAOCAN_NAME,TC.TAOCAN_TYPE,TC.TAOCAN_MONEY,'1' UDTYPE ");
			nullSql = " UNION ALL SELECT 0 DEP_ID,'未知机构' DEP_NAME,-1 USERSTATE,LBT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY,'2' UDTYPE FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU on SU.USER_ID=LBT.CONTRACT_USER " + " WHERE COALESCE(SU.USER_ID,0)=0 "+contractdep + taocanCdtionSql + timeCdtionSql + " GROUP BY LBT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY ";
		}
		else
		{
			// 其他数据库
			usersql = new StringBuffer("SELECT USER_ID,'[操作员]'" + lianjiefuhao + nullFunction + "(" + TableLfSysuser.USER_NAME + ",'未知操作员') USER_NAME,USER_STATE USERSTATE,TC.TAOCAN_CODE,TC.TAOCAN_NAME,TC.TAOCAN_TYPE,TC.TAOCAN_MONEY,'1' UDTYPE ");
			nullSql = " UNION ALL SELECT null,'未知机构',-1 USERSTATE,LBT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY,'2' UDTYPE FROM LF_DEDUCTIONS_LIST LBT LEFT JOIN LF_SYSUSER SU on SU.USER_ID=LBT.CONTRACT_USER " + " WHERE SU.USER_ID IS NULL " + contractdep + taocanCdtionSql + timeCdtionSql + " GROUP BY LBT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY ";
		}

		String dep = "";
		// 判断是单企业还是多企业
		if(StaticValue.getCORPTYPE() == 1)
		{
			int dep_id = getInt("dep_Id", "select dep_id from  lf_dep where corp_code='" + corpCode + "' and dep_level=1", StaticValue.EMP_POOLNAME);
			dep = dep_id + "";
		}
		else
		{
			dep = "1";
		}
		// 外层总套餐机构条件
		String depid = "";
		if(!dep.equals(depId))
		{
			depid = new DepDAO().getChildUserDepByParentID(Long.parseLong(depId), "LDT.CONTRACT_DEP");
			depid = " AND "+depid;
		}
		//不允许1 =1方式
		String ondepcondsql = "";
		// 各种不同数据库
		switch (StaticValue.DBTYPE)
		{
			case 1:
				// oracle
				ondepcondsql = " d.DEP_PATH=SUBSTR(TC.DEP_PATH,0,length(d.DEP_PATH)) ";
				break;
			case 2:
				// sqlserver
				ondepcondsql = " d.DEP_PATH=SUBSTRING(TC.DEP_PATH,0,len(d.DEP_PATH)+1) ";
				break;
			case 3:
				// mysql
				ondepcondsql = " d.DEP_PATH=SUBSTRING(TC.DEP_PATH,1,length(d.DEP_PATH)) ";
				break;
			case 4:
				// DB2
				ondepcondsql = " d.DEP_PATH=SUBSTR(TC.DEP_PATH,1,length(d.DEP_PATH)) ";
				break;
			default:
				break;
		}

		// 关联的部门sql
		depsql.append(" FROM LF_DEP d LEFT JOIN (SELECT LDT.CONTRACT_DEP,LD.DEP_PATH,LDT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY FROM LF_DEP_TAOCAN LDT " 
				+"INNER JOIN LF_BUS_TAOCAN LBT ON LDT.TAOCAN_CODE=LBT.TAOCAN_CODE" + depid + " INNER JOIN LF_SYSUSER LS ON LDT.CONTRACT_USER=LS.USER_ID INNER JOIN LF_DEP LD ON LD.DEP_ID=LS.DEP_ID AND LD.DEP_ID<>" 
				+ depId + " AND LD.DEP_STATE=1 " + contractdep + taocanCdtionSql
				+ " GROUP BY LDT.CONTRACT_DEP,LD.DEP_PATH,LDT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY) TC ON " 
				+ ondepcondsql + " WHERE SUPERIOR_ID=" + depId + " AND d." + TableLfDep.DEP_STATE + "=1 " + conditionSqldep+" GROUP BY DEP_ID,DEP_NAME,TC.TAOCAN_CODE,TC.TAOCAN_NAME,TC.TAOCAN_TYPE,TC.TAOCAN_MONEY");

		// 操作员语句
		usersql.append(" FROM LF_SYSUSER sysuser LEFT JOIN (SELECT LDT.CONTRACT_USER,LDT.TAOCAN_CODE,LBT.TAOCAN_NAME,LBT.TAOCAN_TYPE,LBT.TAOCAN_MONEY FROM LF_DEP_TAOCAN LDT INNER JOIN LF_BUS_TAOCAN LBT ON LDT.TAOCAN_CODE=LBT.TAOCAN_CODE "+contractdep+ taocanCdtionSql + ") TC ON sysuser.USER_ID=TC.CONTRACT_USER " + "WHERE sysuser.DEP_ID=" + depId + " AND sysuser.USER_ID<>1 " + conditionSqluser);

		if("-3".equals(depId) || dep.equals(depId))
		{
			sql.append(depsql).append(" UNION ALL ").append(usersql).append(nullSql);
		}
		else
		{
			sql.append(depsql).append(" UNION ALL ").append(usersql);
		}
		String totalsql = "select tot." + TableLfDep.DEP_ID + ",tot." + TableLfDep.DEP_NAME + ",tot.USERSTATE USER_STATE,tot.TAOCAN_CODE,tot.TAOCAN_NAME,tot.TAOCAN_TYPE,tot.TAOCAN_MONEY,tot.UDTYPE from (" + sql.toString() + ") tot ";

		return totalsql;
	}

	/**
	 * 获取外层查询条件sql
	 * 
	 * @param depbillqueryvo
	 * @return
	 */
	public String getConditionTaoCanSql(DepBillQueryVo depbillqueryvo, String bieming)
	{
		String sql = "";

		// 套餐名称
		if(depbillqueryvo.getTaocanname() != null && !"".equals(depbillqueryvo.getTaocanname()))
		{
			sql = sql + " AND " + bieming + "TAOCAN_NAME LIKE '%" + depbillqueryvo.getTaocanname().trim() + "%'";
		}

		// 套餐类型 包年 包月。。。
		if(depbillqueryvo.getTaocantype() != null)
		{
			sql = sql + " AND " + bieming + "TAOCAN_TYPE=" + depbillqueryvo.getTaocantype() + " ";
		}

		return sql;
	}

	/**
	 * 获取时间查询条件sql
	 * 
	 * @param depbillqueryvo
	 * @return
	 */
	public String getConditionTimeSql(DepBillQueryVo depbillqueryvo, String bieming)
	{
		String sql = "";

		// 年
		if(depbillqueryvo.getY() != null)
		{
			sql = sql + " AND " + bieming + "IYEAR=" + depbillqueryvo.getY().trim() + " ";
		}

		// 月
		if(depbillqueryvo.getImonth() != null)
		{
			sql = sql + " AND " + bieming + "IMONTH=" + depbillqueryvo.getImonth().trim() + " ";
		}

		return sql;
	}


}
