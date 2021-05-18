package com.montnets.emp.group.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.group.vo.LfList2groVo;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 
 * @description
 */
//群组DAO
public class GenericGroupInfoVoDAO extends SuperDAO
{
	public List<GroupInfoVo> findGroupInfoVo(GroupInfoVo groupInfoVo)
			throws Exception
	{
		//获取字段SQL
		String fieldSql = GenericGroupInfoVoSQL.getFieldSql();
		//获取表SQL
		String tableSql = GenericGroupInfoVoSQL.getTableSql();
		//获取条件SQL
		String conditionSql = GenericGroupInfoVoSQL
				.getConditionSql(groupInfoVo);
		if(conditionSql!=null&&conditionSql.length()>0){
			conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		//获取排序SQL
		String orderBySql = GenericGroupInfoVoSQL.getOrderBySql();
		//拼接SQL
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		//查询
		List<GroupInfoVo> returnVoList = findVoListBySQL(GroupInfoVo.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}

	public List<GroupInfoVo> findGroupInfoVo(Long loginUserID,
			GroupInfoVo groupInfoVo, PageInfo pageInfo) throws Exception
	{
		//查询字段SQL
		String fieldSql = GenericGroupInfoVoSQL.getFieldSql();
		//表SQL
		String tableSql = GenericGroupInfoVoSQL.getTableSql();
		//条件SQL
		String conditionSql = GenericGroupInfoVoSQL
				.getConditionSql(groupInfoVo);
		if (conditionSql != null && conditionSql.length() > 0) {
			conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		//排序SQL
		String orderBySql = GenericGroupInfoVoSQL.getOrderBySql();

		String sql = "";
		if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).append(orderBySql).toString();
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).toString();
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).toString();
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).toString();
		}
		//统计
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql).toString();
		//查询
		List<GroupInfoVo> returnVoList =new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						GroupInfoVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	
	public List<GroupInfoVo> findLikeGroupInfoVo(Long loginUserID,
			GroupInfoVo groupInfoVo, PageInfo pageInfo) throws Exception
	{
		//字段SQL
		String fieldSql = GenericGroupInfoVoSQL.getFieldSql();
		//表SQL
		String tableSql = GenericGroupInfoVoSQL.getTableSql();
		//条件SQL
		String conditionSql = GenericGroupInfoVoSQL
				.getConditionSql1(groupInfoVo);
		if (conditionSql != null && conditionSql.length() > 0) {
			conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		//排序SQL
		String orderBySql = GenericGroupInfoVoSQL.getOrderBySql();
		//拼接SQL
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		//统计SQL
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql).toString();
		//查询
		/*List<GroupInfoVo> returnVoList = findPageVoListBySQL(
						GroupInfoVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);*/
		List<GroupInfoVo> returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				GroupInfoVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	
	public List<GroupInfoVo> findGroupInfoVo(Long loginUserID,
			GroupInfoVo groupInfoVo) throws Exception
	{
		//字段SQL
		String fieldSql = GenericGroupInfoVoSQL.getFieldSql();
		//表SQL
		String tableSql = GenericGroupInfoVoSQL.getTableSql();
		//条件SQL
		String conditionSql = GenericGroupInfoVoSQL
				.getConditionSql(groupInfoVo);
		if (conditionSql != null && conditionSql.length() > 0) {
			conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		//排序SQL
		String orderBySql = GenericGroupInfoVoSQL.getOrderBySql();
		//拼接SQL
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		//查询
		List<GroupInfoVo> returnVoList = findVoListBySQL(
						GroupInfoVo.class, sql, 
						StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	
	public List<LfList2groVo> findGroupInfoVo(LfList2groVo vo,Long udgid) throws Exception
	{
		//查询lfemployee表
		StringBuffer querySql = new StringBuffer("select employee.guid,NAME,mobile,l2g_type,share_type from ")
		   					  .append(TableLfEmployee.TABLE_NAME).append(" employee").append(StaticValue.getWITHNOLOCK())
		   					  .append(" inner join ").append(TableLfList2gro.TABLE_NAME).append(" list2gro ").append(StaticValue.getWITHNOLOCK()).append("on ")
		   					  .append("employee.guid=list2gro.guid ");
		//查询lfmalist表
		StringBuffer querySql2 = new StringBuffer("select malist.guid,NAME,mobile,l2g_type,share_type from ")
			  .append(TableLfMalist.TABLE_NAME).append(" malist").append(StaticValue.getWITHNOLOCK())
			  .append(" inner join ").append(TableLfList2gro.TABLE_NAME).append(" list2gro ").append(StaticValue.getWITHNOLOCK()).append("on ")
			  .append("malist.guid=list2gro.guid ");
		
		StringBuffer whereSql = new StringBuffer(" where ").append("udg_id=").append(udgid);
		//加入查询条件
		if(vo.getName()!=null)
		{
			whereSql.append(" and ").append(TableLfEmployee.NAME).append(" like ").append("'%").append(vo.getName()).append("%'");
		}
		if(vo.getMobile()!=null)
		{
			whereSql.append(" and ").append(TableLfEmployee.MOBILE).append(" like ").append("'%").append(vo.getMobile()).append("%'");
		}
		List<LfList2groVo> vos =new ArrayList<LfList2groVo>();
		vos=findVoListBySQL(LfList2groVo.class, querySql.append(whereSql).toString(), StaticValue.EMP_POOLNAME);
		List<LfList2groVo> vos2 =findVoListBySQL(LfList2groVo.class, querySql2.append(whereSql).toString(), StaticValue.EMP_POOLNAME);
		//查询出来的结果合并
		vos.addAll(vos2);
		return vos;
	}
	public List<LfList2groVo> findGroupInfoVoByPageInfo(LfList2groVo vo,PageInfo pageInfo,Long udgid) throws Exception
	{
		//查询lfemployee表
		StringBuffer querySql = new StringBuffer("select employee.guid,NAME,mobile,l2g_type,share_type from ")
				.append(TableLfEmployee.TABLE_NAME).append(" employee").append(StaticValue.getWITHNOLOCK())
				.append(" inner join ").append(TableLfList2gro.TABLE_NAME).append(" list2gro ").append(StaticValue.getWITHNOLOCK()).append("on ")
				.append("employee.guid=list2gro.guid ");
		//查询lfmalist表
		StringBuffer querySql2 = new StringBuffer("select malist.guid,NAME,mobile,l2g_type,share_type from ")
				.append(TableLfMalist.TABLE_NAME).append(" malist").append(StaticValue.getWITHNOLOCK())
				.append(" inner join ").append(TableLfList2gro.TABLE_NAME).append(" list2gro ").append(StaticValue.getWITHNOLOCK()).append("on ")
				.append("malist.guid=list2gro.guid ");
		
		StringBuffer whereSql = new StringBuffer(" where ").append("udg_id=").append(udgid);
		//加入查询条件
		if(vo.getName()!=null)
		{
			whereSql.append(" and ").append(TableLfEmployee.NAME).append(" like ").append("'%").append(vo.getName()).append("%'");
		}
		if(vo.getMobile()!=null)
		{
			whereSql.append(" and ").append(TableLfEmployee.MOBILE).append(" like ").append("'%").append(vo.getMobile()).append("%'");
		}
		//List<LfList2groVo> vos =new ArrayList<LfList2groVo>();
		StringBuffer sql=new StringBuffer(querySql.append(whereSql).toString());
		StringBuffer sql2=new StringBuffer(querySql2.append(whereSql).toString());
		sql.append(" UNION ALL ").append(sql2);
		
		String countSql = "select count(*) totalcount from ("+sql.toString()+" ) temp";
		
		
		//vos=findVoListBySQL(LfList2groVo.class, querySql.append(whereSql).toString(), StaticValue.EMP_POOLNAME);
		//List<LfList2groVo> vos2 =findVoListBySQL(LfList2groVo.class, querySql2.append(whereSql).toString(), StaticValue.EMP_POOLNAME);
		List<LfList2groVo>  vos=new DataAccessDriver().getGenericDAO().findPageVoListBySQL(LfList2groVo.class,sql.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//查询出来的结果合并
		//vos.addAll(vos2);
		return vos;
	}
	
	/**
	 *   增加分页	查询员工群组中的内容
	 * @param groupId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> findGroupUserByIds(Long groupId,PageInfo pageInfo,String name) throws Exception{
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
			if(name != null && !"".equals(name)){
				sqlStr.append(" and (case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
				") when 0 then employee.").append(TableLfEmployee.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
				" end) like '%").append(name).append("%'");
			}
		
		//查询
		String countSql = "select count(*) totalcount from ("+sqlStr.toString()+") a";
		sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc");
		returnVoList =new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}

    /**
     * 增加分页	查询员工群组中的内容
     * @param groupId
     * @param pageInfo
     * @param name
     * @param corpCode
     * @return
     * @throws Exception
     */
    public List<GroupInfoVo> findGroupUserByIds(Long groupId,PageInfo pageInfo,String name,String corpCode) throws Exception{
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
        //企业编码条件
        sqlStr.append(" and (employee.").append(TableLfEmployee.CORP_CODE).append(" = '").append(corpCode).append("'");
        sqlStr.append(" or malist.").append(TableLfMalist.CORP_CODE).append(" = '").append(corpCode).append("')");
        if(name != null && !"".equals(name)){
            sqlStr.append(" and (case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
                    ") when 0 then employee.").append(TableLfEmployee.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
                    " end) like '%").append(name).append("%'");
        }

        //查询
        String countSql = "select count(*) totalcount from ("+sqlStr.toString()+") a";
        sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc");
        returnVoList =new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
        //返回
        return returnVoList;
    }
	/**
	 *   增加分页	查询客户群组中的内容
	 * @param groupId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> findGroupCliByIds(Long groupId,PageInfo pageInfo,String name) throws Exception{
		//初始化LIST
		List<GroupInfoVo> returnVoList = null;
		//拼凑查询字段
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(
				TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(
				TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(
				TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(") when 1 then client.").append(TableLfClient.MOBILE).append("  else malist.")
				.append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
						") when 1 then client.").append(TableLfEmployee.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
						" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		//拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
				.append(TableLfClient.TABLE_NAME).append(" client  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=client.").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
						" malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
				.append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
				.append("=udgroup.").append(TableLfUdgroup.GROUP_ID).append(" where udgroup.")
				.append(TableLfUdgroup.UDG_ID).append("=").append(groupId);
			if(name != null && !"".equals(name)){
				sqlStr.append(" and (case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
				") when 1 then client.").append(TableLfClient.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
				" end) like '%").append(name).append("%'");
			}
		
		//查询
		String countSql = "select count(*) totalcount from ("+sqlStr.toString()+") a";
		sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc");
		returnVoList =new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}

    /**
     *   增加分页	查询客户群组中的内容
     * @param groupId
     * @param pageInfo
     * @param name
     * @param corpCode
     * @return
     * @throws Exception
     */
    public List<GroupInfoVo> findGroupCliByIds(Long groupId,PageInfo pageInfo,String name,String corpCode) throws Exception{
        //初始化LIST
        List<GroupInfoVo> returnVoList = null;
        //拼凑查询字段
        StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(
                TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(
                TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(
                TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(
                TableLfList2gro.L2G_TYPE).append(") when 1 then client.").append(TableLfClient.MOBILE).append("  else malist.")
                .append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
                        ") when 1 then client.").append(TableLfEmployee.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
                        " end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
        //拼凑TABLE
        sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
                .append(TableLfClient.TABLE_NAME).append(" client  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
                .append("=client.").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
                " malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
                .append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
                .append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
                .append("=udgroup.").append(TableLfUdgroup.GROUP_ID).append(" where udgroup.")
                .append(TableLfUdgroup.UDG_ID).append("=").append(groupId);
        //企业编码条件
        sqlStr.append(" and (client.").append(TableLfClient.CORP_CODE).append(" = '").append(corpCode).append("'");
        sqlStr.append(" or malist.").append(TableLfMalist.CORP_CODE).append(" = '").append(corpCode).append("')");
        if(name != null && !"".equals(name)){
            sqlStr.append(" and (case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
                    ") when 1 then client.").append(TableLfClient.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
                    " end) like '%").append(name).append("%'");
        }

        //查询
        String countSql = "select count(*) totalcount from ("+sqlStr.toString()+") a";
        sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc");
        returnVoList =new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
        //返回
        return returnVoList;
    }












}
