package com.montnets.emp.inbox.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SendMessage;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.inbox.dao.LfMotaskVoDao;
import com.montnets.emp.inbox.dao.ReciveBoxDao;
import com.montnets.emp.inbox.vo.LfMotaskVo1;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import com.montnets.emp.table.engine.TableLfMotask;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 个人收件箱业务处理层
 * @author Administrator
 *
 */
public class ReciveBoxBiz extends SuperBiz {

	LfMotaskVoDao voDao = new LfMotaskVoDao();
	/**
	 * 收件箱查询方法
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfMotaskVo1> findLfmotaskRecive2(LfSysuser CurSysuser ,LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
	throws Exception
	{	
		
		LfMotaskVoDao voDao = new LfMotaskVoDao();
		
		List<LfMotaskVo1> replyVolist = null;
		try {
			String sql = "";
			//个人权限
			if(CurSysuser.getPermissionType()==1)
			{
				//拼接sql
			   // sql = "select * from " + TableLfMotask.TABLE_NAME + "  where ("
				//+ TableLfMotask.USER_GUID + " ="+conditionMap.get("userGuid")+" or "+TableLfMotask.TASK_ID+" in (select "+TableLfMttask.TASKID+" from " +
				//TableLfMttask.TABLE_NAME+ " where "+TableLfMttask.USER_ID+" ="+conditionMap.get("userId")+") or "+ TableLfMotask.DEP_ID+" in( select "+TableLfMotask.DEP_ID+" from "+TableLfDomination.TABLE_NAME
				//+" where "+TableLfDomination.USER_ID+"="+conditionMap.get("userId")+"))";
				 sql = "select mo.*,employee.name as employeeName,sysu."+TableLfSysuser.USER_ID+",sysu."+TableLfSysuser.NAME+",sysu."+
				    TableLfSysuser.USER_NAME+",sysu."+TableLfSysuser.USER_STATE+",sysu."+TableLfSysuser.DEP_ID+" SYSDEPID from "+TableLfMotask.TABLE_NAME+" mo left  join LF_EMPLOYEE employee on  mo.phone=employee.mobile , "+
				    TableLfSysuser.TABLE_NAME+" sysu where mo."+TableLfMotask.USER_GUID+"=sysu."+TableLfSysuser.GUID+" and sysu."+TableLfSysuser.USER_ID+" in ("+
				    CurSysuser.getUserId()+")";
				
			}
			//机构权限
			else if(CurSysuser.getPermissionType()==2)
			{
				 String depIds = null;
				 depIds = voDao.getDepIds(CurSysuser.getUserId());
				//拼接sql
			    sql = "select mo.* ,employee.name as employeeName,sysu."+TableLfSysuser.USER_ID+",sysu."+TableLfSysuser.NAME+",sysu."+
			    TableLfSysuser.USER_NAME+",sysu."+TableLfSysuser.USER_STATE+",sysu."+TableLfSysuser.DEP_ID+" SYSDEPID from "+TableLfMotask.TABLE_NAME+" mo left  join LF_EMPLOYEE employee on  mo.phone=employee.mobile , "+
			    TableLfSysuser.TABLE_NAME+" sysu where mo."+TableLfMotask.USER_GUID+"=sysu."+TableLfSysuser.GUID+" and (sysu."+TableLfSysuser.DEP_ID+" in ("+
			    depIds+") or mo."+TableLfMotask.USER_GUID+"="+CurSysuser.getGuId()+" )";
			    
			}
			else
			{
				return null;
			}			
			replyVolist = voDao.findLfmotaskRecive(sql,conditionMap, orderbyMap, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"收件箱查询异常");
		}		
		return replyVolist;
	}
	
	public List<LfMotaskVo1> findLfmotaskRecive(LfSysuser CurSysuser ,LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
	throws Exception
	{
		List<LfMotaskVo1> replyVolist = null;
		List<LfMotaskVo1> motalist=new ArrayList<LfMotaskVo1>();
		Map<Long, LfSysuser> map=new HashMap<Long, LfSysuser>();
		try {

			String sql = "";
			//个人权限
			if(CurSysuser.getPermissionType()==1)
			{
				 sql = "select mo.mo_id,mo.sp_user sp_user, mo.spnumber spnumber,mo.phone phone,mo.delivertime delivertime,mo.msgcontent msgcontent,mo.msgfmt msgfmt, "
					 +"mo.corpcode corpcode,mo.spisuncm spisuncm,USER_GUID "
					 +" from "+TableLfMotask.TABLE_NAME+" mo where mo."+TableLfMotask.USER_GUID+" in ("+ CurSysuser.getGuId()+") ";
				map.put(CurSysuser.getGuId(), CurSysuser);
			}
			//机构权限
			else if(CurSysuser.getPermissionType()==2)
			{
				 String depIds = null;
				 depIds = this.getDepIds(CurSysuser.getUserId());
				 
				 List<String> guidlist=this.getUserGuids(depIds,map);
				//拼接sql
			    sql = "select mo.mo_id,mo.sp_user sp_user, mo.spnumber spnumber,mo.phone phone,mo.delivertime delivertime,mo.msgcontent msgcontent,mo.msgfmt msgfmt, "
					+"mo.corpcode corpcode,mo.spisuncm spisuncm,USER_GUID "
					+"from "+TableLfMotask.TABLE_NAME+" mo where ( ";
			    for(String guids:guidlist){
			    	sql=sql+" mo."+TableLfMotask.USER_GUID+" in ("+guids+") or ";
			    }
					sql=sql+" mo."+TableLfMotask.USER_GUID+"="+CurSysuser.getGuId()+") ";		
			    
			}
			else
			{
				return null;
			}			
			replyVolist =voDao.findLfmotaskRecive(sql,conditionMap, orderbyMap, pageInfo);
		
			
			for(LfMotaskVo1 mot1:replyVolist){
				LfSysuser sys=map.get(mot1.getUserguid());
				if(sys!=null){
					mot1.setUserId(sys.getUserId());
					mot1.setName(sys.getName());
					mot1.setSysdepId(sys.getDepId());
					motalist.add(mot1);
				}
			}
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"收件箱查询异常");
		}		
		return motalist;
	}
	
	
	
	/**
	 * 个人收件箱查询
	 * @param cursysuser
	 * 		当前登录操作员对象
	 * @param corpcode
	 * 		企业编码
	 * @param conditionMap
	 * 		条件
	 * @param orderbyMap
	 * 		排序条件
	 * @param pageInfo
	 * 		分页对象
	 * @return
	 * @throws Exception
	 */
	public List<LfMotaskVo1> findLfmotaskRecive_V1(LfSysuser cursysuser ,String corpcode,LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
	throws Exception
	{
		List<LfMotaskVo1> replyVolist = null;
		List<LfMotaskVo1> motalist=new ArrayList<LfMotaskVo1>();
		Map<Long, LfSysuser> map=new HashMap<Long, LfSysuser>();
		try {

			String sql = "";
			//个人权限
			if(cursysuser.getPermissionType()==1)
			{
				 sql = "select mo.mo_id,mo.sp_user sp_user, mo.spnumber spnumber,mo.phone phone,mo.delivertime delivertime,mo.msgcontent msgcontent,mo.msgfmt msgfmt, "
					 +"mo.corpcode corpcode,mo.spisuncm spisuncm,USER_GUID "
					 +" from "+TableLfMotask.TABLE_NAME+" mo where mo."+TableLfMotask.USER_GUID+" in ("+ cursysuser.getGuId()+") ";
				map.put(cursysuser.getGuId(), cursysuser);
			}
			//机构权限
			else if(cursysuser.getPermissionType()==2)
			{
				 String depIds = null;
				 depIds = this.getDepIds_V1(cursysuser.getUserId(),corpcode);
				 
				 List<String> guidlist=this.getUserGuids_V1(depIds,map,corpcode);
				//拼接sql
			    sql = "select mo.mo_id,mo.sp_user sp_user, mo.spnumber spnumber,mo.phone phone,mo.delivertime delivertime,mo.msgcontent msgcontent,mo.msgfmt msgfmt, "
					+"mo.corpcode corpcode,mo.spisuncm spisuncm,USER_GUID "
					+"from "+TableLfMotask.TABLE_NAME+" mo where ( ";
			    for(String guids:guidlist){
			    	sql=sql+" mo."+TableLfMotask.USER_GUID+" in ("+guids+") or ";
			    }
					sql=sql+" mo."+TableLfMotask.USER_GUID+"="+cursysuser.getGuId()+") ";		
					map.put(cursysuser.getGuId(), cursysuser);
			}
			else
			{
				return motalist;
			}			
			replyVolist =voDao.findLfmotaskRecive(sql,conditionMap, orderbyMap, pageInfo);
		
			
			for(LfMotaskVo1 mot1:replyVolist){
				LfSysuser sys=map.get(mot1.getUserguid());
				if(sys!=null){
					mot1.setUserId(sys.getUserId());
					mot1.setName(sys.getName());
					mot1.setSysdepId(sys.getDepId());
					motalist.add(mot1);
				}
			}
			map.clear();
			map=null;
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"收件箱查询异常");
		}		
		return motalist;
	}
	
	
	
