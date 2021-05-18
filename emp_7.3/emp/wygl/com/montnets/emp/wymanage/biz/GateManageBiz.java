package com.montnets.emp.wymanage.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.gateway.AgwAccount;
import com.montnets.emp.entity.gateway.AgwParamConf;
import com.montnets.emp.entity.gateway.AgwParamValue;
import com.montnets.emp.entity.gateway.AgwSpBind;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.wy.AIpcominfo;
import com.montnets.emp.entity.wy.ASiminfo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wymanage.dao.GenericGateManageVoDAO;
import com.montnets.emp.wymanage.vo.ASiminfoVo;
import com.montnets.emp.wymanage.vo.GateManageVo;
import com.montnets.emp.wymanage.vo.GateShowVo;

/**
 * 网优通道管理biz
 * 
 * @project p_wygl
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-3-25 上午10:36:41
 * @description
 */

public class GateManageBiz extends SuperBiz{
	
	GenericGateManageVoDAO gatemanagevodao=new GenericGateManageVoDAO();
	
	/**
	 * 获取网优通道列表（包含sim卡信息）
	 * 
	 * @param gatemanagevo
	 *        查询条件vo
	 * @param map
	 *       sim卡map
	 * @param pageInfo
	 *        分页对象
	 * @return
	 *         返回动态bean集合
	 * @throws Exception
	 */
	public List<DynaBean> getAIpcomsByVO(GateManageVo gatemanagevo, Map<Integer, List<ASiminfoVo>> map, PageInfo pageInfo) throws Exception
	{

		// 定义通道账户集合
		List<DynaBean> aipcomVosList;
		// 定义SIM卡集合
		List<DynaBean> simlist;
		try
		{
			// 获取SIM卡信息列表
			simlist = gatemanagevodao.findSimsByVo(gatemanagevo);
			// MONTHLIMIT,HOURLIMIT,PHONENO,DESCRIPTION,DAYLIMIT,SIMNO,MOBILEAREA,ID,CREATETIME,GATEID,UNICOM
			//Map<Integer, String> areamapMap = this.getAreaMap();
			//通道ID字符串
			String gateids="";
			for (DynaBean dbsim : simlist)
			{
				//获取通道ID
				int gateid = dbsim.get("gateid") != null ? Integer.parseInt(dbsim.get("gateid").toString()) : 0;
				if(gateid != 0)
				{
					//组合通道字符串
					gateids=gateids+gateid+ ",";
					// SIM卡号
					String phoneno = dbsim.get("phoneno") != null ? dbsim.get("phoneno").toString() : "";
					// 卡序号
					int simno = dbsim.get("simno") != null ?Integer.parseInt(dbsim.get("simno").toString()) : 0;
					// 所属国别
					//int mobilearea = dbsim.get("mobilearea") != null ? Integer.parseInt(dbsim.get("mobilearea").toString()) : 0;
					// 所属国别名称
					String areaname = dbsim.get("description") != null ? dbsim.get("description").toString() : "";
					// 运营商id
					Integer unicom = dbsim.get("unicom") != null ? Integer.parseInt(dbsim.get("unicom").toString()) : -1;
					// 运营商名称
					String unicomname = "";
					if(unicom == 0)
					{
						unicomname = "移动";
					}
					else if(unicom == 1)
					{
						unicomname = "联通";
					}
					else if(unicom == 21)
					{
						unicomname = "电信";
					}
					else if(unicom == 5)
					{
						unicomname = "国际";
					}
					else
					{
						unicomname = "--";
					}
					//sim卡信息
					ASiminfoVo siminfo = new ASiminfoVo();
					//所属区域
					siminfo.setAreaname(areaname);
					//siminfo.setMobilearea(mobilearea);
					//SIM卡号
					siminfo.setPhoneno(phoneno);
					//sim卡序号
					siminfo.setSimno(simno);
					//运营商
					siminfo.setUnicom(unicom);
					//运营商名称
					siminfo.setUnicomname(unicomname);
					//sim信息集合
					List<ASiminfoVo> sims = null;
					
					if(map.containsKey(gateid))
					{
						sims = map.get(gateid);
						sims.add(siminfo);
					}
					else
					{
						sims = new ArrayList<ASiminfoVo>();
						sims.add(siminfo);
					}
					map.remove(gateid);
					Collections.sort(sims);
					map.put(gateid, sims);
				}
			}
			//截取字符串后面的逗号
			if(!"".equals(gateids)){
				gateids=gateids.substring(0,gateids.length()-1);
				gatemanagevo.setGateids(gateids);
			}else{
				gatemanagevo.setGateids("-1");
			}
			// 获取网优通道账号信息
			aipcomVosList = gatemanagevodao.findGateManagesByVo(gatemanagevo, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询网优通道biz层异常！");
			throw e;
		}
		// 返回数据集合
		return aipcomVosList;
	}

	/**
	 * 获取所有国别
	 * 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAreaList() throws Exception
	{

		// 定义区域集合
		List<DynaBean> acitys = new ArrayList<DynaBean>();
		String sql = "select * from A_AREACODE ";
		try
		{
			acitys = new SuperDAO().getListDynaBeanBySql(sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询所有国别biz层异常！");
			throw e;
		}
		// 返回数据集合
		return acitys;
	}
	
	/**
	 * 获取国别map
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, String> getAreaMap() throws Exception
	{
		Map<Integer, String> areamap=new HashMap<Integer, String>();
		
		try
		{
			List<DynaBean> arealist=this.getAreaList();
			for (DynaBean dynaBean : arealist)
			{
				int mobilearea=Integer.parseInt(dynaBean.get("areacode").toString());
				String areaname=dynaBean.get("areaname")!=null?dynaBean.get("areaname").toString():"";
				areamap.put(mobilearea, areaname);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询所有国别biz层异常！");
			throw e;
		}
		
		// 返回数据集合
		return areamap;
	}
	
	/**
	 * 获取通道号
	 * @return
	 * @throws Exception
	 */
	public String getGatenum() throws Exception{
		//查询所有短信通道
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("gateType", "1");
		List<XtGateQueue> gatesList = empDao.findListByCondition(XtGateQueue.class, conditionMap, null);
		List<String> gatenumlist=new ArrayList<String>();
		//通道号加入list
		if(gatesList!=null&&gatesList.size()>0){
			for (XtGateQueue xgq : gatesList)
			{
				gatenumlist.add(xgq.getSpgate());
			}
		}
		int i=20000001;
		String gatenums="";
		while(true){
			if(!gatenumlist.contains(i+"")){
				gatenums=i+"";
				break;
			}
			i++;
		}
		//返回通道号
		return gatenums;
		
	}
	
	/**
	 * 获取通道账户名称
	 * @return
	 * @throws Exception
	 */
	public String getGateUserID() throws Exception{
		//查询所有通道账户名称
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//conditionMap.put("userType", "1");
		List<Userdata> userList = empDao.findListByCondition(Userdata.class, conditionMap, null);
		//通道账户名称集合
		List<String> gateuserlist=new ArrayList<String>();
		if(userList!=null&&userList.size()>0){
			for (Userdata userdata : userList)
			{
				gateuserlist.add(userdata.getUserId());
			}
		}
		int i=1;
		//通道账号字符串
		String gateuserid="";
		while(true){
			//补零字符串
			String zero="";
			if((i+"").length()==1){
				zero="000";
			}else if((i+"").length()==2){
				zero="00";
			}else if((i+"").length()==3){
				zero="0";
			}
			//判断生成的通道账户名称是否存在于list中
			if(!gateuserlist.contains("WY"+zero+i+"")){
				gateuserid="WY"+zero+i+"";
				break;
			}
			i++;
		}
		return gateuserid;
	}
	
	
	
	/**
	 * 添加通道账户
	 * @param conn
	 * @param userdata
	 * @return
	 * @throws Exception
	 */
	public Long addUserdataReturnId(Connection conn,Userdata userdata)throws Exception
	{
		Long uid=0L;
		try 
		{
			if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				uid=empTransDao.saveObjectReturnIDWithTri(conn,userdata);
			}
			else
			{
				uid=empTransDao.saveObjectReturnID(conn,userdata);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"添加通道账户biz异常！");
			throw new Exception("添加通道账户biz异常！");
		}
		return uid;
	}
	
