package com.montnets.emp.charging.biz;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.charging.dao.LfDepBalanceDAO;
import com.montnets.emp.charging.vo.LfDepBalanceDefVo;
import com.montnets.emp.charging.vo.LfDepBalanceVo;
import com.montnets.emp.charging.vo.LfDepRechargeLogVo;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfBalanceDef;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDepRechargeLog;
import com.montnets.emp.entity.sysuser.LfDepUserBalance;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

public  class ChargingBiz  extends SuperBiz{
	
	protected SuperOpLog spLog = new SuperOpLog();
	protected String opModule=StaticValue.SMS_BOX;
	protected String opType=StaticValue.OTHER;
		
	/**
	 * 新增充值/回收日志
	 * @description    
	 * @param log
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-8 下午05:59:15
	 */
	public boolean addBalanceLog(LfDepRechargeLog log) throws Exception
	{
		boolean returnFlag = false;
		try{
			log.setOptDate(new Timestamp(System.currentTimeMillis()));
			log.setOptType(101);
			returnFlag = empDao.save(log);
		}catch(Exception e){
			EmpExecutionContext.error(e,"操作日志入库失败！");
		}
		return returnFlag;
	}
	
	/**
	 *    查询指定机构ID下的子级机构的    充值与回收信息
	 * @param depId   机构ID
	 * @return
	 * @throws Exception
	 */
	public List<LfDepBalanceVo> getDepBalanceVoByDepId(LfSysuser lfSysuser,Long depId,String depName,PageInfo pageInfo)
	{
		List<LfDepBalanceVo> list = null;
		try{
			list = new LfDepBalanceDAO().findLfDepBalanceVos(lfSysuser,depId,depName,pageInfo);
		}catch(Exception e){
			EmpExecutionContext.error(e,"查询指定机构ID下的子级机构的充值与回收信息出现异常！");
		}
		return list;
	}
	
