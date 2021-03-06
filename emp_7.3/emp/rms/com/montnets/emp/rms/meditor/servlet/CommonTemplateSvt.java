package com.montnets.emp.rms.meditor.servlet;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.commontempl.entity.LfTemplate;
import com.montnets.emp.rms.meditor.biz.imp.CommonTemplateBiz;
import com.montnets.emp.rms.meditor.entity.LfSubTemplate;
import com.montnets.emp.rms.meditor.entity.LfTempParam;
import com.montnets.emp.rms.meditor.tools.ExcelTool;
import com.montnets.emp.rms.meditor.tools.ParamTool;
import com.montnets.emp.rms.servmodule.constant.ServerInof;
import com.montnets.emp.rms.templmanage.biz.RmsShortTemplateBiz;
import com.montnets.emp.rms.templmanage.servlet.Mbgl_templateSvt;
import com.montnets.emp.rms.vo.LfShortTemplateVo;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.util.OOSUtil;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;

/**
 * @Author:yangdl
 * @Data:Created in 17:52 2018.8.8 008
 */
public class CommonTemplateSvt extends BaseServlet {

    private final String PATH = "rms/commontempl";
    private final OOSUtil oosUtil = new OOSUtil();
    final CommonBiz commBiz = new CommonBiz();
    private final CommonTemplateBiz commonTemplateBiz = new CommonTemplateBiz();
    private static String ottType = "12";
    private static String rmsType = "11";