	/**
	 * 添加通道以及通道账户以及绑定
	 * @param conn
	 * @param ipcom
	 * @return
	 * @throws Exception
	 */
	public synchronized Long addXtGate(Connection conn,AIpcominfo ipcom,GateShowVo show) throws Exception{
		XtGateQueue xtgate=new XtGateQueue();
		xtgate.setGateType(1);
		//获取通道号
		String gatenum=getGatenum();
		xtgate.setSpgate(gatenum);
		xtgate.setGateName(ipcom.getGatename());
		show.setGatename(ipcom.getGatename());
		show.setCorpsign(ipcom.getCorpsign());
		show.setGatenum(gatenum);
		//运营商
		xtgate.setSpisuncm(0);
		xtgate.setLongSms(1l);
		xtgate.setSplitRule(1);
		xtgate.setEndSplit(1);
		xtgate.setEachSign(0);
		xtgate.setMaxWords(1000);
		xtgate.setSingleLen(70);
		xtgate.setRiseLevel(0);
		xtgate.setSignType(0);
		xtgate.setSignstr(ipcom.getCorpsign()==null||"".equals(ipcom.getCorpsign())?" ":ipcom.getCorpsign());
		xtgate.setSignlen(ipcom.getCorpsign()==null?0:ipcom.getCorpsign().trim().length());
		xtgate.setMultilen1(xtgate.getSingleLen()-3);
		xtgate.setMultilen2(xtgate.getMultilen1()-xtgate.getSignstr().trim().length());
		xtgate.setSignDropType(0);
		//添加通道
		Long gateid=this.empTransDao.saveObjectReturnID(conn,xtgate);
		//为0表示出现异常，或保存失败
		if(gateid-0==0)
		{
			throw new Exception("添加通道失败");
		}
		Userdata userdata = new Userdata();
		//获取通道账户名称
		String userid = getGateUserID();
		userdata.setUserId(userid);
		userdata.setUserPassword(userid);
		userdata.setStaffName(ipcom.getGatename());
		userdata.setUserType(1l);
		userdata.setLoginId(userid);
		userdata.setCorpAccount("200001");
		userdata.setStatus(0);
		userdata.setFeeFlag(1l);
		userdata.setRiseLevel(0l);
		userdata.setMoUrl(" ");
		userdata.setRptUrl(" ");
		userdata.setAccouttype(1);
		userdata.setSptype(1);
		show.setGateusername(userid);
		//添加通道账户
		Long uid = addUserdataReturnId(conn,userdata);
		//为0表示出现异常，或保存失败
		if(uid-0==0)
		{
			throw new Exception("添加通道账户失败");
		}
		AgwAccount account = new AgwAccount();
		account.setPtAccUid(Integer.valueOf(uid.toString()));
		account.setPtAccName(ipcom.getGatename());
		account.setPtAccId(userid);
		account.setSpAccid(userid);
		account.setSpAccPwd(userid);
		account.setPtAccpwd(userid);
		account.setServiceType("SMS");
		account.setFeeUserType(0);
		account.setPtIp(ipcom.getPtip());
		account.setPtPort(ipcom.getPtport());
		account.setSpPort(ipcom.getPort());
		account.setSpIp(ipcom.getIp());
		account.setSpeedLimit(100);
		account.setProtocolCode(50);
		account.setProtocolParam("ExpireHour=24;OutDbgInfo=3;ReturnMoUdhi=0;MsgIdOn=1");
		account.setSpId(userid);
		account.setSpType(0);
		account.setSpFeeFlag(1);
		account.setFeeUrl(" ");
		account.setBalance(0l);
		account.setBalanceTh(0l);
		account.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		Integer gwNo = 100;
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("gwNo", StaticValue.DESC);
		List<AgwAccount> accountList = this.empDao.findListByCondition(AgwAccount.class, null, orderbyMap);
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		if(accountList != null && accountList.size() > 0)
		{
			gwNo = accountList.get(0).getGwNo() + 1;
		}else{
			conditionMap.put("gwType", "4000");
			this.paramConfToParamValue(conn,99,conditionMap);
		}
		//序号
		account.setGwNo(gwNo);
		if(this.empTransDao.save(conn, account))
		{
			conditionMap.clear();
			conditionMap.put("gwType", "3000");
			this.paramConfToParamValue(conn,gwNo,conditionMap);
		}
		//添加绑定
		AgwSpBind agw = new AgwSpBind();
		agw.setPtAccUid(Integer.valueOf(uid.toString()));
		agw.setGateId(Integer.parseInt(gateid.toString()));
		this.empTransDao.save(conn, agw);
		return gateid;
	}
	