	public List<String> getUserGuids(String depids,Map<Long, LfSysuser> map) throws Exception{
		String guids="";
		List<String> guidlsit=new ArrayList<String>();
		//通用查询方法排序字段集合
		LinkedHashMap<String,String> condition = new LinkedHashMap<String,String>();
		//排序字段
		condition.put("depId&in",depids);
		//通过当前操作员的机构获取所有下级机构
		List<LfSysuser> lfs=this.empDao.findListBySymbolsCondition(LfSysuser.class, condition, null);
		
		//判断下级机构集合是否为空
		if(null !=lfs && 0!=lfs.size())
		{	int index=0;
			for (LfSysuser user :lfs)
			{
				guids+=user.getGuId()+",";
				map.put(user.getGuId(), user);
				index++;
				if(index%500==0){
					if(guids.contains(","))
					{
						guids=guids.substring(0,guids.lastIndexOf(","));
					}
					guidlsit.add(guids);
					guids="";
					
				}
			}
			if(guids.contains(","))
			{
				guids=guids.substring(0,guids.lastIndexOf(","));
				guidlsit.add(guids);
			}
		}
		
		return guidlsit;
	}
	
	
	/**
	 * 获取所有用户guid
	 * @param depids
	 * 		机构id字符串
	 * @param map
	 * 		操作员Map
	 * @param corpcode
	 * 		企业编码
	 * @return
	 * @throws Exception
	 */
	public List<String> getUserGuids_V1(String depids,Map<Long, LfSysuser> map,String corpcode) throws Exception{
		//guid字符串
		String guids="";
		//guidlist
		List<String> guidlsit=new ArrayList<String>();
		//通过机构id字符串和企业编码获取所有操作员
		List<LfSysuser> lfs=voDao.findUsersByDepIds(depids,corpcode);
		//判断下级机构集合是否为空
		if(null !=lfs && 0!=lfs.size())
		{	int index=0;
			for (LfSysuser user :lfs)
			{
				guids+=user.getGuId()+",";
				map.put(user.getGuId(), user);
				index++;
				if(index%500==0){
					if(guids.contains(","))
					{
						guids=guids.substring(0,guids.lastIndexOf(","));
					}
					guidlsit.add(guids);
					guids="";
					
				}
			}
			if(guids.contains(","))
			{
				guids=guids.substring(0,guids.lastIndexOf(","));
				guidlsit.add(guids);
			}
		}
		
		return guidlsit;
	}
	
