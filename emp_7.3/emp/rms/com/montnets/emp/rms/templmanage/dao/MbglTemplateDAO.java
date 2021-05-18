package com.montnets.emp.rms.templmanage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.commontempl.table.TableLfTemplate;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTmplRela;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

public class MbglTemplateDAO extends SuperDAO{
	
	/**
	 *   查询所有的彩信素材列表VO
	 * @param loginUserId	操作员用户ID
	 * @param lfTemplateVo	素材VO
	 * @param pageInfo	分页信息
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplateVo> findLfTemplateVoList(Long loginUserId,
			LfTemplateVo lfTemplateVo, PageInfo pageInfo) throws Exception
	{
		//获取查询SQL
		String fieldSql = getFieldSql();
		//获取表SQL
		String tableSql = getTableSql();
		//获取查询SQL
		String conditionSql = getConditionSql(lfTemplateVo);
		//获取排序SQL
		String orderSql = getOrderBySql();
		//拼接SQL
		String sql = fieldSql + tableSql + conditionSql + orderSql;
		//获取总计
		String countSql = "select count(*) totalcount from (" + fieldSql + tableSql + conditionSql + ") mytemp";
		//返回查询数据
		return new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(LfTemplateVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
	}
	
	/**
	 *  获取所有彩信素材列表
	 * @param loginUserId	操作员用户ID
	 * @param lfTemplateVo	素材VO
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplateVo> findLfTemplateVoList(Long loginUserId,
			LfTemplateVo lfTemplateVo) throws Exception
	{
		//获取查询SQL
		String fieldSql = getFieldSql();
		//获取表SQL
		String tableSql = getTableSql();
		 
		//获取查询SQL
		String conditionSql = getConditionSql(lfTemplateVo);
		//获取排序SQL
		String orderSql = getOrderBySql();
		
		//拼接SQL
		String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).append(orderSql).toString();
		//System.out.println(sql);	
				//获取总计
				String countSql = new StringBuffer("select count(*) totalcount from (").append(sql)
						.append(") mytemp").toString();	
		//查询数据
		List<LfTemplateVo> returnVoList = findVoListBySQL(LfTemplateVo.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	
	
	
	/**
	 * 获取查询字段
	 * @return
	 */
	private  String getFieldSql()
	{
		//拼接查询字段
				StringBuffer filedSql = new StringBuffer("SELECT ");
				filedSql
				.append("NULL AS ID").append(",")
				.append("NULL AS SHARE_TYPE").append(",")
				.append("B.NAME").append(",")
				.append("B.USER_NAME").append(",")
				.append("NULL AS USER_STATE").append(",")
				.append("C.DEP_NAME").append(",")
				.append("A.").append(TableLfTemplate.TM_ID).append(",")
				.append("A.").append(TableLfTemplate.USER_ID).append(",")
				.append("A.").append(TableLfTemplate.BIZ_CODE).append(",")
				.append("A.").append(TableLfTemplate.CORP_CODE).append(",")
				.append("A.").append(TableLfTemplate.DEGREE).append(",")
				.append("A.").append(TableLfTemplate.DEGREE_SIZE).append(",")
				.append("A.").append(TableLfTemplate.DSFLAG).append(",")
				.append("A.").append(TableLfTemplate.EMP_TEMPLID).append(",")
				.append("A.").append(TableLfTemplate.ERROR_CODE).append(",")
				.append("A.").append(TableLfTemplate.INDUSTRYID).append(",")
				.append("A.").append(TableLfTemplate.USEID).append(",")
				.append("A.").append(TableLfTemplate.ISPUBLIC).append(",")
				.append("A.").append(TableLfTemplate.ISPASS).append(",")
				.append("A.").append(TableLfTemplate.MMS_TMPLID).append(",")
				.append("A.").append(TableLfTemplate.PARAMCNT).append(",")
				.append("A.").append(TableLfTemplate.ADDTIME).append(",")
				.append("A.").append(TableLfTemplate.AUDITSTATUS).append(",")
				.append("A.").append(TableLfTemplate.SP_TEMPLID).append(",")
				.append("A.").append(TableLfTemplate.SUBMITSTATUS).append(",")
				.append("A.").append(TableLfTemplate.TM_CODE).append(",")
				.append("A.").append(TableLfTemplate.TM_MSG).append(",")
				.append("A.").append(TableLfTemplate.TM_NAME).append(",")
				.append("A.").append(TableLfTemplate.TM_STATE).append(",")
				.append("A.").append(TableLfTemplate.TMP_TYPE).append(",")
				.append("A.").append(TableLfTemplate.USECOUNT).append(",")
				.append("A.").append(TableLfTemplate.ISSHORTTEMP).append(",")
				/*.append("A.").append(TableLfTemplate.TMPLSTATUS)*/
				.append("A.").append(TableLfTemplate.VER);
				//返回查询字段字符串
				return filedSql.toString();
	}

