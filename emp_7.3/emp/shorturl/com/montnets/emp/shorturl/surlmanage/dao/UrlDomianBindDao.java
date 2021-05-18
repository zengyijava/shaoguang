package com.montnets.emp.shorturl.surlmanage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.aliyun.oss.model.RoutingRule.Condition;
import com.ibm.db2.jcc.b.SqlException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomain;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomainCorp;
import com.montnets.emp.shorturl.surlmanage.vo.LfDomainCorpVo;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.ArrayUtils;


public class UrlDomianBindDao extends SuperDAO{
	IEmpTransactionDAO empTransDao = new DataAccessDriver().getEmpTransDAO();
	public List<LfDomainCorpVo> getDomainCorpVos(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo)throws Exception{
		
		 String sql = "select c.CORP_ID,c.CORP_CODE,c.CORP_NAME," +
				"d.ID,d.DOMAIN,l.FLAG,l.CREATE_UID,l.CREATE_USER,l.CREATE_TM,l.UPDATE_TM "+
                "from LF_corp c "+
                "left join LF_DOMAIN_CORP l on c.CORP_CODE = l.CORP_CODE "+
                "left join LF_DOMAIN d on l.DOMAIN_ID = d.ID "+
                "where d.ID is not null ";
		
		String conditionSql = getConditionSql(conditionMap);

		sql = sql + conditionSql;
		
		String countSql = "select count(*) totalcount FROM (";
        countSql += sql;
        countSql += " ) A";
 
		List<LfDomainCorpVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				LfDomainCorpVo.class, sql.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		
		return returnList;
		
	}
	
	/**
	 * 组装SQL过滤语句
	 * @param conditionMap
	 * @return
	 */
	private String getConditionSql(LinkedHashMap<String, String> conditionMap) throws Exception{
		String sql = " ";
		if (conditionMap.size()<1) {
			return sql;
		}
		
		//企业id
		if (conditionMap.get("corpID")!=null&&!"".equals(conditionMap.get("corpID").trim())) {
			sql += " and c.CORP_ID ='"+conditionMap.get("corpID")+"' ";
		}
		//企业编号
		if (conditionMap.get("corpCode")!=null&&!"".equals(conditionMap.get("corpCode").trim())) {
			sql += " and c.CORP_CODE like'%"+conditionMap.get("corpCode")+"%' ";
		}
		//企业名称
		if (conditionMap.get("corpName")!=null&&!"".equals(conditionMap.get("corpName").trim())) {
			sql += " and c.CORP_NAME like'%"+conditionMap.get("corpName").trim()+"%' ";
		}
		//状态
		String flag = conditionMap.get("flag");
		if (flag!=null&&!"".equals(flag)) {
			
			sql += " and l.FLAG="+flag;
		}

		//短域名
		if (conditionMap.get("domain")!=null&&!"".equals(conditionMap.get("domain").trim())) {
			sql += " and d.DOMAIN like'%"+conditionMap.get("domain").trim()+"%'";
		}
		
		//开始时间
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		if (conditionMap.get("startTime")!=null&&!"".equals(conditionMap.get("startTime").trim())) {
			
			sql += " and l.CREATE_TM >= "+genericDao.getTimeCondition(conditionMap.get("startTime").trim());
		}
		//结束时间
		if (conditionMap.get("recvtime")!=null&&!"".equals(conditionMap.get("recvtime").trim())) {
			sql += " and l.CREATE_TM <= "+genericDao.getTimeCondition(conditionMap.get("recvtime").trim());
		}
		
		return sql ;
	}
	
