package com.montnets.emp.finance.biz;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.ydcw.LfDfadvanced;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.finance.dao.YdcwSpecialDAO;

public class YdcwBiz extends SuperBiz
{
	private IEmpDAO ydcwDAO=new DataAccessDriver().getEmpDAO();
	private YdcwSpecialDAO ydcwSpecialDAO=new YdcwSpecialDAO();
	/**
	 * 获取SP账号密码
	 * @param spUserId
	 * @return
	 * @throws Exception
	 */
	public String getSpPwdBySpUserId(String spUserId) throws Exception {
		Userdata spUser = new Userdata();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();

		conditionMap.put("userId", spUserId);
		//短信SP账号
		conditionMap.put("accouttype","1");
		List<Userdata> tempList = ydcwDAO.findListByCondition(Userdata.class, conditionMap, null);
		if(tempList != null && tempList.size() == 1){
			spUser = tempList.get(0);
		}
		
		return spUser.getUserPassword();
	}

	
	/**
	 * 短信模板
	 * @param busCode
	 * @return
	 * @throws Exception 
	 */
	//获取移动财务短信模板
	public List<LfTemplate> getTemplatesList(String corpCode,String userIdStr,String busCode) throws Exception{
		if(corpCode == null || "".equals(corpCode) || userIdStr == null || "".equals(userIdStr)){
			return null;
		}
		//当前登录账号的guid
		Long userId = Long.parseLong(userIdStr);
		//查询彩信静态模板
		LfTemplate template = new LfTemplate();
		//操作员用户ID
		template.setUserId(userId);
		//查询移动财务模板
		template.setDsflag(3L);
		//查询启用的模板
		template.setTmState(1L);
		template.setBizCode(busCode);
		template.setCorpCode(corpCode);
		List<LfTemplate> lfTemplateList = null;
		try{
			lfTemplateList = ydcwSpecialDAO.getYdcwTemplate(template);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "获取移动财务短信模板异常!");
		}
		return lfTemplateList;
	}
	

	//获取业务类型
	public List<LfBusManager> getBusManagerList(String corpCode)  {
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode&in","0,"+corpCode);
		orderbyMap.put("corpCode", "asc");
		
		//设置启用查询条件
		conditionMap.put("state", "0");
		//设置查询手动和手动+触发
		conditionMap.put("busType&in", "0,2");
		
		List<LfBusManager> busList = null;
		try {
			busList = empDao.findListBySymbolsCondition(LfBusManager.class, conditionMap, orderbyMap);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取业务类型异常!");
		}
		return busList;
		
	}
	
	
	
	/**
	 * DESC: 判断文本文件的编码格式
	 * @param inp 文本文件流
	 * @return 字符集编码
	 */
	public static String get_charset(InputStream objInputStream)
	{
		if(null == objInputStream)
		{
			return null;
		}
		// 默认返回GBK
		String charset = "GBK";
		
		try
		{
			byte[] first3Bytes = new byte[3];
			
			BufferedInputStream objbufInStream = new BufferedInputStream(objInputStream);
			objbufInStream.mark(0);
			// 通过读取文件流的开头标示判断格式
			int read = objbufInStream.read(first3Bytes, 0, 3);
			if(read == -1)
				return charset;
			if(first3Bytes[0] == (byte) 0xFF 
					&& first3Bytes[1] == (byte) 0xFE)
			{
				charset = "Unicode";
			}
			else if(first3Bytes[0] == (byte) 0xFE 
					&& first3Bytes[1] == (byte) 0xFF)
			{
				charset = "UTF-16BE";
			}
			else if(first3Bytes[0] == (byte) 0xEF 
					&& first3Bytes[1] == (byte) 0xBB 
					&& first3Bytes[2] == (byte) 0xBF)
			{
				charset = "UTF-8";
			}
			
			objbufInStream.reset();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "判断文本文件编码格式异常！");
		}
		
		return charset;
	}
	
	/**
	 * 高级设置存为默认
	 * @param conditionMap 删除原来设置条件
	 * @param lfDfadvanced 更新默认高级设置对象
	 * @return
	 */
	public String setDefault(LinkedHashMap<String, String> conditionMap, LfDfadvanced lfDfadvanced){
		String result = "fail";
		Connection conn = null;
		try {
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			
			//删除原有的设置
			empTransDao.delete(conn, LfDfadvanced.class, conditionMap);
			
			//新增默认高级设置信息
			boolean saveResult = empTransDao.save(conn, lfDfadvanced);
			
			//成功
			if(saveResult){
				result = "seccuss";
				empTransDao.commitTransaction(conn);
			}
			else{
				empTransDao.rollBackTransaction(conn);
			}
			return result;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "高级设置存为默认异常！");
			empTransDao.rollBackTransaction(conn);
			return result;
		}
		finally{
			// 关闭连接
			if(conn != null){
				empTransDao.closeConnection(conn);
			}
		}
	}
	
}