    /**
     * ??????
     *
     * @param request
     * @param response
     */
    public void find(HttpServletRequest request, HttpServletResponse response) {
        try {
            LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
                    .getAttribute(StaticValue.SESSION_USER_KEY);
            PageInfo pageInfo = new PageInfo();
            LfTemplateVo lfTemplate = new LfTemplateVo();
            java.util.Map<String, List<LfIndustryUse>> induUseMap = null;
            List<LfIndustryUse> industryList = null;
            List<LfIndustryUse> useList = null;
            // ????????????????????????
            String loginOrgcode = loginSysuser.getCorpCode();
            lfTemplate.setCorpCode(loginOrgcode);
            // ????????????-11???????????????
            lfTemplate.setTmpType(11);
            lfTemplate.setIsPublic(1);// ????????????????????? 0 -?????????1-???
            pageInfo.setPageSize(9);// 100000??????????????????9?????????
            if (!"100000".equals(loginOrgcode)) {
                lfTemplate.setAuditstatus(1);// ????????????

                pageInfo.setPageSize(10);// ????????????10?????????
            }

            // ????????????/ID
            String key = "";
            if (!StringUtils.IsNullOrEmpty(request.getParameter("templName"))) {
                key = new String(request.getParameter("templName").getBytes("ISO-8859-1"), "UTF-8");
            }
            // ??????????????????
            String templStatus = request.getParameter("templStatus");
            if (!StringUtils.IsNullOrEmpty(templStatus)
                    && !"-2".equals(templStatus)) {// -2?????????
                lfTemplate.setAuditstatus(Integer.parseInt(templStatus));
            }
            // ??????-????????????
            String type = request.getParameter("type");
            // ??????ID
            String InduOrUseId = request.getParameter("InduOrUseId");

            if (!StringUtils.IsNullOrEmpty(type)
                    && !StringUtils.IsNullOrEmpty(InduOrUseId)) {
                if (type.equals("0")) {
                    // ??????ID
                    lfTemplate.setIndustryid(Integer.parseInt(InduOrUseId));
                } else if (type.equals("1")) {
                    // //??????
                    lfTemplate.setUseid(Integer.parseInt(InduOrUseId));
                }

            }

            //??????div ??????
            String indudvHeight = request.getParameter("indudv_height");
            //??????div ??????
            String usedvHeight = request.getParameter("usedv_height");
            // ?????????????????????
            boolean isFirstEnter = false;

            isFirstEnter = pageSet(pageInfo, request);

            String templName = request.getParameter("templName");
            if (!StringUtils.IsNullOrEmpty(templName)) {
                lfTemplate.setTmName(new String(templName.getBytes("ISO-8859-1"), "UTF-8"));

            }
            LfShortTemplateVo lv = new LfShortTemplateVo();
            lv.setCorpCode(loginOrgcode);
            //??????????????????
            List<LfShortTemplateVo> lfShortTemList = new RmsShortTemplateBiz().getLfShortTemList(lv);

            LfTemplateVo lfClone = (LfTemplateVo) BeanUtils.cloneBean(lfTemplate);
            lfClone.setTmName(null);//?????????????????????????????????
            // ??????????????????
            List<LfTemplateVo> commonTemList = commonTemplateBiz
                    .getCommonTempalateList(lfClone, pageInfo, key);

            //????????????????????????
            for (LfTemplateVo lf : commonTemList) {
                for (LfShortTemplateVo sv : lfShortTemList) {
                    if (sv.getTempId().equals(lf.getTmid())) {
                        lf.setIsShortTemp(1);//1???????????????
                    }
                }
            }
            //??????mmsList ??????????????????????????????????????????html,???????????????????????????
            TxtFileUtil txtfileutil = new TxtFileUtil();
            String dirUrl = null;
            dirUrl = txtfileutil.getWebRoot();
            for (LfTemplateVo lf : commonTemList) {
                String firstFramePath = dirUrl + lf.getTmMsg().replace("fuxin.rms", "firstframe.jsp");
                File fFrame = new File(firstFramePath);
                String tmid = lf.getTmid().toString();
                String filePath = dirUrl + lf.getTmMsg().replace("fuxin.rms", "");
                if (!fFrame.exists()) {//??????????????????????????????
                    long startTM = System.currentTimeMillis();
                    boolean downLoadRmsFromOss = new Mbgl_templateSvt().downLoadRmsFromOss(request, filePath, tmid, "");
                    long endTM = System.currentTimeMillis();
                    EmpExecutionContext.info("??????ID" + lf.getTmid() + ",rms ???????????????" + (endTM - startTM) + " ms");
                    if (!downLoadRmsFromOss) {
                        lf.setTmMsg("rms/mbgl/404.jsp");
                    }
                }
            }


            // ??????-????????????
            induUseMap = getIndustryUseList(request, response);
            industryList = induUseMap.get("industry");
            useList = induUseMap.get("use");

            request.setAttribute("commonTemList", commonTemList);
            request.setAttribute("industryList", industryList);
            request.setAttribute("useList", useList);
            request.setAttribute("pageInfo", pageInfo);
            // ??????????????????
            request.setAttribute("lfTemplate", lfTemplate);
            request.setAttribute("InduOrUseId", InduOrUseId);
            request.setAttribute("indudvHeight", indudvHeight);
            request.setAttribute("usedvHeight", usedvHeight);

            new ServerInof().setServerName(getServletContext().getServerInfo());
            request.getRequestDispatcher(PATH + "/common_template.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????????????????????????????");
        }

    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     */
    public void downPhoneFile(HttpServletRequest request, HttpServletResponse response) {
        try {

            Locale locale = MessageUtils.getLocale(request);
            //????????????ID
            String tmId = request.getParameter("tmId");
            //????????????
            String tempType = request.getParameter("tempType");
            //????????????
            LinkedHashMap<String, ArrayList<String>> phoneParamMap = null;
            //????????????
            String excleJson = "";
            //excel??????
            HSSFWorkbook wk = null;
            //????????????
            List<LfTempParam> paramList = commonTemplateBiz.getParamByTmId(tmId);
            //????????????????????????????????????excel??????
            //?????????&??????
            if ("12".equals(tempType) || "11".equals(tempType)) {
                if (!StringUtils.IsNullOrEmpty(tmId)) {
                    //??????????????????
                    String ottFrontJson = commonTemplateBiz.getContentbyTmId(tmId, "1", "12");
                    //?????????????????????
                    String rmsFrontJson = commonTemplateBiz.getContentbyTmId(tmId, "1", "11");
                    boolean b = true;
                    //??????????????????????????????????????????????????????????????????excel
                    if (StringUtils.isNotEmpty(rmsFrontJson) && ParamTool.checkParam(rmsFrontJson,locale)) {
                        phoneParamMap = ParamTool.convertParamRms(ottFrontJson,rmsFrontJson,locale);
                        excleJson = ParamTool.getFrameParamRms(ottFrontJson,rmsFrontJson,locale);
                        wk = ExcelTool.crateExcel(Long.parseLong(tmId), phoneParamMap, excleJson,paramList,locale);
                        b = false;
                    }
                    //??????????????????????????????????????????excel
                    //??????????????????????????????,???????????????????????????excel
                    if (b) {
                        phoneParamMap = ParamTool.convertParamOtt(ottFrontJson,locale);
                        excleJson = ParamTool.getFrameParamOtt(ottFrontJson,locale);
                        wk = ExcelTool.crateExcel(Long.parseLong(tmId), phoneParamMap, excleJson,paramList,locale);
                    }
                }
            }
            //?????????
            if ("13".equals(tempType)) {
                if (!StringUtils.IsNullOrEmpty(tmId)) {
                    String richfrontJson = commonTemplateBiz.getContentbyTmId(tmId, "1", "13");
                    phoneParamMap = ParamTool.convertParamRichText(richfrontJson,locale);
                    excleJson = ParamTool.getFrameParamRichText(richfrontJson,locale);
                    wk = ExcelTool.crateExcel(Long.parseLong(tmId), phoneParamMap, excleJson,paramList,locale);
                }
            }
            //?????????
            if("15".equals(tempType)){
                String richfrontJson = commonTemplateBiz.getContentbyTmId(tmId, "1", "15");
                phoneParamMap = ParamTool.convertParamRichText(richfrontJson,locale);
                excleJson = ParamTool.getFrameParamRichText(richfrontJson,locale);
                wk = ExcelTool.crateExcel(Long.parseLong(tmId), phoneParamMap, excleJson,paramList,locale);
            }
            SimpleDateFormat FMT = new SimpleDateFormat("yyyyMMddHHmmss");
            // ?????? ????????????EXCEL??????
            String excleFileName = FMT.format(new Date()) + "_phone.xls";
            // ????????????EXCEL ??????
            exportExcel(excleFileName, wk, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????????????????????????????");
        }
    }

    // ??????
    public void exportExcel(String excleFileName, HSSFWorkbook wk,
                            HttpServletResponse response) throws ServletException, IOException {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            response.reset();// ???????????????
            // ???????????????????????????
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(excleFileName.getBytes("GBK"), "ISO-8859-1"));
            // ???????????????????????????Excel
            response.setContentType("application/ynd.ms-excel;charset=UTF-8");
            // ???wk??????????????????OutputStream?????????????????????
            wk.write(out);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "???????????????????????????????????????");
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }

        }
    }
    /**
     * ??????-???????????? ??????
     *
     * @return
     */
    public java.util.Map<String, List<LfIndustryUse>> getIndustryUseList(
            HttpServletRequest request, HttpServletResponse response) {
        PageInfo pageInfo = new PageInfo();
        List<LfIndustryUse> industryUseList = new ArrayList<LfIndustryUse>();
        // ??????????????????
        List<LfIndustryUse> industryList = new ArrayList<LfIndustryUse>();
        // ??????????????????
        List<LfIndustryUse> useList = new ArrayList<LfIndustryUse>();
        LfIndustryUse lfIndustryUse = new LfIndustryUse();

        // ??????-????????????
        String InDuName = request.getParameter("InDuName");
        if (!StringUtils.IsNullOrEmpty(InDuName)) {
            lfIndustryUse.setName(InDuName);
        }

        // ????????????????????????
        java.util.Map<String, List<LfIndustryUse>> map = new HashMap<String, List<LfIndustryUse>>();

        industryUseList = commonTemplateBiz.getIndustryUseList(lfIndustryUse,
                pageInfo);
        // ??????type ????????????
        for (int i = 0; i < industryUseList.size(); i++) {
            LfIndustryUse lf = industryUseList.get(i);
            if (lf.getType().equals(0)) {
                industryList.add(lf);
            } else if (lf.getType().equals(1)) {
                useList.add(lf);
            }
        }
        map.put("industry", industryList);
        map.put("use", useList);
        return map;
    }
    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     */
    public void updateUseCount(HttpServletRequest request,
                               HttpServletResponse response) {
        LfTemplate lf = new LfTemplate();
        // ????????????ID
        String tmid = request.getParameter("tmid");
        if (!StringUtils.IsNullOrEmpty(tmid)) {
            lf.setTmid(Long.parseLong(tmid));
        }
        // ??????????????????
        String usecount = request.getParameter("usecount");
        if (!StringUtils.IsNullOrEmpty(tmid)) {
            lf.setUsecount(Long.parseLong(usecount));
        }
        PrintWriter pw = null;
        StringBuffer sbf = new StringBuffer();
        try {
            pw = response.getWriter();
            if (commonTemplateBiz.updateUseCount(lf)) {
                sbf.append("success");
            } else {
                sbf.append("fault");
            }
        } catch (IOException e) {
            EmpExecutionContext.error(e, "???????????????????????????????????????");
        } finally {
            if (null != pw) {
                pw.write(sbf.toString());
                pw.flush();
                pw.close();
            }
        }
    }
}
