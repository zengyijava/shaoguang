package com.montnets.emp.rms.rmsapi.biz.impl;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.biz.RMSApiBiz;
import com.montnets.emp.rms.rmsapi.comm.HttpSendUtils;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;
import com.montnets.emp.rms.rmsapi.model.PackageHeader;
import com.montnets.emp.rms.rmsapi.model.SendTempParams;
import com.montnets.emp.rms.rmsapi.model.TempParams;
import com.montnets.emp.rms.rmsapi.model.UploadTempParams;
import com.montnets.emp.rms.rmsapi.util.EncodeUtil;
import com.montnets.emp.rms.rmsapi.util.JSONToMapUtil;
import com.montnets.emp.rms.rmsapi.util.ValidateUtil;
import com.montnets.emp.util.MD5;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名称:RMSApiBiz.java
 * 文件描述:功能描述
 * 内容摘要:富信平台接口实现类
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-1-10    陈林艳     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 *
 * @author:   chenlinyan<15112605627@163.com>
 * @version:  0.1  
 * @Date:     2018-1-10 下午15:10:08
 */
public class IRMSApiBiz implements RMSApiBiz {
	
	/**
	 * 上传模板
     *
	 * @param tempList
	 * @return
	 * @throws Exception
     * @author chenly
	 */
    @Override
	public Map<String, String> subTemplate(String userid,String pwd,List<TempParams> tempList)
			throws Exception {
		Map<String,String> map=new HashMap<String, String>();
		if(ValidateUtil.accountIsNull(userid, pwd)){
			map.put("result", "-1");
			//modify by tanglili 20201202
			//增加错误码描述
			map.put("desc","SP账号或密码为空！");
			EmpExecutionContext.info("SP账号或密码为空");
			return map;
		}
		String url=RMSHttpConstant.RMS_UPLOAD_TEMPLATE_URL_V2;
		UploadTempParams param=new UploadTempParams();
		param.setUserid(userid);
		param.setPwd(MD5.getMD5Str(userid.toUpperCase()+RMSHttpConstant.FIXATION_SEQUENCE+pwd+param.getTimestamp()));
		param.setContent(EncodeUtil.base64Encode(tempList));
		param.setSign(MD5.getMD5Str(param.getContent()+param.getTimestamp()+RMSHttpConstant.FIXATION_SEQUENCE));
        String result = (HttpSendUtils.sendPostSubTemplate(RMSHttpConstant.RMS_IP, RMSHttpConstant.RMS_PORT, url, param, RMSHttpConstant.DEFAULT_RESEND));
		map=JSONToMapUtil.jsonStrToMapStr(result);
		return map;
	}

	@Override
	public Map<String, String> subTemplate(String userid, String pwd, List<TempParams> tempList, String smcontent, String smparamreg,String title,String templVer) throws Exception {
		Map<String,String> map=new HashMap<String, String>();
		if(ValidateUtil.accountIsNull(userid, pwd)){
			map.put("result", "-1");
			//modify by tanglili 20201202
			//增加错误码描述
			map.put("desc","SP账号或密码为空！");
			EmpExecutionContext.info("SP账号或密码为空");
			return map;
		}
		String url=RMSHttpConstant.RMS_UPLOAD_TEMPLATE_URL;
		UploadTempParams param=new UploadTempParams();
		param.setUserid(userid);
		param.setPwd(MD5.getMD5Str(userid.toUpperCase()+RMSHttpConstant.FIXATION_SEQUENCE+pwd+param.getTimestamp()));
		param.setContent(EncodeUtil.base64Encode(tempList));
		param.setSign(MD5.getMD5Str(param.getContent()+param.getTimestamp()+RMSHttpConstant.FIXATION_SEQUENCE));
		param.setSmcontent(smcontent);
		param.setSmparamreg(smparamreg);
		param.setTitle(title);
		param.setTmplver(templVer);
		param.setOrigin(StaticValue.getCORPTYPE() == 1?"1":"2");//StaticValue.CORPTYPE == 1 为托管版；  给RSC的模板来源标识 1-托管版EMP，2-标准版EMP
        String result = (HttpSendUtils.sendPostSubTemplate(RMSHttpConstant.RMS_IP, RMSHttpConstant.RMS_PORT, url, param, RMSHttpConstant.DEFAULT_RESEND));
		map=JSONToMapUtil.jsonStrToMapStr(result);
		return map;
	}

