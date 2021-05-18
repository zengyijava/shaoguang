package com.montnets.emp.weix.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.entity.weix.LfWcAlink;
import com.montnets.emp.entity.weix.LfWcMsg;
import com.montnets.emp.weix.biz.WeixBiz;
import com.montnets.emp.weix.common.util.GlobalMethods;

@SuppressWarnings("serial")
public class WeixCommSvt extends HttpServlet
{
	private BaseBiz	baseBiz	= new BaseBiz();

	private WeixBiz	weixBiz	= new WeixBiz();

	/**
	 * EMP企业微信公众平台验证接入
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		// 接入地址
		String url = request.getRequestURI();
		// 字段URL，提取唯一身份标识 
		String identity = url.substring(url.lastIndexOf("/") + 1, url.length());
		// 查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		// TOKEN标志
		String token = "";
		PrintWriter out = response.getWriter();

		try
		{
			conditionMap.put("url", identity);
			List<LfWcAccount> acctList = baseBiz.getByCondition(LfWcAccount.class, conditionMap, null);
			if(acctList != null && acctList.size() > 0)
			{
				LfWcAccount acct = acctList.get(0);
				token = acct.getToken();
			}
			//验证是否通过
			boolean vpass = weixBiz.verifySignature(signature, timestamp, nonce, token);

			if(vpass)
			{
				out.print(echostr);
			}
		}
		catch (NoSuchAlgorithmException ex)
		{
			EmpExecutionContext.error(ex,"EMP企业微信公众平台验证是否通过异常");
			out.print("Exception:" + ex.getMessage());
		}
		catch (Exception ex)
		{
			EmpExecutionContext.error(ex,"EMP企业微信公众平台验证异常");
			out.print("Exception:" + ex.getMessage());
		}
		finally
		{
			out.close();
		}
	}

	/**
	 * EMP企业微信响应腾讯微信服务器POST请求接入方法
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String path = request.getContextPath();
		//服务器地址
		String weixBasePath = GlobalMethods.getWeixBasePath();
		// 获取微信服务器请求的XML格式数据
		String requestXml = "";
		// EMP企业公众平台返回的XML格式数据
		String responseXml = "";
		// 待返回msgXml格式消息
		String msgXml = "";
		// 请求参数的Map化，方便后面取值
		HashMap<String, String> paramsXmlMap = new HashMap<String, String>();
		// 当前公众帐号
		LfWcAccount acct = new LfWcAccount();
		// 当前ALink帐号
		LfWcAlink alink = new LfWcAlink();
		// 上行消息记录
		LfWcMsg lmsg = new LfWcMsg();
		// 下行消息记录
		LfWcMsg rmsg = new LfWcMsg();
		// 上行消息内容
		String msg = "";
		// 流程控制标识-是否空响应
		boolean flag = true;

		
		try
		{
			// EmpExecutionContext.debug("----EMP企业微信公众平台上行/下行消息开始----");
			requestXml = weixBiz.readXMLFromRequestBody(request);
			// 转码，解决乱码的问题
			requestXml = new String(requestXml.getBytes("iso8859-1"), "UTF-8");
			// EmpExecutionContext.debug("微信服务发送请求:" + requestXml.toString());
			paramsXmlMap = weixBiz.getParamsXmlMap(requestXml);
			// 获取当前公众帐号信息
			acct = weixBiz.findAccountByOpenId(paramsXmlMap.get("ToUserName"));
//findbugs:	if(acct.getAId() != null && !"".equals(acct.getAId()))
			if(acct.getAId() != null)
			{
				// 建立普通微信和EMP企业微信公众帐号的关联
				alink = weixBiz.addAlink(paramsXmlMap, acct);
				// 保存上行记录到LF_WC_MSG表中
				lmsg = weixBiz.createMsg(acct, alink, "0", requestXml, paramsXmlMap);

				// 消息类型判断(0:text;1:image;2:localtion;link:3;event:4)
				switch (lmsg.getMsgType())
				{
					case 0:
						// 文本消息处理
						msg = weixBiz.extractMsg(paramsXmlMap);
						if(msg != null && !"".equals(msg))
						{
							// 关键字匹配搜索逻辑,返回msgXml**/
							// TODO-关键字搜索规则在getKeywordMsgXml方法实现
							msgXml = weixBiz.getKeywordMsgXml(acct, msg);
							// EmpExecutionContext.debug("关键字匹配模板MSG_XML值:" + msgXml);
						}
						else
						{
							// TODO--(关键字为空时的处理-调用默认回复)
							msgXml = weixBiz.getDefaultReplyMsgXml(acct);
							// EmpExecutionContext.debug("默认回复MSG_XML值:" + msgXml);
						}
						break;
					case 1:
						// TODO图片消息处理
						//weixBiz.getLocationMsgXml(paramsXmlMap);
						msgXml = "";
						break;
					case 2:
						// TODO地理位置消息处理
						//weixBiz.getLocationMsgXml(paramsXmlMap);
						msgXml = "";
						break;
					case 4:
						// 事件类消息处理(subscribe,unsubscribe,CLICK)消息
						if("subscribe".equals(paramsXmlMap.get("Event")))
						{
							// 关注时回复已经取消选择模板功能，回复内容为简单的文本
							msgXml = weixBiz.getSubscribeMsgXml(acct);
							if(msgXml != null && !"".equals(msgXml))
							{
								msgXml = weixBiz.attachInviteMsgXml(acct, alink.getWcId(), weixBasePath, msgXml);
							} else
							{
								msgXml = weixBiz.getInviteMemberMsgXml(acct, alink.getWcId(), weixBasePath);
							}
							//EmpExecutionContext.debug("关注事件MSG_XML值:" + msgXml);
						} 
						else if("CLICK".equals(paramsXmlMap.get("Event")))
						{
							String EventKey = paramsXmlMap.get("EventKey");
							msgXml = weixBiz.getMenuMsgXml(acct, EventKey);
							// 如果菜单没有关联图文获取msgXML为空，则将noReply设置为true
							if(msgXml == null || "".equals(msgXml))
							{
								flag = false;
							}
						}
						else
						{
							// TODO(其他"事件"类型消息处理
							msgXml = "";
						}
						break;
					default:
						msgXml = "";
				}

				if(flag == true)
				{
					// msgXml为空执行默认回复处理
					if(msgXml == null || "".equals(msgXml))
					{
						msgXml = weixBiz.getDefaultReplyMsgXml(acct);
						// EmpExecutionContext.debug("默认回复MSG_XML值:" + msgXml);
					}

					// 创建返回的responseXml数据
					responseXml = weixBiz.createResponseXml(paramsXmlMap, msgXml);

					// 准备返回数据
					// 检查responseXml是否符合条件
					if(!weixBiz.checkMsgXml(responseXml))
					{
						responseXml = weixBiz.getSystemDefaultResponseXml(paramsXmlMap);
					}

					// 保存下行记录到LF_WC_MSG表中
					HashMap<String, String> rparamsXmlMap = new HashMap<String, String>();
					rparamsXmlMap = weixBiz.getParamsXmlMap(responseXml);
					rmsg = weixBiz.createMsg(acct, alink, "1", responseXml, rparamsXmlMap);

					// 将PicUrl,Url,MusicUrl,HQMusicUrl等标签对中的{basePath}替换成当前服务器的路径
					responseXml = weixBiz.handleMsgXmlBasePath(responseXml, weixBasePath);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "EMP企业微信响应客户端请求失败！");
		}
		finally
		{
			response.setContentType("text/xml;charset=UTF-8");
			PrintWriter out = response.getWriter();
			// EmpExecutionContext.debug(responseXml);
			// EmpExecutionContext.debug("----EMP企业微信公众平台上行/下行消息结束----");
			/** 服务器响应的XML数据返回给微信服务器 **/
			out.print(responseXml);
			out.close();
		}
	}

	/** 初始化 **/
	public void init() throws ServletException
	{

	}

	public WeixCommSvt()
	{
		super();
	}

	public void destroy()
	{
		super.destroy();
	}
}
