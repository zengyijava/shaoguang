package com.montnets.emp.txgl.corpmanage.biz;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.corpmanage.dao.GenericLfSpCorpBindVoDAO;
import com.montnets.emp.entity.client.LfCliDepConn;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfReviewSwitch;
import com.montnets.emp.entity.sysuser.LfRoles;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.sysuser.LfUser2role;
import com.montnets.emp.entity.tailmanage.GwMsgtail;
import com.montnets.emp.entity.tailmanage.GwTailbind;
import com.montnets.emp.entity.tailmanage.GwTailctrl;
import com.montnets.emp.netnews.entity.LfWXDataType;
import com.montnets.emp.table.corp.TableLfCorp;

public class EnterpriseManageBiz extends SuperBiz
{
	/**
	 * @description 新增企业信息
	 * @param corp 企业信息
	 * @param dep 机构信息
	 * @return  true：成功，false：失败 
	 *
	 * @description 增加插入审核开关初始化数据相关代码
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-14 下午07:21:12
	 */
	public boolean addLfCorp(LfCorp corp, LfDep dep)
	{
		// 获取连接
		Connection conn = empTransDao.getConnection();
		BaseBiz baseBiz = new BaseBiz();
		String corpCode = corp.getCorpCode();
		boolean result = false;
		try
		{
			// 再保存数据库之前再次判断一下是否已存在．如果已存在则返回false.
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", corpCode);
			try
			{
				List<LfCorp> list = baseBiz.getByCondition(LfCorp.class, conditionMap, null);
				if(list.size() > 0)
				{
					return false;
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "新增企业信息，获取企业信息集合异常。");
			}

			// 初始化全局表配置（机构最大级数、同级机构最大数、机最大总数、机构充值最大级数、
			//密码位数、密码组合形式、修改周期、过期提醒、错误上线、动态口令模板，
			//短信贴尾内容(默认值：回复TD退订)(暂时去除)
			//,短信贴尾标识(0：关闭；1：开启 ，默认值0)）
			String paramKey[] = {"dep.maxlevel","dep.maxchild","dep.maxdep","dep.maxchargelevel",
					"pwd.count","pwd.combtype","pwd.upcycle","pwd.pastalarm","pwd.errlimit",
					"pwd.dynpwd",
					//"sms.tailcontents",
					"sms.tailflag"};
			String paramValue[] = {StaticValue.DEP_MAXLEVEL_COUNT+"",StaticValue.DEP_MAXCHILD_COUNT+"",
					StaticValue.DEP_MAXDEP_COUNT+"",StaticValue.MAX_CHARGE_DEP+"",
					StaticValue.getPassCount(), StaticValue.getPassCombtype(), StaticValue.getPassUpcycle(),
					StaticValue.getPassPastalarm(), StaticValue.getPassErrlimit(), StaticValue.getPassDynpwd(),
					StaticValue.SMS_TAILCONTENTS,StaticValue.getSmsTailflag()};
			List<LfCorpConf> corpConfList = new ArrayList<LfCorpConf>();
			LfCorpConf corpConf = null;
			for (int i = 0; i < paramKey.length; i++)
			{
				corpConf = new LfCorpConf();
				corpConf.setCorpCode(corpCode);
				corpConf.setParamKey(paramKey[i]);
				corpConf.setParamValue(paramValue[i]);
				corpConfList.add(corpConf);
			}
			// 开启事务
			empTransDao.beginTransaction(conn);
			empTransDao.saveObjectReturnID(conn, corp);

			// 添加员工机构(顶级机构)
			LfEmployeeDep employeeDep = new LfEmployeeDep();
			employeeDep.setDepName(dep.getDepName());
			employeeDep.setParentId(0L);
			// employeeDep.setDepPcode("-1");
			employeeDep.setDepEffStatus("A");
			employeeDep.setDepLevel(1);
			employeeDep.setCorpCode(dep.getCorpCode());

			Long emplyeeDepId = empTransDao.saveObjectReturnID(conn, employeeDep);
			employeeDep.setDepId(emplyeeDepId);
			employeeDep.setDeppath(emplyeeDepId + "/");
			// 修改员工机构机构Deppath
			empTransDao.update(conn, employeeDep);

			// 添加客户机构（顶级机构）
			LfClientDep clientDep = new LfClientDep();
			clientDep.setDepName(dep.getDepName());
			clientDep.setParentId(0L);
			// clientDep.setDepPcode("-1");
			clientDep.setDepLevel(1);
			clientDep.setAddType(0);
			clientDep.setCorpCode(dep.getCorpCode());

			Long clientDepId = empTransDao.saveObjectReturnID(conn, clientDep);
			clientDep.setDepId(clientDepId);
			clientDep.setDeppath(clientDepId + "/");
			// 修改客户机构机构Deppath
			empTransDao.update(conn, clientDep);

			// 添加操作员机构(顶级机构)
			Long depId = empTransDao.saveObjectReturnID(conn, dep);
			dep.setDepId(depId);
			dep.setDeppath(depId + "/");

			empTransDao.update(conn, dep);


			// 操作员-操作员机构管辖关系
			List<LfSysuser> usersList = new GenericLfSpCorpBindVoDAO().findDomSysuserByDepID(dep.getSuperiorId().toString(), null);
			List<LfDomination> domList = new ArrayList<LfDomination>();
			for (int i = 0; i < usersList.size(); i++)
			{
				LfDomination dom = new LfDomination();
				dom.setDepId(dep.getDepId());
				dom.setUserId(usersList.get(i).getUserId());
				domList.add(dom);
			}
			// 获取审核开关配置信息
			List<LfReviewSwitch> switchList = getReviewSwitch(corpCode);

			empTransDao.save(conn, domList, LfDomination.class);
			empTransDao.save(conn, switchList, LfReviewSwitch.class);
			empTransDao.save(conn, corpConfList, LfCorpConf.class);

			//企业编码不为100001，则设置短信贴尾初始化信息
			//100001企业网关有初始化数据，所以不需要设置初始化信息
			if(!"100001".equals(corpCode))
			{
				// 设置短信贴尾初始化信息
				GwMsgtail gwMsgtail = new GwMsgtail();
				gwMsgtail.setTailname("默认全局贴尾");
				gwMsgtail.setContent("回复TD退订");
				gwMsgtail.setCorpcode(corpCode);
				gwMsgtail.setUserid(0L);
				gwMsgtail.setCreatetime(new Timestamp(System.currentTimeMillis()));
				gwMsgtail.setUpdatetime(new Timestamp(System.currentTimeMillis()));
				empTransDao.save(conn, gwMsgtail);
				GwTailbind gwTailbind = new GwTailbind();
				gwTailbind.setTailid(0L);
				gwTailbind.setBuscode(" ");
				gwTailbind.setSpuserid(" ");
				gwTailbind.setTailtype(0);
				gwTailbind.setCorpcode(corpCode);
				gwTailbind.setUserid(0L);
				gwTailbind.setCreatetime(new Timestamp(System.currentTimeMillis()));
				gwTailbind.setUpdatetime(new Timestamp(System.currentTimeMillis()));
				empTransDao.save(conn, gwTailbind);
				String sql="";
				if(StaticValue.DBTYPE==StaticValue.SQLSERVER_DBTYPE)
				{
					sql = "UPDATE GW_TAILBIND SET TAIL_ID=(SELECT TOP 1 MM.TAIL_ID FROM GW_MSGTAIL MM " +
							"INNER JOIN GW_TAILBIND TT ON MM.CORP_CODE=TT.CORP_CODE WHERE MM.CONTENT='回复TD退订' " +
							"AND MM.TAIL_NAME='默认全局贴尾' AND MM.CORP_CODE='"+corpCode+"')  WHERE CORP_CODE='"+corpCode+"'";
				}else if(StaticValue.DBTYPE==StaticValue.MYSQL_DBTYPE)
				{
					sql = " UPDATE GW_TAILBIND A INNER JOIN " +
							" GW_MSGTAIL B  SET A.TAIL_ID =  B.TAIL_ID " +
							"WHERE A.CORP_CODE = B.CORP_CODE AND B.CONTENT='回复TD退订' AND B.TAIL_NAME='默认全局贴尾' AND B.CORP_CODE='"+corpCode+"'";
				}
				empTransDao.executeBySQLReturnCount(conn, sql);
				GwTailctrl gwTailctrl = new GwTailctrl();
				gwTailctrl.setOvertailflag(0);
				gwTailctrl.setOthertailflag(0);
				gwTailctrl.setCorpcode(corpCode);
				gwTailctrl.setUserid(0L);
				gwTailctrl.setCreatetime(new Timestamp(System.currentTimeMillis()));
				gwTailctrl.setUpdatetime(new Timestamp(System.currentTimeMillis()));
				empTransDao.save(conn, gwTailctrl);

				//设置移动网讯,新增互动项,类别初始下拉框
				List<LfWXDataType> wxDataList = new ArrayList<LfWXDataType>();//创建对象集合
				List<String> dataList = new ArrayList<String>();
				//五个初始化数据
				dataList.add("消费意向调查");
				dataList.add("满意度调查");
				dataList.add("客户留言");
				dataList.add("产品调查");
				dataList.add("优秀员工投票");
				for(int i=0;i<dataList.size();i++) {
					String initData = dataList.get(i);//name
					LfWXDataType lfWXDataType = new LfWXDataType();
					lfWXDataType.setName(initData);
					lfWXDataType.setCorpCode(corpCode);
					wxDataList.add(lfWXDataType);
				}
				empTransDao.save(conn,wxDataList,LfWXDataType.class);
			}


			// 提交事务
			empTransDao.commitTransaction(conn);
			result = true;
		}
		catch (Exception e)
		{
			// 事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "新增企业信息异常。");
		}
		finally
		{
			// 关闭连接
			empTransDao.closeConnection(conn);
		}
		return result;
	}