	/**
	 * 删除模板
     *
	 * @param tempIds
	 * @return
	 * @throws Exception
     * @author chenly
	 */
    @Override
	public Map<String, String> deleteTemplates(String userid,String pwd,String tempIds)
			throws Exception {
		Map<String,String> map=new HashMap<String, String>();
		if(ValidateUtil.accountIsNull(userid, pwd)){
			map.put("result", "-1");
			EmpExecutionContext.info("用户名或密码为空");
			return map;
		}
		PackageHeader param=new PackageHeader();
		param.setUserid(userid);
		param.setPwd(MD5.getMD5Str(userid.toUpperCase()+RMSHttpConstant.FIXATION_SEQUENCE+pwd+param.getTimestamp()));
		param.setTmplid(tempIds);
		String url=RMSHttpConstant.RMS_DELETE_TEMPLATE_URL;
        String result = HttpSendUtils.sendPost(RMSHttpConstant.RMS_IP, RMSHttpConstant.RMS_PORT, url, param, RMSHttpConstant.DEFAULT_RESEND);
		 map=JSONToMapUtil.jsonStrToMapStr(result);
		return map;
	}

	/**
	 * 查询模板审批状态
     *
	 * @param tempIds
	 * @return
	 * @throws Exception
     * @author chenly
	 */
    @Override
	public Map<String, Object> queryTemplateStatus(String userid,String pwd,String tempIds)
			throws Exception {
		Map<String,Object> map=new HashMap<String, Object>();
		if(ValidateUtil.accountIsNull(userid, pwd)){
			map.put("result", "-1");
			EmpExecutionContext.info("用户名或密码为空");
			return map;
		}
		PackageHeader param=new PackageHeader();
		param.setUserid(userid);
		param.setPwd(MD5.getMD5Str(userid.toUpperCase()+RMSHttpConstant.FIXATION_SEQUENCE+pwd+param.getTimestamp()));
		param.setTmplid(tempIds);
		String url=RMSHttpConstant.RMS_QUERY_TEMPLATE_URL_V2;
        String result = HttpSendUtils.sendPost(RMSHttpConstant.RMS_IP, RMSHttpConstant.RMS_PORT, url, param, RMSHttpConstant.DEFAULT_RESEND);
		map=JSONToMapUtil.jsonStrToMapObj(result);
		return map;
	}

