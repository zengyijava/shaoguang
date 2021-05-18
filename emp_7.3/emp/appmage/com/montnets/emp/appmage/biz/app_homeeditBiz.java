/**
 * Program  : app_homeeditDao.java
 * Author   : zousy
 * Create   : 2014-6-13 上午08:40:09
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.appmage.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.montnets.emp.appmage.dao.app_homeeditDao;
import com.montnets.emp.appwg.bean.AppMainPage;
import com.montnets.emp.appwg.bean.AppMessage;
import com.montnets.emp.appwg.initbuss.HandleMsgBiz;
import com.montnets.emp.appwg.initbuss.IInterfaceBuss;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.appmage.LfAppSitInfo;
import com.montnets.emp.entity.appmage.LfAppSitPlant;
import com.montnets.emp.util.PageInfo;


/**
 * APP首页模块
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-13 上午08:40:09
 */
public class app_homeeditBiz extends SuperBiz
{
	app_homeeditDao homeDao = new app_homeeditDao();
	IInterfaceBuss wgbizBiz = new HandleMsgBiz();
	
	public List<DynaBean> getSitInfos(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception{
		return homeDao.getSitInfos(conditionMap, pageInfo);
	}
	
	public boolean delete(String sid){
		LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			condMap.put("sid", sid);
			empTransDao.delete(conn, LfAppSitPlant.class, condMap);
			empTransDao.delete(conn, LfAppSitInfo.class, condMap);
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "删除APP首页出现异常！");
			return false;
		}finally{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * @description    
	 * @param status 0 暂存  1 保存
	 * @param lgcorpcode
	 * @param lguserid
	 * @param json
	 * @param reqSid
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-6-19 下午02:19:17
	 */
	public Long saveOrCache(int status,String lgcorpcode,long lguserid,JSONObject json,String reqSid,String infoname){
		Connection conn = empTransDao.getConnection();
		boolean isUpdate = true;
		Long sid = 0L;
		try
		{
			JSONArray heads = (JSONArray)json.get("heads");
			JSONArray lists = (JSONArray)json.get("lists");
			if(StringUtils.isBlank(reqSid)||!StringUtils.isNumeric(reqSid)){
				isUpdate = false;
				sid = new DataAccessDriver().getEmpDAO().getIdByPro(532, 1);
			}else{
				sid = Long.valueOf(reqSid);
			}
			empTransDao.beginTransaction(conn);
			LinkedHashMap<String, String> cond = null;
			//修改 删除原有的plant数据
			if(isUpdate){
				cond = new LinkedHashMap<String, String>();
				cond.put("sid",reqSid);
				empTransDao.delete(conn, LfAppSitPlant.class, cond);
			}
			String infoName = null;
			List<LfAppSitPlant> plants = new ArrayList<LfAppSitPlant>();
			for(int i=0;i<heads.length();i++){
				JSONObject item = heads.getJSONObject(i);
				if(i==0){
					infoName = item.getString("title");
				}
				LfAppSitPlant plant = new LfAppSitPlant(lgcorpcode,sid);
				plant.setCreatetime(new Timestamp(System.currentTimeMillis()));
				plant.setFeildvalues(item.toString());
				plant.setPlanttype("head");
				plants.add(plant);
//				empTransDao.save(conn, plant);
				
			}
			for(int i=0;i<lists.length();i++){
				JSONObject item = lists.getJSONObject(i);
				LfAppSitPlant plant = new LfAppSitPlant(lgcorpcode,sid);
				plant.setCreatetime(new Timestamp(System.currentTimeMillis()));
				plant.setFeildvalues(item.toString());
				plant.setPlanttype("list");
				plants.add(plant);
//				empTransDao.save(conn, plant);
			}
			empTransDao.save(conn, plants, LfAppSitPlant.class);
			if(isUpdate){
				LinkedHashMap<String, String> objMap = new LinkedHashMap<String, String>();
				objMap.put("status", String.valueOf(status));
				if(StringUtils.isNotBlank(infoname)){
					objMap.put("name", StringEscapeUtils.escapeSql(infoname.trim()));
				}
				empTransDao.update(conn,LfAppSitInfo.class,objMap,cond);
			}else{
				LfAppSitInfo info = new LfAppSitInfo();
				if(StringUtils.isNotBlank(infoname)){
					info.setName(infoname.trim());
				}else{
					info.setName(infoName);
				}
				info.setStatus(status);
				info.setCorpcode(lgcorpcode);
				info.setCreatetime(new Timestamp(System.currentTimeMillis()));
				info.setUserid(lguserid);
				info.setSid(sid);
				empTransDao.save(conn, info);
			}
			empTransDao.commitTransaction(conn);
			return sid;
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "保存或暂存APP首页出现异常！");
			return 0L;
		}finally{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * @description     更新为已发布状态
	 * @param lgcorpcode
	 * @param sid
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-6-23 上午11:58:34
	 */
	public boolean updateStatus(String lgcorpcode,String sid,long validity){
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			homeDao.updateStatus(conn,lgcorpcode,sid,validity);
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "APP首页发布更新出现异常！");
			return false;
		}finally{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * @description 更新状态为 已发布--》已下架 取消发布     
	 * @param lgcorpcode
	 * @param sid
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-6-23 下午01:42:20
	 */
	public int unpublish(String lgcorpcode,String sid){
		int result = -1;
		Connection conn = null;
		try
		{
			AppMessage message = new AppMessage();
			message.setSid(Long.valueOf(sid));
			int msg = wgbizBiz.CancelSetMainPage(message);
			if(msg == 1){
				conn = empTransDao.getConnection();
				empTransDao.beginTransaction(conn);
				if(homeDao.unpublish(conn,lgcorpcode,sid)<1){
					result = -1;
				}else{
					result = 1;
				}
				empTransDao.commitTransaction(conn);
			}else{
				result = msg;
			}
			
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "APP首页发布取消更新出现异常！");
			result = -1;
		}finally{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	/**
	 * APP首页发布JSON数据处理
	 * @Title: publish
	 * @Description: TODO
	 * @param plants
	 * @param lgcorpcode
	 * @param sid
	 * @param validity
	 * @return int
	 */
	public int publish(List<LfAppSitPlant> plants,String lgcorpcode,String sid,String validity){
		int result = -1;
		if(StringUtils.isBlank(sid)||"0".equals(sid)){
			return -1;
		}
		try
		{
			List<AppMainPage> mainPageTopList = new ArrayList<AppMainPage>();
			List<AppMainPage> mainPageTitleList = new ArrayList<AppMainPage>();
				for(LfAppSitPlant plant:plants){
					JSONObject item = new JSONObject(plant.getFeildvalues());
					AppMainPage page = new AppMainPage();
					page.setContent(item.getString("content"));
					page.setPic(item.getString("thumb"));
					page.setTitle(item.getString("title"));
					page.setUrl(item.getString("url"));
					if("head".equals(plant.getPlanttype())){
						mainPageTopList.add(page);
					}
					if("list".equals(plant.getPlanttype())){
						mainPageTitleList.add(page);
					}
				}
				if(plants.size()>0){
					long validate = 0L;
					try
					{
						if(StringUtils.isNotBlank(validity)){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							validate = sdf.parse(validity).getTime();
						}
					}
					catch (Exception e)
					{
						EmpExecutionContext.error(e, "日期转换异常！");
					}
					if(validate<=0){
						//若validate无效 则默认一年
						Calendar calendar = Calendar.getInstance();
						calendar.add(Calendar.YEAR, 1);
						validate = calendar.getTimeInMillis();
					}
					//首页发布对象
					AppMessage message = new AppMessage();
					message.setMainPageTitleList(mainPageTitleList);
					message.setMainPageTopList(mainPageTopList);
					message.setSid(Long.valueOf(sid));
					message.setValidity(validate);
					int msg = wgbizBiz.SetAppMainPage(message);
					if(msg == 1){
						if(updateStatus(lgcorpcode, sid,validate)){
							result = 1;
						}else{
							result = -1;
						}
					}else{
						result = msg;
					}
				}
		}
		catch (JSONException e)
		{
			EmpExecutionContext.error(e, "APP首页发布JSON数据处理异常！");
		}
		return result;
	}
	
	/**
	 * @description  先保存再发布
	 * @param status
	 * @param lgcorpcode
	 * @param lguserid
	 * @param json
	 * @param reqSid
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-6-23 下午01:45:58
	 */
	public int saveInPublish(String lgcorpcode,long lguserid,JSONObject json,String reqSid,String validity,String infoname){
		Connection conn = empTransDao.getConnection();
		boolean isUpdate = true;
		List<LfAppSitPlant> plants = new ArrayList<LfAppSitPlant>();
		Long sid = 0L;
		try
		{
			JSONArray heads = (JSONArray)json.get("heads");
			JSONArray lists = (JSONArray)json.get("lists");
			if(StringUtils.isBlank(reqSid)||!StringUtils.isNumeric(reqSid)){
				isUpdate = false;
				sid = new DataAccessDriver().getEmpDAO().getIdByPro(532, 1);
			}else{
				sid = Long.valueOf(reqSid);
			}
			empTransDao.beginTransaction(conn);
			LinkedHashMap<String, String> cond = null;
			//修改 删除原有的plant数据
			if(isUpdate){
				cond = new LinkedHashMap<String, String>();
				cond.put("sid",reqSid);
				empTransDao.delete(conn, LfAppSitPlant.class, cond);
			}
			String infoName = null;
			for(int i=0;i<heads.length();i++){
				JSONObject item = heads.getJSONObject(i);
				if(i==0){
					infoName = item.getString("title");
				}
				LfAppSitPlant plant = new LfAppSitPlant(lgcorpcode,sid);
				plant.setCreatetime(new Timestamp(System.currentTimeMillis()));
				plant.setFeildvalues(item.toString());
				plant.setPlanttype("head");
				plants.add(plant);
			}
			for(int i=0;i<lists.length();i++){
				JSONObject item = lists.getJSONObject(i);
				LfAppSitPlant plant = new LfAppSitPlant(lgcorpcode,sid);
				plant.setCreatetime(new Timestamp(System.currentTimeMillis()));
				plant.setFeildvalues(item.toString());
				plant.setPlanttype("list");
				plants.add(plant);
			}
			empTransDao.save(conn, plants, LfAppSitPlant.class);
			if(isUpdate){
				LinkedHashMap<String, String> objMap = new LinkedHashMap<String, String>();
				//默认状态  待发布 
				objMap.put("status", "1");
				if(StringUtils.isNotBlank(infoname)){
					objMap.put("name", StringEscapeUtils.escapeSql(infoname.trim()));
				}
				empTransDao.update(conn,LfAppSitInfo.class,objMap,cond);
			}else{
				LfAppSitInfo info = new LfAppSitInfo();
				if(StringUtils.isNotBlank(infoname)){
					info.setName(infoname.trim());
				}else{
					info.setName(infoName);
				}
				info.setStatus(1);
				info.setCorpcode(lgcorpcode);
				info.setCreatetime(new Timestamp(System.currentTimeMillis()));
				info.setUserid(lguserid);
				info.setSid(sid);
				empTransDao.save(conn, info);
			}
			empTransDao.commitTransaction(conn);
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "保存或暂存APP首页出现异常！");
			return -99;
			
		}finally{
			empTransDao.closeConnection(conn);
		}
			
		//保存后 发布
		return publish(plants, lgcorpcode, String.valueOf(sid),validity);
	}
}