	//超级管理员
	public void addRoleimpower(Connection conn,Long roleid,Integer type,Long userid)throws Exception{
		//审核员
		String sql="";
		if(type==1){
			//超级管理员
			sql="insert into lf_impower(role_id,privilege_id) select "+roleid+",privilege_id from lf_impower where role_id=3";
		}else if(type==2){
			//审核员
			sql="insert into lf_impower(role_id,privilege_id) select "+roleid+",privilege_id from lf_privilege where menuname in " +
					"('信息审批','模板审批','网讯审核') and privilege_id in (select privilege_id from lf_impower where role_id=3)";
		}else if(type==3){
			//开户员
			sql="insert into lf_impower(role_id,privilege_id) select "+roleid+",privilege_id from lf_privilege where menuname in " +
					"('机构管理','操作员管理','角色设置','通讯录权限管理','员工通讯录','员工机构管理','职位管理','客户通讯录','客户属性管理')" +
					" and privilege_id in (select privilege_id from lf_impower where role_id=3)";
		}else if(type==4){
			//充值员
			sql="insert into lf_impower(role_id,privilege_id) select "+roleid+",privilege_id from lf_privilege where menuname in ('充值/回收管理') " +
					" and privilege_id in (select privilege_id from lf_impower where role_id=3)";
		}else if(type==5){
			//发送员
			sql="insert into lf_impower(role_id,privilege_id) select "+roleid+",privilege_id from lf_privilege where " +
					" (modname in ('企业短信','短信客服','移动财务','网讯发送','网讯查询统计','彩信应用') or menuname in " +
					"('个人群组','网讯编辑','互动项管理','网讯素材','业务管理','机构统计报表','操作员统计报表')) and privilege_id in " +
					"(select privilege_id from lf_impower where role_id=3) ";
		}

		Statement ps = null;
		try {
			ps = conn.createStatement();
			ps.executeUpdate(sql);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"初始化角色报错");
			throw e;
		} finally {
			if(ps!=null){
				ps.close();
			}
		}

