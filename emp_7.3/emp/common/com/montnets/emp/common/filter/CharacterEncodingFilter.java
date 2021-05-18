package com.montnets.emp.common.filter;

import com.montnets.emp.common.constant.StaticValue;

import javax.servlet.*;
import java.io.IOException;


/**
 * 字符编码过滤
 * @author Administrator
 *
 */
public class CharacterEncodingFilter implements Filter
{
	protected FilterConfig filterConfig = null;

    /**
     * 编码
     */
	protected String encoding;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.filterConfig = filterConfig;
		//获取系统web.xml配置的字符编码
		this.encoding = filterConfig.getInitParameter("encoding");
		
		//如果么有配置，获取staticValue给的字符编码
		if(this.encoding == null){
			this.encoding = StaticValue.ENCODING;
		}

	}
	
	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException
	{
		if (encoding != null){
			//设置请求编码
			servletRequest.setCharacterEncoding(encoding);
			//设置响应编码
			servletResponse.setCharacterEncoding(encoding);
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy()
	{
		filterConfig = null;
		encoding = null;
	}

	
}