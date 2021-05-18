/**
 * 
 */
package com.montnets.emp.netnews.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.netnews.vo.VisitDATAvo;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * @author Administrator
 *
 */
public class Wx_netVisitDao extends SuperDAO{
	

	/**
	 * 2013-7-1
	 * @param busid
	 * @param busname
	 * @param lguserid
	 * @param lgcorpcode
	 * @param pageInfo
	 * @return
	 * List<LfWXBASEINFO>
	 */
	public List<LfWXBASEINFO> getVisitDatas(String busid, String busname,
			String lguserid, String lgcorpcode,String begintime,String endtime,PageInfo pageInfo) throws Exception {
		String fieldSql = "select distinct t.* from ";
		String tableSql = " lf_mttask lm "+StaticValue.getWITHNOLOCK()+" left join lf_wx_baseinfo t on lm.TEMP_ID=t.NETID and lm.MS_TYPE=6 left join lf_sysuser u on t.creatid=u.user_id where (u.user_id="
			+lguserid+" or u.dep_id in(select dep_id from lf_Domination d where  d.user_id="+lguserid+"))";
		String orderbySql = " order by t.id desc";
		String andSql=" and t.CORP_CODE = '"+lgcorpcode+"' ";
		if(busid!=null && busid.length()>0)
		{
			andSql=andSql+" and t.id = "+ busid;
		}
		if(busname!=null && busname.length()>0)
		{
			andSql =andSql+ " and t.name like '%"+busname+"%'";
		}
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		//指定时间段，开始时间
		if (begintime != null
				&& !"".equals(begintime))
		{
			andSql =andSql+ " and t.TIMEOUT>="+genericDao.getTimeCondition(begintime)+" ";
		}
		//指定时间段，结束时间
		if (endtime != null
				&& !"".equals(endtime))
		{
			andSql =andSql+ " and t.TIMEOUT<="+genericDao.getTimeCondition(endtime)+" ";
		}
		
		String sql = fieldSql+tableSql+andSql+orderbySql;
		String countSql = new StringBuffer("select count(*) totalcount from (").append(fieldSql+tableSql+andSql+") a").toString();
		return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LfWXBASEINFO.class,sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
	}
	
	
	
	
	
	/**
	 * 2013-7-1
	 * @param busid
	 * @param busname
	 * @param lguserid
	 * @param lgcorpcode
	 * @param pageInfo
	 * @return
	 * List<LfWXBASEINFO>
	 */
	public List<VisitDATAvo> getMtTasks(String busid, String busname,String serachuserid,
			String lguserid, String lgcorpcode,String begintime,String endtime,PageInfo pageInfo) throws Exception {

		String fieldSql = "select lw.ID,lw.NETID,lw.NAME NETNAME,t.MSG,u.NAME,dep.DEP_NAME,t.TIMER_TIME CREATEDATE,lw.TIMEOUT,t.SUB_STATE,t.RE_STATE,t.SENDSTATE,t.TIMER_STATUS,t.MT_ID,t.TASKID from ";
		String tableSql = " LF_MTTASK t "+StaticValue.getWITHNOLOCK()+" left join lf_sysuser u on t.USER_ID=u.user_id left join LF_DEP dep on dep.dep_id=u.dep_id left join lf_wx_baseinfo lw " +
				" on lw.NETID=t.TEMP_ID where t.MS_TYPE=6 and (u.user_id="
			+lguserid+" or u.dep_id in(select dep_id from lf_Domination d where  d.user_id="+lguserid+"))";
		String orderbySql = " order by t.MT_ID desc";
		String andSql="";
		if (StaticValue.getCORPTYPE() == 1 && !"100000".equals(lgcorpcode)) {
			andSql = andSql+" and t.CORP_CODE = '" + lgcorpcode + "' ";
		}
		
		if(serachuserid!=null && serachuserid.length()>0)
		{
			andSql=andSql+" and u.user_id = "+serachuserid+" ";
		}
		
		if(busid!=null && busid.length()>0)
		{
			andSql=andSql+" and lw.id =" + busid;
		}
		

		if(busname!=null && busname.length()>0)
		{
			andSql =andSql+ " and lw.name like '%"+busname+"%'";
		}
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		//指定时间段，开始时间
		if (begintime != null
				&& !"".equals(begintime))
		{
			andSql =andSql+ " and t.TIMER_TIME>="+genericDao.getTimeCondition(begintime)+" ";
		}
		//指定时间段，结束时间
		if (endtime != null
				&& !"".equals(endtime))
		{
			andSql =andSql+ " and t.TIMER_TIME<="+genericDao.getTimeCondition(endtime)+" ";
		}
		
		String countSql = new StringBuffer("select count(*) totalcount from ").append(tableSql+andSql).toString();
		String sql = fieldSql+tableSql+andSql+orderbySql;
		return new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(VisitDATAvo.class,sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
	}
	
	
	
	
	
	
	/**
	 * 2013-7-1
	 * @param busid
	 * @param busname
	 * @param lguserid
	 * @param lgcorpcode
	 * @param pageInfo
	 * @return
	 * List<LfWXBASEINFO>
	 */
	public List<LfWXPAGE> getWxPagesByNetId(String netid,PageInfo pageInfo) throws Exception {
		String fieldSql = "select * from ";
		String tableSql=" LF_WX_PAGE   ";
		String andSql="";
		if(netid!=null&&netid.length()>0){
			andSql=andSql+" where NETID="+netid+" ";
		}
		String sql = fieldSql+tableSql+andSql;
		String countSql = new StringBuffer("select count(*) totalcount from ").append(tableSql+andSql).toString();
		return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LfWXPAGE.class,sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
	}
	
	
	/**
	 * 2013-7-1
	 * @param busid
	 * @param busname
	 * @param lguserid
	 * @param lgcorpcode
	 * @param pageInfo
	 * @return
	 * List<LfWXBASEINFO>
	 */
	public List<LfSysuser> getSysusers(	String lguserid, String lgcorpcode) throws Exception {
		String fieldSql = "select t.* from ";
		String tableSql = " lf_sysuser t where t.USER_STATE=1 and (t.user_id="
			+lguserid+" or t.dep_id in(select dep_id from lf_Domination d where  d.user_id="+lguserid+"))";
		String orderbySql = " order by t.user_id desc";
		String andSql="";
		if(StaticValue.getCORPTYPE() ==1&&!"100000".equals(lgcorpcode)){
			andSql=" and t.CORP_CODE = '"+lgcorpcode+"' ";
		}
		
		String sql = fieldSql+tableSql+andSql+orderbySql;
		return findEntityListBySQL(LfSysuser.class, sql,StaticValue.EMP_POOLNAME);
	}
	

	/** 获得权限
	 * 2013-7-1
	 * @param pageInfo
	 * @param lguserid
	 * @param tableName
	 * @return
	 * List<DynaBean>
	 */
	//获取权限
	public  String getDominationSql(String userId)
	{
		StringBuffer dominationSql = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userId);
		String sql = new StringBuffer(" where (c.").append(
				TableLfSysuser.USER_ID).append("=").append(userId).append(
				" or c.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(dominationSql).append(")")
				.append(")").toString();
		return sql;
	}

