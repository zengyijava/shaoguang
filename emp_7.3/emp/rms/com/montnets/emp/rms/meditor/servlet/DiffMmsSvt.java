package com.montnets.emp.rms.meditor.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.atom.SendSmsAtom;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.RmsBalanceLogBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.rms.meditor.biz.imp.CommonTemplateBiz;
import com.montnets.emp.rms.meditor.biz.imp.DiffMmsBiz;
import com.montnets.emp.rms.meditor.biz.imp.OTTTaskBiz;
import com.montnets.emp.rms.meditor.biz.imp.ParseExcel2JsonBiz;
import com.montnets.emp.rms.tools.RmsTxtFileUtil;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.CheckUtil;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:yangdl
 * @Data:Created in 14:37 2018.8.8 008
 */
public class DiffMmsSvt extends BaseServlet {
    /**
     * 添加序列号
     */
    private final SendSmsAtom smsAtom = new SendSmsAtom();
    private static final long serialVersionUID = 7157676361030395403L;
    // 富信允许发送号码最大值
    private static final Long RMS_MAX_PHONE_NUM = 5000000L;
    private final BaseBiz baseBiz = new BaseBiz();
    private final WgMsgConfigBiz msgConfigBiz = new WgMsgConfigBiz();
    private final KeyWordAtom keyWordAtom = new KeyWordAtom();

    //代表不同内容发送
    private static final String SENDTYPE = "1";

    private final RmsTxtFileUtil txtFileUtil = new RmsTxtFileUtil();
    private final RmsBalanceLogBiz rmsBalanceLogBiz = new RmsBalanceLogBiz();
    private final OTTTaskBiz ottTaskBiz = new OTTTaskBiz();
    private final DiffMmsBiz diffMmsBiz = new DiffMmsBiz();
    private final CommonTemplateBiz commonTemplateBiz = new CommonTemplateBiz();
    private final ParseExcel2JsonBiz parseExcel2JsonBiz = new ParseExcel2JsonBiz();

