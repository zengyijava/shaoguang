package com.montnets.emp.common.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.SMParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;

/**
 * 尾号管理biz
 * 
 * @author Administrator
 * 
 */
public class SubnoManagerBiz
{
	// 数据库操作dao
	private IEmpDAO empDao = new DataAccessDriver().getEmpDAO();

	// 数据库操作dao，用于事务
	
	private IEmpTransactionDAO empTransDao = new DataAccessDriver().getEmpTransDAO();
	
	//定时对象
	protected static  Timer timer = null;
	//定时清理尾号任务对象
	protected static TimerTask timerTask = null;

	/**
	 * 定期过去尾号详情表中过期的记录并删除,4个小时调用一次 2012-8-2
	 */
	public void executeDelSubno()
	{
		//定时对象
		timer = new Timer();
		/*
		 * Calendar cal = Calendar.getInstance(); cal.add(Calendar.DAY_OF_MONTH,
		 * 1); //每天2:00点执行 cal.set(Calendar.HOUR_OF_DAY, 2);
		 * cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
		 */
		
		timerTask = new TimerTask()
		{
			public void run()
			{
				
				try
				{
					//条件
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					Timestamp nowTime = new Timestamp(System.currentTimeMillis());
					StringBuffer ids = new StringBuffer();
					StringBuffer ids2 = new StringBuffer();
					//conditionMap.put("isValid", "2");
					conditionMap.put("validity&is not null", "1");
					conditionMap.put("validity&>", "0");
					//获取尾号对象详情记录
					List<LfSubnoAllotDetail> details = empDao.findListBySymbolsCondition
							(LfSubnoAllotDetail.class,
									conditionMap, null);
					//获取尾号对象记录
					List<LfSubnoAllot> allots = empDao.findListBySymbolsCondition(
							LfSubnoAllot.class, conditionMap, null);
					//清空
					conditionMap.clear();
					//尾号详细对象
					LfSubnoAllotDetail detail = null;
					LfSubnoAllot allot = null;
					//循环处理尾号对象详细
					for (int i = 0; i < details.size(); i++)
					{
						detail = details.get(i);
						if (detail.getValidity() == null
								|| detail.getValidity() == 0)
						{
							continue;
						}
						//计算间隔
						Long internal = nowTime.getTime()
								- detail.getCreateTime().getTime();
						if (internal >= detail.getValidity())
						{
							ids.append(detail.getSudId()).append(",");
						}
					}
					for (int j = 0; j < allots.size(); j++)
					{
						allot = allots.get(j);
						// 如果没有有效期字段直接跳过这条记录，遍历下一条
						if (allot.getValidity() == null
								|| allot.getValidity() == 0)
						{
							continue;
						}
						Long internal = nowTime.getTime()
								- allot.getCreateTime().getTime();
						if (internal >= allot.getValidity())
						{
							ids2.append(allot.getSuId()).append(",");
						}
					}
					//有记录
					if (ids != null && ids.length() > 0)
					{
						ids.deleteCharAt(ids.lastIndexOf(","));
						int delNum = empDao.delete(LfSubnoAllotDetail.class,
								ids.toString());
						EmpExecutionContext.debug("尾号模块定时器在 "
								+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
										.format(nowTime) + " 删除了" + delNum
								+ "条过期尾号详情记录！");
					}
					//有记录
					if (ids2 != null && ids2.length() > 0)
					{
						ids2.deleteCharAt(ids2.lastIndexOf(","));
						int delNum2 = empDao.delete(LfSubnoAllot.class, ids2
								.toString());
						EmpExecutionContext.debug(new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss").format(nowTime)
								+ " 定时器删除了" + delNum2 + "条过期尾号概要记录！");
					}
				} catch (Exception e)
				{
					//异常处理
					EmpExecutionContext.error(e, "定期删除尾号详情表中过期的记录异常。");
				} 
			}
		};
		
		try
		{
			//设置定时
			timer.schedule(timerTask, 60 * 1000L, 4 * 3600 * 1000L);// 延迟60s执行，每隔四个小时执行一次
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置处理尾号定时任务异常。");
		}
	}

	/**
	 * 停止处理尾号定时任务
	 */
	public static void stopDelSubno(){
		if(timerTask != null){
			timerTask.cancel();
			timerTask = null;
		}
		if(timer != null){
			timer.cancel();
			timer = null;
		}
	}
	
