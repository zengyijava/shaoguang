/**
 * Program  : app_morecordselectSvt.java
 * Author   : zousy
 * Create   : 2014-6-12 上午11:38:16
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.appmage.svt;

import com.montnets.emp.appmage.biz.app_homeeditBiz;
import com.montnets.emp.appmage.biz.app_msgSendBiz;
import com.montnets.emp.appwg.biz.WgMwCommuteBiz;
import com.montnets.emp.appwg.biz.WgMwFileBiz;
import com.montnets.emp.appwg.wginterface.IWgMwCommuteBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.appmage.LfAppSitInfo;
import com.montnets.emp.entity.appmage.LfAppSitPlant;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * APP首页
 *
 * @author zousy <zousy999@qq.com>
 * @version 1.0.0
 * @2014-6-12 上午11:38:16
 */
public class app_homeeditSvt extends BaseServlet {
    private static final String empRoot = "appmage";
    private static final String base = "/homeedit";
    private static final app_homeeditBiz homeBiz = new app_homeeditBiz();
    private static final GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
    private static final BaseBiz baseBiz = new BaseBiz();
    private static final WgMwFileBiz fileBiz = new WgMwFileBiz();
    private static final IWgMwCommuteBiz commuteBiz = new WgMwCommuteBiz();
    private static final app_msgSendBiz msgSendBiz = new app_msgSendBiz();

