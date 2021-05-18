package com.montnets.emp.common.biz;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SysConfValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.entity.gateway.LfSpFee;
import com.montnets.emp.entity.pasgroup.Userfee;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 
 * 文件名称:RmsBalanceLogBiz.java
 * 文件描述:富信运营商余额扣除与回收
 * 内容摘要:
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-2-1    qiyin     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 * @author:   qiyin<15112605627@163.com>
 * @version:  0.1  
 * @Date:     2018-2-1 上午11:28:52
 */
public class RmsBalanceLogBiz extends BalanceLogBiz
{
	
	
	/**
	 * 检查富信运营商余额
	 * @param spUser SP账号
	 * @param count  发送条数(扣费金额)
	 * @param corpCode 企业编码
	 * @param iskoufei 是否扣费(false:否;true:是)
	 * @param mstype 账户类型
	 * @return 	
	 * 			-1：	lessgwfee-运营商余额不足
	 * 			-2：	nogwfee-没有运营商余额，
	 * 			-3：	feefail-执行运营商余额扣费失败
	 * 			0：	koufeiSuccess-扣费成功
	 * 			1：	feewarn-扣费成功，但余额不多
	 *			2：	notneedtocheck-后付费账户无需扣费,或没有配置需要检查运营商计费
	 */
	public String checkGwFeeRMS(String spUser, int count, String corpCode,
                                boolean iskoufei, Integer mstype)
	{
		//检查配置文件是否需要判断运营商计费
		//String gwFeeCheck = SystemGlobals.getValue("emp.gwfee.check");
		//修改为缓存中获取(缓存会读取数据库)，每2分钟会刷新一次缓存
		String gwFeeCheck=String.valueOf(SysConfValue.getGwfeeCheck());
		if(gwFeeCheck == null || !"1".equals(gwFeeCheck))
		{
			return "notneedtocheck";
		}
		if(spUser == null || "".equals(spUser))
		{
			EmpExecutionContext.error("运营商余额检查，前端应用账户异常，spUser为空，返回feefail。count：" + count
												+ "，corpCode:" + corpCode
												+ "，iskoufei:" + iskoufei
												+ "，mstype:" + mstype);
			return "feefail";			
		}
		try
		{
			String spaccuid = "";
			//单企业：0，多企业：1
			int corptype = StaticValue.getCORPTYPE();
			//单企业版时需要去查找前端账户对应的后端账户
			if(corptype == 0)
			{
				//获取发送账户spsuer对应的运营商账号
				spaccuid = getSpaccuidBySpUser(spUser,mstype);
				if(spaccuid== null || "".equals(spaccuid))
				{
					EmpExecutionContext.error("运营商扣费查询余额信息失败，前端应用账户无法找到对应的后端账户，返回nogwfee。spUser="+spUser
												+ "，count：" + count
												+ "，corpCode:" + corpCode
												+ "，iskoufei:" + iskoufei
												+ "，mstype:" + mstype);
					return "nogwfee";
				}
				if("spfeeflag=2".equals(spaccuid))
				{
					//后付费账户无需检查
					return "notneedtocheck";
				}
			}else
			{
				//多企业版使用前端账户进行扣费
				spaccuid = spUser;
			}
			spaccuid = spaccuid.toUpperCase();
			//获取对应账户的运营商余额信息
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			conditionMap.put("spUser", spaccuid);
			conditionMap.put("accountType", mstype.toString());
			
			List<LfSpFee> spFeeList = empDao.findListByCondition(LfSpFee.class, conditionMap, null);
			//判断是否获取到余额信息
			if(spFeeList == null || spFeeList.size() == 0)
			{
				EmpExecutionContext.error("运营商扣费查询余额信息失败，LfSpFee获取不到数据，返回feefail，spUser:" + spUser
						+ "，count：" + count
						+ "，corpCode:" + corpCode
						+ "，iskoufei:" + iskoufei
						+ "，mstype:" + mstype);
				return "feefail";
			}
			LfSpFee fee = spFeeList.get(0);
			if(fee.getSpFeeFlag()==2)
			{
				EmpExecutionContext.info("后付费账户无需进行运营商扣费，返回notneedtocheck，spUser:" + spUser
						+ "，count：" + count
						+ "，corpCode:" + corpCode
						+ "，iskoufei:" + iskoufei
						+ "，mstype:" + mstype);
				//后付费账户无需检查
				return "notneedtocheck";
			}
			//对应企业的余额更新时间
			Timestamp feeUpdateTime = fee.getUpdateTime();
			//上一次获取余额的状态
			String spResult = fee.getSpResult();
			//获取余额
			long feeCount = fee.getRmsBalance();
			//如果更新时间为空，或上一次获取余额的状态未成功，或扣费金额大于余额，或更新时间离现在时间大于特定的时间间隔
			if(feeUpdateTime==null || !"ok".equals(spResult) || count-feeCount>0
					|| System.currentTimeMillis() - feeUpdateTime.getTime() > updateTimeInterval)
			{
				//执行运营商余额获取方法
				updateSpFee(fee);
				//重新获取运营商余额状态
				spResult = fee.getSpResult();
				//重新获取的运营商余额
				feeCount = fee.getRmsBalance();
				//获取运营商失败
				if(spResult == null || !"ok".equals(spResult))
				{
					EmpExecutionContext.error("运营商扣费调用接口获取余额信息失败，使用本地余额进行扣费，spUser:" + spUser
							+ "，count：" + count
							+ "，corpCode:" + corpCode
							+ "，iskoufei:" + iskoufei
							+ "，mstype:" + mstype
							+ "，spResult:"+spResult
							+ "，feeCount:"+feeCount);
					//return "feefail";
				}
				//扣费金额大于余额，返回运营商余额不足
				if(count-feeCount>0)
				{
					EmpExecutionContext.error("运营商扣费失败，扣费金额大于余额，返回lessgwfee="+String.valueOf(feeCount)
							+"，spUser:" + spUser 
							+ "，count：" + count
							+ "，corpCode:" + corpCode
							+ "，iskoufei:" + iskoufei
							+ "，mstype:" + mstype);
					return "lessgwfee="+String.valueOf(feeCount);
				}
			}

			//扣费后剩余的余额
			long afterCount = feeCount - count;
			//如果扣费，更新余额
			if(iskoufei)
			{
				//更新时间
				fee.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				//更新余额
				fee.setRmsBalance(afterCount);
				if(!empDao.update(fee))
				{
					EmpExecutionContext.error("运营商扣费更新余额信息失败，返回feefail，spUser:" + spUser
							+ "，count：" + count
							+ "，corpCode:" + corpCode
							+ "，iskoufei:" + iskoufei
							+ "，mstype:" + mstype);
					return "feefail"; 
				}
			}
			return "koufeiSuccess";
		}catch(Exception e)
		{
			EmpExecutionContext.error(e, "运营商扣费异常，spUser:" + spUser
										+ "，count：" + count
										+ "，corpCode:" + corpCode
										+ "，iskoufei:" + iskoufei
										+ "，mstype:" + mstype);
			return "feefail";
		}
	}
	
	
	/**
	 * 
	 * @param sendCount 回收数量
	 * @param spUser sp账号
	 * @param mstype 信息类型
	 * @description  修改退费时更新运营商账户余额表
	 * @author qiyin <15112605627@163.com>
	 */
	public void huishouFee(int sendCount, String spUser, Integer mstype)
	{
		//检查配置文件是否需要判断运营商计费
		//String gwFeeCheck = SystemGlobals.getValue("emp.gwfee.check");
		//修改为缓存中获取(缓存会读取数据库)，每2分钟会刷新一次缓存
		String gwFeeCheck=String.valueOf(SysConfValue.getGwfeeCheck());
		if(gwFeeCheck == null || !"1".equals(gwFeeCheck))
		{
			return ;
		}
		//单企业：0，多企业：1
		int corptype = StaticValue.getCORPTYPE();
		String spacc = "";
		//如果是单企业，则去查找前端账户对应的后端账户
		if(corptype == 0)
		{
			spacc = getSpaccuidBySpUser(spUser,mstype);
			//获取不到后端账户或付费类型为后付费，则直接返回
			if(spacc == null || "spfeeflag=2".equals(spacc))
			{
				return ;
			}
		}else
		{
			//如果是多企业，则直接使用前端账户
			spacc = spUser;
		}
		spacc = spacc.toUpperCase();
		SpecialDAO specialDao = new SpecialDAO();
		try
		{
			//更新数据库
			specialDao.updateLfSpFeeBalanceRMS(spacc,String.valueOf(sendCount),mstype.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"运营商退费失败！spUser="+spUser+"，spacc:"+spacc+"，sendCount:"+sendCount+"，mstype:"+mstype);
		}
	}
	
