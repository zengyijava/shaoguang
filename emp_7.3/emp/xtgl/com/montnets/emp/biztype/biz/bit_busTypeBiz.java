package com.montnets.emp.biztype.biz;

import com.montnets.emp.biztype.dao.bit_busTypeDao;
import com.montnets.emp.biztype.vo.LfBusManagerVo;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 
 *<p>project name p_xtgl</p>
 *<p>Title: bit_busTypeBiz</p>
 *<p>Description: </p>
 *<p>Company: Montnets Technology CO.,LTD.</p>
 * @author dingzx
 * @date 2015-1-14下午02:33:25
 */
public class bit_busTypeBiz extends SuperBiz {
	
	private bit_busTypeDao busTypeDao;
	public bit_busTypeBiz()
	{
		busTypeDao = new bit_busTypeDao();
	}

	/**
	 * 查询业务集合
	 * @description    
	 * @param lfBusManagerVo  业务条件
	 * @param pageInfo  分页
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-13 下午02:08:32
	 */
	public List<LfBusManagerVo> getLfBusManagerVo(LfBusManagerVo lfBusManagerVo, PageInfo pageInfo)throws Exception {
		List<LfBusManagerVo> busManagerVosList = null;
		try {
			busManagerVosList = busTypeDao.findLfBusManagerVo(lfBusManagerVo, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "业务管理查询异常。");
			//异常处理
			throw e;
		}
		return busManagerVosList;
	}
	
	/**
	 * 加载机构树
	 * @description    
	 * @param userid
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-13 下午02:34:37
	 */
	public String getDepartmentJosnData(Long userid)throws Exception {
		StringBuffer tree = null;
		//根据用户id获取用户信息
		LfSysuser currUser = empDao.findObjectByID(LfSysuser.class, userid);
		if(currUser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			//机构biz
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			
			try {
				//如果是100000的企业
				if(currUser.getCorpCode().equals("100000"))
				{
					LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
					LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
					conditionMap.put("depState", "1");
					orderbyMap.put("depId", StaticValue.ASC);
					
					lfDeps = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
				}else
				{
					//获取管辖范围内的所有机构
					lfDeps = depBiz.getAllDeps(userid );
				}
				LfDep lfDep = null;
				//机构树的json数据拼写
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					//tree.append(",level:").append(lfDep.getDepLevel());
					//tree.append(",dlevel:").append(lfDep.getDepLevel());
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
		
			} catch (Exception e) {
				//异常处理
				EmpExecutionContext.error(e,"业务管理加载机构数异常！");
			}
		}
		if(tree==null){
			return "[]";
		}
		return tree.toString();
	}
	
	/**
	 * 通过操作员id获得机构树
	 * @description    
	 * @param userid
	 * @param corpCode
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-13 下午02:35:41
	 */
	public String getDepartmentJosnData(Long userid,String corpCode)throws Exception {
		//树字符串
		StringBuffer tree = null;
		//根据用户id获取用户信息
		LfSysuser currUser = empDao.findObjectByID(LfSysuser.class, userid);
		//个人权限
		if(currUser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			//机构biz
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			
			try {
				//如果是100000的企业
				if(currUser.getCorpCode().equals("100000"))
				{	//查询条件
					LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
					LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
					//机构状态
					conditionMap.put("depState", "1");
					//机构ID
					orderbyMap.put("depId", StaticValue.ASC);
					
					lfDeps = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
				}else
				{
					//获取管辖范围内的所有机构
					lfDeps = depBiz.getAllDepByUserIdAndCorpCode(userid, corpCode);
				}
				LfDep lfDep = null;
				//机构树的json数据拼写
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					//tree.append(",level:").append(lfDep.getDepLevel());
					//tree.append(",dlevel:").append(lfDep.getDepLevel());
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
		
			} catch (Exception e) {
				//异常处理
				EmpExecutionContext.error(e,"业务管理加载机构数异常！");
			}
		}
		if(tree==null){
			return "[]";
		}
		return tree.toString();
	}
	
