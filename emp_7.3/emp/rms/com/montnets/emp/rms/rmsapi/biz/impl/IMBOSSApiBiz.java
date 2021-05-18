package com.montnets.emp.rms.rmsapi.biz.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.biz.MBOSSApiBiz;
import com.montnets.emp.rms.rmsapi.comm.HttpSendTool;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;
import com.montnets.emp.rms.rmsapi.dao.impl.IQueryHisRecordDAO;
import com.montnets.emp.rms.rmsapi.model.QueryHisRecordParams;
import com.montnets.emp.rms.rmsapi.util.JSONToMapUtil;
import com.montnets.emp.util.MD5;

public class IMBOSSApiBiz implements MBOSSApiBiz{

	private HttpSendTool hst;
	
	public IMBOSSApiBiz(){
		hst=new HttpSendTool();
	}
	
	/**
	 * 查询富信历史记录
	 * @author chenlinyan<15773241398@163.com>
	 * @param params 查询历史记录参数
	 * @return map{rstate:'',resultMsg:'',statuscode:'',scont:List<map<String,String>>}
	 * @throws Exception
	 */
	public Map<String, Object> queryHisRecord(QueryHisRecordParams params) throws Exception {
		Map<String,Object> map=new HashMap<String, Object>();
		String userid=params.getUserid();
		if(null==userid||"".equals(userid)){
			map.put("rstate", "-1");
			map.put("resultMsg", "sp账号为空");
			EmpExecutionContext.info("sp账号为空");
			return map;
		}
		params.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()));
		String pwd=new IQueryHisRecordDAO().findPwdByUserId(userid);
		params.setToken(MD5.getMD5Str(userid+","+pwd+","+params.getTimestamp()));
		String result=hst.sendPostToMBOSS(RMSHttpConstant.RMS_MOSS_QUERY_IP, RMSHttpConstant.RMS_MOSS_QUERY_PORT, RMSHttpConstant.MBOSS_QUERY_HISRECORD_URL, params, RMSHttpConstant.DEFAULT_RESEND);
		map=JSONToMapUtil.jsonToMap(result);
		return map;
	}

}
