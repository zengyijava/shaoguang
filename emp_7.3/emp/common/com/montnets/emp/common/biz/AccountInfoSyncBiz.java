/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-1-22 下午07:24:09
 */
package com.montnets.emp.common.biz;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AccountInfoSyncDAO;
import com.montnets.emp.entity.gateway.AgwAccount;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.util.MD5;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.webservice.accountsync.AccountSyncWebservice;

/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-1-22 下午07:24:09
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

public class AccountInfoSyncBiz extends SuperBiz
{
	AccountSyncWebservice accountSyncWebservice = new AccountSyncWebservice();
	
	AccountInfoSyncDAO accountInfoSyncDAO = new AccountInfoSyncDAO();

	/**
	 *
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-1-25 下午07:38:21
	 */
	public void accountInfoSync()
	{
		try
		{
            Map<String,String> actsyncs = accountInfoSyncDAO.getGlobalValByKey(new String[]{"ACTSYNCFLAG","ACTSYNCDBFLAG"});

			// 是否开启账号同步 1:开启 0：关闭
			String actsyncflag = actsyncs.get("ACTSYNCFLAG");

            // 是否更新数据库 1:开启 0：关闭
            String actsyncdbflag = actsyncs.get("ACTSYNCDBFLAG");

            // 未开启同步则结束
			if(!"1".equals(actsyncflag))
			{
				EmpExecutionContext.info("未开启签名同步开关，本次任务结束。");
                return;
			}
			// 查询后端账号
			List<AgwAccount>  agwAccountList = accountInfoSyncDAO.getSyncAccount();
			// 设置请求参数
			if(agwAccountList != null && agwAccountList.size() > 0)
			{
				// 账号，多个以逗号分割
				StringBuffer userIds = new StringBuffer();
				// 密码，多个以逗号分割
				StringBuffer pwds = new StringBuffer();
				// 处理返回结果
				AgwAccount agwAccount = null;
				for (int i = 1; i <= agwAccountList.size(); i++)
				{
					agwAccount = agwAccountList.get(i-1);
					userIds.append(agwAccount.getSpAccid()).append(",");
					pwds.append(MD5.getMD5Str(agwAccount.getSpAccPwd())).append(",");
					// 10个为一组
					if(i % 10 == 0)
					{
						userIds.deleteCharAt(userIds.lastIndexOf(","));
						pwds.deleteCharAt(pwds.lastIndexOf(","));
						accountInfoSync(userIds.toString(), pwds.toString(),actsyncdbflag, null);
						userIds.setLength(0);
						pwds.setLength(0);
					}
				}

				// 剩余的为最后一组
				if(userIds.length() > 0)
				{
					userIds.deleteCharAt(userIds.lastIndexOf(","));
					pwds.deleteCharAt(pwds.lastIndexOf(","));
					accountInfoSync(userIds.toString(), pwds.toString(),actsyncdbflag, null);
					userIds.setLength(0);
					pwds.setLength(0);
				}
			}else{
                EmpExecutionContext.info("签名同步未查询到后端账号！");
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "自动账号信息同步异常！");
		}
	}

	public List<String> executeRequest(String userId, String pwd, String url, String opType)
	{
		return accountSyncWebservice.getAccountInfoList(userId, pwd, url, opType);
	}