	/**
	 * 获取查询表名sql
	 * @return
	 */
	private  String getTableSql()
	{
		StringBuffer tableSql = new StringBuffer(" FROM ");
		tableSql.append(TableLfTemplate.TABLE_NAME).append(" A ")
		.append("LEFT JOIN LF_SYSUSER  B ON A.USER_ID = B.USER_ID ")
		.append("LEFT JOIN LF_DEP C ON B.DEP_ID = C.DEP_ID ");
		return tableSql.toString();
	}

	/**
	 * 机构权限sql拼接
	 * @param loginUserId
	 * @return
	 */
	private  String getDominationSql(Long loginUserId)
	{
		//拼接sql
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(loginUserId);
		String dominationSql = new StringBuffer(" where (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(loginUserId).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(domination).append(")").toString();
		return dominationSql;
	}
	
	//获得共享模板sql
	private  String getShareSql(Long loginUserId)
	{
		//拼接sql
		StringBuffer share = new StringBuffer("select ").append(
			TableLfSysuser.DEP_ID).append(" from ").append(
			TableLfSysuser.TABLE_NAME).append(" where ").append(
			TableLfSysuser.USER_ID).append("=").append(loginUserId);
		String shareSql = new StringBuffer(" or (lfalltemplate.").append(
				TableLfTmplRela.toUserType).append("=").append(1).append(
				" and lfalltemplate.").append(TableLfTmplRela.toUser).append(" in (")
				.append(share).append("))").
				append(" or (lfalltemplate.").append(
				TableLfTmplRela.toUserType).append("=").append(2).append(
				" and lfalltemplate.").append(TableLfTmplRela.toUser).append(" =")
				.append(loginUserId).append(")").toString();
		return shareSql;
	}
	
