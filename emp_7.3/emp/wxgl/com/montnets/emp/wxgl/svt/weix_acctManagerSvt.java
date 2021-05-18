package com.montnets.emp.wxgl.svt;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.ottbase.util.RandomStrUtil;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxgl.biz.AccountBiz;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("serial")
public class weix_acctManagerSvt extends BaseServlet {
    /**
     * 公众帐号逻辑层
     */
    private final AccountBiz accountBiz = new AccountBiz();

    /**
     * 资源路径
     */
    private final String PATH = "/wxgl/account";

    /**
     * 基础逻辑层
     */
    private final BaseBiz baseBiz = new BaseBiz();

    /**
     * 公众帐号的类型
     */
    private final LinkedHashMap<String, String> tpList = new LinkedHashMap<String, String>();

    /**
     * 公众帐号管理页面
     *
     * @param request
     * @param response
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 分页信息
        PageInfo pageInfo = new PageInfo();
        //是否是第一次进入
        boolean isFirstEnter = pageSet(pageInfo, request);

        //添加与日志相关
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        long startTimeByLog = System.currentTimeMillis();  //开始时间

        try {
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            // 用来存储公众帐号
            List<DynaBean> beans = new ArrayList<DynaBean>();
            // 存放查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //是否是第一进入页面，不是第一次进入，则执行if语句块中的代码
            if (!isFirstEnter) {
                // 获取查询条件
                String name = request.getParameter("name");   //公众帐号名称
                // 微信公众帐号openid
                String ghname = request.getParameter("ghname");

                // 客户基本资料判断
                if (name != null && !"".equals(name.trim())) {
                    conditionMap.put("name", name.trim());
                }
                if (ghname != null && !"".equals(ghname.trim())) {
                    conditionMap.put("ghname", ghname.trim());
                }
            }
            //企业公众帐号列表
            beans = accountBiz.findAllAccountByCorpCode(lgcorpcode, conditionMap, pageInfo);

            request.setAttribute("beans", beans);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "公众帐号管理-公众帐号列表页面加载出错！");
        } finally {

            //添加与日志相关
            long endTimeByLog = System.currentTimeMillis();  //查询结束时间
            long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时

            //增加操作日志
            Object loginSysuserObj = null;
            try {
                loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            } catch (Exception e) {
                EmpExecutionContext.error(e, "session为null");
            }
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String opContent = "查询开始时间：" + sdf.format(startTimeByLog) + "，耗时：" + consumeTimeByLog + "ms" + "，查询总数：" + pageInfo.getTotalRec();

                EmpExecutionContext.info("公众帐号管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                        loginSysuser.getUserName(), opContent, "GET");
            }

            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(PATH + "/weix_accountList.jsp").forward(request, response);
        }
    }

    /**
     * 公众帐号新增页面
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void doAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            tpList.put("0", MessageUtils.extractMessage("wxgl", "wxgl_java_title_1", request));
            tpList.put("1", MessageUtils.extractMessage("wxgl", "wxgl_java_title_2", request));
            tpList.put("2", MessageUtils.extractMessage("wxgl", "wxgl_java_title_3", request));
            tpList.put("3", MessageUtils.extractMessage("wxgl", "wxgl_java_title_4", request));
            tpList.put("4", MessageUtils.extractMessage("wxgl", "wxgl_java_title_5", request));
            tpList.put("5", MessageUtils.extractMessage("wxgl", "wxgl_java_title_6", request));
            tpList.put("6", MessageUtils.extractMessage("wxgl", "wxgl_java_title_7", request));
            tpList.put("7", MessageUtils.extractMessage("wxgl", "wxgl_java_title_8", request));
            tpList.put("8", MessageUtils.extractMessage("wxgl", "wxgl_java_title_9", request));
            tpList.put("9", MessageUtils.extractMessage("wxgl", "wxgl_java_title_10", request));
            tpList.put("10", MessageUtils.extractMessage("wxgl", "wxgl_java_title_11", request));
            // 公众帐号的类型
            request.setAttribute("tpList", tpList);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "公众帐号管理-公众帐号新增页面加载错误！");
        } finally {
            request.getRequestDispatcher(PATH + "/weix_addAccount.jsp").forward(request, response);
        }
    }

    /**
     * 编辑公众帐号
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void doEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 公众帐号编号
            String aId = request.getParameter("tmId");
            // 公众帐号类型
            LfWeiAccount acct = baseBiz.getById(LfWeiAccount.class, aId);
            tpList.put("0", MessageUtils.extractMessage("wxgl", "wxgl_java_title_1", request));
            tpList.put("1", MessageUtils.extractMessage("wxgl", "wxgl_java_title_2", request));
            tpList.put("2", MessageUtils.extractMessage("wxgl", "wxgl_java_title_3", request));
            tpList.put("3", MessageUtils.extractMessage("wxgl", "wxgl_java_title_4", request));
            tpList.put("4", MessageUtils.extractMessage("wxgl", "wxgl_java_title_5", request));
            tpList.put("5", MessageUtils.extractMessage("wxgl", "wxgl_java_title_6", request));
            tpList.put("6", MessageUtils.extractMessage("wxgl", "wxgl_java_title_7", request));
            tpList.put("7", MessageUtils.extractMessage("wxgl", "wxgl_java_title_8", request));
            tpList.put("8", MessageUtils.extractMessage("wxgl", "wxgl_java_title_9", request));
            tpList.put("9", MessageUtils.extractMessage("wxgl", "wxgl_java_title_10", request));
            tpList.put("10", MessageUtils.extractMessage("wxgl", "wxgl_java_title_11", request));
            request.setAttribute("tpList", tpList);
            request.setAttribute("acct", acct);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "公众帐号管理-公众帐号编辑页面加载错误！");
        } finally {
            request.getRequestDispatcher(PATH + "/weix_editAccount.jsp").forward(request, response);
        }
    }

    /**
     * 添加和编辑微信公众帐号(OpType == "add" 新增微信公众帐号 || OpType == "edit" 编辑微信公众帐号)
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 当前登录企业
        String lgcorpcode = request.getParameter("lgcorpcode");
        // 头像地址
        String acctImg = "";
        // 操作类型
        String OpType = "";
        // 图片存放的根目录:"file/weix/account/"
        String baseFileDir = getServletContext().getRealPath(WXStaticValue.WEIX_ACCOUNT);
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
            upload.setSizeMax(4L * 1024L * 1024L);
            // 取得所有上传文件的信息
            List<FileItem> fileList = null;
            try {
                fileList = upload.parseRequest(request);
            } catch (FileUploadException e) {
                EmpExecutionContext.error(e, "公众帐号上传图像失败！");
                throw e;
            }

            Iterator<FileItem> iter = fileList.iterator();
            LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
            while (iter.hasNext()) {
                FileItem item = iter.next();
                String fieldName = item.getFieldName();

                if (!item.isFormField() && item.getName().length() > 0) {
                    // 文件名
                    String fileName = item.getName();
                    // 获得文件大小
                    long fileSize = item.getSize();
                    allFileSize += fileSize;
                    // 判断文件大小是否超过限制
                    if (allFileSize > maxSize) {
                        item.delete();
                        item = null;
                        break;
                    }

                    // 文件类型
                    String allImgExt = ".jpg|.jpeg|.gif|.bmp|.png|";
                    String extName = fileName.substring(fileName.indexOf("."), fileName.length());

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
                            String strid = UUID.randomUUID().toString();
                            String newFileName = strid.replaceAll("-", "") + WXStaticValue.SERVER_NUMBER + extName;
                            File uploadedFile = new File(filepath, newFileName);
                            if (uploadedFile.exists()) {
                                if (!uploadedFile.delete()) {
                                    throw new FileUploadException();
                                }
                            }
                            item.write(uploadedFile);

                            acctImg = WXStaticValue.WEIX_ACCOUNT + "/" + year + "/" + month + "/" + day + "/" + newFileName;
                        } catch (FileUploadException e) {
                            EmpExecutionContext.error(e, "公众帐号上传图像失败！");
                            throw e;
                        } finally {
                            // 使用集群，将文件上传到文件服务器
                            if (WXStaticValue.ISCLUSTER == 1) {
                                // 上传文件到文件服务器
                                CommonBiz comBiz = new CommonBiz();
                                boolean upFileRes = comBiz.upFileToFileServer(acctImg);
                                if (!upFileRes) {
                                    EmpExecutionContext.error("上传文件到服务器失败。错误码：" + IErrorCode.B20023);
                                }
                            }
                        }
                    }
                } else {
                    fieldMap.put(fieldName, item.getString("UTF-8").toString());
                }
            }

            // 公众帐号AID
            String aId = fieldMap.get("aId");
            // 公众帐号类型
            String acctType = fieldMap.get("acctType");
            // 公众帐号名称
            String acctName = fieldMap.get("acctName");
            // 微信帐号
            String acctCode = fieldMap.get("acctCode");
            // 原始帐号
            String acctOpenId = fieldMap.get("acctOpenId");
            // AppId
            String appId = fieldMap.get("appid");
            // AppSecret
            String secret = fieldMap.get("appsecret");
            // 操作类型
            OpType = fieldMap.get("OpType");
            // 公众帐号描述
            String acctInfo = fieldMap.get("acctInfo");

            // 新增公众帐号
            if ("add".equals(OpType)) {
                LfWeiAccount acct = new LfWeiAccount();
                acct.setName(acctName);
                acct.setType(Integer.valueOf(acctType));
                acct.setCode(acctCode);
                acct.setOpenId(acctOpenId);
                acct.setAppId(appId);
                acct.setSecret(secret);
                acct.setInfo(acctInfo);
                acct.setCorpCode(lgcorpcode);
                acct.setIsApprove(0);
                acct.setImg(acctImg);
                acct.setSyncState(1);
                // 设置日期格式(new Date()为获取当前系统时间)
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                acct.setCreateTime(Timestamp.valueOf(df.format(new Date())));
                acct.setBindTime(Timestamp.valueOf(df.format(new Date())));
                acct.setUrl(RandomStrUtil.getUniqueneStr((char) 65, 20));
                acct.setToken(RandomStrUtil.getComCharStr(15));

                // TODO-判断原始微信号是否唯一
                Long addTemp = baseBiz.addObjProReturnId(acct);

                if (addTemp != null && addTemp > 0) {
                    // 新增成功
                    request.setAttribute("tmresult", "true");
                } else {
                    // 新增失败
                    request.setAttribute("tmresult", "false");
                }
            } else if ("edit".equals(OpType)) {
                // 修改模板
                LfWeiAccount acct = baseBiz.getById(LfWeiAccount.class, aId);
                acct.setName(acctName);
                acct.setType(Integer.valueOf(acctType));
                acct.setCode(acctCode);
                acct.setAppId(appId);
                acct.setSecret(secret);
                acct.setInfo(acctInfo);
                acct.setCorpCode(lgcorpcode);
                if (acctImg != null && !"".equals(acctImg)) {
                    acct.setImg(acctImg);
                }
                // 设置日期格式(new Date()为获取当前系统时间)
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                acct.setModifyTime(Timestamp.valueOf(df.format(new Date())));

                boolean upTemp = baseBiz.updateObj(acct);
                // 修改成功
                if (upTemp) {
                    request.setAttribute("tmresult", "true");
                    request.setAttribute("input", fieldMap.get("input"));
                    //更新内存中的存在的account
                    new WeixBiz().updateAccountMapWhitAid(acct);
                } else {
                    // 修改失败
                    request.setAttribute("tmresult", "false");
                }
                LfWeiAccount newacct = baseBiz.getById(LfWeiAccount.class, aId);
                request.setAttribute("acct", newacct);
            }
        } catch (Exception ex) {
            request.setAttribute("tmresult", "false");
            EmpExecutionContext.error(ex, "公众帐号管理-保存公众帐号出错！");
        } finally {
            if ("add".equals(OpType)) {
                request.getRequestDispatcher(PATH + "/weix_addAccount.jsp").forward(request, response);
            } else if ("edit".equals(OpType)) {
                request.getRequestDispatcher(PATH + "/weix_editAccount.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher(PATH + "/weix_addAccount.jsp").forward(request, response);
            }
        }
    }

    /**
     * 检查提交信息是否合法
     * 1.判断acctOpenId唯一
     * 2.如果AppId和AppSecret不为空，判断唯一性
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void doCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String words = "";
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try {
            // 请求验证类型
            String aId = request.getParameter("aId");
            String corpCode = request.getParameter("corpCode");
            String acctOpenId = request.getParameter("acctOpenId");
            String appid = request.getParameter("appid");
            String appsecret = request.getParameter("appsecret");
            String tp = request.getParameter("type");

            //编辑时公众帐号的时候，aId不能为空，参与查询条件
            if ("edit".equals(tp) && (null == aId || "".equals(aId))) {
                return;
            }

            List<LfWeiAccount> acctList = null;
            conditionMap.put("openId", acctOpenId);
            conditionMap.put("corpCode", corpCode);
            if (null != aId && !"".equals(aId)) {
                conditionMap.put("AId&<>", aId);
            }
            acctList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);
            if (acctList != null && acctList.size() > 0) {
                words = "repeatOpenId";
                return;
            }

            conditionMap.clear();
            conditionMap.put("corpCode", corpCode);
            conditionMap.put("appId", appid);
            conditionMap.put("secret", appsecret);

            if (null != aId && !"".equals(aId)) {
                conditionMap.put("AId&<>", aId);
            }
            acctList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);
            if (acctList != null && acctList.size() > 0) {
                words = "repeatAppidAndSecret";
                return;
            }
            words = "success";
        } catch (Exception e) {
            words = "error";
            EmpExecutionContext.error(e, "公众帐号管理-doCheck验证失败!");
        } finally {
            // 异步返回结果
            response.setContentType("text/html");
            response.getWriter().print("@" + words);
        }
    }

    /**
     * 免责声明
     *
     * @param request
     * @param response
     * @throws IOException
     * @description
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2013-10-30 上午11:04:12
     */
    public void doRelief(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result = "";
        String approve = request.getParameter("approve");
        HttpSession session = request.getSession(false);
        try {
            if ("1".equals(approve)) {
                session.setAttribute("approve", "1");
                result = "success";
            } else {
                session.setAttribute("approve", "0");
                result = "refuse";
            }
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "公众帐号管理-doCheck验证失败!");
        } finally {
            // 异步返回结果
            response.setContentType("text/html");
            response.getWriter().print("@" + result);
        }
    }


    /**
     * 异步请求同步当前公众帐号对应的群组和用户关注列表
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2013-12-9 下午03:13:08
     */
    public void doSyncProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String result = "";
        response.setContentType("text/html");
        PrintWriter out = null;
        WeixBiz weixbiz = new WeixBiz();
        try {
            // 企业编号
            String corpCode = request.getParameter("lgcorpcode");
            // 公众帐号
            String aId = request.getParameter("aId");
            // token
            String accessToken = weixbiz.getToken(aId);


            if (null != accessToken && !"".equals(accessToken)) {
                // 同步用户群组
                boolean isSynchOk = weixbiz.syncDataFromWeix(accessToken, aId, corpCode);
                if (isSynchOk) {
                    // 同步成功
                    result = "success";
                } else {
                    // 同步失败
                    result = "fail";
                }
            } else {
                // 获取token失败
                result = "errorToken";
            }
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "在线客服-添加客服doAdd方法异常！");
        } finally {
            out = response.getWriter();
            out.print(result);
            out.close();
        }
    }
}
