/**
 * @description 在线客服 - 客服系统
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-20 下午05:46:26
 */
package com.montnets.emp.custrep.svt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.json.simple.JSONObject;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.custrep.biz.CustRepBiz;
import com.montnets.emp.entity.online.LfOnlServer;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.entity.wxsysuser.LfSysUser;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.util.PageInfo;

/**
 * @description 在线客服 - 客服系统 - 服务时长
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-20 下午05:46:26
 */

public class curp_serverTimeSvt extends BaseServlet
{

    /**
     * @description serialVersionUID
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-26 下午03:23:45
     */
    private static final long   serialVersionUID = 7476938938044346316L;

    // 资源路径
    private static final String PATH             = "/zxkf/custrep/";

    private final BaseBiz                     baseBiz          = new BaseBiz();

    /**
     * 进入 在线客服 - 客服系统 - 客户评价 查询页面
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-23 上午10:00:42
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        PageInfo pageInfo = new PageInfo();
        boolean isFirstEnter = pageSet(pageInfo, request);
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try {
            List<LfWeiAccount> otWeiAccList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);
            String aid = request.getParameter("aid");
            if(!"".equals(aid) && aid != null)
            {
                conditionMap.put("AId", aid);
            }
            List<LfSysUser> otSysUserList = baseBiz.getByCondition(LfSysUser.class, conditionMap, null);
            
            if(!isFirstEnter)
            {
                String cusid = request.getParameter("cusid");
                if(!"".equals(cusid) && cusid != null)
                {
                    conditionMap.put("customeId", cusid);
                }
                String enddate = request.getParameter("enddate");
                if(!"".equals(enddate) && enddate != null)
                {
                    conditionMap.put("enddate", enddate);
                }
                String startdate = request.getParameter("startdate");
                if(!"".equals(startdate) && startdate != null)
                {
                    conditionMap.put("startdate", startdate);
                }
            }
            CustRepBiz custBiz = new CustRepBiz();
            List<DynaBean> resultList = custBiz.getServerTime(conditionMap, pageInfo); 
            request.setAttribute("resultList", resultList);
            
            request.setAttribute("otWeiAccList", otWeiAccList);
            request.setAttribute("otSysUserList", otSysUserList);
        }catch (Exception e) {
            EmpExecutionContext.error(e, "查询客服服务时长信息失败。");
        } finally {
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(PATH + "/curp_serverTime.jsp").forward(
                    request, response);
        }
    }

    /**
     * 加载所有公共账号和客服人员
     * 
     * @description
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-26 下午03:11:56
     */
    public void findAllAcct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            String aid = request.getParameter("aid");

            conditionMap.put("corpCode", lgcorpcode);
            
