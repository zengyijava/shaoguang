package com.montnets.emp.rms.meditor.servlet;

import com.google.gson.Gson;
import com.montnets.emp.common.atom.SendSmsAtom;
import com.montnets.emp.common.biz.BadWordsFilterBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.RmsBalanceLogBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.rms.dao.UserDataDAO;
import com.montnets.emp.rms.entity.LfDfadvanced;
import com.montnets.emp.rms.meditor.biz.imp.CommonTemplateBiz;
import com.montnets.emp.rms.meditor.biz.imp.OTTTaskBiz;
import com.montnets.emp.rms.meditor.biz.imp.UserBizImp;
import com.montnets.emp.rms.servmodule.constant.ServerInof;
import com.montnets.emp.rms.templmanage.servlet.Mbgl_templateSvt;
import com.montnets.emp.rms.tools.RmsTxtFileUtil;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.rms.vo.UserDataVO;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.StringUtils;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:yangdl
 * @Data:Created in 15:57 2018.8.7 007
 */
public class SameMmsSvt extends BaseServlet {
    /**
     * 富信允许发送号码最大值
     */
    private static final Long RMS_MAX_PHONE_NUM = 5000000L;
    private final WgMsgConfigBiz msgConfigBiz = new WgMsgConfigBiz();
    private final PhoneUtil phoneUtil = new PhoneUtil();
    private final BaseBiz baseBiz = new BaseBiz();
    private final UserBizImp userBizImp = new UserBizImp();
    private final KeyWordAtom keyWordAtom = new KeyWordAtom();
    private final BadWordsFilterBiz badFilter = new BadWordsFilterBiz();
    private final String empRoot = "rms";
    private final String basePath = "/samemms";
    /**
     * 代表相同内容发送
     */
    private static final String SENDTYPE = "0";
    /**
     * 代表企业富信相同内容发送
     */
    private static final String SAMESEND = "13";
    private final String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
    private final RmsBalanceLogBiz rmsBalanceLogBiz = new RmsBalanceLogBiz();
    private final SendSmsAtom smsAtom = new SendSmsAtom();
    private final BlackListAtom blackBiz = new BlackListAtom();
    private final CommonVariables cv = new CommonVariables();
    private final RmsTxtFileUtil fileUtil = new RmsTxtFileUtil();
    private final OTTTaskBiz ottTaskBiz = new OTTTaskBiz();
    private final Mbgl_templateSvt templateSvt = new Mbgl_templateSvt();
    private final UserDataDAO userDataDAO = new UserDataDAO();
    private final CommonBiz commonBiz = new CommonBiz();
    private final CommonTemplateBiz commonTemplateBiz = new CommonTemplateBiz();
    private final SmsBiz smsBiz = new SmsBiz();

    /**
     * 查询方法
     *
     * @param request  request对象
     * @param response response对象
     */
    public void find(HttpServletRequest request, HttpServletResponse response) {
        List<String> spUsers = new ArrayList<String>();
        LfDfadvanced lfDfadvanced = null;
        try {
            EmpExecutionContext.logRequestUrl(request, "<企业富信相同内容发送页面跳转>");
            String lguserid = "";
            String corpCode = "";
            LfSysuser sysUser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //获取当前操作员的id
            lguserid = String.valueOf(sysUser.getUserId());
            //获取企业编码
            corpCode = sysUser.getCorpCode();
            if(StaticValue.getCORPTYPE() == 1){
                //托管版
                List<UserDataVO> spUserList = userDataDAO.getSPUserList(corpCode);
                for(UserDataVO vo : spUserList){
                    spUsers.add(vo.getUserId());
                }
            }else {
                //标准版
                List<Userdata> spUserList1 = smsBiz.getSpUserList(sysUser,true);
                for(Userdata vo : spUserList1){
                    spUsers.add(vo.getUserId());
                }
            }

            //获取任务ID
            Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);

            //获取高级设置默认信息
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.clear();
            conditionMap.put("userid", lguserid);
            conditionMap.put("flag", SAMESEND);
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("id", StaticValue.DESC);
            List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, conditionMap, orderMap);

