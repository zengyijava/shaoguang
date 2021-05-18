package com.montnets.emp.rms.wbs.server;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.montnets.emp.rms.wbs.model.RmsRequest;
import com.montnets.emp.rms.wbs.model.RmsResponse;

/**
 * 富信统计信息同步接口
 * @ClassName IRmsService
 * @Description TODO
 * @author zhouxiangxian 203492752@qq.com
 * @date 2018年1月9日
 */

@WebService(targetNamespace = "http://www.montnets.com")
public interface IRmsService {
	
	@WebMethod(action = "http://www.montnets.com/UnionSubInterFace")
    public @WebResult(name = "UnionSubInterFaceResult")
    RmsResponse UnionSubInterFace(@WebParam(name = "request") RmsRequest rmsRequest);
}
