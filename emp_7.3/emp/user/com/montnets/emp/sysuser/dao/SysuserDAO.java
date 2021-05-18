package com.montnets.emp.sysuser.dao;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.engine.LfService;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfRoles;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.sysuser.vo.LfSysuser2Vo;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfRoles;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.sysuser.TableLfUser2role;
import com.montnets.emp.table.tailnumber.TableLfSubnoAllot;
import com.montnets.emp.util.PageInfo;
/**
 * 操作员DAO
 * @author Administrator
 *
 */
public class SysuserDAO extends SuperDAO 
{
	/**
	 * 通过查询条件vo获取操作员vo集合
	 * @param sysuserVo
	 * @param userId
	 * @param depId
	 * @param roleId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuserVo> findLfSysuserVo(LfSysuserVo sysuserVo,
			Long userId, Long depId, Long roleId, PageInfo pageInfo)
			throws Exception
	{
		List<LfSysuserVo> lfSysuserVo = null;
		if (depId == null && roleId == null)
		{
			lfSysuserVo = getAllSysuser(sysuserVo, userId, pageInfo);
		} else if (depId != null && roleId == null)
		{
			lfSysuserVo = getSysuserByDepId(sysuserVo, depId, pageInfo);
		} else if (depId == null && roleId != null)
		{
			lfSysuserVo = getSysuserByRoleId(sysuserVo, userId, roleId,
					pageInfo);
		} else
		{
			lfSysuserVo = getSysuserByDepIdandRoleId(sysuserVo, userId, depId,
					roleId, pageInfo);
		}
		lfSysuserVo = completeLfSysuserVo(lfSysuserVo);
		return lfSysuserVo;
	}

	
	/**
	 * 通过id获取操作员对象
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	public LfSysuserVo findLfSysuserVoByID(String ID) throws Exception
	{
		LfSysuserVo returnVo = new LfSysuserVo();
		String sql = "select sysuser." + TableLfSysuser.USER_ID + ",sysuser."+TableLfSysuser.USER_TYPE+ ",sysuser."
				+ TableLfSysuser.NAME + ",sysuser." + TableLfSysuser.USER_STATE
				+ ",sysuser." + TableLfSysuser.HOLDER + ",sysuser."
				+ TableLfSysuser.REG_TIME + ",sysuser." + TableLfSysuser.MOBILE
				+ ",sysuser." + TableLfSysuser.GUID + ",sysuser."
				+ TableLfSysuser.OPH + ",sysuser."
				+ TableLfSysuser.QQ + ",sysuser." + TableLfSysuser.E_MAIL + ",sysuser." + TableLfSysuser.ISEXISTSUBNO
				+ ",sysuser." + TableLfSysuser.SEX + ",sysuser."
				+ TableLfSysuser.PASSW +",sysuser."+TableLfSysuser.FAX
				+",sysuser."+TableLfSysuser.MSN+",sysuser."+TableLfSysuser.BIRTHDAY
				+",sysuser."+TableLfSysuser.IS_REVIEWER+",sysuser."+TableLfSysuser.COMMENTS
				+",sysuser.duties"+ ",sysuser."
				+ TableLfSysuser.USER_NAME + ",sysuser."+TableLfSysuser.PERMISSION_TYPE
				+",sysuser."+TableLfSysuser.USER_CODE+",sysuser."+TableLfSysuser.GUID+",sysuser."+TableLfSysuser.SHOWNUM
				+",dep." + TableLfDep.DEP_ID
				+ ",dep." + TableLfDep.DEP_NAME;
		String baseSql = " from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDep.TABLE_NAME
				+ " dep on sysuser." + TableLfSysuser.DEP_ID + "=dep."
				+ TableLfDep.DEP_ID;
		sql += baseSql;
		sql += " where sysuser." + TableLfSysuser.USER_ID + "=" + ID;
		List<LfSysuserVo> returnList = findVoListBySQL(LfSysuserVo.class, sql,
				StaticValue.EMP_POOLNAME);
		returnList = completeLfSysuserVo(returnList);
		if (returnList != null && returnList.size() > 0)
		{
			returnVo = returnList.get(0);
		}
		return returnVo;
	}

//  此方法作用类似于completeLfSysuserVo，去掉了填充domDepList,因为页面显示太慢，这个属性也没用
	/**
	 * 
	 * @param lfsysuser
	 *            LfSysuserVo的List。
	 * @return
	 * @throws Exception
	 */
	protected List<LfSysuserVo> completeLfSysuserVo(List<LfSysuserVo> lfsysuser)
			throws Exception
	{
		StringBuffer sb = new StringBuffer("");
		LinkedHashMap<Long, String> subnoMap = new LinkedHashMap<Long, String>();
		List<LfSubnoAllot> allotList = null;
		LfSysuserVo userVo = null;
		for(int j=0;j< lfsysuser.size(); j++){
			userVo = lfsysuser.get(j);
			if(userVo.getIsExistSubno() == 1){
				sb.append("'").append(userVo.getGuId()).append("',");
			}
		}
		String sql = "";
		if(sb.length()>0){
			sql = sb.toString().substring(0, sb.toString().length()-1);
			//拼接sql
			StringBuffer subnoSql = new StringBuffer("SELECT * FROM ").append(TableLfSubnoAllot.TABLE_NAME)
			.append(" WHERE ").append(TableLfSubnoAllot.LOGINID).append(" IN ( ").append(sql).append(" ) and MENUCODE is null  and BUS_CODE is null")
			.append(" and ").append(TableLfSubnoAllot.ISVALID).append(" = 1 ");
			allotList = findEntityListBySQL(LfSubnoAllot.class, subnoSql.toString(),StaticValue.EMP_POOLNAME);
		}
		
		if(allotList != null && allotList.size()>0){
			LfSubnoAllot allot = null;
			for(int k=0;k<allotList.size();k++){
				allot = allotList.get(k);
				subnoMap.put(Long.valueOf(allot.getLoginId()), allot.getUsedExtendSubno());
			}
		}
		for (int i = 0; i < lfsysuser.size(); i++) {
			LfSysuserVo lfSysuserVo = lfsysuser.get(i);
			if(lfSysuserVo.getIsExistSubno() == 1){
				String usedSubno = subnoMap.get(lfSysuserVo.getGuId());
				if(usedSubno != null && !"".equals(usedSubno)){
					lfSysuserVo.setUsedSubno(usedSubno);
				}
			}
			//拼接sql
			if(lfSysuserVo.getPermissionType() == 2){
				//有机构权限
				String domDepSql = "select dep.*" + " from "
				+ TableLfDomination.TABLE_NAME + " domination inner join "
				+ TableLfDep.TABLE_NAME + " dep on domination."
				+ TableLfDomination.DEP_ID + "=dep." + TableLfDep.DEP_ID
				+ " where domination." + TableLfDomination.USER_ID + "="
				+ lfSysuserVo.getUserId() + " order by dep."
				+ TableLfDep.DEP_ID + " asc";
				lfSysuserVo.setDomDepList(findEntityListBySQL(LfDep.class,
						domDepSql, StaticValue.EMP_POOLNAME));
			}
			//拼接sql
			String roleSql = "select roles.*" + " from "
					+ TableLfRoles.TABLE_NAME + " roles inner join "
					+ TableLfUser2role.TABLE_NAME + " user2role on roles."
					+ TableLfRoles.ROLE_ID + "=user2role."
					+ TableLfUser2role.ROLE_ID + " where user2role."
					+ TableLfUser2role.ROLE_ID + "!=1 and user2role."
					+ TableLfUser2role.USER_ID + "=" + lfSysuserVo.getUserId()
					+ " order by roles." + TableLfRoles.ROLE_ID + " asc";
			lfSysuserVo.setRoleList(findEntityListBySQL(LfRoles.class, roleSql,
					StaticValue.EMP_POOLNAME));
		}
		return lfsysuser;
	}
	/**
	 * 
	 * @param GL_UserID
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	private List<LfSysuserVo> getAllSysuser(LfSysuserVo sysuserVo,
			Long GL_UserID, PageInfo pageInfo) throws Exception
	{
		String sql = "select sysuser." + TableLfSysuser.USER_ID +",sysuser."+TableLfSysuser.USER_TYPE+ ",sysuser."
				+ TableLfSysuser.NAME + ",sysuser." + TableLfSysuser.USER_STATE
				+ ",sysuser." + TableLfSysuser.HOLDER + ",sysuser."
				+ TableLfSysuser.REG_TIME + ",sysuser." + TableLfSysuser.MOBILE
				+ ",sysuser." + TableLfSysuser.OPH + ",sysuser."
				+ TableLfSysuser.QQ + ",sysuser." + TableLfSysuser.E_MAIL + ",sysuser." + TableLfSysuser.ISEXISTSUBNO
				+ ",sysuser." + TableLfSysuser.SEX + ",sysuser."
				+ TableLfSysuser.PASSW +",sysuser."+TableLfSysuser.FAX
				+",sysuser."+TableLfSysuser.MSN+",sysuser."+TableLfSysuser.BIRTHDAY
				+",sysuser."+TableLfSysuser.IS_REVIEWER+ ",sysuser."+TableLfSysuser.COMMENTS
				+",sysuser.duties"+",sysuser."
				+ TableLfSysuser.USER_NAME +",sysuser."+TableLfSysuser.PERMISSION_TYPE+ 
				",sysuser."+TableLfSysuser.USER_CODE+",sysuser."+TableLfSysuser.GUID+",sysuser."+TableLfSysuser.SHOWNUM+",dep." + TableLfDep.DEP_ID
				+ ",dep." + TableLfDep.DEP_NAME;
		String countSql = "select count(*) totalcount";
		String baseSql = " from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDep.TABLE_NAME
				+ " dep on sysuser." + TableLfSysuser.DEP_ID + "=dep."
				+ TableLfDep.DEP_ID;
		sql += baseSql;
		countSql += baseSql;
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(GL_UserID);
		String dominationSql = new StringBuffer(" where sysuser.").append(TableLfSysuser.USER_ID).append("<>1 and ").append(" (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(GL_UserID).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(domination).append("))").toString();

		sql += dominationSql;
		countSql += dominationSql;
		StringBuffer conditionSql = new StringBuffer(GenericLfSysuserVoSQL
				.getConditionSql(sysuserVo));
		sql += conditionSql;
		countSql += conditionSql;
		String orderSql = " order by sysuser." + TableLfSysuser.USER_NAME
				+ " asc";
		sql += orderSql;
		List<LfSysuserVo> returnList =  new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						LfSysuserVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 * 
	 * @param GL_UserID
	 * @param depId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	private List<LfSysuserVo> getSysuserByDepId(LfSysuserVo sysuserVo,
			Long depId, PageInfo pageInfo) throws Exception
	{
		String sql = "";
		String countSql = "";
		sql = "select sysuser." + TableLfSysuser.USER_ID + ",sysuser."+TableLfSysuser.USER_TYPE+",sysuser."
				+ TableLfSysuser.NAME + ",sysuser." + TableLfSysuser.USER_STATE
				+ ",sysuser." + TableLfSysuser.HOLDER + ",sysuser."
				+ TableLfSysuser.REG_TIME + ",sysuser." + TableLfSysuser.MOBILE
				+ ",sysuser." + TableLfSysuser.OPH + ",sysuser."
				+ TableLfSysuser.QQ + ",sysuser." + TableLfSysuser.E_MAIL + ",sysuser." + TableLfSysuser.ISEXISTSUBNO
				+ ",sysuser." + TableLfSysuser.SEX + ",sysuser."
				+ TableLfSysuser.PASSW+",sysuser."+TableLfSysuser.FAX
				+",sysuser."+TableLfSysuser.MSN+",sysuser."+TableLfSysuser.BIRTHDAY
				+",sysuser."+TableLfSysuser.IS_REVIEWER+",sysuser."+TableLfSysuser.COMMENTS
				+",sysuser.duties"+ ",sysuser."
				+ TableLfSysuser.USER_NAME +",sysuser."+TableLfSysuser.PERMISSION_TYPE
				+",sysuser."+TableLfSysuser.USER_CODE+",sysuser."+TableLfSysuser.GUID
				+",sysuser."+TableLfSysuser.SHOWNUM
				+",dep." + TableLfDep.DEP_ID
				+ ",dep." + TableLfDep.DEP_NAME;
		countSql = "select count(*) totalcount";
		String baseSql = " from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDep.TABLE_NAME
				+ " dep on sysuser." + TableLfSysuser.DEP_ID + "=dep."
				+ TableLfDep.DEP_ID + " where sysuser." + TableLfSysuser.DEP_ID
				+ "=" + depId+" and sysuser."+ TableLfSysuser.USER_ID+"<>1";
		//如果条件中选中了包含子机构
		if(sysuserVo.getIsAll()!=null&&sysuserVo.getIsAll()==1){
			baseSql = " from " + TableLfSysuser.TABLE_NAME
			+ " sysuser inner join " + TableLfDep.TABLE_NAME
			+ " dep on sysuser." + TableLfSysuser.DEP_ID + "=dep."
			+ TableLfDep.DEP_ID + " where sysuser."+ TableLfSysuser.USER_ID+"<>1"
			+ " and (dep." + TableLfDep.DEP_PATH + " like '"
			+depId+"/%' or dep."+TableLfDep.DEP_PATH  + " like '%/"+depId+"/%')";
		}
		sql += baseSql;
		countSql += baseSql;
		StringBuffer conditionSql = new StringBuffer(GenericLfSysuserVoSQL
				.getConditionSql(sysuserVo));
		sql += conditionSql;
		countSql += conditionSql;
		String orderSql = " order by sysuser." + TableLfSysuser.USER_NAME
				+ " asc";
		sql += orderSql;
		List<LfSysuserVo> returnList =  new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						LfSysuserVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 * 
	 * @param GL_UserID
	 * @param roleId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	private List<LfSysuserVo> getSysuserByRoleId(LfSysuserVo sysuserVo,
			Long GL_UserID, Long roleId, PageInfo pageInfo) throws Exception
	{
		String sql = "select sysuser." + TableLfSysuser.USER_ID +",sysuser."+TableLfSysuser.USER_TYPE+ ",sysuser."
				+ TableLfSysuser.NAME + ",sysuser." + TableLfSysuser.USER_STATE
				+ ",sysuser." + TableLfSysuser.HOLDER + ",sysuser."
				+ TableLfSysuser.REG_TIME + ",sysuser." + TableLfSysuser.MOBILE
				+ ",sysuser." + TableLfSysuser.OPH + ",sysuser."
				+ TableLfSysuser.QQ + ",sysuser." + TableLfSysuser.E_MAIL + ",sysuser." + TableLfSysuser.ISEXISTSUBNO
				+ ",sysuser." + TableLfSysuser.SEX + ",sysuser."
				+ TableLfSysuser.PASSW +",sysuser."+TableLfSysuser.FAX
				+",sysuser."+TableLfSysuser.MSN+",sysuser."+TableLfSysuser.BIRTHDAY
				+",sysuser."+TableLfSysuser.IS_REVIEWER+",sysuser."+TableLfSysuser.COMMENTS
				+",sysuser.duties"+ ",sysuser."
				+ TableLfSysuser.USER_NAME  +",sysuser."+TableLfSysuser.PERMISSION_TYPE
				+",sysuser."+TableLfSysuser.USER_CODE+",sysuser."+TableLfSysuser.GUID
				+",sysuser."+TableLfSysuser.SHOWNUM
				+ ",dep." + TableLfDep.DEP_ID
				+ ",dep." + TableLfDep.DEP_NAME;
		String countSql = "select count(*) totalcount";
		String baseSql = " from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDep.TABLE_NAME
				+ " dep on sysuser." + TableLfSysuser.DEP_ID + "=dep."
				+ TableLfDep.DEP_ID + " inner join "
				+ TableLfUser2role.TABLE_NAME + " user2role on user2role."
				+ TableLfUser2role.USER_ID + "=sysuser."
				+ TableLfSysuser.USER_ID;
		sql += baseSql;
		countSql += baseSql;
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(GL_UserID);
		String dominationSql = new StringBuffer(" where (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(GL_UserID).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(domination).append("))").append(" and sysuser.").append(TableLfSysuser.USER_ID).append("<>1").toString();
		sql += dominationSql;
		countSql += dominationSql;
		String conditionSql = " and user2role." + TableLfUser2role.ROLE_ID
				+ "=" + roleId;

		conditionSql += GenericLfSysuserVoSQL.getConditionSql(sysuserVo);

		sql += conditionSql;
		countSql += conditionSql;

		sql += conditionSql;
		countSql += conditionSql;
		String orderSql = " order by sysuser." + TableLfSysuser.USER_NAME
				+ " asc";
		sql += orderSql;
		List<LfSysuserVo> returnList =  new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						LfSysuserVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 * 
	 * @param GL_UserID
	 * @param depId
	 * @param roleId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	private List<LfSysuserVo> getSysuserByDepIdandRoleId(LfSysuserVo sysuserVo,
			Long GL_UserID, Long depId, Long roleId, PageInfo pageInfo)
			throws Exception
	{
		String sql = "select sysuser." + TableLfSysuser.USER_ID +",sysuser."+TableLfSysuser.USER_TYPE+ ",sysuser."
				+ TableLfSysuser.NAME + ",sysuser." + TableLfSysuser.USER_STATE
				+ ",sysuser." + TableLfSysuser.HOLDER + ",sysuser."
				+ TableLfSysuser.REG_TIME + ",sysuser." + TableLfSysuser.MOBILE
				+ ",sysuser." + TableLfSysuser.OPH + ",sysuser."
				+ TableLfSysuser.QQ + ",sysuser." + TableLfSysuser.E_MAIL + ",sysuser." + TableLfSysuser.ISEXISTSUBNO
				+ ",sysuser." + TableLfSysuser.SEX + ",sysuser."
				+ TableLfSysuser.PASSW +",sysuser."+TableLfSysuser.FAX
				+",sysuser."+TableLfSysuser.MSN+",sysuser."+TableLfSysuser.BIRTHDAY
				+",sysuser."+TableLfSysuser.IS_REVIEWER+",sysuser."+TableLfSysuser.COMMENTS
				+",sysuser.duties"+ ",sysuser."
				+ TableLfSysuser.USER_NAME  +",sysuser."+TableLfSysuser.PERMISSION_TYPE
				+",sysuser."+TableLfSysuser.USER_CODE+",sysuser."+TableLfSysuser.GUID
				+",sysuser."+TableLfSysuser.SHOWNUM
				+ ",dep." + TableLfDep.DEP_ID
				+ ",dep." + TableLfDep.DEP_NAME;
		String countSql = "select count(*) totalcount";
		String baseSql = " from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDep.TABLE_NAME
				+ " dep on sysuser." + TableLfSysuser.DEP_ID + "=dep."
				+ TableLfDep.DEP_ID + " inner join "
				+ TableLfUser2role.TABLE_NAME + " user2role on user2role."
				+ TableLfUser2role.USER_ID + "=sysuser."
				+ TableLfSysuser.USER_ID;
		sql += baseSql;
		countSql += baseSql;
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(GL_UserID);
		String dominationSql = new StringBuffer(" where (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(GL_UserID).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(domination).append("))").append(" and sysuser.").append(TableLfSysuser.USER_ID).append("<>1").toString();
		sql += dominationSql;
		countSql += dominationSql;
		String conditionSql = " and user2role." + TableLfUser2role.ROLE_ID
				+ "=" + roleId + " and sysuser." + TableLfSysuser.DEP_ID + "="
				+ depId;
		//如果条件中选中了包含子机构
		if(sysuserVo.getIsAll()!=null&&sysuserVo.getIsAll()==1){
			conditionSql = " and user2role." + TableLfUser2role.ROLE_ID
			+ "=" + roleId + " and (dep." + TableLfDep.DEP_PATH + " like '"
			+depId+"/%' or dep."+TableLfDep.DEP_PATH + " like '%/"+depId+"/%')";
		}
		conditionSql += GenericLfSysuserVoSQL.getConditionSql(sysuserVo);

		sql += conditionSql;
		countSql += conditionSql;
		String orderSql = " order by sysuser." + TableLfSysuser.USER_NAME
				+ " asc";
		sql += orderSql;
		List<LfSysuserVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						LfSysuserVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnList;
	}
	/**
	 * 通过角色id查询所有有该角色的操作员
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser2Vo> findAllLfSysuserByRoleId(Long roleId) throws Exception{
		//拼接sql
		String sql = new StringBuffer("select sysuser.").append(TableLfSysuser.USER_ID).append(",sysuser.")
						.append(TableLfSysuser.USER_TYPE).append(",sysuser.")
						.append(TableLfSysuser.NAME).append(",sysuser.")
						.append(TableLfSysuser.USER_STATE)
						.append(",sysuser.").append(TableLfSysuser.HOLDER).append(",sysuser.")
						.append(TableLfSysuser.REG_TIME).append(",sysuser.").append( TableLfSysuser.MOBILE)
						.append(",sysuser." ).append(TableLfSysuser.OPH ).append( ",sysuser.")
						.append(TableLfSysuser.QQ).append(",sysuser." ).append( TableLfSysuser.E_MAIL)
						.append(",sysuser." ).append( TableLfSysuser.SEX ).append( ",sysuser.")
						.append(TableLfSysuser.GUID).append( ",sysuser.")
						.append(TableLfSysuser.PASSW).append( ",sysuser.")
						.append(TableLfSysuser.USER_NAME).append( ",sysuser." ).append( TableLfDep.DEP_ID)
						.append(" from ").append(TableLfSysuser.TABLE_NAME)
						.append(" sysuser "+StaticValue.getWITHNOLOCK()+" where sysuser.").append(TableLfSysuser.USER_ID)
						.append(" in(select userrole.").append(TableLfUser2role.USER_ID)
						.append(" from ").append(TableLfUser2role.TABLE_NAME)
						.append(" userrole where userrole.").append(TableLfUser2role.ROLE_ID)
						.append("=").append(roleId).append(")")
						.toString();
		//返回查询结果
		return findVoListBySQL(LfSysuser2Vo.class, sql,
				StaticValue.EMP_POOLNAME);
	}
	
	public List<LfSysuser> getLfSysUserByUserCode(String corpCode,String userCode)
		throws Exception {
	//拼接sql
	StringBuffer sql = new StringBuffer("select * from ").append(
			TableLfSysuser.TABLE_NAME).append(" lfSysUser ")
			/*.append(" and lfSysUser.").append(
					TableLfSysuser.CORP_CODE).append(" = '").append(
							corpCode).append("' ")*/
			.append(" where lfSysUser.").append(
			TableLfSysuser.USER_CODE).append(" = '").append(
					corpCode+userCode).append("' ");
	//执行查询
	List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql
			.toString(), StaticValue.EMP_POOLNAME);
	//返回结果
	return returnList;
	}
	
	public List<LfSysuser> findDomUsedUserBySysuserID(String sysuserID)
		throws Exception {
		//拼接sql
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(sysuserID)
				.append(" and " + TableLfSysuser.USER_STATE + " = 1");
		StringBuffer dominationSql = new StringBuffer(" where (").append(
				TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
				" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
				domination).append("))");
		String sql = new StringBuffer("select * from ").append(
				TableLfSysuser.TABLE_NAME).append(dominationSql).append(" and ").append(TableLfSysuser.USER_ID).append("!=1").toString();
		//排序条件拼接
		sql += " order by " + TableLfSysuser.NAME + " asc";
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
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
			// 这里是为了解决员工机构关联表出现了多条记录的处理方法，产生2条记录的原因可能是机器卡，或者同步
			List<LfEmpDepConn> connList = null;
			StringBuffer sb = new StringBuffer();
			sb.append(" select * from ").append(TableLfEmpDepConn.TABLE_NAME)
					.append(" c " + StaticValue.getWITHNOLOCK()).append(" where c.")
					.append(TableLfEmpDepConn.USER_ID).append(" = ").append(
							userId);
			connList = findEntityListBySQL(LfEmpDepConn.class, sb.toString(),
					StaticValue.EMP_POOLNAME);
			LfEmpDepConn conn = null;
			if (connList != null && connList.size() > 0)
			{
				conn = connList.get(0);
				Long id = conn.getDepId();
				sql = new StringBuffer(" select e.* from ").append(
						TableLfEmployeeDep.TABLE_NAME).append(
						" e " + StaticValue.getWITHNOLOCK()).append(" where  e.")
						.append(TableLfEmployeeDep.DEP_ID).append(" = ")
						.append(id).append(" or ").append(
								TableLfEmployeeDep.PARENT_ID).append(" = ")
						.append(id).append(" order by ").append(
								TableLfEmployeeDep.DEP_ID).append(
								" " + StaticValue.ASC).toString();
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
						sql, StaticValue.EMP_POOLNAME);
			}
		} else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfEmployeeDep.PARENT_ID).append(" = ").append(depId)
					.toString();
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
		}

		return lfEmployeeDepList;
	}
	
	/**
	 * 注销下行业务定时任务
	 * 
	 * @param conn
	 * @param userId
	 * @return 成功返回true
	 */
	public boolean CanceledMtService(Connection conn, Long userId)
	{
		//id不允许为空
		if (userId == null)
		{
			EmpExecutionContext.error("注销下行业务服务失败，操作员id为空");
			return false;
		}
		//保存返回结果
		boolean result = false;
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", userId.toString());
			List<LfService> servicesList = new DataAccessDriver().getEmpDAO().findListByCondition(
					LfService.class, conditionMap, null);

			// 没记录，不需删除
			if (servicesList == null || servicesList.size() == 0)
			{
				return true;
			}

			//定时任务biz
			TaskManagerBiz timerBiz = new TaskManagerBiz();
			// LfService service = null;
			//排序条件
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("runState", "0");

			//循环处理任务集合
			for (int i = 0; i < servicesList.size(); i++)
			{
				conditionMap.clear();
				conditionMap.put("serId", servicesList.get(i).getSerId()
						.toString());
				// service = servicesList.get(i);
				// service.setRunState(0);
				//更新成失效
				// empTransDao.update(conn, service);
				//按条件更新
				new DataAccessDriver().getEmpTransDAO().update(conn, LfService.class, objectMap,
						conditionMap);

				// 删除定时器
				timerBiz.delTimerTask(conn, "ser|"
						+ servicesList.get(i).getSerId());
			}
			result = true;
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"注销下行业务服务失败!");
			return false;
		}
		//返回结果
		return result;
	}

	// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
	/**
	 * 获取机构的第一级的子机构
	 * @param depId
	 * @param pageInfo
	 * @return
	 * @throws Exception 
	 * @throws Exception 
	 */
	public List<LfDep> getFirstLevelChildDeps(Long depId, String corpCode){
		if(depId == null || corpCode == null){
			return null;
		}
		String sql = "";
		try {
			sql = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
			.append(" WHERE ").append(TableLfDep.SUPERIOR_ID).append(" = ").append(depId)
			.append(" AND ").append(TableLfDep.DEP_STATE).append(" = 1 ")
			.append(" AND ").append(TableLfDep.CORP_CODE).append(" = ").append(corpCode).toString();
			//System.out.println("---------获取机构的第一级子机构SQL---------" + sql);
			List<LfDep> firstLevelChildDeps = findEntityListBySQL(
						LfDep.class, sql, StaticValue.EMP_POOLNAME);
			return firstLevelChildDeps;
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"获取机构的第一级子机构异常!");
			return null;
		}
	}
	// end
	
	
	
	
}
