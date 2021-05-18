package com.montnets.emp.global.servlet;


import com.montnets.emp.common.context.EmpExecutionContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;

public class glo_operWebSvt extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 996425178330896501L;


    //写文件时候要的换行符
    private final String line = System.getProperties().getProperty("line.separator");

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * dopost方法
     */
    @Override
    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //this.request = request;
            Class c = getClass();
            String method = request.getParameter("method");
            if (method == null || "".equals(method)) {
                find(request, response);
                return;
            }
            Class[] parameterTypes = {HttpServletRequest.class, HttpServletResponse.class};
            Method clazzMethod = c.getMethod(method, parameterTypes);
            Object[] Objparams = {request, response};
            clazzMethod.invoke(this, Objparams);
        } catch (SecurityException e) {
            EmpExecutionContext.error(e, "调用servlet方法异常！");
        } catch (NoSuchMethodException e) {
            EmpExecutionContext.error(e, "servlet对应方法不存在！");
        } catch (IllegalArgumentException e) {
            EmpExecutionContext.error(e, "调用servlet方法异常！");
        } catch (IllegalAccessException e) {
            EmpExecutionContext.error(e, "调用servlet方法异常！");
        } catch (InvocationTargetException e) {
            EmpExecutionContext.error(e, "调用servlet方法异常！");
        }
    }

    public void find(HttpServletRequest request, HttpServletResponse response) {
        try {

            request.getRequestDispatcher("/common/glo_operWeb.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "svt跳转到glo_operWeb.jsp异常。");
        }
    }


    /**
     * 处理WEB.XML文件
     *
     * @param request
     * @param response
     */
    public void getweb(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            String pathUrl = getWebRoot() + "WEB-INF/web.xml";
            if (pathUrl != null && !"".equals(pathUrl) && checkFile(pathUrl)) {
                response.getWriter().print(getWebMsg(pathUrl));
            } else {
                response.getWriter().print("errorurl");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "处理WEB.XML文件异常。");
        }
    }

    /**
     * 解析文件内容
     *
     * @return
     */
    public String getWebMsg(String fileName) {

        StringBuffer menuStr = new StringBuffer("");
        BufferedReader br = null;
        String str = null;
        String returnMsg = "";
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
            LinkedHashSet<String> configNameSet = new LinkedHashSet<String>();
            //模块ID对应其模块名称
            LinkedHashMap<String, String> menuidnameMap = new LinkedHashMap<String, String>();
            //启用的模块
            List<String> menuList = new ArrayList<String>();
            //未启用的模块
            List<String> unmenuList = new ArrayList<String>();
            List<String> configxmlList = new ArrayList<String>();
            while ((str = br.readLine()) != null) {
                if (str.contains("ENTITY") && str.contains("SYSTEM")) {
                    String configName = str.substring(str.indexOf("ENTITY") + 6, str.indexOf("SYSTEM")).trim();
                    if (configName != null && !"".equals(configName)) {
                        //用于判断是否存在
                        configNameSet.add(configName);
                        //保存存在的配置模块
                        configxmlList.add(configName);
                    }
                }
                if (str.contains("&") && str.contains(";")) {
                    str = str.trim();
                    //判断是启用还是注释的模块
                    //获取首几个字母
                    String substr = str.substring(0, 2);
                    //该模块处于启用状态
                    if (substr.contains("&")) {
                        distinguishUMenu(str, menuidnameMap, configNameSet, menuList);
                    } else if (substr.contains("<!")) {
                        distinguishNMenu(str, menuidnameMap, configNameSet, unmenuList);
                    }
                }
                str = null;
            }

            if (configxmlList != null) {
                for (int j = 0; j < configxmlList.size(); j++) {
                    String temp = configxmlList.get(j);
                    String menuname = menuidnameMap.get(temp);
                    if (menuname == null || "".equals(menuname)) {
                        menuname = getMenuName(temp);
                    }
                    menuStr.append("#").append(temp).append("_0_" + menuname);
                }
            }
            returnMsg = menuStr.toString();
            if (menuList != null) {
                for (int j = 0; j < menuList.size(); j++) {
                    String temp = menuList.get(j);
                    returnMsg = returnMsg.replace("#" + temp + "_0", "#" + temp + "_1");
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "svt解析文件内容异常。");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "svt解析文件内容，关闭资源异常。");
                }
            }
        }
        // System.out.println("获取web.xml的信息："+returnMsg);
        return returnMsg;
    }


    /**
     * 查询的时候  webxml 解析启用模块信息
     *
     * @param menustr       读取的一行信息
     * @param menuidnameMap ID与名字对应MAP
     * @param configNameSet system里的模块id
     * @param menuList      所启用的模块
     *                      &xtgl;	<!-- 系统管理 -->
     */
    public void distinguishUMenu(String menustr, LinkedHashMap<String, String> menuidnameMap, LinkedHashSet<String> configNameSet, List<String> menuList) {
        try {
            String name = menustr.substring(menustr.indexOf("&") + 1, menustr.indexOf(";"));
            //获取模块的名称
            String chname = "";
            if (name != null && !"".equals(name) && configNameSet.contains(name)) {
                //启用的模块
                menuList.add(name);
            }
            try {
                chname = menustr.substring(menustr.indexOf("<!--") + 4, menustr.indexOf("-->")).trim();
                if (chname != null && !"".equals(chname)) {
                    menuidnameMap.put(name, chname);
                } else {
                    //这里是获取默认的模块名称
                    chname = getMenuName(name);
                    menuidnameMap.put(name, chname);
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "查询的时候  webxml 解析启用模块信息异常。");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询的时候  webxml 解析启用模块信息异常。");
        }
    }


    /**
     * 查询的时候  webxml 	解析注释模块信息
     *
     * @param menustr       读取的一行信息
     * @param menuidnameMap ID与名字对应MAP
     * @param configNameSet system里的模块id
     * @param unmenuList    未启用的模块
     *                      <!-- &xtgl;系统管理 -->
     */
    public void distinguishNMenu(String menustr, LinkedHashMap<String, String> menuidnameMap, LinkedHashSet<String> configNameSet, List<String> unmenuList) {
        try {
            String name = menustr.substring(menustr.indexOf("&") + 1, menustr.indexOf(";"));
            //模块的名称
            String chname = "";
            if (name != null && !"".equals(name) && configNameSet.contains(name)) {
                unmenuList.add(name);
            }
            try {
                chname = menustr.substring(menustr.indexOf(";") + 1, menustr.indexOf("-->")).trim();
                if (chname != null && !"".equals(chname)) {
                    menuidnameMap.put(name, chname);
                } else {
                    //这里是获取默认的模块名称
                    chname = getMenuName(name);
                    menuidnameMap.put(name, chname);
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "查询的时候 webxml解析注释模块信息异常。");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询的时候 webxml解析注释模块信息异常。");
        }

    }


    /**
     * 修改web.xml文件
     *
     * @param request
     * @param response
     */
    public void updateWebXml(HttpServletRequest request, HttpServletResponse response) {
        String opModule = "请求处理WEBXML";
        String corpCode = null;
        String userId = null;
        String userName = null;
        String opContent = null;
        String opType = "OTHER";
        String result = null;
        String opStatus = "失败";
        StringBuffer sb = null;
        //获取出界面的模块ID_模块状态&
        String menustr = request.getParameter("menustr");
        //System.out.println(menustr);
        try {
            if (menustr == null || "".equals(menustr)) {
//				response.getWriter().print("");
                result = "";
                return;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "svt修改web.xml文件 异常。");
        }
        String[] menuNameAttrArr = menustr.split("&");
        List<String> menuList = new ArrayList<String>();
        LinkedHashMap<String, String> menuMap = new LinkedHashMap<String, String>();
        if (menuNameAttrArr != null && menuNameAttrArr.length > 0) {
            for (int i = 0; i < menuNameAttrArr.length; i++) {
                String[] temp = menuNameAttrArr[i].split("_");
                if (temp != null && temp.length > 0) {
                    menuList.add(temp[0]);
                    menuMap.put(temp[0], temp[1]);
                }
            }
        }
        String menunameStr = "";
        try {
            menunameStr = URLDecoder.decode(request.getParameter("menunamestr"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            EmpExecutionContext.error(e1, "svt解码异常。");
        }
        String[] menunameArr = menunameStr.split("&");
        LinkedHashMap<String, String> menunameMap = new LinkedHashMap<String, String>();
        if (menunameArr != null && menunameArr.length > 0) {
            for (int i = 0; i < menunameArr.length; i++) {
                String[] tempname = menunameArr[i].split("_");
                if (tempname != null && tempname.length > 0) {
                    menunameMap.put(tempname[0], tempname[1]);
                }
            }
        }

        String pathUrl = getWebRoot() + "WEB-INF/web.xml";
        BufferedReader br = null;
        String str = null;
        //配置模块前的str
        StringBuffer systemStr = new StringBuffer();
        //配置模块后的str
        StringBuffer systemStr1 = new StringBuffer();
        List<StringBuffer> bufferList = new ArrayList<StringBuffer>();

        //标认符
        boolean oneisFlag = true;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(pathUrl), "utf-8"));
            while ((str = br.readLine()) != null) {
                if (str.contains("<!--") && str.contains("-->") && str.contains(";")) {
                    oneisFlag = false;
                }
                if (oneisFlag) {
                    systemStr.append(str).append(line);
                }
                if (!oneisFlag && !str.contains("<!--") && !str.contains("-->") && !str.contains(";")) {
                    systemStr1.append(str).append(line);
                }
                if (str.contains("</web-app>")) {
                    break;
                }
            }
            bufferList.add(systemStr);
            bufferList.add(systemStr1);
            boolean flag = writeWebXml(pathUrl, menuList, menuMap, menunameMap, bufferList);
            sb = new StringBuffer();
            //遍历修改后的模块ID和模块状态
            for (Iterator it = menuMap.keySet().iterator(); it.hasNext(); ) {
                Object key = it.next();
                sb.append("模块ID：").append(key).append("，模块状态：").append(menuMap.get(key)).append("&");
//			    System.out.println( key+"="+ menuMap.get(key));
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }

            if (flag) {
                opStatus = "成功";
//				response.getWriter().print("success");
                result = "success";
            } else {
//				response.getWriter().print("fail");
                result = "fail";
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "修改web.xml文件异常。");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "修改web.xml文件，关闭资源异常。");
                }
            }
            opContent = "修改web.xml文件" + opStatus + "。（" + (sb==null?"":sb.toString()) + "）";
            EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
            try {
                response.getWriter().print(result);
            } catch (IOException e) {
            }
        }

    }


    /**
     * 写入web.xml
     *
     * @param pathUrl     web.xml路径
     * @param menuList    页面上所拥有的模块ID
     * @param menuMap     模块ID对应其模块状态
     * @param menunameMap 模块ID对应其模块名称
     * @param bufferList  web.xml其他信息
     * @return
     */
    public boolean writeWebXml(String pathUrl, List<String> menuList, LinkedHashMap<String, String> menuMap, LinkedHashMap<String, String> menunameMap
            , List<StringBuffer> bufferList) {
        boolean isFlag = false;
        try {
            StringBuffer updateMenuBuffer = new StringBuffer();
            if (menuList != null && menuList.size() > 0) {
                String menuid = null;
                String menuAttr = null;
                String menuname = null;
                for (int i = 0; i < menuList.size(); i++) {
                    //模块ID
                    menuid = menuList.get(i);
                    //模块状态
                    menuAttr = menuMap.get(menuid);
                    //模块名称
                    menuname = menunameMap.get(menuid);
                    //&xtgl;	<!-- 系统管理 -->
                    if (menuAttr != null && "1".equals(menuAttr) && !"".equals(menuAttr)) {
                        updateMenuBuffer.append("&").append(menuid).append(";").append("<!--").append(menuname).append("-->").append(line);
                    } else if (menuAttr != null && "2".equals(menuAttr) && !"".equals(menuAttr)) {
                        updateMenuBuffer.append("<!--").append("&").append(menuid).append(";").append(menuname).append("-->").append(line);
                    }
                }
            }
            StringBuffer contentBuffer = new StringBuffer();
            contentBuffer.append(bufferList.get(0)).append(updateMenuBuffer).append(bufferList.get(1));
            isFlag = writeToTxtFile(pathUrl, contentBuffer.toString());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "写入web.xml异常。");
            isFlag = false;
        }
        return isFlag;

    }


    /**
     * 写文件
     *
     * @param fileName
     * @param content
     * @return
     */
    public boolean writeToTxtFile(String fileName, String content) {
        Writer writer = null;
        try {
            File file = new File(fileName);
            //判断文件是否存在
            if (!file.exists() != false) {
                //新建文件
                boolean state = file.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("创建文件失败");
                }
            }
            writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            //将内容写入文件
            writer.write(content);
            //刷新流
            writer.flush();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "文件写入异常。");
            return false;
        } finally {
            try {
                if (writer != null) {
                    //关闭流
                    writer.close();
                }
            } catch (IOException e) {
                EmpExecutionContext.error(e, "文件关闭异常。");
            }
        }
        return true;

    }

    /**
     * 新增模块
     *
     * @param request
     * @param response
     */
    public void addMenu(HttpServletRequest request, HttpServletResponse response) {
        String opModule = "请求处理WEBXML";
        String corpCode = null;
        String userId = null;
        String userName = null;
        String opContent = null;
        String opType = "ADD";
        BufferedReader br = null;
        String returnMsg = "";
        String menuid = null;
        String menuname = "";
        try {
            request.setCharacterEncoding("utf-8");
            //模块ID
            menuid = request.getParameter("menuid");
            //模块名称

            try {
                menuname = URLDecoder.decode(request.getParameter("menuname"), "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                EmpExecutionContext.error(e1, "新增模块，解码异常。");
            }

            String pathUrl = getWebRoot() + "WEB-INF/web.xml";


            String str = null;
            //放SYSTEM的
            StringBuffer systemStr = new StringBuffer();

            //第一段的标认符
            boolean oneisFlag = true;
            //第二段的标认符
            boolean twoisFlag = false;
            //第三段的标认符
            boolean threeisFlag = false;
            //第四段的标认符
            boolean fourisFlag = false;
            String configName = "";


            br = new BufferedReader(new InputStreamReader(new FileInputStream(pathUrl), "utf-8"));
            while ((str = br.readLine()) != null) {
                //处理<web-app之前的字符  并且把需要增加的写进去
                if (str.contains("]")) {
                    oneisFlag = false;
                    //<!ENTITY cxtj SYSTEM  "cxtjConfig.xml">
                    configName = menuid + "Config.xml";
                    systemStr.append("<!ENTITY ").append(menuid).append(" SYSTEM ")
                            .append("\"").append(configName).append("\">").append(line)
                            .append(str).append(line);
                }
                if (oneisFlag) {
                    systemStr.append(str).append(line);
                }
                //处理<web-app>字符  写入
                if (str.contains("<web-app")) {
                    twoisFlag = true;
                }
                if (twoisFlag) {
                    systemStr.append(str).append(line);
                }
                if (twoisFlag && str.contains(">")) {
                    twoisFlag = false;
                }
                //处理&;字符  写入
                if (str.contains(";") && str.contains("<!--")) {
                    threeisFlag = true;
                }
                if (threeisFlag && !str.contains(";") && !str.contains("<!--")) {
                    systemStr.append("&").append(menuid).append(";").append("<!--").append(menuname).append("-->").append(line);
                    //systemStr.append(str).append(line);
                }
                if (threeisFlag) {
                    systemStr.append(str).append(line);
                }
            }

            boolean isFlag = writeToTxtFile(pathUrl, systemStr.toString());
            if (isFlag) {
                String content = "<?xml version='1.0' encoding='UTF-8' ?> ";
                String path = getWebRoot() + "WEB-INF/" + configName;
                try {
                    File file = new File(path);
                    if (!file.exists()) {
                        boolean state = file.createNewFile();
                        if (!state) {
                            EmpExecutionContext.error("创建文件失败");
                        }
                    }
                    writeToTxtFile(path, content);
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "新增模块，写文件异常。");
                    returnMsg = "fail";
                }
            }
            returnMsg = "success";

        } catch (Exception e) {
            returnMsg = "fail";
            EmpExecutionContext.error(e, "新增模块异常。");
        } finally {
            if (br != null) {
                try {
                    br.close();
                    String OpStatus = returnMsg.equals("success") ? "成功" : "失败";
                    //添加操作日志
                    opContent = "新增模块" + OpStatus + "。（模块ID：" + menuid + "，模块名称：" + menuname + "）";
                    EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
                    response.getWriter().print(returnMsg);
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "新增模块，关闭资源异常。");
                }
            }
        }
    }

    /**
     * 检测web.xml文件是否存在
     *
     * @param url 全路径
     * @return
     */
    public boolean checkFile(String url) {
        try {
            File file = new File(url);
            if (file.isFile() && file.exists()) {
                return true;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "检测web.xml文件是否存在异常。");
        }
        return false;
    }

    /**
     * 获取基本功能模块的名称，如果未在web.xml后加模块注释
     *
     * @param code
     * @return
     */
    public String getMenuName(String code) {
        String name = "";
        try {
            if ("xtgl".equals(code)) {
                name = "系统管理";
            } else if ("dxkf".equals(code)) {
                name = "短信客服";
            } else if ("dxzs".equals(code)) {
                name = "短信助手";
            } else if ("txgl".equals(code)) {
                name = "通信管理";
            } else if ("ydcw".equals(code)) {
                name = "移动财务";
            } else if ("ydwx".equals(code)) {
                name = "移动网讯";
            } else if ("ydcx".equals(code)) {
                name = "移动彩讯";
            } else if ("cxtj".equals(code)) {
                name = "查询统计";
            } else if ("znyq".equals(code)) {
                name = "智能引擎";
            } else if ("global".equals(code)) {
                name = "全局文件";
            } else if ("user".equals(code)) {
                name = "用户管理";
            } else if ("client".equals(code)) {
                name = "客户管理";
            } else if ("group".equals(code)) {
                name = "群组管理";
            } else if ("frame".equals(code)) {
                name = "主题框架";
            } else {
                name = code;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取基本功能模块的名称，如果未在web.xml后加模块注释异常。");
            name = code;
        }
        return name;
    }


    /**
     * 获取路径
     *
     * @return
     */
    public String getWebRoot() {
        String realUrl = getClass().getProtectionDomain().getCodeSource()
                .getLocation().getPath();
        String newUrl = "";

        if (realUrl.contains("/WEB-INF/")) {
            newUrl = realUrl.substring(0, realUrl.lastIndexOf("WEB-INF/"));
        }

        realUrl = newUrl.replace("%20", " ");////此路径不兼容jboss

        return realUrl;
    }

    /**
     * 解析文件内容
     *
     * @return
     */
    public List<String> getWebMsg1() {
        List<String> menuList = new ArrayList<String>();
        BufferedReader br = null;
        try {
            String fileName = getWebRoot() + "WEB-INF/web.xml";

            String str = null;
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
            //模块ID对应其模块名称 启用的模块
            while ((str = br.readLine()) != null) {
                if (str.contains("&") && str.contains(";")) {
                    str = str.trim();
                    //判断是启用还是注释的模块
                    //获取首几个字母
                    String substr = str.substring(0, 6);
                    //该模块处于启用状态
                    if (substr.contains("&")) {
                        String name = str.substring(str.indexOf("&") + 1, str.indexOf(";"));
                        //获取模块的名称
                        if (name != null && !"".equals(name)) {
                            //启用的模块
                            menuList.add(name);
                        }
                    }
                }
                str = null;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "解析文件内容异常。");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "解析文件内容，关闭资源异常。");
                }
            }
        }
        return menuList;
    }


}
