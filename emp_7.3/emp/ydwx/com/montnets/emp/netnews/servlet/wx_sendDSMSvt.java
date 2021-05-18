package com.montnets.emp.netnews.servlet;

import com.montnets.emp.common.atom.SendSmsAtom;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sms.LfDrafts;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.biz.WXSendBiz;
import com.montnets.emp.netnews.common.CompressEncodeing;
import com.montnets.emp.netnews.daoImpl.Wx_netMangerDaoImpl;
import com.montnets.emp.netnews.daoImpl.Wx_ueditorDaoImpl;
import com.montnets.emp.netnews.entity.LfDfadvanced;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.*;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class wx_sendDSMSvt extends BaseServlet {

    private final SmsBiz smsBiz = new SmsBiz();
    private final BlackListAtom blBiz = new BlackListAtom();
    //换行符
    private final String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

    //常用文件读写工具类
    private final TxtFileUtil txtfileutil = new TxtFileUtil();
    private final BalanceLogBiz biz = new BalanceLogBiz();
    private final WXSendBiz wxSendBiz = new WXSendBiz();
    private final String empRoot = "ydwx";
    private final String basePath = "/sendWXDSM";
    //初始化zip压缩文件
    private static ZipFile zipFile;

    private final BaseBiz baseBiz = new BaseBiz();
    private final KeyWordAtom keyWordAtom = new KeyWordAtom();
    private final PhoneUtil phoneUtil = new PhoneUtil();

    /**
     * @param request
     * @param response
     */

    private final ResourceBundle bundle = ResourceBundle.getBundle("resourceBundle");
    private final String MovePhoneHead = bundle
            .getString("montnets.wx.MovePhoneHead"); // 移动
    private final String MovePhoneHeadURL = bundle
            .getString("montnets.wx.MovePhoneHead.url"); // 移动

    private final String UnionPhoneHead = bundle
            .getString("montnets.wx.UnionPhoneHead"); // 联通号码段
    private final String UnionPhoneHeadURL = bundle
            .getString("montnets.wx.UnionPhoneHead.url"); // 联通号码段

    private final String TelPhoneHead1 = bundle
            .getString("montnets.wx.TelPhoneHead1"); // 电信
    private final String TelPhoneHead1URL = bundle
            .getString("montnets.wx.TelPhoneHead1.url"); // 电信

    private final String TelPhone = bundle.getString("montnets.wx.Phone");


    public void find(HttpServletRequest request, HttpServletResponse response) {
        try {
            //错误编码
            String errorCode = "";
            //企业编码
            String corpCode = request.getParameter("lgcorpcode");
            //用户id
            //String userid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String userid = SysuserUtil.strLguserid(request);

            //用户名
            String username = request.getParameter("lgusername");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> conditionMapCorp = new LinkedHashMap<String, String>();

            //查找当前用户是否带有审批流
            LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
            conditionMapCorp.put("corpCode&in", "0," + corpCode);
            //排序
            orconp.put("corpCode", "asc");
            conditionMap.put("userId", username);
            List<LfFlow> flowList = baseBiz.getByCondition(LfFlow.class,
                    conditionMap, null);
            //将查询结果返回页面
            if (flowList != null && flowList.size() > 0) {
                request.setAttribute("isFlow", "true");
            } else {
                request.setAttribute("isFlow", "false");
            }

            //设置启用查询条件
            conditionMapCorp.put("state", "0");
            //设置查询手动和手动+触发
            conditionMapCorp.put("busType&in", "0,2");

            //查找当前机构下的所有业务类型
            try {
                List<LfBusManager> busList = baseBiz.getByCondition(
                        LfBusManager.class, conditionMapCorp, orconp);
                request.setAttribute("busList", busList);
            } catch (Exception e) {
                request.setAttribute("findresult", "-1");
                //错误码
                EmpExecutionContext.error("EBFV001");
                EmpExecutionContext.error(e, "动态网讯查询当前机构下业务类型异常!");
            }
            //清空map
            conditionMap.clear();

            //查找当前用户的发送账户(sp账号)
            LfSysuser currUser = baseBiz.getById(LfSysuser.class, userid);
            List<Userdata> spUserList = smsBiz.getSpUserList(currUser);
            //将查询结果返回前台
            request.setAttribute("spUserList", spUserList);

            //查找动态短信模板
            LinkedHashMap<String, String> conditionMapTemp = new LinkedHashMap<String, String>();
            conditionMapTemp.put("tmpType", "3");
            conditionMapTemp.put("tmState", "1");
            conditionMapTemp.put("isPass&in", "0,1");
            conditionMapTemp.put("dsflag", "1");

            LinkedHashMap<String, String> orderbyMapTemp = new LinkedHashMap<String, String>();
            orderbyMapTemp.put("tmid", StaticValue.ASC);
            //动态短信模板的集合
            List<LfTemplate> tmpList = baseBiz.getByCondition(LfTemplate.class, Long.parseLong(userid), conditionMapTemp, orderbyMapTemp);

            //产生taskId
            Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
            request.setAttribute("taskId", taskId.toString());
            String opContent = "获取taskid(" + taskId + ")成功";
            setLog(request, "动态网讯发送", opContent, StaticValue.GET);
            request.setAttribute("tmpList", tmpList);
            request.setAttribute("errorCode", errorCode);
            request.setAttribute("isExistSubNo", currUser.getIsExistSubNo());
            //页面跳转

            //查询网讯
//			conditionMap.clear();
//			Timestamp now = new Timestamp(System.currentTimeMillis());
//			conditionMap.put("CORPCODE", corpCode);
//			conditionMap.put("STATUS", "2");//定稿
//			String datetime=now.toString().substring(0, now.toString().lastIndexOf("."));
//			conditionMap.put("TIMEOUT&>", datetime);//对比有效时间
//			List<LfWXBASEINFO> infos = new ArrayList<LfWXBASEINFO>();
//			infos=baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);
//			request.setAttribute("infos", infos);
            //获取高级设置默认信息
            conditionMap.clear();
            conditionMap.put("userid", userid);
            //8：动态网讯发送
            conditionMap.put("flag", "8");
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("id", StaticValue.DESC);
            List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, conditionMap, orderMap);
            LfDfadvanced lfDfadvanced = null;
            if (lfDfadvancedList != null && lfDfadvancedList.size() > 0) {
                lfDfadvanced = lfDfadvancedList.get(0);
            }
            request.setAttribute("lfDfadvanced", lfDfadvanced);
            request.getRequestDispatcher(empRoot + basePath + "/wx_senddsm.jsp").forward(
                    request, response);
        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "进入动态网讯发送页面异常!");
            request.setAttribute("findresult", "-1");
            try {
                request.getRequestDispatcher(empRoot + basePath + "/wx_senddsm.jsp").forward(request, response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1, "发送跳转异常！");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, "IO异常！");
            }
        }
    }

    /***
     * 查询网讯内容
     * @param request
     * @param response
     */
    public void getTemplate(HttpServletRequest request, HttpServletResponse response) {
        //企业编码
        String corpCode = request.getParameter("lgcorpcode");
        String state = "1";
        //如果状态为1是静态的，如果是2为动态的
        state = request.getParameter("state");
        //查询启用的模板
        List<DynaBean> infos = new ArrayList<DynaBean>();
        String name = request.getParameter("tmName");
        //String lguserid=request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);


        Timestamp now = new Timestamp(System.currentTimeMillis());
        LfWXBASEINFO info = new LfWXBASEINFO();
        info.setNAME(name);
        info.setCORPCODE(corpCode);
        info.setSTATUS(2);
        info.setTIMEOUT(now);

        //网讯是否运营商商审核 0表示运营商不审核，1表示运营商审核
        if (StaticValue.getIsWxOperatorReview() == 1) {
            info.setOperAppStatus(1);//运营商审核通过
        }
        if (lguserid != null && !"".equals(lguserid)) {
            info.setCREATID(new Long(lguserid));
        }
        info.setTempType(new Integer(state));//使用静态的模板

        PageInfo pageInfo = new PageInfo();
        pageSet(pageInfo, request);
        pageInfo.setPageSize(10);
        try {
            infos = new WXSendBiz().getNetTemplate(info, pageInfo);
        } catch (Exception e1) {
            EmpExecutionContext.error(e1, "查询模板异常！" + name);
        }
        try {
            request.setAttribute("state", state);
            request.setAttribute("lgcorpcode", corpCode);
            request.setAttribute("corpCode", corpCode);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("infos", infos);
            request.setAttribute("wx_name", name);
            request.getRequestDispatcher(empRoot + basePath + "/wx_dynamicTemplate.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询网讯模板跳转页面异常!");
        }
    }

    /***
     * 查询网讯页面
     * @param netid 网讯ID
     * @param lguserid 用户ID
     * @param request
     * @return
     */
    public String sendMsgInfo(String netid, String lguserid, HttpServletRequest request) {
        String url = "";
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("NETID", netid);
            List<LfWXBASEINFO> li = baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);

            conditionMap.put("CREATID", lguserid);
            List<LfWXPAGE> pages = baseBiz.getByCondition(LfWXPAGE.class, conditionMap, null);
            if (li != null && li.size() > 0) {
                LfWXBASEINFO base = li.get(0);
                if (pages != null && pages.get(0).getPARENTID() == 0) {
                    request.setAttribute("base", base);
                    request.setAttribute("netid", base.getNETID());
                    url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/w/" + CompressEncodeing.CompressNumber(pages.get(0).getID(), 6);
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询网讯信息异常!");
        }
        return url;
    }

    /**
     * 手机号 要替换URL的部分URL 被替换的URL
     */
    public String UrlRep(String phone, String url, String repUrl) {
        String str = "";
        if (MovePhoneHead.indexOf(phone.substring(0, 3)) > -1) {
            str = url.replace(repUrl, MovePhoneHeadURL);
        } else if (UnionPhoneHead.indexOf(phone.substring(0, 3)) > -1) {
            str = url.replace(repUrl, UnionPhoneHeadURL);
        } else if (TelPhoneHead1.indexOf(phone.substring(0, 3)) > -1) {
            str = url.replace(repUrl, TelPhoneHead1URL);
        } else {
            str = url;
        }
        return str;
    }

    /**
     * 预览提交方法
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void preview(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String result = "";
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        long startTime = System.currentTimeMillis();
        //sp账号
        String spUser = "";
        //发送类型
        String sendType = "";

        //企业编码
        String lgcorpcode = "";

        //有效总数
        Long effCount = 0l;
        //提交号码数
        Long subCount = 0l;
        Long badCount = 0l;
        Long badModeCount = 0l;
        //重复号码条数
        Long repeatCount = 0l;
        //黑名单条数
        Long blackCount = 0l;
        // 包含关键字条数
        Long kwCount = 0l;
        String busCode = "";
        //动态内容
        String dtMsg = null;
        Map<String, List<String>> kwsList = null;
        String smsContent = null;
        int index;
        //短信模板参数个数
        int templateCount = 0;
        int conCount = 0;

        //常用文件读取工具类
        TxtFileUtil txtfileutil = new TxtFileUtil();

        StringBuffer contentSb = new StringBuffer();
        StringBuffer badContentSb = new StringBuffer();
        //存放用来预览显示的信息
        StringBuffer viewContentSb = new StringBuffer();
        //草稿箱文件相对路径
        String draftFilePath = "";
        //是否包含草稿文件
        String containDraft = null;
        boolean isOverSize = false;
        //上传文件最大大小
        long maxSize = StaticValue.MAX_SIZE;
        long zipSize = StaticValue.ZIP_SIZE;

        //***该参数仅为了记录日志需要***
        String opContent = "";
        String filenamestr = "";
        int fileCount_log = 0;
        //********************
        //贴尾
        String smsTail = "";
        BufferedReader bufferedReader = null;
        //此变量是为了区分上传多个文件时临时文件名称重复的问题
        int fileCount = 0;
        long allFileSize = 0L;
        SendSmsAtom smsAtom = new SendSmsAtom();
        // 预览参数传递变量类
        PreviewParams preParams = new PreviewParams();
        try {

            String repurl = request.getScheme() + "://" + request.getServerName();

            String taskId = request.getParameter("taskid");
            //获取运营商号码段
            String[] haoduan = new WgMsgConfigBiz().getHaoduan();
            String checkrepeat = request.getParameter("checkrepeat");

            //true表示要过滤,false表示不过滤
            boolean ischecked = false;
            if (checkrepeat != null && checkrepeat.equals("2")) {
                ischecked = true;
            }


            //当前登录用户
            //Long userId=Long.valueOf(request.getParameter("lguserid")==null?"0":request.getParameter("lguserid"));
            //漏洞修复 session里获取操作员信息
            Long userId = SysuserUtil.longLguserid(request);

            LfSysuser curSysuser = new LfSysuser();
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                curSysuser = (LfSysuser) loginSysuserObj;
            } else {
                curSysuser = baseBiz.getLfSysuserByUserId(userId);//当前登录操作员对象
            }

            //生成网讯地址
            String neturl = "";
            //路径
            String[] url = txtfileutil.getSaveUrl(userId);

            File zipUrl = null;
            HashSet<Long> repeatList = new HashSet<Long>();

            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置上传文件大小
            factory.setSizeThreshold(1024 * 1024);
            //上传临时文件存放地址
            String temp = url[0].substring(0, url[0].lastIndexOf("/"));
            //上传临时文件存放
            factory.setRepository(new File(temp));
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> fileList = null;
            try {
                fileList = upload.parseRequest(request);
            } catch (FileUploadException e) {
                EmpExecutionContext.error("EBFV003");
                throw e;
            }
            List<BufferedReader> readerList = new ArrayList<BufferedReader>();
            int fileIndex = 0;
            String netid = "";
            int netInfoLeng = 0;
            Iterator<FileItem> it = fileList.iterator();
            while (it.hasNext()) {
                FileItem fileItem = (FileItem) it.next();
                //文件名
                String fileName = fileItem.getFieldName();
                if (fileName.equals("spuserHidden")) {
                    //sp账号
                    spUser = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("lgcorpcode")) {
                    //当前用户企业编码
                    lgcorpcode = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("lguserid")) {
                    //当前用户userid
                    userId = Long.valueOf(fileItem.getString("UTF-8").toString());
                } else if (fileName.equals("busCodeHidden")) {
                    //业务类型
                    busCode = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("dtMsg")) {
                    //短信内容
                    dtMsg = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("smsTail")) {
                    //贴尾内容
                    smsTail = fileItem.getString("UTF-8").toString().replace("\r\n", " ");
                } else if (fileName.equals("containDraft")) {
                    containDraft = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("draftFile")) {
                    draftFilePath = fileItem.getString("UTF-8").toString();

                } else if (fileName.endsWith("sendtypeHidden")) {
                    //发送类型（短信模板发送，文件内容发送）
                    sendType = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("netId")) {
                    //网讯内容
                    netid = fileItem.getString("UTF-8").toString();
//					neturl = request.getScheme()+ "://"+ request.getServerName()+ ":"+ request.getServerPort()+ request.getContextPath()+ "/w/";
                    //读取配置文件 may
                    neturl = SystemGlobals.getValue("wx.pageurl") + "/w/";
                    String netInfo = wxSendBiz.sendMsgInfo(netid, taskId);
                    if (netInfo == null || netInfo.trim().length() == 0) {

                        EmpExecutionContext.error("获取网讯模板编码失败！");
                        goPreview("error", request, response);
                        return;
                    }
                    //注明：该字段是为了处理网讯转换回10进制处理pageid，taskid时候解密处理
                    //解密长度随着这个两个参数的值变化而变化的
                    netInfoLeng = netInfo.length();
                    neturl += netInfo;
                    //neturl = sendMsgInfo(netid,String.valueOf(userId),request);
                } else if (!fileItem.isFormField()
                        && fileItem.getName().length() > 0) {

                    //此变量是为了区分上传多个文件时临时文件名称重复的问题
                    fileCount++;
                    allFileSize += fileItem.getSize();
                    //判断文件大小是否超过限制
                    if (allFileSize > maxSize) {
                        fileItem.delete();
                        fileItem = null;
                        isOverSize = true;
                        break;
                    }
                    //查找短信内容中的参数个数（如果是动态模板短信）
                    String eg = "#[pP]_[1-9][0-9]*#";
                    Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(dtMsg);
                    while (m.find()) {
                        String rstr = m.group();
                        rstr = rstr.toUpperCase();
                        String pc = rstr.substring(rstr.indexOf("#P_") + 3, rstr.lastIndexOf("#"));
                        int pci = Integer.parseInt(pc);
                        if (pci > templateCount) {
                            templateCount = pci;
                        }
                    }

                    //此变量是为了区分上传多个文件时临时文件名称重复的问题
                    fileCount++;

                    String fileCurName = fileItem.getName();
                    //仅为了输出日志的需要
                    filenamestr = fileCurName + "," + filenamestr;
                    fileCount_log = fileCount_log + 1;

                    //文件类型
                    String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();
                    //检验文件类型的合法性
                    if (!fileType.equals(".rar") && !fileType.equals(".zip") && !fileType.equals(".xls")
                            && !fileType.equals(".et") && !fileType.equals(".xlsx") && !fileType.equals(".txt")) {
                        // 文件类型不合法
                        EmpExecutionContext.error("相同内容预览，文件上传失败，文件类型不合法。userId：" + userId + "，errCode:" + ErrorCodeInfo.V10003);
                        throw new EMPException(ErrorCodeInfo.V10003);
                    }
                    //如果是zip文件
                    if (fileType.equals(".zip")) {
                        //判断压缩文件的大小
                        if (fileItem.getSize() > zipSize) {
                            fileItem.delete();
                            fileItem = null;
                            isOverSize = true;
                            break;
                        }
                        //创建临时文件
                        String zipFileStr = url[0].replace(".txt", "_" + fileCount + ".zip");
                        zipUrl = new File(zipFileStr);
                        //将上传的文件写入临时文件
                        fileItem.write(zipUrl);
                        ZipFile zipFile = new ZipFile(zipUrl);
                        Enumeration zipEnum = zipFile.getEntries();
                        ZipEntry entry = null;
                        while (zipEnum.hasMoreElements()) {
                            //这个变量的作用是，如果压缩包里面有多个文件，就需要多个临时文件，另外删除临时文件也可以用到
                            fileIndex++;
                            //解压缩后的文件名的标签序号，用于删除文件时
                            String fileIndexStr = String.valueOf(fileIndex) + "_" + String.valueOf(fileCount);
                            entry = (ZipEntry) zipEnum.nextElement();
                            //处理txt文件
                            if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".txt")) {
                                InputStream instream = zipFile.getInputStream(entry);
                                String charset = get_charset(instream);
                                BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), charset));
                                if (charset.startsWith("UTF-")) {
                                    reader.read(new char[1]);
                                }
                                readerList.add(reader);
                            } else if (!entry.isDirectory() && (entry.getName().toLowerCase().endsWith(".xls") || entry.getName().toLowerCase().endsWith(".et"))) {
                                readerList.add(jxExcel(url[0], zipFile.getInputStream(entry), fileIndexStr, 1));

                            }
                            //如果是.xlsx文件
                            else if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".xlsx")) {
                                readerList.add(jxExcel(url[0], zipFile.getInputStream(entry), fileIndexStr, 2));
                            }
                        }
                    } else if (fileType.equals(".xls") || fileType.equals(".et")) {
                        readerList.add(jxExcel(url[0], fileItem.getInputStream(), "_" + String.valueOf(fileCount), 1));
                    } else if (fileType.equals(".xlsx")) {
                        readerList.add(jxExcel(url[0], fileItem.getInputStream(), "_" + String.valueOf(fileCount), 2));
                    }// 读取zip压缩文件流
                    else if (fileType.equals(".rar")) {
                        String FileStr = url[0];
                        preParams.setPhoneFilePath(url);
                        //发送类型。1-相同内容，2动态模板，3不同内容
                        preParams.setSendType(3);
                        readerList.addAll(smsAtom.parseRar(fileItem, FileStr, fileCount, preParams));
                    } else {


                        //文件编码
                        String charset = "";
                        InputStream instream = fileItem.getInputStream();
                        try {
                            charset = get_charset(instream);
                        } catch (Exception e) {
                            //result=new ErrorCode().infoMap.get("EBFV004");
                            EmpExecutionContext.error("EBFV004");
                            EmpExecutionContext.error(e, "判断上传文件编码异常!");
                        }
                        //江流转换成对应编码格式
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(fileItem.getInputStream(), charset));
                        if (charset.startsWith("UTF-")) {
                            reader.read(new char[1]);
                        }
                        readerList.add(reader);
                    }
                }
            }

            //检查发送比较SP账号是否属于登录企业（防止攻击）
            boolean checkFlag = new CheckUtil().checkSysuserInCorp(curSysuser, lgcorpcode, spUser, null);
            if (!checkFlag) {
                EmpExecutionContext.error("动态网讯发送预览时，检查操作员和发送账号是否是当前企业下，checkFlag:" + checkFlag
                        + "，userid:" + curSysuser.getUserId()
                        + "，spuser:" + spUser);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(MessageUtils.extractMessage("common", "common_empLangName", request));
                result = info.getErrorInfo(IErrorCode.B20007);
                return;
            }

            //增加贴尾内容
            if (!"".equals(smsTail) && smsTail != null) {
                dtMsg = dtMsg + smsTail.trim();
            }
            // 0.处理草稿箱文件
            if (containDraft != null && StringUtils.isNotBlank(draftFilePath)) {
                TxtFileUtil txtFileUtil = new TxtFileUtil();
                String webRoot = txtFileUtil.getWebRoot();
                File draftFile = new File(webRoot, draftFilePath);
                if (!draftFile.exists() && StaticValue.getISCLUSTER() == 1) {
                    CommonBiz comBiz = new CommonBiz();
                    String downloadRes = "error";
                    //最大尝试次数
                    int retryTime = 3;
                    while (!"success".equals(downloadRes) && retryTime-- > 0) {
                        downloadRes = comBiz.downloadFileFromFileCenter(draftFilePath);
                    }
                    if (!"success".equals(downloadRes)) {
                        EmpExecutionContext.error("动态网讯草稿箱文件从文件服务器下载失败。");
                    }
                }
                if (!draftFile.exists()) {
                    EmpExecutionContext.error("动态网讯未找到草稿箱发送文件！");
                    result = "error";
                    return;
                }

                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(draftFile), "GBK"));
                    readerList.add(bufferedReader);
                } catch (Exception e) {
                    EmpExecutionContext.error("动态网讯读取草稿箱发送文件异常！");
                    result = "error";
                    return;
                }
            }


            String tmp;
            String phoneNum = "";
            //号码非法状态：1-非法，2-黑名单，3-重复，4关键字
            int mid = 0;
            //号码返回状态
            int resultStatus = 0;
            String formatIssue = "";
            String fileContent = ""; //文件内容
            //国外通道信息
            DynaBean spGate = smsBiz.getInterSpGateInfo(spUser);
            // 获取1到n-1条英文短信内容的长度
            int longSmsFirstLen = 0;
            // 单条短信字数
            int singlelen = 0;
            //英文短信签名长度
            int signLen = 0;
            //是否支持英文短信，1：支持；0：不支持
            int gateprivilege = 0;

            //change by denglj 2018.11.29
            BufferedReader reader = null;
            if (spGate != null) {
                // 获取1到n-1条英文短信内容的长度
                longSmsFirstLen = Integer.parseInt(spGate.get("enmultilen1").toString());
                // 单条短信字数
                singlelen = Integer.parseInt(spGate.get("ensinglelen").toString());
                //英文短信签名长度
                signLen = Integer.parseInt(spGate.get("ensignlen").toString());
                // 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
                if (signLen == 0) {
                    //国外通道英文短信签名长度
                    signLen = smsBiz.getenSmsSignLen(spGate.get("ensignstr").toString().trim());
                }
                //是否支持英文短信，1：支持；0：不支持
                gateprivilege = Integer.parseInt(spGate.get("gateprivilege").toString());
                if ((gateprivilege & 2) == 2) {
                    gateprivilege = 1;
                }
                //签名前置
                if ((gateprivilege & 4) == 4) {
                    longSmsFirstLen = longSmsFirstLen - signLen;
                }
            }
            //号码类型
            int phoneType = 0;
            //0:英文短信;1:中文短信
            String SmsCharType = "1";
            //英文短信长度
            int smsLen = 0;
            //下标0(0:英文短信;1:中文短信);下标1(处理后的短信内容);下标2(英文短信长度)
            String[] smsContentInfo = {"1", "", "0"};
            try {
                //发送短信号码个数不超过500万
                for (int r = 0; r < readerList.size(); r++) {

                    if (effCount > StaticValue.MAX_PHONE_NUM) {
                        break;
                    }
                    reader = readerList.get(r);

                    //逐行读取
                    while ((tmp = reader.readLine()) != null) {
                        subCount++;
                        mid = 0;
                        formatIssue = "";
                        tmp = tmp.trim();

                        if (StringUtils.isEmpty(tmp)) {
                            subCount--;
                            continue;
                        }

                        fileContent = tmp + ";" + fileContent;
                        index = tmp.indexOf(",");

                        //手机号码长度不合格
                        if (index < 7 || index > 21) {
                            mid = 1;
                            /*手机号码长度不合格*/
                            formatIssue = "(" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSMS_1", request) + ")";
                        } else {
                            //过滤号码部分
                            phoneNum = tmp.substring(0, index);
                            if ((phoneType = phoneUtil.getPhoneType(phoneNum, haoduan)) == -1) {
                                mid = 1;
                                /*手机号码号段或号码格式错误*/
                                formatIssue = "(" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSMS_2", request) + ")";
                            } else if ((resultStatus = phoneUtil.checkRepeat(phoneNum, repeatList)) != 0 && ischecked) {
                                //返回1为重复号码
                                if (resultStatus == 1) {
                                    mid = 3;
                                }
                                //返回-1为非法号码
                                else {
                                    mid = 1;
                                    /*手机号码格式不正确*/
                                    formatIssue = "(" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSMS_3", request) + ")";
                                }
                            } else if (blBiz.checkBlackList(lgcorpcode, phoneNum, busCode)) {
                                //过滤黑名单
                                mid = 2;
                            } else {
                                //过滤内容部分
                                smsContent = tmp.substring(index + 1).trim();


                                //内容长度为零
                                if (smsContent.length() == 0) {
                                    mid = 1;
                                    /*短信内容长度为零*/
                                    formatIssue = "(" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSMS_4", request) + ")";
                                } else if ("2".equals(sendType) && dtMsg != null) {
                                    conCount = smsContent.split(",").length;
                                    if (conCount < templateCount) {
                                        //动态模板提交时，如果文件内的参数少于模板内参数则视为格式不正确
                                        mid = 1;
                                        /*发送文件参数少于模板参数个数*/
                                        formatIssue = "(" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSMS_5", request) + ")";
                                    } else {
                                        //动态内容拼接处理
                                        smsContent = combineContent(smsContent, dtMsg);
                                    }
                                }

                                //下标0(0:英文短信;1:中文短信);下标1(处理后的短信内容);下标2(短信长度)
                                smsContentInfo = smsBiz.getSmsContentInfo(smsContent.replaceAll(StaticValue.EXECL_SPLID, ","), longSmsFirstLen, singlelen, signLen, gateprivilege);
                                //处理后的短信内容
                                smsContent = smsContentInfo[1];


                                //如果没在判断参数个数时不合格，就继续往下走
                                if (mid == 0) {
                                    //发送内容字数不合格
                                    if (smsContent.length() > 990 || smsContent.length() == 0) {
                                        mid = 1;
                                        /*发送内容字数必须是0-990个*/
                                        formatIssue = "(" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSMS_6", request) + ")";
                                    }
                                    //检查关键字
                                    else if (keyWordAtom.filterKeyWord(smsContent.toUpperCase(), lgcorpcode) != 0) {
                                        mid = 4;
                                    }
                                    //国际号码
                                    else if (phoneType == 3 && spGate != null) {
                                        //0:英文短信;1:中文短信
                                        SmsCharType = smsContentInfo[0];
                                        //英文短信长度
                                        smsLen = Integer.valueOf(smsContentInfo[2]);
                                        //英文短信大于620字或中文短信大于270字
                                        if (("0".equals(SmsCharType) && smsLen > (720 - 100)) || ("1".equals(SmsCharType) && smsContent.length() > (360 - 90))) {
                                            mid = 1;
                                            /*发送内容已超过国外通道短信最大长度*/
                                            formatIssue = "(" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSMS_7", request) + ")";
                                        }
                                    }
                                    if (mid == 0) {
//										//重重阻碍，修成正果
//										if(ischecked)
//										{
//											repeatList.add(phoneNum);
//										}
                                        //通过截取部分传输过去后，再做处理
                                        String tempPhone = phoneNum;
                                        String regx = "";//如果不符合这两种特殊的情况就不做处理
                                        if (tempPhone.length() > 2) {
                                            if (tempPhone.indexOf("+") > -1) {
                                                String first = tempPhone.substring(1, 2);//取加号后面一位数，避免数字太长了，导致转换不了
                                                tempPhone = tempPhone.substring(2);
                                                regx = "*0" + first + netInfoLeng;//组装之后的字符串
                                            } else if (tempPhone.indexOf("00") > -1) {
                                                String first = tempPhone.substring(0, 2);//取加号后面一位数，避免数字太长了，导致转换不了
                                                if ("00".equals(first)) {
                                                    tempPhone = tempPhone.substring(2);
                                                    regx = "*1" + netInfoLeng;//组装之后的字符串
                                                }
                                            }
                                        }
                                        //contentSb.append(phoneNum+","+smsContent).append(line);
                                        if ("1".equals(TelPhone.trim())) {
                                            // 手机号 要替换URL的部分URL 被替换的URL\
                                            contentSb.append(phoneNum).append(",").append(smsContent).append(" ").append(UrlRep(tmp, neturl, repurl)).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                                        } else {
                                            contentSb.append(phoneNum).append(",").append(smsContent).append(" ").append(neturl).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                                        }
                                        effCount++;
                                        if (effCount - 10 <= 0) {
                                            if ("2".equals(sendType)) {
                                                //预览信息
                                                viewContentSb.append(String.valueOf(templateCount) + "MWHS]#" + String.valueOf(conCount) + "MWHS]#");
                                            }
                                            if ("1".equals(TelPhone.trim())) {
                                                // 手机号 要替换URL的部分URL 被替换的URL\

                                                viewContentSb.append(phoneNum).append("MWHS]#").append(smsContent).append(" ").append(UrlRep(tmp, neturl, repurl)).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                                            } else {
                                                viewContentSb.append(phoneNum).append("MWHS]#").append(smsContent).append(" ").append(neturl).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                                            }
                                            //viewContentSb.append(phoneNum+"MWHS]#"+smsContent).append(line);
                                            //将预览信息写入文件
                                            txtfileutil.writeToTxtFile(url[3], viewContentSb
                                                    .toString());
                                            viewContentSb.setLength(0);

                                        }
                                    }
                                }
                            }
                        }

                        switch (mid) {
                            case 1:
                                /*格式非法：*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(formatIssue).append("：").append(tmp).append(line);
                                badModeCount++;
                                badCount++;
                                break;
                            case 2:
                                /*黑名单号码：*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(tmp).append(line);
                                blackCount++;
                                badCount++;
                                break;
                            case 3:
                                /*重复号码：*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code3", request)).append(tmp).append(line);
                                repeatCount++;
                                badCount++;
                                break;
                            case 4:
                                /*包含关键字：*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code13", request)).append(tmp).append(line);
                                kwCount++;
                                badCount++;
                                break;
                        }
                        // may add 加入了网讯插入数据的部分

                        //1000个号码写一次文件
                        if (effCount % 1000 == 0 && effCount >= 1000) {
                            txtfileutil.writeToTxtFile(url[0], contentSb
                                    .toString());


                            contentSb = new StringBuffer();
                        }
                        if (badCount % 1000 == 0 && badCount >= 1000) {

                            txtfileutil.writeToTxtFile(url[2], badContentSb
                                    .toString());
                            badContentSb = new StringBuffer();
                        }

                    }
                    //关闭流
                    reader.close();
                }

            } catch (Exception e) {
                //错误码
                EmpExecutionContext.error("EBFV005");
                //删除文件
                txtfileutil.deleteFile(url[0]);
                //异常处理
                EmpExecutionContext.error(e, "解析文件内容异常!");
                throw e;
            } finally {
                //add by denglj 2018.11.29
                if (reader != null) {

                    reader.close();
                }

                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                //清内存
    			try{
    				IOUtils.closeReaders(getClass(), readerList);
    			}catch(IOException e){
    				EmpExecutionContext.error(e, "");
    			}
                readerList.clear();
                repeatList.clear();
                readerList = null;
                repeatList = null;
                if (zipUrl != null) {
                    if (!zipUrl.delete()) {
                        //doThing
                    }
                }
                String FileStr = url[0];
                String FileStrTemp;
                String fileStr2;
                File file = null;
                //删除临时文件
                for (int j = 0; j < fileCount; j++) {
                    //删除没有经过压缩的excel的临时文件
                    int b = j + 1;
                    fileStr2 = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp_" + b + ".txt";
                    file = new File(fileStr2);
                    if (file.exists()) {
                        if (!file.delete()) {
                            //.....
                        }
                    }
                    //删除压缩多个文件的临时文件
                    for (int i = 0; i < fileIndex; i++) {
                        int a = i + 1;
                        FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + a + "_" + b + ".txt";
                        file = new File(FileStrTemp);
                        if (file.exists()) {
                            if (!file.delete()) {
                                //.....
                            }
                        }
                    }
                }

            }
            //--------------------------------------------------------------------------------------
            //再清空信息
            //repeatList.clear();
            readerList = null;
            repeatList = null;

            //超出导入文件的最大限制
            if (isOverSize) {
                result = "overSize";
            }
            //发送号码总数超出限制
            else if (effCount > StaticValue.MAX_PHONE_NUM) {
                for (int i = 0; i < url.length; i++) {
                    txtfileutil.deleteFile(url[i]);
                }
                result = "overstep";
            } else {
                if (!"".equals(fileContent)) {
                    saveWXtable(netid, taskId, dtMsg, fileContent);
                }

                txtfileutil.writeToTxtFile(url[0], contentSb.toString());

                //还需判断计费机制是否开启,只有当开启的情况下才进行下面的判断，这里需要添加一个判断发送总条数是否大于当前机构的可发送的最大条数.
                Long maxcount = 0L;
                int yct = 0;
                //统计预发送条数
                yct = smsBiz.countAllOprSmsNumber(spUser,
                        dtMsg, (("".equals(sendType) || sendType == null)
                                ? 3 : Integer.valueOf(sendType)) > 1 ? 2 : 1, url[1], null);
                //不满足机构扣费与运营上扣费费用的标识符，用于页面显示
                boolean isCharg = true;
                String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                //机构、SP账号检查余额标识，只要有其一个失败，则为false，之后的扣费将不再执行
                boolean isAllCharge = true;

                if (biz.IsChargings(userId)) {
                    //(还需判断计费机制是否开启,只有当开启的情况下才进行下面的判断)这里需要添加一个判断发送总条数是否大于当前机构的可发送的最大条数. add by chenhong 2012.06.12
                    EmpExecutionContext.debug("date:" + dateStr + "    lfsysuser.corpCode:" + curSysuser.getCorpCode() + ";lfCorp.corpCode:" + lgcorpcode);
                    //提供一个可获取最大可发送条数的方法.
                    maxcount = biz.getAllowSmsAmount(curSysuser);
                    EmpExecutionContext.debug("date:" + dateStr + "     余额:" + maxcount);
                    if (maxcount == null) {
                        maxcount = 0L;
                    }
                    //机构余额小于发送条数
                    if (maxcount - yct < 0) {
                        //设置检查标识为失败
                        isAllCharge = false;
                        EmpExecutionContext.error("动态网讯预览，机构余额小于发送条数，taskid:" + taskId
                                + "，corpCode:" + lgcorpcode
                                + "，userid：" + userId
                                + "，depFeeCount:" + maxcount
                                + "，preSendCount" + yct);
                    }
                    EmpExecutionContext.debug("date:" + dateStr + "     预发送条数:" + yct);
                } else {
                    isCharg = false;
                }
                //过滤号码写入文件
                if (badContentSb.length() > 0) {
                    txtfileutil.writeToTxtFile(url[2], badContentSb.toString());
                }
                if (filenamestr.length() > 0) {
                    opContent = "文件数：" + fileCount_log + "，文件名：" + filenamestr;
                }

                // SP账号余额
                Long spUserAmount = -1L;
                Long feeFlag = 2L;
                if (isAllCharge) {
                    //获取SP账号类型
                    feeFlag = biz.getSpUserFeeFlag(spUser, 1);
                    if (feeFlag == null || feeFlag < 0) {
                        EmpExecutionContext.error("动态网讯预览， 获取SP账号计费类型出现异常！spUser=" + spUser);
                    } else if (feeFlag == 1) {
                        //----增加SP账号检查---
                        spUserAmount = biz.getSpUserAmount(spUser);
                        if (spUserAmount != null) {
                            if (spUserAmount - yct < 0) {
                                //设置检查标识为失败
                                isAllCharge = false;
                            }
                        }
                        //设置检查标识为失败

                        EmpExecutionContext.info("动态网讯预览， SP账号计费类型为预付费,spUser=" + spUser);
                    } else if (feeFlag == 2) {
                        EmpExecutionContext.info("动态网讯预览， SP账号计费类型为后付费,spUser=" + spUser);
                    }
                }

                // 2013-08-07 add may
                String spFeeResult = "";
                if (isAllCharge) {
                    try {
                        spFeeResult = biz.checkGwFee(spUser, yct, lgcorpcode, 1);
                    } catch (Exception ex) {
                        EmpExecutionContext.error(ex, "移动网讯动态发送中查询网关费用异常");
                        request.setAttribute("result", result);

                    }
                }

                // ****增加预览时候，详细信息*****
                //格式化时间
                SimpleDateFormat sdfdate = new SimpleDateFormat("HH:mm:ss");
                String info = "预览S：" + sdfdate.format(startTime) + "，耗时：" + (System.currentTimeMillis() - startTime) + "ms，提交总数：" + subCount + "，有效数：" + effCount + "，";
                setLog(request, "动态网讯发送", info + opContent + " taskId：" + taskId, StaticValue.GET);

                if (subCount == 0) {
                    //没有可发送的号码
                    result = "noPhone";
                } else {
                    result = url[4] + "&" + String.valueOf(subCount)
                            + "&" + String.valueOf(effCount)
                            + "&" + String.valueOf(maxcount)
                            + "&" + String.valueOf(yct)
                            + "&" + String.valueOf(badModeCount)
                            + "&" + String.valueOf(repeatCount)
                            + "&" + String.valueOf(blackCount)
                            + "&" + url[1] + "&" + String.valueOf(kwCount) + "&" + String.valueOf(isCharg) + "&" + spFeeResult + "&" + spUserAmount + "&" + feeFlag;

                }
            }
        } catch (Exception ex) {
            //异常错误
            result = "error";
            //打印异常
            EmpExecutionContext.error(ex, "动态网讯预览异常！");
        } finally {
            goPreview(result, request, response);
        }
    }

    private String combineContent(String info, String inputContent) {
        String content = inputContent.replace("\r\n", " ");
        String params[] = info.split(",");
        int size = (info.length() - info.replace(",", "").length()) + 1;
        StringBuffer tempParam = null;
        for (int i = 1; i < size + 1; i++) {
            tempParam = new StringBuffer();
            tempParam.append("#P_").append(i).append("#");
            if (i < params.length + 1) {
                content = content.replace(tempParam.toString(), params[i - 1]);
                content = content.replace(tempParam.toString().replace("#P_", "#p_"), params[i - 1]);
            }
        }

        return content.replaceAll(StaticValue.EXECL_SPLID, ",");
    }

    /**
     * 跳转到预览
     *
     * @param result
     * @param request
     * @param response
     */
    private void goPreview(String result, HttpServletRequest request, HttpServletResponse response) {
        try {
            //将结果返回给页面
            request.setAttribute("result", result);
            //跳到中间页，（防止并发引起的异常）
            request.getRequestDispatcher(empRoot + basePath + "/dsm_preview.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "动态网讯发送预览页面跳转异常!");
        }
    }

    //
    public void saveWXtable(String netid, String taskId, String dtMsg, String fileContent) throws Exception {
        Wx_ueditorDaoImpl ueditorDao = new Wx_ueditorDaoImpl();
        ueditorDao.insertTempData(netid, taskId, dtMsg, fileContent);
    }

    /**
     * 点击提交按钮发送
     *
     * @param request
     * @param response
     */
    public void send(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        SmsSendBiz cfsb = new SmsSendBiz();
        SuperOpLog spLog = new SuperOpLog();
        String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
        //日志信息
        String opModule = StaticValue.SMS_BOX;
        String opSper = StaticValue.OPSPER;
        String opType = StaticValue.ADD;
        //日志内容
        String opContent = "动态网讯发送";
        //任务taskid
        String taskId = request.getParameter("taskId");
        //sp账号
        String spUser = request.getParameter("spUser");
        //任务主题
        String title = request.getParameter("taskname");
        //主题为默认时,直接返回(防止重发)
        /*不作为短信内容发送*/
        if (title != null && MessageUtils.extractMessage("ydwx", "ydwx_wxfs_jtwxfs_wxzt_value_1", request).equals(title.trim())) {
            EmpExecutionContext.error("动态网讯发送获取参数异常，" + "title:" + title + "，taskId：" + taskId);
            response.getWriter().print(ErrorCodeInfo.getInstance(langName).getErrorInfo(IErrorCode.V10001));
            return;
        }
        //短信内容
        String msg = request.getParameter("msg");
        //贴尾内容
        String smsTail = request.getParameter("smsTail");
        if (smsTail != null && !"".equals(smsTail)) {
            msg = msg + smsTail.trim();
        }
        //发送类型：2－动态模板短信；3－文件内容短信
        String sendType = request.getParameter("sendType");
        //业务类型
        String busCode = request.getParameter("busCode");
        //是否定时
        String timerStatuss = request.getParameter("timerStatus");
        Integer timerStatus = timerStatuss == null || "".equals(timerStatuss) ? 0 : Integer.valueOf(timerStatuss);
        //定时时间
        String timerTime = request.getParameter("timerTime");
        //提交类型
        Integer subState = Integer.valueOf("2");
        //用户id
        //String userid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String userid = SysuserUtil.strLguserid(request);


        //企业编码
        String corpcode = request.getParameter("lgcorpcode");
        //用户名
        String lgusername = request.getParameter("lgusername");
        String preStr = request.getParameter("preStr");
        //发送级别
        String[] preStrArr = preStr.split("&");
        //日志内容
        opContent = opContent + "（任务名称：" + title + "）";


        LfMttask mttask = new LfMttask();
        //提交总数
        mttask.setSubCount(Long.valueOf(preStrArr[1]));
        //有效总数
        mttask.setEffCount(Long.valueOf(preStrArr[2]));
        //号码文件地址
        mttask.setMobileUrl(preStrArr[preStrArr.length - 6]);
        if (taskId != null) {
            mttask.setTaskId(Long.parseLong(taskId));
        }
        mttask.setTitle(title);
        mttask.setSpUser(spUser);
        mttask.setBmtType(("".equals(sendType) || sendType == null)
                ? 3 : Integer.valueOf(sendType));
        mttask.setTimerStatus(timerStatus);
        //信息类型：1－短信  2－彩信 6-网讯
        mttask.setMsType(6);
        mttask.setSubState(subState);
        mttask.setBusCode(busCode);
        mttask.setMsg(msg);
        //根据发送类型去判断 短信类型
        mttask.setMsgType(("".equals(sendType) || sendType == null)
                ? 3 : Integer.valueOf(sendType));
        mttask.setSendstate(0);
        mttask.setCorpCode(corpcode);
        //发送优先级
        mttask.setSendLevel(0);
        mttask.setIsReply(0);
        //尾号
        mttask.setSubNo(request.getParameter("subNo"));
        String netid = request.getParameter("netId");
        //网讯模板
        mttask.setTempid(Long.valueOf(netid));
        int icount = 0;
        try {
            //统计预发送条数
            icount = smsBiz.countAllOprSmsNumber(mttask.getSpUser(),
                    mttask.getMsg(), mttask.getMsgType() > 1 ? 2 : 1, mttask.getMobileUrl(), null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "网讯发送异常");
            icount = 0;
        }
        //发送短信总条数(网关发送总条数)
        mttask.setIcount(String.valueOf(icount));

        //检查发送比较SP账号是否属于登录企业（防止攻击）
        LfSysuser curSysuser = null;
        Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
        if (loginSysuserObj != null) {
            curSysuser = (LfSysuser) loginSysuserObj;
        } else {
            try {
                curSysuser = baseBiz.getLfSysuserByUserId(userid);
            } catch (Exception e) {
                EmpExecutionContext.error("获取当前登录人异常，" + "lguserid:" + userid);
            }//当前登录操作员对象
        }
        boolean checkFlag = new CheckUtil().checkSysuserInCorp(curSysuser, corpcode, spUser, null);
        if (!checkFlag) {
            EmpExecutionContext.error("动态网讯发送时，检查操作员和发送账号是否是当前企业下，checkFlag:" + checkFlag
                    + "，userid:" + curSysuser.getUserId()
                    + "，spuser:" + spUser);
            //异步调用提示
            response.getWriter().print("error");
            return;
        }

        //结果
        String result = "";

        try {
            // 定时发送
            if (timerStatus == 1) {
                //日期格式化
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timerTime = timerTime + ":00";
                mttask.setTimerTime(new Timestamp(sdf.parse(timerTime).getTime()));
            } else {
                //立即发送，则定时时间取提交时间
                mttask.setTimerTime(mttask.getSubmitTime());
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("mobileUrl", mttask.getMobileUrl());
            List<LfMttask> mtLists = baseBiz.getByCondition(LfMttask.class, conditionMap, null);
            if (mtLists != null && mtLists.size() > 0) {
                result = "fileerror";
            }
            if ("".equals(result)) {
                //发送账号验证
                boolean flag = new CheckUtil().checkSysuserInCorp(curSysuser, corpcode, spUser, null);
                if (!flag) {
                    String name = "";
                    if (curSysuser != null) {
                        name = "(" + curSysuser.getUserName() + ")";
                    }
                    //自定义异常
                    throw new Exception("操作员" + name + "和发送账号(" + spUser + ")检查不通过!");
                }
                //当前操作用户
                mttask.setUserId(Long.valueOf(userid));

                //获取发送信息等缓存数据（是否计费、是否审核、用户编码）
                Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
                //调用发送方法
                result = cfsb.addSmsLfMttask(mttask, infoMap);
                //结果
                String reultClong = result;
                //根据错误编码得到错误信息
                result = new WGStatus(langName).getInfoMap().get(result);
                //如果错误信息找不到，将错误编码的值返回给result
                if (result == null) {
                    result = reultClong;
                }
                //提交成功 删除当前草稿
                String draftId = request.getParameter("draftId");
                if (StringUtils.isNotBlank(draftId) && ("timerSuccess".equals(result) || "createSuccess".equals(result) || "000".equals(result))) {
                    baseBiz.deleteByIds(LfDrafts.class, draftId);
                }
                //发送成功,将taskid返回界面,用于查看发送记录
                if ("000".equals(result)) {
                    result = result + "&" + taskId;
                    if (loginSysuserObj != null) {
                        LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                        //EmpExecutionContext.info("模块名称：动态网讯发送，企业："+loginSysuser.getCorpCode()+"，"+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"+"动态网讯发送（任务名称："+title+"）成功。");
                        EmpExecutionContext.info("动态网讯发送", loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), "动态网讯发送成功。[任务名称](" + title + "）", "OTHER");
                    }
                } else {
                    //增加操作日志
                    if (loginSysuserObj != null) {
                        LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                        //EmpExecutionContext.error("模块名称：动态网讯发送，企业："+loginSysuser.getCorpCode()+"，"+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"+"动态网讯发送（任务名称："+title+"）失败，错误："+result);
                        EmpExecutionContext.info("动态网讯发送", loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), "动态网讯发送失败。[任务名称,错误](" + title + "，" + result + "）", "OTHER");
                    }
                }
                //保存日志
                spLog.logSuccessString(lgusername, opModule, opType, opContent, corpcode);
            }
        } catch (EMPException empex) {
            //捕获自定义异常
            ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
            //获取异常内容提示
            String desc = info.getErrorInfo(empex.getMessage());
            result = desc;
            //失败日志
            spLog.logFailureString(lgusername, opModule, opType, opContent + opSper, empex, corpcode);
            EmpExecutionContext.error(empex, "动态网讯发送异常!");
        } catch (Exception e) {
            //网关没开
//			if(e.getClass().getName().equals("org.apache.http.conn.HttpHostConnectException"))
//			{
            if (e.getClass().isAssignableFrom(HttpHostConnectException.class)) {
                result = e.getLocalizedMessage();
                //保存日志
                spLog.logSuccessString(lgusername, opModule, opType, opContent, corpcode);
            } else {
                //错误
                result = "error";
                //失败异常日志
                spLog.logFailureString(lgusername, opModule, opType, opContent + opSper, e, corpcode);
            }
            //错误码
            EmpExecutionContext.error("EBFB010");
            //异常处理
            EmpExecutionContext.error(e, "动态网讯发送异常!");
        } finally {
            //异步调用提示
            response.getWriter().print(result);
        }
    }

    /**
     * 失败重提的方法add 2012.07.05
     *
     * @param request
     * @param response
     */
    public void reSendSMS(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String opModule = StaticValue.SMS_BOX;
        String opSper = StaticValue.OPSPER;
        String opType = StaticValue.ADD;
        SuperOpLog spLog = new SuperOpLog();
        String opContent = "重新提交短信任务";
        SmsSendBiz cfsb = new SmsSendBiz();
        String result = "";
        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        //短信任务id
        Long mtid = 0L;
        //用户名
        String username = null;
        String lgguid = null;
        //企业编码
        String corpcode = null;
        try {
            //短信任务id
            mtid = Long.parseLong(request.getParameter("mtid"));
            username = request.getParameter("lgusername");
            corpcode = request.getParameter("lgcorpcode");
            lgguid = request.getParameter("lgguid");
            //根据任务id查出短信任务
            LfMttask lfMttask = baseBiz.getById(LfMttask.class, mtid);
            if (!goToFile(lfMttask.getMobileUrl())) {
                //不允许重新提交.
                response.getWriter().print("nofindfile");
                return;
            }
            if (lfMttask.getIsRetry() != null && lfMttask.getIsRetry().toString().equals("1")) {
                response.getWriter().print("isretry");
                return;
            }
            //为了避免用户在上次请求还未返回时刷新页面.再次点失败重发造成的重复发送问题.这儿加一个Session判断这种情况的发生.
            if (request.getSession(false).getAttribute("isretryMtId") != null) {
                String a = request.getSession(false).getAttribute("isretryMtId").toString();
                if (a.equals(mtid.toString())) {
                    //已经重发
                    response.getWriter().print("isretry");
                    return;
                }
            }
            request.getSession(false).setAttribute("isretryMtId", mtid);

            //重新生成一条taskId
            Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
            lfMttask.setTaskId(taskId);

            if (lfMttask.getIsReply() == 1) {
                SMParams smParams = new SMParams();
                smParams.setCodes(taskId.toString());
                smParams.setCodeType(5);
                smParams.setCorpCode(corpcode);
                smParams.setAllotType(1);
                smParams.setSubnoVali(false);
                smParams.setTaskId(taskId);
                if (lgguid != null) {
                    smParams.setLoginId(lgguid);
                }
                //循环尾号发送,需获取新的尾号
                ErrorCodeParam errorCodeParam = new ErrorCodeParam();
                LfSubnoAllotDetail subnoAllotDetail = GlobalVariableBiz.getInstance().getSubnoDetail(smParams, errorCodeParam);
                if (errorCodeParam.getErrorCode() != null && "EZHB237".equals(errorCodeParam.getErrorCode())) {
                    response.getWriter().print("noUsedSubNo");
                    return;
                } else if (errorCodeParam.getErrorCode() != null && "EZHB238".equals(errorCodeParam.getErrorCode())) {
                    response.getWriter().print("noSubNo");
                    return;
                }
                if (subnoAllotDetail == null || subnoAllotDetail.getUsedExtendSubno() == null) {
                    response.getWriter().print("noSubNo");
                    return;
                }
                lfMttask.setSubNo(subnoAllotDetail.getUsedExtendSubno());
            }
            // 定时发送变成实时发送
            if (lfMttask.getTimerStatus() == 1) {
                lfMttask.setTimerStatus(0);
            }

            //定时
            lfMttask.setTimerTime(new Timestamp(System.currentTimeMillis()));
            lfMttask.setSubmitTime(new Timestamp(System.currentTimeMillis()));
            lfMttask.setSendstate(0);
            lfMttask.setMtId(null);
            //需要将原来的文件重生产生一个。
            String[] url = txtfileutil.getSaveUrl(lfMttask.getUserId());
            String oldPath = txtfileutil.getWebRoot() + lfMttask.getMobileUrl();
            txtfileutil.copyFile(oldPath, url[0]);
            lfMttask.setMobileUrl(url[1]);

            //获取发送信息等缓存数据（是否计费、是否审核、用户编码）
            Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
            //调用发送方法
            result = cfsb.addSmsLfMttaskResend(lfMttask, infoMap);
            String reultClong = result;

            if (!"createSuccess".equals(result) && !"000".equals(result) && !"saveSuccess".equals(result)
                    && !"timerSuccess".equals(result) && !"timerFail".equals(result) && !"false".equals(result) && !"nomoney".equals(result)) {
                String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
                //根据错误编码得到错误信息
                result = new WGStatus(langName).getInfoMap().get(result);
                if (result == null) {
                    result = reultClong;
                }
            }

            if (!"false".equals(result) && !"nomoney".equals(result)) {
                //将原来的设置 为已重新提交.
                objectMap.put("isRetry", "1");
                conditionMap.put("mtId", mtid.toString());
                baseBiz.update(LfMttask.class, objectMap, conditionMap);

                //增加操作日志
                Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
                if (loginSysuserObj != null) {
                    LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                    EmpExecutionContext.info("网讯发送", loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), "重新提交短信任务。[短信任务id](" + mtid + "）", "OTHER");
                }

            } else {
                request.getSession(false).setAttribute("isretryMtId", "");
            }


            opContent += "(任务名称:" + lfMttask.getTitle() + ")";
            //存日志
            spLog.logSuccessString(username, opModule, opType, opContent, corpcode);

        } catch (Exception e) {
//			if(e.getClass().getName().equals("org.apache.http.conn.HttpHostConnectException"))
//			{
            if (e.getClass().isAssignableFrom(HttpHostConnectException.class)) {
                result = e.getLocalizedMessage();
                spLog.logSuccessString(username, opModule, opType, opContent, corpcode);
            } else {
                result = "error";
                spLog.logFailureString(username, opModule, opType, opContent + opSper, e, corpcode);
            }
            try {
                //将原来的设置 为已重新提交.
                objectMap.put("isRetry", "1");
                conditionMap.put("mtId", mtid.toString());
                baseBiz.update(LfMttask.class, objectMap, conditionMap);
            } catch (Exception e2) {
                EmpExecutionContext.error(e2, "重新提交网讯异常!");
                result = "error";
            }

            EmpExecutionContext.error("EBFB010");
            EmpExecutionContext.error(e, "重新提交发送网讯异常!");
        } finally {
            response.getWriter().print(result);
        }

    }

    /**
     * 检查文件是否存在
     *
     * @param url：文件地址
     * @return
     */
    public boolean goToFile(String url) {
        TxtFileUtil tfu = new TxtFileUtil();
        //不存在
        boolean result = false;
        try {
            //返回结果，是否存在
            result = tfu.checkFile(url);
        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "检查文件是否存在异常!");
            return false;
        }
        //返回结果
        return result;
    }

    /**
     * 验证号码是否合法
     *
     * @param request
     * @param response
     */
    public void checkMoblieLegal(HttpServletRequest request,
                                 HttpServletResponse response) {
        String mobile = request.getParameter("mobile");
        String separator = request.getParameter("separator");
        String spisuncm = request.getParameter("spisuncm");

        int result = 0;
        try {
            Pattern pa = Pattern.compile("( {2,})|\n|\r");
            Matcher m = pa.matcher(mobile);
            mobile = m.replaceAll("");
            String[] numbers = mobile.split(separator);
            String[] haoduan = new WgMsgConfigBiz().getHaoduan();
            if (spisuncm == null) {
                for (int i = 0; i < numbers.length; i++) {
                    if (!numbers[i].equals("")) {
                        result = phoneUtil.checkMobile(numbers[i], haoduan);
                        if (result == 0) {
                            break;
                        }
                    }
                }
            } else {
                for (int i = 0; i < numbers.length; i++) {
                    if (!numbers[i].equals("")) {
                        result = phoneUtil.checkMobile(numbers[i], Integer
                                .valueOf(spisuncm), haoduan);
                        if (result == 0) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            result = 2;
            EmpExecutionContext.error(e, "验证号码有效性异常!");
        } finally {
            try {
                response.getWriter().print(result);
            } catch (IOException e) {
                EmpExecutionContext.error(e, "异步调用异常！");
            }
        }
    }

    /**
     * 检查关键字
     *
     * @param request
     * @param response
     */
    public void checkBadWord(HttpServletRequest request,
                             HttpServletResponse response) {
        //内容
        String tmMsg = request.getParameter("tmMsg");
        String words = new String();
        try {
            //调用检查关键字的方法，并返回结果
            words = keyWordAtom.checkText(tmMsg);
        } catch (Exception e) {
            words = "error";
            EmpExecutionContext.error(e, "检查发送内容关键字异常!");
        } finally {
            try {
                response.getWriter().print(words);
            } catch (IOException e) {
                EmpExecutionContext.error(e, "异步调用异常！");
            }
        }
    }

    /**
     * @param request
     * @param response
     */
    public void getBindUserId(HttpServletRequest request,
                              HttpServletResponse response) {
        //条件map
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        // String busCode = request.getParameter("busCode");
        // conditionMap.put("busCode", busCode);
        conditionMap.put("platFormType", "1");
        try {
            List<LfSpDepBind> userList = baseBiz.getByCondition(
                    LfSpDepBind.class, conditionMap, null);
            if (userList != null && userList.size() > 0) {
                for (LfSpDepBind spDepBind : userList) {
                    response.getWriter().print("<option value='" + spDepBind.getSpUser()
                            + "'>" + spDepBind.getSpUser() + "</option>");
                }
            } else {
                response.getWriter().print("");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询互动项内容异常!");

        }
    }

    /**
     * 获取群组信息
     *
     * @param request
     * @param response
     */
    public void getUdg(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<LfUdgroup> udgList = baseBiz.getEntityList(LfUdgroup.class);
            request.setAttribute("udgList", udgList);
            request.getRequestDispatcher(empRoot + "/sms/sms_udg.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询群组信息异常!");

        }
    }

    /*
     * 解压zip目录文件
     */
    public boolean unZip(String unZipfileName) {

        boolean flag = false;

        byte[] b = new byte[1024];

        int size = 0;
        //上传zip压缩文件目录
        String upzipdir = "";
        FileOutputStream fileOut = null;
        File file;
        InputStream inputStream = null;

        try {

            zipFile = new ZipFile(unZipfileName);
            for (Enumeration<ZipEntry> entries = zipFile.getEntries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();

                String filepath = upzipdir + File.separator + entry.getName();
                file = new File(filepath);

                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    // 如果指定文件的目录不存在,则创建之.
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }

                    inputStream = zipFile.getInputStream(entry);

                    fileOut = new FileOutputStream(file);
                    while ((size = inputStream.read(b)) > 0) {
                        fileOut.write(b, 0, size);
                    }

                }
            }
           

            flag = true;

        } catch (IOException ioe) {
            EmpExecutionContext.error(ioe, "解压zip文件目录异常!");
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "解压zip文件目录异常!关闭文件流异常");
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "解压zip文件目录异常!关闭文件流异常");
                }
            }
            if (zipFile != null) {
                try {
                	 zipFile.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "解压zip文件目录异常!关闭文件流异常");
                }
            }
        }
        return flag;
    }

    /**
     * 过滤重复号码
     *
     * @param aa
     * @param ee
     * @return
     */
    private boolean checkRepeat(HashSet<Long> aa, String ee) {
        if (aa.contains(Long.parseLong(ee))) {
            return false;
        } else {
            //aa.add(ee);
        }
        return true;
    }

    /**
     * 读取用来存储预览十条信息的文件并显示
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void readSmsContent(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //预览文件地址
        String url = request.getParameter("url");
        String netid = request.getParameter("netid");

        String taskId = request.getParameter("taskId");
        //地址处理
        url = txtfileutil.getPhysicsUrl(url);
        //发送类型
        String sendType = request.getParameter("sendType");
        BufferedReader reader = null;
        String tmp = "";
        String phoneStr = "";
        //
        String smsContent = "";
        int index;
        int x = 0;
        CommonVariables cv = new CommonVariables();

        int ishidephome = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(url)), "GBK"));
            @SuppressWarnings("unchecked")
            Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
            if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                ishidephome = 1;
            }


            //动态模板短信发送
            if ("2".equals(sendType)) {
                /*编号 手机号码 文件内参数 短信内容 预览*/
                response.getWriter().print("<thead><tr align='center'><th>" + MessageUtils.extractMessage("ydwx", "ydwx_wxgl_hdxgl_bianhao", request) + "</th><th><center><div style='width:105px'>" + MessageUtils.extractMessage("ydwx", "ydwx_wxcxtj_hftj_shoujihaoma", request) + "</div></center></th>" +
                        "<th>" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_2", request) + "</th>" +
                        "<th>" + MessageUtils.extractMessage("ydwx", "ydwx_wxcxtj_fwtj_dxnr", request) + "</th><th>" + MessageUtils.extractMessage("common", "common_preview", request) + "</th></tr></thead><tbody>");
                //逐行读取
                while ((tmp = reader.readLine()) != null) {
                    x++;
                    tmp = tmp.trim();
                    //内容截取
                    String[] snum = tmp.split("MWHS]#");
                    //手机号特殊处理
                    String phone = snum[2] != null ? snum[2] : "";
                    if (phone != null && !"".equals(phone) && ishidephome == 0) {
                        phone = cv.replacePhoneNumber(phone);
                    }
                    //拆分 内容与链接
                    String[] s = snum[3].split("http://");
                    if (s.length >= 2) {
                        // 取得是最后一个地址
                        String pathurl = s[s.length - 1].substring(0, s[s.length - 1].lastIndexOf(".") - 2);
                        response.getWriter().print("<tr align ='center'><td>" + x + "</td><td>" + phone + "</td>" +
                                "<td>" + snum[1] + "</td>" +
                                "<td style='width:250px;'><xmp style='word-break: break-all;white-space:normal;'>" + snum[3] + "</xmp></td><td><a id=\"show\" href=\"javascript:showContent('" + netid + "','" + taskId + "','" + phone + "','http://" + pathurl + "')\">" + MessageUtils.extractMessage("common", "common_preview", request) + "</a></td></tr>");
                    }
                }
                response.getWriter().print("</tbody>");
            }
            //文件内容短信发送
            else {
                /*编号 手机号码 短信内容*/
                response.getWriter().print("<thead><tr align='center'><th>" + MessageUtils.extractMessage("ydwx", "ydwx_wxgl_hdxgl_bianhao", request) + "</th>" +
                        "<th><center><div style='width:89px'>" + MessageUtils.extractMessage("ydwx", "ydwx_wxcxtj_hftj_shoujihaoma", request) + "</div></center></th>" +
                        "<th>" + MessageUtils.extractMessage("ydwx", "ydwx_wxcxtj_fwtj_dxnr", request) + "</th></thead><tbody>");
                while ((tmp = reader.readLine()) != null) {
                    x++;
                    tmp = tmp.trim();
                    index = tmp.indexOf("MWHS]#");
                    phoneStr = tmp.substring(0, index);
                    smsContent = tmp.substring(index + 6).trim();
                    //手机号特殊处理
                    String phone = phoneStr != null ? phoneStr : "";
                    if (phone != null && !"".equals(phone) && ishidephome == 0) {
                        phone = cv.replacePhoneNumber(phone);
                    }
                    response.getWriter().print("<tr align ='center'><td >" + x
                            + "</td><td>" + phone + "</td>" +
                            "<td ><xmp style='word-break: break-all;white-space:normal;'>"
                            + smsContent + "</xmp></td></tr>");
                }
                response.getWriter().print("</tbody>");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "从Session取出按钮权限信息出现异常！");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

    }

    //根据网讯ID获取网讯所有页面
    public void showNetById(HttpServletRequest request, HttpServletResponse response) {
        String netid = request.getParameter("netId");
        String taskId = request.getParameter("taskId");
        String phone = request.getParameter("phone");
        String pathurl = request.getParameter("pathurl");

        List<LfWXPAGE> pageList = new ArrayList<LfWXPAGE>();
        try {
            if (pathurl != null) {
                String[] rexurl = pathurl.split("/w/");
                if (rexurl.length > 1) {

                    //根据netid获取page集合
                    Wx_netMangerDaoImpl netMangerDao = new Wx_netMangerDaoImpl();
                    pageList = netMangerDao.getnetByID(netid);
                    //判断下服务器是是否存在该文件
                    for (int i = 0; i < pageList.size(); i++) {
                        CommonBiz biz = new CommonBiz();
                        LfWXPAGE page = pageList.get(i);
                        if (page.getNAME().matches("Default web page|默认网讯页面|默認網訊頁面")) {
                            page.setNAME(MessageUtils.extractMessage("ydwx", "ydwx_survey_7", request));
                        }
                        String w = CompressEncodeing.CompressNumber(page.getID(), 6);
                        String url = w + "-";
                        String t = CompressEncodeing.CompressNumber(Long.valueOf(taskId), 6);
                        url += t;
                        int netInfoLeng = url.length();
                        //通过截取部分传输过去后，再做处理
                        String tempPhone = phone;
                        String regx = "";//如果不符合这两种特殊的情况就不做处理
                        if (tempPhone.length() > 2) {
                            if (tempPhone.indexOf("+") > -1) {
                                String first = tempPhone.substring(1, 2);//取加号后面一位数，避免数字太长了，导致转换不了
                                tempPhone = tempPhone.substring(2);
                                regx = "*0" + first + netInfoLeng;//组装之后的字符串
                            } else if (tempPhone.indexOf("00") > -1) {
                                String first = tempPhone.substring(0, 2);//取加号后面一位数，避免数字太长了，导致转换不了
                                if ("00".equals(first)) {
                                    tempPhone = tempPhone.substring(2);
                                    regx = "*1" + netInfoLeng;//组装之后的字符串
                                }
                            }
                        }
                        String httpUrl = rexurl[0] + "/w/" + url + CompressEncodeing.JieMPhone(tempPhone) + regx;
                        page.setCONTENT(httpUrl);
                        //判断网讯静态文件是否存在
                        if (!isStaticFileExist(page)) {
                            //分布式 ：如果存在就下载该文件到本地服务器上
                            if (StaticValue.getISCLUSTER() == 1) {
                                //获取JSP相对路径
                                String strRelativeSrc = "file/wx/PAGE/wx_" + page.getID() + ".jsp";
                                String result = biz.downloadFileFromFileCenter(strRelativeSrc);
                                if ("error".equals(result)) {
                                    page.setCONTENT("notexists");//用于页面显示错误页面
                                }
                            } else {
                                page.setCONTENT("notexists");//用于页面显示错误页面
                            }

                        }
                    }
                }
            }
            response.getWriter().print(pageList);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取网讯所有页面异常!");
        }

    }


    /**
     * DESC: 判断网讯静态文件是否存在
     *
     * @param objWxpage 网讯页面
     * @return true = 存在页面文件
     */
    public boolean isStaticFileExist(LfWXPAGE objWxpage) {

        //获取JSP相对路径
        String strRelativeSrc = "file/wx/PAGE/wx_" + objWxpage.getID() + ".jsp";
        boolean blisExist = true;
        String strPath = new TxtFileUtil().getWebRoot() + strRelativeSrc;
        File objFile = new File(strPath);
        if (!objFile.exists()) {
            blisExist = false;
        }

        return blisExist;

    }


    //判断txt文本编码
    public String get_charset(InputStream inp) throws Exception {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = new BufferedInputStream(inp);
        bis.mark(0);
        int read = bis.read(first3Bytes, 0, 3);
        if (read == -1) {
            return charset;
        }
        if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
            charset = "Unicode";
        } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
            charset = "UTF-16BE";
            // checked = true;
        } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
            charset = "UTF-8";
            // checked = true;
        }
        bis.reset();
        //返回结果
        return charset;
    }

    /**
     * 解析EXCEL，并读取内容
     *
     * @param url       文件目录
     * @param Newfile   新文件对象
     * @param ins       文件流对象
     * @param fileIndex 压缩文件内序号
     * @param exceltype excel类型。1-xls，et；2-xlsx
     * @return
     */
    public BufferedReader jxExcel(String url, InputStream ins, String fileIndex, int exceltype) {
        String FileStr = url;//url[0];
        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt"))+"_temp"+fileIndex+".txt";
        File Newfile = new File(FileStrTemp);
        if(Newfile.exists())
        {
            Newfile.delete();
        }

        try
        {
            FileOutputStream fos=new FileOutputStream(Newfile);
            OutputStreamWriter osw=new OutputStreamWriter(fos, "GBK");
            BufferedWriter  bw=new BufferedWriter(osw);
            String phoneNum="";
            String Context="";

            if(exceltype == 1)
            {
                HSSFWorkbook workbook = new HSSFWorkbook(ins);
                //循环每张表
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                    HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                    // 循环每一行
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++)
                    {
                        HSSFRow row = sheet.getRow(rowNum);
                        if (row == null) {
                            continue;
                        }
                        //得到第一列的电话号码
                        phoneNum = getCellFormatValue(row.getCell(0));
                        Context="";
                        //循环每一列（内容以,分隔开来）
                        for (int k = 1; k < row.getLastCellNum(); k++) {
                            HSSFCell cell = row.getCell(k);
                            if(cell !=null && cell.toString().length() >0)
                            {
                                Context +=","+getCellFormatValue(cell);
                            }

                        }
                        //一行一行的将内容写入到txt文件中。
                        bw.write(phoneNum+Context+line);
                    }
                }
            }else
            {

                XSSFWorkbook workbook = new XSSFWorkbook(ins);
                //循环每张表
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                    XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                    // 循环每一行
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++)
                    {
                        XSSFRow row = sheet.getRow(rowNum);
                        if (row == null) {
                            continue;
                        }
                        phoneNum =  getCellFormatValue(row.getCell(0));
                        Context="";
                        for (int k = 1; k < row.getLastCellNum(); k++) {
                            XSSFCell cell = row.getCell(k);
                            if(cell !=null && cell.toString().length() >0)
                            {
                                Context +=","+getCellFormatValue(cell);
                            }
                        }
                        bw.write(phoneNum+Context+line);
                    }
                }
            }

            bw.close();
            osw.close();
            fos.close();

            FileInputStream fis = new FileInputStream(Newfile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis,"GBK"));

            return br;
        }catch (FileNotFoundException e) {
            EmpExecutionContext.error(e, "解析的EXCEL文件不存在!");
        } catch (IOException e) {
            EmpExecutionContext.error(e, "解析的EXCEL文件异常!");
        }catch(Exception e){
            EmpExecutionContext.error(e, "解析EXCEL文件异常!");
        }
        return null;
    }

    /**
     * 判断单元格格式，返回字符串Excel2007
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(XSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case XSSFCell.CELL_TYPE_NUMERIC:
                case XSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，取得该Cell的Date值
                        Date date = cell.getDateCellValue();
                        // 把Date转换成本地格式的字符串
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        if (c.get(Calendar.HOUR) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0) {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        }
                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        // 是否有小数部分（分开处理）
                        if (Math.floor(cell.getNumericCellValue()) == cell.getNumericCellValue()) {
                            cellvalue = String.valueOf((long) cell.getNumericCellValue());
                        } else {
                            cellvalue = cell.getRawValue();
                        }

                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case XSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getStringCellValue();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue.replaceAll(",", StaticValue.EXECL_SPLID);
    }

    /**
     * 判断单元格格式，返回字符串Excel2003
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 数字
                case HSSFCell.CELL_TYPE_NUMERIC:
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，取得该Cell的Date值
                        Date date = cell.getDateCellValue();
                        // 把Date转换成本地格式的字符串
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        if (c.get(Calendar.HOUR) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0) {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        }
                    }
                    // 如果是纯数字
                    else {
                        // 是否有小数部分（分开处理）
                        if (Math.floor(cell.getNumericCellValue()) == cell.getNumericCellValue()) {
                            cellvalue = String.valueOf((long) cell.getNumericCellValue());
                        } else {
                            cellvalue = String.valueOf(cell.getNumericCellValue());
                        }
                        //System.out.println(cellvalue);
                    }
                    break;
                // 字符串
                case HSSFCell.CELL_TYPE_STRING:
                    cellvalue = cell.getStringCellValue();
                    break;
                // 字符串
                case HSSFCell.CELL_TYPE_FORMULA:
                    cellvalue = cell.getCellFormula();
                    break;
                // 空值
                case HSSFCell.CELL_TYPE_BLANK:
                    cellvalue = " ";
                    break;
                // 故障
                case HSSFCell.CELL_TYPE_ERROR:
                    cellvalue = " ";
                    break;
                default:
                    cellvalue = " ";
                    break;
            }
        } else {
            cellvalue = "";
        }
        return cellvalue.replaceAll(",", StaticValue.EXECL_SPLID);
    }

    public void getSubNo(HttpServletRequest request, HttpServletResponse response) {
        String corpCode = request.getParameter("lgcorpcode");
        String guid = request.getParameter("lgguid");
        String spUser = request.getParameter("spUser");
        String isReply = request.getParameter("isReply");
        String circleSubNo = request.getParameter("circleSubNo");
        String taskId = request.getParameter("taskId");
        String subNo = "";
        LfSubnoAllotDetail subnoAllotDetail = null;
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try {
            if ("0".equals(isReply)) {
                //0表示不需要回复
                subNo = "";
            } else if ("1".equals(isReply)) {
                //1表示  本次任务，需要回复
                //获取循环尾号，避免产生大量没有使用的尾号，只获取一次
                if (circleSubNo != null && !"".equals(circleSubNo)) {
                    subNo = circleSubNo;
                } else {
                    SMParams smParams = new SMParams();
                    // 编码（0模块编码1业务编码2产品编码3机构id4操作员guid,5任务id）
                    smParams.setCodes(taskId.toString());
                    //编码类别
                    smParams.setCodeType(5);
                    smParams.setCorpCode(corpCode);
                    //(分配类型0固定1自动有效期7天，null表是不设有效期)
                    smParams.setAllotType(1);
                    //尾号是否确定插入表
                    smParams.setSubnoVali(false);
                    smParams.setTaskId(Long.parseLong(taskId));
                    smParams.setLoginId(guid);
                    ErrorCodeParam errorCodeParam = new ErrorCodeParam();
                    subnoAllotDetail = GlobalVariableBiz.getInstance().getSubnoDetail(smParams, errorCodeParam);
                    if (errorCodeParam.getErrorCode() != null && "EZHB237".equals(errorCodeParam.getErrorCode())) {
                        //没有可用的尾号（尾号已经用完）
                        response.getWriter().print("noUsedSubNo");
                        return;
                    } else if (errorCodeParam.getErrorCode() != null && "EZHB238".equals(errorCodeParam.getErrorCode())) {
                        //获取尾号失败
                        response.getWriter().print("noSubNo");
                        return;
                    }
                    subNo = subnoAllotDetail != null ? subnoAllotDetail.getUsedExtendSubno() : null;
                    if (subNo == null || "".equals(subNo)) {
                        //获取尾号失败
                        response.getWriter().print("noSubNo");
                        return;
                    }
                }
            } else if ("2".equals(isReply)) {
                //2表示我的尾号（操作员固定尾号）
                // 获取操作员固定尾号
                conditionMap.put("loginId", guid);
                conditionMap.put("corpCode", corpCode);
                conditionMap.put("menuCode&is null", "isnull");
                //conditionMap.put("taskId&is null", "isnull");
                conditionMap.put("busCode&is null", "isnull");
                List<LfSubnoAllot> allots = baseBiz.getByCondition(LfSubnoAllot.class, conditionMap, null);
                LfSubnoAllot allot = allots != null && allots.size() > 0 ? allots.get(0) : null;
                if (allot == null || allot.getUsedExtendSubno() == null || "".equals(allot.getUsedExtendSubno())) {
                    //获取尾号失败
                    response.getWriter().print("noSubNo");
                    return;
                }
                subNo = allot.getUsedExtendSubno();
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取尾号信息异常!");
        }
        conditionMap.clear();
        conditionMap.put("userId", spUser);
        LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
        orderByMap.put("spisuncm", StaticValue.ASC);
        JSONObject jsonObject = null;
        int strLen = 0;
        String yd = "true";
        String lt = "true";
        String dx = "true";
        try {
            List<GtPortUsed> gtPortUseds = baseBiz.getByCondition(GtPortUsed.class, conditionMap, orderByMap);
            GtPortUsed gtPortUsed = null;
            jsonObject = new JSONObject();
            for (int i = 0; i < gtPortUseds.size(); i++) {
                gtPortUsed = gtPortUseds.get(i);
                //拓展尾号长度
                int cpnoLen = gtPortUsed.getCpno() != null ? gtPortUsed.getCpno().trim().length() : 0;
                //通道号+拓展尾号+尾号  的长度
                strLen = gtPortUsed.getSpgate().length() + cpnoLen + subNo.length();
                //判断各运营商的通道号+拓展尾号+尾号是否大于20，如果大于20
                //则前台需提示XX运营商通道号+尾号长度大于20，不允许发送
                if (gtPortUsed.getSpisuncm() == 0 && strLen > 20) {
                    yd = "false";
                } else if (gtPortUsed.getSpisuncm() == 1 && strLen > 20) {
                    lt = "false";
                } else if (gtPortUsed.getSpisuncm() == 21 && strLen > 20) {
                    dx = "false";
                }

            }
            //是否可以进行发送的标志
            jsonObject.put("sendFlag", yd + "&" + lt + "&" + dx);
            //尾号
            jsonObject.put("subNo", subNo);
            response.getWriter().print(jsonObject.toString());
        } catch (Exception e) {
            try {
                response.getWriter().print("error");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, "IO异常！");
            }
            EmpExecutionContext.error(e, "获取尾号信息异常!");
        }
    }

    /**
     * 检查关键字
     *
     * @param request
     * @param response
     */
    public void checkBadWord1(HttpServletRequest request, HttpServletResponse response) {
        String tmMsg = request.getParameter("tmMsg");
        String corpCode = request.getParameter("corpCode");
        String words = new String();
        try {
            words = keyWordAtom.checkText(tmMsg, corpCode);
        } catch (Exception e) {
            words = "error";
            EmpExecutionContext.error(e, "检查发送内容关键字异常!");
        } finally {
            try {
                response.getWriter().print("@" + words);
            } catch (IOException e) {
                EmpExecutionContext.error(e, "异步调用异常！");
            }
        }
    }

    public void getTmMsg1(HttpServletRequest request, HttpServletResponse response) {
        //发送模块获取模板（解决断网断库session超时用）

        String result = null;
        //模板id
        String tmId = request.getParameter("tmId");
        try {
            if ("".equals(tmId)) {
                result = "";
            } else {
                //根据id查询模板记录
                LfTemplate template = baseBiz.getById(LfTemplate.class, tmId);
                result = template.getTmMsg();
            }
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "获取模板信息失败!");
        } finally {
            //异步返回操作结果
            try {
                response.getWriter().print("@" + result);
            } catch (IOException e) {
                EmpExecutionContext.error(e, "异步调用异常！");
            }
        }
    }

    /**
     * 高级设置存为默认
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void setDefault(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //返回信息
        String result = "fail";
        try {
            //String lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);


            String lgcorpcode = request.getParameter("lgcorpcode");
            String busCode = request.getParameter("busCode");
            String spUser = request.getParameter("spUser");
            String flag = request.getParameter("flag");
            String repeat = request.getParameter("repeat");
            ;
            if (flag == null || "".equals(flag)) {
                EmpExecutionContext.error("动态网讯发送高级设置存为默认参数异常！FLAG:" + flag);
                response.getWriter().print(result);
                return;
            }
            if (lguserid == null || "".equals(lguserid)) {
                EmpExecutionContext.error("动态网讯发送高级设置存为默认参数异常！lguserid：" + lguserid);
                response.getWriter().print(result);
                return;
            }
            if (busCode == null || "".equals(busCode)) {
                EmpExecutionContext.error("动态网讯发送高级设置存为默认参数异常！busCode：" + busCode);
                response.getWriter().print(result);
                return;
            }
            if (spUser == null || "".equals(spUser)) {
                EmpExecutionContext.error("动态网讯发送高级设置存为默认参数异常！spUser：" + spUser);
                response.getWriter().print(result);
                return;
            }
            if (repeat == null || "".equals(repeat)) {
                EmpExecutionContext.error("动态网讯发送高级设置存为默认参数异常！repeat：" + repeat);
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
            lfDfadvanced.setBuscode(busCode);
            lfDfadvanced.setSpuserid(spUser);
            lfDfadvanced.setFlag(Integer.parseInt(flag));
            lfDfadvanced.setCreatetime(new Timestamp(System.currentTimeMillis()));
            lfDfadvanced.setRepeatfilter(Integer.parseInt(repeat));

            //高级设置存为默认
            result = wxSendBiz.setDefault(conditionMap, lfDfadvanced);
            //操作结果
            String opResult = "动态网讯发级设置存为默认失败。";
            if (result != null && "success".equals(result)) {
                opResult = "动态网讯发送高级设置存为默认成功。";
            }

            //操作员信息
            LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
            //操作员姓名
            String opUser = sysuser == null ? "" : sysuser.getUserName();
            //操作日志信息
            StringBuffer content = new StringBuffer();
            content.append("[业务编码，SP账号，重号过滤](").append(busCode).append("，").append(spUser).append("，").append(repeat).append(")");

            //操作日志
            EmpExecutionContext.info("动态网讯发送", lgcorpcode, lguserid, opUser, opResult + content.toString(), "OTHER");

            response.getWriter().print(result);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "动态网讯发送高级设置存为默认异常！");
            response.getWriter().print(result);
        }
    }

    /**
     * 保存草稿箱
     *
     * @param request
     * @param response
     */
    public void toDraft(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        long startTime = System.currentTimeMillis();

        //企业编码
        String lgcorpcode = "";
        //动态内容
        String dtMsg = null;
        Map<String, List<String>> kwsList = null;
        String smsContent = null;
        int index;
        //短信模板参数个数
        int templateCount = 0;

        //常用文件读取工具类
        TxtFileUtil txtfileutil = new TxtFileUtil();

        //上传文件最大大小
//		long maxSize = 100*1024*1024;
        long zipSize = 10 * 1024L * 1024L;

        //发送的手机号码的集合
        List<String> phoneList = new ArrayList<String>();
        //此变量是为了区分上传多个文件时临时文件名称重复的问题
        //草稿箱文件相对路径
        String selDraftFilePath = "";
        //草稿箱id
        String draftid = "";
        //发送主题
        String taskname = "";
        //处理返回结果
        JSONObject json = new JSONObject();
        json.put("ok", "0");
        int fileCount = 0;
        String filenamestr = "";
        try {
            //当前登录用户
            //	Long userId=Long.valueOf(request.getParameter("lguserid")==null?"0":request.getParameter("lguserid"));
            //漏洞修复 session里获取操作员
            Long userId = SysuserUtil.longLguserid(request);


            //路径
            String[] url = txtfileutil.getSaveUrl(userId);

            File zipUrl = null;
            HashSet<Long> repeatList = new HashSet<Long>();

            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置上传文件大小
            factory.setSizeThreshold(1024 * 1024);
            //上传临时文件存放地址
            String temp = url[0].substring(0, url[0].lastIndexOf("/"));
            //上传临时文件存放
            factory.setRepository(new File(temp));
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> fileList = null;
            try {
                fileList = upload.parseRequest(request);
            } catch (FileUploadException e) {
                EmpExecutionContext.error("EBFV003");
                throw e;
            }
            List<BufferedReader> readerList = new ArrayList<BufferedReader>();
            int fileIndex = 0;
            Iterator<FileItem> it = fileList.iterator();
            while (it.hasNext()) {
                FileItem fileItem = (FileItem) it.next();
                //文件名
                String fileName = fileItem.getFieldName();
                if (fileName.equals("lgcorpcode")) {
                    //当前用户企业编码
                    lgcorpcode = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("lguserid")) {
                    //当前用户userid
                    userId = Long.valueOf(fileItem.getString("UTF-8").toString());
                } else if (fileName.equals("dtMsg")) {
                    //短信内容
                    dtMsg = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("draftFile")) {
                    selDraftFilePath = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("draftId")) {
                    draftid = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("taskname")) {
                    //标题
                    taskname = fileItem.getString("UTF-8").toString();

                } else if (!fileItem.isFormField()
                        && fileItem.getName().length() > 0) {

                    //此变量是为了区分上传多个文件时临时文件名称重复的问题
                    fileCount++;
                    //查找短信内容中的参数个数（如果是动态模板短信）
                    String eg = "#[pP]_[1-9][0-9]*#";
                    Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(dtMsg);
                    while (m.find()) {
                        String rstr = m.group();
                        rstr = rstr.toUpperCase();
                        String pc = rstr.substring(rstr.indexOf("#P_") + 3, rstr.lastIndexOf("#"));
                        int pci = Integer.parseInt(pc);
                        if (pci > templateCount) {
                            templateCount = pci;
                        }
                    }

                    //此变量是为了区分上传多个文件时临时文件名称重复的问题
                    fileCount++;

                    String fileCurName = fileItem.getName();
                    //仅为了输出日志的需要
                    filenamestr = fileCurName + "," + filenamestr;

                    //文件类型
                    String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();

                    //如果是zip文件
                    if (fileType.equals(".zip")) {
                        //判断压缩文件的大小
                        if (fileItem.getSize() > zipSize) {
                            fileItem.delete();
                            fileItem = null;

                            break;
                        }
                        //创建临时文件
                        String zipFileStr = url[0].replace(".txt", "_" + fileCount + ".zip");
                        zipUrl = new File(zipFileStr);
                        //将上传的文件写入临时文件
                        fileItem.write(zipUrl);
                        ZipFile zipFile = new ZipFile(zipUrl);
                        Enumeration zipEnum = zipFile.getEntries();
                        ZipEntry entry = null;
                        while (zipEnum.hasMoreElements()) {
                            //这个变量的作用是，如果压缩包里面有多个文件，就需要多个临时文件，另外删除临时文件也可以用到
                            fileIndex++;
                            //解压缩后的文件名的标签序号，用于删除文件时
                            String fileIndexStr = String.valueOf(fileIndex) + "_" + String.valueOf(fileCount);
                            entry = (ZipEntry) zipEnum.nextElement();
                            //处理txt文件
                            if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".txt")) {
                                InputStream instream = zipFile.getInputStream(entry);
                                String charset = get_charset(instream);
                                BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), charset));
                                if (charset.startsWith("UTF-")) {
                                    reader.read(new char[1]);
                                }
                                readerList.add(reader);
                            } else if (!entry.isDirectory() && (entry.getName().toLowerCase().endsWith(".xls") || entry.getName().toLowerCase().endsWith(".et"))) {
                                readerList.add(jxExcel(url[0], zipFile.getInputStream(entry), fileIndexStr, 1));

                            }
                            //如果是.xlsx文件
                            else if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".xlsx")) {
                                readerList.add(jxExcel(url[0], zipFile.getInputStream(entry), fileIndexStr, 2));
                            }
                        }
                    } else if (fileType.equals(".xls") || fileType.equals(".et")) {
                        readerList.add(jxExcel(url[0], fileItem.getInputStream(), "_" + String.valueOf(fileCount), 1));
                    } else if (fileType.equals(".xlsx")) {
                        readerList.add(jxExcel(url[0], fileItem.getInputStream(), "_" + String.valueOf(fileCount), 2));
                    } else {
                        //文件编码
                        String charset = "";
                        InputStream instream = fileItem.getInputStream();
                        try {
                            charset = get_charset(instream);
                        } catch (Exception e) {
                            //result=new ErrorCode().infoMap.get("EBFV004");
                            EmpExecutionContext.error("EBFV004");
                            EmpExecutionContext.error(e, "判断上传文件编码异常!");
                        }
                        //江流转换成对应编码格式
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(fileItem.getInputStream(), charset));
                        if (charset.startsWith("UTF-")) {
                            reader.read(new char[1]);
                        }
                        readerList.add(reader);
                    }
                }
            }


            String tmp;
            try {
                for (int r = 0; r < readerList.size(); r++) {
                    BufferedReader reader = readerList.get(r);
                    while ((tmp = reader.readLine()) != null) {
                        tmp = tmp.trim();
                        phoneList.add(tmp);
                    }
                    reader.close();
                }
            } catch (Exception e) {
                //错误码
                EmpExecutionContext.error("EBFV005");
                //删除文件
                txtfileutil.deleteFile(url[0]);
                //异常处理
                EmpExecutionContext.error(e, "解析文件内容异常!");
                throw e;
            } finally {
    			try{
    				IOUtils.closeReaders(getClass(), readerList);
    			}catch(IOException e){
    				EmpExecutionContext.error(e, "");
    			}
                //清内存
                readerList.clear();
                repeatList.clear();
                readerList = null;
                repeatList = null;
                if (zipUrl != null) {
                    if (!zipUrl.delete()) {
                        //doThing
                    }
                }
                String FileStr = url[0];
                String FileStrTemp;
                String fileStr2;
                File file = null;
                //删除临时文件
                for (int j = 0; j < fileCount; j++) {
                    //删除没有经过压缩的excel的临时文件
                    int b = j + 1;
                    fileStr2 = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp_" + b + ".txt";
                    file = new File(fileStr2);
                    if (file.exists()) {
                        if (!file.delete()) {
                            //.....
                        }
                    }
                    //删除压缩多个文件的临时文件
                    for (int i = 0; i < fileIndex; i++) {
                        int a = i + 1;
                        FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + a + "_" + b + ".txt";
                        file = new File(FileStrTemp);
                        if (file.exists()) {
                            if (!file.delete()) {
                                //.....
                            }
                        }
                    }
                }

            }
            // 0.处理草稿箱文件
            if (StringUtils.isNotBlank(selDraftFilePath)) {
                TxtFileUtil txtFileUtil = new TxtFileUtil();
                String webRoot = txtFileUtil.getWebRoot();
                File draftFile = new File(webRoot, selDraftFilePath);
                if (!draftFile.exists() && StaticValue.getISCLUSTER() == 1) {
                    CommonBiz comBiz = new CommonBiz();
                    String downloadRes = "error";
                    //最大尝试次数
                    int retryTime = 3;
                    while (!"success".equals(downloadRes) && retryTime-- > 0) {
                        downloadRes = comBiz.downloadFileFromFileCenter(selDraftFilePath);
                    }
                    if (!"success".equals(downloadRes)) {
                        EmpExecutionContext.error("动态网讯草稿箱文件从文件服务器下载失败。");
                    }
                }
                if (!draftFile.exists()) {
                    //草稿箱号码文件不存在
                    json.put("ok", "-1");
                    EmpExecutionContext.error("动态网讯未找到草稿箱发送文件！");
                    return;
                }
                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(new FileReader(draftFile));
                    String tmps = null;
                    while ((tmps = bufferedReader.readLine()) != null) {
                        phoneList.add(tmps.trim());
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error("动态网讯读取草稿箱发送文件异常！");
                    return;
                } finally {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                }
            }

            //草稿箱对象
            LfDrafts drafts = new LfDrafts();

            //写入草稿箱文件
            TxtFileUtil txtFileUtil = new TxtFileUtil();
            String physicsUrl = "";
            String webRoot = txtFileUtil.getWebRoot();
            String uploadPath = StaticValue.FILEDIRNAME + "drafttxt/";
            //构建年月日目录结构
            String dirPath = txtFileUtil.createDir(webRoot + uploadPath, Calendar.getInstance());
            GetSxCount sx = GetSxCount.getInstance();
            //保存文件名
            String saveName = "draft_" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(System.currentTimeMillis()) + sx.getCount() + ".txt";
            //每次保存生成新的草稿文件路径
            String newDraftFilePath = uploadPath + dirPath + saveName;
            //全路径
            physicsUrl = webRoot + newDraftFilePath;

            if (draftid == null || "".equals(draftid.trim())) {
                drafts.setCorpcode(lgcorpcode);
                drafts.setCreatetime(new Timestamp(System.currentTimeMillis()));
                //草稿类型，0－相同内容；1－不同内容动态模板；2－不同内容文件发送；3－客户群组群发；4-静态网讯发送；5－动态网讯发送
                drafts.setDraftstype(5);
                drafts.setUserid(userId);
                drafts.setMobileurl(newDraftFilePath);
            } else {
                drafts.setId(Long.parseLong(draftid));
                drafts.setMobileurl(newDraftFilePath);
            }


            //若文件存在 则清空内容 否则 生成新文件
            txtFileUtil.emptyTxtFile(physicsUrl);

            //号码文件全新写入
            String line = System.getProperties().getProperty(StaticValue.LINE_SEPARATOR);
            StringBuffer contentSb = new StringBuffer("");
            //统计号码数
            int count = 0;
            for (String phone : phoneList) {
                contentSb.append(phone + line);
                count++;
                //达到5000 写一次文件
                if (count % 5000 == 0) {
                    txtFileUtil.writeToTxtFile(physicsUrl, contentSb.toString());
                    contentSb.setLength(0);
                    count = 0;
                }
            }
            if (count > 0) {
                // 剩余的号码写文件
                txtFileUtil.writeToTxtFile(physicsUrl, contentSb.toString());
                contentSb.setLength(0);
            }

            //使用集群，将文件上传到文件服务器
            if (StaticValue.getISCLUSTER() == 1) {
                //上传文件到文件服务器
                CommonBiz comBiz = new CommonBiz();
                boolean upFileRes = false;
                //最大尝试次数
                int retryTime = 3;
                while (!upFileRes && retryTime-- > 0) {
                    upFileRes = comBiz.upFileToFileServer(newDraftFilePath);
                }
                if (!upFileRes) {
                    EmpExecutionContext.error("动态网讯草稿箱文件上传文件到服务器失败。错误码：" + IErrorCode.B20023);
                    return;
                }
            }

            boolean result = false;
            drafts.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            //若参数为空 则默认赋值一个空格 （oracle数据库兼容）
            drafts.setMsg(StringUtils.defaultIfEmpty(dtMsg, " "));
            drafts.setTitle(StringUtils.defaultIfEmpty(taskname, " "));

            // 主题：taskname 内容：msg 手机号文件路径：physicsUrl 时间
            if (draftid == null || "".equals(draftid.trim())) {
                Long id = baseBiz.addObjReturnId(drafts);
                if (id != null && id > 0) {
                    drafts.setId(id);
                    result = true;
                }
            } else {
                result = baseBiz.updateObj(drafts);
                if (!result) {
                    //草稿箱对应记录已删除
                    json.put("ok", "-2");
                    return;
                }
            }

            json.put("draftid", drafts.getId());
            json.put("ok", result ? "1" : "0");

//				txtfileutil.writeToTxtFile(url[0], contentSb.toString());

        } catch (Exception e) {
            EmpExecutionContext.error(e, "动态网讯暂存草稿处理异常！");
        } finally {
            request.setAttribute("result", json.toString());
            try {
                request.getRequestDispatcher(empRoot + "/sendWXDSM/wx_todraft.jsp").forward(request, response);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "动态网讯暂存草稿跳转异常！");
            }
        }

    }


    /**
     * 写日志
     *
     * @param request   请求对象
     * @param opModule  菜单名称
     * @param opContent 操作内容
     * @param opType
     */
    public void setLog(HttpServletRequest request, String opModule, String opContent, String opType) {
        try {
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), opContent, opType);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, opModule + opType + opContent + "日志写入异常");
        }
    }


}