	/**
	 * 同步账号绑定的通道签名信息
	 * 
	 * @param userIds
	 *        多个账号
	 * @param pwds
	 *        对应的账号密码
     * @param actsyncdbflag
	 *        是否更新数据库 1：更新 0：不更新
	 * @param spisuncm
	 *        通道运营商类型 为null 处理所有运营商
	 */
	public void accountInfoSync(String userIds, String pwds, String actsyncdbflag ,String spisuncm)
	{
		String url = StaticValue.getMbossWebservicesUrl();
        // 0：自动同步 1：手动同步
        String opType = (spisuncm == null?"0":"1");
		// 调用查询接口获取账号信息
		List<String> accountInfoList = executeRequest(userIds.toString(), pwds.toString(), url, opType);

        if(accountInfoList == null ){
            EmpExecutionContext.info("签名同步操作接口返回通道账号信息为空，userIds："+userIds);
            return;
        }

        if(!"1".equals(actsyncdbflag)){
            EmpExecutionContext.info("签名同步未开启更新数据库状态功能！");
            return;
        }

		String[] infos = null;
		// 更新数据库
		for (String info : accountInfoList)
		{
			infos = info.split(",");
			if(infos.length != 9)
			{
				EmpExecutionContext.error("解析数据格式异常！info：" + info);
				continue;
			}
			// 如果运营商类型不匹配 则不处理
			if(spisuncm != null && !spisuncm.equals(infos[3]))
			{
				continue;
			}
			updateGate(infos);
		}
	}

    /**
     * 拉取指定通道签名信息
     * @param userId
     * @param pwd
     * @param spgate
     * @return
     */
    public String[] pullAccountInfo(String userId, String pwd, String spgate)
    {
        String url = StaticValue.getMbossWebservicesUrl();

        // 调用查询接口获取账号信息
        List<String> accountInfoList = executeRequest(userId.toString(), pwd.toString(), url, "1");

        if(accountInfoList == null ){
            EmpExecutionContext.info("签名同步操作接口返回通道账号信息为空，userId："+userId);
            return null;
        }

        String[] infos = null;

        for (String info : accountInfoList)
        {
            infos = info.split(",");
            if(infos.length != 9)
            {
                EmpExecutionContext.error("解析数据格式异常！info：" + info);
                continue;
            }
            // 如果通道账号匹配
            if((infos[1]+infos[2]).equals(spgate))
            {
                return infos;
            }
        }
        return null;
    }