	/**
	 * 修改通道账户
	 * @param conn
	 * @param ipcom
	 * @throws Exception
	 */
	public synchronized void updateXtGate(Connection conn,AIpcominfo ipcom) throws Exception{
		//判断传入ipcomgateid是否为空
		if(ipcom!=null&&ipcom.getGateid()!=null&&!"".equals(ipcom.getGatename())){
			XtGateQueue xtgate=this.empDao.findObjectByID(XtGateQueue.class, ipcom.getGateid());
			if(xtgate!=null){
				xtgate.setGateName(ipcom.getGatename());
				xtgate.setSignstr(ipcom.getCorpsign()==null||"".equals(ipcom.getCorpsign())?" ":ipcom.getCorpsign());
				xtgate.setSignlen(ipcom.getCorpsign()==null?0:ipcom.getCorpsign().length());
				xtgate.setMultilen2(xtgate.getMultilen1()-xtgate.getSignstr().trim().length());
				this.empTransDao.update(conn, xtgate);
				//查找绑定表
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("gateId", ipcom.getGateid().toString());
				List<AgwSpBind> asbs=this.empDao.findListByCondition(AgwSpBind.class, conditionMap, null);
				
				if(asbs!=null&&asbs.size()>0){
					AgwSpBind asb=asbs.get(0);
					Userdata uda=this.empDao.findObjectByID(Userdata.class, asb.getPtAccUid());
					if(uda!=null){
						uda.setStaffName(ipcom.getGatename());
						//修改账户名称
						this.empTransDao.update(conn, uda);
						LinkedHashMap<String, String> conditionmap = new LinkedHashMap<String, String>();
						conditionmap.put("ptAccUid", asb.getPtAccUid().toString());
						LinkedHashMap<String, String> setmap = new LinkedHashMap<String, String>();
	//					account.setPtIp(ipcom.getIp());
	//					account.setPtPort(ipcom.getPort());
	//					account.setSpPort(ipcom.getPort());
	//					account.setSpIp(ipcom.getIp());
						setmap.put("ptAccName", ipcom.getGatename());
						setmap.put("ptIp", ipcom.getPtip());
						setmap.put("ptPort", ipcom.getPtport().toString());
						setmap.put("spIp", ipcom.getIp());
						setmap.put("spPort", ipcom.getPort().toString());
						this.empTransDao.update(conn, AgwAccount.class, setmap, conditionmap);
						
					}
					
				
				}
			}
		}
		
	}
	
