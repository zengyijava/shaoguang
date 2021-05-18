package com.montnets.emp.tailnumber.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.vo.LfSubnoAllotVo;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.tailnumber.vo.dao.GenericLfSubnoAllotVoDAO;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author Administrator
 * 
 */
public class SubnoBiz extends SuperBiz
{

	/**
	 * 更新尾号对象
	 * @param subnoAllot
	 * @return
	 */
	public boolean updateSubnoAllot(LfSubnoAllot subnoAllot)
	{
		//尾号不能为空
		if (subnoAllot == null)
		{
			EmpExecutionContext.error("修改子号分配失败，子号分配对象为空！");
			return false;
		}
		//结果
		boolean result = false;
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		try
		{
			//开始事务
			empTransDao.beginTransaction(conn);

			// LfSubnoAllot beforeSubnoAllot =
			// empDao.findObjectByID(LfSubnoAllot.class, subnoAllot.getSuId());
			//更新到数据库
			empTransDao.update(conn, subnoAllot);
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// if(!beforeSubnoAllot.getSpUser().equals(subnoAllot.getSpUser())){
			conditionMap.clear();
			conditionMap.put("suId", subnoAllot.getSuId().toString());
			//执行删除
			empTransDao.delete(conn, LfSubnoAllotDetail.class, conditionMap);

			// }

			conditionMap.clear();
			conditionMap.put("userId", subnoAllot.getSpUser());
			//获取路由
			List<GtPortUsed> gtList = empDao.findListByCondition(
					GtPortUsed.class, conditionMap, null);
			//尾号详情集合
			List<LfSubnoAllotDetail> details = new ArrayList<LfSubnoAllotDetail>();
			LfSubnoAllotDetail subnoDetailTemp = null;
			//循环处理路由集合
			for (GtPortUsed gpu : gtList)
			{
				subnoDetailTemp = new LfSubnoAllotDetail();
				subnoDetailTemp.setCodes(subnoAllot.getCodes());
				subnoDetailTemp.setCodeType(subnoAllot.getCodeType());
				subnoDetailTemp.setSpUser(subnoAllot.getSpUser());
				subnoDetailTemp.setAllotType(subnoAllot.getAllotType());
				subnoDetailTemp.setUsedExtendSubno(subnoAllot
						.getUsedExtendSubno());
				subnoDetailTemp.setSpgate(gpu.getSpgate());
				String usedExtendSubno = subnoAllot.getUsedExtendSubno() != null ? subnoAllot
						.getUsedExtendSubno().trim()
						: "";
				subnoDetailTemp.setSpNumber(gpu.getSpgate().trim()
						+ gpu.getCpno().trim() + usedExtendSubno);
				subnoDetailTemp.setSuId(subnoAllot.getSuId());
				subnoDetailTemp.setCorpCode(subnoAllot.getCorpCode());

				details.add(subnoDetailTemp);
			}

			if (subnoAllot.getAllotType() == 0)
			{
				empTransDao.save(conn, details, LfSubnoAllotDetail.class);
			}

			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e)
		{
			//异常处理
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "更新尾号对象异常。");
			return false;
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		//返回结果
		return result;
	}

	/**
	 * 删除尾号
	 * @param suId
	 * @return
	 */
	public boolean delSubnoAllot(String suId)
	{
		//子号id不能为空
		if (suId == null || "".equals(suId.trim()))
		{
			EmpExecutionContext.error("删除子号分配失败，子号id为空！");
			return false;
		}
		//结果
		boolean result = false;
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		try
		{
			//开始事务
			empTransDao.beginTransaction(conn);
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("suId", suId);
			//删除尾号详细
			empTransDao.delete(conn, LfSubnoAllotDetail.class, conditionMap);
			//删除尾号
			empTransDao.delete(conn, LfSubnoAllot.class, suId);
			//提交事务
			empTransDao.commitTransaction(conn);
			//结果为成功
			result = true;
		} catch (Exception e)
		{
			//失败回滚事务
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "删除尾号异常。");
			return false;
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		//返回结果
		return result;
	}

