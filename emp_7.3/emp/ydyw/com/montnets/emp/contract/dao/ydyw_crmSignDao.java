/**
 * Program  : ydyw_crmSignDao.java
 * Author   : zousy
 * Create   : 2015-1-5 下午02:53:23
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.contract.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.client.TableLfClientDepSp;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.ydyw.TableLfBusTaoCan;
import com.montnets.emp.table.ydyw.TableLfContract;
import com.montnets.emp.table.ydyw.TableLfContractTaocan;
import com.montnets.emp.table.ydyw.TableLfDepTaocan;
import com.montnets.emp.table.ydyw.TableLfProCharges;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2015-1-5 下午02:53:23
 */
public class ydyw_crmSignDao extends SuperDAO
{
	public List<DynaBean> getcrmSignList(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception{
		List<DynaBean> beanList = null;
		String userIdStr = conditionMap.get("userId");
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		String from = " from "+TableLfContract.TABLE_NAME+" contract,"+TableLfDep.TABLE_NAME+" dep,"+TableLfSysuser.TABLE_NAME+" sysuser";
		String sql = "select contract.*,sysuser."+TableLfSysuser.NAME+",sysuser."+TableLfSysuser.USER_NAME+",dep."+TableLfDep.DEP_NAME+ from;
		String countSql  = "select count(*) totalcount"+ from;
		String orderSql = " order by contract."+TableLfContract.CONTRACT_DATE+" DESC,contract."+TableLfContract.CONTRACT_ID+" DESC";
		StringBuffer sqlCon = new StringBuffer(" where contract."+TableLfContract.CONTRACT_DEP+" = dep."+TableLfDep.DEP_ID+" AND contract."+TableLfContract.USER_ID
				+" = sysuser."+TableLfSysuser.USER_ID);
		//机构权限
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userIdStr);
		String dominationSql = new StringBuffer(" and (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(userIdStr).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(domination).append("))").append(" and sysuser.").append(TableLfSysuser.USER_ID).append("<>1").toString();
		sqlCon.append(dominationSql);
		
		//签约ID
		String contractId = conditionMap.get("id");
		if(StringUtils.isNotBlank(contractId)){
			sqlCon.append(" and contract."+TableLfContract.CONTRACT_ID+" = "+ contractId);
		}
		//机构编号
		String lgcorpcode = conditionMap.get("lgcorpcode");
		if(StringUtils.isNotBlank(lgcorpcode)){
			sqlCon.append(" and contract."+TableLfContract.CORP_CODE+" = '"+ lgcorpcode +"'");
		}
		//手机号码
		String mobile = conditionMap.get("mobile");
		if(StringUtils.isNotBlank(mobile)){
			sqlCon.append(" and contract."+TableLfContract.MOBILE+" like '%"+mobile+"%'" );
		}
		
		//姓名
		String name = conditionMap.get("name");
		if(StringUtils.isNotBlank(name)){
			sqlCon.append(" and contract."+TableLfContract.CUSTOM_NAME+" like '%"+ name+"%'");
		}
//		
//		//签约套餐名称
//		String tcName = conditionMap.get("tcName");
//		if(StringUtils.isNotBlank(tcName)){
//			sqlCon.append(" and tc."+TableLfBusTaoCan.TAOCAN_NAME+" like '%"+tcName+"%'");
//		}
//		
		//签约套餐编号
		String tcCode = conditionMap.get("tcCode");
		if(StringUtils.isNotBlank(tcCode)){
			String inSql = "select "+TableLfContractTaocan.CONTRACT_ID+" from "+TableLfContractTaocan.TABLE_NAME+" where "
			+TableLfContractTaocan.TAOCAN_CODE+" like '%"+tcCode+"%'";
			sqlCon.append(" and contract."+TableLfContract.CONTRACT_ID+" in ("+inSql+")");
		}
		//过滤禁用的套餐
//		sqlCon.append(" and tc."+TableLfBusTaoCan.STATE+" = 0");
		//过滤禁用的签约套餐关系
		//sqlCon.append(" and ct."+TableLfContractTaocan.IS_VALID+" != '1'");
		//过滤禁用（非正常）的签约
		sqlCon.append(" and contract."+TableLfContract.IS_VALID+" = '0'");
		//计费类型
		String chargesType = conditionMap.get("chargesType");
		if(StringUtils.isNotBlank(chargesType)){
			sqlCon.append(" and tc."+TableLfBusTaoCan.TAOCAN_TYPE+" = "+ chargesType);
		}
		