	/**
	 * 回收费用，并给出返回值
	 *@anthor qiyin<15112605627@163.com>
	 *@param sendCount 回收数量
	 *@param spUser sp账号
	 *@param mstype 信息类型【1短信/富信，2彩信】
	 *@return {false：入库失败，true：'入库成功'，empnotfee：'emp配置不扣费'，notneedtocheck：'后付费'，error：'入库出现错误'}
	 */
	public String huishouFeeRms(int sendCount, String spUser, Integer mstype)
	{
		//检查配置文件是否需要判断运营商计费
		String result="false";
		//String gwFeeCheck = SystemGlobals.getValue("emp.gwfee.check");
		//修改为缓存中获取(缓存会读取数据库)，每2分钟会刷新一次缓存
		String gwFeeCheck=String.valueOf(SysConfValue.getGwfeeCheck());
		if(gwFeeCheck == null || !"1".equals(gwFeeCheck))
		{
			result="empnotfee";
			return result;
		}
		//单企业：0，多企业：1
		int corptype = StaticValue.getCORPTYPE();
		String spacc = "";
		//如果是单企业，则去查找前端账户对应的后端账户
		if(corptype == 0)
		{
			spacc = getSpaccuidBySpUser(spUser,mstype);
			//获取不到后端账户或付费类型为后付费，则直接返回
			if(spacc == null || "spfeeflag=2".equals(spacc))
			{
				result="notneedtocheck";
				return result;
			}
		}else
		{
			//如果是多企业，则直接使用前端账户
			spacc = spUser;
		}
		spacc = spacc.toUpperCase();
		SpecialDAO specialDao = new SpecialDAO();
		try
		{
			//更新数据库
			boolean flag=specialDao.updateLfSpFeeBalanceRMS(spacc,String.valueOf(sendCount),mstype.toString());
			if(flag){
				result="true";
			}
		}
		catch (Exception e)
		{
			result="error";
			EmpExecutionContext.error(e,"运营商退费失败！spUser="+spUser+"，spacc:"+spacc+"，sendCount:"+sendCount+"，mstype:"+mstype);
		}
		return result;
	}