		LfUser2role user2role = new LfUser2role();
		user2role.setRoleId(roleid);
		user2role.setUserId(userid);
		if(!empTransDao.save(conn, user2role)){
			throw new Exception("添加角色权限出错");
		}


	}


	/**
	 * @description  获取审核开关配置信息
	 * @param corpCode 企业编码
	 * @return 审核开关配置信息的集合
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-14 下午07:20:55
	 */
	private List<LfReviewSwitch> getReviewSwitch(String corpCode)
	{
		List<LfReviewSwitch> switchList = new ArrayList<LfReviewSwitch>();

		// 添加短信任务审核开关信息
		switchList.add(new LfReviewSwitch(2, 0, 1, corpCode));
		// 添加彩信任务审核开关信息
		switchList.add(new LfReviewSwitch(2, 0, 2, corpCode));
		// 添加短信模板审核开关信息
		switchList.add(new LfReviewSwitch(2, -1, 3, corpCode));
		// 添加彩信模板审核开关信息
		switchList.add(new LfReviewSwitch(2, -1, 4, corpCode));
		// 添加网讯模板审核开关信息
		switchList.add(new LfReviewSwitch(2, -1, 6, corpCode));

		return switchList;
	}

	public boolean setParam(String rptflag, Long corpId){
		try {
			Connection conn = empTransDao.getConnection();
			String sql = "UPDATE "+TableLfCorp.TABLE_NAME+" SET "+TableLfCorp.RPTFLAG+"='"+rptflag+"' WHERE "+TableLfCorp.CORP_ID+"="+corpId+"";
			int count = empTransDao.executeBySQLReturnCount(conn, sql);
			//更新成功设置为true
			return count>0?true:false;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "修改企业信息,设置参数异常！");
			return false;
		}
	}

	/**
	 * 修改企业信息
	 * @param objectMap
	 * @param conMap
	 * @param depName
	 * @return
	 */
	public boolean updateLfCorp(LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conMap, String depName)
	{
		// 获取连接
		Connection conn = empTransDao.getConnection();
		boolean result = false;
		try
		{
			// 开启事务
			empTransDao.beginTransaction(conn);
			empTransDao.update(conn, LfCorp.class, objectMap, conMap);
			// empTransDao.update(conn, crop);
			// this.updaateDepByCropCode(conn, crop.getCorpCode(), oldCorpCode);

			// 修改企业时，对应修改操作员机构表（顶级机构）
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 操作员机构级别，第一级（1）
			conditionMap.put("depLevel", "1");
			conditionMap.put("corpCode", objectMap.get("corpCode"));

			LfDep dep = empDao.findListByCondition(LfDep.class, conditionMap, null).get(0);
			// 操作员机构名称
			dep.setDepName(depName);
			empTransDao.update(conn, dep);

			// 修改企业时，对应修改客户机构表（顶级机构）
			LfClientDep clientDep = empDao.findListByCondition(LfClientDep.class, conditionMap, null).get(0);
			// 客户机构名称
			clientDep.setDepName(depName);
			empTransDao.update(conn, clientDep);
			// 客户机构级别，第一级（1）
			conditionMap.put("depLevel", "1");

			// 修改企业时，对应修改员工机构表（顶级机构）
			LfEmployeeDep employeeDep = empDao.findListByCondition(LfEmployeeDep.class, conditionMap, null).get(0);
			// 员工机构名称
			employeeDep.setDepName(depName);
			empTransDao.update(conn, employeeDep);
			// 提交事务
			empTransDao.commitTransaction(conn);
			result = true;
		}
		catch (Exception e)
		{
			// 事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "修改企业信息异常！");
		}
		finally
		{
			// 关闭连接
			empTransDao.closeConnection(conn);
		}
		return result;
	}

	/**
	 * @description 添加企业管理员
	 * @param user 操作员对象
	 * @param depId 机构id
	 * @param corp 企业对象
	 * @return true- 成功，fasle- 失败      			 
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-15 上午09:21:13
	 */
	public boolean addCorpManager(LfSysuser user,String depId,LfCorp corp)
	{
		// 获取连接
		Connection conn = empTransDao.getConnection();
		boolean result = false;
		try
		{
			Long depIdL = Long.parseLong(depId);

			empTransDao.beginTransaction(conn);
			long addRs = empTransDao.saveObjProReturnID(conn, user);
			LfDomination ld = new LfDomination();
			ld.setDepId(depIdL);
			ld.setUserId(addRs);
			if(!empTransDao.save(conn, ld))
			{
				throw new Exception("添加企业操作员时，添加机构权限失败！");
			}
			//获取当前插入的机构根据id
			LfDep root = empDao.findObjectByID(LfDep.class, depIdL);

			LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
			conditionMap1.put("corpCode", root.getCorpCode());
			//根据corpcode得到顶级客户机构
			LfClientDep clientDep = empDao.findListByCondition(LfClientDep.class, conditionMap1, null).get(0);
			LfCliDepConn cdc = new LfCliDepConn();
			cdc.setUserId(addRs);
			cdc.setDepId(clientDep.getDepId());
			cdc.setDepCodeThird("0000000000000000");
			if(!empTransDao.save(conn, cdc))
			{
				throw new Exception("添加企业操作员时，添加客户机构失败！");
			}

			//根据corpcode得到顶级员工机构
			LfEmployeeDep empDep = empDao.findListByCondition(LfEmployeeDep.class, conditionMap1, null).get(0);
			LfEmpDepConn led = new LfEmpDepConn();
			led.setUserId(addRs);
			led.setDepId(empDep.getDepId());
			led.setDepCodeThird("0000000000000000");
			if(!empTransDao.save(conn, led))
			{
				throw new Exception("添加企业操作员时，添加员工机构失败！");
			}


			//给新企业添加默认角色  超级管理员  审核员 开户员 充值员 发送员
			LfRoles role1=new LfRoles();
			role1.setRoleName("超级管理员");
			role1.setCorpCode(user.getCorpCode());
			role1.setGuId(user.getGuId());
			Long roleid1=empTransDao.saveObjectReturnID(conn, role1);
			addRoleimpower(conn, roleid1, 1,addRs);
			LfRoles role2=new LfRoles();
			role2.setRoleName("审核员");
			role2.setCorpCode(user.getCorpCode());
			role2.setGuId(user.getGuId());
			Long roleid2=empTransDao.saveObjectReturnID(conn, role2);
			addRoleimpower(conn, roleid2, 2,addRs);

			LfRoles role3=new LfRoles();
			role3.setRoleName("开户员");
			role3.setCorpCode(user.getCorpCode());
			role3.setGuId(user.getGuId());
			Long roleid3=empTransDao.saveObjectReturnID(conn, role3);
			addRoleimpower(conn, roleid3, 3,addRs);

			LfRoles role4=new LfRoles();
			role4.setRoleName("充值员");
			role4.setCorpCode(user.getCorpCode());
			role4.setGuId(user.getGuId());
			Long roleid4=empTransDao.saveObjectReturnID(conn, role4);
			addRoleimpower(conn, roleid4, 4,addRs);

			LfRoles role5=new LfRoles();
			role5.setRoleName("发送员");
			role5.setCorpCode(user.getCorpCode());
			role5.setGuId(user.getGuId());
			Long roleid5=empTransDao.saveObjectReturnID(conn, role5);
			addRoleimpower(conn, roleid5, 5,addRs);

			//添加操作员角色权限
			LfUser2role user2role = new LfUser2role();
			user2role.setRoleId(Long.valueOf(3));
			user2role.setUserId(addRs);
			if(!empTransDao.save(conn, user2role))
			{
				throw new Exception("添加企业操作员时，添加操作员角色权限失败！");
			}
			if (corp != null && !empTransDao.update(conn, corp))
			{
				throw new Exception("添加企业操作员时，更新企业信息失败！");
			}
			result = true;
			empTransDao.commitTransaction(conn);
		}catch(Exception e)
		{
			result = false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"添加企业管理员异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}


	/**
	 * 更新admin操作员信息
	 * @description
	 * @param user  操作员对象
	 * @param ipMac  解绑IPMAC标识  0：解除；其他：不解除
	 * @param corpCode 企业编码
	 * @return      true:成功；false:失败			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-8-17 上午11:37:49
	 */
	public boolean updateAdmin(LfSysuser user, String ipMac, String corpCode)
	{
		// 获取连接
		Connection conn = null;
		//返回值
		boolean resultRs = false;

		try
		{
			//获取连接
			conn = empTransDao.getConnection();
			//开启事务
			empTransDao.beginTransaction(conn);
			List<DynaBean> lfMacipList = null;
			String sql = "";
			//解除IP/MAC绑定
			if(ipMac != null && "0".equals(ipMac))
			{
				//IP/MAC绑定表是否有记录，更新之前查询，否则记录会被锁
				sql = "SELECT * FROM LF_MACIP WHERE GUID IN (SELECT GUID FROM LF_SYSUSER WHERE CORP_CODE='"+corpCode+"' AND USER_NAME='admin')";
				lfMacipList = new SuperDAO().getListDynaBeanBySql(sql);
			}
			//更新操作员信息
			resultRs = empTransDao.update(conn, user);
			//更新操作员信息成功
			if(resultRs)
			{
				//有记录，修改IP/MAC字段为空
				if(lfMacipList != null && lfMacipList.size() > 0)
				{
					//设置IP/MAC字段为空
					sql = "UPDATE LF_MACIP SET IP_ADDR=' ', MAC_ADDR=' ' WHERE GUID IN (SELECT GUID FROM LF_SYSUSER WHERE CORP_CODE='"+corpCode+"' AND USER_NAME='admin')";
					int count = empTransDao.executeBySQLReturnCount(conn, sql);
					//更新成功设置为true
					resultRs=count>0?true:false;
				}
			}
			else
			{

				EmpExecutionContext.error("修改企业管理员admin基础信息失败，corpCode:"+corpCode);
				resultRs = false;
			}
			empTransDao.commitTransaction(conn);
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "修改企业管理员admin信息失败，corpCode:"+corpCode);
			resultRs = false;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return resultRs;
	}
}