		//签约状态
		String contractState = conditionMap.get("contractState");
		if(StringUtils.isNotBlank(contractState)){
			sqlCon.append(" and contract."+TableLfContract.CONTRACT_STATE+" = "+ contractState);
		}
		//签约账号
		String acctNo = conditionMap.get("acctNo");
		if(StringUtils.isNotBlank(acctNo)){
			sqlCon.append(" and contract."+TableLfContract.ACCT_NO+" like '%"+acctNo+"%'");
		}
		//操作员
		String opName = conditionMap.get("opName");
		if(StringUtils.isNotBlank(opName)){
			sqlCon.append(" and sysuser."+TableLfSysuser.NAME+" like '%"+opName+"%'");
		}
		//机构
		String depName = conditionMap.get("depName");
		if(StringUtils.isNotBlank(depName)){
			String isAll = conditionMap.get("isAll");
			if("on".equals(isAll)){//包含子机构
				sqlCon.append(" and (dep."+TableLfDep.DEP_PATH+" like '"+depName+"/%' or dep."+TableLfDep.DEP_PATH+" like '%/"+depName+"/%')" );
			}else{
				sqlCon.append(" and contract."+TableLfContract.CONTRACT_DEP+" = "+ depName);
			}
		}
		//起始时间
		String starttime = conditionMap.get("starttime");
		if(StringUtils.isNotBlank(starttime)){
			sqlCon.append(" and contract."+TableLfContract.CONTRACT_DATE+" >="+ genericDao.getTimeCondition(starttime));
		}
		//结束时间
		String endtime = conditionMap.get("endtime");
		if(StringUtils.isNotBlank(endtime)){
			sqlCon.append(" and contract."+TableLfContract.CONTRACT_DATE+" <="+genericDao.getTimeCondition(endtime));
		}
		
		sql = sql + sqlCon.toString() + orderSql;
		if(pageInfo == null){
			beanList = getListDynaBeanBySql(sql);
		}else{
			countSql = countSql + sqlCon;
			beanList = genericDao.findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		return beanList;
	}
	
	public List<DynaBean> getTaocans(List<String> ids) throws Exception{
		List<DynaBean> beanList = null;
		String idStr = "";
		if(ids == null || ids.size()==0)
		{
			return beanList;
		}else{
			for(String id:ids){
				idStr +=","+id;
			}
		}
		idStr = idStr.substring(1);
		String sql = "SELECT tc."+TableLfBusTaoCan.TAOCAN_NAME +",tc."+TableLfBusTaoCan.TAOCAN_CODE+",tc."+TableLfBusTaoCan.TAOCAN_MONEY+",tc."+TableLfBusTaoCan.TAOCAN_TYPE+",tc."+TableLfBusTaoCan.STATE
		+",ct."+TableLfContractTaocan.CONTRACT_ID+" FROM "+TableLfBusTaoCan.TABLE_NAME+" tc,"+TableLfContractTaocan.TABLE_NAME+" ct WHERE ct."+TableLfContractTaocan.TAOCAN_CODE
		+" = tc."+TableLfBusTaoCan.TAOCAN_CODE+" AND ct."+TableLfContractTaocan.CONTRACT_ID+" in ("+idStr+") AND ct."+TableLfContractTaocan.IS_VALID+" != '1'"
		+" order by ct."+TableLfContractTaocan.CONTRACT_ID+",tc."+TableLfBusTaoCan.STATE;
		beanList = getListDynaBeanBySql(sql);
		return beanList;
	}
	
