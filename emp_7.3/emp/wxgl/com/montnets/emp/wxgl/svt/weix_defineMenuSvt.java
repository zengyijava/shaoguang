package com.montnets.emp.wxgl.svt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiMenu;
import com.montnets.emp.entity.wxgl.LfWeiTemplate;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.constant.WeixHttpUrl;
import com.montnets.emp.ottbase.constant.WeixMessage;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.ottbase.util.GlobalMethods;
import com.montnets.emp.wxgl.base.util.TrustSSL;
import com.montnets.emp.wxgl.biz.AccountBiz;
import com.montnets.emp.wxgl.biz.MenuBiz;

/**
 * 自定义图文模块servlet
 * 
 * @project p_weix
 * @author linzhihan <zhihanking@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-13 下午05:44:32
 * @description
 */
@SuppressWarnings("serial")
public class weix_defineMenuSvt extends BaseServlet
{

    // 资源路径
    private static final String PATH       = "/wxgl/definemenu";

    // 基础逻辑层
    private final BaseBiz  baseBiz    = new BaseBiz();

    // 菜单逻辑层
    private final MenuBiz menuBiz    = new MenuBiz();


    // 微信菜单发布逻辑层
    private final TrustSSL  trustSSL   = new TrustSSL();
    
    //微信biz
    private final WeixBiz weixBiz   = new WeixBiz();

    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 企业编码
        String lgcorpcode = request.getParameter("lgcorpcode");
        // EMP企业公众帐号
        // 查询所有的公众帐号
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("corpCode", lgcorpcode);
        List<LfWeiAccount> otWeiAccList = new ArrayList<LfWeiAccount>();
        try
        {
            otWeiAccList = new BaseBiz().getByCondition(LfWeiAccount.class, conditionMap, null);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "自定义菜单-获取公众帐号记录失败！");
        }
        finally
        {
            request.setAttribute("acctList", otWeiAccList);
            request.getRequestDispatcher(PATH + "/weix_defineMenu.jsp").forward(request, response);
        }
    }

    /**
     * 通过公众号ID获取菜单信息
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    public void getMenuByAId(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        // 公众号ID
        String aid = request.getParameter("aid");

        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        try
        {
        	
        	// TODO 添加日志
			//添加日志，用于观察url请求参数获取的情况 p
			EmpExecutionContext.logRequestUrl(request, null);
        	
            // appid信息
            String appStr = "";
            // 公众号信息
            LfWeiAccount account = baseBiz.getById(LfWeiAccount.class, aid);
            if(account != null)
            {
                // appid信息赋值
                appStr = (account.getAppId() == null ? "" : account.getAppId()) + "&" + (account.getSecret() == null ? "" : account.getSecret());
            }
            conditionMap.put("AId", aid);
            // 先按一级菜单排序
            orderbyMap.put("PId", StaticValue.ASC);
            orderbyMap.put("morder", StaticValue.ASC);
            // 获得该公众号对应的菜单信息
            List<LfWeiMenu> menuList = baseBiz.getByCondition(LfWeiMenu.class, conditionMap, orderbyMap);
            // 返回结果的Map<key：一级菜单Id，value：一级菜单对应的菜单信息>
            LinkedHashMap<Long, JSONArray> resultJsonMap = new LinkedHashMap<Long, JSONArray>();

            if(menuList != null && menuList.size() > 0)
            {
                for (LfWeiMenu menu : menuList)
                {
                    JSONArray resultArray = new JSONArray();
                    Long key = menu.getMId();
                    // 如果不是一级菜单，是一级菜单时pid=0
                    if(menu.getPId().intValue() != 0)
                    {
                        // 获取对应的一级菜单信息
                        resultArray = resultJsonMap.get(menu.getPId());
                        if(resultArray == null)
                        {
                            continue;
                        }
                        key = menu.getPId();
                    }
                    // 设置菜单属性
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("mname", menu.getMname());
                    jsonObj.put("mid", menu.getMId());
                    jsonObj.put("pid", menu.getPId());
                    jsonObj.put("murl", menu.getMurl());
                    jsonObj.put("tid", menu.getTId());
                    jsonObj.put("mtype", menu.getMtype());
                    jsonObj.put("mkey", menu.getMkey());

                    resultArray.add(jsonObj);
                    resultJsonMap.put(key, resultArray);
                }
            }
            // 将分组后的信息添加到一个json数组中
            JSONArray resultArray = new JSONArray();
            Iterator<Long> iter = resultJsonMap.keySet().iterator();
            while(iter.hasNext())
            {
                Long key = iter.next();
                resultArray.add(resultJsonMap.get(key));
            }
            // 返回结果
            response.getWriter().print(appStr + "@" + resultArray.toString());
        }
        catch (Exception e)
        {
            response.getWriter().print("@");
            EmpExecutionContext.error(e, "通过公众号ID获取菜单信息异常！");
        }

    }

    /**
     * 新增菜单
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    public void addMenu(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        // 已存在的菜单个数
        String size = request.getParameter("size");
        // 菜单名称
        String mname = request.getParameter("mname");
        // 父级菜单ID，一级菜单时为0
        String pid = request.getParameter("pid");
        // 公众号Id
        String aid = request.getParameter("aid");
        // 企业编码
        String lgcorpcode = request.getParameter("lgcorpcode");
        // 一级菜单添加时字节长度判断为8
        if("0".equals(pid) && mname.getBytes("GBK").length > 14)
        {
            response.getWriter().print("overlength");
            return;
        }
        // 二级菜单添加时字节长度判断为16
        else if(mname.getBytes("GBK").length > 16)
        {
            response.getWriter().print("overlength");
            return;
        }
        // 设置属性
        LfWeiMenu menu = new LfWeiMenu();
        menu.setPId(Long.valueOf(pid));
        menu.setMname(mname);
        menu.setMorder(Integer.parseInt(size) + 1);
        menu.setCorpCode(lgcorpcode);
        menu.setAId(Long.valueOf(aid));
        menu.setMkey(menuBiz.getKeyId(pid, aid));
        try
        {
            // 如果不是一级菜单，则需判断是否是第一个2级菜单创建，是则需重置一级菜单的动作
            if("0".equals(size) && pid != null && !"0".equals(pid))
            {
                LfWeiMenu pmenu = baseBiz.getById(LfWeiMenu.class, Long.valueOf(pid));
                if(pmenu.getMtype() > 0)
                {
                    pmenu.setMurl("");
                    pmenu.setMsgText("");
                    pmenu.setMsgXml("");
                    pmenu.setMtype(0);
                    pmenu.setTId(0l);
                    // 属性值设为null时该方法无法将值更新至数据库
                    boolean res = baseBiz.updateObj(pmenu);
                    if(!res)
                    {
                        throw new Exception();
                    }
                }
            }
            Long result = baseBiz.addObjProReturnId(menu);
            if(result != null && result > 0)
            {
                response.getWriter().print("true" + result.toString());
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "添加微信自定义菜单失败！");
            response.getWriter().print(false);
        }

    }

    /**
     * 删除菜单
     * 
     * @param request
     * @param response
     */
    public void delMenu(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("text/html");
        String mid = request.getParameter("mid");
        try
        {
            // 查找待删除的菜单信息
            LfWeiMenu menu = baseBiz.getById(LfWeiMenu.class, mid);
            if(menu == null)
            {
                // 查找不到则返回找不到菜单信息
                response.getWriter().print("nomenu");
            }
            else
            {
                // 返回删除结果
                response.getWriter().print(menuBiz.delMenu(menu));
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "调用删除微信自定义菜单异常！");
        }
    }

    /**
     * 修改菜单名称
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    public void updateMenuName(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        // 父级菜单ID，为0则表示为一级菜单
        String pid = request.getParameter("pid");
        // 菜单id
        String mid = request.getParameter("mid");
        // 修改后的名称
        String mname = request.getParameter("mname");
        // 一级菜单添加时字节长度判断为8
        if("0".equals(pid) && mname.getBytes("GBK").length > 8)
        {
            response.getWriter().print("overlength");
            return;
        }
        // 二级菜单添加时字节长度判断为16
        else if(mname.getBytes("GBK").length > 16)
        {
            response.getWriter().print("overlength");
            return;
        }
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();

        conditionMap.put("MId", mid);
        objectMap.put("mname", mname);

        try
        {
            // 返回更新结果
            response.getWriter().print(baseBiz.update(LfWeiMenu.class, objectMap, conditionMap));
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "微信修改自定义菜单名称失败！");
        }

    }

    /**
     * 调整顺序
     * 
     * @param request
     * @param response
     */
    public void updateOrder(HttpServletRequest request, HttpServletResponse response)
    {
        PrintWriter out = null;
        response.setContentType("text/html");
        try
        {
            out = response.getWriter();
            // 原调整的菜单顺序1
            String mId1 = request.getParameter("mid1");
            // 原调整的菜单顺序2
            String mId2 = request.getParameter("mid2");
            // 判断是否为无效的字符串
            if(!GlobalMethods.isInvalidString(mId1) && !GlobalMethods.isInvalidString(mId2))
            {
                boolean suc = menuBiz.updateOrder(mId1, mId2);
                if(suc)
                {
                    out.print("@菜单顺序更新成功！");
                }
                else
                {
                    out.print("#菜单顺序更新失败！");
                }
            }
        }
        catch (IOException e)
        {
            EmpExecutionContext.error(e, "菜单顺序更新发生异常！");
        }
    }

    /**
     * 设置动作或修改动作
     * mtype:
     * 1:图文
     * 2：链接地址
     * 3.人工客服
     * 4：LBS
     * 5：微站
     * 6：抽奖
     * 
     * @param request
     * @param response
     */
    public void setAction(HttpServletRequest request, HttpServletResponse response)
    {
        // 菜单id
        String menuId = request.getParameter("mid");
        // 动作类型
        String mType = request.getParameter("mtype");
        // 动作跳转url
        String url = request.getParameter("url");
        // 关联模板id
        String tId = request.getParameter("tid");
        // 当前的公众帐号的appid
        String appid = request.getParameter("appid");
        response.setContentType("text/html");
        try
        {
            PrintWriter out = response.getWriter();
            // 微信公众帐号授权地址
            //String accessUrl = WeixHttpUrl.WX_AUTHORIZE_URL + "appid=" + appid + "&redirect_uri=";
            // 授权重定向地址
            String redirect_url = "";
            if(!GlobalMethods.isInvalidString(menuId))
            {
                LfWeiMenu menu = baseBiz.getById(LfWeiMenu.class, Long.valueOf(menuId));
                if(!GlobalMethods.isInvalidString(mType))
                {
                    menu.setMtype(Integer.parseInt(mType));
                    // 初始化菜单的值
                    menu.setMurl(url);
                    menu.setMsgXml("");
                    menu.setMsgText("");
                    String mkey = menu.getMkey();
                    if(mkey.contains("_module_"))
                    {
                        menu.setMkey(mkey.substring(0, mkey.lastIndexOf("_module_")));
                    }

                    if("2".equalsIgnoreCase(mType))
                    {
                        // 输入url
                        menu.setMurl(url);
                        menu.setTId(0L);
                    }
                    else if("1".equals(mType))
                    {
                        // 模板id为空或为0,表示没有关联模板
                        if(tId != null && !"".equals(tId) && !"0".equals(tId))
                        {
                            menu.setTId(Long.valueOf(tId));
                        }
                        else
                        {
                            // 没选模板，模板id设为0
                            menu.setTId(0l);
                        }
                    }
                    else if("3".equals(mType))
                    {
                        // 3表示在线客服
                        menu.setTId(0L);
                        menu.setMkey(menu.getMkey() + "_module_zxkf");
                    }
                    else if("4".equals(mType))
                    {
                        // 4表示LBS采集点查询
                        menu.setTId(0L);
                        menu.setMkey(menu.getMkey() + "_module_lbs");
                    }
                    else if("5".equals(mType))
                    {
                        // 5表示微站
                        redirect_url = GlobalMethods.getWeixBasePath() + "wzgl_sitePreview.hts?method=toAccessSite&response_type=code&scope=snsapi_base&state=aid" + String.valueOf(menu.getAId()) + "tid" + tId;
                        menu.setMurl(redirect_url);
                        menu.setTId(Long.valueOf(tId));
                        menu.setMkey(menu.getMkey() + "_module_wzgl");
                    }
                    else if("6".equals(mType))
                    {
                        // 6表示抽奖
                        menu.setTId(Long.valueOf(tId));
                        menu.setMkey(menu.getMkey() + "_module_choujiang");
                    }
                    else if("7".equals(mType))
                    {
                        // 7表单
                        redirect_url = GlobalMethods.getWeixBasePath() + "wzgl_formManager.hts?method=toAccessForm&response_type=code&scope=snsapi_base&state=aid" + String.valueOf(menu.getAId()) + "tid" + tId;
                        menu.setMurl(redirect_url);
                        menu.setTId(Long.valueOf(tId));
                        menu.setMkey(menu.getMkey() + "_module_form");
                    }

                    boolean result = baseBiz.updateObj(menu);
                    if(result)
                    {
                        out.print("@" + MessageUtils.extractMessage("wxgl", "wxgl_java_title_12", request));
                    }
                    else
                    {
                        out.print( MessageUtils.extractMessage("wxgl", "wxgl_java_title_13", request));
                    }
                }
                else
                {
                    // 未知的动作类型
                    out.print( MessageUtils.extractMessage("wxgl", "wxgl_java_title_14", request));
                }
            }
            else
            {
                // 未获取到menuid
                out.print( MessageUtils.extractMessage("wxgl", "wxgl_java_title_15", request));
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,  MessageUtils.extractMessage("wxgl", "wxgl_java_title_16", request));
        }

    }

    /**
     * 获取图文模板名称
     * 
     * @param request
     * @param response
     */
    public void getTempName(HttpServletRequest request, HttpServletResponse response)
    {
        // 图文模板ID
        String tid = request.getParameter("tid");
        response.setContentType("text/html");
        try
        {
            String tempName = "";
            // 查找对应的图文模板信息
            LfWeiTemplate template = baseBiz.getById(LfWeiTemplate.class, tid);
            if(template != null)
            {
                tempName = template.getName();
            }
            // 返回名称
            response.getWriter().print(tempName);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取图文模板名称失败！");
        }
    }

    /**
     * 发布菜单
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    public void release(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // 公众帐号
        String aid = request.getParameter("aid");
        // 发布菜单请求地址
        String postUrl = WeixHttpUrl.WX_CREATEMENU_URL;
        // 输出对象
        PrintWriter out = null;
        // 错误编码
        String errcode = null;
        // 错误消息
        String errmsg = null;
        // 返回编码
        response.setContentType("text/html");
        try
        {
            out = response.getWriter();
            // 查询公众帐号对应的菜单
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("AId", aid);
            LfWeiAccount account = baseBiz.getById(LfWeiAccount.class, Long.valueOf(aid));

            // 第一步： 获取token
            String token = (new WeixBiz()).getToken(account);
            // 第二步：保证token有效的情况下，提交发送菜单请求，负责不用调用发布菜单接口
            if(token != null)
            {
                // 公众帐号对应的菜单json格式
                JSONObject jsonMenu = menuBiz.getMenuJsonByAccount(aid);
                // 执行发布请求
                JSONObject result = trustSSL.createRemoteMenu(postUrl, jsonMenu.toString(), token);
                errcode = String.valueOf(result.get("errcode"));
                errmsg = (String) result.get("errmsg");
            }

            // 第三部：对errcode进行判断，如果为“0”，表明发布成功；
            if("0".equals(errcode))
            {
                out.print(MessageUtils.extractMessage("wxgl", "wxgl_java_title_21", request));
            }
            else
            {
                // 根据返回错误码 若因为token的原因导致发布失败 则置空token 使其下次重新获取
                if(token != null && weixBiz.validateAssessToken(errcode))
                {
                    weixBiz.reGetToken(account.getOpenId());
                }
                
                try
                {
                    String message = WeixMessage.getValue(errcode);
                    if(!GlobalMethods.isInvalidString(message))
                    {
                        out.print(message + "！");
                    }
                    else
                    {
                        out.print(errmsg + "！");
                    }
                }
                catch (Exception e)
                {
                    EmpExecutionContext.error(e, "加载message配置信息失败！");
                    out.print(errmsg + "！");
                }
            }
        }
        catch (Exception e)
        {
        	if(out!=null){
        		out.print(MessageUtils.extractMessage("wxgl", "wxgl_java_title_22", request));
        	}
        	EmpExecutionContext.error(e, "公众帐号发布失败！");
        }
        finally
        {
        	if(out!=null){
        		out.close();
        	}
        }
    }
}
