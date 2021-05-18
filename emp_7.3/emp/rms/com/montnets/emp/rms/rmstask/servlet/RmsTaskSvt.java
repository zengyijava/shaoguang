package com.montnets.emp.rms.rmstask.servlet;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.rms.report.bean.RptStaticValue;
import com.montnets.emp.rms.rmstask.biz.RmsTaskBiz;
import com.montnets.emp.rms.rmstask.vo.DetailMtTaskVo;
import com.montnets.emp.rms.rmstask.vo.LfMttaskVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 群发历史查询、群发任务查询
 * @author  Cheng
 * @date 2018-8-2 09:27:04
 */
public class RmsTaskSvt extends BaseServlet {

    private final RmsTaskBiz rmsTaskBiz = new RmsTaskBiz();
    private final BaseBiz baseBiz = new BaseBiz();

    public void find(HttpServletRequest request, HttpServletResponse response){
        //记录开始查询时间
        Long start = System.currentTimeMillis();
        String requestPath = request.getRequestURI();
        //截取判断从哪个页面跳转 群发任务查看 =》rmsTaskRecord     群发历史查询 =》rms_rmsTaskHistory
        String titlePath = requestPath.substring(requestPath.lastIndexOf("/") + 1, requestPath.indexOf(".htm"));
        String modName = "企业富信-数据查询";
        String menuName = "rmsTaskRecord".equals(titlePath) ? "群发任务查看" : "群发历史查询";
        String jspPath = "rmsTaskRecord".equals(titlePath) ? "rmsTaskRecord.jsp" : "rmsHistoryRecord.jsp";
        //操作员Id
        String lguserid = null;
        //操作员名字
        String lgusername = null;
        //企业编码
        String corpCode = "";
        //分页对象
        PageInfo pageInfo = new PageInfo();
        //是否第一次进入
        Boolean isFirstEnter = false;
        //是否是从发送详情页面返回
        Boolean isBack = false;
        // sp账号查询结果集
        List<LfSpDepBind> spUserList = new ArrayList<LfSpDepBind>();
        //SP账号
        String spUser = "";
        //档位
        String degree = "";
        //任务批次
        String taskId="";
        //是否包含子机构
        String isContainsSun="";
        //机构名字
        String depName = "";
        //操作员名字
        String userName = "";
        //机构Id
        String depId = "";
        //操作员Id
        String userId = "";
        //查询开始时间
        String sendTime = "";
        //查询结束时间
        String recvTime = "";
        //富信主题
        String tmName = "";
        //发送主题
        String title = "";
        //模板ID
        String tempId = "";
        //发送状态
        String taskState = "";
        //查询对象
        LfMttaskVo mtVo = new LfMttaskVo();
        //用于数据回显
        LfMttaskVo newMtVo = new LfMttaskVo();
        //结果集合
        List<LfMttaskVo> mtVoList = null;
        //结果集条数
        Integer countSize = null;
        //号码查看权限
        String phoneLookPri = null;
        try {
            //按钮权限Map
            Map<String,String> btnMap = (Map<String,String>)request.getSession(false).getAttribute("btnMap");
            //号码查看权限
            phoneLookPri = btnMap.get(StaticValue.PHONE_LOOK_CODE);
            LfSysuser loginSysUser = rmsTaskBiz.getCurrenUser(request);
            corpCode = loginSysUser.getCorpCode();
            lguserid = loginSysUser.getUserId().toString();
            lgusername = loginSysUser.getName();
            isFirstEnter = pageSet(pageInfo, request);
            //获取SP账号
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            if(StaticValue.getCORPTYPE() == 1 && !"100000".equals(corpCode)){
                conditionMap.put("corpCode",corpCode);
            }
            spUserList = baseBiz.getByCondition(LfSpDepBind.class, conditionMap, null);
            //是否为返回操作
            isBack = "true".equals(request.getParameter("isBack"));
            if(!isFirstEnter || isBack) {
                if(isBack){
                    //从session中取值
                    Cookie[] cookies = request.getCookies();
                    String jsonStr = "";
                    String pageStr = "";
                    for(Cookie cookie:cookies){
                        if("rmsMtTaskVo".equals(cookie.getName())){
                            jsonStr = URLDecoder.decode(cookie.getValue(),"utf-8");
                        }
                        if("rmsMtTask_pageInfo".equals(cookie.getName())){
                            pageStr = cookie.getValue();
                        }
                    }
                    mtVo = JSONObject.parseObject(jsonStr, LfMttaskVo.class);
                    pageInfo.setPageSize(Integer.parseInt(pageStr.split("&")[0]));
                    pageInfo.setPageIndex(Integer.parseInt(pageStr.split("&")[1]));
                }else {
                    mtVo.setMenuName(menuName);
                    spUser = request.getParameter("spUser");
                    degree = request.getParameter("degree");
                    taskId = request.getParameter("taskId");
                    isContainsSun = request.getParameter("isContainsSun");
                    depName = request.getParameter("depNam");
                    userName = request.getParameter("userName");
                    depId = request.getParameter("depid");
                    userId = request.getParameter("userid");
                    sendTime = request.getParameter("sendtime");
                    recvTime = request.getParameter("recvtime");
                    tmName = request.getParameter("tmName");
                    title = request.getParameter("title");
                    taskState = request.getParameter("sendstate");
                    tempId = request.getParameter("tempId");
                    //depName与userName 不参与sql查询
                    mtVo.setDepName(depName);
                    mtVo.setUserName(userName);

                    if(StringUtils.isNotEmpty(taskId)){
                        mtVo.setTaskId(Long.parseLong(taskId));
                    }
                    if(StringUtils.isNotEmpty(tempId)){
                        mtVo.setTempId(Long.parseLong(tempId));
                    }
                    if(StringUtils.isNotEmpty(degree)){
                        mtVo.setDegree(Integer.parseInt(degree));
                    }
                    if(StringUtils.isNotEmpty(depId)){
                        mtVo.setDepIds(depId);
                    }
                    if(StringUtils.isNotEmpty(userId)){
                        String userids = userId.endsWith(",")? userId.substring(0, userId.length()-1): userId;
                        mtVo.setUserIds(userids);
                    }
                    mtVo.setCurrUserId(Long.parseLong(lguserid));
                    mtVo.setCurrCorpCode(corpCode);
                    mtVo.setTmName(tmName);
                    mtVo.setIscontainsSun(StringUtils.isNotEmpty(isContainsSun) && "1".equals(isContainsSun));
                    mtVo.setDepName(depName);
                    mtVo.setTitle(title);
                    mtVo.setSpUser(spUser);
                    mtVo.setTaskState(Integer.parseInt(taskState));
                    if("rmsTaskRecord".equals(titlePath)){
                        mtVo.setStartSendTime(sendTime);
                        mtVo.setEndSendTime(recvTime);
                    }else {
                        mtVo.setStartSubTime(sendTime);
                        mtVo.setEndSubTime(recvTime);
                    }
                    //将vo对象存到cookie中，方便导出功能与详情页面点击返回读取数据
                    String rptVoJson = URLEncoder.encode(JSONObject.toJSONString(mtVo),"utf-8");
                    Cookie degreeRptVo = new Cookie("rmsMtTaskVo",rptVoJson);
                    response.addCookie(degreeRptVo);
                    //将当前分页信息也存到cookie中，方便详情页面点击返回操作时取值
                    Cookie pageInfoCookie = new Cookie("rmsMtTask_pageInfo",pageInfo.getPageSize() + "&" + pageInfo.getPageIndex());
                    response.addCookie(pageInfoCookie);
                }
                //将mtVo对象复制一份用于数据回显
                newMtVo = (LfMttaskVo) BeanUtils.cloneBean(mtVo);
                //结果集
                mtVoList = rmsTaskBiz.getMtTask(mtVo, pageInfo);

                //国际化处理
                Map<String, String> langMap = new HashMap<String, String>(16);
                langMap.put("succSubmit", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_tjcg", request), "提交成功"));
                langMap.put("allFail", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_qbtjsb", request), "全部提交失败"));
                langMap.put("partfail", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_bftjsb", request), "部分提交失败"));
                langMap.put("timer", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_timeing", request), "定时中"));
                langMap.put("cancelled", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_canceled", request), "已撤销"));
                langMap.put("overtime", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_freezed", request), "超时未提交"));
                langMap.put("freezon", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_tjcg", request), "已冻结"));

                //处理结果集
                rmsTaskBiz.handleMtVoList(mtVoList, langMap);

                //获取加密类
                ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
                //加密mtId
                encryptOrDecrypt.batchEncrypt(mtVoList, "MtId", "MtIdCipher");

                countSize = mtVoList == null ? 0:mtVoList.size();
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,modName + "-" + menuName + "，查询异常！");
            countSize = -1;
        }finally {
            if(!isFirstEnter || isBack){
                //第一次进入或者返回不记录日志
                String conditionStr = "隶属机构=" + depName + ",SP账号=" + spUser
                        + ",操作员=" + userName + ",档位=" + degree
                        + "档,查询开始时间=" + sendTime + ",查询结束时间=" + recvTime
                        + ",任务批次=" + taskId + ",富信主题=" + tmName
                        + ",发送状态=" + taskState + ",查询总数=" + countSize;
                //记录日志
                String queryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start);
                String opContent = modName + "-" + menuName + "，totalcount:" + countSize + "条 ,查询时间："+ queryTime +",耗时:"+(System.currentTimeMillis() - start)+"ms,条件:"+ conditionStr;
                EmpExecutionContext.info("企业富信", corpCode, lguserid, lgusername, opContent, StaticValue.GET);
                request.setAttribute("mtVoList", mtVoList);
                request.setAttribute("mtVo", newMtVo);
                request.setAttribute("phoneLookPri", phoneLookPri);
            }
            request.setAttribute("sendTime", sendTime);
            request.setAttribute("recvTime", recvTime);
            request.setAttribute("currUserId", Long.parseLong(lguserid));
            request.setAttribute("countSize", countSize);
            request.setAttribute("spList", spUserList);
            request.setAttribute("pageInfo", pageInfo);
            try {
                request.getRequestDispatcher("rms/rmstask/" + jspPath).forward(request, response);
            } catch (Exception e) {
                EmpExecutionContext.error(modName + "-" + menuName + "，跳转页面异常！");
            }
        }
    }

    /**
     * 模板预览
     * @param request
     * @param response
     */
    public void tempPreview(HttpServletRequest request, HttpServletResponse response){
        // 模板Id(网关返回Id)
        String tmId = request.getParameter("tmId");
        BufferedReader br = null;
        try {
            if(StringUtils.isEmpty(tmId)){
                throw new Exception(" 数据查询 > 数据查询 > 群发历史查询 > 模板预览异常 > tmId参数为空");
            }
            //根据mtId查询lf_template
            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("sptemplid",tmId);
            List<LfTemplate> list = baseBiz.getByCondition(LfTemplate.class, conditionMap, null);
            if(list.size() == 0){
                throw new Exception(" 数据查询 > 数据查询 > 群发历史查询 > 模板预览异常 > 根据此tmId:"+ tmId +"找不到相应模板记录！");
            }
            //获取url
            String url = list.get(0).getTmMsg();
            //获取标题
            String tmName = list.get(0).getTmName();
            if(StringUtils.isEmpty(url)){
                throw new Exception(" 数据查询 > 数据查询 > 群发历史查询 > 模板预览异常 > 根据此tmId:"+ tmId +"找不到相应模板的文件路径！");
            }
            // 地址处理
            String filePath = new TxtFileUtil().getWebRoot() + url.replace("fuxin.rms", "fuxinPreview.html");
            File file = new File(filePath);
            StringBuilder htmlStr = new StringBuilder();
            if(!file.exists() || !file.isFile()){
                throw new Exception(" 数据查询 > 数据查询 > 群发历史查询 > 模板预览异常 > 预览文件路径:'"+ filePath +"'不存在或不是文件！");
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                htmlStr.append(lineTxt);
            }
            //插入标题
            htmlStr.insert(0, "<h4 style='margin-bottom: 10px;font-size: 14px;text-align: center;'>"+tmName+"</h4>");
            String finalStr = htmlStr.toString().replace("\r\n", "&lt;BR/&gt;").replace("\n", "&lt;BR/&gt;");
            response.getWriter().print(finalStr);
        }catch (Exception e){
            EmpExecutionContext.error(e,e.getMessage());
            try {
                response.getWriter().print("源文件不存在");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1,"数据查询 > 数据查询 > 群发历史查询 > 模板预览异常 > response.getWriter()方法异常！");
            }
        }finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e,"数据查询 > 数据查询 > 群发历史查询 > 模板预览异常 > 关闭IO对象异常！");
                }
            }
        }
    }

    /**
     * 群发历史查询点击详情查看
     * @param request
     * @param response
     */
    public void findAllSendInfo(HttpServletRequest request, HttpServletResponse response){

        //记录开始查询时间
        Long start = System.currentTimeMillis();
        PageInfo pageInfo = new PageInfo();
        //档位
        Integer degree = 0;
        //富信主题
        String tmName = "";
        //获取当前任务的发送时间
        String sendTime = "";
        //模板加密Id
        String encryptMtId = "";
        //企业编码
        String corpCode = "";
        //任务批次
        String taskId = "";
        //发送详情
        List<DetailMtTaskVo> mtTaskVos = new ArrayList<DetailMtTaskVo>();
        //返回结果
        Integer countSize = -1;
        try {
            //按钮权限Map
            Map<String,String> btnMap = (Map<String,String>)request.getSession(false).getAttribute("btnMap");
            //国际化
            //汇总信息
            String sendInfo = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_243", request);
            String fszsw = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_238", request);
            String fscgs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_239", request);
            String tjsbs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_240", request);
            String jssbs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_241", request);
            String tiao = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_242", request);

            //先获取当前操作员
            LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");

            corpCode = sysuser.getCorpCode();

            //获取页面传过来的lf_mttask表中的mtid
            encryptMtId  = request.getParameter("mtId");
            //获取加密类
            ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
            //解密mtId
            String mtId = encryptOrDecrypt.decrypt(encryptMtId);
            //根据mtid获取任务信息
            LfMttask lfMttask = baseBiz.getById(LfMttask.class, mtId);
            //获取当前任务的发送时间
            sendTime = lfMttask.getTimerTime().toString().substring(0,19);
            //批次Id
            taskId = lfMttask.getTaskId().toString();
            Long tempId  = lfMttask.getTempid();
            //获取档位与富信主题
            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
            if(tempId != 0){
                conditionMap.put("sptemplid",tempId.toString());
                List<LfTemplate> templates = baseBiz.findListByCondition(LfTemplate.class, conditionMap, null);
                if(templates != null && templates.size() > 0){
                    LfTemplate lfTemplate = templates.get(0);
                    //档位
                    degree = lfTemplate.getDegree();
                    //富信主题
                    tmName = lfTemplate.getTmName();
                }
            }

            pageSet(pageInfo, request);

            conditionMap.clear();

            //发送状态    4.接收失败  5.提交失败
            String sendType = request.getParameter("sendType");
            conditionMap.put("errorcode", StringUtils.defaultIfEmpty(sendType,"-1"));
            conditionMap.put("sendType", sendType);

            //下载状态  1.下载成功  2.下载失败  3.状态未返
            String downType = request.getParameter("downType");
            conditionMap.put("errorcode2", downType);
            conditionMap.put("downType", downType);

            //运营商
            String unicom = request.getParameter("spisuncm");
            conditionMap.put("unicom", unicom);

            //手机号
            String phone = request.getParameter("phone");
            conditionMap.put("phone", phone);

            if(lfMttask.getTaskType()==1){
                conditionMap.put("taskId", lfMttask.getTaskId().toString());
            }else{
                conditionMap.put("batchId", lfMttask.getBatchID().toString());
            }
            conditionMap.put("sendTime", sendTime);

            mtTaskVos = rmsTaskBiz.getSendDetailMtTask(conditionMap, pageInfo);

            //支持国际化
            Map<String, String> langMap = new HashMap<String, String>(16);
            langMap.put("lt", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("common", "rms_fxapp_fsmx_liantong", request), "联通"));
            langMap.put("yd", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_yidong", request), "移动"));
            langMap.put("dx", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_dianxin", request), "电信"));
            langMap.put("unknown", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_report_unknown", request), "未知"));
            langMap.put("noReturn", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_unback", request), "未返"));
            langMap.put("succeed", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_succeed", request), "成功"));
            langMap.put("fail", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_failure", request), "失败"));
            //处理结果集
            rmsTaskBiz.handleDetailMtTaskVo(mtTaskVos, langMap, btnMap, true);

            countSize = mtTaskVos == null ? 0 : mtTaskVos.size();

            //存到页面回显
            request.setAttribute("conditionMap", conditionMap);
            //存到cookie中方便导出功能
            String rptVoJson = URLEncoder.encode(JSONObject.toJSONString(conditionMap),"utf-8");
            Cookie degreeRptVo = new Cookie("rmsMtDetailTaskVo",rptVoJson);
            response.addCookie(degreeRptVo);


            //接收失败总数
            long rCount= lfMttask.getRfail2() == null ? 0L : lfMttask.getRfail2();
            //提交失败总数
            String failCount = lfMttask.getFaiCount() == null ? "0" : lfMttask.getFaiCount();
            //发送总数
            String icount = lfMttask.getIcount2() == null ? "0" : lfMttask.getIcount2();

            //提交总数
            Long icount1 = Long.parseLong(icount);
            //提交失败总数的long类型
            Long fail = Long.parseLong(failCount);
            //发送成功数
            long suc = icount1 - fail;
            if(lfMttask.getIcount2() != null) {
                //sendinfo ="发送总数为-条，其中发送成功数为-条，提交失败数为-条，接收失败数为-条。";
                sendInfo = fszsw + icount + fscgs + suc + tjsbs + failCount + jssbs + rCount + tiao;
            }
            request.setAttribute("sendInfo", sendInfo);
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业富信-群发历史查询-发送详情查看异常");
        }finally {
            try {
                request.setAttribute("tmName", tmName);
                request.setAttribute("taskId", taskId);
                request.setAttribute("degree", degree);
                request.setAttribute("sendTime", sendTime);
                request.setAttribute("isRptFlag", this.getRptFlag(corpCode));
                request.setAttribute("mtId", encryptMtId);
                request.setAttribute("pageInfo", pageInfo);
                request.setAttribute("countSize", countSize);
                request.setAttribute("mtTaskVos", mtTaskVos);
                request.getRequestDispatcher("rms/rmstask/rmsAllSendRecord.jsp").forward(request, response);
            } catch (Exception e) {
                EmpExecutionContext.error("企业富信-群发历史查询-发送详情查看异常,跳转页面异常！");
            }
        }
    }

    /**
     * 判断当前企业是否需要下载状态字段
     */
    private Boolean getRptFlag(String corpCode){
        //加载排序条件
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        //查询条件
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        PageInfo pageInfo=new PageInfo();
        try {
            if("100000".equals(corpCode)){
                //10万号
                return true;
            }else{
                if(corpCode != null && !"".equals(corpCode)) {
                    conditionMap.put("corpCode", corpCode);
                }
                List<LfCorp> list = baseBiz.getByConditionNoCount(LfCorp.class, null, conditionMap,
                        orderbyMap, pageInfo);
                if(list.size()>0){
                    // 0:表示不需要;1:需要通知状态报告;2:需要下载状态报告;3:通知、下载状态报告都要;(默认通知状态报告必须)
                    return list.get(0).getRptflag() == 3;
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"");
        }
        return false;
    }
    /**
     * 群发历史查询发送详情报表导出
     *
     * @param request
     * @param response
     */
    public void getDetailRptExcel(HttpServletRequest request, HttpServletResponse response){
        //记录日志
        LfSysuser loginSysUser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
        //起始ms数
        long start = System.currentTimeMillis();
        //返回结果
        String result = "false";
        //报表结果集
        List<DetailMtTaskVo> detailMtTaskVos;
        // 查询条件对象
        LinkedHashMap<String,String> conditionMap = null;
        try {
            //按钮权限Map
            Map<String,String> btnMap = (Map<String,String>)request.getSession(false).getAttribute("btnMap");
            //从session中取值
            Cookie[] cookies = request.getCookies();
            String mapStr = "";
            for(Cookie cookie:cookies){
                if("rmsMtDetailTaskVo".equals(cookie.getName())){
                    mapStr = URLDecoder.decode(cookie.getValue(),"utf-8");
                }
            }
            conditionMap = JSONObject.parseObject(mapStr, LinkedHashMap.class);

            detailMtTaskVos = rmsTaskBiz.getSendDetailMtTask(conditionMap, null);
            //支持国际化
            Map<String, String> langMap = new HashMap<String, String>(16);
            langMap.put("lt", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("common", "rms_fxapp_fsmx_liantong", request), "联通"));
            langMap.put("yd", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_yidong", request), "移动"));
            langMap.put("dx", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_dianxin", request), "电信"));
            langMap.put("unknown", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_report_unknown", request), "未知"));
            langMap.put("noReturn", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_unback", request), "未返"));
            langMap.put("succeed", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_succeed", request), "成功"));
            langMap.put("fail", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_failure", request), "失败"));
            //处理结果集
            rmsTaskBiz.handleDetailMtTaskVo(detailMtTaskVos, langMap, btnMap, true);

            langMap.clear();
            langMap.put("langName", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("common", "common_empLangName", request), "zh_CN"));
            langMap.put("number", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("common", "common_serialNumber", request), "序号"));
            langMap.put("unicom", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_operator2", request), "运营商"));
            langMap.put("phone", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fxsend_telphone", request), "手机号码"));
            langMap.put("sendStatus", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_task_sendStatusRpt", request), "发送状态报告"));
            langMap.put("downStatus", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_task_downStatusRpt", request), "下载状态报告"));
            langMap.put("rmsTaskTitle", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_task_taskSendDetail", request), "群发历史发送详情查看"));
            if(detailMtTaskVos.size() > 0 && detailMtTaskVos.size() < 500000){
                HashMap<String,String> map = rmsTaskBiz.createRptExcelDetailFile(detailMtTaskVos, this.getRptFlag(loginSysUser.getCorpCode()) , langMap);
                request.getSession(false).setAttribute("rmsTask_excel_map",map);
                result = "true";
            }
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业富信-数据查询-群发历史查询-发送详情查看报表导出功能异常！");
        }finally {
            String queryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start);
            String opContent = "企业富信-数据查询-群发历史查询-发送详情查看报表导出,导出时间："+ queryTime +",耗时:"+(System.currentTimeMillis() - start)+"ms";
            EmpExecutionContext.info("企业富信-数据查询-群发历史查询-发送详情查看报表导出", loginSysUser.getCorpCode(), loginSysUser.getUserId().toString(), loginSysUser.getName(), opContent, StaticValue.GET);
            try {
                response.getWriter().write(result);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "企业富信-数据查询-群发历史查询-发送详情查看报表导出功能异常,跳转页面异常！");
            }
        }
    }


    /**
     * 群发历史查询、群发任务查询报表导出
     *
     * @param request
     * @param response
     */
    public void getRptExcel(HttpServletRequest request, HttpServletResponse response){
        //记录日志
        LfSysuser loginSysUser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
        // 查询条件对象
        LfMttaskVo mtVo = null;
        //起始ms数
        long start = System.currentTimeMillis();
        //返回结果
        String result = "false";
        //报表结果集
        List<LfMttaskVo> mtVoList = null;
        try {
            //从session中取值
            Cookie[] cookies = request.getCookies();
            String jsonStr = "";
            for(Cookie cookie:cookies){
                if("rmsMtTaskVo".equals(cookie.getName())){
                    jsonStr = URLDecoder.decode(cookie.getValue(),"utf-8");
                }
            }
            mtVo = JSONObject.parseObject(jsonStr, LfMttaskVo.class);
            //结果集
            mtVoList = rmsTaskBiz.getMtTask(mtVo, null);

            //国际化处理
            Map<String, String> langMap = new HashMap<String, String>(16);
            langMap.put("succSubmit", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_tjcg", request), "提交成功"));
            langMap.put("allFail", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_qbtjsb", request), "全部提交失败"));
            langMap.put("partfail", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_bftjsb", request), "部分提交失败"));
            langMap.put("timer", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_timeing", request), "定时中"));
            langMap.put("cancelled", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_canceled", request), "已撤销"));
            langMap.put("overtime", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_freezed", request), "超时未提交"));
            langMap.put("freezon", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_tjcg", request), "已冻结"));

            //处理结果集
            rmsTaskBiz.handleMtVoList(mtVoList, langMap);

            //判断从哪个页面导出
            String menuName = request.getParameter("menuName");
            //menuName为rmsTask或者rmsTaskHis
            if(StringUtils.isEmpty(menuName) || (!"rmsTask".equals(menuName) && !"rmsTaskHis".equals(menuName))){
                return;
            }
            //支持国际化
            langMap.clear();
            langMap.put("langName", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("common", "common_empLangName", request), "zh_CN"));
            langMap.put("operator", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("common", "common_operator", request), "操作员"));
            langMap.put("organization", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("common", "common_organization", request), "隶属机构"));
            langMap.put("spUser", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_spzh", request), "SP账号"));
            langMap.put("tempId", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_tempid2", request), "模板ID"));
            langMap.put("sendTopic", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_sendtopic", request), "发送主题"));
            langMap.put("taskId", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_rwpc2", request), "任务批次"));
            langMap.put("sendTime", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_fssj", request), "发送时间"));
            langMap.put("phoneNum", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_hmgs", request), "号码个数"));
            langMap.put("busType", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fxsend_bstype2", request), "业务类型"));
            langMap.put("rmsTopic", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_tempchoose_fxtopic", request), "富信主题"));
            langMap.put("sendStatus", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_sendStatus", request), "发送状态"));
            langMap.put("degree", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_degreerep_range", request), "档位"));
            langMap.put("succSend", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_degreerep_fscgs", request), "发送成功数"));
            langMap.put("receiveFail", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_degreerep_jssbs", request), "接收失败数"));
            langMap.put("failSubmit", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_failSubmit", request), "提交失败数"));
            langMap.put("delayNum", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_DelayNum", request), "滞留数"));
            langMap.put("taskStatus", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_taskStatus", request), "任务状态"));
            langMap.put("validPhoneNum", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fxsend_validatenos2", request), "有效号码数"));
            langMap.put("createTime", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_dwtjbb_cjsj", request), "创建时间"));
            langMap.put("level", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_taskrecord_degree_p", request), "档"));
            langMap.put("rmsTask", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_task_menuName1", request), "群发任务查询"));
            langMap.put("rmsHisTask", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_task_menuName2", request), "群发历史查询"));

            if(mtVoList.size() > 0 && mtVoList.size() < 500000){
                HashMap<String,String> map = rmsTaskBiz.createRptExcelFile(mtVoList, menuName, langMap);
                request.getSession(false).setAttribute("rmsTask_excel_map",map);
                result = "true";
            }
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业富信-数据查询-群发历史查询、群发任务查询报表导出功能异常！");
        }finally {
            String queryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start);
            String opContent = "企业富信-数据查询-群发历史查询、群发任务查询报表导出,导出时间："+ queryTime +",耗时:"+(System.currentTimeMillis() - start)+"ms";
            EmpExecutionContext.info("群发历史查询、群发任务查询报表导出", loginSysUser.getCorpCode(), loginSysUser.getUserId().toString(), loginSysUser.getName(), opContent, StaticValue.GET);
            try {
                response.getWriter().write(result);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "企业富信-数据查询-群发历史查询、群发任务查询报表导出功能异常,跳转页面异常！");
            }
        }
    }
    /**
     * 群发历史查询、群发任务查询报表下载文件
     * @param request
     * @param response
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("rmsTask_excel_map");
        session.removeAttribute("rmsTask_excel_map");
        if(obj != null){
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            Map<String, String> resultMap = (Map<String, String>) obj;
            String filePath = resultMap.get("filePath");
            String fileName = resultMap.get("fileName");
            dfs.downFile(request, response, filePath, fileName);
        }
    }

    /**
     * 查询此模板是否设置定时任务
     * @param request 请求
     * @param response 响应
     */
    public void isScheduledTask(HttpServletRequest request, HttpServletResponse response){
        PrintWriter pw = null;
        String tmId = request.getParameter("tmid");
        try {
            StringBuilder builder = new StringBuilder();
            pw = response.getWriter();
            String sql = "select * from lf_timer a LEFT JOIN lf_mttask b on a.TASK_EXPRESSION = b.TASKID where b.TEMP_ID ='"+tmId+"' ";
            if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
                //oracle数据库
                sql = "select * from lf_timer a LEFT JOIN lf_mttask b on a.TASK_EXPRESSION =cast(b.TASKID AS varchar(250)) where b.TEMP_ID ='"+tmId+"' ";
                sql+= "and b.SUB_STATE = 2 AND (a.START_TIME > current_timestamp or a.NEXT_TIME> current_timestamp)";
            } else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
                //sqlserver数据库
                sql = "select * from lf_timer a LEFT JOIN lf_mttask b on a.TASK_EXPRESSION =cast(b.TASKID AS varchar(250)) where b.TEMP_ID ='"+tmId+"' ";
                sql+= "and b.SUB_STATE = 2 AND (a.START_TIME > current_timestamp or a.NEXT_TIME> current_timestamp)";
            }else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
                //db2数据库
                sql += "  and b.SUB_STATE = 2 AND (a.START_TIME > sysdate or a.NEXT_TIME>sysdate)";
            }else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
                //mysql数据库
                sql += "  and b.SUB_STATE = 2 AND (a.START_TIME > NOW() or a.NEXT_TIME>now())";
            }
            SuperDAO superDao = new SuperDAO();
            List<DynaBean> returnList = superDao.getListDynaBeanBySql(sql);
            if(returnList != null && returnList.size( )> 0) {
                builder.append("true");
            }else {
                builder.append("false");
            }
            pw.write(builder.toString());
            pw.flush();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询此模板是否设置定时任务失败：tmId："+tmId);
        } finally {
            IOUtils.closeQuietly(pw);
        }
    }
}
