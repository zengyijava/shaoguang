package com.montnets.emp.mmsmate.biz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.vo.LfMaterialVo;
import com.montnets.emp.entity.mmsmate.LfMaterial;
import com.montnets.emp.entity.mmsmate.LfMaterialSort;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * 
 * @project montnets_biz
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-15 下午03:10:38
 * @description  
 */
//彩信模板业务逻辑处理
public class MaterialBiz extends SuperBiz
{

	/**
	 *  获取彩信模板信息
	 * @param loginUserId	操作员用户ID
	 * @param conditionMap
	 * @param pageInfo	分页信息
	 * @return
	 * @throws Exception
	 */
	public List<LfMaterial> getMaterialInfo(Long loginUserId,LinkedHashMap<String,String> conditionMap,PageInfo pageInfo)throws Exception{
		//彩信模板列表
		List<LfMaterial> lfMaterialList = null;
		//设置其排序
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		try{
			orderbyMap.put("mtalId", "asc");
			if(null == pageInfo)
			{
				//查询所有模板列表
				lfMaterialList = empDao.findListBySymbolsCondition(loginUserId, LfMaterial.class, conditionMap, orderbyMap);
				return lfMaterialList;
			}
			//去除彩信素材的权限设置
			lfMaterialList = empDao.findPageListBySymbolsCondition(null, LfMaterial.class, conditionMap, orderbyMap, pageInfo);
		}catch(Exception e){
			EmpExecutionContext.error(e,"获取素材失败！");
			throw e;
		}
		return lfMaterialList;
	}
	
	/**
	 * 更新模板信息
	 * @param objectMap		//存放更新的字段
	 * @param materialId	彩信模板ID
	 * @return
	 * @throws Exception
	 */
	public boolean updateMaterial(LinkedHashMap<String,String> objectMap,String materialId)throws Exception{
		boolean updateOk = false;
		if(null == objectMap || 0 ==materialId.length()|| null == materialId)
		{
			return false;
		}
		//设置起查询条件
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		try{
			//进行数据库更新
			conditionMap.put("mtalId",materialId);
			updateOk = empDao.update(LfMaterial.class, objectMap, conditionMap);
		}catch(Exception e){
			EmpExecutionContext.error(e,"更新素材失败！");
			throw e;
		}
		return updateOk ;
	}
	
	/**
	 * 判断名称是否存在
	 * @param sortName	名称
	 * @param corpCode	企业编码
	 * @return
	 * @throws Exception
	 */
	public boolean isSortNameExists(String sortName,String corpCode)throws Exception{
		boolean exists = false;
		List<LfMaterialSort> lfMaterialSortList = new ArrayList<LfMaterialSort>();
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		try{
			//设置其查询条件
			conditionMap.put("sortName", sortName);
			conditionMap.put("corpCode", corpCode);
			//进行数据库查询
			lfMaterialSortList = empDao.findListByCondition(LfMaterialSort.class, conditionMap, null);
			if(lfMaterialSortList.size()>0)
			{
				exists = true;
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"判断分类名称是否存在失败！");
			throw e;
		}
		return exists; 
	}
	
	/**
	 * 判断模板名称是否重复
	 * @param mtalName
	 * @return
	 * @throws Exception
	 */
	public boolean isMatlNameExists(String mtalName)throws Exception{
		boolean exists = false;
		List<LfMaterial> lfMaterialList = new ArrayList<LfMaterial>();
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		try{
			//设置其条件
			conditionMap.put("mtalName", mtalName);
			//数据库查询
			lfMaterialList = empDao.findListByCondition(LfMaterial.class, conditionMap, null);
			//判断是否重复
			if(lfMaterialList.size()>0)
			{
				exists = true;
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"判断模板名称是否重复失败！");
			throw e;
		}
		return exists; 
	}
	
