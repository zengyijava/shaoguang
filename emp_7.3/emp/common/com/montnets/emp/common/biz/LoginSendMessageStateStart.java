package com.montnets.emp.common.biz;

import java.util.LinkedHashMap;
import java.util.List;
import com.montnets.emp.common.biz.receive.IReportStart;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.securectrl.LfDynPhoneWord;
import com.montnets.emp.entity.system.LfReport;
public class LoginSendMessageStateStart  implements IReportStart{

	/**
	 * 发送手机动态口令，获取状态报告
	 */
	public boolean start(LfReport report){
		try{
			BaseBiz baseBiz = new BaseBiz();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			conditionMap.put("ptMsgId", report.getMtmsgid());  //根据网关返回的msgid
			//查询动态口令信息
			List<LfDynPhoneWord> dPhoneWord = baseBiz.getByCondition(LfDynPhoneWord.class, conditionMap, null);
			if(dPhoneWord != null && dPhoneWord.size()>0){
				LfDynPhoneWord phoneword = dPhoneWord.get(0);
                if(phoneword != null){
                    phoneword.setErrorCode("0000000");//网关返回状态报告则发送成功
					//设置成获取状态报告成功
                    objectMap.put("errorCode", "0000000");
                    return baseBiz.update(LfDynPhoneWord.class, objectMap, conditionMap);
				}
			}else{
				return true;
			}
		}catch (Exception e) {
			//异常返回false;
			EmpExecutionContext.error(e, "发送手机动态口令并获取状态报告异常。");
			return false;
		}
		return false;
	
	}

}
