package com.montnets.emp.rms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.table.client.TableLfCliDepConn;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.pasroute.TableLfMmsAccbind;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTemplate;
import com.montnets.emp.table.template.TableLfTmplRela;
import com.montnets.emp.util.PageInfo;

/**
 *   彩信DAO
 * @author Administrator
 *
 */
public class SameMmsDao extends SuperDAO{
	
	/**
	 *   获取sp账号
	 * @param lfMmsAccbind
	 * @return
	 * @throws Exception
	 */
	public  List<LfMmsAccbind> getMmsSpUser(LfMmsAccbind lfMmsAccbind) throws Exception{
		StringBuffer sqlStr = new StringBuffer();
		String sql="SELECT distinct USR.USERID  FROM GT_PORT_USED GT INNER JOIN USERDATA USR ON UPPER(USR.USERID)=UPPER(GT.USERID)"
			+" INNER JOIN XT_GATE_QUEUE GATE ON GATE.SPGATE=GT.SPGATE"
			+" INNER JOIN (SELECT GWACC.PTACCUID AS DESTUID,GATE2.SPGATE AS SPGATE,GATE2.SPISUNCM AS SPISUNCM FROM A_GWACCOUNT GWACC"
		    +" INNER JOIN A_GWSPBIND BIND ON BIND.PTACCUID=GWACC.PTACCUID INNER JOIN XT_GATE_QUEUE GATE2 ON GATE2.ID=BIND.GATEID" 
    		+" WHERE GATE2.GATETYPE=2) TB2 ON GT.SPGATE=TB2.SPGATE AND GT.SPISUNCM=TB2.SPISUNCM WHERE USR.USERTYPE=0" 
		    +" AND USR.STATUS=0 AND USR.ACCOUNTTYPE=2 AND GATE.STATUS=0  AND GATE.GATETYPE=2 AND GT.STATUS=0" 
		    +" AND GT.GATETYPE=2";
		sqlStr.append("select * from ").append(TableLfMmsAccbind.TABLE_NAME).append(" lmab where lmab.")
		.append(TableLfMmsAccbind.IS_VALIDATE).append("=1");
		sqlStr.append(" and lmab.").append(TableLfMmsAccbind.MMS_USER).append(" in (")
		.append(sql)
		.append(")");
		if(lfMmsAccbind.getCorpCode()!=null&&!"".equals(lfMmsAccbind.getCorpCode())){
			sqlStr.append(" and lmab.").append(TableLfMmsAccbind.CORP_CODE).append("='").append(lfMmsAccbind.getCorpCode()).append("'");
		}
		return findEntityListBySQL(LfMmsAccbind.class, sqlStr.toString(), StaticValue.EMP_POOLNAME);
	}

