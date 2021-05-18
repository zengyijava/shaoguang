package com.montnets.emp.mondetails.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitor.LfMonDbstate;
import com.montnets.emp.entity.monitor.LfMonDbwarn;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;

/**
 * @功能概要：
 * @项目名称： emp_std_192.169.1.81
 * @初创作者： Administrator
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016/5/5 19:12
 * <p>修改记录1：</p>
 * <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class mon_dataBaseMonSvt extends BaseServlet
{

    final String empRoot="ptjk";
    final String base="/mondetails";
    final BaseBiz baseBiz = new BaseBiz();

    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
            request.getRequestDispatcher(this.empRoot+base+"/mon_dataBaseIndex.jsp").forward(request,response);
    }

    /**
     * 数据库监控详情查询
     * @param request
     * @param response
     */
    public void getInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PageInfo pageInfo = new PageInfo();
        try {
            pageSet(pageInfo,request);
            LfMonDbstate bean = getConditionBean(request);
            List<LfMonDbstate> dbstateList = new ArrayList<LfMonDbstate>();
            List<LfMonDbstate> pageList = new ArrayList<LfMonDbstate>();
            Map<String,LfMonDbstate> dbMonMap = MonitorStaticValue.getDbMonMap();
            if(dbMonMap == null || dbMonMap.size() < 1)
            {
                dbMonMap = MonitorStaticValue.getDbMonMapTemp();
            }

            Iterator<String> keys = dbMonMap.keySet().iterator();


            while(keys.hasNext()){
                String key = keys.next();
                LfMonDbstate dbstate = dbMonMap.get(key);
                if(bean.getProcename() != null && !dbstate.getProcename().contains(bean.getProcename()))
                {
                    continue;
                }
                if(bean.getProcenode() != null && !bean.getProcenode().equals(dbstate.getProcenode()))
                {
                    continue;
                }
                if(bean.getEvttype() != null && !bean.getEvttype().equals(dbstate.getEvttype()))
                {
                    continue;
                }
                if(bean.getDbconnectstate() != null && !bean.getDbconnectstate().equals(dbstate.getDbconnectstate()))
                {
                    continue;
                }
                if(bean.getAddopr() != null && !bean.getAddopr().equals(dbstate.getAddopr()))
                {
                    continue;
                }
                if(bean.getProcetype() != null && !bean.getProcetype().equals(dbstate.getProcetype()))
                {
                    continue;
                }
                if(bean.getDispopr() != null && !bean.getDispopr().equals(dbstate.getDispopr()))
                {
                    continue;
                }
                if(bean.getDelopr() != null && !bean.getDelopr().equals(dbstate.getDelopr()))
                {
                    continue;
                }
                if(bean.getModiopr() != null && !bean.getModiopr().equals(dbstate.getModiopr()))
                {
                    continue;
                }
                if(bean.getMonstatus() != null && !bean.getMonstatus().equals(dbstate.getMonstatus()))
                {
                    continue;
                }
                dbstateList.add(dbstate);

            }
            //排序
            Collections.sort(dbstateList, new Comparator<LfMonDbstate>() {
                public int compare(LfMonDbstate o1, LfMonDbstate o2) {
                    return o1.getProcename().compareTo(o2.getProcename());
                }
            });
            //分页设置
            pageSetList(pageInfo,dbstateList.size());
            for(int i=(pageInfo.getPageIndex()-1)*pageInfo.getPageSize();i<pageInfo.getPageIndex()*pageInfo.getPageSize()&&i<dbstateList.size();i++)
            {
                pageList.add(dbstateList.get(i));
            }

            request.setAttribute("pageList", pageList);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"数据库监控详情查询异常！");
            request.setAttribute("findresult", "-1");
        }
        finally
        {
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(this.empRoot+base+"/mon_dataBaseMon.jsp").forward(request,
                    response);
        }
    }


    /**
     * 设置告警信息
     * @param request
     * @param response
     */
    public void setMon(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String monphone = StringUtils.defaultIfEmpty(request.getParameter("monphone")," ");
        String monemail = StringUtils.defaultIfEmpty(request.getParameter("monemail")," ");
        boolean result = false;
        try
        {
            LinkedHashMap<String,String> objMap = new LinkedHashMap<String, String>();
            objMap.put("monphone",monphone);
            objMap.put("monemail",monemail);
            int count = baseBiz.updateBySymbolsCondition(LfMonDbwarn.class,objMap,null);
            if(count == 0)
            {
                LfMonDbwarn dbwarn = new LfMonDbwarn();
                dbwarn.setMonemail(monemail);
                dbwarn.setMonphone(monphone);
                dbwarn.setCreatetime(new Timestamp(System.currentTimeMillis()));
                dbwarn.setUpdatetime(new Timestamp(System.currentTimeMillis()));
                result = baseBiz.addObj(dbwarn);
            }
            else
            {
                result = true;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"设置数据库监控告警信息异常！");
        }
        out.print(result);


    }

    /**
     * 获取告警信息
     * @param request
     * @param response
     */
    public void getMon(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("monphone","");
        jsonObject.put("monemail","");
        try {
            List<LfMonDbwarn> monDbwarns = baseBiz.findListByCondition(LfMonDbwarn.class,null,null);
            if(monDbwarns != null && monDbwarns.size() > 0)
            {
                LfMonDbwarn dbwarn = monDbwarns.get(0);
                jsonObject.put("monphone",dbwarn.getMonphone().trim());
                jsonObject.put("monemail",dbwarn.getMonemail().trim());
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"获取数据库监控告警信息异常！");
        }
        out.print(jsonObject.toString());


    }

    /**
     * 修改监控状态
     * @param request
     * @param response
     */
    public void changeSate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        boolean result = false;
        try
        {
            String procetype = request.getParameter("procetype");
            String procenode = request.getParameter("procenode");
            String monstatus = StringUtils.defaultString(request.getParameter("monstatus"),"0");
            LinkedHashMap<String,String> objMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String,String> condMap = new LinkedHashMap<String, String>();
            objMap.put("monstatus",monstatus);
            condMap.put("procetype",procetype);
            condMap.put("procenode",procenode);
            result = baseBiz.update(LfMonDbstate.class,objMap,condMap);
            if(result)
            {
                String key = procetype + procenode;
                Integer monStatus = Integer.parseInt(monstatus);
                LfMonDbstate dbstate = MonitorStaticValue.getDbMonMap().get(key);
                if(dbstate != null)
                {
                    dbstate.setMonstatus(monStatus);
                }
                dbstate = MonitorStaticValue.getDbMonMapTemp().get(key);
                if(dbstate != null)
                {
                    dbstate.setMonstatus(monStatus);
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"修改数据库监控告警信息异常！");
        }
        out.print(result);
    }

    /**
     *  * 设置分页信息
     * @description
     * @param pageInfo
     * @param totalCount
     */

    public void pageSetList(PageInfo pageInfo,int totalCount ) {
        //当前页数
        int pageSize = pageInfo.getPageSize();
        //总页数
        int totalPage = totalCount % pageSize == 0 ? totalCount
                / pageSize : totalCount / pageSize + 1;
        pageInfo.setTotalRec(totalCount);
        pageInfo.setTotalPage(totalPage);
        //如果当前页数大于最大页数，则跳转到第一页
        if (pageInfo.getPageIndex() > totalPage)
        {
            pageInfo.setPageIndex(1);
        }
    }

    /**
     * 设置查询条件Bean
     * @description
     * @param request
     */
    public LfMonDbstate getConditionBean(HttpServletRequest request)
    {
        LfMonDbstate dbstate = new LfMonDbstate();
        String procename = request.getParameter("procename");
        String procenode = request.getParameter("procenode");
        String evttype = request.getParameter("evttype");
        String dbconnectstate = request.getParameter("dbconnectstate");
        String addopr = request.getParameter("addopr");
        String procetype = request.getParameter("procetype");
        String dispopr = request.getParameter("dispopr");
        String delopr = request.getParameter("delopr");
        String modiopr = request.getParameter("modiopr");
        String monstatus = request.getParameter("monstatus");

        if(StringUtils.isNotBlank(procename))
        {
            dbstate.setProcename(procename);
        }
        if(StringUtils.isNotBlank(procenode))
        {
            dbstate.setProcenode(Long.parseLong(procenode));
        }
        if(StringUtils.isNotBlank(evttype))
        {
            dbstate.setEvttype(Integer.parseInt(evttype));
        }
        if(StringUtils.isNotBlank(dbconnectstate))
        {
            dbstate.setDbconnectstate(Integer.parseInt(dbconnectstate));
        }
        if(StringUtils.isNotBlank(addopr))
        {
            dbstate.setAddopr(Integer.parseInt(addopr));
        }
        if(StringUtils.isNotBlank(procetype))
        {
            dbstate.setProcetype(Integer.parseInt(procetype));
        }
        if(StringUtils.isNotBlank(dispopr))
        {
            dbstate.setDispopr(Integer.parseInt(dispopr));
        }
        if(StringUtils.isNotBlank(delopr))
        {
            dbstate.setDelopr(Integer.parseInt(delopr));
        }
        if(StringUtils.isNotBlank(modiopr))
        {
            dbstate.setModiopr(Integer.parseInt(modiopr));
        }
        if(StringUtils.isNotBlank(monstatus))
        {
            dbstate.setMonstatus(Integer.parseInt(monstatus));
        }

        return dbstate;
    }
}
