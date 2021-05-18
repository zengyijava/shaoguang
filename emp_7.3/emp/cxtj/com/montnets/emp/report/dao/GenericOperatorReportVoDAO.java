package com.montnets.emp.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.report.vo.MtDataReportVo;
import com.montnets.emp.report.vo.UserAreaRptVo;
import com.montnets.emp.table.pasroute.TableLfMmsAccbind;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.table.report.TableMmsDatareport;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;


/**
 * 操作员统计报表VODAO
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:42:15
 * @description
 */
public class GenericOperatorReportVoDAO extends SuperDAO
{
	/**
	 * 
	 * 根据企业编码，或者对应的操作员统计报表信息
	 * （如果企业编码为空，则显示所有）
	 * 
	 * @param curUserId
	 * 				  当期登录的操作员ID
	 * 
	 * @param userIds
	 * 				 管辖范围内的操作员ID
	 * 
	 * @param depIds
	 * 				所管辖的机构ids
	 * @param mtDataReportVo
	 * 
	 * @param corpCode
	 * 				企业编码
	 * @param pageInfo
	 * 
	 * @return
	 * 
	 * 
	 * @throws Exception
	 */
	public List<MtDataReportVo> getMtDataReportInfoDep(Long curUserId,String depIds,String userIds,
			MtDataReportVo mtDataReportVo,String corpCode,PageInfo pageInfo) throws Exception
	{
		//获取sql
		String sql;
		//短信
		if(mtDataReportVo.getMstype()!=null &&mtDataReportVo.getMstype()==0)
		{
			//获取操作员报表用的sql语句
			sql=this.getSMSSql(curUserId, depIds, userIds, mtDataReportVo, corpCode);
		}
		//彩信
		else if(mtDataReportVo.getMstype()!=null &&mtDataReportVo.getMstype()==1)
		{
			//获取操作员报表用的sql语句
			sql=this.getMMSSql(curUserId, depIds, userIds, mtDataReportVo, corpCode);
		}
		else{
			return null;
		}
		String dataSql = sql + " order by SYSUSER.USER_ID asc ";
		
		List<MtDataReportVo> returnList;
		if(pageInfo != null)
		{
			//分页总数查询
			String countSql = "select count(*) totalcount FROM (" + sql + " ) A";
			returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(MtDataReportVo.class, dataSql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
		else
		{
			returnList = findVoListBySQL(MtDataReportVo.class, dataSql, StaticValue.EMP_POOLNAME);
		}
		return returnList;
	}
	
	//详细数据查询 分页
	public List<DynaBean> getMtDataReportInfoDetailList(String userid,MtDataReportVo mtDataReportVo,String corpCode,String reporttype,PageInfo pageInfo) throws Exception
	{
		//获取操作员报表用的sql语句
		String sql=this.getDateDetailSql(userid, mtDataReportVo, corpCode,reporttype);
		if(sql==null||"".equals(sql))
		{
			return null;
		}
		String oderbystr;
		if("2".equals(reporttype))
		{
			oderbystr=" Y DESC,IMONTH DESC ";
		}
		else
		{
			oderbystr=" IYMD DESC ";
		}
		String datasql=sql+" ORDER BY "+oderbystr+" ";
		//分页总数查询
		String countSql = "select count(*) totalcount FROM (" + sql + " ) A";
		
	 	List<DynaBean>  returnList;
	 	if(pageInfo!=null)
	 	{
	 	     returnList=new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(datasql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	 	}
	 	else
	 	{
	 		 returnList=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(datasql);
	 	}
		return returnList;
	}
	
	  
	/**
	 * 
	 * 根据查询的条件，进行合计处理
	 * 
	 * @param curUserId
	 * 			   当前操作员ID
	 * @param depIds
	 * 			       查询条件   机构列表
	 * @param userid
	 * 			     查询条件    操作员账号列表
	 * @param mtDataReportVo
	 * 
	 * @param corpCode
	 * 				 企业编码
	 * @param pageInfo
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public long[] findDetailSumCount(String userid,
			MtDataReportVo mtDataReportVo,String corpCode,String reporttype) throws Exception
	{
		 
		//合计提交总数
		long icount = 0L;
		//合计接收成功数
		long rsucc = 0L;
		//合计发送失败数
		long rfail1 = 0L;
		//合计接收失败数
		long rfail2 = 0L;
		//合计未返数
		long rnret = 0L;
		//查询字段
		String fieldSql = "SELECT "+ReportDAO.getPublicCountSql("MDREPORT")+" FROM  ";
		//获取sql
		String sql1=this.getDateDetailSql(userid, mtDataReportVo, corpCode,reporttype);//获取操作员报表用的sql语句
		if(sql1==null||"".equals(sql1)){
			return null;
		}
		//分页总sql语句
		String sql =" ("+sql1+" ) MDREPORT";
		//总sql语句   
	    sql = fieldSql+sql;
	    //创建连接
		Connection conn = null;
		//连接操作对象
		PreparedStatement ps = null;
		//结果集
		java.sql.ResultSet rs = null;
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//加载sql
			ps = conn.prepareStatement(sql);
			//执行sql并获取结果集
			rs = ps.executeQuery();
			//遍历结果集
			if (rs.next())
			{
				icount = rs.getLong("ICOUNT");
				rsucc = rs.getLong("RSUCC");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"根据查询的条件，进行合计处理异常");
			throw e;
		} finally
		{
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
		
	//详细数据查询 分页
	public List<DynaBean> getAreaMtDataReportInfoDetailList(String userid,UserAreaRptVo userareavo,String corpCode,String reporttype,PageInfo pageInfo) throws Exception
	{
		//获取操作员报表用的sql语句
		String sql=this.getAreaDateDetailSql(userid, userareavo, corpCode,reporttype);
		if(sql==null||"".equals(sql))
		{
			return null;
		}
		String oderbystr=" ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME ";
		String datasql=sql+" ORDER BY "+oderbystr+" ASC ";
		//分页总数查询
		String countSql = "select count(*) totalcount FROM (" + sql + " ) A";
		
	 	List<DynaBean>  returnList;
	 	if(pageInfo!=null){
	 	     returnList=new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(datasql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	 	}else{
	 		 returnList=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(datasql);
	 	}
		return returnList;
	}
	
	  
	/**
	 * 
	 * 根据查询的条件，进行合计处理
	 * 
	 * @param curUserId
	 * 			   当前操作员ID
	 * @param depIds
	 * 			       查询条件   机构列表
	 * @param userid
	 * 			     查询条件    操作员账号列表
	 * @param userareavo
	 * 
	 * @param corpCode
	 * 				 企业编码
	 * @param pageInfo
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public long[] findAreaDetailSumCount(String userid,
			UserAreaRptVo userareavo,String corpCode,String reporttype) throws Exception
	{
		 
		//合计提交总数
		long icount = 0L;
		//合计接收成功数
		long rsucc = 0L;
		//合计发送失败数
		long rfail1 = 0L;
		//合计接收失败数
		long rfail2 = 0L;
		//合计未返数
		long rnret = 0L;

		//查询字段
		String fieldSql = "SELECT "+ReportDAO.getPublicCountSql("MDREPORT")+" FROM  ";
		//获取sql
		String sql1=this.getAreaDateDetailSql(userid, userareavo, corpCode,reporttype);//获取操作员报表用的sql语句
		if(sql1==null||"".equals(sql1)){
			return null;
		}
		//分页总sql语句
		String sql =" ("+sql1+" ) MDREPORT";
		//总sql语句   
	    sql = fieldSql+sql;
	    //创建连接
		Connection conn = null;
		//连接操作对象
		PreparedStatement ps = null;
		//结果集
		java.sql.ResultSet rs = null;
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//加载sql
			ps = conn.prepareStatement(sql);
			//执行sql并获取结果集
			rs = ps.executeQuery();
			//遍历结果集
			if (rs.next())
			{
				icount = rs.getLong("ICOUNT");
				rsucc = rs.getLong("RSUCC");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"根据查询的条件，进行合计处理异常");
			throw e;
		} finally
		{
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
	
		
	/**
	 * 
	 * 根据企业编码，或者对应的操作员统计报表信息
	 * （如果企业编码为空，则显示所有）(不带分页的查询）
	 * 
	 * @param curUserId
	 * 				  当期登录的操作员ID
	 * 
	 * @param userIds
	 * 				 管辖范围内的操作员ID
	 * 
	 * @param depIds
	 * 				所管辖的机构ids
	 * @param mtDataReportVo
	 * 
	 * @param corpCode
	 * 				企业编码
	 * 
	 * @return
	 * 
	 * 
	 * @throws Exception
	 */
	public List<MtDataReportVo> getMtDataReportInfoDepUnPage(Long curUserId,String depIds,String userIds,
			MtDataReportVo mtDataReportVo,String corpCode) throws Exception
	{
		
				//获取sql
				String sql ="";
				if(mtDataReportVo.getMstype()!=null
						&&mtDataReportVo.getMstype()==0){
					sql=this.getSMSSql(curUserId, depIds, userIds, mtDataReportVo, corpCode);//获取操作员报表用的sql语句
				}else if(mtDataReportVo.getMstype()!=null
						&&mtDataReportVo.getMstype()==1){
					sql=this.getMMSSql(curUserId, depIds, userIds, mtDataReportVo, corpCode);//获取操作员报表用的sql语句
				}else{
					return null;
				}
				
				//分页查询语句
				String countSql = "select count(*) totalcount FROM (";
				
				countSql += sql;
			 	//countSql += conditionSql+baseSql;
			 	countSql +=" ) A";
			 	//查询集合
			 	List<MtDataReportVo> returnList = findVoListBySQL(
			 			MtDataReportVo.class, sql,  
								StaticValue.EMP_POOLNAME);
			 	
				return returnList;
	}
	
	
	/**
	 * 
	 * 个人权限用 
	 * 
	 * @param curUserId
	 * @param mtDataReportVo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<MtDataReportVo> getMtDataReportInfoPersonal(Long curUserId,
			MtDataReportVo mtDataReportVo,String corpCode,PageInfo pageInfo) throws Exception
   {
		
		return this.getMtDataReportInfoDep(curUserId, null,null, mtDataReportVo, corpCode, pageInfo);
		
   }
	 
	
	/**
	 * 
	 * 根据查询的条件，进行合计处理
	 * 
	 * @param curUserId
	 * 			   当前操作员ID
	 * @param depIds
	 * 			       查询条件   机构列表
	 * @param userIds
	 * 			     查询条件    操作员账号列表
	 * @param mtDataReportVo
	 * 
	 * @param corpCode
	 * 				 企业编码
	 * @param pageInfo
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public long[] findSumCount(Long curUserId,String depIds,String userIds,
			MtDataReportVo mtDataReportVo,String corpCode,PageInfo pageInfo) throws Exception
	{
		//获取sql
		String sql1;
		if(mtDataReportVo.getMstype()!=null &&mtDataReportVo.getMstype()==0)
		{
			//获取操作员报表用的sql语句
			sql1=this.getSMSSql(curUserId, depIds, userIds, mtDataReportVo, corpCode);
		}
		else if(mtDataReportVo.getMstype()!=null &&mtDataReportVo.getMstype()==1)
		{
			//获取操作员报表用的sql语句
			sql1=this.getMMSSql(curUserId, depIds, userIds, mtDataReportVo, corpCode);
		}
		else
		{
			return null;
		}
		
		//分页总sql语句
		String sql =" ("+sql1+" ) MDREPORT";
		
		//查询字段
		String fieldSql = "SELECT "+ReportDAO.getPublicCountSql("MDREPORT")+" FROM  ";
		//总sql语句   
	    sql = fieldSql+sql;
	    
	    //合计提交总数
		long icount = 0L;
		//合计接收成功数
		long rsucc = 0L;
		//合计发送失败数
		long rfail1 = 0L;
		//合计接收失败数
		long rfail2 = 0L;
		//合计未返数
		long rnret = 0L;
	    
	    //创建连接
		Connection conn = null;
		//连接操作对象
		PreparedStatement ps = null;
		//结果集
		java.sql.ResultSet rs = null;
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//加载sql
			ps = conn.prepareStatement(sql);
			//执行sql并获取结果集
			rs = ps.executeQuery();
			//遍历结果集
			if (rs.next())
			{
				icount = rs.getLong("ICOUNT");
				rsucc = rs.getLong("RSUCC");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
			}
			long[] returnLong = new long[5];
			returnLong[0] = icount;
			returnLong[1] = rsucc;
			returnLong[2] = rfail1;
			returnLong[3] = rfail2;
			returnLong[4] = rnret;
			return returnLong;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"根据查询的条件，进行合计处理异常");
			throw e;
		} 
		finally
		{
			//关闭连接
			close(rs, ps, conn);
		}
	}
	
	/**
	 * 
	 * 短信获取查询操作员报表用的主SQL语句
	 * 
	 * @param curUserId
	 * @param depIds
	 * @param userIds
	 * @param mtDataReportVo
	 * @param corpCode
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * @throws Exception
	 * 
	 */
	public String getSMSSqlBf(Long curUserId,String depIds,String userIds,
			MtDataReportVo mtDataReportVo,String corpCode) throws Exception, Exception
	{
		//总sql定义
		String sql = "";
		String multiSql = "";
		//数据库的判空函数 默认为oracle的判断NULL函数
		String nullFunction = "NVL";
		//时间查询条件数组
		String[] timeStrCondition = new String[3];
		//格式化传入起始时间
		String sendtime = mtDataReportVo.getSendTime().replaceAll("-", "");
		String endtime=mtDataReportVo.getEndTime().replaceAll("-", "");
		
		//各种数据库
		switch(StaticValue.DBTYPE)
		{
		case 1:
			//oracle
			nullFunction = "NVL";
			//timeFunction = " TO_DATE(DATAREPORT."+TableMtDatareport.IYMD+",'YYYY-MM-DD') ";
//			timeStrCondition[0] = " AND TO_DATE(DATAREPORT."+TableMtDatareport.IYMD+",'YYYY-MM-DD')>=TO_DATE('"+mtDataReportVo.getSendTime()+"','YYYY-MM-DD') "; 
//			timeStrCondition[1] = " AND TO_DATE(DATAREPORT."+TableMtDatareport.IYMD+",'YYYY-MM-DD')<=TO_DATE('"+mtDataReportVo.getEndTime()+"','YYYY-MM-DD') ";
//			timeStrCondition[2] = " AND TO_DATE(DATAREPORT."+TableMtDatareport.IYMD+",'YYYY-MM-DD') BETWEEN TO_DATE('"+mtDataReportVo.getSendTime()+"','YYYY-MM-DD')  AND TO_DATE('"+mtDataReportVo.getEndTime()+"','YYYY-MM-DD') ";
			break;
		case 2:
			//sqlserver2005
			nullFunction = "ISNULL";
			//timeFunction = "  CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) ";
//			timeStrCondition[0] = " AND CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) >= CAST('" + mtDataReportVo.getSendTime()+"'  AS DATETIME)"; 
//			timeStrCondition[1] = " AND CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) <= CAST('" + mtDataReportVo.getEndTime()+"'   AS DATETIME)";
//			timeStrCondition[2] = " AND CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) BETWEEN  CAST('"+mtDataReportVo.getSendTime()+"'  AS DATETIME)  AND  CAST('"+mtDataReportVo.getEndTime()+"'  AS DATETIME)";	
			break;
		case 3:
			//MYSQL
			nullFunction = "IFNULL";
			//timeFunction = "  CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) ";
//			timeStrCondition[0] = " AND CAST(DATAREPORT."+TableMtDatareport.IYMD+" AS DATETIME) >= CAST('" + mtDataReportVo.getSendTime()+"'  AS DATETIME)"; 
//			timeStrCondition[1] = " AND CAST(DATAREPORT."+TableMtDatareport.IYMD+" AS DATETIME) <= CAST('" + mtDataReportVo.getEndTime()+"'   AS DATETIME)";
//			timeStrCondition[2] = " AND CAST(DATAREPORT."+TableMtDatareport.IYMD+" AS DATETIME) BETWEEN  CAST('"+mtDataReportVo.getSendTime()+"'  AS DATETIME)  AND  CAST('"+mtDataReportVo.getEndTime()+"'  AS DATETIME)";	
			break;
		case 4:
			//DB2
			nullFunction = "COALESCE";
//			timeStrCondition[0] = " AND DATAREPORT."+TableMtDatareport.IYMD+">="+mtDataReportVo.getSendTime().replace("-", "")+" "; 
//			timeStrCondition[1] = " AND DATAREPORT."+TableMtDatareport.IYMD+"<="+mtDataReportVo.getEndTime().replace("-", "")+" ";
//			timeStrCondition[2] = " AND DATAREPORT."+TableMtDatareport.IYMD+" BETWEEN "+mtDataReportVo.getSendTime().replace("-", "")+"  AND "+mtDataReportVo.getEndTime().replace("-", "")+" ";
			break;
			default:break;
		}
		
//		timeStrCondition[0] = " AND DATAREPORT." + TableMtDatareport.IYMD+ ">="+ sendtime + " ";
//		timeStrCondition[1] = " AND DATAREPORT." + TableMtDatareport.IYMD+ "<="+ endtime +" ";
//		timeStrCondition[2] = " AND DATAREPORT." + TableMtDatareport.IYMD+ " BETWEEN  "+ sendtime+ "  AND  "+ endtime + " ";
		
		
		switch(StaticValue.getCORPTYPE())
		{
		case 0:
			//单企业
			multiSql = "";
            break;
		case 1:
			//多企业 
			multiSql = " LEFT JOIN LF_SP_DEP_BIND SPBIND ON DATAREPORT.USERID=SPBIND.SPUSER ";
			break;
			default:break;
		}
		
		
		String domination = " ";
		String conditionSql = "  ";//条件查询
		
		timeStrCondition[0] = " WHERE DATAREPORT.IYMD>="+ sendtime + " ";
		timeStrCondition[1] = " WHERE DATAREPORT.IYMD<="+ endtime +" ";
		timeStrCondition[2] = " WHERE DATAREPORT.IYMD>="+ sendtime+ " AND DATAREPORT.IYMD<="+ endtime + " ";
		//时间查询条件
		String timewheresql=" ";
		//时间条件的查询
		if( (0 != mtDataReportVo.getSendTime().length() && 0 == mtDataReportVo.getEndTime().length()))
		{
			timewheresql += timeStrCondition[0];
		
		}else if ( 0 == mtDataReportVo.getSendTime().length() && 0 != mtDataReportVo.getEndTime().length() )
		{
			timewheresql += timeStrCondition[1];
		    
		}else if (0 != mtDataReportVo.getSendTime().length() && 0 != mtDataReportVo.getEndTime().length() )
		{
			timewheresql += timeStrCondition[2] ;
		}else
		{
			//如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();
			
			if( null != yearAndMonth )
			{
				timewheresql += " WHERE DATAREPORT.Y="+yearAndMonth[0]+" AND DATAREPORT.IMONTH="+yearAndMonth[1]+"";
			}
		}
		
		//总的sql查询字段
		sql = "SELECT "+ReportDAO.getPublicCountSql("DATAREPORT")+"," 
		           +"SYSUSER.USER_STATE USER_STATE,SYSUSER.USER_ID USER_ID,DEP.DEP_ID DEP_ID,MAX("+nullFunction+"(SYSUSER.NAME,'未知操作员')) NAME,"
		           +"MAX("+nullFunction+"(DEP.DEP_NAME,'未知机构')) DEP_NAME FROM MT_DATAREPORT DATAREPORT "
		           +  multiSql
		           +" LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_CODE = DATAREPORT.P1 LEFT JOIN LF_DEP DEP ON SYSUSER.DEP_ID=DEP.DEP_ID "+timewheresql+" ";

		 //分组
		String groupbysql = "  GROUP BY SYSUSER.USER_ID,SYSUSER.USER_STATE,DEP.DEP_ID ";
				      
		//个人权限用，查询当前操作员的相关报表信息
		if( null != curUserId )
		{
			domination += " AND (SYSUSER.USER_ID= "+curUserId+" OR SYSUSER.USER_ID IS NULL )";
		}
		
		//选择的操作员
		if( null != userIds && 0 != userIds.length())
		{
			String useridsql=getUseridSqlStr(userIds, "SYSUSER.USER_ID");
			if("-1".equals(userIds))
			{
				domination += " AND (SYSUSER.USER_ID IS NULL )";
			}else if(!"-1".equals(userIds) && userIds.contains("-1"))
			{
				domination += " AND (("+useridsql+") OR SYSUSER.USER_ID IS NULL )";
			}else
			{
				domination += " AND ("+useridsql+" )";
			}
			
		}
		conditionSql+=domination;
		//判断单多企业
		if( null != corpCode && 0 != corpCode.length() && !"".equals(multiSql) )
		{
			conditionSql += " AND SPBIND.CORP_CODE = '"+corpCode+"'";
		}
		//判断部门条件
		if( null != depIds && 0 != depIds.length())
		{
			//判断是否选中未知
			if("-3".equals(depIds))
			{
				conditionSql += " AND (DEP.DEP_ID IS NULL) ";
			}else if(!"-3".equals(depIds) && depIds.contains("-3"))
			{
				if(depIds.contains(",")){
					conditionSql += " AND (DEP.DEP_ID in ("+depIds+") OR DEP.DEP_ID IS NULL)";
				}else{
//					String depSql=new DepDAO().getChildUserDepByParentID(Long.parseLong(depIds),"DEP."+TableLfDep.DEP_ID);
//					conditionSql += " AND (("+depSql+") OR DEP.DEP_ID IS NULL)";
					String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+depIds, StaticValue.EMP_POOLNAME);
					conditionSql += " AND ((DEP.DEP_ID IN (SELECT DEP_ID FROM LF_DEP WHERE DEP_PATH LIKE '"+deppath+"%')) OR DEP.DEP_ID IS NULL)";
				}
				
				
			}else{
				if(depIds.contains(",")){
					conditionSql += " AND (DEP."+TableLfDep.DEP_ID+" in ("+depIds+")  OR DEP.DEP_ID IS NULL)";
				}else{
//					String depSql=new DepDAO().getChildUserDepByParentID(Long.parseLong(depIds),"DEP.DEP_ID");
//					if(!"".equals(depSql.trim()) && null!= depSql){
//						conditionSql += " AND ("+depSql+")";
//					}
					String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+depIds, StaticValue.EMP_POOLNAME);
					conditionSql += " AND (DEP.DEP_ID IN (SELECT DEP_ID FROM LF_DEP WHERE DEP_PATH LIKE '"+deppath+"%'))";
				}
				
				
			}
			
		}
		
		
		
		sql+=conditionSql;
		sql+=groupbysql;
	 
		return sql;
		
	}
	
	/**
	 * 
	 * 短信获取查询操作员报表用的主SQL语句
	 * 
	 * @param curUserId
	 * @param depIds
	 * @param userIds
	 * @param mtDataReportVo
	 * @param corpCode
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * @throws Exception
	 * 
	 */
	public String getSMSSql(Long curUserId,String depIds,String userIds,
			MtDataReportVo mtDataReportVo,String corpCode) throws Exception, Exception
	{
		//数据库的判空函数 默认为oracle的判断NULL函数
		String nullFunction = "NVL";
		
		//各种数据库
		switch(StaticValue.DBTYPE)
		{
			case 1:
				//oracle
				nullFunction = "NVL";
				break;
			case 2:
				//sqlserver2005
				nullFunction = "ISNULL";
				break;
			case 3:
				//MYSQL
				nullFunction = "IFNULL";
				break;
			case 4:
				//DB2
				nullFunction = "COALESCE";
				break;
			default:break;
		}
		
		String multiSql = "";
		//判断单多企业
		switch(StaticValue.getCORPTYPE())
		{
			case 0:
				//单企业
				multiSql = "";
	            break;
			case 1:
				//多企业 
				multiSql = " LEFT JOIN LF_SP_DEP_BIND SPBIND ON DATAREPORT.USERID=SPBIND.SPUSER ";
				break;
			default:break;
		}
		
		// 时间条件的查询
		String timewheresql = " WHERE DATAREPORT.IYMD>="+ mtDataReportVo.getSendTime()+ " AND DATAREPORT.IYMD<="+ mtDataReportVo.getEndTime() + " ";
		
		//总的sql查询字段
		String sql = "SELECT "+ReportDAO.getPublicCountSql("DATAREPORT")+"," 
		           +"SYSUSER.USER_STATE USER_STATE,SYSUSER.USER_ID USER_ID,MAX(DEP.DEP_ID) DEP_ID,MAX("+nullFunction+"(SYSUSER.NAME,'未知操作员')) NAME,"
		           +"MAX("+nullFunction+"(DEP.DEP_NAME,'未知机构')) DEP_NAME FROM MT_DATAREPORT DATAREPORT "
		           +  multiSql;
		//获取是否开启p2参数配置
		String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
		//获取不到配置文件值
		if(depcodethird2p2 == null || depcodethird2p2.trim().length() == 0)
		{
			depcodethird2p2 = "false";
		}
		if("true".equals(depcodethird2p2)){
			sql=sql+" LEFT JOIN LF_DEP DEP ON dep.DEP_CODE_THIRD = DATAREPORT.P2 LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_CODE = DATAREPORT.P1 "+timewheresql+" ";
		}else{
			sql=sql+" LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_CODE = DATAREPORT.P1 LEFT JOIN LF_DEP DEP ON SYSUSER.DEP_ID=DEP.DEP_ID "+timewheresql+" ";
		}
		           

		//权限范围
		String domination = " ";
		
		//个人权限用，查询当前操作员的相关报表信息
		if( null != curUserId )
		{
			domination += " AND (SYSUSER.USER_ID= "+curUserId+" OR SYSUSER.USER_ID IS NULL )";
		}
		
		//选择的操作员
		if( null != userIds && 0 != userIds.length())
		{
			String useridsql=getUseridSqlStr(userIds, "SYSUSER.USER_ID");
			if("-1".equals(userIds))
			{
				domination += " AND (SYSUSER.USER_ID IS NULL )";
			}
			else if(!"-1".equals(userIds) && userIds.contains("-1"))
			{
				domination += " AND (("+useridsql+") OR SYSUSER.USER_ID IS NULL )";
			}
			else
			{
				domination += " AND ("+useridsql+" )";
			}
			
		}
		//条件查询
		String conditionSql = domination;
		//判断单多企业
		if( null != corpCode && 0 != corpCode.length() && !"".equals(multiSql) )
		{
			conditionSql += " AND SPBIND.CORP_CODE = '"+corpCode+"'";
		}
		//判断部门条件
		if( null != depIds && 0 != depIds.length())
		{
			//判断是否选中未知
			if("-3".equals(depIds))
			{
				conditionSql += " AND (DEP.DEP_ID IS NULL) ";
			}
			else if(!"-3".equals(depIds) && depIds.contains("-3"))
			{
				if(depIds.contains(","))
				{
					conditionSql += " AND (DEP.DEP_ID in ("+depIds+") OR DEP.DEP_ID IS NULL)";
				}
				else
				{
					String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+depIds, StaticValue.EMP_POOLNAME);
					conditionSql += " AND (DEP.DEP_ID IN (SELECT DEP_ID FROM LF_DEP WHERE DEP_PATH LIKE '"+deppath+"%') OR DEP.DEP_ID IS NULL)";
				}
			}
			else
			{
				//页面未选机构的时候，会进来这里
				if(depIds.contains(","))
				{
						//获取企业的顶级机构id
						int dep_id = getInt("dep_Id",
								"select dep_id from  lf_dep where corp_code='" + corpCode + "' and dep_level=1", StaticValue.EMP_POOLNAME);
						//企业顶级机构id
						String dep = dep_id + "";
						//如果选择的机构是顶级机构，则要把未知机构的查出来
						if (dep.equals(mtDataReportVo.getDepName())) 
						{
							conditionSql += " AND (DEP.DEP_ID in ("+depIds+") OR DEP.DEP_ID IS NULL)";
						}else{
							conditionSql += " AND (DEP.DEP_ID in ("+depIds+"))";
						}
				}
				//页面选一个机构后，会进这里
				else
				{
					String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+depIds, StaticValue.EMP_POOLNAME);
					conditionSql += " AND (DEP.DEP_ID IN (SELECT DEP_ID FROM LF_DEP WHERE DEP_PATH LIKE '"+deppath+"%'))";
				}
			}
		}
		
