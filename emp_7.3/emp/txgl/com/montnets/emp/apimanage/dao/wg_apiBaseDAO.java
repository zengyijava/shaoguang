package com.montnets.emp.apimanage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;

public class wg_apiBaseDAO extends SuperDAO {

	/**
	 * 个性化接口企业信息
	 * @description    
	 */
	public List<DynaBean> getApiList(LinkedHashMap<String, String> conditionMap,PageInfo pageinfo) throws Exception
	{
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		String sql = "select ent.*,corp.corp_name  corp_name from GW_MULTI_ENTERP ent left join LF_CORP corp on ent.ecid= corp.corp_code ";
		String condition ="";
		//方法类型
        String funtype = conditionMap.get("funtype");
        if(funtype!=null&&!"".equals(funtype))
        {
        	condition += " and  ent.funtype like '%"+funtype+"%'";
        }
        //请求格式
        String reqfmt = conditionMap.get("reqfmt");
        if(reqfmt!=null&&!"".equals(reqfmt))
        {
        	condition += " and  ent.reqfmt = "+reqfmt;
        }
        String respfmt = conditionMap.get("respfmt");
        if(respfmt!=null&&!"".equals(respfmt))
        {
        	condition += " and  ent.respfmt = "+respfmt;
        }
        //开始时间
		if(conditionMap.get("startdate") != null && !"".equals(conditionMap.get("startdate")))
		{
			condition +=" and ent.createtm>= " + genericDao.getTimeCondition(conditionMap.get("startdate"));
		}
		//结束时间
		if(conditionMap.get("enddate") != null && !"".equals(conditionMap.get("enddate")))
		{
			condition +=" and ent.createtm<= " + genericDao.getTimeCondition(conditionMap.get("enddate"));
		}
		//企业编号
		 String etccode = conditionMap.get("etccode");
        if(etccode!=null&&!"".equals(etccode))
        {
        	condition += " and  corp.corp_code like '%"+etccode+"%'";
        }
		//企业名称
		 String etcname = conditionMap.get("etcname");
	        if(etcname!=null&&!"".equals(etcname))
	        {
	        	condition += " and corp.corp_name like '%"+etcname+"%'";
	        }
		sql=sql+condition.replaceFirst("^(\\s*)(?i)and", "$1where");
		
		//记录数sql拼接
		String countSql = new StringBuffer(
				"select count(*) totalcount from (").append(sql)
				.append(")  total").toString();
        return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageinfo, StaticValue.EMP_POOLNAME,null);

		
	}
	/**
	 * 函数列表信息
	 */
	public List<DynaBean> getFunList(LinkedHashMap<String, String> conditionMap,PageInfo pageinfo) throws Exception{
        String ecid = conditionMap.get("ecid");
        String temp="";
        if(ecid!=null&&!"".equals(ecid))
        {
            temp+= " and  ecid = "+ecid;
        }
        
        String funtype = conditionMap.get("funtype");
        if(funtype!=null&&!"".equals(funtype))
        {
            temp+= " and  funtype = '"+funtype+"'";
        }
		//可以默认取第一种类型，以便与查询出不重复的数据
        String sql = "select main.funname,main.cfunname,main.rettype as reqtype,main.funtype , main.cust_intfname, main.status, temp.rettype resptype from GW_BASEPROTOCOL main left join  (select distinct rettype,FUNNAME from GW_BASEPROTOCOL where CMDTYPE=2 "+temp+" )  temp on main.FUNNAME=temp.FUNNAME " +
        		" where  main.cmdtype=1";
        String orderbySql=" order by main.CREATETM desc";
        sql=sql+temp;

        String countSql = new StringBuffer(
                "select count(*) totalcount from (").append(sql)
                .append(")derivedtbl_1").toString();
        sql=sql+orderbySql;

        return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageinfo, StaticValue.EMP_POOLNAME,null);

	}
	
	/**
	 * 获得梦网编码字段
	 */
	public List<DynaBean> getMWCode(String funname,String cmdtype) throws Exception{

        String sql = "select argname from GW_BASEPARA where FUNNAME='"+funname+"' and CMDTYPE='"+cmdtype+"'";
        return getListDynaBeanBySql(sql);

	}
	
	
	
}
