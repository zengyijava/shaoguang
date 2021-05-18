package com.montnets.emp.shorturl.surlmanage.biz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;

import com.montnets.emp.shorturl.surlmanage.dao.UrlManagerDao;
import com.montnets.emp.shorturl.surlmanage.vo.LfNeturlVo;
import com.montnets.emp.util.PageInfo;

public class UrlManagerBiz extends SuperBiz{
	
	
	public List<LfNeturlVo> getNeturlVos(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo){
		if (conditionMap.get("lguserid")==null) {
			return null;
		}
		List<LfNeturlVo > urlList = null;
		try {
			//分页不为空，需要分页
			if (pageInfo != null)
			{
				//按条件获取记录，分页
				urlList = new UrlManagerDao().getNeturlVos(conditionMap,pageInfo);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询url源地址集合biz异常");
		}
		return urlList;
	}

	/**\
	 * 逻辑删除
	 * @param urlid
	 * @param ispass
	 * @return
	 */

	public boolean update(String urlid, String ispass) {
		
		return new UrlManagerDao().update(urlid,ispass);
	}
	
	/**
	 * 查所有创建者的名字
	 * @param urlList
	 * @return
	 */
	public Map<String, String> getAllUser(List<LfNeturlVo> urlList) {
		Map<String, String> users = new LinkedHashMap<String, String>();
		if (urlList==null||urlList.size()<1) {
			return users;
		}
		String userids = "";
		for (int i = 0; i < urlList.size(); i++) {
			userids += urlList.get(i).getCreatetm()+",";
		}
		userids = userids.substring(0,userids.length()-1);
		users = new UrlManagerDao().getusers(userids);
		return users;
	}
	/**
	 * 加载机构
	 * @description    
	 * @param depId
	 * @param userId
	 * @param corpCode
	 * @return       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-13 下午02:39:59
	 */
	public String getDepartmentJosnData2(Long depId,Long userId,String corpCode) {
		//机构树json串
		StringBuffer tree = null;
		//操作员对象
		LfSysuser sysuser = null;
		try {
			//根据操作员ID获得操作员对象
			sysuser = empDao.findObjectByID(LfSysuser.class, userId);
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
