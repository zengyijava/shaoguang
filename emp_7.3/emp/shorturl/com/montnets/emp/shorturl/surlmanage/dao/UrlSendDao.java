package com.montnets.emp.shorturl.surlmanage.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfReviewer2level;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.shorturl.comm.constant.UrlGlobals;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomain;
import com.montnets.emp.shorturl.surlmanage.entity.LfUrlTask;
import com.montnets.emp.shorturl.surlmanage.table.TableLfUrlTask;

public class UrlSendDao extends SuperDAO {

	SimpleDateFormat format = new SimpleDateFormat("yyyyMM");

	// public List<LfUrlVisit> getUrlDetails() {
	// Connection conn = null;
	// PreparedStatement ps = null;
	// ResultSet rs = null;
	// List<LfUrlVisit> resultList = new ArrayList<LfUrlVisit>();
	// StringBuffer sql = new StringBuffer();
	// sql.append("select top ").append(UrlGlobals.INIT_LOAD_COUNT)
	// .append(" ")
	// .append(TableLfUrlVisit.SHORT_URL).append(",").append(
	// TableLfUrlVisit.SRC_URL).append(",").append(
	// TableLfUrlVisit.TASKID).append(",").append(
	// TableLfUrlVisit.PHONE).append(",").append(TableLfUrlVisit.VISIT_NUM).append(",").append(
	// TableLfUrlVisit.FIRST_VISIT_TM).append(",").append(
	// TableLfUrlVisit.LAST_VISIT_TM).append(",").append(TableLfUrlVisit.CREATE_TM).append(" from ")
	// .append(TableLfUrlVisit.TABLE_NAME).append(
	// format.format(new Date())).append(" order by ").append(
	// TableLfUrlVisit.CREATE_TM).append(" desc");
	// try {
	// conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
	// ps = conn.prepareStatement(sql.toString());
	// rs = ps.executeQuery();
	// while (rs.next()) {
	// LfUrlVisit detail = new LfUrlVisit();
	// detail.setShortUrl(rs.getString(TableLfUrlVisit.SHORT_URL));
	// detail.setTaskId(rs.getLong(TableLfUrlVisit.TASKID));
	// detail.setSrcUrl(rs.getString(TableLfUrlVisit.SRC_URL));
	// detail.setPhone(rs.getString(TableLfUrlVisit.PHONE));
	// detail.setVisitNum(rs.getInt(TableLfUrlVisit.VISIT_NUM));
	// detail.setCreateTm(rs.getTimestamp(TableLfUrlVisit.CREATE_TM));
	// detail.setFirstVisitTm(rs
	// .getTimestamp(TableLfUrlVisit.FIRST_VISIT_TM));
	// detail.setLastVisitTm(rs
	// .getTimestamp(TableLfUrlVisit.LAST_VISIT_TM));
	// resultList.add(detail);
	// }
	// return resultList;
	// } catch (Exception e) {
	// EmpExecutionContext.error(e, "初始化加载短链接信息异常");
	// } finally {
	// try {
	// close(rs, ps, conn);
	// } catch (Exception e) {
	// EmpExecutionContext.error(e, "关闭数据库连接异常");
	// }
	// }
	// return resultList;
	// }

	/**
	 * 初始化短链接任务表状态异常
	 */
	public int initHandleStatus() throws Exception {
        //兼容多种数据库处理
        String multiSql = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? "NOW()" :
                StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE ? "GETDATE()" :
                        StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE ? "SYSDATE" : "CURRENT DATE";

		Connection conn = null;
		PreparedStatement ps = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(TableLfUrlTask.TABLE_NAME)
				.append(" set ").append(TableLfUrlTask.STATUS).append(
						" = status-1 ,").append(TableLfUrlTask.UPDATE_TM).append(
						" = " + multiSql + " ").append("where ").append(
						TableLfUrlTask.STATUS).append(" = 11 or ").append(
								TableLfUrlTask.STATUS).append(" =21 ");
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql.toString());
			
