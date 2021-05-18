/**
 * 
 */
package com.montnets.emp.appmage.biz;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.appmage.dao.MttaskSelectDAO;
import com.montnets.emp.appmage.vo.MttaskDetailVo;
import com.montnets.emp.appmage.vo.MttaskSelectVo;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.appmage.LfAppMwClient;
import com.montnets.emp.entity.apptask.LfAppMttask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;


public class MttaskSelectBiz extends SuperBiz {
	
	private MttaskSelectDAO smstaskDao;
	public MttaskSelectBiz()
	{
		smstaskDao=new MttaskSelectDAO();
	}
	
	
	
	
	/**
	 * 发送详情（查两个表示时调用）
	 * @param conditionMap
	 * @param orderMap
	 * @param pageInfo
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List <MttaskDetailVo> getMtTaskTwo(MttaskDetailVo mtsvo,PageInfo pageInfo)
	throws Exception
	{
		List<MttaskDetailVo> mttaskList = null;
		List<DynaBean> dynlist=null;
		try {
			//如果分页信息为空则表名是导出数据的查询
			if(pageInfo == null)
			{
				dynlist = smstaskDao.findMtTaskVoTwoTable(mtsvo);
			}
			//如果分页信息为不为空时表示是详情页面的查询
			else
			{
				dynlist=smstaskDao.findMtTaskVoTwoTable(pageInfo, mtsvo);

			}
			//拼装VO
			mttaskList=completeMttaskDetailVo(dynlist,mtsvo.getCorpcode());
			//统计任务条数
			//txTaskcount(mtsvo.getTaskid());
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"发送详情（查两个表示时调用）异常");
			throw e;
		}
		return mttaskList;
	}
	
	
	private void txTaskcount(String taskid) throws Exception
	{
		if(taskid==null||"".equals(taskid)){
			return;
		}
		//发送成功数和发送失败数MAP
		Map<String, List<String>> countmap =new HashMap<String, List<String>>();
		getCountMap(countmap,taskid);
		//成功数
		Long succount=0l;
		//失败数
		Long faicount=0l;
		//已读数
		Long readcount=0l;
		//未读数
		Long unreadcount=0l;
		//已读未读数
		List<String> valuestrs = countmap.get(taskid!=null?taskid:"");
		if(valuestrs!=null&&valuestrs.size()>0){
			for(String countstr:valuestrs){
				if(countstr!=null&&countstr.contains(",")){
					String contsplit[]=countstr.split(",");
					if(contsplit.length==2){
//						00 110 120
//						失败就是 1 10
						String rptstate=contsplit[0];
						//发送成功数
						if("0".equals(rptstate)){
							succount=succount+Long.parseLong(contsplit[1]);
							unreadcount=unreadcount+Long.parseLong(contsplit[1]);
						}else if("1".equals(rptstate)){
							succount=succount+Long.parseLong(contsplit[1]);
							readcount=readcount+Long.parseLong(contsplit[1]);
						}else if(!"".equals(rptstate)&&!"0".equals(rptstate)&&!"1".equals(rptstate)){
							faicount=faicount+Long.parseLong(contsplit[1]);
						}
					}
				}
			}
		}
		LinkedHashMap<String, String> setMap=new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
		setMap.put("succount", succount+"");
		setMap.put("faicount", faicount+"");
		setMap.put("readcount", readcount+"");
		setMap.put("unreadcount", unreadcount+"");
		conditionMap.put("taskid", taskid);
		this.empDao.update(LfAppMttask.class, setMap, conditionMap);
		
	}
	
	
	
	private List<MttaskDetailVo> completeMttaskDetailVo(List<DynaBean> dynlist,String corpcode) throws Exception{
		//mtTaskVosList大于0，则进行Vo拼装。
		List<MttaskDetailVo> mtsList=new ArrayList<MttaskDetailVo>();
		if(dynlist!=null&& dynlist.size()>0&&corpcode!=null){
			//客户Map
			Map<String,LfAppMwClient>  appClientmap=new LinkedHashMap<String, LfAppMwClient>();
			getAppClientMap(dynlist, appClientmap);
			//拼装VO
			LfAppMwClient appclient=null;
			MttaskDetailVo mtsvo=null;
			for (int i = 0; i < dynlist.size(); i++) {
				DynaBean dyn=dynlist.get(i);
				if(dyn != null)
				{
					//newvo对象
					mtsvo = new MttaskDetailVo();
					//用户APP账号
					String appuseracount = dyn.get("tousername") != null ? dyn.get("tousername").toString() : "--";
					//用户APP账号
					mtsvo.setAppuseraccount(appuseracount);
					appclient = appClientmap.get(appuseracount);
					if(appclient != null)
					{
						//昵称
						mtsvo.setAppusername(appclient.getNickname());
					}
					else
					{
						//昵称
						mtsvo.setAppusername("--");
					}
					appclient = null;
					//发送主题
					mtsvo.setTitle(dyn.get("title")!=null?dyn.get("title").toString():null);
					//发送内容
					mtsvo.setContent(dyn.get("content")!=null?dyn.get("content").toString():null);
					//发送状态
					mtsvo.setSendstate(dyn.get("sendstate")!=null?dyn.get("sendstate").toString():null);
					//发送时间
					mtsvo.setCreatetime(dyn.get("createtime")!=null?Timestamp.valueOf(dyn.get("createtime").toString()):null);
					//企业编码
					mtsvo.setCorpcode(dyn.get("corp_code")!=null?dyn.get("corp_code").toString():null);
					//回执状态
					mtsvo.setRptstate(dyn.get("rpt_state")!=null?dyn.get("rpt_state").toString():null);
					//回执时间
					mtsvo.setRecrpttime(dyn.get("recrpttime")!=null?Timestamp.valueOf(dyn.get("recrpttime").toString()):null);
					mtsList.add(mtsvo);
				}
			}
		}
		return mtsList;
	}
	
	

	
	/**
	 * 群发历史与群发任务查询的方法
	 * @param mtsVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<MttaskSelectVo> getMttaskSelectVoWithoutDomination(LfSysuser curuser,MttaskSelectVo mtsVo, PageInfo pageInfo) throws Exception
	{
		List<DynaBean> dynsList;
		List<MttaskSelectVo> mtTaskVosList;
		
		try
		{
			if(pageInfo == null)
			{
				dynsList = smstaskDao.findMttaskSelectVoWithoutDomination(curuser,mtsVo);
			}
			else
			{
				dynsList = smstaskDao.findMttaskSelectVoWithoutDomination(curuser,mtsVo, pageInfo);
			}
			//拼装VO
			mtTaskVosList=completeMttaskSelectVo(dynsList,mtsVo.getCorpcode());
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取短信发送任务或非数据权限范围查找短信任务异常");
			throw e;
		}
		return mtTaskVosList;
	}
	
	
		
	/**
	 * 树
	 * @return
	 */
	public String getDepartmentJosnData2(Long depId,Long userId){
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			sysuser = empDao.findObjectByID(LfSysuser.class, userId);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询用户表");
			tree = new StringBuffer("[]");
		}
		if(sysuser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			
			try {
				lfDeps = null;
				
				 if ((sysuser != null) && (sysuser.getCorpCode().equals("100000")))
			        {
			          if (depId == null)
			          {
			            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			            LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();

			            conditionMap.put("superiorId", "0");

			            conditionMap.put("depState", "1");

			            orderbyMap.put("depId", "ASC");
			            orderbyMap.put("deppath", "ASC");
			            lfDeps = new BaseBiz().getByCondition(LfDep.class, conditionMap, orderbyMap);
			          }
			          else
			          {
			            lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,sysuser.getCorpCode());
			          }

			        }
			        else if (depId == null) {
			          lfDeps = new ArrayList();
			          LfDep lfDep = (LfDep)depBiz.getAllDepByUserIdAndCorpCode(userId,sysuser.getCorpCode()).get(0);
			          lfDeps.add(lfDep);
			        }
			        else {
			          lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,sysuser.getCorpCode());
			        }
				
				
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
		
			} catch (Exception e) {
				EmpExecutionContext.error(e,"查询部门相关信息");
				tree = new StringBuffer("[]");
			}
		}
		return tree.toString();
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	/****
	 * 通过用户ID，部门ID查询用户相关信息
	 * @param userId 用户ID
	 * @param depId 部门ID
	 * @return 用户集合
	 * @throws Exception
	 */
	public List<LfSysuser> getAllSysusersOfSmsTaskRecordByDep(Long userId,Long depId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			if (null != userId)
				lfSysuserList = new MttaskSelectDAO().findDomUserBySysuserIDOfSmsTaskRecordByDep(userId
						.toString(),depId.toString());

		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过用户ID,部门ID查询用户相关信息异常");
			throw e;
		}
		return lfSysuserList;
	}
	
	

	
	private List<MttaskSelectVo> completeMttaskSelectVo(List<DynaBean> mtTaskVosList,String corpcode) throws Exception{
		//mtTaskVosList大于0，则进行Vo拼装。
		List<MttaskSelectVo> mtsList=new ArrayList<MttaskSelectVo>();
		if(mtTaskVosList!=null&& mtTaskVosList.size()>0&&corpcode!=null){
			//操作员map
			Map<String,LfSysuser>  lfSysuserMap=new LinkedHashMap<String, LfSysuser>();
			getSysuserMap(mtTaskVosList, lfSysuserMap);
			//拼装VO
			LfSysuser lfSysuser=null;
			MttaskSelectVo mtsvo=null;
			for (int i = 0; i < mtTaskVosList.size(); i++) {
				DynaBean dyn=mtTaskVosList.get(i);
				if(dyn != null)
				{
					//newvo对象
					mtsvo = new MttaskSelectVo();
					//操作员ID
					String keyuser = dyn.get("user_id") != null ? dyn.get("user_id").toString() : "";
					//操作员ID
					mtsvo.setUserid(dyn.get("user_id") != null ? Long.parseLong(dyn.get("user_id").toString()) : null);
					lfSysuser = lfSysuserMap.get(keyuser);
					if(lfSysuser != null)
					{
						//操作员名称
						mtsvo.setUsername(lfSysuser.getName());
						//操作员状态
						mtsvo.setUserstate(lfSysuser.getUserState());
					}
					else
					{
						//操作员名称
						mtsvo.setUsername("");
						//操作员状态
						mtsvo.setUserstate(2);
					}
					lfSysuser = null;
					//发送时间
					mtsvo.setBigintime(dyn.get("bigintime")!=null?Timestamp.valueOf(dyn.get("bigintime").toString()):null);
					//发送结束时间
					mtsvo.setEndtime(dyn.get("endtime")!=null?Timestamp.valueOf(dyn.get("endtime").toString()):null);
					//企业编码
					mtsvo.setCorpcode(dyn.get("corp_code")!=null?dyn.get("corp_code").toString():null);
					//自增id
					mtsvo.setId(dyn.get("id")!=null?Long.parseLong(dyn.get("id").toString()):null);
					//app帐号
					mtsvo.setAppacount(dyn.get("appacount")!=null?dyn.get("appacount").toString():null);
					//发送状态
					mtsvo.setSendstate(dyn.get("sendstate")!=null?Integer.parseInt(dyn.get("sendstate").toString()):null);
					//任务ID
					mtsvo.setTaskid(dyn.get("taskid") != null ? Long.parseLong(dyn.get("taskid").toString()) : null);
					//标题】
					mtsvo.setTitle(dyn.get("title")!=null?dyn.get("title").toString():null);
					//短信内容
					mtsvo.setMsg(dyn.get("msg")!=null?dyn.get("msg").toString():null);
					//短信内容地址
					mtsvo.setMsgurl(dyn.get("msg_url")!=null?dyn.get("msg_url").toString():null);
					//内容类型
					mtsvo.setMsgtype(dyn.get("msg_type")!=null?Integer.parseInt(dyn.get("msg_type").toString()):null);
					//发送总数
					mtsvo.setSubcount(dyn.get("sub_count")!=null?Long.parseLong(dyn.get("sub_count").toString()):0l);
					//发送成功数
					mtsvo.setSuccount(dyn.get("suc_count")!=null?Long.parseLong(dyn.get("suc_count").toString()):0l);
					//发送失败数
					mtsvo.setFaicount(dyn.get("fai_count")!=null?Long.parseLong(dyn.get("fai_count").toString()):0l);
					//已读数
					mtsvo.setReadcount(dyn.get("read_count")!=null?Long.parseLong(dyn.get("read_count").toString()):0l);
					//未读数
					mtsvo.setUnreadcount(dyn.get("unread_count")!=null?Long.parseLong(dyn.get("unread_count").toString()):0l);
					
					mtsList.add(mtsvo);
				}
			}
		}
		return mtsList;
	}

	
	

	/**
	 * 获取APP用户信息map
	 * @param mtTaskVosList
	 * @param appclientmap
	 * @throws Exception
	 */
	private void getAppClientMap(List<DynaBean> mtTaskVosList,Map<String,LfAppMwClient>  appclientmap)throws Exception{
		//查询操作员
		String appcode="";
		for (int i = 0; i < mtTaskVosList.size(); i++) {
			DynaBean dyn=mtTaskVosList.get(i);
			if(dyn!=null&&dyn.get("tousername")!=null){
				appcode+=dyn.get("tousername").toString()+",";
			}
		}
		if(appcode!=null&&!"".equals(appcode)){
			appcode=appcode.substring(0, appcode.length()-1);
		}else{
			return;
		}
		
		LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
		conditionMap.put("appcode&in", appcode);
		List<LfAppMwClient>  appclients=empDao.findListBySymbolsCondition(LfAppMwClient.class, conditionMap, null);
		for (int i = 0; i < appclients.size(); i++) {
			appclientmap.put(appclients.get(i).getAppcode().toString(), appclients.get(i));
		}
	}
	
	
	
	
	/**
	 * 获取操作员map
	 * @param mtTaskVosList
	 * @param lfSysuserMap
	 * @throws Exception
	 */
	private void getSysuserMap(List<DynaBean> mtTaskVosList,Map<String,LfSysuser>  lfSysuserMap)throws Exception{
		//查询操作员
		String userids="";
		for (int i = 0; i < mtTaskVosList.size(); i++) {
			DynaBean dyn=mtTaskVosList.get(i);
			if(dyn!=null&&dyn.get("user_id")!=null){
				userids+=dyn.get("user_id").toString()+",";
			}
		}
		
		if(userids!=null&&!"".equals(userids)){
			userids=userids.substring(0, userids.length()-1);
		}else{
			return;
		}

		LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
		conditionMap.put("userId&in", userids);
		List<LfSysuser>  lfSysusers=empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);
		for (int i = 0; i < lfSysusers.size(); i++) {
			lfSysuserMap.put(lfSysusers.get(i).getUserId().toString(), lfSysusers.get(i));
		}
	}
	
	
	

	
	/**
	 * 获取成功失败已读未读条数统计map   key:taskid  value:sendstate,count
	 * @param corpcode
	 * 企业编码
	 * @param countmap
	 * 需要组建的map
	 * @throws Exception
	 */
	private void getCountMap(Map<String, List<String>> countmap,String taskid)throws Exception{
		List<DynaBean> dynsList;
		try
		{
				dynsList = smstaskDao.findCountByTaskIdandSendstate(taskid);
				List<String> valustrs=null;
				if(dynsList!=null&&dynsList.size()>0){
					for(DynaBean dyn:dynsList){
						if(dyn.get("taskid")!=null&&dyn.get("rpt_state")!=null&&dyn.get("icount")!=null){
							String keystr=dyn.get("taskid").toString();
							if(countmap.get(keystr)!=null){
								valustrs=countmap.get(keystr);
							}else{
								valustrs=new ArrayList<String>();
							}
							String valuestr=dyn.get("rpt_state").toString()+","+dyn.get("icount").toString();
							valustrs.add(valuestr);
							countmap.put(keystr, valustrs);
						}
					}
				}
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取app发送记录map");
			throw e;
		}
	}
	
	
	

}

