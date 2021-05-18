package com.montnets.emp.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.report.vo.DynParmReportVo;
import com.montnets.emp.table.report.TableMmsDatareport;
import com.montnets.emp.table.report.TableMtDatareport;
import com.montnets.emp.table.selfparam.TableLfWgParamConfig;
import com.montnets.emp.util.PageInfo;

/**
 * 动态参数报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:46:41
 * @description
 */
public class GenericDynParamVoDAO extends SuperDAO {
	
	

	
	 
	
	 
	/**
	 * 动态参数报表
	 * @param dynParmReportVo
	 * @param paramType
	 * @param paramSubNum
	 * @param paramValues
	 * 					参数值
	 * @return
	 */
	public String getSql(DynParmReportVo dynParmReportVo,String paramType,Integer paramSubNum)
	{
 		String sql = "";
 	
		String[] timeStrCondition = new String[3];
		String[] paramSearch = new String[3];
		String[] groupByStr = new String[3];
		String domination = " ";
		String conditionSql = "  ";//条件查询
 		String baseSql = " " ;
 		String leftJoinStr = "";
 		String P2 = TableMtDatareport.P2;
		String P3 = TableMtDatareport.P3;
		String P4 = TableMtDatareport.P4;

		String sendtime = dynParmReportVo.getSendTime().replaceAll("-", "");
		String endtime=dynParmReportVo.getEndTime().replaceAll("-", "");
		
 		//时间查询
		switch(StaticValue.DBTYPE)
		{
		case 1:
			//oracle
//  			timeStrCondition[0] = " AND TO_DATE(DATAREPORT."+TableMtDatareport.IYMD+",'YYYY-MM-DD')>=TO_DATE('"+dynParmReportVo.getSendTime()+"','YYYY-MM-DD') "; 
//			timeStrCondition[1] = " AND TO_DATE(DATAREPORT."+TableMtDatareport.IYMD+",'YYYY-MM-DD')<=TO_DATE('"+dynParmReportVo.getEndTime()+"','YYYY-MM-DD') ";
//			timeStrCondition[2] = " AND TO_DATE(DATAREPORT."+TableMtDatareport.IYMD+",'YYYY-MM-DD') BETWEEN TO_DATE('"+dynParmReportVo.getSendTime()+"','YYYY-MM-DD')  AND TO_DATE('"+dynParmReportVo.getEndTime()+"','YYYY-MM-DD') ";
			
			switch( paramSubNum )
			{
			
			case 1:
				groupByStr[0] = "   SUBSTR(DATAREPORT."+P2+",0,(CASE WHEN INSTR(DATAREPORT."+P2+",'#')<1 THEN 100 ELSE INSTR(DATAREPORT."+P2+",'#')-1 END)) ";
				groupByStr[1] = "   SUBSTR(DATAREPORT."+P3+",0,(CASE WHEN INSTR(DATAREPORT."+P3+",'#')<1 THEN 100 ELSE INSTR(DATAREPORT."+P3+",'#')-1 END)) ";
				groupByStr[2] = "   SUBSTR(DATAREPORT."+P4+",0,(CASE WHEN INSTR(DATAREPORT."+P4+",'#')<1 THEN 100 ELSE INSTR(DATAREPORT."+P4+",'#')-1 END)) ";
				
				break;
			case 2:
				groupByStr[0] = "   SUBSTR(DATAREPORT."+P2+",(CASE WHEN INSTR(DATAREPORT."+P2+",'#')<1 THEN 100 ELSE INSTR(DATAREPORT."+P2+",'#')+1 END),LENGTH(DATAREPORT."+P2+")) ";
				groupByStr[1] = "   SUBSTR(DATAREPORT."+P3+",(CASE WHEN INSTR(DATAREPORT."+P3+",'#')<1 THEN 100 ELSE INSTR(DATAREPORT."+P3+",'#')+1 END),LENGTH(DATAREPORT."+P3+")) ";
				groupByStr[2] = "   SUBSTR(DATAREPORT."+P4+",(CASE WHEN INSTR(DATAREPORT."+P4+",'#')<1 THEN 100 ELSE INSTR(DATAREPORT."+P4+",'#')+1 END),LENGTH(DATAREPORT."+P4+")) ";
			 
				break;
				
				default:break;
			}
			paramSearch[0] = " ,"+groupByStr[0];
			paramSearch[1] = " ,"+groupByStr[1];
			paramSearch[2] = " ,"+groupByStr[2];
			
			
			break;
		case 2:
			//sqlserver
//  			timeStrCondition[0] = " AND CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) >= CAST('" + dynParmReportVo.getSendTime()+"' AS DATETIME)"; 
//			timeStrCondition[1] = " AND CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) <= CAST('" + dynParmReportVo.getEndTime()+"'   AS DATETIME)";
//			timeStrCondition[2] = " AND CAST(STR(DATAREPORT."+TableMtDatareport.IYMD+") AS DATETIME) BETWEEN  CAST('"+dynParmReportVo.getSendTime()+"'  AS DATETIME)  AND  CAST('"+dynParmReportVo.getEndTime()+"'  AS DATETIME)";	
//			
			
			switch( paramSubNum )
			{
			
			case 1:
				groupByStr[0] = "   SUBSTRING(DATAREPORT."+P2+",0,(CASE WHEN CHARINDEX('#',DATAREPORT."+P2+")<1 THEN 100 ELSE CHARINDEX('#',DATAREPORT."+P2+") END)) ";
				groupByStr[1] = "   SUBSTRING(DATAREPORT."+P3+",0,(CASE WHEN CHARINDEX('#',DATAREPORT."+P3+")<1 THEN 100 ELSE CHARINDEX('#',DATAREPORT."+P3+") END)) ";
				groupByStr[2] = "   SUBSTRING(DATAREPORT."+P4+",0,(CASE WHEN CHARINDEX('#',DATAREPORT."+P4+")<1 THEN 100 ELSE CHARINDEX('#',DATAREPORT."+P4+") END)) ";
				
				break;
			case 2:
				groupByStr[0] = "   SUBSTRING(DATAREPORT."+P2+",(CASE WHEN CHARINDEX('#',DATAREPORT."+P2+")<1 THEN 100 ELSE CHARINDEX('#',DATAREPORT."+P2+")+1 END),64) ";
				groupByStr[1] = "   SUBSTRING(DATAREPORT."+P3+",(CASE WHEN CHARINDEX('#',DATAREPORT."+P3+")<1 THEN 100 ELSE CHARINDEX('#',DATAREPORT."+P3+")+1 END),64) ";
				groupByStr[2] = "   SUBSTRING(DATAREPORT."+P4+",(CASE WHEN CHARINDEX('#',DATAREPORT."+P4+")<1 THEN 100 ELSE CHARINDEX('#',DATAREPORT."+P4+")+1 END),64) ";
			 
				break;
				
				default:break;
			}
			paramSearch[0] = " ,"+groupByStr[0];
			paramSearch[1] = " ,"+groupByStr[1];
			paramSearch[2] = " ,"+groupByStr[2];
			
			break;
		case 3:
			//MYSQL
//			timeStrCondition[0] = " AND CAST(DATAREPORT."+TableMtDatareport.IYMD+" AS DATETIME) >= CAST('" + dynParmReportVo.getSendTime()+"' AS DATETIME)"; 
//			timeStrCondition[1] = " AND CAST(DATAREPORT."+TableMtDatareport.IYMD+" AS DATETIME) <= CAST('" + dynParmReportVo.getEndTime()+"'   AS DATETIME)";
//			timeStrCondition[2] = " AND CAST(DATAREPORT."+TableMtDatareport.IYMD+" AS DATETIME) BETWEEN  CAST('"+dynParmReportVo.getSendTime()+"'  AS DATETIME)  AND  CAST('"+dynParmReportVo.getEndTime()+"'  AS DATETIME)";	
//			
			
			switch( paramSubNum )
			{
			
			case 1:
				groupByStr[0] = "   substring_index(DATAREPORT."+P2+",'#',1) ";
				groupByStr[1] = "   substring_index(DATAREPORT."+P3+",'#',1) ";
				groupByStr[2] = "   substring_index(DATAREPORT."+P4+",'#',1) ";
				
				break;
			case 2:
				groupByStr[0] = "   SUBSTRING(DATAREPORT."+P2+", length(substring_index(DATAREPORT."+P2+",'#',1))+2) ";
				groupByStr[1] = "   SUBSTRING(DATAREPORT."+P3+", length(substring_index(DATAREPORT."+P3+",'#',1))+2) ";
				groupByStr[2] = "   SUBSTRING(DATAREPORT."+P4+", length(substring_index(DATAREPORT."+P4+",'#',1))+2) ";
			 
				break;
				
				default:break;
			}
			paramSearch[0] = " ,"+groupByStr[0];
			paramSearch[1] = " ,"+groupByStr[1];
			paramSearch[2] = " ,"+groupByStr[2];
			
			
			break;
		case 4:
			//DB2
//			timeStrCondition[0] = " AND  DATAREPORT."+TableMtDatareport.IYMD+"  >= " + dynParmReportVo.getSendTime().replace("-", "")+" "; 
//			timeStrCondition[1] = " AND  DATAREPORT."+TableMtDatareport.IYMD+"  <= " + dynParmReportVo.getEndTime().replace("-", "")+"  ";
//			timeStrCondition[2] = " AND  DATAREPORT."+TableMtDatareport.IYMD+"  BETWEEN  "+dynParmReportVo.getSendTime().replace("-", "")+"  AND  "+dynParmReportVo.getEndTime().replace("-", "")+" ";	
//			
			
			switch( paramSubNum )
			{
			
				
			case 1:
				groupByStr[0] = "   SUBSTR(DATAREPORT."+P2+",1,(CASE WHEN LOCATE(DATAREPORT."+P2+",'#')<1 THEN 100 ELSE LOCATE(DATAREPORT."+P2+",'#')-1 END)) ";
				groupByStr[1] = "   SUBSTR(DATAREPORT."+P3+",1,(CASE WHEN LOCATE(DATAREPORT."+P3+",'#')<1 THEN 100 ELSE LOCATE(DATAREPORT."+P3+",'#')-1 END)) ";
				groupByStr[2] = "   SUBSTR(DATAREPORT."+P4+",1,(CASE WHEN LOCATE(DATAREPORT."+P4+",'#')<1 THEN 100 ELSE LOCATE(DATAREPORT."+P4+",'#')-1 END)) ";
				
				break;
			case 2:
				groupByStr[0] = "   SUBSTR(DATAREPORT."+P2+",(CASE WHEN LOCATE(DATAREPORT."+P2+",'#')<1 THEN 100 ELSE LOCATE(DATAREPORT."+P2+",'#')+1 END),LENGTH(DATAREPORT."+P2+")) ";
				groupByStr[1] = "   SUBSTR(DATAREPORT."+P3+",(CASE WHEN LOCATE(DATAREPORT."+P3+",'#')<1 THEN 100 ELSE LOCATE(DATAREPORT."+P3+",'#')+1 END),LENGTH(DATAREPORT."+P3+")) ";
				groupByStr[2] = "   SUBSTR(DATAREPORT."+P4+",(CASE WHEN LOCATE(DATAREPORT."+P4+",'#')<1 THEN 100 ELSE LOCATE(DATAREPORT."+P4+",'#')+1 END),LENGTH(DATAREPORT."+P4+")) ";
			 
				break;
				
				default:break;
			}
			paramSearch[0] = " ,"+groupByStr[0];
			paramSearch[1] = " ,"+groupByStr[1];
			paramSearch[2] = " ,"+groupByStr[2];
			
			break;
			default:break;
		}
		
	//sql主句
		sql = "SELECT"+ sumFields();
	
		
		//参数判断
		if( "Param2".equalsIgnoreCase(paramType))
		{
			baseSql +="  GROUP BY "+ groupByStr[0];
			sql += paramSearch[0];
			leftJoinStr = TableMtDatareport.P2;
			
		}else if("Param3".equalsIgnoreCase(paramType))
		{
			baseSql +="  GROUP BY " + groupByStr[1];
			sql += paramSearch[1];
			leftJoinStr = TableMtDatareport.P3;

		}
		else if("Param4".equalsIgnoreCase(paramType))
		{
			baseSql += "  GROUP BY "+ groupByStr[2];
			sql += paramSearch[2];
			leftJoinStr = TableMtDatareport.P4;

		}
 		
		
		sql +=" PA "
		    +"  FROM "+TableMtDatareport.TABLE_NAME+" DATAREPORT ";

		//条件
		conditionSql+=domination;
		
	    
		
		timeStrCondition[0] = " WHERE DATAREPORT." + TableMmsDatareport.IYMD+ ">="+ sendtime + " ";
		timeStrCondition[1] = " WHERE DATAREPORT." + TableMmsDatareport.IYMD+ "<="+ endtime +" ";
		timeStrCondition[2] = " WHERE DATAREPORT.IYMD>="+ sendtime+ " AND DATAREPORT.IYMD<="+ endtime + " ";
		
		String timewheresql=" ";
	  
		//时间条件的查询
		if( (0 != dynParmReportVo.getSendTime().length() && 0 == dynParmReportVo.getEndTime().length()))
		{
			timewheresql += timeStrCondition[0];
		
		}else if ( 0 == dynParmReportVo.getSendTime().length() && 0 != dynParmReportVo.getEndTime().length() )
		{
			timewheresql += timeStrCondition[1];
		    
		}else if (0 != dynParmReportVo.getSendTime().length() && 0 != dynParmReportVo.getEndTime().length() )
		{
			timewheresql += timeStrCondition[2] ;
		}else
		{
			//如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();
			
			if( null != yearAndMonth )
			{
				timewheresql += " WHERE DATAREPORT."+TableMtDatareport.Y+"="+yearAndMonth[0]+"" +
						        " AND DATAREPORT."+TableMtDatareport.IMONTH+"="+yearAndMonth[1]+"";
			}
		}
		
		
		//条件
		sql+=timewheresql;
		sql+=conditionSql;
		sql+=baseSql;
		//总sql
	    sql="SELECT"+sumFields()+",PARAMCONF."+TableLfWgParamConfig.PARAMVALUE+" PA ,PARAMCONF."+TableLfWgParamConfig.PARAMNAME+" PARAMNAME"+" FROM ("+sql+") T1"
	    +" LEFT JOIN "+TableLfWgParamConfig.TABLE_NAME+" PARAMCONF ON T1.PA"
		+" = PARAMCONF."+TableLfWgParamConfig.PARAMVALUE+" WHERE "
		+" (PARAMCONF."+TableLfWgParamConfig.PARAM+"='"+paramType+"' OR PARAMCONF."+TableLfWgParamConfig.PARAM+" IS NULL) "
		+" AND  (PARAMCONF."+TableLfWgParamConfig.PARAMSUBNUM+"="+paramSubNum+" OR PARAMCONF."+TableLfWgParamConfig.PARAMSUBNUM+" IS NULL ) "
	    +" GROUP BY PARAMCONF."+TableLfWgParamConfig.PARAMNAME+" ,PARAMCONF."+TableLfWgParamConfig.PARAMVALUE;
	    
	 /*   if( null != paramValues && 0 != paramValues.length())
	    {
	    	sql +="  AND PARAMCONF."+TableLfWgParamConfig.PARAMVALUE+" IN ("+paramValues+")";
	    }*/
	   // sql+="   ;
	    
 	    //System.out.println(sql);
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
			Calendar cal = Calendar.getInstance();
			Integer month = cal.get(Calendar.MONTH);
			month+=1;
			//年
			datetime[0] = String.valueOf(cal.get(Calendar.YEAR));
			//月
			datetime[1] = String.valueOf(month);
		}catch(Exception e){
			//异常
			EmpExecutionContext.error(e,"自定义参数统计报表获取年份和月份数组异常");
		}
				
