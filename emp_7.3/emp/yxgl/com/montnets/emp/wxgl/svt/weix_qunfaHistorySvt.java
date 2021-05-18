package com.montnets.emp.wxgl.svt;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiSendlog;
//import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.util.PageInfo;

@SuppressWarnings("serial")
public class weix_qunfaHistorySvt extends BaseServlet
{

    private final static String PATH = "/wxgl/qunfa";
    //调用znly中，ottbase包中的BaseBiz
    //BaseBiz                     baseBiz     = new BaseBiz();
    //调用公用的BaseBiz
    private final BaseBiz                     baseBiz = new BaseBiz();
    
    
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String lgcorpcode = request.getParameter("lgcorpcode");
        PageInfo pageInfo = new PageInfo();
        // 是否首次请求
        boolean isFirstEnter;
        
        //添加与日志相关
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
        
        try
        {
            isFirstEnter = pageSet(pageInfo, request);
            
            // 查询所有公众帐号
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            List<LfWeiAccount> otWeiAcctList = baseBiz.findListByCondition(LfWeiAccount.class, conditionMap, null);
            request.setAttribute("otWeiAcctList", otWeiAcctList);
            
            conditionMap.clear();
            if(!isFirstEnter)
            {
                String aId = request.getParameter("aId");
                if(null!=aId&&!"".equals(aId)){
                    conditionMap.put("aId", aId);
                }
            }
            LinkedHashMap<String, String> orderbyMap  =  new LinkedHashMap<String, String>();
            orderbyMap.put("createtime", "DESC");
            List<LfWeiSendlog> logList = baseBiz.getByConditionNoCount(LfWeiSendlog.class, null, conditionMap, orderbyMap, pageInfo);
            request.setAttribute("logList", logList);
        }
        catch(Exception e)
        {
            EmpExecutionContext.error(e, "用户群发页面加载失败！");
        }
        finally
        {
        	

        	//添加与日志相关 p
			long endTimeByLog = System.currentTimeMillis();  //查询结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时
			
			//增加操作日志 p
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
				
				EmpExecutionContext.info("微信群发历史", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent, "GET");
			}
        	
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(PATH + "/yxgl_sendHistory.jsp").forward(request, response);
        }
    }
}
