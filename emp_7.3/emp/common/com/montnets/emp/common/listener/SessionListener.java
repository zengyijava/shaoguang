package com.montnets.emp.common.listener;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.*;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.LoginBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.LoginInfo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.monitoronline.LfMonOnlcfg;
import com.montnets.emp.entity.monitoronline.LfMonOnluser;
import com.montnets.emp.util.SuperOpLog;

/**
 * 监听session
 * @author Administrator
 *
 */
public class SessionListener implements HttpSessionListener,HttpSessionAttributeListener {

	//待删除集合，大于等于10，批量处理过期session
	private static List<String> delKeyList = new CopyOnWriteArrayList<String>();
	/**
	 * session创建是调用
	 */
	public void sessionCreated(HttpSessionEvent arg0) {

	}

	/**
	 * 销毁session是执行
	 */
	public void sessionDestroyed(HttpSessionEvent arg0) {
		try
		{
            HttpSession session = arg0.getSession();
			String sessionid = session.getId();
			//系统用户登录信息Map
			Map<String,LoginInfo> loginMap = StaticValue.getLoginInfoMap();
			Iterator<String> itr = loginMap.keySet().iterator();
			String key;
			
			String removeKey = null;
			while(itr.hasNext())
			{
				key=itr.next();
				LoginInfo logininfo = loginMap.get(key);
				
				//判断销毁的sessinid是否是登录用户的登录sessionid
				if(sessionid.equals(logininfo.getSessionId()))
				{
					removeKey = key;
					break;
				}
			}
			if(removeKey != null)
			{
                String logoutType = "主动";
                //距上次访问会话到当前时间毫秒数
                long time =System.currentTimeMillis() - session.getLastAccessedTime();
                //大于超时时间或2分钟 认为是超时退出
                if(time >= session.getMaxInactiveInterval()*1000L || time >= 2*60*1000L){
                    logoutType = "超时";
                }
                //记录退出操作日志
                LoginInfo loginInfo = StaticValue.getLoginInfoMap().get(removeKey);
                EmpExecutionContext.info("销毁session中，会话持有操作员username:"+loginInfo.getUserName()+",corpcode:"+loginInfo.getCorpCode());
                new SuperOpLog().logSuccessString(loginInfo.getUserName(), "退出", StaticValue.OTHER, logoutType+"退出时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "，ip地址：" + loginInfo.getLoginIP(), loginInfo.getCorpCode());
				//放入到待删除
				delKeyList.add(removeKey);
				try
				{
					// 是否加载监控模块，16：监控
					if(new SmsBiz().isWyModule("16"))
					{
						BaseBiz baseBiz = new BaseBiz();
						//删除在线用户详情
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						conditionMap.put("sesseionid", sessionid);
						baseBiz.deleteByCondition(LfMonOnluser.class, conditionMap);
						// 设置在线用户详情
						LoginBiz loginBiz = new LoginBiz();
						loginBiz.setOnlineUser();
						//更新在线用户人数
						loginBiz.setMonOnlcfg();
						List<LfMonOnlcfg> lfMonOnlcfgList = baseBiz.getByCondition(LfMonOnlcfg.class, null, null);
						if(lfMonOnlcfgList != null && lfMonOnlcfgList.size() > 0)
						{
							synchronized (StaticValue.getMonOnlinecfg())
							{
								// 在线用户数
								StaticValue.getMonOnlinecfg().setOnlinenum(lfMonOnlcfgList.get(0).getOnlinenum());
							}
						}
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "会话失效，清除在线用户异常！");
				}
			}
			
			//待删除大于等于10
			if(delKeyList.size() >= 10)
			{
				Iterator<String> it = delKeyList.iterator();
				String delKey = "";
				int count = 0;
				while(it.hasNext())
				{
					delKey = it.next();
					//删除过期session
					StaticValue.getLoginInfoMap().remove(delKey);
					//删除待删除集合对应记录
					delKeyList.remove(delKey);
					count++;
				}
				EmpExecutionContext.info("清除过期session记录"+count+"条，在线用户数："+ StaticValue.getLoginInfoMap().size());
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "销毁失效session失败！");
		}
	}

	/**
	 * 当一个attribute被增加到session后执行这个方法
	 */
	public void attributeAdded(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub

	}

	/**
	 * 当一个attribute被从session中移除后执行这个方法
	 */
	public void attributeRemoved(HttpSessionBindingEvent se) {
	}

	public void attributeReplaced(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub
	}

}