	/**
	 * 根据尾号和企业编码改变尾号的状态为 2012-8-3
	 * 
	 * @param subno
	 *            尾号
	 * @param corpCode
	 *            企业编码
	 * @param validate
	 *            有效期 为null表示永久有效
	 * @param conn
	 *            数据库连接用作事务，可以为null
	 * @return true表示修改成功false表示修改失败 boolean
	 */
	public boolean updateSubnoStat(String subno, String corpCode,
			Long validate)
	{
		// 如果找不到尾号参数对应的记录也会返回false
		boolean result = false;
		if (subno == null || "".equals(subno.trim()) || corpCode == null
				|| "".equals(corpCode.trim()))
		{
			EmpExecutionContext.error("参数为空！subno:" + subno + ";corpCode:"
					+ corpCode + ";validate:" + validate);
			//返回
			return false;
		}
		
		//没连接则自己获取
		Connection connection = null;

		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 企业编码
			conditionMap.put("corpCode", corpCode);
			// 尾号，这两个字段在两张表里面字段名称相同
			conditionMap.put("usedExtendSubno", subno);
			// 找出无效的概要和详细
			conditionMap.put("isValid", "2");
			//查询记录
			List<LfSubnoAllotDetail> details = empDao.findListByCondition(
					LfSubnoAllotDetail.class, conditionMap, null);

			connection = empTransDao.getConnection();
			//开启事务
			empTransDao.beginTransaction(connection);
			
			if (details != null && details.size() > 0)
			{
				LfSubnoAllotDetail detail = details.get(0);
				// 有有效期就设置有效期，没有有效期就设为0(表示永久有效)
				detail.setValidity(validate == null ? 0L : validate);
				//有效性(1有效，2无效)
				detail.setIsValid(1);
				result = empTransDao.update(connection, detail);
				if(!result){
					EmpExecutionContext.error("更新尾号详细表有效性失败！");
				}
			}
			
			//查询记录
			List<LfSubnoAllot> allots = empDao.findListByCondition(
					LfSubnoAllot.class, conditionMap, null);
			if (allots != null && allots.size() > 0)
			{
				LfSubnoAllot allot = allots.get(0);
				// 有有效期就设置有效期，没有有效期就设为0(表示永久有效)
				allot.setValidity(validate == null ? 0L : validate);
				//有效性(1有效，2无效)
				allot.setIsValid(1);
				result = empTransDao.update(connection, allot);
				if(!result){
					EmpExecutionContext.error("更新尾号概要表有效性失败！");
				}
			}
			empTransDao.commitTransaction(connection);

		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(connection);
			//异常处理
			EmpExecutionContext.error(e, "更新尾号状态失败！");
			result = false;
		} finally{
			empTransDao.closeConnection(connection);
		}
		//返回结果
		return result;
	}
	
	/**
	 * 根据尾号和企业编码改变尾号的状态为 2012-8-3
	 * 
	 * @param subno
	 *            尾号
	 * @param corpCode
	 *            企业编码
	 * @param validate
	 *            有效期 为null表示永久有效
	 * @param conn
	 *            数据库连接用作事务，可以为null
	 * @return true表示修改成功false表示修改失败 boolean
	 */
	public boolean updateSubnoStat(String subno, String corpCode,
			Long validate, Connection connection)
	{
		// 如果找不到尾号参数对应的记录也会返回false
		boolean result = false;
		if (subno == null || "".equals(subno.trim()) || corpCode == null
				|| "".equals(corpCode.trim()))
		{
			EmpExecutionContext.error("参数为空！subno:" + subno + ";corpCode:"
					+ corpCode + ";validate:" + validate);
			//返回
			return false;
		}

		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 企业编码
			conditionMap.put("corpCode", corpCode);
			// 尾号，这两个字段在两张表里面字段名称相同
			conditionMap.put("usedExtendSubno", subno);
			// 找出无效的概要和详细
			conditionMap.put("isValid", "2");
			//查询记录
			List<LfSubnoAllotDetail> details = empDao.findListByCondition(
					LfSubnoAllotDetail.class, conditionMap, null);
			if (details != null && details.size() > 0)
			{
				LfSubnoAllotDetail detail = details.get(0);
				// 有有效期就设置有效期，没有有效期就设为0(表示永久有效)
				detail.setValidity(validate == null ? 0L : validate);
				//有效性(1有效，2无效)
				detail.setIsValid(1);
				result = empTransDao.update(connection, detail);
				if(!result){
					EmpExecutionContext.error("更新尾号详细表有效性失败！");
				}
			}
			
			//查询记录
			List<LfSubnoAllot> allots = empDao.findListByCondition(
					LfSubnoAllot.class, conditionMap, null);
			if (allots != null && allots.size() > 0)
			{
				LfSubnoAllot allot = allots.get(0);
				// 有有效期就设置有效期，没有有效期就设为0(表示永久有效)
				allot.setValidity(validate == null ? 0L : validate);
				//有效性(1有效，2无效)
				allot.setIsValid(1);
				result = empTransDao.update(connection, allot);
				if(!result){
					EmpExecutionContext.error("更新尾号概要表有效性失败！");
				}
			}

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "更新尾号状态失败！");
			result = false;
		} 
		//返回结果
		return result;
	}

	/**
	 * 获取有效的尾号
	 * 2012-8-3
	 * 
	 * @param codes
	 *            编码
	 * @param codeType
	 *            编码类型(0模块编码;1业务编码;2产品编码;3机构id;4操作员guid;5任务id)
	 * @return String
	 */
	public String getValidSubno(String codes, Integer codeType,
			String corpCode, ErrorCodeParam errorCode)
	{
		try
		{
			//以下各编码类型一定不能合为一个---廖骥荣批注
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//企业编码
			conditionMap.put("corpCode", corpCode);
			//conditionMap.put("codes", codes);
			//conditionMap.put("codeType",codeType.toString());
			// 模块编码
			if (codeType == 0)
			{
				//模块编码
				conditionMap.put("menuCode", codes);
			} else if (codeType - 1 == 0)
			{
				// 业务编码
				conditionMap.put("busCode", codes);
			} else if (codeType - 3 == 0)
			{
				// 机构编码
				conditionMap.put("depId", codes);
			} else if (codeType - 4 == 0)
			{
				// 操作员guid
				conditionMap.put("loginId", codes);
			} 
			//查询记录
			List<LfSubnoAllot> allots = empDao.findListByCondition(
					LfSubnoAllot.class, conditionMap, null);
			// 如果找到尾号记录
			if (allots != null && allots.size() > 0)
			{
				LfSubnoAllot allot = allots.get(0);
				return allot.getUsedExtendSubno();
			}
			// 找不到尾号记录，再调用生成尾号方法
			else
			{
				SMParams smParams = new SMParams();

				smParams.setCodes(codes);
				smParams.setCodeType(codeType);
				smParams.setAllotType(0);
				smParams.setSubnoVali(true);
				smParams.setCorpCode(corpCode);

				LfSubnoAllotDetail detailTemp = GlobalVariableBiz.getInstance()
						.getSubnoDetail(smParams, errorCode);
				return detailTemp == null ? "" : detailTemp
						.getUsedExtendSubno();
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取有效的尾号异常。");
			return "";
		}
	}

	/**
	 * 获取尾号详情对象
	 * @param //spUserid
	 *            发送账号 可以为null
	 * @param //codes
	 *            编码（0模块编码1业务编码2产品编码3机构id4操作员guid,5任务id）
	 * @param //codeType
	 *            编码类别
	 * @param //allotType
	 *            (分配类型0固定1自动有效期7天，null表是不设有效期)
	 * @param //subnoVali
	 *            尾号是否确定插入表
	 * @return 尾号详情记录
	 * @throws Exception
	 */
	public LfSubnoAllotDetail getSubnoDetail(SMParams smParams,
			ErrorCodeParam errorCode)
	{
		String spUserid = smParams.getSpUserid();
		String codes = smParams.getCodes();
		Integer codeType = smParams.getCodeType();
		String corpCode = smParams.getCorpCode();
		Integer allotType = smParams.getAllotType();
		boolean subnoVali = smParams.getSubnoVali();
		// errorCode = new ErrorCodeParam();
		//不能为空
		if (codes == null || "".equals(codes) || codeType == null)
		{
			EmpExecutionContext.error("参数编码或编码类型为空！");
			return null;
		}

		Connection connection = empTransDao.getConnection();

		boolean flag = true;
		// 返回对象
		LfSubnoAllotDetail detail = null;
		// 如果当前账号有概要表记录则保存在这个对象里面
		LfSubnoAllot curAllot = null;

		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", corpCode);
			Integer digit = empDao.findListBySymbolsCondition(LfCorp.class,
					conditionMap, null).get(0).getSubnoDigit();
			// 获取范围最大尾号
			int i = Integer.parseInt(this.getMaxSubno(digit));

			while (flag)
			{
				// 如果尾号都已经用完避免死循环
				i--;
				if (0 >= i)
				{
					errorCode.setErrorCode("EZHB237");
					break;
				}
				String curSubno;
				// 判断发送账号是否为空，如果为空
				if (spUserid == null || "".equals(spUserid.trim()))
				{
					// 获取全局自增尾号
					curSubno = this.getGlobalSubnoAllot(corpCode);
				} else
				{
					// 如果发送账号不为空，则根据发送账号、编码、编码类型查询尾号概要表
					conditionMap.clear();
					List<LfSubnoAllot> allots = new ArrayList<LfSubnoAllot>();
					conditionMap.put("spUser", spUserid);
					conditionMap.put("corpCode", corpCode);
					if (codeType == 0)
					{
						// 模块编码
						conditionMap.put("menuCode", codes);
					} else if (codeType - 1 == 0)
					{
						// 业务编码
						conditionMap.put("busCode", codes);
					} else if (codeType - 3 == 0)
					{
						// 机构编码
						conditionMap.put("depId", codes);
					} else if (codeType - 4 == 0)
					{
						// 操作员guid
						conditionMap.put("loginId", codes);
					} else if (codeType - 5 == 0)
					{
						// 任务id
						conditionMap.put("taskId", codes);
					}
					allots = empDao.findListBySymbolsCondition(
							LfSubnoAllot.class, conditionMap, null);
					// 尾号概要表里面有对应记录
					if (allots != null && allots.size() > 0)
					{
						// 读取该尾号概要表记录当前使用子号和分配范围
						curAllot = allots.get(0);
						String curSub = curAllot.getUsedExtendSubno();
						String maxSub = curAllot.getExtendSubnoEnd();
						String updateSub;
						// 如果当前使用尾号为空，将当前尾号设为起始尾号
						if (curSub == null || "".equals(curSub))
						{
							updateSub = curAllot.getExtendSubnoBegin();
						} else if (Integer.parseInt(curSub) >= Integer
								.parseInt(maxSub))
						{
							// 将当前尾号设为起始尾号
							updateSub = curAllot.getExtendSubnoBegin();
						} else
						{
							updateSub = this.createSubno(
									new StringBuffer(curSub), curSub.length())
									.toString();
						}
						curSubno = updateSub;
						curAllot.setUsedExtendSubno(curSubno);
						//更新到数据库
						empDao.update(curAllot);
					} else
					{
						// 尾号概要表里没有对应记录，读取Lf_corp获取尾号
						curSubno = getGlobalSubnoAllot(corpCode);
					}
				}

				// 到尾号详情表里面验证该尾号是否已经被占用
				conditionMap.clear();
				conditionMap.put("usedExtendSubno", curSubno);
				conditionMap.put("corpCode", corpCode);
				List<LfSubnoAllotDetail> allotDetails = empDao
						.findListBySymbolsCondition(LfSubnoAllotDetail.class,
								conditionMap, null);
				// 如果没有记录表明该尾号没有被占用
				if ((allotDetails == null || allotDetails.size() == 0) && this.isContainGtPortCpno(curSubno))
				{
					// 开始事务
					empTransDao.beginTransaction(connection);
					// 返回对象
					detail = new LfSubnoAllotDetail();
					flag = false;

					detail.setMenuCode(smParams.getMenuCode());
					detail.setBusCode(smParams.getBusCode());
					detail.setDepId(smParams.getDepId());
					detail.setLoginId(smParams.getLoginId());
					detail.setTaskId(smParams.getTaskId());

					// 自动尾号设置有效期7天(7*24小时)
					if (allotType != null && (allotType - 0) == 1)
					{
						// 如果自动尾号且概要表里面有记录则更新，没有则只插详情表
						if (curAllot != null)
						{
							curAllot.setUsedExtendSubno(curSubno);
							empTransDao.update(connection, curAllot);
							detail.setSuId(curAllot.getSuId());// 当主表不为空时，在详情表里面添加概要表id
						}
						// 有效期设为7天(单位毫秒)
						detail.setValidity(StaticValue.VALIDITY);
						detail.setAllotType(1);
						detail.setSpUser(spUserid);
						if (codeType == 0)
						{
							// 模块编码
							detail.setMenuCode(codes);
						} else if (codeType - 1 == 0)
						{
							// 业务编码
							detail.setBusCode(codes);
						} else if (codeType - 3 == 0)
						{
							// 机构编码
							detail.setDepId(Long.parseLong(codes));
						} else if (codeType - 4 == 0)
						{
							// 操作员guid
							detail.setLoginId(codes);
						} else if (codeType - 5 == 0)
						{
							// 操作员guid
							detail.setTaskId(Long.parseLong(codes));
						}
						if (!subnoVali)
						{
							// 自动的如果假插，有效期设为1小时(毫秒)
							detail.setValidity(1 * 3600 * 1000L);
							// 设置有效期为无效
							detail.setIsValid(2);
						} else
						{
							// detail.setValidity(StaticValue.VALIDITY);
							// 设为有效
							detail.setIsValid(1);
						}

						detail.setUsedExtendSubno(curSubno);
						detail.setCorpCode(corpCode);
						//保存到数据库
						empTransDao.save(connection, detail);
					} else
					{
						// 固定
						// 插入概要表记录
						LfSubnoAllot subnoAllot = new LfSubnoAllot();
						subnoAllot.setMenuCode(smParams.getMenuCode());
						subnoAllot.setBusCode(smParams.getBusCode());
						subnoAllot.setLoginId(smParams.getLoginId());
						subnoAllot.setDepId(smParams.getDepId());
						subnoAllot.setTaskId(smParams.getTaskId());

						// 设置企业编码
						detail.setCorpCode(corpCode);
						subnoAllot.setCorpCode(corpCode);
						// 设置发送账号
						detail.setSpUser(spUserid);
						subnoAllot.setSpUser(spUserid);

						// 设置编码
						
						// 模块编码
						if (codeType == 0)
						{
							detail.setMenuCode(codes);
							subnoAllot.setMenuCode(codes);
						} else if (codeType - 1 == 0)
						{
							// 业务编码
							detail.setBusCode(codes);
							subnoAllot.setBusCode(codes);
						} else if (codeType - 3 == 0)
						{
							// 机构编码
							detail.setDepId(Long.parseLong(codes));
							subnoAllot.setDepId(Long.parseLong(codes));
						} else if (codeType - 4 == 0)
						{
							// 操作员guid
							detail.setLoginId(codes);
							subnoAllot.setLoginId(codes);
						} else if (codeType - 5 == 0)
						{
							// 任务id
							detail.setTaskId(Long.parseLong(codes));
							subnoAllot.setTaskId(Long.parseLong(codes));
						}
						// 设置当前使用尾号
						detail.setUsedExtendSubno(curSubno);
						subnoAllot.setUsedExtendSubno(curSubno);
						// 设置尾号类型固定还是自动
						subnoAllot.setAllotType(0);
						detail.setAllotType(0);
						// 如果不是确定插值，则将有效期设为1个小时
						if (!subnoVali)
						{
							// 固定的如果假插，有效期设为0(表示永久),有效期设置为无效
							detail.setValidity(1 * 3600 * 1000L);
							detail.setIsValid(2);
							subnoAllot.setValidity(1 * 3600 * 1000L);
							subnoAllot.setIsValid(2);
						} else
						{
							// 固定的如果真插，有效期设为0(表示永久),有效期设置为有效
							detail.setValidity(0L);
							detail.setIsValid(1);
							subnoAllot.setValidity(0L);
							subnoAllot.setIsValid(1);
						}
						// 设置创建时间
						subnoAllot.setCreateTime(detail.getCreateTime());
						Long sudId = empTransDao.saveObjectReturnID(connection,
								subnoAllot);
						detail.setSudId(sudId);
						empTransDao.save(connection, detail);
					}
					//提交事务
					empTransDao.commitTransaction(connection);
				}
			}
		} catch (Exception e)
		{
			//异常处理
			//回滚
			empTransDao.rollBackTransaction(connection);
			EmpExecutionContext.error(e, "获取尾号详情对象异常。");
			errorCode.setErrorCode("EZHB238");
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(connection);
		}
		//返回结果
		return detail;
	}

	/**
	 * 判断分配的尾号是否与路由分配的子号相包含
	 * @param subno 当前分配的尾号
	 * @return 相包含则不能使用，返回false；不相包含，可以用，返回true
	 */
	public boolean isContainGtPortCpno(String subno)
	{
		try
		{
			//尾号不能为空
			if (subno == null || subno.trim().length() == 0)
			{
				EmpExecutionContext.error("验证尾号与路由扩展子号是否包含异常，尾号为空");
				return false;
			}
			
			//获取所有路由记录
			List<GtPortUsed> portsList = empDao.findListByCondition(GtPortUsed.class, null, null);
			if (portsList == null || portsList.size() == 0)
			{
				//没路由则直接可用
				return true;
			}
			
			GtPortUsed port = null;
			for (int i = 0; i < portsList.size(); i++)
			{
				port = portsList.get(i);
				if (port.getCpno() == null)
				{
					//该条路由记录没有配置子号，则跳过
					continue;
				}
				//验证是否相包含
				if (port.getCpno().startsWith(subno)
						|| subno.startsWith(port.getCpno()))
				{
					return false;
				}
			}
			return true;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e, "判断分配的尾号是否与路由分配的子号相包含异常。");
			return false;
		}
		
	}
	
	/**
	 * 从Lf_corp表中自动分配一个子号
	 * 2012-8-1
	 * 
	 * @return 从Lf_corp表中自动分配一个子号，并将当前记录更新 String
	 */
	public String getGlobalSubnoAllot(String corpCode)
	{
		// 当前尾号
		String guSubno = "";
		//企业对象
		LfCorp corp;
		// 尾号位数
		Integer digit;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		// 将要更新尾号
		String updateSubno;
		try
		{
			conditionMap.put("corpCode", corpCode);
			corp = empDao.findListBySymbolsCondition(LfCorp.class,
					conditionMap, null).get(0);
			// 获取当前尾号
			guSubno = corp.getCurSubno();
			// 获取位数
			digit = corp.getSubnoDigit();
			// 获取范围最大尾号
			String maxSubno = this.getMaxSubno(digit);
			// 当前尾号在尾号范围之内
			if (Integer.parseInt(guSubno) < Integer.parseInt(maxSubno))
			{
				updateSubno = this.createSubno(new StringBuffer(guSubno),
						guSubno.length()).toString();
			} else
			{
				updateSubno = "0";
			}
			corp.setCurSubno(updateSubno);
			// 将数据库中的当前尾号加1
			empDao.update(corp);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "从Lf_corp表中自动分配一个子号异常。");
		}
		//返回结果
		return guSubno;
	}

	/**
	 * 
	 * 2012-8-1
	 * 
	 * @param digit
	 *            位数（如4位范围就是0000-9999）
	 * @return 最大尾号 String
	 */
	public String getMaxSubno(Integer digit)
	{
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < digit; i++)
		{
			buffer.append("9");
		}
		return buffer.toString();
	}

	/**
	 * 获取尾号详情
	 * @param spUserid
	 * @param codes
	 * @param codeType
	 * @return
	 */
	public List<LfSubnoAllotDetail> getSubnoDetail(String spUserid,
			String codes, Integer codeType)
	{
		//尾号对象
		LfSubnoAllot subnoAllot = null;
		//尾号
		String subno = null;
		//尾号对象详细
		List<LfSubnoAllotDetail> subnoDetailsList = new ArrayList<LfSubnoAllotDetail>();

		try
		{
			//获取尾号对象
			subnoAllot = this.getSubnoAllotByCodes(codes, codeType);
			//循环分配尾号
			subno = this.cycleGetSubno(codes, codeType, subnoAllot);
			//没尾号
			if (subno == null || "".equals(subno))
			{
				//返回
				return null;
			}
			//设置发送账号
			if (spUserid == null || "".equals(spUserid.trim()))
			{
				spUserid = subnoAllot.getSpUser();
			}
			//发送账号不能为空
			if (spUserid == null || "".equals(spUserid.trim()))
			{
				EmpExecutionContext.error("发送账户为空！");
				return null;
			}
			//获取路由对象记录
			List<GtPortUsed> portsList = this.getPortByUserId(spUserid);
			//没记录
			if (portsList == null || portsList.size() == 0)
			{
				EmpExecutionContext.error("没找到发送账户“" + spUserid + "”所绑定的下行路由！");
				//返回
				return null;
			}
			//尾号详细对象
			LfSubnoAllotDetail subnoDetail = null;
			//循环处理路由
			for (int i = 0; i < portsList.size(); i++)
			{
				subnoDetail = new LfSubnoAllotDetail();
				subnoDetail.setSpUser(portsList.get(i).getUserId());
				subnoDetail.setSpgate(portsList.get(i).getSpgate());
				subnoDetail.setSubno(subnoAllot.getSubno());
				subnoDetail.setUsedExtendSubno(subno);
				subnoDetail.setCodes(codes);
				subnoDetail.setCodeType(codeType);
				String cpno = "";
				if (portsList.get(i).getCpno() != null
						&& portsList.get(i).getCpno().trim().length() != 0)
				{
					cpno = portsList.get(i).getCpno();
				}
				subnoDetail.setSpNumber(portsList.get(i).getSpgate() + cpno
						+ subno);
				subnoDetail.setAllotType(subnoAllot.getAllotType());
				subnoDetail.setBusCode(subnoAllot.getBusCode());
				subnoDetail.setSuId(subnoAllot.getSuId());

				subnoDetailsList.add(this
						.addOrUpdateSubnoAllotDetail(subnoDetail));
			}

			subnoAllot.setUsedExtendSubno(subno);
			//更新到数据库
			empDao.update(subnoAllot);

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取尾号详情异常。");
		}
		//返回结果
		return subnoDetailsList;
	}


	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private List<GtPortUsed> getPortByUserId(String userId) throws Exception
	{
		//路由对象集合
		List<GtPortUsed> portsList = null;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//排序
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		//操作员id
		conditionMap.put("userId", userId);
		//路由标识
		conditionMap.put("routeFlag&<>", "2");
		//按条件查询
		portsList = empDao.findListBySymbolsCondition(GtPortUsed.class,
				conditionMap, orderbyMap);
		//返回记录
		return portsList;
	}

	/**
	 * 循环获取尾号
	 * @param codes
	 * @param codeType
	 * @param subnoAllot
	 * @return
	 * @throws Exception
	 */
	private String cycleGetSubno(String codes, Integer codeType,
			LfSubnoAllot subnoAllot) throws Exception
	{
		//尾号对象不能为空
		if (subnoAllot == null)
		{
			return null;
		}
		//尾号
		String subno = null;
		//获取尾号
		subno = this.getFixedSubnoByCodes(codes, codeType);
		if (subno != null && !"".equals(subno))
		{
			//返回
			return subno;
		}

		if (subnoAllot.getAllotType() == 0)
		{
			//返回
			return null;
		}
		//使用中尾号为空
		if (subnoAllot.getUsedExtendSubno() == null
				|| "".equals(subnoAllot.getUsedExtendSubno().trim()))
		{
			subno = subnoAllot.getExtendSubnoBegin();
			//返回
			return subno;
		}
		//
		subno = this.createSubno(
				new StringBuffer(subnoAllot.getUsedExtendSubno()),
				subnoAllot.getUsedExtendSubno().length()).toString();
		boolean result = this.isSubnoLimits(subno, subnoAllot
				.getExtendSubnoBegin(), subnoAllot.getExtendSubnoEnd());
		if (!result)
		{
			subno = subnoAllot.getExtendSubnoBegin();
		}
		//返回
		return subno;
	}

	/**
	 * 获取固定尾号详情
	 */
	private String getFixedSubnoByCodes(String codes, Integer codeType)
			throws Exception
	{
		//尾号
		String subno = null;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//编码
			conditionMap.put("codes", codes);
			//编码类型
			conditionMap.put("codeType", codeType.toString());
			//分配类型
			conditionMap.put("allotType", "0");
			//查询记录
			List<LfSubnoAllotDetail> subnoDetailsList = empDao
					.findListByCondition(LfSubnoAllotDetail.class,
							conditionMap, null);
			//有记录
			if (subnoDetailsList != null && subnoDetailsList.size() > 0)
			{
				//获取第一条记录尾号
				subno = subnoDetailsList.get(0).getUsedExtendSubno();
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取固定尾号详情异常。");
			throw e;
		}
		//返回结果
		return subno;
	}

	/**
	 * 
	 */
	private StringBuffer createSubno(StringBuffer subno, int subnoLength)
			throws Exception
	{
		//尾号长度
		if (subnoLength <= 0)
		{
			subno = subno.append("0");
			//返回
			return subno;
		}
		// 获取尾号字符串最后一位的数字
		int subnoLastNum = Integer.valueOf(String.valueOf(subno
				.charAt(subnoLength - 1)));
		// 如果该数字大于0小于9
		if (subnoLastNum < 9 && subnoLength > 0)
		{
			subnoLastNum += 1;
			subno.setCharAt(subnoLength - 1, String.valueOf(subnoLastNum)
					.toCharArray()[0]);
			//返回尾号
			return subno;
		} else if (subnoLastNum >= 9 && subnoLength > 0)
		{
			subno.setCharAt(subnoLength - 1, '0');
			subnoLength--;
			//创建尾号
			this.createSubno(subno, subnoLength);
		}
		//返回尾号
		return subno;
	}

	/**
	 * 
	 * @param subno
	 * @param extendSubnoBegin
	 * @param extendSubnoEnd
	 * @return
	 * @throws Exception
	 */
	private boolean isSubnoLimits(String subno, String extendSubnoBegin,
			String extendSubnoEnd) throws Exception
	{
		//判断开始结果
		boolean beginResult = this.isSubnoLimitInBegin(subno, 0,
				subno.length(), extendSubnoBegin);
		//判断结束结果
		boolean endResult = this.isSubnoLimitInEnd(subno, 0, subno.length(),
				extendSubnoEnd);
		//返回结果
		return beginResult && endResult;
	}

	/**
	 * 
	 * @param subno
	 * @param index
	 * @param subnoLength
	 * @param extendSubnoBegin
	 * @return
	 * @throws Exception
	 */
	private boolean isSubnoLimitInBegin(String subno, int index,
			int subnoLength, String extendSubnoBegin) throws Exception
	{
		//长度不对
		if (subno.length() < extendSubnoBegin.length())
		{
			//返回
			return false;
		} else if (subno.length() > extendSubnoBegin.length())
		{
			//返回
			return true;
		}
		//相等
		if (index == extendSubnoBegin.length())
		{
			//返回
			return true;
		}
		String tempSigle = subno.substring(index, index + 1);
		String tempBeginSigle = extendSubnoBegin.substring(index, index + 1);
		//对比
		if (tempBeginSigle.compareTo(tempSigle) < 0)
		{
			return true;
		} else if (tempBeginSigle.compareTo(tempSigle) > 0)
		{
			return false;
		} else
		{
			subnoLength--;
			boolean result = this.isSubnoLimitInBegin(subno, index + 1,
					subnoLength, extendSubnoBegin);
			return result;
		}
	}

	/**
	 * 
	 * @param subno
	 * @param index
	 * @param subnoLength
	 * @param extendSubnoEnd
	 * @return
	 * @throws Exception
	 */
	private boolean isSubnoLimitInEnd(String subno, int index, int subnoLength,
			String extendSubnoEnd) throws Exception
	{
		//尾号超过范围
		if (subno.length() > extendSubnoEnd.length())
		{
			//返回
			return false;
		} else if (subno.length() < extendSubnoEnd.length())
		{
			//返回
			return true;
		}

		if (index == extendSubnoEnd.length())
		{
			return true;
		}

		String tempSigle = subno.substring(index, index + 1);
		String tempEndSigle = extendSubnoEnd.substring(index, index + 1);

		if (tempEndSigle.compareTo(tempSigle) > 0)
		{
			return true;
		} else if (tempEndSigle.compareTo(tempSigle) < 0)
		{
			return false;
		} else
		{
			subnoLength--;
			boolean result = this.isSubnoLimitInEnd(subno, index + 1,
					subnoLength, extendSubnoEnd);
			//返回
			return result;
		}
	}

	/**
	 * 根据编码获取尾号对象
	 */
	public LfSubnoAllot getSubnoAllotByCodes(String codes, Integer codeType)
			throws Exception
	{
		//尾号对象
		LfSubnoAllot subnoAllot = null;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//编码
			conditionMap.put("codes", codes);
			//编码类型
			conditionMap.put("codeType", codeType.toString());
			//查询记录
			List<LfSubnoAllot> subnoAllotsList = empDao.findListByCondition(
					LfSubnoAllot.class, conditionMap, null);
			//有记录
			if (subnoAllotsList != null && subnoAllotsList.size() > 0)
			{
				//获取第一条
				subnoAllot = subnoAllotsList.get(0);
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "根据编码获取尾号对象异常。");
			throw e;
		}
		//返回对象
		return subnoAllot;
	}

	/**
	 * 
	 */
	public LfSubnoAllot getSubnoAllotByCodes(String codes, Integer codeType,
			String corpCode) throws Exception
	{
		//尾号对象
		LfSubnoAllot subnoAllot = null;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//编码
			conditionMap.put("codes", codes);
			//类型
			conditionMap.put("codeType", codeType.toString());
			//企业编码
			conditionMap.put("corpCode", corpCode);
			//查询记录
			List<LfSubnoAllot> subnoAllotsList = empDao.findListByCondition(
					LfSubnoAllot.class, conditionMap, null);
			//有记录
			if (subnoAllotsList != null && subnoAllotsList.size() > 0)
			{
				//获取第一条
				subnoAllot = subnoAllotsList.get(0);
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "根据编码获取尾号对象异常。");
			throw e;
		}
		//返回
		return subnoAllot;
	}

	/**
	 * 保存或更新尾号详情
	 */
	private LfSubnoAllotDetail addOrUpdateSubnoAllotDetail(
			LfSubnoAllotDetail subnoAllotDetail) throws Exception
	{
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//编码
			conditionMap.put("codes", subnoAllotDetail.getCodes());
			//编码类型
			conditionMap.put("codeType", subnoAllotDetail.getCodeType()
					.toString());
			//尾号
			conditionMap.put("usedExtendSubno", subnoAllotDetail
					.getUsedExtendSubno());
			// conditionMap.put("subno", subnoAllotDetail.getSubno());
			//执行查询
			List<LfSubnoAllotDetail> subnoDetailsList = empDao
					.findListByCondition(LfSubnoAllotDetail.class,
							conditionMap, null);
			//没记录
			if (subnoDetailsList == null || subnoDetailsList.size() == 0)
			{
				subnoAllotDetail.setCreateTime(Timestamp
						.valueOf((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
								.format(Calendar.getInstance().getTime())));
				//保存到数据库
				Long sudId = empDao.saveObjectReturnID(subnoAllotDetail);
				subnoAllotDetail.setSudId(sudId);
			} else
			{
				//删除记录
				empDao.delete(LfSubnoAllotDetail.class, subnoDetailsList.get(0)
						.getSudId().toString());

				// subnoAllotDetail.setSudId(subnoDetailsList.get(0).getSudId());
				subnoAllotDetail.setUpdateTime(Timestamp
						.valueOf((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
								.format(Calendar.getInstance().getTime())));
				//保存到数据库
				Long sudId = empDao.saveObjectReturnID(subnoAllotDetail);
				subnoAllotDetail.setSudId(sudId);

			}

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "保存或更新尾号详情异常。");
			throw e;
		}
		//返回对象
		return subnoAllotDetail;
	}

	/**
	 * 根据 路由信息 发送账号 尾号 获取对应的全通道号
	 * 
	 * @param portsList
	 * @param spUser
	 * @param subno
	 * @return
	 */
	public Map<Integer, String> getPortUserBySpuser(List<GtPortUsed> portsList,
			String spUser, String subno)
	{
		//通道号
		String spNumber = "";
		//没路由记录
		if (portsList == null || portsList.size() == 0)
		{
			EmpExecutionContext.error("没找到发送账户“" + spUser + "”所绑定的下行路由！");
			//返回
			return null;
		}
		//没尾号
		if (subno == null || "".equals(subno))
		{
			EmpExecutionContext.error("循环尾号为空！");
			return null;
		}
		//尾号
		Map<Integer, String> subnosMap = new HashMap<Integer, String>();
		//循环处理路由
		for (int i = 0; i < portsList.size(); i++)
		{
			//扩展子号
			String cpno = "";

			if (portsList.get(i).getCpno() != null
					&& portsList.get(i).getCpno().trim().length() != 0)
			{
				cpno = portsList.get(i).getCpno();
			}
			//获取通道号
			spNumber = portsList.get(i).getSpgate() + cpno
					+ (subno == null ? "" : subno);
			if (spNumber.length() <= 20 && portsList.get(i).getRouteFlag() == 0)
			{
				subnosMap.put(portsList.get(i).getSpisuncm(), spNumber);
			}
		}
		//返回
		return subnosMap;
	}

	/***
	 * 审批催办用 验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由
	 * @param spUser
	 *            发送账号
	 * @param subno
	 *            循环尾号
	 * @return 1通过 2失败 没有绑定一条上下行路由/下行路由 3失败 发送账号没有绑定路由 4 该发送账号的全通道号有一个超过20位
	 * 5 失败 该SP账号配置的运营商和要发送的手机号码不匹配
	 */
	public String checkPortUsed(String spUser, String subno,int spisuncm)
	{
		String returnMsg = "";
		//全通道号
		String spNumber = "";
		//扩展尾号
		String cpno = "";
		//尾号
		Map<Integer, String> subnosMap = new HashMap<Integer, String>();
		//路由对象
		GtPortUsed portUsed = null;
		List<GtPortUsed> portsList = null;
		//运营商是否匹配
		boolean isPiPei=false;
		try
		{
			SmsBiz smsBiz = new SmsBiz();
			// 获取发送账号的绑定的路由信息
			portsList = smsBiz.getPortByUserId(spUser);
			if (portsList != null && portsList.size() > 0)
			{
				for (int i = 0; i < portsList.size(); i++)
				{
					portUsed = portsList.get(i);
					// 获取该发送账号的全通道号
					if (portUsed.getCpno() != null
							&& portUsed.getCpno().trim().length() > 0)
					{
						cpno = portUsed.getCpno();
						//获取全通道号
						spNumber = portUsed.getSpgate() + cpno + subno;
					} else
					{
						spNumber = portUsed.getSpgate() + subno;
					}
					// 超过20位就跳出
					if (spNumber.length() > 20)
					{
						returnMsg = "4";
						break;
					}
					// 循环出绑定了上下行路由的SPUSER
					if (portUsed.getRouteFlag() == 0
							|| portUsed.getRouteFlag() == 1)
					{
						subnosMap.put(portUsed.getSpisuncm(), spNumber);
					}
				}
				//判断手机号码与通道绑定的运营商是否匹配
				for (int i = 0; i < portsList.size(); i++)
				{
					portUsed = portsList.get(i);
					//有匹配的运营商
					if(portUsed.getSpisuncm().intValue()==spisuncm){
						isPiPei=true;
						break;
					}
				}
                //若存在超过20位的通道则直接返回 否则 判断是否存在绑定上下行路由或下行路由的通道
                if(!"4".equals(returnMsg)){
                    // 该SPUSER没有绑定上下行路由或下行路由的通道
                    if (subnosMap.size() == 0)
                    {
                        returnMsg = "2";
                    } else
                    {
                    	//运营商匹配
                    	if(isPiPei){
                    		returnMsg = "1";
                    	}else{
                    		returnMsg="5";
                    	}
                    }
                }
			} else
			{
				returnMsg = "3";
			}
		} catch (Exception e)
		{
			returnMsg = "errer";
			//异常处理
			EmpExecutionContext.error(e, "验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由异常。");
		}
		//返回
		return returnMsg;
	}
	
	
	/**
	 * 验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由
	 * 
	 * @param spUser
	 *            发送账号
	 * @param subno
	 *            循环尾号
	 * @return 1通过 2失败 没有绑定一条上下行路由/下行路由 3失败 发送账号没有绑定路由 4 该发送账号的全通道号有一个超过20位
	 */
	public String checkPortUsed(String spUser, String subno)
	{
		String returnMsg = "";
		//全通道号
		String spNumber = "";
		//扩展尾号
		String cpno = "";
		//尾号
		Map<Integer, String> subnosMap = new HashMap<Integer, String>();
		//路由对象
		GtPortUsed portUsed = null;
		List<GtPortUsed> portsList = null;
		try
		{
			SmsBiz smsBiz = new SmsBiz();
			// 获取发送账号的绑定的路由信息
			portsList = smsBiz.getPortByUserId(spUser);
			if (portsList != null && portsList.size() > 0)
			{
				for (int i = 0; i < portsList.size(); i++)
				{
					portUsed = portsList.get(i);
					// 获取该发送账号的全通道号
					if (portUsed.getCpno() != null
							&& portUsed.getCpno().trim().length() > 0)
					{
						cpno = portUsed.getCpno();
						//获取全通道号
						spNumber = portUsed.getSpgate() + cpno + subno;
					} else
					{
						spNumber = portUsed.getSpgate() + subno;
					}
					// 超过20位就跳出
					if (spNumber.length() > 20)
					{
						returnMsg = "4";
						break;
					}
					// 循环出绑定了上下行路由的SPUSER
					if (portUsed.getRouteFlag() == 0
							|| portUsed.getRouteFlag() == 1)
					{
						subnosMap.put(portUsed.getSpisuncm(), spNumber);
					}
				}
                //若存在超过20位的通道则直接返回 否则 判断是否存在绑定上下行路由或下行路由的通道
                if(!"4".equals(returnMsg)){
                    // 该SPUSER没有绑定上下行路由或下行路由的通道
                    if (subnosMap.size() == 0)
                    {
                        returnMsg = "2";
                    } else
                    {
                        returnMsg = "1";
                    }
                }
			} else
			{
				returnMsg = "3";
			}
		} catch (Exception e)
		{
			returnMsg = "errer";
			//异常处理
			EmpExecutionContext.error(e, "验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由异常。");
		}
		//返回
		return returnMsg;
	}
	
	/**
	 * @description    	验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由
	 * @param spUser 	发送账号
	 * @param subno  	循环尾号
	 * @return       	1通过 2失败 没有绑定一条上下行路由/下行路由 3失败 发送账号没有绑定路由 4 该发送账号的全通道号有一个超过20位		 
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-22 下午06:51:14
	 */
	public Integer ischeckPortUsed(String spUser, String subno)
	{
		Integer returnMsg = null;
		//全通道号
		String spNumber = "";
		//扩展尾号
		String cpno = "";
		//尾号
		Map<Integer, String> subnosMap = new HashMap<Integer, String>();
		//路由对象
		GtPortUsed portUsed = null;
		List<GtPortUsed> portsList = null;
		try
		{
			SmsBiz smsBiz = new SmsBiz();
			// 获取发送账号的绑定的路由信息
			portsList = smsBiz.getPortByUserId(spUser);
			if (portsList != null && portsList.size() > 0)
			{
				for (int i = 0; i < portsList.size(); i++)
				{
					portUsed = portsList.get(i);
					// 获取该发送账号的全通道号
					if (portUsed.getCpno() != null
							&& portUsed.getCpno().trim().length() > 0)
					{
						cpno = portUsed.getCpno();
						//获取全通道号
						spNumber = portUsed.getSpgate() + cpno + subno;
					} else
					{
						spNumber = portUsed.getSpgate() + subno;
					}
					// 超过20位就跳出
					if (spNumber.length() > 21)
					{
						returnMsg = 4;
						break;
					}
					// 循环出绑定了上下行路由的SPUSER
					if (portUsed.getRouteFlag() == 0
							|| portUsed.getRouteFlag() == 1)
					{
						subnosMap.put(portUsed.getSpisuncm(), spNumber);
					}
					cpno = "";
					spNumber = "";
				}
                //若存在超过20位的通道则直接返回 否则 判断是否存在绑定上下行路由或下行路由的通道
                if(returnMsg == null){
                    // 该SPUSER没有绑定上下行路由或下行路由的通道
                    if (subnosMap.size() == 0)
                    {
                        returnMsg = 2;
                    } else
                    {
                        returnMsg = 1;
                    }
                }
			} else
			{
				returnMsg = 3;
			}
		} catch (Exception e)
		{
			returnMsg = null;
			//异常处理
			EmpExecutionContext.error(e, "验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由异常。");
		}
		//返回
		return returnMsg;
	}

}