			return ps.executeUpdate();
		} catch (Exception e) {
			EmpExecutionContext.error(e, "初始化数据状态异常");
			throw e;
		} finally {
			try {
				close(null, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "初始化数据状态操作-关闭数据库连接异常");
			}
		}

	}

	/**
	 * 获取短链接生成起始标示
	 * 
	 * @return
	 */
	public Map<String, Long> getUrlCount() {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String handleNode = StaticValue.getServerNumber();
		String sql = "select url_count,url_step,begin_count,end_count from LF_URLCTRL where HANDLE_NODE = '"
				+ handleNode + "'";
		// Long urlCount = 0L;
		// Long urlStep = 0L;
		Map<String, Long> resultMap = new HashMap<String, Long>();
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				// urlCount = rs.getLong(1);
				resultMap.put("urlCount", rs.getLong(1));
				resultMap.put("urlStep", rs.getLong(2));
				resultMap.put("beginCount", rs.getLong(3));
				resultMap.put("endCount", rs.getLong(4));
			}
			return resultMap;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取短链接起始标识异常");
		} finally {
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "关闭数据库连接异常");
			}
		}
		return resultMap;
	}

	public int updateUrlCount(Long urlCount, Long step, String beginUrl,
			String endUrl, String curUrl) {
        //兼容多种数据库处理
        String multiSql = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? "NOW()" :
                StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE ? "GETDATE()" :
                        StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE ? "SYSDATE" : "CURRENT DATE";

		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = "update LF_URLCTRL set url_count = ? ,url_step = ?,begin_url = ?,end_url = ?,cur_url = ?,update_tm = " + multiSql + " where handle_node = ?";
		try {
			if (urlCount == null || urlCount < 0) {
				return result;
			}
			if (step == null || step < 0) {
				return result;
			}
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);

			ps = conn.prepareStatement(sql);
			beginTransaction(conn);
			ps.setLong(1, urlCount);
			ps.setLong(2, step);
			ps.setString(3, beginUrl);
			ps.setString(4, endUrl);
			ps.setString(5, curUrl);
			ps.setString(6, StaticValue.getServerNumber());

			result = ps.executeUpdate();
			commitTransaction(conn);
			return result;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "修改短链接生成标识异常");
			rollBackTransaction(conn);
		} finally {
			try {
				if (null != conn) {
					conn.setAutoCommit(true);
				}
				close(null, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "关闭数据库连接异常");
			}
		}
		return result;
	}

	// 开启事务
	public void beginTransaction(Connection conn) throws Exception {
		conn.setAutoCommit(false);
	}

	// 提交事务
	public void commitTransaction(Connection conn) throws Exception {
		conn.commit();
	}

	// 事务回滚
	public boolean rollBackTransaction(Connection conn) {
		try {
			if (conn != null) {
				conn.rollback();
			}
			return true;
		} catch (SQLException e) {
			EmpExecutionContext.error(e, "回滚失败");
			return false;
		}
	}

	public boolean countUrlSend() {
		Connection conn = null;
		CallableStatement cs = null;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			beginTransaction(conn);
			// TOWR 编写存储过程
			cs = conn.prepareCall("{call lf_counturlv0()}");
			cs.execute();
			commitTransaction(conn);
			return true;
		} catch (Exception e) {
			rollBackTransaction(conn);
			EmpExecutionContext.error(e, "数据库连接已关闭，无法回滚事务！");
			return false;
		} finally {
			try {
				commitTransaction(conn);
				close(null, cs, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "短链接发送统计操作-关闭数据库连接异常");
			}
		}
	}

	///**
	// * 批处理短链接发送详情
	// *
	// * @param detailList
	// * @param task
	// * @return
	// */
	// public boolean addDetailBatch(List<LfUrlVisit> detailList,LfUrlTask task)
	// {
	// if (detailList == null || detailList.isEmpty()) {
	// return false;
	// }
	// if (task == null) {
	// return false;
	// }
	// Connection conn = null;
	// PreparedStatement ps = null;
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	// String sql =
	// "insert into "+TableLfUrlVisit.TABLE_NAME+sdf.format(task.getSubmittm())+" values (?,?,?,?,0,GETDATE(),GETDATE(),?,?,0,' ',?)";
	// try {
	// conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
	// ps = conn.prepareStatement(sql);
	// beginTransaction(conn);
	// for (LfUrlVisit detail : detailList) {
	// ps.setString(1, detail.getShortUrl());
	// ps.setString(2, detail.getSrcUrl());
	// ps.setLong(3, detail.getTaskId());
	// ps.setString(4, detail.getPhone());
	// ps.setTimestamp(5, detail.getCreateTm());
	// ps.setString(6, detail.getCorpCode());
	// ps.setString(7, PhoneAreaUtils.getAreaCode(detail.getPhone()));
	// ps.addBatch();
	// }
	// ps.executeBatch();
	// commitTransaction(conn);
	// return true;
	// } catch (Exception e) {
	// EmpExecutionContext.error(e, "短链接发送详情入库任务处理,批处理异常");
	// rollBackTransaction(conn);
	// return false;
	// } finally {
	// try {
	// if(null != conn){
	// conn.setAutoCommit(true);
	// }
	// close(null, ps, conn);
	// } catch (Exception e) {
	// EmpExecutionContext.error(e, "短链接发送详情入库任务处理,关闭数据库连接异常");
	// }
	// }
	// }

	public void updateHandleStatus(LfUrlTask task) {

		if (task == null) {
			return;
		}
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "update " + TableLfUrlTask.TABLE_NAME
				+ " set file_status = ? where id = ?";
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			beginTransaction(conn);
			ps = conn.prepareStatement(sql);

			ps.setInt(1, task.getStatus());
			ps.setLong(2, task.getId());
			ps.executeUpdate();
			commitTransaction(conn);

		} catch (Exception e) {
			rollBackTransaction(conn);
			EmpExecutionContext.error(e, "短链接发送详情入库任务处理,修改任务状态异常");
		} finally {
			try {
				if (null != conn) {
					conn.setAutoCommit(true);
				}
				close(null, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "短链接发送详情入库任务处理,关闭数据库连接异常");
			}
		}

	}

	public void updateHandleLine(LfUrlTask task, Integer lineNum) {
		if (task == null) {
			return;
		}
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "update " + TableLfUrlTask.TABLE_NAME
				+ " set handle_line = handle_line+? where id = ?";
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			beginTransaction(conn);
			ps = conn.prepareStatement(sql);

			ps.setInt(1, lineNum);
			ps.setLong(2, task.getId());
			ps.executeUpdate();
			commitTransaction(conn);

		} catch (Exception e) {
			rollBackTransaction(conn);
			EmpExecutionContext.error(e, "短链接发送详情入库任务处理,修改任务执行行数异常");
		} finally {
			try {
				if (null != conn) {
					conn.setAutoCommit(true);
				}
				close(null, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "短链接发送详情入库任务处理,关闭数据库连接异常");
			}
		}

	}

	public List<LfUrlTask> getTaskList() {
		List<LfUrlTask> list = new ArrayList<LfUrlTask>();
		PageInfo pageInfo = new PageInfo();
		String sql = "select * from LF_URLTASK where (STATUS = 10 or STATUS = 20) ";
		try {
			pageInfo.setPageSize(UrlGlobals.getREAD_COUNT());
			pageInfo.setPageIndex(1);
			String countSql = "select count(*) totalcount from ("+ sql +") a";
			list = new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(LfUrlTask.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取短链接发送失败、未处理任务异常");
		}
		return list;
	}

	public List<LfDomain> findDomainByCorpcode(String corpCode) {
		if (StringUtils.isBlank(corpCode)) {
			EmpExecutionContext.error("获取发送用的域名-企业编码为空");
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select D.ID,D.DOMAIN,D.LEN_ALL,D.LEN_EXTEN,D.VALID_DAYS from LF_DOMAIN D join LF_DOMAIN_CORP C on D.ID = C.DOMAIN_ID WHERE D.FLAG=0 AND  C.FLAG =0 AND  C.CORP_CODE = '"
				+ corpCode+"'";
		List<LfDomain> domainlist = new ArrayList<LfDomain>();
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				LfDomain domain = new LfDomain();
				domain.setId(rs.getLong(1));
				domain.setDomain(rs.getString(2));
				domain.setLenAll(rs.getInt(3));
				domain.setLenExten(rs.getLong(4));
				domain.setValidDays(rs.getLong(5));
				domainlist.add(domain);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取发送用的域名dao层异常");
		}finally {
			try {
				
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "短链接发送详情入库任务处理,关闭数据库连接异常");
			}
		}
		return domainlist;
	}

    /**
     * 获取审批流程
     * @param conditionMap
     * @return
     */
    public LfReviewer2level checkFlowType(LinkedHashMap<String, String> conditionMap) {
	    List<LfReviewer2level> reviewer2levelList = null;
        try {
            reviewer2levelList =  new DataAccessDriver().getEmpDAO().findListByCondition(LfReviewer2level.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据userid和flow_id获取LfReviewer2level对象失败" + e.getMessage());
        }
        if(reviewer2levelList != null && reviewer2levelList.size() > 0) {
            return  reviewer2levelList.get(0);
        } else {
            return null;
        }

    }

    // public LfUrlVisit getDeteilByShortUrl(String shortUrl) {
	// Connection conn = null;
	// PreparedStatement ps = null;
	// ResultSet rs = null;
	// LfUrlVisit detail = new LfUrlVisit();
	// StringBuffer sql = new StringBuffer();
	// sql.append("select ")
	// .append(TableLfUrlVisit.SHORT_URL).append(",").append(
	// TableLfUrlVisit.SRC_URL).append(",").append(
	// TableLfUrlVisit.TASKID).append(",").append(
	// TableLfUrlVisit.PHONE).append(",").append(
	// TableLfUrlVisit.VISIT_NUM).append(",").append(
	// TableLfUrlVisit.FIRST_VISIT_TM).append(",").append(
	// TableLfUrlVisit.LAST_VISIT_TM).append(",").append(
	// TableLfUrlVisit.CREATE_TM).append(" from ").append(
	// TableLfUrlVisit.TABLE_NAME).append(
	// format.format(new Date())).append(" where ").append(
	// TableLfUrlVisit.SHORT_URL).append(" =").append("\'"+shortUrl+"\'");
	// try {
	// conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
	// ps = conn.prepareStatement(sql.toString());
	// rs = ps.executeQuery();
	// while (rs.next()) {
	// detail.setShortUrl(rs.getString(TableLfUrlVisit.SHORT_URL));
	// detail.setTaskId(rs.getLong(TableLfUrlVisit.TASKID));
	// detail.setSrcUrl(rs.getString(TableLfUrlVisit.SRC_URL));
	// detail.setPhone(rs.getString(TableLfUrlVisit.PHONE));
	// detail.setVisitNum(rs.getInt(TableLfUrlVisit.VISIT_NUM));
	// detail.setCreateTm(rs.getTimestamp(TableLfUrlVisit.CREATE_TM));
	// detail.setFirstVisitTm(rs
	// .getTimestamp(TableLfUrlVisit.FIRST_VISIT_TM));
	// detail.setLastVisitTm(rs
	// .getTimestamp(TableLfUrlVisit.LAST_VISIT_TM));
	// }
	// return detail;
	// } catch (Exception e) {
	// EmpExecutionContext.error(e, "初始化加载短链接信息异常");
	// } finally {
	// try {
	// close(rs, ps, conn);
	// } catch (Exception e) {
	// EmpExecutionContext.error(e, "关闭数据库连接异常");
	// }
	// }
	// return detail;
	// }

	// public List<AreaPhNoVo> getAreaPhNoList(LinkedHashMap<String, String>
	// conditionMap,PageInfo pageinfo) throws Exception{
	//		
	// List<PbServicetype> pbstList = new
	// BaseBiz().getEntityList(PbServicetype.class);
	// //移动
	// String ydServepro="";
	// //联通
	// String ltServepro="";
	// //电信
	// String dxServepro="";
	// if(pbstList!=null && pbstList.size()>0){
	// int len = pbstList.size();
	// for(int i = 0 ; i < len ; i++){
	// PbServicetype pbServicetype = pbstList.get(i);
	// if(pbServicetype.getSpisuncm()==0){
	// ydServepro = pbServicetype.getServiceno();
	// }
	// if(pbServicetype.getSpisuncm()==1){
	// ltServepro = pbServicetype.getServiceno();
	// }
	// if(pbServicetype.getSpisuncm()==21){
	// dxServepro = pbServicetype.getServiceno();
	// }
	// }
	// }
	// String sql="";
	// if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
	// sql="select m.MOBILE mobile,p.PROVINCECODE areacode,p.PROVINCE province, p.city city, m.ID id,"
	// +
	// " (case when SUBSTRING(cast(mobile as varchar(20)),1,3) in ("+ydServepro+") then '移动'"
	// +" when SUBSTRING(cast(mobile as varchar(20)),1,4) in ("+ydServepro+") then '移动'"
	// +" when SUBSTRING(cast(mobile as varchar(20)),1,3) in("+ltServepro+") then '联通'"
	// +" when SUBSTRING(cast(mobile as varchar(20)),1,4) in("+ltServepro+") then '联通'"
	// +" when SUBSTRING(cast(mobile as varchar(20)),1,3) in("+dxServepro+")then '电信'"
	// +" when SUBSTRING(cast(mobile as varchar(20)),1,4) in("+dxServepro+")then '电信'"
	// +" else '未知' end) servepro from a_mobilearea m , a_provincecity p"
	// +" where m.AREACODE=p.AREACODE";
	// }else{
	// sql="select m.MOBILE mobile,p.PROVINCECODE areacode,p.PROVINCE province, p.city city, m.ID id,"
	// +
	// " (case when substr(m.MOBILE,1,3) in ("+ydServepro+") then '移动'"
	// +" when substr(m.MOBILE,1,4) in ("+ydServepro+") then '移动'"
	// +" when substr(m.MOBILE,1,3) in("+ltServepro+") then '联通'"
	// +" when substr(m.MOBILE,1,4) in("+ltServepro+") then '联通'"
	// +" when substr(m.MOBILE,1,3) in("+dxServepro+")then '电信'"
	// +" when substr(m.MOBILE,1,4) in("+dxServepro+")then '电信'"
	// +" else '未知' end) servepro from a_mobilearea m , a_provincecity p"
	// +" where m.AREACODE=p.AREACODE";
	// }
	// String provinceCode=conditionMap.get("provinceCode");
	// String province=conditionMap.get("province");
	// String mobile=conditionMap.get("mobile");
	// String servePro=conditionMap.get("servePro");
	// String city=conditionMap.get("city");
	// String tempSql=null;
	// if(provinceCode!=null&&!"".equals(provinceCode))
	// {
	// sql=sql+" and p.PROVINCECODE='"+provinceCode+"'";
	// }
	// if(province!=null&&!"".equals(province))
	// {
	// sql=sql+" and p.province='"+province+"'";
	// }
	// if(mobile!=null&&!"".equals(mobile))
	// {
	// sql=sql+" and m.MOBILE='"+mobile+"'";
	// }
	// if(city!=null&&!"".equals(city))
	// {
	// sql=sql+" and p.city='"+city+"'";
	// }
	// if(servePro!=null&&!"".equals(servePro))
	// {
	// tempSql="SELECT * FROM("+sql+")  B WHERE SERVEPRO='"+servePro+"'";
	// }
	// if (pageinfo == null) {
	// return findVoListBySQL(AreaPhNoVo.class, sql,
	// StaticValue.EMP_POOLNAME);
	// } else {
	// String countSql = new StringBuffer(
	// "select count(*) totalcount from (").append(sql)
	// .append(") A").toString();
	// if(tempSql!=null&&!tempSql.equals("") && StaticValue.DBTYPE !=
	// StaticValue.SQLSERVER_DBTYPE){
	// sql=tempSql;
	// countSql = new StringBuffer(
	// "select count(*) totalcount from (").append(sql)
	// .append(") A").toString();
	// }
	// List<AreaPhNoVo> areaVoList = new
	// DataAccessDriver().getGenericDAO().findPageVoListBySQL(AreaPhNoVo.class,
	// sql, countSql, pageinfo, StaticValue.EMP_POOLNAME);
	// if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE &&
	// servePro!=null&&!"".equals(servePro)){
	// List<AreaPhNoVo> findAreaVoList = new LinkedList<AreaPhNoVo>();
	// for(AreaPhNoVo areavo:areaVoList){
	// if(servePro.equals(areavo.getServePro())){
	// findAreaVoList.add(areavo);
	// }
	// }
	// areaVoList = findAreaVoList;
	// }
	// return areaVoList;
	// }
	// }
}