	/**
	 * 2013-7-1
	 * @param netid
	 * @param i
	 * @return
	 * int
	 */
	public int getVisitCount(Long netid,String taskid,Long pageid, int i) {
		String sql ="";
		int re=0;
		List<DynaBean> beans;
		if(i==0){//访问次数
			sql = "select count(*) cu from ((select id from lf_wx_visitlog where refid in (select id from lf_wx_page where netid = "+netid+")";
					if(null!=pageid){
						sql=sql+" and refid="+pageid+" ";
					}
					sql=sql+")) t";
		}else if(i==1){//访问人数
			sql = "select count(*) cu from ((select distinct(phone) from lf_wx_visitlog where refid in (select id from lf_wx_page where netid = "+netid+")";
			if(null!=pageid){
				sql=sql+" and refid="+pageid+" ";
			}
			sql=sql+")) t";
		}else if(i==2){//访问成功
			sql = "select count(*) cu from ((select id from lf_wx_visitlog where refid in (select id from lf_wx_page where netid = "+netid+") and visitstatus = 0 ";
			if(null!=pageid){
				sql=sql+" and refid="+pageid+" ";
			}
			sql=sql+")) t";
		}else if(i==3){//访问次数 按批次
			String taskids=taskid!=null&&taskid.length()>0?taskid:"-1"+" ";
			sql = "select count(*) cu from ((select id from lf_wx_visitlog where taskid="+ taskids+
					" and refid in (select id from lf_wx_page where netid = "+netid+") ";
			if(null!=pageid){
				sql=sql+" and refid="+pageid+" ";
			}
			sql=sql+")) t";
		}else if(i==4){ //访问成功 按批次
			String taskids=taskid!=null&&taskid.length()>0?taskid:"-1"+" ";
			sql = "select count(*) cu from ((select id from lf_wx_visitlog where  taskid="+taskids+
					" and  refid in (select id from lf_wx_page where netid = "+netid+") and visitstatus = 0 ";
			if(null!=pageid){
				sql=sql+" and refid="+pageid+" ";
			}
			sql=sql+")) t";
		}else if(i==5){ //访问人数 按批次
			String taskids=taskid!=null&&taskid.length()>0?taskid:"-1"+" ";
			sql = "select count(*) cu from ((select distinct(phone) from lf_wx_visitlog where  taskid="+taskids +
					" and  refid in (select id from lf_wx_page where netid = "+netid+") ";
			if(null!=pageid){
				sql=sql+" and refid="+pageid+" ";
			}
			sql=sql+")) t";
		}else{
			
		}
		beans = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		if(beans!=null &&beans.size()>0){
			if(beans.get(0).get("cu")!=null){
				re = Integer.parseInt(beans.get(0).get("cu").toString());
			}
		}
		return re;
	}

	
	/**
	 * 2013-7-1
	 * @param netid
	 * 查询发送数据
	 * @param i
	 * @return
	 * int
	 */
	public Long getVisitSendCount(Long netid, int i) {
		String sql ="";
		long re=0l;
		List<DynaBean> beans;
		if(i==0){//发送成功数
			sql = "select sum(SUC_COUNT) cu from LF_MTTASK "+StaticValue.getWITHNOLOCK()+" where TEMP_ID = "+netid+" ";
		}else if(i==1){
			sql = "select sum(RNRET) cu from LF_MTTASK "+StaticValue.getWITHNOLOCK()+" where TEMP_ID= "+netid+" ";
		}
		beans = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		if(beans!=null &&beans.size()>0){
			if(beans.get(0).get("cu")!=null){
				re = Long.parseLong(beans.get(0).get("cu").toString());
			}else{
				re=0;
			}
		}
		return re;
	}

	
	
