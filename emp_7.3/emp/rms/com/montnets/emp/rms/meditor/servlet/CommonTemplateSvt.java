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
     * 查询
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
            // 当前登录企业编号
            String loginOrgcode = loginSysuser.getCorpCode();
            lfTemplate.setCorpCode(loginOrgcode);
            // 模板类型-11：富信模板
            lfTemplate.setTmpType(11);
            lfTemplate.setIsPublic(1);// 是否为公共模板 0 -不是，1-是
            pageInfo.setPageSize(9);// 100000账号设置每页9条数据
            if (!"100000".equals(loginOrgcode)) {
                lfTemplate.setAuditstatus(1);// 审核通过

                pageInfo.setPageSize(10);// 设置每页10条数据
            }

            // 模板名称/ID
            String key = "";
            if (!StringUtils.IsNullOrEmpty(request.getParameter("templName"))) {
                key = new String(request.getParameter("templName").getBytes("ISO-8859-1"), "UTF-8");
            }
            // 模板审核状态
            String templStatus = request.getParameter("templStatus");
            if (!StringUtils.IsNullOrEmpty(templStatus)
                    && !"-2".equals(templStatus)) {// -2为全部
                lfTemplate.setAuditstatus(Integer.parseInt(templStatus));
            }
            // 行业-用途类型
            String type = request.getParameter("type");
            // 行业ID
            String InduOrUseId = request.getParameter("InduOrUseId");

            if (!StringUtils.IsNullOrEmpty(type)
                    && !StringUtils.IsNullOrEmpty(InduOrUseId)) {
                if (type.equals("0")) {
                    // 行业ID
                    lfTemplate.setIndustryid(Integer.parseInt(InduOrUseId));
                } else if (type.equals("1")) {
                    // //用途
                    lfTemplate.setUseid(Integer.parseInt(InduOrUseId));
                }

            }

            //行业div 高度
            String indudvHeight = request.getParameter("indudv_height");
            //用途div 高度
            String usedvHeight = request.getParameter("usedv_height");
            // 是否第一次打开
            boolean isFirstEnter = false;

            isFirstEnter = pageSet(pageInfo, request);

            String templName = request.getParameter("templName");
            if (!StringUtils.IsNullOrEmpty(templName)) {
                lfTemplate.setTmName(new String(templName.getBytes("ISO-8859-1"), "UTF-8"));

            }
            LfShortTemplateVo lv = new LfShortTemplateVo();
            lv.setCorpCode(loginOrgcode);
            //快捷场景集合
            List<LfShortTemplateVo> lfShortTemList = new RmsShortTemplateBiz().getLfShortTemList(lv);

            LfTemplateVo lfClone = (LfTemplateVo) BeanUtils.cloneBean(lfTemplate);
            lfClone.setTmName(null);//用于发送页面的查询处理
            // 公共模板列表
            List<LfTemplateVo> commonTemList = commonTemplateBiz
                    .getCommonTempalateList(lfClone, pageInfo, key);

            //设置快捷场景标识
            for (LfTemplateVo lf : commonTemList) {
                for (LfShortTemplateVo sv : lfShortTemList) {
                    if (sv.getTempId().equals(lf.getTmid())) {
                        lf.setIsShortTemp(1);//1为快捷场景
                    }
                }
            }
            //遍历mmsList 中的每一个对象是否含有第一帧html,没有就从阿里云下载
            TxtFileUtil txtfileutil = new TxtFileUtil();
            String dirUrl = null;
            dirUrl = txtfileutil.getWebRoot();
            for (LfTemplateVo lf : commonTemList) {
                String firstFramePath = dirUrl + lf.getTmMsg().replace("fuxin.rms", "firstframe.jsp");
                File fFrame = new File(firstFramePath);
                String tmid = lf.getTmid().toString();
                String filePath = dirUrl + lf.getTmMsg().replace("fuxin.rms", "");
                if (!fFrame.exists()) {//不存在则从阿里云下载
                    long startTM = System.currentTimeMillis();
                    boolean downLoadRmsFromOss = new Mbgl_templateSvt().downLoadRmsFromOss(request, filePath, tmid, "");
                    long endTM = System.currentTimeMillis();
                    EmpExecutionContext.info("模板ID" + lf.getTmid() + ",rms 解析耗时：" + (endTM - startTM) + " ms");
                    if (!downLoadRmsFromOss) {
                        lf.setTmMsg("rms/mbgl/404.jsp");
                    }
                }
            }


            // 行业-用途列表
            induUseMap = getIndustryUseList(request, response);
            industryList = induUseMap.get("industry");
            useList = induUseMap.get("use");

            request.setAttribute("commonTemList", commonTemList);
            request.setAttribute("industryList", industryList);
            request.setAttribute("useList", useList);
            request.setAttribute("pageInfo", pageInfo);
            // 用于页面回显
            request.setAttribute("lfTemplate", lfTemplate);
            request.setAttribute("InduOrUseId", InduOrUseId);
            request.setAttribute("indudvHeight", indudvHeight);
            request.setAttribute("usedvHeight", usedvHeight);

            new ServerInof().setServerName(getServletContext().getServerInfo());
            request.getRequestDispatcher(PATH + "/common_template.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "公共模板查询出现异常");
        }

    }

    /**
     * 动态生成参数模板下载
     *
     * @param request
     * @param response
     */
    public void downPhoneFile(HttpServletRequest request, HttpServletResponse response) {
        try {

            Locale locale = MessageUtils.getLocale(request);
            //模板自增ID
            String tmId = request.getParameter("tmId");
            //模板类型
            String tempType = request.getParameter("tempType");
            //参数集合
            LinkedHashMap<String, ArrayList<String>> phoneParamMap = null;
            //参数信息
            String excleJson = "";
            //excel对象
            HSSFWorkbook wk = null;
            //参数集合
            List<LfTempParam> paramList = commonTemplateBiz.getParamByTmId(tmId);
            //模板内容解析模板内容生成excel对象
            //富媒体&卡片
            if ("12".equals(tempType) || "11".equals(tempType)) {
                if (!StringUtils.IsNullOrEmpty(tmId)) {
                    //卡片模板内容
                    String ottFrontJson = commonTemplateBiz.getContentbyTmId(tmId, "1", "12");
                    //富媒体模板内容
                    String rmsFrontJson = commonTemplateBiz.getContentbyTmId(tmId, "1", "11");
                    boolean b = true;
                    //当卡片的补充方式富媒体存在时以富媒体为主生成excel
                    if (StringUtils.isNotEmpty(rmsFrontJson) && ParamTool.checkParam(rmsFrontJson,locale)) {
                        phoneParamMap = ParamTool.convertParamRms(ottFrontJson,rmsFrontJson,locale);
                        excleJson = ParamTool.getFrameParamRms(ottFrontJson,rmsFrontJson,locale);
                        wk = ExcelTool.crateExcel(Long.parseLong(tmId), phoneParamMap, excleJson,paramList,locale);
                        b = false;
                    }
                    //当只存在卡片时以卡片内容生成excel
                    //如果富媒体不存在参数,继续以卡片内容生成excel
                    if (b) {
                        phoneParamMap = ParamTool.convertParamOtt(ottFrontJson,locale);
                        excleJson = ParamTool.getFrameParamOtt(ottFrontJson,locale);
                        wk = ExcelTool.crateExcel(Long.parseLong(tmId), phoneParamMap, excleJson,paramList,locale);
                    }
                }
            }
            //富文本
            if ("13".equals(tempType)) {
                if (!StringUtils.IsNullOrEmpty(tmId)) {
                    String richfrontJson = commonTemplateBiz.getContentbyTmId(tmId, "1", "13");
                    phoneParamMap = ParamTool.convertParamRichText(richfrontJson,locale);
                    excleJson = ParamTool.getFrameParamRichText(richfrontJson,locale);
                    wk = ExcelTool.crateExcel(Long.parseLong(tmId), phoneParamMap, excleJson,paramList,locale);
                }
            }
            //企业秀
            if("15".equals(tempType)){
                String richfrontJson = commonTemplateBiz.getContentbyTmId(tmId, "1", "15");
                phoneParamMap = ParamTool.convertParamRichText(richfrontJson,locale);
                excleJson = ParamTool.getFrameParamRichText(richfrontJson,locale);
                wk = ExcelTool.crateExcel(Long.parseLong(tmId), phoneParamMap, excleJson,paramList,locale);
            }
            SimpleDateFormat FMT = new SimpleDateFormat("yyyyMMddHHmmss");
            // 组装 号码文件EXCEL表头
            String excleFileName = FMT.format(new Date()) + "_phone.xls";
            // 生成号码EXCEL 文件
            exportExcel(excleFileName, wk, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "下载号码文件出异常！");
        }
    }

    // 下载
    public void exportExcel(String excleFileName, HSSFWorkbook wk,
                            HttpServletResponse response) throws ServletException, IOException {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            response.reset();// 清空输出流
            // 解决中文文件名乱码
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String(excleFileName.getBytes("GBK"), "ISO-8859-1"));
            // 定义输出文件类型为Excel
            response.setContentType("application/ynd.ms-excel;charset=UTF-8");
            // 将wk表个对象写入OutputStream流中并进行输出
            wk.write(out);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "下载手机号码文件出现异常！");
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }

        }
    }
    /**
     * 行业-用途列表 查询
     *
     * @return
     */
    public java.util.Map<String, List<LfIndustryUse>> getIndustryUseList(
            HttpServletRequest request, HttpServletResponse response) {
        PageInfo pageInfo = new PageInfo();
        List<LfIndustryUse> industryUseList = new ArrayList<LfIndustryUse>();
        // 所有行业集合
        List<LfIndustryUse> industryList = new ArrayList<LfIndustryUse>();
        // 所有用途集合
        List<LfIndustryUse> useList = new ArrayList<LfIndustryUse>();
        LfIndustryUse lfIndustryUse = new LfIndustryUse();

        // 行业-用途名称
        String InDuName = request.getParameter("InDuName");
        if (!StringUtils.IsNullOrEmpty(InDuName)) {
            lfIndustryUse.setName(InDuName);
        }

        // 最终的返回结果集
        java.util.Map<String, List<LfIndustryUse>> map = new HashMap<String, List<LfIndustryUse>>();

        industryUseList = commonTemplateBiz.getIndustryUseList(lfIndustryUse,
                pageInfo);
        // 根据type 进行分组
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
     * 记录模板使用次数
     *
     * @param request
     * @param response
     */
    public void updateUseCount(HttpServletRequest request,
                               HttpServletResponse response) {
        LfTemplate lf = new LfTemplate();
        // 模板自增ID
        String tmid = request.getParameter("tmid");
        if (!StringUtils.IsNullOrEmpty(tmid)) {
            lf.setTmid(Long.parseLong(tmid));
        }
        // 模板使用次数
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
            EmpExecutionContext.error(e, "更新模板使用次数出现异常！");
        } finally {
            if (null != pw) {
                pw.write(sbf.toString());
                pw.flush();
                pw.close();
            }
        }
    }
}