	/**
	 * 模板发送
     *
	 * @param tempParams
	 * @return
	 * @throws Exception
     * @author chenly
	 */
    @Override
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
		tempParams.setTimestamp(new SimpleDateFormat("MMddHHmmss").format(new Date()));
		tempParams.setPwd(MD5.getMD5Str(userid.toUpperCase() + RMSHttpConstant.FIXATION_SEQUENCE + pwd+ tempParams.getTimestamp()));
		// 兼容相同内容发送时，参数为空的情况
		tempParams.setContent(((tempParams.getContent() != null && !"".equals(tempParams.getContent())) ? URLEncoder.encode(tempParams.getContent(),RMSHttpConstant.UTF8_ENCODE) : ""));
		String url = RMSHttpConstant.RMS_SEND_TEMPLATE_URL;
        String result = HttpSendUtils.sendPost(RMSHttpConstant.RMS_SEND_IP, RMSHttpConstant.RMS_SEND_PORT, url, tempParams, RMSHttpConstant.DEFAULT_RESEND);
		map = JSONToMapUtil.jsonStrToMapStr(result);
		return map;
	}

	/**
	 * 下载模板
     *
	 * @param userid 用户账号
	 * @param pwd 密码
	 * @param tempIds 模板id
	 * @return  map{result:'',timestamp:'',sign:'',content:List<TempParams>}
	 * @throws Exception
     * @author chenlinyan<1               5               7               7               3               2               4               1               3               9               8               @               1               6               3               .               com>
	 */
    @Override
	public Map<String, Object> getTemplate(String userid,String pwd,String tempIds) throws Exception {
		Map<String,Object> map=new HashMap<String, Object>();
		if(ValidateUtil.accountIsNull(userid, pwd)){
			map.put("result", "-1");
			EmpExecutionContext.info("用户名或密码为空");
			return map;
		}
		PackageHeader param=new PackageHeader();
		param.setUserid(userid);
		param.setPwd(MD5.getMD5Str(userid.toUpperCase()+RMSHttpConstant.FIXATION_SEQUENCE+pwd+param.getTimestamp()));
		param.setTmplid(tempIds);
		String url=RMSHttpConstant.RMS_GET_TEMPLATE_URL;
        String result = HttpSendUtils.sendPost(RMSHttpConstant.RMS_IP, RMSHttpConstant.RMS_PORT, url, param, RMSHttpConstant.DEFAULT_RESEND);
		map=JSONToMapUtil.getTempJsonToMap(result);
		return map;
	}

	/**
	 * 同步RCOS 平台创建的公共模板
     *
	 * @param userid
	 * @param pwd
	 * @param tempIds
	 * @return
	 * @throws Exception
	 */
    @Override
	public Map<String, Object> getTemplateFromRCOS(String userid,String pwd,String tempIds) throws Exception {
		Map<String,Object> map=new HashMap<String, Object>();
		if(ValidateUtil.accountIsNull(userid, pwd)){
			map.put("result", "-1");
			EmpExecutionContext.info("用户名或密码为空");
			return map;
		}
		PackageHeader param=new PackageHeader();
		param.setUserid(userid);
		param.setPwd(MD5.getMD5Str(userid.toUpperCase()+RMSHttpConstant.FIXATION_SEQUENCE+pwd+param.getTimestamp()));
		param.setTmplid(tempIds);
		String url=RMSHttpConstant.RCOS_GET_TEMPLATE_URL;
        String result = HttpSendUtils.sendPost(RMSHttpConstant.RCOS_IP, RMSHttpConstant.RCOS_PORT, url, param, RMSHttpConstant.DEFAULT_RESEND);
		map=JSONToMapUtil.getCommonTempJsonToMap(result);
		return map;
	}


	@Override
	public Map<String, Object> syncEcTemplateFromRCOS(String userid, String pwd, String tempIds)throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (ValidateUtil.accountIsNull(userid, pwd)) {
			map.put("result", "-1");
			EmpExecutionContext.info("用户名或密码为空");
			return map;
		}
		PackageHeader param = new PackageHeader();
		param.setUserid(userid);
		param.setPwd(MD5.getMD5Str(userid.toUpperCase() + RMSHttpConstant.FIXATION_SEQUENCE + pwd + param.getTimestamp()));
		param.setTmplid(tempIds);
		String url = RMSHttpConstant.RCOS_GET_EC_EMPLATE_URL;
        String result = HttpSendUtils.sendPost(RMSHttpConstant.RMS_IP, RMSHttpConstant.RMS_PORT, url, param, RMSHttpConstant.DEFAULT_RESEND);
		map = JSONToMapUtil.getEcTempJsonToMap(result);
		return map;
	}

	/**
	 * 查询RCOS创建的公共模板状态
     *
	 * @param tempIds 下载模板的模板ID,多个模板ID以英文逗号分隔
	 * @return
     * @author xuty
	 */

	@Override
	public Map<String, Object> queryTemplateStatusFromRCOS(String userid, String pwd, String tempIds) throws Exception {
		Map<String,Object> map=new HashMap<String, Object>();
		if(ValidateUtil.accountIsNull(userid, pwd)){
			map.put("result", "-1");
			EmpExecutionContext.info("用户名或密码为空");
			return map;
		}
		PackageHeader param=new PackageHeader();
		param.setUserid(userid);
		param.setPwd(MD5.getMD5Str(userid.toUpperCase()+RMSHttpConstant.FIXATION_SEQUENCE+pwd+param.getTimestamp()));
		param.setTmplid(tempIds);
		String url=RMSHttpConstant.RCOS_SYN_TEMPLATE_URL;
        String result = HttpSendUtils.sendPost(RMSHttpConstant.RCOS_IP, RMSHttpConstant.RCOS_PORT, url, param, RMSHttpConstant.DEFAULT_RESEND);
		map=JSONToMapUtil.jsonStrToMapObj(result);
		return map;
	}
}
