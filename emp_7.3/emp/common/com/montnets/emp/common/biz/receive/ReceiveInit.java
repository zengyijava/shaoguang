package com.montnets.emp.common.biz.receive;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfBusProcess;

/**
 * 
 * @author Administrator
 *
 */
public class ReceiveInit extends SuperBiz
{
	
	/**
	 * 注册业务类
	 * @param regType
	 * @param codes
	 * @param codeType
	 * @param className
	 * @return
	 */
	public boolean RegisteBus(Integer regType, String codes, Integer codeType, String className) {
		//regType为空
		if(regType == null){
			EmpExecutionContext.error("注册接收平台出错，regType为空！");
			return false;
		}
		//className为空
		if(className == null || "".equals(className.trim())){
			EmpExecutionContext.error("注册接收平台出错，className为空！");
			return false;
		}
		//busCode或menuCode为空
		if(codes == null && codeType == null){
			EmpExecutionContext.error("busCode或menuCode为空！");
			return false;
		}
		//判断是否已存在
		if(this.isBusProcExists(regType,codes, codeType)){
			return true;
		}
		//结果
		boolean result = false;
		try{
			//注册类对象
			LfBusProcess busPorc = new LfBusProcess();
			busPorc.setRegType(regType);
			busPorc.setCodes(codes);
			busPorc.setCodeType(codeType);
			busPorc.setClassName(className);
			//保存记录
			result = empDao.save(busPorc);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "注册业务类异常。");
		}
		//返回结果
		return result;
	}
	
	/**
	 * 验证业务类是否已存在
	 * @param codes
	 * @param codeType
	 * @param className
	 * @return
	 */
	private boolean isBusProcExists(Integer regType,String codes, Integer codeType) {
		//结果
		boolean result = false;
		try{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("codes", codes);
			conditionMap.put("regType", regType.toString());
			conditionMap.put("codeType", codeType.toString());
			//conditionMap.put("className", className);
			//按条件查询
			List<LfBusProcess> busProcsList = empDao.findListByCondition(LfBusProcess.class, conditionMap, null);
			//有记录
			if(busProcsList != null && busProcsList.size() > 0){
				result = true;
			}
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "验证业务类是否已存在异常。");
		}
		//返回结果
		return result;
	}
}