    /**
     * 更新信息到对应通道
     * @param infos
     */
	public boolean updateGate(String[] infos)
	{
		boolean result = false;

		String userid = infos[0];
		// 主通道号
		String spgate = infos[1];
		// 扩展子号
		String cpno = infos[2];
		// 运营商类型0：移动，1：联通，21：电信，5：国外
		String spisuncm = infos[3];
		// 签名长度 0:自动计算，非0为签名固定长度（英文固定长度为其两倍）
		String signlen = infos[4];
		// 中文签名
		String signstr = infos[5];
		// 支持纯英文短信标示 0：不支持 非0:支持
		String enFlag = infos[6];
		// 英文签名
		String enSign = infos[7];
		// 签名前/后置 0：后置，非0前置
		String gateprivilege = infos[8];

		try
		{

            //是否支持纯英文短信
            boolean isSupportEn = !"0".equals(enFlag);

            signstr = signstr.trim();
            enSign = enSign.trim();

            if(StringUtils.isBlank(signlen) || Integer.parseInt(signlen) -10 > 0)
            {
                EmpExecutionContext.error("数据格式不合法，签名长度模式为空或超过最大值。signlen：" + signlen);
                return false;
            }
            //中文签名允许的最大长度
            int maxlen = "0".equals(signlen)?10:Integer.parseInt(signlen);

            //英文签名长度
            String ensignlen = String.valueOf(2*maxlen);

            //中文签名长度验证
            if(signstr.length() > maxlen)
            {
                EmpExecutionContext.error("数据格式不合法，中文签名长度超过最大值（"+maxlen+"）。sign：" + signstr );
                return false;
            }

            //英文签名长度验证
            if(isSupportEn && enSign.replaceAll("[\\[\\]\\|\\^\\{\\}\\~\\\\]", "**").length() > 2*maxlen)
            {
                EmpExecutionContext.error("数据格式不合法，英文签名长度超过最大值（"+2*maxlen+"）。enSign：" + enSign );
                return false;
            }

			// 签名 1：固定 0：自动
			String signType = "0".equals(signlen) ? "0" : "1";

			XtGateQueue gate = null;

			// WEB侧通道号＝主通道号+子号
			spgate += cpno;

			// 根据条件userid spgate 查找到对应匹配记录
			gate = accountInfoSyncDAO.getBindXtGate(userid, spgate, spisuncm);
			if(gate == null)
			{
				EmpExecutionContext.info("未找到对应后端通道信息。userid：" + userid + ",spgate：" + spgate + ",spisuncm：" + spisuncm);
				return false;
			}
			boolean isGW = gate.getSpisuncm() - 5 == 0;// 是否国外运营商类型


			// 短信通道
			if(gate.getGateType() == 1)
			{

				int gp = gate.getGateprivilege();
				String bytstr = Integer.toBinaryString(gp);
				String zerostr = "";
				for (int i = 0; i < 32 - bytstr.length(); i++)
				{
					zerostr = zerostr + "0";
				}
				String gpstr = zerostr + bytstr;
				StringBuffer b = new StringBuffer(gpstr);
				gateprivilege = "0".equals(gateprivilege) ? "0" : "1";

                //签名前后置标示
				b.setCharAt(29, gateprivilege.charAt(0));

                //是否支持英文短信标示
				b.setCharAt(30, isSupportEn?'1':'0');

				// 设置标示
				gate.setGateprivilege(Integer.valueOf(b.toString(), 2));

				String signdroptype = gate.getSignDropType().toString();
				if("1".equals(signType))
				{
					gate.setSignDropType(0);
				}
				else if("1".equals(signdroptype))
				{
					gate.setSignDropType(1);
				}
				else if("0".equals(signdroptype))
				{
					gate.setSignDropType(2);
				}

				if(isGW)
				{
					if(isSupportEn)
					{
						gate.setEnsignstr(enSign == null || "".equals(enSign) ? " " : enSign);
						gate.setEnsignlen("0".equals(signType) ? 0 : Integer.valueOf(ensignlen));
						gate.setEnmultilen1(gate.getEnsinglelen() - 160 == 0 ? 153 : gate.getEnsinglelen() - 6);
						gate.setEnmultilen2(gate.getEnmultilen1() - ("0".equals(signType) ? gate.getEnsignstr().trim().replaceAll("[\\[\\]\\|\\^\\{\\}\\~\\\\]", "**").length() : gate.getEnsignlen()));
					}
					else
					{
						gate.setEnsinglelen(160);
						gate.setEnsignstr(" ");
						gate.setEnsignlen(10);
						gate.setEnmultilen1(153);
						gate.setEnmultilen2(143);
					}
					gate.setMaxWords(360); // 国外通道中英文最大长度
					gate.setEnmaxwords(720);
				}
				else
				{
					gate.setMaxWords(1000); // 国内通道中英文最大长度
					gate.setEnmaxwords(2000);
				}

				// 由于oracle数据库短信签名为空字符串时，插入报错，这里做特殊处理将空字符串改成空格
				gate.setSignstr(signstr == null || "".equals(signstr) ? " " : signstr);
				gate.setSignlen("0".equals(signType) ? 0 : Integer.valueOf(signlen));
				gate.setMultilen1(gate.getSingleLen() - 3);
				gate.setMultilen2(gate.getMultilen1() - ("0".equals(signType) ? gate.getSignstr().trim().length() : gate.getSignlen()));
				Integer signfixlen = gate.getSignlen();
				if("0".equals(signType))
				{
					signfixlen = 0;
				}
				gate.setSignType(Integer.valueOf(signType));
				gate.setSignFixLen(signfixlen);
			}
			else
			{// 彩信通道
				if(isGW)
				{
					gate.setSignstr(signstr == null || "".equals(signstr) ? " " : signstr);
					gate.setEnsignstr(enSign == null || "".equals(enSign) ? " " : enSign);
				}
				else
				{
					gate.setSignstr(signstr == null || "".equals(signstr) ? " " : signstr);
				}
			}

			// 更新对应通道
			result = updatespgate(gate, spgate, spisuncm, gate.getGateType().toString());

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新通道信息失败!");
		}
		finally
		{
			if(result)
			{
				EmpExecutionContext.info("更新通道信息成功!info:" + Arrays.toString(infos));
			}
			else
			{
				EmpExecutionContext.error("更新通道信息失败!info:" + Arrays.toString(infos));
			}
		}
		return result;
	}

