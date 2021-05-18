package com.montnets.emp.rms.rmstask.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.rmstask.vo.DetailMtTaskVo;
import com.montnets.emp.rms.rmstask.vo.LfMttaskVo;
import com.montnets.emp.table.sms.TableMtTask01_12;
import com.montnets.emp.table.sms.TableMtTaskC;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RmsTaskDao extends SuperDAO{
	private IGenericDAO dao = new DataAccessDriver().getGenericDAO();
	public List<LfMttaskVo> findMtTask(LfMttaskVo mtVo, PageInfo pageInfo) {
		List<LfMttaskVo> lfMttaskVos = new ArrayList<LfMttaskVo>();
		try {
			//获取操作员
			LfSysuser lfSysuser = new DataAccessDriver().getEmpDAO().findObjectByID(LfSysuser.class,mtVo.getCurrUserId());
			//查询字段拼接
			String fieldSql = RmsTaskSql.getFieldSql();
			//查询表拼接
			String tableSql = RmsTaskSql.getTableSql();
			//权限控制sql
			String dominationSql ="";
			String dominationSql2="";

			//如果是非10W号则需要进行权限控制
			if(!"100000".equals(mtVo.getCurrCorpCode()) && StringUtils.isEmpty(mtVo.getUserIds()) && StringUtils.isEmpty(mtVo.getDepIds())){
				//如果登录操作员不是管理员，则进行管辖权限控制。因为接入类短信有可能没有操作员ID。
				if(!"admin".equals(lfSysuser.getUserName())){
					//机构权限
					dominationSql = RmsTaskSql.getDominationSql(mtVo.getCurrUserId());
					//个人权限
					dominationSql2 = RmsTaskSql.getDominationSql2(mtVo.getCurrUserId());
				}else{
					//如果登录操作员是管理员，并且是多企业版的话，就加上企业编码的查询条件。
					if(StaticValue.getCORPTYPE() == 1){
						mtVo.setCorpCode(lfSysuser.getCorpCode());
					}
				}
			}

			//根据机构Id组装下级机构
			if(StringUtils.isNotEmpty(mtVo.getDepIds())) {
				//包含子机构
				if(mtVo.getIscontainsSun()){
					LfDep lfDep = new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(mtVo.getDepIds()));
					//如果是一级机构则直接根据企业编码查询
					if(lfDep != null && lfDep.getDepLevel() == 1){
						if(StaticValue.getCORPTYPE() == 1){
							//多企业
							mtVo.setDepIds("");
							mtVo.setCorpCode(lfDep.getCorpCode());
						}else{
							//单企业
							mtVo.setDepIds("");
						}
					}else{
						String depid = new DepDAO().getChildUserDepByParentID(Long.parseLong(mtVo.getDepIds()) ,TableLfDep.DEP_ID);
                        mtVo.setDepIds(depid);
					}
				}else{
                    //不包含子机构
                    mtVo.setDepIds(TableLfDep.DEP_ID + "=" + mtVo.getDepIds());
                }
			}
			//组装过滤条件
			String conditionSql = RmsTaskSql.getConditionSql(mtVo);
			//判断当前操作员的查看权限，如果是100000企业、admin和管辖范围是顶级机构不需要考虑权限
			if(!"100000".equals(mtVo.getCorpCode()) && !"admin".equals(lfSysuser.getUserName())){
				if(lfSysuser.getPermissionType() == 1){
					//个人权限  只能查看自己的数据
					conditionSql += dominationSql2;
				}else if(lfSysuser.getPermissionType() == 2){
					//机构权限
					conditionSql += dominationSql;
				}
			}
			//组装排序条件
			String orderBySql = RmsTaskSql.getOrderBySql();

			String sql = fieldSql + tableSql + conditionSql + orderBySql;
			if(pageInfo == null){
				lfMttaskVos = findVoListBySQL(LfMttaskVo.class,sql,StaticValue.EMP_POOLNAME);
			}else {
				//分页
				String countSql = "select count(*) totalcount from (" + sql + ") A";
				lfMttaskVos = dao.findPageVoListBySQL(LfMttaskVo.class,sql,countSql,pageInfo,StaticValue.EMP_POOLNAME);
			}
			return lfMttaskVos;
		}catch (Exception e){
			EmpExecutionContext.error(e,"企业富信-数据查询-" + mtVo.getMenuName() + ",方法findMtTask执行异常！");
		}
		return lfMttaskVos;
	}

	public List<LfSysuser> findDomUserBySysuserIDOfSmsTaskRecordByDep(String sysuserID,String depId) throws Exception {
		//拼接sql
		StringBuilder domination = new StringBuilder("select ").append(
			TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(" where ").append(
							TableLfDomination.USER_ID).append("=").append(sysuserID);
		StringBuilder dominationSql = new StringBuilder(" where (").append(
			TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
			" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
					domination).append(")) and ").append(TableLfSysuser.USER_ID)
					.append("<>1 and ").append(TableLfSysuser.DEP_ID).append("=").append(depId);
		String sql = "select * from " +
				TableLfSysuser.TABLE_NAME + dominationSql;
		//排序条件拼接
		sql += " order by " + TableLfSysuser.NAME + " asc";
		//返回结果
		return findEntityListBySQL(LfSysuser.class, sql, StaticValue.EMP_POOLNAME);
	}

	/**
	 * 获取任务滞留条数
	 * @param taskids 任务Id集合
	 * @return
	 * @date 2015-4-3 上午10:05:07
	 */
	public Map<Long, Long> getTaskRemains(String taskids){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		//一天前
		calendar.add(Calendar.DATE, -1);
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		String time = genericDao.getTimeCondition(sdf.format(calendar.getTime()));
		String sql = "select "+TableMtTaskC.TASK_ID+",count(*) as total from "+TableMtTaskC.TABLE_NAME+" where "+
	   TableMtTaskC.SEND_STATUS +" = 208 and "+
		TableMtTaskC.TASK_ID+" in ("+taskids+") and "+TableMtTaskC.SEND_TIME+" >= "+time+" group by "+
		TableMtTaskC.TASK_ID;
		List<DynaBean> list = getListDynaBeanBySql(sql);
		if(list == null || list.size() == 0){
			return null;
		}
		Map<Long, Long> map = new HashMap<Long, Long>();
		for(DynaBean bean:list){
			long taskId = Long.valueOf(bean.get("taskid").toString());
			long total = Long.valueOf(bean.get("total").toString());
			map.put(taskId, total);
		}
		return map;
	}

	public List<DetailMtTaskVo> findSendDeatilMt(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
		List<DetailMtTaskVo> detailMtTaskVos = new ArrayList<DetailMtTaskVo>();
		try {
			//查询字段拼接
			String fieldSql = RmsTaskSql.getSendDetailFieldSql();
			//查询表拼接
			String tableSql = RmsTaskSql.getSendDetailTableSql(conditionMap.get("tableName"));
			//组装过滤条件
			String conditionSql = RmsTaskSql.getSendDetailConditionSql(conditionMap);
			//组装SQL语句
			String sql = fieldSql + tableSql + conditionSql;
			//组装统计语句
			String countSql = "select count(*) totalcount" + tableSql + conditionSql;
			//组装排序语句
			sql += " ORDER BY mt." + TableMtTask01_12.SEND_TIME;
			if(pageInfo == null){
				detailMtTaskVos = findVoListBySQL(DetailMtTaskVo.class,sql,StaticValue.EMP_POOLNAME);
			}else {
				//分页
				detailMtTaskVos = dao.findPageVoListBySQL(DetailMtTaskVo.class,sql,countSql,pageInfo,StaticValue.EMP_POOLNAME);
			}
		}catch (Exception e){
			EmpExecutionContext.error(e,"企业富信-群发历史查询-发送详情查看异常,Dao层查询异常！");
		}
		return detailMtTaskVos;
	}
}