	/**
	 * 获取尾号
	 * @param subnoAllotVo
	 * @param pageInfo
	 * @return
	 */
	public List<LfSubnoAllotVo> getSubnoAllotVo(LfSubnoAllotVo subnoAllotVo,
			PageInfo pageInfo)
	{
		//尾号对象集合
		List<LfSubnoAllotVo> subnoVosList = null;
		try
		{
			//按条件获取尾号对象记录
			subnoVosList = new GenericLfSubnoAllotVoDAO().findLfSubnoAllotVo(
					subnoAllotVo, pageInfo);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取尾号异常。");
		}
		//返回结果
		return subnoVosList;
	}

	/**
	 * 通过编码获取尾号详情
	 * @param codes
	 * @param codeType
	 * @return
	 */
	public LfSubnoAllotDetail getSubnoDetailByCode(String codes,
			Integer codeType)
	{
		//尾号详细对象
		LfSubnoAllotDetail subnoDetail = null;
		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//编码
			conditionMap.put("codes", codes);
			//编码类型
			conditionMap.put("codeType", codeType.toString());
			//按条件获取尾号详细记录
			List<LfSubnoAllotDetail> subnoDetailsList = empDao
					.findListByCondition(LfSubnoAllotDetail.class,
							conditionMap, null);
			//有记录
			if (subnoDetailsList != null && subnoDetailsList.size() > 0)
			{
				//获取第一条
				subnoDetail = subnoDetailsList.get(0);
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "通过编码获取尾号详情异常。");
		}
		//返回结果
		return subnoDetail;
	}

	/**
	 * 通过通道号获取尾号
	 * @param spNumber
	 * @return
	 */
	public LfSubnoAllotDetail getSubnoDetailBySpNumber(String spNumber)
	{
		//尾号详细对象
		LfSubnoAllotDetail subnoDetail = null;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//全通道号码
			conditionMap.put("spNumber", spNumber);
			//按条件获取尾号详细记录
			List<LfSubnoAllotDetail> subnosList = empDao.findListByCondition(
					LfSubnoAllotDetail.class, conditionMap, null);
			//有记录
			if (subnosList != null && subnosList.size() != 0)
			{
				//获取第一条
				subnoDetail = subnosList.get(0);
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "通过通道号获取尾号异常。");
		}
		//返回结果
		return subnoDetail;
	}

	/**
	 * 通过编码获取尾号
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
			//按条件获取尾号记录
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
			EmpExecutionContext.error(e, "通过编码获取尾号异常。");
			throw e;
		}
		//返回结果
		return subnoAllot;
	}

	/**
	 * 根据编码获取尾号
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
			//编码类型
			conditionMap.put("codeType", codeType.toString());
			//企业编码
			conditionMap.put("corpCode", corpCode);
			//按条件获取记录
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
			EmpExecutionContext.error(e, "根据编码获取尾号异常。");
			throw e;
		}
		//返回结果
		return subnoAllot;
	}

	/**
	 * 更具登录id获取尾号
	 * @param loginId
	 * @return
	 * @throws Exception
	 */
	public LfSubnoAllot getSubnoByLoginId(String loginId) throws Exception
	{
		//尾号对象
		LfSubnoAllot subno = null;
		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//登录id
			conditionMap.put("loginId", loginId);
			conditionMap.put("shareType", "2");
			//按条件查询
			List<LfSubnoAllot> subnosList = empDao.findListByCondition(
					LfSubnoAllot.class, conditionMap, null);
			//有记录
			if (subnosList != null && subnosList.size() == 1)
			{
				//获取第一条
				subno = subnosList.get(0);
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "更具登录id获取尾号异常。");
			throw e;
		}
		//返回结果
		return subno;
	}

	/**
	 * 根据机构id获取尾号
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public LfSubnoAllot getSubnoByDepId(Long depId) throws Exception
	{
		//尾号对象
		LfSubnoAllot subno = null;
		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//结构编码
			conditionMap.put("depId", depId.toString());
			conditionMap.put("shareType", "1");
			//按条件获取记录
			List<LfSubnoAllot> subnosList = empDao.findListByCondition(
					LfSubnoAllot.class, conditionMap, null);
			//有记录
			if (subnosList != null && subnosList.size() == 1)
			{
				//获取第一条
				subno = subnosList.get(0);
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "根据机构id获取尾号异常。");
			throw e;
		}
		//返回结果
		return subno;
	}

	/**
	 * 判断尾号是否存在
	 * @param subno
	 * @param shareType
	 * @return
	 * @throws Exception
	 */
	public boolean isSubnoExists(String subno, Integer shareType)
			throws Exception
	{
		//尾号不能为空，类型不能为空
		if (subno == null || shareType == null)
		{
			//返回
			return true;
		}
		//结果
		boolean result = true;
		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//尾号
			conditionMap.put("subno", subno);
			//类型
			conditionMap.put("shareType", shareType.toString());
			//按条件获取记录
			List<LfSubnoAllot> subnosList = empDao.findListByCondition(
					LfSubnoAllot.class, conditionMap, null);
			//有记录
			if (subnosList == null || subnosList.size() == 0)
			{
				//返回
				result = false;
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "判断尾号是否存在异常。");
			throw e;
		}
		//返回结果
		return result;
	}

	/**
	 * 判断尾号是否已绑定
	 * @param id
	 * @param shareType
	 * @return
	 * @throws Exception
	 */
	public boolean isBindedExists(String id, Integer shareType)
			throws Exception
	{
		//id不能为空，类型不能为空
		if (id == null || "".equals(id.trim()) || shareType == null)
		{
			//返回
			return true;
		}
		//结果
		boolean result = true;
		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			if (shareType == 1)
			{
				//机构id
				conditionMap.put("depId", id);
			} else if (shareType == 2)
			{
				//登录id
				conditionMap.put("loginId", id);
			}
			//按条件获取记录
			List<LfSubnoAllot> subnosList = empDao.findListByCondition(
					LfSubnoAllot.class, conditionMap, null);
			//没记录
			if (subnosList == null || subnosList.size() == 0)
			{
				//返回结果
				result = false;
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "判断尾号是否已绑定异常。");
			throw e;
		}
		//返回结果
		return result;
	}

	/**
	 * 获取模块编码
	 * @param menuName
	 * @param menuCode
	 * @return
	 * @throws Exception
	 */
	public String getMenuCode(String menuName, String menuCode)
			throws Exception
	{
		//模块编码
		String menuCodes = "";
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		List<LfPrivilege> lfPrivilegeList = new ArrayList<LfPrivilege>();
		List<String> distinctFieldList = new ArrayList<String>();
		//模块名称
		conditionMap.put("menuName&like", menuName);
		//模块编码
		conditionMap.put("menuCode&in", menuCode);
		distinctFieldList.add("menuCode");
		try
		{
			//获取记录
			lfPrivilegeList = empDao.findDistinctListBySymbolsCondition(
					LfPrivilege.class, distinctFieldList, conditionMap, null);
			//有记录
			if (null != lfPrivilegeList && 0 != lfPrivilegeList.size())
			{
				//循环处理记录
				for (LfPrivilege lfPrivilege : lfPrivilegeList)
				{
					menuCodes += lfPrivilege.getMenuCode() + ",";
				}
				if (0 != menuCodes.length() && menuCodes.contains(","))
				{
					menuCodes = menuCodes.substring(0, menuCodes
							.lastIndexOf(","));
				}
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取模块编码异常。");
			throw e;
		}
		//返回结果
		return menuCodes;
	}

	/**
	 * 检查尾号
	 * @param spUser
	 * @param usedExtendSubno
	 * @return
	 */
	public boolean checkSubno(String spUser, int subnoLength)
	{

		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//发送账号
			conditionMap.put("userId", spUser);
			//获取路由记录
			List<GtPortUsed> portsList = empDao.findListByCondition(
					GtPortUsed.class, conditionMap, null);

			//没记录
			if (portsList == null || portsList.size() == 0)
			{
				//返回
				return false;
			}

			// List<GtPortUsed> portsTemp = new ArrayList<GtPortUsed>();
			// GtPortUsed portTemp = null;
			Integer maxLen = null;
			if (portsList.size() > 0)
			{
				//计算最大长度
				maxLen = portsList.get(0).getSpgate().trim().length()
						+ portsList.get(0).getCpno().trim().length();
				Integer temp = null;
				//循环路由记录，并处理
				for (int i = 0; i < portsList.size(); i++)
				{
					if (portsList.get(i) == null)
					{
						break;
					}
					temp = portsList.get(i).getSpgate().trim().length()
							+ portsList.get(i).getCpno().trim().length();
					if (maxLen < temp)
					{
						maxLen = temp;
					}
				}

			}

			// GtPortUsed port = portsList.get(0);
			//长度小于21位
			if (maxLen + subnoLength <= 21)
			{
				//返回
				return true;
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "检查尾号异常。");
			return false;
		}
		//返回结果
		return false;
	}

	/**
	 * 新增 操作员的时候对 子号的处理
	 * 
	 * @return
	 */
	public String doSysuserSubno(String corpCode, Connection conn, String guid)
	{
		//尾号对象集合
		List<LfSubnoAllot> allots = null;
		//尾号对象
		LfSubnoAllot allot = null;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//企业编码
		conditionMap.put("corpCode", corpCode);
		//编码
		conditionMap.put("codes", StaticValue.IM_CZYWHGL_MENUCODE);
		//编码类型
		conditionMap.put("codeType", "0");
		String returnMsg = "0";
		try
		{
			//按条件获取记录
			allots = empDao.findListByCondition(LfSubnoAllot.class,
					conditionMap, null);
			//有记录
			if (allots != null && allots.size() > 0)
			{
				//获取第一条
				allot = allots.get(0);
				conditionMap.clear();
				conditionMap.put("userId", allot.getSpUser());
				//获取路由记录
				List<GtPortUsed> gpList = empDao.findListByCondition(
						GtPortUsed.class, conditionMap, null);
				//有路由记录
				if (gpList != null && gpList.size() == 3)
				{
					Long userExtendSubno = Long.valueOf(allot
							.getUsedExtendSubno());
					Long endSubno = Long.valueOf(allot.getUsedExtendSubno());
					if (userExtendSubno > endSubno)
					{
						// 子号已经满
						returnMsg = "1"; 
					} else
					{
						LfSubnoAllotDetail subnoDetailTemp = null;
						List<LfSubnoAllotDetail> details = new ArrayList<LfSubnoAllotDetail>();
						for (GtPortUsed gpu : gpList)
						{
							subnoDetailTemp = new LfSubnoAllotDetail();
							subnoDetailTemp.setCodes(allot.getCodes());
							subnoDetailTemp.setCodeType(allot.getCodeType());
							subnoDetailTemp.setSpUser(allot.getSpUser());
							subnoDetailTemp.setAllotType(allot.getAllotType());
							subnoDetailTemp.setUsedExtendSubno(allot
									.getUsedExtendSubno());
							subnoDetailTemp.setCorpCode(corpCode);
							// lfSubnoAllotDetail.setSpNumber(gpu.getSpgate());
							subnoDetailTemp.setSpgate(gpu.getSpgate());
							subnoDetailTemp.setSpNumber(gpu.getSpgate().trim()
									+ gpu.getCpno().trim()
									+ allot.getUsedExtendSubno().trim());
							subnoDetailTemp.setSuId(allot.getSuId());
							subnoDetailTemp.setCreateTime(new Timestamp(System
									.currentTimeMillis()));
							subnoDetailTemp.setLoginId(guid);
							details.add(subnoDetailTemp);
						}
						Long subno = Long.valueOf(userExtendSubno + 1);
						allot.setUsedExtendSubno(String.valueOf(subno));
						empTransDao.update(conn, allot);
						empTransDao.save(conn, details,
								LfSubnoAllotDetail.class);
						returnMsg = "3";
					}
				} else
				{
					// 路由没有绑定完3个上下行
					returnMsg = "4"; 
				}
			} else
			{
				// 没有绑定子号管理
				returnMsg = "2"; 
			}
		} catch (Exception e)
		{
			//异常处理
			returnMsg = "0";
			EmpExecutionContext.error(e, "新增操作员的时候对子号的处理异常。");
		}
		//返回结果
		return returnMsg;
	}

}