            // 用来存储所有公众账号
            List<LfWeiAccount> otWeiAccList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);

            // 用来存储所有客服人员
            conditionMap.put("permissionType", "3");
            if(null != aid && !"".equals(aid))
            {
                conditionMap.put("AId", aid);
            }
            List<LfSysUser> otSysUserList = baseBiz.getByCondition(LfSysUser.class, conditionMap, null);
            
            request.setAttribute("otWeiAccList", otWeiAccList);
            request.setAttribute("otSysUserList", otSysUserList);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "在线客服-客服系统-客户评价记录查询页面加载出错！");
        }
        finally
        {
            request.getRequestDispatcher(PATH + "/zxkf_custEvaluate.jsp").forward(request, response);
        }
    }

    /**
     * 客户评价得分情况
     * 
     * @description
     * @param request
     * @param response
     * @author fanglu <fanglu@montnets.com>
     * @throws IOException
     * @datetime 2013-12-26 下午02:14:53
     */
    @SuppressWarnings("unchecked")
    public void findcustomerEvaluate(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String result = "fail@";
        try
        {
            String aid = request.getParameter("aid");
            String startdate = request.getParameter("startdate");
            String enddate = request.getParameter("enddate");
            String cusid = request.getParameter("cusid");
            if(null == aid || "".equals(aid))
            {
                EmpExecutionContext.error("获取公共账号信息为空！");
            }
            else
            {
                String customername = "";
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("AId", aid);
                conditionMap.put("score& is not null", "0");
                if(null != startdate && !"".equals(startdate))
                {
                    conditionMap.put("createTime& >='"+startdate+"'", "0");
                }
                if(null != enddate && !"".equals(enddate))
                {
                    conditionMap.put("createTime& <='"+enddate+"'", enddate);
                }
                if(null != cusid && !"".equals(cusid))
                {
                    conditionMap.put("customeId", cusid);
                }
                List<LfOnlServer> otOnlServerList = baseBiz.getByCondition(LfOnlServer.class, conditionMap, null);
                LfOnlServer otOnlServer = null;
                JSONObject jsonObject = new JSONObject();
                Map<String, String> infoMap = new HashMap<String, String>();
                if(null != otOnlServerList && otOnlServerList.size() > 0)
                {
                    for (Iterator<LfOnlServer> iterator2 = otOnlServerList.iterator(); iterator2.hasNext();)
                    {
                        otOnlServer = (LfOnlServer) iterator2.next();
                        Long customerId = otOnlServer.getCustomeId();
                        LfSysUser otSysUser = baseBiz.getById(LfSysUser.class, customerId);
                        if(null != otSysUser)
                        {
                            customername = otSysUser.getName();
                            break;
                        }
                    }

                    StringBuffer xmldateString = new StringBuffer();
                    xmldateString.append("<graph caption='客服得分' xAxisName='客户 - 客服' yAxisName='得分' showNames='1' decimalPrecision='0' formatNumberScale='0' numberSuffix='分' bgColor='ecf7ff' outCnvBaseFontSize='14'>");
                    for (Iterator<LfOnlServer> otIterator = otOnlServerList.iterator(); otIterator.hasNext();)
                    {
                        otOnlServer = (LfOnlServer) otIterator.next();
                        
                        //以下是为了说明每个服务号对应的微信用户名称
                        String openId = otOnlServer.getFromUser();
                        conditionMap.clear();
                        conditionMap.put("openId", openId);
                        List<LfWeiUserInfo> otWeiUserInfoList = baseBiz.getByCondition(LfWeiUserInfo.class, conditionMap, null);
                        String userName = "";
                        if(null != otWeiUserInfoList)
                        {
                            userName = otWeiUserInfoList.get(0).getNickName();
                            infoMap.put(userName,otOnlServer.getSerNum());
                        }
                        
                        //以下是每个服务号对客服的评分
                        int score = otOnlServer.getScore();
                        xmldateString.append("<set name='");
                        xmldateString.append(userName);
                        xmldateString.append(" - ");
                        xmldateString.append(customername);
                        xmldateString.append("' value='");
                        xmldateString.append(score);
                        xmldateString.append("' color='588526'/>");
                    }
                    xmldateString.append("</graph>");
                    Document document = DocumentHelper.parseText(xmldateString.toString());
                    jsonObject.putAll(infoMap);
                    result = "success@" + document.asXML() + "&" + jsonObject.toString();
                }
                else
                {
                    result = "success@";
                }
            }
        }
        catch (Exception exception)
        {
            EmpExecutionContext.error(exception, "在线客服-客服系统-客户评价记录查询页面加载出错！");
        }
        finally
        {
            response.getWriter().print(result);
        }
    }
    
    /**
     * 根据公共账号的AId，加载公众账号对应的客服
     * @param request
     * @param response
     * @throws IOException                   
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-13 下午05:02:32
     */
    public void findCustomerByAId(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String result = "";
        try
        {
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            // 公众账号aid
            // 用来存储公共账号对应的所有客服人员
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            String aid = request.getParameter("aid");
            if(null != aid && !"".equals(aid.trim()))
            {
                conditionMap.put("AId", aid);
            }
            conditionMap.put("corpCode", lgcorpcode);
            
            conditionMap.put("permissionType", "3");
            List<LfSysUser> otSysUserList = baseBiz.getByCondition(LfSysUser.class, conditionMap, null);

            if(null != otSysUserList)
            {
                StringBuffer sb = new StringBuffer();
                sb.append("[");

                // 得到集合的长度
                int size = otSysUserList.size();
                int i = 0;

                for (LfSysUser otSysUser : otSysUserList)
                {
                    i++;

                    String cusid = String.valueOf(otSysUser.getUserId());
                    String cusName = otSysUser.getName();

                    sb.append("{\"cusid\":\"");
                    sb.append(cusid);
                    sb.append("\",\"cusName\":\"");
                    sb.append(cusName);
                    sb.append("\"}");

                    // 如果i小于size字符串sb中加","
                    if(i < size)
                    {
                        sb.append(",");
                    }
                }

                sb.append("]");

                // 拼好的字符串赋值给变量
                result = "success@" + sb.toString();
            }
            else
            {
                result = "success@[]";
            }                
        }
        catch (Exception e)
        {
            result = "fail@";
            EmpExecutionContext.error(e, "在线客服-客服统计-客户评价列表页面加载出错！");
        }
        finally
        {
            response.getWriter().print(result);
        }
    }
}