    /**
     * 点击提交按钮发送
     *
     * @param request
     * @param response
     */
    public void send(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //检验结果
        String result = "";
        //手机号
        //内容有效时长,默认值为48小时
        Integer validtm = 0;
        //操作员ID
        Long lguserid = null;
        //企业编码
        String corpCode = "";
        try {

            //登录操作员信息
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //登录操作员信息为空
            if (lfSysuser == null) {
                EmpExecutionContext.error("富信不同内容发送，从session获取登录操作员信息异常。lfSysuser为null，errCode：" + IErrorCode.V10001);
                throw new EMPException(IErrorCode.V10018);
            }

            lguserid = lfSysuser.getUserId();

            //内容有效时长
            String validtmStr = request.getParameter("validtm");
            if (validtmStr != null && !"".equals(validtmStr) && validtmStr.matches("^\\d+$")) {
                validtm = Integer.parseInt(validtmStr);
                //最长时间为72小时 最短时间为1小时
                if (validtm > 72 || validtm < 1) {
                    EmpExecutionContext.error("富信不同内容发送，内容有效时长设置超过最大值72小时或者低于最小值1小时");
                    throw new EMPException(IErrorCode.RM00016);
                }
            } else {
                EmpExecutionContext.error("富信不同内容发送，内容有效时长设置错误");
                throw new EMPException(IErrorCode.RM00016);
            }

            //富信选择模板文件的ID
            String tempId = request.getParameter("tempId");

            //模板ID
            if (tempId == null || "".equals(tempId)) {
                EmpExecutionContext.error("富信不同内容发送，获取模板ID异常。");
                throw new EMPException(IErrorCode.RM0007);
            }


            //调用发送方法
            result = diffMmsBiz.send(request);

        } catch (EMPException empex) {
            ErrorCodeInfo info = ErrorCodeInfo.getInstance();
            //获取自定义异常编码
            String message = empex.getMessage();

            //获取自定义异常提示信息
            result = info.getErrorInfo(message);
            if (lguserid == null) {
                lguserid = 0L;
            }
            EmpExecutionContext.error(empex, lguserid.toString(), corpCode, info.getErrorInfo(message), message);
            EmpExecutionContext.logRequestUrl(request, "企业富信不同内容发送异常！ 后台请求");
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "企业富信不同内容发送预览出现异常！");
        } finally {
            response.getWriter().print(result);
        }
    }


    /**
     * 处理模板V2.0
     * 企业富信不同内容页面发送进行预览操作
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常
     */
    public void preview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long startTime = System.currentTimeMillis();
        // 返回结果
        String result = "";
        // 发送账号
        String spUser;
        // 操作员id
        Long lguserid = null;
        // 企业编码
        String lgcorpcode = "";
        // 业务类型
        String busCode;
        //重号过滤
        String checkRepeat;
        //模板Id
        String tempId;
        //模板内容
        String tmsMsg;
        //富信余额
        String fxBalance;
        //SP账号余额
        String spFeeBalance;
        // 预览参数传递变量类
        PreviewParams preParams = new PreviewParams();
        //操作员对象
        LfSysuser lfSysuser;
        //所有上传文件的名字字符串
        String allFileNames;
        //模板类型
        String tempType;
        try {
            //从session中获取操作员对象
            lfSysuser = ottTaskBiz.getCurrSysUserFromSession(request);
            lguserid = lfSysuser.getUserId();

            Locale locale = MessageUtils.getLocale(request);
            //任务ID
            String taskId = request.getParameter("taskId");

            //校验TaskId
            if (taskId == null || "".equals(taskId) || !taskId.matches("\\d+")) {
                EmpExecutionContext.error("企业富信不同内容预览，获取TaskId异常。userId：" + lguserid + "，errCode:" + IErrorCode.V10002);
                throw new EMPException(IErrorCode.V10001);
            }

            //模板版本
            String tempVer = request.getParameter("tempVer");

            //校验TempVer
            if (tempVer == null || "".equals(tempVer) || !tempVer.matches("V[1-9]\\.0")) {
                EmpExecutionContext.error("企业富信不同内容预览，获取TempVer异常。userId：" + lguserid + "，errCode:" + IErrorCode.V10002);
                throw new EMPException(IErrorCode.V10001);
            }


            Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
            // 是否有预览号码权限.
            if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                // 号码是否可见，0：不可见，1：可见
                preParams.setIshidephone(1);
            }

            // 获取运营商号码段
            String[] haoduan = msgConfigBiz.getHaoduan();
            if (haoduan == null) {
                EmpExecutionContext.error("企业富信不同内容预览，获取运营商号码段异常。userId：" + lguserid + "，errCode:" + IErrorCode.V10002);
                throw new EMPException(IErrorCode.V10002);
            }

            //设置文件名参数
            String[] fileNameparam = {taskId};

            // 获取号码文件url
            String[] phoneFilePath = txtFileUtil.getSaveRmsMobileFileUrl(lguserid, fileNameparam);
            if (phoneFilePath == null) {
                EmpExecutionContext.error("企业富信不同内容发送预览，获取发送文件路径失败。userId：" + lguserid + "，errCode:" + IErrorCode.V10013);
                throw new EMPException(IErrorCode.V10013);
            }

            //判断号码文件url是否存在，存在则返回
            if (new File(phoneFilePath[0]).exists()) {
                EmpExecutionContext.error("企业富信不同内容发送预览，获取发送文件路径失败，文件路径已存在，文件路径：" + phoneFilePath[0] + "，userid:" + lguserid + "，errCode：" + IErrorCode.V10013);
                throw new EMPException(IErrorCode.V10013);
            }

            preParams.setPhoneFilePath(phoneFilePath);

            //存储文件相关信息的Map
            Map<String, String> fileMap = new HashMap<String, String>();

            //动态模板发送，即不同内容发送
            preParams.setSendType(2);

            //表单元素集合
            List<FileItem> fileList = ottTaskBiz.getFileList(request, phoneFilePath, lguserid, SENDTYPE);

            //从表单元素集合中解析表单信息封装到一个Map
            Map<String, String> fieldInfo = ottTaskBiz.parseRequestForm2Map(fileList);

            //获取发送账号
            spUser = fieldInfo.get("mmsUser");
            // 获取企业编码
            lgcorpcode = fieldInfo.get("lgcorpcode");
            // 获取业务编码
            busCode = fieldInfo.get("busCode");
            //所有的上传文件名字
            allFileNames = fieldInfo.get("allFileNames");
            //过滤重号
            checkRepeat = fieldInfo.get("checkRepeat");
            //模板Id
            tempId = fieldInfo.get("tempId");
            //模板类型
            tempType = fieldInfo.get("templateType");
            // 清空Map
            fieldInfo.clear();

            //校验模板ID
            if (tempId == null || "".equals(tempId)) {
                EmpExecutionContext.error("企业富信不同内容预览，获取模板ID异常。userId：" + lguserid + "，errCode:" + IErrorCode.V10002);
                throw new EMPException(IErrorCode.V10001);
            }
            //校验tempType
            if (tempType == null || "".equals(tempType)) {
                EmpExecutionContext.error("企业富信不同内容预览，获取tempType异常。userId：" + lguserid + "，errCode:" + IErrorCode.V10002);
                throw new EMPException(IErrorCode.V10001);
            }

            //根据模板Id检验能否从数据库获取相应的记录
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            map.put("sptemplid", tempId);
            List<LfTemplate> lfTemplateList = baseBiz.getByCondition(LfTemplate.class, map, null);

            if (lfTemplateList == null || lfTemplateList.size() == 0) {
                EmpExecutionContext.error("企业富信不同内容发送预览，根据模板Id获取LfTemplate对象异常，userid:" + lguserid + "，errCode：" + IErrorCode.V10001);
                throw new EMPException(IErrorCode.V10001);
            }
            LfTemplate lfTemplate = lfTemplateList.get(0);

            //模板版本
            String ver = lfTemplate.getVer();
            //模板名称
            String tmName = lfTemplate.getTmName();
            if (tmName == null || "".equals(tmName)) {
                EmpExecutionContext.error("企业富信不同内容预览，获取tmName异常。userId：" + lguserid + "，errCode:" + IErrorCode.V10002);
                throw new EMPException(IErrorCode.V10001);
            }

            //设置过滤重号
            boolean isCheckRepeat = "1".equals(checkRepeat);
            preParams.setCheckRepeat(isCheckRepeat);
            //操作员、企业编码、SP账号检查
            boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, lgcorpcode, spUser, null);
            if (!checkFlag) {
                EmpExecutionContext.error("企业富信不同内容发送预览，检查操作员、企业编码、发送账号不通过，taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，userid：" + lguserid
                        + "，spUser：" + spUser
                        + "，errCode:" + IErrorCode.B20007);
                throw new EMPException(IErrorCode.B20007);
            }
            //获取模板文本内容
            tmsMsg = commonTemplateBiz.getTextContentbyTempid(tempId, tempType);

            //富信模板文件内容是否为空校验
            String content = commonTemplateBiz.getContentbyTempid(tempId, "1", tempType);
            if (content == null || content.trim().length() == 0) {
                EmpExecutionContext.error("企业富信不同内容发送预览，获取模板内容异常，获取模板内容为空。taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，userid：" + lguserid
                        + "，errCode:" + IErrorCode.V10001);
                EmpExecutionContext.logRequestUrl(request, "后台请求");
                result = "emptyTemplate";
                return;
            }
            //处理参数 将参数变为#p_n#
            tmsMsg = tmsMsg.replaceAll("<input type=\"button\" class=\"j-btn\" unselectable=\"on\" readonly=\"\" value=\"\\{#参数(\\d+)#}\">", "#p_$1#");

            //模板内容关键字校验
            String words = keyWordAtom.checkText(tmsMsg, lgcorpcode);

            //关键字检查
            if (!"".equals(words)) {
                EmpExecutionContext.error("企业富信不同内容发送预览，关键字检查未通过，发送内容包含违禁词组:'" + words + "'，taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，userid：" + lguserid
                        + "，errCode:" + IErrorCode.V10016);
                result = "keywords&" + words;
                return;
            }

            //校验exlJson
            String exlJson = lfTemplate.getExlJson();
            if (exlJson == null || "".equals(exlJson.trim())) {
                EmpExecutionContext.error("企业富信不同内容发送预览，根据模板Id获取LfTemplate对象的exlJson字段内容为空");
                throw new EMPException(IErrorCode.V10001);
            }
            //文件上传不合格则直接抛异常
            if (fileList.size() == 0 && preParams.getInvalidFileName().size() > 0) {
                StringBuilder temp = new StringBuilder(MessageUtils.getWord("common", locale, "common_send_18"));
                for (Map.Entry<String, String> entry : preParams.getInvalidFileName().entrySet()) {
                    temp.append(entry.getKey()).append(":").append(entry.getValue()).append("  ");
                }
                EmpExecutionContext.error("企业富信不同内容发送预览，号码模板文件格式有误，taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，userid：" + lguserid
                        + "，spUser：" + spUser
                        + ", invalidFileNames: " + temp.toString()
                );
                throw new EMPException(temp.toString());
            }
            //从表单元素集合中解析上传文件
            ottTaskBiz.parseRequestFileForm2ReaderList(fileList, preParams, phoneFilePath, lguserid, SENDTYPE, fileMap, allFileNames, tempVer);

            //传入模板中参数个数
            JSONObject jsonObject = JSON.parseObject(exlJson);
            Integer paramCount = jsonObject.getInteger("allParamCount");
            preParams.setTempParamCount(paramCount);

            // 解析Excel，验证号码合法性、过滤黑名单、过滤重号、过滤关键字、拼接动态内容、生成有效号码文件和无效号码文件
            ottTaskBiz.parseRmsPhoneAndContent(fileList, preParams, haoduan, lguserid.toString(), lgcorpcode, busCode, spUser,  tempId, tmName, tempType,ver,locale);

            // 判断有效号码数是否超出范围
            if (preParams.getEffCount() > RMS_MAX_PHONE_NUM) {
                // 删除前面生成的有效号码和无效号码文件
                for (String aPhoneFilePath : phoneFilePath) {
                    txtFileUtil.deleteFile(aPhoneFilePath);
                }
                // 文件内有效号码大于500万
                result = "overstep";
                EmpExecutionContext.error("企业富信不同内容发送预览，有效号码数超过" + RMS_MAX_PHONE_NUM + "，无法进行发送，taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + lguserid);
                return;
            }

            // 没有发送号码，返回页面提示
            if (preParams.getSubCount() == 0) {
                result = "noPhone";
                EmpExecutionContext.error("企业富信不同内容发送预览，没有提交号码，无法进行发送。taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + lguserid);
                return;
            }

            // 预发送条数
            int preSendCount = 0;

            //预发送条数就是有效号码数
            preSendCount = preParams.getEffCount();

            //SP账号类型,1:预付费;2:后付费
            Long feeFlag = 2L;
            //获取SP账号类型 1为短信 2为彩信
            feeFlag = rmsBalanceLogBiz.getSpUserFeeFlag(spUser, 1);
            if (feeFlag == null || feeFlag < 0) {
                EmpExecutionContext.error("企业富信不同内容发送预览，获取SP账号计费类型异常。errCode：" + IErrorCode.B20044
                        + "，taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，spUser:" + spUser
                        + "，feeFlag:" + feeFlag);
                throw new EMPException(IErrorCode.B20044);
            }

            //从MBOSS获取富信余额，本地存一分 msType 1为短信 2为彩信
            fxBalance = rmsBalanceLogBiz.checkGwFeeRMS(spUser, preSendCount, lgcorpcode, false, 1);
            //检查SP账号余额(USERFEE)
            spFeeBalance = rmsBalanceLogBiz.checkSpBalanceRMS(spUser, (long) preSendCount, lgcorpcode, false);

            Map<String, Object> objMap = new HashMap<String, Object>();
            //无效文件数
            objMap.put("invalidFile", preParams.getInvalidFileName().size());
            objMap.put("tempParamCount", preParams.getTempParamCount());
            objMap.put("preSendCount", preSendCount);
            //富信余额
            objMap.put("fxBalance", fxBalance);
            //SP账号余额
            objMap.put("spFeeBalance", spFeeBalance);
            //SP账号扣费标记
            objMap.put("feeFlag", feeFlag);
            //发送标记
            objMap.put("sendType", "1");
            //存储拼接结果字符串（参数内容，参数个数，手机号，拼接内容）的文件url
            objMap.put("resultUrl", preParams.getPhoneFilePath()[4]);
            //模板档位
            objMap.put("tempDegree", lfTemplate.getDegree());
            // 预览信息 json
            result = preParams.getJsonStr(objMap);
            
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            StringBuilder opContent = new StringBuilder().append("预览S：").append(sdf.format(startTime)).append("，耗时：")
                    .append((System.currentTimeMillis() - startTime)).append("ms，文件数:").append(fileMap.get("fileCount")).append("，总大小:")
                    .append(Long.parseLong(fileMap.get("allFileSize")) / 1024).append("KB");
            //记录上传文件名
            if (Integer.parseInt(fileMap.get("fileCount")) > 0) {
                StringBuilder loadFileName = new StringBuilder(fileMap.get("loadFileName"));
                opContent.append("，文件名：").append(loadFileName.deleteCharAt(loadFileName.length() - 1).append("'"));
            }
            opContent.append("，提交总数：").append(preParams.getSubCount())
                    .append("，有效数：").append(preParams.getEffCount())
                    .append("，发送条数：").append(preSendCount)
                    .append("，taskId：").append(taskId)
                    .append("，spUser:").append(spUser);
            EmpExecutionContext.info("企业富信不同内容发送预览", lgcorpcode, lguserid.toString(), lfSysuser.getUserName(), opContent.toString(), "OTHER");
        } catch (EMPException empex) {
            ErrorCodeInfo info = ErrorCodeInfo.getInstance();
            //获取自定义异常编码
            String message = empex.getMessage();
            result = info.getErrorInfo(message);
            if (result == null) {
                result = message;
            }
            // 拼接前台自定义异常标识
            EmpExecutionContext.error(empex, lguserid, lgcorpcode);
            EmpExecutionContext.logRequestUrl(request, "后台请求");
        } catch (Exception ex) {
            result = "error";
            EmpExecutionContext.error(ex, lguserid, lgcorpcode);
            EmpExecutionContext.logRequestUrl(request, "后台请求");
        } finally {
            preParams.getValidPhone().clear();
            preParams.setValidPhone(null);
            preParams = null;
            request.setAttribute("result", result);
            String basePath = "/samemms";
            String empRoot = "rms";
            request.getRequestDispatcher(empRoot + basePath + "/rms_sameMmsPre.jsp").forward(request, response);
        }
    }

    /**
     * 读取用来存储预览十条信息的文件并显示
     *
     * @param request  request对象
     * @param response response对象
     * @throws IOException 异常
     */
    public void readRmsContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url = "";
        String tempUrl = "";
        BufferedReader reader = null;
        try {
            PrintWriter writer = response.getWriter();
            // 预览文件地址
            tempUrl = request.getParameter("url");
            // 地址处理
            url = txtFileUtil.getPhysicsUrl(tempUrl);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(url)), "utf-8"));
            String tmp = "";
            CommonVariables cv = new CommonVariables();
            Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
            int ishidephome = 0;
            //是否拥有号码预览权限
            if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                ishidephome = 1;
            }
            List<String[]> strings = new ArrayList<String[]>();
            // 逐行读取
            while ((tmp = reader.readLine()) != null) {
                tmp = tmp.trim();
                // 内容截取
                String[] snum = tmp.split("MWHS]#");
                //snum[0] 实际参数内容 sum[1] 实际参数个数 sum[2]手机号 sum[3]参数转成的json
                // 手机号特殊处理
                if (snum[2] != null && !"".equals(snum[2])) {
                    snum[2] = ishidephome == 0 ? cv.replacePhoneNumber(snum[2]) : snum[2];
                }
                snum[0] = snum[0].replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;");
                if (snum[3] != null && !"".equals(snum[3])) {
                    snum[3] = URLEncoder.encode(snum[3].replaceAll(" ", "%20"), "utf-8");
                }
                strings.add(snum);
            }
            writer.print(JSON.toJSONString(strings));
            //writer.print("</tbody>");
            reader.close();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "不同内容群发，读取用于存储预览信息的文件失败！url:" + url);
            throw e;
        } finally{
        	if(reader != null) {
                reader.close();
            }
        }
    }

    /**
     * 彩信内容中的参数个数
     *
     * @param templateCount 参数个数
     * @param tmsMsg        模板内容
     */
    private int getTemplateCount(int templateCount, String tmsMsg) {
        String eg = "#[pP]_[1-9][0-9]*#";
        Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(tmsMsg);
        String paramStr = "";
        String pc = "";
        int paramCount = 0;
        while (m.find()) {
            paramStr = m.group();
            paramStr = paramStr.toUpperCase();
            pc = paramStr.substring(paramStr.indexOf("#P_") + 3,
                    paramStr.lastIndexOf("#"));
            paramCount = Integer.parseInt(pc);
            if (paramCount > templateCount) {
                templateCount = paramCount;
            }
        }
        return templateCount;
    }

    /**
     * 预览结束后删除预览资源文件
     *
     * @param request
     * @param response
     */
    public void deleteDiffSendPreviewFile(HttpServletRequest request, HttpServletResponse response) {
        String tempUrl;
        try {
            tempUrl = request.getParameter("tmUrl");
            if (tempUrl == null || "".equals(tempUrl)) {
                throw new EMPException("企业富信-不同内容发送预览-预览结束后删除资源文件异常-tempUrl为null");
            }
            String srcDirPath = txtFileUtil.getWebRoot() + tempUrl.replace("fuxin.rms", "src/diffSend");
            //递归删除生成的src资源文件 \file\rms\templates\100001\485\src\diffSend
            delDirByRecursion(new File(srcDirPath));
            //删除生成html文件
            File htmlFile = new File(txtFileUtil.getWebRoot() + tempUrl.replace("fuxin.rms", "diffSendPreview.html"));
            if (htmlFile.exists() && htmlFile.isFile()) {
                if (!htmlFile.delete()) {
                    throw new EMPException("企业富信-不同内容发送预览-预览结束后删除生成预览html文件异常");
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, e.getMessage());
        }
    }

    /**
     * 递归删除目录下所有内容
     *
     * @param dir
     */
    private static void delDirByRecursion(File dir) {
        // 判断是否是一个目录, 不是直接删除; 如果是一个目录则继续调用自己
        if (dir.isDirectory()) {
            // 获取子文件/目录
            File[] subFiles = dir.listFiles();
            // 遍历该目录
            for (File subFile : subFiles) {
                delDirByRecursion(subFile);
            }
        }
        // 删除空目录或文件
        boolean flag = dir.delete();
        if (!flag) {
            EmpExecutionContext.error("删除文件失败！");
        }
    }
}
