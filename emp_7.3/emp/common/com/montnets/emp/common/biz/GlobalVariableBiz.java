package com.montnets.emp.common.biz;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.SMParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;

/**
 * 
 * @author Administrator
 *
 */
public class GlobalVariableBiz
{
	//类实例
	private static volatile GlobalVariableBiz instance;
	//尾号管理biz
	SubnoManagerBiz subnoManagerBiz =new SubnoManagerBiz();;
	//数据库操作dao
	private IEmpDAO genericBaseDAO= new DataAccessDriver().getEmpDAO();
	
	//索引0为taskId，索引1为guid
	//private static long[] n = {0,0};
	
	//索引0为taskId，索引1为guid
	private static long[] endValue = {0,0};
	
	//索引0为taskId，索引1为guid
	private static long[] curValue = {0,0};
	
	/**
	 * 初始化方法
	 */
	private GlobalVariableBiz()
	{
		
	}

	/**
	 * 请将相应的参数设进SMParams对象
	 * 2012-8-2
	 * @param //spUserid 发送账号 可以为null
	 * @param //codes 编码（0模块编码;1业务编码;2产品编码;3机构id;4操作员guid;5任务id）
	 * @param //codeType 编码类型
	 * @param //corpCode 企业编码
	 * @param //allotType 分配类型(0固定，1自动(有效期为7天),null表示永久尾号)
	 * @param //conn 数据库连接 可以为null
	 * @return 子号详情记录
	 * @throws Exception
	 * LfSubnoAllotDetail
	 */
	public synchronized LfSubnoAllotDetail getSubnoDetail(SMParams smParams, ErrorCodeParam errorCode) throws Exception
	{
		//获取尾号详细对象
		return subnoManagerBiz.getSubnoDetail(smParams, errorCode);
	}
	/**
	 * 
	 * 2012-8-3
	 * @param codes 编码
	 * @param codeType 编码类型(0模块编码;1业务编码;2产品编码;3机构id;4操作员guid;5任务id)
	 * @param corpCode 企业编码
	 * @return
	 * String
	 */
	public synchronized String getValidSubno(String codes,Integer codeType,String corpCode, ErrorCodeParam errorCode)
	{
		//获取有效尾号
		return  subnoManagerBiz.getValidSubno(codes,codeType,corpCode, errorCode);
	}
	
	/**
	 * 获取taskid或guid
	 * @param key taskId,guid
	 * @param count 步长
	 * @return 返回值
	 */
	public synchronized Long getValueByKey(String key, long count) {
		//全局变量名称不能为空
		if(key == null || "".equals(key.trim()))
		{
			EmpExecutionContext.error("获取全局变量ID，未传入全局变量名称！");
			//返回
			return null;
		}
		
		if(count < 1L)
		{
			EmpExecutionContext.error("获取全局变量"+key+"，传入获取总数异常，count:"+count);
			//返回null
			return null;
		}
		
		//索引0为taskId，索引1为guid
		int index = -1;
		try
		{
			//taskId
			if("taskId".equals(key))
			{
				index = 0;
			}
			else if("guid".equals(key))
			{
				//guid
				index = 1;
			}
			//未定义的全局变量
			if(index == -1)
			{
				EmpExecutionContext.error("获取全局变量ID，未定义的全局变量！key="+key);
				return null;
			}
			//加上请求需要的数量
			curValue[index] += count;
			//超过内存中值，则重新从库中取
			if(curValue[index] >= endValue[index] )
			{
				EmpExecutionContext.info("获取"+key+" 从数据库取值 前，当前值："+curValue[index]+"，获取总数："+count);
				//从数据库中取值
				boolean dbr = this.getDBValueByKey(index, key, count);
				if(!dbr)
				{
					EmpExecutionContext.error("获取全局变量ID，从数据库获取"+key+"值失败。");
					return 0l;
				}
				
				EmpExecutionContext.info("获取"+key+" 从数据库取值 后，当前值："+curValue[index]+"，获取总数："+count);

			}
			//分配的值
			long resultId = curValue[index];
			if("taskId".equals(key))
			{
				//taskId = taskid*10+一位随机数
				resultId = (resultId * 10) + (int)(Math.random() * 10);
			}
			EmpExecutionContext.info("生成全局可用"+key+"："+resultId+"，获取总数："+count);
			//返回分配的值
			return resultId;
		}catch(Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取全局变量ID，获取"+key+"异常，获取总数："+count);
			return null;
		}
		
	}
	