    public void find(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        //查询开始时间
        long stratTime = System.currentTimeMillis();
        PageInfo pageInfo = new PageInfo();
        try {
            pageSet(pageInfo, request);
            LinkedHashMap<String, String> conditionMap = getCondition(request,
                    new String[]{"sid", "name", "creater", "starttime",
                            "endtime", "status"});
            List list = homeBiz.getSitInfos(conditionMap, pageInfo);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("list", list);

            //企业编码
            String corpCode = null;
            //当前操作员ID
            String userId = null;
            //当前操作员登录名
            String userName = null;
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                if (loginSysuser != null && !"".equals(loginSysuser.getCorpCode())) {
                    corpCode = loginSysuser.getCorpCode();
                }
                if (loginSysuser != null && loginSysuser.getUserId() != null) {
                    userId = String.valueOf(loginSysuser.getUserId());
                }
                if (loginSysuser != null && !loginSysuser.getUserName().equals("") && loginSysuser.getUserName() != null) {
                    userName = loginSysuser.getUserName();
                }
            }

            //查询出的数据的总数量
            int totalCount = pageInfo.getTotalRec();
            //日志信息
            String opContent = "开始时间：" + sdf.format(stratTime) + " 耗时：" +
                    (System.currentTimeMillis() - stratTime) + "ms  数量：" + totalCount;

            EmpExecutionContext.info("APP首页", corpCode, userId, userName, opContent, "GET");

        } catch (Exception e) {
            EmpExecutionContext.error(e, "APP首页编辑页面查询异常！");
            request.setAttribute("findresult", "-1");
            request.setAttribute("pageInfo", pageInfo);
        } finally {
            request.getRequestDispatcher(
                    empRoot + base + "/app_homelist.jsp").forward(request,
                    response);
        }
    }

    /**
     * 更新首页名称
     *
     * @param request
     * @param response
     * @return void
     * @throws IOException
     * @Title: updateInfo
     * @Description: TODO
     */
    public void updateInfo(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String lgcorpcode = request.getParameter("lgcorpcode");
        String sid = request.getParameter("sid");
        String name = request.getParameter("name");
        boolean result = false;
        try {
            if (StringUtils.isNotBlank(lgcorpcode)
                    && StringUtils.isNotBlank(sid)) {
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("corpcode", lgcorpcode);
                conditionMap.put("sid", sid);
                LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
                objectMap.put("name", StringEscapeUtils.escapeSql(name));
                result = baseBiz.update(LfAppSitInfo.class, objectMap,
                        conditionMap);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "更新首页名称异常！");
        }
        out.print(result);

    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description 新增
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-6-17 下午03:01:12
     */
    public void toAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        request.setAttribute("validity", sdf.format(calendar.getTime()));
        String appCode = commuteBiz.getAppECode();
        request.setAttribute("appCode", appCode);
        request.getRequestDispatcher(empRoot + base + "/app_homeedit.jsp")
                .forward(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws JSONException
     * @description 保存
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-6-18 上午11:06:30
     */
    public void save(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        Long result = 0L;
        PrintWriter out = response.getWriter();
        String json = request.getParameter("json");
        String lgcorpcode = request.getParameter("lgcorpcode");
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        String sid = request.getParameter("sid");
        String infoname = request.getParameter("infoname");
        try {
            if (StringUtils.isNotBlank(json)
                    && StringUtils.isNotBlank(lgcorpcode)
                    && StringUtils.isNotBlank(lguserid)) {
                JSONObject obj = new JSONObject(json);
                result = homeBiz.saveOrCache(1, lgcorpcode, Long
                        .parseLong(lguserid), obj, sid, infoname);
            }

            // 增加操作日志
            String opContent = null;
            if (result > 0 && result != null) {
                opContent = "新建APP保存成功。";
            } else {
                opContent = "新建APP保存失败。";
            }
            Object loginSysuserObj = request.getSession(false).getAttribute(
                    "loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                opContent += "(ID：" + sid + ",信息名称：" + infoname
                        + ")";
                EmpExecutionContext.info("APP首页发布", loginSysuser.getCorpCode(),
                        loginSysuser.getUserId().toString(), loginSysuser
                                .getUserName(), opContent, "OTHER");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "保存APP首页信息出现异常！");
        }
        out.print(result);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws JSONException
     * @description 暂存
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-6-19 上午11:49:29
     */
    public void cache(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        Long result = 0L;
        PrintWriter out = response.getWriter();
        String json = request.getParameter("json");
        String lgcorpcode = request.getParameter("lgcorpcode");
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);


        String sid = request.getParameter("sid");
        String infoname = request.getParameter("infoname");
        try {
            if (StringUtils.isNotBlank(json)
                    && StringUtils.isNotBlank(lgcorpcode)
                    && StringUtils.isNotBlank(lguserid)) {
                JSONObject obj = new JSONObject(json);
                // 取消草稿状态 暂存和保存一样 状态为待发布
                result = homeBiz.saveOrCache(1, lgcorpcode, Long
                        .parseLong(lguserid), obj, sid, infoname);
                // 增加操作日志
                Object loginSysuserObj = request.getSession(false).getAttribute(
                        "loginSysuser");
                if (loginSysuserObj != null) {
                    LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                    String opContent = "新建APP暂存(ID：" + sid + ",信息名称："
                            + infoname + ",对象:" + obj.toString() + ")。返回信息result=" + result;
                    EmpExecutionContext.info("APP首页发布", loginSysuser
                                    .getCorpCode(),
                            loginSysuser.getUserId().toString(), loginSysuser
                                    .getUserName(), opContent, "OTHER");
                }
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "暂存APP首页信息出现异常！");
        }
        out.print(result);

    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws JSONException
     * @description 编辑界面 发布
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-6-23 上午11:44:02
     */
    public void publishInEdit(HttpServletRequest request,
                              HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        int result = -1;
        PrintWriter out = response.getWriter();
        String json = request.getParameter("json");
        String lgcorpcode = request.getParameter("lgcorpcode");
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);


        String validity = request.getParameter("validity");
        String sid = request.getParameter("sid");
        String infoname = request.getParameter("infoname");
        try {
            if (StringUtils.isNotBlank(json)
                    && StringUtils.isNotBlank(lgcorpcode)
                    && StringUtils.isNotBlank(lguserid)) {
                JSONObject obj = new JSONObject(json);
                result = homeBiz.saveInPublish(lgcorpcode, Long
                        .valueOf(lguserid), obj, sid, validity, infoname);
                // 增加操作日志
                Object loginSysuserObj = request.getSession(false).getAttribute(
                        "loginSysuser");
                if (loginSysuserObj != null) {
                    LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                    String opContent = "新建发布APP(ID：" + sid + ",信息名称："
                            + infoname + ",对象:" + obj.toString() + ")。返回信息result=" + result;
                    EmpExecutionContext.info("APP首页发布", loginSysuser
                                    .getCorpCode(),
                            loginSysuser.getUserId().toString(), loginSysuser
                                    .getUserName(), opContent, "OTHER");
                }
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "发布APP首页信息出现异常！");
        }
        out.print(result);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description 修改 或 复制
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-6-17 下午03:01:05
     */
    public void toUpdate(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        List<LfAppSitPlant> plants = null;
        try {
            String lgcorpcode = request.getParameter("lgcorpcode");
            String sid = request.getParameter("sid");
            LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
            condMap.put("corpcode", lgcorpcode);
            condMap.put("sid", sid);
            plants = baseBiz.findListByCondition(LfAppSitPlant.class, condMap,
                    null);
            String appCode = commuteBiz.getAppECode();
            request.setAttribute("appCode", appCode);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询页面元素出现异常！");
        } finally {
            request.setAttribute("plants", plants);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1);
            request.setAttribute("validity", sdf.format(calendar.getTime()));
            request.getRequestDispatcher(
                    empRoot + base + "/app_homeedit.jsp").forward(request,
                    response);
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description 加载app首页页面元素
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-6-20 上午09:18:17
     */
    public void getJson(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String lgcorpcode = request.getParameter("lgcorpcode");
        String sid = request.getParameter("sid");
        LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
        condMap.put("corpcode", lgcorpcode);
        condMap.put("sid", sid);
        JSONObject jsonObject = new JSONObject();
        try {
            List<LfAppSitInfo> infos = new BaseBiz().findListByCondition(
                    LfAppSitInfo.class, condMap, null);
            if (infos != null && infos.size() > 0) {
                jsonObject.put("infoname", infos.get(0).getName());
            }
            // 增加排序显示
            orderMap.put("plantid", StaticValue.ASC);
            List<LfAppSitPlant> plants = new BaseBiz().findListByCondition(
                    LfAppSitPlant.class, condMap, orderMap);
            JSONArray heads = new JSONArray();
            JSONArray lists = new JSONArray();
            for (LfAppSitPlant plant : plants) {
                JSONObject item = new JSONObject(plant.getFeildvalues());
                if ("head".equals(plant.getPlanttype())) {
                    heads.put(item);
                }
                if ("list".equals(plant.getPlanttype())) {
                    lists.put(item);
                }
            }
            jsonObject.put("heads", heads);
            jsonObject.put("lists", lists);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询页面元素出现异常！");
        } finally {
            out.print(jsonObject.toString());
        }
    }

    /**
     * 发布
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-6-20 下午05:27:31
     */
    public void publish(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        int result = -1;
        PrintWriter out = response.getWriter();
        String lgcorpcode = request.getParameter("lgcorpcode");
        String sid = request.getParameter("sid");
        LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
        condMap.put("corpcode", lgcorpcode);
        condMap.put("sid", sid);
        List<LfAppSitPlant> plants = new ArrayList<LfAppSitPlant>();
        try {
            plants = new BaseBiz().findListByCondition(LfAppSitPlant.class,
                    condMap, null);
//			System.out.println(plants);
            result = homeBiz.publish(plants, lgcorpcode, sid, null);

            // 增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute(
                    "loginSysuser");
            if (loginSysuserObj != null) {
                String infoname = null;
                List<LfAppSitInfo> infos = new BaseBiz().findListByCondition(
                        LfAppSitInfo.class, condMap, null);
                if (infos != null && infos.size() > 0) {
                    infoname = infos.get(0).getName();
                }
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String opContent = "APP首页发布成功(ID：" + sid + ",信息名称：" + infoname
                        + ")。返回信息result=" + result;
                EmpExecutionContext.info("APP首页发布", loginSysuser.getCorpCode(),
                        loginSysuser.getUserId().toString(), loginSysuser
                                .getUserName(), opContent, "OTHER");
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "APP首页发布出现异常！");
            result = -1;
        }
        out.print(result);

    }

    /**
     * APP首页发布异步取消
     *
     * @param request
     * @param response
     * @return void
     * @throws ServletException
     * @throws IOException
     * @Title: unpublish
     * @Description: TODO
     */
    public void unpublish(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        int result = -1;
        PrintWriter out = response.getWriter();
        String lgcorpcode = request.getParameter("lgcorpcode");
        String sid = request.getParameter("sid");
        try {
            if (StringUtils.isNotBlank(lgcorpcode)
                    && StringUtils.isNotBlank(sid)) {
                result = homeBiz.unpublish(lgcorpcode, sid);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "APP首页取消发布异常！");
            result = -1;
        } finally {
            out.print(result);
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description 删除app首页
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-6-17 下午01:59:59
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String sid = request.getParameter("sid");
        boolean result = false;
        if (StringUtils.isNotBlank(sid)) {
            result = homeBiz.delete(sid);
        }
        //增加操作日志
        try {
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String opContent = "删除APP(ID：" + sid + ")。";
                EmpExecutionContext.info("APP首页发布", loginSysuser.getCorpCode(),
                        loginSysuser.getUserId().toString(), loginSysuser.getUserName(), opContent, "DELETE");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "从Session取出用户信息出现异常！");
        }
        out.print(result);
    }

    /*
     * 上传单图片
     */
    public void uploadImg(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        // 图片存放的根目录
        String baseFileDir = getServletContext().getRealPath(
                "/file/apphome");
        File baseFile = new File(baseFileDir);
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }
        // 上传文件最大大小
        long maxSize = 5 * 1024 * 1024L;
        String appCode = "appCode";
        response.setContentType("text/html");
        // 构造出文件工厂，用于存放JSP页面中传递过来的文件
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置缓存大小，如果文件大于缓存大小时，则先把文件放到缓存中
        factory.setSizeThreshold(1024 * 1024);
        // 设置上传文件的保存路径
        factory.setRepository(new File(baseFileDir));
        // 产生Servlet上传对象
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置可以上传文件大小的上界20MB
        upload.setSizeMax(20 * 1024 * 1024L);
        // 取得所有上传文件的信息
        List<FileItem> fileList = null;
        // 上传图片保存url路径
        String imgUrl = "";
        // 上传文件
        FileItem fileItem = null;
        // 文件扩展名
        String extName = "";
        // 文件类型
        String allImgExt = ".jpg|.jpeg|.bmp|.png|";
        PrintWriter out = response.getWriter();
        try {
            fileList = upload.parseRequest(request);
        } catch (FileUploadException e) {
            EmpExecutionContext.error(e, "APP首页编辑解析上传对象异常！");
            out.print("parseError");
            return;
        }
        Iterator<FileItem> iter = fileList.iterator();
        // 解析上传内容信息
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            if (!item.isFormField() && item.getName().length() > 0) {
                // 获得文件大小
                long fileSize = item.getSize();
                // 判断文件大小是否超过限制
                if (fileSize > maxSize) {
                    item.delete();
                    item = null;
                    out.print("overSize");
                    return;
                } else {
                    // 处理上传信息
                    String fileName = item.getName();
                    extName = fileName.substring(fileName.lastIndexOf("."),
                            fileName.length()).toLowerCase();
                    // 文件格式判断
                    if (allImgExt.indexOf(extName + "|") == -1) {
                        out.print("fileError");
                        return;
                    } else {
                        fileItem = item;
                    }
                }
            } else {
                if ("appcode".equals(item.getFieldName())) {
                    appCode = item.getString("UTF-8").toString();
                }
            }
        }
        // 如果获取到正确的上传文件信息
        if (fileItem != null) {
            String dirpath = baseFileDir + File.separator;
            Calendar cal = Calendar.getInstance();
            // 年
            int year = cal.get(Calendar.YEAR);
            // 月
            int month = cal.get(Calendar.MONTH) + 1;
            // 日
            int day = cal.get(Calendar.DAY_OF_MONTH);

            String filepath = dirpath + File.separator + year + File.separator
                    + month + File.separator + day;
            File uf = new File(filepath);
            if (!uf.exists()) {
                uf.mkdirs();
            }
            try {
                String strid = UUID.randomUUID().toString();
                String newFileName = strid.replaceAll("-", "")
                        + StaticValue.getServerNumber() + extName;
                File uploadedFile = new File(filepath, newFileName);
                if (uploadedFile.exists()) {

                    boolean state = uploadedFile.delete();
                    if (!state) {
                        EmpExecutionContext.error("删除失败！");
                    }
                }
                // 按尺寸缩放图片并写入文件
                writeImg(fileItem, null, null, uploadedFile);
                // fileItem.write(uploadedFile);
                imgUrl = "file/apphome/" + year + "/" + month + "/" + day + "/"
                        + newFileName;
                // 配置了app文件服务器 则上传到平台
                // 上传文件到app平台文件服务器
                String result = fileBiz.uploadToMwFileSer(uploadedFile
                        .getPath(), "10", appCode);
                if (result != null && result.length() > 1) {
                    imgUrl += "?bak=" + result;
                } else {
                    EmpExecutionContext.error("上传文件至APP文件服务器异常！");
                    // 上传文件失败 删除本地服务器文件
                    if (uploadedFile.exists()) {
                        boolean state = uploadedFile.delete();
                        if (!state) {
                            EmpExecutionContext.error("删除失败！");
                        }
                    }
                    out.print("uploadError");
                    return;
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "APP首页编辑上传图像失败！");
                out.print("uploadError");
                return;
            }
        }

        out.print(imgUrl);
    }

    public LinkedHashMap<String, String> getCondition(
            HttpServletRequest request, String[] cons) {
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        for (String key : cons) {
            String value = request.getParameter(key);
            if (StringUtils.isNotBlank(value)) {
                conditionMap.put(key, value);
            }
        }
        conditionMap.put("lgcorpcode", StringUtils.isNotBlank(request
                .getParameter("lgcorpcode")) ? request
                .getParameter("lgcorpcode") : "-1");
        return conditionMap;
    }

    /**
     * 根据特定尺寸缩放图片
     *
     * @param item
     * @param maxWidth
     * @param maxHeight
     * @param uploadFile
     * @return void
     * @throws Exception
     * @throws
     * @Title: writeImg
     * @Description: TODO
     * @date 2015-8-24 下午12:01:17
     */
    public void writeImg(FileItem item, Integer maxWidth, Integer maxHeight,
                         File uploadFile) throws Exception {
        BufferedImage sourceImg = ImageIO.read(item.getInputStream());
        // 不超过超过给定宽高的 直接写文件
        if ((maxWidth == null || sourceImg.getWidth(null) < maxWidth)
                && (maxHeight == null || sourceImg.getHeight(null) < maxHeight)) {
            item.write(uploadFile);
            return;
        }
        // 为等比缩放计算输出的图片宽度及高度
        double rate1 = ((double) sourceImg.getWidth(null)) / (double) maxWidth
                + 0.1;
        double rate2 = ((double) sourceImg.getHeight(null))
                / (double) maxHeight + 0.1;
        // 根据缩放比率大的进行缩放控制
        double rate = rate1 > rate2 ? rate1 : rate2;
        int newWidth = (int) (((double) sourceImg.getWidth(null)) / rate);
        int newHeight = (int) (((double) sourceImg.getHeight(null)) / rate);
        BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight,
                BufferedImage.TYPE_INT_RGB);

        /*
         * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
         */
        tag.getGraphics().drawImage(
                sourceImg.getScaledInstance(newWidth, newHeight,
                        Image.SCALE_SMOOTH), 0, 0, null);
        FileOutputStream out = new FileOutputStream(uploadFile);
        // JPEGImageEncoder可适用于其他图片类型的转换
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(tag);
        out.close();
    }
}