	public boolean insert(String addcorpCode, String domainId,Long createUid,Long updateUid,String opUser) {
        //兼容多种数据库处理
        String multiSql = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? "NOW()" :
                StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE ? "GETDATE()" :
                        StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE ? "SYSDATE" : "CURRENT DATE";
		String sql = "insert into LF_DOMAIN_CORP(CORP_CODE,DOMAIN_ID,CREATE_UID,CREATE_USER,CREATE_TM,UPDATE_UID,UPDATE_TM )values(?,?,?,?,"+ multiSql +",?," + multiSql + ")";
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = -1;
		boolean result = false;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			empTransDao.beginTransaction(conn);
			ps = conn.prepareStatement(sql);
			ps.setString(1, addcorpCode);
			ps.setLong(2, Long.valueOf(domainId));
			ps.setLong(3, createUid);
			ps.setString(4, opUser);
			ps.setLong(5, updateUid);
			
			rs = ps.executeUpdate();
			if (rs>0) {
				result = true;
			}
			empTransDao.commitTransaction(conn);
            empTransDao.closeConnection(conn);
  
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除链接地址失败！");
		}finally{
			try {
				close(null,ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "删除链接地址操作-关闭数据库连接异常");
			}
		}
		return result;
	}
	
	public boolean delete(String domainId,String addcorpCode) {
		String sql = "delete from  LF_DOMAIN_CORP where DOMAIN_ID in ("+domainId+") and CORP_CODE ="+addcorpCode;
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = -1;
		boolean result = false;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			empTransDao.beginTransaction(conn);
			ps = conn.prepareStatement(sql);
			rs = ps.executeUpdate();
			if (rs>0) {
				result = true;
			}
			empTransDao.commitTransaction(conn);
            empTransDao.closeConnection(conn);
  
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除链接地址失败！");
		}finally{
			try {
				close(null,ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "删除链接地址操作-关闭数据库连接异常");
			}
		}
		return result;
	}
	
	public boolean updateTime(String addcorpCode,Long updateUid) {
        //兼容多种数据库处理
        String multiSql = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? "NOW()" :
                StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE ? "GETDATE()" :
                        StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE ? "SYSDATE" : "CURRENT DATE";
		String sql = "update LF_DOMAIN_CORP set UPDATE_TM = " + multiSql + ", UPDATE_UID = "+updateUid+" where CORP_CODE = ("+addcorpCode+")";
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = -1;
		boolean result = false;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			empTransDao.beginTransaction(conn);
			ps = conn.prepareStatement(sql);
			rs = ps.executeUpdate();
			if (rs>0) {
				result = true;
			}
			empTransDao.commitTransaction(conn);
            empTransDao.closeConnection(conn);
  
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除链接地址失败！");
		}finally{
			try {
				close(null,ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "删除链接地址操作-关闭数据库连接异常");
			}
		}
		return result;
	}
	
	//根据条件查找映射表
	public List<Long>  getDomainBindIds(LinkedHashMap<String, String> conditionMap) {
		List<Long> list = new ArrayList<Long>();
      
       Connection conn = null;
       PreparedStatement ps = null;
       ResultSet rs = null;

       try {
           StringBuffer sql = new StringBuffer();
           StringBuffer conditonSql = new StringBuffer();
           
           sql.append("select CORP_CODE,DOMAIN_ID from LF_DOMAIN_CORP "); 
           conditonSql.append(" WHERE CORP_CODE in ("+conditionMap.get("corpCode")+")");
           
           
           String sqlStr = sql.toString() + conditonSql.toString();
           conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
           ps = conn.prepareStatement(sqlStr);
           rs = ps.executeQuery();
           while(rs.next()){
        	   list.add(rs.getLong("DOMAIN_ID"));
           }

       } catch (NumberFormatException e) {
           EmpExecutionContext.error(e, "获取发送短信成功数和失败数中格式转换异常"+e.getMessage());
       }catch (Exception e) {
           EmpExecutionContext.error(e, "获取发送短信总数量失败 :"+e.getMessage());
       }finally{
           try {
               super.close(rs, ps, conn);
           } catch (Exception e) {
               EmpExecutionContext.error(e, "关闭获取发送短信总数量链接失败:"+e.getMessage());
           }
       }
       return list;
   }
	
	//根据条件查找短域名信息
	public List<LfDomain> getDomains(String ids) {
		List<LfDomain> domains= new ArrayList<LfDomain>();
		LfDomain lfDomain = new LfDomain();
       Connection conn = null;
       PreparedStatement ps = null;
       ResultSet rs = null;

       try {
           StringBuffer sql = new StringBuffer();
           StringBuffer conditonSql = new StringBuffer();
           
           sql.append("select ID,DOMAIN,FLAG,LEN_EXTEN FROM LF_DOMAIN "); 
           conditonSql.append(" WHERE ID in ("+ids+")");
           
           
           String sqlStr = sql.toString() + conditonSql.toString();
           conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
           ps = conn.prepareStatement(sqlStr);
           rs = ps.executeQuery();
           while(rs.next()){
        	   lfDomain = new LfDomain();
        	   lfDomain.setId(rs.getLong("ID"));
        	   lfDomain.setDomain(rs.getString("DOMAIN"));
        	   lfDomain.setFlag(rs.getInt("FLAG"));
        	   lfDomain.setLenExten(rs.getLong("LEN_EXTEN"));
        	   domains.add(lfDomain);
        	   
           }

       } catch (NumberFormatException e) {
           EmpExecutionContext.error(e, "获取发送短信成功数和失败数中格式转换异常"+e.getMessage());
       }catch (Exception e) {
           EmpExecutionContext.error(e, "获取发送短信总数量失败 :"+e.getMessage());
       }finally{
           try {
               super.close(rs, ps, conn);
           } catch (Exception e) {
               EmpExecutionContext.error(e, "关闭获取发送短信总数量链接失败:"+e.getMessage());
           }
       }
       return domains;
   }
	
	public List<LfDomain> getDomainsPageinfo(String ids,PageInfo pageInfo)throws Exception{
		
		StringBuffer sql = new StringBuffer();
        StringBuffer conditonSql = new StringBuffer();
        
        //sql.append("select ID,DOMAIN,FLAG,LEN_EXTEN FROM LF_DOMAIN ");
        sql.append("select * FROM LF_DOMAIN ");
        conditonSql.append(" WHERE ID in ("+ids+") and FLAG = 0 ");
        
        String sqlStr = sql.toString() + conditonSql.toString();
		
		String countSql = "select count(*) totalcount FROM (";
       countSql += sqlStr;
       countSql += " ) A";

       List<LfDomain> returnList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(
    		   LfDomain.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		
		return returnList;
	}

    /**
     * @param availableIds
     * @return 返回已经被该企业绑定的域名信息
     */
    public List<DynaBean> getBindedDomains(String corpCode, String availableIds) throws Exception {
        List<DynaBean> bindedDomainList = null;
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ID,DOMAIN,LEN_EXTEN,DTYPE FROM LF_DOMAIN WHERE ID IN(")
            .append("SELECT DOMAIN_ID FROM LF_DOMAIN_CORP WHERE DOMAIN_ID IN(")
            .append(availableIds).append(") ")
            .append("AND CORP_CODE=")
            .append(corpCode)
            .append(")");
        bindedDomainList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql.toString());
        return bindedDomainList;
    }

    /**
     * @param corpCode
     * @param pageInfo
     * @return 返回可用的域名信息
     */
    public List<DynaBean> getAvailableDomains(String corpCode, PageInfo pageInfo) throws Exception {
        List<DynaBean> availableDomainList = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ID,DOMAIN,LEN_EXTEN,DTYPE FROM LF_DOMAIN ")
            .append("WHERE ID NOT IN (SELECT ID FROM LF_DOMAIN_CORP LEFT JOIN LF_DOMAIN ON LF_DOMAIN_CORP.DOMAIN_ID=LF_DOMAIN.ID ")
                .append("WHERE LF_DOMAIN.DTYPE=1) UNION ")
                .append("SELECT ID,DOMAIN,LEN_EXTEN,DTYPE FROM lf_domain WHERE id IN (")
                .append("SELECT DOMAIN_ID FROM lf_domain_corp WHERE corp_code=")
                .append(corpCode).append(")");

        String countSql = "SELECT COUNT(*) totalcount FROM (";
        countSql += sql.toString();
        countSql += ") A";

        availableDomainList = new DataAccessDriver().getGenericDAO()
                .findPageDynaBeanBySQL(sql.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME, null);

        return availableDomainList;
    }

    /**
     *
     * @param corpCode
     * @param oldList
     * @param newList
     * @throws Exception
     */
    public int updateBinds(String corpCode, String oldList, List<LfDomainCorp> newList) throws Exception{
        Connection connection = null;
        int count = 0;
        StringBuffer delSql = new StringBuffer("DELETE FROM LF_DOMAIN_CORP WHERE CORP_CODE='")
                .append(corpCode).append("'")
                .append(" AND DOMAIN_ID IN (")
                .append(oldList)
                .append(" )");
        try {
            connection = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
            empTransDao.beginTransaction(connection);
            if(oldList != null && oldList.length() > 0) {
                count = empTransDao.executeBySQLReturnCount(connection, delSql.toString());
            }
            if(((0 == count && oldList == null) || count > 0) && (newList != null && newList.size() > 0)) {
                count = empTransDao.save(connection, newList, LfDomainCorp.class);
            }
            if(count > 0) {
                empTransDao.commitTransaction(connection);
            } else {
                empTransDao.rollBackTransaction(connection);
            }
        } catch (Exception e) {
            empTransDao.rollBackTransaction(connection);
            throw new Exception(e);
        } finally {
            empTransDao.closeConnection(connection);
        }
        return count;
    }


}