	/**
	 * 查询条件
	 * @param lfTemplateVo
	 * @return
	 */
	public  String getConditionSql(LfTemplateVo lfTemplateVo)
	{
		StringBuilder conditionSql = new StringBuilder(" WHERE 1=1 ");
		//查询条件---状态
		if (lfTemplateVo.getTmState() != null) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.TM_STATE).append("=").append(
					lfTemplateVo.getTmState());
		}
		//查询条件---姓名
		if (lfTemplateVo.getName() != null
				&& !"".equals(lfTemplateVo.getName())) {
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME)
					.append(" like '%").append(lfTemplateVo.getName().trim())
					.append("%'");
		}
		//查询条件---模板内容
		if (lfTemplateVo.getTmMsg() != null
				&& !"".equals(lfTemplateVo.getTmMsg())) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.TM_MSG).append(" like '%").append(
					lfTemplateVo.getTmMsg()).append("%'");
		}
		//查询条件---模板名称
		if (lfTemplateVo.getTmName() != null
				&& !"".equals(lfTemplateVo.getTmName())) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.TM_NAME).append(" like '%").append(
					lfTemplateVo.getTmName()).append("%'");
		}
		//查询条件---模板ID
		if (lfTemplateVo.getSptemplid() != null) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.SP_TEMPLID).append(" like '%").append(
					lfTemplateVo.getSptemplid()).append("%'");
		}
		
		//查询条件---类型
		if (lfTemplateVo.getTmpType() != null) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.TMP_TYPE).append("=").append(
					lfTemplateVo.getTmpType());
		}
		//查询条件---是否是公共模板
		if (lfTemplateVo.getIsPublic() != null) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.ISPUBLIC).append("=").append(
					lfTemplateVo.getIsPublic());
		}
		//查询条件---行业
		if (lfTemplateVo.getIndustryid() != null) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.INDUSTRYID).append("=").append(
					lfTemplateVo.getIndustryid());
		}
		//查询条件---用途
		if (lfTemplateVo.getUseid() != null) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.USEID).append("=").append(
					lfTemplateVo.getUseid());
		}
		
		//查询条件---审核是否通过
		if (lfTemplateVo.getIsPass() != null) {
			if (lfTemplateVo.getIsPass() == 5) {
				conditionSql.append(" and A.").append(
						TableLfTemplate.ISPASS).append(" in (0,1)");
			} else {
				conditionSql.append(" and A.").append(
						TableLfTemplate.ISPASS).append("=").append(
						lfTemplateVo.getIsPass());
			}
		}
		//查询条件---操作员id
		if (lfTemplateVo.getUserId() != null) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.USER_ID).append("=").append(
					lfTemplateVo.getUserId());
		}
		//查询条件---模板类型
		if (lfTemplateVo.getDsflag() != null) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.DSFLAG).append("=").append(
					lfTemplateVo.getDsflag());
		}
		//查询条件----运营商审批状态
		if (lfTemplateVo.getAuditstatus() != null) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.AUDITSTATUS).append("=").append(
					lfTemplateVo.getAuditstatus());
		}
		//查询条件----运营商提交状态
		if (lfTemplateVo.getSubmitstatus() != null) {
			conditionSql.append(" and A.").append(
					TableLfTemplate.SUBMITSTATUS).append("=").append(
					lfTemplateVo.getSubmitstatus());
		}
		//查询条件 ----创建时间：开始时间
		if(lfTemplateVo.getAddStartm() != null && lfTemplateVo.getAddEndtm()==null ){
			//.append("to_date('" )
			if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE) {//oracle数据库时间格式不能直接拿字符串进行大小比较
				conditionSql.append(" and A.").append(TableLfTemplate.ADDTIME).append(">=").append("TO_DATE(\'").append(lfTemplateVo.getAddStartm()).append("\',\'yyyy-MM-dd hh24:mi:ss\')");
			} else {
				conditionSql.append(" and A.").append(TableLfTemplate.ADDTIME).append(">=").append("'").append(lfTemplateVo.getAddStartm()).append("'");
						//.append("','yyyy-MM-dd hh24:mi:ss')");
			}
		}
		//查询条件 ----创建时间：结束时间
		if(lfTemplateVo.getAddStartm() == null && lfTemplateVo.getAddEndtm() != null){
			//.append("to_date('" )
			if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE) {
				conditionSql.append(" and A.").append(TableLfTemplate.ADDTIME).append("<=").append("TO_DATE(\'").append(lfTemplateVo.getAddEndtm()).append("\',\'yyyy-MM-dd hh24:mi:ss\')");
			} else {
				conditionSql.append(" and A.").append(TableLfTemplate.ADDTIME).append("<=").append("'").append(lfTemplateVo.getAddEndtm()).append("'");
						//.append("','yyyy-MM-dd hh24:mi:ss')");
			}
		}
		//查询条件 ----创建时间：结束时间、结束时间
		if(lfTemplateVo.getAddStartm() != null && lfTemplateVo.getAddEndtm() != null){

			//	.append("to_date('" )
			//.append("','yyyy-MM-dd hh24:mi:ss')")
			//.append("to_date('" )
			if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE) {
				conditionSql.append(" and A.").append(TableLfTemplate.ADDTIME)
						.append(">=").append("TO_DATE(\'").append(lfTemplateVo.getAddStartm()).append("\',\'yyyy-MM-dd hh24:mi:ss\')")
						.append(" and A.").append(TableLfTemplate.ADDTIME)
						.append("<=").append("TO_DATE(\'").append(lfTemplateVo.getAddEndtm()).append("\',\'yyyy-MM-dd hh24:mi:ss\')");
			} else {
				conditionSql.append(" and A.").append(TableLfTemplate.ADDTIME)
						.append(">=").append("'").append(lfTemplateVo.getAddStartm()).append("'")
						//.append("','yyyy-MM-dd hh24:mi:ss')")
						.append(" and A.").append(TableLfTemplate.ADDTIME)
						.append("<=").append("'").append(lfTemplateVo.getAddEndtm()).append("'");
						//.append("','yyyy-MM-dd hh24:mi:ss')");
			}
		}
        
        if(lfTemplateVo.getCorpCode() != null){
        	conditionSql.append(" and A.").append(
					TableLfTemplate.CORP_CODE).append(" = '").append(
					lfTemplateVo.getCorpCode()).append("'");
        }
        
		//返回拼接sql
		return conditionSql.toString();
	}

	/**
	 * 获取排序sql
	 * @return
	 */
	private  String getOrderBySql()
	{
		//拼接sql
		String orderBySql = new StringBuffer(" order by A.").append(
				TableLfTemplate.ADDTIME).append(" desc").toString();
		return orderBySql;
	}
	/**
	 * 获取分组sql
	 * @return
	 */
	private  String getGroupBySql()
	{
		//拼接sql
		String groupBySql = new StringBuffer("temp group by temp.").append(
				TableLfTemplate.TM_ID).toString();
		return groupBySql;
	}
	/**
	 * 获取where sql
	 * @return
	 */
	private  String getWhereSql()
	{
		//拼接sql
		String groupBySql = new StringBuffer(" where A.").append(TableLfTmplRela.id).append(" IN ").toString();
		return groupBySql;
	}
	/**
	 * 获取未审核的状态模板ID
	 */
	public Map<String,String> getNocheckTemlateList() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,String> map = new HashMap<String, String>();
		Connection conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		//ISPASS = -1 为未审核,3:审核中,4:禁用，TMP_TYPE = 11 为富信模板，SP_TEMPLID !=0代表网关返回了模板ID
