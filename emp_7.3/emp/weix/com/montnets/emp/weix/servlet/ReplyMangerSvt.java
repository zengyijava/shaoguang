package com.montnets.emp.weix.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.weix.*;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.weix.biz.ReplyBiz;
import com.montnets.emp.weix.common.util.DateFormatter;
import com.montnets.emp.weix.common.util.GlobalMethods;
import com.montnets.emp.weix.common.util.TitleImgBean;
import com.montnets.emp.weix.dao.TempleDao;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author chensj
 */
public class ReplyMangerSvt extends BaseServlet {

    private final String opModule = "回复管理";
    private final String empRoot = "weix";
    private final String basePath = "/temple";
    private final BaseBiz baseBiz = new BaseBiz();

    /**
     * 查询回复模板
     *
     * @param request
     * @param response
     * @description
     * @author zousy <zousy999@qq.com>
     * @datetime 2013-9-26 下午07:32:07
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PageInfo pageInfo = new PageInfo();
        TempleDao tDao = new TempleDao();
        // 是否首次请求
        boolean isFirstEnter;
        String lgcorpcode = request.getParameter("lgcorpcode");
        try {
            isFirstEnter = pageSet(pageInfo, request);
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            List<LfWcAccount> accoutlist = baseBiz.getByCondition(LfWcAccount.class, conditionMap, null);
            if (!isFirstEnter) {
                // 模板类型
                String tempType = request.getParameter("tempType");
                // 起始时间
                String startdate = request.getParameter("startdate");
                // 结束时间
                String enddate = request.getParameter("enddate");
                // 公众账号ID
                String accoutid = request.getParameter("accoutid");
                // 关键字
                String serkey = request.getParameter("serkey");
                // 回复内容
                String serReply = request.getParameter("serReply");
                if (tempType != null && !"".equals(tempType.trim())) {
                    conditionMap.put("tempType", tempType.trim());
                }

                if (startdate != null && !"".equals(startdate)) {
                    conditionMap.put("startdate", startdate);
                }
                if (enddate != null && !"".equals(enddate)) {
                    conditionMap.put("enddate", enddate);
                }
                if (accoutid != null && !"".equals(accoutid.trim())) {
                    conditionMap.put("accoutid", accoutid.trim());
                }
                if (serkey != null && !"".equals(serkey.trim())) {
                    conditionMap.put("serkey", serkey.trim());
                }
                if (serReply != null && !"".equals(serReply.trim())) {
                    conditionMap.put("serReply", serReply.trim());

                }
            }
            if (!GlobalMethods.isInvalidString(lgcorpcode)) {

                conditionMap.put("lgcorpcode", lgcorpcode);
            }
            List<DynaBean> beans = tDao.getBaseInfos(conditionMap, pageInfo);
            request.setAttribute("templelist", beans);
            request.setAttribute("accoutlist", accoutlist);
            request.setAttribute("pageInfo", pageInfo);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取回复管理失败！");
        } finally {
            request.getRequestDispatcher(empRoot + basePath + "/tmpList.jsp").forward(request, response);
        }
    }

    /**
     * 新建文本回复
     *
     * @param request
     * @param response
     */
    public void newText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lgcorpcode = request.getParameter("lgcorpcode");
        try {
            BaseBiz baseBiz = new BaseBiz();
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            List<LfWcAccount> accoutlist = baseBiz.getByCondition(LfWcAccount.class, conditionMap, null);
            request.setAttribute("accoutlist", accoutlist);
        } catch (ServletException e) {
            EmpExecutionContext.error(e, "新建文本回复Servlet异常！");
        } catch (Exception e) {
            EmpExecutionContext.error(e, "新建文本回失败！");
        } finally {
            request.getRequestDispatcher(empRoot + basePath + "/newTextReply.jsp").forward(request, response);
        }
    }

    /**
     * 保存文本回复
     * 2013-7-31
     *
     * @param request
     * @param response void
     * @throws IOException
     */
    public void saveTextReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String words = request.getParameter("words");
        String accuntid = request.getParameter("accuntnum");
        String replyname = request.getParameter("replyname");
        String replycont = request.getParameter("replycont");
        String lgcorpcode = request.getParameter("lgcorpcode");
        ReplyBiz replyBiz = new ReplyBiz();
        String remsg = replyBiz.saveTextReply(words, accuntid, replyname, replycont, lgcorpcode);

        Object sysObj = request.getSession().getAttribute("loginSysuser");
        LfSysuser lfSysuser = (LfSysuser) sysObj;
        String opContent = "保存文本（回复名称：" + replyname + "）回复";

        if (remsg != null && "success".equals(remsg)) {
            opContent += "成功";
            //EmpExecutionContext.info("模块名称：回复管理，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，保存文本（回复名称："+replyname+"）回复成功");
        } else {
            opContent += "失败";
        }

        EmpExecutionContext.info(opModule, lfSysuser.getCorpCode(),
                lfSysuser.getUserId().toString(), lfSysuser.getUserName(),
                opContent, StaticValue.ADD);
        response.getWriter().print(remsg);
    }

    /**
     * 编辑文本 图文
     * 2013-7-31
     *
     * @param request
     * @param response
     */
    public void doEditemp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tid = request.getParameter("tid");
        String msgtype = request.getParameter("msgtype");
        String lgcorpcode = request.getParameter("lgcorpcode");
        TempleDao tDao = new TempleDao();
        BaseBiz baseBiz = new BaseBiz();
        String replname = "";
        String keywords = "";
        // 跳转页面
        String page = "/ediTextReply.jsp";
        List<LfWcAccount> accoutlist = new ArrayList<LfWcAccount>();
        LfWcTemplate te = new LfWcTemplate();
        if (GlobalMethods.isInvalidString(msgtype)) {
            msgtype = "0";
        }
        if ("2".equals(msgtype)) {
            page = "/editMutiImgTmp.jsp";
        }
        if ("1".equals(msgtype)) {
            page = "/editSingleImgTmp.jsp";
        }
        try {
            // 回复名称
            replname = URLDecoder.decode(request.getParameter("replname"), "UTF-8");
            // 公众账号
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            accoutlist = baseBiz.getByCondition(LfWcAccount.class, conditionMap, null);
            // 模板的关键字
            keywords = tDao.getkeyname(tid);
            // 模板
            te = baseBiz.getById(LfWcTemplate.class, tid);
            Long currid = te.getAId();
            request.setAttribute("keywords", keywords);
            request.setAttribute("replname", replname);
            request.setAttribute("tid", tid);
            request.setAttribute("accoutlist", accoutlist);
            request.setAttribute("currid", currid);
            if ("0".equals(msgtype)) {
                // 文本
                int a = te.getMsgText().trim().length();
                request.setAttribute("msgtext", te.getMsgText());
                request.setAttribute("msgleng", a);
            } else {
                // 图文
                List<LfWcRimg> echorimgs = TempleDao.getLfWcRimgbyids(te.getRimgids());
                String time = "";
                Timestamp tamp = te.getModifytime();
                if (tamp == null) {
                    tamp = te.getCreatetime();
                }
                time = DateFormatter.DateToString(tamp);
                request.setAttribute("echorimgs", echorimgs);
                request.setAttribute("time", time);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "编辑回复内容跳转发生错误！");
        } finally {
            request.getRequestDispatcher(empRoot + basePath + page).forward(request, response);
        }
    }

    /**
     * 保存编辑后的文本信息
     * 2013-7-31
     *
     * @param request
     * @param response void
     * @throws IOException
     */
    public void editTextReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String words = request.getParameter("words");
        String accoutid = request.getParameter("accoutid");
        String replyname = request.getParameter("replyname");
        String replycont = request.getParameter("replycont");
        String lgcorpcode = request.getParameter("lgcorpcode");
        String accuntnum = request.getParameter("accuntnum");
        ReplyBiz replyBiz = new ReplyBiz();
        boolean flag = replyBiz.editTextReply(words, accoutid, replyname, replycont, lgcorpcode, accuntnum);
        if (flag) {
            Object sysObj = request.getSession().getAttribute("loginSysuser");
            if (sysObj != null) {
                LfSysuser lfSysuser = (LfSysuser) sysObj;
                EmpExecutionContext.info(opModule, lfSysuser.getCorpCode(),
                        lfSysuser.getUserId().toString(), lfSysuser.getUserName(),
                        "保存编辑后的文本信息（回复名称：" + replyname + "）回复成功", StaticValue.UPDATE);
                //EmpExecutionContext.info("模块名称：回复管理，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，保存编辑后的文本信息（回复名称："+replyname+"）成功");
            }
            response.getWriter().print("success");
        } else {
            Object sysObj = request.getSession().getAttribute("loginSysuser");
            if (sysObj != null) {
                LfSysuser lfSysuser = (LfSysuser) sysObj;
                EmpExecutionContext.error("模块名称：" + opModule + "，企业：" + lfSysuser.getCorpCode() + "，操作员：" + lfSysuser.getUserId() + "[" + lfSysuser.getUserName() + "]，保存编辑后的文本信息（回复名称：" + replyname + "）失败");
            }
            response.getWriter().print("fail");
        }
    }

    /**
     * 删除模板
     * 2013-7-31
     *
     * @param request
     * @param response void
     * @throws IOException
     */
    public void delTemple(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tempIds = request.getParameter("tempIds");
        ReplyBiz replyBiz = new ReplyBiz();
        String remsg = replyBiz.delTemplebiz(tempIds);
        if ("sccuess".equals(remsg)) {
            Object sysObj = request.getSession().getAttribute("loginSysuser");
            if (sysObj != null) {
                LfSysuser lfSysuser = (LfSysuser) sysObj;

                EmpExecutionContext.info(opModule, lfSysuser.getCorpCode(),
                        lfSysuser.getUserId().toString(), lfSysuser.getUserName(),
                        "删除模板（模板id：" + tempIds + "）成功", StaticValue.DELETE);
                //EmpExecutionContext.info("模块名称：回复管理，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，删除模板（模板id："+tempIds+"）成功");
            }
            response.getWriter().print("sccuess");
        } else {
            Object sysObj = request.getSession().getAttribute("loginSysuser");
            if (sysObj != null) {
                LfSysuser lfSysuser = (LfSysuser) sysObj;
                EmpExecutionContext.error("模块名称：" + opModule + "，企业：" + lfSysuser.getCorpCode() + "，操作员：" + lfSysuser.getUserId() + "[" + lfSysuser.getUserName() + "]，删除模板（模板id：" + tempIds + "）失败");
            }
            response.getWriter().print("fail");
        }
    }

    /**
     * 判断该模板是否被关联到了
     * 2013-9-5
     * void
     *
     * @throws IOException
     */
    public void checkRelated(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tempid = request.getParameter("tempid");
        ReplyBiz replyBiz = new ReplyBiz();
        String remsg = replyBiz.checkRelated(tempid);
        if ("true".equals(remsg)) {
            response.getWriter().print("true");
        } else {
            response.getWriter().print("false");
        }
    }

    /**
     * 返回已经关联的模板ID和没有关联的模板ID(k10002,10001-k)
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void getRelatedTemples(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tempid = request.getParameter("tempid");
        ReplyBiz replyBiz = new ReplyBiz();
        String remsg = replyBiz.getRelatedTemples(tempid);
        response.getWriter().print(remsg);
    }

    /**
     * 回复预览
     *
     * @param request
     * @param response
     */
    public void preview(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ReplyBiz replyBiz = new ReplyBiz();
        try {
            // 回复管理中的模板ID
            String tempId = request.getParameter("tempId");
            // 关注时回复ID
            String evtId = request.getParameter("evtId");
            // 默认回复ID
            String tetId = request.getParameter("tetId");
            // 回复类型（0：文本，1:单图文，2：多图文）
            String msgtype = "0";
            String textRely = "";
            String time = "";
            Timestamp tamp;
            // 是否包含模板
            boolean hasTemplate = true;
            List<TitleImgBean> imgbeans = null;
            // 根据参数的值 先处理默认回复和关注时回复中不关联模板的情况 若关联模板则只需找到相应的模板进行处理即可
            // 默认回复的预览
            if (!GlobalMethods.isInvalidString(tetId)) {
                LfWcRtext rtext = baseBiz.getById(LfWcRtext.class, tetId);
                if (rtext != null) {
                    Long tid = rtext.getTId();
                    // 不关联模板
                    if (tid == null || tid.intValue() == 0) {
                        hasTemplate = false;
                        textRely = rtext.getMsgText();
                        tamp = rtext.getModifytime();
                        if (tamp == null) {
                            tamp = rtext.getCreatetime();
                        }
                        time = DateFormatter.DateToString(tamp);
                    }// 关联模版
                    else {
                        tempId = String.valueOf(tid);
                    }
                }
            }// 关注时回复的预览
            else if (!GlobalMethods.isInvalidString(evtId)) {
                LfWcRevent revent = baseBiz.getById(LfWcRevent.class, evtId);
                if (revent != null) {
                    Long tid = revent.getTId();
                    // 不关联模板
                    if (tid == null || tid.intValue() == 0) {
                        hasTemplate = false;
                        textRely = revent.getMsgText();
                        tamp = revent.getModifytime();
                        if (tamp == null) {
                            tamp = revent.getCreatetime();
                        }
                        time = DateFormatter.DateToString(tamp);
                    }// 关联模版
                    else {
                        tempId = String.valueOf(tid);
                    }
                }
            }
            // 当默认回复以及关注时回复id均为空或者有关联模板的情况 则统一根据模板id处理模板
            if (hasTemplate && !GlobalMethods.isInvalidString(tempId)) {
                // 回复管理中的模板ID
                LfWcTemplate template = baseBiz.getById(LfWcTemplate.class, tempId);
                msgtype = String.valueOf(template.getMsgType());
                tamp = template.getModifytime();
                if (tamp == null) {
                    tamp = template.getCreatetime();
                }
                time = DateFormatter.DateToString(tamp);
                textRely = template.getMsgText();
                if (!GlobalMethods.isInvalidString(template.getRimgids())) {
                    imgbeans = replyBiz.getImgByList(TempleDao.getLfWcRimgbyids(template.getRimgids()));
                }
            }
            request.setAttribute("msgType", msgtype);// 预览类型
            request.setAttribute("textRely", textRely);// 文本内容
            request.setAttribute("time", time);// 时间
            request.setAttribute("tempId", tempId);
            request.setAttribute("evtId", evtId);
            request.setAttribute("tetId", tetId);
            request.setAttribute("imgbeans", imgbeans);// 解析图文后的bean
            request.getRequestDispatcher(empRoot + basePath + "/tmpPreview.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, opModule + "-回复预览失败!");
            request.getRequestDispatcher(empRoot + basePath + "/tmpPreview.jsp").forward(request, response);
        }
    }

    public void uploadImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        // 用于存放输出的信息
        String message = "";
        String imgurl = "";

        // 图片存放的根目录
        String baseFileDir = getServletContext().getRealPath(StaticValue.WC_RIMG);
        boolean isOverSize = false;
        // 上传文件最大大小
        long maxSize = 100L * 1024L * 1024L;
        long allFileSize = 0L;

        try {

            // 构造出文件工厂，用于存放JSP页面中传递过来的文件
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置缓存大小，如果文件大于缓存大小时，则先把文件放到缓存中
            factory.setSizeThreshold(1024 * 1024);
            // 设置上传文件的保存路径
            factory.setRepository(new File(baseFileDir));

            // 产生Servlet上传对象
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 设置可以上传文件大小的上界4MB
            upload.setSizeMax(8L * 1024L * 1024L);

            // 取得所有上传文件的信息
            List<FileItem> fileList = upload.parseRequest(request);
            Iterator<FileItem> iter = fileList.iterator();

            while (iter.hasNext()) {
                FileItem item = iter.next();
                // 获得此表单元素的name属性
                String fieldName = item.getFieldName();
                // 如果接收到的参数不是一个普通表单(例text等)的元素，那么执行下面代码
                if (!item.isFormField()) {
                    // 文件名
                    String fileName = item.getName();
                    // 获得文件大小
                    long fileSize = item.getSize();
                    allFileSize += fileSize;
                    // 判断文件大小是否超过限制
                    if (allFileSize > maxSize) {
                        item.delete();
                        item = null;
                        isOverSize = true;
                        break;
                    }

                    // 文件类型
                    String allImgExt = ".jpg|.jpeg|.gif|.bmp|.png|";
                    String extName = fileName.substring(fileName.indexOf("."), fileName.length()).toLowerCase();

                    // 如果是图片文件
                    if (allImgExt.indexOf(extName + "|") != -1) {
                        String dirpath = baseFileDir + File.separator;

                        Calendar cal = Calendar.getInstance();
                        // 年
                        int year = cal.get(Calendar.YEAR);
                        // 月
                        int month = cal.get(Calendar.MONTH) + 1;
                        // 日
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        String filepath = dirpath + File.separator + year + File.separator + month + File.separator + day;

                        File uf = new File(filepath);
                        // 更改文件的保存路径，以防止文件重名的现象出现
                        if (!uf.exists()) {
                            uf.mkdirs();
                        }
                        try {
                            String strid = UUID.randomUUID().toString().replaceAll("-", "");
                            String shortstrid = strid.substring(0, 10);
                            String newFileName = shortstrid + StaticValue.getServerNumber() + extName;
                            File uploadedFile = new File(filepath, newFileName);
                            if (uploadedFile.exists()) {
                                if (!uploadedFile.delete()) {
                                    throw new FileUploadException();
                                }
                            }
                            item.write(uploadedFile);

                            imgurl = StaticValue.WC_RIMG + "/" + year + "/" + month + "/" + day + "/" + newFileName;
                            message = "imgurl:" + imgurl + "," + "fieldName:" + fieldName;
                        } catch (FileUploadException e) {
                            message = "error";
                            EmpExecutionContext.error(e, opModule + "-图片上传失败！");
                        } finally {
                            //使用集群，将文件上传到文件服务器
                            if (StaticValue.getISCLUSTER() == 1) {
                                //上传文件到文件服务器
                                CommonBiz comBiz = new CommonBiz();
                                boolean upFileRes = comBiz.upFileToFileServer(imgurl);
                                if (!upFileRes) {
                                    EmpExecutionContext.error("上传文件到服务器失败。错误码：" + IErrorCode.B20023);
                                }
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            message = "error";
            EmpExecutionContext.error(e, opModule + "-图片上传失败！");
        } finally {
            // 异步返回结果
            response.getWriter().print("@" + message);
        }
    }

    /**
     * 新增或编辑图文
     *
     * @param request
     * @param response
     * @throws IOException
     * @description
     * @author zousy <zousy999@qq.com>
     * @datetime 2013-9-30 上午11:03:01
     */
    public void saveOrUpdateImgReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 服务器资源访问地址(微信图文详情图片访问地址basePath)
        String weixPath = "{basePath}";
        String weixFilePath = "{weixFilePath}";
        // 企业编号
        String lgcorpcode = request.getParameter("lgcorpcode");
        // 模板Id
        String tid = request.getParameter("tid");// 模板id
        // 公众帐号id
        String item_acctnum = request.getParameter("item-acctnum");
        // 回复名称
        String item_name = request.getParameter("item-name");
        // 关键
        String words = request.getParameter("words");
        Long aid = 0L;
        if (!GlobalMethods.isInvalidString(item_acctnum)) {
            aid = Long.parseLong(item_acctnum);
        }
        // 页面图文编号集合
        String item_ids = request.getParameter("itemIds");
        List<LfWcRimg> vorimgs = new ArrayList<LfWcRimg>();
        LfWcRimg rimg;
        if (!GlobalMethods.isInvalidString(item_ids)) {
            String[] items = new String[]{};
            // 标题
            String title = "";
            // 图片
            String url = "";
            // 正文
            String content = "";
            // 连接
            String link = "";
            items = item_ids.split(",");
            for (String s : items) {
                rimg = new LfWcRimg();
                title = request.getParameter("item-title" + s);
                url = request.getParameter("item-url" + s);
                content = request.getParameter("item-content" + s);
                link = request.getParameter("item-link" + s);
                rimg.setTitle(title);
                rimg.setPicurl(url);
                rimg.setDescription(content);
                rimg.setLink(link);
                vorimgs.add(rimg);
            }
        }
        ReplyBiz replyBiz = new ReplyBiz();
        boolean flag = replyBiz.saveOrUpdateReply(vorimgs, lgcorpcode, weixPath, weixFilePath, aid, item_name, words, tid);
        String msg = "";
        if (flag) {
            Object sysObj = request.getSession().getAttribute("loginSysuser");
            if (sysObj != null) {
                LfSysuser lfSysuser = (LfSysuser) sysObj;
                EmpExecutionContext.info(opModule, lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(),
                        lfSysuser.getUserName(), "图文（回复名称：" + item_name + "）操作成功", StaticValue.OTHER);
                //EmpExecutionContext.info("模块名称：回复管理，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，图文（回复名称："+item_name+"）操作成功");
            }
            msg = "@{OPP}图文成功！";
        } else {
            Object sysObj = request.getSession().getAttribute("loginSysuser");
            if (sysObj != null) {
                LfSysuser lfSysuser = (LfSysuser) sysObj;
                EmpExecutionContext.error("模块名称：" + opModule + "，企业：" + lfSysuser.getCorpCode() + "，操作员：" + lfSysuser.getUserId() + "[" + lfSysuser.getUserName() + "]，图文（回复名称：" + item_name + "）操作失败");
            }
            msg = "抱歉，{OPP}图文失败了！";
        }
        if (GlobalMethods.isInvalidString(tid)) {
            msg = msg.replace("{OPP}", "新增");
        } else {
            msg = msg.replace("{OPP}", "修改");
        }
        response.getWriter().print(msg);
    }

    /**
     * 新增图文
     *
     * @param request
     * @param response
     */
    public void addImgTmp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String type = request.getParameter("type");
        String lgcorpcode = request.getParameter("lgcorpcode");
        String page = "";
        // "1"表示多图文
        if (type != null && "1".equals(type)) {
            page = "addMutilImgTmp.jsp";
        } else {
            page = "addSingleImgTmp.jsp";
        }
        try {
            BaseBiz baseBiz = new BaseBiz();
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            List<LfWcAccount> accoutlist = baseBiz.getByCondition(LfWcAccount.class, conditionMap, null);
            request.setAttribute("accoutlist", accoutlist);
        } catch (Exception ex) {
            EmpExecutionContext.error(ex, opModule + "-添加图文页面加载失败！");
        } finally {
            request.getRequestDispatcher(empRoot + basePath + "/" + page).forward(request, response);
        }
    }
}