	/**
	 * 获取SP账号的密码
	 * @param spUser SP账号
	 * @return SP账号的密码
	 * @throws Exception 异常对象
	 */
		public String getSPPassword(String spUser) throws Exception {
			String pw = null;
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM ").append(TableUserdata.TABLE_NAME)
					.append(" WHERE ").append(TableUserdata.USER_ID).append(" = '").append(spUser).append("'");
			List<Userdata> list = findEntityListBySQL(Userdata.class,sql.toString(),StaticValue.EMP_POOLNAME);

			if(list != null){
				Userdata user = list.get(0);
				pw = user.getUserPassword();
			}
			return pw;
		}
	
	
	/**
	 *   查询彩信模板
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplate> getMMSTemplate(LfTemplate template) throws Exception{
		
		/*select lftemplate.*  
		from LF_TEMPLATE lftemplate left join LF_SYSUSER sysuser  on lftemplate.USER_ID= sysuser.USER_ID
		left join LF_DEP lfdep on sysuser.DEP_ID=lfdep.DEP_ID 
		where (sysuser.USER_ID=2 
		or sysuser.DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=2)) 
		and lftemplate.TM_STATE=1 and lftemplate.TMP_TYPE=4 and lftemplate.ISPASS in (0,1) order by lftemplate.ADDTIME desc*/
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select lftemplate.* from ").append(TableLfTemplate.TABLE_NAME).append(" lftemplate ")
		.append(StaticValue.getWITHNOLOCK()).append(" left join ").append(TableLfSysuser.TABLE_NAME).append(" sysuser ")
		.append(StaticValue.getWITHNOLOCK()).append(" on lftemplate.").append(TableLfTemplate.USER_ID).append(" = sysuser.")
		.append(TableLfSysuser.USER_ID).append(" left join ").append(TableLfDep.TABLE_NAME).append(" lfdep ")
		.append(StaticValue.getWITHNOLOCK()).append(" on sysuser.").append(TableLfSysuser.DEP_ID).append(" = ")
		.append(" lfdep.").append(TableLfDep.DEP_ID) .append(" where (sysuser.").append(TableLfSysuser.USER_ID)
		.append(" = ").append(template.getUserId()).append(" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in ")
		.append(" (select domination.").append(TableLfDomination.DEP_ID).append(" from ").append(TableLfDomination.TABLE_NAME)
		.append(" domination ").append(StaticValue.getWITHNOLOCK()).append(" where domination.").append(TableLfDomination.USER_ID)
		.append(" = ").append(template.getUserId()).append(" ))");
		//模板类型(1.通用动态模块;0.通用静态模块;2.智能抓取模块;3.移动财务模块)
		if(template.getDsflag() != null){
			sqlStr.append(" and lftemplate.").append(TableLfTemplate.DS_FLAG).append(" = ").append(template.getDsflag());
		}
		//模板状态(1.启用，0.禁用,2.草稿)
		if(template.getTmState() != null){
			sqlStr.append(" and lftemplate.").append(TableLfTemplate.TM_STATE).append(" = ").append(template.getTmState());
		}
		//审批状态（-1.未审批；0.无需审批；1.审批通过；2.审批未通过）
		sqlStr.append(" and lftemplate.").append(TableLfTemplate.ISPASS).append(" in (0,1) ");
		//模板（3-短信模板;4-彩信模板）
		sqlStr.append(" and lftemplate.").append(TableLfTemplate.TMP_TYPE).append(" = 4 ");
		//网关审批状态
		sqlStr.append(" and lftemplate.").append(TableLfTemplate.AUDITSTATUS).append(" = 1 ");
		sqlStr.append("  order by lftemplate.").append(TableLfTemplate.TM_ID).append(" ").append(StaticValue.DESC);
		return findEntityListBySQL(LfTemplate.class, sqlStr.toString(), StaticValue.EMP_POOLNAME);
	}
	
	/**
	 *  处理群组获取手机号码的方法
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public String getClientGroupUserById(String groupId)throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//List<String> returnList=new ArrayList<String>();
		StringBuilder buffer = new StringBuilder();
		
		
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.L2G_TYPE).append(",case(list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(") when 0 then employee.").append(TableLfEmployee.MOBILE).append(" when 1 then client.")
				.append(TableLfClient.MOBILE).append("  else malist.")
				.append(TableLfMalist.MOBILE).append(" end as MOBILE ");
		//拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
				.append(TableLfClient.TABLE_NAME).append(" client  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=client.").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
						" malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=malist.").append(TableLfMalist.GUID).append(" left join ").append(TableLfEmployee.TABLE_NAME).append(
						" employee " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=employee.").append(TableLfEmployee.GUID)
				.append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
				.append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
				.append("=udgroup.").append(TableLfUdgroup.UDG_ID).append(" where udgroup.")
				.append(TableLfUdgroup.UDG_ID).append("=").append(groupId).append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc");
	
		try {
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sqlStr.toString());
			ps = conn.prepareStatement(sqlStr.toString());
			//执行SQL
			rs = ps.executeQuery();
			while (rs.next()) {
				//获取手机号码
				//returnList.add(rs.getString(2));
				buffer.append(rs.getString(2)).append(",");
			}
		} catch (SQLException e) {
			EmpExecutionContext.error(e,"获取群组手机号码失败！");
			throw e;
		}finally {
			//关闭数据库资源
			try {
				close(rs, ps, conn);
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		return buffer.toString();
		
	}
	
	
	/**
	 *   获取员工的手机号码
	 * @param depIdStr
	 * @param depPathList
	 * @return
	 * @throws Exception
	 */
	public String findEmployeePhoneByDepIds(String depIdStr,List<String> depPathList) throws Exception {
		//List<String> returnList=new ArrayList<String>();
		StringBuilder phoneStr = new StringBuilder();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select emp.").append(TableLfEmployee.MOBILE).append(" from ")
		.append(TableLfEmployee.TABLE_NAME).append(" emp ").append(StaticValue.getWITHNOLOCK())
		.append(" where ");
		boolean flag = false;
		if(!"".equals(depIdStr) && depIdStr.length() > 0){
			depIdStr = depIdStr.substring(0, depIdStr.length()-1);
			buffer.append("  emp.").append(TableLfEmployee.DEP_ID).append(" in ( ").append(depIdStr).append(" )");
			flag = true;
		}
		if(depPathList != null && depPathList.size() > 0){
			if(flag){
				buffer.append(" or ");
			}
			for(int i=0;i<depPathList.size();i++){
				String depstr = "dep"+i;
				if(i > 0){
					buffer.append(" or ");
				}
				buffer.append(" emp.").append(TableLfEmployee.DEP_ID).append(" in (select ")
				.append(depstr).append(".").append(TableLfEmployeeDep.DEP_ID).append(" from ")
				.append(TableLfEmployeeDep.TABLE_NAME).append(" ").append(depstr).append(StaticValue.getWITHNOLOCK()).append(" where ")
				.append(depstr).append(".").append(TableLfClientDep.DEP_PATH).append(" like ")
				.append("'").append(depPathList.get(i)).append("%')");
			}
		}
		try {
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + buffer.toString());
			ps = conn.prepareStatement(buffer.toString());
			//执行SQL
			rs = ps.executeQuery();
			while (rs.next()) {
				//获取手机号码
				//returnList.add(rs.getString(1));
				phoneStr.append(rs.getString(1)).append(",");
			}
		} catch (SQLException e) {
			EmpExecutionContext.error(e,"获取员工手机号码失败！");
			throw e;
		}finally {
			//关闭数据库资源
			try {
				close(rs, ps, conn);
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		//返回结果集
		return phoneStr.toString();
		
	}
	
	/**
	 *   获取员工的手机号码(重载方法)
	 * @param depIdStr
	 * @param depPathList
	 * @return
	 * @throws Exception
	 */
	public String findEmployeePhoneByDepIds(String depIdStr,List<String> depPathList,String corpCode) throws Exception {
		//List<String> returnList=new ArrayList<String>();
		StringBuilder phoneStr = new StringBuilder();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select emp.").append(TableLfEmployee.MOBILE).append(" from ")
		.append(TableLfEmployee.TABLE_NAME).append(" emp ").append(StaticValue.getWITHNOLOCK())
		.append(" where emp.corp_code='"+corpCode+"' and ");
		boolean flag = false;
		if(!"".equals(depIdStr) && depIdStr.length() > 0){
			depIdStr = depIdStr.substring(0, depIdStr.length()-1);
			buffer.append("  (emp.").append(TableLfEmployee.DEP_ID).append(" in ( ").append(depIdStr).append(" )");
			flag = true;
		}
		if(depPathList != null && depPathList.size() > 0){
			if(flag){
				buffer.append(" or ");
			}else{
				buffer.append(" (");
			}
			for(int i=0;i<depPathList.size();i++){
				String depstr = "dep"+i;
				if(i > 0){
					buffer.append(" or ");
				}
				buffer.append(" emp.").append(TableLfEmployee.DEP_ID).append(" in (select ")
				.append(depstr).append(".").append(TableLfEmployeeDep.DEP_ID).append(" from ")
				.append(TableLfEmployeeDep.TABLE_NAME).append(" ").append(depstr).append(StaticValue.getWITHNOLOCK()).append(" where ")
				.append(depstr).append(".").append(TableLfClientDep.DEP_PATH).append(" like ")
				.append("'").append(depPathList.get(i)).append("%')");
			}
			buffer.append(" ) ");
		}else if(flag){
			buffer.append(" ) ");
		}
		try {
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + buffer.toString());
			ps = conn.prepareStatement(buffer.toString());
			//执行SQL
			rs = ps.executeQuery();
			while (rs.next()) {
				//获取手机号码
				//returnList.add(rs.getString(1));
				phoneStr.append(rs.getString(1)).append(",");
			}
		} catch (SQLException e) {
			EmpExecutionContext.error(e,"获取员工手机号码失败！");
			throw e;
		}finally {
			//关闭数据库资源
			try {
				close(rs, ps, conn);
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		//返回结果集
		return phoneStr.toString();
		
	}
	
	/**
	 *   处理客户机构查询其客户的手机号码的方法
	 * @param depIdStr	不包含的机构串
	 * @param depPathList	包含子机构的机构PATH
	 * @return
	 * @throws Exception
	 */
	public String findClientPhoneByDepIds(String depIdStr,List<String> depPathList) throws Exception {
		StringBuilder phoneStr = new StringBuilder();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//示范sql语句
		/*select client.mobile from lf_client client where  
		or client.dep_id in (select dep1.dep_id from lf_client_dep dep1 where dep1.dep_path like '%/82/%')
		or client.dep_id in (select dep2.dep_id from lf_client_dep dep2 where dep2.dep_path like '1/82/%')*/
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select client.").append(TableLfClient.MOBILE).append(" from ")
		.append(TableLfClient.TABLE_NAME).append(" client ").append(StaticValue.getWITHNOLOCK())
		.append(" where ");
		boolean flag = false;
		if(!"".equals(depIdStr) && depIdStr.length() > 0){
			depIdStr = depIdStr.substring(0, depIdStr.length()-1);
			buffer.append(" client.").append(TableLfClient.DEP_ID).append(" in ( ").append(depIdStr).append(" )");
			flag = true;
		}
		
		if(depPathList != null && depPathList.size() > 0){
			if(flag){
				buffer.append(" or ");
			}
			for(int i=0;i<depPathList.size();i++){
				String depstr = "dep"+i;
				if(i > 0){
					buffer.append(" or ");
				}
				buffer.append("  client.").append(TableLfClient.DEP_ID).append(" in (select ")
				.append(depstr).append(".").append(TableLfClientDep.DEP_ID).append(" from ")
				.append(TableLfClientDep.TABLE_NAME).append(" ").append(depstr).append(StaticValue.getWITHNOLOCK()).append(" where ")
				.append(depstr).append(".").append(TableLfClientDep.DEP_PATH).append(" like ")
				.append("'").append(depPathList.get(i)).append("%')");
			}
		}
		try {
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + buffer.toString());
			ps = conn.prepareStatement(buffer.toString());
			//执行SQL
			rs = ps.executeQuery();
			while (rs.next()) {
				//获取手机号码
				//returnList.add(rs.getString(1));
				phoneStr.append(rs.getString(1)).append(",");
			}
		} catch (SQLException e) {
			EmpExecutionContext.error(e,"查询客户机构下的客户手机号码失败！");
			throw e;
		}finally {
			//关闭数据库资源
			try {
				close(rs, ps, conn);
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		//返回结果集
		return phoneStr.toString();
		
	}
	
	
	
	/**
	 * 获取员工机构列表——用于构建机构树
	 * 
	 * @param userId
	 *            当前操作员的ID
	 * @param depId
	 *            传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(
			String userId, String depId) throws Exception
	{
		String sql = "";
		List<LfEmployeeDep> lfEmployeeDepList = null;
		if (depId == null || "".equals(depId))
		{
			/*
			 * sql = new StringBuffer(" select e.* from ").append(
			 * TableLfEmployeeDep
			 * .TABLE_NAME).append(" e "+StaticValue.getWITHNOLOCK()+",").append(
			 * TableLfEmpDepConn
			 * .TABLE_NAME).append(" c "+StaticValue.getWITHNOLOCK())
			 * .append(" where c.")
			 * .append(TableLfEmpDepConn.USER_ID).append(" = "
			 * ).append(userId).append(" and (c.")
			 * .append(TableLfEmpDepConn.DEP_ID
			 * ).append(" =e.").append(TableLfEmployeeDep.DEP_ID)
			 * .append(" or ")
			 * .append(TableLfEmployeeDep.PARENT_ID).append(" = c."
			 * ).append(TableLfEmpDepConn.DEP_ID) .append(")").toString();
			 */
			// 这里是为了解决员工机构关联表出现了多条记录的处理方法，产生2条记录的原因可能是机器卡，或者同步
			List<LfEmpDepConn> connList = null;
			StringBuffer sb = new StringBuffer();
			sb.append(" select * from ").append(TableLfEmpDepConn.TABLE_NAME)
					.append(" c " + StaticValue.getWITHNOLOCK()).append(" where c.")
					.append(TableLfEmpDepConn.USER_ID).append(" = ").append(
							userId);
			connList = findEntityListBySQL(LfEmpDepConn.class, sb.toString(),
					StaticValue.EMP_POOLNAME);
			//LfEmpDepConn conn = null;
			if (connList != null && connList.size() > 0)
			{
				//conn = connList.get(0);
				//Long id = conn.getDepId();
				String ids = "";
				for(LfEmpDepConn co:connList){
					ids+=co.getDepId()+",";
				}
				ids = ids.substring(0,ids.lastIndexOf(","));
				sql = new StringBuffer(" select e.* from ").append(
						TableLfEmployeeDep.TABLE_NAME).append(
						" e " + StaticValue.getWITHNOLOCK()).append(" where  e.")
						.append(TableLfEmployeeDep.DEP_ID).append(" in(")
						.append(ids).append(") or ").append(
								TableLfEmployeeDep.PARENT_ID).append(" in(")
						.append(ids).append(") order by ").append(
								TableLfEmployeeDep.ADD_TYPE).append(
								" " + StaticValue.ASC).toString();
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
						sql, StaticValue.EMP_POOLNAME);
			}
		} else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfEmployeeDep.PARENT_ID).append(" = ").append(depId).append(" order by ").append(
							TableLfEmployeeDep.ADD_TYPE).append(" " + StaticValue.ASC).toString();
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
		}

		return lfEmployeeDepList;
	}
	
	
	/**
	 * 获取员工机构列表——用于构建机构树(重载方法)
	 * 
	 * @param userId
	 *            当前操作员的ID
	 * @param depId
	 *            传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @param corpCode 企业编码           
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(
			String userId, String depId,String corpCode) throws Exception
	{
		String sql = "";
		List<LfEmployeeDep> lfEmployeeDepList = null;
		if (depId == null || "".equals(depId))
		{
			// 这里是为了解决员工机构关联表出现了多条记录的处理方法，产生2条记录的原因可能是机器卡，或者同步
			List<LfEmpDepConn> connList = null;
			StringBuffer sb = new StringBuffer();
			sb.append(" select * from ").append(TableLfEmpDepConn.TABLE_NAME)
					.append(" c " + StaticValue.getWITHNOLOCK()).append(" where c.")
					.append(TableLfEmpDepConn.USER_ID).append(" = ").append(
							userId);
			connList = findEntityListBySQL(LfEmpDepConn.class, sb.toString(),
					StaticValue.EMP_POOLNAME);
			if (connList != null && connList.size() > 0)
			{
				 //机构ID字符串
				String ids = "";
				for(LfEmpDepConn co:connList){
					ids+=co.getDepId()+",";
				}
				//去掉最后一个逗号
				ids = ids.substring(0,ids.lastIndexOf(","));
				//生成SQL语句，必须加企业编码
				sql = new StringBuffer(" select * from ").append(
						TableLfEmployeeDep.TABLE_NAME).append(
						" " + StaticValue.getWITHNOLOCK()).append(" where  (")
						.append(TableLfEmployeeDep.DEP_ID).append(" in(")
						.append(ids).append(") or ").append(
								TableLfEmployeeDep.PARENT_ID).append(" in(")
						.append(ids).append(")) ").append(" AND CORP_CODE='"+corpCode+"' ").append(" order by ").append(
								TableLfEmployeeDep.ADD_TYPE).append(
								" " + StaticValue.ASC).toString();
				//查询数据
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
						sql, StaticValue.EMP_POOLNAME);
			}
		} else
		{
			//查询的SQL语句中增加企业编码
			sql = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfEmployeeDep.PARENT_ID).append(" = ").append(depId).append(" AND CORP_CODE='"+corpCode+"' ").append(" order by ").append(
							TableLfEmployeeDep.ADD_TYPE).append(" " + StaticValue.ASC).toString();
			//查询数据
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
		}

		return lfEmployeeDepList;
	}
	
	/**
	 * 获取客户机构列表——用于构建机构树
	 * 
	 * @param userId
	 *            当前操作员的ID
	 * @param depId
	 *            传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,
                                                                String depId) throws Exception
	{
		String sql = "";
		if (depId == null || "".equals(depId))
		{
			sql = new StringBuffer(" select e.* from ").append(
					TableLfClientDep.TABLE_NAME).append(
					" e " + StaticValue.getWITHNOLOCK() + ",").append(
					TableLfCliDepConn.TABLE_NAME).append(
					" c " + StaticValue.getWITHNOLOCK()).append(" where c.").append(
					TableLfCliDepConn.USER_ID).append(" = ").append(userId)
					.append(" and (c.").append(TableLfEmpDepConn.DEP_ID)
					.append(" =e.").append(TableLfClientDep.DEP_ID).append(
							" or ").append(TableLfClientDep.PARENT_ID).append(
							" = c.").append(TableLfCliDepConn.DEP_ID).append(
							")").toString();
		} else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfClientDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfClientDep.PARENT_ID).append(" = ").append(depId)
					.toString();
		}
		List<LfClientDep> lfClientDepList = findEntityListBySQL(
				LfClientDep.class, sql, StaticValue.EMP_POOLNAME);
		return lfClientDepList;
	}
	
	/**
	 * 获取客户机构列表——用于构建机构树(重载方法)
	 * 
	 * @param userId
	 *            当前操作员的ID
	 * @param depId
	 *            传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @param corpCode 企业编码
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,
                                                                String depId, String corpCode) throws Exception
	{
		//定义SQL语句字符串
		String sql = "";
		//查询第一级和第二级机构
		//查询必须带企业编码
		if (depId == null || "".equals(depId))
		{
			sql = new StringBuffer(" select e.* from ").append(
					TableLfClientDep.TABLE_NAME).append(
					" e " + StaticValue.getWITHNOLOCK() + ",").append(
					TableLfCliDepConn.TABLE_NAME).append(
					" c " + StaticValue.getWITHNOLOCK()).append(" where c.").append(
					TableLfCliDepConn.USER_ID).append(" = ").append(userId).append(" AND e.CORP_CODE='"+corpCode+"' ")
					.append(" and (c.").append(TableLfEmpDepConn.DEP_ID)
					.append(" =e.").append(TableLfClientDep.DEP_ID).append(
							" or ").append(TableLfClientDep.PARENT_ID).append(
							" = c.").append(TableLfCliDepConn.DEP_ID).append(
							")").toString();
		} else
		{
			//根据父机构，查询第一级子机构
			//查询必须带企业编码
			sql = new StringBuffer(" select * from ").append(
					TableLfClientDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfClientDep.PARENT_ID).append(" = ").append(depId).append(" AND CORP_CODE='"+corpCode+"' ")
					.toString();
		}
		//查询数据
		List<LfClientDep> lfClientDepList = findEntityListBySQL(
				LfClientDep.class, sql, StaticValue.EMP_POOLNAME);
		return lfClientDepList;
	}
	
	/**
	 *   增加分页	查询员工群组中的内容
	 * @param groupId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> findGroupUserByIds(Long groupId, PageInfo pageInfo) throws Exception{
		//初始化LIST
		List<GroupInfoVo> returnVoList = null;
		//拼凑查询字段
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(
				TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(
				TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(
				TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(") when 0 then employee.").append(TableLfEmployee.MOBILE).append("  else malist.")
				.append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
						") when 0 then employee.").append(TableLfEmployee.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
						" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		//拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
				.append(TableLfEmployee.TABLE_NAME).append(" employee  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=employee.").append(TableLfEmployee.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
						" malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
				.append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
				.append("=udgroup.").append(TableLfUdgroup.GROUP_ID).append(" where udgroup.")
				.append(TableLfUdgroup.UDG_ID).append("=").append(groupId);
		//查询
		String countSql = "select count(*) totalcount from ("+sqlStr.toString()+") a";
		sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc");
		returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	
	
	public List<GroupInfoVo> findGroupClientByIds(Long groupId, PageInfo pageInfo) throws Exception{
		//初始化LIST
		List<GroupInfoVo> returnVoList = null;
		//拼凑查询字段
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(
				TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(
				TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(
				TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(") when 1 then client.").append(TableLfClient.MOBILE).append("  else malist.")
				.append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
						") when 1 then client.").append(TableLfClient.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
						" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		//拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
				.append(TableLfClient.TABLE_NAME).append(" client  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=client.").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
						" malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
				.append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
				.append("=udgroup.").append(TableLfUdgroup.UDG_ID).append(" where udgroup.")
				.append(TableLfUdgroup.UDG_ID).append("=").append(groupId);
		//查询
		String countSql = "select count(*) totalcount from ( " + sqlStr.toString() + " ) a";
		sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc ");

		returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	
	
	
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
		String fieldSql =getFieldSql();
		//获取表SQL
		String tableSql = getTableSql();
		//获取审核的SQL
		String dominationSql =getDominationSql(loginUserId);
		//获取查询SQL
		String conditionSql = getConditionSql(lfTemplateVo);
		//获取排序SQL
		String orderBySql = getOrderBySql();
		//拼接SQL
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//获取总计
		String countSql = new StringBuffer("select count(distinct lftemplate.").append(TableLfTemplate.TM_ID).append(") totalcount ")
				.append(tableSql).append(dominationSql).append(conditionSql)
				.toString();
		//查询数据
		List<LfTemplateVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						LfTemplateVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		//返回
		return returnList;
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
		//获取字段SQL
		String fieldSql = getFieldSql();
		//获取表SQL
		String tableSql = getTableSql();
		//获取审批SQL
		String dominationSql = getDominationSql(loginUserId);
		//获取查询条件SQL
		String conditionSql = getConditionSql(lfTemplateVo);
		//获取排序SQL
		String orderBySql = getOrderBySql();
		//获取拼接SQL
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//查询数据
		List<LfTemplateVo> returnVoList = findVoListBySQL(LfTemplateVo.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	
	
	/**
	 *    查询客户机构人员
	 * @param clientDep	当前机构对象
	 * @param containType	是否包含  1包含   2不包含
	 * @param pageInfo	分页
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findClientsByDepId(LfClientDep clientDep, Integer containType, PageInfo pageInfo) throws Exception
	{
		List<DynaBean> beanList = null;
		String sql = "select distinct client.NAME,client.MOBILE,client.GUID " ;
		String countSql = "select count(*) totalcount ";
		String baseSql = " from LF_CLIENT client inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ";
		StringBuffer conditionSql = new StringBuffer(" where ");
		conditionSql.append(" client.CORP_CODE = '").append(clientDep.getCorpCode()).append("'");
		if(containType == 1){
			conditionSql.append(" and depsp.DEP_ID in (select DEP_ID from LF_CLIENT_DEP where DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
		}else if(containType == 2){
			conditionSql.append(" and depsp.DEP_ID = ").append(clientDep.getDepId());
		}
		String orderSql = " order by client.GUID DESC";
		sql += baseSql;
		countSql += baseSql;
		sql += conditionSql + orderSql;
		countSql += conditionSql;
		beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return beanList;
	}
	
	
	
	/**
	 *   获取机构客户 人数
	 * @param clientDep	机构对象
	 * @param containType 1包含  2是不包含
	 * @return
	 * @throws Exception
	 */
	public Integer findClientsCountByDepId(LfClientDep clientDep, Integer containType) throws Exception
	{
	/*	select COUNT(c.CLIENT_ID) from 
		(select DISTINCT a.CLIENT_ID  from LF_ClIENT_DEP_SP a where  and a.DEP_ID in 
			(select b.DEP_ID from LF_CLIENT_DEP b where  and b.DEP_PATH LIKE '1/%')
		)c*/
		StringBuffer sqlBuffer = new StringBuffer(" select COUNT(c.CLIENT_ID) as totalcount from ") ;
		sqlBuffer.append(" (select a.CLIENT_ID  from LF_ClIENT_DEP_SP a where ");
		if(containType == 1){
			sqlBuffer.append(" a.DEP_ID in (select b.DEP_ID from LF_CLIENT_DEP b where b.DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
		}else if(containType == 2){
			sqlBuffer.append(" a.DEP_ID = ").append(clientDep.getDepId());
		}
		sqlBuffer.append(")c");
		
		Integer count = findCountBySQL(sqlBuffer.toString());
		
		return count;
	}
	
	
	/**
	 *   处理客户机构查询其客户的手机号码的方法
	 * @param depIdStr	不包含的机构串
	 * @param depPathList	包含子机构的机构PATH
	 * @return
	 * @throws Exception
	 */
	public String getClientPhoneByDepIds(String depIdStr,List<String> depPathList) throws Exception {
		StringBuilder phoneStr = new StringBuilder();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//示范sql语句
		/*select distinct client.MOBILE  from LF_CLIENT client 
			inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID 
			where   and client.CORP_CODE = '100001'
			and (depsp.DEP_ID in (1,2,3) or  depsp.DEP_ID in 
			(select DEP_ID from LF_CLIENT_DEP where DEP_PATH like '1/%') );*/
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select client.MOBILE  from LF_CLIENT client ");
		buffer.append(" inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ");
		buffer.append(" where  ");
		boolean flag = false;
		if(!"".equals(depIdStr) && depIdStr.length() > 0){
			depIdStr = depIdStr.substring(0, depIdStr.length()-1);
			buffer.append(" ( depsp.DEP_ID in (").append(depIdStr).append(")");
			flag = true;
		}
		if(depPathList != null && depPathList.size() > 0){
			if(flag){
				buffer.append(" or ");
			}else{
				buffer.append(" (");
			}
			for(int i=0;i<depPathList.size();i++){
				String depstr = "dep"+i;
				if(i > 0){
					buffer.append(" or ");
				}
				buffer.append(" depsp.DEP_ID in ( select ").append(depstr)
				.append(".DEP_ID from LF_CLIENT_DEP ").append(depstr)
				.append("  where ").append(depstr).append(".DEP_PATH like ")
				.append("'").append(depPathList.get(i)).append("%')");
			}
			buffer.append(" ) ");
		}else if(flag){
			buffer.append(" ) ");
		}
		
		try {
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + buffer.toString());
			ps = conn.prepareStatement(buffer.toString());
			//执行SQL
			rs = ps.executeQuery();
			while (rs.next()) {
				//获取手机号码
				//returnList.add(rs.getString(1));
				phoneStr.append(rs.getString(1)).append(",");
			}
		} catch (SQLException e) {
			EmpExecutionContext.error(e,"查询客户机构下客户手机号码失败！");
			throw e;
		}finally {
			//关闭数据库资源
			try {
				close(rs, ps, conn);
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		//返回结果集
		return phoneStr.toString();
		
	}
	
	
	/**
	 *   处理客户机构查询其客户的手机号码的方法
	 * @param depIdStr	不包含的机构串
	 * @param depPathList	包含子机构的机构PATH
	 * @return
	 * @throws Exception
	 */
	public String getClientPhoneByDepIds(String depIdStr,List<String> depPathList,String corpCode) throws Exception {
		StringBuilder phoneStr = new StringBuilder();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//示范sql语句
		/*select distinct client.MOBILE  from LF_CLIENT client 
			inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID 
			where   and client.CORP_CODE = '100001'
			and (depsp.DEP_ID in (1,2,3) or  depsp.DEP_ID in 
			(select DEP_ID from LF_CLIENT_DEP where DEP_PATH like '1/%') );*/
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select client.MOBILE  from LF_CLIENT client ");
		buffer.append(" inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ");
		buffer.append(" where   client.corp_code='"+corpCode+"' and ");
		boolean flag = false;
		if(!"".equals(depIdStr) && depIdStr.length() > 0){
			depIdStr = depIdStr.substring(0, depIdStr.length()-1);
			buffer.append(" ( depsp.DEP_ID in (").append(depIdStr).append(")");
			flag = true;
		}
		if(depPathList != null && depPathList.size() > 0){
			if(flag){
				buffer.append(" or ");
			}else{
				buffer.append(" (");
			}
			for(int i=0;i<depPathList.size();i++){
				String depstr = "dep"+i;
				if(i > 0){
					buffer.append(" or ");
				}
				buffer.append(" depsp.DEP_ID in ( select ").append(depstr)
				.append(".DEP_ID from LF_CLIENT_DEP ").append(depstr)
				.append("  where ").append(depstr).append(".DEP_PATH like ")
				.append("'").append(depPathList.get(i)).append("%')");
			}
			buffer.append(" ) ");
		}else if(flag){
			buffer.append(" ) ");
		}
		
		try {
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + buffer.toString());
			ps = conn.prepareStatement(buffer.toString());
			//执行SQL
			rs = ps.executeQuery();
			while (rs.next()) {
				//获取手机号码
				//returnList.add(rs.getString(1));
				phoneStr.append(rs.getString(1)).append(",");
			}
		} catch (SQLException e) {
			EmpExecutionContext.error(e,"查询客户机构下客户手机号码失败！");
			throw e;
		}finally {
			//关闭数据库资源
			try {
				close(rs, ps, conn);
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		//返回结果集
		return phoneStr.toString();
		
	}
	
	
	/**
	 * 获取查询字段
	 * @return
	 */
	private  String getFieldSql()
	{
		//拼接查询字段
		String fieldSql = new StringBuffer("select distinct lftemplate.* ,sysuser.")
				.append(TableLfSysuser.NAME).append(",sysuser.").append(
						TableLfSysuser.USER_NAME).append(",sysuser.")
				.append(TableLfSysuser.USER_STATE)
				.append(",lfdep.").append(TableLfDep.DEP_NAME).append(",0 as ").append(TableLfTmplRela.id)
				.append(",1 as ").append(TableLfTmplRela.shareType)
				.toString();
		//返回查询字段字符串
		return fieldSql;
	}

	/**
	 * 获取查询表名sql
	 * @return
	 */
	private  String getTableSql()
	{
		//拼接sql
//		String tableSql = new StringBuffer(" from ").append(
//				TableLfTemplate.TABLE_NAME).append(" lftemplate left join ")
//				.append(TableLfSysuser.TABLE_NAME).append(
//						" sysuser  on lftemplate.").append(
//						TableLfTemplate.USER_ID).append("= sysuser.").append(
//						TableLfSysuser.USER_ID).append(" left join ").append(
//						TableLfDep.TABLE_NAME).append(" lfdep on sysuser.")
//				.append(TableLfSysuser.DEP_ID).append("=lfdep.").append(
//						TableLfDep.DEP_ID).toString();
		//返回查询表名字符串
		return TemplateSQL.getTableSql();
	}

	/**
	 * 获取共享条件sql
	 * @param loginUserId
	 * @return
	 */
	private  String getDominationSql(Long loginUserId)
	{
		//拼接sql
//		StringBuffer domination = new StringBuffer("select ").append(
//				TableLfDomination.DEP_ID).append(" from ").append(
//				TableLfDomination.TABLE_NAME).append(" where ").append(
//				TableLfDomination.USER_ID).append("=").append(loginUserId);
//		String dominationSql = new StringBuffer(" where (sysuser.").append(
//				TableLfSysuser.USER_ID).append("=").append(loginUserId).append(
//				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
//				.append(domination).append("))").toString();
		return TemplateSQL.getDominationSql(loginUserId)+TemplateSQL.getShareSql(loginUserId)+")";
	}
	
	/**
	 * 查询条件
	 * @param lfTemplateVo
	 * @return
	 */
	private  String getConditionSql(LfTemplateVo lfTemplateVo)
	{
		StringBuffer conditionSql = new StringBuffer();

		//查询条件---类型
		if (lfTemplateVo.getDsflag() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.DS_FLAG).append("=").append(
					lfTemplateVo.getDsflag());
		}
		//查询条件---状态
		if (lfTemplateVo.getTmState() != null) {
			conditionSql.append(" and lftemplate.").append(
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
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_MSG).append(" like '%").append(
					lfTemplateVo.getTmMsg()).append("%'");
		}
		//查询条件---模板名称
		if (lfTemplateVo.getTmName() != null
				&& !"".equals(lfTemplateVo.getTmName())) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_NAME).append(" like '%").append(
					lfTemplateVo.getTmName()).append("%'");
		}
		//查询条件---类型
		if (lfTemplateVo.getTmpType() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TMP_TYPE).append("=").append(
					lfTemplateVo.getTmpType());
		}
		//查询条件---审核是否通过
		if (lfTemplateVo.getIsPass() != null) {
			if (lfTemplateVo.getIsPass() == 5) {
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ISPASS).append(" in (0,1)");
			} else {
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ISPASS).append("=").append(
						lfTemplateVo.getIsPass());
			}
		}
		//查询条件---操作员id
		if (lfTemplateVo.getUserId() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.USER_ID).append("=").append(
					lfTemplateVo.getUserId());
		}
		//查询条件---模板类型
//		if (lfTemplateVo.getDsflag() != null
//				&& !"".equals(lfTemplateVo.getDsflag())) {
//			conditionSql.append(" and lftemplate.").append(
//					TableLfTemplate.DS_FLAG).append("=").append(
//					lfTemplateVo.getDsflag());
//		}
		//查询条件----运营商审批状态
		if (lfTemplateVo.getAuditstatus() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.AUDITSTATUS).append("=").append(
					lfTemplateVo.getAuditstatus());
		}
		//查询条件----运营商提交状态
		if (lfTemplateVo.getSubmitstatus() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.SUBMITSTATUS).append("=").append(
					lfTemplateVo.getSubmitstatus());
		}
		//返回拼接sql
		return conditionSql.toString();
	}

	/**
	 * 获取排序sql
	 * @return
	 */
	private String getOrderBySql()
	{
		//拼接sql
		String orderBySql = new StringBuffer(" order by lftemplate.").append(
				TableLfTemplate.ADD_TIME).append(" desc").toString();
		return orderBySql;
	}

}
