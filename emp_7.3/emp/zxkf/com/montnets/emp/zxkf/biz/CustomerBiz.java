package com.montnets.emp.zxkf.biz;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxsysuser.LfSysUser;
import com.montnets.emp.ottbase.util.MD5;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.zxkf.dao.CustomerDao;

public class CustomerBiz extends SuperBiz{
	private CustomerDao	customerDao	= new CustomerDao();
	
	/**
	 * 获取在线客服列表
	 * @author fyt
	 *
	 */
	public List<DynaBean> findCustomerList(String corpcode,LinkedHashMap<String,String> conditionMap,PageInfo pageInfo){
		List<DynaBean> beans = null;
		try{
			beans = customerDao.findCustomerList(corpcode,conditionMap,pageInfo);
		}catch(Exception e){
			EmpExecutionContext.error(e,"findCustList-获取客服列表失败！");
		}
		return beans;
	}
	
	/**
	 * 创建客服帐号/编辑客服帐号
	 * @description    
	 * @param corpcode(企业编码)
	 * @param aId(所属公众帐号)
	 * @param userName(登录名)
	 * @param ptype(身份-管理员|客服)
	 * @param status(启用/禁用)
	 * @param pwd(密码)
	 * @return       			 
	 * @author Administrator <foyoto@gmail.com>
	 * @datetime 2013-12-10 上午10:23:09
	 */
	public Long createCustomerReturnId(String corpcode,String aId,String name,String userName,String ptype,String state,String pwd){
		Long resultId = null;
		try{
		LfSysUser otSysUser = new LfSysUser();
		otSysUser.setAId(Long.valueOf(aId));
		otSysUser.setCorpCode(corpcode);
		otSysUser.setPermissionType(Integer.valueOf(ptype));
		otSysUser.setUserState(Integer.valueOf(state));
		otSysUser.setName(name);
		otSysUser.setUserName(userName);
		// 设置日期
		otSysUser.setRegTime(new Timestamp(System.currentTimeMillis()));
	    Long tempId = empDao.getIdByPro(19, 1);
	    otSysUser.setUserId(tempId);
	    //设置密码
	    otSysUser.setPassword(MD5.getMD5Str(pwd+userName.toLowerCase()));
	    
		// 保存客服对象
	    resultId = empDao.saveObjectReturnID(otSysUser);
		}catch(Exception e){
			EmpExecutionContext.error(e,"createOtSysUser-创建客服失败！");
		}
		return resultId;
	}
	
	/**
     * 创建客服帐号/编辑客服帐号
     * @description    
     * @param corpcode(企业编码)
     * @param aId(所属公众帐号)
     * @param userName(登录名)
     * @param ptype(身份-管理员|客服)
     * @param status(启用/禁用)
     * @param pwd(密码)
     * @return                   
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2013-12-10 上午10:23:09
     */
    public boolean updateCustomerReturnId(String aId,String custId, String name, String userName,String ptype,String state){
        Boolean resultId = null;
        try{
            if(custId!=null&&!"".equals(custId)){
                LfSysUser otSysUser = empDao.findObjectByID(LfSysUser.class, Long.valueOf(custId));
                otSysUser.setPermissionType(Integer.valueOf(ptype));
                otSysUser.setUserState(Integer.valueOf(state));
                otSysUser.setName(name);
                otSysUser.setUserName(userName);
                
                // 保存客服对象
                resultId = empDao.update(otSysUser);
            }else {
                resultId = false; 
            } 
           
        }catch(Exception e){
            EmpExecutionContext.error(e,"updateCustomerReturnId-更新客服信息失败！");
        }
        return resultId;
    }
    
    public boolean resetCustomerPwdReturnId(String custId,String pwd){
        Boolean resultId = null;
        try{
            if(custId!=null&&!"".equals(custId)){
                LfSysUser otSysUser = empDao.findObjectByID(LfSysUser.class, Long.valueOf(custId));
                // 设置密码
                otSysUser.setPassword(MD5.getMD5Str(pwd+otSysUser.getUserName().toLowerCase()));
                
                // 保存客服对象
                resultId = empDao.update(otSysUser);
            }else {
                resultId = false; 
            } 
           
        }catch(Exception e){
            EmpExecutionContext.error(e,"updateCustomerReturnId-更新客服信息失败！");
        }
        return resultId;
    }
    
    /**
     * 加载客服所服务人员的名单
     * @description    
     * @param owerId 客服ID
     * @return
     * @throws Exception       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-23 上午09:55:40
     */
    public JSONObject findCustomerToClient(String owerId) throws Exception
    {
        return customerDao.findCustomerToClient(owerId);
    }
	
}



