package com.montnets.emp.weix.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.weix.biz.AccountBiz;
import com.montnets.emp.weix.common.util.RandomStrUtil;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("serial")
public class AcctManagerSvt extends BaseServlet {
    /**
     * 操作模块
     */
    private final String opModule = "公众帐号管理";

    /**
     * 公众帐号逻辑层
     */
    private final AccountBiz accountBiz = new AccountBiz();
    /**
     * 资源路径
     */
    private static final String PATH = "/weix/account";
    /**
     * 基础逻辑层
     */
    private final BaseBiz baseBiz = new BaseBiz();
    /**
     * 公众帐号的类型
     */
    private static LinkedHashMap<String, String> tpList = new LinkedHashMap<String, String>();

    /**
     * 公众帐号管理页面
     *
     * @param request
     * @param response
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 分页信息
        PageInfo pageInfo = new PageInfo();
        boolean isFirstEnter = pageSet(pageInfo, request);
        try {
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            // 用来存储公众帐号
            List<DynaBean> beans = new ArrayList<DynaBean>();
            // 存放查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

            if (!isFirstEnter) {
                // 获取查询条件
                String name = request.getParameter("name");
                String ghname = request.getParameter("ghname");

                // 客户基本资料判断
                if (name != null && !"".equals(name.trim())) {
                    conditionMap.put("name", name.trim());
                }
                if (ghname != null && !"".equals(ghname.trim())) {
                    conditionMap.put("ghname", ghname.trim());
                }
            }

            beans = accountBiz.findAllAccountByCorpCode(lgcorpcode, conditionMap, pageInfo);

            request.setAttribute("beans", beans);

        } catch (Exception e) {
            EmpExecutionContext.error(e, opModule + "-公众帐号列表页面加载出错！");
        } finally {
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(PATH + "/accountList.jsp").forward(request, response);
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
            // 公众帐号类型
            tpList = getAcctType();
            request.setAttribute("tpList", tpList);
        } catch (Exception e) {
            EmpExecutionContext.error(e, opModule + "-公众帐号新增页面加载错误！");
        } finally {
            request.getRequestDispatcher(PATH + "/addAccount.jsp").forward(request, response);
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
            tpList = getAcctType();
            LfWcAccount acct = baseBiz.getById(LfWcAccount.class, aId);
            request.setAttribute("tpList", tpList);
            request.setAttribute("acct", acct);
        } catch (Exception e) {
            EmpExecutionContext.error(e, opModule + "-公众帐号编辑页面加载错误！");
        } finally {
            request.getRequestDispatcher(PATH + "/editAccount.jsp").forward(request, response);
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
        // 图片存放的根目录
        String baseFileDir = getServletContext().getRealPath(StaticValue.WC_ACCOUNT);
        boolean isOverSize = false;
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
                FileItem item = (FileItem) iter.next();
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
                        isOverSize = true;
                        break;
                    }

                    // 文件类型
                    String allImgExt = ".jpg|.jpeg|.gif|.bmp|.png|";
                    String extName = fileName.substring(fileName.indexOf("."), fileName.length()).toLowerCase();

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
                            String newFileName = strid.replaceAll("-", "") + StaticValue.getServerNumber() + extName;
                            File uploadedFile = new File(filepath, newFileName);
                            if (uploadedFile.exists()) {
                                if (!uploadedFile.delete()) {
                                    throw new FileUploadException();
                                }
                            }
                            item.write(uploadedFile);

                            acctImg = StaticValue.WC_ACCOUNT + "/" + year + "/" + month + "/" + day + "/" + newFileName;
                        } catch (FileUploadException e) {
                            EmpExecutionContext.error(e, "公众帐号上传图像失败！");
                            throw e;
                        } finally {
                            //使用集群，将文件上传到文件服务器
                            if (StaticValue.getISCLUSTER() == 1) {
                                //上传文件到文件服务器
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
            // 操作类型
            OpType = fieldMap.get("OpType");
            // 公众帐号描述
            String acctInfo = fieldMap.get("acctInfo");

            // 新增公众帐号
            if ("add".equals(OpType)) {
                LfWcAccount acct = new LfWcAccount();
                acct.setName(acctName);
                acct.setType(Integer.valueOf(acctType));
                acct.setCode(acctCode);
                acct.setOpenId(acctOpenId);
                acct.setInfo(acctInfo);
                acct.setCorpCode(lgcorpcode);
                acct.setIsApprove(0);
                acct.setImg(acctImg);
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
                    //增加操作日志
                    Object loginSysuserObj = request.getSession().getAttribute("loginSysuser");
                    if (loginSysuserObj != null) {
                        LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;

                        EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(),
                                loginSysuser.getUserId().toString(), loginSysuser.getUserName(),
                                "添加微信公众帐号（帐号名称:" + acctName + "，微信帐号:" + acctCode + "，原始帐号:" + acctOpenId + "）成功",
                                StaticValue.ADD);
						/*EmpExecutionContext.info("模块名称：" + opModule + "，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"添加微信公众帐号（帐号名称:"+acctName+"，微信帐号:"+acctCode+"，原始帐号:"+acctOpenId+"）成功");*/
                    }
                    request.setAttribute("tmresult", "true");
                } else {
                    // 新增失败
                    EmpExecutionContext.error("模块名称：" + opModule + "，企业：" + lgcorpcode + "，添加微信公众帐号（帐号名称:" + acctName + "，微信帐号:" + acctCode + "，原始帐号:" + acctOpenId + "）失败");
                    request.setAttribute("tmresult", "false");
                }
            } else if ("edit".equals(OpType)) {
                LfSysuser user = (LfSysuser) request.getSession().getAttribute("loginSysuser");
                // 修改模板
                LfWcAccount acct = baseBiz.getById(LfWcAccount.class, aId);
                acct.setName(acctName);
                acct.setType(Integer.valueOf(acctType));
                acct.setCode(acctCode);
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
                    EmpExecutionContext.info(opModule, lgcorpcode,
                            user.getUserId().toString(), user.getUserName(),
                            "修改微信公众帐号（帐号名称:" + acctName + "，微信帐号:" + acctCode + "，原始帐号:" + acctOpenId + "）成功",
                            StaticValue.UPDATE);
                    //EmpExecutionContext.info("模块名称：" + opModule + "，企业："+lgcorpcode+"，修改微信公众帐号（帐号名称:"+acctName+"，微信帐号:"+acctCode+"，原始帐号:"+acctOpenId+"）成功");
                    request.setAttribute("tmresult", "true");
                } else {
                    // 修改失败
                    EmpExecutionContext.error("模块名称：" + opModule + "，企业：" + lgcorpcode + "，修改微信公众帐号（帐号名称:" + acctName + "，微信帐号:" + acctCode + "，原始帐号:" + acctOpenId + "）失败");
                    request.setAttribute("tmresult", "false");
                }
                LfWcAccount newacct = baseBiz.getById(LfWcAccount.class, aId);
                request.setAttribute("acct", newacct);
            }
        } catch (Exception ex) {
            request.setAttribute("tmresult", "false");
            EmpExecutionContext.error(ex, opModule + "-保存公众帐号出错！");
        } finally {
            if ("add".equals(OpType)) {
                request.getRequestDispatcher(PATH + "/addAccount.jsp").forward(request, response);
            } else if ("edit".equals(OpType)) {
                request.getRequestDispatcher(PATH + "/editAccount.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher(PATH + "/addAccount.jsp").forward(request, response);
            }
        }
    }

    /**
     * 检查提交信息是否合法
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void doCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String words = "";
        try {
            // 请求验证类型
            String tp = request.getParameter("type");
            String corpCode = request.getParameter("corpCode");
            if ("add".equals(tp)) {
                String acctOpenId = request.getParameter("acctOpenId");
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("openId", acctOpenId);
                conditionMap.put("corpCode", corpCode);
                List<LfWcAccount> acctList = baseBiz.getByCondition(LfWcAccount.class, conditionMap, null);
                if (acctList != null && acctList.size() > 0) {
                    words = "repeat";
                }
            }
        } catch (Exception e) {
            words = "error";
            EmpExecutionContext.error(e, opModule + "-doCheck验证失败!");
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
        HttpSession session = request.getSession();
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
            EmpExecutionContext.error(e, opModule + "-doCheck验证失败!");
        } finally {
            // 异步返回结果
            response.setContentType("text/html");
            response.getWriter().print("@" + result);
        }
    }

    /**
     * 微信公众帐号的类型
     *
     * @return
     */
    private LinkedHashMap<String, String> getAcctType() {
        tpList.put("0", "生活时尚");
        tpList.put("1", "明星名人");
        tpList.put("2", "IT科技");
        tpList.put("3", "本地城市");
        tpList.put("4", "金融理财");
        tpList.put("5", "休闲娱乐");
        tpList.put("6", "文化教育");
        tpList.put("7", "机构品牌");
        tpList.put("8", "实用工具");
        tpList.put("9", "新闻资讯");
        tpList.put("10", "公益慈善");
        tpList.put("11", "企业品牌");
        tpList.put("12", "其他");
        return tpList;
    }
}
