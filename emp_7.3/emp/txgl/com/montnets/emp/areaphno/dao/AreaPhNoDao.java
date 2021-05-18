package com.montnets.emp.areaphno.dao;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.segnumber.PbServicetype;
import com.montnets.emp.servmodule.txgl.vo.AreaPhNoVo;
import com.montnets.emp.util.PageInfo;

public class AreaPhNoDao extends SuperDAO {
	BaseBiz baseBiz = new BaseBiz();
	public List<AreaPhNoVo> getAreaPhNoList(LinkedHashMap<String, String> conditionMap,PageInfo pageinfo) throws Exception{
		
		List<PbServicetype> pbstList = baseBiz.getEntityList(PbServicetype.class);
		//移动
		String ydServepro="";
		//联通
		String ltServepro="";
		//电信
		String dxServepro="";
		if(pbstList!=null && pbstList.size()>0){
			int len = pbstList.size();
			for(int i = 0 ; i < len ; i++){
				PbServicetype pbServicetype = pbstList.get(i);
				if(pbServicetype.getSpisuncm()==0){
					ydServepro = pbServicetype.getServiceno();
				}
				if(pbServicetype.getSpisuncm()==1){
					ltServepro = pbServicetype.getServiceno();
				}
				if(pbServicetype.getSpisuncm()==21){
					dxServepro = pbServicetype.getServiceno();
				}
			}
		}
		String sql="";
		if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
			sql="select m.MOBILE mobile,p.PROVINCECODE areacode,p.PROVINCE province, p.city city, m.ID id," +
					" (case when SUBSTRING(cast(mobile as varchar(20)),1,3) in ("+ydServepro+") then '移动'"
					+" when SUBSTRING(cast(mobile as varchar(20)),1,4) in ("+ydServepro+") then '移动'"
					+" when SUBSTRING(cast(mobile as varchar(20)),1,3) in("+ltServepro+") then '联通'"
					+" when SUBSTRING(cast(mobile as varchar(20)),1,4) in("+ltServepro+") then '联通'"
					+" when SUBSTRING(cast(mobile as varchar(20)),1,3) in("+dxServepro+")then '电信'"
					+" when SUBSTRING(cast(mobile as varchar(20)),1,4) in("+dxServepro+")then '电信'"
					+" else '未知' end) servepro from a_mobilearea m , a_provincecity p"
					+" where m.AREACODE=p.AREACODE";
		}else{
			sql="select m.MOBILE mobile,p.PROVINCECODE areacode,p.PROVINCE province, p.city city, m.ID id," +
					" (case when substr(m.MOBILE,1,3) in ("+ydServepro+") then '移动'"
					+" when substr(m.MOBILE,1,4) in ("+ydServepro+") then '移动'"
					+" when substr(m.MOBILE,1,3) in("+ltServepro+") then '联通'"
					+" when substr(m.MOBILE,1,4) in("+ltServepro+") then '联通'"
					+" when substr(m.MOBILE,1,3) in("+dxServepro+")then '电信'"
					+" when substr(m.MOBILE,1,4) in("+dxServepro+")then '电信'"
					+" else '未知' end) servepro from a_mobilearea m , a_provincecity p"
					+" where m.AREACODE=p.AREACODE";
		}
		String provinceCode=conditionMap.get("provinceCode");
		String province=conditionMap.get("province");
		String mobile=conditionMap.get("mobile");
		String servePro=conditionMap.get("servePro");
		String city=conditionMap.get("city");
		String tempSql=null;
		if(provinceCode!=null&&!"".equals(provinceCode))
		{
			sql=sql+" and p.PROVINCECODE='"+provinceCode+"'";
		}
		if(province!=null&&!"".equals(province))
		{
			sql=sql+" and p.province='"+province+"'";
		}
		if(mobile!=null&&!"".equals(mobile))
		{
			sql=sql+" and m.MOBILE='"+mobile+"'";
		}
		if(city!=null&&!"".equals(city))
		{
			sql=sql+" and p.city='"+city+"'";
		}
		if(servePro!=null&&!"".equals(servePro))
		{	
			tempSql="SELECT * FROM("+sql+")  B WHERE SERVEPRO='"+servePro+"'";
		}
		if (pageinfo == null) {
			return findEntityListBySQL(AreaPhNoVo.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(") A").toString();
			if(tempSql!=null&&!tempSql.equals("") && StaticValue.DBTYPE != StaticValue.SQLSERVER_DBTYPE){
				sql=tempSql;
				countSql = new StringBuffer(
						"select count(*) totalcount from (").append(sql)
						.append(") A").toString();
			}
			List<AreaPhNoVo> areaVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(AreaPhNoVo.class,
					sql, countSql, pageinfo, StaticValue.EMP_POOLNAME);
			if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE && servePro!=null&&!"".equals(servePro)){
				List<AreaPhNoVo> findAreaVoList = new LinkedList<AreaPhNoVo>(); 
				for(AreaPhNoVo areavo:areaVoList){
					if(servePro.equals(areavo.getServePro())){
						findAreaVoList.add(areavo);
					}
				}
				areaVoList = findAreaVoList;
			}
			return areaVoList;
		}
	}
	
}