	/***
	 * 通过网关编号获取通道配置信息
	* @Description: TODO
	* @param @param conn 数据库连接
	* @param @param gwNo 编号
	* @param @param conditionMap 条件信息
	* @param @throws Exception  自定义异常
	* @return void
	 */
	private void paramConfToParamValue(Connection conn,Integer gwNo,LinkedHashMap<String, String> conditionMap) throws Exception {
		List<AgwParamValue> paramList = new ArrayList<AgwParamValue>();
		BaseBiz baseBiz = new BaseBiz();
		try {
			List<AgwParamConf> confList = baseBiz.getByCondition(
					AgwParamConf.class, conditionMap, null);
			if (confList != null && confList.size() > 0) {
				for (AgwParamConf conf : confList) {
					AgwParamValue agwParam = new AgwParamValue();
					agwParam.setGwNo(gwNo);
					agwParam.setGwType(conf.getGwType());
					agwParam.setParamItem(conf.getParamItem());
					agwParam.setParamValue(conf.getDefaultValue());
					paramList.add(agwParam);
				}
			}
			baseBiz.addList(conn,AgwParamValue.class, paramList);
		} catch (Exception e) {
			//writer.print("error");
			EmpExecutionContext.error(e,"通过网关编号获取通道配置信息异常！");
			throw new Exception("通过网关编号获取通道配置信息异常！");
		}
	}
	