	/**
	 * 更新对应通道信息
	 *
	 * @param gate
	 *        通道对象
	 * @param spgate
	 *        通道号码
	 * @param spisuncm
	 *        运营商类型
	 * @param gatetype
	 *        通道类型
	 * @return
	 * @throws Exception
	 */
	public Boolean updatespgate(XtGateQueue gate, String spgate, String spisuncm, String gatetype) throws Exception
	{
		boolean result = true;
		Connection conn = empTransDao.getConnection();
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> gateMap = new LinkedHashMap<String, String>();
			if("1".equals(gatetype))
			{
				gateMap.put("multilen1", gate.getMultilen1() != null ? gate.getMultilen1().toString() : null);
				gateMap.put("enmultilen1", gate.getEnmultilen1() != null ? gate.getEnmultilen1().toString() : null);
				if(StaticValue.getCORPTYPE() == 0)
				{
					gateMap.put("multilen2", gate.getMultilen2() != null ? gate.getMultilen2().toString() : null);
					gateMap.put("enmultilen2", gate.getEnmultilen2() != null ? gate.getEnmultilen2().toString() : null);
				}
				else if(StaticValue.getCORPTYPE() == 1 && gate.getSignType() == 1)
				{
					gateMap.put("multilen2", gate.getMultilen2() != null ? gate.getMultilen2().toString() : null);
					gateMap.put("enmultilen2", gate.getEnmultilen2() != null ? gate.getEnmultilen2().toString() : null);
				}
				gateMap.put("signlen", gate.getSignlen() != null ? gate.getSignlen().toString() : null);
				gateMap.put("ensignlen", gate.getEnsignlen() != null ? gate.getEnsignlen().toString() : null);
				gateMap.put("maxwords", gate.getMaxWords() != null ? gate.getMaxWords().toString() : null);
				gateMap.put("enmaxwords", gate.getEnmaxwords() != null ? gate.getEnmaxwords().toString() : null);
				// 单条短信字数
				gateMap.put("singlelen", gate.getSingleLen() != null ? gate.getSingleLen().toString() : null);
				gateMap.put("ensinglelen", gate.getEnsinglelen() != null ? gate.getEnsinglelen().toString() : null);
			}
			if(StaticValue.getCORPTYPE() == 0)
			{
				// 短信签名
				gateMap.put("signstr", gate.getSignstr());
				gateMap.put("ensignstr", gate.getEnsignstr());
			}

			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("gateType", gatetype);

			empTransDao.beginTransaction(conn);
			empTransDao.update(conn, gate);
			if(gateMap != null && gateMap.size() > 0)
			{
				// 更新路由绑定表
				empTransDao.update(conn, GtPortUsed.class, gateMap, conditionMap);
				if(StaticValue.getCORPTYPE() == 1 && gate.getSignType() == 0) {
					String lenFunName = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? "LENGTH" : "LEN";
					String sql = "UPDATE GT_PORT_USED SET MULTILEN2=MULTILEN1-"+lenFunName+"(SIGNSTR),ENMULTILEN2=ENMULTILEN1-"+lenFunName+"(ENSIGNSTR) WHERE SPGATE='" + spgate + "' AND SPISUNCM=" + spisuncm + " AND GATETYPE=" + gatetype;
					executeSql(conn, sql);
				}
			}
			empTransDao.commitTransaction(conn);
			result = true;
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "修改通道号失败");
			result = false;
			throw e;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}

		return result;
	}

	public void executeSql(Connection conn, String sql) throws Exception
	{
		Statement ps = null;
		try
		{
			ps = conn.createStatement();
			ps.executeUpdate(sql);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if(ps != null){
				try{
					ps.close();
				}catch(Exception e){
					EmpExecutionContext.error(e, "关闭资源异常");
				}
			}
		}
	}
}
