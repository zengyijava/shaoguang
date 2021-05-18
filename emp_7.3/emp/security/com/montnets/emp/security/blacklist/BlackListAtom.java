package com.montnets.emp.security.blacklist;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.blacklist.PbListBlack;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.security.blacklist.dao.BlackListAtomDAO;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.table.blacklist.TablePbListBlack;

/**
 * @author Administrator
 */
public class BlackListAtom extends SuperBiz
{
	ErrorLoger errorLoger = new ErrorLoger();
	// 短信黑名单集合
	private static HashMap<String, HashMap<Long, Byte>>			AllBlackList	= new HashMap<String, HashMap<Long, Byte>>();

	// 同步黑名单的最大ID
	private static Long								smsMaxId		= 0l;

	// 彩信黑名单集合
	private final static HashMap<String, String>	AllMMsBlackList	= new HashMap<String, String>();

	// 彩信黑名单最大ID
	private static Long								mmsMaxId		= 0l;

	/***
	 * 过滤黑名单的新方法
	 * 
	 * @param phoneNum
	 *        号码
	 * @param busCode
	 *        业务类型
	 * @return false-不是黑名单号码，true-是黑名单号码
	 * @throws EMPException
	 *         EMP自定义异常
	 */
	public Boolean checkBlackList(String corpCode, String phoneNum, String busCode)
	{
		try
		{
			// 先判断是否有黑名单数据，没有则直接返回
			if(AllBlackList.size() == 0)
			{
				return false;
			}
			//去掉开头00或+ 以及0086或 +86
			phoneNum = formatPhone(phoneNum);

			//转为数字类型
			Long phone = Long.parseLong(phoneNum.trim());
			
			// 因为全部业务类型的业务编码是空的.
			// 且过滤进首先要到全部业务类型中找一次,如果没有找到,则再到所对应的业务编码中找一次.
			//String key = phoneNum + "," + "@" + "," + corpCode;
			String key = corpCode;
			if(AllBlackList.containsKey(key))
			{
				if(AllBlackList.get(key).containsKey(phone))
				{
					return true;
				}
			}
			// 如果在全部业务类型中不包含
			//key = phoneNum + "," + busCode + "," + corpCode;
			key = busCode + key;
			if(AllBlackList.containsKey(key))
			{
				if(AllBlackList.get(key).containsKey(phone))
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "过滤黑名单异常。"));
			return null;
		}
		return false;
	}

	/**
	 * @description 检查监控号码 是否黑名单
	 * @param phoneNum
	 * @return true是黑名单;false不是黑名单
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 下午06:33:38
	 */
	public Boolean monPhonecheckBlack(String phoneNum)
	{
		try
		{
			// 先判断是否有黑名单数据，没有则直接返回
			if(AllBlackList.size() == 0)
			{
				return false;
			}
			//国际号码(黑名单不支持国际号码)
			if("+".equals(phoneNum.substring(0, 1)) || "00".equals(phoneNum.substring(0, 2)))
			{
				return false;
			}
			//转为数字类型
			Long phone = Long.parseLong(phoneNum.trim());
			//String key = phoneNum + "," + "@" + ",100001";
			String key = "100001";
			if(AllBlackList.containsKey(key))
			{
				if(AllBlackList.get(key).containsKey(phone))
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "过滤黑名单异常。"));
			return false;
		}
		return false;
	}

	/**
	 * 黑名单赋值 的方法
	 */
	public void SetAllBlackList()
	{
		try
		{
			// 先清除集合中的值在添加进入集合
			AllBlackList.clear();
			//设置所有黑名单信息至缓存，记录更新最大ID
			smsMaxId = new BlackListAtomDAO().setAllBlackList(AllBlackList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, " 短信黑名单加载出现了异常!"));
		}
	}

	/**
	 * 添加黑名单到内存中
	 * 
	 * @param phones
	 *        号码集合
	 * @param busCode
	 *        业务类型
	 * @param corpCode
	 *        企业编码
	 */
	public void addBlackList(Long phone, String busCode, String corpCode)
	{
		try
		{
			HashMap<Long, Byte> valueMap = null;
			String key = busCode + corpCode;
			if(AllBlackList.containsKey(key.trim()))
			{
				AllBlackList.get(key.trim()).put(phone, null);
			}
			else
			{
				valueMap = new HashMap<Long, Byte>();
				valueMap.put(phone, null);
				AllBlackList.put(key.trim(), valueMap);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "新增短信黑名单到静态变量异常！");
		}
	}

	/**
	 * 批量添加黑名单到内存中
	 * 
	 * @param phones
	 *        号码集合
	 * @param busCode
	 *        业务类型
	 * @param corpCode
	 *        企业编码
	 */
	public void addBlackListByList(List<String> phones, String busCode, String corpCode)
	{
		try
		{
			HashMap<Long, Byte> valueMap = null;
			String key = busCode + corpCode;
			Long phone = 0L;
			for(String phoneStr: phones)
			{
			    if(phoneStr == null || "".equals(phoneStr))
			    {
			    	continue;
			    }
				phone = Long.parseLong(phoneStr.trim());
				if(AllBlackList.containsKey(key.trim()))
				{
					AllBlackList.get(key.trim()).put(phone, null);
				}
				else
				{
					valueMap = new HashMap<Long, Byte>();
					valueMap.put(phone, null);
					AllBlackList.put(key.trim(), valueMap);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "批量新增短信黑名单到静态变量异常！");
		}
	}

	/**
	 * 删除内存中的黑名单数据
	 * 
	 * @param phone
	 *        手机号
	 * @param busCode
	 *        业务类型
	 * @param corpCode
	 *        企业编码
	 */
	public void delBlackList(Long phone, String busCode, String corpCode)
	{
		try
		{
			String key = busCode + corpCode;
			//删除黑名单
			if(AllBlackList.containsKey(key.trim()))
			{
				AllBlackList.get(key.trim()).remove(phone);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "从静态变量删除短信黑名单异常！");
		}
	}

	/**
	 * 同步短信黑名单
	 */
	public void loadSmsBlist(boolean isReset)
	{
		try
		{
			BaseBiz baseBiz = new BaseBiz();
			// 获取上一次同步的最大ID
			Long maxBlId = smsMaxId;
	
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			conditionMap.put("id&>", maxBlId.toString());
			// 黑名单类型。1：短信；2：彩信
			conditionMap.put("blType", "1");
			//orderbyMap.put("id", "desc");
		    //改成升序排列  20170527 modify by tanglili
			orderbyMap.put("id", "asc");
			List<PbListBlack> blList = baseBiz.getByCondition(PbListBlack.class, conditionMap, orderbyMap);
			int addCount = 0;
			int delCount = 0;
			if(blList != null && blList.size() > 0)
			{
				//maxBlId = blList.get(0).getId();
				//升序排列 最大ID取最后一个值   20170527 modify by tanglili
				maxBlId = blList.get(blList.size()-1).getId();
				smsMaxId = maxBlId;
				for (PbListBlack bl : blList)
				{
					// 操作类型 0：取消黑名单;1：视为黑名单
					if(bl.getOptype() == 1)
					{
						addBlackList(bl.getPhone(), bl.getSvrType(), bl.getCorpCode());
						addCount++;
					}
					else if(bl.getOptype() == 0)
					{
						delBlackList(bl.getPhone(), bl.getSvrType(), bl.getCorpCode());
						delCount++;
					}
				}
			}
			EmpExecutionContext.info("定时增量加载短信黑名单成功，新增："+addCount+"，删除：" + delCount);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "定时同步短信黑名单异常。"));
		}

	}

	/**
	 * 删除无效的黑名单
	 * 
	 * @return
	 */
	public boolean delInValidBlackList()
	{
		try
		{
			BaseBiz baseBiz = new BaseBiz();

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

			// 操作类型 0：取消黑名单;1：视为黑名单
			conditionMap.put("optype", "0");
			Integer result = baseBiz.deleteByCondition(PbListBlack.class, conditionMap);
			EmpExecutionContext.info("删除无效黑名单成功，共删除：" + result);
			if(result > 0)
			{
				return true;
			}
			return false;

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "定时同步短信黑名单时删除历史数据异常。"));
			return false;
		}
	}

	/**
	 * 同步彩信黑名单
	 */
	public void loadMmsBlist(boolean isReset)
	{
		try
		{
			BaseBiz baseBiz = new BaseBiz();
			Long maxBlId = mmsMaxId;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			conditionMap.put("id&>", maxBlId.toString());
			// 黑名单类型。1：短信；2：彩信
			conditionMap.put("blType", "2");
			orderbyMap.put("id", "desc");
			List<PbListBlack> blList = baseBiz.getByCondition(PbListBlack.class, conditionMap, orderbyMap);
			int addCount = 0;
			int delCount = 0;
			if(blList != null && blList.size() > 0)
			{
				maxBlId = blList.get(0).getId();
				mmsMaxId = maxBlId;
				for (PbListBlack bl : blList)
				{
					// 操作类型 0：取消黑名单;1：视为黑名单
					if(bl.getOptype() == 1)
					{
						addmmsBlackList(bl.getPhone(), bl.getCorpCode());
						addCount++;
					}
					else if(bl.getOptype() == 0)
					{
						delmmsBlackList(bl.getPhone(), bl.getCorpCode());
						delCount++;
					}
				}
			}
			EmpExecutionContext.info("定时增量加载彩信黑名单成功，新增："+addCount+"，删除：" + delCount);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "定时同步彩信黑名单异常。"));
		}

	}

	/**
	 * 组彩信黑名单赋值 的方法
	 */
	public void SetMmsBlackList()
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			// orderbyMap.put("blId", StaticValue.DESC);
			orderbyMap.put("id", StaticValue.DESC);
			// 给黑名单赋值 的方法.
			HashMap<String, String> BlackList = new HashMap<String, String>();
			// 启用状态
			conditionMap.put("optype", "1");// 启用状态
			conditionMap.put("blType", "2");// 彩信黑名单
			// 只需要查询三个字段，phone,corp_code,bl_id
			String columName = TablePbListBlack.PHONE + "," + TablePbListBlack.CORPCODE + "," + TablePbListBlack.ID;
			List<String[]> lfBlacksList = empDao.findListByConditionByColumName(PbListBlack.class, conditionMap, columName, null);
			// 查询返回结果不为空
			if(lfBlacksList != null && lfBlacksList.size() > 0)
			{
				this.setMmsMaxId(Long.valueOf(lfBlacksList.get(0)[2]));
				for (int i = 0; i < lfBlacksList.size(); i++)
				{
					String[] black = lfBlacksList.get(i);
					BlackList.put(black[0] + "," + black[1], "");
				}
				// 先清除集合中的值在添加进入集合
				AllMMsBlackList.clear();
				AllMMsBlackList.putAll(BlackList);
			}
			EmpExecutionContext.info("彩信黑名单全量加载成功!共加载:" + AllMMsBlackList.size());
			BlackList.clear();

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "彩信黑名单加载出现了异常!"));
		}
	}

	/**
	 * 批量新增彩信黑名单到静态变量中去
	 * 
	 * @param phones
	 * @param corpCode
	 */
	public void addmmsBlackListByList(List<String> phones, String corpCode)
	{
		try
		{
			for (int i = 0; i < phones.size(); i++)
			{
				AllMMsBlackList.put(phones.get(i).trim() + "," + corpCode, "");
			}
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "批量新增彩信黑名单到静态变量异常！"));
		}
	}

	/**
	 * 新增彩信黑名单到静态变量中
	 * 
	 * @param phone
	 * @param corpCode
	 */
	public void addmmsBlackList(Long phone, String corpCode)
	{
		try
		{
			AllMMsBlackList.put(phone + "," + corpCode, "");

		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "新增彩信黑名单到静态变量异常！"));
		}
	}

	/**
	 * 将彩信黑名单从静态变量中删除的方法
	 * 
	 * @param phone
	 * @param corpCode
	 */
	public void delmmsBlackList(Long phone, String corpCode)
	{
		try
		{
			// 给黑名单赋值 的方法.
			AllMMsBlackList.remove(phone + "," + corpCode);
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "彩信黑名单从静态变量中删除异常！"));
		}
	}

	/**
	 * 处理彩信的黑名单过滤
	 * 
	 * @param corpCode
	 *        企业编码
	 * @param phoneNum
	 *        手机号码
	 * @return 存在返回true ，不存在则返回false
	 */
	public boolean checkMmsBlackList(String corpCode, String phoneNum)
	{
		try
		{
			if(AllMMsBlackList == null || AllMMsBlackList.size() == 0)
			{
				return false;
			}
			if(AllMMsBlackList.containsKey(phoneNum + "," + corpCode))
			{
				return true;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "处理彩信的黑名单过滤异常。"));
		}
		return false;
	}

	/**
	 * 获取同步黑名单的最大id
	 * 
	 * @return
	 */
	public Long getSmsMaxId()
	{
		return smsMaxId;
	}

	/**
	 * 设置同步黑名单的最大ID
	 * 
	 * @param blid
	 */
	public void setSmsMaxId(Long id)
	{
		smsMaxId = id;
	}

	public Long getMmsMaxId()
	{
		return mmsMaxId;
	}

	public void setMmsMaxId(Long id)
	{
		mmsMaxId = id;
	}
	
	 /**
     * 格式化手机字符串    去掉开头00或+ 以及0086或 +86
     * @param phone
     * @return
     */
    public String formatPhone(String phone){
        if(phone==null||"".equals(phone.trim())){
            return null;
        }
        return phone.replaceAll("^(0086|\\+86|00|\\+)","");
    }
    /**
	 * 根据上行记录指令设置黑名单
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-2-1 下午03:36:35
	 */
	public void setBlackByMoTaskOrder()
	{
		try
		{
			BlackListAtomDAO blackListAtomDAO = new BlackListAtomDAO();
			Map<String,String> blaOrder = blackListAtomDAO.getGlobalValByKey(new String[]{"ADDBLABYMOORDERFLAG","ADDBLAMOMAXID","ADDBLAMOORDER","DELBLAMOORDER","BLACKCORPCODE"});
			if(blaOrder != null && blaOrder.size() > 0)
			{
				// 通过上行指令新增黑名单0:关1:开
				Integer addBlaByBoOrderBlag = Integer.parseInt(blaOrder.get("ADDBLABYMOORDERFLAG"));
				// 上行记录新增黑名单指令最大ID
				Integer moAddBlaMaxId = Integer.parseInt(blaOrder.get("ADDBLAMOMAXID"));
				// 黑名单指令
				String order = blaOrder.get("ADDBLAMOORDER").trim();
				// 删除黑名单指令
				String delOrder = blaOrder.get("DELBLAMOORDER").trim();
				// 开上行指令维护黑名单企业
				String blackCorpCode = blaOrder.get("BLACKCORPCODE").trim();
				// 存在开通上行指令维护黑名单企业
				if(blackCorpCode != null && blackCorpCode.length() > 5)
				{
					//开启
					if(addBlaByBoOrderBlag == 1)
					{
						//设置指令
						if(delOrder != null && delOrder.length() > 0)
						{
							order += "," + delOrder;
						}
						// 指令集合
						StringBuffer orders = new StringBuffer();
						if(order.indexOf(",") < 0)
						{
							orders.append("'").append(order).append("'");
						}
						else
						{
							String[] orderClump = order.split(",");
							for(int i=0; i<orderClump.length; i++)
							{
								orders.append("'").append(orderClump[i]).append("'").append(",");
							}
							if(orders.indexOf(",") > -1)
							{
								orders.deleteCharAt(orders.lastIndexOf(","));
							}
						}
						// 企业编码集合
						StringBuffer blackCorpCodeSb = new StringBuffer();
						//设置企业编码
						if(blackCorpCode.indexOf(",") < 0)
						{
							blackCorpCodeSb.append("'").append(blackCorpCode).append("'");
						}
						else
						{
							String[] blackCorpCodeClump = blackCorpCode.split(",");
							for(int i=0; i<blackCorpCodeClump.length; i++)
							{
								blackCorpCodeSb.append("'").append(blackCorpCodeClump[i]).append("'").append(",");
							}
							if(blackCorpCodeSb.indexOf(",") > -1)
							{
								blackCorpCodeSb.deleteCharAt(blackCorpCodeSb.lastIndexOf(","));
							}
						}
						
						//根据最大ID和指令
						List<DynaBean> moTaskList = blackListAtomDAO.getMoTask(moAddBlaMaxId, orders.toString(), blackCorpCodeSb.toString());
						if(moTaskList != null && moTaskList.size() > 0)
						{
							//保存黑名单
							saveBlack(moTaskList, delOrder);
							moTaskList.clear();
							moTaskList = null;
						}
						else
						{
							EmpExecutionContext.info("查询上行记录指令设置黑名单数据条数为空，order："+order+"，moAddBlaMaxId:"+moAddBlaMaxId+"，blackCorpCode");
						}
					}
					else
					{
						EmpExecutionContext.info("根据上行记录指令设置黑名单功能未开启，addBlaByBoOrderBlag："+addBlaByBoOrderBlag);
					}
				}
				else
				{
					EmpExecutionContext.info("根据上行记录指令设置黑名单，未存在开通上行指令维护黑名单企业，blackCorpCode："+blackCorpCode);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据上行记录指令新增黑名单异常！");
		}
	}
	
	/***
	 * 短信短信黑名单保存方法(单企业)
	 * @param phones
	 * @param busCode
	 * @param corpCode
	 * @return
	 */
	public Integer singleSaveBlack(List<DynaBean> moTaskList)
	{

		boolean result = false;
		Connection conn = this.empTransDao.getConnection();
		List<String> phones = new LinkedList<String>();
		String moPhone = "";
		PbListBlack listBlack = null;
		List<PbListBlack> failBlacksList = new ArrayList<PbListBlack>();
		int count = 0;
		try
		{
			empTransDao.beginTransaction(conn);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String maxId = moTaskList.get(0).get("id").toString();
			//查询黑名单
			HashSet<String> resultList = this.getBlackListMap();
			for(DynaBean moTask:moTaskList)
			{
				moPhone = moTask.get("phone").toString().trim();
				//企业黑名单不存在此号码
				if(resultList.add(moPhone))
				{
					phones.add(moPhone);
					listBlack = new PbListBlack();
					listBlack.setUserId("000000");
					listBlack.setSpgate(" ");
					listBlack.setSpnumber(" ");
					listBlack.setPhone(Long.parseLong(moPhone));
					listBlack.setOptype(1);
					listBlack.setBlType(1);
					listBlack.setOptTime(timestamp);
					listBlack.setCorpCode("100001");
					listBlack.setSvrType(" ");
					//保存短信黑名单记录
					result=empTransDao.save(conn, listBlack);
					//失败记录
					if(!result)
					{
						failBlacksList.add(listBlack);
					}
					else
					{
						//计数
						count++;
					}
				}
				else
				{
					EmpExecutionContext.error("根据上行记录指令新增黑名单失败！已存在相同的黑名单信息，phone:"+moPhone);
				}
			}
			//更新失败的记录再次更新
			if(failBlacksList.size() > 0)
			{
				empTransDao.save(conn, failBlacksList, PbListBlack.class);
				count += failBlacksList.size();
			}
			// 更新最大ID
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			objectMap.put("globalValue", maxId);
			conditionMap.put("globalKey", "MOADDBLAMAXID");
			empTransDao.update(conn, LfGlobalVariable.class, objectMap, conditionMap);
			empTransDao.commitTransaction(conn);
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存短信黑名单异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return count;
	}
	
	/**
	 * 根据上行记录指令设置黑名单
	 * @description    
	 * @param moTaskList  上行记录
	 * @param delOrder    删除黑名单指令		 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-2-2 上午11:59:07
	 */
	public void saveBlack(List<DynaBean> moTaskList, String delOrder)
	{

		boolean result = false;
		Long exisPhoneKey = 0L;
		String moPhoneStr = "";
		Long moPhone = 0L;
		String corpCode = "";
		String msgContent = "";
		BaseBiz baseBiz = new BaseBiz();
		PbListBlack listBlack = null;
		List<PbListBlack> lfBlacksList = null;
		List<PbListBlack> pbListBlacks = new ArrayList<PbListBlack>();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int count = 0;
		int delCount = 0;
		boolean delState = true;
		//已新增的黑名单
		HashSet<Long> existList = new HashSet<Long>();
		//大写的删除黑名单指令
		String delOrderUpper = "";
		if(delOrder != null && delOrder.length() > 0)
		{
			delOrderUpper = ","+delOrder+",";
			delOrderUpper = delOrderUpper.toUpperCase();
		}
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String maxId = moTaskList.get(moTaskList.size()-1).get("id").toString();
			for(DynaBean moTask:moTaskList)
			{
				moPhoneStr = moTask.get("phone").toString().trim();
				moPhone = Long.parseLong(moPhoneStr);
				corpCode = moTask.get("corp_code").toString().trim();
				msgContent = moTask.get("msgcontent").toString().trim().toUpperCase();
				
				exisPhoneKey = Long.parseLong(moPhoneStr+corpCode);
				//组装过滤条件  			
				conditionMap.clear();
				conditionMap.put("phone", moPhoneStr);
				conditionMap.put("corpCode", corpCode);
				conditionMap.put("svrType", " ");
				conditionMap.put("optype", "1");
				conditionMap.put("blType", "1");
				//查询黑名单
				lfBlacksList = baseBiz.getByCondition(PbListBlack.class, conditionMap, null);
				//表中存在数据，先删除
				if(lfBlacksList != null && lfBlacksList.size() > 0)
				{
					int resultDel = 0;
					boolean resultDelState = false;
					int resutlSave = 0;
					for (PbListBlack pbListBlack : lfBlacksList) 
					{
						conditionMap.clear();
						conditionMap.put("id", pbListBlack.getId().toString());
						//将id置空，状态改为禁用
						pbListBlack.setId(null);
						pbListBlack.setOptype(0);
						pbListBlack.setOptTime(timestamp);
						//删除黑名单表记录
						resultDel = empTransDao.delete(conn, PbListBlack.class,conditionMap);
						if(resultDel > 0)
						{
							if(!resultDelState)
							{
								resultDelState = true;
								pbListBlacks.add(pbListBlack);
							}
						}
						else
						{
							delState = false;
						}
					}

					//删除黑名单
					if(delOrderUpper.length() > 0 && delOrderUpper.indexOf(","+msgContent+",") >= 0)
					{
						if(resultDelState && pbListBlacks.size() > 0)
						{
							//重新插入记录，状态设置为禁用
							resutlSave = empTransDao.save(conn, pbListBlacks, PbListBlack.class);
							//更新成功
							if(resutlSave > 0)
							{
								//提交
								empTransDao.commitTransaction(conn);
								//删除已新增的
								existList.remove(exisPhoneKey);
								delCount++;
								EmpExecutionContext.info("根据上行记录指令删除黑名单成功！moPhone:"+moPhoneStr+"，corpCode:"+corpCode+"，msgContent:"+msgContent);
							}
							else
							{
								//回滚
								empTransDao.rollBackTransaction(conn);
								EmpExecutionContext.info("根据上行记录指令删除黑名单失败，moPhone："+moPhoneStr+"，corpCode："+corpCode+"，msgContent:"+msgContent);
							}
						}
						else
						{
							//回滚
							empTransDao.rollBackTransaction(conn);
							EmpExecutionContext.info("根据上行记录指令删除黑名单失败，moPhone："+moPhoneStr+"，corpCode："+corpCode+"，msgContent:"+msgContent);
						}
						continue;
					}
				}
				//删除黑名单
				else if(delOrderUpper.length() > 0 && delOrderUpper.indexOf(","+msgContent+",") >= 0)
				{
					EmpExecutionContext.info("根据上行记录指令删除黑名单失败，表中未存在该黑名单号码！moPhone:"+moPhoneStr+"，corpCode:"+corpCode+"，msgContent:"+msgContent);
					continue;
				}
				//删除已存在的黑名单成功
				if(delState)
				{
					//新增时，是否已新增过
					if(existList.add(exisPhoneKey))
					{
						listBlack = new PbListBlack();
						listBlack.setUserId("000000");
						listBlack.setSpgate(" ");
						listBlack.setSpnumber(" ");
						listBlack.setPhone(moPhone);
						listBlack.setOptype(1);
						listBlack.setBlType(1);
						listBlack.setOptTime(timestamp);
						listBlack.setCorpCode(corpCode);
						listBlack.setSvrType(" ");
						//保存短信黑名单记录
						result=empTransDao.save(conn, listBlack);
						//成功
						if(result)
						{
							//提交
							empTransDao.commitTransaction(conn);
							count++;
							EmpExecutionContext.info("根据上行记录指令新增黑名单成功！moPhone:"+moPhoneStr+"，corpCode:"+corpCode+"，msgContent:"+msgContent);
						}
						else
						{
							//回滚
							empTransDao.rollBackTransaction(conn);
							EmpExecutionContext.info("根据上行记录指令新增黑名单失败！moPhone："+moPhoneStr+"，corpCode："+corpCode+"，msgContent:"+msgContent);
						}
					}
					else
					{
						//回滚
						empTransDao.rollBackTransaction(conn);
						EmpExecutionContext.info("根据上行记录指令设置黑名单，本次任务已新增过此号码！moPhone："+moPhoneStr+"，corpCode："+corpCode+"，msgContent:"+msgContent);
					}
				}
				else
				{
					//回滚
					empTransDao.rollBackTransaction(conn);
					EmpExecutionContext.info("根据上行记录指令新增时，删除已存在黑名单失败，moPhone："+moPhoneStr+"，corpCode："+corpCode+"，msgContent:"+msgContent);
				}
			}

			// 更新最大ID
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("globalValue", maxId);
			conditionMap.clear();
			conditionMap.put("globalKey", "ADDBLAMOMAXID");
			boolean resutlUpdate = empDao.update(LfGlobalVariable.class, objectMap, conditionMap);
			if(resutlUpdate)
			{
				EmpExecutionContext.info("根据上行记录指令设置黑名单，更新下行记录最大ID成功！maxId:"+maxId);
			}
			else
			{
				EmpExecutionContext.info("根据上行记录指令设置黑名单，更新下行记录最大ID失败！maxId:"+maxId);
			}
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"根据上行记录指令维护黑名单异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
			existList.clear();
			existList = null;
			if(lfBlacksList != null)
			{
				lfBlacksList.clear();
				lfBlacksList = null;
			}
			pbListBlacks.clear();
			pbListBlacks = null;
		}
		EmpExecutionContext.info("根据上行记录指令维护黑名单完成，查询记录总数："+moTaskList.size()+"，共新增："+count+"，共删除："+delCount);
	}
	
	/**
	 * 获取黑名单
	 * @param busCode   业务类型
	 * @param corpCode  企业编码
	 * @return
	 * @throws Exception
	 */
	public HashSet<String> getBlackListMap()
	{
		HashSet<String> resultList = new HashSet<String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			//组装过滤条件  			
			conditionMap.put("corpCode", "100001");
			conditionMap.put("svrType", " ");
			conditionMap.put("optype", "1");
			conditionMap.put("blType", "1");
			//查询方法
			List<PbListBlack> lfBlacksList = new BaseBiz().getByCondition(PbListBlack.class, conditionMap, null);
			if(lfBlacksList == null || lfBlacksList.size() == 0){
				return resultList;
			}
			//循环查询结果
			PbListBlack blackList = null;
			Long userphone;
			for(int i = 0; i < lfBlacksList.size(); i++){
				blackList = lfBlacksList.get(i);
				userphone = blackList.getPhone();
				resultList.add(userphone.toString());
			}
		}catch(Exception e){
			EmpExecutionContext.error(e, "获取黑名单列表异常！");
		}
		//返回结果list
		return resultList;
	}
}
