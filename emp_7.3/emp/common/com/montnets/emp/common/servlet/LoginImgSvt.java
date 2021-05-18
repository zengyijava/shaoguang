/**
 * Program : LoginImgSvt.java
 * Author : zousy
 * Create : 2013-11-20 下午03:45:22
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.common.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.GenericEmpDAO;
import com.montnets.emp.entity.system.LfSpeUICfg;
import com.montnets.emp.util.TxtFileUtil;

/**
 * @author Administrator <510061684@qq.com>
 * @version 1.0.0
 * @2013-11-20 下午03:45:22
 */
public class LoginImgSvt extends HttpServlet
{

    /**
     * doget方法
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

    /**
     * dopost方法
     */
    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding(StaticValue.ENCODING);
        response.setCharacterEncoding(StaticValue.ENCODING);
        String method = request.getParameter("method");
        // 请求路径
        String requestUrl = request.getServletPath()
                + (request.getPathInfo() == null ? "" : request.getPathInfo());
        try {

            Method clazzMethod = getValidMethod(method);
            if(clazzMethod == null){
                EmpExecutionContext.error("请求方法名称不合法 url:"+requestUrl+",method:"+method);
                return;
            }
            Object[] Objparams = { request, response };
            clazzMethod.invoke(this, Objparams);
        } catch (IllegalAccessException e) {
            EmpExecutionContext.error(e,"非法的方法访问权限！url:"+requestUrl+",method:"+method);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "方法转发异常。url:"+requestUrl+",method:"+method);
        }
    }

    /**
	 * 异步请求资源路径(该方法已不被使用)
	 * @description    
	 * @param request
	 * @param response
	 * @throws IOException       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-11-26 上午10:05:07
	 */
	public void getLoginImg(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String url = request.getParameter("url");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		if(url == null || "".equals(url.trim()))
		{
			out.print("");
			return;
		}
		String servletPath = request.getSession(false).getServletContext().getRealPath("/");
		File f = new File(servletPath + url);
		// 文件本地不存在 且为集群时 则 从文件服务器上下载文件
		if(!f.exists() && StaticValue.getISCLUSTER() == 1)
		{
			CommonBiz comBiz = new CommonBiz();
			String downMsg = comBiz.downloadFileFromFileCenter(url);
			if("error".equals(downMsg))
			{
				EmpExecutionContext.error("从文件服务器下载文件" + url + "失败！");
			}
		}
		if(f.exists())
		{
			out.print(url);
		}
		else
		{
			out.print("");
		}
	}
	
	
	public void getCfgInfo(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		//查找当前登录操作员企业下个性化设置信息
		String corpCode = "100001";
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		conditionMap.put("corpCode", corpCode);
		List<LfSpeUICfg> cfgs;
		JSONObject json =new JSONObject();
		try
		{
			cfgs = new GenericEmpDAO().findListByCondition(LfSpeUICfg.class, conditionMap, null);
			if(cfgs.size()>0){
				//个性化设置信息
				LfSpeUICfg cfg = cfgs.get(0);
				String dispContent = cfg.getDispContent();
				String loginLogo = cfg.getLoginLogo();
				String bgImg = cfg.getBgImg();
				loginLogo=downloadFile(loginLogo);
				bgImg=downloadFile(bgImg);
				json.put("dispContent", dispContent);
				json.put("loginLogo", loginLogo);
				json.put("bgImg", bgImg);
			}else{
				//json.put("dispContent", "11100000");
				//没有查询到数据,显示默认的数据
				json.put("dispContent", "11111000");
				json.put("loginLogo", "");
				json.put("bgImg", "");	
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取个性化设置失败！");
		}finally{
			out.print(json.toString());
		}
	}
	
	public String downloadFile(String url){
		if(url!=null&&!"".equals(url.trim())){
			String servletPath=new TxtFileUtil().getWebRoot();
			File f = new File(servletPath+url);
			//文件本地不存在 且为集群时 则 从文件服务器上下载文件
			if(!f.exists()&& StaticValue.getISCLUSTER() ==1){
					CommonBiz comBiz = new CommonBiz();
					String downMsg = comBiz.downloadFileFromFileCenter(url);
					if("error".equals(downMsg)){
					EmpExecutionContext.error("从文件服务器下载文件"+url+"失败！");
					}
			}
			if(!f.exists()){
				url="";
			}
		}else{
			url="";
		}
		return url;
	}

    /**
     * 判断是否有效的方法名 数字、字母、下划线、$组成 并且不能以数字开头 且 当前请求servlet存在对应的方法
     * @param method
     * @return
     */
    private Method getValidMethod(String method){

        if(method == null){return null;}
        Pattern pattern = Pattern.compile("^[a-zA-Z$_][a-zA-Z$_0-9]*$");
        if(pattern.matcher(method).matches()){
            Class c = this.getClass();
            Method[] methods = c.getMethods();
            for (Method m : methods) {
                Class[] paraTypes = m.getParameterTypes();
                if(method.equals(m.getName()) && m.getReturnType().getClass().isInstance(Void.TYPE) //方法名相同 返回值void
                        && paraTypes != null && paraTypes.length==2 //2个参数
                        && paraTypes[0].getClass().isInstance(HttpServletRequest.class)&&paraTypes[1].getClass().isInstance(HttpServletResponse.class)){//两个参数分别为HttpServletRequest，HttpServletResponse
                    return m;
                }
            }
        }
        return null;
    }

    /**
     * 加载图片失败时 在文件服务器上查找
     * @param request
     * @param response
     * @throws IOException
     */
    public void imgfix(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String res = "";
        try
        {
            String fileUrl = request.getParameter("url");
            if(fileUrl == null){
				fileUrl = "";
			}
            if(fileUrl != null && fileUrl.startsWith("http://"))
            {
                fileUrl = fileUrl.replaceFirst("^(.*)?/file/","file/");
            }
            fileUrl = fileUrl.replaceFirst("^/","");
            if(!fileUrl.startsWith("file/"))
            {
                return;
            }
            if(res.length() > 0)
            {
                res = StaticValue.BASEURL+downloadFile(fileUrl);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"处理图片请求转发异常！");
        }finally{
            out.print(res);
        }
    }
}
