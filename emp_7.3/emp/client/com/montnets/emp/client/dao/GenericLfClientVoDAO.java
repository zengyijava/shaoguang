package com.montnets.emp.client.dao;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.client.vo.LfClientVo;
import com.montnets.emp.client.vo.LfEmpDepConnVo;
import com.montnets.emp.client.vo.LfSysuser2Vo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.client.LfCliDepConn;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClient5Pro;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.client.LfClientMultiPro;
import com.montnets.emp.table.client.TableLfCliDepConn;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.client.TableLfCustFieldValue;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.EmpUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

/**
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 
 * @description
 */
public class GenericLfClientVoDAO extends SuperDAO
{

	public List<LfClientVo> findClientVo2(Long loginUserID,String corpcode,
			LfClientVo lfClientVo, PageInfo pageInfo) throws Exception
	{
		//查询sql
		String depConnSql = new StringBuffer(" select ").append(
				TableLfCliDepConn.DEP_ID).append(" from ").append(
				TableLfCliDepConn.TABLE_NAME).append(" where ").append(
				TableLfCliDepConn.USER_ID).append(" = ").append(loginUserID).toString();
		//执行查询
		List<LfCliDepConn> codeDepConnsList = findPartEntityListBySQL(
				LfCliDepConn.class, depConnSql, StaticValue.EMP_POOLNAME);
		//没记录
		if (codeDepConnsList == null || codeDepConnsList.size() == 0)
		{
			//返回初始化
			return new ArrayList<LfClientVo>();
		}
		//获取机构id
		Long depConnId = codeDepConnsList.get(0).getDepId();
		//查询sql
		String sql="";
		//lirj 2012.05.05 add 
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
	   		 sql = new StringBuffer("select client.").append(
					TableLfClient.CLIENT_ID).append(",client.").append(
					TableLfClient.DEP_ID).append(",client.").append(
					TableLfClient.MOBILE).append(",client.").append(
					TableLfClient.NAME).append(",client.")
					.append(TableLfClient.MSN).append(",client.").append(
							TableLfClient.QQ).append(",client.").append(
							TableLfClient.SEX).append(",client.").append(
							TableLfClient.BIRTHDAY).append(",client.").append(
							TableLfClient.OPH).append(",client.").append(
							TableLfClient.E_MAIL).append(",client.").append(
							TableLfClient.DEP_CODE).append(",client.").append(
							TableLfClient.PHONE).append(",client.").append(
							TableLfClient.BATCH_NO).append(",client.").append(
							TableLfClient.GUID).append(",client.").append(
							TableLfClient.CLIENT_CODE).toString();
	   		
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			
			 sql = new StringBuffer("select client.").append(
					TableLfClient.CLIENT_ID).append(",client.").append(
					TableLfClient.DEP_ID).append(",client.").append(
					TableLfClient.MOBILE).append(",client.").append(
					TableLfClient.NAME).append(",client.")
					.append(TableLfClient.MSN).append(",client.").append(
							TableLfClient.QQ).append(",client.").append(
							TableLfClient.SEX).append(",client.").append(
							TableLfClient.BIRTHDAY).append(",client.").append(
							TableLfClient.OPH).append(",client.").append(
							TableLfClient.E_MAIL).append(",client.").append(
							TableLfClient.DEP_CODE).append(",client.").append(
							TableLfClient.PHONE).append(",client.").append(
							TableLfClient.BATCH_NO).append(",client.").append(
							TableLfClient.GUID).append(",client.").append(
							TableLfClient.CLIENT_CODE).append(",ROW_NUMBER() Over(Order By ").append(" client.").append(TableLfClient.CORP_CODE).append(") As rn").toString();
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用mysql数据库的SQL语句
			sql = "select client.* ";
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			sql = new StringBuffer("select client.").append(
					TableLfClient.CLIENT_ID).append(",client.").append(
					TableLfClient.DEP_ID).append(",client.").append(
					TableLfClient.MOBILE).append(",client.").append(
					TableLfClient.NAME).append(",client.")
					.append(TableLfClient.MSN).append(",client.").append(
							TableLfClient.QQ).append(",client.").append(
							TableLfClient.SEX).append(",client.").append(
							TableLfClient.BIRTHDAY).append(",client.").append(
							TableLfClient.OPH).append(",client.").append(
							TableLfClient.E_MAIL).append(",client.").append(
							TableLfClient.DEP_CODE).append(",client.").append(
							TableLfClient.PHONE).append(",client.").append(
							TableLfClient.BATCH_NO).append(",client.").append(
							TableLfClient.GUID).append(",client.").append(
							TableLfClient.CLIENT_CODE).toString();
		}
	   	//分页sql
		String countSql = "select count(*) totalcount";
		//查询表格
		String tableSql = new StringBuffer(" from ").append(
				TableLfClient.TABLE_NAME).append(" client ").append(
				" where ").append(TableLfClient.CORP_CODE).append("='").append(corpcode).append("'").toString();
		//拼sql
		sql += tableSql;
		//拼接sql
		countSql += tableSql;
		//条件sql
		StringBuffer conditionSql = new StringBuffer();
		//机构id
		if (null != lfClientVo.getDepId())
		{
			conditionSql.append(" and client.").append(TableLfClient.DEP_ID)
					.append(" = ").append(lfClientVo.getDepId());
		} else
		{
			conditionSql.append(" and client.").append(TableLfClient.DEP_ID)
					.append(" = ").append(depConnId);
		}
		//姓名
		if (null != lfClientVo.getName() && !"".equals(lfClientVo.getName()))
		{
			conditionSql.append(" and client.").append(TableLfClient.NAME)
					.append(" like '%").append(lfClientVo.getName()).append(
							"%'");
		}
		//手机号
		if (null != lfClientVo.getMobile()
				&& !"".equals(lfClientVo.getMobile()))
		{
			conditionSql.append(" and client.").append(TableLfClient.MOBILE)
					.append(" like '%").append(lfClientVo.getMobile()).append(
							"%'");
		}
		//群组id
		if (null != lfClientVo.getUdgId())
		{
			String guidSql = new StringBuffer("select ").append(
					TableLfList2gro.GUID).append(" from ").append(
					TableLfList2gro.TABLE_NAME).append(" where ").append(
					TableLfList2gro.UDG_ID).append("=").append(
					lfClientVo.getUdgId()).toString();
			conditionSql.append(" and client.").append(TableLfEmployee.GUID)
					.append(" not in (").append(guidSql).append(")");
		}
		//拼接条件
		sql += conditionSql.toString();
		//拼接条件
		countSql += conditionSql.toString();
		//调用通讯录公共分页方法 分数据库存实现
		List<LfClientVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
						LfClientVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		//拼对象
		returnList = completeLfClientVo(loginUserID,corpcode, returnList);
		//返回结果
		return returnList;
	}
	
	public List<LfClientVo> findClientVo(Long loginUserID,String corpcode,
			LfClientVo lfClientVo, PageInfo pageInfo) throws Exception
	{

		//查询sql
		String sql="";
		
	   	if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			 sql = new StringBuffer("select client.*").append(",ROW_NUMBER() Over(Order By ")
			 			.append(" client.").append(TableLfClient.CORP_CODE).append(") As rn").toString();
		} 
		else 
		{
			sql = "select client.* ";
		}
	   	//分页sql
		String countSql = "select count(*) totalcount";
		//查询表格
		String tableSql = new StringBuffer(" from ")
				.append(TableLfClient.TABLE_NAME).append(" client ").append(" where ")
				.append(TableLfClient.CORP_CODE).append("='").append(corpcode).append("'").toString();
		//拼接sql
		sql += tableSql;
		//拼接sql
		countSql += tableSql;
		//条件sql
		StringBuffer conditionSql = new StringBuffer();
		String depIdTemp = null;
		String depPath = null;
		String depConnSql = null;
		
		if (null != lfClientVo.getDepId())
		{
			//查询选择的机构的子机构
			depIdTemp = lfClientVo.getDepId().toString();
			
			depConnSql = new StringBuffer("select * from ").append(TableLfClientDep.TABLE_NAME).
				append(" where ").append(TableLfClientDep.DEP_ID).append("=").append(depIdTemp).toString();
		} 
		else
		{
			//没带depId条件，则查绑定机构的子机构
			depConnSql = new StringBuffer(" select dep.* from ").append(TableLfClientDep.TABLE_NAME).append(" dep left join ")
				.append(TableLfCliDepConn.TABLE_NAME).append(" conn on conn.").append(TableLfCliDepConn.DEP_ID).append(" = dep.")
				.append(TableLfClientDep.DEP_ID).append(" where conn.").append(TableLfCliDepConn.USER_ID).append(" = ")
				.append(loginUserID).toString();
			
		}
		//获取登录操作员所绑定的机构ID
		List<LfClientDep> codeDepConnsList = findPartEntityListBySQL(
				LfClientDep.class, depConnSql, StaticValue.EMP_POOLNAME);
		
		if (codeDepConnsList == null || codeDepConnsList.size() == 0)
		{
			//没权限返回空
			return new ArrayList<LfClientVo>();
		}

		depPath = codeDepConnsList.get(0).getDeppath();
		//获取子机构，包含自己
		conditionSql.append(" and (")
			.append(getClientChildByDepPath(depPath,"client."+TableLfClient.DEP_ID))
			.append(") ");
		//姓名
		if (null != lfClientVo.getName() && !"".equals(lfClientVo.getName()))
		{
			conditionSql.append(" and client.").append(TableLfClient.NAME)
					.append(" like '%").append(lfClientVo.getName()).append(
							"%'");
		}
		//手机号
		if (null != lfClientVo.getMobile()
				&& !"".equals(lfClientVo.getMobile()))
		{
			conditionSql.append(" and client.").append(TableLfClient.MOBILE)
					.append(" like '%").append(lfClientVo.getMobile()).append(
							"%'");
		}
		//群组id
		if (null != lfClientVo.getUdgId())
		{
			String guidSql = new StringBuffer("select ").append(
					TableLfList2gro.GUID).append(" from ").append(
					TableLfList2gro.TABLE_NAME).append(" where ").append(
					TableLfList2gro.UDG_ID).append("=").append(
					lfClientVo.getUdgId()).toString();
			conditionSql.append(" and client.").append(TableLfEmployee.GUID)
					.append(" not in (").append(guidSql).append(")");
		}
		//拼接条件sql
		sql += conditionSql.toString();
		//拼接条件sql
		countSql += conditionSql.toString();
		
		//调用通讯录公共分页方法 分数据库存实现
		List<LfClientVo> lfClientVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
						LfClientVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		
		//获取包含自己的子机构，用于拼机构名字
		String depSql = new StringBuffer("select clientDep.").append(TableLfClientDep.DEP_ID).append(" ,clientDep.").append(TableLfClientDep.DEP_NAME)
				.append(" from ").append(TableLfClientDep.TABLE_NAME)
				.append(" clientDep")
				.append(StaticValue.getWITHNOLOCK()).append(" where ")
				.append(getClientChildByDepPath(depPath, TableLfClient.DEP_ID))
				.toString();
		
		//List<LfClientDep> cliDepList = findEntityListBySQL(LfClientDep.class, depSql, StaticValue.EMP_POOLNAME);//包含了子机构的所有机构
		Map<Long, String> depMap =new AddrBookSpecialDAO().findDepIDandName(depSql);
		for (int i = 0; i < lfClientVoList.size(); i++)
		{
			lfClientVoList.get(i).setDepName(depMap.get(lfClientVoList.get(i).getDepId()));
		}
		//返回结果
		return lfClientVoList;
	}
	
	
	/**
	 *   客户记录对应机构名称设值
	 * @param loginUserID
	 * @param corpcode
	 * @param lfClientVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findClientVocd(Long loginUserID,String corpcode,
			LfClientVo lfClientVo, PageInfo pageInfo) throws Exception
	{

		List<DynaBean> beans = null;
		//List<String> depPathList = new ArrayList<String>();
		try {
			if(lfClientVo.getDepId() == null){
				//depPathList = this.getLoginClientConnByUserId(loginUserID);
				lfClientVo.setDepId(getClientConnByLoginUserId(loginUserID));
			}else if(lfClientVo.getDepId() != null){
				//LfClientDep lfdep = new BaseBiz().getById(LfClientDep.class, lfClientVo.getDepId());
				//if(lfdep != null){
					//depPathList.add(lfdep.getDeppath());
				//}else{
					//depPathList = this.getLoginClientConnByUserId(loginUserID);
				//}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"客户机构查询处理机构出现异常！");
		}
		//if(depPathList == null || depPathList.size() == 0){
			//return null;
		//}
		String fieldSql ="select  a.client_id,a.name,a.mobile,a.client_code,a.job as dep_name ,a.iscontract,a.guid ";
		String tableSql = "from lf_client a "+StaticValue.getWITHNOLOCK()+", ("+ this.getClientIDByDepID(lfClientVo.getDepId())+") b  where  a.client_id = b.client and a.corp_code = '"+corpcode+"'  " ;
		String conditionSql=getConditionSql(lfClientVo);
		String orderbySql = " order by a.client_id DESC";
		String sql=fieldSql+tableSql+conditionSql+orderbySql;
		String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql).append(conditionSql).toString(); 
		//分页
		if(pageInfo!=null)
		{
			beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		//导出全部，所以不用分页
		else
		{
			beans = getListDynaBeanBySql(sql);
		}
		//String depidconn = this.getLoginClientConnStrByUserId(loginUserID);
		beans = setClientDeps(beans,corpcode);
		return beans;
			
	}
	
	/**
	 *   客户记录对应机构名称设值
	 * @param loginUserID
	 * @param corpcode
	 * @param lfClientVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findClientVocd(Long loginUserID,String corpcode,
			LfClientVo lfClientVo, PageInfo pageInfo,LinkedHashMap<String,String> conditionMap) throws Exception
	{

		List<DynaBean> beans = null;
		//List<String> depPathList = new ArrayList<String>();
		try {
			if(lfClientVo.getDepId() == null){
				//depPathList = this.getLoginClientConnByUserId(loginUserID);
				lfClientVo.setDepId(getClientConnByLoginUserId(loginUserID));
			}else if(lfClientVo.getDepId() != null){
				//LfClientDep lfdep = new BaseBiz().getById(LfClientDep.class, lfClientVo.getDepId());
				//if(lfdep != null){
					//depPathList.add(lfdep.getDeppath());
				//}else{
					//depPathList = this.getLoginClientConnByUserId(loginUserID);
				//}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"客户机构查询处理机构出现异常！");
		}
		//if(depPathList == null || depPathList.size() == 0){
			//return null;
		//}
		String fieldSql ="select tmp.client_id,tmp.name,tmp.mobile,tmp.client_code,tmp.dep_name as dep_name,tmp.dep_id as dep_id,CL.APP_CODE ,tmp.iscontract,tmp.guid ";
		String tableSql = " from (select  a.client_id,a.name,a.mobile,a.client_code,a.job as dep_name,a.GUID,a.job as dep_id,a.iscontract from lf_client a "+StaticValue.getWITHNOLOCK()+", ("+ this.getClientIDByDepID(lfClientVo.getDepId())+") b  where  a.client_id = b.client and a.corp_code = '"+corpcode+"'  " ;
		String conditionSql=getAPPConditionSql(lfClientVo,conditionMap);
		String orderbySql = " order by tmp.client_id DESC";
		String sql=fieldSql+tableSql+conditionSql+orderbySql;
		String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql).append(conditionSql).toString(); 
		//分页
		if(pageInfo!=null)
		{
			beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		//导出全部，所以不用分页
		else
		{
			beans = getListDynaBeanBySql(sql);
		}
		//String depidconn = this.getLoginClientConnStrByUserId(loginUserID);
		beans = setClientDepInfo(beans,corpcode);
		return beans;
			
	}
	
	/**
	 *   查询该操作员的客户权限
	 * @param lguserid	当前操作员
	 * @return
	 */
	public List<String> getLoginClientConnByUserId(Long lguserid){
		List<String> depPathList = new ArrayList<String>();
		try{
			StringBuffer buffer = new StringBuffer();
			buffer.append(" select dep.DEP_PATH from LF_CLIENT_DEP dep "+StaticValue.getWITHNOLOCK()+" where dep.DEP_ID in (")
			.append(" select conn.DEP_ID from LF_CLIDEP_CONN conn "+StaticValue.getWITHNOLOCK()+" where conn.USER_ID = ").append(lguserid).append(" )");
			List<DynaBean> beans = getListDynaBeanBySql(buffer.toString());
			if(beans !=null && beans.size()>0){
				for(DynaBean bean :beans){
					Object object = bean.get("dep_path");
					if(object != null && !"".equals(object)){
						depPathList.add(String.valueOf(object));
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"查询该操作员的客户权限出现异常！");
		}
		return depPathList;
	}
	
	/**
	 *  根据当前操作员的客户权限进行查询(修改之前的代码)
	 * @param depPathList	所拥有的权限 deppath
	 * @return
	 */
	public String getClientByDepPath(List<String> depPathList){
		StringBuffer buffer = new StringBuffer();
		try{
			buffer.append(" ( select sp.client_id as client from LF_ClIENT_DEP_SP sp where " );
			for(int i=0;i<depPathList.size();i++){
				String depPath = depPathList.get(i);
				buffer.append( " sp.dep_id in (select dep_id from LF_CLIENT_DEP where dep_path like ")
				.append(" '").append(depPath).append("%'").append(" )");
				if(i != depPathList.size()-1){
					buffer.append(" or ");
				}
			}
			buffer.append("  ) ");
		}catch (Exception e) {
			EmpExecutionContext.error(e,"查询 客户权限出现异常！");
		}
		return buffer.toString();
	}
	/**
	 *  根据当前操作员的客户权限进行查询
	 * @param depPathList	所拥有的权限 deppath
	 * @return
	 */
	public String getClientIdByDepPath(List<String> depPathList){
		StringBuffer buffer = new StringBuffer();
		try{
			buffer.append(" ( select sp.client_id from LF_ClIENT_DEP_SP sp where " );
			for(int i=0;i<depPathList.size();i++){
				String depPath = depPathList.get(i);
				buffer.append( " sp.dep_id in (select dep_id from LF_CLIENT_DEP where dep_path like ")
				.append(" '").append(depPath).append("%'").append(" )");
				if(i != depPathList.size()-1){
					buffer.append(" or ");
				}
			}
			buffer.append("  ) ");
		}catch (Exception e) {
			EmpExecutionContext.error(e,"查询 客户权限出现异常！");
		}
		return buffer.toString();
	}
	
	
	/**
	 *  获取该操作员的 客户机构管辖权限的    机构ID字符串
	 * @param lguserid	当前操作员lguserid
	 * @return	机构ID字符串
	 */
	public String getLoginClientConnStrByUserId(Long lguserid){
		StringBuffer returnmsg = new StringBuffer();
		try{
			StringBuffer buffer = new StringBuffer();
			buffer.append(" select dep.DEP_PATH from LF_CLIENT_DEP dep where dep.DEP_ID in (")
			.append(" select conn.DEP_ID from LF_CLIDEP_CONN conn where conn.USER_ID = ").append(lguserid).append(" )");
			List<DynaBean> beans = getListDynaBeanBySql(buffer.toString());
			buffer = new StringBuffer();
			if(beans !=null && beans.size()>0){
				buffer.append(" select dep_id from LF_CLIENT_DEP where " );
				for(int i=0;i<beans.size();i++){
					Object object = beans.get(i).get("dep_path");
					if(object != null && !"".equals(object)){
						buffer.append(" dep_path like '").append(String.valueOf(object)).append("%' ");
						if(i != beans.size()-1){
							buffer.append(" or ");
						}
					}
				}
			}
			if(buffer != null && buffer.length()>0){
				List<DynaBean> depidbeans = getListDynaBeanBySql(buffer.toString());
				if(beans !=null && beans.size()>0){
					for(int j=0;j<depidbeans.size();j++){
						Object object = depidbeans.get(j).get("dep_id");
						if(object != null && !"".equals(object)){
							returnmsg.append(String.valueOf(object));
							if(j != depidbeans.size()-1){
								returnmsg.append(",");
							}
						}
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e," 获取该操作员的 客户机构管辖权限出现异常！");
		}
		return returnmsg.toString();
	}
	
	
	/**
	 *   根据当前操作员的权限 查询其值
	 * @param beans
	 * @param depidconn
	 * @return
	 */
	private List<DynaBean> setClientDeps(List<DynaBean> beans,String corpCode){
		try{
			StringBuffer clientIds = new StringBuffer();;
			if(beans != null && beans.size()>0){
				String sql = "";
				if(beans.size()>=1000)
				{
					sql = "select sp.client_id,sp.dep_id from LF_ClIENT_DEP_SP sp "+StaticValue.getWITHNOLOCK()+" where sp.client_id in (select client_id from lf_client "+StaticValue.getWITHNOLOCK() +" where corp_code="+corpCode+") order by client_id asc";
				}
				else
				{
					for(int i=0;i<beans.size();i++){
						clientIds.append(beans.get(i).get("client_id").toString()).append(",");
					}
					clientIds.deleteCharAt(clientIds.length()-1);
					sql = "select sp.client_id,sp.dep_id from LF_ClIENT_DEP_SP sp "+StaticValue.getWITHNOLOCK()+" where sp.client_id in ("+clientIds+") order by client_id asc";
				}
				List<DynaBean> clientIdAndDepIdList = getListDynaBeanBySql(sql);
				//机构名称map  key:机构id  value:机构名称
				LinkedHashMap<String, String> depIdMap = new LinkedHashMap<String, String>();
				if(clientIdAndDepIdList!=null && clientIdAndDepIdList.size()>0)
				{
					String depNameSql = "select dep_id,dep_name from lf_client_dep "+StaticValue.getWITHNOLOCK()+" where corp_code='"+corpCode+"'";
					
					List<DynaBean> depNameList = getListDynaBeanBySql(depNameSql);
					if(depNameList==null ||depNameList.size()<=0)
					{
						EmpExecutionContext.error("客户通讯录查询时查询机构异常！");
						return new ArrayList<DynaBean>();  
					}
					for(DynaBean dep:depNameList)
					{
						depIdMap.put(dep.get("dep_id").toString(), dep.get("dep_name").toString());
					}
					//客户机构名称map key:客户id  value:机构名称
					LinkedHashMap<String, String> depNameMap = new LinkedHashMap<String, String>();
					String clientId = clientIdAndDepIdList.get(0).get("client_id").toString();
					StringBuffer depNames = new StringBuffer();
					//循环出放入查出的客户的对应机构名称放入map
					for(int i=0; i< clientIdAndDepIdList.size();i++){
						if(!clientId.equals(clientIdAndDepIdList.get(i).get("client_id").toString()))
						{
							depNames.deleteCharAt(depNames.length()-1);
							depNameMap.put(clientId, depNames.toString());
							clientId = clientIdAndDepIdList.get(i).get("client_id").toString();
							depNames.setLength(0);
							depNames.append(depIdMap.get(clientIdAndDepIdList.get(i).get("dep_id").toString())).append(",");
						}
						else
						{
							depNames.append(depIdMap.get(clientIdAndDepIdList.get(i).get("dep_id").toString())).append(",");
						}
						
					}
					//上面的循环会漏掉最后一个客户的机构映射关系放入map,所以这里补上
					depNames.deleteCharAt(depNames.length()-1);
					depNameMap.put(clientId, depNames.toString());
					//循环给结果集设机构名称
					for(DynaBean client:beans)
					{
						client.set("dep_name", depNameMap.get(client.get("client_id").toString()));
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"查询客户机构名称出现异常！");
		}
		return beans;
	}
	
	/**
	 * 设置客户机构名称及编码
	 * @description    
	 * @param beans
	 * @param corpCode
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-22 下午01:53:28
	 */
	private List<DynaBean> setClientDepInfo(List<DynaBean> beans,String corpCode){
		try{
			StringBuffer clientIds = new StringBuffer();;
			if(beans != null && beans.size()>0){
				String sql = "";
				if(beans.size()>=1000)
				{
					sql = "select sp.client_id,sp.dep_id from LF_ClIENT_DEP_SP sp "+StaticValue.getWITHNOLOCK()+" where sp.client_id in (select client_id from lf_client "+StaticValue.getWITHNOLOCK() +" where corp_code="+corpCode+") order by client_id asc";
				}
				else
				{
					for(int i=0;i<beans.size();i++){
						clientIds.append(beans.get(i).get("client_id").toString()).append(",");
					}
					clientIds.deleteCharAt(clientIds.length()-1);
					sql = "select sp.client_id,sp.dep_id from LF_ClIENT_DEP_SP sp "+StaticValue.getWITHNOLOCK()+" where sp.client_id in ("+clientIds+") order by client_id asc";
				}
				List<DynaBean> clientIdAndDepIdList = getListDynaBeanBySql(sql);
				//机构名称map  key:机构id  value:机构名称
				LinkedHashMap<String, String> depIdMap = new LinkedHashMap<String, String>();
				if(clientIdAndDepIdList!=null && clientIdAndDepIdList.size()>0)
				{
					String depNameSql = "select dep_id,dep_name from lf_client_dep "+StaticValue.getWITHNOLOCK()+" where corp_code='"+corpCode+"'";
					
					List<DynaBean> depNameList = getListDynaBeanBySql(depNameSql);
					if(depNameList==null ||depNameList.size()<=0)
					{
						EmpExecutionContext.error("客户通讯录查询时查询机构异常！");
						return new ArrayList<DynaBean>();  
					}
					for(DynaBean dep:depNameList)
					{
						depIdMap.put(dep.get("dep_id").toString(), dep.get("dep_name").toString());
					}
					//客户机构名称map key:客户id  value:0-机构名称;1-机构编码
					LinkedHashMap<String, String[]> depInfoMap = new LinkedHashMap<String, String[]>();
					//0-机构名称;1-机构编码
					String[] depInfo = new String[]{"",""};
					String clientId = clientIdAndDepIdList.get(0).get("client_id").toString();
					//循环出放入查出的客户的对应机构名称放入map
					for(int i=0; i< clientIdAndDepIdList.size();i++){
						if(!clientId.equals(clientIdAndDepIdList.get(i).get("client_id").toString()))
						{
							depInfo[0] = depInfo[0].substring(0, depInfo[0].length()-1);
							depInfo[1] = depInfo[1].substring(0, depInfo[1].length()-1);
							depInfoMap.put(clientId, depInfo);
							clientId = clientIdAndDepIdList.get(i).get("client_id").toString();
							depInfo = new String[]{"",""};
							depInfo[0] += depIdMap.get(clientIdAndDepIdList.get(i).get("dep_id").toString()) + ",";
							depInfo[1] += clientIdAndDepIdList.get(i).get("dep_id").toString() + ",";
						}
						else
						{
							depInfo[0] += depIdMap.get(clientIdAndDepIdList.get(i).get("dep_id").toString()) + ",";
							depInfo[1] += clientIdAndDepIdList.get(i).get("dep_id").toString() + ",";
						}
						
					}
					//上面的循环会漏掉最后一个客户的机构映射关系放入map,所以这里补上
					depInfo[0] = depInfo[0].substring(0, depInfo[0].length()-1);
					depInfo[1] = depInfo[1].substring(0, depInfo[1].length()-1);
					depInfoMap.put(clientId, depInfo);
					//循环给结果集设机构名称
					for(DynaBean client:beans)
					{
						client.set("dep_name", depInfoMap.get(client.get("client_id").toString())[0]);
						client.set("dep_id", depInfoMap.get(client.get("client_id").toString())[1]);
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"查询客户机构名称出现异常！");
		}
		return beans;
	}
	
	/**
	 *  获取当前操作员所在权限范围内  查询出的客户所对应的机构名称
	 * @param clientid	客户ID
	 * @param depidconn	当前操作员的客户机构权限
	 * @return
	 */
	private  String setClientDepName(Long clientid,String depidconn){
		String returnmsg = "";
		try{
			StringBuffer buffer = new StringBuffer();
			buffer.append(" select dep.dep_name from LF_CLIENT_DEP dep where dep.dep_id in " );
			buffer.append(" ( select sp.dep_id from LF_ClIENT_DEP_SP sp where sp.client_id = ").append(clientid);
			buffer.append(" and sp.dep_id in (").append(depidconn).append(" ) ) order by dep.dep_id asc ");
			List<DynaBean> deptnams = getListDynaBeanBySql(buffer.toString());
			StringBuilder builder = new StringBuilder();
			if(deptnams!=null && deptnams.size()>0){
				for(DynaBean aa :deptnams){
					builder.append(aa.get("dep_name")+",");
				}
				returnmsg = builder.substring(0, builder.length()-1);
			}
		}catch (Exception e) {
			returnmsg = "";
			EmpExecutionContext.error(e,"获取当前操作员所在权限范围内  查询出的客户所对应的机构名称出现异常！");
		}
		return returnmsg;
	}
	
	/**
	 *  页面查询所或者的机构
	 * @param lfClientVo
	 * @return
	 */
	public String getConditionSql(LfClientVo lfClientVo)
	{
		StringBuilder sb = new StringBuilder();
		if(lfClientVo.getName()!=null && !"".equals(lfClientVo.getName())){
			sb.append("and a.name like '%" +lfClientVo.getName()+ "%' ");
		}
		if(lfClientVo.getMobile()!=null && !"".equals(lfClientVo.getMobile())){
			sb.append("and a.mobile like '%" +lfClientVo.getMobile()+ "%' ");
		}
		if(lfClientVo.getIsContract()!=null){
			sb.append("and a.iscontract = " +lfClientVo.getIsContract()+ " ");
		}
		return sb.toString();
	}
	
	/**
	 *  页面查询所或者的机构
	 * @param lfClientVo
	 * @return
	 */
	public String getAPPConditionSql(LfClientVo lfClientVo,LinkedHashMap<String,String> conditionMap)
	{
		StringBuilder sb = new StringBuilder();
		if(lfClientVo.getName()!=null && !"".equals(lfClientVo.getName())){
			sb.append("and a.name like '%" +lfClientVo.getName()+ "%' ");
		}
		if(lfClientVo.getMobile()!=null && !"".equals(lfClientVo.getMobile())){
			sb.append("and a.mobile like '%" +lfClientVo.getMobile()+ "%' ");
		}
		if(lfClientVo.getIsContract()!=null){
			sb.append("and a.iscontract = " +lfClientVo.getIsContract()+ " ");
		}
		sb.append(")  tmp left join LF_APP_MW_CLIENT CL on CL.GUID = tmp.GUID   ");
		//是否需要增加 [and]
		boolean hasCondtion=false;
		
		String appCount=conditionMap.get("appacount");
		if(appCount!=null && !"".equals(appCount)){
			hasCondtion=true;
			if("未注册".equals(appCount.trim())){
				sb.append(" where  CL.APP_CODE is null ");
			}else {
				sb.append(" where  CL.APP_CODE like '" +appCount+ "%' ");
			}
		}
		
		String appStatue=conditionMap.get("appstatue");
		if(appStatue!=null && !"".equals(appStatue)){
			if("1".equals(appStatue)){
				if(hasCondtion){
					sb.append(" and  CL.GUID  is null  ");	
				}else{
					sb.append(" where CL.GUID  is null  ");
				}
				
			}
			if("0".equals(appStatue)){
				if(hasCondtion){
					sb.append(" and CL.GUID  is not null  ");	
				}else {
					sb.append(" where CL.GUID  is not null  ");
				}

			}
		}
		
		
		return sb.toString();
	}
	
	public void saveDepClient(Long clientid,String depid){
		String sql = "INSERT into LF_ClIENT_DEP_SP (CLIENT_ID,DEP_ID) values("+clientid+","+depid+")";
		try {
			executeBySQL(sql, StaticValue.EMP_POOLNAME);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "新增客户机构关联表失败");
		}
	}
	
	
	/**
	 *   通过当前操作员的权限去查询其客户的所在机构
	 * @param id 客户ID	
	 * @return
	 */
	public String[] getDepNameByClient(String clientid,Long lguserid){
		String[] re = new String[3];
		try{
			String depidconn = this.getLoginClientConnStrByUserId(lguserid);
			StringBuffer buffer = new StringBuffer();
			buffer.append(" select dep.dep_name,dep.dep_id from LF_CLIENT_DEP dep where dep.dep_id in " );
			buffer.append(" (select sp.dep_id from LF_ClIENT_DEP_SP sp where sp.client_id = ").append(clientid);
			buffer.append(" and sp.dep_id in (").append(depidconn).append(" )) order by dep.dep_id asc ");
			List<DynaBean> deptnams = getListDynaBeanBySql(buffer.toString());
			if(deptnams!=null && deptnams.size()>0){
				StringBuilder sb = new StringBuilder();
				StringBuilder sb1 = new StringBuilder();
				StringBuilder sb2 = new StringBuilder("#^@");
				for(DynaBean aa :deptnams){
					sb.append(aa.get("dep_name")+",");
					sb1.append(aa.get("dep_id")+",");
					sb2.append(";"+aa.get("dep_id")).append(";").append(aa.get("dep_name")).append("#^@");
				}
				re[0] = sb.substring(0, sb.length()-1);
				re[1] = sb1.substring(0, sb1.length()-1);
				re[2] = sb2.toString();
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"通过当前操作员的权限去查询其客户的所在机构出现异常！");
		}
		return re;
	}
	
	public void cleanClentiddep(String clientid){
		String sql = "delete LF_ClIENT_DEP_SP where CLIENT_ID = "+clientid;
		try {
			executeBySQL(sql, StaticValue.EMP_POOLNAME);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除客户机构关联表失败！");
		}
	}
	
	public Map<String,Set<String>> findClientPhoneNameSetcd(String depids){
		Map<String,Set<String>> map = new HashMap<String, Set<String>>();
		if(!GlobalMethods.isInvalidString(depids)){
			depids = depids.substring(0, depids.length()-1);
		}
		Set<String> set = new HashSet<String>();
		Set<String> setnun = new HashSet<String>();
		String sql = "SELECT a.name,a.mobile from LF_CLIENT a where a.CLIENT_ID in " +
				"      (SELECT client_id from LF_ClIENT_DEP_SP where DEP_ID in ("+depids+"))";
		List<DynaBean> deptnams = getListDynaBeanBySql(sql);
		if(deptnams!=null && deptnams.size()>0){
			for(DynaBean aa :deptnams){
				set.add(aa.get("name").toString()+aa.get("mobile"));
				setnun.add(aa.get("mobile").toString());
			}
			map.put("namephone", set);
			map.put("phone", setnun);
		}
		return map;
	}
	
	
	
	
	/**
	 * 获取客户子机构id字符串，包含父机构id
	 * @param depId 父机构机构id
	 * @param refName 字段名
	 * @return 非sqlserver数据库格式为：refName in (id,id,id) or refName in (id,id,id)，sqlserver返回refName in (select DepID from GetCliDepChildByPID(1,depId)
	 * @throws Exception
	 */
	public String getClientChildByParentID(Long depId, String refName) throws Exception{
		
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
		{
			//oracle
			return this.getChildByParentIDOracle(depId, refName);
		}
		else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
		{
			//sqlserver
			String sql = new StringBuffer(refName)
						.append(" in ( ").append("select DepID from GetCliDepChildByPID(1,")
						.append(depId).append(") ) ").toString();//获取子机构，包含自己
			//返回sql
			return sql;
		}
		else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//mysql
			return this.getCliChildIdForMysql(depId.toString(), refName);
		}
		else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//db2
			return this.getCliChildIdForMysql(depId.toString(), refName);
		}
		//返回空
		return null;
	}
	
	public String getClientChildByDepPath(String depPath, String refName) throws Exception{
		//获取子机构，包含自己
		String sql = new StringBuffer(refName)
						.append(" in ( ").append("select ").append(TableLfClientDep.DEP_ID)
						.append(" from ").append(TableLfClientDep.TABLE_NAME).append(" where ")
						.append(TableLfClientDep.DEP_PATH).append(" like '").append(depPath)
						.append("%')").toString();
		//返回sql
		return sql;
	}
	
	
	/**
	 * 根据depId获取子机构Id，包含自己（SqlServer）（暂不用）
	 * @param depId 机构Id
	 * @return 机构Id，格式为id1,id2,id3
	 * @throws Exception
	 */
	public String getChildByParentIDSqlServer(Long depId) throws Exception{
		//查询sql
		String sql = new StringBuffer("select * from GetCliDepChildByPID(1,").append(depId).append(")").toString();
		//数据库连接
		Connection conn = null;
		PreparedStatement ps = null;
		//结果集
		ResultSet rs = null;
		StringBuffer deps = new StringBuffer();
		String depIds = "";
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			//预处理sql
			ps = conn.prepareStatement(sql);
			//执行sql并返回结果集
			rs = ps.executeQuery();
			//循环处理结果集
			while (rs.next()) {
				deps.append(rs.getLong("DepID")).append(",");
			}
			depIds = deps.substring(0,deps.lastIndexOf(",")).toString();
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取子机构出现异常！");
			throw e;
		} finally
		{
			//关闭连接等
			close(rs, ps, conn);
		}
		//返回结果
		return depIds;
	}
	
	/**
	 * 根据depId获取子机构Id，包含自己（Oracle）
	 * @param depId 机构Id
	 * @return 机构Id，格式为id1,id2,id3
	 * @throws Exception
	 */
	public String getChildByParentIDOracle(Long depId, String refName) throws Exception {
		//数据库连接
		Connection conn = null;
		//结果集
		ResultSet rs = null;
		String depIds = "";
		CallableStatement proc = null;
		
		try 
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//调用存储过程
			proc = conn.prepareCall("{ call GETCLIDEPCHILDBYPID(?,?,?,?) }");
			//设置参数
			proc.setInt(1, 1);
			proc.setLong(2, depId);
			proc.setString(3,StringUtils.getRandom());
			//获取返回
			proc.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
			//执行
			proc.execute();
			//返回结果集
			rs = (ResultSet) proc.getObject(4);
			//处理结果集
			depIds = parseRS(rs, refName);
		} 
		catch (Exception e) 
		{
			//异常处理
			EmpExecutionContext.error(e,"获取子机构出现异常！");
			throw e;
		} 
		finally 
		{
			try 
			{
				close(rs, proc, conn);
			} 
			catch (SQLException e) 
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		//返回结果
		return depIds;
	}
	
	/**
	 * 根据depId获取子机构Id，包含自己（mysql）（暂不用）
	 * @param depId 机构Id
	 * @return 机构Id，格式为id1,id2,id3
	 * @throws Exception
	 */
	public String getChildByParentIDMySql(Long depId, String refName) throws Exception{
		//调用存储过程sql
		String sql  = "call GETCLIDEPCHILDBYPID(?,?,?)";
		//获取连接
		Connection conn = null;
		//结果集
		ResultSet rs = null;
		String depIds = "";
		CallableStatement comm = null;
		
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			comm = ((java.sql.Connection) conn).prepareCall(sql);
			//设置参数
			comm.setInt(1,1);
			comm.setLong(2,depId);
			comm.setString(3,StringUtils.getRandom());
			//执行
			comm.execute();
			//获取结果集
			rs = comm.getResultSet();
			//处理结果集
			depIds = parseRS(rs, refName);
			
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取子机构出现异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, comm, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.debug("关闭数据库资源出错！");
			}
		}
		//返回结果
		return depIds;
	}
	
	/**
	 * 获取子机构id（包含自己）（mysql专用）
	 * @param depId 父机构id
	 * @return 返回格式为:id,id,id
	 * @throws Exception
	 */
	public String getCliChildIdForMysql(String depId, String refName) throws Exception
	{
		StringBuffer depIds = new StringBuffer();
		String conditionDepid = depId;
		//查询sql
		String sql = "";
		//数据库连接
		Connection conn = null;
		PreparedStatement ps = null;
		//结果集
		ResultSet rs = null;
		//记录数
		int incount = 0;
		int n = 1;
		
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//结果
			boolean hasNext = true;
			//加入父id
			depIds.append(refName).append(" in ( ").append(depId);
			//循环处理
			while(hasNext)
			{
				//查询sql
				sql = "select dep_Id from lf_client_dep where PARENT_ID in (" + conditionDepid + ")";
				//预处理sql
				ps = conn.prepareStatement(sql);
//				ps.setString(1, conditionDepid);
				//执行并返回结果集
				rs = ps.executeQuery();
				conditionDepid = "";
				//循环处理结果集
				while(rs.next())
				{
					//记录数量
					incount ++;

					//if(incount > StaticValue.inConditionMax*n)
					if(incount > StaticValue.getInConditionMax()*n)
					{
						n++;
						depIds.append(") or ").append(refName).append(" in (")
								.append(rs.getString("dep_id"));
						
						conditionDepid = conditionDepid.substring(0,conditionDepid.length()-1);
						conditionDepid += ") or PARENT_ID in (";
					}
					else
					{
						depIds.append(",").append(rs.getString("dep_id"));
					}

					conditionDepid += rs.getString("dep_id") + ",";
				}
				if(conditionDepid.length() == 0)
				{
					hasNext = false;
				}else
				{
					hasNext = true;
					conditionDepid = conditionDepid.substring(0,conditionDepid.length()-1);
				}
			}
			depIds.append(" ) ");
			
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取子机构出现异常！");
			throw e;
			
		} finally
		{

			close(rs, ps, conn);
		}
		//返回结果
		return depIds.toString();	
	}
	
	/**
	 * 解析结果集
	 * @param rs
	 * @param refName
	 * @return
	 * @throws Exception
	 */
	private String parseRS(ResultSet rs, String refName) throws Exception 
	{
		StringBuffer deps = new StringBuffer();
		//记录计数
		int i = 1;
		int cou=1;
		//结果集有值
		if(rs.next())
		{
			deps.append(refName).append(" in ( ").append(rs.getLong("DepID"));
		}
		else
		{
			return "";
		}
		
		while (rs.next()) {
			i++;
			//if(i > cou*StaticValue.inConditionMax)
			if(i > cou*StaticValue.getInConditionMax())
			{
				//记录数操作数量，则拆分为or xx in(id,id,id...)格式
				cou++;
				deps.append(") or ").append(refName).append(" in (").append(rs.getLong("DepID"));
			}
			else
			{
				deps.append(",").append(rs.getLong("DepID"));
			}
		}
		
		deps.append(" ) ");
		//返回结果
		return deps.toString();
	}
	
	//删除时客户部门时--找到所有机构客户
	public List<LfClientVo> findAllClientVo(Long loginUserID,String corpcode,
			LfClientVo lfClientVo) throws Exception
	{

		String depConnSql = new StringBuffer(" select ").append(
				TableLfCliDepConn.DEP_ID).append(" from ").append(
				TableLfCliDepConn.TABLE_NAME).append(" where ").append(
				TableLfCliDepConn.USER_ID).append(" = ").append(loginUserID).toString();
		//找出操作员绑定客户机构记录
		List<LfCliDepConn> codeDepConnsList = findPartEntityListBySQL(LfCliDepConn.class, depConnSql, StaticValue.EMP_POOLNAME);
		if (codeDepConnsList == null || codeDepConnsList.size() == 0)
		{
			return new ArrayList<LfClientVo>();
		}

		Long  depConnId= codeDepConnsList.get(0).getDepId();

		String sql = "";

			//适用ORACEL数据库的SQL语句
	   		 sql = new StringBuffer("select client.").append(
					TableLfClient.CLIENT_ID).append(",client.").append(
					TableLfClient.DEP_ID).append(",client.").append(
					TableLfClient.MOBILE).append(",client.").append(
					TableLfClient.NAME).append(",client.")
					.append(TableLfClient.MSN).append(",client.").append(
							TableLfClient.QQ).append(",client.").append(
							TableLfClient.SEX).append(",client.").append(
							TableLfClient.BIRTHDAY).append(",client.").append(
							TableLfClient.OPH).append(",client.").append(
							TableLfClient.E_MAIL).append(",client.").append(
							TableLfClient.DEP_CODE).append(",client.").append(
							TableLfClient.PHONE).append(",client.").append(
							TableLfClient.BATCH_NO).append(",client.").append(
							TableLfClient.GUID).append(",client.").append(
							TableLfClient.CLIENT_CODE).toString();
	   	//分页sql
		String countSql = "select count(*) totalcount";
		//查询表格sql
		String tableSql = new StringBuffer(" from ").append(
				TableLfClient.TABLE_NAME).append(" client ").append(
				" where ").append(TableLfClient.CORP_CODE).append("='").append(corpcode).append("'").toString();
		//拼接sql
		sql += tableSql;
		//拼接sql
		countSql += tableSql;
		//条件sql
		StringBuffer conditionSql = new StringBuffer();
		//机构id
		if (null != lfClientVo.getDepId())
		{
			conditionSql.append(" and client.").append(TableLfClient.DEP_ID).append("=").append(lfClientVo.getDepId());
		} else
		{
			conditionSql.append(" and client.").append(TableLfClient.DEP_ID).append("=").append(depConnId);
		}
		//姓名
		if (null != lfClientVo.getName() && !"".equals(lfClientVo.getName()))
		{
			conditionSql.append(" and client.").append(TableLfClient.NAME)
					.append(" like '%").append(lfClientVo.getName()).append(
							"%'");
		}
		//手机号
		if (null != lfClientVo.getMobile() && !"".equals(lfClientVo.getMobile()))
		{
			conditionSql.append(" and client.").append(TableLfClient.MOBILE)
					.append(" like '%").append(lfClientVo.getMobile()).append("%'");
		}
		//群组id
		if (null != lfClientVo.getUdgId())
		{
			String guidSql = new StringBuffer("select ").append(
					TableLfList2gro.GUID).append(" from ").append(
					TableLfList2gro.TABLE_NAME).append(" where ").append(
					TableLfList2gro.UDG_ID).append("=").append(
					lfClientVo.getUdgId()).toString();
			conditionSql.append(" and client.").append(TableLfEmployee.GUID)
					.append(" not in (").append(guidSql).append(")");
		}
		//拼接条件sql
		sql += conditionSql.toString();
		//拼接条件sql
		countSql += conditionSql.toString();
		//执行查询
		List<LfClientVo> returnList = findAllVoListBySQL(
						LfClientVo.class, sql, countSql,
						StaticValue.EMP_POOLNAME,null,null);
		//处理结果
		returnList = completeLfClientVo(loginUserID,corpcode, returnList);
		//返回
		return returnList;
	}
	
	public List<LfClient> findLfClient(String depCode,String corpCode) throws Exception
	{
		//查询sql
		StringBuffer sql = new StringBuffer("select ").append(
				TableLfClient.MOBILE).append(" from ").append(
				TableLfClient.TABLE_NAME).append(" where  ")
				.append(TableLfClient.CORP_CODE).append("='").append(corpCode.trim()).append("' ");
		//机构编码
		if (null != depCode && !"".equals(depCode))
		{
			sql.append(" and ").append(TableLfClient.DEP_CODE)
					.append(" like '").append(EmpUtils.getPartDepCode(depCode))
					.append("%'");
		}
		//执行查询
		List<LfClient> returnList = findPartEntityListBySQL(LfClient.class, sql
				.toString(), StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
	}

	private List<LfClientVo> completeLfClientVo(Long loginUserID,String corpcode,
			List<LfClientVo> lfClientVoList) throws Exception
	{
		//数据库连接
		Connection conn = null;
		// List<LfClientVo> resultVoList = new ArrayList<LfClientVo>();
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//查询sql
			String sql = new StringBuffer("select lfclientdep.").append(
					TableLfClientDep.DEP_NAME).append(", lfclientdep.").append(
					TableLfClientDep.DEP_ID)
					.append(" from ").append(
					TableLfClientDep.TABLE_NAME).append(" lfclientdep ")
					.append(" where ").append(TableLfClientDep.CORP_CODE).append("='").append(corpcode)
					.append("'").toString();
			//执行查询
			List<LfClientDep> lfCliDepsList = findPartEntityListBySQL(conn,
					LfClientDep.class, sql, StaticValue.EMP_POOLNAME);
			//没记录
			if (lfCliDepsList == null || lfCliDepsList.size() == 0)
			{
				//返回初始化
				return new ArrayList<LfClientVo>();
			}
			//循环处理记录
			for (int i = 0; i < lfClientVoList.size(); i++)
			{
				for (int j = 0; j < lfCliDepsList.size(); j++)
				{
					if (lfClientVoList.get(i).getDepId()-lfCliDepsList.get(j).getDepId()==0)
					{
						lfClientVoList.get(i).setDepName(lfCliDepsList.get(j).getDepName());
					}
				}
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"查询客户机构记录出现异常！");
			throw e;
		} finally
		{
			if (conn != null)
			{
				conn.close();
			}
		}
		//返回结果
		return lfClientVoList;
	}
	
	
	public boolean filtClientAdmin(long conn_id,String corpCode)
	throws Exception {
		//查询字段
		String fieldSql = new StringBuffer("select  ").append("*").toString();
		//查询表格
		String tableSql = new StringBuffer(" from ")
		.append(TableLfCliDepConn.TABLE_NAME).toString();
		//条件sql
		String whereSql ="";
		whereSql =  new StringBuffer(" ").append(" where ").append(TableLfCliDepConn.CONN_ID).append(" = ").append(conn_id).toString();
		//查询sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(whereSql).toString();	
		//执行查询
		List<LfEmpDepConnVo> returnVoList = findAllVoTypeListBySQL(LfEmpDepConnVo.class, sql
				.toString(), StaticValue.EMP_POOLNAME,null,null);
		List<LfSysuser2Vo> returnVoList2 = new ArrayList<LfSysuser2Vo>();
		String sqlDep = "select * from "+TableLfClientDep.TABLE_NAME +" where "+TableLfClientDep.DEP_ID+"="+returnVoList.get(0).getDep_id();

		List<LfClientDep> clientDep = findEntityListBySQL(LfClientDep.class, sqlDep, StaticValue.EMP_POOLNAME);
		if(clientDep.get(0).getParentId()==0&& StaticValue.getCORPTYPE() ==1)
		{
			//多企业版
			long uid = (long)(returnVoList.get(0).getUser_id());
			String fieldSql2 = new StringBuffer("select  ").append(" * ").toString();
			String tableSql2 = new StringBuffer(" from ")
			.append(TableLfSysuser.TABLE_NAME).toString();
//			String whereSql2 ="";
//				whereSql2 =  new StringBuffer(" ").append(" where ").append(TableLfSysuser.HOLDER).append(" = 'sysadmin'  and ").append(TableLfSysuser.USER_ID)
//				.append(" = ").append(uid).append(" and ").append(TableLfSysuser.CORP_CODE).append("='").append(corpCode+"'").toString();
//				toString();
			String whereSql2 ="";
			whereSql2 =  new StringBuffer(" ").append(" where ").append(TableLfSysuser.USER_NAME).append(" = 'admin'  and ").append(TableLfSysuser.USER_ID)
			.append(" = ").append(uid).append(" and ").append(TableLfSysuser.CORP_CODE).append("='").append(corpCode+"'").toString();
			toString();
			String sql2 = new StringBuffer(fieldSql2).append(tableSql2).append(whereSql2).toString();	
			returnVoList2 = findAllVoTypeListBySQL(LfSysuser2Vo.class, sql2
					.toString(), StaticValue.EMP_POOLNAME,null,null);
		}
		else if(clientDep.get(0).getParentId()==0&& StaticValue.getCORPTYPE() ==0){
			//单企业版
			long uid = (long)(returnVoList.get(0).getUser_id());
			String fieldSql2 = new StringBuffer("select  ").append(" * ").toString();
			String tableSql2 = new StringBuffer(" from ")
			.append(TableLfSysuser.TABLE_NAME).toString();
			String whereSql2 ="";
				whereSql2 =  new StringBuffer(" ").append(" where ").append(TableLfSysuser.USER_NAME).append(" = 'admin'  and ").append(TableLfSysuser.USER_ID)
				.append(" = ").append(uid).append(" and ").append(TableLfSysuser.CORP_CODE).append("='").append(corpCode+"'").toString();
				toString();
			String sql2 = new StringBuffer(fieldSql2).append(tableSql2).append(whereSql2).toString();	
			returnVoList2 = findAllVoTypeListBySQL(LfSysuser2Vo.class, sql2
					.toString(), StaticValue.EMP_POOLNAME,null,null);
		}
		//返回结果
		if(returnVoList2.size()>0)
			return false;
		else 
			return true;
		
	}
	
	/**
	 * 按条件获取客户通讯录手机号码和姓名
	 * @param depId 机构id
	 * @param corpCode 机构编码
	 * @return 返回map，key为手机号+姓名，value为姓名
	 * @throws Exception
	 */
	public Map<String,String> findClientPhoneNamesMap(String depId, String corpCode) throws Exception
	{
		//返回结果，key为手机号+姓名，value为姓名
		Map<String,String> returnList = new HashMap<String,String>();
		//数据库连接
		Connection conn = null;
		//预处理
		PreparedStatement ps = null;
		//结果集
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//sql语句
			String sql = new StringBuffer("select ").append(TableLfClient.MOBILE).append(",").append(TableLfClient.NAME)
						.append(" from ").append(TableLfClient.TABLE_NAME)
						.append(" where ").append(TableLfClient.DEP_ID).append(" = ").append(depId)
						.append(" and ").append(TableLfClient.CORP_CODE).append(" = ").append(corpCode)
						.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			//预处理sql
			ps = conn.prepareStatement(sql);
			//执行查询语句
			rs = ps.executeQuery();
			//循环处理结果集
			while(rs.next())
			{
				returnList.put(rs.getString(1)+rs.getString(2), rs.getString(1));
				
			}
		} catch (Exception ex)
		{
			//异常处理
			EmpExecutionContext.error(ex,"执行SQL语句异常！");
			throw ex;
		} finally
		{
			//关闭数据库资源
			close(rs, ps, conn);
		}
		//返回结果
		return returnList;
	}
	public Set<String> findClientPhoneNamesSet(String depId, String corpCode) throws Exception
	{
		//返回结果，key为手机号+姓名，value为姓名
		Set<String> returnList = new HashSet<String>();
		//数据库连接
		Connection conn = null;
		//预处理
		PreparedStatement ps = null;
		//结果集
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//sql语句
			String sql = new StringBuffer("select ").append(TableLfClient.MOBILE).append(",").append(TableLfClient.NAME)
						.append(" from ").append(TableLfClient.TABLE_NAME)
						.append(" where ").append(TableLfClient.DEP_ID).append(" = ").append(depId)
						.append(" and ").append(TableLfClient.CORP_CODE).append(" = '").append(corpCode).append("'")
						.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			//预处理sql
			ps = conn.prepareStatement(sql);
			//执行查询语句
			rs = ps.executeQuery();
			//循环处理结果集
			while(rs.next())
			{
				returnList.add(rs.getString(1)+rs.getString(2));
				
			}
		} catch (Exception ex)
		{
			//异常处理
			EmpExecutionContext.error(ex,"执行SQL语句异常！");
			throw ex;
		} finally
		{
			//关闭数据库资源
			close(rs, ps, conn);
		}
		//返回结果
		return returnList;
	}
	
	public Set<Long> findClientPhoneSet(String depId, String corpCode) throws Exception
	{
		//返回结果，key为手机号+姓名，value为姓名
		Set<Long> returnList = new HashSet<Long>();
		//数据库连接
		Connection conn = null;
		//预处理
		PreparedStatement ps = null;
		//结果集
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//sql语句
			String sql = new StringBuffer("select ").append(TableLfClient.MOBILE)
						.append(" from ").append(TableLfClient.TABLE_NAME)
						.append(" where ").append(TableLfClient.DEP_ID).append(" = ").append(depId)
						.append(" and ").append(TableLfClient.CORP_CODE).append(" = '").append(corpCode).append("'")
						.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			//预处理sql
			ps = conn.prepareStatement(sql);
			//执行查询语句
			rs = ps.executeQuery();
			//循环处理结果集
			while(rs.next())
			{
				returnList.add(rs.getLong(1));
				
			}
		} catch (Exception ex)
		{
			//异常处理
			EmpExecutionContext.error(ex,"执行SQL语句异常！");
			throw ex;
		} finally
		{
			//关闭数据库资源
			close(rs, ps, conn);
		}
		//返回结果
		return returnList;
	}
	/**
	 * 按条件获取客户属性值
	 * @param id 客户属性id
	 * @return 返回map，key为属性值，value为属性值id
	 * @throws Exception
	 */
	public Map<String,String> findCustFieldValueList(Long id) throws Exception
	{
		//返回结果，key为属性值，value为属性id
		Map<String,String> returnList = new HashMap<String,String>();
		//数据库连接
		Connection conn = null;
		//预处理
		PreparedStatement ps = null;
		//结果集
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//sql语句
			String sql = new StringBuffer("select ").append(TableLfCustFieldValue.FIELD_VALUE).append(",").append(TableLfCustFieldValue.ID)
						.append(" from ").append(TableLfCustFieldValue.TABLE_NAME)
						.append(" where ").append(TableLfCustFieldValue.FIELD_ID).append(" = ").append(id)
						.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			//预处理sql
			ps = conn.prepareStatement(sql);
			//执行查询语句
			rs = ps.executeQuery();
			//循环处理结果集
			while(rs.next())
			{
				returnList.put(rs.getString(1), rs.getString(2));
				
			}
		} catch (Exception ex)
		{
			//异常处理
			EmpExecutionContext.error(ex,"执行SQL语句异常！");
			throw ex;
		} finally
		{
			//关闭数据库资源
			close(rs, ps, conn);
		}
		//返回结果
		return returnList;
	}
	
	/**
	 *   获取该企业下面客户的信息
	 * @param corpCode	企业编码
	 * @param namephoneSet	名字+手机号码的string
	 * @return  key name+phone value clientid
	 * @throws Exception
	 */
	public LinkedHashMap<String,String> findClientPhoneNameSet(String corpCode,Set<String> namephoneSet)throws Exception{
		LinkedHashMap<String,String> keyMap = new LinkedHashMap<String, String>();
		String sql = "SELECT a.name,a.mobile,a.client_id from LF_CLIENT a where a.CORP_CODE = '"+corpCode+"'"; 
		List<DynaBean> deptnams = getListDynaBeanBySql(sql);
		if(deptnams!=null && deptnams.size()>0){
			String name = "";
			String mobile = "";
			String id = "";
			for(DynaBean bean :deptnams){
				name = String.valueOf(bean.get("name"));
				mobile = String.valueOf(bean.get("mobile"));
				id = String.valueOf(bean.get("client_id"));
				namephoneSet.add(name+mobile+"");
				keyMap.put(name+mobile+"",id);
				name = "";
				mobile = "";
				id = "";
			}
		}
		return keyMap;
	}
	
	/**
	 *   获取存在的客户ID所对应的客户关联表中的机构ID
	 * @param existClientid
	 * @return
	 */
	public LinkedHashMap<Long,HashSet<Long>> getExistClientMsg(String existClientid){
		LinkedHashMap<Long,HashSet<Long>> depspMap = new LinkedHashMap<Long, HashSet<Long>>();
		existClientid =existClientid.substring(0, existClientid.length()-1);
		String sql = "SELECT CLIENT_ID,DEP_ID from LF_ClIENT_DEP_SP where CLIENT_ID in ("+existClientid+")";
		List<DynaBean> beans= getListDynaBeanBySql(sql);
		if(beans != null && beans.size()>0){
			for(DynaBean bean :beans){
				Long clientid =Long.parseLong(bean.get("client_id")+"");
				Long depid = Long.parseLong(bean.get("dep_id")+"");
				if(depspMap.containsKey(clientid)){
					HashSet<Long> depidtemp = depspMap.get(clientid);
					depidtemp.add(depid);
					depspMap.remove(clientid);
					depspMap.put(clientid, depidtemp);
				}else{
					HashSet<Long> depidSet = new HashSet<Long>();
					depidSet.add(depid);
					depspMap.put(clientid, depidSet);
				}
			}
		}

		return depspMap;
	}
	
	/**
     * 客户通讯录修改方法(大于5属性)
     */
	public void  updateClientMultiPro(Connection conn,List<LfClientMultiPro> updateList) throws Exception
	{
		Statement ps = null;
		try{ 
			ps=conn.createStatement();
			String sql = "update LF_CLIENT set ";
			StringBuffer sf = null;
			for(int i=0;i<updateList.size();i++){

				//在上传XLS中的客户记录
				LfClientMultiPro updateClient = updateList.get(i);
				if(updateClient == null){
					continue;
				}
				sf=new StringBuffer();
					sf.append(sql);
					if(isEffectValue(updateClient.getName()+"")){
						sf.append("Name ='"+updateClient.getName().replaceAll("'", "''")+"',");
					}
					if(isEffectValue(updateClient.getSex()+"")){
						sf.append("Sex ="+updateClient.getSex()+",");
					}
					if(isEffectValue(updateClient.getJob())){
						sf.append("Job ='"+updateClient.getJob()+"',") ;
					}
					if(isEffectValue(updateClient.getProfession())){
						sf.append(" Profession ='"+updateClient.getProfession()+"'," );
					}
					if(isEffectValue(updateClient.getEname())){
						sf.append(" Ename ='"+updateClient.getEname()+"'," );
					}
					if(isEffectValue(updateClient.getQq())){
						sf.append(" Qq ='"+updateClient.getQq()+"'," );
					}
					if(isEffectValue(updateClient.getArea())){
						sf.append(" Area ='"+updateClient.getArea()+"',") ;
					}
					if(isEffectValue(updateClient.getMsn())){
						sf.append(" Msn ='"+updateClient.getMsn()+"'," );
					}	

					if(isEffectValue(updateClient.getEMail())){
						sf.append(" E_Mail ='"+updateClient.getEMail()+"'," ) ;
					}
					if(isEffectValue(updateClient.getOph())){
						sf.append(" Oph ='"+updateClient.getOph()+"'," );
					}
					if(updateClient.getBirthday()!=null){
						sf.append(" Birthday ='"+updateClient.getBirthday()+"'," );
					}
					if(updateClient.getUserId()!=null){
						sf.append(" USER_ID ='"+updateClient.getUserId()+"'," );
					}
					
					String s = "";
					Class clazz = Class.forName("com.montnets.emp.entity.client.LfClientMultiPro");
					Method mf = null;
					for(int m=1;m<51;m++){
		               if(m<10){
		                   s = "Field0" + m;
		               }else {
		              	   s = "Field" + m;
		               }
		               mf = clazz.getDeclaredMethod("get" + s);
		               Object object = mf.invoke(updateClient);
		               if(object != null && !"".equals(object)){
		            	   sf.append(" "+s+" ='"+object.toString()+"'," );
		               }
		            }
					
					if(isEffectValue(updateClient.getComments())){
						sf.append(" Comments ='"+updateClient.getComments()+"' ") ;
					}else{
						sf.append(" Comments ='' ") ;
					}
					sf.append("  WHERE CLIENT_ID ="+updateClient.getClientId()+"");
				EmpExecutionContext.sql("execute sql : " + sf.toString());
				ps.addBatch(sf.toString());
				if(StaticValue.DBTYPE==StaticValue.DB2_DBTYPE&&i>0&&i%10000==0){
					ps.executeBatch();
					ps.clearBatch();
				}
			
		}
			//批量插入
			ps.executeBatch();

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"批量导入客户通讯录出现异常！");
			//回滚
			throw e;
		}finally {
			if(ps != null){
				ps.close();
			}
		}
	}
	
    /**
     * 客户通讯录修改方法(5属性)
     */
	public void  updateCleint5Pro(Connection conn,List<LfClient5Pro> updateList) throws Exception
	{
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		Statement ps = null;
		try{ 
			ps=conn.createStatement();
			String sql = "update LF_CLIENT set ";
			StringBuffer sf = null;
			for(int i=0;i<updateList.size();i++){

				//在上传XLS中的客户记录
				LfClient5Pro updateClient = updateList.get(i);
				if(updateClient == null){
					continue;
				}
				sf=new StringBuffer();
					sf.append(sql);
					if(isEffectValue(updateClient.getName()+"")){
						sf.append("Name ='"+updateClient.getName().replaceAll("'", "''")+"',");
					}
					if(isEffectValue(updateClient.getSex()+"")){
						sf.append("Sex ="+updateClient.getSex()+",");
					}
					if(isEffectValue(updateClient.getJob())){
						sf.append("Job ='"+updateClient.getJob()+"',") ;
					}
					if(isEffectValue(updateClient.getProfession())){
						sf.append(" Profession ='"+updateClient.getProfession()+"'," );
					}
					if(isEffectValue(updateClient.getEname())){
						sf.append(" Ename ='"+updateClient.getEname()+"'," );
					}
					if(isEffectValue(updateClient.getQq())){
						sf.append(" Qq ='"+updateClient.getQq()+"'," );
					}
					if(isEffectValue(updateClient.getArea())){
						sf.append(" Area ='"+updateClient.getArea()+"',") ;
					}
					if(isEffectValue(updateClient.getMsn())){
						sf.append(" Msn ='"+updateClient.getMsn()+"'," );
					}	

					if(isEffectValue(updateClient.getEMail())){
						sf.append(" E_Mail ='"+updateClient.getEMail()+"'," ) ;
					}
					if(isEffectValue(updateClient.getOph())){
						sf.append(" Oph ='"+updateClient.getOph()+"'," );
					}
					//日期类型处理
					if(updateClient.getBirthday()!=null){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						sf.append(" Birthday ="+genericDao.getTimeCondition(sdf.format(updateClient.getBirthday())+"")+"," );
						
						//sf.append(" Birthday ='"+updateClient.getBirthday()+"'," );
						
					}
					if(updateClient.getUserId()!=null){
						sf.append(" USER_ID ='"+updateClient.getUserId()+"'," );
					}
					
					String s = "";
					Class clazz = Class.forName("com.montnets.emp.entity.client.LfClient5Pro");
					Method mf = null;
					for(int m=1;m<6;m++){
	                   s = "Field0" + m;
		               mf = clazz.getDeclaredMethod("get" + s);
		               Object object = mf.invoke(updateClient);
		               if(object != null && !"".equals(object)){
		            	   sf.append(" "+s+" ='"+object.toString()+"'," );
		               }
		            }
					
					if(isEffectValue(updateClient.getComments())){
						sf.append(" Comments ='"+updateClient.getComments()+"' ") ;
					}else{
						sf.append(" Comments ='' ") ;
					}
					sf.append("  WHERE CLIENT_ID ="+updateClient.getClientId()+"");
				EmpExecutionContext.sql("execute sql : " + sf.toString());
				ps.addBatch(sf.toString());
				if(StaticValue.DBTYPE==StaticValue.DB2_DBTYPE&&i>0&&i%10000==0){
					ps.executeBatch();
					ps.clearBatch();
				}
			
		}
			//批量插入
			ps.executeBatch();

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"批量导入客户通讯录出现异常！");
			//回滚
			throw e;
		}finally {
			if(ps != null){
				ps.close();
			}
		}
	}
	public void deleteTable(Connection conn,List<LfClient5Pro> updateList,String[] depstrs )throws Exception{
		Statement st = null;
		try{ 
//			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
//			conn.setAutoCommit(false);
			st = conn.createStatement();
			for(int i=0;i<updateList.size();i++){

				//在上传XLS中的客户记录
				LfClient5Pro updateClient = updateList.get(i);
				if(updateClient == null){
					continue;
				}
				for(String dep:depstrs)
				{
					StringBuffer sf=new StringBuffer();
					sf.append("DELETE FROM LF_ClIENT_DEP_SP " );
					sf.append(" WHERE CLIENT_ID ="+updateClient.getClientId()+" AND dep_Id = "+dep);
					EmpExecutionContext.sql("execute sql : " + sf.toString());
					st.addBatch(sf.toString());
				}
			
		}
			//批量插入
			st.executeBatch();
//			conn.commit();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"批量导入客户通讯录出现异常！");
			//回滚
//			conn.rollback();
			throw e;
		} finally
		{
//			conn.setAutoCommit(true);
			//关闭数据库资源
			if(st != null){
				st.close();
			}
		}
	}
	
	
	
	
	/**
	 *  判断是否为空 
	 * @param temp	值
	 * @return
	 */
	public boolean isEffectValue(String temp){
		if(temp == null || "".equals(temp)){
			return false;
		}
		return true;
	}
	
	public Set<String> getDepID(){
		Set<String> depspSet = new HashSet<String>();
		String sql = "select CLIENT_ID,DEP_ID from LF_ClIENT_DEP_SP ";
		List<DynaBean> beanList= getListDynaBeanBySql(sql);
		if(beanList != null && beanList.size()>0){
			for(DynaBean bead : beanList)
			{
				depspSet.add(bead.get("dep_id").toString().trim() + "@" + bead.get("client_id").toString().trim());
			}
		}
		return depspSet;
	}
	
	
	public void  deleteLfList2gro(String l2gType,String dep_id,Connection conn)throws Exception{
		Statement st = null;
		String sql="delete from LF_LIST2GRO where l2g_type=1 and guId in " +
		"("+getSQL("guid", dep_id)+")";
		try
		{
			st=conn.createStatement();
			st.execute(sql);
		}
		catch (SQLException e)
		{
			EmpExecutionContext.error(e,"删除群组信息失败！");
			//回滚
//			conn.rollback();
			throw e;
		}finally{
			if(st != null){
				st.close();
			}
		}

	}
	
	//删除客户机构下，客户的生日祝福
	public void  deleteLfBirthdayMember(String dep_id,Connection conn)throws Exception{

		Statement st = null;
		String sql="delete from LF_BIRTHDAYMEMBER where type=2 and member_type = 1 and member_id in ("+getSQL("guid",dep_id)+")";
		try
		{
			st=conn.createStatement();
			st.execute(sql);
		}
		catch (SQLException e)
		{
			EmpExecutionContext.error(e,"删除生日祝福失败！");
			//回滚
//			conn.rollback();
			throw e;
		}finally {
			if(st != null){
				st.close();
			}
		}
	}
	
	//删除客户信息
	public void  deleteLfClient(String dep_id,Connection conn)throws Exception{
		Statement st = null;
		String sql="delete from LF_Client where  client_id in ("+getSQL("client_id",dep_id)+")";
		try
		{
			st=conn.createStatement();
			st.execute(sql);
		}
		catch (SQLException e)
		{
			EmpExecutionContext.error(e,"删除客户信息失败！");
			//回滚
//			conn.rollback();
			throw e;
		}finally {
			if(st != null){
				st.close();
			}
		}
	}
	
	
	
	
	//批量删除的公共方法
	public String getSQL(String str,String dep_id){
		if(str!=null&&"CLIENT_ID".equals(str.toUpperCase())){
			return "SELECT client_id FROM (SELECT count(client_id) as total,client_id FROM(SELECT a.CLIENT_ID,a.dep_id "+
			"FROM LF_ClIENT_DEP_SP a,(SELECT client_id FROM lf_client_dep_sp WHERE "+
			"dep_id = "+dep_id+") b WHERE a.CLIENT_ID = b.client_id) c GROUP BY c.client_id ) d WHERE d.total=1";
		}
		return "SELECT "+str+" FROM LF_CLIENT WHERE client_id IN "+
		"( SELECT client_id FROM (SELECT count(client_id) as total,client_id FROM(SELECT a.CLIENT_ID,a.dep_id "+
		"FROM LF_ClIENT_DEP_SP a,(SELECT client_id FROM lf_client_dep_sp WHERE "+
		"dep_id = "+dep_id+") b WHERE a.CLIENT_ID = b.client_id) c GROUP BY c.client_id ) d WHERE d.total=1 )";
		
	} 
	
	
	/**
	 *   查询该操作员的客户权限
	 * @param lguserid	当前操作员
	 * @return
	 */
	public Long getClientConnByLoginUserId(Long lguserid){
		Long depID=0L;
		try{
			StringBuffer buffer = new StringBuffer();
			buffer.append("select dep_id from LF_CLIDEP_CONN "+StaticValue.getWITHNOLOCK()+" where USER_ID = ").append(lguserid);
			List<DynaBean> beans = getListDynaBeanBySql(buffer.toString());
			if(beans !=null && beans.size()>0){
				for(DynaBean bean :beans){
					Object object = bean.get("dep_id");
					if(object != null && !"".equals(object)){
						depID=Long.parseLong(String.valueOf(object));
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"查询该操作员的客户权限出现异常！");
		}
		return depID;
	}
	
	/**
	 * 根据机构ID获取客户ID
	 * @param depId
	 * @return
	 */
	public String getClientIDByDepID(Long depId) throws Exception{
		String sql="select  distinct(sp.client_id) as client from LF_ClIENT_DEP_SP sp" +
				" where  "+getClientChildByParentID(depId,"sp.dep_id");
		return sql;
	}
	
	public List<DynaBean> getMWClient(String code) throws Exception{
		//客户对象集合
		List<DynaBean> beans;
		String sql="SELECT * from LF_APP_MW_CLIENT  where APP_CODE='"+code+"'";
		beans = getListDynaBeanBySql(sql);
		return beans;
		
	}
	/**
	 * 根据手机号，姓名条件查询该机构下是否存在同一个人
	 * @param conditionMap
	 * @return
	 */
	public int hasClient(LinkedHashMap<String, String> conditionMap){
		int num=0;
		List<DynaBean> beans;
		StringBuffer sf=new StringBuffer();
		sf.append("select sp.CLIENT_ID from LF_ClIENT_DEP_SP sp ,LF_ClIENT client where sp.CLIENT_ID=client.CLIENT_ID ");
		if(conditionMap.get("name")!=null && !"".equals(conditionMap.get("name"))){
			sf.append("and client.NAME='"+conditionMap.get("name")+"' ");
		}
		if(conditionMap.get("mobile")!=null && !"".equals(conditionMap.get("mobile"))){
			sf.append("and client.MOBILE='"+conditionMap.get("mobile")+"' ");
		}
		if(conditionMap.get("corpCode")!=null && !"".equals(conditionMap.get("corpCode"))){
			sf.append("and client.CORP_CODE='"+conditionMap.get("corpCode")+"' ");
		}
		if(conditionMap.get("depid")!=null && !"".equals(conditionMap.get("depid"))){
			String depid=conditionMap.get("depid").substring(0, conditionMap.get("depid").length()-1);
			sf.append("and sp.DEP_ID in ("+depid+") ");
		}
		if(conditionMap.get("clientid")!=null && !"".equals(conditionMap.get("clientid"))){
			
			sf.append("and client.CLIENT_ID <> ("+conditionMap.get("clientid")+") ");
		}
		
		beans = getListDynaBeanBySql(sf.toString());
		if(beans!=null){
			num=beans.size();
		}
		return num;
	}
	
	/**
	 * 根据客户手机号码获取未知机构成员编码
	 * @description    
	 * @param corpCode 企业编码
	 * @param modile  客户手机号码
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-21 下午02:59:33
	 */
	public List<DynaBean> getUnknowDepAppClientId(String corpCode, String modile)
	{
		String sql="SELECT client.CLIENT_ID AS CLIENTID FROM LF_CLIENT client, LF_ClIENT_DEP_SP depsp " +
				"WHERE client.CLIENT_ID = depsp.CLIENT_ID AND depsp.DEP_ID=-10 AND client.CORP_CODE='"+corpCode+"' AND MOBILE='"+modile+"'";
		
		return getListDynaBeanBySql(sql);
	}
	
	public LinkedHashMap<Long,Long> getUndepClientMsg(String corpCode)throws Exception{
		LinkedHashMap<Long,Long> msgMap = new LinkedHashMap<Long,Long>();
		String sql = "select client.mobile , client.client_id from lf_client client, lf_client_dep_sp dep "
			+"where dep.dep_id = -10 and client.corp_code ='"+corpCode
			+"' and client.client_id = dep.client_id"; 
		List<DynaBean> list = getListDynaBeanBySql(sql);
		if(list!=null && list.size()>0){
			Long phone = null;
			Long clientId = null;
			for(DynaBean bean :list){
				phone = Long.valueOf(bean.get("mobile")+"");
				clientId = Long.valueOf(bean.get("client_id")+"");
				msgMap.put(phone, clientId);
			}
		}
		return msgMap;
	}
	
	/**
	 *  相同的机构名称或机构编码记录数
	 * @description    
	 * @param depName  机构名称
	 * @param depCode	机构编码
	 * @return       	记录数		 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-10-29 下午12:06:07
	 */
	public List<DynaBean> sameDepNameOrDepCode(String depName, String depCode, String scode, String corpcode)
	{
		
		String sql = "select DEP_CODE_THIRD from Lf_CLIENT_DEP where CORP_CODE = '"+corpcode+"' and (DEP_CODE_THIRD = '"
					+depCode+"' or (DEP_NAME = '"+depName+"' and PARENT_ID = "+scode+"))";

		return getListDynaBeanBySql(sql);
	}
	
}
