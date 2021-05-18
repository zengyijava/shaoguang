/**
 * 
 */
package com.montnets.emp.appmage.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.appmage.vo.MttaskDetailVo;
import com.montnets.emp.appmage.vo.MttaskSelectVo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;



public class MttaskSelectDAO extends SuperDAO{


	
	
	/**
	 * 发送详情的查询
	 * @param conditionMap
	 * @param orderbyMap
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findMtTaskVoTwoTable(MttaskDetailVo mdvo)
    throws Exception
	{
		//组装SQL语句
	    String sql = getMttaskDetailSql(mdvo);
	  //调用查询方法
	    List<DynaBean> mttaskList = getListDynaBeanBySql(sql);
	   return mttaskList;
	}
	
	
	/**
	 * 发送详情的详情查询
	 * @param conditionMap
	 * @param orderbyMap
	 * @param tableName
	 * @param pageInfo 分页
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findMtTaskVoTwoTable(PageInfo pageInfo,MttaskDetailVo mdvo) 
	throws Exception
	{
		//组装SQL语句
	    String sql = getMttaskDetailSql(mdvo);
	  //组装统计语句
	    String countSql = new StringBuffer("select count(*) totalcount from ("+sql+") mt").toString();
	  //调用查询方法 
	    List<DynaBean> mttaskList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql,pageInfo , StaticValue.EMP_POOLNAME, null);
	    return mttaskList;
	}
	
	
	
	
	
	
	private String getMttaskDetailSql(MttaskDetailVo mdvo)throws Exception{
		StringBuffer sql= new StringBuffer("SELECT ID, MSG_ID, APP_MSG_ID, Taskid, MSG_TYPE, appacount, toUserName, UserId, sendState, Title, content, MSG_XML, MSG_TEXT, PARENT_ID, "
                     +" CORP_CODE, CREATETIME, APPUSERACCOUNT,RPT_STATE,SendMsgType,RecRPTTime FROM LF_APP_MTMSG ");
		
		//是否有where，默认为false即没where
		boolean hasWhere = false;
		//任务ID
		if(mdvo.getTaskid()!=null&&!"".equals(mdvo.getTaskid())){
			hasWhere = addWhereOrAnd(sql, hasWhere);
			sql.append(" Taskid="+mdvo.getTaskid() +" ");
		}
		
		//用户APP账号
		if(mdvo.getAppuseraccount()!=null&&!"".equals(mdvo.getAppuseraccount())){
			hasWhere = addWhereOrAnd(sql, hasWhere);
			sql.append(" TOUSERNAME LIKE '%"+mdvo.getAppuseraccount()+"%' ");
		}
		
		//昵称
		if(mdvo.getAppusername()!=null&&!"".equals(mdvo.getAppusername())){
			hasWhere = addWhereOrAnd(sql, hasWhere);
			sql.append(" TOUSERNAME IN (SELECT APP_CODE FROM LF_APP_MW_CLIENT WHERE NICK_NAME LIKE '%"+mdvo.getAppusername()+"%') ");
		}
		
		//发送状态
		if(mdvo.getSendstate()!=null&&!"".equals(mdvo.getSendstate())){
			if("-2".equals(mdvo.getSendstate())){
				hasWhere = addWhereOrAnd(sql, hasWhere);
				sql.append(" RPT_STATE NOT IN ('0','1') ");
			}else{
				hasWhere = addWhereOrAnd(sql, hasWhere);
				sql.append(" RPT_STATE IN ("+mdvo.getSendstate()+") ");
			}
			
		}
		
		//回执状态
		if(mdvo.getRptstate()!=null&&!"".equals(mdvo.getRptstate())){
			if("-1".equals(mdvo.getRptstate())){
				hasWhere = addWhereOrAnd(sql, hasWhere);
				sql.append(" RPT_STATE NOT IN ('0','1') ");
			}else{
				hasWhere = addWhereOrAnd(sql, hasWhere);
				sql.append(" RPT_STATE='"+mdvo.getRptstate()+"'");
			}
		}
		
		return sql.toString();
	}
	
	

	


	

	
	/**
	 * 查询app发送任务
	 * @param curuserid
	 * @param mtsVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findMttaskSelectVoWithoutDomination(LfSysuser curuser,
			MttaskSelectVo mtsVo, PageInfo pageInfo) throws Exception
	{
		//查询字段拼接
		String sql =getAppMttaskSelectSql(mtsVo,curuser);
		
		String orderBySql = " ORDER BY ID DESC ";
		//组装统计SQL语句
		String countSql = new StringBuffer("select count(*) totalcount from ")
				.append("("+sql+") a").toString();
		sql=sql+orderBySql;
		
		//调用查询语句
		List<DynaBean> mtsVoList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return mtsVoList;
	}

	/**
	 * 获取短信发送任务
	 * @param mtsVo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findMttaskSelectVoWithoutDomination(LfSysuser curuser,MttaskSelectVo mtsVo)throws Exception
	{
		//查询字段拼接
		String sql =getAppMttaskSelectSql(mtsVo,curuser);
		
		String orderBySql = " ORDER BY ID DESC ";
		
		sql=sql+orderBySql;
		
		 //调用查询方法
		List<DynaBean> mtsVoList = getListDynaBeanBySql(sql);

		return mtsVoList;
	}
	
	
	
	private String getAppMttaskSelectSql(MttaskSelectVo mtsVo,LfSysuser curuser)throws Exception{
		StringBuffer sql = 
			new StringBuffer("select ID,TASKID,TITLE,MSG,MSG_URL,MSG_TYPE,APPACOUNT,BIGINTIME,ENDTIME,SENDSTATE,USER_ID,CORP_CODE,SUB_COUNT,SUC_COUNT,FAI_COUNT,READ_COUNT,UNREAD_COUNT FROM LF_APP_MTTASK ");
		
		//是否有where，默认为false即没where
		boolean hasWhere = false;
		
		//操作员
		if(mtsVo.getUserids()!= null && !"".equals(mtsVo.getUserids()))
		{
			hasWhere = addWhereOrAnd(sql, hasWhere);
			sql.append(" USER_ID in ("+mtsVo.getUserids()+") ");
		}else{
			if(mtsVo.getCorpcode()!=null&&"100000".equals(mtsVo.getCorpcode())){
				
			}else{
				if(curuser.getPermissionType()==1){
					hasWhere = addWhereOrAnd(sql, hasWhere);
					sql.append(" USER_ID in ("+curuser.getUserId()+") ");
				}else{
					hasWhere = addWhereOrAnd(sql, hasWhere);
					sql.append(" (USER_ID="+curuser.getUserId()+" OR USER_ID IN (SELECT USER_ID FROM LF_SYSUSER WHERE " +
					" DEP_ID IN (SELECT DEP_ID FROM LF_DOMINATION WHERE USER_ID="+curuser.getUserId()+"))) ");
				}
			}
		}
		
		//发送状态
		//if(mtsVo.getSendstate()!=null&&!"".equals(mtsVo.getSendstate())){  //findbugs
		if(mtsVo.getSendstate()!=null){
			hasWhere = addWhereOrAnd(sql, hasWhere);
			sql.append(" SENDSTATE="+mtsVo.getSendstate()+" ");
		}
		//内容类型
		
		//if(mtsVo.getMsgtype()!= null && !"".equals(mtsVo.getMsgtype()))  //findbugs:
		if(mtsVo.getMsgtype()!= null)
		{
			hasWhere = addWhereOrAnd(sql, hasWhere);
			sql.append(" MSG_TYPE="+mtsVo.getMsgtype()+" ");
		}
		
		//发送主题
		if(mtsVo.getTitle()!= null && !"".equals(mtsVo.getTitle()))
		{
			hasWhere = addWhereOrAnd(sql, hasWhere);
			sql.append(" TITLE LIKE '%"+mtsVo.getTitle()+"%' ");
		}
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		//指定时间段，开始时间
		if (mtsVo.getBigintimestr() != null
				&& !"".equals(mtsVo.getBigintimestr()))
		{
			hasWhere = addWhereOrAnd(sql, hasWhere);
			sql.append(" BIGINTIME>="+genericDao.getTimeCondition(mtsVo.getBigintimestr()));
		}
		//指定时间段，结束时间
		if (mtsVo.getEndtimestr() != null
				&& !"".equals(mtsVo.getEndtimestr()))
		{
			hasWhere = addWhereOrAnd(sql, hasWhere);
			sql.append(" BIGINTIME<="+genericDao.getTimeCondition(mtsVo.getEndtimestr()));
		}
				
		return sql.toString();
	}
	
	

	/**
	 * 获取短信发送任务
	 * 
	 * @param corpcode
	 *        企业编码
	 * @param type
	 *        查询类型 1 发送成功 发送失败数统计查询 2 已读 未读数统计查询
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findCountByTaskIdandSendstate(String taskid)throws Exception
	{
		String fiedsql = " TASKID,RPT_STATE,COUNT(*) ICOUNT ";
		// 查询字段拼接
		String sql = " SELECT " + fiedsql + " FROM LF_APP_MTMSG ";
		if(taskid != null && !"".equals(taskid))
		{
			sql = sql + " WHERE TASKID=" + taskid + " ";
		}
		String groupby = " GROUP BY TASKID,RPT_STATE ";

		sql = sql + groupby;
		// 调用查询方法
		List<DynaBean> mtsVoList = getListDynaBeanBySql(sql);
		return mtsVoList;
	}
	
	
	
	
	public List<LfSysuser> findDomUserBySysuserIDOfSmsTaskRecordByDep(String sysuserID,String depId)
    throws Exception {
		//拼接sql
		StringBuffer domination = new StringBuffer("select ").append(
			TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(" where ").append(
							TableLfDomination.USER_ID).append("=").append(sysuserID);
		StringBuffer dominationSql = new StringBuffer(" where (").append(
			TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
			" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
					domination).append(")) and ").append(TableLfSysuser.USER_ID)
					.append("<>1 and ").append(TableLfSysuser.DEP_ID).append("=").append(depId);
		String sql = new StringBuffer("select * from ").append(
			TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
		//排序条件拼接
		sql += " order by " + TableLfSysuser.NAME + " asc";
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
			StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
	}
	
	
	
	/**
	 * 按情况拼接where或and
	 * @param sql
	 * @param hasWhere false为没where关键字
	 * @return 第一次拼接后将会有where，直接返回true
	 */
	private boolean addWhereOrAnd(StringBuffer sql, boolean hasWhere)
	{
		if(!hasWhere)
		{
			sql.append(" where ");
		}
		else
		{
			sql.append(" and ");
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}

