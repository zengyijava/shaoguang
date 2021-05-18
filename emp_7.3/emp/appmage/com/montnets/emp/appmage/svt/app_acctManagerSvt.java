package com.montnets.emp.appmage.svt;

import com.montnets.emp.appmage.biz.app_acctManagerBiz;
import com.montnets.emp.appwg.biz.WgMwCommuteBiz;
import com.montnets.emp.appwg.wginterface.IWgMwCommuteBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.appmage.LfAppAccount;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class app_acctManagerSvt extends BaseServlet {
    /**
     * @Title: app_acctManagerSvt.java
     * @Package com.montnets.emp.appmage.svt
     * @Description: TODO()
     * @author A18ccms A18ccms_gmail_com
     * @date 2014-6-12 下午05:36:05
     * @version V1.0
     */

    private static final long serialVersionUID = 1L;
    // 资源路径
    private static final String PATH = "/appmage/account";
    // 公众帐号逻辑层
    private static final app_acctManagerBiz accountBiz = new app_acctManagerBiz();
    // 公众帐号的类型
    static LinkedHashMap<String, String> tpList = new LinkedHashMap<String, String>();
    private static final BaseBiz baseBiz = new BaseBiz();

    /**
     * 公众帐号管理页面
     *
     * @param request
     * @param response
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        //查询开始时间
        long stratTime = System.currentTimeMillis();
        // 分页信息
        PageInfo pageInfo = new PageInfo();
        boolean isFirstEnter = pageSet(pageInfo, request);
        try {
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            // 用来存储公众帐号
            List<DynaBean> beans = new ArrayList<DynaBean>();

            // 获取查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            String account = request.getParameter("account");
            String username = request.getParameter("username");
            if (!isFirstEnter) {
                // 存放查询条件
                // 客户基本资料判断
                if (name != null && !"".equals(name.trim())) {
                    conditionMap.put("name", name);
                }
                if (code != null && !"".equals(code.trim())) {
                    conditionMap.put("code", code);
                }

                if (account != null && !"".equals(account.trim())) {
                    conditionMap.put("account", account);
                }

                if (username != null && !"".equals(username.trim())) {
                    //创建人
                    conditionMap.put("username", username);
                }


            }

            beans = accountBiz.findAllAccountByCorpCode(lgcorpcode, conditionMap, pageInfo);
            //如果虽然有该文件路径，但是该文件不存在就设置为默认图片
            for (int i = 0; i < beans.size(); i++) {
                DynaBean bean = beans.get(i);
                if (bean != null && bean.get("img") != null && !"".equals(bean.get("img"))) {
                    if (checkFile(bean.get("img") + "")) {
                        continue;
                    } else {
                        bean.set("img", null);
                    }
                }
            }
            //查询数据总共记录数
            int count = accountBiz.getRecordCount(lgcorpcode);
            request.setAttribute("count", count);
            request.setAttribute("conditionMap", conditionMap);
            request.setAttribute("beans", beans);

            //当前登录用户的登录名
            String lguserId = null;
            String lguserName = null;
            Object sysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (sysuserObj != null) {
                LfSysuser sysuser = (LfSysuser) sysuserObj;
                lguserName = (sysuser.getUserName() != null && !"".equals(sysuser.getUserName())) ? sysuser.getUserName() : null;
                lguserId = String.valueOf(sysuser.getUserId() != null ? sysuser.getUserId() : null);
            }

            //查询出的数据的总数量
            int totalCount = pageInfo.getTotalRec();
            //日志信息
            String opContent = "开始时间：" + sdf.format(stratTime) + " 耗时：" +
                    (System.currentTimeMillis() - stratTime) + "ms  数量：" + totalCount;

            EmpExecutionContext.info("APP企业帐号管理", lgcorpcode, lguserId, lguserName, opContent, "GET");

        } catch (Exception e) {
            EmpExecutionContext.error(e, "APP公众帐号管理页面加载出错！");
        } finally {
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(PATH + "/app_acctManager.jsp").forward(request, response);
        }
    }


    /**
     * 检查文件是存储在
     *
     * @param fileUrl
     * @return
     */
    protected boolean checkFile(String fileUrl) {
        TxtFileUtil tfu = new TxtFileUtil();
        //结果
        boolean result = false;
        try {
            //检查文件是否存在并返回结果
            result = tfu.checkFile(fileUrl);
        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "检查文件是否存在出现异常！");
        }
        //返回结果
        return result;
    }

    /**
     * 公众帐号新增页面
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void doEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String tmId = request.getParameter("tmId");
            LfAppAccount account = accountBiz.getById(LfAppAccount.class, Long.parseLong(tmId));
            //如果虽然有该文件路径，但是该文件不存在就设置为默认图片
            String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
            if (account != null && account.getImg() != null && !"".equals(account.getImg())) {
                if (!checkFile(account.getImg())) {//
                    account.setImg(iPath + "/appmage/account/img/default_acct.png");
                }
            } else if (account != null && ("".equals(account.getImg()) || account.getImg() == null)) {
                account.setImg(iPath + "/appmage/account/img/default_acct.png");
            }
            //从session里面取值
            String corpCode = "";
            String lguserid = "";
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                corpCode = loginSysuser.getCorpCode();
                lguserid = loginSysuser.getUserId().toString();
            }
            request.setAttribute("lgcorpcode", corpCode);
            request.setAttribute("lguserid", lguserid);
            request.setAttribute("account", account);
            request.setAttribute("tmId", tmId);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "公众帐号管理-公众帐号新增页面加载错误！");
        } finally {
            request.getRequestDispatcher(PATH + "/app_editAccount.jsp").forward(request, response);
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
            //从session里面取值
            String corpCode = "";
            String lguserid = "";
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                corpCode = loginSysuser.getCorpCode();
                lguserid = loginSysuser.getUserId().toString();
            }
            request.setAttribute("lgcorpcode", corpCode);
            request.setAttribute("lguserid", lguserid);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "公众帐号管理-公众帐号新增页面加载错误！");
        } finally {
            request.getRequestDispatcher(PATH + "/app_addAccount.jsp").forward(request, response);
        }
    }


    /**
     * 公众帐号新增页面
     * 调用接口
     * 1	登录成功
     * -1	未配置企业账户
     * -2	host或port为空
     * -3	用户名或密码为空
     * -4	初始化登录xmpp参数异常
     * -5	初始化并登录xmpp异常
     * 401	用户名密码无效
     * 408	用户请求超时
     * 409	冲突错误
     * 501	服务器不提供此功能
     * 502	服务器内部错误
     * 504	连接服务器超时
     * 其他	不可恢复错误
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void Add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //日志文本
        String opContent = null;
        // 头像地址
        String acctImg = "";
        // 图片存放的根目录
        String baseFileDir = getServletContext().getRealPath("file/app/head");
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
            upload.setSizeMax((long) 4 * 1024 * 1024);
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
                FileItem item = (FileItem) iter.next();
                String fieldName = item.getFieldName();

                if (!item.isFormField() && item.getName().length() > 0) {
                    // 由于界面上有了上传文件的处理，提交时候就不用上传了
                } else {
                    fieldMap.put(fieldName, item.getString("UTF-8").toString());
                }
            }

            String userId = fieldMap.get("lguserid");
            String name = fieldMap.get("name");                 //企业名称
            String fakeid = fieldMap.get("fakeid");             //企业编码
            String code = fieldMap.get("code");                 //APP账号
            String ip = fieldMap.get("ip");                     //通讯IP
            String lgcorpcode = fieldMap.get("lgcorpcode");
            String port = fieldMap.get("port");                 //通讯IP端口
            String tmId = fieldMap.get("tmId");                 //企业ID
            String contents = fieldMap.get("contents");         //备注
            String password = fieldMap.get("password");         //APP账号密码
            acctImg = fieldMap.get("company_logo");
            String fileSvrUrl = fieldMap.get("fileSvrUrl");    //文件服务器地址
            LfAppAccount account = null;
            request.setAttribute("lgcorpcode", lgcorpcode);
            request.setAttribute("lguserid", userId);
            if (validateAPPCode("fakeid", fakeid, lgcorpcode) == "exist" && (tmId == null || "".equals(tmId))) {
                request.setAttribute("result", "100");
                request.getRequestDispatcher("/app_acctmanager.htm?method=doAdd&lgcorpcode=" + lgcorpcode + "&lguserid=" + userId).forward(request, response);
                return;
            }
            if (validateAPPCode("code", code, lgcorpcode) == "exist" && (tmId == null || "".equals(tmId))) {
                request.setAttribute("result", "101");
                request.getRequestDispatcher("/app_acctmanager.htm?method=doAdd&lgcorpcode=" + lgcorpcode + "&lguserid=" + userId).forward(request, response);
                return;
            }
            if (tmId != null && !"".equals(tmId)) {
                // 表示是修改
                account = baseBiz.getById(LfAppAccount.class, tmId);
                opContent = "修改app企业账号。[企业名称,企业编码,app账户,账户密码,文件服务器地址,通讯IP,通讯IP端口,备注]" +
                        "(" + account.getName() + "," + account.getFakeid() + "," + account.getCode() + "," + account.getPwd() + "," +
                        account.getFileSvrUrl() + "," + account.getUrl() + "," + account.getPort() + "," + account.getInfo() + ")-->";

            } else {// 表示是新增

                // 再去数据库查下，防止重复添加导致问题
                account = accountBiz.getAppAccount(lgcorpcode);
                if (account != null) {
                    EmpExecutionContext.info("新增app企业账号，防止重复添加，到数据库再查询成功获取到app企业账户对象。");
                    tmId = account.getAid().toString();
                } else {
                    account = new LfAppAccount();
                }
            }
            account.setFileSvrUrl(fileSvrUrl);
            account.setApptype(0);

            if (name != null) {
                account.setName(name);
            }
            if (code != null) {
                account.setCode(code);
            }
            if (fakeid != null) {
                account.setFakeid(fakeid);
            }
            if (port != null && !"".equals(port)) {
                account.setPort(Integer.parseInt(port));
            }
            account.setCorpcode(lgcorpcode);
            account.setUrl(ip);

            if (!"".equals(acctImg)) {
                account.setImg(acctImg);
            }
            account.setInfo(contents);
            account.setPwd(password);
            if (tmId != null && !"".equals(tmId)) {// 表示是修改

                boolean result = baseBiz.updateObj(account);


                if (result) {
                    opContent += "(" + name + "," + fakeid + "," + code + "," + password + "," +
                            fileSvrUrl + "," + ip + "," + port + "," + contents + ")成功。";
                    IWgMwCommuteBiz biz = new WgMwCommuteBiz();
                    int log = biz.Login();
                    // 新增成功
                    if (log == 1) {
                        request.setAttribute("result", "true");
                    } else {
                        request.setAttribute("result", log + "");
                    }
                } else {
                    opContent += "(" + name + "," + fakeid + "," + code + "," + password + "," +
                            fileSvrUrl + "," + ip + "," + port + "," + contents + ")失败。";
                    // 失败
                    request.setAttribute("result", "false");
                }
                //增加操作日志
                Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
                if (loginSysuserObj != null) {
                    LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                    EmpExecutionContext.info("APP企业账号管理", loginSysuser.getCorpCode(),
                            loginSysuser.getUserId().toString(), loginSysuser
                                    .getUserName(), opContent, "UPDATE");
                }
                request.getRequestDispatcher("/app_acctmanager.htm?method=doEdit&tmId=" + tmId + "&lgcorpcode=" + lgcorpcode + "&lguserid=" + userId).forward(request, response);
            } else {// 表示是新增

                account.setCreatetime(new Timestamp(System.currentTimeMillis()));
                if (userId != null && !"".equals(userId)) {
                    account.setCreater(Long.parseLong(userId));
                }

                Long addTemp = baseBiz.addObjProReturnId(account);

                if (addTemp != null && addTemp > 0) {

                    IWgMwCommuteBiz biz = new WgMwCommuteBiz();
                    int log = biz.Login();
                    // 新增成功
                    opContent = "新增app企业账号。[企业名称,企业编码,app账户,账户密码,文件服务器地址,通讯IP,通讯IP端口,备注]" +
                            "(" + name + "," + fakeid + "," + code + "," + password + "," +
                            fileSvrUrl + "," + ip + "," + port + "," + contents + ")成功。";
                    if (log == 1) {
                        request.setAttribute("result", "true");
                    } else {
                        request.setAttribute("result", log + "");
                    }
                } else {
                    opContent = "新增app企业账号。[企业名称,企业编码,app账户,账户密码,文件服务器地址,通讯IP,通讯IP端口,备注]" +
                            "(" + name + "," + fakeid + "," + code + "," + password + "," +
                            fileSvrUrl + "," + ip + "," + port + "," + contents + ")失败。";
                    // 新增失败
                    request.setAttribute("result", "false");
                }
                //增加操作日志
                Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");

                if (loginSysuserObj != null) {
                    LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                    EmpExecutionContext.info("APP企业账号管理", loginSysuser.getCorpCode(),
                            loginSysuser.getUserId().toString(), loginSysuser
                                    .getUserName(), opContent, "ADD");
                }

                request.getRequestDispatcher(PATH + "/app_addAccount.jsp?lgcorpcode=" + lgcorpcode + "&lguserid=" + userId).forward(request, response);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "公众帐号管理-公众帐号新增页面加载错误！");
        }

    }

    /**
     * 校验是否重复
     *
     * @param
     * @param
     * @throws IOException
     * @throws ServletException
     */
    public String validateAPPCode(String name, String value, String corpCode) throws ServletException, IOException {
        String st = "true";
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put(name, value);
        conditionMap.put("corpcode", corpCode);
        if (conditionMap != null) {
            try {

                List<LfAppAccount> wxDatas = baseBiz.getByCondition(LfAppAccount.class, conditionMap, null);
                if (wxDatas != null && wxDatas.size() > 0) {
                    st = "exist";
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "验证编码是否存在异常!");
            }
        }
        return st;
    }

    /**
     * 校验是否能连接
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void testConn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String urlip = request.getParameter("urlip");
        String port = request.getParameter("port");
        String fileSvrUrl = request.getParameter("fileSvrUrl");
        int socketConnectionCount = 100;
        int socketConnectionTime = 30;//30秒
        String value = "sucess";
        URL url = null;
        Socket socket = null;
        try {
            socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(urlip, Integer.parseInt(port));
            int communicateTime = 0; // 当前已尝试连接的次数
            while (communicateTime < socketConnectionCount) {
                socket.connect(inetSocketAddress, socketConnectionTime * 1000);
                communicateTime++;
                if (socket.isConnected()) {
                    break;
                }
            }
            //文件服务器地址访问是否正确
            try {
                url = new URL(fileSvrUrl);
                InputStream in = url.openStream();
                in.close();
            } catch (IOException e) {
                value = "fileError";
                EmpExecutionContext.error(e, "测试文件服务器地址连接失败!");
            }

        } catch (MalformedURLException e) {
            value = "ipError";
            EmpExecutionContext.error(e, "测试通讯IP及端口连接失败!");
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error("流关闭异常！");
            }

        }
        PrintWriter writer = response.getWriter();
        writer.print(value);
    }


    /*
     * 上传单图片
     */
    public void uploadImg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dirUrl = new TxtFileUtil().getWebRoot();
        // 图片存放的根目录
        String baseFileDir = getServletContext().getRealPath("file/app/head");
        response.setContentType("text/html");
        // 构造出文件工厂，用于存放JSP页面中传递过来的文件
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置缓存大小，如果文件大于缓存大小时，则先把文件放到缓存中
        factory.setSizeThreshold(1024 * 1024);
        // 设置上传文件的保存路径
        factory.setRepository(new File(baseFileDir));
        // 产生Servlet上传对象
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置可以上传文件大小的上界4MB
        upload.setSizeMax((long) 1 * 1024 * 1024);
        // 取得所有上传文件的信息
        List<FileItem> fileList = null;
        // 上传图片保存url路径
        String imgUrl = "";
        try {
            fileList = upload.parseRequest(request);
        } catch (FileUploadException e) {
            EmpExecutionContext.error(e, "个性化界面设置解析上传对象异常！");
            return;
        }
        Iterator<FileItem> iter = fileList.iterator();
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            String fieldName = item.getFieldName();
            if (!item.isFormField() && item.getName().length() > 0) {
                // 文件名
                String fileName = item.getName();
                // 获得文件大小

                BufferedImage sourceImg = ImageIO.read(item.getInputStream());
                int width = sourceImg.getWidth();//获得宽
                int height = sourceImg.getHeight();//获得高
                // 判断文件大小是否超过限制
                if (width > 50 || height > 50) {
                    item.delete();
                    item = null;
                    response.getWriter().print("overSize");
                    return;
                }

                // 文件类型
                String allImgExt = ".jpg|.jpeg|.gif|.bmp|.png|";
                String extName = fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();

                // 如果是图片文件
                if (allImgExt.indexOf(extName + "|") != -1) {
                    Calendar cal = Calendar.getInstance();
                    // 年
                    int year = cal.get(Calendar.YEAR);
                    // 月
                    int month = cal.get(Calendar.MONTH) + 1;
                    // 日
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    String filepath = baseFileDir + File.separator + year + File.separator + month + File.separator + day;
                    File uf = new File(filepath);
                    // 更改文件的保存路径，以防止文件重名的现象出现
                    if (!uf.exists()) {
                        uf.mkdirs();
                    }
                    try {
                        String strid = UUID.randomUUID().toString();
                        String newFileName = strid.replaceAll("-", "") + StaticValue.getServerNumber() + extName;
                        File uploadedFile = new File(filepath, newFileName);
                        if (uploadedFile.exists()) {
                            boolean state = uploadedFile.delete();
                            if (!state) {
                                EmpExecutionContext.error("删除失败！");
                            }
                        }

                        item.write(uploadedFile);
                        //如果此处需要压缩就增加
//							String pressFileName = strid.replaceAll("-", "")+ StaticValue.SERVER_NUMBER+"_press" + extName;
//							CompressPic mypic = new CompressPic();
//							String picres=mypic.compressPic(filepath+ File.separator, filepath+ File.separator, newFileName, pressFileName, 80, 80, true); 
//							if("ok".equals(picres)){//压缩成功
//								imgUrl ="file/app/head/" + year + "/" + month + "/" + day + "/" + pressFileName;
//							}else{
                        imgUrl = "file/app/head/" + year + "/" + month + "/" + day + "/" + newFileName;
//							}
                        fieldMap.put(fieldName, imgUrl);
                    } catch (Exception e) {
                        EmpExecutionContext.error(e, "个性化界面设置上传图像失败！");
                        return;
                    }
                    //不做上传APP服务器的处理
//						finally{
//							//使用集群，将文件上传到文件服务器
//							if(StaticValue.ISCLUSTER ==1){
//								//上传文件到文件服务器
//								CommonBiz comBiz = new CommonBiz();
//								boolean upFileRes = comBiz.upFileToFileServer(imgUrl);
//								if(!upFileRes){
//									EmpExecutionContext.error("个性化界面设置上传文件到服务器失败。错误码：" + IErrorCode.B10039);
//									return;
//								}
//							}
//						}
                }
            }
        }
        response.getWriter().print(imgUrl);
    }


}