	public List<DynaBean> findClientInfoByPhone(String phone,String corpCode){
		List<DynaBean> beanList = null;
		String sql = "SELECT client."+TableLfClient.GUID+",client."+TableLfClient.CLIENT_CODE+",client."+TableLfClient.MOBILE
		+",client."+TableLfClient.NAME+",sp."+TableLfClientDepSp.DEP_ID+",dep."+TableLfClientDep.DEP_NAME
		+" FROM "+TableLfClient.TABLE_NAME+" client,"+TableLfClientDepSp.TABLE_NAME+" sp,"+TableLfClientDep.TABLE_NAME+" dep"
		+" WHERE sp."+TableLfClientDepSp.DEP_ID+" = dep."+TableLfClientDep.DEP_ID+" AND client."+TableLfClient.CLIENT_ID+" = sp."+TableLfClientDepSp.CLIENT_ID
		+" AND client."+TableLfClient.MOBILE + " = '"+phone +"' AND client."+TableLfClient.CORP_CODE + " = '"+corpCode+"'"
		+" ORDER BY client."+TableLfClient.GUID;
		beanList = getListDynaBeanBySql(sql);
		return beanList;
	}
	
	public List<DynaBean> findClientDepsByGuid(String guid,String corpCode){
		List<DynaBean> beanList = null;
		String sql = "SELECT dep."+TableLfClientDep.DEP_NAME+",dep."+TableLfClientDep.DEP_ID
		+" FROM "+TableLfClient.TABLE_NAME+" client,"+TableLfClientDepSp.TABLE_NAME+" sp,"+TableLfClientDep.TABLE_NAME+" dep"
		+" WHERE sp."+TableLfClientDepSp.DEP_ID+" = dep."+TableLfClientDep.DEP_ID+" AND client."+TableLfClient.CLIENT_ID+" = sp."+TableLfClientDepSp.CLIENT_ID
		+" AND client."+TableLfClient.GUID + " = "+guid +" AND client."+TableLfClient.CORP_CODE + " = '"+corpCode+"'"
		+" ORDER BY client."+TableLfClient.GUID;
		beanList = getListDynaBeanBySql(sql);
		return beanList;
	}
	