	public HashMap<String,Long> getMoney(String lgcorpcode) throws Exception{
		HashMap<String,Long> hm=new HashMap<String,Long>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", lgcorpcode);
		List<LfDepUserBalance> list=new BaseBiz().getByCondition(LfDepUserBalance.class, conditionMap, orderbyMap);
		Long smsCount=0L;
		Long mmsCount=0L;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				LfDepUserBalance ba=list.get(i);
				Long sms=ba.getSmsBalance();
				smsCount=sms+smsCount;
				Long mms =ba.getMmsBalance();
				mmsCount=mms+mmsCount;
			}
		}
		hm.put("sms", smsCount);
		hm.put("mms", mmsCount);
		return hm;
	}
	
	
	
	/**
	 *    查询指定机构ID下的子级机构的充值与回收信息
	 * @param depId   机构ID
	 * @return
	 * @throws Exception
	 */
	public List<LfDepBalanceVo> getDepBalanceVoByAdmin(String corpCode,String depId,String depName,PageInfo pageInfo)
	{
		List<LfDepBalanceVo> list = null;
		try{
			list = new LfDepBalanceDAO().findLfDepBalanceVosByAdmin(corpCode,depId,depName,pageInfo);
		}catch(Exception e){
			EmpExecutionContext.error(e,"查询指定机构ID下的子级机构的充值与回收信息出现异常！");
		}
		return list;
	}
	
	/**
	 *    查询操作日志
	 * @return
	 * @throws Exception
	 */
	//查询充值/回收操作日志
	public List<LfDepRechargeLogVo> getDepBalanceLogVos(LfSysuser lfSysuser,LfDepRechargeLogVo lfDepRechargeLogVo,PageInfo pageInfo)
	{
		List<LfDepRechargeLogVo> list = null;
		try{
			list = new LfDepBalanceDAO().findLfDepBalanceLogVos(lfSysuser,lfDepRechargeLogVo,pageInfo);
		}catch(Exception e){
			EmpExecutionContext.error(e,"查询充值/回收操作日志出现异常！");
		}
		return list;
	}
	
	/**
	 *  查询当前机构 以及其子机构
	 * @param depId 当前机构ID
	 * @return
	 */
	public List<LfDep> getLfDepAndDepSon(Long depId){
		List<LfDep> depList = null;
		try {
			depList = new LfDepBalanceDAO().findLfDepAndDepSon(depId);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询当前机构 以及其子机构出现异常！");
		}
		return depList;
	}
	
	/**
	 * 短信充值
	 * @param lfSysuser 操作员
	 * @param depId	充值目的机构ID
	 * @param count 充值总数
	 * @return  返回值信息 			
	 * 			0:短信充值成功
	 * 		   -1:短信充值失败
	 * 		   -2:短信充值数目不能为空
	 *  	   -3:获取操作员上级机构或用户短信余额记录失败
	 *  	   -4:机构下没有可用短信余额
	 *  	   -5:充值数目大于短信可分配余额
	 * 		-9999:短信充值接口调用异常
	 */
	public int addSmsBalance(LfSysuser lfSysuser,Long depId,Long count)
	{
		
		return setBalanceNumber(lfSysuser, depId, count, 0);
		
		/*调用存储过程,此方法暂时保留
		 
		if(count == null || count == 0){
			return "短信充值数目不能为空";
		}
		String corpCode = lfSysuser.getCorpCode();
		String userName = lfSysuser.getUserName();
		String userDepId = String.valueOf(lfSysuser.getDepId());
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		List<LfDepUserBalance> pareBalances = null;
		LfDepUserBalance pareBal = null;
		//父级机构短信可分配余额
		Long pareSmsBalance = 0L;				
		
		String parentId = "";
		if(!"admin".equals(userName)){
			//非admin只能给下级机构进行充值，所以上级机构就是操作员所在机构
			parentId = userDepId;
		}else{
			conditionMap.put("depId", depId.toString());
			try {
				List<LfDep> depList = empDao.findListByCondition(LfDep.class, conditionMap, null);
				if(depList != null && depList.size()>0){
					parentId = depList.get(0).getSuperiorId().toString();
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e);
				return "获取操作员上级机构失败";
			}
			conditionMap.clear();
		}
		
		if(!"admin".equals(userName) || ("admin".equals(userName) && !userDepId.equals(String.valueOf(depId)))){
			//给下级机构进行充值，需要获取上级机构的余额
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("targetId", parentId);
			try {
				pareBalances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
				if(pareBalances == null || pareBalances.size() == 0){
					return "机构下没有可用短信余额";
				}else{
					pareBal = pareBalances.get(0);					
					pareSmsBalance = pareBal.getSmsBalance();		
					if(count > pareSmsBalance){
						return "充值数目大于短信可分配余额";
					}
				}
			} catch (Exception e2) {
				EmpExecutionContext.error(e2);
				return "获取用户短信余额记录失败";
			}
			conditionMap.clear();
		}
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("targetId", String.valueOf(depId));
		List<LfDepUserBalance> sonBalances = null;
		boolean updateOrAdd = true;
		String returnStr = "";
		try {
			sonBalances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
		} catch (Exception e1) {
			EmpExecutionContext.error(e1);
			return "获取用户短信余额记录失败";
		}
		//获取连接
		Connection conn = empTransDao.getConnection();
		try {
			//开启事务
			empTransDao.beginTransaction(conn);
			LfDepUserBalance balance = null;
			//给子机构进行充值，如果已有充值记录，则进行修改，否则进行添加
			if(sonBalances != null && sonBalances.size() > 0){
				balance = sonBalances.get(0);
				//短信余额：短信可用余额
				Long smsBalance = balance.getSmsBalance();		
				balance.setSmsBalance(smsBalance + count);
				//这里是先将子级机构进行充值  count
				empTransDao.update(conn, balance);					
			}else{	
				balance = new LfDepUserBalance();
				//企业编号
				balance.setCorpCode(lfSysuser.getCorpCode()); 
				//所属机构id
				balance.setTargetId(depId);	 	
				//短信余额：短信可用余额
				balance.setSmsBalance(count);	
				//短信已发短信数量
				balance.setSmsCount(0L);
				//彩信余额：彩信可用余额
				balance.setMmsBalance(0L);		 
				//彩信已发 数量
				balance.setMmsCount(0L);  		
				empTransDao.save(conn, balance);
			}
			//如果是给下级机构进行充值，则扣取上级机构的余额(给顶级机构充值则不需)
			if(!"admin".equals(userName) || ("admin".equals(userName) && !userDepId.equals(String.valueOf(depId)))){
				pareBal.setSmsBalance(pareSmsBalance - count);
				//这里是将父级机构进行短信可分配余额 减去count
				empTransDao.update(conn, pareBal);					
			}
			//提交事务
				empTransDao.commitTransaction(conn);
		} catch (Exception e) {
				updateOrAdd  = false;
				//事务回滚
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e);
		}finally{
			//关闭连接
				empTransDao.closeConnection(conn);
				if(updateOrAdd){
					returnStr = "短信充值成功";
				}else{
					returnStr = "短信充值失败";
				}
		}
		return returnStr;*/
		
	}
	/**
	 * 短信回收
	 * @param lfSysuser操作员
	 * @param depId充值目的机构ID
	 * @param count充值总数
	 * @return  返回值信息 			
	 * 			0:短信回收成功
	 * 		   -1:短信回收失败
	 * 		   -2:短信充值数目不能为空
	 *  	   -3:获取操作员上级机构或用户短信余额记录失败
	 *  	   -4:机构下没有可用短信余额
	 *  	   -5:回收短信数大于机构可分配数目
	 *         -7:机构还没有进行充值
	 * 		-9999:短信回收接口调用异常
	 */
	public int recSmsBalance(LfSysuser lfSysuser,Long depId,Long count)
	{
		return setBalanceNumber(lfSysuser, depId, count, 1);
		
		/*调用存储过程,此方法暂时保留
		if(count == null || count == 0){
			return "短信回收数目不能为空";
		}
		String corpCode = lfSysuser.getCorpCode();
		String userName = lfSysuser.getUserName();
		String userDepId = String.valueOf(lfSysuser.getDepId());
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		String parentId = "";
		if(!"admin".equals(userName)){
			//非admin只能回收下级机构，所以上级机构就是操作员所在机构
			parentId = userDepId;
		}else{
			conditionMap.put("depId", depId.toString());
			try {
				List<LfDep> depList = empDao.findListByCondition(LfDep.class, conditionMap, null);
				if(depList != null && depList.size()>0){
					parentId = depList.get(0).getSuperiorId().toString();
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e);
				return "获取操作员上级机构失败";
			}
			conditionMap.clear();
		}
		
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("targetId", parentId);
		List<LfDepUserBalance> pareBalances = null;
		LfDepUserBalance pareBal = null;
		//父级机构短信可分配余额
		Long pareSmsBalance = 0L;			
		if(!"admin".equals(userName) || ("admin".equals(userName) && !userDepId.equals(String.valueOf(depId)))){			
			try {
				pareBalances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
				if(pareBalances == null || pareBalances.size() == 0){
					return "机构下没有可用短信余额";
				}else{
					pareBal = pareBalances.get(0);	
					pareSmsBalance = pareBal.getSmsBalance();		
				}
			} catch (Exception e2) {
				EmpExecutionContext.error(e2);
				return "获取用户短信余额记录失败";
			}
			conditionMap.clear();
		}
		
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("targetId", String.valueOf(depId));
		List<LfDepUserBalance> sonBalances = null;
		LfDepUserBalance sonBal = null;
		//该机构可分配数目
		Long sonSmsBalance = 0L ;		
		boolean updateOrAdd = true;
		String returnStr = "";
		try {
			sonBalances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
			if(sonBalances == null || sonBalances.size() == 0){
				return "机构还没有进行充值";
			}else{
				sonBal = sonBalances.get(0);
				sonSmsBalance = sonBal.getSmsBalance();
				if(count > sonSmsBalance){
					return "回收短信数大于机构可分配数目";
				}
			}
		} catch (Exception e1) {
			EmpExecutionContext.error(e1);
			return "获取用户短信余额记录失败";
		}
		//获取连接
		Connection conn = empTransDao.getConnection();
		try {
			//开启事务
				empTransDao.beginTransaction(conn);
				//短信余额：短信可用余额
				Long smsBalance = sonBal.getSmsBalance();	
				//这里是先将子级机构进行回收  count 减
				sonBal.setSmsBalance(smsBalance - count);
				empTransDao.update(conn, sonBal);			
				//非回收顶级机构
				if(!"admin".equals(userName)  || ("admin".equals(userName) && !userDepId.equals(String.valueOf(depId)))){	
					pareBal.setSmsBalance(pareSmsBalance + count);
					//这里是将父级机构进行短信可分配余额 加去count
					empTransDao.update(conn, pareBal);					
				}
				//提交事务
				empTransDao.commitTransaction(conn);
		} catch (Exception e) {
				updateOrAdd  = false;
				//事务回滚
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e);
		}finally{
			//关闭连接
				empTransDao.closeConnection(conn);
				if(updateOrAdd){
					returnStr = "短信回收成功";
				}else{
					returnStr = "短信回收失败";
				}
		}
		return returnStr;*/
	}
	/**
	 * 彩信充值
	 * @param lfSysuser操作员
	 * @param depId充值目的机构ID
	 * @param count充值总数
	 * @return  返回值信息 			
	 * 			0:彩信充值成功
	 * 		   -1:彩信充值失败
	 * 		   -2:彩信充值数目不能为空
	 *  	   -3:获取操作员上级机构或用户彩信余额记录失败
	 *  	   -4:机构下没有可用彩信余额
	 *  	   -5:充值数目大于彩信可分配余额
	 * 		-9999:彩信充值接口调用异常
	 */
	public int addMmsBalance(LfSysuser lfSysuser,Long depId,Long count)
	{
		return setBalanceNumber(lfSysuser, depId, count, 2);
		
		/*调用存储过程,此方法暂时保留
		if(count == null || count == 0){
			return "彩信充值数目不能为空";
		}
		String corpCode = lfSysuser.getCorpCode();
		String userName = lfSysuser.getUserName();
		String userDepId = String.valueOf(lfSysuser.getDepId());
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		String parentId = "";
		if(!"admin".equals(userName)){
			//非admin只能给下级机构进行充值，所以上级机构就是操作员所在机构
			parentId = userDepId;
		}else{
			conditionMap.put("depId", depId.toString());
			try {
				List<LfDep> depList = empDao.findListByCondition(LfDep.class, conditionMap, null);
				if(depList != null && depList.size()>0){
					parentId = depList.get(0).getSuperiorId().toString();
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e);
				return "获取操作员上级机构失败";
			}
			conditionMap.clear();
		}
		
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("targetId", parentId);
		List<LfDepUserBalance> pareBalances = null;
		LfDepUserBalance pareBal = null;
		//父级机构彩信可分配余额
		Long pareMmsBalance = 0L;		
		
		if(!"admin".equals(userName) || ("admin".equals(userName) && !userDepId.equals(String.valueOf(depId)))){
			try {
				pareBalances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
				if(pareBalances == null || pareBalances.size() == 0){
					return "机构下没有可用彩信余额";
				}else{
					pareBal = pareBalances.get(0);					
					pareMmsBalance = pareBal.getMmsBalance();		
					if(count > pareMmsBalance){
						return "充值数目大于彩信可分配余额";
					}
				}
			} catch (Exception e2) {
				EmpExecutionContext.error(e2);
				return "获取用户彩信余额记录失败";
			}
			conditionMap.clear();
		}
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("targetId", String.valueOf(depId));
		List<LfDepUserBalance> sonBalances = null;
		boolean updateOrAdd = true;
		String returnStr = "";
		try {
			sonBalances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
		} catch (Exception e1) {
			EmpExecutionContext.error(e1);
			return "获取用户彩信余额记录失败";
		}
		//获取连接
		Connection conn = empTransDao.getConnection();
		try {
			//开启事务
				empTransDao.beginTransaction(conn);
				LfDepUserBalance balance = null;
			if(sonBalances != null && sonBalances.size() > 0){
				balance = sonBalances.get(0);
				//彩信余额：彩信可用余额
				Long mmsBalance = balance.getMmsBalance();		
				balance.setMmsBalance(mmsBalance + count);
				//这里是先将子级机构进行充值  count
				empTransDao.update(conn, balance);					
			}else{	
				balance = new LfDepUserBalance();
				//企业编号
				balance.setCorpCode(lfSysuser.getCorpCode()); 
				//所属机构id
				balance.setTargetId(depId);	 	
				//短信余额：短信可用余额
				balance.setSmsBalance(0L);	
				//短信已发短信数量
				balance.setSmsCount(0L);	
				 //彩信余额：彩信可用余额
				balance.setMmsBalance(count);	
				//彩信已发 数量
				balance.setMmsCount(0L);  		
				empTransDao.save(conn, balance);
			}
			//非顶级机构充值
			if(!"admin".equals(userName) || ("admin".equals(userName) && !userDepId.equals(String.valueOf(depId)))){
				//这里是将父级机构进行短信可分配余额 减去count
				pareBal.setMmsBalance(pareMmsBalance- count);	
				empTransDao.update(conn, pareBal);					
			}
				empTransDao.commitTransaction(conn);
		} catch (Exception e) {
				updateOrAdd  = false;
				//事务回滚
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e);
		}finally{
			//关闭连接
				empTransDao.closeConnection(conn);
				if(updateOrAdd){
					returnStr = "彩信充值成功";
				}else{
					returnStr = "彩信充值失败";
				}
		}
		return returnStr;*/
	}
	/**
	 * 彩信回收
	 * @param lfSysuser操作员
	 * @param depId充值目的机构ID
	 * @param count充值总数
	 * @return  返回值信息 			
	 * 			0:彩信回收成功
	 * 		   -1:彩信回收失败
	 * 		   -2:彩信充值数目不能为空
	 *  	   -3:获取操作员上级机构或用户彩信余额记录失败
	 *  	   -4:机构下没有可用彩信余额
	 *  	   -5:回收彩信数大于机构可分配数目
	 * 		   -7:机构还没有进行充值
	 * 		-9999:彩信充值接口调用异常
	 */
	public int recMmsBalance(LfSysuser lfSysuser,Long depId,Long count)
	{
		return setBalanceNumber(lfSysuser, depId, count, 3);
		
		/*调用存储过程,此方法暂时保留
		if(count == null || count == 0){
			return "彩信回收数目不能为空";
		}
		String corpCode = lfSysuser.getCorpCode();
		String userName = lfSysuser.getUserName();
		String userDepId = String.valueOf(lfSysuser.getDepId());
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		String parentId = "";
		if(!"admin".equals(userName)){
			//非admin只能给下级机构进行充值，所以上级机构就是操作员所在机构
			parentId = userDepId;
		}else{
			conditionMap.put("depId", depId.toString());
			try {
				List<LfDep> depList = empDao.findListByCondition(LfDep.class, conditionMap, null);
				if(depList != null && depList.size()>0){
					parentId = depList.get(0).getSuperiorId().toString();
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e);
				return "获取操作员上级机构失败";
			}
			conditionMap.clear();
		}
		
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("targetId", parentId);
		List<LfDepUserBalance> pareBalances = null;
		LfDepUserBalance pareBal = null;
		//父级机构彩信可分配余额
		Long pareMmsBalance = 0L;			
		if(!"admin".equals(userName) || ("admin".equals(userName) && !userDepId.equals(String.valueOf(depId)))){
			try {
				pareBalances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
				if(pareBalances == null || pareBalances.size() == 0){
					return "机构下没有可用彩信余额";
				}else{
					pareBal = pareBalances.get(0);	
					pareMmsBalance = pareBal.getMmsBalance();		
				}
			} catch (Exception e2) {
				EmpExecutionContext.error(e2);
				return "获取用户彩信余额记录失败";
			}
			conditionMap.clear();
		}
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("targetId", String.valueOf(depId));
		List<LfDepUserBalance> sonBalances = null;
		LfDepUserBalance sonBal = null;
		//该机构可分配数目
		Long sonMmsBalance = 0L ;		
		boolean updateOrAdd = true;
		String returnStr = "";
		try {
			sonBalances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
			if(sonBalances == null || sonBalances.size() == 0){
				return "机构还没有进行充值";
			}else{
				sonBal = sonBalances.get(0);
				sonMmsBalance = sonBal.getMmsBalance();
				if(count > sonMmsBalance){
					return "回收彩信数大于机构可分配数目";
				}
			}
		} catch (Exception e1) {
			EmpExecutionContext.error(e1);
			return "获取用户彩信余额记录失败";
		}
		Connection conn = empTransDao.getConnection();
		try {
				empTransDao.beginTransaction(conn);
				//彩信余额：短信可用余额
				Long mmsBalance = sonBal.getMmsBalance();		
				//这里是先将子级机构进行回收  count 减
				sonBal.setMmsBalance(mmsBalance - count);
				empTransDao.update(conn, sonBal);		
				//非顶级就该回收
				if(!"admin".equals(userName) || ("admin".equals(userName) && !userDepId.equals(String.valueOf(depId)))){
					//这里是将父级机构进行彩信可分配余额 加去count
					pareBal.setMmsBalance(pareMmsBalance+ count);	
					empTransDao.update(conn, pareBal);					
				}
				empTransDao.commitTransaction(conn);
		} catch (Exception e) {
				updateOrAdd  = false;
				//事务回滚
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e);
		}finally{
			//关闭连接
				empTransDao.closeConnection(conn);
				if(updateOrAdd){
					returnStr = "彩信回收成功";
				}else{
					returnStr = "彩信回收失败";
				}
		}
		return returnStr;*/
	}
	
	/**
	 * 短彩信充值、回收接口
	 * @param lfSysuser	操作员
	 * @param depId  充值目的机构ID
	 * @param count  充值总数
	 * @param oprType  操作类型:0:短信充值 1:短信回收 2:彩信充值 3:彩信回收
	 * @return
	 */
	private int setBalanceNumber(LfSysuser lfSysuser,Long depId,Long count, int oprType)
	{
		//返回值,默认为-1:短信充值失败
  		int result = -1;
  		int type=1;
		//操作类型参数合法,lfSysuser对象不为空,调用存储过程, 操作类型 0:短信充值 1:短信回收 2:彩信充值 3:彩信回收
		if(lfSysuser != null && (oprType >= 0 && oprType <=3))
		{
			String corpCode = lfSysuser.getCorpCode();
			String userName = lfSysuser.getUserName();
			long userDepId = lfSysuser.getDepId();
			boolean falg = false;//默认无跨机构充值的权限
			Long supDepId = null; //当前充值机构的父机构ID
	  		Connection connection = null;
			CallableStatement cs = null;
			//记录日志信息
			String oprInfo = "";
			//操作状态
			String oprInfoStatu = "成功，共充值费用：";
			switch (oprType)
			{
				case 0:
					oprInfo = "短信充值";
					break;
				case 1:
					oprInfo = "短信回收";
					type=2;
					break;
				case 2:
					oprInfo = "彩信充值";
					break;
				case 3:
					oprInfo = "彩信回收";
					type=2;
					break;
				default:
					break;
			}
			try
			{
			//创建连接
			connection = empTransDao.getConnection();
			
				try
				{
					//admin用户和无跨机构充值权限的用户使用旧的存储过程，其他用户使用新的存储过程
					//判断用户是否有跨机构充值的权限
					falg = new LfDepBalanceDAO().checkUserBalance(depId,lfSysuser.getUserId(),corpCode);
					if(userName.equals("admin") || !falg)
					{
						//短信充值或回收
						if(oprType == 0 || oprType == 1)
						{	
							
							cs = connection.prepareCall("{call COSTRECOVERY(?,?,?,?,?,?,?)}");
						}
						//彩信充值或回收
						else if(oprType == 2 || oprType == 3)
						{
							cs = connection.prepareCall("{call MMSCOSTRECOVERY(?,?,?,?,?,?,?)}");
						}
						cs.setLong(1, userDepId);
						cs.setLong(2, depId);
						cs.setString(3, userName);
						cs.setLong(4, count);
						cs.setString(5, corpCode);
						
						//充值回收标识
						if(oprType == 0 || oprType == 2)
						{
							cs.setLong(6, 1);//标识,1:充值 2:回收
						}
						else if(oprType == 1 || oprType == 3)
						{
							cs.setLong(6, 2);//标识,1:充值 2:回收
							oprInfoStatu = "成功，共回收费用：";
						}
						cs.registerOutParameter(7, java.sql.Types.INTEGER);
						cs.execute();
						//返回值
						result = cs.getInt(7);
					}
					else
					{
						LfDep  lfDep = new LfDepBalanceDAO().getDepByDepIdAndCorpCode(depId, corpCode);
						if(lfDep != null)
						{
							supDepId = lfDep.getSuperiorId();							
						}
						//有跨机构权限的操作员分配了顶级机构的充值权限父级机构给-1
						if(lfDep != null && lfDep.getDepLevel() == 1 && supDepId == 0 )
						{
							supDepId = -1l;
						}

						//短信充值或回收
						if(oprType == 0 || oprType == 1)
						{	
							
							cs = connection.prepareCall("{call LF_SMSBALANCEV0(?,?,?,?,?,?)}");
						}
						//彩信充值或回收
						else if(oprType == 2 || oprType == 3)
						{
							cs = connection.prepareCall("{call LF_MMSBALANCEV0(?,?,?,?,?,?)}");
						}
						cs.setLong(1, supDepId);
						cs.setLong(2, depId);
						cs.setLong(3, count);
						cs.setString(4, corpCode);
						
						//充值回收标识
						if(oprType == 0 || oprType == 2)
						{
							cs.setLong(5, 1);//标识,1:充值 2:回收
						}
						else if(oprType == 1 || oprType == 3)
						{
							cs.setLong(5, 2);//标识,1:充值 2:回收
							oprInfoStatu = "成功，共回收费用：";
						}
						cs.registerOutParameter(6, java.sql.Types.INTEGER);
						cs.execute();
						//返回值
						result = cs.getInt(6);
					}
					//短信充值成功，更改短信告警阀值的已告警次数
					if(oprType == 0 && result == 0)
					{
						boolean falg1 = false;
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
						objectMap.put("alarmedCount", "0");

						conditionMap.put("targetId", String.valueOf(depId));
						conditionMap.put("corpCode", corpCode);
						falg1 = empDao.update(LfDepUserBalance.class,objectMap,conditionMap);
						if(falg1)
						{
							EmpExecutionContext.info("更新短信告警次数成功！");
						}
						else
						{
							EmpExecutionContext.error("更新短信告警次数失败,depId:"+depId+",企业编码："+corpCode);
						}
					}
					//彩信充值成功，更改彩信告警阀值的已告警次数
					if(oprType == 2 && result == 0)
					{
						boolean falg2 = false;
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
						objectMap.put("mmsAlarmedCount", "0");
						
						conditionMap.put("targetId", String.valueOf(depId));
						conditionMap.put("corpCode", corpCode);
						falg2 = empDao.update(LfDepUserBalance.class,objectMap,conditionMap);
						if(falg2)
						{
							EmpExecutionContext.info("更新彩信告警次数成功！");
						}
						else
						{
							EmpExecutionContext.error("更新彩信告警次数失败,depId:"+depId+",企业编码："+corpCode);
						}
					}
			}
			catch (SQLException e)
			{
				EmpExecutionContext.error(e, oprInfo + "失败!");
				result = -1;
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, oprInfo + "接口调用异常!");
			result = -9999;
		}
		finally{		
			if (cs != null)
			{
				try {
					cs.close();
				} catch (SQLException e) {
					EmpExecutionContext.error(e, oprInfo + "接口关闭存储过程异常!");
					
				}
			}
			empTransDao.closeConnection(connection);
			
			try
			{
				//写操作日志
				String opContent;
				if(result == 0)
				{
					opContent = oprInfo + oprInfoStatu + count;
					spLog.logSuccessString(userName, oprInfo, opType, opContent, corpCode);
					EmpExecutionContext.info("充值/回收管理", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "设置短彩信阀值成功[操作，金额]（"+oprInfo+"，"+count+"）", "OTHER");
				}
				else
				{
					opContent ="短彩信充值、回收接口调用异常。错误码："+result;
					spLog.logFailureString(userName, oprInfo, opType, opContent, null, corpCode);
					EmpExecutionContext.info("充值/回收管理", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "设置短彩信阀值失败[操作，金额，错误码]（"+oprInfo+"，"+count+"，"+result+"）", "OTHER");
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "短彩信充值、回收接口调用写操作日志失败");
			}
			//充值回收 处理余额增加的机构 将其已提醒次数置零
			updateAlarmedCount(depId, type);
		}
		return result;	
	}
	else
	{
		String opContent ="短彩信充值、回收接口获取参数异常。错误码："+result;
		spLog.logFailureString(lfSysuser.getUserName(), "短彩信充值、回收", opType, opContent, null, lfSysuser.getCorpCode());
		EmpExecutionContext.error("短彩信充值、回收接口接口获取参数异常，oprType:" + oprType +";lfSysuser:" + lfSysuser);
		return result;	
	}
}
	/**
	 * 更新已提醒次数
	 * @description    
	 * @param depId 机构ID
	 * @param type 1:充值  2 回收
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-11-29 上午10:43:39
	 */
	public void updateAlarmedCount(Long depId,int type){
		if(depId==null){
			return;
		}
		try
		{
			//如果为回收 且机构不为总机构
			if(type==2){
				LfDep dep=empDao.findObjectByID(LfDep.class, depId);
				if(dep.getSuperiorId()==0){
					return;
				}
				dep=empDao.findObjectByID(LfDep.class, dep.getSuperiorId());
				//需要更新的机构ID
				depId=dep.getDepId();
			}
			LinkedHashMap<String, String> objMap=new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> conMap=new LinkedHashMap<String, String>();
			objMap.put("alarmedCount", "0");
			conMap.put("targetId", String.valueOf(depId));
			empDao.update(LfDepUserBalance.class, objMap, conMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新机构阀值短信已提醒次数失败！");
		}
		
	}
	// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
	/**
	 *    查询指定机构ID下的子级机构的充值与回收信息
	 * @param depId   机构ID
	 * @return
	 * @throws Exception
	 */
	public List<LfDepBalanceVo> getDepBalanceVoByOperator(Long userId, String corpCode,String depId,String depName,PageInfo pageInfo)
	{
		List<LfDepBalanceVo> list = null;
		try{
			list = new LfDepBalanceDAO().findLfDepBalanceVosByOperator(userId, corpCode,depId,depName,pageInfo);
		}catch(Exception e){
			EmpExecutionContext.error(e,"查询指定机构ID下的子级机构的充值与回收信息出现异常！");
		}
		return list;
	}
	
	
	
	// EMP5.7新需求：增加对操作员充值和回收权限   by pengj	
	/**
	 * 获取操作员拥有的所有充值回收权限机构id的list（包括操作员表在权限表中的机构id和操作员默认的充值回收权限机构id）
	 * @param userId 操作员id
	 * @return 机构的集合
	 * @throws Exception
	 * @author pengj
	 * @datetime 2015-12-3 下午15:00:00
	 */
	public List<Long> getBalancePriDepsIds(String userId, String depId, String corpCode)
	{
		List<Long> getBalancePriDepsIds = null;
		try{
			
			getBalancePriDepsIds = new LfDepBalanceDAO().getBalancePriDepsIds(userId, depId, corpCode);
		}catch(Exception e){
			EmpExecutionContext.error(e,"获取充值与回收机构id出现异常！");
		}
		return getBalancePriDepsIds;
	}
	
	
	/**
	 * 通过机构权限中的机构，查出其所有父机构，用于创建充值回收权限的机构树，此方法可以优化，该未优化版本暂不使用
	 * @param userId 当前操作员的ID
	 * @param depId 机构id
	 * @param corpCode 企业编码
	 * @return 机构的集合
	 * @throws Exception  			 
	 * @author pengj
	 * @datetime 2015-11-25 下午15:00:00
	 */
	/*
	public List<LfDep> getOperatorBalancePriDeps(String userId,String depId, String corpCode) throws Exception {
		List<LfDep> deps = null;
		try{
			deps = new LfDepBalanceDAO().getOperatorBalancePriDeps(userId,depId, corpCode);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取充值回收权限机构树信息出现异常！");
		}
		return deps;
	}
	*/
	
	/**
	 * 通过机构权限中的机构，查出其所有父机构，用于创建充值回收权限的机构树，此方法可以优化，该未优化版本暂不使用
	 * @param userId 当前操作员的ID
	 * @param depId 机构id
	 * @param corpCode 企业编码
	 * @return 机构的集合
	 * @throws Exception  			 
	 * @author pengj
	 * @datetime 2015-11-25 下午15:00:00
	 */
	
	public List<LfDep> getOperatorBalancePriDeps(String userId,String depId, String corpCode, String sysDepId) throws Exception {
		List<LfDep> deps = null;
		try{
			deps = new LfDepBalanceDAO().getOperatorBalancePriDeps(userId,depId, corpCode, sysDepId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取充值回收权限机构树信息出现异常！");
		}
		return deps;
	}
	
	// end
	
	
	// end
	
	
	/**
	 * 根据机构id获得机构的短彩余额信息
	 * @Title: getDepBalanceBydepId
	 * @Description: TODO
	 * @param depId 
	 * @return LfDepBalanceVo
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-11-20 上午10:23:48
	 */
	public LfDepBalanceVo getDepBalanceBydepId(String depId,String lgCorpcode){
		LfDepBalanceVo depBalance = null;
		try {
			if(depId != null && !"".equals(depId.trim()))
			{
				depBalance = new LfDepBalanceDAO().getDepBalanceBydepId(Long.valueOf(depId),lgCorpcode);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"根据机构ID获取机构的短彩余额异常");
		}		
		return depBalance;
		
	}
	/**
	 * 根据机构id获取子机构的信息
	 */
	public List<LfDepBalanceDefVo> getDepSon(Long depId,String lgCorpcode){
		List<LfDepBalanceDefVo> lfDeps = null;
		
		try {
			if(depId != null && lgCorpcode != null && !"".equals(lgCorpcode.trim()))
			{
				lfDeps = new LfDepBalanceDAO().getDepSon(depId,lgCorpcode);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"根据机构ID获取子机构异常");
		}
		
		return lfDeps;
	}
	
	/**
	 * 根据机构id获取子机构的信息
	 */
	public List<LfDepBalanceDefVo> getDepSonNoAdmin(Long depId,String lgCorpcode,Long lguserid){
		List<LfDepBalanceDefVo> lfDeps = null;
		
		try {
			if(lguserid != null && depId != null && lgCorpcode != null && !"".equals(lgCorpcode.trim()))
			{
				lfDeps = new LfDepBalanceDAO().getDepSonNoAdmin(depId,lgCorpcode,lguserid);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"根据机构ID获取子机构异常");
		}
		
		return lfDeps;
	}
	/**
	 * 检查改操作员是否对选中的机构的子机构有批量充值的权限
	 */
	public boolean checkLguser(Long depId,Long lguserid,String lgcorpCode){
		boolean falg = false;
		try {
			if(depId != null && lguserid != null && lgcorpCode != null)
			{
				falg = new LfDepBalanceDAO().checkLguser(depId, lguserid, lgcorpCode);
			}
		} catch (Exception e) {
			falg = false;
			EmpExecutionContext.error(e,"查询该操作员是否有对选中机构的子机构有批量充值的权限异常！");
		}
		return falg;
	}
	
	/**
	 * 批量充值
	 * @Title: addBalanceAll
	 * @Description: TODO
	 * @param depId
	 * @param smsMap
	 * @param lgUserId
	 * @return 返回信息
	 * 	 		0:短信充值成功
	 * 		   -1:短信充值失败
	 * 		   -2:充值成功，保存默认值失败
	 *  	   -3:该机构下没有可用余额
	 *  	   -4:充值数据大于可分配余额！
	 *  	   -5:获取机构余额信息失败
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-11-24 下午03:51:36
	 */
	public int addBalanceAll(LfSysuser sysuser,List<LfDepBalanceVo> balanceVos,
			Long sonDepBalanceAllsms,Long sonDepBalanceAllmms,Long depId){
		int returnNum = -1;
		//获得一个链接
		Connection conn = null;
		try {
			conn = empTransDao.getConnection();
			//开启事务
			empTransDao.beginTransaction(conn);
			//判断短信或彩信的总充值数量大于0
			if(sonDepBalanceAllmms >0 || sonDepBalanceAllsms > 0)
			{
				//先扣除上级机构的短彩余额
				int num = UpdateBalance(conn,sysuser,balanceVos,sonDepBalanceAllsms,sonDepBalanceAllmms,depId);
				//如果等于0说明没有余额记录或余额不足
				if(num == 0)
				{
					//判断是否余额不足或无余额信息记录
					returnNum = findDepBalance(conn,sysuser,depId);
				}
				//扣费成功开始充值
				else if(num == 1)
				{
					returnNum = stratAddBalance(conn,sysuser,depId,balanceVos);
				}
				else
				{
					EmpExecutionContext.error("机构批量充值失败，扣除上级机构余额失败。");
					empTransDao.rollBackTransaction(conn);
					returnNum = -1;
					return returnNum;
				}
			}
			if(returnNum < 0)
			{
				//回滚事务
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("机构批量充值失败，returnNum:"+returnNum);
			}
			else
			{
				//提交事务
				empTransDao.commitTransaction(conn);
			}
		} catch (Exception e) {
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			returnNum = -1;
			EmpExecutionContext.error(e,"批量充值失败！");
		}
		finally
		{
			//添加充值日志
			BalanceLog(sysuser,depId,balanceVos,returnNum);
			//关闭数据链接
			empTransDao.closeConnection(conn);
		}
		return returnNum;
	}
	
	/**
	 * 扣除机构短彩余额
	 * @Title: UpdateBalance
	 * @Description: TODO
	 * @param sysuser
	 * @param balanceVos
	 * @param sonDepBalanceAllsms
	 * @param sonDepBalanceAllmms
	 * @param depId
	 * @return int
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-12-1 下午01:55:42
	 */
	private int UpdateBalance(Connection conn, LfSysuser sysuser,List<LfDepBalanceVo> balanceVos,
			Long sonDepBalanceAllsms,Long sonDepBalanceAllmms,Long depId){
		int sum = -1;
		try {
		String sql = "UPDATE LF_DEP_USER_BALANCE SET SMS_BALANCE = SMS_BALANCE - "+sonDepBalanceAllsms+",MMS_BALANCE = MMS_BALANCE -"+sonDepBalanceAllmms+" WHERE CORP_CODE= '"+sysuser.getCorpCode()+"' " +
					 " AND SMS_BALANCE >= "+sonDepBalanceAllsms+" AND MMS_BALANCE >= "+sonDepBalanceAllmms+" AND TARGET_ID ="+depId+"";
			sum = empTransDao.executeBySQLReturnCount(conn, sql);
			
		} catch (Exception e) {
			sum = - 5;
			EmpExecutionContext.error(e,"扣除短彩信余额失败！");
		}
		
		return sum;
	}
	/**
	 * 查询机构余额是否有记录
	 * @Title: findDepBalance
	 * @Description: TODO
	 * @param conn
	 * @param sysuser
	 * @param depId
	 * @return int
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-12-1 下午02:34:39
	 */
	private int findDepBalance(Connection conn, LfSysuser sysuser,Long depId){
		int sum = -1;
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", sysuser.getCorpCode());
			conditionMap.put("targetId", String.valueOf(depId));
			
			List<LfDepUserBalance> lfDepUserBalances = empDao.findListByCondition(LfDepUserBalance.class, conditionMap, null);
			if(lfDepUserBalances == null || lfDepUserBalances.size() <= 0)
			{
				sum = -3;
				EmpExecutionContext.error("批量充值失败,该机构下没有可用余额！");
			}
			else
			{
				sum = -4;
				EmpExecutionContext.error("批量充值余额失败,充值数据大于可分配余额！");
			}
		} catch (Exception e) {
			sum = -5;
			EmpExecutionContext.error(e,"查询机构余额是否有记录异常！");
		}
		
		return sum;
	}
	/**
	 * 开始批量充值
	 * @Title: StratAddBalance
	 * @Description: TODO
	 * @param conn
	 * @param sysuser
	 * @param depId
	 * @param balanceVos
	 * @return int 返回0成功，-1失败
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-12-1 下午03:07:54
	 */
	private int stratAddBalance(Connection conn, LfSysuser sysuser,Long depId,List<LfDepBalanceVo> balanceVos){
		//返回值
		int sum = -1;
		//需要充值的机构Id
		Long sonDepId;
		//短信充值数量
		Long smsCount;
		//彩信充值数量
		Long mmsCount;
		//企业编码
		String corpCode = "";
		try {
			corpCode = sysuser.getCorpCode();
			List<LfDepUserBalance> lfDepUserBalances = new ArrayList<LfDepUserBalance>();
			for (LfDepBalanceVo lfDepBalanceVo : balanceVos) {
				sonDepId = lfDepBalanceVo.getDepId();
				smsCount = lfDepBalanceVo.getSmsBalance();
				mmsCount = lfDepBalanceVo.getMmsBalance();
				String conSql = "UPDATE LF_DEP_USER_BALANCE SET SMS_BALANCE = SMS_BALANCE + "+smsCount+", MMS_BALANCE = MMS_BALANCE +"+mmsCount;
				//充值时，更新短彩告警阀值告警次数
				String upSql = "";
				//短信充值 彩信不充值
				if(smsCount > 0 && mmsCount < 1)
				{
					upSql = ", ALARMED_COUNT = 0";
				}
				//短信不充值 彩信充值
				else if(smsCount < 1 && mmsCount > 0)
				{
					upSql = ", MMSALARMEDCOUNT = 0";
				}
				//短信和彩信都充值
				else if (smsCount > 0 && mmsCount > 0) 
				{
					upSql = ", ALARMED_COUNT = 0 , MMSALARMEDCOUNT = 0";
				}
				String whereSql = " WHERE CORP_CODE = '"+corpCode+"' AND TARGET_ID = "+sonDepId+"";
				
				//总sql
				String sql = conSql + upSql + whereSql;
				int sun = empTransDao.executeBySQLReturnCount(conn, sql);
				//0表示余额信息表无记录,新增记录
				if(sun == 0)
				{
					LfDepUserBalance depUserBalance = new LfDepUserBalance();
					//充值
					depUserBalance.setTargetId(sonDepId);
					depUserBalance.setSmsBalance(smsCount);
					depUserBalance.setSmsCount(0L);
					depUserBalance.setMmsBalance(mmsCount);
					depUserBalance.setMmsCount(0L);
					depUserBalance.setCorpCode(corpCode);
					depUserBalance.setMmsAlarmedCount(0);
					lfDepUserBalances.add(depUserBalance);
				}
				//充值成功
				else if(sun == 1)
				{
					sum = 0;
				}
				else
				{
					EmpExecutionContext.error("批量充值失败，更新机构余额信息异常，SMS_BALANCE:"+smsCount+",MMS_BALANCE:"+mmsCount+",CORP_CODE:"+corpCode+",TARGET_ID:"+sonDepId);
					return -1;
				}
			}
			if(lfDepUserBalances != null && lfDepUserBalances.size() > 0)
			{
				int num = empTransDao.save(conn, lfDepUserBalances, LfDepUserBalance.class);
				if(num < 1)
				{
					//失败
					sum = -1;
					EmpExecutionContext.error("批量充值失败，新增机构余额失败。企业编码："+corpCode);
					return sum;
				}
				else
				{
					sum = 0;//成功
				}
			}
		} catch (Exception e) {
			sum = -1;
			EmpExecutionContext.error(e,"批量充值异常！企业编码："+corpCode);
		}
		return sum;
	}
	
	
	/**
	 * 添加充值日志
	 * @Title: addBalanceLog
	 * @Description: TODO 
	 * @return void
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-11-30 下午08:04:23
	 */
	public  void BalanceLog(LfSysuser sysuser,Long superDepId,List<LfDepBalanceVo> balanceVos,int returnmsg) {
		
		List<LfDepRechargeLog> depRechargeLogs = new ArrayList<LfDepRechargeLog>();
		//子机构的id
		Long sonDepId;
		//短信充值数量
		Long smsCount;
		//彩信充值
		Long mmsCount;
		try {
			//企业编码
			String corpcode = sysuser.getCorpCode();
			Long lgguid = sysuser.getGuId();
			for (LfDepBalanceVo balanceVo : balanceVos) {
				sonDepId = balanceVo.getDepId();
				smsCount = balanceVo.getSmsBalance();
				mmsCount = balanceVo.getMmsBalance();
				//短信
				if(smsCount > 0)
				{
					LfDepRechargeLog depRechargeLog = new LfDepRechargeLog();
					depRechargeLog.setCorpCode(corpcode);
					depRechargeLog.setCount(smsCount);
					depRechargeLog.setSrcTargetId(superDepId);
					depRechargeLog.setMsgType(1);//充值类型，1短信，2彩信
					depRechargeLog.setDstTargetId(sonDepId);
					// 充值类型（100为总公司充值/回收；101机构为机构充值/回收； 102机构为操作员充值/回收）
					depRechargeLog.setOptType(101);
					depRechargeLog.setOptId(lgguid);
					//充值源名称/描述--->充值目的名称/描述
					depRechargeLog.setOptInfo(returnmsg + " ");
					//备注
					depRechargeLog.setMemo(" ");
					//结果0成功，1失败
					if(returnmsg == 0){ 
						depRechargeLog.setResult(0);
					}else{
						depRechargeLog.setResult(1);
					}
					depRechargeLog.setOptDate(new Timestamp(System.currentTimeMillis()));
					depRechargeLogs.add(depRechargeLog);
				}
				//彩信
				if(mmsCount > 0)
				{
					LfDepRechargeLog depRechargeLog = new LfDepRechargeLog();
					depRechargeLog.setCorpCode(corpcode);
					depRechargeLog.setCount(mmsCount);
					depRechargeLog.setSrcTargetId(superDepId);
					depRechargeLog.setMsgType(2);//充值类型，1短信，2彩信
					depRechargeLog.setDstTargetId(sonDepId);
					// 充值类型（100为总公司充值/回收；101机构为机构充值/回收； 102机构为操作员充值/回收）
					depRechargeLog.setOptType(101);
					depRechargeLog.setOptId(lgguid);
					//充值源名称/描述--->充值目的名称/描述
					depRechargeLog.setOptInfo(returnmsg + " ");
					//备注
					depRechargeLog.setMemo(" ");
					//结果0成功，1失败
					if(returnmsg == 0){ 
						depRechargeLog.setResult(0);
					}else{
						depRechargeLog.setResult(1);
					}
					depRechargeLog.setOptDate(new Timestamp(System.currentTimeMillis()));
					depRechargeLogs.add(depRechargeLog);
				}
			}
			empDao.save(depRechargeLogs, LfDepRechargeLog.class);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"添加充值日志异常！");
		}
	}
	
	/**
	 * 设置机构默认值
	 * @Title: addDefault
	 * @Description: TODO
	 * @param sysuser
	 * @param balanceVos
	 * @return int 0成功，-1失败
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-12-1 下午07:30:32
	 */
	public  int addDefault(LfSysuser sysuser,List<LfDepBalanceVo> balanceVos) {
		//返回值
		int sum = -1;
		//机构Id
		Long depId;
		//短信数量
		Long smsCount;
		//彩信数量
		Long mmsCount;
		//当前操作员Id
		Long userId;
		//当前企业编码
		String corpCode;
		Connection conn = null;
		try {
			//企业编码
			corpCode = sysuser.getCorpCode();
			userId = sysuser.getUserId();
			Timestamp time = new Timestamp(System.currentTimeMillis());
			//先删除原有的再新增
			//创建一个链接
			conn = empTransDao.getConnection();
			//开启事务
			empTransDao.beginTransaction(conn);
			//机构ids
			String depIds = "";
			List<LfBalanceDef> balanceDefs = new ArrayList<LfBalanceDef>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			for (LfDepBalanceVo lfDepBalanceVo : balanceVos) {
				depId = lfDepBalanceVo.getDepId();
				//短信余额
				smsCount = lfDepBalanceVo.getSmsBalance();
				//彩信余额
				mmsCount = lfDepBalanceVo.getMmsBalance();
				
				LfBalanceDef balanceDef = new LfBalanceDef();
				balanceDef.setDepid(depId);
				balanceDef.setSmscount(smsCount);
				balanceDef.setMmscount(mmsCount);
				balanceDef.setCorpcode(corpCode);
				balanceDef.setModiuserid(userId);
				balanceDef.setCreatetime(time);
				balanceDef.setModitime(time);
				balanceDefs.add(balanceDef);
				
				depIds += depId+",";
			}
			//去掉最后的逗号
			depIds = depIds.substring(0,depIds.length()-1);
			objectMap.put("depid", depIds);
			objectMap.put("corpcode", corpCode);
			//删除余额
			int nun = empTransDao.delete(conn,LfBalanceDef.class,objectMap);
			//删除成功,再添加
			if(nun > -1)
			{
				int mun = empTransDao.save(conn,balanceDefs, LfBalanceDef.class);	
				if(mun > 0)
				{
					//添加成功提交事务
					sum = 0;
					empTransDao.commitTransaction(conn);
				}
				else
				{
					sum = -1;
					//失败，回滚事务
					empTransDao.rollBackTransaction(conn);
					EmpExecutionContext.error("设置机构批量充值数量默认值失败，新增记录失败。");
					return sum;
				}
			}
			else
			{
				sum = -1;
				//删除失败，回滚事务
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("设置机构批量充值数量默认值失败，删除记录失败。 企业编码："+corpCode);
				return sum;
			}
		} catch (Exception e) {
			sum = -1;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"设置批量充值默认值异常！");
		}
		finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return sum;
	}
	/**
	 * 撤销告警阀值
	 * @Title: deleteAlarm
	 * @Description: TODO
	 * @param depId
	 * @param corpCode
	 * @return boolean
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-12-15 下午02:12:47
	 */
	public  boolean deleteAlarm(Long depId,String corpCode){
		boolean falg = false;
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("smsAlarm", "0");
			objectMap.put("mmsAlarm", "0");
			objectMap.put("alarmName", " ");
			objectMap.put("alarmPhone", " ");
			
			conditionMap.put("targetId", String.valueOf(depId));
			conditionMap.put("corpCode", corpCode);
			falg = empDao.update(LfDepUserBalance.class, objectMap,conditionMap);
			
		} catch (Exception e) {
			falg = false;
			EmpExecutionContext.error(e,"撤销告警阀值异常！depId:"+depId+",corpCode:"+corpCode);
		}
		return falg;
	}
	
}
