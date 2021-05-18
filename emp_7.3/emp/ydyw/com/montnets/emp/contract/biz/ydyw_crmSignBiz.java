/**
 * Program  : ydyw_crmSignQueryBiz.java
 * Author   : zousy
 * Create   : 2015-1-5 下午02:45:55
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.contract.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.contract.dao.ydyw_crmSignDao;
import com.montnets.emp.entity.client.LfClient5Pro;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.client.LfClientDepSp;
import com.montnets.emp.entity.ydyw.*;
import com.montnets.emp.mobilebus.biz.BuckleFeeBiz;
import com.montnets.emp.mobilebus.dao.BuckleFeeDAO;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2015-1-5 下午02:45:55
 */
public class ydyw_crmSignBiz extends SuperBiz
{
	private ydyw_crmSignDao crmSignDao = new ydyw_crmSignDao();
	private SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private BuckleFeeBiz buckleBiz = new BuckleFeeBiz();
	private sendCrmSms crmSms = new sendCrmSms();
	public List<DynaBean> getcrmSignList(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception{
		return crmSignDao.getcrmSignList(conditionMap, pageInfo);
	}
	public List<DynaBean> getTaocans(List<String> ids) throws Exception{
		return crmSignDao.getTaocans(ids);
	}
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,String depId) throws Exception {
		List<LfClientDep> deps = null;
		try{
			deps = new AddrBookSpecialDAO().getCliSecondDepTreeByUserIdorDepId(userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取客户机构树信息出现异常！");
		}
		return deps;
	}
	
	public List<DynaBean> findClientInfoByPhone(String phone,String corpCode){
		return crmSignDao.findClientInfoByPhone(phone,corpCode);
	}
	public List<DynaBean> findClientDepsByGuid(String guid,String corpCode){
		return crmSignDao.findClientDepsByGuid(guid,corpCode);
	}
	
	/****
	 * 改变签约状态
	* @Description: TODO
	* @param @param id 
	* @param @param state 状态
	* @param @return 
	* @return boolean 是否修改成功
	 */
	public boolean updateState(String id,String state){
		List<DynaBean> tcBeans = new ArrayList<DynaBean>();
		LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
		condMap.put("contractId",id);
		//condMap.put("state","0");
		if("0".equals(state)){
			condMap.put("isvalid","2");
			 
		}else if("1".equals(state)){
			condMap.put("isvalid","0");
		}
		Connection conn = null;
		boolean result = false;
		try
		{
			tcBeans = crmSignDao.findTaocansByCond(condMap);//查询出当前签约用户需要处理的套餐 （不考虑套餐禁用启用）
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			//改变签约状态 时间等
			result = crmSignDao.updateState(conn,id,state);
			if(result){//更改标示状态
				if("1".equals(state)){//取消签约
					buckleFeeForCancelContract(tcBeans,conn, Long.valueOf(id));
				}
				if("0".equals(state)){//恢复签约
					updateBuktime(tcBeans,conn, Long.valueOf(id));
				}
			}
			empTransDao.commitTransaction(conn);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更改签约状态异常！");
			empTransDao.rollBackTransaction(conn);
			result = false;
		}finally{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/***
	 *  保存与修改签约客户信息
	* @Description: TODO
	* @param @param map 需要处理的数据
	* @param @return
	* @return JSONObject
	 */
	@SuppressWarnings("finally")
	public JSONObject saveOrUpdate(Map<String, String> map){
		String id = map.get("id");
		String guidStr = map.get("guid");
		String corpcode = map.get("lgcorpcode");
    	JSONObject json = new JSONObject();
    	boolean isnewClient = map.get("noexist") != null;
		if(StringUtils.isBlank(guidStr) || StringUtils.isBlank(corpcode)){
		   	json.put("errcode", "-1");
			return json;
		}
		Long guid = Long.valueOf(guidStr);
		boolean isAdd = StringUtils.isBlank(id);
		Connection conn = empTransDao.getConnection();
		try
		{
			String phone = map.get("phone");
			String msg = "";
			String depIds = map.get("depId");
			String[] codes = map.get("codes").split(",");
			Long sysdepId = Long.valueOf(map.get("sysdepId"));
			Long sysuserId = Long.valueOf(map.get("sysuserId"));
			LfContract contract = new LfContract();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			if(isAdd){
				contract.setContractdate(timestamp);
				contract.setContractstate(0);
				contract.setIsvalid("0");
				contract.setContractdep(sysdepId);
				contract.setContractuser(sysuserId);
				contract.setContractsource(0);//柜台录入
				contract.setCorpcode(corpcode);
			}else{
				contract = empDao.findObjectByID(LfContract.class, Long.parseLong(id));
			}
			contract.setMobile(map.get("phone"));
			contract.setCustomname(map.get("cliname"));
			contract.setClientcode(map.get("cliCode"));
			contract.setAcctidenttype(map.get("cardType"));
			contract.setAcctidentno(map.get("cardNo"));
			contract.setAddress(map.get("address"));
			contract.setAcctno(map.get("account"));
			contract.setAcctname(map.get("accountName"));
			contract.setDebitaccount(map.get("feeAccount"));
			contract.setDebitacctname(map.get("feeAccountName"));
			contract.setUpdatetime(timestamp);
			contract.setGuid(guid);
			contract.setDepid(sysdepId);
			contract.setUserid(sysuserId);
			
			empTransDao.beginTransaction(conn);
			//签约账号唯一性检查
			String acctNo = contract.getAcctno();
			String _acctNo = map.get("_account");
			if(StringUtils.isNotBlank(acctNo)){
				if(!acctNo.equals(_acctNo) && crmSignDao.findByAcctNo(conn, acctNo,corpcode) > 0){
					json.put("errcode", "-2");//签约账号已存在
					return json;
				}
			}else{
				String _phone = map.get("_phone");
				if((StringUtils.isNotBlank(_acctNo)||!contract.getMobile().equals(_phone)) &&crmSignDao.findByMobile(conn, contract.getMobile(),corpcode) > 0){
					json.put("errcode", "-4");//该签约用户已存在为空的签约账号
					return json;
				}
			}
			
			//客户编号唯一性检查
			String clientcode = contract.getClientcode();
			if(isnewClient && StringUtils.isNotBlank(clientcode)){
				if(crmSignDao.findByClientcode(conn, clientcode,corpcode) > 0){
					json.put("errcode", "-3");//客户已存在
					return json;
				}
			}
			if(StringUtils.isBlank(clientcode)){
				contract.setClientcode(String.valueOf(contract.getGuid()));
			}
			//保存或更新签约信息
			Long contractId = null;
			if(isAdd){
				contractId = empTransDao.saveObjectReturnID(conn, contract);
				contract.setContractid(contractId);
			}else{
				empTransDao.update(conn, contract);
			}
			contractId = contract.getContractid();
			
			//同步客户通讯录
			if(isnewClient){//新增客户直接同步至客户通讯里
				LfClient5Pro client = new LfClient5Pro();
				client.setClientId(guid);
				client.setGuId(guid);
				client.setCorpCode(corpcode);
				client.setDepId(sysdepId);
				client.setName(map.get("cliname"));
				client.setMobile(map.get("phone"));
				client.setSex(2);
				client.setClientCode(StringUtils.defaultString(map.get("cliCode"), guidStr));
				syncClient(conn, client, depIds.split(","));
			}
			crmSignDao.updateClientState(conn, guid, 1);
			
			//guid发生变化 即关联客户变化 则 处理修改前的签约状态
			String _guidStr = map.get("_guid");
			if(StringUtils.isNotBlank(_guidStr)&&!_guidStr.equals(guidStr)){
				LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
				condMap.put("guid", _guidStr);
				condMap.put("isvalid", "0");
				List<LfContract> contracts = empDao.findListByCondition(LfContract.class, condMap, null);
				if(contracts==null || contracts.size() == 1){//该客户只存在当前一个签约 则 更新客户签约状态
					crmSignDao.updateClientState(conn, Long.valueOf(_guidStr), 0);
				}
			}
			
			//处理机构套餐统计表
			List<String> addList = new ArrayList<String>();
			Set<String> codeSet = crmSignDao.findDepTaocan(conn, sysuserId, codes);
			LfDepTaocan depTaocanTmp = new LfDepTaocan();
			depTaocanTmp.setCorpcode(corpcode);
			depTaocanTmp.setContractdep(sysdepId);
			depTaocanTmp.setContractuser(sysuserId);
			depTaocanTmp.setDepid(sysdepId);
			depTaocanTmp.setUserid(sysuserId);
			List<LfDepTaocan> deptcs = new ArrayList<LfDepTaocan>();
			for(int i=0;i<codes.length;i++){
				String code = codes[i];
				addList.add(code);
				if(codeSet.contains(code)){
					continue;
				}
				LfDepTaocan bean = (LfDepTaocan) BeanUtils.cloneBean(depTaocanTmp);
				bean.setTaocancode(codes[i]);
				deptcs.add(bean);
			}
			empTransDao.save(conn, deptcs, LfDepTaocan.class);
			
			//处理签约套餐关联信息
			String[] moneys = map.get("moneys").split(",");
			String[] types = map.get("types").split(",");
			String[] names = map.get("names").replaceAll("&lt;", "<").replaceAll("&gt;", ">").split("<>");
			//待删除的套餐列表
			List<DynaBean> delList = new ArrayList<DynaBean>();
			//修改前后相同的套餐列表
			List<String> sameList = new ArrayList<String>();
			List<String> nameList = new ArrayList<String>();
			if(!isAdd){
				LinkedHashMap<String, String> map2 = new LinkedHashMap<String, String>(); 
				//map2.put("state", "0"); 禁用启用全部
				map2.put("contractId", String.valueOf(contractId));
				map2.put("lgcorpcode", contract.getCorpcode());
				//原始套餐
				List<DynaBean> lists = findSelTaocans(map2);
				Iterator<DynaBean> its = lists.iterator();
				while(its.hasNext())
				{
					DynaBean bean = its.next();
					String code = String.valueOf(bean.get("taocan_code"));
					if(addList.contains(code)){
						sameList.add(code);
						int i = addList.indexOf(code);
						nameList.add(names[i]);
						addList.remove(i);
						moneys = (String[]) ArrayUtils.remove(moneys, i);
						types = (String[]) ArrayUtils.remove(types, i);
						names = (String[]) ArrayUtils.remove(names, i);
						its.remove();
					}
				}
				delList = lists;
			}
			//处理新增的签约套餐关系
			LfContractTaocan ctTmp = new LfContractTaocan();
			ctTmp.setContractid(contractId);
			ctTmp.setDebitaccount(contract.getDebitaccount());
			ctTmp.setContractdep(contract.getDepid());
			ctTmp.setContractuser(contract.getUserid());
			ctTmp.setUserid(contract.getUserid());
			ctTmp.setDepid(contract.getContractdep());
			ctTmp.setCorpcode(contract.getCorpcode());
			ctTmp.setGuid(contract.getGuid());
			ctTmp.setCreatetime(timestamp);
			ctTmp.setUpdatetime(timestamp);
			ctTmp.setIsvalid("0");
			List<LfContractTaocan> ctLists = new ArrayList<LfContractTaocan>();
			for(int i=0;i<addList.size();i++){
				msg +="、["+names[i]+"]";
				LfContractTaocan bean = (LfContractTaocan) BeanUtils.cloneBean(ctTmp);
				bean.setTaocancode(addList.get(i));
				bean.setTaocantype(Integer.valueOf(types[i]));
				bean.setTaocanmoney(Long.valueOf(moneys[i]));
				bean.setTaocanname(names[i]);
				bean.setBucklefeetime(new Timestamp(buckleBiz.buckleTime(addList.get(i)).getTimeInMillis()));
				ctLists.add(bean);
			}
			empTransDao.save(conn, ctLists, LfContractTaocan.class);
			
			//处理作删除的签约套餐关系
			ctTmp = new LfContractTaocan();
			ctTmp.setUserid(contract.getUserid());
			ctTmp.setDepid(contract.getContractdep());
			ctTmp.setUpdatetime(timestamp);
			ctTmp.setIsvalid("1");//删除 （禁用）标示
			for(int i=0;i<delList.size();i++){
				LfContractTaocan bean = (LfContractTaocan) BeanUtils.cloneBean(ctTmp);
				bean.setId(Long.valueOf(String.valueOf(delList.get(i).get("id"))));
				empTransDao.update(conn, bean);
			}
			//处理前后相同的签约套餐关系 状态修改为正常状态
			if(sameList.size()>0){
				IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
				LinkedHashMap<String, String> objMap = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
				condMap.put("isvalid", "0,1");
				condMap.put("contractid", String.valueOf(contract.getContractid()));
				condMap.put("corpcode", contract.getCorpcode());
				objMap.put("isvalid", "0");
				objMap.put("debitaccount", contract.getDebitaccount());
				objMap.put("updatetime",genericDAO.getTimeCondition(sdf.format(new Date())));
				for(int i=0;i<sameList.size();i++){
					String code = sameList.get(i);
					msg +="、["+nameList.get(i)+"]";
					condMap.put("taocancode", code);
					objMap.put("bucklefeetime", genericDAO.getTimeCondition(sdf.format(buckleBiz.buckleTime(code).getTime())));
					empTransDao.update(conn, LfContractTaocan.class, objMap, condMap);
				}

			}
			empTransDao.commitTransaction(conn);
			contractBucklefee(contractId);
			if(msg.length()>0)
			{
				msg = msg.substring(1);
			}
			msg = "您已成功签约"+msg+"套餐业务，感谢您的使用";
			String result = crmSms.sendMsg(corpcode, phone, msg);
			if("000".equals(result)){
				json.put("errcode", "0");
			}else if("nosp".equals(result)){
				json.put("errcode", "1");
			}else{
				json.put("errcode", "1");
			}
			return json;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "签约操作异常！");
			empTransDao.rollBackTransaction(conn);
			json.put("errcode", "-99");
			return json;
		}finally{
			empTransDao.closeConnection(conn);
			
		}
	}
	
	/**
	 * @description 同步客户信息   
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @throws Exception 
	 * @datetime 2015-1-15 上午10:28:36
	 */
	public void syncClient(Connection conn,LfClient5Pro client,String[] deps) throws Exception{
			empTransDao.save(conn, client);
			Long clientId = client.getClientId();
			LfClientDepSp clientDepsp = null;
			List<LfClientDepSp> depspList = new ArrayList<LfClientDepSp>();
			for(String depid:deps){
				if(StringUtils.isNotBlank(depid)){
					clientDepsp = new LfClientDepSp();
					clientDepsp.setClientId(clientId);
					clientDepsp.setDepId(Long.valueOf(depid));
					depspList.add(clientDepsp);
				}
			}
			if(depspList.size()>0){
				empTransDao.save(conn, depspList, LfClientDepSp.class);
			}
	}
	
	public List<DynaBean> findTaocans(LinkedHashMap<String, String> condMap) throws Exception{
		return crmSignDao.findTaocans(condMap);
	}
	
	public String getZF(String type,String money){
		String[] zfs = new String[]{"","免费","%d元/月","%d元/季","%d元/年"};
		return zfs[Integer.parseInt(type)].replace("%d",money);
	}

	/**
	 *
	 * @param type		套餐类型
	 * @param money		金额
	 * @param empLangName 国际化处理
	 * @return 固定格式的字符
	 */
	public String getZF(String type,String money,String empLangName){
		String free = "zh_HK".equals(empLangName)?"Free":"zh_CN".equals(empLangName)?"免费":"免費";
		String monthly = "zh_HK".equals(empLangName)?"%d yuan per month":"%d元/月";
		String quarterly = "zh_HK".equals(empLangName)?"%d yuan per quarter":"%d元/季";
		String yearly = "zh_HK".equals(empLangName)?"%d yuan per year":"%d元/年";
		String[] zfs = new String[]{"",free,monthly,quarterly,yearly};
		return zfs[Integer.parseInt(type)].replace("%d",money);
	}

	public List<DynaBean> findSelTaocans(LinkedHashMap<String, String> condMap) throws Exception{
		return crmSignDao.findSelTaocans(condMap);
	}
	
	/**
	 * @description    恢复签约过程中 对有效的套餐计算扣费时间 
	 * @param conn
	 * @param contractId
	 * @throws Exception       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-1-23 下午02:26:44
	 */
	public void updateBuktime(List<DynaBean> tcBeans,Connection conn,long contractId) throws Exception{
		//对所选套餐计算扣费时间 如果扣费时间等于当天时间 则直接扣费 并更新下次扣费时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sBuffer = new StringBuffer("");//扣费信息批量存入文件
		List<LfDeductionsDisp> disps = new ArrayList<LfDeductionsDisp>();//计费流水表
		List<LfDeductionsList> lists = new ArrayList<LfDeductionsList>();//计费统计表
		List<LfContractTaocan> conCts = new ArrayList<LfContractTaocan>();//签约套餐关联表
		for(DynaBean bean:tcBeans){
			String debitAccount = buckleBiz.propVal(bean.get("debitaccount"));
			int tcType = buckleBiz.propVal(bean.get("taocan_type"))==null?2:Integer.valueOf(buckleBiz.propVal(bean.get("taocan_type")));
			int tcMoney = bean.get("taocan_money")==null?0:Integer.parseInt(buckleBiz.propVal(bean.get("taocan_money")));
			String tcCode = buckleBiz.propVal(bean.get("taocan_code"));
			String lastBucklefee = buckleBiz.propVal(bean.get("last_bucklefee"));
			Long id = Long.valueOf(buckleBiz.propVal(bean.get("id")));
			int buckleDay = bean.get("buckle_date")==null?1:Integer.valueOf(buckleBiz.propVal(bean.get("buckle_date")));
			int tryDay = bean.get("try_days")==null?0:Integer.valueOf(buckleBiz.propVal(bean.get("try_days")));
			int tryType = bean.get("try_type")==null?0:Integer.valueOf(buckleBiz.propVal(bean.get("try_type")));
			Date tryStartDate = bean.get("trystart_date")==null?null:sdf.parse(buckleBiz.propVal(bean.get("trystart_date")));
			Date tryEndDate = bean.get("tryend_date")==null?null:sdf.parse(buckleBiz.propVal(bean.get("tryend_date")));
			int buckleType = bean.get("buckle_type")==null?1:Integer.valueOf(buckleBiz.propVal(bean.get("buckle_type")));
			Date buckleDate = bean.get("bucklefee_time")==null?null:sdf.parse(buckleBiz.propVal(bean.get("bucklefee_time")));
			//套餐状态
			int state = bean.get("state")==null?0:Integer.parseInt(buckleBiz.propVal(bean.get("state")));
			//扣费时间
			Calendar time = buckleBiz.buckleTime(buckleDate, tryType, tryDay, tryStartDate,tryEndDate, buckleDay, buckleType);
			//扣费标识
			String lastBuck = null;
			Calendar curCalendar = Calendar.getInstance();
			if(StringUtils.isNotBlank(debitAccount)&&tcType !=1 && state == 0 && curCalendar.get(Calendar.YEAR) == time.get(Calendar.YEAR) && curCalendar.get(Calendar.DAY_OF_YEAR) == time.get(Calendar.DAY_OF_YEAR) ){
				//扣费并重新计算下次扣费时间
				String flag = buckleBiz.getFlag(tcType);
				if(flag.equals(lastBucklefee)){//已经扣费
					
				}else{
					String tcName = buckleBiz.propVal(bean.get("taocan_name"));
					String mobile = buckleBiz.propVal(bean.get("mobile"));
					String cusName = buckleBiz.propVal(bean.get("custom_name"));
					String acctNo = buckleBiz.propVal(bean.get("acct_no"));
					String corpCode = buckleBiz.propVal(bean.get("corp_code"));
					String depName = buckleBiz.propVal(bean.get("dep_name"));
					int conState = Integer.valueOf(buckleBiz.propVal(bean.get("contract_state")));
					int conSource = Integer.valueOf(buckleBiz.propVal(bean.get("contract_source")));
					long conDep = Long.valueOf(buckleBiz.propVal(bean.get("contract_dep")));
					long conUser = Long.valueOf(buckleBiz.propVal(bean.get("contract_user")));
					long conId = Long.valueOf(buckleBiz.propVal(bean.get("contract_id")));
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					String msgId = buckleBiz.getMsgId(corpCode);
					
					//计费流水表
					LfDeductionsDisp disp = new LfDeductionsDisp();
					//计费表
					LfDeductionsList list = new LfDeductionsList();
					disp.setAcctno(acctNo);list.setAcctno(acctNo);
					disp.setContractdep(conDep);list.setContractdep(conDep);
					disp.setContractid(conId);list.setContractid(conId);
					disp.setContractstate(conState);list.setContractstate(conState);
					disp.setCorpcode(corpCode);list.setCorpcode(corpCode);
					disp.setCustomname(cusName);list.setCustomname(cusName);
					disp.setDebitaccount(debitAccount);list.setDebitaccount(debitAccount);
					disp.setDeductionsmoney(tcMoney);
					disp.setDeductionstype(0);list.setDeductionstype(0);//扣费
					disp.setDepname(depName);list.setDepname(depName);
					disp.setMobile(mobile);list.setMobile(mobile);
					disp.setOprstate(0);
					disp.setOprtime(timestamp);
					disp.setTaocancode(tcCode);list.setTaocancode(tcCode);
					disp.setTaocanmoney(tcMoney);list.setTaocanmoney(tcMoney);
					disp.setTaocantype(tcType);list.setTaocantype(tcType);
					disp.setTaocanname(tcName);list.setTaocanname(tcName);
					disp.setContractuser(conUser);list.setContractuser(conUser);
					disp.setDepid(conDep);list.setDepid(conDep);
					disp.setUserid(conUser);list.setUserid(conUser);
					disp.setUpdatetime(timestamp);list.setUdpatetime(timestamp);
					list.setBucklefeetime(timestamp);
					list.setBupsummoney(0);
					list.setBuckuptimer(0);
					list.setBuptimer(0);
					list.setContracttype(conSource);
					list.setImonth(curCalendar.get(Calendar.MONTH)+1);
					list.setIyear(curCalendar.get(Calendar.YEAR));
					disp.setMsgid(msgId);//流水号
					list.setMsgid(msgId);
					disps.add(disp);
					lists.add(list);
					int buckupMaxtimer = Integer.valueOf(buckleBiz.propVal(bean.get("buckup_maxtimer")));
					//是否最后一次补扣标识
					String lastBucleUpFee = buckupMaxtimer==0?"1":"0";
					sBuffer.append(msgId).append("|").append(mobile).append("|").append(debitAccount==null?"":debitAccount).append("|")
					.append(tcCode).append("|").append(tcMoney).append("|0|").append(lastBucleUpFee+"|").append(System.currentTimeMillis()).append("|&");
					
					if(tcType == 2){
						time.add(Calendar.MONTH, 1);
					}else if(tcType == 3){
						time.add(Calendar.MONTH, 3);
					}else if(tcType == 4){
						time.add(Calendar.YEAR, 1);
					}
					lastBuck = flag;
				}
				
			}
			LfContractTaocan ct = new LfContractTaocan();
			ct.setId(id);
			ct.setLastbucklefee(lastBuck);
			ct.setIsvalid("0");
			ct.setBucklefeetime(new Timestamp(time.getTimeInMillis()));
			ct.setUpdatetime(new Timestamp(System.currentTimeMillis()));
			conCts.add(ct);
		}
		
		empTransDao.save(conn, disps, LfDeductionsDisp.class);
		empTransDao.save(conn, lists, LfDeductionsList.class);
		new BuckleFeeDAO().batchUpdate(conn, conCts);
		//批量存入扣费文件
		if(sBuffer.length()>0){
			buckleBiz.writeBuckleFeeFile(sBuffer.toString());
		}
	}
	
	/**
	 * @description  取消签约扣费  
	 * @param conn
	 * @param contractId
	 * @throws Exception       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-1-23 下午03:42:42
	 */
	public void buckleFeeForCancelContract(List<DynaBean> tcBeans,Connection conn,long contractId) throws Exception{
		StringBuffer sBuffer = new StringBuffer("");//扣费信息批量存入文件
		List<LfDeductionsDisp> disps = new ArrayList<LfDeductionsDisp>();//计费流水表
		List<LfDeductionsList> lists = new ArrayList<LfDeductionsList>();//计费统计表
		List<LfContractTaocan> conCts = new ArrayList<LfContractTaocan>();//签约套餐关联表
		for(DynaBean bean:tcBeans){
			String debitAccount = buckleBiz.propVal(bean.get("debitaccount"));
			int tcType = buckleBiz.propVal(bean.get("taocan_type"))==null?2:Integer.valueOf(buckleBiz.propVal(bean.get("taocan_type")));
			int tcMoney = bean.get("taocan_money")==null?0:Integer.parseInt(buckleBiz.propVal(bean.get("taocan_money")));
			String tcCode = buckleBiz.propVal(bean.get("taocan_code"));
			String lastBucklefee = buckleBiz.propVal(bean.get("last_bucklefee"));
			Long id = Long.valueOf(buckleBiz.propVal(bean.get("id")));
			String lastBuck = null;
			//套餐状态
			int state = bean.get("state")==null?0:Integer.parseInt(buckleBiz.propVal(bean.get("state")));
			//扣费
			String flag = buckleBiz.getFlag(tcType);
			if(StringUtils.isNotBlank(debitAccount)&&tcType !=1&&state==0&&StringUtils.isNotBlank(lastBucklefee) && !flag.equals(lastBucklefee)){
				String tcName = buckleBiz.propVal(bean.get("taocan_name"));
				String mobile = buckleBiz.propVal(bean.get("mobile"));
				String cusName = buckleBiz.propVal(bean.get("custom_name"));
				String acctNo = buckleBiz.propVal(bean.get("acct_no"));
				String corpCode = buckleBiz.propVal(bean.get("corp_code"));
				String depName = buckleBiz.propVal(bean.get("dep_name"));
				int conState = Integer.valueOf(buckleBiz.propVal(bean.get("contract_state")));
				int conSource = Integer.valueOf(buckleBiz.propVal(bean.get("contract_source")));
				long conDep = Long.valueOf(buckleBiz.propVal(bean.get("contract_dep")));
				long conUser = Long.valueOf(buckleBiz.propVal(bean.get("contract_user")));
				long conId = Long.valueOf(buckleBiz.propVal(bean.get("contract_id")));
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				String msgId = buckleBiz.getMsgId(corpCode);
				
				//计费流水表
				LfDeductionsDisp disp = new LfDeductionsDisp();
				//计费表
				LfDeductionsList list = new LfDeductionsList();
				disp.setAcctno(acctNo);list.setAcctno(acctNo);
				disp.setContractdep(conDep);list.setContractdep(conDep);
				disp.setContractid(conId);list.setContractid(conId);
				disp.setContractstate(conState);list.setContractstate(conState);
				disp.setCorpcode(corpCode);list.setCorpcode(corpCode);
				disp.setCustomname(cusName);list.setCustomname(cusName);
				disp.setDebitaccount(debitAccount);list.setDebitaccount(debitAccount);
				disp.setDeductionsmoney(tcMoney);
				disp.setDeductionstype(0);list.setDeductionstype(0);//扣费
				disp.setDepname(depName);list.setDepname(depName);
				disp.setMobile(mobile);list.setMobile(mobile);
				disp.setOprstate(0);
				disp.setOprtime(timestamp);
				disp.setTaocancode(tcCode);list.setTaocancode(tcCode);
				disp.setTaocanmoney(tcMoney);list.setTaocanmoney(tcMoney);
				disp.setTaocantype(tcType);list.setTaocantype(tcType);
				disp.setTaocanname(tcName);list.setTaocanname(tcName);
				disp.setContractuser(conUser);list.setContractuser(conUser);
				disp.setDepid(conDep);list.setDepid(conDep);
				disp.setUserid(conUser);list.setUserid(conUser);
				disp.setUpdatetime(timestamp);list.setUdpatetime(timestamp);
				list.setBucklefeetime(timestamp);
				list.setBupsummoney(0);
				list.setBuckuptimer(0);
				list.setBuptimer(0);
				list.setContracttype(conSource);
				Calendar calendar = Calendar.getInstance();
				list.setImonth(calendar.get(Calendar.MONTH)+1);
				list.setIyear(calendar.get(Calendar.YEAR));
				disp.setMsgid(msgId);//流水号
				list.setMsgid(msgId);
				disps.add(disp);
				lists.add(list);
				int buckupMaxtimer = Integer.valueOf(buckleBiz.propVal(bean.get("buckup_maxtimer")));
				//是否最后一次补扣标识
				String lastBucleUpFee = buckupMaxtimer==0?"1":"0";
				sBuffer.append(msgId).append("|").append(mobile).append("|").append(debitAccount==null?"":debitAccount).append("|")
				.append(tcCode).append("|").append(tcMoney).append("|0|").append(lastBucleUpFee+"|").append(System.currentTimeMillis()).append("|&");
				
				lastBuck = flag;
			}
			LfContractTaocan ct = new LfContractTaocan();
			ct.setId(id);
			ct.setLastbucklefee(lastBuck);
			ct.setIsvalid("2");
			ct.setUpdatetime(new Timestamp(System.currentTimeMillis()));
			conCts.add(ct);
		}
		empTransDao.save(conn, disps, LfDeductionsDisp.class);
		empTransDao.save(conn, lists, LfDeductionsList.class);
		new BuckleFeeDAO().batchUpdate(conn, conCts);
		//批量存入扣费文件
		if(sBuffer.length()>0){
			buckleBiz.writeBuckleFeeFile(sBuffer.toString());
		}
	}
	
	/***
	 * 签约操作处理扣费
	* @Description: TODO
	* @param @param id
	* @return void
	 */
	public void contractBucklefee(Long id){
		List<DynaBean> tcBeans = new ArrayList<DynaBean>();
		LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
		condMap.put("contractId",String.valueOf(id));
		condMap.put("isvalid","0");
		Connection conn = null;
		try
		{
			tcBeans = crmSignDao.findTaocansByCond(condMap);
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			updateBuktime(tcBeans, conn, id);
			empTransDao.commitTransaction(conn);
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"签约操作处理扣费异常！");
		}
		
	}

	/**
	 * 格式化手机字符串
	 *
	 * @param phone
	 * @return
	 */
	public String formatPhone(String phone) {
		if (phone == null || "".equals(phone.trim())) {
			return null;
		}
		if(phone.startsWith("0086")||phone.startsWith("+86")){
			return phone.replaceAll("^(0086|\\+86)", "");
		}else if(phone.startsWith("+")||phone.startsWith("00")){
			phone = "00"+phone.replaceAll("^\\+?0*","");
		}
		return phone;
	}
}