	/**
	 * 业务管理加载机构
	 * @description    
	 * @param depId
	 * @param userId
	 * @return       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-13 下午02:39:02
	 */
	public String getDepartmentJosnData2(Long depId,Long userId) {
		//树json格式字符串
		StringBuffer tree = null;
		//操作员对象
		LfSysuser sysuser = null;
		try {
			//更具当前操作员id获取操作员对象
			sysuser = empDao.findObjectByID(LfSysuser.class, userId);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务管理加载机构树异常！");
			tree = new StringBuffer("[]");
		}
		//判断是否是个人权限
		if(sysuser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			//机构业务类
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			
			try {
				lfDeps = null;
				 //操作员企业编码
				 if ((sysuser != null) && (sysuser.getCorpCode().equals("100000")))
			        {
			          if (depId == null)
			          {//查询条件
			            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			            LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			            //父级ID
			            conditionMap.put("superiorId", "0");
			            //机构状态
			            conditionMap.put("depState", "1");
			            //机构id 排倒序
			            orderbyMap.put("depId", "ASC");
			            //机构path
			            orderbyMap.put("deppath", "ASC");
			            lfDeps = new BaseBiz().getByCondition(LfDep.class, conditionMap, orderbyMap);
			          }
			          else
			          {
			        	lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			          }

			        }
			        else if (depId == null) {
			          lfDeps = new ArrayList();
			          LfDep lfDep = (LfDep)depBiz.getAllDeps(userId).get(0);
			          lfDeps.add(lfDep);
			        }
			        else {
			          lfDeps = new DepBiz().getDepsByDepSuperId(depId);
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
				EmpExecutionContext.error(e,"业务管理加载机构数异常");
				tree = new StringBuffer("[]");
			}
		}
		return tree.toString();
	}
	/**
	 * 业务管理加载机构
	 * @description    
	 * @param depId
	 * @param sysuser
	 * @return       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-13 下午02:39:59
	 */
	public String getDepartmentJosnData2(Long depId, LfSysuser sysuser) {
		//机构树json串
		StringBuffer tree = null;
		//操作员对象
//		LfSysuser sysuser = null;
        String corpCode = null;
        Long userId = null;
		try {
			//根据操作员ID获得操作员对象
//			sysuser = empDao.findObjectByID(LfSysuser.class, userId);
            if(sysuser != null) {
                corpCode = sysuser.getCorpCode();
                userId =  sysuser.getUserId();
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务管理加载机构树异常！");
			tree = new StringBuffer("[]");
		}
		//个人权限
		if(sysuser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			//机构biz
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			
			try {
				lfDeps = null;
				//判断当前登录操作员对象是否为空，并且企业编码是否为100000
				 if ((sysuser != null) && (sysuser.getCorpCode().equals("100000")))
			        {
			          if (depId == null)
			          {//查询条件
			            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			            LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			            //父级机构
			            conditionMap.put("superiorId", "0");
			            //机构状态
			            conditionMap.put("depState", "1");
			            //机构id
			            orderbyMap.put("depId", "ASC");
			            orderbyMap.put("deppath", "ASC");
			            lfDeps = new BaseBiz().getByCondition(LfDep.class, conditionMap, orderbyMap);
			          }
			          else
			          {//通过机构ID获取机构列表
			        	lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			          }

			        }
			        else if (depId == null) {
			          lfDeps = new ArrayList();
			          //LfDep lfDep = (LfDep)depBiz.getAllDeps(userId).get(0);
			          LfDep lfDep = (LfDep) depBiz.getAllDepByUserIdAndCorpCode(userId, corpCode).get(0);
			          lfDeps.add(lfDep);
			        }
			        else {
			          //lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			          lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,corpCode);
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
				EmpExecutionContext.error(e,"业务管理加载机构数异常");
				tree = new StringBuffer("[]");
			}
		}
		return tree.toString();
	}
}