	/**
	 * 扣除SP账号余额
	 * @date 2018-6-26 09:13:47
	 * @param spUser sp账号
	 * @param sendCount 发送条数
	 * @param corpCode 企业编码
	 * @param isKoufei 是否开启扣费
	 * @return 结果
	 * 		lessSpFee-SP账号余额不足
	 * 	 	feeFail-执行SP账号扣费失败
	 * 	 	koufeiSuccess-扣费成功
	 * 	 	notNeedToCheck-后付费账户无需扣费
	 */
    public String checkSpBalanceRMS(String spUser, Long sendCount, String corpCode, boolean isKoufei) {
		try {
			if(spUser == null || "".equals(spUser)) {
				EmpExecutionContext.error("SP余额检查,spUser为空，返回feefail。"
						+ "发送条数：" + sendCount
						+ "，企业编码:" + corpCode
						+ "，是否开启扣费:" + isKoufei
				);
				return "feeFail";
			}
			//获取SP账号的计费类型
			//SP账号类型,1:预付费;2:后付费
			Long feeFlag = -3L;
			//获取SP账号类型
			//accountType 1为短信 2为彩信
			feeFlag = getSpUserFeeFlag(spUser, 1);
			if(feeFlag < 0){
				EmpExecutionContext.error("SP余额扣费查询SP账号类型失败，Userdata获取不到数据，返回feefail，spUser:" + spUser
						+ "，发送数:" + sendCount
						+ "，企业编码:" + corpCode
						+ "，是否开启扣费:" + isKoufei
				);
				return "feeFail";
			}
			//如果为后付费
			if(feeFlag == 2){
				EmpExecutionContext.info("后付费账户无需进行SP余额扣费，返回notneedtocheck，spUser:" + spUser
							+ "，发送数:" + sendCount
							+ "，企业编码:" + corpCode
							+ "，是否开启扣费:" + isKoufei
						);
				//后付费账户无需检查
				return "notNeedToCheck";
			}
			//获取SP账户余额
			spUser = spUser.toUpperCase();
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			conditionMap.put("userId", spUser);

			List<Userfee> userfees = empDao.findListByCondition(Userfee.class, conditionMap, null);
			//判断是否获取到余额信息
			if(userfees == null || userfees.size() == 0) {
				EmpExecutionContext.error("SP账号扣费查询余额信息失败，Userfee获取不到数据，返回feefail，spUser:" + spUser
						+ "，发送数:" + sendCount
						+ "，企业编码:" + corpCode
						+ "，是否开启扣费:" + isKoufei
				);
				return "feeFail";
			}
			Userfee fee = userfees.get(0);
			Long feeCount = fee.getSendNum();
			//扣费金额大于余额，返回SP账号余额不足
			if(sendCount - feeCount > 0) {
				EmpExecutionContext.error("SP账号扣费失败，扣费金额大于余额，返回lessgwfee=余额，spUser:" + spUser
						+ "，发送数:" + sendCount
						+ "，企业编码:" + corpCode
						+ "，是否开启扣费:" + isKoufei);
				return "lessSpFee="+String.valueOf(feeCount);
			}
			//扣费后剩余的余额
			long afterCount = feeCount - sendCount;
			//如果扣费，更新余额
			if(isKoufei){
				fee.setSendNum(afterCount);
				fee.setSendedNum(fee.getSendedNum() + sendCount);
				if(!empDao.update(fee)) {
					EmpExecutionContext.error("SP余额扣除失败，返回feefail，spUser:" + spUser
							+ "，发送数:" + sendCount
							+ "，企业编码:" + corpCode
						);
					return "feeFail";
				}
			}
			return "koufeiSuccess";
		} catch (Exception e) {
			EmpExecutionContext.error(e,"SP余额检查,返回feefail。发送条数：" + sendCount
					+ "，企业编码:" + corpCode
					+ "，是否开启扣费:" + isKoufei
			);
			return "feeFail";
		}
	}

