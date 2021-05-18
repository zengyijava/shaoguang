package com.montnets.emp.inbox.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;

public class ReciveBoxDao extends SuperDAO {
	
	/**
	 * 获取人员表中的号码和名称
	 * @param mobiles
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findLfEmployeesByMobiles(String mobiles,String lgcorpcode)
	{
		try
		{
			//sql语句
			String esql="";
			//号码条件
			String insqlstr="";
			//判断号个数
			String[] arraymobile=mobiles.split(",");
			//号码个数小于5万则使用拆分in查询
			if(arraymobile.length<40000){
				insqlstr=new StringBuffer(" WHERE (").append(getSqlStr(mobiles, "MOBILE")).append(") ").toString();
			}else{
				//大于一万则不加此条件
				insqlstr="";
			}
			//是否十万企业
			if(!"100000".equals(lgcorpcode)){
				//普通企业
				//esql="select MOBILE,NAME from lf_employee where MOBILE in ("+mobiles+") and corp_code='"+lgcorpcode+"'";
				esql="select MOBILE,NAME from lf_employee "+StaticValue.getWITHNOLOCK()+" "+insqlstr;
				if(!"".equals(insqlstr)){
					esql=esql+" AND corp_code='"+lgcorpcode+"'";
				}else{
					esql=esql+" WHERE corp_code='"+lgcorpcode+"'";
				}
				
			}else{
				//10万号企业
				//esql="select MOBILE,NAME from lf_employee where MOBILE in ("+mobiles+")";
				esql="select MOBILE,NAME from lf_employee "+StaticValue.getWITHNOLOCK()+" "+insqlstr+" ";
			}
			
			return new DataAccessDriver().getGenericDAO().findDynaBeanBySql(esql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据手机号查询员工姓名，异常。");
			return new ArrayList<DynaBean>();
		}
	}

	
	/**
	 * 获取客户表中的号码和名称
	 * @param mobiles
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findLfClientsByMobiles(String mobiles,String lgcorpcode)
	{	
		try
		{
			String csql="";
			//号码条件
			String insqlstr="";
			//判断号个数
			String[] arraymobile=mobiles.split(",");
			//号码个数小于5万则使用拆分in查询
			if(arraymobile.length<40000){
				insqlstr=new StringBuffer(" WHERE (").append(getSqlStr(mobiles, "MOBILE")).append(") ").toString();
			}else{
				//大于一万则不加此条件
				insqlstr="";
			}
			if(!"100000".equals(lgcorpcode)){
				//普通企业
				//csql="select MOBILE,NAME from LF_CLIENT where MOBILE in ("+mobiles+") and corp_code='"+lgcorpcode+"'";
				csql="select MOBILE,NAME from LF_CLIENT "+StaticValue.getWITHNOLOCK()+" "+insqlstr ;
				if(!"".equals(insqlstr)){
					csql=csql+" AND corp_code='"+lgcorpcode+"'";
				}else{
					csql=csql+" WHERE corp_code='"+lgcorpcode+"'";
				}
			}else{
				//10万号企业
				//csql="select MOBILE,NAME from LF_CLIENT where MOBILE in ("+mobiles+")";
				csql="select MOBILE,NAME from LF_CLIENT  "+StaticValue.getWITHNOLOCK()+" "+insqlstr+" ";
			}
			return new DataAccessDriver().getGenericDAO().findDynaBeanBySql(csql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据手机号查询客户姓名，异常。");
			return new ArrayList<DynaBean>();
		}
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
	
	
	
	/**
	 * 处理SQL条件,不允许使用1 =1方式
	 * @param conSql 条件
	 * @return 处理后的条件
	 */
	public String getConditionSql(String conSql)
	{
		String conditionSql = "";
		try {
			//存在查询条件
			if(conSql != null && conSql.length() > 0)
			{
				//将条件字符串首个and替换为where,不允许1 =1方式
				conditionSql = conSql.replaceFirst("^(\\s*)(?i)and", "$1where");
			}
			return conditionSql;
		} catch (Exception e) {
			EmpExecutionContext.error("处理SQL条件异常，conSql:" + conSql);
			return null;
		}
	}

	
	

}
