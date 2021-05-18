package com.montnets.emp.wymanage.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.authen.atom.AuthenAtom;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wymanage.vo.GateManageVo;

/**
 * 
 * @project p_wygl
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-3-25 上午11:23:43
 * @description
 */
public class GenericGateManageVoDAO extends SuperDAO{

	
	/**
	 * 获取网优通道账号信息
	 * @param gatemanagevo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findGateManagesByVo(GateManageVo gatemanagevo, PageInfo pageInfo) throws Exception {
		String sql = this.getGateManageSql(gatemanagevo);
		//总条数语句
		String countSql = "select count(*) totalcount FROM (";
		countSql += sql;
		countSql += " ) A";
		//查询结果集
		List<DynaBean> returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return returnList;
	}
	
	
	
	/**
	 * 获取SIM卡信息列表
	 * @param gatemanagevo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findSimsByVo(GateManageVo gatemanagevo) throws Exception {
		String sql = this.getSimSql(gatemanagevo);
		//查询结果集
		List<DynaBean> returnList =getListDynaBeanBySql(sql);
		return returnList;
	}

	
	/**
	 * 获取Sim卡信息查询语句
	 * @param gatemanagvo
	 * @return
	 */
	public String getSimSql(GateManageVo gatemanagvo){
		String fieldMonthY=" MONTHLIMIT,HOURLIMIT,PHONENO,DESCRIPTION,DAYLIMIT,SIMNO,MOBILEAREA,ID,CREATETIME,GATEID,UNICOM ";
		StringBuffer busreportsql=new StringBuffer(" select ");
		busreportsql.append(fieldMonthY);
		busreportsql.append(" FROM A_SIMINFO ASIM ");
		StringBuffer conditionSql = new StringBuffer();
		AuthenAtom atom=new AuthenAtom();
		//SIM卡号
		if (gatemanagvo.getPhoneno() != null	&& !"".equals(gatemanagvo.getPhoneno())&&atom.holesProcessing(gatemanagvo.getPhoneno())) {
			conditionSql.append(" AND ASIM.PHONENO like '%"+gatemanagvo.getPhoneno().trim()+"%' ");
		}
		
		// 运营商
		if(gatemanagvo.getUnicom() != null && !"".equals(gatemanagvo.getUnicom())&&atom.holesProcessing(gatemanagvo.getUnicom()))
		{
			conditionSql.append(" AND ASIM.UNICOM = "+gatemanagvo.getUnicom().trim()+" ");
		}
		
		//通道ID
		if(gatemanagvo.getGateid() != null && !"".equals(gatemanagvo.getGateid()))
		{
			conditionSql.append(" AND ASIM.GATEID = "+gatemanagvo.getGateid().trim()+" ");
		}
				    
		//总的条件语句
		String sql = busreportsql.toString()+ conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		
	    return sql;
	}
	
	
	
	/**
	 * 获取网优通道账户查询语句
	 * @param gatemanagvo
	 * @return
	 */
	public String getGateManageSql(GateManageVo gatemanagvo){
		String fieldMonthY=" ID,GATENAME,IP,PORT,PTIP,PTPORT,GATEID,CORPSIGN,COMMON,CREATETIME ";
		StringBuffer busreportsql=new StringBuffer(" select ");
		busreportsql.append(fieldMonthY);
		busreportsql.append(" FROM A_IPCOMINFO AIPCOM ");
		StringBuffer conditionSql = new StringBuffer();
		AuthenAtom atom=new AuthenAtom();
		//通道名称
		if (gatemanagvo.getGatename() != null	&& !"".equals(gatemanagvo.getGatename())&&atom.holesProcessing(gatemanagvo.getGatename())) {
			conditionSql.append(" AND AIPCOM.GATENAME like '%"+gatemanagvo.getGatename().trim()+"%' ");
		}
		
		// IP
		if(gatemanagvo.getIp() != null && !"".equals(gatemanagvo.getIp())&&holesProcessing(gatemanagvo.getIp()))
		{
			conditionSql.append(" AND AIPCOM.IP LIKE '%"+gatemanagvo.getIp().trim()+"%' ");
		}
		
		// 端口号
		if(gatemanagvo.getPort() != null && !"".equals(gatemanagvo.getPort())&&atom.holesProcessing(gatemanagvo.getPort()))
		{
			conditionSql.append(" AND AIPCOM.PORT = "+gatemanagvo.getPort().trim()+" ");
		}

		// 通道号ID字符串
		if(gatemanagvo.getGateids() != null && !"".equals(gatemanagvo.getGateids()))
		{
			conditionSql.append(" AND AIPCOM.GATEID in ("+gatemanagvo.getGateids()+") ");
		}
	    
		//总的条件语句
		String sql = busreportsql.toString() + conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		
	    return sql;
	}
	

	
	/**
	 * 验证请求中是否存在漏洞
	 * @param request
	 * @return 是否可以继续请求
	 */
	public boolean holesProcessing(String request){
		boolean isContinue=true;
		if(request==null||"".equals(request)){
			return isContinue;
		}
		String signal=";,',\",--,(,),";
		String[] singalArr=signal.split(",");
		for(int i=0;i<singalArr.length;i++){
			if(request.indexOf(singalArr[i])>-1){
				isContinue=false;
				return isContinue;
			}
		}
		return isContinue;
	}
	
	
}
