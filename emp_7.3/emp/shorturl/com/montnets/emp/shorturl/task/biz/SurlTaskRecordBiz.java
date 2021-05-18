package com.montnets.emp.shorturl.task.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.shorturl.surlmanage.entity.LfUrlTask;
import com.montnets.emp.shorturl.surlmanage.vo.LfUrlTaskVo;
import com.montnets.emp.shorturl.task.dao.SurlTaskRecordDao;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

public class SurlTaskRecordBiz extends SuperBiz{

	SurlTaskRecordDao taskDao = new SurlTaskRecordDao();
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * 查询
	 * @param conditionMap
	 * @param pageInfo
	 */
	public List<LfUrlTaskVo> find (LinkedHashMap<String, String> conditionMap, PageInfo pageInfo){
		List<LfUrlTaskVo> mtList = null;
		try {
			if (pageInfo != null) {
				mtList = taskDao.geturlTaskVos(conditionMap,pageInfo);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询发送任务信息集合biz异常");
		}
		return mtList;
		
	}
	
	
	
	/**
	 * 获取用户名下拉框
	 * @param userId
	 * @return
	 */
	public Map<String, String> getUsers(String userId){
		
		return taskDao.getUser(userId);
	}



	public boolean reSend(String id,Integer urlstatus, String type, LfUrlTask task) {
		Connection conn = empTransDao.getConnection();
		try {
			empTransDao.beginTransaction(conn);
			LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
			objectMap.put("status", urlstatus-3);
			Timestamp time = new Timestamp(new Date().getTime());
			objectMap.put("updatetm", time);
			//判断是否需要修改定时状态
			if ("1".equals(type)) {
				JSONObject jsonObject = JSON.parseObject(task.getRemark());
				jsonObject.put("timerStatus", "0");
				jsonObject.put("timerTime", sdf.format(new Date()));
				objectMap.put("remark", jsonObject.toJSONString());
			}
			boolean result = empTransDao.update(conn, LfUrlTask.class,
					objectMap, id, null);
			if (!result) {
				EmpExecutionContext.error("短链重发，操作失败");
				empTransDao.rollBackTransaction(conn);
				return false;
			}
			empTransDao.commitTransaction(conn);
			return true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"短链接biz重发一次");
			empTransDao.rollBackTransaction(conn);
		}finally{
			empTransDao.closeConnection(conn);
		}
		return false;
	}

    public Map<String, Object> getFailDetails(String id) {

        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(id)) {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("id", id);
            try {
                List<LfUrlTask> list = taskDao.findFailDetails(conditionMap);
                if(list != null && list.size() > 0) {
                    LfUrlTask task = list.get(0);
                    if(task != null) {
                        String message = task.getRemark1();
                        map.put("result", message);
                        map.put("isOk", 200);
                        return map;
                    }
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "获取失败详细信息出现错误,id=" + id + e.getMessage());
                map.put("result", "");
                map.put("isOk", 500);
                return map;
            }
        }
        map.put("result", "");
        map.put("isOk", 404);
        return map;
    }

}