	/**
	 * 新增彩信素材分类
	 * @param lfMaterialSort
	 * @return
	 * @throws Exception
	 */
	public boolean addMaterialSort(LfMaterialSort lfMaterialSort) throws Exception{
		boolean addOk  = false;
		String newCode = "";
		Integer sortLevel = null;
		String parentCode = null;
	
		try
		{
			//判断是否是顶级
			if(null != lfMaterialSort.getParentCode() && 0 != lfMaterialSort.getParentCode().length())
			{
				parentCode = lfMaterialSort.getParentCode();
				//设置其级别
				sortLevel = this.getSortLevel(parentCode);
				lfMaterialSort.setSortLevel(sortLevel);
				 addOk = empDao.save(lfMaterialSort);
				//分类级别
			}
			/*if( null != parentCode   && null != sortLevel)	
			{*/
				//获取新的编码
			 /* newCode = this.getNewcode(lfMaterialSort.getParentCode(),
					lfMaterialSort.getSortLevel());
			  lfMaterialSort.setChildCode(newCode);	*/	
			  //素材分类编码
			 // lfMaterialSort.setSortLevel(sortLevel);	
			  //分类级别
			//}
			  
			
		}catch(Exception e){
			EmpExecutionContext.error(e,"新增彩信素材分类失败！");
			throw e;
		}
		return addOk ;
	}
	
	/**
	 * 更新素材名称
	 * @param sortName	名称
	 * @param sortId	素材ID
	 * @return
	 * @throws Exception
	 */
	public boolean updateSortName(String sortName,String sortId)throws Exception{
		boolean updateOk = false;
		LinkedHashMap<String,String> objectMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		try{
			//设置修改的名称
			objectMap.put("sortName", sortName);
			//设置查询的条件
			conditionMap.put("sortId", sortId);
			//执行数据库操作
			updateOk = empDao.update(LfMaterialSort.class, objectMap, conditionMap);
		}catch(Exception e){
			EmpExecutionContext.error(e,"更新素材名称失败！");
			throw e;
		}
		return updateOk;
	}