	/**
	 * 回收SP账号余额
	 * @param recyleCount 回收数
	 * @param spUser SP账号
	 * @return 回收结果 false 回收失败 true 回收成功 error 出现错误
	 */
    public String recycleSpFee(Long recyleCount, String spUser) {
    	String result;
		//获取SP账号的计费类型
		//SP账号类型,1:预付费;2:后付费
		Long feeFlag = -3L;
		//获取SP账号类型
		//accountType 1为短信 2为彩信
		feeFlag = getSpUserFeeFlag(spUser, 1);
		if(feeFlag < 0){
			EmpExecutionContext.error("企业富信回收SP账号余额失败，Userdata获取不到数据，返回error，" +
					"spUser:" + spUser
					+ "，回收数:" + recyleCount
			);
			return "error";
		}
		//如果为后付费
		if(feeFlag != 1){
			EmpExecutionContext.warn("企业富信回收SP账号余额失败，后付费SP账号不需要回收，返回noNeedToCheck，" +
					"spUser:" + spUser
					+ "，回收数:" + recyleCount
			);
			return "noNeedToCheck";
		}
		try {
			//更新数据库
			SpecialDAO specialDao = new SpecialDAO();
			boolean flag = specialDao.updateUserFeeBalanceRMS(spUser,recyleCount);
			if(flag){
				result = "true";
			}else {
				result = "false";
			}
		} catch (Exception e) {
			result = "error";
			EmpExecutionContext.error(e,"企业富信回收SP账号余额失败！SP账号="+spUser+"，回收数:"+recyleCount);
		}
		return result;
    }
}