		return datetime;
	}
	
	
	
	
 

	/**
	 * 
	 * 
	 * 
	 * @param paramType
	 * @param paramSubNum
	 * @param dynParmReportVo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<DynParmReportVo> getMtDataReportInfo(String paramType,Integer paramSubNum,DynParmReportVo dynParmReportVo,PageInfo pageInfo) throws Exception
	{
		
		
		String sql = this.getSql(dynParmReportVo, paramType, paramSubNum);//获取操作员报表用的sql语句
		
		String countSql = "select count(*) totalcount FROM (";
		
		countSql += sql;
	 	//countSql += conditionSql+baseSql;
	 	countSql +=" ) A";

	 	//System.out.println(sql);
	 	List<DynParmReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
	 			DynParmReportVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnList;
	}
  
	
	/**
	 * @param paramType
	 * @param paramSubNum
	 * @param dynParmReportVo
	 * @return
	 * @throws Exception
	 */
	public List<DynParmReportVo> getMtDataReportInfo_unpage(String paramType,Integer paramSubNum,DynParmReportVo dynParmReportVo) throws Exception
	{
		
		String sql = this.getSql(dynParmReportVo, paramType, paramSubNum);//获取操作员报表用的sql语句
		
	 	List<DynParmReportVo> returnList = findVoListBySQL(
	 			DynParmReportVo.class, sql,
						StaticValue.EMP_POOLNAME);
		return returnList;
	}
  
 

	/**
	 * @param paramType
	 * @param paramSubNum
	 * @param dynParmReportVo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<DynParmReportVo> getMtDataReportInfoUnPage(String paramType,Integer paramSubNum,
			DynParmReportVo dynParmReportVo) throws Exception
	{
		
				String sql = this.getSql(dynParmReportVo, paramType, paramSubNum);//获取操作员报表用的sql语句
				
				String countSql = "select count(*) totalcount FROM (";
				
				countSql += sql;
			 	//countSql += conditionSql+baseSql;
			 	countSql +=" ) A";

			 	
			 	List<DynParmReportVo> returnList = findVoListBySQL(
			 			DynParmReportVo.class, sql,  
								StaticValue.EMP_POOLNAME);
			 	
				return returnList;
	}
	
	
	public long[] findSumCount(String paramType,Integer paramSubNum,
			DynParmReportVo dynParmReportVo,PageInfo pageInfo) throws Exception
	{
        int count = sumFieldsCount();
        long[] arrays = new long[count];

		String fieldSql = "SELECT "+sumFields()+" FROM  ";
		
		String sql =" ("+this.getSql( dynParmReportVo, paramType, paramSubNum )+" ) MDREPORT";
		
		
   
	    sql = fieldSql+sql;
	    
	    //System.out.println(sql);
	    
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next())
			{
                arrays = sumFieldsArrays(rs);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询动态参数合计处理异常");
			throw e;
		} finally
		{
			close(rs, ps, conn);
		}
			return arrays;
	}

    /**
     * 统计字段
     * @return
     */
    public String sumFields()
    {
        return " SUM(ICOUNT) ICOUNT,SUM(RSUCC) RSUCC,SUM(RFAIL1) RFAIL1,SUM(RFAIL2) RFAIL2,SUM(RNRET) RNRET ";
    }


    public int sumFieldsCount()
    {
        return sumFields().split(",").length;
    }
    /**
     * 统计字段返回数组
     * @return
     */
    public long[] sumFieldsArrays(ResultSet rs) throws SQLException {
        int count = sumFieldsCount();
        long[] arrays = new long[count];
        for(int i = 0;i<arrays.length;i++)
        {
            arrays[i] = rs.getLong(i+1);
        }
        return arrays;
    }
	
	
}