	public boolean updateState(Connection conn,String id,String state) throws Exception{
		String selectSql = "SELECT "+TableLfContract.CONTRACT_STATE+" FROM "+TableLfContract.TABLE_NAME+" WHERE "+TableLfContract.CONTRACT_ID+" = "+ id;
		PreparedStatement ps = null;
		ResultSet rsResultSet = null;
		try {
			    ps = conn.prepareStatement(selectSql);
			    rsResultSet = ps.executeQuery();
			    String _state = null;
			    while(rsResultSet.next()){
			    	_state = rsResultSet.getString(1);
			    }
				if(state.equals(_state)){
					return true;
				}
				
				if(("2".equals(_state)&&!"1".equals(state)) || ("1".equals(_state) && !"0".equals(state))){//取消签约不能变更为 已冻结 已冻结的无法变更恢复签约）
					return false;
				}
				String updateSql = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
				String timeString = genericDao.getTimeCondition(sdf.format(new Date()));
				if("1".equals(state)||"2".equals(state)){//取消签约 或 冻结操作
					updateSql = "UPDATE "+TableLfContract.TABLE_NAME+" SET "+TableLfContract.CONTRACT_STATE+" = "+ state +","+TableLfContract.CANCEL_CONTIME+" = "+timeString
					+","+TableLfContract.CANCEL_CONTYPE+" = "+0+" WHERE "+TableLfContract.CONTRACT_ID+" = "+ id ;	
				}else{//恢复签约
					updateSql = "UPDATE "+TableLfContract.TABLE_NAME+" SET "+TableLfContract.CONTRACT_STATE+" = "+ state +","+TableLfContract.CANCEL_CONTIME+" = null"
					+","+TableLfContract.CANCEL_CONTYPE+" = -1 WHERE "+TableLfContract.CONTRACT_ID+" = "+ id ;	
				}
				return this.executeBySQL(conn, updateSql);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"updateState");
			return false;
		}finally{
			closeConnection(ps, rsResultSet);
		}
	   
	}
	
	public void closeConnection(PreparedStatement ps,ResultSet rs) throws SQLException{
		if(rs!=null){
			ps.close();
		}
		if(rs!=null){
			ps.close();
		}
	}
	
	public int findByAcctNo(Connection conn,String account,String corpCode) throws SQLException{
		int count = 0;
		String selectSql = "SELECT count(*) FROM "+TableLfContract.TABLE_NAME+" WHERE "+TableLfContract.ACCT_NO+" = '"+ account+"' AND "
		+TableLfContract.CORP_CODE+" = '"+corpCode+"'";
		PreparedStatement ps = null;
		ResultSet rsResultSet = null;
		try {
			 ps = conn.prepareStatement(selectSql);
			 rsResultSet = ps.executeQuery();
			 if(rsResultSet.next()){
				 count = rsResultSet.getInt(1);
			 }
		    return count;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"findByAcctNo");
			return count;
		}finally{
			closeConnection(ps, rsResultSet);
		}
		
	}
	
	public int findByMobile(Connection conn,String mobile,String corpCode) throws SQLException{
		int count = 0;
		String selectSql = "SELECT count(*) FROM "+TableLfContract.TABLE_NAME+" WHERE "+TableLfContract.MOBILE+" = '"+ mobile+"' AND "
		+TableLfContract.CORP_CODE+" = '"+corpCode+"' AND ("+TableLfContract.ACCT_NO +" is NULL OR "+TableLfContract.ACCT_NO +" = '')"; 
		PreparedStatement ps = null;
		ResultSet rsResultSet = null;
		try {
			ps = conn.prepareStatement(selectSql);
		    rsResultSet = ps.executeQuery();
		    if(rsResultSet.next()){
		    	count = rsResultSet.getInt(1);
		    }
	    return count;	
		} catch (Exception e) {
			EmpExecutionContext.error(e,"findByMobile");
			return count;
		}finally{
			closeConnection(ps, rsResultSet);
		}

	}
	
	public int findByClientcode(Connection conn,String clientCode,String corpCode) throws SQLException{
		int count = 0;
		String selectSql = "SELECT count(*) FROM "+TableLfClient.TABLE_NAME+" WHERE "+TableLfClient.CLIENT_CODE+" = '"
		+ clientCode+"' AND "+TableLfClient.CORP_CODE+" = '"+corpCode+"'";
		PreparedStatement ps = null;
		ResultSet rsResultSet = null;
		try {
			ps = conn.prepareStatement(selectSql);
		    rsResultSet = ps.executeQuery();
		    if(rsResultSet.next()){
		    	count = rsResultSet.getInt(1);
		    }
		    return count;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"findByClientcode");
			return count;
		}finally{
			closeConnection(ps, rsResultSet);
		}
	}
	public Set<String> findDepTaocan(Connection conn,Long userId,String[] codes) throws SQLException{
		Set<String> codeSet = new HashSet<String>();
		String inString = "";
		String qt = "'";
		for(String code:codes){
			inString +=","+qt+code+qt;
		}
		if(inString.length()>0){
			inString = inString.substring(1);
		}
		String selectSql = "select "+TableLfDepTaocan.TAOCAN_CODE+" FROM "+ TableLfDepTaocan.TABLE_NAME+" where "
		+TableLfDepTaocan.CONTRACT_USER +" = "+ userId +" AND "+TableLfDepTaocan.TAOCAN_CODE +" in ("+inString+")";
		PreparedStatement ps = null;
		ResultSet rsResultSet = null;
		try {
		ps = conn.prepareStatement(selectSql);
		codeSet = new HashSet<String>();//数据库中已存在签约操作员下的相应套餐
	    rsResultSet = ps.executeQuery();
	    while(rsResultSet.next()){
	    	codeSet.add(rsResultSet.getString(1));
	    }
	    return codeSet;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"findDepTaocan");
			return codeSet;
		}finally{
			closeConnection(ps, rsResultSet);
		}
	}
	
	/**
	 * @description    修改签约状态
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-1-19 下午01:40:35
	 */
	public boolean updateClientState(Connection conn,Long guid,int state) throws Exception{
		String sql = "UPDATE "+TableLfClient.TABLE_NAME+" SET ISCONTRACT = "+state+" WHERE "+TableLfClient.GUID+" = "+guid ;
		return this.executeBySQL(conn, sql);
	}
	
	public List<DynaBean> findTaocans(LinkedHashMap<String, String> condMap) throws Exception{
		String ids = condMap.get("ids");
		String state = condMap.get("state");
		String type = condMap.get("type");
		String name = condMap.get("name");
		String lgcorpcode = condMap.get("lgcorpcode");
		String sql = "SELECT "+TableLfBusTaoCan.TAOCAN_ID+","+TableLfBusTaoCan.TAOCAN_CODE+","+TableLfBusTaoCan.TAOCAN_NAME+","
		+TableLfBusTaoCan.TAOCAN_MONEY+","+TableLfBusTaoCan.TAOCAN_TYPE
		+" FROM "+TableLfBusTaoCan.TABLE_NAME;
		StringBuffer sb = new StringBuffer("");
		sb.append(" WHERE "+TableLfBusTaoCan.CORP_CODE+" = '"+lgcorpcode+"'");
		if(StringUtils.isNotBlank(state)){
			sb.append(" AND "+TableLfBusTaoCan.STATE+" = "+state);
		}
		if(StringUtils.isNotBlank(type)){
			sb.append(" AND "+TableLfBusTaoCan.TAOCAN_TYPE+" = "+type);
		}
		if(StringUtils.isNotBlank(name)){
			sb.append(" AND "+TableLfBusTaoCan.TAOCAN_NAME+" like '%"+name+"%'");
		}
		if(StringUtils.isNotBlank(ids)){
			sb.append(" AND "+TableLfBusTaoCan.TAOCAN_ID+" not in ("+ids+")");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		String time = genericDao.getTimeCondition(sdf.format(calendar.getTime()));
		//增加套餐有效时间的过滤
		sb.append(" AND "+TableLfBusTaoCan.START_DATE+" <="+ time);
		sb.append(" AND "+TableLfBusTaoCan.END_DATE+" >="+ time);
		return getListDynaBeanBySql(sql+sb.toString());
	}
	
	/**
	 * @description   查找已选择的套餐列表 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-1-20 下午01:40:52
	 */
	public List<DynaBean> findSelTaocans(LinkedHashMap<String, String> condMap) throws Exception{
		String contractId = condMap.get("contractId");
		String state = condMap.get("state");
		String isvalid = condMap.get("isvalid");
		String lgcorpcode = condMap.get("lgcorpcode");
		String sql = "SELECT tc."+TableLfBusTaoCan.TAOCAN_ID+",tc."+TableLfBusTaoCan.TAOCAN_CODE+",tc."+TableLfBusTaoCan.TAOCAN_NAME+",tc."
		+TableLfBusTaoCan.TAOCAN_TYPE+",tc."+TableLfBusTaoCan.TAOCAN_MONEY+",tc."+TableLfBusTaoCan.STATE+",ct."+TableLfContractTaocan.ID
		+" FROM "+TableLfContractTaocan.TABLE_NAME+" ct,"+TableLfBusTaoCan.TABLE_NAME+" tc WHERE ct."+TableLfContractTaocan.TAOCAN_CODE
		+" = tc."+TableLfBusTaoCan.TAOCAN_CODE;
		StringBuffer sb = new StringBuffer("");
		if(StringUtils.isNotBlank(state)){
			sb.append(" AND tc."+TableLfBusTaoCan.STATE+" = "+state);
		}
		if(StringUtils.isNotBlank(contractId)){
			sb.append(" AND ct."+TableLfContractTaocan.CONTRACT_ID+" = "+contractId);
		}
		if(StringUtils.isNotBlank(isvalid)){
			sb.append(" AND ct."+TableLfContractTaocan.IS_VALID+" = '"+isvalid+"'");
		}
		sb.append(" AND tc."+TableLfBusTaoCan.CORP_CODE+" = '"+lgcorpcode+"'");
		sb.append(" ORDER by tc."+TableLfBusTaoCan.STATE);
		return getListDynaBeanBySql(sql+sb.toString());
	}
	
	public List<DynaBean> findTaocansByCond(LinkedHashMap<String, String> condMap) throws Exception{
		String contractId = condMap.get("contractId");
		String state = condMap.get("state");
		String isvalid = condMap.get("isvalid");
		String lgcorpcode = condMap.get("lgcorpcode");
		String sql = "SELECT tc."+TableLfBusTaoCan.TAOCAN_CODE+",tc."+TableLfBusTaoCan.TAOCAN_TYPE+",tc."+TableLfBusTaoCan.TAOCAN_MONEY+",tc."+TableLfBusTaoCan.TAOCAN_NAME+",tc."+TableLfBusTaoCan.STATE
		+",charge."+TableLfProCharges.BUCKLE_DATE+",charge."+TableLfProCharges.TRY_DAYS+",charge."+TableLfProCharges.TRY_TYPE+",charge."+TableLfProCharges.TRYSTART_DATE+",charge."+TableLfProCharges.TRYEND_DATE+",charge."+TableLfProCharges.BUCKLE_TYPE+",charge."+TableLfProCharges.BUCKUP_MAXTIMER
		+",ct."+TableLfContractTaocan.BUCKLEFEE_TIME+",ct."+TableLfContractTaocan.ID+",ct."+TableLfContractTaocan.BUCKLEFEE_TIME+",ct."+TableLfContractTaocan.DEBITACCOUNT+",ct."+TableLfContractTaocan.LAST_BUCKLEFEE
		+",contract."+TableLfContract.MOBILE+",contract."+TableLfContract.CUSTOM_NAME+",contract."+TableLfContract.IDENT_NO+",contract."+TableLfContract.CONTRACT_ID
		+",contract."+TableLfContract.ACCT_NO+",contract."+TableLfContract.CONTRACT_STATE+",contract."+TableLfContract.CONTRACT_SOURCE+",contract."+TableLfContract.CONTRACT_DEP
		+",contract."+TableLfContract.CONTRACT_USER+",contract."+TableLfContract.DEP_ID+",contract."+TableLfContract.USER_ID+",contract."+TableLfContract.CORP_CODE
		+",dep."+TableLfDep.DEP_NAME
		+" FROM "+TableLfContractTaocan.TABLE_NAME+" ct,"+TableLfBusTaoCan.TABLE_NAME+" tc,"+TableLfProCharges.TABLE_NAME+" charge,"+TableLfContract.TABLE_NAME+" contract,"+TableLfDep.TABLE_NAME+" dep"
		+" WHERE ct."+TableLfContractTaocan.TAOCAN_CODE +" = tc."+TableLfBusTaoCan.TAOCAN_CODE+" AND ct."+TableLfContractTaocan.TAOCAN_CODE+" = charge."+TableLfProCharges.TAOCAN_CODE +" AND ct."
		+TableLfContractTaocan.CONTRACT_ID+" = contract."+TableLfContract.CONTRACT_ID+" AND contract."+TableLfContract.CONTRACT_DEP+" = dep."+TableLfDep.DEP_ID;
		StringBuffer sb = new StringBuffer("");
		if(StringUtils.isNotBlank(state)){
			sb.append(" AND tc."+TableLfBusTaoCan.STATE+" = "+state);
		}
		if(StringUtils.isNotBlank(contractId)){
			sb.append(" AND ct."+TableLfContractTaocan.CONTRACT_ID+" = "+contractId);
		}
		if(StringUtils.isNotBlank(isvalid)){
			sb.append(" AND ct."+TableLfContractTaocan.IS_VALID+" = '"+isvalid+"'");
		}
		if(StringUtils.isNotBlank(lgcorpcode)){
			sb.append(" AND tc."+TableLfBusTaoCan.CORP_CODE+" = '"+lgcorpcode+"'");
		}
		return getListDynaBeanBySql(sql+sb.toString());
	}
	/**
	 * @description    更改valid标示  2表示取消冻结操作处理后的标示  
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-1-21 下午05:15:32
	 */
	public boolean updateValid(Connection conn,String id,String state) throws Exception{
		String updateSql = null;
		if("1".equals(state)||"2".equals(state)){//取消签约 或 冻结操作
			updateSql = "UPDATE "+TableLfContractTaocan.TABLE_NAME+" SET "+TableLfContractTaocan.IS_VALID+" = '2'"
			+" WHERE "+TableLfContract.CONTRACT_ID+" = "+ id+" AND "+TableLfContractTaocan.IS_VALID+" = '0'";	
		}else{//恢复签约
			updateSql = "UPDATE "+TableLfContractTaocan.TABLE_NAME+" SET "+TableLfContractTaocan.IS_VALID+" = '0'"
			+" WHERE "+TableLfContract.CONTRACT_ID+" = "+ id+" AND "+TableLfContractTaocan.IS_VALID+" = '2'";	
		}
		return this.executeBySQL(conn, updateSql);
	}
}