//		String sql = "SELECT SP_TEMPLID,CORP_CODE FROM LF_TEMPLATE WHERE  TMP_TYPE = 11 AND SP_TEMPLID !=0 ORDER BY CORP_CODE ";//ISPASS = -1 为未审核,3:审核中,4:禁用，TMP_TYPE = 11 为富信模板，SP_TEMPLID !=0代表网关返回了模板ID, ISMATERIAL =1-公共素材
		String sql = "SELECT SP_TEMPLID,CORP_CODE FROM LF_TEMPLATE WHERE  TMP_TYPE IN (11,12,13) AND SP_TEMPLID !=0 AND ISMATERIAL != 1  ORDER BY CORP_CODE ";//ISPASS = -1 为未审核,3:审核中,4:禁用，TMP_TYPE = 11 为富信模板，12 为卡片，13 为富文本，SP_TEMPLID !=0代表网关返回了模板ID
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				long tmCode = rs.getLong("SP_TEMPLID");
				String corpCode = rs.getString("CORP_CODE");
				if(StringUtils.IsNullOrEmpty(map.get(corpCode))){
					map.put(corpCode, String.valueOf(tmCode));
					continue;
				}
				String tmCodes  = map.get(corpCode)+","+tmCode;
				map.put(corpCode, tmCodes);
			}
			
		} catch (SQLException e) {
			EmpExecutionContext.error("查询 LF_TEMPLATE 表出现异常"+e.toString());
		} finally{
			try {
				if(conn!=null){
					conn.close();
				}
				if(rs!=null){
					rs.close();
				}
				if(ps!=null){
					ps.close();
				}
				
			} catch (SQLException e) {
				EmpExecutionContext.error("关闭数据库流出现异常"+e.toString());
			}
		}
		return map;
		
	}
	/**
	 * 更新网关返回的模板审核状态
	 */
	public boolean updateTemplateStatus(LfTemplateVo vo) {
		PreparedStatement ps = null;
		Connection conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		String sql = "UPDATE  LF_TEMPLATE SET AUDITSTATUS = ? WHERE SP_TEMPLID = ?";
		boolean updateFlag = false;
		try {
			ps = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			ps.setInt(1, vo.getAuditstatus());
			ps.setLong(2, vo.getSptemplid());
			int count = ps.executeUpdate();
			if(count > 0){
				updateFlag = true;
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				EmpExecutionContext.error("事务回滚 出现异常"+e1.toString());
			}
			EmpExecutionContext.error("更新 LF_TEMPLATE表 出现异常"+e.toString());
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				EmpExecutionContext.error("关闭数据库流出现异常"+e.toString());
			}
		}
		return updateFlag;
	}
	
	
	public boolean updateTemplate(LfTemplate ltmt) {
		PreparedStatement ps = null;
		Connection conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		String sql = "UPDATE  LF_TEMPLATE SET TM_MSG = ? ,TM_NAME = ? ,SP_TEMPLID = ?,DEGREE = ?, DEGREE_SIZE = ?,SUBMITSTATUS = ?,TM_STATE = ?,PARAMCNT = ?,DSFLAG = ?,INDUSTRYID = ?,USEID = ?  WHERE TM_ID = ?";//
		boolean updateFlag = false;
		try {
			ps = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			ps.setString(1, ltmt.getTmMsg());
			ps.setString(2, ltmt.getTmName());
			ps.setLong(3, ltmt.getSptemplid());
			ps.setInt(4, ltmt.getDegree());
			ps.setLong(5, ltmt.getDegreeSize());
			ps.setLong(6, ltmt.getSubmitstatus());
			ps.setLong(7, ltmt.getTmState());
			ps.setInt(8, ltmt.getParamcnt());
			ps.setLong(9, ltmt.getDsflag());
			ps.setInt(10, ltmt.getIndustryid());
			ps.setInt(11, ltmt.getUseid());
			ps.setLong(12, ltmt.getTmid());
			int count = ps.executeUpdate();
			if(count > 0){
				updateFlag = true;
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				EmpExecutionContext.error("事务回滚 出现异常"+e1.toString());
			}
			EmpExecutionContext.error("更新 LF_TEMPLATE表 出现异常"+e.toString());
		}finally{
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				EmpExecutionContext.error("关闭数据库流出现异常"+e.toString());
			}
		}
		return updateFlag;
		
	}
	
	public LfTemplate getTmplateByTmid(long tmid) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		LfTemplate lt  = null;
		Connection conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		//ISPASS = -1 为未审核,TMP_TYPE = 11 为富信模板
		String sql = "SELECT * FROM LF_TEMPLATE WHERE TM_ID = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setLong(1, tmid);
			rs = ps.executeQuery();
			while(rs.next()){
				lt= new LfTemplate();
				lt.setTmid(rs.getLong("TM_ID"));
				lt.setTmMsg(rs.getString("TM_MSG"));
				lt.setTmName(rs.getString("TM_NAME"));
				lt.setDegree(rs.getInt("DEGREE"));
				lt.setDegreeSize(rs.getLong("DEGREE_SIZE"));
				lt.setAddtime(rs.getTimestamp("ADDTIME"));
				lt.setIsPublic(rs.getInt("ISPUBLIC"));
				lt.setIsPass(rs.getInt("ISPASS"));
				lt.setTmState(rs.getLong("TM_STATE"));
				lt.setVer(rs.getString("VER"));
				lt.setExlJson(rs.getString("EXLJSON"));
			}
			
		} catch (SQLException e) {
			EmpExecutionContext.error("查询 LF_TEMPLATE 表出现异常"+e.toString());
		} finally{
			try {
				if(conn!=null){
					conn.close();
				}
				if(rs!=null){
					rs.close();
				}
				if(ps!=null){
					ps.close();
				}
			} catch (SQLException e) {
				EmpExecutionContext.error("关闭数据库流出现异常"+e.toString());
			}
		}
		return lt;
	}
	
	/**
	 * 查询富信模板-用于进行 rms 文件下载
	 * @return
	 */
	public List<LfTemplateVo> getTemplateList(){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LfTemplateVo lt  = null;
		List<LfTemplateVo> list = new ArrayList<LfTemplateVo>();
		try {
		 conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		//兼容不同数据库 1 oracle ; 2 SQL Server2005 ; 3 MySQL ; 4 DB2
		//TMP_TYPE = 11 为富信模板
		String sql;
		switch (StaticValue.DBTYPE){
			case 1:
				sql = "SELECT TM_ID,TM_MSG,TM_NAME,DEGREE,DEGREE_SIZE,ADDTIME,ISPUBLIC,ISPASS,TM_STATE,AUDITSTATUS,VER " +
						"FROM LF_TEMPLATE WHERE TMP_TYPE = 11 AND ROWNUM <=100 ORDER BY ADDTIME DESC";
				break;
			case 2:
				sql = "SELECT TOP 100 TM_ID,TM_MSG,TM_NAME,DEGREE,DEGREE_SIZE,ADDTIME,ISPUBLIC,ISPASS,TM_STATE," +
						"AUDITSTATUS,VER FROM LF_TEMPLATE WHERE TMP_TYPE = 11 ORDER BY ADDTIME DESC";
				break;
			case 3:
				sql = "SELECT TM_ID,TM_MSG,TM_NAME,DEGREE,DEGREE_SIZE,ADDTIME,ISPUBLIC,ISPASS,TM_STATE," +
						"AUDITSTATUS,VER FROM LF_TEMPLATE WHERE TMP_TYPE = 11 ORDER BY ADDTIME DESC LIMIT 0,100";
				break;
			case 4:
				sql = "SELECT TM_ID,TM_MSG,TM_NAME,DEGREE,DEGREE_SIZE,ADDTIME,ISPUBLIC,ISPASS,TM_STATE,AUDITSTATUS,VER " +
						"FROM (SELECT TM_ID,TM_MSG,TM_NAME,DEGREE,DEGREE_SIZE,ADDTIME,ISPUBLIC,ISPASS,TM_STATE,AUDITSTATUS,VER," +
						"ROW_NUMBER() OVER(ORDER BY ADDTIME DESC) AS ROWNUM FROM LF_TEMPLATE WHERE TMP_TYPE = 11) " +
						"WHERE ROWNUM <= 100";
				break;
			default:
				EmpExecutionContext.error("查询 LF_TEMPLATE 表出现异常,无法识别的数据库类型！");
				throw new EMPException("");
		}
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				lt= new LfTemplateVo();
				lt.setTmid(rs.getLong("TM_ID"));
				lt.setTmMsg(rs.getString("TM_MSG"));
				lt.setTmName(rs.getString("TM_NAME"));
				lt.setDegree(rs.getInt("DEGREE"));
				lt.setDegreeSize(rs.getLong("DEGREE_SIZE"));
				lt.setAddtime(rs.getTimestamp("ADDTIME"));
				lt.setIsPublic(rs.getInt("ISPUBLIC"));
				lt.setIsPass(rs.getInt("ISPASS"));
				lt.setTmState(rs.getLong("TM_STATE"));
				lt.setAuditstatus(rs.getInt("AUDITSTATUS"));
				lt.setVer(rs.getString("VER"));
				list.add(lt);
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询 LF_TEMPLATE 表出现异常");
		} finally{
			try {
				if(null != conn){
					conn.close();
				}
				if(null != rs){
					rs.close();
				}
				if(null != ps){
					ps.close();
				}
			} catch (Exception e) {
				EmpExecutionContext.error("查询 LF_TEMPLATE 表,关闭数据库流出现异常"+e.toString());
			}
		}
		return list;
		
	}
	public long getTotalCountTemplate() {
		//模板总数
		long totalCount = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		String sql = "SELECT COUNT(1) AS totalCount FROM LF_TEMPLATE WHERE TMP_TYPE = 11 ";//TMP_TYPE = 11 为富信模板
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				totalCount = rs.getLong("totalCount");
			}
			
		} catch (SQLException e) {
			EmpExecutionContext.error(e,"查询 LF_TEMPLATE 表出现异常");
		} finally{
			try {
				if(null != conn){
					conn.close();
				}
				if(null != rs){
					rs.close();
				}
				if(null != ps){
					ps.close();
				}
			} catch (SQLException e) {
				EmpExecutionContext.error("关闭数据库流出现异常"+e.toString());
			}
		}
		return totalCount;
	}
 
}
