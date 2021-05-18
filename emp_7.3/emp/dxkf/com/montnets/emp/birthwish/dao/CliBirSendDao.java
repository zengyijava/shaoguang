package com.montnets.emp.birthwish.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.util.PageInfo;
/**
 *   客户生日祝福DAO
 * @author Administrator
 *
 */
public class CliBirSendDao  extends SuperDAO{
	
	/**
	 *  查询客户机构下的客户
	 * @param clientDep	机构对象
	 * @param LfClient	客户对象
	 * @param containType	包含1不包含2
	 * @param pageInfo 分页
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findClientsByDepId(LfClientDep clientDep,LfClient LfClient,Integer containType,PageInfo pageInfo) throws Exception
	{
		List<DynaBean> beanList = null;
		String sql = "select client.NAME,client.MOBILE,client.CLIENT_ID,depsp.DEP_ID,client.GUID " ;
		String countSql = "select count(*) totalcount ";
		String baseSql = " from LF_CLIENT client inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ";
		StringBuffer conditionSql = new StringBuffer("");
		conditionSql.append(" where client.CORP_CODE = '").append(LfClient.getCorpCode()).append("'");
		if(clientDep != null){
			if(containType == 1){
				conditionSql.append(" and depsp.DEP_ID in (select DEP_ID from LF_CLIENT_DEP where DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
			}else if(containType == 2){
				conditionSql.append(" and depsp.DEP_ID = ").append(clientDep.getDepId());
			}
		}
		String orderSql = " order by client.CLIENT_ID DESC";
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
	public Integer findClientsCountByDepId(LfClientDep clientDep,Integer containType) throws Exception
	{
		StringBuffer sqlBuffer = new StringBuffer(" select COUNT(c.CLIENT_ID) as totalcount from ") ;
		sqlBuffer.append(" (select  a.CLIENT_ID  from LF_ClIENT_DEP_SP a");
		if(containType == 1){
			sqlBuffer.append(" where a.DEP_ID in (select b.DEP_ID from LF_CLIENT_DEP b where b.DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
		}else if(containType == 2){
			sqlBuffer.append(" where a.DEP_ID = ").append(clientDep.getDepId());
		}
		sqlBuffer.append(")c");
		
		Integer count = findCountBySQL(sqlBuffer.toString());
		
		return count;
	}
	
	
	/**
	 * 获取当天生日的客户手机号码
	 * @param bsId 生日祝福设置Id
	 * @return 返回手机号码集合
	 * @throws Exception
	 */
	public Map<String,String> findClientPhones(Long bsId) throws Exception
	{
		//获取绑定包含子机构的机构的机构编码的sql
		StringBuffer depBuffer = new StringBuffer();
		depBuffer.append("select mem.MEMBER_ID,mem.member_type ,dep.DEP_PATH from LF_BIRTHDAYMEMBER mem ");
		depBuffer.append(" inner join LF_CLIENT_DEP dep on mem.MEMBER_ID = dep.DEP_ID ");
		depBuffer.append(" where mem.TYPE = 2 and mem.member_type in (2,3) and mem.BS_ID = ").append(bsId);
		List<DynaBean> beanList = getListDynaBeanBySql(depBuffer.toString());
		String nocontainstr = "";
		String containstr = "";
		List<String> deppathList = new ArrayList<String>();
		if(beanList != null && beanList.size()>0){
			for(DynaBean bean:beanList){
				String type = String.valueOf(bean.get("member_type"));
				String id = String.valueOf(bean.get("member_id"));
				if(type != null && "2".equals(type)){
					nocontainstr = nocontainstr + id + ",";
				}else if(type != null && "3".equals(type)){
					String deppath = String.valueOf(bean.get("dep_path"));
					containstr = containstr + id + ",";
					deppathList.add(deppath);
				}
			}
		}
		Map<String,String> clientsMap = new HashMap<String,String>();

		if(nocontainstr != null && !"".equals(nocontainstr)){
			nocontainstr = nocontainstr.substring(0, nocontainstr.length()-1);
			depBuffer = new StringBuffer();
			depBuffer.append("select client.MOBILE,client.NAME from LF_CLIENT client ");
			depBuffer.append(" inner join LF_ClIENT_DEP_SP sp on client.CLIENT_ID = sp.CLIENT_ID ");
			depBuffer.append(" where sp.DEP_ID in (").append(nocontainstr).append(") ");
			depBuffer.append(this.getBirthdayMsg());
			List<DynaBean> nocontainstrList = getListDynaBeanBySql(depBuffer.toString());
			if(nocontainstrList != null && nocontainstrList.size()>0){
				for(DynaBean bean:nocontainstrList){
					String name = String.valueOf(bean.get("name"));
					String mobile = String.valueOf(bean.get("mobile"));
					if("".equals(name) || "".equals(mobile)){
						continue;
					}
					clientsMap.put(mobile, name);
				}
			}
		}
		if(deppathList != null && deppathList.size()>0){
			depBuffer = new StringBuffer();
			depBuffer.append("select client.MOBILE,client.NAME from LF_CLIENT client ");
			depBuffer.append(" inner join LF_ClIENT_DEP_SP sp on client.CLIENT_ID = sp.CLIENT_ID ");
			depBuffer.append(" where (");
			for(int a=0;a<deppathList.size();a++){
				String temp = deppathList.get(a);
				if(temp != null && !"".equals(temp)){
					depBuffer.append(" sp.DEP_ID in (").append("select dep.DEP_ID from LF_CLIENT_DEP dep ");
					depBuffer.append(" where dep.DEP_PATH like '").append(temp).append("%')");
				}
				if(a != deppathList.size()-1){
					depBuffer.append(" or ");
				}
			}
			depBuffer.append(")");
			depBuffer.append(this.getBirthdayMsg());
			List<DynaBean> containstrList = getListDynaBeanBySql(depBuffer.toString());
			if(containstrList != null && containstrList.size()>0){
				for(DynaBean bean:containstrList){
					String name = String.valueOf(bean.get("name"));
					String mobile = String.valueOf(bean.get("mobile"));
					if("".equals(name) || "".equals(mobile)){
						continue;
					}
					clientsMap.put(mobile, name);
				}
			}
		}
		if(clientsMap == null || clientsMap.size() == 0 )
		{
			return null;
		}
		return clientsMap;

	}
	
