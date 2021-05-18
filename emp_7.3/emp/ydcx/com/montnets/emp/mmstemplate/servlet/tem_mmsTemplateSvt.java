package com.montnets.emp.mmstemplate.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.WebgatePropInfo;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.template.LfTmplRela;
import com.montnets.emp.entity.template.MmsTemplate;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.mmstemplate.biz.TemplateBiz;
import com.montnets.emp.samemms.biz.*;
import com.montnets.emp.samemms.vo.LfTemplateVo;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.servmodule.ydcx.constant.ServerInof;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yejiangmin <282905282@qq.com>
 * @project p_ydcx
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-14 下午02:25:03
 * @description
 */
@SuppressWarnings("serial")
public class tem_mmsTemplateSvt extends BaseServlet {

    //操作模块
    private final String opModule = StaticValue.TEMP_MANAGE;
    //操作用户
    private final String opSper = StaticValue.OPSPER;
    private final TemplateBiz mtb = new TemplateBiz();
    private final KeyWordAtom keyWordAtom = new KeyWordAtom();
    private static String dirUrl = null;
    private final TxtFileUtil txtfileutil = new TxtFileUtil();
    private final CreateTmsFile mpb = new CreateTmsFile();
    private final String PATH = "/ydcx/template";
    private final BaseBiz baseBiz = new BaseBiz();
    private final SuperOpLog spLog = new SuperOpLog();
    //String opUser="";
    //操作类型
    //String opType = null;
    //操作内容
    //String opContent =null;
    static Map<String, String> infoMap = new HashMap<String, String>();

    static {
        infoMap.put("1", "短信模板");
        infoMap.put("2", "彩信模板");
        infoMap.put("3", "网讯模板");
    }