		//运营商条件
		if(mtDataReportVo.getSpnumtype() != null)
		{
			//运营商   0国内  1国外   ""全部
			if(mtDataReportVo.getSpnumtype()==0)
			{
				conditionSql += " AND DATAREPORT.SPISUNCM IN (0,1,21) ";
			}
			else if(mtDataReportVo.getSpnumtype()==1)
			{
				conditionSql += " AND DATAREPORT.SPISUNCM=5 ";
			}
		}
		
		//分组
		String groupbysql = " GROUP BY SYSUSER.USER_ID,SYSUSER.USER_STATE ";
		
		sql+=conditionSql + groupbysql;
	 
		return sql;
	}
	
	/**
	 *  in 字符串拆分
	 * @param idstr
	 * @param columnstr
	 * @return
	 */
	private String getUseridSqlStr(String idstr,String columnstr){
		String sql=" 1=2 ";
		if(idstr!=null&&!"".equals(idstr)&&columnstr!=null&&!"".equals(columnstr)){
			if(idstr.contains(",")){
				String[] useriday=idstr.split(",");
				if(useriday.length<900){
					sql=" "+columnstr+" IN ("+idstr+") ";
				}else{
					String zidstr="";
					sql="";
					for(int i=0;i<useriday.length;i++){
						if((i+1)%900==0){
							zidstr=zidstr+useriday[i];
							sql=sql+" "+columnstr +" IN ("+zidstr+") OR ";
							zidstr="";
							
						}else{
							zidstr=zidstr+useriday[i]+",";
						}
					}
					if(!"".equals(sql)&&"".equals(zidstr)){
						sql=sql.substring(0,sql.lastIndexOf("OR"));
					}else if(!"".equals(sql)&&!"".equals(zidstr)){
						zidstr=zidstr.substring(0, zidstr.length()-1);
						sql=sql+" "+columnstr +" IN ("+zidstr+") ";
					}else{
						sql=" 1=2 ";
					}
				}
			}else{
				sql=" "+columnstr+" = "+idstr;
			}
		}
		return sql;
	}
	
	/**
	 * 
	 * 短信获取查询操作员报表用的主SQL语句
	 * 
	 * @param curUserId
	 * @param depIds
	 * @param idstr
	 * @param mtDataReportVo
	 * @param corpCode
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * @throws Exception
	 * 
	 */
	public String getDateDetailSql(String idstr,MtDataReportVo mtDataReportVo,String corpCode,String reporttype) throws Exception, Exception
	{
		String tablename;
		if(mtDataReportVo.getMstype()!=null &&mtDataReportVo.getMstype()==0)
		{
			tablename = "MT_DATAREPORT";
		}
		else if(mtDataReportVo.getMstype()!=null &&mtDataReportVo.getMstype()==1)
		{
			tablename = "MMS_DATAREPORT";
		}
		else
		{
			return null;
		}
		
		String groupcloumnname;
		if("2".equals(reporttype))
		{
			groupcloumnname=" Y,IMONTH ";
		}
		else
		{
			groupcloumnname=" IYMD ";
		}
		
		// 报表数据查询条件
		String conditionSql = " ";
		
		//运营商条件
		if(mtDataReportVo.getSpnumtype() != null)
		{
			//运营商   0国内  1国外   ""全部
			if(mtDataReportVo.getSpnumtype()==0)
			{
				conditionSql += " AND SPISUNCM IN (0,1,21) ";
			}
			else if(mtDataReportVo.getSpnumtype()==1)
			{
				conditionSql += " AND SPISUNCM=5 ";
			}
		}
		
		String multiSql = "";
		//判断单多企业
		switch(StaticValue.getCORPTYPE())
		{
			case 0:
				//单企业
				multiSql = "";
	            break;
			case 1:
				//多企业 
				multiSql = " LEFT JOIN LF_SP_DEP_BIND SPBIND ON DATAREPORT.USERID=SPBIND.SPUSER ";
				break;
			default:break;
		}
		
		// 时间条件的查询
		conditionSql += " AND IYMD>="+ mtDataReportVo.getSendTime()+ " AND IYMD<="+ mtDataReportVo.getEndTime() + " ";
		
		//总的sql查询字段
		String sql = "SELECT "+groupcloumnname+","+ReportDAO.getPublicCountSql("DATAREPORT")+" FROM "+tablename+" DATAREPORT "
		    +multiSql;
		//获取是否开启p2参数配置
		String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
		//获取不到配置文件值
		if(depcodethird2p2 == null || depcodethird2p2.trim().length() == 0)
		{
			depcodethird2p2 = "false";
		}
		if("true".equals(depcodethird2p2)){
			sql=sql+" LEFT JOIN LF_DEP DEP ON DEP.DEP_CODE_THIRD=DATAREPORT.P2 LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_CODE = DATAREPORT.P1 WHERE ";
		}else{
			sql=sql+" LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_CODE = DATAREPORT.P1 WHERE ";
		}
			
		
		//选择的操作员
		if( null != idstr && 0 != idstr.length()&&!"0".equals(idstr))
		{
			sql += " SYSUSER.USER_ID="+idstr+" ";
		}
		else
		{
			//mySql
			if (StaticValue.DBTYPE == 3||StaticValue.DBTYPE == 4) 
			{
				sql += " COALESCE(SYSUSER.USER_ID,0)=0 ";
			}
			else 
			{
				sql += " (SYSUSER.USER_ID IS NULL ) ";
			}
		}
		
		//判断单多企业
		if( null != corpCode && 0 != corpCode.length() && !"".equals(multiSql) )
		{
			conditionSql += " AND SPBIND.CORP_CODE = '"+corpCode+"'";
		}
		if("true".equals(depcodethird2p2)){
			//判断部门条件
			if( null != mtDataReportVo.getUserName() && 0 != mtDataReportVo.getUserName().length())
			{
				//判断是否选中未知
				if("-3".equals(mtDataReportVo.getUserName()))
				{
					conditionSql += " AND (DEP.DEP_ID IS NULL) ";
				}
				else if(!"-3".equals(mtDataReportVo.getUserName()) && mtDataReportVo.getUserName().contains("-3"))
				{
					if(mtDataReportVo.getDepName().contains(","))
					{
						conditionSql += " AND (DEP.DEP_ID in ("+mtDataReportVo.getUserName()+") OR DEP.DEP_ID IS NULL)";
					}
					else
					{
						String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+mtDataReportVo.getUserName(), StaticValue.EMP_POOLNAME);
						conditionSql += " AND (DEP.DEP_ID IN (SELECT DEP_ID FROM LF_DEP WHERE DEP_PATH LIKE '"+deppath+"%') OR DEP.DEP_ID IS NULL)";
					}
				}
				else
				{
					//获取企业的顶级机构id
					int dep_id = getInt("dep_Id",
							"select dep_id from  lf_dep where corp_code='" + corpCode + "' and dep_level=1", StaticValue.EMP_POOLNAME);
					//企业顶级机构id
					String dep = dep_id + "";
					//页面未选机构的时候，会进来这里
					if(mtDataReportVo.getUserName().contains(","))
					{
						//如果选择的机构是顶级机构，则要把未知机构的查出来
						if (dep.equals(mtDataReportVo.getDepName())) 
						{
							conditionSql += " AND (DEP.DEP_ID in ("+mtDataReportVo.getUserName()+") OR DEP.DEP_ID IS NULL)";
						}else{
							conditionSql += " AND (DEP.DEP_ID in ("+mtDataReportVo.getUserName()+"))";
						}
						
					}
					//页面选一个机构后，会进这里
					else
					{
						String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+mtDataReportVo.getUserName(), StaticValue.EMP_POOLNAME);
						//如果选择的机构是顶级机构，则要把未知机构的查出来
						if (dep.equals(mtDataReportVo.getDepName())) 
						{
							conditionSql += " AND (DEP.DEP_ID IN (SELECT DEP_ID FROM LF_DEP WHERE DEP_PATH LIKE '"+deppath+"%') OR DEP.DEP_ID IS NULL)";
						}else{
							conditionSql += " AND (DEP.DEP_ID IN (SELECT DEP_ID FROM LF_DEP WHERE DEP_PATH LIKE '"+deppath+"%'))";
						}
					}
				}
			}
		}
		
		 //分组
		String groupbysql = "  GROUP BY  "+groupcloumnname+"   ";
				      
		sql += conditionSql + groupbysql;
		return sql;
		
	}
	
	/**
	 * 
	 * 短信获取查询操作员报表用的主SQL语句
	 * 
	 * @param curUserId
	 * @param depIds
	 * @param idstr
	 * @param userareavo
	 * @param corpCode
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * @throws Exception
	 * 
	 */
	public String getAreaDateDetailSql(String idstr,UserAreaRptVo userareavo,String corpCode,String reporttype) throws Exception
	{
		String tablename;
		String gatetype;
		
		if(userareavo.getMstype()!=null &&userareavo.getMstype()==0)
		{
			tablename= "MT_DATAREPORT";
			gatetype=" AND xg.GATETYPE=1 ";
		}
		else if(userareavo.getMstype()!=null &&userareavo.getMstype()==1)
		{
			tablename= "MMS_DATAREPORT";
			gatetype=" AND xg.GATETYPE=2 ";
		}
		else
		{
			return null;
		}
		
		// 报表数据查询条件
		String conditionSql = " ";
		
		//运营商条件
		if(userareavo.getSpnumtype() != null)
		{
			//运营商   0国内  1国外   ""全部
			if(userareavo.getSpnumtype()==0)
			{
				conditionSql += " AND DATAREPORT.SPISUNCM IN (0,1,21) ";
			}
			else if(userareavo.getSpnumtype()==1)
			{
				conditionSql += " AND DATAREPORT.SPISUNCM=5 ";
			}
		}

		//国家代码
		if(userareavo.getAreacode()!=null&&!"".equals(userareavo.getAreacode().trim()))
		{
			conditionSql += " AND ac.AREACODE LIKE '%"+userareavo.getAreacode().trim()+"%' ";
		}
		
		//国家名称
		if(userareavo.getAreaname()!=null&&!"".equals(userareavo.getAreaname().trim())){
			conditionSql += " AND ac.AREANAME LIKE '%"+userareavo.getAreaname().trim()+"%' ";
		}
		
		// 时间条件的查询
		conditionSql += " AND IYMD>="+ userareavo.getSendtime()+ " AND IYMD<="+ userareavo.getEndtime() + " ";
		
		String multiSql = "";
		//判断单多企业
		switch(StaticValue.getCORPTYPE())
		{
			case 0:
				//单企业
				multiSql = "";
	            break;
			case 1:
				//多企业 
				multiSql = " LEFT JOIN LF_SP_DEP_BIND SPBIND ON DATAREPORT.USERID=SPBIND.SPUSER ";
				break;
			default:break;
		}
		
		
		//总的sql查询字段
		String sql = "SELECT ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME,"+ReportDAO.getPublicCountSql("DATAREPORT")+" FROM "+tablename+" DATAREPORT "
		           +multiSql
						+" LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_CODE = DATAREPORT.P1 " +
		           		" LEFT JOIN A_AREACODE ac ON ac.CODE=DATAREPORT.AREACODE LEFT JOIN XT_GATE_QUEUE xg ON xg.SPGATE=DATAREPORT.SPGATE AND xg.SPISUNCM=5 "+gatetype
		           		+ " WHERE ";
		
		//选择的操作员
		if( null != idstr && 0 != idstr.length()&&!"0".equals(idstr))
		{
			sql += " SYSUSER.USER_ID="+idstr+" ";
		}
		else
		{
			//mySql
			if (StaticValue.DBTYPE == 3||StaticValue.DBTYPE == 4) 
			{
				sql += " COALESCE(SYSUSER.USER_ID,0)=0 ";
			}
			else 
			{
				sql += " (SYSUSER.USER_ID IS NULL ) ";
			}
		}
		
		//判断单多企业
		if( null != corpCode && 0 != corpCode.length() && !"".equals(multiSql) )
		{
			conditionSql += " AND SPBIND.CORP_CODE = '"+corpCode+"'";
		}
		
		
		//分组
		String groupbysql = " GROUP BY ac.AREACODE,ac.AREANAME,xg.SPGATE,xg.GATENAME   ";
		
		sql+=conditionSql + groupbysql;
		return sql;
	}
	
	/**
	 * 
	 * 彩信获取查询操作员报表用的主SQL语句
	 * 
	 * @param curUserId
	 * @param depIds
	 * @param userIds
	 * @param mtDataReportVo
	 * @param corpCode
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * @throws Exception
	 * 
	 */
	public String getMMSSql(Long curUserId,String depIds,String userIds,
			MtDataReportVo mtDataReportVo,String corpCode) throws Exception, Exception
	{
		//数据库的判空函数 默认为oracle的判断NULL函数
		String nullFunction = "NVL";
		
		//各种数据库
		switch(StaticValue.DBTYPE)
		{
			case 1:
				//oracle
				nullFunction = "NVL";
				break;
			case 2:
				//sqlserver2005
				nullFunction = "ISNULL";
				break;
			case 3:
				//MYSQL
				nullFunction = "IFNULL";
				break;
			case 4:
				//DB2
				nullFunction = "COALESCE";
				break;
			default:break;
		}
		
		String multiSql = "";
		//判断单多企业
		switch(StaticValue.getCORPTYPE())
		{
			case 0:
				//单企业
				multiSql = "";
	            break;
			case 1:
				//多企业 
				multiSql = " LEFT JOIN LF_MMSACCBIND SPBIND ON DATAREPORT.USERID = SPBIND.MMS_USER ";
				break;
			default:break;
		}
		
		// 时间条件的查询
		String timewheresql = " WHERE DATAREPORT.IYMD>="+ mtDataReportVo.getSendTime()+ " AND DATAREPORT.IYMD<="+ mtDataReportVo.getEndTime() + " ";
		
		//总的sql查询字段
		String sql = "SELECT "+ReportDAO.getPublicCountSql("DATAREPORT")+"," 
		           +"SYSUSER.USER_STATE USER_STATE,SYSUSER.USER_ID USER_ID,MAX(DEP.DEP_ID) DEP_ID,MAX("+nullFunction+"(SYSUSER.NAME,'未知操作员')) NAME,"
		           +"MAX("+nullFunction+"(DEP.DEP_NAME,'未知机构')) DEP_NAME FROM MMS_DATAREPORT DATAREPORT "
		           +  multiSql;
		//获取是否开启p2参数配置
		String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
		//获取不到配置文件值
		if(depcodethird2p2 == null || depcodethird2p2.trim().length() == 0)
		{
		   	depcodethird2p2 = "false";
		}
		if("true".equals(depcodethird2p2)){
		   	sql=sql+" LEFT JOIN LF_DEP DEP ON dep.DEP_CODE_THIRD = DATAREPORT.P2 LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_CODE = DATAREPORT.P1 "+timewheresql+" ";
		}else{
		    sql=sql+" LEFT JOIN LF_SYSUSER SYSUSER ON SYSUSER.USER_CODE = DATAREPORT.P1 LEFT JOIN LF_DEP DEP ON SYSUSER.DEP_ID=DEP.DEP_ID "+timewheresql+" ";
		}

		//权限范围
		String domination = " ";
				    
		//个人权限用，查询当前操作员的相关报表信息
		if( null != curUserId )
		{
			if("true".equals(depcodethird2p2)){
				domination += " AND (SYSUSER.USER_ID= "+curUserId+")";
			}else{
				domination += " AND (SYSUSER.USER_ID= "+curUserId+" OR SYSUSER.USER_ID IS NULL )";
			}
		}
		
		//选择的操作员
		if( null != userIds && 0 != userIds.length())
		{
			String useridsql=getUseridSqlStr(userIds, "SYSUSER.USER_ID");
			if("-1".equals(userIds))
			{
				domination += " AND (SYSUSER.USER_ID IS NULL )";
			}
			else if(!"-1".equals(userIds) && userIds.contains("-1"))
			{
				domination += " AND (("+useridsql+") OR SYSUSER.USER_ID IS NULL )";
			}
			else
			{
				domination += " AND ("+useridsql+" )";
			}
		}
		
		//条件查询
		String conditionSql = domination;
		//判断单多企业
		if( null != corpCode && 0 != corpCode.length() && !"".equals(multiSql) )
		{
			conditionSql += " AND SPBIND.CORP_CODE = '"+corpCode+"'";
		}
		//判断部门条件
		if( null != depIds && 0 != depIds.length())
		{
			//判断是否选中未知
			if("-3".equals(depIds))
			{
				conditionSql += " AND (DEP.DEP_ID IS NULL) ";
			}
			else if(!"-3".equals(depIds) && depIds.contains("-3"))
			{
				if(depIds.contains(","))
				{
					conditionSql += " AND (DEP.DEP_ID in ("+depIds+") OR DEP.DEP_ID IS NULL)";
				}
				else
				{
					String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+depIds, StaticValue.EMP_POOLNAME);
					conditionSql += " AND (DEP.DEP_ID IN (SELECT DEP_ID FROM LF_DEP WHERE DEP_PATH LIKE '"+deppath+"%') OR DEP.DEP_ID IS NULL)";
				}
			}
			else
			{
				//页面未选机构的时候，会进来这里
				if(depIds.contains(","))
				{
					//获取企业的顶级机构id
					int dep_id = getInt("dep_Id",
							"select dep_id from  lf_dep where corp_code='" + corpCode + "' and dep_level=1", StaticValue.EMP_POOLNAME);
					//企业顶级机构id
					String dep = dep_id + "";
					//如果选择的机构是顶级机构，则要把未知机构的查出来
					if (dep.equals(mtDataReportVo.getDepName())) 
					{
						conditionSql += " AND (DEP.DEP_ID in ("+depIds+") OR DEP.DEP_ID IS NULL)";
					}else{
						conditionSql += " AND (DEP.DEP_ID in ("+depIds+"))";
					}
					
				}
				//页面选一个机构后，会进这里
				else
				{
					String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+depIds, StaticValue.EMP_POOLNAME);
					conditionSql += " AND (DEP.DEP_ID IN (SELECT DEP_ID FROM LF_DEP WHERE DEP_PATH LIKE '"+deppath+"%'))";
				}
			}
		}
		
		//运营商条件
		if(mtDataReportVo.getSpnumtype() != null)
		{
			//运营商   0国内  1国外   ""全部
			if(mtDataReportVo.getSpnumtype()==0)
			{
				conditionSql += " AND DATAREPORT.SPISUNCM IN (0,1,21) ";
			}
			else if(mtDataReportVo.getSpnumtype()==1)
			{
				conditionSql += " AND DATAREPORT.SPISUNCM=5 ";
			}
		}
		
		//分组
		String groupbysql = " GROUP BY SYSUSER.USER_ID,SYSUSER.USER_STATE ";
		
		sql+=conditionSql + groupbysql;
		
		return sql;
	}
		
	/**
	 * 
	 * 彩信获取查询操作员报表用的主SQL语句
	 * 
	 * @param curUserId
	 * @param depIds
	 * @param userIds
	 * @param mtDataReportVo
	 * @param corpCode
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * @throws Exception
	 * 
	 */
	public String getMMSSqlBf(Long curUserId,String depIds,String userIds,
			MtDataReportVo mtDataReportVo,String corpCode) throws Exception, Exception
	{
		//总sql定义
		String sql = "";
		String multiSql = "";
		String nullFunction = "NVL";//数据库的判空函数 默认为oracle的判断NULL函数
		//时间查询条件数组
		String[] timeStrCondition = new String[3];
		String sendtime = mtDataReportVo.getSendTime().replaceAll("-", "");
		String endtime=mtDataReportVo.getEndTime().replaceAll("-", "");
		//各种数据库
		switch(StaticValue.DBTYPE)
		{
		case 1:
			//oracle
			nullFunction = "NVL";
//			timeStrCondition[0] = " AND TO_DATE(DATAREPORT."+TableMmsDatareport.IYMD+",'YYYY-MM-DD')>=TO_DATE('"+mtDataReportVo.getSendTime()+"','YYYY-MM-DD') "; 
//			timeStrCondition[1] = " AND TO_DATE(DATAREPORT."+TableMmsDatareport.IYMD+",'YYYY-MM-DD')<=TO_DATE('"+mtDataReportVo.getEndTime()+"','YYYY-MM-DD') ";
//			timeStrCondition[2] = " AND TO_DATE(DATAREPORT."+TableMmsDatareport.IYMD+",'YYYY-MM-DD') BETWEEN TO_DATE('"+mtDataReportVo.getSendTime()+"','YYYY-MM-DD')  AND TO_DATE('"+mtDataReportVo.getEndTime()+"','YYYY-MM-DD') ";
			break;
		case 2:
			//sqlserver2005
			nullFunction = "ISNULL";
//			timeStrCondition[0] = " AND CAST(STR(DATAREPORT."+TableMmsDatareport.IYMD+") AS DATETIME) >= CAST('" + mtDataReportVo.getSendTime()+"'  AS DATETIME)"; 
//			timeStrCondition[1] = " AND CAST(STR(DATAREPORT."+TableMmsDatareport.IYMD+") AS DATETIME) <= CAST('" + mtDataReportVo.getEndTime()+"'   AS DATETIME)";
//			timeStrCondition[2] = " AND CAST(STR(DATAREPORT."+TableMmsDatareport.IYMD+") AS DATETIME) BETWEEN  CAST('"+mtDataReportVo.getSendTime()+"'  AS DATETIME)  AND  CAST('"+mtDataReportVo.getEndTime()+"'  AS DATETIME)";	
			break;
		case 3:
			//MYSQL
			nullFunction = "IFNULL";
//			timeStrCondition[0] = " AND CAST(DATAREPORT."+TableMmsDatareport.IYMD+" AS DATETIME) >= CAST('" + mtDataReportVo.getSendTime()+"'  AS DATETIME)"; 
//			timeStrCondition[1] = " AND CAST(DATAREPORT."+TableMmsDatareport.IYMD+" AS DATETIME) <= CAST('" + mtDataReportVo.getEndTime()+"'   AS DATETIME)";
//			timeStrCondition[2] = " AND CAST(DATAREPORT."+TableMmsDatareport.IYMD+" AS DATETIME) BETWEEN  CAST('"+mtDataReportVo.getSendTime()+"'  AS DATETIME)  AND  CAST('"+mtDataReportVo.getEndTime()+"'  AS DATETIME)";	
			break;
		case 4:
			//DB2
			nullFunction = "COALESCE";
//			timeStrCondition[0] = " AND DATAREPORT."+TableMmsDatareport.IYMD+">="+mtDataReportVo.getSendTime().replace("-", "")+" "; 
//			timeStrCondition[1] = " AND DATAREPORT."+TableMmsDatareport.IYMD+"<="+mtDataReportVo.getEndTime().replace("-", "")+" ";
//			timeStrCondition[2] = " AND DATAREPORT."+TableMmsDatareport.IYMD+" BETWEEN "+mtDataReportVo.getSendTime().replace("-", "")+"  AND "+mtDataReportVo.getEndTime().replace("-", "")+" ";
		
			break;
			default:break;
		}
		
		timeStrCondition[0] = " WHERE DATAREPORT." + TableMmsDatareport.IYMD+ ">="+ sendtime + " ";
		timeStrCondition[1] = " WHERE DATAREPORT." + TableMmsDatareport.IYMD+ "<="+ endtime +" ";
		timeStrCondition[2] = " WHERE DATAREPORT." + TableMmsDatareport.IYMD+ " BETWEEN  "+ sendtime+ "  AND  "+ endtime + " ";
		
		switch(StaticValue.getCORPTYPE())
		{
		case 0:
			//单企业
			multiSql = "";
            break;
		case 1:
			//多企业 
			multiSql = " LEFT JOIN "+TableLfMmsAccbind.TABLE_NAME+" SPBIND ON DATAREPORT."+TableMmsDatareport.USER_ID+"="+TableLfMmsAccbind.MMS_USER+" ";
			break;
			default:break;
		}
		
		//时间是必有条件  让其作为where后第一个条件
		String timewheresql=" ";
		//时间条件的查询
		if( (0 != mtDataReportVo.getSendTime().length() && 0 == mtDataReportVo.getEndTime().length()))
		{
			timewheresql += timeStrCondition[0];
		
		}else if ( 0 == mtDataReportVo.getSendTime().length() && 0 != mtDataReportVo.getEndTime().length() )
		{
			timewheresql += timeStrCondition[1];
		    
		}else if (0 != mtDataReportVo.getSendTime().length() && 0 != mtDataReportVo.getEndTime().length() )
		{
			timewheresql += timeStrCondition[2] ;
		}else
		{
			//如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();
			
			if( null != yearAndMonth )
			{
				timewheresql += " WHERE DATAREPORT."+TableMmsDatareport.Y+"="+yearAndMonth[0]+" " +
						        " AND DATAREPORT."+TableMmsDatareport.IMONTH+"="+yearAndMonth[1]+"";
			}
		}
		
		//总的sql查询字段
		sql = "SELECT "+ReportDAO.getPublicCountSql("DATAREPORT")
				   +", SYSUSER."+TableLfSysuser.USER_STATE+" USER_STATE, "
		           +" SYSUSER."+TableLfSysuser.USER_ID+" USER_ID, "
		           +" DEP."+TableLfDep.DEP_ID+" DEP_ID, "
		           +" MAX("+nullFunction+"(SYSUSER."+TableLfSysuser.NAME+",'未知操作员')) NAME,"
		           +" MAX("+nullFunction+"(DEP."+TableLfDep.DEP_NAME+",'未知机构')) DEP_NAME "
		           +" FROM "+TableMmsDatareport.TABLE_NAME+" DATAREPORT "
		           +  multiSql
		           +" LEFT JOIN "+TableLfSysuser.TABLE_NAME+" SYSUSER ON SYSUSER."+TableLfSysuser.USER_CODE+" = DATAREPORT."+TableMmsDatareport.P1
		           +" LEFT JOIN "+TableLfDep.TABLE_NAME+" DEP ON SYSUSER."+TableLfSysuser.DEP_ID+" = DEP."+TableLfDep.DEP_ID
		           +timewheresql+" ";
		
		
		//String countSql = "select count(*) totalcount FROM (";
		String domination = " ";
		String conditionSql = "  ";//条件查询
		 //分组
		String baseSql = "  GROUP BY " 
				       //+"DATAREPORT."+TableMmsDatareport.IYMD+","
				       + "  SYSUSER.USER_ID,SYSUSER.USER_STATE,DEP.DEP_ID ";
				    
		// 为了修改 彩信查询跳转到空白页面 而去掉
//	   if( !"".equals(multiSql))
//	   {
//		   //非单企业
//		   baseSql+= "   ORDER BY DEP."+TableLfDep.DEP_ID+" ASC ";
//	   }
				      
	 //个人权限用，查询当前操作员的相关报表信息
		if( null != curUserId )
		{
			domination += " AND (SYSUSER."+TableLfSysuser.USER_ID+" = "+curUserId+"  OR SYSUSER."+TableLfSysuser.USER_ID+" IS NULL )";
		}
		
		//选择的操作员
		if( null != userIds && 0 != userIds.length())
		{
			String useridsql=getUseridSqlStr(userIds, "SYSUSER.USER_ID");
			if("-1".equals(userIds))
			{
				domination += " AND (SYSUSER.USER_ID IS NULL )";
			}else if(!"-1".equals(userIds) && userIds.contains("-1"))
			{
				domination += " AND (("+useridsql+") OR SYSUSER.USER_ID IS NULL )";
			}else
			{
				domination += " AND ("+useridsql+" )";
			}
			
		}
		
		conditionSql+=domination;
		//判断单多企业
		if( null != corpCode && 0 != corpCode.length() && !"".equals(multiSql) )
		{
			conditionSql += " AND SPBIND."+TableLfSpDepBind.CORP_CODE+" = '"+corpCode+"'";
		}
		//判断部门条件
		if( null != depIds && 0 != depIds.length())
		{
			//判断是否选中未知
			if("-3".equals(depIds))
			{
				conditionSql += " AND (DEP."+TableLfDep.DEP_ID+" IS NULL)";
			}else if(!"-3".equals(depIds) && depIds.contains("-3"))
			{
				if(depIds.contains(",")){
					conditionSql += " AND (DEP."+TableLfDep.DEP_ID+" in ("+depIds+") OR DEP.DEP_ID IS NULL)";
				}else{
					//String depSql=new DepDAO().getChildUserDepByParentID(Long.parseLong(depIds),"DEP."+TableLfDep.DEP_ID);
					//conditionSql += " AND (("+depSql+") OR DEP.DEP_ID IS NULL)";
					String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+depIds, StaticValue.EMP_POOLNAME);
					conditionSql += " AND ((DEP.DEP_ID IN (SELECT DEP_ID FROM LF_DEP WHERE DEP_PATH LIKE '"+deppath+"%')) OR DEP.DEP_ID IS NULL)";
				}
				
				
			}else{
				if(depIds.contains(",")){
					conditionSql += " AND (DEP."+TableLfDep.DEP_ID+" in ("+depIds+")  OR DEP.DEP_ID IS NULL)";
				}else{
//					String depSql=new DepDAO().getChildUserDepByParentID(Long.parseLong(depIds),"DEP."+TableLfDep.DEP_ID);
//					if(!"".equals(depSql.trim()) && null!= depSql){
//						conditionSql += " AND ("+depSql+")";
//					}
					String deppath = getString("DEP_PATH", "SELECT DEP_PATH FROM LF_DEP WHERE DEP_ID="+depIds, StaticValue.EMP_POOLNAME);
					conditionSql += " AND (DEP.DEP_ID IN (SELECT DEP_ID FROM LF_DEP WHERE DEP_PATH LIKE '"+deppath+"%'))";
				}
				
				
			}
			//conditionSql += " AND (DEP."+TableLfDep.DEP_ID+" in ("+depIds+") OR DEP.DEP_ID IS NULL)";
			
		}
		
		
		sql+=conditionSql;
		sql+=baseSql;
		return sql;
	}
			
	/**
	 * 
	 * 获取当前的年份和月份的数组，用于限制查询当前月份的操作员报表
	 * 
	 * @return
	 */
	public String[] getYearAndMonth()
	{
		String[] datetime= new String[2];
		
		try{
			//获取当前时间对象
			Calendar cal = Calendar.getInstance();
			Integer month = cal.get(Calendar.MONTH);
			//月份加一
			month+=1;
			datetime[0] = String.valueOf(cal.get(Calendar.YEAR));
			datetime[1] = String.valueOf(month);
		}catch(Exception e){
			EmpExecutionContext.error(e,"操作员统计报表获取年份和月份数组异常");
		}
		
		
		return datetime;
	}
	
 
	
	
}
