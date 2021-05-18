package com.montnets.emp.group.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.segnumber.PbServicetype;
import com.montnets.emp.group.dao.GenericGroupInfoVoDAO;
import com.montnets.emp.util.PageInfo;

public class GroupManagerBiz extends GrpBaseMgrBiz{

	private final GenericGroupInfoVoDAO groupInfoVoDAO;
	private final BaseBiz baseBiz = new BaseBiz();
	public GroupManagerBiz()
	{
		groupInfoVoDAO = new GenericGroupInfoVoDAO();
		addrbookSpecialDAO = new AddrBookSpecialDAO();
	}
	
	/**
	 * 	删除群组成员
	 * @param l2gId	关联ID
	 * @return
	 * @throws Exception
	 */
	public Integer delGroupMembers(String l2gId) throws Exception
	{
		//结果
		Integer result = 0;
		//设值MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//设值
			conditionMap.put("l2gId&in", l2gId);
			//删除操作
			result = empDao.delete(LfList2gro.class, conditionMap);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"删除个人群组成员失败！");
			throw e;
		}
		//返回
		return result;
	}
	
	/**
	 * 更新群组成员信息
	 * @param l2gId
	 * @param udgId
	 * @return
	 * @throws Exception
	 */
	public boolean updateGroupInfo(String l2gId, String udgId) throws Exception
	{
		//结果
		boolean updateok = false;
		//更新字段
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		//更新条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
			//设值
			objectMap.put("udgId", udgId);
			conditionMap.put("l2gId", l2gId);
			//执行数据库操作
			updateok = empDao.update(LfList2gro.class, objectMap, conditionMap);

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"更新个人群组成员失败！");
			throw e;
		}
		//返回
		return updateok;
	}
	
	/**
	 * 	删除群组信息
	 * @param udgId	群组
	 * @return
	 * @throws Exception
	 */
	public Integer delGroupsAllInfo(String udgId) throws Exception
	{
		//判断ID是否为空 
		if (udgId == null)
		{
			//返回
			return null;
		}
		//删除关联操作
		this.delGroupsRecords(udgId);
		//删除群组
		Integer result = empDao.delete(LfUdgroup.class, udgId);
		//返回结果
		return result;
	}
	
	// 验证群组是否存在成员
	public boolean checkGrMember(String udgId, String personId)
			throws Exception
	{
		// Integer re = 0;
		// List<LfList2gro> lfList2groList=new ArrayList<LfList2gro>();
		//结果
		boolean exists = false;
		//设值MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//设值
			conditionMap.put("guId", personId);
			conditionMap.put("udgId", udgId);
			//查询
			List<LfList2gro> r = new BaseBiz().getByCondition(LfList2gro.class,
					conditionMap, null);
			//有记录，已存在
			if (r != null && r.size() > 0)
			{
				exists = true;
			}
			//清空
			conditionMap.clear();

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"验证群组成员重复异常！");
			throw e;
		}
		//返回
		return exists;
	}
	
	/**
	 * 删除群组关联记录
	 * @param udgId
	 * @return
	 * @throws Exception
	 */
	private Integer delGroupsRecords(String udgId) throws Exception
	{
		//结果
		Integer result = 0;
		//设值MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//设值
			conditionMap.put("udgId", udgId);
			//删除关联
			result = empDao.delete(LfList2gro.class, conditionMap);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"删除群组关联记录失败！");
			throw e;
		}
		//返回结果
		return result;
	}
	
	/**
	 * 通过群组ID 以及登录远的用户ID 查询出该群组里的人员的信息
	 * 
	 * @param udgId
	 * @param userId
	 * @return
	 */
	public List<GroupInfoVo> getGroupVoInfo(Long udgId, Long userId)
	{
		//群组成员列表
		List<GroupInfoVo> groupVosList = null;
		try
		{
			//群组对象
			GroupInfoVo groupInfoVo = new GroupInfoVo();
			groupInfoVo.setUdgId(udgId);
			groupInfoVo.setUserId(userId);
			//分页对象
			PageInfo pageinfo = new PageInfo();
			pageinfo.setPageIndex(1);
			pageinfo.setPageSize(Integer.MAX_VALUE);
			//查询
			groupVosList = groupInfoVoDAO.findGroupInfoVo(userId, groupInfoVo,
                    pageinfo);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"查询群组成员记录失败！");
		}
		//返回
		return groupVosList;
	}
	
	/**
	 * 判断群组是否存在 
	 * @param udgId	群组ID
	 * @return
	 * @throws Exception
	 */
	public boolean groupMemberExists(String udgId) throws Exception
	{
		//结果
		boolean exists = false;
		//群组成员对象集合
		List<LfList2gro> lfList2groList = new ArrayList<LfList2gro>();
		//设值MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//设值
			conditionMap.put("udgId", udgId);
			//查询
			lfList2groList = empDao.findListByCondition(LfList2gro.class,
					conditionMap, null);
			//判断是否为空
			if (lfList2groList.size() > 0)
			{
				//返回
				exists = true;
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"验证群组人员存在失败！");
			throw e;
		}
		//返回结果
		return exists;
	}
	
	/**
	 * 复制自建群组添加人员的方法，并且加过滤手机号码
	 * 
	 * @param udgId
	 * @param personId
	 * @param l2gType
	 * @return
	 * @throws Exception
	 */
	public String addList2groCheckMoblie(String udgId, String[] personId,
			String l2gType, Long userId) throws Exception
	{
		// 成功条数
		Integer result = 0; 
		String returnMsg = "";
		//群组成员对象集合
		List<LfList2gro> lfList2groList = new ArrayList<LfList2gro>();
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			GroupInfoVo groupInfoVo = new GroupInfoVo();
			groupInfoVo.setUdgId(Long.valueOf(udgId));
			// 把该群组下的所有人员的手机放在HASHSET中
			groupInfoVo.setUserId(userId);
			//分页对象
			PageInfo pageinfo = new PageInfo();
			pageinfo.setPageIndex(1);
			pageinfo.setPageSize(Integer.MAX_VALUE);
			//查询记录
			List<GroupInfoVo> groupVosList = groupInfoVoDAO.findGroupInfoVo(
					userId, groupInfoVo, pageinfo);
			HashSet<String> repeatList = new HashSet<String>();
			if (groupVosList != null && groupVosList.size() > 0)
			{
				for (int i = 0; i < groupVosList.size(); i++)
				{
					GroupInfoVo groupinfo = groupVosList.get(i);
					String mobile = groupinfo.getMobile();
					if (mobile != null && !"".equals(mobile))
					{
						repeatList.add(mobile);
					}
				}
			}
			StringBuffer badStr = new StringBuffer("");
			for (int i = 0; i < personId.length; i++)
			{
				// 装的分别的GUID和手机号码
				String guidMoblie[] = personId[i].split("&"); 
				conditionMap.put("guId", guidMoblie[0]);
				conditionMap.put("udgId", udgId);
				List<LfList2gro> person = new BaseBiz().getByCondition(
						LfList2gro.class, conditionMap, null);
				if (person == null || person.size() == 0)
				{
					if (this.checkRepeat(repeatList, guidMoblie[1]))
					{
						LfList2gro lfList2gro = new LfList2gro();
						lfList2gro.setGuId(Long.parseLong(guidMoblie[0]));
						lfList2gro.setL2gType(new Integer(l2gType));
						lfList2gro.setUdgId(new Integer(udgId));
						lfList2groList.add(lfList2gro);
					} else
					{
						badStr.append(guidMoblie[1]).append(",");
					}
				}
				conditionMap.clear();
			}
			if (lfList2groList.size() > 0)
			{
				result = empDao.save(lfList2groList, LfList2gro.class);
			}
			String badMoblie = badStr.toString();
			if (badMoblie.length() > 0)
			{
				returnMsg = badMoblie.substring(0, badMoblie.length() - 1);
				returnMsg = returnMsg + "&" + result;
			} else
			{
				returnMsg = "&" + result;
			}
		} catch (Exception e)
		{
			returnMsg = "&-1";
			//异常处理
			EmpExecutionContext.error(e,"添加群组人员号码记录失败！");
		}
		//返回结果
		return returnMsg;
	}
	
	/**
	 * 验证重复
	 * 
	 * @param aa
	 * @param ee
	 * @return
	 */
	private boolean checkRepeat(HashSet<String> aa, String ee)
	{
		try
		{
			//判断是否包含
			if (aa.contains(ee))
			{
				//返回
				return false;
			} else
			{
				aa.add(ee);
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"群组成员验证号码重复出现异常！");
			return false;
		}
		//返回结果
		return true;
	}
	
	/**
	 * 通过群组Id获取员工信息,个人群组模块使用
	 * @param udgId
	 * @param loginId
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployee> getEmployeeByGuidForGroup(String udgId,String loginId,LinkedHashMap<String, String> conditionMap) throws Exception
	{
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("name", StaticValue.ASC);
		return addrbookSpecialDAO.getEmployeeByGuidForGroup(udgId, loginId, conditionMap, orderbyMap, null);
	}
	
	/**
	 * 通过群组Id获取自定义通讯录内容
	 * @param udgId 群组Id
	 * @param loginId 操作员ID
	 * @param conditionMap 查询条件
	 * @param shareType 共享类型
	 * @return
	 * @throws Exception
	 */
	public List<LfMalist> getMalistListByUdgId(String udgId,String loginId,LinkedHashMap<String, String> conditionMap,Integer shareType) throws Exception
	{
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("name", StaticValue.ASC);
		return addrbookSpecialDAO.getMalistByGuid(udgId, loginId, conditionMap, orderbyMap, null,shareType);
	}
	
	/**
	 * 共享群组
	 * @param addList 新添加的共享关系
	 * @param deleteList 已经去除共享的集合
	 * @return
	 */
	public boolean shareGroup(List<LfUdgroup> addList,String deleteList) {
		Connection conn = empTransDao.getConnection();
		//新添加的个数统计
		int addCount = 0;
		//删除的个数统计
		int deleteCount = 0;
		boolean result = false;
		try {
			//开始事务
			empTransDao.beginTransaction(conn);
			if(addList != null && addList.size()>0){
				addCount = empTransDao.save(conn, addList, LfUdgroup.class);
				result = addCount > 0;
			}
			if(deleteList != null && !"".equals(deleteList)){
				deleteCount = empTransDao.delete(conn, LfUdgroup.class, deleteList);
				result = deleteCount > 0;
			}
			//提交事务
			empTransDao.commitTransaction(conn);
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"共享群组出现异常！");
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			return false;
		} finally {
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 共享群组
	 * @param addList 新添加的共享关系
	 * @param deleteList 已经去除共享的集合
	 * @param lfUdgroup  需要修改的共享状态
	 * @return
	 */
	public boolean shareGroup(List<LfUdgroup> addList,String deleteList,LfUdgroup lfUdgroup) {
		Connection conn = empTransDao.getConnection();
		//新添加的个数统计
		int addCount = 0;
		//删除的个数统计
		int deleteCount = 0;
		boolean result = false;
		try {
			//开始事务
			empTransDao.beginTransaction(conn);
			if(addList != null && addList.size()>0){
				addCount = empTransDao.save(conn, addList, LfUdgroup.class);
				result = addCount > 0;
			}
			if(deleteList != null && !"".equals(deleteList)){
				deleteCount = empTransDao.delete(conn, LfUdgroup.class, deleteList);
				result = deleteCount > 0;
			}
			result = empTransDao.update(conn, lfUdgroup);
			//提交事务
			empTransDao.commitTransaction(conn);
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"共享群组出现异常！");
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			return false;
		} finally {
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**获取所有的 号段
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] getHaoduan() throws Exception
	{
		String[] haoduans = new String[3];
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			List<PbServicetype> haoduanList = empDao.findListByCondition(
					PbServicetype.class, conditionMap, null);
			for (int i = 0; i < haoduanList.size(); i++)
			{
				PbServicetype pbSer = haoduanList.get(i);
				if (pbSer.getSpisuncm() == 0)
				{
					haoduans[0] = pbSer.getServiceno();
				} else if (pbSer.getSpisuncm() == 1)
				{
					haoduans[1] = pbSer.getServiceno();
				} else if (pbSer.getSpisuncm() == 21)
				{
					haoduans[2] = pbSer.getServiceno();
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取号段异常！");
			throw e;
		}
		return haoduans;
	}
	
	/**
	 * 通过员工机构id查找树
	 * @param userId
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId,String depId) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			deps = addrbookSpecialDAO.getEmpSecondDepTreeByUserIdorDepId( userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"群组中获取员工机构树出现异常！");
		}
		return deps;
	}

    /**
     * 通过员工机构id查找树 增加企业编码条件
     * @param userId
     * @param depId
     * @param corpCode
     * @return
     * @throws Exception
     */
    public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId,String depId,String corpCode) throws Exception {
        List<LfEmployeeDep> deps = null;
        try{
            deps = addrbookSpecialDAO.getEmpSecondDepTreeByUserIdorDepId( userId,depId,corpCode);
        }catch (Exception e) {
            EmpExecutionContext.error(e,"群组中获取员工机构树出现异常！");
        }
        return deps;
    }
	
	/**
	 *  检测号码
	 * @param mobile
	 * @param haoduan
	 * @return
	 * @throws Exception
	 */
	public int checkMobile(String mobile, String[] haoduan) throws Exception
	{
		if (mobile.length() != 11)
			return 0;
		for (int k = mobile.length(); --k >= 0;)
		{
			if (!Character.isDigit(mobile.charAt(k)))
			{
				return 0;
			}
		}

		String number = haoduan[0] + "," + haoduan[1] + "," + haoduan[2];
		if (number.replace(mobile.substring(0, 3), "").length() == number
				.length())
		{
			return 0;
		}
		return 1;
	}

	/**
	 * 群组修改
	 *
	 * @param flag 1表示员工  2表示客户
	 * @param curName
	 * @param udgName
	 * @param ygStr
	 * @param qzStr
	 * @param gxStr
	 * @param zjStr
	 * @param userid
	 */
	public String editGroup(int flag, String curName, String udgName, String ygStr, String qzStr, String gxStr, String zjStr, String userid, String uid, String lgcorpcode) {
		Connection conn = null;
		try {
			conn = baseBiz.getConnection();
			//开启事务
			baseBiz.beginTransaction(conn);
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			if(!udgName.equals(curName)) {
				conditionMap.put("userId", userid);
				conditionMap.put("udgName", udgName);
				List<LfUdgroup> udList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, null);
				if(udList != null && udList.size() > 0) {
					return "exists";
				}else {
					//更新群组名称
					conditionMap.remove("udgName");
					LfUdgroup group = baseBiz.getById(LfUdgroup.class, uid);
					conditionMap.put("groupid", group.getGroupid().toString());
					LinkedHashMap<String,String> objectMap = new LinkedHashMap<String,String>();
					objectMap.put("udgName", udgName);
					baseBiz.update(conn, LfUdgroup.class, objectMap, conditionMap);
				}
			}
			//直接将所有群组绑定的关系全部删除
			conditionMap.clear();
			conditionMap.put("udgId", uid);
			baseBiz.deleteByCondition(conn, LfList2gro.class, conditionMap);

			List<LfList2gro> l2gList = new ArrayList<LfList2gro>();
			//解析来自共享群组的记录
			addL2gList(l2gList, gxStr, 2, 1, uid);
			//解析来自自建群组的记录
			addL2gList(l2gList, qzStr, 2, 0, uid);
			//解析来自员工或客户的记录
			addL2gList(l2gList, ygStr, flag == 2 ? 1:0, 0, uid);
			//解析自建群组记录
			String[] zjArry = zjStr.split(",");
			GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
			List<LfMalist> maLists = new ArrayList<LfMalist>();
			int len = zjArry.length;
			Long curValue = globalBiz.getValueByKey("guid", len);
			for(int y=0;y<len;y++) {
				if(zjArry[y] != null && zjArry[y].length() > 0) {
					String[] infoArry = zjArry[y].split("[|]");
					LfMalist mal = new LfMalist();
					Long guid = curValue-y;
					mal.setCorpCode(lgcorpcode);
					mal.setUserId(Long.parseLong(userid));
					mal.setName(infoArry[0]);
					mal.setMobile(infoArry[1]);
					mal.setGuId(guid);
					maLists.add(mal);

					LfList2gro l2g = new LfList2gro();
					l2g.setGuId(guid);
					l2g.setUdgId(Integer.valueOf(uid));
					l2g.setL2gType(2);
					l2g.setSharetype(0);
					l2gList.add(l2g);
				}
			}
			baseBiz.addList(conn, LfMalist.class, maLists);
			baseBiz.addList(conn, LfList2gro.class, l2gList);
			//提交事务
			baseBiz.commitTransaction(conn);
			return "true";
		}catch (Exception e){
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"修改群组失败");
			return "error";
		}finally {
			empTransDao.closeConnection(conn);
		}
	}

	private void addL2gList(List<LfList2gro> l2gList, String arrayStr, int l2gType, int sharetype, String uid) {
		String[] gxArry = arrayStr.split(",");
		for (String aGxArry : gxArry) {
			if (aGxArry != null && aGxArry.length() > 0) {
				LfList2gro l2g = new LfList2gro();
				l2g.setGuId(Long.valueOf(aGxArry));
				l2g.setUdgId(Integer.valueOf(uid));
				l2g.setL2gType(l2gType);
				l2g.setSharetype(sharetype);
				l2gList.add(l2g);
			}
		}
	}
}