	/**
	 * 获取素材分类的树
	 * @return
	 */
	public  String getMaterialJosnData(){
	 
		List<LfMaterialSort> lfMaterialSortList;
		StringBuffer tree = null;
		try {
			//获取所有类别分类列别
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			orderbyMap.put("sortId", "asc");
			lfMaterialSortList = empDao.findListByCondition(LfMaterialSort.class, null, orderbyMap);
			
			LfMaterialSort lfMaterialSort = null;
			tree = new StringBuffer("[");
			for (int i = 0; i < lfMaterialSortList.size(); i++) {
				lfMaterialSort = lfMaterialSortList.get(i);
				tree.append("{");
				tree.append("id:").append(lfMaterialSort.getChildCode());
				tree.append(",name:'").append(lfMaterialSort.getSortName()).append("'");
				tree.append(",pId:").append(lfMaterialSort.getParentCode());
				tree.append(",isParent:").append(true);
				tree.append("}");
				if(i != lfMaterialSortList.size()-1){
					tree.append(",");
				}
			}
			tree.append("]");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取素材分类的树出现异常！");
		}
		if(tree==null){
			return "[]";
		}
		return tree.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public  String getMaterialJosnData(String corpCode){
	 
		List<LfMaterialSort> lfMaterialSortList;
		StringBuffer tree = null;
		try {
			//设置条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", corpCode+",100000");
			//设置序列
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			orderbyMap.put("sortId", "asc");
			//查询
			lfMaterialSortList = empDao.findListBySymbolsCondition(LfMaterialSort.class, conditionMap, orderbyMap);
			//构造数
			LfMaterialSort lfMaterialSort = null;
			tree = new StringBuffer("[");
			for (int i = 0; i < lfMaterialSortList.size(); i++) {
				lfMaterialSort = lfMaterialSortList.get(i);
				tree.append("{");
				tree.append("id:").append(lfMaterialSort.getSortId());
				tree.append(",childCode:").append(lfMaterialSort.getChildCode());
				tree.append(",name:'").append(lfMaterialSort.getSortName()).append("'");
				tree.append(",pId:").append(lfMaterialSort.getParentCode());
				tree.append(",isParent:").append(true);
				tree.append("}");
				if(i != lfMaterialSortList.size()-1){
					tree.append(",");
				}
			}
			tree.append("]");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取素材分类的树出现异常！");
		}
		if(tree==null){
			return "[]";
		}
		return tree.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public  String getMaterialJosnData2(String corpCode){
	 
		List<LfMaterialSort> lfMaterialSortList;
		StringBuffer tree = null;
		try {
			//设置条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", corpCode+",100000");
			//设置序列
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			orderbyMap.put("sortId", "asc");
			//查询
			lfMaterialSortList = empDao.findListBySymbolsCondition(LfMaterialSort.class, conditionMap, orderbyMap);
			//构造数
			LfMaterialSort lfMaterialSort = null;
			List<Integer> sortIds = new ArrayList<Integer>();
			tree = new StringBuffer("[");
			for (int i = 0; i < lfMaterialSortList.size(); i++) {
				lfMaterialSort = lfMaterialSortList.get(i);
				sortIds.add(lfMaterialSort.getSortId());
				tree.append("{");
				//tree.append("id:").append(lfMaterialSort.getChildCode());
				tree.append("id:").append(lfMaterialSort.getSortId());
				tree.append(",name:'").append(lfMaterialSort.getSortName()).append("'");
				tree.append(",pId:").append(lfMaterialSort.getParentCode());
				tree.append(",isParent:").append(true);
				tree.append("}");
				if(i != lfMaterialSortList.size()-1){
					tree.append(",");
				}
			}
			//获取素材
			String strSortIds = sortIds.toString();
			strSortIds = strSortIds.substring(1,strSortIds.length()-1);
			conditionMap.clear();
			conditionMap.put("sortId&in", strSortIds);
			conditionMap.put("corpCode", corpCode);
			List<LfMaterial> lfMaterials= empDao.findListBySymbolsCondition(LfMaterial.class, conditionMap, null);
			LfMaterial lfMaterial = null;
			if(!lfMaterialSortList.isEmpty() && !lfMaterials.isEmpty()){
				tree.append(",");
			}
			for (int i = 0; i < lfMaterials.size(); i++) {
				lfMaterial = lfMaterials.get(i);
				
				
				tree.append("{");
				tree.append("id:'u").append(lfMaterial.getMtalId()).append("'");
				tree.append(",address:'").append(lfMaterial.getMtalAddress()).append("'");
				tree.append(",type:'").append(lfMaterial.getMtalType().toUpperCase()).append("'");
				tree.append(",size:'").append(lfMaterial.getMtalSize()).append("'");
				tree.append(",width:").append(lfMaterial.getMtalWidth());
				tree.append(",height:").append(lfMaterial.getMtalHeight());
				tree.append(",name:'").append(lfMaterial.getMtalName()).append("'");
				tree.append(",pId:").append(lfMaterial.getSortId());
				tree.append(",isParent:").append(false);
				tree.append("}");
					
				
				if(i != lfMaterials.size()-1){
					tree.append(",");
				}
			}
			tree.append("]");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取素材分类的树出现异常！");
		}
		return tree.toString();
	}
	
	/**
	 *  通过用户ID 获取其用户名称
	 * @return
	 * @throws Exception
	 */
	public Map<Long,String> getUserNameByUserId(String corpCode) throws Exception
	{
		Map<Long,String> userInfoMap = new LinkedHashMap<Long,String>();
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		List<LfSysuser> lfSysuserList = null;
		try
		{
			conditionMap.put("corpCode&in", corpCode+",100000");
			//查询所有操作员
			lfSysuserList = empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);
			//放入MAP中
			for(LfSysuser lfSysuser:lfSysuserList)
			{
				userInfoMap.put(lfSysuser.getUserId(), lfSysuser.getName()+"("+lfSysuser.getUserName()+")");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过用户ID获取用户名称失败！");
			 throw e;
		}
		return userInfoMap;
	}
	

	/**
	 *  获取彩信素材信息
	 * @param loginUserId	操作员ID
	 * @param conditionMap	条件
	 * @param pageInfo	分页信息
	 * @return
	 * @throws Exception
	 */
	public List<LfMaterialVo> getMaterialInfos(Long loginUserId,LinkedHashMap<String,String> conditionMap,PageInfo pageInfo)throws Exception{
		List<LfMaterialVo> lfMaterialVoList = new ArrayList<LfMaterialVo>(); 
		List<LfMaterial> lfMaterialList = null;
		Map<Long,String> userInfoMap = null;
		LfMaterialVo lfMaterialVo = null;
		
		try{
			//获取所有操作员信息
			LfSysuser curUser = empDao.findObjectByID(LfSysuser.class, loginUserId);
			userInfoMap = this.getUserNameByUserId(curUser.getCorpCode());
			//获取所有彩信模板信息
			lfMaterialList = this.getMaterialInfo(loginUserId, conditionMap, pageInfo);
			//循环获取VO
			for(LfMaterial lfMaterial:lfMaterialList)
			{
				lfMaterialVo = new LfMaterialVo();
				lfMaterialVo.setMtalId(lfMaterial.getMtalId());
				lfMaterialVo.setMtalName(lfMaterial.getMtalName());
				lfMaterialVo.setMtalSize(lfMaterial.getMtalSize());
				lfMaterialVo.setMtalType(lfMaterial.getMtalType());
				lfMaterialVo.setMtalUptime(lfMaterial.getMtalUptime());
				lfMaterialVo.setSortId(lfMaterial.getSortId());
				lfMaterialVo.setUserId(lfMaterial.getUserId());
				lfMaterialVo.setUserName(userInfoMap.get(lfMaterial.getUserId()));
				//lfMaterialVo.setUserState(user)
				lfMaterialVo.setComments(lfMaterial.getComments());
				lfMaterialVo.setMtalAddress(lfMaterial.getMtalAddress());
				lfMaterialVo.setMtalHeight(lfMaterial.getMtalHeight());
				lfMaterialVo.setMtalWidth(lfMaterial.getMtalWidth());
				lfMaterialVoList.add(lfMaterialVo);
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"获取彩信素材信息失败！");
			throw e;
		}
		
		return lfMaterialVoList;
		
	}
	
	/**
	 * 通过分类编码获取素材分类ID
	 * @param childCode
	 * @return
	 * @throws Exception
	 */
	public Integer getSortIdByChildCode(String childCode)throws Exception
	{
		Integer sortId = null;
 		List<LfMaterialSort> lfMaterialSortList = new ArrayList<LfMaterialSort>();
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		try
		{
			//设置条件
			conditionMap.put("sortId", childCode);
			//数据库查询
			lfMaterialSortList = empDao.findListByCondition(LfMaterialSort.class, conditionMap, null);
			
			if(null !=lfMaterialSortList && 0 != lfMaterialSortList.size())
			{
				for(LfMaterialSort lfMaterialSort:lfMaterialSortList)
				{
					sortId = lfMaterialSort.getSortId();
				}
			}
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过分类编码获取素材分类ID失败！");
			 throw e;
		}
		return sortId;
	}
	
	/**
	 * 获取类别的级别
	 * @param parentCode
	 * @return
	 * @throws Exception
	 */
	public Integer getSortLevel(String parentCode)throws Exception{
		
		Integer sortLevel = null;
		List<LfMaterialSort> lfMaterialSortList = new ArrayList<LfMaterialSort>();
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		try
		{
			//设置查询条件 
			conditionMap.put("sortId", parentCode);
			//查询相应的彩信 素材分裂的对象
			lfMaterialSortList = empDao.findListByCondition(LfMaterialSort.class, conditionMap, null);
			//获取其彩信素材分类对象ID
			if(null !=lfMaterialSortList && 0 != lfMaterialSortList.size())
			{
				for(LfMaterialSort lfMaterialSort:lfMaterialSortList)
				{
					sortLevel = lfMaterialSort.getSortLevel();
				}
			}
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取类别的级别失败！");
			//进入异常
			 throw e;
		}
		return sortLevel;
		
	}
	
	/**
	 *  获取新的编码
	 * @param parentCode	父类编码
	 * @param level			级别
	 * @return
	 * @throws Exception
	 */
	public String getNewcode(String parentCode,int level)throws Exception
	{
		String newCode = "";
		//条件
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		List<LfMaterialSort> list = null;
		boolean exists = true;
		int count = 1;
		while(exists)
		{
			if(parentCode.equals("0"))
			{
				parentCode = "1000000000";
			}
			int temp[] = new int[parentCode.length()];
			for(int i = 0;i<parentCode.length();i++)
			{
				temp[i] = Integer.parseInt(parentCode.substring(i,i+1));
				if(level == i)
				{
					temp[i]+=count;
				}
				if(temp[i]<0)
				{
					newCode+="0"+temp[i];
				}else
				{
					newCode+=temp[i];
				}
			}
			
			conditionMap.put("childCode",newCode);
			try {
			list = empDao.findListByCondition(LfMaterialSort.class, conditionMap, null);
			if(null != list && list.size()>0)
			{
				exists = true;
				newCode = "";
				count++;
 			}else{
 				exists = false;
 			}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"获取新的编码失败！");
				throw e;
			}
		}
		return newCode;
		
	}
	
	/**
	 * 删除彩信素材分类
	 * @param depCode
	 * @return
	 */
	public Integer delMaterialSortDep(String depCode) {
		
		Integer intResult = -1;
		try{
			//设置其查询条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("parentCode", depCode);
			//查询出所有 的彩信子素材分类 
			List<LfMaterialSort> materialSortsList = empDao.findListByCondition(LfMaterialSort.class, conditionMap, null);
			//该素材类别存在子类别，不允许删除
			if(materialSortsList != null && materialSortsList.size() > 0){
				intResult = 0;
				return intResult;
			}
			
			conditionMap.clear();
			//对该类别做删除操作
			conditionMap.put("sortId", depCode);
			int delNum = empDao.delete(LfMaterialSort.class, conditionMap);
			if(delNum > 0){
				intResult = 1;
			}else{
				intResult = -1;
			}
			
		}catch(Exception e){
			//进入异常
			EmpExecutionContext.error(e,"删除彩信素材分类出现异常！");
		}
		return intResult;
	}
 
	
	
	/**
	 *  通过父编码获取子类的IDS
	 * @param parentCode
	 * @return
	 * @throws Exception
	 */
	public String getSortIdsByParentCode(String parentCode)throws Exception
	{
		String sortIds = "";
		boolean doall = false;
 		List<LfMaterialSort> lfMaterialSortList = new ArrayList<LfMaterialSort>();
 		//设置条件的MAP
 		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		try
		{
			while(!doall)
			{
				conditionMap.put("parentCode&in", parentCode);
				parentCode = "";
				lfMaterialSortList = empDao.findListBySymbolsCondition(LfMaterialSort.class, conditionMap, null);
				conditionMap.clear();
				if(null !=lfMaterialSortList && 0 != lfMaterialSortList.size())
				{
					for(LfMaterialSort lfMaterialSort:lfMaterialSortList)
					{
						sortIds += lfMaterialSort.getSortId()+",";
						parentCode += lfMaterialSort.getChildCode()+",";						
					}
					
					if( null != parentCode && !"".equals(parentCode))
					{
						parentCode = parentCode.substring(0,parentCode.lastIndexOf(","));
					}
				}else
				{
					doall = true;
				}
				if(lfMaterialSortList!=null){
				lfMaterialSortList.clear();
				}
 				
				
			}
			
			
			/*if( null != sortIds && 0 < sortIds.length())
			{
				sortIds = sortIds.substring(0,sortIds.lastIndexOf(","));
			}*/
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过父编码获取子类的ID集合失败！");
			 throw e;
		}
		return sortIds;
	}
}
