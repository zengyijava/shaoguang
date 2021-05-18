package com.montnets.emp.monmanage.biz.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.entity.monitor.MMonSproce;
import com.montnets.emp.monitor.constant.MonDproceParams;
import com.montnets.emp.util.PageInfo;

/**
 * 程序监控管理接口
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-26 下午04:57:00
 */
public interface IPrcMonCfgBiz
{

	/**
	 * 获取程序信息
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin 
	 * @datetime 2013-11-27 上午09:48:17
	 */
	public List<DynaBean> getSprc(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap);
	
	/**
	 * 获取主机信息
	 * @description    
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午09:48:17
	 */
	public List<DynaBean> getHost();
	
	
	/**
	 * 添加程序信息
	 * @description    
	 * @param sprc 程序基本信息
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午03:08:36
	 */
	public boolean addPrc(MMonSproce sprc);
	
	/**
	 * 修改程序信息
	 * @description    
	 * @param sprc 程序信息
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午03:08:36
	 */
	public boolean editPrc(LfMonSproce sprc,String mqurl);
	
	/**
	 * 获取mq的配置信息
	 * @description    
	 * @return       			 
	 * @author zhangmin 
	 * @datetime 2013-11-27 上午09:48:17
	 */
	public List<DynaBean> getMqMsg();
	
	/**
	 * 逻辑删除程序
	 * @description    
	 * @param hostid
	 * @param procid
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-1-17 下午05:53:24
	 */
	public boolean delete(Long proceid);
	
	public MonDproceParams copyBy(LfMonSproce monSproce);
	
	public void updateBy(MonDproceParams monDproceParams ,LfMonSproce monSproce);
}