            if (lfDfadvancedList != null && lfDfadvancedList.size() > 0) {
                lfDfadvanced = lfDfadvancedList.get(0);
            }
            //根据企业编码查询业务类型
            conditionMap.clear();
            orderMap.clear();
            conditionMap.put("corpCode&in", "0," + corpCode);
            orderMap.put("corpCode", "asc");
            conditionMap.put("state", "0");
            List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, orderMap);

            request.setAttribute("busList", busList);
            request.setAttribute("lfDfadvanced", lfDfadvanced);
            request.setAttribute("spUserList", spUsers);
            request.setAttribute("taskId", taskId);

            //获取立即发送传过来的模板id
            String tmIdSend = request.getParameter("id");
            if (StringUtils.isNotEmpty(tmIdSend)) {
                request.setAttribute("tmIdSend", tmIdSend);
            }
            //设置服务器名称
            new ServerInof().setServerName(getServletContext().getServerInfo());

            request.getRequestDispatcher(empRoot + basePath + "/rms_sameMms.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信相同内容发送页面跳转失败！");
        }
    }

    /**
     * 高级设置选项存为默认
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常
     */
    public void setDefault(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //返回信息
        String result = "fail";
        try {
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);
            String lgcorpcode = SysuserUtil.getCorpcode(request);
            String spUser = request.getParameter("spUser");
            String busCode = request.getParameter("busCode");
            String checkRepeat = request.getParameter("checkrepeat");
            String flag = request.getParameter("flag");
            if (flag == null || "".equals(flag)) {
                EmpExecutionContext.error("企业富信相同内容发送高级设置存为默认参数异常！flag:" + flag);
                response.getWriter().print(result);
                return;
            }
            if (lguserid == null || "".equals(lguserid)) {
                EmpExecutionContext.error("企业富信相同内容发送高级设置存为默认参数异常！lguserid：" + lguserid);
                response.getWriter().print(result);
                return;
            }
            if (spUser == null || "".equals(spUser)) {
                EmpExecutionContext.error("企业富信相同内容发送高级设置存为默认参数异常！spUser：" + spUser);
                response.getWriter().print(result);
                return;
            }
            if (busCode == null || "".equals(busCode)) {
                EmpExecutionContext.error("企业富信相同内容发送高级设置存为默认参数异常！busCode：" + busCode);
                response.getWriter().print(result);
                return;
            }
            //原记录删除条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userid", lguserid);
            conditionMap.put("flag", flag);

            //更新对象
            LfDfadvanced lfDfadvanced = new LfDfadvanced();
            lfDfadvanced.setUserid(Long.parseLong(lguserid));
            lfDfadvanced.setSpuserid(spUser);
            lfDfadvanced.setFlag(Integer.parseInt(flag));
            lfDfadvanced.setBuscode(busCode);
            lfDfadvanced.setRepeatfilter(Integer.parseInt(org.apache.commons.lang.StringUtils.defaultIfEmpty(checkRepeat, "0")));
            lfDfadvanced.setCreatetime(new Timestamp(System.currentTimeMillis()));

            result = userBizImp.setDefault(conditionMap, lfDfadvanced);

            //操作结果
            String opResult = "企业富信相同内容发送高级设置存为默认失败。";
            if ("success".equals(result)) {
                opResult = "企业富信相同内容发送高级设置存为默认成功。";
            }
            //操作员信息
            LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
            //操作员姓名
            String opUser = sysuser == null ? "" : sysuser.getUserName();

            //操作日志
            EmpExecutionContext.info("企业富信相同内容发送", lgcorpcode, lguserid, opUser, opResult + "[SP账号](" + spUser + ")", "OTHER");

            response.getWriter().print(result);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信相同内容发送高级设置存为默认异常！");
            response.getWriter().print(result);
        }
    }

    /**
     * 预览前，对发送主题关键字检测   定时时间 内容时效性校验
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常
     */
    public void checkBeforeSend(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //发送主题
            String title = request.getParameter("title");
            //富信模板ID
            String tempId = request.getParameter("tempId");
            //发送类型   0即时 还是   1定时
            String timerStatus = request.getParameter("timerStatus");
            //定时时间
            String timerTime = request.getParameter("timerTime");
            //内容时效
            String validHourNum = request.getParameter("validHourNum");
            //返回值
            StringBuffer returnMsg = new StringBuffer();
            //企业编码
            String corpCode = request.getParameter("corpCode");
            //处理发送主题关键字
            if (title != null && !"".equals(title)) {
                String message = title.toUpperCase();
                List<String> kwsList = keyWordAtom.getKwInUsed(corpCode);
                if (kwsList != null && kwsList.size() > 0 && !badFilter.checkTextString(message, kwsList)) {
                    String c = badFilter.checkText(message);
                    returnMsg.append("stage1&").append(c);
                    response.getWriter().print(returnMsg);
                    return;
                }
            }
            //处理富信模板文件
            LinkedHashMap<String, String> contionMap = new LinkedHashMap<String, String>();
            contionMap.put("sptemplid", tempId);
            List<LfTemplate> templates = baseBiz.getByCondition(LfTemplate.class, contionMap, null);
            if (templates == null || templates.size() == 0) {
                returnMsg.append("stage2&").append("富信模板文件不存在，请重新选择");
                response.getWriter().print(returnMsg);
                return;
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //定时发送 获取服务器时间返回
            if ("1".equals(timerStatus)) {
                Date serverTime = new Date();
                Date sendTime = format.parse(timerTime);
                //发送时间应该大于服务器时间
                if (!serverTime.before(sendTime)) {
                    returnMsg.append("stage3&").append("预发送时间小于服务器当前时间！请合理预定发送时间！");
                    response.getWriter().print(returnMsg);
                    return;
                }
            }
            //内容时效
            if (!"".equals(validHourNum) && validHourNum != null) {
                if (!validHourNum.matches("^\\d+$")) {
                    returnMsg.append("stage4&").append("内容时效输入错误");
                    response.getWriter().print(returnMsg);
                    return;
                } else {
                    if (Integer.valueOf(validHourNum) > 72) {
                        returnMsg.append("stage4&").append("内容时效最大只允许72小时");
                        response.getWriter().print(returnMsg);
                        return;
                    }
                }
            }
            returnMsg = new StringBuffer("success");
            response.getWriter().print(returnMsg);
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "企业富信不同内容发送提交前验证出现异常！");
        }
    }

    /**
     * 相同内容页面发送进行预览操作
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常
     */
    public void preview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //-----1.声明变量-----
        //记录耗时
        Long startTime = System.currentTimeMillis();
        //返回值
        String result = "";
        //操作员ID
        Long lguserid = -1L;
        //业务类型
        String busCode = "";
        //富信发送账号
        String spuser = "";
        //当前登录企业
        String lgcorpcode = "";
        //员工机构IDS
        String empDepIds = "";
        //客户机构IDS
        String cliDepIds = "";
        //群组IDS
        String groupIds = "";
        //选择对象中的号码串
        String userMoblieStr = "";
        //手工输入的号码
        String phoneStr = "";
        //批量输入的号码
        String inputphone = "";
        //模板文件路径
        String templateUrl;
        //所有上传文件流集合
        List<BufferedReader> readerList = new ArrayList<BufferedReader>();
        //所有上传文件的名字字符串
        String allFileNames;
        //模板内容
        String tmsMsg = null;
        // 预览参数传递变量类
        LfSysuser lfSysuser = null;
        //模板类型
        String tempType = "";
        //模板id
        String tempId = "";
        PreviewParams preParams = new PreviewParams();
        try {
            String tmId = request.getParameter("tmId");

            //任务ID
            String taskId = request.getParameter("taskId");
            //-----2.部分校验-----
            Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
            //从session中获取操作员对象
            lfSysuser = ottTaskBiz.getCurrSysUserFromSession(request);
            lguserid = lfSysuser.getUserId();

            //号段
            String[] haoduan = msgConfigBiz.getHaoduan();
            if (haoduan == null) {
                EmpExecutionContext.error("企业富信相同内容发送预览，获取运营商号码段异常。userId：" + lguserid + "，errCode:" + IErrorCode.V10002);
                throw new EMPException(IErrorCode.V10002);
            }

            //设置文件名参数
            String[] fileNameparam = {taskId};

            //获取富信号码文件路径
            String[] url = fileUtil.getSaveRmsMobileFileUrl(lguserid, fileNameparam);
//            if (url == null || url.length < 5) {
            if (url == null) {
                EmpExecutionContext.error("企业富信相同内容发送预览，获取发送文件路径失败。userId：" + lguserid + "，errCode:" + IErrorCode.V10013);
                throw new EMPException(IErrorCode.V10013);
            }
            //判断文件是否存在，存在则返回
            if (new File(url[0]).exists()) {
                EmpExecutionContext.error("企业富信相同内容发送预览，获取号码文件路径失败，文件路径已存在，文件路径：" + url[0] + "，userid:" + lguserid + "，errCode：" + IErrorCode.V10013);
                throw new EMPException(IErrorCode.V10013);
            }

            //-----2.从request对象中获取表单信息集合（表单信息+文件信息）-----

            //存储文件相关信息的Map
            Map<String, String> fileMap = new HashMap<String, String>();

            //表单元素集合
            List<FileItem> fileList = ottTaskBiz.getFileList(request, url, lguserid, SENDTYPE);

            //从表单元素集合中解析表单信息封装到一个Map
            Map<String, String> fieldInfo = ottTaskBiz.parseRequestForm2Map(fileList);

            //所有的上传文件名字
            allFileNames = fieldInfo.get("allFileNames");

            //从表单元素集合中解析上传文件并转换为BufferedReader集合
            readerList = ottTaskBiz.parseRequestFileForm2ReaderList(fileList, preParams, url, lguserid, SENDTYPE, fileMap, allFileNames, null);

            //手动输入的电话号码集合
            phoneStr = fieldInfo.get("phoneStr");
            //员工机构ids
            empDepIds = fieldInfo.get("empDepIds");
            //客户机构ids
            cliDepIds = fieldInfo.get("cliDepIds");
            //群组IDS
            groupIds = fieldInfo.get("groupIds");
            //选择对象中，选择用户时的号码串
            userMoblieStr = fieldInfo.get("userMoblieStr");
            //企业编码
            lgcorpcode = fieldInfo.get("lgcorpcode");
            //sp账户
            spuser = fieldInfo.get("mmsUser");
            //业务类型编码
            busCode = fieldInfo.get("busCode");
            //批量输入
            inputphone = fieldInfo.get("inputphone");
            //获取模板路径
            templateUrl = fieldInfo.get("tempUrl");
            //模板类型
            tempType = fieldInfo.get("templateType");
            //模板Id
            tempId = fieldInfo.get("tempId");
            //操作员、企业编码、SP账号检查
            boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, lgcorpcode, spuser, null);
            if (!checkFlag) {
                EmpExecutionContext.error("企业富信相同内容发送预览，检查操作员、企业编码、发送账号不通过，taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，userid：" + lguserid
                        + "，spUser：" + spuser
                        + "，errCode:" + IErrorCode.B20007);
                throw new EMPException(IErrorCode.B20007);
            }
            // 是否有预览号码权限.
            if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                //号码是否可见，0：不可见，1：可见
                preParams.setIshidephone(1);
            }
            preParams.setPhoneFilePath(url);

            //根据模板Id检验能否从数据库获取相应的记录
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            map.put("sptemplid", tempId);
            LfTemplate lfTemplate = null;
            List<LfTemplate> lfTemplateList = baseBiz.getByCondition(LfTemplate.class, map, null);

            if (lfTemplateList == null || lfTemplateList.size() == 0) {
                EmpExecutionContext.error("企业富信相同内容发送预览，根据模板Id获取LfTemplate对象异常，userid:" + lguserid + "，errCode：" + IErrorCode.V10001);
                throw new EMPException(IErrorCode.V10001);
            }

            lfTemplate = lfTemplateList.get(0);

            //根据富信模板文件URL获取模板文本内容
            tmsMsg = commonTemplateBiz.getTextContentbyTempid(tempId, tempType);

            String content = commonTemplateBiz.getContentbyTempid(tempId, "1", tempType);
            if (content == null || content.trim().length() <= 0) {
                EmpExecutionContext.error("企业富信相同内容发送预览，获取模板内容异常，获取模板内容为空。taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，userid：" + lguserid
                        + "，errCode:" + IErrorCode.V10001);
                EmpExecutionContext.logRequestUrl(request, "后台请求");
                result = "emptyTemplate";
                return;
            }

            //-----3.关键字检查-----
            if(org.apache.commons.lang.StringUtils.isNotEmpty(tmsMsg)){
                String words = keyWordAtom.checkText(tmsMsg, lgcorpcode);
                //包含关键字
                if (!"".equals(words)) {
                    EmpExecutionContext.error("企业富信相同内容发送预览，关键字检查未通过，发送内容包含违禁词组:'" + words + "'，taskid:" + taskId
                            + "，corpCode:" + lgcorpcode
                            + "，userid：" + lguserid
                            + "，errCode:" + IErrorCode.V10016);
                    result = "keywords&" + words;
                    return;
                }
            }

            //-----4.解析文本文件流，验证号码合法性、过滤黑名单、过滤重号、生成有效号码文件和无效号码文件-----
            smsAtom.parsePhone(readerList, preParams, haoduan, lguserid.toString(), lgcorpcode, busCode, request);

            //这里将选择对象中的用户的手机号码与主界面所输入的手机号码连起来
            if (!"".equals(userMoblieStr) && userMoblieStr.length() > 0) {
                phoneStr += userMoblieStr;
            }
            //这里将批量输入手机号与上一步手机号拼接
            if (!"".equals(inputphone) && inputphone.length() > 0) {
                phoneStr += inputphone;
            }

            //-----5.处理单个号码以及通讯录-----
            if ((cliDepIds != null && empDepIds != null && groupIds != null && phoneStr != null) &&
                    (!"".equals(cliDepIds) || !"".equals(empDepIds) || !"".equals(groupIds) || !"".equals(phoneStr))) {
                parseAddrAndInputPhone(preParams, empDepIds, cliDepIds, phoneStr, groupIds, lgcorpcode, haoduan, busCode, request);
            }

            //没有提交号码，返回页面提示
            if (preParams.getSubCount() == 0) {
                result = "noPhone";
                EmpExecutionContext.error("企业富信相同内容发送预览，没有提交号码，无法进行发送。taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + lguserid);
                return;
            }

            //判断有效号码数是否超出范围
            if (preParams.getEffCount() > RMS_MAX_PHONE_NUM) {
                //删除前面生成的有效号码和无效号码文件
                for (String anUrl : url) {
                    fileUtil.deleteFile(anUrl);
                }
                //文件内有效号码大于500万
                result = "overstep";
                EmpExecutionContext.error("企业富信相同内容发送预览，有效号码数超出过最大限制:" + RMS_MAX_PHONE_NUM + "，无法进行发送，taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + lguserid);
                return;
            }

            //处理预览号码内容，有值则去掉最后一个分号
            String previewPhone = preParams.getPreviewPhone();
            if (previewPhone.lastIndexOf(";") > 0) {
                previewPhone = previewPhone.substring(0, previewPhone.lastIndexOf(";"));
                preParams.setPreviewPhone(previewPhone);
            }

            //预发送条数就是有效号码数
            int preSendCount = preParams.getEffCount();

            //富信发送不需要机构计费
            //SP账号类型,1:预付费;2:后付费
            Long feeFlag = -3L;
            //获取SP账号类型
            //accountType 1为短信 2为彩信
            feeFlag = rmsBalanceLogBiz.getSpUserFeeFlag(spuser, 1);
            if (feeFlag == null || feeFlag < 0) {
                EmpExecutionContext.error("企业富信相同内容发送预览，获取SP账号计费类型异常。errCode：" + IErrorCode.B20044
                        + "，taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，userid:" + lguserid
                        + "，spUser:" + spuser
                        + "，feeFlag:" + feeFlag);
                throw new EMPException(IErrorCode.B20044);
            }

            String fxBalance = "koufeiSuccess";
            String spFeeBalance = "koufeiSuccess";
            //机构和SP账号余额检查都通过
            //从MBOSS获取富信余额，本地存一分(LF_SPFEE)
            //msType 1为短信 2为彩信
            fxBalance = rmsBalanceLogBiz.checkGwFeeRMS(spuser, preSendCount, lgcorpcode, false, 1);
            //检查SP账号余额(USERFEE)
            spFeeBalance = rmsBalanceLogBiz.checkSpBalanceRMS(spuser, (long) preSendCount, lgcorpcode, false);
            Map<String, Object> objMap = new HashMap<String, Object>();

            //富信余额(运营商)
            objMap.put("fxBalance", fxBalance);
            //SP账号余额
            objMap.put("spFeeBalance", spFeeBalance);
            //SP账号类型,1:预付费;2:后付费
            objMap.put("feeFlag", feeFlag);
            //发送标记
            objMap.put("sendType", "0");
            //模板档位
            objMap.put("tempDegree", lfTemplate.getDegree());
            //预览信息 json
            result = preParams.getJsonStr(objMap);

            //操作日志信息
            //格式化时间
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            StringBuilder opContent = new StringBuilder().append("预览S：").append(sdf.format(startTime)).append("，耗时：")
                    .append((System.currentTimeMillis() - startTime)).append("ms，文件数:").append(fileMap.get("fileCount")).append("，总大小:")
                    .append(Long.parseLong(fileMap.get("allFileSize")) / 1024).append("KB");
            //记录上传文件名
            if (Integer.parseInt(fileMap.get("fileCount")) > 0) {
                StringBuilder loadFileName = new StringBuilder(fileMap.get("loadFileName"));
                opContent.append("，文件名：").append(loadFileName.deleteCharAt(loadFileName.length() - 1).append("'"));
            }
            //选择员工机构
            if (empDepIds != null && empDepIds.length() > 1) {
                opContent.append("，empDepIds：").append(empDepIds.substring(1, empDepIds.length() - 1));
            }
            //选择客户机构
            if (cliDepIds != null && cliDepIds.length() > 1) {
                opContent.append("，cliDepIds：").append(cliDepIds.substring(1, cliDepIds.length() - 1));
            }
            //选择群组
            if (groupIds != null && groupIds.length() > 1) {
                opContent.append("，groupIds：").append(groupIds.substring(1, groupIds.length() - 1));
            }

            opContent.append("，提交总数：").append(preParams.getSubCount())
                    .append("，有效数：").append(preParams.getEffCount())
                    .append("，短信条数：").append(preSendCount)
                    .append("，taskId：").append(taskId)
                    .append("，spUser:").append(spuser);
            EmpExecutionContext.info("企业富信相同内容发送预览", lgcorpcode, lguserid.toString(), lfSysuser.getUserName(), opContent.toString(), "OTHER");
        } catch (EMPException empex) {
            ErrorCodeInfo info = ErrorCodeInfo.getInstance();
            //获取自定义异常编码
            String message = empex.getMessage();
            //获取自定义异常提示信息
            result = info.getErrorInfo(message);
            EmpExecutionContext.error(empex, lguserid, lgcorpcode);
            EmpExecutionContext.logRequestUrl(request, "企业富信相同内容发送预览出现异常！ 后台请求");
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "企业富信相同内容发送预览出现异常！");
        } finally {
            readerList.clear();
            readerList = null;
            preParams.getValidPhone().clear();
            preParams.setValidPhone(null);
            preParams = null;
            request.setAttribute("result", result);
            request.getRequestDispatcher(empRoot + basePath + "/rms_sameMmsPre.jsp").forward(request, response);
        }
    }

    /**
     * 解析通讯录 + 手动输入号码
     *
     * @param params     预览参数对象
     * @param empDepIds  员工机构ID
     * @param cliDepIds  客户机构ID
     * @param phoneStr   需要注意:这里传的是选择对象的号码与手动输入号码
     * @param groupIds   群组ID
     * @param lgcorpcode 企业编码
     * @param haoduan    号段
     * @param busCode    业务类型编码
     * @param request    为多语言做准备
     * @throws Exception 异常对象
     */
    private void parseAddrAndInputPhone(PreviewParams params, String empDepIds, String cliDepIds, String phoneStr, String groupIds, String lgcorpcode, String[] haoduan, String busCode, HttpServletRequest request) throws Exception {
        try {
            // 运营商有效号码数
            int[] oprValidPhone = params.getOprValidPhone();
            //// 运营商标识。0:移动号码 ;1:联通号码;2:电信号码;3:国际号码
            int index;
            //号码返回状态
            int resultStatus;
            //有效号码
            StringBuilder contentSb = new StringBuilder();
            //无效号码
            StringBuilder badContentSb = new StringBuilder();

            if (!"".equals(phoneStr)) {
                //最后一位不为","时,增加","
                if (!",".equals(phoneStr.substring(phoneStr.length() - 1))) {
                    phoneStr += ",";
                }
            }
            //处理员工机构
            if (empDepIds != null && empDepIds.length() > 0 && !",".equals(empDepIds)) {
                phoneStr = userBizImp.getEmployeePhoneSrrByDepId(phoneStr, empDepIds, lgcorpcode);
            }
            //处理客户机构
            if (!"".equals(cliDepIds) && cliDepIds.length() > 0) {
                phoneStr = userBizImp.getClientPhoneStrByDepId(phoneStr, cliDepIds, lgcorpcode);
            }
            //处理群组机构
            if (!"".equals(groupIds) && groupIds.length() > 0) {
                phoneStr = userBizImp.getGroupPhoneStrById(phoneStr, groupIds);
            }
            String[] phones = phoneStr.split(",");
            StringBuilder phoneString = new StringBuilder(params.getPreviewPhone());
            for (String num : phones) {
                params.setSubCount(params.getSubCount() + 1);
                if (num != null) {
                    if (num.length() >= 7 && num.length() <= 21) {
                        // 去掉号码中+86前缀
                        num = num.trim();
                        num = StringUtils.parseMobile(num);
                        if ((index = phoneUtil.getPhoneType(num, haoduan)) < 0) {
                            // 过滤号段
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            continue;
                        } else if ((resultStatus = phoneUtil.checkRepeat(num, params.getValidPhone())) != 0) {
                            //1为重复号码
                            if (resultStatus == 1) {
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code3", request)).append(num).append(line);
                                params.setRepeatCount(params.getRepeatCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                continue;
                            } else {
                                //-1为非法号码，如包含字母等
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num).append(line);
                                params.setBadCount(params.getBadCount() + 1);
                                params.setBadModeCount(params.getBadModeCount() + 1);
                                continue;
                            }
                        } else if (blackBiz.checkBlackList(lgcorpcode, num, busCode)) {
                            //黑名单校验
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(num).append(line);
                            params.setBlackCount(params.getBlackCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            continue;
                        }

                        contentSb.append(num).append(line);
                        params.setEffCount(params.getEffCount() + 1);

                        // 累加运营商有效号码数(区分运营商)
                        oprValidPhone[index] += 1;
                        //如果没有预览号码权限，则将前面10个号码替换为136****1234
                        if (params.getEffCount() < 11) {
                            if (!"".equals(num) && params.getIshidephone() == 0) {
                                num = cv.replacePhoneNumber(num);
                            }
                            phoneString.append(num).append(";");
                        }
                    } else {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                    }
                }
            }
            //预览号码
            params.setPreviewPhone(phoneString.toString());
            // 设置各运营商有效号码数
            params.setOprValidPhone(oprValidPhone);
            if (contentSb.length() > 0) {
                FileOutputStream fos = new FileOutputStream(params.getPhoneFilePath()[0], true);
                fileUtil.repeatWriteToTxtFile(fos, contentSb.toString());
                contentSb.setLength(0);
            }

            if (badContentSb.length() > 0) {
                FileOutputStream fos = new FileOutputStream(params.getPhoneFilePath()[2], true);
                fileUtil.repeatWriteToTxtFile(fos, badContentSb.toString());
                badContentSb.setLength(0);
            }
        } catch (Exception var25) {
            EmpExecutionContext.error(var25, "相同内容预览，解析手工输入和通讯录的号码异常。");
            throw new EMPException("B20005", var25);
        } finally {
            if (phoneStr != null) {
                phoneStr = null;
            }
        }
    }

    /**
     * 查询富信模板
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常对象
     */
    public void getLfTemplateByMms(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EmpExecutionContext.logRequestUrl(request, "<企业富信发送查询富信模板>");
        //查询条件vo
        LfTemplateVo lfTemplateVo = new LfTemplateVo();
        List<LfTemplateVo> templateList = null;
        String tempName;
        String tempId;
        String tempType;
        try {
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            //是否选择公共模板
            Boolean choosePublic = request.getParameter("isPublic") != null && "true".equals(request.getParameter("isPublic"));
            //模板名称
            tempName = request.getParameter("tempName");
            //模板Id
            tempId = request.getParameter("tempId");
            //0是相同内容发送  1是不同内容发送
            tempType = request.getParameter("tempType");

           // String userIdStr = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String userIdStr = SysuserUtil.strLguserid(request);


            String pageSource = request.getParameter("pageSource");
            //公共场景还是我的场景引用点击进来的，source=commontemplate 为公共场景
            String source = request.getParameter("source");
            if (userIdStr == null || "".equals(userIdStr.trim()) || "undefined".equals(userIdStr.trim())) {
                EmpExecutionContext.info("静态彩信发送查询彩信模板获取登录操作员ID异常！lguserid=" + userIdStr + "。改成从Session获取。");
                LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
                userIdStr = String.valueOf(loginSysuser.getUserId());
            }
            //当前登录用户id
            Long lguserid = Long.valueOf(userIdStr);

            if (tempName != null && !"".equals(tempName)) {
                tempName = tempName.trim();
                lfTemplateVo.setTmName(tempName);
            }
            //0是相同内容发送  1是不同内容发送
            if (tempType != null && !"".equals(tempType)) {
                lfTemplateVo.setDsflag(Long.parseLong(tempType));
            }
            //查询启用的模板
            lfTemplateVo.setTmState(1L);
            //模板种类（3-短信模板;4-彩信模板;11-富信模板）
            lfTemplateVo.setTmpType(11);
            //网关审批状态 1为已通过
            lfTemplateVo.setAuditstatus(1);
            //选择公共模板则增加查询条件
            if (choosePublic) {
                lfTemplateVo.setIsPublic(1);
            } else {
                lfTemplateVo.setIsPublic(0);
            }
            if (tempId != null && !"".equals(tempId)) {
                lfTemplateVo.setSptemplid(Long.parseLong(tempId));
            }
            //分页
            pageInfo.setPageSize(8);
            //条件查询结果集
            Mbgl_templateSvt svt = new Mbgl_templateSvt();
            templateList = svt.findTemplateBycontition(lguserid, lfTemplateVo, pageInfo);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("temList", templateList);
            request.setAttribute("tempType", tempType);
            request.setAttribute("tempName", tempName);
            request.setAttribute("tempId", tempId);
            request.setAttribute("pageSource", pageSource);
            request.setAttribute("choosePublic", choosePublic ? "true" : "false");
            request.setAttribute("source", source);
            //设置服务器名称
            new ServerInof().setServerName(getServletContext().getServerInfo());
            request.getRequestDispatcher(empRoot + basePath + "/rms_tempChoose.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信-富信发送-查询富信模板出现异常！");
            request.getSession(false).setAttribute("error", e);
        }
    }

    /**
     * 检测文件是否存在
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常对象
     */
    public void goToFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //文件地址url
        String url = request.getParameter("url");
        try {
            response.getWriter().print(fileUtil.checkFile(url));
        } catch (Exception e) {
            //异常处理
            response.getWriter().print("");
            EmpExecutionContext.error(e, "静态彩信发送检测文件是否存在出现异常！");
        }
    }

    /**
     * 企业富信相同内容发送
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常对象
     */
    public void sendSameRMS(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //检验结果
        String result = "";
        //手机号
        String phoneStr = "";
        //内容有效时长,默认值为48小时
        int validtm = 0;
        //提交发送时服务器时间，用于比较还有耗时计算
        Date date = Calendar.getInstance().getTime();
        //操作员ID
        Long lguserid = null;
        //企业编码
        String corpCode = "";
        //发送类型  定时还是即时
        String timerStatus;
        //发送时间
        String sendTime;
        // 富信发送主题
        String title;
        //实体对象
        LfMttask lfMttask;

        try {

            //任务ID
            String taskId = request.getParameter("taskId");
            //号码文件地址
            String phoneFileUrl = request.getParameter("phoneFileUrl");
            //用户ID
            //String lguseridStr = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguseridStr = SysuserUtil.strLguserid(request);

            //企业编码
            corpCode = request.getParameter("lgcorpcode");
            //富信主题
            title = request.getParameter("taskName");
            //定时还是即时
            timerStatus = request.getParameter("timerStatus");
            //发送时间
            sendTime = request.getParameter("timerTime");

            //当前登录操作员id
            if (lguseridStr == null || "null".equals(lguseridStr.trim()) || lguseridStr.trim().length() == 0 || "undefined".equals(lguseridStr.trim())) {
                EmpExecutionContext.error("企业富信相同内容发送，获取操作员id异常。userid:" + lguseridStr + "，errCode：" + IErrorCode.V10001);
                EmpExecutionContext.logRequestUrl(request, "后台请求");
                throw new EMPException(IErrorCode.V10001);
            }

            lguserid = Long.valueOf(lguseridStr);

            //特殊处理，如果sendType为空时，则为即时发送。
            if (timerStatus == null || "".equals(timerStatus)) {
                timerStatus = "0";
            }

            if ("1".equals(timerStatus)) {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
                String serverTime = format1.format(date);
                sendTime = sendTime + ":00";
                if (!format1.parse(serverTime).before(format1.parse(sendTime)) && !format1.parse(serverTime).equals(format1.parse(sendTime))) {
                    EmpExecutionContext.error("企业富信相同内容发送，定时发送时间小于服务器当前时间。");
                    throw new EMPException(IErrorCode.RM0005);
                }
            }

            //内容有效时长
            String validtmStr = request.getParameter("validtm");
            if (validtmStr != null && !"".equals(validtmStr) && validtmStr.matches("^\\d+$")) {
                validtm = Integer.parseInt(validtmStr);
                //最长时间为3天即72小时
                if (validtm > 72 || validtm < 1) {
                    EmpExecutionContext.error("企业富信相同内容发送，内容有效时长设置错误");
                    throw new EMPException(IErrorCode.RM00016);
                }
            } else {
                EmpExecutionContext.error("企业富信相同内容发送，内容有效时长设置错误");
                throw new EMPException(IErrorCode.RM00016);
            }

            //校验号码文件地址
            if (!checkPhoneFileUrl(phoneFileUrl)) {
                EmpExecutionContext.error("企业富信相同内容发送，校验手机号码Url地址异常。userid:" + lguseridStr);
                EmpExecutionContext.logRequestUrl(request, "后台请求");
                throw new EMPException(IErrorCode.RM0001);
            }


            // 封装lfMttask对象
            lfMttask = ottTaskBiz.getLfMttask(request);

            if (lfMttask == null) {
                EmpExecutionContext.error("企业富信相同内容发送封装LfMttask对象异常，" + "title:" + title + "，taskId：" + taskId + "，errCode：" + IErrorCode.V10001);
                EmpExecutionContext.error(result);
                throw new EMPException(IErrorCode.V10001);
            }
            //定时任务
            if (lfMttask.getTimerStatus() == 1) {
                result = ottTaskBiz.addRmsLfMttaskSend(lfMttask);
            } else {
                //非实时任务
                if (lfMttask.getEffCount() <= 100000) {
                    //调用发送方法，存表（LF_MTTASK）与发送
                    result = ottTaskBiz.addRmsLfMttaskSend(lfMttask);
                } else {
                    //非定时任务，大于十万改为用定时发送
                    lfMttask.setTimerStatus(2);
                    lfMttask.setTimerTime(new Timestamp(System.currentTimeMillis() + (30 * 1000)));
                    //调用发送方法，存表（LF_MTTASK）与发送
                    String timerStr = ottTaskBiz.addRmsLfMttaskSend(lfMttask);
                    if ("timerSuccess".equals(timerStr)) {
                        result = "handled";
                    } else {
                        result = timerStr;
                    }
                }
            }
        } catch (EMPException empex) {
            ErrorCodeInfo info = ErrorCodeInfo.getInstance();
            //获取自定义异常编码
            String message = empex.getMessage();
            //获取自定义异常提示信息
            result = info.getErrorInfo(message);
            if (lguserid == null) lguserid = 0L;
            EmpExecutionContext.error(empex, lguserid.toString(), corpCode, info.getErrorInfo(message), message);
            EmpExecutionContext.logRequestUrl(request, "企业富信相同内容发送异常！ 后台请求");
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "企业富信相同内容发送预览出现异常！");
        } finally {
            response.getWriter().print(result);
        }
    }

    /**
     * 校验存放号码文件路径
     *
     * @param phoneFileUrl 号码文件路径
     * @return 返回值
     */
    private boolean checkPhoneFileUrl(String phoneFileUrl) throws Exception {
        try {
            //手机号码地址
            if ("".equals(phoneFileUrl) || phoneFileUrl == null) {
                EmpExecutionContext.error("富信相同内容发送，获取手机号码Url地址异常。");
                return false;
            }
            //文件地址不存在
            if (!fileUtil.checkFile(phoneFileUrl)) {
                EmpExecutionContext.error("富信相同内容发送，获取手机号码Url地址异常,文件路径不存在");
                return false;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "富信相同内容发送，处理号码文件路径url异常");
            throw e;
        }
        return true;
    }


    /**
     * 根据彩信模板类型其彩信tms文件路径
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getTmMsgByBmtype(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dirUrl = fileUtil.getWebRoot();
        String htmlUrl = request.getParameter("tmUrl");

        String url = dirUrl + htmlUrl.replaceAll("fuxin.rms$", "");
        String filePath = dirUrl + htmlUrl.replace(".rms", ".html");
        String param = request.getParameter("paramInFile");
        param = URLDecoder.decode(param);
        String mms = null;
        try {
            if (filePath == null || "".equals(filePath)) {
                response.getWriter().print("");
            } else {
                File file = new File(filePath);
                StringBuffer sbuf = new StringBuffer();
                if (file.isFile() && file.exists()) {
                    InputStreamReader isr = null;
                    BufferedReader br = null;
                    FileInputStream fis = null;
                    try{
                    	fis = new FileInputStream(file);
	                    isr = new InputStreamReader(fis, "utf-8");
	                    br = new BufferedReader(isr);
	                    String lineTxt = null;
	                    while ((lineTxt = br.readLine()) != null) {
	                        sbuf.append(lineTxt);
	                    }
                    }finally{
                    	if(null != fis)
                    		fis.close();
                    	if(null != isr)
                    		isr.close();
                    	if(null != br)
                    		br.close();
                    }
                }
                if (null != param && !"".equals(param)) {
                    sbuf = new StringBuffer(getTemplateCount(sbuf.toString(), param));
                }

                if (sbuf != null) {
                    mms = sbuf.toString().replace("\r\n", "&lt;BR/&gt;");
                    mms = mms.replace("\n", "&lt;BR/&gt;");
                }
            }
            response.getWriter().print(mms);
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取彩信文件信息出现异常！");
        }
    }

    /**
     * 处理点预览时查询富信文件信息
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常对象
     */
    public void getTmMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String tmUrl = request.getParameter("tmUrl");
            String isokDownload = "";
            //判断是否使用集群   以及如果不存在该文件
            if (StaticValue.getISCLUSTER() == 1 && !fileUtil.checkFile(tmUrl)) {
                //下载到本地
                if (!"success".equals(commonBiz.downloadFileFromFileCenter(tmUrl))) {
                    isokDownload = "notmsfile";
                }
            }

            if ("".equals(isokDownload)) {
                if (tmUrl == null || "".equals(tmUrl)) {
                    response.getWriter().print("");
                } else {
                    String mms = fileUtil.readFileByLines(tmUrl);
                    if (mms != null && !"".equals(mms)) {
                        mms = mms.replace("\r\n", "&lt;BR/&gt;");
                        mms = mms.replace("\n", "&lt;BR/&gt;");
                    }
                    response.getWriter().print(mms);
                }
            } else {
                response.getWriter().print("");
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取富信模板文件信息出现异常！");
        }
    }

    /**
     * 彩信内容中的参数个数
     * 2018-1-25,caihq
     *
     * @param tmpContent 参数内容
     * @param param      参数，多个参数以逗号隔开
     */
    private String getTemplateCount(String tmpContent, String param) {
        String result = tmpContent;
        String[] paramArr = param.split(",");
        String eg = "#[pP]_[1-9][0-9]*#";
        Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(tmpContent);
        String paramStr = "";
        String pc = "";
        tmpContent = " " + tmpContent + "";
        String[] splitTplContent = tmpContent.split(eg);
        //int paramCount = 0;
        int count = 0;
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            sb.append(splitTplContent[count]);
            paramStr = m.group();
            paramStr = paramStr.toUpperCase();
            pc = paramStr.substring(paramStr.indexOf("#P_") + 3, paramStr.lastIndexOf("#"));
            //paramCount = Integer.parseInt(pc);
            int paramPos = Integer.parseInt(pc);
//			if (paramCount > templateCount) {
//				templateCount = paramCount;
//			}
            count++;
            sb.append(paramArr[paramPos - 1].replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;"));

//            String str = paramArr[paramPos-1].replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;");
//            tmpContent = tmpContent.replaceAll(paramStr, str);
        }
        sb.append(splitTplContent[count]);
        result = sb.toString();
        return result;
    }

    /**
     * 富信重发方法
     *
     * @return 重发结果
     */
    public void rmsReSend(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String returnStr = "reSendFail";
        try {
            String taskId = request.getParameter("taskId");
            if (taskId == null) {
                EmpExecutionContext.error("富信重发失败，调用富信重发出现异常,获取taskId出现异常。");
                response.getWriter().print(returnStr);
                return;
            }

        /*    ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//加密mtId

            taskId=encryptOrDecrypt.decrypt(taskId);
			System.out.println(taskId);
			*/
            returnStr = ottTaskBiz.rmsReSend(Long.parseLong(taskId));
            response.getWriter().print(returnStr);
        } catch (EMPException empex) {
            ErrorCodeInfo info = ErrorCodeInfo.getInstance();
            response.getWriter().print(info.getErrorInfo(empex.getMessage()));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "富信重发失败，调用富信重发出现异常。");
            response.getWriter().print(returnStr);
        }
    }

    /**
     * 定时任务撤销
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void changeTimerState(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //结果字串
        String result = "cancelFail";
        String taskId = request.getParameter("taskId");
        //获取不到TaskId
        if (taskId == null || "".equals(taskId)) {
            EmpExecutionContext.error("企业富信发送，定时任务撤销操作异常，TaskId为空");
            response.getWriter().print(result);
            return;
        }
        /*
        //获取加密类
        ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
        //解密mtId
        taskId=encryptOrDecrypt.decrypt(taskId);
        */
        try {
            LfMttask lfMttask = commonBiz.getLfMttaskbyTaskId(Long.parseLong(taskId));
            //撤销条件： mtTask不为空 发送状态为0 定时状态为1 定时时间不能为空
            if (!(lfMttask != null && lfMttask.getSendstate() == 0 && lfMttask.getTimerStatus() == 1 && lfMttask.getTimerTime() != null)) {
                EmpExecutionContext.error("企业富信发送，定时任务撤销操作异常，指定参数不能为空或存在异常。LfMttask对象，Sendstate ,TimerStatus,TimerTime");
                response.getWriter().print(result);
                return;
            }

            //获取当前时间毫秒数
            long nowTime = Calendar.getInstance().getTime().getTime();
            //发送时间毫秒数
            long timerTime = lfMttask.getTimerTime().getTime();
            //定时时间小于当前系统时间30秒之内的任务不允许撤销
            if (timerTime - nowTime < 30 * 1000) {
                response.getWriter().print("timeError");
                return;
            }

            //调用撤消任务的方法
            result = ottTaskBiz.cancelRmsTimerTask(lfMttask);

        } catch (Exception e) {
            String opContent = "企业富信撤销短信任务（短信任务TaskId：" + taskId + "）";
            EmpExecutionContext.error(e, request.getParameter("lguserid"), request.getParameter("lgcorpcode"), opContent + "企业富信群发任务撤销任务异常！", null);
        }
        response.getWriter().print(result);
    }

    /**
     * 发送界面的快捷方式
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void shortCut2Send(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> spUsers = new ArrayList<String>();
        LfDfadvanced lfDfadvanced = new LfDfadvanced();
        try {
            //获取当前操作员信息
            LfSysuser lfSysuser = ottTaskBiz.getCurrSysUserFromSession(request);
            //获取模板Id
            String tempId = request.getParameter("tempId");
            if (tempId == null || "".equals(tempId)) {
                EmpExecutionContext.error("企业短链-快捷方式跳转异常-传入tempId为null");
                throw new EMPException();
            }
            //获取模板相关信息
            LfTemplate lfTemplate = baseBiz.getById(LfTemplate.class, tempId);

            //获取任务ID
            Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);

            //获取绑定SP账号
            if(StaticValue.getCORPTYPE() == 1){
                //托管版
                List<UserDataVO> spUserList = userDataDAO.getSPUserList(lfSysuser.getCorpCode());
                for(UserDataVO vo : spUserList){
                    spUsers.add(vo.getUserId());
                }
            }else {
                //标准版
                List<Userdata> spUserList1 = smsBiz.getSpUserList(lfSysuser,true);
                for(Userdata vo : spUserList1){
                    spUsers.add(vo.getUserId());
                }
            }

            //获取高级设置默认信息
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.clear();
            conditionMap.put("userid", lfSysuser.getUserId().toString());
            conditionMap.put("flag", SAMESEND);
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("id", StaticValue.DESC);
            List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, conditionMap, orderMap);

            if (lfDfadvancedList != null && lfDfadvancedList.size() > 0) {
                lfDfadvanced = lfDfadvancedList.get(0);
            }

            //根据企业编码查询业务类型
            conditionMap.clear();
            orderMap.clear();
            conditionMap.put("corpCode&in", "0," + lfSysuser.getCorpCode());
            orderMap.put("corpCode", "asc");
            conditionMap.put("state", "0");
            List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, orderMap);

            request.setAttribute("busList", busList);
            request.setAttribute("lfDfadvanced", lfDfadvanced);
            request.setAttribute("spUserList", spUsers);
            request.setAttribute("taskId", taskId);
            request.setAttribute("isShortCut", "true");
            request.setAttribute("lfTemplate", lfTemplate);

            String tmIdSend = request.getParameter("tempId");
            if (StringUtils.isNotEmpty(tmIdSend)) {
                request.setAttribute("tmIdSend", tmIdSend);
                //request.getSession().setAttribute("tmIdSend", tmIdSend);
            }

            //设置服务器名称
            new ServerInof().setServerName(getServletContext().getServerInfo());

            request.getRequestDispatcher(empRoot + basePath + "/rms_sameMms.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业短链-快捷方式跳转异常");
        }
    }

    /**
     * 查询公共场景
     *
     * @param request
     * @param response
     */
    public void findPublicTemplatePage(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter pw = null;
        Gson json = new Gson();
        PageInfo pageInfo = null;
        LfTemplateVo lfTemplate;
        try {
            String loginUserId = request.getParameter("loginUserId");
            String lfTemplateVo_ = request.getParameter("lfTemplateVo");
            String pageInfo_ = request.getParameter("pageInfo");
            pw = response.getWriter();
            pageInfo = json.fromJson(pageInfo_, PageInfo.class);
            lfTemplate = json.fromJson(lfTemplateVo_, LfTemplateVo.class);
            lfTemplate.setIsPublic(1);
            lfTemplate.setTmState(1L);
            lfTemplate.setAuditstatus(1);
            //模板类型-11：富信模板
            lfTemplate.setTmpType(11);
            List<LfTemplateVo> list = commonTemplateBiz.getCommonTempalateList(lfTemplate, pageInfo, "");
            //下载第一帧用于预览
            templateSvt.downFirstFrame(request, list);
            Map<String, Object> res = new HashMap<String, Object>();
            res.put("pageInfo", pageInfo);
            res.put("vo", lfTemplate);
            res.put("record", list);
            pw.write(json.toJson(res));
            pw.flush();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信-富信发送-查询公共模板异常");
        } finally {
            if (null != pw) {
                pw.close();
            }
        }
    }


}
