package com.montnets.emp.netnews.servlet;

import com.montnets.emp.common.atom.AddrBookAtom;
import com.montnets.emp.common.atom.SendSmsAtom;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sms.LfDrafts;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfOpratelog;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.biz.WXSendBiz;
import com.montnets.emp.netnews.common.AllUtil;
import com.montnets.emp.netnews.common.CompressEncodeing;
import com.montnets.emp.netnews.daoImpl.Wx_ueditorDaoImpl;
import com.montnets.emp.netnews.entity.LfDfadvanced;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.netnews.entity.LfWXTrustCols;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.*;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
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

public class wx_sendrSvt extends BaseServlet {


    /**
     *
     */
    private static final long serialVersionUID = -2489247682395829133L;

    //	LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
    private final BaseBiz baseBiz = new BaseBiz();
    final Wx_ueditorDaoImpl ueditorDao = new Wx_ueditorDaoImpl();
    final ResourceBundle bundle = ResourceBundle.getBundle("resourceBundle");
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
//	String uploadpath = bundle.getString("montnets.wx.filepath");


    final String empRoot = "ydwx";
    final SmsBiz smsBiz = new SmsBiz();
    private final WgMsgConfigBiz msgConfigBiz = new WgMsgConfigBiz();
    private final WXSendBiz wxSendBiz = new WXSendBiz();
    final BalanceLogBiz biz = new BalanceLogBiz();
    private final TxtFileUtil txtfileutil = new TxtFileUtil();
    private final AddrBookAtom addrBookAtom = new AddrBookAtom();
    private final PhoneUtil phoneUtil = new PhoneUtil();
    private final BlackListAtom blBiz = new BlackListAtom();
    final String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

