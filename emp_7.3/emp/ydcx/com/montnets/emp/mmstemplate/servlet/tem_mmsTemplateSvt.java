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
 * @datetime 2013-9-14 ??????02:25:03
 * @description
 */
@SuppressWarnings("serial")
public class tem_mmsTemplateSvt extends BaseServlet {

    //????????????
    private final String opModule = StaticValue.TEMP_MANAGE;
    //????????????
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
    //????????????
    //String opType = null;
    //????????????
    //String opContent =null;
    static Map<String, String> infoMap = new HashMap<String, String>();

    static {
        infoMap.put("1", "????????????");
        infoMap.put("2", "????????????");
        infoMap.put("3", "????????????");
    }

    /**
     * @param request
     * @param response
     */
    //??????????????????????????????
    public void find(HttpServletRequest request, HttpServletResponse response) {

        String lgguid = "";
        Long userGuid = null;
        Long lguserId = null;
        LfSysuser sysUser = null;
        //????????????GUID
        lgguid = request.getParameter("lgguid");
        try {
            if (lgguid == null || "".equals(lgguid.trim()) || "undefined".equals(lgguid.trim())) {
                EmpExecutionContext.error("????????????????????????lgguid???????????????lgguid=" + lgguid + "????????????Session?????????");
                LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
                lgguid = String.valueOf(loginSysuser.getGuId());
            }
            userGuid = Long.valueOf(lgguid);
            //????????????????????????
            sysUser = baseBiz.getByGuId(LfSysuser.class, userGuid);
            //????????????????????????ID
            lguserId = sysUser.getUserId();
        } catch (Exception e) {
            //????????????
            EmpExecutionContext.error(e, "????????????????????????????????????????????????");
        }

        List<LfTemplateVo> mmsList = null;
        LfTemplateVo mt = new LfTemplateVo();

        dirUrl = txtfileutil.getWebRoot();
        PageInfo pageInfo = new PageInfo();
        //??????????????????
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        long begin_time = System.currentTimeMillis();
        try {
            //?????????????????????
            boolean isFirstEnter = false;
            isFirstEnter = pageSet(pageInfo, request);
            if (!isFirstEnter) {
                //??????
                String theme = request.getParameter("theme");
                //??????
                String tmCode = request.getParameter("tmCode");

                //???????????????
                String userName = request.getParameter("userName");
                //????????????
                String rstate = request.getParameter("rState");
                //????????????
                String state = request.getParameter("state");
                //????????????
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
            //?????????3-????????????;4-???????????????
            //????????????id
            String flowIdStr = request.getParameter("flowId");
            if (flowIdStr != null && !"".equals(flowIdStr)) {
                mt.setFlowID(Long.valueOf(flowIdStr));
            }
            mmsList = mtb.getTemplateByCondition(lguserId, mt, pageInfo);
            //?????????????????????
            new ServerInof().setServerName(getServletContext().getServerInfo());
            request.setAttribute("mmsVo", mt);
            request.setAttribute("mmsList", mmsList);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("findresult", "");
            //??????????????????
            if (pageInfo != null) {
                long end_time = System.currentTimeMillis();
                String opContent = "?????????????????????" + format.format(begin_time) + ",??????:" + (end_time - begin_time) + "??????????????????" + pageInfo.getTotalRec();
                opSucLog(request, "????????????", opContent, "GET");
            }
            request.getRequestDispatcher(PATH + "/tem_mmsTemplate.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????????????????");
            request.setAttribute("findresult", "-1");
            request.setAttribute("pageInfo", pageInfo);
        }
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String opUser = ((LfSysuser) request.getSession(false).getAttribute("loginSysuser")).getUserName();
        String opType = null;
        String opContent = null;
        //????????????????????????ID
        //String id = request.getParameter("lguserid");
        //???????????? session????????????????????????
        String id = SysuserUtil.strLguserid(request);


        String lgcorpcode = request.getParameter("lgcorpcode");
        Long lguserId = null;
        try {
            lguserId = Long.valueOf(id);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????????????????lguserid???????????????");
        }
        //??????
        String opT = request.getParameter("opType");
        if ("add".equals(opT)) {
            opType = StaticValue.ADD;
            opContent = "??????????????????";
        } else if ("copy".equals(opT)) {
            opType = StaticValue.OTHER;
            opContent = "??????????????????";
        } else if ("edit".equals(opT)) {
            opType = StaticValue.UPDATE;
            opContent = "??????????????????";
        }
        TmsFile tmsfile = new TmsFile();
        String corpcodedir = lgcorpcode + "/";
        new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_TEMPLATES + corpcodedir);
        String fileName = StaticValue.MMS_TEMPLATES + corpcodedir + "3_" + (int) (lguserId - 0) + "_"
                + (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime()) + ".tms";
        //????????????
        String templateType = request.getParameter("templateType");
        //????????????ID
        String mmsTitle = request.getParameter("mt");
        //????????????
        String mmsCode = request.getParameter("mmsCode");
        //?????????????????????????????????????????????
        mmsTitle = mmsTitle.replace("???", "???");
        LfTemplate lmt = null;
        //????????????
        String state = request.getParameter("s");
        //??????
        String[] content = request.getParameterValues("cont[]");
        //??????????????????
        Integer paramCnt = 0;
        //??????http??????
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
                        //nr = nr.replaceAll("\\{#??????([1-9][0-9]*)#\\}", "#p_$1#");
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
                //??????????????????????????????
                if ("success".equals(commBiz.uploadFileToFileCenter(fileName))) {
                    isuploadFileServerSuccess = true;
                }
            }
            //??????????????????????????????????????????????????????????????????????????????????????????
            if (StaticValue.getISCLUSTER() != 1 || isuploadFileServerSuccess == true) {
                if ("add".equals(opT) || "copy".equals(opT)) {
                    String repate = checkRepeat(request);
                    if ("repeat".equals(repate)) {
                        response.getWriter().print("repeat");
                        return;
                    }

                    lmt = new LfTemplate();
                    //????????????
                    lmt.setTmName(mmsTitle);
                    lmt.setTmCode(mmsCode == null ? "" : mmsCode.trim());
                    //????????????(????????????-0????????????-1?????????1?????????2)
                    lmt.setIsPass(-1);
                    //???????????????0?????????1?????????2?????????
                    lmt.setTmState(Long.parseLong(state));
                    //????????????
                    lmt.setAddtime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    //????????????
                    lmt.setTmMsg(fileName);
                    //?????????ID
                    lmt.setUserId(lguserId);
                    lmt.setCorpCode(lgcorpcode);
                    // ?????????3-????????????;4-???????????????
                    lmt.setTmpType(new Integer("4"));
                    lmt.setParamcnt(paramCnt);
                    lmt.setDsflag(Long.parseLong(templateType));
                    //??????????????????
                    lmt.setAuditstatus(-1);
                    //????????????????????????
                    lmt.setTmplstatus(0);
                    String emp_templid = getEmpTemplateLid();
                    lmt.setEmptemplid(emp_templid);
                    lmt.setSubmitstatus(0);
                    MmsTemplate mmstl = new MmsTemplate();//??????????????????????????????
                    mmstl.setUserId(lguserId.toString()); //?????????
                    if (StaticValue.getCORPTYPE() == 1) {
                        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                        conditionMap.put("corpCode", lgcorpcode);
                        //???????????????
                        conditionMap.put("isValidate", "1");
                        List<LfMmsAccbind> mmsaccs = baseBiz.getByCondition(LfMmsAccbind.class, conditionMap, null);
                        if (mmsaccs != null && mmsaccs.size() > 0) {
                            mmstl.setUserId(mmsaccs.get(0).getMmsUser());
                        } else {
                            lmt.setTmState(2L);
                        }
                    }
                    mmstl.setAuditStatus(0);//????????????
                    mmstl.setTmplStatus(0);//????????????
                    mmstl.setParamCnt(paramCnt);//????????????
                    mmstl.setTmplPath(httpUrl + fileName);//????????????
                    mmstl.setRecvTime(lmt.getAddtime());//??????????????????
                    //???""???????????????
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


                    //??????????????????
                    if (result > 0) {
                        Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
                        if (loginSysuserObj != null) {
                            LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                            String contnet = opContent + "?????????[??????????????????????????????????????????](" + lmt.getTmName() + "???" + lmt.getDsflag() + "???" + lmt.getTmState() + ")";
                            EmpExecutionContext.info("????????????", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "ADD");
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
                    //????????????(????????????-0????????????-1?????????1?????????2)
                    lmt.setTmState(Long.parseLong(state));
                    //???????????????0?????????1?????????2????????????
                    lmt.setAddtime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    lmt.setTmMsg(fileName);
                    lmt.setUserId(lguserId);
                    lmt.setCorpCode(lgcorpcode);
                    lmt.setTmpType(new Integer("4"));
                    lmt.setParamcnt(paramCnt);
                    lmt.setDsflag(Long.parseLong(templateType));
                    //????????????????????????0?????????????????????
					/*if(paramCnt>0)
					{
						lmt.setDsflag(1L);
					}
					else
					{
						lmt.setDsflag(0L);
					}*/

                    MmsTemplate mmstl = new MmsTemplate();//??????????????????????????????
                    mmstl.setUserId(lguserId.toString()); //?????????
                    if (StaticValue.getCORPTYPE() == 1) {
                        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                        conditionMap.put("corpCode", lgcorpcode);
                        //???????????????
                        conditionMap.put("isValidate", "1");
                        List<LfMmsAccbind> mmsaccs = baseBiz.getByCondition(LfMmsAccbind.class, conditionMap, null);
                        if (mmsaccs != null && mmsaccs.size() > 0) {
                            mmstl.setUserId(mmsaccs.get(0).getMmsUser());
                        } else {
                            lmt.setTmState(2L);
                        }
                    }
                    mmstl.setAuditStatus(0);//????????????
                    mmstl.setTmplStatus(0);//????????????
                    mmstl.setParamCnt(paramCnt);//????????????
                    mmstl.setTmplPath(httpUrl + fileName);//????????????
                    mmstl.setRecvTime(lmt.getAddtime());//??????????????????
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

                    //??????????????????
                    if (result) {
                        Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
                        if (loginSysuserObj != null) {
                            LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                            String contnet = opContent + "?????????[??????????????????????????????????????????](" + oldlmt.getTmName() + "???" + oldlmt.getDsflag() + "???" + oldlmt.getTmState() + ")"
                                    + "-->(" + lmt.getTmName() + "???" + lmt.getDsflag() + "???" + lmt.getTmState() + ")";
                            EmpExecutionContext.info("????????????", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "UPDATE");
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
            EmpExecutionContext.error(ex, "???????????????????????????????????????????????????");
            return;
        } catch (Exception e) {
            spLog.logFailureString(opUser, opModule, opType, opContent + opSper, e, lgcorpcode);
            EmpExecutionContext.error(e, "???????????????????????????????????????????????????");
            response.getWriter().print("false");
        }
    }


    /**
     * ????????????????????????
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
            //???????????????
            try {
                List<LfTemplate> tempList = baseBiz.getByCondition(LfTemplate.class, conditionMap, null);
                if (tempList != null && tempList.size() > 0) {
                    return "repeat";
                }

            } catch (Exception e) {
                EmpExecutionContext.error(e, "?????????????????????????????????");
                //??????????????????
                return "";
            }
        }
        return "";
    }


    @SuppressWarnings("unchecked")
    public void uploadTms(HttpServletRequest request, HttpServletResponse response) {
        // ????????????
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
            //????????????
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
                    //????????????
                    //lmt.setTmName("????????????");
                    lmt.setTmName(MessageUtils.extractMessage("ydcx", "ydcx_cxyy_mbbj_drmb", request));
                    //????????????(????????????-0????????????-1?????????1?????????2)
                    lmt.setIsPass(0);
                    //???????????????0?????????1?????????2?????????
                    lmt.setTmState(2L);
                    //????????????
                    lmt.setAddtime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    //????????????
                    lmt.setTmMsg(fileName);
                    //?????????ID
                    lmt.setUserId(lguserid);
                    lmt.setTmCode(importCode);
                    //???????????? 	0????????????	1???????????????	2???????????????	3????????????
                    lmt.setSubmitstatus(0);
                    lmt.setCorpCode(lgcorpcode);
                    // ?????????3-????????????;4-???????????????
                    lmt.setTmpType(4);
                    String tmsText = mpb.getTmsText(fileName);
                    int paramCnt = getParamCnt(tmsText);
                    lmt.setParamcnt(paramCnt);
                    //????????????????????????0?????????????????????
                    if (paramCnt > 0) {
                        lmt.setDsflag(1L);
                    } else {
                        lmt.setDsflag(0L);
                    }
                    String emp_templid = getEmpTemplateLid();
                    lmt.setEmptemplid(emp_templid);
                    //??????????????????
                    lmt.setAuditstatus(-1);
                    //????????????????????????
                    lmt.setTmplstatus(0);
                    //??????????????????????????????
                    MmsTemplate mmstl = new MmsTemplate();
                    //?????????
                    mmstl.setUserId(lguserid.toString());
                    //????????????
                    mmstl.setAuditStatus(0);
                    //????????????
                    mmstl.setTmplStatus(0);
                    //????????????
                    mmstl.setParamCnt(paramCnt);
                    //????????????
                    mmstl.setTmplPath(dirUrl + fileName);
                    //??????????????????
                    mmstl.setRecvTime(lmt.getAddtime());
                    mmstl.setAuditor("");
                    mmstl.setAuditTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    mmstl.setRemarks("");

                    long result = mtb.addTemplate(mmstl, lmt);

                    //??????????????????
                    if (result > 0) {
                        Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
                        if (loginSysuserObj != null) {
                            LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                            String contnet = "??????????????????" + "?????????[??????????????????????????????????????????](" + lmt.getTmName() + "???" + lmt.getDsflag() + "???" + lmt.getTmState() + ")";
                            EmpExecutionContext.info("????????????", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "ADD");
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
            EmpExecutionContext.error(e, "??????tms?????????????????????");
        }
    }

    //??????
    public void doCopy(HttpServletRequest request, HttpServletResponse response) {
        String tmId = request.getParameter("tmId");
        String opType = request.getParameter("opType");
        try {
            if (StaticValue.getCORPTYPE() == 1) {
                //????????????ID
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
            EmpExecutionContext.error(e, "?????????????????????????????????");
        }
    }

    //??????
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String opUser = ((LfSysuser) request.getSession(false).getAttribute("loginSysuser")).getUserName();
        String opType = null;
        String opContent = null;
        opType = StaticValue.DELETE;

        String corpcode = request.getParameter("lgcorpcode");
        try {
            //???????????????????????????ID
            String[] ids = request.getParameter("ids").toString().split(",");

            //?????????????????????????????????
            String idsStr = request.getParameter("ids").toString();
            LinkedHashMap<String, String> logConditionMap = new LinkedHashMap<String, String>();
            logConditionMap.put("tmid&" + StaticValue.IN, idsStr);
            LinkedHashMap<String, String> logOrderMap = new LinkedHashMap<String, String>();
            logOrderMap.put("tmid", StaticValue.ASC);
            List<LfTemplate> deleteTemplateList = new BaseBiz().getByCondition(LfTemplate.class, logConditionMap, logOrderMap);

            int count = mtb.delTempByTmId(ids);
            opContent = "??????" + ids.length + "???????????????";

            //??????????????????
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                //??????????????????
                String deleteMsg = "";
                for (int i = 0; i < deleteTemplateList.size(); i++) {
                    deleteMsg += "[" + deleteTemplateList.get(i).getTmid() + "???" + deleteTemplateList.get(i).getTmName() + "???" + deleteTemplateList.get(i).getDsflag() + "]???";
                }
                String contnet = "??????????????????" + "?????????(?????????" + ids.length + ")[??????ID??????????????????????????????](" + deleteMsg + ")";
                EmpExecutionContext.info("????????????", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "DELETE");
            }

            response.getWriter().print(count);
            //????????????????????????
            spLog.logSuccessString(opUser, opModule, opType, opContent, corpcode);
        } catch (Exception e) {
            response.getWriter().print(0);
            //????????????????????????
            spLog.logFailureString(opUser, opModule, opType, opContent + opSper, e, corpcode);
            EmpExecutionContext.error(e, "???????????????????????????");
        }
    }

    //??????????????????
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
            //???????????? session????????????????????????
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
                    + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(Calendar.getInstance().getTime());  //?????????????????????
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
                    //????????????????????????????????????
                    String name = dirUrl + StaticValue.MMS_TEMPRESOURCE + corpcodedir + imgUrl;
                    fileItem.write(new File(name));
                    //???????????????
                    size = fileItem.getSize();
                    BufferedImage bi = ImageIO.read(new File(name));
                    width = bi.getWidth();
                    //???
                    height = bi.getHeight();
                    //???
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
                //????????????
                out.print("{url:'" + StaticValue.MMS_TEMPRESOURCE + corpcodedir + imgUrl + "',size:" + size + ",width:" + width + ",height:" + height + "}");
            } else if (!"".equals(musicUrl)) {
                //????????????
                out.print("{url:'" + StaticValue.MMS_TEMPRESOURCE + corpcodedir + musicUrl + "',size:" + size + "}");
            }/*else if(!"".equals(tmsUrl)){
				//???????????????????????????????????????json??????
				String result =this.getTmsFileInfo(StaticValue.MMS_TEMPRESOURCE+tmsUrl);
				out.print(result);
			}*/
        } catch (FileUploadException e) {
            StringBuffer logInfo = new StringBuffer();
            logInfo.append("?????????????????????????????????????????????????????????userId:").append(lguserid).append("???lgcorpcode:").append(lgcorpcode)
                    .append("?????????:").append(System.currentTimeMillis() - startTime).append("ms");
            EmpExecutionContext.error(e, logInfo.toString());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "???????????????????????????????????????");
        }
    }

    //???????????????????????????
	/*public void getTmMsg(HttpServletRequest request,
			HttpServletResponse response) {
		// ????????????ID
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
    //???????????????????????????????????????????????????
    public void checkBadWord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String words = "";
        String templateType = request.getParameter("templateType");
        try {
            String[] content = request.getParameterValues("content[]");
            //???????????????????????????????????????
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
                        //Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(jo.get("textContent").toString().replaceAll("\\{#??????([1-9][0-9]*)#\\}", "#p_$1#"));
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
                            words += "[???" + (i + 1) + "??????" + s + "]";
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
            EmpExecutionContext.error(e, "?????????????????????????????????????????????????????????????????????");
        } finally {
            response.getWriter().print(words);
        }
    }

    //??????????????????
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
            EmpExecutionContext.error(e, "?????????????????????????????????");
        }
    }

    /**
     * ??????????????????????????????????????????
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
            EmpExecutionContext.error(e, "?????????????????????????????????????????????????????????");
        }
    }

    //?????????????????????
    public void doAdd(HttpServletRequest request, HttpServletResponse response) {
        try {
            //???????????????????????????
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
            //??????
            request.setAttribute("type", type);
            //?????????????????????
            new ServerInof().setServerName(getServletContext().getServerInfo());
            request.getRequestDispatcher(PATH + "/tem_addMmsTemplate.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
        }
    }

    //?????????????????????
    public void changeState(HttpServletRequest request, HttpServletResponse response) {
        try {
            //??????ID
            String id = request.getParameter("id");
            //?????????????????????
            String state = request.getParameter("t");
            LfTemplate tem = baseBiz.getById(LfTemplate.class, id);
            tem.setTmState(Long.parseLong(state));
            boolean r = baseBiz.updateObj(tem);

            String changeStr = "";
            if (Long.parseLong(state) == 0L) {
                changeStr = "???????????????????????????";
            } else {
                changeStr = "???????????????????????????";
            }
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String contnet = "??????????????????????????????(????????????ID??????" + id + "???????????????????????????" + tem.getTmName() + ")???" + changeStr + "???";
                EmpExecutionContext.info("????????????", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "UPDATE");
            }

            if (r) {
                response.getWriter().print("true");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "???????????????????????????????????????");
        }
    }

    //????????????
    public void doExport(HttpServletRequest request, HttpServletResponse response) {
        InputStream inStream = null;
        try {
            //???????????????????????????
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
            EmpExecutionContext.error(e, "?????????????????????????????????");
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "????????????????????????????????????????????????");
                }
            }
        }
    }

    //??????
    public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //???????????????????????????
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
            EmpExecutionContext.error(e, "???????????????????????????");
        }
    }

    //??????????????????
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
            //???????????????
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
                        //???????????????????????????js????????????
                        // ???????????????
                        //text = text.replace("\\","\\\\");
                        //?????????????????????
                        //text = text.replace("\"","\\\"");
                        jsonObject.put("textContent", text != null ? text.replaceAll("#[pP]_([1-9][0-9]*)#", "{#??????$1#}") : "");
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

            EmpExecutionContext.error(e, "???????????????????????????????????????");
            return null;
        }
        return jsonArray.toString();
    }

    //?????????
    private boolean writeFile(String filePath, byte[] contents) {
        boolean result = false;
        FileOutputStream fos = null;
        try {
            filePath = dirUrl + filePath;
            File file = new File(filePath);
            if (!file.exists()) {
                boolean state = file.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("??????????????????");
                }
            } else {
                if (file.isFile()) {
                    boolean delete = file.delete();
                    if (!delete) {
                        EmpExecutionContext.error("??????????????????");
                    }
                }
                boolean state = file.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("??????????????????");
                }
            }


            fos = new FileOutputStream(filePath);
            fos.write(contents, 0, contents.length);
            fos.close();
            result = true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????????????????????????????");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "???????????????????????????????????????????????????");
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
     * ????????????id?????????
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

            //????????????4?????????????????????????????????
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
            //????????????
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
            EmpExecutionContext.error(e, "??????????????????id?????????");
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
            EmpExecutionContext.error(e, "??????tms?????????????????????");
            return false;
        }
        return isOk;
    }

    /**
     * ?????????
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
        //??????????????????????????????????????????????????????????????????????????????????????????????????????
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
                    paramContent = paramContent.replaceAll(",", "???");
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
            EmpExecutionContext.error(e, "???????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     */
    public void checkMmsFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            TxtFileUtil tfu = new TxtFileUtil();
            String url = request.getParameter("url");
            //??????????????????????????????????????????????????????????????????????????????????????????????????????
            if (StaticValue.getISCLUSTER() == 1 && !tfu.checkFile(url)) {
                CommonBiz commBiz = new CommonBiz();
                commBiz.downloadFileFromFileCenter(url);
            }
            response.getWriter().print(tfu.checkFile(url));
        } catch (Exception e) {
            //????????????
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
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
            EmpExecutionContext.error(e, "??????tms?????????????????????");
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }


    /**
     * ????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void getMmsTplDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            //??????ID
            String tmpid = request.getParameter("tmpid");
            //???????????????ID
            //String lguserid = request.getParameter("lguserid");
            //???????????? session????????????????????????
            String lguserid = SysuserUtil.strLguserid(request);


            LfSysuser user = baseBiz.getById(LfSysuser.class, lguserid);
            //???????????????????????????
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //LfTemplate template = baseBiz.getById(LfTemplate.class, tmpid);
            JSONObject jsonObject = new JSONObject();
            //???????????????MAP
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

                //?????????????????????1???  2 ??????
                jsonObject.put("haveRecord", "1");
                JSONObject member = null;
                LfFlowRecord record = null;
                for (int i = 0; i < flowRecords.size(); i++) {
                    member = new JSONObject();
                    record = flowRecords.get(i);
                    member.put("mmsRlevel", record.getRLevel().toString() + "/" + record.getRLevelAmount().toString());
                    //?????????
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
                    //????????????
                    int state = record.getRState();
                    switch (state) {
                        case -1:
                            member.put("mmsexstate", "?????????");
                            break;
                        case 1:
                            member.put("mmsexstate", "????????????");
                            break;
                        case 2:
                            member.put("mmsexstate", "???????????????");
                            break;
                        default:
                            member.put("mmsexstate", "[???????????????]");
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
            //?????????????????????
            //?????????????????????
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
                    //????????????  1?????????  4??????  5????????????
                    Integer rtype = lastrecord.getRType();
                    firstcondition = lastrecord.getRCondition() + "";
                    ReviewBiz reviewBiz = new ReviewBiz();
                    //???????????????????????????
                    if (rtype == 5) {
                        //??????????????????
                        boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(lastrecord.getPreRv().intValue(), lastrecord.getProUserCode());
                        //???????????????????????????
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
                        //??????????????????????????????
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
            EmpExecutionContext.error(e, "???????????????????????????????????????????????????");
        }
    }

    //???????????????
    public void createinstalltree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //??????id
            String depId = request.getParameter("depId");
            //String lguserid = request.getParameter("lguserid");
            //???????????? session????????????????????????
            String lguserid = SysuserUtil.strLguserid(request);


//			String tempId = request.getParameter("tempId");
            String tree = getinstallTree(depId, lguserid);
            response.getWriter().print(tree);
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "??????????????????????????????????????????");
        }
    }

    //???????????????
    private String getinstallTree(String depId, String lguserid) {
        StringBuffer tree = new StringBuffer("[");
        try {
            LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
            String corpCode = lfsysuser.getCorpCode();
            //????????????
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            //??????????????????
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //????????????
            conditionMap.put("depState", "1");
            orderbyMap.put("depId", StaticValue.ASC);
            //????????????
            conditionMap.put("corpCode", corpCode);
            //DEPID??????
            if (depId != null && !"".equals(depId)) {
                //????????????id
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
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
            tree = new StringBuffer("");
        }
        return tree.toString();

    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSysuserByDepId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //????????????
            String lgcorpcode = request.getParameter("lgcorpcode");
            //??????ID
            String depId = request.getParameter("depId");
            //????????????
            String searchname = request.getParameter("searchname");
            //????????????
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            //??????????????????
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
            EmpExecutionContext.error(e, "???????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????
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
            //??????ID
            //String lguserid = request.getParameter("lguserid");
            //???????????? session????????????????????????
            String lguserid = SysuserUtil.strLguserid(request);

            lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
            //????????????????????????
            String depidstr = request.getParameter("depidstr");
            //???????????????????????????
            String useridstr = request.getParameter("useridstr");
            //?????????Id
            tempId = request.getParameter("tempid");
            //???????????? 1:????????????  2???????????????
            infoType = request.getParameter("infotype");
            String returnmsg = mtb.updateShareTemp(depidstr, useridstr, tempId, infoType, lfsysuser);

            //??????????????????
            if ("success".equals(returnmsg)) {
                Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
                if (loginSysuserObj != null) {
                    LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                    String contnet = "???????????????????????????(????????????ID??????" + tempId + "????????????????????????ID??????" + depidstr + "???????????????????????????ID??????" + useridstr + ")???";
                    EmpExecutionContext.info("????????????", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "ADD");


                }
            }

            response.getWriter().print(returnmsg);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????????????????" + infoMap.get(infoType) + "ID:" + tempId);
            response.getWriter().print("");
        }
    }

    /**
     * ?????????????????????????????????????????????
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
            //??????????????????USERID
            HashSet<Long> userSet = new HashSet<Long>();
            //???????????????USERID
            HashSet<Long> depSet = new HashSet<Long>();
            if (bindobjList != null && bindobjList.size() > 0) {
                for (int i = 0; i < bindobjList.size(); i++) {
                    LfTmplRela temp = bindobjList.get(i);
                    //???1-?????????2--?????????
                    if (temp.getToUserType() == 2 && !userSet.contains(temp.getToUser())) {
                        userSet.add(temp.getToUser());
                        LfSysuser user = baseBiz.getById(LfSysuser.class, temp.getToUser());
                        sb.append("<option value=\'" + temp.getToUser() + "\' isdeporuser='2'>[?????????]" + user.getName());
                        sb.append("</option>");
                    } else if (temp.getToUserType() == 1 && !depSet.contains(temp.getToUser())) {
                        depSet.add(temp.getToUser());
                        LfDep dep = baseBiz.getById(LfDep.class, temp.getToUser());
                        sb.append("<option value=\'" + temp.getToUser() + "\' isdeporuser='1'>[??????]" + dep.getDepName() + "</option>");
                    }
                }
            }
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????????????????????????????????????????");
            response.getWriter().print("");
        }
    }

    /**
     * @param request
     * @param modName   ????????????
     * @param opContent ????????????
     * @param opType    ???????????? ADD UPDATE DELETE GET OTHER
     * @description ????????????????????????
     * @author zousy <zousy999@qq.com>
     * @datetime 2015-3-3 ??????11:29:50
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

