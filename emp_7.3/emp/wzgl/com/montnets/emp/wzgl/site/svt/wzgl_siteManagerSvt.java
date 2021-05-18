/**
 * @description
 * @author Administrator <foyoto@gmail.com>
 * @datetime 2013-12-23 下午07:28:09
 */
package com.montnets.emp.wzgl.site.svt;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.site.LfSitInfo;
import com.montnets.emp.entity.site.LfSitPage;
import com.montnets.emp.entity.site.LfSitPlant;
import com.montnets.emp.entity.site.LfSitType;
import com.montnets.emp.ottbase.constant.SiteParams;
import com.montnets.emp.ottbase.util.StringUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wzgl.site.biz.SiteBiz;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator <foyoto@gmail.com>
 * @description
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-12-23 下午07:28:09
 */
public class wzgl_siteManagerSvt extends BaseServlet {
    /**
     * @description serialVersionUID
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-25 下午03:33:16
     */
    private static final long serialVersionUID = -1079222152659642834L;

    // 资源路径
    public static final String PATH = "/wzgl/site";

    // 基础逻辑层
    private final BaseBiz baseBiz = new BaseBiz();

    /**
     * 公用siteBiz层
     */
    private final SiteBiz siteBiz = new SiteBiz();

    /**
     * 微信列表
     *
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2013-12-24 上午09:49:46
     */
    @SuppressWarnings("unchecked")
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 分页信息
        PageInfo pageInfo = new PageInfo();
        // 是否是弹出框显示列表
        String isArtDialog = request.getParameter("isArtDialog");
        // 返回跳转
        String isOperateReturn = request.getParameter("isOperateReturn");
        try {
            String corpCode = request.getParameter("lgcorpcode");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> typeHashMap = new LinkedHashMap<String, String>();

            if ("true".equals(isOperateReturn)) {
                conditionMap = (LinkedHashMap<String, String>) request.getSession(false).getAttribute("wzgl_sitePageCondtionMap");
                pageInfo = (PageInfo) request.getSession(false).getAttribute("wzgl_sitePagePageInfo");
                typeHashMap = (LinkedHashMap<String, String>) request.getSession(false).getAttribute("wzgl_sitetypeHashMap");
            } else {
                conditionMap.put("corpCode", corpCode);

                boolean isFirstEnter = pageSet(pageInfo, request);
                // 系统微站
                conditionMap.put("isSystem", "1");
                // 标准微站
                conditionMap.put("pattern", "0");
                // 微信类型

                List<LfSitType> siteTypes = baseBiz.getByCondition(LfSitType.class, conditionMap, null);

                if (siteTypes != null) {
                    for (LfSitType tp : siteTypes) {
                        typeHashMap.put(String.valueOf(tp.getTypeId()), tp.getName());
                    }
                }
                conditionMap.clear();
                // 微站名称
                String name = request.getParameter("name");
                // 微站类型
                String typeId = request.getParameter("typeId");

                if (!isFirstEnter) {
                    // 微站名称
                    if (name != null && !"".equals(name.trim())) {
                        conditionMap.put("name&like", name.trim());
                    }
                    // 微站类型
                    if (typeId != null && !"".equals(typeId.trim())) {
                        conditionMap.put("typeId", typeId.trim());
                    }
                }

                conditionMap.put("corpCode", corpCode);
                conditionMap.put("isSystem", "0");

                if ("true".equals(isArtDialog)) {
                    pageInfo.setPageSize(10);
                }
                request.getSession(false).setAttribute("wzgl_sitePagePageInfo", pageInfo);
                request.getSession(false).setAttribute("wzgl_sitePageCondtionMap", conditionMap);
                request.getSession(false).setAttribute("wzgl_sitetypeHashMap", typeHashMap);
            }
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("createtime", "DESC");
            // 微信列表
            List<LfSitInfo> infoList = baseBiz.getByConditionNoCount(LfSitInfo.class, null, conditionMap, orderMap, pageInfo);
            request.setAttribute("typeHashMap", typeHashMap);
            request.setAttribute("infoList", infoList);
            request.setAttribute("conditionMap", conditionMap);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "微站管理-微站列表查询失败！");
        } finally {
            request.setAttribute("pageInfo", pageInfo);
            if ("true".equals(isArtDialog)) {
                request.getRequestDispatcher(PATH + "/wzgl_artDialogSite.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher(PATH + "/wzgl_siteManager.jsp").forward(request, response);
            }
        }
    }

    /**
     * 编辑微站
     *
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-10 下午02:32:00
     */
    public void doEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String corpCode = request.getParameter("lgcorpcode");
        String sId = request.getParameter("sId");
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

        try {
            // 查询当前站点
            LfSitInfo otSitInfo = baseBiz.getById(LfSitInfo.class, sId);
            // 查询当前站点页面
            conditionMap.put("sId", String.valueOf(sId));
            conditionMap.put("corpCode", corpCode);
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("pageId", "ASC");
            List<LfSitPage> otSitPageList = baseBiz.getByCondition(LfSitPage.class, conditionMap, orderMap);

            // 需要传递到微站创建页面的数据
            request.setAttribute("otSitInfo", otSitInfo);
            request.setAttribute("otPageList", otSitPageList);
            request.setAttribute("edit", "true");
        } catch (Exception e) {
            EmpExecutionContext.error(e, "跳转到微站编辑页面异常！");
        } finally {
            request.getRequestDispatcher(PATH + "/wzgl_addSite.jsp").forward(request, response);
        }
    }

    /**
     * 选择微站风格
     *
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-24 下午03:00:01
     */
    public void chooseSiteType(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String corpCode = request.getParameter("lgcorpcode");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            // 系统微站
            conditionMap.put("isSystem", "1");
            // 普通微站
            conditionMap.put("pattern", "0");
            // 企业编码
            conditionMap.put("corpCode", corpCode);
            // 排序
            orderMap.put("seqNum", "ASC");
            // 微信列表
            List<LfSitType> otSitTypeList = baseBiz.getByCondition(LfSitType.class, conditionMap, orderMap);
            if (null == otSitTypeList) {
                otSitTypeList = new ArrayList<LfSitType>();
            }
            request.setAttribute("otSitTypeList", otSitTypeList);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "微站管理-创建微站-风格选择页面加载失败！");
        } finally {
            request.getRequestDispatcher(PATH + "/wzgl_siteTypeChoose.jsp").forward(request, response);
        }
    }

    /**
     * 点击“下一步”按钮，根据模板风格创建站点
     *
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-25 下午02:56:28
     */
    @SuppressWarnings("unchecked")
    public void createSiteByTemp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String corpCode = request.getParameter("lgcorpcode");
            String typeId = request.getParameter("typeId");

            JSONObject jsonObject = siteBiz.createSiteByTemp(corpCode, typeId);

            if (null != jsonObject) {
                LfSitInfo otSitInfo = (LfSitInfo) jsonObject.get("otSitInfo");
                List<LfSitPage> newPageList = (List<LfSitPage>) jsonObject.get("otPageList");
                // 需要传递到微站创建页面的数据
                request.setAttribute("otSitInfo", otSitInfo);
                request.setAttribute("otPageList", newPageList);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据风格模板创建微站失败！");
        } finally {
            request.getRequestDispatcher(PATH + "/wzgl_addSite.jsp").forward(request, response);
        }
    }

    /**
     * 修改站点名字
     *
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-26 上午09:35:43
     */
    public void updateSiteName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 用来标识操作结果
        String result = "";
        try {
            // 微站ID
            String sid = request.getParameter("sid");
            String name = request.getParameter("name");
            if (null == sid || "".equals(sid)) {
                result = "fail";
                EmpExecutionContext.error("获取站点信息失败！");
            } else {

                if (name.getBytes("GBK").length > 12) {
                    response.getWriter().print("overlength");
                    return;
                }

                // 得到站点信息
                LfSitInfo otSitInfo = baseBiz.getById(LfSitInfo.class, sid);

                if (null == name || "".equals(name.trim())) {
                    name = "未命名";
                }
                // 将信息保存到数据库
                otSitInfo.setName(name);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                otSitInfo.setModitytime(Timestamp.valueOf(df.format(new Date())));
                boolean isupdate = baseBiz.updateObj(otSitInfo);
                if (isupdate) {
                    // 更新成功
                    result = "success";
                } else {
                    // 更新失败
                    result = "fail";
                    EmpExecutionContext.error("修改站点名称失败！");
                }
            }
        } catch (Exception e) {
            // 更新失败
            result = "fail";
            EmpExecutionContext.error(e, "微站管理-创建微站-修改微站名称发生异常！");
        } finally {
            response.getWriter().print(result);
        }
    }

    /**
     * 修改微站页面名字
     *
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-26 上午09:35:59
     */
    public void updatePageName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 用来标识操作结果
        String result = "";
        try {
            // 微站页面ID
            String pageId = request.getParameter("pageid");
            String name = request.getParameter("name");
            if (null == pageId || "".equals(pageId)) {
                result = "fail";
                EmpExecutionContext.error("获取站点页面信息失败！");
            } else {
                if (name.getBytes("GBK").length > 12) {
                    response.getWriter().print("overlength");
                    return;
                }

                // 得到站点信息
                LfSitPage otSitPage = baseBiz.getById(LfSitPage.class, pageId);

                if (null == name || "".equals(name.trim())) {
                    name = "未命名";
                }
                // 将信息保存到数据库
                otSitPage.setName(name);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                otSitPage.setModitytime(Timestamp.valueOf(df.format(new Date())));
                boolean isupdate = baseBiz.updateObj(otSitPage);
                if (isupdate) {
                    // 更新成功
                    result = "success";
                } else {
                    // 更新失败
                    result = "fail";
                    EmpExecutionContext.error("修改站点页面名称失败！");
                }
            }
        } catch (Exception e) {
            // 更新失败
            result = "fail";
            EmpExecutionContext.error(e, "微站管理-创建微站-修改微站页面名称发生异常！");
        } finally {
            response.getWriter().print(result);
        }
    }

    /**
     * 删除微站页面
     *
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-26 上午09:36:12
     */
    public void deletePageInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 用来标识操作结果
        String result = "fail";
        try {
            // 微站页面ID
            String pageId = request.getParameter("pageid");
            String corpCode = request.getParameter("lgcorpcode");
            if (null == pageId || "".equals(pageId)) {
                EmpExecutionContext.error("获取站点页面信息失败！");
            } else {
                boolean isdelete = siteBiz.deletePageInfo(pageId, corpCode);
                if (isdelete) {
                    result = "success";
                }
            }
        } catch (Exception e) {
            // 删除失败
            EmpExecutionContext.error(e, "微站管理-创建微站-修改微站页面名称发生异常！");
        } finally {
            response.getWriter().print(result);
        }
    }

    /**
     * 复制微站页面
     *
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-26 上午09:36:21
     */
    public void copyPageInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LfSitPage otSitPage = null;
        try {
            String corpCode = request.getParameter("lgcorpcode");
            String pageId = request.getParameter("pageid");
            if (null == pageId || "".equals(pageId)) {
                EmpExecutionContext.error("获取站点页面信息失败！");
            } else {
                otSitPage = siteBiz.copyPageInfo(pageId, corpCode);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "复制微站页面失败！");
        } finally {
            request.setAttribute("otSitPage", otSitPage);
            request.getRequestDispatcher(PATH + "/wzgl_copyPageInfo.jsp").forward(request, response);
        }
    }

    /**
     * 获取微站页面
     *
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2013-12-28 下午03:06:10
     */
    public void getPageInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 返回结果
        String result = "";
        // 当前微站页面ID
        String pageId = request.getParameter("pageId");
        // 微站页面类型
        String ptype = request.getParameter("ptype");
        // 访问来源
        String from = request.getParameter("from");
        // 微站页面空间数据
        HashMap<String, JSONObject> resultMap = new HashMap<String, JSONObject>();
        HashMap<String, String> plantMap = new HashMap<String, String>();
        // 当前页面控件的名称
        String[] plants = SiteParams.getStypeMap(ptype);
        try {
            //if(null == pageId || "".equals(pageId) || plants == null || "".equals(plants))  //findbugs
            if (null == pageId || "".equals(pageId) || plants == null) {
                result = "fail";
                EmpExecutionContext.error("获取站点页面信息失败！");
            } else {
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("pageId", pageId);
                List<LfSitPlant> otSitPlantList = baseBiz.getByCondition(LfSitPlant.class, conditionMap, null);

                if (otSitPlantList != null && otSitPlantList.size() > 0) {
                    for (LfSitPlant sitplant : otSitPlantList) {
                        resultMap.put(sitplant.getPlantType(), StringUtils.parsJsonObj(sitplant.getFeildValues()));
                        plantMap.put(sitplant.getPlantType(), String.valueOf(sitplant.getPlantId()));
                    }
                } else {
                    result = "fail";
                }

                request.setAttribute("resultMap", resultMap);
                request.setAttribute("plantMap", plantMap);
                request.setAttribute("plants", plants);
            }
        } catch (Exception e) {
            result = "fail";
            EmpExecutionContext.error(e, "获取站点页面信息失败！");
        } finally {
            if ("fail".equals(result)) {
                response.getWriter().print(result);
            } else {
                if (from != null && !"".equals("phone")) {
                    request.getRequestDispatcher(PATH + "/normal/wzgl_normal_page_phone.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher(PATH + "/normal/wzgl_normal_page.jsp").forward(request, response);
                }
            }
        }
    }

    /**
     * 获取微站页面
     *
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2013-12-28 下午03:06:10
     */
    public void getPagePreview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String urlToken = request.getParameter("urlToken");
        String from = request.getParameter("from");
        LfSitPage otSitPage = null;
        try {
            //当前页面类型
            otSitPage = siteBiz.getSitePageByToken(urlToken);
            request.setAttribute("otSitPage", otSitPage);
            request.setAttribute("from", from);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "微站预览失败！");
        } finally {
            if (null != otSitPage && null != otSitPage.getPageId()) {
                request.getRequestDispatcher(PATH + "/wzgl_pagePreview.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/wxcommon/weixerror.jsp").forward(request, response);
            }
        }
    }

    /**
     * 获取页面控件
     *
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2013-12-30 下午04:41:09
     */
    public void getPlantFormInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 返回结果
        String result = "";
        // 页面Id
        String pageId = request.getParameter("pageId");
        // 页面类型
        String ptype = request.getParameter("ptype");
        // 企业编号
        String corpCode = request.getParameter("lgcorpcode");
        // 返回结果
        HashMap<String, JSONObject> resultMap = new HashMap<String, JSONObject>();
        try {
            if (null == pageId || "".equals(pageId) || null == ptype || "".equals(ptype)) {
                result = "fail";
                EmpExecutionContext.error("获取站点页面信息失败！");
            } else {
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("pageId", pageId);
                conditionMap.put("corpCode", corpCode);
                List<LfSitPlant> otSitPlantList = baseBiz.getByCondition(LfSitPlant.class, conditionMap, null);

                if (otSitPlantList != null && otSitPlantList.size() > 0) {
                    for (LfSitPlant sitplant : otSitPlantList) {
                        resultMap.put(sitplant.getPlantType(), StringUtils.parsJsonObj(sitplant.getFeildValues()));
                    }
                    request.setAttribute("resultMap", resultMap);
                } else {
                    result = "fail";
                }
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取页面编辑器失败！");
        } finally {
            if ("fail".equals(result)) {
                response.getWriter().print(result);
            } else {
                request.getRequestDispatcher(PATH + "/normal/wzgl_" + ptype + "_plants_form.jsp").forward(request, response);
            }
        }
    }

    /**
     * 删除微站
     *
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-28 下午03:08:31
     */
    public void deleteSiteInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 用来标识操作结果
        String result = "fail";
        try {
            String sId = request.getParameter("sId");
            boolean issuccess = siteBiz.deleteSiteInfo(sId);
            if (issuccess) {
                result = "success";
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "微站管理-创建微站-删除微站发生异常！");
        } finally {
            response.getWriter().print(result);
        }
    }

    /**
     * 微站信息”保存“功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-31 下午04:48:19
     */
    public void createPlantValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 用来标识操作结果
        String result = "fail";
        try {
            // 当前微站页面控件值
            String count = request.getParameter("count");
            String plantId = request.getParameter("plantId");
            String plantType = request.getParameter("plantType");
            String corpCode = request.getParameter("lgcorpcode");
            if (null == count || "".equals(count) || "0".equals(count)) {
                EmpExecutionContext.error("保存信息失败，count为空，未获取到保存数据信息！");
            } else if (null == plantId || "".equals(plantId)) {
                EmpExecutionContext.error("plantId为空，保存信息失败！");
            } else if (null == plantType || "".equals(plantType)) {
                EmpExecutionContext.error("plantType为空，保存信息失败！");
            } else if (null == corpCode || "".equals(corpCode)) {
                EmpExecutionContext.error("corpCode为空，保存信息失败！");
            } else {
                ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();
                int size = Integer.parseInt(count);
                for (int i = 0; i < size; i++) {
                    // 根据控件类型获取页面控件值
                    HashMap<String, String> map = new HashMap<String, String>();
                    if ("normal_head".equals(plantType)) {
                        String head_title = request.getParameter("head_title" + i);
                        String head_imgurl = request.getParameter("head_imgurl" + i);
                        String head_link = request.getParameter("head_link" + i);
                        String head_tp = request.getParameter("head_tp" + i);
                        // 类型值
                        String head_tpvalue = request.getParameter("tp" + head_tp + "_value" + i);
                        // 类型备注
                        String head_tpnote = request.getParameter("tp" + head_tp + "_note" + i);

                        map.put("head_title", head_title);
                        map.put("head_imgurl", head_imgurl);
                        map.put("head_link", head_link);
                        map.put("head_tp", head_tp);
                        map.put("head_tpvalue", head_tpvalue);
                        map.put("head_tpnote", head_tpnote);
                    } else if ("normal_link".equals(plantType)) {
                        String link_phone = request.getParameter("link_phone");
                        String link_bgcolor = request.getParameter("link_bgcolor");
                        String link_note = request.getParameter("link_note");

                        map.put("link_phone", link_phone);
                        map.put("link_bgcolor", link_bgcolor);
                        map.put("link_note", link_note);
                    } else if ("normal_list".equals(plantType)) {
                        String list_title = request.getParameter("list_title" + i);
                        String list_tp = request.getParameter("list_tp" + i);
                        String list_note = request.getParameter("list_note" + i);
                        // 类型值
                        String list_tpvalue = request.getParameter("tp" + list_tp + "_value" + i);
                        // 类型备注
                        String list_tpnote = request.getParameter("tp" + list_tp + "_note" + i);

                        String list_imgurl = request.getParameter("list_imgurl" + i);

                        map.put("list_title", list_title);
                        map.put("list_note", list_note);
                        map.put("list_tp", list_tp);
                        map.put("list_tpvalue", list_tpvalue);
                        map.put("list_tpnote", list_tpnote);
                        map.put("list_imgurl", list_imgurl);
                    } else if ("normal_menu".equals(plantType)) {
                        String menu_title = request.getParameter("menu_title" + i);
                        String menu_tp = request.getParameter("menu_tp" + i);
                        String menu_fontcolor = request.getParameter("menu_fontcolor" + i);
                        String menu_bgcolor = request.getParameter("menu_bgcolor" + i);
                        // 类型值
                        String menu_tpvalue = request.getParameter("tp" + menu_tp + "_value" + i);
                        // 类型备注
                        String menu_tpnote = request.getParameter("tp" + menu_tp + "_note" + i);

                        map.put("menu_title", menu_title);
                        map.put("menu_tp", menu_tp);
                        map.put("menu_tpvalue", menu_tpvalue);
                        map.put("menu_tpnote", menu_tpnote);
                        map.put("menu_fontcolor", menu_fontcolor);
                        map.put("menu_bgcolor", menu_bgcolor);
                    } else if ("normal_tab".equals(plantType)) {
                        String tab_content = request.getParameter("tab_content" + i);
                        String tab_name = request.getParameter("tab_name" + i);
                        String tab_title = request.getParameter("tab_title" + i);
                        String tab_imgurl = request.getParameter("tab_imgurl" + i);

                        map.put("tab_content", tab_content);
                        map.put("tab_name", tab_name);
                        map.put("tab_title", tab_title);
                        map.put("tab_imgurl", tab_imgurl);
                    } else if ("normal_content".equals(plantType)) {
                        String content_imgurl = request.getParameter("content_imgurl");
                        String content_body = request.getParameter("content_body");
                        String content_title = request.getParameter("content_title");

                        map.put("content_imgurl", content_imgurl);
                        map.put("content_body", content_body);
                        map.put("content_title", content_title);
                    } else if ("normal_scontent".equals(plantType)) {
                        String content_imgurl = request.getParameter("content_imgurl");
                        String content_title = request.getParameter("content_title");

                        map.put("content_imgurl", content_imgurl);
                        map.put("content_title", content_title);
                    }

                    values.add(map);
                }
                // 更新控件字段值
                boolean isupdate = siteBiz.updatePlantValues(plantType, corpCode, plantId, values);
                if (isupdate) {
                    result = "success";
                    EmpExecutionContext.info("保存信息成功!");
                } else {
                    result = "fail";
                }
            }
        } catch (Exception e) {
            result = "fail";
            EmpExecutionContext.error(e, "保存信息成功！");
        } finally {
            response.getWriter().print(result);
        }
    }

    /**
     * 上传图片文件到web服务器
     *
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-2 上午11:51:40
     */
    @SuppressWarnings("unchecked")
    public void sendPic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = request.getParameter("fileName");

        String msgtype = "image";

        String[] path = siteBiz.getResourceUrl(msgtype);
        // 上传文件到web服务器
        String result = siteBiz.uploadToServer(path, msgtype, fileName, request);

        JSONObject resultObj = new JSONObject();
        resultObj.put("filepath", path[1]);
        resultObj.put("result", result);
        response.getWriter().print(resultObj.toString());
    }

    /**
     * 获取微站的所有页面
     *
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2014-1-4 下午03:11:43
     */
    public void getSitePages(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String corpCode = request.getParameter("lgcorpcode");
        String siteId = request.getParameter("sId");
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

        // 分页信息
        PageInfo pageInfo = new PageInfo();
        pageSet(pageInfo, request);
        pageInfo.setPageSize(10);

        try {
            conditionMap.put("corpCode", corpCode);
            conditionMap.put("sId", siteId);

            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("sId", StaticValue.DESC);
            List<LfSitPage> sitPageList = baseBiz.getByConditionNoCount(LfSitPage.class, null, conditionMap, orderbyMap, pageInfo);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("sitPageList", sitPageList);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "错误提示！");
        } finally {
            request.getRequestDispatcher(PATH + "/wzgl_artDialogPages.jsp").forward(request, response);
        }
    }

    /**
     * 获取微站的信息，放回json格式数据
     *
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-16 上午10:15:21
     */
    @SuppressWarnings("unchecked")
    public void getSiteInfo(HttpServletRequest request, HttpServletResponse response) {
        // 微站ID
        String siteid = request.getParameter("siteid");
        response.setContentType("text/html");
        JSONObject siteObj = new JSONObject();
        try {
            // 查找对应的微站信息
            LfSitInfo site = baseBiz.getById(LfSitInfo.class, siteid);
            if (site != null) {
                siteObj.put("sid", site.getSId());
                siteObj.put("name", site.getName());
                siteObj.put("url", site.getUrl());
            }
            // 返回名称
            response.getWriter().print(siteObj.toString());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取微站信息失败！");
        }
    }

}
