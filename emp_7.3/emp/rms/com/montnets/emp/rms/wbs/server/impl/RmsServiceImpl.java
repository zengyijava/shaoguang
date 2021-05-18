package com.montnets.emp.rms.wbs.server.impl;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;
import com.montnets.emp.rms.wbs.biz.ISvcContBiz;
import com.montnets.emp.rms.wbs.biz.impl.SvcContBizImpl;
import com.montnets.emp.rms.wbs.model.RmsRequest;
import com.montnets.emp.rms.wbs.model.RmsResponse;
import com.montnets.emp.rms.wbs.model.SvcCont;
import com.montnets.emp.rms.wbs.server.IRmsService;
import com.montnets.emp.rms.wbs.thread.TransferDataThread;
import com.montnets.emp.rms.wbs.util.IEncodeAndDecode;
import com.montnets.emp.rms.wbs.util.ParseXML;
import com.montnets.emp.rms.wbs.util.StringUtils;
import com.montnets.emp.rms.wbs.util.Validate;
import com.montnets.emp.rms.wbs.util.impl.Base64MD5Coding;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @ClassName RmsServiceImpl
 * @Description TODO webservices富信同步接口实现类
 * @author zhouxiangxian 203492752@qq.com
 * @date 2018年1月11日
 */
@WebService(endpointInterface = "com.montnets.emp.rms.wbs.server.IRmsService", targetNamespace = "http://www.montnets.com")
public class RmsServiceImpl implements IRmsService {
	/**
	 * 获取解析对象
	 */
	private ParseXML parseXml = ParseXML.getInstance();

	/**
	 * 富信同步数据接口实现类
	 */
	public RmsResponse UnionSubInterFace(RmsRequest rmsRequest) {
		// 返回内容
		String svcCXML = "";
		// 请求的内容
		String svcCont = "";
		// 默认响应标记
		boolean defaultResponse = true;
		try {
			// 1代表加密，需要解密
			if (rmsRequest.getEncryFlag() == 1) {
				// 准备解密
				IEncodeAndDecode bmc = new Base64MD5Coding();
				svcCont = bmc.decode(rmsRequest.getSvcCont(), RMSHttpConstant.SYSN_DATA_ENCRY_KEY);
				if (svcCont != null) {
					// 设置解密之后的内容
					rmsRequest.setSvcCont(svcCont);
				}
			} else {
				svcCont = rmsRequest.getSvcCont();
			}
			// 获取内容,去掉空格
			svcCont = rmsRequest.getSvcCont().trim();
			// 对报文进行解析
			List<SvcCont> list = parseXml.parseXmlToModel(svcCont);
			if (list.size() <= 0) {
				// 说明解析出错直接返回
				defaultResponse = false;
				return getResponse(svcCont, rmsRequest, false);
			}
			// 获取sp账号和企业编码的关系
			ISvcContBiz svcContBiz = new SvcContBizImpl();
			// 获取sp账号和企业编码的关系
			Map<String, String> map = svcContBiz.findSpAndCorpCode();
			// 遍历List<SvcCont>,进行数据的有效性验证，如果数据合法，为其设置好企业编码;否则不设置企业编码
			for (SvcCont cont : list) {
				// 取出内容
				// 判断传入数据是否为空spisuncm、userid、degree、icount、rsucc、rfail;imyd字段是否合法
				if (Validate.isNull(cont.getSpisuncm()) || Validate.isNull(cont.getUserid())
						|| !Validate.isNumber(cont.getDegree()) || !Validate.isNumber(cont.getIcount())
						|| !Validate.isNumber(cont.getRsucc()) || !Validate.isNumber(cont.getRfail())
						|| !Validate.isYMD(cont.getIymd())) {
					// 设置数据错误标记
					cont.setResultCode(1);
					continue;
				}
				// 数据合法，为其设置好企业编码
				// 如果当前没找到设置默认的，并设置sp账号匹配企业编码时不区分大小写
				// 根据sp账号取出企业编码的内容，并将内容设置到SvcCont实体中
				cont.setCorpCode(map.get(cont.getUserid().toUpperCase()) == null ? "未知企业" : map.get(cont.getUserid().toUpperCase()));
			}
			// 定位错误数据的位置
			List<Integer> errorLocation = new ArrayList<Integer>();
			// 记录失败条数
			int index = 0;
			// 时间戳,开始入库时间
			Long startTime = StringUtils.getLongTime();
			// 入库临时表执行批量插入
			boolean isUpdate = svcContBiz.insertBatch(list);
			// 定义一个数据转移线程启动的标记
			boolean startFlag = false;
			for (SvcCont c : list) {
				// 判断是否结束标志
				if (c.getIymd() != null && "0".equals(c.getIymd().trim())) {
					// 传入结束标记；如果该次数据中有非法数据，不允许转移，否则允许转移
					if (c.getResultCode() == 0) {
						startFlag = true;
					}
				} else {
					// 同步未结束
					if (c.getResultCode() == 0) {
						// 批量更新失败的话，执行单条更新，找出入库失败的位置
						if (!isUpdate && !svcContBiz.insertSimple(c)) {
							errorLocation.add(list.indexOf(c));
							// 失败记录数增加
							index++;
						}
					} else {
						// 定位出错的位置
						errorLocation.add(list.indexOf(c));
						index++;
					}
				}
			}
			// 数据的转移处理
			// 启动数据转移线程
			if (startFlag) {
				new Thread(new TransferDataThread(svcContBiz)).start();
			}
			// 根据插入数据结果，生成相应报文
			svcCXML = parseXml.getSvcContXml(svcCont, errorLocation);
			// 入库结束时间
			Long endTime = StringUtils.getLongTime();
			EmpExecutionContext.info("富信同步数据接口调用,临时数据入LF_RMS_SYN_TEMP表程序耗时:" + (endTime - startTime) + "ms，本次处理数据条数：" + list.size()
					+ "，失败条数：" + index);
			return getResponse(svcCXML, rmsRequest, true);
		} catch (Exception e) {
			svcCXML = parseXml.getAllErrorXml(svcCont);
			EmpExecutionContext.error(e, "富信同步数据接口调用，接口入口处捕获异常!返回报文:" + svcCXML);
		}
		return getResponse(svcCXML, rmsRequest, defaultResponse);
	}

	/**
	 * 返回RmsResponse对象
	 */
	public RmsResponse getResponse(String xml, RmsRequest rmsRequest, boolean flag) {
		RmsResponse response = new RmsResponse();
		String result = "";// 返回内容
		// 判断是否对返回的报文作是否加密处理
		if (rmsRequest.getEncryFlag() == 1) {
			response.setEncryFlag(1);
			IEncodeAndDecode bmc = new Base64MD5Coding();
			result = bmc.encoding(xml, RMSHttpConstant.SYSN_DATA_ENCRY_KEY);
		} else {
			response.setEncryFlag(0);
			result = xml;
		}
		EmpExecutionContext.info("响应报文：" + xml);
		// 设置返回的内容
		response.setSvcCont(result);
		response.setBizCode(rmsRequest.getBizCode());
		response.setActionCode(rmsRequest.getActionCode());
		if (flag) {
			response.setResultMsg("数据接收成功");
		} else {
			response.setResultMsg("报文格式错误，请按约定格式传送!");
		}
		return response;
	}

}