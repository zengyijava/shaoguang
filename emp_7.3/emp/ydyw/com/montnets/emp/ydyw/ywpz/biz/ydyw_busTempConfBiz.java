package com.montnets.emp.ydyw.ywpz.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.GenericEmpDAO;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.ydyw.ywpz.dao.ydyw_busTempConfDAO;
import com.montnets.emp.ydyw.ywpz.entity.LfBusTailTmp;
import com.montnets.emp.ydyw.ywpz.vo.LfBusTailTmpVo;
import org.apache.commons.lang.StringUtils;

public class ydyw_busTempConfBiz extends SuperBiz{

	//初始化DAO对象
    ydyw_busTempConfDAO ydywDao = new ydyw_busTempConfDAO();
	
	public List<LfBusTailTmpVo> findByParams(String busName, String busCode,
			String depId, String userId, String startTime, String endTime, 
			PageInfo pageInfo, String corpCode, int type) throws Exception {
		//return返回结果集
		if(type == 1) {
			return ydywDao.findByParams2(busName, busCode, depId, userId, startTime, endTime, pageInfo, corpCode);
		} else {
			return ydywDao.findByParams(busName, busCode, depId, userId, startTime, endTime, pageInfo, corpCode);
		}
		
	}
	
	public boolean saveTails(String busId, String tmIds, List<LfBusTailTmp> saveList) throws Exception {
		boolean flag = false;
		int saveCount = -1;
		Connection conn = null;
		
		try {
			conn = empTransDao.getConnection();
			//开启事务
			empTransDao.beginTransaction(conn);
			
			LinkedHashMap<String, String> conditionMap  = new LinkedHashMap<String, String>();
			conditionMap.put("busId", busId);
			conditionMap.put("associateType", "1");
			
			//根据业务ID批量删除原来的模板配置
			int delCount = empTransDao.delete(conn, LfBusTailTmp.class, conditionMap);
			
			if (delCount > 0) {
				//批量插入修改后的模板配置
				saveCount = empTransDao.save(conn, saveList, LfBusTailTmp.class);				
			} else {
				throw new Exception();
			}
			
			//提交事务
			empTransDao.commitTransaction(conn);
		} catch (Exception e) {
			flag = false;
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			saveCount = -1;
			EmpExecutionContext.error(e, "修改模板配置失败，事务回滚！");
		} finally {
			if (saveCount != -1) {
				flag = true;
			}
			
			//关闭事务连接
			empTransDao.closeConnection(conn);
		}
		
		return flag;
	}

	public List<LfTemplate> getLfTemplate(LinkedHashMap<String,String> conditionMap, List<LfSysuser> ids, LfSysuser sysuser){
        List<LfTemplate> lfTemplate = new ArrayList<LfTemplate>();
	    StringBuilder sql = new StringBuilder("select * from LF_TEMPLATE where 1 = 1 ");
        String tmpType = conditionMap.get("tmpType");
        String tmState = conditionMap.get("tmState");
        String dsFlag = conditionMap.get("dsflag&in");
        StringBuilder userId = new StringBuilder(" and user_id in (");
        if(!StringUtils.isEmpty(tmpType)) {
            sql.append(" AND TMP_TYPE = " + tmpType);
        }
        if(!StringUtils.isEmpty(tmState)) {
            sql.append(" AND tm_state = " + tmState);
        }
        if(!StringUtils.isEmpty(dsFlag)) {
            sql.append(" AND dsFlag in( " + dsFlag).append(")");
        }
        for (int i = 0; i < ids.size(); i++) {
            userId.append(ids.get(i).getUserId() + ",");
            if(i > 0 && i % 500 == 0){
                userId.deleteCharAt(userId.lastIndexOf(",")).append(") or user_id in (");
            }
        }
        if(!StringUtils.isEmpty(userId.toString())) {
            sql.append(userId.append(sysuser.getUserId()).append(")"));
        }
        sql.append(" order by TM_ID DESC");
        try {
            lfTemplate = new GenericEmpDAO().findEntityListBySQL(LfTemplate.class, sql.toString(), StaticValue.EMP_POOLNAME);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据用户查询业务模板异常");
        }
        return lfTemplate;
    }

}