	public String getDepIds_V1(Long curUserId,String corpcode) throws Exception
	{
		String depIds = "";
		//通用查询方法排序字段集合
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		//排序字段
		orderbyMap.put("depId","asc");
		//部门集合
		List<LfDep> depList = new ArrayList<LfDep>();
		
		//通过当前操作员的机构获取所有下级机构
		depList = voDao.findDomDepBySysuserID_V1(curUserId.toString(),corpcode, orderbyMap);
		
		//判断下级机构集合是否为空
		if(null !=depList && 0!=depList.size())
		{
			for (LfDep dep :depList)
			{
				depIds+=dep.getDepId().toString()+",";
			}
			
			if(depIds.contains(","))
			{
				depIds=depIds.substring(0,depIds.lastIndexOf(","));
			}
		}
		
		return depIds;
	}
	
	public List<LfMotaskVo1> findLfmotaskRecive3(LfSysuser CurSysuser ,LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
	throws Exception
	{
		List<LfMotaskVo1> replyVolist = null;
		try {

			String sql = "";
			//个人权限
			if(CurSysuser.getPermissionType()==1)
			{
				 sql = "select mo.mo_id,mo.sp_user sp_user, mo.spnumber spnumber,mo.phone phone,mo.delivertime delivertime,mo.msgcontent msgcontent,mo.msgfmt msgfmt, "
					 +"mo.ptmsgid ptmsgid,mo.uids uids,mo.orguid orguid,mo.ecid ecid,mo.serviceid serviceid, mo.spsc spsc,mo.sendstatus sendstatus,mo.tp_pid tp_pid, "
					 +"mo.tp_udhi tp_udhi,mo.donetime donetime,mo.user_guid user_guid,mo.menucode menucode,mo.dep_id dep_id,mo.task_id task_id,mo.bus_code bus_code, "
					 +"mo.corpcode corpcode,mo.spisuncm spisuncm,mo.subno subno, "
					 +"employee.name as employeeName, "
					 +"sysu."+TableLfSysuser.USER_ID+" "+TableLfSysuser.USER_ID+", "
					 +"sysu."+TableLfSysuser.NAME+" "+TableLfSysuser.NAME+", "
					 +"sysu."+TableLfSysuser.USER_NAME+" "+TableLfSysuser.USER_NAME+", "
					 +"sysu."+TableLfSysuser.USER_STATE+" "+TableLfSysuser.USER_STATE+", "
					 +"sysu."+TableLfSysuser.DEP_ID+" SYSDEPID "
					 +"from "+TableLfMotask.TABLE_NAME+" mo left  join LF_EMPLOYEE employee on  mo.phone=employee.mobile , "+
					 TableLfSysuser.TABLE_NAME+" sysu where mo."+TableLfMotask.USER_GUID+"=sysu."+TableLfSysuser.GUID+" and sysu."+TableLfSysuser.USER_ID+" in ("+
					 CurSysuser.getUserId()+")";
				
			}
			//机构权限
			else if(CurSysuser.getPermissionType()==2)
			{
				 String depIds = null;
				 depIds = this.getDepIds(CurSysuser.getUserId());
				//拼接sql
			    sql = "select mo.mo_id,mo.sp_user sp_user, mo.spnumber spnumber,mo.phone phone,mo.delivertime delivertime,mo.msgcontent msgcontent,mo.msgfmt msgfmt, "
			    	+"mo.ptmsgid ptmsgid,mo.uids uids,mo.orguid orguid,mo.ecid ecid,mo.serviceid serviceid, mo.spsc spsc,mo.sendstatus sendstatus,mo.tp_pid tp_pid, "
					+"mo.tp_udhi tp_udhi,mo.donetime donetime,mo.user_guid user_guid,mo.menucode menucode,mo.dep_id dep_id,mo.task_id task_id,mo.bus_code bus_code, "
					+"mo.corpcode corpcode,mo.spisuncm spisuncm,mo.subno subno, "
			    	+"employee.name as employeeName, "
			    	+"sysu."+TableLfSysuser.USER_ID+" "+TableLfSysuser.USER_ID+", "
					+"sysu."+TableLfSysuser.NAME+" "+TableLfSysuser.NAME+", "
					+"sysu."+TableLfSysuser.USER_NAME+" "+TableLfSysuser.USER_NAME+", "
					+"sysu."+TableLfSysuser.USER_STATE+" "+TableLfSysuser.USER_STATE+", "
					+"sysu."+TableLfSysuser.DEP_ID+" SYSDEPID "
					+"from "+TableLfMotask.TABLE_NAME+" mo left  join LF_EMPLOYEE employee on  mo.phone=employee.mobile , "+
					TableLfSysuser.TABLE_NAME+" sysu where mo."+TableLfMotask.USER_GUID+"=sysu."+TableLfSysuser.GUID+" and (sysu."+TableLfSysuser.DEP_ID+" in ("+
					depIds+") or mo."+TableLfMotask.USER_GUID+"="+CurSysuser.getGuId()+" )";
			    
			}
			else
			{
				return null;
			}			
			replyVolist =voDao.findLfmotaskRecive(sql,conditionMap, orderbyMap, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"收件箱查询异常");
		}		
		return replyVolist;
	}
	
	public String getDepIds(Long curUserId) throws Exception
	{
		String depIds = "";
		//通用查询方法排序字段集合
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		//排序字段
		orderbyMap.put("depId","asc");
		//部门集合
		List<LfDep> depList = new ArrayList<LfDep>();
		
		//通过当前操作员的机构获取所有下级机构
		depList = voDao.findDomDepBySysuserID(curUserId.toString(), orderbyMap);
		
		//判断下级机构集合是否为空
		if(null !=depList && 0!=depList.size())
		{
			for (LfDep dep :depList)
			{
				depIds+=dep.getDepId().toString()+",";
			}
			
			if(depIds.contains(","))
			{
				depIds=depIds.substring(0,depIds.lastIndexOf(","));
			}
		}
		
		return depIds;
	}
	
	 /**
     * 快捷回复发送
     * @param mttask  任务对象
     * @param flag  是否启用计费
     * @return
     */
	public String SendMsgByOne(LfMttask mttask,boolean flag) {
		String result = "2";
		BalanceLogBiz balanceBiz = new BalanceLogBiz();
		SendMessage sendMessage = new SendMessage();
		Long mtId = null;
		int sendCount = Integer.parseInt(mttask.getIcount());
		
		try {
			//设置账号密码
			 //加载过滤条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", mttask.getSpUser());
			//SP账户类型   1:短信SP账号;2:彩信SP账号
			conditionMap.put("accouttype", "1");
			//调用查询方法获取发送账号
			List<Userdata> tempList = empDao.findListByCondition(
					Userdata.class, conditionMap, null);
			Userdata userData = null;
			if (tempList != null && tempList.size() == 1) {
				userData = tempList.get(0);
			}
			// 判断查到账号 且账号密码不为空或空字符串
			if(userData!=null&&userData.getUserPassword()!=null&&!"".equals(userData.getUserPassword())){
				mttask.setSpPwd(userData.getUserPassword());
			}
			
			//审批状态设置为无需审批
			mttask.setReState(0);
			// 检查sp余额是否足够发送,spResult>0表示可以发送
			int spResult = balanceBiz.checkSpUserFee(mttask.getSpUser(),
					sendCount, 1);
			String retStr = "";
			// 扣费失败
			if (spResult < 0) {
				if (spResult == -3) {
					// 没有账号信息
					EmpExecutionContext.error("查询不到sp账号信息。spuser:"
							+ mttask.getSpUser());
					retStr = "noSpInfo";
				} else if (spResult == -2) {
					// 余额不足
					EmpExecutionContext.error("sp账号余额不足。spuser:"
							+ mttask.getSpUser());
					retStr = "noSuffiSpFee";
				} else {
					EmpExecutionContext.error("查询sp账号信息异常。spuser:"
							+ mttask.getSpUser());
					retStr = "spFail";
				}
				return retStr;
			}
			
	        //如果启用了计费
			if(flag){
				//先进行扣费操作
				int resultMsg = balanceBiz.sendSmsAmountByUserId(null, mttask.getUserId(), Integer.parseInt(mttask.getIcount()));
				if(resultMsg != 0)
				{
					if(resultMsg==-2){
						//短信余额不足
						result = "6";				
					}else if(resultMsg==-1){
						//短信扣费失败
						result = "7";				
					}else if(resultMsg==-4){
						//用户所属机构没有充值
						result = "9";				
					}
					return result;
				}
			}
			//使用集群，上传文件到文件服务器
			if(StaticValue.getISCLUSTER() == 1)
			{
				CommonBiz comBiz = new CommonBiz();
				// 上传文件到文件服务器
				mttask.setFileuri(comBiz.uploadFileToFileServer(mttask.getMobileUrl()));
			}
			else
			{
				mttask.setFileuri(StaticValue.BASEURL);
			}
			//保存发送任务
			mtId = empDao.saveObjectReturnID(mttask);
			mttask.setMtId(mtId);
			//扣费成功，则进行发送操作
			String wegGateResult = sendMessage.sendSms(mttask, null);
			if("000".equals(wegGateResult) ){
				//发送到网关成功
				result = "1";
			}
			else
			{
				empDao.delete(LfMttask.class, String.valueOf(mtId));
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"快捷回复异常");
		}
		return result;
	}
	/**
	 * 短信发送
	 * @param spUserid  发送账号
	 * @param msg  短信内容
	 * @param phone  手机号
	 * @param priority  优先级
	 * @param sa  尾号
	 * @param sendUser  发送者
	 * @param buscode  业务类型
	 * @param Rptflag  是否需要状态报告
	 * @return
	 */
	public String SendMessage(String spUserid,String msg,String phone,String priority,String sa,LfSysuser sendUser,String buscode,String Rptflag)
	{
		String resultStr ="";
		try {
			//根据发送账号获取企业账户信息
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", spUserid);
			List<Userdata> tempList = empDao.findListByCondition(
					Userdata.class, conditionMap, null);
			Userdata userData = null;
			if (tempList != null && tempList.size() == 1) {
				userData = tempList.get(0);
			}
			
			//调用发送接口
			HttpSmsSend smsSend = new HttpSmsSend();
			WGParams params = new WGParams();
			//组装发送对象属性值
			params.setCommand("MT_REQUEST");
			params.setSpid(spUserid);
			params.setSppassword(userData == null ? null : userData.getUserPassword());
			params.setSm(msg);
			params.setDa(phone);
			params.setPriority(priority);
			params.setSa(sa);  //设置尾号
			params.setParam1(sendUser.getUserCode().toString());
			params.setSvrtype(buscode);  //设置业务类型
			params.setRptflag(Rptflag);
			//调用发送接口
			String result = smsSend.createbatchMtRequest(params);				

			int index = result.indexOf("mterrcode");
			if(index<0)
			{
                  //发送失败
				resultStr="error";

			}else
			{
				resultStr = result.substring(index + 10, index + 13);

				if (resultStr.equals("000")) {
					resultStr = "000";						
				} else {	
					//发送失败
					resultStr = result.substring(index - 8, index - 1);					
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"短信发送异常");
		}
		return resultStr;
	}
	
	public void getEmpMapByMobiles(Map<String, String> phoneNameMap,String mobiles,String lgcorpcode){
		try{
			if(lgcorpcode==null){
				lgcorpcode="0";
			}
			ReciveBoxDao recboxdao=new ReciveBoxDao();
			List<DynaBean> employees=recboxdao.findLfEmployeesByMobiles(mobiles,lgcorpcode);
			if(employees!=null && employees.size()>0){
				for (int i = 0; i < employees.size(); i++) 
				{
					DynaBean db=employees.get(i);
					if(db.get("mobile")!=null&&db.get("name")!=null){
						phoneNameMap.put(db.get("mobile").toString(), db.get("name").toString());
					}
				}
			}
					
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取员工号码名称map异常");
		}
		
	}
	
	
	public Map<String, String> getCliMapByMobiles(Map<String, String> phoneNameMap,String mobiles,String lgcorpcode){
		try{
			if(lgcorpcode==null){
				lgcorpcode="0";
			}
			ReciveBoxDao recboxdao=new ReciveBoxDao();
			List<DynaBean> clients= recboxdao.findLfClientsByMobiles(mobiles,lgcorpcode);
			if(clients!=null && clients.size()>0){
				for (int i = 0; i < clients.size(); i++) 
				{
					DynaBean db=clients.get(i);
					if(db.get("mobile")!=null&&db.get("name")!=null){
						phoneNameMap.put(db.get("mobile").toString(), db.get("name").toString());
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取客户号码名称map异常");
		}
		
		return  phoneNameMap;
	}
	
	
}