    public void find(HttpServletRequest request, HttpServletResponse response) {
        String lgcorpcode = null;
        Long lguserid = null;
        try {
            //当前登录企业
            lgcorpcode = request.getParameter("lgcorpcode");
            //当前登录操作员id
            //漏洞修复 session里获取操作员信息
            if (request.getSession(false) == null || request.getSession(false).getAttribute("loginSysuser") == null) {
                lguserid = Long.parseLong(request.getParameter("lguserid"));
            } else {
                LfSysuser sysUser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
                lguserid = sysUser.getUserId();
            }

//			if(request.getParameter("lguserid")!=null)
//			{
//				lguserid = Long.valueOf(request.getParameter("lguserid"));
//			}else
            if (lguserid == null) {
                EmpExecutionContext.info("lguserid为空！");
                request.setAttribute("findresult", "-1");
                try {
                    request.getRequestDispatcher(empRoot + "/sendWX/wx_send.jsp").forward(request, response);
                    return;
                } catch (ServletException e1) {
                    EmpExecutionContext.error(e1, lguserid, lgcorpcode);
                    return;
                } catch (IOException e1) {
                    EmpExecutionContext.error(e1, lguserid, lgcorpcode);
                    return;
                }
            }

            //当前登录操作员对象
            LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userId", curSysuser.getUserName());
            //判断是否有审核流程
            List<LfFlow> flowList = baseBiz.getByCondition(LfFlow.class,
                    conditionMap, null);

            if (flowList != null && flowList.size() > 0) {
                request.setAttribute("isFlow", "true");
            } else {
                request.setAttribute("isFlow", "false");
            }

            LinkedHashMap<String, String> conditionbusMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
            conditionbusMap.put("corpCode&in", "0," + lgcorpcode);
            orconp.put("corpCode", "asc");

            //设置启用查询条件
            conditionbusMap.put("state", "0");
            //设置查询手动和手动+触发
            conditionbusMap.put("busType&in", "0,2");

            //根据企业编码查询业务类型
            List<LfBusManager> busList = baseBiz.getByCondition(
                    LfBusManager.class, conditionbusMap, orconp);

            request.setAttribute("busList", busList);

            conditionMap.clear();
            if (busList != null && busList.size() > 0) {

            }
            conditionMap.put("platFormType", "1");
            conditionMap.put("corpCode", lgcorpcode);
            conditionMap.put("isValidate", "1");
            // 区分帐号是否绑定业务bindType

            List<Userdata> spUserList = smsBiz.getSpUserList(curSysuser);
            request.setAttribute("sendUserList", spUserList);

            //------------------------------begin查询静态模板
            conditionMap.clear();
            //短信模板
            conditionMap.put("tmpType", "3");
            //有效
            conditionMap.put("tmState", "1");
            //无需审核或审核通过
            conditionMap.put("isPass&in", "0,1");
            //静态模板
            conditionMap.put("dsflag", "0");

            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            //模板id
            orderbyMap.put("tmid", "asc");
            //查询相同内容静态模板
            List<LfTemplate> tmpList = baseBiz.getByCondition(LfTemplate.class, lguserid, conditionMap, orderbyMap);
            if (tmpList != null && tmpList.size() > 0) {
                request.setAttribute("tmpList", tmpList);
            }
            LfDep dep = baseBiz.getById(LfDep.class, curSysuser.getDepId());
            if (dep != null) {
                request.setAttribute("depSign", dep.getDepName());
            }
            //产生taskId
            CommonBiz commonBiz = new CommonBiz();
            Long taskId = commonBiz.getAvailableTaskId();
            String opContent = "获取taskid(" + taskId + ")成功";
            setLog(request, "静态网讯发送", opContent, StaticValue.GET);
            request.setAttribute("taskId", taskId.toString());
            request.setAttribute("lguserid", lguserid);

            //获取高级设置默认信息
            conditionMap.clear();
            conditionMap.put("userid", String.valueOf(lguserid));
            //7：静态网讯发送
            conditionMap.put("flag", "7");
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("id", StaticValue.DESC);
            List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, conditionMap, orderMap);
            LfDfadvanced lfDfadvanced = null;
            if (lfDfadvancedList != null && lfDfadvancedList.size() > 0) {
                lfDfadvanced = lfDfadvancedList.get(0);
            }
            request.setAttribute("lfDfadvanced", lfDfadvanced);

            request.getRequestDispatcher(empRoot + "/sendWX/wx_send.jsp").forward(
                    request, response);

        } catch (Exception e) {
            EmpExecutionContext.error(e, lguserid, lgcorpcode);
            request.setAttribute("findresult", "-1");
            try {
                request.getRequestDispatcher(empRoot + "/sendWX/wx_send.jsp").forward(
                        request, response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1, lguserid, lgcorpcode);
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, lguserid, lgcorpcode);
            }
        }
    }


    // 用于页面异步数据的读取
    public void getLfTemplateBySms(HttpServletRequest request, HttpServletResponse response) {
        //查询条件vo
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        PageInfo pageInfo = new PageInfo();

        try {
            pageSet(pageInfo, request);
            //模板名称
            String tmName = request.getParameter("tmName");
            //模板内容
            String tmMsg = request.getParameter("tmMsg");
            //当前登录用户id
            String lgcorpcode = request.getParameter("lgcorpcode");
            //Long lguserid = Long.valueOf(request.getParameter("lguserid"));
            //漏洞修复 session里获取操作员信息
            Long lguserid = SysuserUtil.longLguserid(request);


            Integer pageSize = 5;
            if (StringUtils.isNotEmpty(request.getParameter("pageSize"))) {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
            }
            if (tmName != null) {
                tmName = tmName.trim();
            }
            if (tmMsg != null) {
                tmMsg = tmMsg.trim();
            }
            tmName = (tmName == null ? "" : tmName);
            tmMsg = (tmMsg == null ? "" : tmMsg);
            conditionMap.put("tmName&like", tmName);
            conditionMap.put("tmMsg&like", tmMsg);
            conditionMap.put("corpCode", lgcorpcode);
            conditionMap.put("tmpType", "3");
            conditionMap.put("tmState", "1");
            conditionMap.put("isPass&in", "0,1");
            conditionMap.put("dsflag", request.getParameter("dsflag"));

            pageSize = 5;
            //条件查询结果集
            pageInfo.setPageSize(pageSize);
            List<LfTemplate> temList = baseBiz.getByConditionNoCount(LfTemplate.class, lguserid, conditionMap, null, pageInfo);
            //获取当前用户管辖机构下的所有用户
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("temList", temList);
            request.getRequestDispatcher(empRoot + "/sendWX/wx_contentTemplate.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取当前用户机构下的所有用户网讯模板信息异常!");
        }
    }


    /**
     * 生成txt文件
     *
     * @param data
     * @param destFile
     * @return
     * @throws IOException
     */
    public boolean sortAndSave(List data, String destFile) {
        BufferedWriter writer = null;
        try {
            StringBuffer sb = new StringBuffer();
            writer = new BufferedWriter(new FileWriter(destFile));
            int dSize = data.size();
            for (int i = 0; i < dSize; i = i + 5) {
                int c = i;
                if (c + 5 <= dSize) {
                    c = c + 5;
                } else {
                    c = dSize;
                }
                for (int j = i; j < c; j++) {
                    sb.append(data.get(j));
                    sb.append("\n");
                }
                writer.write(sb.toString());
                sb.delete(0, sb.length());
            }

            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            EmpExecutionContext.error(e, "生成txt文件异常！");
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "生成txt文件异常！关闭流失败");
                }
            }
        }
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
     * 显示接收者信息
     *
     * @param netid
     * @param lguserid
     * @param request
     * @param taskId
     * @return
     */
    public String sendMsgInfo(String netid, String lguserid, HttpServletRequest request, String taskId) {
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
                    String w = CompressEncodeing.CompressNumber(pages.get(0).getID(), 6);
                    url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/w/" + w + "-";
                }
                String t = CompressEncodeing.CompressNumber(Long.valueOf(taskId), 6);
                url += t;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "拼接网讯地址接收者URL异常");
        }
        return url;
    }

    /**
     * 预览方法
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void upNumber(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = "";
        String phoneStr = "";
        String spUser = "";
        String depIdStr = "";
        String groupClient = "";
        //所选群组信息
        String groupStr = "";
        String busCode = "";
        //有效号码数
        Long effCount = 0l;
        //提交总数
        Long subCount = 0l;
        //不符合条件数
        Long badCount = 0l;
        //格式错误数
        Long badModeCount = 0l;
        //重复数
        Long repeatCount = 0l;
        //黑名单数据
        Long blackCount = 0l;
        //内容
        String msg = "";
        //贴尾
        String smsTail = "";
        //***该参数仅为了记录日志需要***
        StringBuffer opContent = new StringBuffer();
        SendSmsAtom smsAtom = new SendSmsAtom();
        String filenamestr = "";
        int fileCount_log = 0;
        long startTime = System.currentTimeMillis();
        //客户机构IDS
        String cliDepIds = "";
        //是否有预览号码权限
        TxtFileUtil txtfileutil = new TxtFileUtil();

        StringBuffer contentSb = new StringBuffer();
        StringBuffer badContentSb = new StringBuffer();
        //草稿箱文件相对路径
        String draftFilePath = "";
        //是否包含草稿文件
        String containDraft = null;
        boolean isOverSize = false;
        long zipSize = StaticValue.ZIP_SIZE;
        long maxSize = StaticValue.MAX_SIZE;
        long allFileSize = 0L;
        String strlguserid = null;
        Long lguserid = null;
        String lgcorpcode = "";
        String[] phones = new String[1];
        // 预览参数传递变量类
        PreviewParams preParams = new PreviewParams();
        try {
            String[] haoduan = msgConfigBiz.getHaoduan();
            String repurl = request.getScheme() + "://" + request.getServerName();
            //当前登录操作员id
            //strlguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            strlguserid = SysuserUtil.strLguserid(request);


            String netid = request.getParameter("netid");
            String taskId = request.getParameter("taskId");
            //生成网讯地址

//			String neturl = request.getScheme()+ "://"+ request.getServerName()+ ":"+ request.getServerPort()+ request.getContextPath()+ "/w/";
            //读取配置文件 may
            String neturl = SystemGlobals.getValue("wx.pageurl") + "/w/";
            String netInfo = wxSendBiz.sendMsgInfo(netid, taskId);
            if (netInfo == null || netInfo.trim().length() == 0) {

                EmpExecutionContext.error("获取网讯模板编码失败！");
                goPreview("error", request, response);
                return;
            }
            //注明：该字段是为了处理网讯转换回10进制处理pageid，taskid时候解密处理
            //解密长度随着这个两个参数的值变化而变化的
            int netInfoLeng = netInfo.length();
            neturl += netInfo;
            lguserid = Long.valueOf(strlguserid == null ? "0" : strlguserid);
            String[] url = txtfileutil.getSaveUrl(lguserid);
            File zipUrl = null;
            HashSet<String> repeatList = new HashSet<String>();

            HashSet<Long> validPhone = new HashSet<Long>();
            DiskFileItemFactory factory = new DiskFileItemFactory();
            File Newfile = null;
            factory.setSizeThreshold(1024 * 1024);
            String temp = url[0].substring(0, url[0].lastIndexOf("/"));
            factory.setRepository(new File(temp));
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> fileList = null;
            try {
                //以文件方式解析表单
                fileList = upload.parseRequest(request);
            } catch (FileUploadException e) {
                EmpExecutionContext.error(e, lguserid, lgcorpcode);
                throw e;
            }

            Iterator<FileItem> it = fileList.iterator();
            List<BufferedReader> readerList = new ArrayList<BufferedReader>();
            FileItem fileItem = null;

//---------------------------------------------------------------------------------------------------
            int fileIndex = 0;
            int fileCount = 0;
            while (it.hasNext()) {
                fileItem = (FileItem) it.next();
                String fileName = fileItem.getFieldName();
                if (fileName.equals("phoneStr")) {
                    //电话号码
                    phoneStr = fileItem.getString("UTF-8").toString();
                    if (",".equals(phoneStr)) {//出现只有","号 的表示没有号码
                        phoneStr = "";
                    } else if (phoneStr.indexOf(",,") > -1) {//去掉号码为空的
                        phoneStr = phoneStr.replace(",,", ",");
                    }

                } else if (fileName.equals("empDepIds")) {
                    //机构id
                    depIdStr = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("cliDepIds")) {
                    //客户机构ids
                    cliDepIds = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("groupClient")) {
                    //群组id
                    groupClient = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("groupIds")) {
                    //群组id
                    groupStr = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("spUser1")) {
                    //发送账号
                    spUser = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("busCode1")) {
                    //业务类型
                    busCode = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("smsTail")) {
                    //贴尾内容
                    smsTail = fileItem.getString("UTF-8").toString().replace("\r\n", " ");
                } else if (fileName.equals("msg")) {
                    //内容
                    msg = fileItem.getString("UTF-8").toString().replace("\r\n", " ");
                    //替换短信内的特殊字符
                    msg = smsBiz.smsContentFilter(msg);
                } else if (fileName.equals("lguserid")) {
                    //当前用户的userid
                    lguserid = Long.parseLong(fileItem.getString("UTF-8").toString());
                } else if (fileName.equals("lgcorpcode")) {
                    //企业编码
                    lgcorpcode = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("containDraft")) {
                    containDraft = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("draftFile")) {
                    draftFilePath = fileItem.getString("UTF-8").toString();

                }
                //上传号码文件
                else if (!fileItem.isFormField() && fileItem.getName().length() > 0) {
                    //判断所有上传文件大小，太大的话就不允许发送
                    allFileSize += fileItem.getSize();
                    if (allFileSize > maxSize) {
                        fileItem.delete();
                        fileItem = null;
                        isOverSize = true;
                        break;
                    }


                    //此变量是为了区分上传多个文件时临时文件名称重复的问题
                    fileCount++;

                    String fileCurName = fileItem.getName();
                    //仅为了输出日志的需要
                    filenamestr = fileCurName + "," + filenamestr;
                    fileCount_log = fileCount_log + 1;

                    String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();
                    //检验文件类型的合法性
                    if (!fileType.equals(".rar") && !fileType.equals(".zip") && !fileType.equals(".xls")
                            && !fileType.equals(".et") && !fileType.equals(".xlsx") && !fileType.equals(".txt")) {
                        // 文件类型不合法
                        EmpExecutionContext.error("相同内容预览，文件上传失败，文件类型不合法。userId：" + lguserid + "，errCode:" + ErrorCodeInfo.V10003);
                        throw new EMPException(ErrorCodeInfo.V10003);
                    }
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
                                readerList.add(jxExcel(url[0], Newfile, zipFile.getInputStream(entry), fileIndexStr, 1));
                            } else if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".xlsx")) {
                                readerList.add(jxExcel(url[0], Newfile, zipFile.getInputStream(entry), fileIndexStr, 2));
                            }
                        }
                    } else if (fileType.equals(".xls") || fileType.equals(".et")) {
                        //如果上传文件是excel2003或者wps表格
                        String FileStr = url[0];
                        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + "_" + fileCount + ".txt";
                        Newfile = new File(FileStrTemp);
                        FileOutputStream fos = new FileOutputStream(Newfile);
                        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
                        BufferedWriter bw = new BufferedWriter(osw);
                        String phoneNum = "";
                        String Context = "";
                        try {
                            HSSFWorkbook workbook = new HSSFWorkbook(fileItem.getInputStream());
                            //循环每张表
                            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                                HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                                // 循环每一行
                                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                    HSSFRow row = sheet.getRow(rowNum);
                                    if (row == null) {
                                        continue;
                                    }
                                    //得到第一列的电话号码
                                    phoneNum = getCellFormatValue(row.getCell(0));
                                    Context = "";
                                    //循环每一列（内容以,分隔开来）
                                    for (int k = 1; k < row.getLastCellNum(); k++) {
                                        HSSFCell cell = row.getCell(k);
                                        if (cell != null && cell.toString().length() > 0) {
                                            Context += "," + getCellFormatValue(cell);
                                        }

                                    }
                                    //一行一行的将内容写入到txt文件中。
                                    bw.write(phoneNum + Context + line);
                                }
                            }
                            bw.close();
                            osw.close();
                            fos.close();
                            FileInputStream fis = new FileInputStream(Newfile);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));
                            readerList.add(br);
                        } catch (FileNotFoundException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        }
                    } else if (fileType.equals(".xlsx")) {
                        String FileStr = url[0];
                        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + "_" + fileCount + ".txt";
                        Newfile = new File(FileStrTemp);
                        FileOutputStream fos = new FileOutputStream(Newfile);
                        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
                        BufferedWriter bw = new BufferedWriter(osw);
                        String phoneNum = "";
                        String Context = "";
                        try {
                            XSSFWorkbook workbook = new XSSFWorkbook(fileItem.getInputStream());
                            //循环每张表
                            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                                XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                                // 循环每一行
                                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                    XSSFRow row = sheet.getRow(rowNum);
                                    if (row == null) {
                                        continue;
                                    }
                                    phoneNum = getCellFormatValue(row.getCell(0));
                                    Context = "";
                                    for (int k = 1; k < row.getLastCellNum(); k++) {
                                        XSSFCell cell = row.getCell(k);
                                        if (cell != null && cell.toString().length() > 0) {
                                            Context += "," + getCellFormatValue(cell);
                                        }
                                    }
                                    bw.write(phoneNum + Context + line);
                                }
                            }
                            bw.close();
                            osw.close();
                            fos.close();

                            FileInputStream fis = new FileInputStream(Newfile);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));
                            readerList.add(br);
                        } catch (FileNotFoundException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        }
                    }// 读取zip压缩文件流
                    else if (fileType.equals(".rar")) {
                        String FileStr = url[0];
                        preParams.setPhoneFilePath(url);
                        readerList.addAll(smsAtom.parseRar(fileItem, FileStr, fileCount, preParams));
                    } else {
                        InputStream instream = fileItem.getInputStream();
                        String charset = get_charset(instream);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(), charset));
                        if (charset.startsWith("UTF-")) {
                            reader.read(new char[1]);
                        }
                        readerList.add(reader);
                    }
                }
            }// 循环结束
            //-----------------------------------------------------------------------------------------------
            LfSysuser curSysuser = new LfSysuser();
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                curSysuser = (LfSysuser) loginSysuserObj;
            } else {
                curSysuser = baseBiz.getLfSysuserByUserId(lguserid);//当前登录操作员对象
            }

            //检查发送比较SP账号是否属于登录企业（防止攻击）
            boolean checkFlag = new CheckUtil().checkSysuserInCorp(curSysuser, lgcorpcode, spUser, null);
            if (!checkFlag) {
                EmpExecutionContext.error("静态网讯发送预览时，检查操作员和发送账号是否是当前企业下，checkFlag:" + checkFlag
                        + "，userid:" + curSysuser.getUserId()
                        + "，spuser:" + spUser);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(MessageUtils.extractMessage("common", "common_empLangName", request));
                result = info.getErrorInfo(IErrorCode.B20007);
                return;
            }


            //增加贴尾内容
            if (!"".equals(smsTail)) {
                msg = msg + smsTail.trim();
            }
            //解析号码文件
            String tmp;
            int mid = 0;
            //号码返回状态
            int resultStatus = 0;
            BufferedReader reader;
            try {
                for (int r = 0; r < readerList.size(); r++) {
                    //如果上传号码大于500w就不允许发送（现在就是1500W）
                    if (effCount > StaticValue.MAX_PHONE_NUM) {
                        break;
                    }
                    reader = readerList.get(r);
                    while ((tmp = reader.readLine()) != null) {

                        subCount++;
                        mid = 0;
                        tmp = tmp.trim();
                        if (phoneUtil.getPhoneType(tmp, haoduan) == -1) {
                            //检查号段
                            mid = 1;
                        } else if (blBiz.checkBlackList(lgcorpcode, tmp, busCode)) {
                            //检查是否是黑名单
                            mid = 2;
                        } else if ((resultStatus = phoneUtil.checkRepeat(tmp, validPhone)) != 0) {
                            //返回1为重复号码
                            if (resultStatus == 1) {
                                mid = 3;
                            }
                            //返回-1为非法号码
                            else {
                                mid = 1;
                            }
                        } else {

                            //通过截取部分传输过去后，再做处理
                            String tempPhone = tmp;
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
                            if ("1".equals(TelPhone.trim())) {
                                // 手机号 要替换URL的部分URL 被替换的URL\
                                contentSb.append(tmp).append(",").append(msg).append(" ").append(UrlRep(tmp, neturl, repurl)).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                                //p_list.add(phone + "," + textSMS + " "+ UrlRep(phone, url, repurl)+ CompressEncodeing.JieMPhone(phone) + "  .");
                            } else {
                                //p_list.add(phone + "," + textSMS + " " + url+ CompressEncodeing.JieMPhone(phone) + "  .");
                                contentSb.append(tmp).append(",").append(msg).append(" ").append(neturl).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                            }
                            effCount++;

                        }

                        switch (mid) {
                            case 1:
                                /*格式非法：*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(tmp).append(line);
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
                        }
                        //一千条存贮一次
                        if (effCount % 10000 == 0 && effCount > 10000) {
                            txtfileutil.writeToTxtFile(url[0], contentSb
                                    .toString());
                            contentSb = null;
                            contentSb = new StringBuffer();
                        }
                        if (badCount % 1000 == 0 && badCount > 1000) {

                            txtfileutil.writeToTxtFile(url[2], badContentSb
                                    .toString());
                            badContentSb = new StringBuffer();
                        }
                    }

                    reader.close();
                }
            } catch (Exception e) {
                txtfileutil.deleteFile(url[0]);
                EmpExecutionContext.error(e, lguserid, lgcorpcode);
                throw e;
            } finally {
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
                    }
                    ;
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
                        }
                    }
                    //删除压缩多个文件的临时文件
                    for (int i = 0; i < fileIndex; i++) {
                        int a = i + 1;
                        FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + a + "_" + b + ".txt";
                        file = new File(FileStrTemp);
                        if (file.exists()) {
                            if (!file.delete()) {
                            }
                        }
                    }
                }

                if (Newfile != null) {
                    if (!Newfile.delete()) {
                    }
                }
            }
//=========================================================================================

            if (isOverSize) {
                result = "overSize";
            } else if (effCount > StaticValue.MAX_PHONE_NUM) {
                for (int i = 0; i < url.length; i++) {
                    txtfileutil.deleteFile(url[i]);
                }
                result = "overstep";
            } else {
                //解析员工通讯录号码字符串
                if (depIdStr.length() > 0) {
                    //通过机构id查找电话
                    phoneStr = phoneStr + getEmpByDepId2(depIdStr, lgcorpcode);
                    String tempdepIdStr = depIdStr.substring(0, depIdStr.length() - 1);
                    //有的时候是e+id号,去掉
                    if (tempdepIdStr.indexOf("e") > -1) {
                        tempdepIdStr = tempdepIdStr.replaceAll("e", "");
                    }
                    /*员工通讯录*/
                    opContent.append(MessageUtils.extractMessage("ydwx", "group_ydbg_xzqz_text_memberaddbook", request) + " id:" + tempdepIdStr).append("，");

                }
                //解析客户通讯录号码字符串
                if (!StringUtils.isEmpty(cliDepIds)) {
                    //通过客户ID值查询客户电话号码
                    phoneStr = wxSendBiz.getClientPhoneStrByDepId(phoneStr, cliDepIds, lgcorpcode);
                    String tempcliDepIds = cliDepIds.substring(0, cliDepIds.length() - 1);
                    //有的时候是e+id号,去掉
                    if (tempcliDepIds.indexOf("e") > -1) {
                        tempcliDepIds = tempcliDepIds.replaceAll("e", "");
                    }
                    /*客户通讯录*/
                    opContent.append(MessageUtils.extractMessage("ydwx", "dxkf_ydkf_xzfsdx_text_khtxl", request) + " id:" + tempcliDepIds).append("，");

                }
                if (groupStr.length() > 0 || groupClient.length() > 0) {
                    //通过群组查找电话
                    phoneStr = phoneStr + getEmpByGroupStr(groupStr, groupClient);
                    if (groupClient.length() > 0) {
                        String tempgroupClient = groupClient.substring(0, groupClient.length() - 1);
                        /*客户群组*/
                        opContent.append(MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_3", request) + " id:" + tempgroupClient).append("，");
                    }
                    if (groupStr.length() > 0) {
                        String tempgroupStr = groupStr.substring(0, groupStr.length() - 1);
                        /*员工群组*/
                        opContent.append(MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_4", request) + " id:" + tempgroupStr).append("，");
                    }

                }


                String draft = "";
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
                            EmpExecutionContext.error("静态网讯草稿箱文件从文件服务器下载失败。");
                        }
                    }
                    if (!draftFile.exists()) {
                        EmpExecutionContext.error("静态网讯未找到草稿箱发送文件！");
                        result = "error";
                        return;
                    }
                    BufferedReader bufferedReader = null;
                    try {
                        bufferedReader = new BufferedReader(new FileReader(draftFile));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            draft = line.trim() + "," + draft;
                        }
                    } catch (Exception e) {
                        EmpExecutionContext.error("静态网讯读取草稿箱发送文件异常！");
                        result = "error";
                        return;
                    } finally {
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    }
                }
                if (!"".equals(draft)) {
                    phoneStr = phoneStr + draft;
                }
                if (phoneStr.length() > 0) {
                    phones = phoneStr.split(",");
                    for (String num : phones) {
                        subCount++;
                        if (num != null) {
                            num = num.trim();
                        }
                        if (num != null && num.length() > 6 && num.length() < 22) {
                            if (phoneUtil.getPhoneType(num, haoduan) == -1) {
                                //过滤号段
                                /*格式非法：*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num)
                                        .append(line);
                                badModeCount++;
                                badCount++;
                                continue;
                            } else if (blBiz.checkBlackList(lgcorpcode, num, busCode)) {
                                /*黑名单号码：*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(num).append(line);
                                blackCount++;
                                badCount++;
                                continue;
                            } else if ((resultStatus = phoneUtil.checkRepeat(num, validPhone)) != 0) {
                                //返回1为重复号码
                                if (resultStatus == 1) {
                                    /*重复号码：*/
                                    badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code3", request)).append(num).append(line);
                                    repeatCount++;
                                    badCount++;
                                    continue;
                                }
                                //返回-1为非法号码
                                else {
                                    /*格式非法：*/
                                    badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num)
                                            .append(line);
                                    badModeCount++;
                                    badCount++;
                                    continue;
                                }
                            }
                            //通过截取部分传输过去后，再做处理
                            String tempPhone = num;
                            String regx = "";//如果不符合这两种特殊的情况就不做处理
                            if (tempPhone.length() > 2) {
                                if (tempPhone.indexOf("+") > -1) {  //（*0表示加号开头,*1表示00开头）
                                    String first = tempPhone.substring(1, 2);//取加号后面一位数，避免数字太长了，导致转换不了
                                    tempPhone = tempPhone.substring(2);
                                    regx = "*0" + first + netInfoLeng;//组装之后的字符串
                                } else if (tempPhone.indexOf("00") > -1) { //（*0表示加号开头,*1表示00开头）
                                    String first = tempPhone.substring(0, 2);//取加号后面一位数，避免数字太长了，导致转换不了
                                    if ("00".equals(first)) {
                                        tempPhone = tempPhone.substring(2);
                                        regx = "*1" + netInfoLeng;//组装之后的字符串
                                    }
                                }
                            }
                            if ("1".equals(TelPhone.trim())) {
                                // 手机号 要替换URL的部分URL 被替换的URL\
                                contentSb.append(num).append(",").append(msg).append(" ").append(UrlRep(num, neturl, repurl)).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                            } else {
                                contentSb.append(num).append(",").append(msg).append(" ").append(neturl).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                            }

                            effCount++;

                        } else if (num.length() < 7 || num.length() > 21) {
                            /*格式非法：*/
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num).append(line);
                            badModeCount++;
                            badCount++;
                            continue;
                        }
                    }
                }
                txtfileutil.writeToTxtFile(url[0], contentSb.toString());
                //页面增加余额判断
                Long maxcount = 0L;
                int yct = 0;
                boolean isCharg = true; //如果启用了计费则为true;未启用则为false;
                String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());


                try {
                    yct = smsBiz.countAllOprSmsNumber(spUser,
                            msg, 1, url[1], null);
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "网讯预览计算短信条数异常");
                    yct = 0;
                }
                //机构、SP账号检查余额标识，只要有其一个失败，则为false，之后的扣费将不再执行
                boolean isAllCharge = true;
                //否需要启用机构计费
                boolean IsChargings = biz.IsChargings(lguserid);
                if (IsChargings) {
                    EmpExecutionContext.info("date:" + dateStr + "    lfsysuser.corpCode:" + curSysuser.getCorpCode() + ";lfCorp.corpCode:" + lgcorpcode);
                    //提供一个可获取最大可发送条数的方法.
                    maxcount = biz.getAllowSmsAmount(curSysuser);
                    EmpExecutionContext.info("date:" + dateStr + "     余额:" + maxcount);
                    if (maxcount == null) {
                        maxcount = 0L;
                    }
                    //机构余额小于发送条数
                    if (maxcount - yct < 0) {
                        //设置检查标识为失败
                        isAllCharge = false;
                        EmpExecutionContext.error("静态网讯预览，机构余额小于发送条数，taskid:" + taskId
                                + "，corpCode:" + lgcorpcode
                                + "，userid：" + strlguserid
                                + "，depFeeCount:" + maxcount
                                + "，preSendCount" + yct);
                    }
                } else {
                    isCharg = false;
                }


                EmpExecutionContext.info("date:" + dateStr + "     预发送条数:" + yct);
                if (badContentSb.length() > 0) {
                    txtfileutil.writeToTxtFile(url[2], badContentSb.toString());
                }

                if (filenamestr.length() > 0) {
                    opContent.append("文件数：" + fileCount_log + "，文件名：" + filenamestr);
                }


                // SP账号余额
                Long spUserAmount = -1L;
                Long feeFlag = 2L;
                if (isAllCharge) {
                    //获取SP账号类型
                    feeFlag = biz.getSpUserFeeFlag(spUser, 1);
                    if (feeFlag == null || feeFlag < 0) {
                        EmpExecutionContext.error("静态网讯预览， 获取SP账号计费类型出现异常！spUser=" + spUser);
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

                        EmpExecutionContext.info("静态网讯预览， SP账号计费类型为预付费,spUser=" + spUser);
                    } else if (feeFlag == 2) {
                        EmpExecutionContext.info("静态网讯预览， SP账号计费类型为后付费,spUser=" + spUser);
                    }
                }

                String spFeeResult = "";
                //运营商扣费判断
                if (isAllCharge) {
                    try {
                        spFeeResult = biz.checkGwFee(spUser, yct, lgcorpcode, 1);
                    } catch (Exception ex) {
                        result = "error";
                        EmpExecutionContext.error(ex, lguserid, lgcorpcode);
                    }
                }

                // ****增加预览时候，详细信息*****
                //格式化时间
                SimpleDateFormat sdfdate = new SimpleDateFormat("HH:mm:ss");
                String info = "预览S：" + sdfdate.format(startTime) + "，耗时：" + (System.currentTimeMillis() - startTime) + "ms，提交总数：" + subCount + "，有效数：" + effCount + "，";
                setLog(request, "静态网讯发送", info + opContent.toString() + " taskId：" + taskId, StaticValue.GET);

                if (subCount - 0 == 0) {
                    result = "noPhone";
                } else {
                    result = url[1] + "&" + String.valueOf(subCount)
                            + "&" + String.valueOf(effCount)
                            + "&" + String.valueOf(badModeCount)
                            + "&" + String.valueOf(repeatCount)
                            + "&" + String.valueOf(blackCount)
                            + "&" + url[1]
                            + "&" + String.valueOf(yct)
                            + "&" + String.valueOf(maxcount)
                            + "&" + String.valueOf(url[4]) + "&" + String.valueOf(isCharg) + "&" + spFeeResult + "&" + spUserAmount + "&" + feeFlag;
                }
            }
        } catch (Exception ex) {
            result = "error";
            EmpExecutionContext.error(ex, lguserid, lgcorpcode);
        } finally {
            goPreview(result, request, response);
        }
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
            request.setAttribute("result", result);
            request.getRequestDispatcher(empRoot + "/sendWX/wx_preview.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "静态网讯跳转预览页面异常!");
        }
    }

    //预览之后提交
    public void add(HttpServletRequest request, HttpServletResponse response) {
        SmsSendBiz cfsb = new SmsSendBiz();
        SuperOpLog spLog = new SuperOpLog();
        String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
        LfOpratelog lfoplog = null;
        //模块
        String opModule = StaticValue.SMS_BOX;
        //操作员
        String opSper = StaticValue.OPSPER;
        //操作类型
        String opType = StaticValue.ADD;
        //内容
        String opContent = "静态网讯发送任务";
        //任务id
        String taskId = request.getParameter("taskId");
        EmpExecutionContext.info(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " 静态网讯发送获取taskid：" + taskId);
        //发送账号
        String spUser = request.getParameter("spUser");
        //任务主题
        String title = request.getParameter("taskname");
        //信息内容
        String msg = request.getParameter("msg");
        //贴尾内容
        String smsTail = request.getParameter("smsTail");
        if (smsTail != null && !"".equals(smsTail)) {
            msg = msg + smsTail.trim();
        }
        //是否定时
        String timerStatuss = request.getParameter("timerStatus");
        Integer timerStatus1 = (timerStatuss == null || "".equals(timerStatuss)) ? 0 : Integer.valueOf(timerStatuss);
        //定时时间
        String timerTime = request.getParameter("timerTime");
        //提交状态
        Integer subState = Integer.valueOf("2");

        String preStr = request.getParameter("preStr");
        //当前登录操作员id
        //String strlguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String strlguserid = SysuserUtil.strLguserid(request);

        Long lguserid = null;
        //当前登录企业
        String lgcorpcode = request.getParameter("lgcorpcode");
        String busCode = request.getParameter("busCode");
        String[] preStrArr = preStr.split("&");
        opContent = opContent + "（任务名称：" + title + "）";
        String result = "";


        //检查发送比较SP账号是否属于登录企业（防止攻击）
        LfSysuser curSysuser = new LfSysuser();
        Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
        if (loginSysuserObj != null) {
            curSysuser = (LfSysuser) loginSysuserObj;
        } else {
            try {
                curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
            } catch (Exception e) {
                EmpExecutionContext.error("获取当前登录人异常，" + "lguserid:" + lguserid);
            }//当前登录操作员对象
        }
        boolean checkFlag = new CheckUtil().checkSysuserInCorp(curSysuser, lgcorpcode, spUser, null);
        if (!checkFlag) {
            EmpExecutionContext.error("静态网讯发送时，检查操作员和发送账号是否是当前企业下，checkFlag:" + checkFlag
                    + "，userid:" + curSysuser.getUserId()
                    + "，spuser:" + spUser);
            result = "error";
            goFind(result, lguserid, lgcorpcode, taskId, request, response);
            return;
        }
        LfMttask mttask = new LfMttask();
        try {
            lguserid = Long.valueOf(strlguserid);
            //主题为默认时,直接返回(防止重发)
            if (title != null && "不作为短信内容发送".equals(title.trim())) {
                EmpExecutionContext.error("静态网讯发送获取参数异常，" + "title:" + title + "，taskId：" + taskId);
                result = ErrorCodeInfo.getInstance(langName).getErrorInfo(IErrorCode.V10001);
                //跳转
                goFind(result, lguserid, lgcorpcode, taskId, request, response);
                return;
            }
            //初始化任务对象
            mttask.setSubCount(Long.valueOf(preStrArr[1]));
            mttask.setEffCount(Long.valueOf(preStrArr[2]));
            mttask.setMobileUrl(preStrArr[6]);

            mttask.setTitle(title);
            mttask.setSpUser(spUser);
            mttask.setBmtType(2);
            mttask.setTimerStatus(timerStatus1);
            mttask.setMsgType(2);
            //网讯发送
            mttask.setMsType(6);
            mttask.setSubState(subState);
            mttask.setBusCode(busCode);//默认业务类型
            mttask.setMsg(msg);
            mttask.setSendstate(0);
            mttask.setCorpCode(lgcorpcode);
            mttask.setSendLevel(0);//发送优先级 默认0系统智能控制
            mttask.setIsReply(0);//默认不需要回复
            mttask.setTaskId(Long.valueOf(taskId));
            String netid = request.getParameter("netid");
            //网讯模板
            mttask.setTempid(Long.valueOf(netid));
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, strlguserid, lgcorpcode);

            lfoplog = new LfOpratelog();
            lfoplog.setOpUser(spUser);
            lfoplog.setOpModule(opModule);
            lfoplog.setOpAction(opType);
            lfoplog.setOpContent(opContent + opSper);
            lfoplog.setCorpCode(lgcorpcode);
            //记录失败操作日志
            logFail(lfoplog, e);
            //跳转
            goFind(result, lguserid, lgcorpcode, taskId, request, response);
            return;
        }

        //将预发送条数插入icount
        int icount = 0;
        try {
            icount = smsBiz.countAllOprSmsNumber(mttask.getSpUser(),
                    mttask.getMsg(), mttask.getMsgType() > 1 ? 2 : 1, mttask.getMobileUrl(), null);
        } catch (Exception e) {
            icount = 0;
            EmpExecutionContext.error(e, strlguserid, lgcorpcode);
        }

        mttask.setIcount(String.valueOf(icount));

        try {
            //定时任务
            if (timerStatus1 == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timerTime = timerTime + ":00";
                mttask.setTimerTime(new Timestamp(sdf.parse(timerTime).getTime()));
            } else {
                //非定时任务
                mttask.setTimerTime(mttask.getSubmitTime());
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("mobileUrl", mttask.getMobileUrl());
            List<LfMttask> mtLists = baseBiz.getByCondition(LfMttask.class, conditionMap, null);
            if (mtLists != null && mtLists.size() > 0) {
                result = "fileerror";
            }
            if ("".equals(result)) {
                boolean flag = new CheckUtil().checkSysuserInCorp(curSysuser, lgcorpcode, spUser, null);
                if (!flag) {
                    throw new Exception("操作员和发送账号检查不通过!");
                }
                mttask.setUserId(lguserid);
                //获取发送信息等缓存数据（是否计费、是否审核、用户编码）
                Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
                //执行发送任务
                result = cfsb.addSmsLfMttask(mttask, infoMap);
                String reultClong = result;

                //判断状态
                result = new WGStatus(langName).getInfoMap().get(result);
                //如果返回状态网关中未定义，则重置为之前状态
                if (result == null) {
                    result = reultClong;
                }

                //提交成功 删除当前草稿
                String draftId = request.getParameter("draftId");
                if (StringUtils.isNotBlank(draftId) && ("timerSuccess".equals(result) || "createSuccess".equals(result) || "000".equals(result))) {
                    baseBiz.deleteByIds(LfDrafts.class, draftId);
                }

                spLog.logSuccessString(spUser, opModule, opType, opContent, lgcorpcode);

                //增加操作日志
                if (loginSysuserObj != null) {
                    LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                    EmpExecutionContext.info("静态网讯发送", loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), "静态网讯发送。[任务名称,状态](" + title + "，" + result + "）", "OTHER");

                }
            }
        } catch (EMPException empex) {
            //捕获自定义异常
            ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
            //获取异常内容提示
            String desc = info.getErrorInfo(empex.getMessage());
            result = "empex" + desc;
            //失败日志
            spLog.logFailureString(spUser, opModule, opType, opContent + opSper, empex, lgcorpcode);
            EmpExecutionContext.error(empex, "执行静态网讯发送定时任务异常!");
        } catch (Exception e) {
//			if(e.getClass().getName().equals("org.apache.http.conn.HttpHostConnectException"))
//			{
            if (e.getClass().isAssignableFrom(HttpHostConnectException.class)) {
                result = e.getLocalizedMessage();

                //成功日志
                spLog.logSuccessString(spUser, opModule, opType, opContent, lgcorpcode);
            } else {
                result = "error";
                //失败日志
                spLog.logFailureString(spUser, opModule, opType, opContent + opSper, e, lgcorpcode);
            }

            EmpExecutionContext.error(e, strlguserid, lgcorpcode);
        } finally {
            goFind(result, lguserid, lgcorpcode, taskId, request, response);
        }
    }

    /**
     * 记录失败的操作日志
     *
     * @param oplog
     * @param eo
     */
    private void logFail(LfOpratelog oplog, Exception eo) {
        try {
            SuperOpLog spLog = new SuperOpLog();
            //失败日志
            spLog.logFailureString(oplog.getOpUser(), oplog.getOpModule(), oplog.getOpAction(), oplog.getOpContent(), eo, oplog.getCorpCode());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "记录日志异常!");
        }
    }

    private void goFind(String result, Long lguserid, String lgcorpcode, String taskId, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getSession(false).setAttribute("mcs_batchResult", result);
            String s = request.getRequestURI();
            s = s + "?method=find&lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode + "&oldtaskId=" + taskId;
            response.sendRedirect(s);
        } catch (Exception e) {
            EmpExecutionContext.error(e, lguserid, lgcorpcode);
        }
    }

    /**
     * wap互动信息
     *
     * @param request
     * @param response
     */
    public void interaction(HttpServletRequest request,
                            HttpServletResponse response) {
        try {
            String table = request.getParameter("table");

            String tablename = table.split("-")[0];
            String tableid = table.split("-")[1];

            String w = request.getParameter("w");
            String h = null, p = null;
            if (w != null && w.length() > 6) {
                Map<String, String> pdph = CompressEncodeing.UnJieM(w);
                p = pdph.get("p");
                h = pdph.get("h");
            }
            if (w != null && w.length() < 6) {
                p = CompressEncodeing.UnCompressNumber(w) + "";
                request.setAttribute("msg", "预览成功");
                request.setAttribute("w", CompressEncodeing.CompressNumber(Long
                        .valueOf(p), 6));

                request.getRequestDispatcher("/wx_info.jsp").forward(request,
                        response);
                return;
            }

            int re = -1;
            String msg = "";
            List<LfWXTrustCols> cols = ueditorDao.getInteraction_cols(tableid);
            // 获取互动动态数据
            StringBuffer inserName = new StringBuffer();
            StringBuffer inserValue = new StringBuffer();

            if (cols != null && cols.size() > 0) {
                inserName.append("insert into ");
                inserName.append(tablename);
                inserName.append(" ( C0_SHOUJIHAOMA , PAGEID ,DATE_TIME  ,");
                if (h == null) {
                    h = "未知";
                }
                if (p == null) {
                    p = "0";
                }
                inserValue.append("  values( '" + h + "' ,'" + p + "' , '"
                        + new Date() + "',");
                for (int i = 0; i < cols.size(); i++) {
                    String[] colsNames = request.getParameterValues(cols.get(i)
                            .getColName());
                    inserName.append(cols.get(i).getColName());

                    if (colsNames != null && colsNames.length > 0) {
                        String colsvalues = "";

                        for (int j = 0; j < colsNames.length; j++) {
                            // 得到互动数据
                            colsvalues = colsvalues + colsNames[j];
                            // 根据数据类型，验证数据是否合法
                            if (cols.get(i).getColType() == 1) {
                                if (!AllUtil.isNomberfolatNO(colsvalues)
                                        && !AllUtil.isNomberNO(colsvalues)) {
                                    re = 1;
                                    msg = "输入数字,格式有错。";
                                }
                            } else if (cols.get(i).getColType() == 2) {
                                if (!AllUtil.isDataNO(colsvalues)) {
                                    re = 2;
                                    msg = "输入日期,格式有错。";
                                }
                            }

                            if (j != colsNames.length - 1) {
                                colsvalues = colsvalues + ",";
                            }
                        }

                        inserValue.append(" '" + colsvalues + "' "); // 多个值时

                    } else {
                        inserValue.append("' '");
                    }
                    if (i != cols.size() - 1) {
                        inserName.append(" , ");
                        inserValue.append(" , ");
                    } else {
                        inserName.append(" ) ");
                        inserValue.append(" ) ");
                    }
                    if (re != -1) {
                        break;
                    }
                }

            }
            String sql = inserName.toString() + inserValue.toString();

            /**
             * @insertsql varchar(max),
             * @tablename varchar(50),
             * @phone varchar(15),
             * @mode varchar(2) output-- 0
             */
            // / 0：多次回复有效、1：首次回复有效、2：末次回复有效
            String mode = ueditorDao.getsaveSql_mode(sql, tablename, h);
            if (mode != null && "1".equals(mode.trim())) {
                msg = "首次回复有效,操作成功";
            } else if (mode != null && "2".equals(mode.trim())) {
                msg = "末次回复有效,操作成功";
            } else if (re == -1 && mode != null && "0".equals(mode.trim())) {
                int i = ueditorDao.getsaveInteraction_cols(sql);
                if (i > 0) {
                    msg = "多次回复有效,操作成功";
                }
            } else {
                msg = msg + "操作失败！！";
            }

            // phone = Base64.encode(phone.getBytes());
            request.setAttribute("msg", msg);
            request.setAttribute("w", CompressEncodeing.CompressNumber(Long
                    .valueOf(p), 6)
                    + CompressEncodeing.JieMPhone(h));
            request.getRequestDispatcher("/wx_info.jsp").forward(request,
                    response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "网讯查看互动处理异常!");
        }

    }


    /**
     * 获取网讯模板信息
     *
     * @param request
     * @param response
     */
    public void getTemplate(HttpServletRequest request,
                            HttpServletResponse response) {
        String output = new String();
        try {
            int sort = Integer.parseInt(request.getParameter("section"));
            int id = Integer.parseInt(request.getParameter("id"));
            output = ueditorDao.getTemplate(sort, id);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取网讯模板信息异常！");
        }
        response.setCharacterEncoding("utf-8");
        try {
            PrintWriter pw = response.getWriter();
            pw.println(output);
        } catch (IOException e1) {
            EmpExecutionContext.error(e1, "响应请求异常!");
        }
    }

    /****
     * 查询员工机构人员返回JSON对象
     * @param request
     * @param response
     */
    public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response) {
        try {
            String depId = request.getParameter("depId");
            String user = request.getParameter("userid");
            LfSysuser lfSysuser = getLoginUser(request);
            List<LfEmployeeDep> empDepList = wxSendBiz.getEmpSecondDepTreeByUserIdorDepId(user, depId, lfSysuser.getCorpCode());
            LfEmployeeDep dep = null;
            StringBuffer tree = new StringBuffer("");
            if (empDepList != null && empDepList.size() > 0) {
                tree.append("[");
                for (int i = 0; i < empDepList.size(); i++) {
                    dep = empDepList.get(i);
                    tree.append("{");
                    tree.append("id:'").append(dep.getDepId() + "'");
                    tree.append(",name:'").append(dep.getDepName()).append("'");
                    tree.append(",pId:'").append(dep.getParentId() + "'");
                    tree.append(",depcodethird:'").append(dep.getDepcodethird() + "'");
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != empDepList.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");
            }
            response.getWriter().print(tree.toString());
        } catch (Exception e) {
            try {
                response.getWriter().print("");
            } catch (IOException e1) {
                EmpExecutionContext.error(e, "异步调用异常！");
            }
            EmpExecutionContext.error(e, "查询员工机构人员异常!");
        }
    }


    //改成OA样式树,点击机构只列出该机构下的员工，不管该机构的子机构的员工
    public void getDepAndEmpTree1(HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        try {
            PageInfo pageinfo = new PageInfo();
            // 页码
            String pageindex = request.getParameter("pageIndex");
            if (pageindex != null && !"".equals(pageindex)) {
                pageinfo.setPageIndex(Integer.parseInt(pageindex) + 1);
            } else {
                pageinfo.setPageIndex(1);
            }
            // 每页显示50条记录
            pageinfo.setPageSize(50);
            String epname = "";
            //机构
            String depId = "";
            // 当前登录企业
            String lgcorpcode = "";
            try {
                epname = request.getParameter("epname");
                // 机构
                depId = request.getParameter("depId");
                // 当前登录企业
                lgcorpcode = request.getParameter("lgcorpcode");
                //查询机构下员工信息
                List<DynaBean> lfDepLoyeeList = new CommonBiz().getDepLoyee(epname, depId, lgcorpcode, pageinfo);
                // 生成html
                StringBuffer sb = new StringBuffer();
                if (lfDepLoyeeList != null && lfDepLoyeeList.size() > 0) {
                    for (DynaBean bean : lfDepLoyeeList) {
                        sb.append("<option value='").append("e_" + bean.get("employeeid").toString())
                                .append("' mobile='").append(bean.get("mobile").toString())
                                .append("'>").append(bean.get("name").toString())
                                .append(" [").append(bean.get("dep_name").toString()).append("]")
                                .append("</option>");
                    }
                }
                String pageStr = pageinfo.getTotalRec() + "@" + pageinfo.getTotalPage() + "@" + sb.toString();

                // 异步返回处理
                response.getWriter().print(pageStr);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "查询机构员工异常！depId:" + depId + "，epname：" + epname + "，lgcorpcode：" + lgcorpcode);
            }
//			String epname = request.getParameter("epname");
//			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//			String depId = request.getParameter("depId");
//			StringBuffer sb = new StringBuffer();
//			//当前登录企业
//			String lgcorpcode = request.getParameter("lgcorpcode");
//
//			//有机构则带上机构id
//			if(depId != null && !"".equals(depId.trim()))
//			{
//				conditionMap.put("depId", depId);
//			}
//
//			conditionMap.put("corpCode", lgcorpcode);
//			if(epname!=null&&!"".equals(epname.trim()))
//			{
//				conditionMap.put("name&like", epname);
//			}
//			//查询员工列表
//			List<LfEmployee> lfEmployeeList = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
//
//			//生成html
//			if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
//				//获取员工的机构id并存入集合，key为depId
//				Map<Long,String> depIdMap = new HashMap<Long,String>();
//				for (LfEmployee user : lfEmployeeList)
//				{
//					depIdMap.put(user.getDepId(), "");
//				}
//
//				StringBuffer bufDepId = new StringBuffer();
//				//循环遍历所有机构id，格式为id1,id2,id3
//				Set<Long> keys = depIdMap.keySet();
//		        for (Iterator<Long> it = keys.iterator(); it.hasNext();)
//		        {
//		        	bufDepId.append(it.next()).append(",");
//		        }
//
//		        String strDepId = null;
//		        if(bufDepId != null && bufDepId.length() != 0)
//		        {
//		        	//截掉多余的,号
//		        	strDepId = bufDepId.substring(0, bufDepId.length()-1);
//		        }
//
//		        //查询出机构对象
//		        conditionMap.clear();
//		        conditionMap.put("depId&in", strDepId);
//		        List<LfEmployeeDep> empDepsList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
//
//				for (LfEmployee user : lfEmployeeList)
//				{
//					sb.append("<option value='").append("e_"+user.getEmployeeId())
//					.append("' mobile='").append(user.getMobile()).append("'>")
//					.append(user.getName().trim());
//
//					for(LfEmployeeDep empDep : empDepsList)
//					{
//						if(user.getDepId().equals(empDep.getDepId()))
//						{
//							//带上机构名称
//							sb.append(" [").append(empDep.getDepName()).append("]");
//						}
//					}
//
//					sb.append("</option>");
//				}
//			}
//			//异步返回处理
//			response.getWriter().print(sb.toString());

        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询机构及子机构的员工信息异常!");
        }
    }

    //点击选择机构按钮的时候如果包含子机构则获取子机构集合
    public void getDep(HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        try {
            String depId = request.getParameter("depId");
            String depName = request.getParameter("depName");
            String depIdsExist = request.getParameter("depIdsExist");
            //当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");

            String[] depIds = depIdsExist.split(",");
            StringBuffer depIdsTemp = new StringBuffer();
            for (int i = 0; i < depIds.length; i++) {
                if (depIds[i].indexOf("e") > -1) {
                    depIdsTemp.append(depIds[i].substring(1) + ",");
                } else if (depIds[i].equals(depId)) {
                    response.getWriter().print("depExist");
                    return;
                }
            }
            //判断新添加的机构是不是已经添加的机构的子机构
            boolean result = isDepAcontainsDepB(depIdsTemp.toString(), depId, lgcorpcode);
            if (result) {
                response.getWriter().print("depExist");
                return;
            } else {
                response.getWriter().print("notExist");
                return;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询子机构集合异常!");
        }
    }

    //判断一个机构是否被包含在其它机构
    private boolean isDepAcontainsDepB(String depIdAs, String depIdB, String corpCode) {
        boolean result = false;
        List<LfEmployeeDep> lfEmployeeDepList = new ArrayList<LfEmployeeDep>();
        LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
        String[] depIdAsTemp = depIdAs.split(",");
        try {
            for (int a = 0; a < depIdAsTemp.length; a++) {
                if (depIdAsTemp[a] != null && !"".equals(depIdAsTemp[a])) {
                    /*lfEmployeeDepList = new GenericLfEmployeeVoDAO().findEmployeeDepsByDepId(corpCode,depIdAsTemp[a]);*/
                    lfEmployeeDepList = wxSendBiz.findEmpDepsByDepId(corpCode, depIdAsTemp[a]);
                    for (int i = 0; i < lfEmployeeDepList.size(); i++) {
                        depIdSet.add(lfEmployeeDepList.get(i).getDepId());
                    }
                }
            }
            result = depIdSet.contains(Long.valueOf(depIdB));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "判断当前机构是否被包含在其它机构异常!");
        }
        return result;
    }

    //判断选择的机构是否把其它已经选择的机构包含了
    public void isDepsContainedByDepB(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ismut = request.getParameter("ismut");
        String depId = request.getParameter("depId");
        if ("0".equals(ismut)) {
            String countttt = addrBookAtom.getEmployeeCountByDepId(depId);
            response.getWriter().print(countttt);
            return;
        }
        List<LfEmployeeDep> lfEmployeeDepList = new ArrayList<LfEmployeeDep>();
        LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
        String depIdsExist = request.getParameter("depIdsExist");
        String[] depIds = depIdsExist.split(",");
        List<String> list = Arrays.asList(depIds);
        //将已经存在的机构id放在一个list里面(如果前缀有e就去掉e放在depIdExistList里面)
        List<Long> depIdExistList = new ArrayList<Long>();
        for (int j = 0; j < depIds.length; j++) {
            if (depIds[j] != null && !"".equals(depIds[j])) {
                if (depIds[j].indexOf("e") > -1) {
                    if (!"".equals(depIds[j].substring(1))) {
                        depIdExistList.add(Long.valueOf(depIds[j].substring(1)));
                    }
                } else {
                    depIdExistList.add(Long.valueOf(depIds[j]));
                }

            }
        }
        //当前登录企业
        String lgcorpcode = request.getParameter("lgcorpcode");

        //查找出要添加的机构的所有子机构，放在一个set里面
        /*lfEmployeeDepList = new GenericLfEmployeeVoDAO().findEmployeeDepsByDepId(lgcorpcode,depId);*/
        lfEmployeeDepList = wxSendBiz.findEmpDepsByDepId(lgcorpcode, depId);
        List<Long> depIdListTemp = new ArrayList<Long>();
        for (int i = 0; i < lfEmployeeDepList.size(); i++) {
            depIdSet.add(lfEmployeeDepList.get(i).getDepId());
        }
        //遍历这个set，看看已经存在的机构是否包含在这个机构的子机构里面，如果包含的话，就重新生成一个option列表的字符串给select控件
        for (int a = 0; a < depIdExistList.size(); a++) {
            if (depIdSet.contains(depIdExistList.get(a))) {
                depIdListTemp.add(depIdExistList.get(a));
            }
        }
        //如果确实有几个已经存在的机构是要添加机构的子机构，那么就从新生成select的html
        String depids = depIdSet.toString();
        depids = depids.substring(1, depids.length() - 1);
        //计算机构人数
        String countttt = addrBookAtom.getEmployeeCountByDepId(depids);
        if (depIdListTemp.size() > 0) {
            String tempDeps = depIdListTemp.toString();
            tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
            response.getWriter().print(countttt + "," + tempDeps);
            return;
        }
        //如果没有包含关系
        else {
            response.getWriter().print("notContains" + "&" + countttt);
            return;
        }
    }

    /**
     * 获取群组信息
     *
     * @param request
     * @param response
     */
    public void getGroupList(HttpServletRequest request,
                             HttpServletResponse response) {
        //此方法获取群组列表
        StringBuffer buffer = new StringBuffer("");
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
        String employee = MessageUtils.extractMessage("common", "common_employee", request);
        String individual = MessageUtils.extractMessage("common", "common_individual", request);
        String shared = MessageUtils.extractMessage("common", "common_shared", request);
        String client = MessageUtils.extractMessage("common", "common_client", request);
        try {
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            String corpCode = lfSysuser.getCorpCode();
            //操作员userid
            //String userId =request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String userId = SysuserUtil.strLguserid(request);
            conditionMap.put("receiver", userId);
            conditionMap.put("gpAttribute", "0");
            orderByMap.put("udgName", "asc");

            //拼成html代码返回
            buffer.append("<select select-one name='groupList' id='groupList' " +
                    "size='15' style='height: 250px; width: 240px; border: 0;color: black;font-size: 12px;margin:-2px 0 0 -2px;'");
            buffer.append(" onclick='a()'>");

            //根据条件查询所有群组
            List<LfUdgroup> udgList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderByMap);
            if (udgList != null && udgList.size() > 0) {

                String udgIds = "";
                for (LfUdgroup udg : udgList) {
                    //udgIds += udg.getUdgId().toString()+",";
                    udgIds += udg.getGroupid().toString() + ",";
                }
                //获取群组id字符串
                udgIds = udgIds.substring(0, udgIds.length() - 1);
                //Map<String,String> countMap = addrBookAtom.getEmployeeCount(udgIds);
                Map<String, String> countMap = new GroupBiz().getGroupMemberCount(udgIds, 1, corpCode);
                for (LfUdgroup udg : udgList) {
                    String mcount = countMap.get(udg.getGroupid().toString());
                    mcount = mcount == null ? "0" : mcount;
                    buffer.append("<option mcount='").append(mcount).append("' gtype='1'").append(" value='").append(udg.getUdgId()).append("'>");
                    buffer.append(udg.getUdgName().replace("<", "&lt;").replace(">", "&gt;")).append(udg.getSharetype() == 0 ? "[" + employee + "/" + individual + "]" : "[" + employee + "/" + shared + "]").append("</option>");
                }
            }

            //将群组列表设为null
            udgList = null;
            //查询出客户群组
            conditionMap.clear();
            //conditionMap.put("userId",userId);
            //登录的用户ID
            conditionMap.put("receiver", userId);
            conditionMap.put("gpAttribute", "1");
            udgList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderByMap);
            if (udgList != null && udgList.size() > 0) {
                String udgIds = "";
                for (LfUdgroup udg : udgList) {
                    //udgIds += udg.getUdgId().toString()+",";
                    udgIds += udg.getGroupid().toString() + ",";
                }
                //获取群组id字符串
                udgIds = udgIds.substring(0, udgIds.length() - 1);
                //Map<String,String> countMap1 = wxSendBiz.getGroupCount(udgIds, "2");
                Map<String, String> countMap1 = new GroupBiz().getGroupMemberCount(udgIds, 2, corpCode);
                for (LfUdgroup lfUdgroup : udgList) {
                    String shareType = "0";
                    if (lfUdgroup.getSharetype() != null && lfUdgroup.getSharetype() == 1) {
                        //共享
                        shareType = "1";
                    }

                    //String mcount = countMap.get(lfUdgroup.getUdgId().toString());
                    String mcount = countMap1.get(lfUdgroup.getGroupid().toString());
                    mcount = mcount == null ? "0" : mcount;
                    //shareType表示是0个人 1共享   groupType1 是员工群组  2是客户群组
                    buffer.append("<option  mcount='" + mcount + "' isdep='3' sharetype ='" + shareType + "' gtype='2'  value='" + lfUdgroup.getGroupid() + "' udgid='" + lfUdgroup.getUdgId() + "'  style='padding-left: 5px;'>")
                            .append(lfUdgroup.getUdgName().replace("<", "&lt;").replace(">", "&gt;"));
                    if (lfUdgroup.getSharetype() == 0) {
                        buffer.append(" [" + client + "/" + individual + "]");
                    } else if (lfUdgroup.getSharetype() == 1) {
                        buffer.append(" [" + client + "/" + shared + "]");
                    }
                    buffer.append("</option>");
                }
            }

            buffer.append("</select>");

        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取群组信息异常!");
        }
        try {
            response.getWriter().print(buffer.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            EmpExecutionContext.error(e, "异步调用异常！");
        }
    }


    public void getGroupMemberByNameAndId(HttpServletRequest request, HttpServletResponse response) {
        try {
            //搜索名称
            String name = request.getParameter("name");
            //群组id
            String udgId = request.getParameter("udgId");
            //群组类型   1表示员工  2表示客户
            String groupType = request.getParameter("groupType");

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("name", name);
            conditionMap.put("udgId", udgId);
            conditionMap.put("groupType", groupType);
            //获取操作员对象
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            List<DynaBean> dynaBeans = wxSendBiz.getGroupMemberByNameAndId(lfSysuser.getUserId(), conditionMap);
            List list = new ArrayList();
            for (DynaBean dynaBean : dynaBeans) {
                Map<String, Object> stringObjectMap = convertBeanToMap(dynaBean);
                list.add(stringObjectMap);
            }
            response.getWriter().print(com.alibaba.fastjson.JSONObject.toJSONString(list));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业网讯->静态网讯发送->选择人员->根据名字跟群组Id获取群组成员信息异常!");
        }
    }

    public static Map<String, Object> convertBeanToMap(DynaBean bean) {
        Map<String, Object> map = null;
        if (bean != null) {
            map = new HashMap<String, Object>();
            DynaProperty[] dynaProperties = bean.getDynaClass().getDynaProperties();
            if (dynaProperties != null) {
                for (DynaProperty property : dynaProperties) {
                    map.put(property.getName(), bean.get(property.getName()));
                }
            }
        }
        return map;
    }

    /**
     * 获取群组员工
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getGroupMember(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        try {
            //搜索名称
            String epname = request.getParameter("epname");
            //群组id
            String udgId = request.getParameter("udgId");
            //如果是选择多个群组就不查询员工
            if ("".equals(udgId) || udgId.split(",").length > 1) {
                response.getWriter().print("");
                return;
            }
            //当前操作员userid
            //String userId = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String userId = SysuserUtil.strLguserid(request);

            udgId = (udgId != null && udgId.length() > 0) ? udgId.substring(0, udgId.length() - 1) : "";

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            if (epname != null && !"".equals(epname)) {
                conditionMap.put("name&like", epname);
            }
            //查询出符合条件的人
            LfUdgroup group = baseBiz.getById(LfUdgroup.class, udgId);
            List<LfEmployee> lfEmployeeList = null;
            if (group != null) {
                lfEmployeeList = wxSendBiz.getEmployeeListByUdgId(group.getGroupid().toString(), userId, conditionMap);
            }

            StringBuffer sb = new StringBuffer();
            //拼成html代码返回
            if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
                for (LfEmployee user : lfEmployeeList) {
                    if (user.getRecState() == 2) {
                        sb.append("<option value='").append("m_" + user.getGuId())
                                .append("' mobile='").append(user.getMobile()).append("'>");
                        sb.append(user.getName().trim()).append("</option>");

                    } else {
                        sb.append("<option value='").append("e_" + user.getEmployeeId())
                                .append("' mobile='").append(user.getMobile()).append("'>");
                        sb.append(user.getName().trim()).append("</option>");
                    }
                }
            }
            response.getWriter().print(sb.toString());

        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取群组员工信息异常!");
        }
    }

    /**
     * 过滤号段
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void filterPh(HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        String[] haoduan = msgConfigBiz.getHaoduan();
        String tmp = request.getParameter("tmp");
        PrintWriter out = response.getWriter();
        //过滤号段
        if (phoneUtil.getPhoneType(tmp, haoduan) == -1) {
            out.print("false");
        } else {
            out.print("true");
        }

    }

    /**
     * 检测文件是否存在
     *
     * @param request
     * @param response
     */
    public void goToFile(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getParameter("url");
        TxtFileUtil tfu = new TxtFileUtil();
        try {
            response.getWriter().print(tfu.checkFile(url));
        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "检测文件是否存在异常!");
        }
    }

    public void getGtInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String spUser = request.getParameter("spUser");
        String[] gtInfos = new String[]{"", "", ""};
        int index = 0;
        int maxLen;
        int totalLen;
        int lastLen;
        int signLen;
        String info = "infos:";
        try {
            List<GtPortUsed> gtPortsList = smsBiz.getPortByUserId(spUser);
            for (GtPortUsed gtPort : gtPortsList) {
                index = gtPort.getSpisuncm() - 2 > 0 ? 2 : gtPort.getSpisuncm();

                maxLen = gtPort.getMaxwords();
                totalLen = gtPort.getMultilen1();
                lastLen = gtPort.getMultilen2();
                signLen = gtPort.getSignlen() == 0 ? gtPort.getSignstr().trim().length() : gtPort.getSignlen();

                gtInfos[index] = String.valueOf(maxLen) + "," + String.valueOf(totalLen) +
                        "," + String.valueOf(lastLen) + "," + String.valueOf(signLen);
            }
            info += gtInfos[0] + "&" + gtInfos[1] + "&" + gtInfos[2] + "&";
        } catch (Exception e) {
            info = "error";
            EmpExecutionContext.error(e, "查询通道信息异常!");
        } finally {
            response.getWriter().print(info);
        }
    }

    /**
     * 获取通道信息,包括英文短信配置参数
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void getSpGateConfig(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String spUser = request.getParameter("spUser");
        String[] gtInfos = new String[]{"", "", "", ""};
        int index = 0;
        int maxLen;
        int totalLen;
        int lastLen;
        int signLen;
        int enmaxLen;
        int entotalLen;
        int enlastLen;
        int ensignLen;
        int gateprivilege = 0;
        int gatepri;
        //签名位置,1:前置;0:后置
        int signLocation = 0;
        int ensinglelen;
        int msgLen = 900;

        String info = "infos:";
        try {
            // 根据发送账号获取路由信息
            List<DynaBean> spGateList = smsBiz.getSpGateInfo(spUser);
            for (DynaBean spGate : spGateList) {
                gateprivilege = 0;
                //中文短信配置参数
                maxLen = Integer.parseInt(spGate.get("maxwords").toString());
                totalLen = Integer.parseInt(spGate.get("multilen1").toString());
                lastLen = Integer.parseInt(spGate.get("multilen2").toString());
                signLen = Integer.parseInt(spGate.get("signlen").toString());
                // 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
                if (signLen == 0) {
                    signLen = spGate.get("signstr").toString().trim().length();
                }
                //英文短信配置参数
                enmaxLen = Integer.parseInt(spGate.get("enmaxwords").toString());
                entotalLen = Integer.parseInt(spGate.get("enmultilen1").toString());
                enlastLen = Integer.parseInt(spGate.get("enmultilen2").toString());
                ensinglelen = Integer.parseInt(spGate.get("ensinglelen").toString());

                //是否支持英文短信，1：支持；0：不支持
                gatepri = Integer.parseInt(spGate.get("gateprivilege").toString());
                index = Integer.parseInt(spGate.get("spisuncm").toString());
                //电信
                if (index == 21) {
                    index = 2;
                    //国外通道
                } else if (index == 5) {
                    index = 3;
                    //是否支持英文短信，1：支持；0：不支持
                    if ((gatepri & 2) == 2) {
                        gateprivilege = 1;
                        //国外通道英文短信最大长度(网讯短信长度需要减100)
                        msgLen = enmaxLen - 100;
                    } else {
                        gateprivilege = 0;
                        //国外通道中文短信最大长度(网讯短信长度需要减90)
                        msgLen = maxLen - 90;
                    }
                }
                //签名位置,1:前置;0:后置
                if ((gatepri & 4) == 4) {
                    signLocation = 1;
                } else {
                    signLocation = 0;
                }
                //英文短信签名
                ensignLen = Integer.parseInt(spGate.get("ensignlen").toString());
                // 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
                if (ensignLen == 0) {
                    if (index == 3) {
                        //国外通道英文短信签名长度
                        ensignLen = smsBiz.getenSmsSignLen(spGate.get("ensignstr").toString().trim());
                    } else {
                        //国内通道英文短信签名长度
                        ensignLen = spGate.get("ensignstr").toString().trim().length();
                    }
                }

                gtInfos[index] = new StringBuffer().append(maxLen).append(",").append(totalLen).append(",")
                        .append(lastLen).append(",").append(signLen).append(",").append(enmaxLen).append(",")
                        .append(entotalLen).append(",").append(enlastLen).append(",").append(ensignLen)
                        .append(",").append(gateprivilege).append(",").append(ensinglelen).append(",").append(signLocation).toString();
            }
            info += gtInfos[0] + "&" + gtInfos[1] + "&" + gtInfos[2] + "&" + gtInfos[3] + "&" + msgLen + "&";
        } catch (Exception e) {
            info = "error";
            EmpExecutionContext.error(e, "获取发送账户绑定的通道信息异常！");
        } finally {
            response.getWriter().print(info);
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
            KeyWordAtom keyWordAtom = new KeyWordAtom();
            words = keyWordAtom.checkText(tmMsg, corpCode);
        } catch (Exception e) {
            words = "error";
            EmpExecutionContext.error(e, "检查内容关键字异常!");
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
            EmpExecutionContext.error(e, "查询网讯模板信息异常!");
        } finally {
            //异步返回操作结果
            try {
                response.getWriter().print("@" + result);
            } catch (IOException e) {
                EmpExecutionContext.error(e, "异步调用异常！");
            }
        }
    }

    //判断txt文本编码
    public String get_charset(InputStream inp) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
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
        } catch (Exception e) {
            EmpExecutionContext.error(e, "判断txt文本编码异常！");
        }
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
    public BufferedReader jxExcel(String url, File Newfile, InputStream ins, String fileIndex, int exceltype) {
        String FileStr = url;//url[0];
        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + fileIndex + ".txt";
        File Newfiletemp=null;
        //TODO
        Newfiletemp = new File(FileStrTemp);
        if (Newfiletemp.exists()) {
            if (!Newfiletemp.delete()) {
            }
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            fos = new FileOutputStream(Newfiletemp);
            osw = new OutputStreamWriter(fos, "GBK");
            bw = new BufferedWriter(osw);
            String phoneNum = "";
            String Context = "";

            if (exceltype == 1) {
                HSSFWorkbook workbook = new HSSFWorkbook(ins);
                //循环每张表
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                    HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                    // 循环每一行
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        HSSFRow row = sheet.getRow(rowNum);
                        if (row == null) {
                            continue;
                        }
                        //得到第一列的电话号码
                        phoneNum = getCellFormatValue(row.getCell(0));
                        Context = "";
                        //循环每一列（内容以,分隔开来）
                        for (int k = 1; k < row.getLastCellNum(); k++) {
                            HSSFCell cell = row.getCell(k);
                            if (cell != null && cell.toString().length() > 0) {
                                Context += "," + getCellFormatValue(cell);
                            }

                        }
                        //一行一行的将内容写入到txt文件中。
                        bw.write(phoneNum + Context + line);
                    }
                }
            } else {

                XSSFWorkbook workbook = new XSSFWorkbook(ins);
                //循环每张表
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                    XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                    // 循环每一行
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        XSSFRow row = sheet.getRow(rowNum);
                        if (row == null) {
                            continue;
                        }
                        phoneNum = getCellFormatValue(row.getCell(0));
                        Context = "";
                        for (int k = 1; k < row.getLastCellNum(); k++) {
                            XSSFCell cell = row.getCell(k);
                            if (cell != null && cell.toString().length() > 0) {
                                Context += "," + getCellFormatValue(cell);
                            }
                        }
                        bw.write(phoneNum + Context + line);
                    }
                }
            }
            fis = new FileInputStream(Newfiletemp);
            br = new BufferedReader(new InputStreamReader(fis, "GBK"));

            return br;
        } catch (FileNotFoundException e) {
            EmpExecutionContext.error(e, "解析的EXCEL不存在!");
            return null;
        } catch (IOException e) {
            EmpExecutionContext.error(e, "解析EXCEL读取错误异常!");
            return null;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "解析EXCEL异常!");
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "解析EXCEL文件异常!关闭文件流异常");
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "解析EXCEL文件异常!关闭文件流异常");
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "解析EXCEL文件异常!关闭文件流异常");
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "解析EXCEL文件异常!关闭文件流异常");
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "解析EXCEL文件异常!关闭文件流异常");
                }
            }
        }

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
        return cellvalue;
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
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
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
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING: // 字符串
                    cellvalue = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_FORMULA: // 公式
                    cellvalue = cell.getCellFormula();
                    break;
                case HSSFCell.CELL_TYPE_BLANK: // 空值
                    cellvalue = " ";
                    break;
                case HSSFCell.CELL_TYPE_ERROR: // 故障
                    cellvalue = " ";
                    break;
                default:
                    cellvalue = " ";
                    break;
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    //过滤重复号码
    private boolean checkRepeat(HashSet<Long> aa, String ee) {
        if (aa.contains(Long.parseLong(ee))) {
            return false;
        } else {
            aa.add(Long.parseLong(ee));
        }
        return true;
    }

    //如果没有传递群组id，那么是以","开头的
    private String getEmpByGroupStr(String groupStr, String groupClient) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            //通过群组获取电话号码字符串
            if (groupStr != null && groupStr.length() > 1) {
                //去掉结尾的逗号
                String udgId = groupStr.substring(0, groupStr.length() - 1);
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("udgId&in", udgId);
                List<LfUdgroup> groupList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, null);
                StringBuffer buffer = new StringBuffer();
                if (groupList != null && groupList.size() > 0) {
                    for (int i = 0; i < groupList.size(); i++) {
                        buffer.append(groupList.get(i).getGroupid());
                        if (i != (groupList.size() - 1)) {
                            buffer.append(",");
                        }
                    }
                }
                /*List<LfEmployee> lfEmployeeList = new AddrBookBiz().getEmployeeListByUdgId(buffer.toString(), null, null);*/
                List<LfEmployee> lfEmployeeList = wxSendBiz.getEmployeeListByUdgId(buffer.toString(), null, null);

                if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
                    for (LfEmployee user : lfEmployeeList) {
                        sb.append(user.getMobile()).append(",");
                    }
                }
            }

            //通过群组获取电话号码字符串
            if (groupClient != null && groupClient.length() > 1) {
                //去掉结尾的逗号
                String udgId = groupClient.substring(0, groupClient.length() - 1);
                String[] udgs = udgId.split(",");
                List<DynaBean> group = null;
                for (int i = 0; i < udgs.length; i++) {
                    group = wxSendBiz.findGroupClientByIds(udgs[i]);
                    if (group != null && group.size() > 0) {
                        for (DynaBean aa : group) {
                            sb.append(aa.get("mobile") + ",");
                        }
                    }
                }


            }


        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "获取群组员工信息异常!");
        }

        return sb.toString();
    }

    //通过id字符串获取员工成员列表(改)depIds为,e1,3,10,e23,这种类型的字符串
    private String getEmpByDepId2(String depIds, String corpCode) {
        StringBuffer phones = new StringBuffer();
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            String[] tempDepIds = depIds.split(",");
            List<String> depIdsList = Arrays.asList(tempDepIds);
            List<String> depIdsList2 = new ArrayList<String>();
            List<String> depIdsList3 = new ArrayList<String>();

            for (int a = depIdsList.size() - 1; a >= 0; a--) {
                if (!"".equals(depIdsList.get(a))) {
                    if (depIdsList.get(a).indexOf("e") > -1) {
                        //不包含子机构的
                        depIdsList3.add(depIdsList.get(a));
                    } else {
                        depIdsList2.add(depIdsList.get(a));
                    }
                }
            }
            StringBuffer buffer = new StringBuffer("");
            List<LfEmployee> employees = null;
            int j = 0;
            //先遍历不包含子机构的
            for (int i = 0; i < depIdsList2.size(); i++) {
                if (depIdsList2.get(i) != null) {
                    buffer.append(depIdsList2.get(i) + ",");
                    j++;
                }
                if (j >= 999) {
                    j = 0;
                    conditionMap.put("depId&in", buffer.toString());
                    conditionMap.put("corpCode", corpCode);
                    employees = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
                    if (employees != null && employees.size() > 0) {
                        for (LfEmployee employee : employees) {
                            phones.append(employee.getMobile()).append(",");
                        }
                    }
                    buffer = new StringBuffer("");
                } else if (i == depIdsList2.size() - 1) {
                    conditionMap.put("depId&in", buffer.toString());
                    conditionMap.put("corpCode", corpCode);
                    employees = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
                    if (employees != null && employees.size() > 0) {
                        for (LfEmployee employee : employees) {
                            phones.append(employee.getMobile()).append(",");
                        }
                    }
                }
            }
            //再遍历包含子机构的
            for (int y = 0; y < depIdsList3.size(); y++) {
                if (depIdsList3.get(y) != null && !"".equals(depIdsList3.get(y)) && depIdsList3.get(y).indexOf("e") > -1) {
                    /*					List<LfEmployee> employeeList = new GenericLfEmployeeVoDAO().findEmpMobileByDepIds(corpCode, depIdsList3.get(y).substring(1));
                     */
                    List<LfEmployee> employeeList = wxSendBiz.findEmpMobilesByDepIds(corpCode, depIdsList3.get(y).substring(1));

                    for (LfEmployee employee : employeeList) {
                        phones.append(employee.getMobile()).append(",");
                    }
                }
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取员工成员列表异常!");
        }
        return phones.toString();
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
            Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
            if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                ishidephome = 1;
            }


            //动态模板短信发送
            if ("2".equals(sendType)) {
                /*编号 手机号码 模板内参数 文件内参数 短信内容*/
                response.getWriter().print("<thead><tr align='center'><th>" + MessageUtils.extractMessage("ydwx", "ydwx_wxgl_hdxgl_bianhao", request) + "</th><th><center><div style='width:89px'>" + MessageUtils.extractMessage("ydwx", "ydwx_wxcxtj_hftj_shoujihaoma", request) + "</div></center></th>" +
                        "<th>" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_1", request) + "</th><th>" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_2", request) + "</th>" +
                        "<th>" + MessageUtils.extractMessage("ydwx", "ydwx_wxcxtj_fwtj_dxnr", request) + "</th></tr></thead><tbody>");
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
                    response.getWriter().print("<tr align ='center'><td>" + x + "</td><td>" + phone + "</td>" +
                            "<td>" + snum[0] + "</td><td>" + snum[1] + "</td>" +
                            "<td><xmp style='word-break: break-all;white-space:normal;'>" + snum[3] + "</xmp></td></tr>");
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
                    index = tmp.indexOf(",");
                    phoneStr = tmp.substring(0, index);
                    smsContent = tmp.substring(index + 1).trim();
                    //手机号特殊处理
                    String phone = phoneStr != null ? phoneStr : "";
                    if (phone != null && !"".equals(phone) && ishidephome == 0) {
                        phone = cv.replacePhoneNumber(phone);
                    }
                    response.getWriter().print("<tr align ='center'><td >" + x
                            + "</td><td>" + phone + "</td>" +
                            "<td ><xmp style='word-break: break-all;white-space:normal;'>"
                            + smsContent + "</xmp></td></tr>");
                    if (x == 10) {
                        break;
                    }
                }
                response.getWriter().print("</tbody>");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "从Session取出信息出现异常！");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

    }

    /**
     * 查询网讯模板
     *
     * @param request
     * @param response
     */
    public void getNetTemplate(HttpServletRequest request, HttpServletResponse response) {
        String pageSize = request.getParameter("pageSize");
        String lgcorpcode = request.getParameter("lgcorpcode");
        String name = request.getParameter("name");
        //String lguserid=request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        Long lguserid = SysuserUtil.longLguserid(request);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        LfWXBASEINFO info = new LfWXBASEINFO();
        info.setNAME(name);
        info.setCORPCODE(lgcorpcode);
        info.setSTATUS(2);
        info.setTIMEOUT(now);
        info.setTempType(1);//使用静态的模板

        //网讯是否运营商商审核 0表示运营商不审核，1表示运营商审核
        if (StaticValue.getIsWxOperatorReview() == 1) {
            info.setOperAppStatus(1);//运营商审核通过
        }

        if (lguserid != null) {
            info.setCREATID(lguserid);
        }
        try {
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(Integer.parseInt(pageSize));
            List<DynaBean> infos = new WXSendBiz().getNetTemplate(info, pageInfo);
            //List<LfWXBASEINFO> infos = baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("baseInfos", infos);
            request.setAttribute("baseinfo", info);
            request.getRequestDispatcher(empRoot + "/sendWX/wx_netTemplate.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取网讯模板信息异常!");
        }
    }

    /**
     * 获取客户机构的 数，只 限查子级
     *
     * @param request
     * @param response
     */
    public void getClientSecondDepJson(HttpServletRequest request, HttpServletResponse response) {
        try {
            String depId = request.getParameter("depId");
            String userid = request.getParameter("userid");
            LfSysuser lfSysuser = getLoginUser(request);
            //此方法只查询两级机构
            List<LfClientDep> clientDepList = new WXSendBiz().getCliSecondDepTreeByUserIdorDepId(userid, depId, lfSysuser.getCorpCode());
            LfClientDep dep = null;
            StringBuffer tree = new StringBuffer("");
            if (clientDepList != null && clientDepList.size() > 0) {
                tree.append("[");
                for (int i = 0; i < clientDepList.size(); i++) {
                    dep = clientDepList.get(i);
                    tree.append("{");
                    tree.append("id:").append(dep.getDepId() + "");
                    tree.append(",name:'").append(dep.getDepName()).append("'");
                    tree.append(",depcodethird:'").append(dep.getDepcodethird()).append("'");
                    //树数据中加入父机构id
                    if (dep.getParentId() - 0 == 0) {
                        tree.append(",pId:").append(0);
                    } else {
                        tree.append(",pId:").append(dep.getParentId());
                    }
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != clientDepList.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");
            }
            response.getWriter().print(tree.toString());
        } catch (Exception e) {
            try {
                response.getWriter().print("");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, "获取客户机构的数异常！");
            }
            EmpExecutionContext.error(e, "获取客户机构子级数异常!");
        }
    }

    /**
     * 查询客户机构下的客户，不包含
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getClientByDepId(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        try {
            StringBuffer sb = new StringBuffer();
            String depId = request.getParameter("depId");
            String pageIndex = "10";
            if (request.getParameter("pageIndex") != null && !"".equals(request.getParameter("pageIndex"))) {
                pageIndex = request.getParameter("pageIndex");
            }
            String epname = request.getParameter("epname");
            String lgcorpcode = request.getParameter("lgcorpcode");
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(50);
            List<DynaBean> beanList = null;

            if (epname != null && !"".equals(epname.trim())) {
                //界面上输入查询条件：名称 查询
                pageInfo.setPageIndex(10);
                beanList = new WXSendBiz().getClients(epname, lgcorpcode, 2, pageInfo);
            } else {
                //异步查询所有值。
                LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
                pageInfo.setPageIndex(Integer.valueOf(pageIndex));
                beanList = new WXSendBiz().getClientsByDepId(clientDep, 2, pageInfo);
            }
            if (beanList != null && beanList.size() > 0) {
                sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
                for (DynaBean bean : beanList) {
                    sb.append("<option value='").append(String.valueOf(bean.get("guid")));
                    sb.append("' isdep='5' et=''  mobile='" + String.valueOf(bean.get("mobile")) + "'>");
                    sb.append(String.valueOf(bean.get("name")).replace("<", "&lt;").replace(">", "&gt;")).append("</option>");
                }
            }
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取客户机构下的客户信息异常!");
        }
    }

    /**
     * 查询员工 机构下的员工，不包含
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getEmployeeByDepId(HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
            StringBuffer sb = new StringBuffer();
            String depId = request.getParameter("depId");
            String pageIndex = request.getParameter("pageIndex");
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(10);
            pageInfo.setPageIndex(Integer.valueOf(pageIndex));
            orderByMap.put("employeeId", StaticValue.ASC);
            conditionMap.put("depId", depId);
            List<LfEmployee> lfEmployeeList = baseBiz.getByCondition(LfEmployee.class, null, conditionMap, orderByMap, pageInfo);
            if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
                sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
                for (LfEmployee user : lfEmployeeList) {
                    sb.append("<option value='").append(user.getGuId()).append("' isdep='4' et='' moblie='" + user.getMobile() + "'>");
                    sb.append(user.getName().trim().replace("<", "&lt;").replace(">", "&gt;")).append("</option>");
                }
            }
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取员工 机构下的员工异常!");
        }
    }

    /**
     * 通过群组ID查询出员工/客户群组 中的群组人员信息，分页
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getGroupUserByGroupId(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        try {
            StringBuffer sb = new StringBuffer();
            String groupId = request.getParameter("depId");
            String pageIndex = request.getParameter("pageIndex");
            String type = request.getParameter("type");
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(10);
            pageInfo.setPageIndex(Integer.valueOf(pageIndex));
            List<GroupInfoVo> groupInfoList = null;
            groupInfoList = new WXSendBiz().getGroupUser(Long.valueOf(groupId), pageInfo, type);
            if (groupInfoList != null && groupInfoList.size() > 0) {
                sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
                for (GroupInfoVo user : groupInfoList) {
                    //默认员工
                    String l2gtype = "0";
                    if (user.getL2gType() == 0) {
                        //员工
                        l2gtype = "4";
                    } else if (user.getL2gType() == 1) {
                        //客户
                        l2gtype = "5";
                    } else if (user.getL2gType() == 2) {
                        //自定义
                        l2gtype = "6";
                    }
                    if (user.getName() != null) {
                        sb.append("<option value='").append(user.getGuId()).append("' isdep='" + l2gtype + "' et='' mobile='" + user.getMobile() + "'>");
                        sb.append(user.getName().trim().replace("<", "&lt;").replace(">", "&gt;")).append("</option>");
                    } else {
                        continue;
                    }

                }
            }
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取群组ID员工异常!");
        }
    }

    /**
     * 这里是检测点击客户机构是否被选择了的机构包含
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void isClientDepContained(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //点击机构的ID
            String depId = request.getParameter("depId");
            //已经选择好的机构ID
            String clientDepIds = request.getParameter("cliDepIds");
            //解析IDS
            String[] depIds = clientDepIds.split(",");
            //机构集合
            List<LfClientDep> lfClientDepList = null;
            //处理是否包含机构ID的集合
            LinkedHashSet<Long> depIdsSet = new LinkedHashSet<Long>();
            //查询条件集合
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //机构对象
            LfClientDep dep = null;
            //循环
            for (int i = 0; i < depIds.length; i++) {
                String id = depIds[i];
                //如果包含了，则说明该机构包含子机构
                if (id == null || "".equals(id)) {
                    continue;
                }
                //如果相等，则眺出
                if (id.equals(depId)) {
                    response.getWriter().print("depExist");
                    return;
                    //遇到包含子机构的机构处理操作
                } else if (id.contains("e")) {
                    Long temp = Long.valueOf(id.substring(1));
                    conditionMap.clear();
                    dep = null;
                    lfClientDepList = null;
                    dep = baseBiz.getById(LfClientDep.class, temp);
                    if (dep != null) {
                        conditionMap.put("deppath&like", dep.getDeppath());
                        conditionMap.put("corpCode", dep.getCorpCode());
                        lfClientDepList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
                        if (lfClientDepList != null && lfClientDepList.size() > 0) {
                            for (int j = 0; j < lfClientDepList.size(); j++) {
                                depIdsSet.add(lfClientDepList.get(j).getDepId());
                            }
                        }
                    }
                    //单个机构，不包含子机构
                } else {
                    dep = null;
                    dep = baseBiz.getById(LfClientDep.class, Long.valueOf(id));
                    if (dep != null) {
                        depIdsSet.add(dep.getDepId());
                    }
                }
            }
            boolean isFlag = false;
            //判断是否包含该机构
            if (depIdsSet.size() > 0) {
                Long tempDepId = Long.valueOf(depId);
                isFlag = depIdsSet.contains(tempDepId);
            }
            //返回
            if (isFlag) {
                response.getWriter().print("depExist");
                return;
            } else {
                response.getWriter().print("noExist");
                return;
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "判断客户机构是否被选择异常!");
        }
    }

    /**
     * 判断是否 该机客户构包含选择了的子机构，并且把子机构删除掉
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void isClientDepContaineDeps(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //是否包含关系 0不包含   1包含
            String ismut = request.getParameter("ismut");
            //机构ID
            String depId = request.getParameter("depId");
            //不包含子机构
            if ("0".equals(ismut)) {
                //查询出单个机构下 （不包含子机构人员）的个数
                //String number = smsBiz.getClientCountByDepId(depId);
                LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
                if (clientDep == null) {
                    response.getWriter().print("nobody");
                    return;
                }
                String number = new WXSendBiz().getDepClientCount(clientDep, 2).toString();
                if (number != null && !"".equals(number)) {
                    if ("0".equals(number)) {
                        response.getWriter().print("nobody");
                    } else {
                        response.getWriter().print(number);
                    }
                } else {
                    response.getWriter().print("nobody");
                }
                return;
            }
            //该机构的包含子机构
            List<LfClientDep> lfClientDepList = null;
            LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
            //条件查询
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //已选好的机构
            String cliDepIds = request.getParameter("cliDepIds");
            String[] depIds = cliDepIds.split(",");
            List<Long> depIdExistList = new ArrayList<Long>();
            //循环
            for (int j = 0; j < depIds.length; j++) {
                String id = depIds[j];
                if (id != null && !"".equals(id)) {
                    if (id.indexOf("e") > -1) {
                        if (!"".equals(id.substring(1))) {
                            depIdExistList.add(Long.valueOf(id.substring(1)));
                        }
                    } else {
                        depIdExistList.add(Long.valueOf(id));
                    }
                }
            }
            //查找出要添加的机构的所有子机构，放在一个set里面
            LfClientDep dep = baseBiz.getById(LfClientDep.class, Long.valueOf(depId));
            if (dep != null) {
                conditionMap.put("deppath&like", dep.getDeppath());
                conditionMap.put("corpCode", dep.getCorpCode());
                lfClientDepList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
                if (lfClientDepList != null && lfClientDepList.size() > 0) {
                    for (int i = 0; i < lfClientDepList.size(); i++) {
                        depIdSet.add(lfClientDepList.get(i).getDepId());
                    }
                    //这里是把包含的机构的ID选择出来
                    List<Long> depIdListTemp = new ArrayList<Long>();
                    for (int a = 0; a < depIdExistList.size(); a++) {
                        if (depIdSet.contains(depIdExistList.get(a))) {
                            depIdListTemp.add(depIdExistList.get(a));
                        }
                    }

                    //如果确实有几个已经存在的机构是要添加机构的子机构，那么就从新生成select的html
                    String depids = depIdSet.toString();
                    depids = depids.substring(1, depids.length() - 1);
                    //计算机构人数
                    //String countttt = smsBiz.getClientCountByDepId(depids);

                    String countttt = new WXSendBiz().getDepClientCount(dep, 1).toString();

                    if (countttt != null && !"".equals(countttt)) {
                        if ("0".equals(countttt)) {
                            response.getWriter().print("nobody");
                            return;
                        } else if (depIdListTemp.size() > 0) {
                            String tempDeps = depIdListTemp.toString();
                            tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
                            response.getWriter().print(countttt + "," + tempDeps);
                            return;
                        } else {
                            response.getWriter().print("notContains" + "&" + countttt);
                            return;
                        }
                    } else {
                        response.getWriter().print("nobody");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "判断客户机构包含选择了的子机构异常!");
            response.getWriter().print("errer");
        }
    }

    /**
     * 获取客户机构的人员
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void getClientDepCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //是否包含关系 0不包含   1包含
            String ismut = request.getParameter("ismut");
            //机构ID
            String depId = request.getParameter("depId");
            //不包含子机构
            if ("0".equals(ismut)) {
                //这里返回机构的总人数
                //查询出单个机构下 （不包含子机构人员）的个数
                //String number = smsBiz.getClientCountByDepId(depId);
                LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
                if (clientDep == null) {
                    response.getWriter().print("nobody");
                    return;
                }
                String number = new WXSendBiz().getDepClientCount(clientDep, 2).toString();
                if (number != null && !"".equals(number)) {
                    if ("0".equals(number)) {
                        response.getWriter().print("nobody");
                        return;
                    } else {
                        response.getWriter().print(number);
                        return;
                    }
                } else {
                    response.getWriter().print("nobody");
                    return;
                }
            } else if ("1".equals(ismut)) {
                LfClientDep dep = baseBiz.getById(LfClientDep.class, Long.valueOf(depId));
                if (dep != null) {
                    String number = new WXSendBiz().getDepClientCount(dep, 1).toString();
                    response.getWriter().print(number);
                    return;
                }
            }
            response.getWriter().print("nobody");
            return;
        } catch (Exception e) {
            response.getWriter().print("errer");
            EmpExecutionContext.error(e, "获取客户机构的人员异常!");
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
            if (flag == null || "".equals(flag)) {
                EmpExecutionContext.error("静态网讯发送高级设置存为默认参数异常！");
                response.getWriter().print(result);
                return;
            }
            if (lguserid == null || "".equals(lguserid)) {
                EmpExecutionContext.error("静态网讯发送高级设置存为默认参数异常！lguserid：" + lguserid);
                response.getWriter().print(result);
                return;
            }
            if (busCode == null || "".equals(busCode)) {
                EmpExecutionContext.error("静态网讯发送高级设置存为默认参数异常！busCode：" + busCode);
                response.getWriter().print(result);
                return;
            }
            if (spUser == null || "".equals(spUser)) {
                EmpExecutionContext.error("静态网讯发送高级设置存为默认参数异常！spUser：" + spUser);
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

            result = wxSendBiz.setDefault(conditionMap, lfDfadvanced);

            //操作结果
            String opResult = "静态网讯发送高级设置存为默认失败。";
            if (result != null && "seccuss".equals(result)) {
                opResult = "静态网讯发送高级设置存为默认成功。";
            }
            //操作员信息
            LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
            //操作员姓名
            String opUser = sysuser == null ? "" : sysuser.getUserName();
            //操作日志信息
            StringBuffer content = new StringBuffer();
            content.append("[业务编码，SP账号](").append(busCode).append("，").append(spUser).append(")");

            //操作日志
            EmpExecutionContext.info("静态网讯发送", lgcorpcode, lguserid, opUser, opResult + content.toString(), "OTHER");

            response.getWriter().print(result);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "静态网讯发送高级设置存为默认异常！");
            response.getWriter().print(result);
        }
    }

    /**
     * 查询草稿
     *
     * @param request
     * @param response
     * @description
     * @author zhangsan <zhangsan@126.com>
     * @datetime 2016-1-18 上午09:24:54
     */
    public void getTmpContext(HttpServletRequest request, HttpServletResponse response) {

        String lgcorpcode = request.getParameter("lgcorpcode");
        String name = request.getParameter("name");
        //String lguserid=request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);


        Timestamp now = new Timestamp(System.currentTimeMillis());
        LfWXBASEINFO info = new LfWXBASEINFO();
        info.setNAME(name);
        info.setCORPCODE(lgcorpcode);
        info.setSTATUS(2);
        info.setTIMEOUT(now);
        info.setTempType(1);//使用静态的模板
        if (lguserid != null && !"".equals(lguserid)) {
            info.setCREATID(new Long(lguserid));
        }
        try {
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(10);
            List<DynaBean> infos = new WXSendBiz().getNetTemplate(info, pageInfo);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("baseInfos", infos);
            request.setAttribute("baseinfo", info);
            request.getRequestDispatcher(empRoot + "/sendWX/wx_netContext.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取网讯草稿信息异常!");
        }

    }

    /**
     * 保存草稿箱
     *
     * @param request
     * @param response
     */
    public void toDraft(HttpServletRequest request, HttpServletResponse response) {

        //草稿箱id
        String draftid = "";
        //发送主题
        String taskname = "";
        //操作员ID
        Long lguserid = null;
        //选取的草稿箱 文件路径
        String selDraftFilePath = "";
        // 企业编码
        String lgcorpcode = "";
        //号码字符串
        String phoneStr = "";
        //贴尾
        String smsTail = "";
        String depIdStr = "";
        String groupClient = "";
        //客户机构IDS
        String cliDepIds = "";
        //所选群组信息
        String groupStr = "";
        //处理返回结果
        JSONObject json = new JSONObject();
        //***该参数仅为了记录日志需要***
        StringBuffer opContent = new StringBuffer();
        json.put("ok", "0");
        //发送的手机号码的集合
        List<String> phoneList = new ArrayList<String>();
        //有效号码数
        Long effCount = 0l;
        StringBuffer contentSb = new StringBuffer();
        //内容
        String msg = "";
        String strlguserid = null;
        try {
            //strlguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            strlguserid = SysuserUtil.strLguserid(request);

            lguserid = Long.valueOf(strlguserid == null ? "0" : strlguserid);
            String[] url = txtfileutil.getSaveUrl(lguserid);
            DiskFileItemFactory factory = new DiskFileItemFactory();
            File Newfile = null;
            factory.setSizeThreshold(1024 * 1024);
            String temp = url[0].substring(0, url[0].lastIndexOf("/"));
            factory.setRepository(new File(temp));
            ServletFileUpload upload = new ServletFileUpload(factory);
            File zipUrl = null;
            List<FileItem> fileList = null;
            try {
                //以文件方式解析表单
                fileList = upload.parseRequest(request);
            } catch (FileUploadException e) {
                EmpExecutionContext.error(e, "解析表单异常");
                throw e;
            }

            Iterator<FileItem> it = fileList.iterator();
            List<BufferedReader> readerList = new ArrayList<BufferedReader>();
            FileItem fileItem = null;
            int fileIndex = 0;
            int fileCount = 0;
            while (it.hasNext()) {
                fileItem = (FileItem) it.next();
                String fileName = fileItem.getFieldName();
                if (fileName.equals("phoneStr")) {
                    //电话号码
                    phoneStr = fileItem.getString("UTF-8").toString();
                    if (",".equals(phoneStr)) {//出现只有","号 的表示没有号码
                        phoneStr = "";
                    } else if (phoneStr.indexOf(",,") > -1) {//去掉号码为空的
                        phoneStr = phoneStr.replace(",,", ",");
                    }

                } else if (fileName.equals("empDepIds")) {
                    //机构id
                    depIdStr = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("cliDepIds")) {
                    //客户机构ids
                    cliDepIds = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("groupClient")) {
                    //群组id
                    groupClient = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("groupIds")) {
                    //群组id
                    groupStr = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("taskname")) {
                    //标题
                    taskname = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("draftFile")) {
                    selDraftFilePath = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("draftId")) {
                    draftid = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("msg")) {
                    //内容
                    msg = fileItem.getString("UTF-8").toString();
                    //替换短信内的特殊字符
                    msg = msg + smsTail;
                    msg = smsBiz.smsContentFilter(msg);
                } else if (fileName.equals("lguserid")) {
                    //当前用户的userid
                    lguserid = Long.parseLong(fileItem.getString("UTF-8").toString());
                } else if (fileName.equals("lgcorpcode")) {
                    //企业编码
                    lgcorpcode = fileItem.getString("UTF-8").toString();
                }
                //上传号码文件
                else if (!fileItem.isFormField() && fileItem.getName().length() > 0) {

                    //此变量是为了区分上传多个文件时临时文件名称重复的问题
                    fileCount++;

                    String fileCurName = fileItem.getName();


                    String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();

                    if (fileType.equals(".zip")) {

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
                                readerList.add(jxExcel(url[0], Newfile, zipFile.getInputStream(entry), fileIndexStr, 1));
                            } else if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".xlsx")) {
                                readerList.add(jxExcel(url[0], Newfile, zipFile.getInputStream(entry), fileIndexStr, 2));
                            }
                        }
                    } else if (fileType.equals(".xls") || fileType.equals(".et")) {
                        //如果上传文件是excel2003或者wps表格
                        String FileStr = url[0];
                        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + "_" + fileCount + ".txt";
                        Newfile = new File(FileStrTemp);
                        FileOutputStream fos = new FileOutputStream(Newfile);
                        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
                        BufferedWriter bw = new BufferedWriter(osw);
                        String phoneNum = "";
                        String Context = "";
                        try {
                            HSSFWorkbook workbook = new HSSFWorkbook(fileItem.getInputStream());
                            //循环每张表
                            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                                HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                                // 循环每一行
                                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                    HSSFRow row = sheet.getRow(rowNum);
                                    if (row == null) {
                                        continue;
                                    }
                                    //得到第一列的电话号码
                                    phoneNum = getCellFormatValue(row.getCell(0));
                                    Context = "";
                                    //循环每一列（内容以,分隔开来）
                                    for (int k = 1; k < row.getLastCellNum(); k++) {
                                        HSSFCell cell = row.getCell(k);
                                        if (cell != null && cell.toString().length() > 0) {
                                            Context += "," + getCellFormatValue(cell);
                                        }

                                    }
                                    //一行一行的将内容写入到txt文件中。
                                    bw.write(phoneNum + Context + line);
                                }
                            }
                            bw.close();
                            osw.close();
                            fos.close();
                            FileInputStream fis = new FileInputStream(Newfile);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));
                            readerList.add(br);
                        } catch (FileNotFoundException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        }
                    } else if (fileType.equals(".xlsx")) {
                        String FileStr = url[0];
                        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + "_" + fileCount + ".txt";
                        Newfile = new File(FileStrTemp);
                        FileOutputStream fos = new FileOutputStream(Newfile);
                        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
                        BufferedWriter bw = new BufferedWriter(osw);
                        String phoneNum = "";
                        String Context = "";
                        try {
                            XSSFWorkbook workbook = new XSSFWorkbook(fileItem.getInputStream());
                            //循环每张表
                            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                                XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                                // 循环每一行
                                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                    XSSFRow row = sheet.getRow(rowNum);
                                    if (row == null) {
                                        continue;
                                    }
                                    phoneNum = getCellFormatValue(row.getCell(0));
                                    Context = "";
                                    for (int k = 1; k < row.getLastCellNum(); k++) {
                                        XSSFCell cell = row.getCell(k);
                                        if (cell != null && cell.toString().length() > 0) {
                                            Context += "," + getCellFormatValue(cell);
                                        }
                                    }
                                    bw.write(phoneNum + Context + line);
                                }
                            }
                            bw.close();
                            osw.close();
                            fos.close();

                            FileInputStream fis = new FileInputStream(Newfile);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));
                            readerList.add(br);
                        } catch (FileNotFoundException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        }
                    } else {
                        InputStream instream = fileItem.getInputStream();
                        String charset = get_charset(instream);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(), charset));
                        if (charset.startsWith("UTF-")) {
                            reader.read(new char[1]);
                        }
                        readerList.add(reader);
                    }
                }
            }// 循环结束
            BufferedReader reader;
            //解析号码文件
            String tmp;
            try {
                for (int r = 0; r < readerList.size(); r++) {
                    reader = readerList.get(r);
                    while ((tmp = reader.readLine()) != null) {
                        tmp = tmp.trim();
                        phoneList.add(tmp);
                    }
                }
            } catch (Exception e) {
                txtfileutil.deleteFile(url[0]);
                EmpExecutionContext.error(e, lguserid, lgcorpcode);
                throw e;
            } finally {
    			try{
    				IOUtils.closeReaders(getClass(), readerList);
    			}catch(IOException e){
    				EmpExecutionContext.error(e, "");
    			}
                readerList.clear();

                readerList = null;
                if (zipUrl != null) {
                    if (!zipUrl.delete()) {
                    }
                    ;
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
                        }
                    }
                    //删除压缩多个文件的临时文件
                    for (int i = 0; i < fileIndex; i++) {
                        int a = i + 1;
                        FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + a + "_" + b + ".txt";
                        file = new File(FileStrTemp);
                        if (file.exists()) {
                            if (!file.delete()) {
                            }
                        }
                    }
                }

                if (Newfile != null) {
                    if (!Newfile.delete()) {
                    }
                }
            }
            //解析员工通讯录号码字符串
            if (depIdStr.length() > 0) {
                //通过机构id查找电话
                phoneStr = phoneStr + getEmpByDepId2(depIdStr, lgcorpcode);
                String tempdepIdStr = depIdStr.substring(0, depIdStr.length() - 1);
                //有的时候是e+id号,去掉
                if (tempdepIdStr.indexOf("e") > -1) {
                    tempdepIdStr = tempdepIdStr.replaceAll("e", "");
                }
                opContent.append("员工通讯录id:" + tempdepIdStr).append("，");

            }
            //解析客户通讯录号码字符串
            if (!StringUtils.isEmpty(cliDepIds)) {
                //通过客户ID值查询客户电话号码
                phoneStr = wxSendBiz.getClientPhoneStrByDepId(phoneStr, cliDepIds, lgcorpcode);
                String tempcliDepIds = cliDepIds.substring(0, cliDepIds.length() - 1);
                //有的时候是e+id号,去掉
                if (tempcliDepIds.indexOf("e") > -1) {
                    tempcliDepIds = tempcliDepIds.replaceAll("e", "");
                }
                opContent.append("客户通讯录id:" + tempcliDepIds).append("，");

            }
            if (groupStr.length() > 0 || groupClient.length() > 0) {
                //通过群组查找电话
                phoneStr = phoneStr + getEmpByGroupStr(groupStr, groupClient);
                if (groupClient.length() > 0) {
                    String tempgroupClient = groupClient.substring(0, groupClient.length() - 1);
                    /*客户群组*/
                    opContent.append(MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_3", request) + " id:" + tempgroupClient).append("，");
                }
                if (groupStr.length() > 0) {
                    String tempgroupStr = groupStr.substring(0, groupStr.length() - 1);
                    /*员工群组*/
                    opContent.append(MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_4", request) + " id:" + tempgroupStr).append("，");
                }

            }

            if (phoneStr.length() > 0) {

                String[] phones = phoneStr.split(",");
                for (String num : phones) {
                    if (num != null) {
                        num = num.trim();
                    }
                    if (num != null && num.length() > 6 && num.length() < 22) {
                        //暂存草稿箱的号码不做重复，非法等方面的校验
                        phoneList.add(num.trim());
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
                        EmpExecutionContext.error("静态网讯草稿箱文件从文件服务器下载失败。");
                    }
                }
                if (!draftFile.exists()) {
                    EmpExecutionContext.error("静态网讯未找到草稿箱发送文件！");
                    //草稿箱号码文件不存在
                    json.put("ok", "-1");
                    return;
                }
                BufferedReader bufferedReader = null;
                try {
                    //读取时编码 保持与写入时一致
                    bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(draftFile), "gbk"));
                    String tmps = null;
                    while ((tmps = bufferedReader.readLine()) != null) {
                        phoneList.add(tmps.trim());
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error("静态网讯读取草稿箱发送文件异常！");
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
            String saveName = "draft" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(System.currentTimeMillis()) + sx.getCount() + ".txt";
            //每次保存生成新的草稿文件路径
            String newDraftFilePath = uploadPath + dirPath + saveName;
            //全路径
            physicsUrl = webRoot + newDraftFilePath;

            if (draftid == null || "".equals(draftid.trim())) {
                drafts.setCorpcode(lgcorpcode);
                drafts.setCreatetime(new Timestamp(System.currentTimeMillis()));
                //草稿类型，0－相同内容；1－不同内容动态模板；2－不同内容文件发送；3－客户群组群发；4-静态网讯发送；5－动态网讯发送
                drafts.setDraftstype(4);
                drafts.setUserid(lguserid);
                drafts.setMobileurl(newDraftFilePath);
            } else {
                drafts.setId(Long.parseLong(draftid));
                drafts.setMobileurl(newDraftFilePath);
            }
            //若文件存在 则清空内容 否则 生成新文件
            txtFileUtil.emptyTxtFile(physicsUrl);

            //号码文件全新写入
            String line = System.getProperties().getProperty(StaticValue.LINE_SEPARATOR);
            StringBuffer contentS = new StringBuffer("");
            //统计号码数
            int count = 0;
            for (String phone : phoneList) {
                contentS.append(phone + line);
                count++;
                //达到5000 写一次文件
                if (count % 5000 == 0) {
                    txtFileUtil.writeToTxtFile(physicsUrl, contentS.toString());
                    contentS.setLength(0);
                    count = 0;
                }
            }
            if (count > 0) {
                // 剩余的号码写文件
                txtFileUtil.writeToTxtFile(physicsUrl, contentS.toString());
                contentS.setLength(0);
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
                    EmpExecutionContext.error("静态网讯草稿箱文件上传文件到服务器失败。错误码：" + IErrorCode.B20023);
                    return;
                }
            }

            boolean result = false;
            drafts.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            //若参数为空 则默认赋值一个空格 （oracle数据库兼容）
            drafts.setMsg(StringUtils.defaultIfEmpty(msg, " "));
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
        } catch (EMPException empex) {
            EmpExecutionContext.error(empex, lguserid, lgcorpcode);
        } catch (Exception e) {
            EmpExecutionContext.error(e, lguserid, lgcorpcode);
        } finally {
            request.setAttribute("result", json.toString());
            try {
                request.getRequestDispatcher(empRoot + "/sendWX/wx_todraft.jsp").forward(request, response);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "静态网讯暂存草稿处理异常！");
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

    public void getDrafts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageInfo pageInfo = new PageInfo();
        List<DynaBean> list = new ArrayList();
        String lgcorpcode = null;
        String lguserid = null;

        try {
            Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
            pageSet(pageInfo, request);
            pageInfo.setPageSize(pageSize);
            String taskName = request.getParameter("taskname");
            String msg = request.getParameter("msg");
            String starttime = request.getParameter("starttime");
            String endtime = request.getParameter("endtime");
            String draftstype = request.getParameter("draftstype");
            LfSysuser sysuser = getLoginUser(request);
            lgcorpcode = sysuser.getCorpCode();
            lguserid = "" + sysuser.getUserId();
            LinkedHashMap<String, String> condMap = new LinkedHashMap();
            condMap.put("corpcode", lgcorpcode);
            condMap.put("userid", lguserid);
            condMap.put("draftstype", draftstype);
            if (StringUtils.isNotBlank(taskName)) {
                condMap.put("title", taskName.trim());
            }

            if (StringUtils.isNotBlank(msg)) {
                condMap.put("msg", msg.trim());
            }

            if (StringUtils.isNotBlank(starttime)) {
                condMap.put("starttime", starttime);
            }

            if (StringUtils.isNotBlank(endtime)) {
                condMap.put("endtime", endtime);
            }

            list = wxSendBiz.getUserDrafts(condMap, pageInfo);
        } catch (Exception var17) {
            EmpExecutionContext.error("获取草稿箱请求URL:" + request.getRequestURI() + "；lgcorpcode:" + lgcorpcode + "，strlguserid：" + lguserid);
        } finally {
            request.setAttribute("list", list);
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(empRoot + "/sendWX/wx_draftlist.jsp").forward(request, response);
        }
    }
}