    /**
     * @param request
     * @param response
     */
    //进入彩信模板查询界面
    public void find(HttpServletRequest request, HttpServletResponse response) {

        String lgguid = "";
        Long userGuid = null;
        Long lguserId = null;
        LfSysuser sysUser = null;
        //操作员的GUID
        lgguid = request.getParameter("lgguid");
        try {
            if (lgguid == null || "".equals(lgguid.trim()) || "undefined".equals(lgguid.trim())) {
                EmpExecutionContext.error("彩信模板编辑获取lgguid参数异常！lgguid=" + lgguid + "。改成从Session获取。");
                LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
                lgguid = String.valueOf(loginSysuser.getGuId());
            }
            userGuid = Long.valueOf(lgguid);
            //获取操作员的对象
            sysUser = baseBiz.getByGuId(LfSysuser.class, userGuid);
            //获取操作员的用户ID
            lguserId = sysUser.getUserId();
        } catch (Exception e) {
            //进入异常
            EmpExecutionContext.error(e, "彩信模板获取当前操作员出现异常！");
        }

        List<LfTemplateVo> mmsList = null;
        LfTemplateVo mt = new LfTemplateVo();

        dirUrl = txtfileutil.getWebRoot();
        PageInfo pageInfo = new PageInfo();
        //日志开始时间
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        long begin_time = System.currentTimeMillis();
        try {
            //是否第一次打开
            boolean isFirstEnter = false;
            isFirstEnter = pageSet(pageInfo, request);
            if (!isFirstEnter) {
                //主题
                String theme = request.getParameter("theme");
                //编码
                String tmCode = request.getParameter("tmCode");

                //操作员名称
                String userName = request.getParameter("userName");
                //审批状态
                String rstate = request.getParameter("rState");
                //模板状态
                String state = request.getParameter("state");
                //模板类型
                String dsFlag = request.getParameter("dsFlag");
                if (theme != null && !"".equals(theme)) {
                    theme = theme.replace("'", "");
                    mt.setTmName(theme);
                }
                if (tmCode != null && !"".equals(tmCode)) {
                    tmCode = tmCode.trim();
                    mt.setTmCode(tmCode);
                }
                if (userName != null && !"".equals(userName)) {
                    mt.setName(userName);
                }
                if (rstate != null && !"".equals(rstate)) {
                    mt.setIsPass(Integer.parseInt(rstate));
                }
                if (state != null && !"".equals(state)) {
                    mt.setTmState(Long.parseLong(state));
                }
                if (dsFlag != null && !"".equals(dsFlag)) {
                    mt.setDsflag(Long.parseLong(dsFlag));
                }
                String auditStatus = request.getParameter("auditStatus");
                if (auditStatus != null && !"".equals(auditStatus)) {
                    mt.setAuditstatus(Integer.parseInt(auditStatus));
                }
                String submitstatus = request.getParameter("submitstatus");
                if (submitstatus != null && !"".equals(submitstatus)) {
                    mt.setSubmitstatus(Integer.parseInt(submitstatus));
                }
            }

            mt.setTmpType(4);
            //模板（3-短信模板;4-彩信模板）
            //审核流程id
            String flowIdStr = request.getParameter("flowId");
            if (flowIdStr != null && !"".equals(flowIdStr)) {
                mt.setFlowID(Long.valueOf(flowIdStr));
            }
            mmsList = mtb.getTemplateByCondition(lguserId, mt, pageInfo);
            //设置服务器名称
            new ServerInof().setServerName(getServletContext().getServerInfo());
            request.setAttribute("mmsVo", mt);
            request.setAttribute("mmsList", mmsList);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("findresult", "");
            //增加查询日志
            if (pageInfo != null) {
                long end_time = System.currentTimeMillis();
                String opContent = "查询开始时间：" + format.format(begin_time) + ",耗时:" + (end_time - begin_time) + "毫秒，数量：" + pageInfo.getTotalRec();
                opSucLog(request, "彩信模板", opContent, "GET");
            }
            request.getRequestDispatcher(PATH + "/tem_mmsTemplate.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "彩信模板管理跳转失败！");
            request.setAttribute("findresult", "-1");
            request.setAttribute("pageInfo", pageInfo);
        }
    }

    /**
     * 新建彩信模板
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String opUser = ((LfSysuser) request.getSession(false).getAttribute("loginSysuser")).getUserName();
        String opType = null;
        String opContent = null;
        //获取操作员的用户ID
        //String id = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String id = SysuserUtil.strLguserid(request);


        String lgcorpcode = request.getParameter("lgcorpcode");
        Long lguserId = null;
        try {
            lguserId = Long.valueOf(id);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "彩信模板管理当前操作员lguserid转化失败！");
        }
        //操作
        String opT = request.getParameter("opType");
        if ("add".equals(opT)) {
            opType = StaticValue.ADD;
            opContent = "新建彩信模板";
        } else if ("copy".equals(opT)) {
            opType = StaticValue.OTHER;
            opContent = "复制彩信模板";
        } else if ("edit".equals(opT)) {
            opType = StaticValue.UPDATE;
            opContent = "修改彩信模板";
        }
        TmsFile tmsfile = new TmsFile();
        String corpcodedir = lgcorpcode + "/";
        new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_TEMPLATES + corpcodedir);
        String fileName = StaticValue.MMS_TEMPLATES + corpcodedir + "3_" + (int) (lguserId - 0) + "_"
                + (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime()) + ".tms";
        //模板类型
        String templateType = request.getParameter("templateType");
        //彩信模板ID
        String mmsTitle = request.getParameter("mt");
        //模板编码
        String mmsCode = request.getParameter("mmsCode");
        //特殊符号处理，•存入数据库乱码
        mmsTitle = mmsTitle.replace("•", "．");
        LfTemplate lmt = null;
        //模板状态
        String state = request.getParameter("s");
        //内容
        String[] content = request.getParameterValues("cont[]");
        //模板参数个数
        Integer paramCnt = 0;
        //获取http路径
        Map<String, String[]> prop = WebgatePropInfo.getProp();
        String httpUrl = prop.get("webgateProp")[0];
        try {

            if (content.length > 0) {
                for (int i = 0; i < content.length; i++) {
                    content[i] = content[i].replace("\r\n", "<brrn/>");
                    content[i] = content[i].replace("\n", "<brn/>");
                    JSONObject jo = (JSONObject) JSONValue.parse(content[i]);
                    FrameItem item = new FrameItem();
                    if (jo.get("ImgUrl") != null && !"".equals(jo.get("ImgUrl").toString())) {
                        item.setImageSrc(dirUrl + jo.get("ImgUrl").toString());
                    }
                    if (jo.get("MusUrl") != null && !"".equals(jo.get("MusUrl").toString())) {
                        item.setAudioSrc(dirUrl + jo.get("MusUrl").toString());
                    }
                    if (jo.get("lasttime") != null && !"".equals(jo.get("lasttime").toString())) {
                        item.setDelayTime(Integer.parseInt(jo.get("lasttime").toString()));
                    }
                    if (jo.get("textContent") != null && !"".equals(jo.get("textContent").toString())) {
                        String nr = jo.get("textContent").toString();
                        nr = nr.replace("<brrn/>", "\r\n");
                        nr = nr.replace("<brn/>", "\n");
                        //nr = nr.replaceAll("\\{#参数([1-9][0-9]*)#\\}", "#p_$1#");
                        nr = nr.replaceAll("\\{#" + MessageUtils.extractMessage("ydcx", "ydcx_cxyy_mbbj_cs", request) + "([1-9][0-9]*)#\\}", "#p_$1#");
                        item.setTextSrc(nr);

                        String eg = "#[pP]_[1-9][0-9]*#";
                        Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(nr);
                        while (m.find()) {
                            String rstr = m.group();
                            rstr = rstr.toUpperCase();
                            String pc = rstr.substring(rstr.indexOf("#P_") + 3, rstr.lastIndexOf("#"));
                            int pci = Integer.parseInt(pc);
                            if (pci > paramCnt) {
                                paramCnt = pci;
                            }
                        }
                    }
                    tmsfile.addFrame(item);
                }
            }
            byte bytes[] = tmsfile.getTmsFileBytes();
            if (bytes.length >= 80 * 1024) {
                response.getWriter().print("overSize");
                return;
            }
            File file = new File(dirUrl + fileName);
            OutputStream out = null;
            out = new FileOutputStream(file);
            out.write(bytes);
            out.close();

            boolean isuploadFileServerSuccess = false;
            if (StaticValue.getISCLUSTER() == 1) {
                CommonBiz commBiz = new CommonBiz();
                //上传文件到文件服务器
                if ("success".equals(commBiz.uploadFileToFileCenter(fileName))) {
                    isuploadFileServerSuccess = true;
                }
            }
            //不集群或者集群上传文件成功，集群上传文件失败则会提示操作失败
            if (StaticValue.getISCLUSTER() != 1 || isuploadFileServerSuccess == true) {
                if ("add".equals(opT) || "copy".equals(opT)) {
                    String repate = checkRepeat(request);
                    if ("repeat".equals(repate)) {
                        response.getWriter().print("repeat");
                        return;
                    }

                    lmt = new LfTemplate();
                    //模板名称
                    lmt.setTmName(mmsTitle);
                    lmt.setTmCode(mmsCode == null ? "" : mmsCode.trim());
                    //是否审核(无需审核-0，未审核-1，同意1，拒绝2)
                    lmt.setIsPass(-1);
                    //模板状态（0无效，1有效，2草稿）
                    lmt.setTmState(Long.parseLong(state));
                    //添加时间
                    lmt.setAddtime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    //模板内容
                    lmt.setTmMsg(fileName);
                    //操作员ID
                    lmt.setUserId(lguserId);
                    lmt.setCorpCode(lgcorpcode);
                    // 模板（3-短信模板;4-彩信模板）
                    lmt.setTmpType(new Integer("4"));
                    lmt.setParamcnt(paramCnt);
                    lmt.setDsflag(Long.parseLong(templateType));
                    //网关审核状态
                    lmt.setAuditstatus(-1);
                    //网关彩信模板状态
                    lmt.setTmplstatus(0);
                    String emp_templid = getEmpTemplateLid();
                    lmt.setEmptemplid(emp_templid);
                    lmt.setSubmitstatus(0);
                    MmsTemplate mmstl = new MmsTemplate();//网关平台彩信模板对象
                    mmstl.setUserId(lguserId.toString()); //创建人
                    if (StaticValue.getCORPTYPE() == 1) {
                        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                        conditionMap.put("corpCode", lgcorpcode);
                        //状态为起效
                        conditionMap.put("isValidate", "1");
                        List<LfMmsAccbind> mmsaccs = baseBiz.getByCondition(LfMmsAccbind.class, conditionMap, null);
                        if (mmsaccs != null && mmsaccs.size() > 0) {
                            mmstl.setUserId(mmsaccs.get(0).getMmsUser());
                        } else {
                            lmt.setTmState(2L);
                        }
                    }
                    mmstl.setAuditStatus(0);//审核状态
                    mmstl.setTmplStatus(0);//模板状态
                    mmstl.setParamCnt(paramCnt);//参数个数
                    mmstl.setTmplPath(httpUrl + fileName);//文件路径
                    mmstl.setRecvTime(lmt.getAddtime());//模板接收时间
                    //由""改为空格了
                    mmstl.setAuditor(" ");
                    mmstl.setAuditTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    mmstl.setRemarks(" ");
                    mmstl.setEmptemplid(emp_templid);
                    mmstl.setTmplId(0L);
                    mmstl.setSubmitstatus(0);
                    mmstl.setReServe1("0");
                    mmstl.setReServe2("0");
                    mmstl.setReServe3("0");
                    mmstl.setReServe4("0");
                    mmstl.setReServe5("0");

                    long result = mtb.addTemplate(mmstl, lmt);


                    //增加操作日志
                    if (result > 0) {
                        Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
                        if (loginSysuserObj != null) {
                            LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                            String contnet = opContent + "成功。[模板名称，模板类型，模板状态](" + lmt.getTmName() + "，" + lmt.getDsflag() + "，" + lmt.getTmState() + ")";
                            EmpExecutionContext.info("模板编辑", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "ADD");
                        }
                    }

                    if (result > 0) {
                        spLog.logSuccessString(opUser, opModule, opType, opContent, lgcorpcode);
                        if (lmt.getTmState() == 2L) {
                            response.getWriter().print("caogaotrue");
                        } else {
                            response.getWriter().print("true");
                        }
                    } else {
                        spLog.logFailureString(opUser, opModule, opType, opContent + opSper, null, lgcorpcode);
                        response.getWriter().print("false");
                    }
                } else if ("edit".equals(opT)) {
                    String repate = checkRepeat(request);
                    if ("repeat".equals(repate)) {
                        response.getWriter().print("repeat");
                        return;
                    }
                    String tmId = request.getParameter("tmId");
                    LfTemplate oldlmt = baseBiz.getById(LfTemplate.class, tmId);
                    lmt = new LfTemplate();
                    lmt.setTmid(Long.valueOf(tmId));
                    lmt.setTmName(mmsTitle);
                    lmt.setTmCode(mmsCode == null ? "" : mmsCode.trim());
                    lmt.setIsPass(-1);
                    //是否审核(无需审核-0，未审核-1，同意1，拒绝2)
                    lmt.setTmState(Long.parseLong(state));
                    //模板状态（0无效，1有效，2存草稿）
                    lmt.setAddtime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    lmt.setTmMsg(fileName);
                    lmt.setUserId(lguserId);
                    lmt.setCorpCode(lgcorpcode);
                    lmt.setTmpType(new Integer("4"));
                    lmt.setParamcnt(paramCnt);
                    lmt.setDsflag(Long.parseLong(templateType));
                    //如果参数个数大于0说明是动态模板
					/*if(paramCnt>0)
					{
						lmt.setDsflag(1L);
					}
					else
					{
						lmt.setDsflag(0L);
					}*/

                    MmsTemplate mmstl = new MmsTemplate();//网关平台彩信模板对象
                    mmstl.setUserId(lguserId.toString()); //创建人
                    if (StaticValue.getCORPTYPE() == 1) {
                        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                        conditionMap.put("corpCode", lgcorpcode);
                        //状态为起效
                        conditionMap.put("isValidate", "1");
                        List<LfMmsAccbind> mmsaccs = baseBiz.getByCondition(LfMmsAccbind.class, conditionMap, null);
                        if (mmsaccs != null && mmsaccs.size() > 0) {
                            mmstl.setUserId(mmsaccs.get(0).getMmsUser());
                        } else {
                            lmt.setTmState(2L);
                        }
                    }
                    mmstl.setAuditStatus(0);//审核状态
                    mmstl.setTmplStatus(0);//模板状态
                    mmstl.setParamCnt(paramCnt);//参数个数
                    mmstl.setTmplPath(httpUrl + fileName);//文件路径
                    mmstl.setRecvTime(lmt.getAddtime());//模板接收时间
                    mmstl.setAuditor(" ");
                    mmstl.setAuditTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    mmstl.setRemarks(" ");
                    mmstl.setEmptemplid(oldlmt.getEmptemplid());
                    mmstl.setTmplId(0L);
                    mmstl.setSubmitstatus(0);
                    mmstl.setReServe1("0");
                    mmstl.setReServe2("0");
                    mmstl.setReServe3("0");
                    mmstl.setReServe4("0");
                    mmstl.setReServe5("0");

                    boolean result = mtb.updateTemplate(mmstl, lmt);

                    //增加操作日志
                    if (result) {
                        Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
                        if (loginSysuserObj != null) {
                            LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                            String contnet = opContent + "成功。[模板名称，模板类型，模板状态](" + oldlmt.getTmName() + "，" + oldlmt.getDsflag() + "，" + oldlmt.getTmState() + ")"
                                    + "-->(" + lmt.getTmName() + "，" + lmt.getDsflag() + "，" + lmt.getTmState() + ")";
                            EmpExecutionContext.info("模板编辑", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "UPDATE");
                        }
                    }

                    if (result) {
                        spLog.logSuccessString(opUser, opModule, opType, opContent, lgcorpcode);
                        if (lmt.getTmState() == 2L) {
                            response.getWriter().print("caogaotrue");
                        } else {
                            response.getWriter().print("true");
                        }
                    } else {
                        spLog.logFailureString(opUser, opModule, opType, opContent + opSper, null, lgcorpcode);
                        response.getWriter().print("false");
                    }
                }
            }
        } catch (EMPException ex) {
            String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
            ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
            String desc = info.getErrorInfo(ex.getMessage());
            response.getWriter().print(desc);
            EmpExecutionContext.error(ex, "彩信模板管理操作彩信模板出现异常！");
            return;
        } catch (Exception e) {
            spLog.logFailureString(opUser, opModule, opType, opContent + opSper, e, lgcorpcode);
            EmpExecutionContext.error(e, "彩信模板管理操作彩信模板出现异常！");
            response.getWriter().print("false");
        }
    }


    /**
     * 检查编码是否重复
     *
     * @param request
     */
    public String checkRepeat(HttpServletRequest request) throws IOException {
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        String tmCode = request.getParameter("mmsCode");
        String corpCode = request.getParameter("lgcorpcode");
        String hiddenCode = request.getParameter("hiddenCode");
        tmCode = tmCode == null ? "" : tmCode.trim();
        if (tmCode != null && tmCode.equals(hiddenCode)) {
            return "";
        } else {
            conditionMap.put("tmCode", tmCode);
            conditionMap.put("corpCode", corpCode);
            conditionMap.put("tmpType", "4");
            //状态为启用
            try {
                List<LfTemplate> tempList = baseBiz.getByCondition(LfTemplate.class, conditionMap, null);
                if (tempList != null && tempList.size() > 0) {
                    return "repeat";
                }

            } catch (Exception e) {
                EmpExecutionContext.error(e, "检查编码是否重复异常！");
                //异步返回结果
                return "";
            }
        }
        return "";
    }


    @SuppressWarnings("unchecked")
    public void uploadTms(HttpServletRequest request, HttpServletResponse response) {
        // 设置语言
        String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
        response.setHeader("Charset", "UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String fileName = "";
        Long lguserid = 0L;
        //String lgcorpcode = "";
        try {
            String lgcorpcode = request.getParameter("lgcorpcode");
            String importCode = request.getParameter("mmsCode");
            String corpcodedir = lgcorpcode + "/";
            new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_TEMPLATES + corpcodedir);
            //文件名称
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(100 * 1024);
            factory.setRepository(new File(dirUrl + StaticValue.MMS_TEMPLATES + corpcodedir));
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();
            //boolean isOver = false;
            while (iter.hasNext()) {
                FileItem fileItem = (FileItem) iter.next();
                String fName = fileItem.getFieldName();
                if (fName.equals("lgcorpcode")) {
                    lgcorpcode = fileItem.getString("UTF-8").toString();
                } else if (fName.equals("lguserid")) {
                    lguserid = Long.valueOf(fileItem.getString("UTF-8"));
                    fileName = StaticValue.MMS_TEMPLATES + corpcodedir + "3_" + (int) (lguserid - 0) + "_"
                            + (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime()) + ".tms";
                } else if (!fileItem.isFormField()
                        && fileItem.getName().length() > 0) {
			    	/*if(fileItem.getSize() - (50*1024) > 0){
			    		isOver = true;
				    	break;
			    	}*/

                    fileItem.write(new File(dirUrl + fileName));
                    boolean isOk = parseTms(fileName, lgcorpcode);
                    if (!isOk) {
                        response.getWriter().print("typeNotMatch");
                        return;
                    }

                    String ret = checkRepeat(request);
                    if (!"".equals(ret)) {
                        response.getWriter().print("repeat");
                        return;
                    }
                    LfTemplate lmt = new LfTemplate();
                    //模板名称
                    //lmt.setTmName("导入模板");
                    lmt.setTmName(MessageUtils.extractMessage("ydcx", "ydcx_cxyy_mbbj_drmb", request));
                    //是否审核(无需审核-0，未审核-1，同意1，拒绝2)
                    lmt.setIsPass(0);
                    //模板状态（0无效，1有效，2草稿）
                    lmt.setTmState(2L);
                    //添加时间
                    lmt.setAddtime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    //模板内容
                    lmt.setTmMsg(fileName);
                    //操作员ID
                    lmt.setUserId(lguserid);
                    lmt.setTmCode(importCode);
                    //提交状态 	0：未提交	1：提交成功	2：提交失败	3；提交中
                    lmt.setSubmitstatus(0);
                    lmt.setCorpCode(lgcorpcode);
                    // 模板（3-短信模板;4-彩信模板）
                    lmt.setTmpType(4);
                    String tmsText = mpb.getTmsText(fileName);
                    int paramCnt = getParamCnt(tmsText);
                    lmt.setParamcnt(paramCnt);
                    //如果参数个数大于0说明是动态模板
                    if (paramCnt > 0) {
                        lmt.setDsflag(1L);
                    } else {
                        lmt.setDsflag(0L);
                    }
                    String emp_templid = getEmpTemplateLid();
                    lmt.setEmptemplid(emp_templid);
                    //网关审核状态
                    lmt.setAuditstatus(-1);
                    //网关彩信模板状态
                    lmt.setTmplstatus(0);
                    //网关平台彩信模板对象
                    MmsTemplate mmstl = new MmsTemplate();
                    //创建人
                    mmstl.setUserId(lguserid.toString());
                    //审核状态
                    mmstl.setAuditStatus(0);
                    //模板状态
                    mmstl.setTmplStatus(0);
                    //参数个数
                    mmstl.setParamCnt(paramCnt);
                    //文件路径
                    mmstl.setTmplPath(dirUrl + fileName);
                    //模板接收时间
                    mmstl.setRecvTime(lmt.getAddtime());
                    mmstl.setAuditor("");
                    mmstl.setAuditTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    mmstl.setRemarks("");

                    long result = mtb.addTemplate(mmstl, lmt);

                    //增加操作日志
                    if (result > 0) {
                        Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
                        if (loginSysuserObj != null) {
                            LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                            String contnet = "导入彩信模板" + "成功。[模板名称，模板类型，模板状态](" + lmt.getTmName() + "，" + lmt.getDsflag() + "，" + lmt.getTmState() + ")";
                            EmpExecutionContext.info("模板编辑", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "ADD");
                        }
                    }

                    if (result > 0) {
                        response.getWriter().print("true");
                    } else {
                        response.getWriter().print("false");
                    }
                }
            }
			/*if (isOver){
				writer.print("isOver");
			}*/
        } catch (Exception e) {
            EmpExecutionContext.error(e, "上传tms文件出现异常！");
        }
    }

    //复制
    public void doCopy(HttpServletRequest request, HttpServletResponse response) {
        String tmId = request.getParameter("tmId");
        String opType = request.getParameter("opType");
        try {
            if (StaticValue.getCORPTYPE() == 1) {
                //彩信模板ID
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("corpCode", request.getParameter("lgcorpcode"));
                List<LfMmsAccbind> mmsaccs = baseBiz.getByCondition(LfMmsAccbind.class, conditionMap, null);
                if (mmsaccs != null && mmsaccs.size() > 0) {
                    request.setAttribute("mmsacc", "true");
                } else {
                    request.setAttribute("mmsacc", "false");
                }
            } else {
                request.setAttribute("mmsacc", "true");
            }

            LfTemplate template = baseBiz.getById(LfTemplate.class, tmId);
            request.setAttribute("opType", opType);
            request.setAttribute("template", template);
            request.getRequestDispatcher(PATH + "/tem_editMmsTemplate.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "复制彩信模板出现异常！");
        }
    }

    //删除
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String opUser = ((LfSysuser) request.getSession(false).getAttribute("loginSysuser")).getUserName();
        String opType = null;
        String opContent = null;
        opType = StaticValue.DELETE;

        String corpcode = request.getParameter("lgcorpcode");
        try {
            //获取需要删除的模板ID
            String[] ids = request.getParameter("ids").toString().split(",");

            //查询删除的彩信模板集合
            String idsStr = request.getParameter("ids").toString();
            LinkedHashMap<String, String> logConditionMap = new LinkedHashMap<String, String>();
            logConditionMap.put("tmid&" + StaticValue.IN, idsStr);
            LinkedHashMap<String, String> logOrderMap = new LinkedHashMap<String, String>();
            logOrderMap.put("tmid", StaticValue.ASC);
            List<LfTemplate> deleteTemplateList = new BaseBiz().getByCondition(LfTemplate.class, logConditionMap, logOrderMap);

            int count = mtb.delTempByTmId(ids);
            opContent = "删除" + ids.length + "条彩信模板";

            //增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                //增加操作日志
                String deleteMsg = "";
                for (int i = 0; i < deleteTemplateList.size(); i++) {
                    deleteMsg += "[" + deleteTemplateList.get(i).getTmid() + "，" + deleteTemplateList.get(i).getTmName() + "，" + deleteTemplateList.get(i).getDsflag() + "]，";
                }
                String contnet = "删除彩信模板" + "成功。(总数：" + ids.length + ")[模板ID，模板名称，模板类型](" + deleteMsg + ")";
                EmpExecutionContext.info("模板编辑", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "DELETE");
            }

            response.getWriter().print(count);
            //添加操作成功日志
            spLog.logSuccessString(opUser, opModule, opType, opContent, corpcode);
        } catch (Exception e) {
            response.getWriter().print(0);
            //添加操作失败日志
            spLog.logFailureString(opUser, opModule, opType, opContent + opSper, e, corpcode);
            EmpExecutionContext.error(e, "删除彩信模板失败！");
        }
    }

    //上传彩信文件
    @SuppressWarnings("unchecked")
    public void upload(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Charset", "UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        long startTime = System.currentTimeMillis();
        String lgcorpcode = null;
        String lguserid = null;
        try {
            lgcorpcode = request.getParameter("lgcorpcode");
            //lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            lguserid = SysuserUtil.strLguserid(request);

            String corpcodedir = lgcorpcode + "/";
            out = response.getWriter();
            long size = 0L;
            int width = 0;
            int height = 0;
            String imgUrl = "";
            String musicUrl = "";
            //String tmsUrl = "";
            String fileName = "2_" + (int) (lguserid != null && !"".equals(lguserid) ? Long.parseLong(lguserid) : 0) + "_"
                    + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(Calendar.getInstance().getTime());  //文件服务器名称
            new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_TEMPLATES + corpcodedir);
            new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_TEMPRESOURCE + corpcodedir);

            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(100 * 1024);
            factory.setRepository(new File(dirUrl + StaticValue.MMS_TEMPLATES + corpcodedir));
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();
            boolean isOver = false;
            while (iter.hasNext()) {
                FileItem fileItem = (FileItem) iter.next();
                if (fileItem.getSize() - (80 * 1024) > 0 && ("chooseImg".equals(fileItem.getFieldName()) || "chooseMusic".equals(fileItem.getFieldName()))) {
                    isOver = true;
                } else if (!fileItem.isFormField()
                        && fileItem.getName().length() > 0
                        && "chooseImg".equals(fileItem.getFieldName())) {
                    imgUrl = fileName + fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
                    //图片的名称，加入图片格式
                    String name = dirUrl + StaticValue.MMS_TEMPRESOURCE + corpcodedir + imgUrl;
                    fileItem.write(new File(name));
                    //写到服务器
                    size = fileItem.getSize();
                    BufferedImage bi = ImageIO.read(new File(name));
                    width = bi.getWidth();
                    //宽
                    height = bi.getHeight();
                    //高
                } else if (!fileItem.isFormField()
                        && fileItem.getName().length() > 0
                        && "chooseMusic".equals(fileItem.getFieldName())) {
                    musicUrl = fileName + fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
                    String name = dirUrl + StaticValue.MMS_TEMPRESOURCE + corpcodedir + musicUrl;
                    fileItem.write(new File(name));
                    size = fileItem.getSize();
                }/*else if(!fileItem.isFormField() && fileItem.getName().length() > 0 && "chooseTms".equals(fileItem.getFieldName())){
			    	tmsUrl = fileName+fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
			    	String name = dirUrl + StaticValue.MMS_TEMPRESOURCE + tmsUrl;
			    	fileItem .write(new File(name));
			    	//size = fileItem.getSize();
			    }*/
            }
            if (isOver) {
                response.getWriter().print("{url:'false'}");
            } else if (!"".equals(imgUrl)) {
                //图片文件
                out.print("{url:'" + StaticValue.MMS_TEMPRESOURCE + corpcodedir + imgUrl + "',size:" + size + ",width:" + width + ",height:" + height + "}");
            } else if (!"".equals(musicUrl)) {
                //声音文件
                out.print("{url:'" + StaticValue.MMS_TEMPRESOURCE + corpcodedir + musicUrl + "',size:" + size + "}");
            }/*else if(!"".equals(tmsUrl)){
				//彩信文件，将彩信文件解析成json格式
				String result =this.getTmsFileInfo(StaticValue.MMS_TEMPRESOURCE+tmsUrl);
				out.print(result);
			}*/
        } catch (FileUploadException e) {
            StringBuffer logInfo = new StringBuffer();
            logInfo.append("彩信上传声音或者图片，表单流上传失败。userId:").append(lguserid).append("，lgcorpcode:").append(lgcorpcode)
                    .append("，耗时:").append(System.currentTimeMillis() - startTime).append("ms");
            EmpExecutionContext.error(e, logInfo.toString());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "上传彩信声音或者图片失败！");
        }
    }

    //获取彩信模板的信息
	/*public void getTmMsg(HttpServletRequest request,
			HttpServletResponse response) {
		// 彩信模板ID
		String tmId = request.getParameter("tmId");
		try {
			if ("".equals(tmId)) {
				writer.print("");
			} else {
				LfTemplate template = baseBiz.getById(LfTemplate.class, tmId);
				writer.print(template.getTmMsg());
			}
		} catch (Exception e) {
			writer.print("");
			EmpExecutionContext.error(e);
		}
	}*/
    //验证关键字及参数格式是否正确的方法
    public void checkBadWord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String words = "";
        String templateType = request.getParameter("templateType");
        try {
            String[] content = request.getParameterValues("content[]");
            //获取当前登录用户的企业编码
            String corpCode = request.getParameter("lgcorpcode");
            String eg = "#[pP]_[1-9][0-9]*#";
            Map<Integer, Integer> values = new HashMap<Integer, Integer>();
            int paramCnt = 0;
            if (content.length > 0) {
                String s = "";
                for (int i = 0; i < content.length; i++) {
                    content[i] = content[i].replace("\r\n", "");
                    content[i] = content[i].replace("\n", "");
                    JSONObject jo = (JSONObject) JSONValue.parse(content[i]);
                    if (jo.get("textContent") != null && !"".equals(jo.get("textContent").toString())) {
                        //s = badFilter.checkText(jo.get("textContent").toString());
                        s = keyWordAtom.checkText(jo.get("textContent").toString(), corpCode);
                        //Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(jo.get("textContent").toString().replaceAll("\\{#参数([1-9][0-9]*)#\\}", "#p_$1#"));
                        Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(jo.get("textContent").toString().replaceAll("\\{#+" + MessageUtils.extractMessage("ydcx", "ydcx_cxyy_mbbj_cs", request) + "+([1-9][0-9]*)#\\}", "#p_$1#"));
                        while (m.find()) {
                            String rstr = m.group();
                            rstr = rstr.toUpperCase();
                            String pc = rstr.substring(rstr.indexOf("#P_") + 3, rstr.lastIndexOf("#"));
                            int pci = Integer.parseInt(pc);
                            if (pci > paramCnt) {
                                paramCnt = pci;
                            }
                            if (!values.containsKey(pci)) {
                                values.put(pci, pci);
                            }
                        }
                        if (!"".equals(s)) {
                            words += "[第" + (i + 1) + "帧：" + s + "]";
                        }
                    }
                }
            }
            if (words.length() > 0) {
                if (",".equals(words.substring(words.length() - 1))) {
                    words = words.substring(0, words.length() - 1);
                }
            } else {
                if ("1".equals(templateType) && paramCnt > 0) {
                    for (int j = 1; j < paramCnt; j++) {
                        if (!values.containsKey(j)) {
                            words = "Pfalse";
                            return;
                        }
                    }
                    if (paramCnt > 20) {
                        words = "moreParam";
                        return;
                    }
                } else if ("1".equals(templateType)) {
                    words = "noParam";
                }
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "彩信模板验证关键字及参数格式是否正确出现异常！");
        } finally {
            response.getWriter().print(words);
        }
    }

    //编码彩信模板
    public void doEdit(HttpServletRequest request, HttpServletResponse response) {
        try {
            String tmId = request.getParameter("tmId");
            if (tmId == null) {
                tmId = request.getAttribute("tmId").toString();
            }
            if (tmId != null && !"".equals(tmId)) {
                LfTemplate tem = baseBiz.getById(LfTemplate.class, tmId);
                if (tem != null) {
                    request.setAttribute("temp", tem);
                }
                request.getRequestDispatcher(PATH + "/tem_editMmsTemplate.jsp")
                        .forward(request, response);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "编码彩信模板出现异常！");
        }
    }

    /**
     * 删除该模板的彩信声音或者图片
     *
     * @param request
     * @param response
     */
    public void delSource(HttpServletRequest request, HttpServletResponse response) {
        try {
            String sourceUrl = request.getParameter("sourceUrl");
            if (sourceUrl != null && !"".equals(sourceUrl) && sourceUrl.indexOf("mmsTemplate") < 0 && sourceUrl.indexOf("material") < 0) {
                File f = new File(txtfileutil.getWebRoot() + sourceUrl);
                if (!f.isDirectory()) {
                    boolean r = f.delete();
                    if (r) {
                        response.getWriter().print("true");
                    }
                }
            } else if (sourceUrl != null && !"".equals(sourceUrl) && (sourceUrl.indexOf("mmsTemplate") >= 0 || sourceUrl.indexOf("material") >= 0)) {
                response.getWriter().print("true");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "删除该模板的彩信声音或者图片出现异常！");
        }
    }

    //跳转到新增页面
    public void doAdd(HttpServletRequest request, HttpServletResponse response) {
        try {
            //查询有没有彩信账号
            if (StaticValue.getCORPTYPE() == 1) {
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("corpCode", request.getParameter("lgcorpcode"));
                List<LfMmsAccbind> mmsaccs = baseBiz.getByCondition(LfMmsAccbind.class, conditionMap, null);
                if (mmsaccs != null && mmsaccs.size() > 0) {
                    request.setAttribute("mmsacc", "true");
                } else {
                    request.setAttribute("mmsacc", "false");
                }
            } else {
                request.setAttribute("mmsacc", "true");
            }
            String type = request.getParameter("type");
            //入口
            request.setAttribute("type", type);
            //设置服务器名称
            new ServerInof().setServerName(getServletContext().getServerInfo());
            request.getRequestDispatcher(PATH + "/tem_addMmsTemplate.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "跳转新增彩信模板页面出现异常！");
        }
    }

    //、修改模板状态
    public void changeState(HttpServletRequest request, HttpServletResponse response) {
        try {
            //模板ID
            String id = request.getParameter("id");
            //模板修改的状态
            String state = request.getParameter("t");
            LfTemplate tem = baseBiz.getById(LfTemplate.class, id);
            tem.setTmState(Long.parseLong(state));
            boolean r = baseBiz.updateObj(tem);

            String changeStr = "";
            if (Long.parseLong(state) == 0L) {
                changeStr = "状态由启用改为禁用";
            } else {
                changeStr = "状态由禁用改为启用";
            }
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String contnet = "修改彩信模板状态成功(彩信模板ID为：" + id + "，彩信模板名称为：" + tem.getTmName() + ")，" + changeStr + "。";
                EmpExecutionContext.info("模板编辑", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "UPDATE");
            }

            if (r) {
                response.getWriter().print("true");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "修改彩信模板状态出现异常！");
        }
    }

    //导出功能
    public void doExport(HttpServletRequest request, HttpServletResponse response) {
        InputStream inStream = null;
        try {
            //获取彩信模板的路径
            String url = request.getParameter("u");
            File file = new File(new TxtFileUtil().getWebRoot() + url);
            if (file.exists()) {
                String filename = URLEncoder.encode(file.getName(), "utf-8");
                response.reset();
                response.setContentType("application/x-msdownload");
                response.addHeader("Content-Disposition", "attachment; filename=\""
                        + filename + "\"");
                int fileLength = (int) file.length();
                response.setContentLength(fileLength);
                if (fileLength != 0) {
                    inStream = new FileInputStream(file);
                    byte[] buf = new byte[4096];
                    ServletOutputStream servletOS = response.getOutputStream();
                    int readLength;
                    while (((readLength = inStream.read(buf)) != -1)) {
                        servletOS.write(buf, 0, readLength);
                    }
                    inStream.close();
                    servletOS.flush();
                    servletOS.close();
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "彩信模板导出出现异常！");
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "彩信模板导出文件流关闭出现异常！");
                }
            }
        }
    }

    //编辑
    public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取彩信模板的路径
        String tmUrl = request.getParameter("tmUrl");
        String lgcorpcode = request.getParameter("lgcorpcode");

        String result = "";
        try {
            if (tmUrl != null && !"".equals(tmUrl)) {
                result = getTmsFileInfo(tmUrl, lgcorpcode);
            }
            response.getWriter().print(result);
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "彩信编辑出现异常！");
        }
    }

    //获取模板信息
    @SuppressWarnings("unchecked")
    public String getTmsFileInfo(String filePath, String lgcorpcode) {

        String fileName;
        filePath = dirUrl + filePath;
        String tmsFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        ParseTmsFile parseTms = new ParseTmsFile();
        List<TmsSmilItem> tmsSmilItem = parseTms.Parse(filePath);
        JSONArray jsonArray = new JSONArray();
        try {
            String corpcodedir = lgcorpcode + "/";
            //创建文件夹
            new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_TEMPRESOURCE + corpcodedir);
            List<TmsSmilParItem> parItemsList = null;
            for (int i = 0; i < tmsSmilItem.size(); i++) {
                parItemsList = tmsSmilItem.get(i).getParItemsList();
                TmsSmilParItem parItem = null;
                JSONObject jsonObject = new JSONObject();
                for (int j = 0; j < parItemsList.size(); j++) {
                    parItem = parItemsList.get(j);
                    String src = parItem.getSrc().substring(parItem.getSrc().lastIndexOf(":") + 1);
                    fileName = StaticValue.MMS_TEMPRESOURCE + corpcodedir + tmsFileName + "_" + src;
                    writeFile(fileName, parItem.getContent());
                    if (parItem.getType().equals("image")) {
                        jsonObject.put("ImgUrl", fileName);
                        jsonObject.put("isize", parItem.getContent().length);
                        BufferedImage bi = ImageIO.read(new File(dirUrl + fileName));
                        jsonObject.put("width", bi.getWidth());
                        jsonObject.put("height", bi.getHeight());
                    } else if (parItem.getType().equals("text")) {
                        String text = new String(parItem.getContent(), 0, parItem.getContent().length, "utf-8");
                        //反斜杠跟引号统一在js里面处理
                        // 反斜杠转义
                        //text = text.replace("\\","\\\\");
                        //英文双引号转义
                        //text = text.replace("\"","\\\"");
                        jsonObject.put("textContent", text != null ? text.replaceAll("#[pP]_([1-9][0-9]*)#", "{#参数$1#}") : "");
                    } else {
                        jsonObject.put("MusUrl", fileName);
                        jsonObject.put("msize", parItem.getContent().length);
                    }
                }

                String dur = tmsSmilItem.get(i).getDur().substring(0, tmsSmilItem.get(i).getDur().indexOf("ms"));
                jsonObject.put("lasttime", Integer.parseInt(dur) / 1000);
                jsonArray.add(jsonObject);
            }
        } catch (Exception e) {

            EmpExecutionContext.error(e, "获取彩信模板信息出现异常！");
            return null;
        }
        return jsonArray.toString();
    }

    //写文件
    private boolean writeFile(String filePath, byte[] contents) {
        boolean result = false;
        FileOutputStream fos = null;
        try {
            filePath = dirUrl + filePath;
            File file = new File(filePath);
            if (!file.exists()) {
                boolean state = file.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("创建文件失败");
                }
            } else {
                if (file.isFile()) {
                    boolean delete = file.delete();
                    if (!delete) {
                        EmpExecutionContext.error("删除文件失败");
                    }
                }
                boolean state = file.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("创建文件失败");
                }
            }


            fos = new FileOutputStream(filePath);
            fos.write(contents, 0, contents.length);
            fos.close();
            result = true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "彩信模板写文件失败！");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "彩信模板写文件失败！关闭文件流异常");
                }
            }
        }
        return result;
    }

    private int getParamCnt(String tmsText) {
        int paramCnt = 0;
        if (tmsText != null && !"".equals(tmsText)) {
            tmsText = tmsText.replace("<brrn/>", "\r\n");
            tmsText = tmsText.replace("<brn/>", "\n");

            String eg = "#[pP]_[1-9][0-9]*#";
            Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(tmsText);
            while (m.find()) {
                String rstr = m.group();
                rstr = rstr.toUpperCase();
                String pc = rstr.substring(rstr.indexOf("#P_") + 3, rstr.lastIndexOf("#"));
                int pci = Integer.parseInt(pc);
                if (pci > paramCnt) {
                    paramCnt = pci;
                }
            }
        }
        return paramCnt;
    }

    /*
     * 获取模板id的方法
     */
    public String getEmpTemplateLid() {
        String code = "";
        try {
            Calendar nowCal = Calendar.getInstance();
            Integer year = nowCal.get(Calendar.YEAR);
            Integer month = nowCal.get(Calendar.MONTH) + 1;
            Integer day = nowCal.get(Calendar.DATE);
            Integer hour = nowCal.get(Calendar.HOUR_OF_DAY);
            Integer minute = nowCal.get(Calendar.MINUTE);
            Integer ss = nowCal.get(Calendar.SECOND);

            String time = year + buP(month) + buP(day) + buP(hour) + buP(minute) + buP(ss);

            //生成一个4位的随机数做为动态口令
    		/*int count =4;
    		StringBuffer sb = new StringBuffer();
            String str = "0123456789";
            Random r = new Random();
            for(int i=0;i<count;i++){
                int num = r.nextInt(str.length());
                sb.append(str.charAt(num));
                str = str.replace((str.charAt(num)+""), "");
            }*/
            //GetSxCount sx = GetSxCount.getInstance();

            String count = GetSxCount.getInstance().getCount().toString();
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //彩信模板
            conditionMap.put("tmpType", "4");
            while (true) {
                conditionMap.put("emptemplid", time + count);
                List<LfTemplate> lfTemplates = baseBiz.getByCondition(LfTemplate.class, conditionMap, null);
                if (lfTemplates != null && lfTemplates.size() > 0) {
                    count = GetSxCount.getInstance().getCount().toString();
                } else {
                    break;
                }
            }
            code = time + count;

            //code =time+sx.getCount()+sb.toString();            

        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取彩信模板id失败！");
        }
        return code;
    }

    public String buP(Integer s) {
        return s < 10 ? '0' + s.toString() : s.toString();
    }

    public boolean parseTms(String filePath, String lgcorpcode) {
        String fileName;
        filePath = dirUrl + filePath;
        String tmsFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        ParseTmsFile parseTms = new ParseTmsFile();
        List<TmsSmilItem> tmsSmilItem = parseTms.Parse(filePath);
        boolean isOk = true;
        try {
            String corpcodedir = lgcorpcode + "/";
            new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_TEMPRESOURCE + corpcodedir);
            List<TmsSmilParItem> parItemsList = null;
            for (int i = 0; i < tmsSmilItem.size(); i++) {
                parItemsList = tmsSmilItem.get(i).getParItemsList();
                TmsSmilParItem parItem = null;
                String type = "";
                for (int j = 0; j < parItemsList.size(); j++) {
                    parItem = parItemsList.get(j);
                    String src = parItem.getSrc().substring(parItem.getSrc().lastIndexOf(":") + 1);
                    fileName = StaticValue.MMS_TEMPRESOURCE + corpcodedir + tmsFileName + "_" + src;
                    type = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    if (parItem.getType().equals("image")) {
                        if (!"jpg".equals(type) && !"jpeg".equals(type) && !"gif".equals(type)) {
                            isOk = false;
                            break;
                        }
                    } else if (parItem.getType().equals("sound")) {
                        if (!"mid".equals(type) && !"midi".equals(type) && !"amr".equals(type)) {
                            isOk = false;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "解析tms文件出现异常！");
            return false;
        }
        return isOk;
    }

    /**
     * 生成树
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getMatelTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String busProTree = (String) request.getAttribute("busProTree");
        String corpCode = request.getParameter("lgcorpcode");
        /* busProTree = mb.getMaterialJosnData2(corpCode);*/
        busProTree = mtb.getMaterialJosnData2(corpCode);
        request.setAttribute("busProTree", busProTree);
        response.getWriter().print(busProTree);
    }

    public void getTmMsg(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        String tmUrl = request.getParameter("tmUrl");
        //如果是集群，并且文件路径下没有彩信模板，则从文件服务器下载彩信模板。
        if (StaticValue.getISCLUSTER() == 1 && !new TxtFileUtil().checkFile(tmUrl)) {
            CommonBiz commBiz = new CommonBiz();
            commBiz.downloadFileFromFileCenter(tmUrl);
        }

        String paramContent = request.getParameter("paramContent");
        try {
            if (tmUrl == null || "".equals(tmUrl)) {
                response.getWriter().print("");
            } else {
                String mms = "";
                if (paramContent != null && !"".equals(paramContent)
                        && !"null".equals(paramContent)) {
                    paramContent = paramContent.replaceAll(",", "，");
                    mms = mpb.getDynTmsFileInfo(tmUrl, paramContent);
                } else {
                    mms = mpb.getTmsFileInfo(tmUrl);
                }
                if (mms != null) {
                    mms = mms.replace("\r\n", "&lt;BR/&gt;");
                    mms = mms.replace("\n", "&lt;BR/&gt;");
                }
                response.getWriter().print(mms);
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取彩信文件信息出现异常！");
        }
    }

    /**
     * 检测文件是否存在
     *
     * @param request
     * @param response
     */
    public void checkMmsFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            TxtFileUtil tfu = new TxtFileUtil();
            String url = request.getParameter("url");
            //如果是集群，并且文件路径下没有彩信模板，则从文件服务器下载彩信模板。
            if (StaticValue.getISCLUSTER() == 1 && !tfu.checkFile(url)) {
                CommonBiz commBiz = new CommonBiz();
                commBiz.downloadFileFromFileCenter(url);
            }
            response.getWriter().print(tfu.checkFile(url));
        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "验证彩信文件是否存在出现异常！");
        }
    }

    public void exportTms(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        InputStream inStream = null;
        try {
            String url = request.getParameter("u");
            File file = new File(new TxtFileUtil().getWebRoot() + url);
            if (file.exists()) {
                String filename = URLEncoder.encode(file.getName(), "utf-8");
                response.reset();
                response.setContentType("application/tms");
                response.addHeader("Content-Disposition",
                        "attachment; filename=\"" + filename + "\"");
                int fileLength = (int) file.length();
                response.setContentLength(fileLength);
                if (fileLength != 0) {
                    inStream = new FileInputStream(file);
                    byte[] buf = new byte[4096];
                    ServletOutputStream servletOS = response.getOutputStream();
                    int readLength;
                    while (((readLength = inStream.read(buf)) != -1)) {
                        servletOS.write(buf, 0, readLength);
                    }
                    inStream.close();
                    servletOS.flush();
                    servletOS.close();
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "导出tms文件出现异常！");
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }


    /**
     * 获取信息发送查询里的详细
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void getMmsTplDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            //模板ID
            String tmpid = request.getParameter("tmpid");
            //操作员用户ID
            //String lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);


            LfSysuser user = baseBiz.getById(LfSysuser.class, lguserid);
            //获取对应的彩信任务
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //LfTemplate template = baseBiz.getById(LfTemplate.class, tmpid);
            JSONObject jsonObject = new JSONObject();
            //设置条件的MAP
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
            //conditionMap.put("ProUserCode",String.valueOf(template.getUserId()));
            conditionMap.put("infoType", "4");
            conditionMap.put("mtId", tmpid);
            conditionMap.put("RState&in", "1,2");
            conditionMap.put("isComplete", "1");
            orderByMap.put("RLevel", StaticValue.ASC);
            orderByMap.put("preRv", StaticValue.DESC);
            List<LfFlowRecord> flowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);
            JSONArray members = new JSONArray();
            if (flowRecords != null && flowRecords.size() > 0) {
                LinkedHashMap<Long, String> nameMap = new LinkedHashMap<Long, String>();
                conditionMap.clear();
                String auditName = "";
                for (int j = 0; j < flowRecords.size(); j++) {
                    LfFlowRecord temp = flowRecords.get(j);
                    auditName = auditName + temp.getUserCode() + ",";
                }
                List<LfSysuser> sysuserList = null;
                if (auditName != null && !"".equals(auditName)) {
                    auditName = auditName.substring(0, auditName.length() - 1);
                    conditionMap.put("userId&in", auditName);
                    conditionMap.put("corpCode", user.getCorpCode());
                    sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
                    if (sysuserList != null && sysuserList.size() > 0) {
                        for (LfSysuser sysuser : sysuserList) {
                            nameMap.put(sysuser.getUserId(), sysuser.getName());
                        }
                    }
                }

                //是否有审批信息1有  2 没有
                jsonObject.put("haveRecord", "1");
                JSONObject member = null;
                LfFlowRecord record = null;
                for (int i = 0; i < flowRecords.size(); i++) {
                    member = new JSONObject();
                    record = flowRecords.get(i);
                    member.put("mmsRlevel", record.getRLevel().toString() + "/" + record.getRLevelAmount().toString());
                    //审批人
                    if (nameMap != null && nameMap.size() > 0 && nameMap.containsKey(record.getUserCode())) {
                        member.put("mmsReviname", nameMap.get(record.getUserCode()));
                    } else {
                        member.put("mmsReviname", "-");
                    }
                    if (record.getRTime() == null) {
                        member.put("mmsrtime", "-");
                    } else {
                        member.put("mmsrtime", df.format(record.getRTime()));
                    }
                    //审批结果
                    int state = record.getRState();
                    switch (state) {
                        case -1:
                            member.put("mmsexstate", "未审批");
                            break;
                        case 1:
                            member.put("mmsexstate", "审批通过");
                            break;
                        case 2:
                            member.put("mmsexstate", "审批不通过");
                            break;
                        default:
                            member.put("mmsexstate", "[无效的标示]");
                    }

                    if ("".equals(record.getComments()) || record.getComments() == null) {
                        member.put("mmsComments", "");
                    } else {
                        member.put("mmsComments", record.getComments());
                    }
                    members.add(member);
                }
                jsonObject.put("members", members);
            } else {
                jsonObject.put("haveRecord", "2");
            }
            conditionMap.clear();
            String firstshowname = "";
            String firstcondition = "";
            //一级都没有审核
            //获取下一级审核
            conditionMap.put("infoType", "4");
            conditionMap.put("mtId", String.valueOf(tmpid));
            conditionMap.put("RState", "-1");
            conditionMap.put("isComplete", "2");
            List<LfFlowRecord> unflowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);
            LfFlowRecord lastrecord = null;
            Long depId = null;
            String[] recordmsg = new String[2];
            recordmsg[0] = "";
            recordmsg[1] = "";
            String isshow = "2";
            if (unflowRecords != null && unflowRecords.size() > 0) {
                isshow = "1";
                StringBuffer useridstr = new StringBuffer();
                for (LfFlowRecord temp : unflowRecords) {
                    useridstr.append(temp.getUserCode()).append(",");
                }
                if (lastrecord == null) {
                    lastrecord = unflowRecords.get(0);
                }
                List<LfSysuser> sysuserList = null;
                if (useridstr != null && !"".equals(useridstr.toString())) {
                    String str = useridstr.toString().substring(0, useridstr.toString().length() - 1);
                    conditionMap.clear();
                    conditionMap.put("userId&in", str);
                    conditionMap.put("corpCode", user.getCorpCode());
                    sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
                    if (sysuserList != null && sysuserList.size() > 0) {
                        for (LfSysuser sysuser : sysuserList) {
                            firstshowname = firstshowname + sysuser.getName() + "&nbsp;&nbsp;";
                            if (depId == null) {
                                depId = sysuser.getDepId();
                            }
                        }
                    }
                }
                if (lastrecord != null) {
                    //审核类型  1操作员  4机构  5逐级审核
                    Integer rtype = lastrecord.getRType();
                    firstcondition = lastrecord.getRCondition() + "";
                    ReviewBiz reviewBiz = new ReviewBiz();
                    //当是逐步审批的时候
                    if (rtype == 5) {
                        //获取逐级审批
                        boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(lastrecord.getPreRv().intValue(), lastrecord.getProUserCode());
                        //逐级审批的最后一级
                        if (isLastLevel) {
                            if (lastrecord.getRLevelAmount() != 1) {
                                lastrecord.setRLevel(1);
                                recordmsg = reviewBiz.getNextFlowRecord(lastrecord, user.getCorpCode());
                            }
                        } else {
                            LfDep dep = baseBiz.getById(LfDep.class, depId);
                            if (dep != null) {
                                LfDep pareDep = baseBiz.getById(LfDep.class, dep.getSuperiorId());
                                if (pareDep != null) {
                                    recordmsg[0] = pareDep.getDepName();
                                    recordmsg[1] = lastrecord.getRCondition() + "";
                                }
                            }
                        }
                    } else {
                        //该流程审批的最后一级
                        if (lastrecord.getRLevel().intValue() != lastrecord.getRLevelAmount().intValue()) {
                            recordmsg = reviewBiz.getNextFlowRecord(lastrecord, user.getCorpCode());
                        }
                    }
                }
            }
            jsonObject.put("isshow", isshow);
            jsonObject.put("firstshowname", firstshowname);
            jsonObject.put("firstcondition", firstcondition);
            jsonObject.put("secondshowname", recordmsg[0]);
            jsonObject.put("secondcondition", recordmsg[1]);

            response.getWriter().print(jsonObject.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取彩信模板详情审批信息出现异常！");
        }
    }

    //创建机构树
    public void createinstalltree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //机构id
            String depId = request.getParameter("depId");
            //String lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);


//			String tempId = request.getParameter("tempId");
            String tree = getinstallTree(depId, lguserid);
            response.getWriter().print(tree);
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "创建共享模板机构树出现异常！");
        }
    }

    //获取机构树
    private String getinstallTree(String depId, String lguserid) {
        StringBuffer tree = new StringBuffer("[");
        try {
            LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
            String corpCode = lfsysuser.getCorpCode();
            //排序集合
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            //查询条件集合
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //机构状态
            conditionMap.put("depState", "1");
            orderbyMap.put("depId", StaticValue.ASC);
            //企业编码
            conditionMap.put("corpCode", corpCode);
            //DEPID为空
            if (depId != null && !"".equals(depId)) {
                //父及机构id
                conditionMap.put("superiorId", depId);
            }
            List<LfDep> lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
            LfDep lfDep = null;
            for (int i = 0; i < lfDeps.size(); i++) {
                lfDep = lfDeps.get(i);
                tree.append("{");
                tree.append("id:").append(lfDep.getDepId());
                tree.append(",name:'").append(lfDep.getDepName()).append("'");
                tree.append(",pId:").append(lfDep.getSuperiorId());
                tree.append(",depId:'").append(lfDep.getDepId()).append("'");
                tree.append(",isParent:").append(true);
                tree.append(",checked:").append(false);
                tree.append("}");
                if (i != lfDeps.size() - 1) {
                    tree.append(",");
                }
            }
            tree.append("]");
        } catch (Exception e) {
            EmpExecutionContext.error(e, "创建共享模板的机构树出现异常！");
            tree = new StringBuffer("");
        }
        return tree.toString();

    }

    /**
     * 获取该机构下的操作员
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSysuserByDepId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //企业编码
            String lgcorpcode = request.getParameter("lgcorpcode");
            //机构ID
            String depId = request.getParameter("depId");
            //查询名称
            String searchname = request.getParameter("searchname");
            //排序集合
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            //查询条件集合
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            StringBuffer sb = new StringBuffer("suceess#");
            conditionMap.put("corpCode", lgcorpcode);
            if (depId != null && !"".equals(depId)) {
                conditionMap.put("depId", depId);
            }
            conditionMap.put("userState&<", "2");
            orderbyMap.put("name", "asc");
            if (searchname != null && !"".equals(searchname)) {
                conditionMap.put("name&like", searchname);
            }

            List<LfSysuser> lfsysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, orderbyMap);
            if (lfsysuserList != null && lfsysuserList.size() > 0) {
                StringBuffer buffer = new StringBuffer();
                for (LfSysuser user : lfsysuserList) {
                    buffer.append(user.getUserId()).append(",");
                    sb.append("<option value='").append(user.getUserId()).append("' isdeporuser='2' >");
                    sb.append(user.getName().trim().replace("<", "&lt;").replace(">", "&gt;"));
                    sb.append("</option>");
                }
            }
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取该机构下的操作员失败！");
        }
    }

    /**
     * 设置模板共享对象
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void updateShareTemp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LfSysuser lfsysuser = null;
        String tempId = null;
        String infoType = "1";
        try {
            //用户ID
            //String lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);

            lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
            //所设置的机构对象
            String depidstr = request.getParameter("depidstr");
            //所设置的操作员对象
            String useridstr = request.getParameter("useridstr");
            //该模板Id
            tempId = request.getParameter("tempid");
            //模板类型 1:短信模板  2：彩信模板
            infoType = request.getParameter("infotype");
            String returnmsg = mtb.updateShareTemp(depidstr, useridstr, tempId, infoType, lfsysuser);

            //增加操作日志
            if ("success".equals(returnmsg)) {
                Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
                if (loginSysuserObj != null) {
                    LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                    String contnet = "共享彩信模板成功。(彩信模板ID为：" + tempId + "，共享的机构对象ID为：" + depidstr + "，共享的操作员对象ID为：" + useridstr + ")。";
                    EmpExecutionContext.info("模板编辑", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "ADD");


                }
            }

            response.getWriter().print(returnmsg);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "更新模板共享对象出错，" + infoMap.get(infoType) + "ID:" + tempId);
            response.getWriter().print("");
        }
    }

    /**
     * 列表页面获取已设置模板共享对象
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String tempId = request.getParameter("tempid");
            String infoType = request.getParameter("infoType");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("templId", tempId);
            conditionMap.put("templType", infoType);
            conditionMap.put("shareType", "1");
            List<LfTmplRela> bindobjList = baseBiz.getByCondition(LfTmplRela.class, conditionMap, null);
            StringBuffer sb = new StringBuffer();
            //保存操作员的USERID
            HashSet<Long> userSet = new HashSet<Long>();
            //保存机构的USERID
            HashSet<Long> depSet = new HashSet<Long>();
            if (bindobjList != null && bindobjList.size() > 0) {
                for (int i = 0; i < bindobjList.size(); i++) {
                    LfTmplRela temp = bindobjList.get(i);
                    //，1-机构，2--操作员
                    if (temp.getToUserType() == 2 && !userSet.contains(temp.getToUser())) {
                        userSet.add(temp.getToUser());
                        LfSysuser user = baseBiz.getById(LfSysuser.class, temp.getToUser());
                        sb.append("<option value=\'" + temp.getToUser() + "\' isdeporuser='2'>[操作员]" + user.getName());
                        sb.append("</option>");
                    } else if (temp.getToUserType() == 1 && !depSet.contains(temp.getToUser())) {
                        depSet.add(temp.getToUser());
                        LfDep dep = baseBiz.getById(LfDep.class, temp.getToUser());
                        sb.append("<option value=\'" + temp.getToUser() + "\' isdeporuser='1'>[机构]" + dep.getDepName() + "</option>");
                    }
                }
            }
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "列表页面获取设置模板共享对象出现异常！");
            response.getWriter().print("");
        }
    }

    /**
     * @param request
     * @param modName   模块名称
     * @param opContent 操作详情
     * @param opType    操作类型 ADD UPDATE DELETE GET OTHER
     * @description 记录操作成功日志
     * @author zousy <zousy999@qq.com>
     * @datetime 2015-3-3 上午11:29:50
     */
    public void opSucLog(HttpServletRequest request, String modName, String opContent, String opType) {
        LfSysuser lfSysuser = null;
        Object obj = request.getSession(false).getAttribute("loginSysuser");
        if (obj == null) {
            return;
        }
        lfSysuser = (LfSysuser) obj;
        EmpExecutionContext.info(modName, lfSysuser.getCorpCode(), String.valueOf(lfSysuser.getUserId()), lfSysuser.getUserName(), opContent, opType);
    }

}