	/**
	 * 从数据库中取变量值
	 * @param key 变量
	 * @param count 取的数量
	 */
	private boolean getDBValueByKey(int type, String key, long count) {
		
		try{
			//行id。1-taskId; 2-guid; 3-serialNum; 4-orderCode; 5-tableNum; 6-batchNumber;
			int fildId = 0;
			if(type ==0)
			{
				//taskId
				fildId = 1;
			}else if(type == 1)
			{
				//guid
				fildId = 2;
			}
			long value = genericBaseDAO.getIdByPro(fildId, StaticValue.MAXTASKIDCOUNT + Integer.valueOf(String.valueOf(count)));
			
			if(value == 0)
			{
				return false;
			}
			
			//重新赋值
			curValue[type] = value - StaticValue.MAXTASKIDCOUNT;
			
			//设置结束值
			endValue[type] = value;
			
			return true;
			
		}catch(Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "从数据库中取变量值异常。");
			return false;
		}
		
	}
	
	/**
	 * 获取自增值
	 * @param fildId 行id。1-taskId; 2-guid; 3-serialNum; 4-orderCode; 5-tableNum; 6-batchNumber;
	 * @param count 步长
	 * @return 返回自增
	 */
	private int getIniId(int fildId, int count)
	{
		Connection conn = null;
		CallableStatement cstmt = null;
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			String procedure = "{call PRO_GET_INT_ID(?,?,?)}";
			cstmt = conn.prepareCall(procedure);
			cstmt.setInt(1, fildId);
			cstmt.setInt(2, count);
			cstmt.registerOutParameter(3, Types.INTEGER);
			
			cstmt.execute();
			return cstmt.getInt(3);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "调用存储过程获取自增值异常。");
			return 0;
		} finally
		{
			try
			{
				if (cstmt != null)
				{
					cstmt.close();
				}
				if (conn != null)
				{
					conn.close();
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "调用存储过程获取自增值后，关闭数据库资源异常。");
			}
		}
	}
	
	private char[] chars=null;
	
	private int jj=0;
	
	
	/**
	 * 生成审批指令的方法（4位字母）
	 * @return
	 */
	public synchronized String getNewNodeCode(){
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//审批指令字符
		conditionMap.put("globalKey", "orderCode");
		List<LfGlobalVariable> gvsList;
		String globalValue="";
		String newGlobalValue=null;
		LfGlobalVariable gv=null;
		try {
			gvsList = genericBaseDAO.findListByCondition(LfGlobalVariable.class, conditionMap, null);
			if(gvsList !=null && gvsList.size() > 0){
				gv = gvsList.get(0);
				if(gv!=null&&gv.getGlobalStrValue()!=null){
					globalValue=gv.getGlobalStrValue();
				}
			}
			if("".equals(globalValue)){
				return newGlobalValue;
			}
			//转换成字符数组
			chars=globalValue.toCharArray();
			//数组长度大于1
			if(chars.length>1){
				int c=(int)chars[chars.length-1];
				//当字符不是z时
				if(c!=122){
//					if(c!=57){
//						c+=1;
//					}
//					else{
//						c=97;
//					}
					//加1
					c+=1;
					chars[chars.length-1]=(char)c;
					newGlobalValue= String.valueOf(chars);
				}else{
					//当字符为z是，进以为，眼来位置的字符改为a
					chars[chars.length-1]='a';
					//循环
					updateChar(chars.length-2);
					if(jj!=0){
						newGlobalValue= "b"+String.valueOf(chars);
					}else{
						newGlobalValue= String.valueOf(chars);
					}
				}
					
			}else{
				int c=(int)chars[0];
				//如果不是字母，将字符初始化为a字符
				if(c<97)
				{
					c=97;
				}
				//如果不是z
				if(c!=122){
//					if(c!=57){
//						c+=1;
//					}else{
//						c=97;
//					}
					//加1
					c+=1;
					//拼接
					newGlobalValue= (char)c+"";
				}else{
					//如果是z,进一位，原来位置的字符变为a
					c=97;
					newGlobalValue= "b"+(char)c;
				}
			}
			//为空判断
			if(newGlobalValue!=null&&!"".equals(newGlobalValue)){
				gv.setGlobalStrValue(newGlobalValue);
				//修改数据库里的对应信息
				this.genericBaseDAO.update(gv);
			}
			if (newGlobalValue != null && newGlobalValue.length()<4)
			{
				String st="";
				for(int i=0;i<4-newGlobalValue.length();i++)
				{
					st="a"+st;
				}
				newGlobalValue=st+newGlobalValue;
			}
			//控制字符串为4位数
			if(newGlobalValue == null){
				newGlobalValue = "";
			}
			if(newGlobalValue.length()>4||"zzzz".equals(newGlobalValue))
			{
				newGlobalValue="a";
				gv.setGlobalStrValue(newGlobalValue);
				this.genericBaseDAO.update(gv);
				newGlobalValue="aaaa";
			}
			return newGlobalValue.toUpperCase();
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "生成审批指令的方法异常。");
			return null;
		}
	}
	
	/**
	 * 
	 * 修改字符
	 */
	private void updateChar(int length){
		int c=(int)chars[length];
		//如果不是z
		if(c!=122){
			//加1
			c+=1;
//			if(c!=57){
//				c+=1;
//			}else{
//				c=97;
//			}
			chars[length]=(char)c;
		}else{
			//如果是z,进一位，原来位置的字符变为a
			c=97;
			chars[length]=(char)c;
			if(length==0){
				jj=1;
			}
			length--;
			if(length>-1){
				//修改相应位置的字符
				updateChar(length);
			}
			
		}
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public static GlobalVariableBiz getInstance()
	{
		//实例为空，则重新初始化
		if (instance == null)
		{
			synchronized (GlobalVariableBiz.class)
			{
				if (instance == null)
				{
					//初始化实例
					instance = new GlobalVariableBiz();
				}
			}
		}
		//返回实例
		return instance;
	}
	
}