	/**
	 *   获取时间条件
	 * @return
	 */
	public String getBirthdayMsg(){
		StringBuffer sqlBuffer = new StringBuffer();
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)//oracle
		{
			sqlBuffer.append(" and to_char(client.BIRTHDAY,'mm-dd') = to_char(sysdate,'mm-dd') ");
		}
		else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)//sql server
		{
			sqlBuffer.append(" and datepart(mm,client.BIRTHDAY) = datepart(mm,getdate()) ")
					.append("and datepart(dd,client.BIRTHDAY) = datepart(dd,getdate())");
		}
		else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)//db2
		{
			sqlBuffer.append(" and month(client.BIRTHDAY) = month(current date) ")
					.append(" and day(client.BIRTHDAY) = day(current date) ");
		}
		else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)//mysql
		{
			sqlBuffer.append(" and date_format(client.BIRTHDAY,'%m-%d') = date_format(now(),'%m-%d') ");
		}
		return sqlBuffer.toString();
	}
	
	
	/**
	 * 获取企业账户绑定表中发送账号为激活的数据
	 * @param conditionMap 传入查询条件
	 * @param orderbyMap  传入排序条件
	 * @param pageInfo	分页信息
	 * @return 企业账户绑定关系表的集合List<LfSpDepBind>
	 * @throws Exception
	 */
	public List<LfSpDepBind> getSpDepBindWhichUserdataIsOk(LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception {
		//拼接sql
		String sql = "select * from " + TableLfSpDepBind.TABLE_NAME + "  where "
				+ TableLfSpDepBind.SP_USER + " in (select "+TableUserdata.USER_ID+" from " + TableUserdata.TABLE_NAME+StaticValue.getWITHNOLOCK()
				+ "  where " +TableUserdata.ACCOUNTTYPE +" =1 and "+ TableUserdata.STATUS + "=0)";

		Iterator<Map.Entry<String, String>> iter = null;
		//获取实体类与数据库字段的MAP映射
		Map<String, String> columns = getORMMap(LfSpDepBind.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			//获取实体类属性
			Field[] fields = LfSpDepBind.class.getDeclaredFields();
			//属性类型
			String fieldType = null;
			while (iter.hasNext()) {
				e = iter.next();
				for (int index = 0; index < fields.length; index++)
				{
					if (fields[index].getName().equals(e.getKey()))
					{
						fieldType = fields[index].getGenericType()
								.toString();
						break;
					}
				}
				if (!"".equals(e.getValue())) {
					String eKey = e.getKey();
					columnName = eKey.indexOf("&") < 0 ? columns.get(eKey)
							: columns.get(eKey
									.subSequence(0, eKey.indexOf("&")));
					if (eKey.contains("&like")) {
						sql = sql + " and " + columnName + " like '%"
								+ e.getValue() + "%' ";
					} else if (eKey.contains("&>")) {
						sql = sql + " and " + columnName + ">" + e.getValue()
								+ " ";
					} else {
						//是否是日期类型
						boolean isDateType = fieldType
						.equals(StaticValue.TIMESTAMP)
						|| fieldType.equals(StaticValue.DATE_SQL)
						|| fieldType.equals(StaticValue.DATE_UTIL);
						if(fieldType.equals("class java.lang.String") || isDateType){
							sql = sql + " and " + columnName + "='" + e.getValue()
							+ "' ";
						}else{
							sql = sql + " and " + columnName + "=" + e.getValue();
						}
					}

				}
			}
		}
		//排序条件拼接
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			sql += " order by ";
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext()) {
				e = iter.next();
				sql = sql + columns.get(e.getKey()) + " " + e.getValue() + ",";
			}

			sql = sql.substring(0, sql.lastIndexOf(","));
		}

		if (pageInfo == null) {
			//不分页，执行查询，返回结果
			return findEntityListBySQL(LfSpDepBind.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			//记录数sql拼接
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")").toString();
			//分页，执行查询，返回结果
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(LfSpDepBind.class,
							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	
}