	/**
	 * 2013-7-1
	 * @param netid
	 * @param i
	 * @return
	 * int
	 */
	public int getVisitHistoryCount(Long netid,String taskid, int i) {
		String sql ="";
		int re=0;
		List<DynaBean> beans;
		if(i==0){//访问次数
			sql = "select count(*) cu from ((select id from LF_WX_VISITLOG_history where refid in (select id from lf_wx_page where netid = "+netid+"))) t";
		}else if(i==1){//访问人数
			sql = "select count(*) cu from ((select distinct(phone) from LF_WX_VISITLOG_history where refid in (select id from lf_wx_page where netid = "+netid+"))) t";
		}else if(i==2){//访问成功
			sql = "select count(*) cu from ((select id from LF_WX_VISITLOG_history where refid in (select id from lf_wx_page where netid = "+netid+") and visitstatus = 0 )) t";
		}else if(i==3){//访问次数
			String taskids=taskid!=null&&taskid.length()>0?taskid:"-1"+" ";
			sql = "select count(*) cu from ((select id from LF_WX_VISITLOG_history where taskid="+taskids+" and  refid in (select id from lf_wx_page where netid = "+netid+"))) t";
		}else if(i==4){//访问成功
			String taskids=taskid!=null&&taskid.length()>0?taskid:"-1"+" ";
			sql = "select count(*) cu from ((select id from LF_WX_VISITLOG_history where taskid="+taskids+" and  refid in (select id from lf_wx_page where netid = "+netid+") and visitstatus = 0 )) t";
		}else if(i==5){//访问人数
			String taskids=taskid!=null&&taskid.length()>0?taskid:"-1"+" ";
			sql = "select count(*) cu from ((select distinct(phone) from LF_WX_VISITLOG_history where taskid="+taskids+" and  refid in (select id from lf_wx_page where netid = "+netid+"))) t";
		}
		beans = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		if(beans!=null &&beans.size()>0){
			if(beans.get(0).get("cu")!=null){
				re = Integer.parseInt(beans.get(0).get("cu").toString());
			}
		}
		return re;
	}

	/**
	 * 2013-7-2
	 * @param netid
	 * @return
	 * List<DynaBean>
	 */
	public List<DynaBean> getVisittrustBean(String netid,String name,String phone,String pagename,String begintime,String endtime,PageInfo pageInfo,String taskid) {
		
		String findsql = "select a.name pagename,b.netid,b.name netname,c.phone,c.visitdate  from";
		String tablesqlString = " LF_WX_PAGE a right JOIN LF_WX_BASEINFO b on a.netid=b.netid right JOIN LF_WX_VISITLOG c on c.refid=a.id " +
				" WHERE a.netid = " +netid ;
		String andSql="";
		if(name!=null && name.length()>0)
		{
			//andSql=andSql+" and ls.name like '%"+name+"%' ";
			andSql =andSql+ " and (exists (select employee.MOBILE from LF_EMPLOYEE employee where employee.NAME like '%"
			+name+"%' and employee.MOBILE=c.phone) or (exists (select client.MOBILE from LF_CLIENT client where client.NAME like '%" 
			+name+"%' and client.MOBILE=c.phone))) ";
		}
		
		if(taskid!=null && taskid.length()>0)
		{
			andSql=andSql+" and c.taskid="+taskid+" ";
		}
		
		
		if(phone!=null && phone.length()>0)
		{
			andSql=andSql+" and c.phone like '%"+phone+"%' ";
		}
		
		if(pagename!=null && pagename.length()>0)
		{
			andSql=andSql+" and a.name like '%"+pagename+"%' ";
		}
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		//指定时间段，开始时间
		if (begintime != null
				&& !"".equals(begintime))
		{
			andSql =andSql+ " and c.visitdate>="+genericDao.getTimeCondition(begintime)+" ";
		}
		//指定时间段，结束时间
		if (endtime != null
				&& !"".equals(endtime))
		{
			andSql =andSql+ " and c.visitdate<="+genericDao.getTimeCondition(endtime)+" ";
		}
		
		String ordersql = " ORDER BY c.visitdate desc";
		String sql = findsql + tablesqlString +andSql+ ordersql;
		String countSql ="select count(*) totalcount from "+tablesqlString +andSql;
		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	}
}
