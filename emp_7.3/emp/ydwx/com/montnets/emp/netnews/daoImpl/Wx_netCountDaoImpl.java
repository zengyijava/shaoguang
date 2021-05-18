package com.montnets.emp.netnews.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.netnews.entity.LfWXData;
import com.montnets.emp.netnews.vo.TRUSTDATAvo;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;


public class Wx_netCountDaoImpl extends BaseBiz
{
	/***
	 * 获得回复次数
	 */
	public int getReplyCount(String tableName,String colname,String colnamevalue, int optype,Long taskid) throws Exception {
		int num = 0;
		if(tableName==null||"".equals(tableName)||colname==null||"".equals(colname)){
			return num;
		
		}
		Connection con = empTransDao.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			//回复次数
			if(optype==0)
			{
				String hfsql="select count(*) from "+tableName+ " where "+colname+" is not null ";
				if(taskid!=null){
					hfsql=hfsql+" and taskid="+taskid;
				}
				ps = con.prepareStatement(hfsql);
			}else if(optype==1) {
				//回复人数
				String hfsql="select count(distinct(phone)) from "+tableName+ " where "+colname+" is not null ";
				if(taskid!=null){
					hfsql=hfsql+" and taskid="+taskid;
				}
				
				ps = con.prepareStatement(hfsql);
			}else if(optype==2) {
				//当前值回复次数
				String sql="select count(*) from "+tableName+ " where "+colname+" is not null ";
				if(colnamevalue!=null&&colnamevalue.length()>0){
					sql=sql+" and "+colname+"='"+colnamevalue+"' ";
				}
				
				if(taskid!=null){
					sql=sql+" and taskid="+taskid;
				}
				
				ps = con.prepareStatement(sql);
			}else if(optype==3) {
				//当前值回复人数
				String sql="select count(distinct(phone)) from "+tableName+ " where "+colname+" is not null ";
				if(colnamevalue!=null&&colnamevalue.length()>0){
					sql=sql+" and "+colname+"='"+colnamevalue+"' ";
				}
				
				if(taskid!=null){
					sql=sql+" and taskid="+taskid;
				}
				
				ps = con.prepareStatement(sql);
			}
			if(ps!=null){
			ps.executeQuery();
			rs = (ResultSet) ps.getResultSet();
			while (rs.next()) {
				num = rs.getInt(1);
			}
			}
		} catch (Exception e) 
		{
			EmpExecutionContext.error(e,"回复次数");
		} finally
		{
			if(rs!=null){
			rs.close();
			}
			if(ps!=null){
			ps.close();
			}
			empTransDao.closeConnection(con);
		}
		return num;
	}
	
	/***
	 * 通过条件查询动态表的相关信息
	 */
	public List<DynaBean> getReplyPageCountList(PageInfo pageInfo,String tablename,String column,String phone,String name,String serchcolname,String begintime,String endtime,String taskid) throws SQLException {
		List<DynaBean> beans = new ArrayList<DynaBean>();
		if(tablename==null||"".equals(tablename)||column==null||"".equals(column)){
			return beans;
		}
		String fieldSql ="select b."+column+",b.phone,b.DATE_TIME ";
		String tableSql =" from "+tablename+" b where  b."+column+" is not null ";
		
		String andSql="";
		//任务id
		if(taskid!=null&& taskid.length()>0)
		{
			andSql=andSql+ " and b.taskid="+taskid;
		}
		//电话号码
		if(phone!=null && phone.length()>0)
		{
			andSql=andSql+ " and b.phone like '%"+phone+"%' ";
		}
		
		
		
		//姓名
		if(name!=null && name.length()>0)
		{
			andSql =andSql+ " and (exists (select employee.MOBILE from LF_EMPLOYEE employee where employee.NAME like '%"
			+name+"%' and employee.MOBILE=b.phone) or (exists (select client.MOBILE from LF_CLIENT client where client.NAME like '%" 
			+name+"%' and client.MOBILE=b.phone))) ";

		}
		//回复内容
		if(serchcolname!=null && serchcolname.length()>0)
		{
			andSql=andSql+ " and b."+column+" like '%"+serchcolname+"%' ";
		}
				
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		//指定时间段，开始时间
		if (begintime != null
				&& !"".equals(begintime))
		{
			andSql =andSql+ " and b.DATE_TIME>="+genericDao.getTimeCondition(begintime)+" ";
		}
		//指定时间段，结束时间
		if (endtime != null
				&& !"".equals(endtime))
		{
			andSql =andSql+ " and b.DATE_TIME<="+genericDao.getTimeCondition(endtime)+" ";
		}
		
		String sql=fieldSql+tableSql+andSql;
		String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql+andSql).toString();
		beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return beans;
	}

	/***
	 * 通过电话号码查询动态表名的相关信息
	 */
	public String getHfCountList(String tablename,String column,String phone) throws SQLException {
		List<DynaBean> beans = null;
		if(tablename==null||"".equals(tablename)||column==null||"".equals(column)){
			return "";
		}
		String fieldSql ="select b."+column+" ";
		String tableSql =" from "+tablename+" b   ";
		String andSql="";
		//电话号码
		if(phone!=null && phone.length()>0)
		{
			andSql=" where b.phone='"+phone+"' ";
		}	
		
		String sql=fieldSql+tableSql+andSql;
		beans = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		StringBuffer result=new StringBuffer("");
		for (DynaBean dynaBean : beans) {
			if(dynaBean.get(column.toLowerCase())!=null){
				result.append(dynaBean.get(column.toLowerCase()).toString()).append(",");
			}
		}
		result.deleteCharAt(result.lastIndexOf(","));
		return result.toString();
	}
	

	
	
		
	
	
	
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
	 * 2013-8-1
	 * 获取互动类型
	 * @param busid
	 * @param busname
	 * @param lguserid
	 * @param lgcorpcode
	 * @param pageInfo
	 * @return
	 * List<LfWXBASEINFO>
	 */
	public List<DynaBean> getWxDatatype(String lgcorpcode) throws Exception {
		String fieldSql = "select t.ID ID,t.NAME NAME from ";
		String tableSql = " LF_WX_DATATYPE t   ";
		String andSql="";
		if(StaticValue.getCORPTYPE() ==1&&!"100000".equals(lgcorpcode)){
			andSql=" where  t.CORP_CODE = '"+lgcorpcode+"' ";
		}
		String sql = fieldSql+tableSql+andSql;
		return new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
	}
	
	
	/**
	 * 获取人员表中的号码和名称
	 * @param mobiles
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findLfEmployeesByMobiles(String mobiles)
			throws Exception {		
		
		String esql="select MOBILE,NAME from lf_employee where MOBILE in ("+mobiles+")";
		return new DataAccessDriver().getGenericDAO().findDynaBeanBySql(esql);
	}

	
	/**
	 * 获取客户表中的号码和名称
	 * @param mobiles
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findLfClientsByMobiles(String mobiles)
			throws Exception {		
		
		String csql="select MOBILE,NAME from LF_CLIENT where MOBILE in ("+mobiles+")";
		return new DataAccessDriver().getGenericDAO().findDynaBeanBySql(csql);
	}
	
	
	/***
	 * 通过条件查询网讯相关信息
	 */
	/**
	 * 获取人员表中的号码和名称
	 * @param mobiles
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findLfEmployeesByMobiles_V1(String mobiles,String lgcorpcode)
			throws Exception {		
		
		//sql语句
		String esql="";
		//号码条件
		String insqlstr="";
		//判断号个数
		String[] arraymobile=mobiles.split(",");
		//号码个数小于5万则使用拆分in查询
		if(arraymobile.length<50000){
			insqlstr=new StringBuffer(" WHERE (").append(getSqlStr(mobiles, "MOBILE")).append(") ").toString();
		}else{
			//大于一万则不加此条件
			insqlstr="";
		}
		//是否十万企业
		if(!"100000".equals(lgcorpcode)){
			//普通企业
			esql="select MOBILE,NAME from lf_employee "+insqlstr;
			if(!"".equals(insqlstr)){
				esql=esql+" AND corp_code='"+lgcorpcode+"'";
			}else{
				esql=esql+" WHERE corp_code='"+lgcorpcode+"'";
			}
			
		}else{
			//10万号企业
			esql="select MOBILE,NAME from lf_employee "+insqlstr+" ";
		}
		
		return new DataAccessDriver().getGenericDAO().findDynaBeanBySql(esql);
	}

	
	/**
	 * 获取客户表中的号码和名称
	 * @param mobiles
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findLfClientsByMobiles_V1(String mobiles,String lgcorpcode)
			throws Exception {		
		
		String csql="";
		//号码条件
		String insqlstr="";
		//判断号个数
		String[] arraymobile=mobiles.split(",");
		//号码个数小于5万则使用拆分in查询
		if(arraymobile.length<50000){
			insqlstr=new StringBuffer(" WHERE (").append(getSqlStr(mobiles, "MOBILE")).append(") ").toString();
		}else{
			//大于一万则不加此条件
			insqlstr="";
		}
		if(!"100000".equals(lgcorpcode)){
			//普通企业
			csql="select MOBILE,NAME from LF_CLIENT "+insqlstr ;
			if(!"".equals(insqlstr)){
				csql=csql+" AND corp_code='"+lgcorpcode+"'";
			}else{
				csql=csql+" WHERE corp_code='"+lgcorpcode+"'";
			}
		}else{
			//10万号企业
			csql="select MOBILE,NAME from LF_CLIENT where ("+insqlstr+")";
		}
		return new DataAccessDriver().getGenericDAO().findDynaBeanBySql(csql);
	}
	
	
	/**
	 * sql in查询  拆分方法
	 * @param idstr
	 * @param columnstr
	 * @return
	 */
	public String getSqlStr(String idstr,String columnstr){
		String sql=" 1=2 ";
		if(idstr!=null&&!"".equals(idstr)&&columnstr!=null&&!"".equals(columnstr)){
			if(idstr.contains(",")){
				String[] useriday=idstr.split(",");
				if(useriday.length<900){
					sql=" "+columnstr+" IN ("+idstr+") ";
				}else{
					String zidstr="";
					sql="";
					for(int i=0;i<useriday.length;i++){
						if((i+1)%900==0){
							zidstr=zidstr+useriday[i];
							sql=sql+" "+columnstr +" IN ("+zidstr+") OR ";
							zidstr="";
							
						}else{
							zidstr=zidstr+useriday[i]+",";
						}
					}
					if(!"".equals(sql)&&"".equals(zidstr)){
						sql=sql.substring(0,sql.lastIndexOf("OR"));
					}else if(!"".equals(sql)&&!"".equals(zidstr)){
						zidstr=zidstr.substring(0, zidstr.length()-1);
						sql=sql+" "+columnstr +" IN ("+zidstr+") ";
					}else{
						sql=" 1=2 ";
					}
				}
			}else{
				sql=" "+columnstr+" = "+idstr;
			}
		}
		return sql;
	}
	
	public List<TRUSTDATAvo> gethuMttaskes(String busid,String busname ,String title,String hudtype,String hudcode,String hudname,String serchuserid,String begintime,String endtime,String lguserid,String lgcorpcode,PageInfo pageInfo) throws Exception
	{

		String fieldSql = "select u.NAME UNAME,dep.dep_name,lm.mt_id,lm.title,t.netid,t.name WNAME,lm.msg,lwd.did,lwd.code,lwd.name WDNAME,lwdt.name WDTNAME,lm.TIMER_TIME,(lm.SUC_COUNT+lm.RNRET) SPCOUNT," +
				"t.DATA_TABLENAME,lwd.COLNAME,lm.SUB_STATE,lm.RE_STATE,lm.SENDSTATE,lm.TIMER_STATUS,lm.taskid from ";
		String tableSql = " lf_mttask lm "+StaticValue.getWITHNOLOCK()+" inner join lf_wx_baseinfo t on lm.TEMP_ID=t.NETID and lm.MS_TYPE=6 " +
				"left join LF_WX_DATA_BIND lwdb on lwdb.NETID=t.NETID left join LF_WX_DATA lwd on lwd.DID=lwdb.DID left join LF_WX_DATATYPE lwdt on lwdt.id=lwd.DATATYPEID " +
				"left join lf_sysuser u "+StaticValue.getWITHNOLOCK()+" on lm.user_id=u.user_id left join lf_dep dep on dep.dep_id=u.dep_id where (u.user_id="+lguserid+" or u.dep_id in(select dep_id from lf_Domination d where  d.user_id="+lguserid+"))";
		String orderbySql = " order by lm.TIMER_TIME desc";
		String andSql=" and t.CORP_CODE = '"+lgcorpcode+"' ";
		//网讯编号
		if(busid!=null && busid.length()>0)
		{
			andSql=andSql+ " and t.NETID =" + busid;
		}
		//网讯名称
		if(busname!=null && busname.length()>0)
		{
			andSql =andSql+ " and t.name like '%"+busname+"%'";
		}
		//网讯主题
		if(title!=null && title.length()>0)
		{
			andSql =andSql+ " and lm.TITLE like '%"+title+"%' ";
		}
		
		//互动类型
		if(hudtype!=null && hudtype.length()>0)
		{
			andSql =andSql+ " and lwd.DATATYPEID= "+hudtype+" ";
		}
		
		//互动编号
		if(hudcode!=null && hudcode.length()>0)
		{
			andSql =andSql+ " and lwd.CODE like '%"+hudcode+"%' ";
		}
		
		//互动名称
		if(hudname!=null && hudname.length()>0)
		{
			andSql =andSql+ " and lwd.NAME like '%"+hudname+"%' ";
		}
		
		//操作员id
		if(serchuserid!=null && serchuserid.length()>0)
		{
			andSql =andSql+ " and u.user_id= "+serchuserid+" ";
		}
		
		
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		//指定时间段，开始时间
		if (begintime != null
				&& !"".equals(begintime.trim()))
		{
			andSql =andSql+ " and lm.TIMER_TIME>="+genericDao.getTimeCondition(begintime)+" ";
		}
		//指定时间段，结束时间
		if (endtime != null
				&& !"".equals(endtime.trim()))
		{
			andSql =andSql+ " and lm.TIMER_TIME<="+genericDao.getTimeCondition(endtime)+" ";
		}
		
		String countSql = new StringBuffer("select count(*) totalcount from ").append(tableSql+andSql).toString();
		String sql = fieldSql+tableSql+andSql+orderbySql;
		return new DataAccessDriver().getGenericDAO().findPageVoListBySQL(TRUSTDATAvo.class,sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
	}
	
	/**
	 * 通过id查询数据库信息
	 */
	public List<LfWXData> getWXDatas(String netid) throws Exception
	{
		String fieldSql = "select lwd.* from ";
		String tableSql = " LF_WX_DATA lwd left join  LF_WX_DATA_BIND lwdb on lwd.DID=lwdb.DID   ";
		String andSql="";
		//网讯id
		if(netid!=null && netid.length()>0)
		{
			andSql=andSql+ " where  lwdb.NETID= "+netid+" ";
		}
		String sql = fieldSql+tableSql+andSql;
		return new SuperDAO().findEntityListBySQL(LfWXData.class,sql,StaticValue.EMP_POOLNAME);
	}
	
}