	/***
	 * 新增网关
	 * @param simstr sim信息
	 * @param ipcom
	 * @throws Exception
	 */
	public GateShowVo addWyGate(AIpcominfo ipcom,String simstr) {
		//获取连接
		Connection conn = this.empTransDao.getConnection();
		GateShowVo show=new GateShowVo();
		try
		{			
			//开启事务
			empTransDao.beginTransaction(conn);
			Long gateid = addXtGate(conn, ipcom,show);
			ipcom.setGateid(Integer.parseInt(gateid.toString()));
			ipcom.setCreatetime(new Timestamp(System.currentTimeMillis()));
			empTransDao.save(conn, ipcom);
			String[] simrow=simstr.split("@");
			List<ASiminfo> simlist=new ArrayList<ASiminfo>();
			for (String srow : simrow)
			{
				String[] coln=srow.split(",");
				if(coln.length==4){
					ASiminfo siminfo=new ASiminfo();
					siminfo.setCreatetime(new Timestamp(System.currentTimeMillis()));
					siminfo.setDescription(coln[2]);
					siminfo.setGateid(Integer.parseInt(gateid.toString()));
					siminfo.setPhoneno(coln[1]);
					siminfo.setSimno(Integer.parseInt(coln[0]));
					siminfo.setUnicom(Integer.parseInt(coln[3]));
					siminfo.setDaylimit(0);
					siminfo.setHourlimit(0);
					siminfo.setMobilearea(0);
					siminfo.setMonthlimit(0);
					simlist.add(siminfo);
				}
			}
			empTransDao.save(conn, simlist, ASiminfo.class);
			empTransDao.commitTransaction(conn);
			return show;
		}
		catch(Exception e)
		{
			//异常处理
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "通道账户添加失败");
			return null;
		}
		finally
		{
			//关闭事务
			empTransDao.closeConnection(conn);
		}		
	}
	
	
	/***
	 * 更新网关信息
	 */
	public boolean updateWyGate(AIpcominfo ipcom,String simstr) {
		//获取连接
		Connection conn = this.empTransDao.getConnection();
		try
		{			
			//开启事务
			empTransDao.beginTransaction(conn);
			ipcom.setCreatetime(new Timestamp(System.currentTimeMillis()));
			empTransDao.update(conn, ipcom);
			updateXtGate(conn, ipcom);
			String[] simrow=simstr.split("@");
			List<ASiminfo> simlist=new ArrayList<ASiminfo>();
			for (String srow : simrow)
			{
				String[] coln=srow.split(",");
				if(coln.length==4){
					ASiminfo siminfo=new ASiminfo();
					siminfo.setCreatetime(new Timestamp(System.currentTimeMillis()));
					siminfo.setDescription(coln[2]);
					siminfo.setGateid(ipcom.getGateid());
					siminfo.setPhoneno(coln[1]);
					siminfo.setSimno(Integer.parseInt(coln[0]));
					siminfo.setUnicom(Integer.parseInt(coln[3]));
					siminfo.setDaylimit(0);
					siminfo.setHourlimit(0);
					siminfo.setMobilearea(0);
					siminfo.setMonthlimit(0);
					simlist.add(siminfo);
				}
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("gateid", ipcom.getGateid().toString());
			empTransDao.delete(conn, ASiminfo.class, conditionMap);
			empTransDao.save(conn, simlist, ASiminfo.class);
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch(Exception e)
		{
			//异常处理
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "通道账户添加失败");
			return false;
		}
		finally
		{
			//关闭事务
			empTransDao.closeConnection(conn);
		}		
	}
	
	/***
	 * 通过网关id获得SIM信息
	 */
	public List<ASiminfoVo> getSimsBygateId(String gateid)throws Exception {
		
		GateManageVo gatemanagevo=new GateManageVo();
		// 获取SIM卡信息列表
		gatemanagevo.setGateid(gateid);
		List<ASiminfoVo> sims=new ArrayList<ASiminfoVo>();
		List<DynaBean> simlist = gatemanagevodao.findSimsByVo(gatemanagevo);
		for (DynaBean dbsim : simlist)
		{
				// SIM卡号
				String phoneno = dbsim.get("phoneno") != null ? dbsim.get("phoneno").toString() : "";
				// 卡序号
				int simno = dbsim.get("simno") != null ?Integer.parseInt(dbsim.get("simno").toString()) : 0;
				// 所属国别
				//int mobilearea = dbsim.get("mobilearea") != null ? Integer.parseInt(dbsim.get("mobilearea").toString()) : 0;
				// 所属国别名称
				String areaname = dbsim.get("description") != null ? dbsim.get("description").toString() : "";
				// 运营商id
				Integer unicom = dbsim.get("unicom") != null ? Integer.parseInt(dbsim.get("unicom").toString()) : -1;
				// 运营商名称
				String unicomname = "";
				if(unicom == 0)
				{
					unicomname = "移动";
				}
				else if(unicom == 1)
				{
					unicomname = "联通";
				}
				else if(unicom == 21)
				{
					unicomname = "电信";
				}
				else if(unicom == 5)
				{
					unicomname = "国际";
				}
				else
				{
					unicomname = "--";
				}

				ASiminfoVo siminfo = new ASiminfoVo();
				siminfo.setAreaname(areaname);
				//siminfo.setMobilearea(mobilearea);
				siminfo.setPhoneno(phoneno);
				siminfo.setSimno(simno);
				siminfo.setUnicom(unicom);
				siminfo.setUnicomname(unicomname);
				sims.add(siminfo);
		}
		Collections.sort(sims);
		return sims;
	}
	
	
	
	

}
