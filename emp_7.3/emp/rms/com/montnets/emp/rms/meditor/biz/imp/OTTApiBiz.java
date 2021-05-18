package com.montnets.emp.rms.meditor.biz.imp;


import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.comm.HttpSendTool;
import com.montnets.emp.rms.rmsapi.comm.HttpSendUtils;
import com.montnets.emp.rms.rmsapi.constant.OTTHttpConstant;
import com.montnets.emp.rms.rmsapi.model.SendTempParams;
import com.montnets.emp.rms.rmsapi.util.JSONToMapUtil;
import com.montnets.emp.rms.rmsapi.util.ValidateUtil;
import com.montnets.emp.util.MD5;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OTTApiBiz implements com.montnets.emp.rms.meditor.biz.IOTTApiBiz {

	/**
	 * 模板发送
	 * @param tempParams
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> sendTemplate(SendTempParams tempParams)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String userid = tempParams.getUserid();
		String pwd = tempParams.getPwd();
		if (ValidateUtil.accountIsNull(userid, pwd)) {
			map.put("result", "-1");
			EmpExecutionContext.info("用户名或密码为空");
			return map;
		}
		Date date = new Date();
		tempParams.setTimestamp(new SimpleDateFormat("MMddHHmmss").format(date));
		tempParams.setPwd(MD5.getMD5Str(userid.toUpperCase() + OTTHttpConstant.FIXATION_SEQUENCE + pwd+ tempParams.getTimestamp()));
		// 兼容相同内容发送时，参数为空的情况
		tempParams.setContent(((tempParams.getContent() != null && !"".equals(tempParams.getContent())) ? URLEncoder.encode(tempParams.getContent(),OTTHttpConstant.UTF8_ENCODE) : ""));
		String url = OTTHttpConstant.RMS_SEND_TEMPLATE_URL;
		String result = HttpSendUtils.sendPost(OTTHttpConstant.getRMS_SEND_IP(),OTTHttpConstant.getRMS_SEND_PORT(), url, tempParams,OTTHttpConstant.DEFAULT_RESEND);
		map = JSONToMapUtil.jsonStrToMapStr(result);
		return map;
	}
}
