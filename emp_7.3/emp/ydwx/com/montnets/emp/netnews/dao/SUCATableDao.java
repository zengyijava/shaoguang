/**
 * 
 */
package com.montnets.emp.netnews.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.netnews.base.GlobalMethods;
import com.montnets.emp.netnews.entity.LfWXSORT;
import com.montnets.emp.netnews.entity.LfWXUploadFile;
import com.montnets.emp.util.PageInfo;

/**
 * @author Administrator
 *
 */
public class SUCATableDao extends BaseBiz{

	private final Logger logger = Logger.getLogger("SUCATableDao");
	
	/**
	 * 查询分类信息
	 */
	public List<DynaBean> getLfWXSORTs(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo,Long sortid){
		
		List<DynaBean> beans = null;
		LfWXSORT lfWXSORT = null;
		String sortString = null;
		try {
			if(sortid==-1L){
				sortString = "-1/";
			}else{
				lfWXSORT = getById(LfWXSORT.class, sortid);
				if(lfWXSORT!=null){
					sortString = lfWXSORT.getSort_path();
				}else{
					sortString = "-1/";
				}
			}
			
		} catch (Exception e) {
			logger.info("查询出错了");
			EmpExecutionContext.error(e, "分类查询出错！");
		}
		String fieldSql ="select a.*  ";
		String type=SystemGlobals.getValue("DBType");
		String tableSql="";
		if("4".equals(type)){
			 tableSql = "from LF_WX_UPLOADFILE a where sortid in " +
				"(SELECT CHAR (id) from LF_WX_SORT b where sort_path like '"+sortString+"%')";
		}else{
		 tableSql = "from LF_WX_UPLOADFILE a where sortid in " +
						"(SELECT id from LF_WX_SORT b where sort_path like '"+sortString+"%')";
		}
		
		String conditionSql=getConditionSql(conditionMap);
		String orderbySql = " order by a.id DESC";
		String sql=fieldSql+tableSql+conditionSql+orderbySql;
		String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql).append(conditionSql).toString(); 
		List<String> timeList =getTimeCondition(conditionMap);
		beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
		return beans;
	}
	
	/**
	 *获得sql语句的参数条件 
	 */
	public String getConditionSql(LinkedHashMap<String, String> conditionMap)
	{
		String buffer=new String();
		
		if(conditionMap.get("serahname")!=null)
		{
			buffer = buffer+" and a.filename like '%"+conditionMap.get("serahname")+"%'";
		}
		if(conditionMap.get("startdate")!=null)
		{
			buffer = buffer+" and a.CREATDATE >=?";
		}
		if(conditionMap.get("enddate")!=null)
		{
			buffer = buffer+" and a.CREATDATE <=?";
		}
		//此处增加公司的判断，区分不同公司素材
		if(conditionMap.get("lgcorpcode")!=null)
		{
			buffer = buffer+" and a.CORP_CODE ='"+conditionMap.get("lgcorpcode")+"'";
		}
		
		return buffer;
	}
	
	/**
	 * 获得时间条件
	 * @param conditionMap
	 * @return
	 */
	private List<String> getTimeCondition(LinkedHashMap<String, String> conditionMap)
	{
		List<String> timeList = new ArrayList<String>();
		if(conditionMap.get("startdate")!=null)
		{
			timeList.add(conditionMap.get("startdate"));
		}
		if(conditionMap.get("enddate")!=null)
		{
			timeList.add(conditionMap.get("enddate"));
		}
		return timeList;
	}

	/**
	 * 删除上传文件列表信息
	 * 2013-6-28
	 * @param sortPath
	 * void
	 */
	public void deleteFileBySort(String sortString) {
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		List<LfWXSORT> list;
		conditionMap.put("sort_path&like", sortString+"%");
		try {
			list = getByCondition(LfWXSORT.class, conditionMap, null);
			if(list!=null && list.size()>0){
				LfWXUploadFile lf;
				LfWXSORT l=list.get(0);
				conditionMap.clear();
				conditionMap.put("SORTID", l.getID()+"");
				List<LfWXUploadFile>	fileList=getByCondition(LfWXUploadFile.class, conditionMap, null);
				if(fileList!=null&&fileList.size()>0){
					lf = fileList.get(0);
					if(lf!=null){
						GlobalMethods.deleteFile(lf.getWEBURL());
						deleteByIds(LfWXUploadFile.class, lf.getID()+"");
					}
				}
				
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询lfwxsort表出错了！");
		}
	}
}
