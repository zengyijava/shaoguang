package com.montnets.emp.weix.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.weix.*;
import com.montnets.emp.weix.biz.i.IWeixBiz;
import com.montnets.emp.weix.common.util.GlobalMethods;
import com.montnets.emp.weix.dao.KeywordDao;
import com.montnets.emp.weix.dao.MsgDao;
import com.montnets.emp.weix.dao.RtextDao;
import com.montnets.emp.weix.dao.TempleDao;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeixBiz extends SuperBiz implements IWeixBiz
{
	MsgDao	msgDao	= new MsgDao();

	/**
	 * 企业微信接入地址验证
	 * 
	 * @param signature:微信加密签名
	 * @param timestamp:时间戳
	 * @param nonce:随机数
	 * @param token:随机字符串
	 * @return
	 * @throws Exception
	 */
	@Override
    public boolean verifySignature(String signature, String timestamp, String nonce, String token) throws Exception
	{
		boolean flag = false;
		String[] array = {token,timestamp,nonce};
		try
		{
			Arrays.sort(array);
			String result = "";
			for (String i : array)
			{
				result += i;
			}
			String ret = "";
			ret = SHAsum(result.getBytes());
			if(ret.equalsIgnoreCase(signature))
			{
				flag = true;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"EMP企业微信对接验证失败 ！");
			//throw new NoSuchAlgorithmException("验证失败 ！");
		}
		return flag;
	}

	/**
	 * 读取微信服务器返回的XML数据，并返回String类型数据
	 * 
	 * @param request
	 * @return
	 */
	@Override
    public String readXMLFromRequestBody(HttpServletRequest request)
	{
		StringBuffer xml = new StringBuffer();
		String line = null;
		try
		{
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null)
			{
				xml.append(line);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"读取微信服务器返回的XML数据异常！");
		}
		return xml.toString();
	}

	/**
	 * 将请求参数转化成Map数据格式，方便读取
	 * 
	 * @param requestXml
	 * @return
	 */
	@Override
    @SuppressWarnings("unchecked")
	public HashMap<String, String> getParamsXmlMap(String requestXml)
	{
		HashMap<String, String> paramsXmlMap = new HashMap<String, String>();
		try
		{
			Document document = DocumentHelper.parseText(requestXml);
			Element items = document.getRootElement();

			for (Iterator i = items.elementIterator(); i.hasNext();)
			{
				Element item = (Element) i.next();
				paramsXmlMap.put(item.getName(), item.getText());
				EmpExecutionContext.debug(item.getName() + ":" + item.getText());
			}
		}
		catch (DocumentException e)
		{
			// 解析异常后处理
			EmpExecutionContext.error(e,"requestXml格式解析失败！");
		}

		return paramsXmlMap;
	}

	/**
	 * 查找企业所有公众帐号
	 * @param corpCode
	 * @return1
	 */
	@Override
    public List<LfWcAccount> findAllAccountByCorpcode(String corpCode)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", corpCode);
		List<LfWcAccount> acctList = new ArrayList<LfWcAccount>();
		try
		{
			if(corpCode != null && !"".equals(corpCode))
			{
				acctList = empDao.findListByCondition(LfWcAccount.class, conditionMap, null);
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取企业公众帐号失败！");
		}

		return acctList;
	}

	/**
	 * 创建消息历史记录
	 * weixBiz.createLMsg(acct,alink,"0",requestXml,paramsXmlMap);
	 * 
	 * @param paramsXmlMap
	 * @return
	 */
	@Override
    public LfWcMsg createMsg(LfWcAccount acct, LfWcAlink alink, String type, String msgXml, HashMap<String, String> paramsXmlMap)
	{
		// 消息类型
		String msgtype = paramsXmlMap.get("MsgType");
		Integer mTp = getMsgType(msgtype, type);
		LfWcMsg lmsg = new LfWcMsg();

		try
		{
			lmsg.setCorpCode(acct.getCorpCode());
			lmsg.setMsgType(mTp);

			lmsg.setWcId(alink.getWcId());
			lmsg.setAId(alink.getAId());
			lmsg.setType(Integer.valueOf(type));
			lmsg.setMsgXml(msgXml);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			lmsg.setCreateTime(Timestamp.valueOf(df.format(new Date())));
			if(msgtype != null && "text".equals(msgtype))
			{
				String content = new String(paramsXmlMap.get("Content"));
				lmsg.setMsgText(StringUtils.defaultString(content, ""));
			}
			else
			{
				// TODO-其他类型的消息获取消息的正文用于页面显示
				lmsg.setMsgText("");
			}
			// id程序自增 14的id已经去掉了，现在修改为514
			long value = empDao.getIdByPro(514, 1);
			lmsg.setMsgId(Long.valueOf(value));

			msgDao.createMsg(lmsg);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"创建上行或者下行记录失败！");
		}
		return lmsg;
	}

	/**
	 * 获取当前公众帐号
	 * 
	 * @param
	 * @return
	 */
	@Override
    public LfWcAccount findAccountByOpenId(String toUserName)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("openId", toUserName);
		LfWcAccount acct = new LfWcAccount();
		List<LfWcAccount> acctList;
		try
		{
			if(toUserName != null && !"".equals(toUserName))
			{
				acctList = empDao.findListByCondition(LfWcAccount.class, conditionMap, null);

				if(acctList != null && acctList.size() > 0)
				{
					acct = acctList.get(0);
				}
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过公众帐号openId获取当前公众帐号失败！");
		}

		return acct;
	}

	/**
	 * 获取当前微信帐号与当前公众帐号的关联关系
	 * 
	 * @param
	 * @return
	 */
	@Override
    public LfWcAlink addAlink(HashMap<String, String> paramsXmlMap, LfWcAccount acct)
	{
		String fromUserName = paramsXmlMap.get("FromUserName");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("openId", fromUserName);
		conditionMap.put("AId", String.valueOf(acct.getAId()));

		LfWcAlink alink = new LfWcAlink();
		LfWcUserInfo userInfo = new LfWcUserInfo();
		List<LfWcAlink> alinkList;
		try
		{
			alinkList = empDao.findListByCondition(LfWcAlink.class, conditionMap, null);

			if(alinkList != null && alinkList.size() > 0)
			{
				alink = alinkList.get(0);
			}
			else
			{
				// 关注时执行下面的操作
				if(!"unsubscribe".equals(paramsXmlMap.get("Event")))
				{
					Connection conn = empTransDao.getConnection();
					try
					{
						userInfo.setName("");
						userInfo.setCorpCode(acct.getCorpCode());
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						userInfo.setCreateTime(Timestamp.valueOf(df.format(new Date())));
						empTransDao.beginTransaction(conn);
						Long wcId = empTransDao.saveObjProReturnID(conn, userInfo);

						alink.setAId(acct.getAId());
						alink.setWcId(wcId);
						alink.setOpenId(fromUserName);
						empTransDao.save(conn, alink);

						empTransDao.commitTransaction(conn);
					}
					catch (Exception e)
					{
						empTransDao.rollBackTransaction(conn);
						EmpExecutionContext.error(e, "创建当前微信帐号与当前公众帐号的关联关系失败！");
					}
					finally
					{
						empTransDao.closeConnection(conn);
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取微信帐号与当前公众帐号的关联失败！");
		}

		return alink;
	}

	/**
	 * 提取关键字，目前只处理了文本消息关键字提取
	 * 
	 * @param paramsXmlMap
	 * @return
	 */
	@Override
    public String extractMsg(HashMap<String, String> paramsXmlMap)
	{
		// 上行消息类型
		String msgtype = paramsXmlMap.get("MsgType");
		String msg = null;
		if(msgtype != null && "text".equals(msgtype))
		{
			try
			{
				msg = paramsXmlMap.get("Content").trim();
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "提取关键字失败！");
			}
		}
		else
		{
			// TODO-其他类型的消息
			msg = "";
		}
		return msg;
	}

	/**
	 *关键字匹配搜索
	 * 
	 * @param acct
	 * @param msg
	 * @return
	 */
	@Override
    public String getKeywordMsgXml(LfWcAccount acct, String msg)
	{
		LfWcKeyword keywordTemp = new LfWcKeyword();
		String msgXml = "";
		try
		{
			// 匹配关键字
			List<LfWcKeyword> keywordsList = findKeyWordsByAccount(acct);
			if(keywordsList != null && keywordsList.size() > 0)
			{
				for (LfWcKeyword keyword : keywordsList)
				{
					if(keyword.getType() == 0 && msg.contains(keyword.getName()))
					{
						keywordTemp.setKId(keyword.getKId());
						break;
					}
					else
						if(keyword.getType() == 1 && msg.equals(keyword.getName()))
						{
							keywordTemp.setKId(keyword.getKId());
							break;
						}
				}

				// 如果匹配到关键字，则查询这个关键字对于的模板
				if(keywordTemp.getKId() != null)  //findbugs修改后的
				{
					List<DynaBean> beans = new ArrayList<DynaBean>();
					beans = findTemplateByKeywordId(String.valueOf(keywordTemp.getKId()));
					if(beans != null && beans.size() > 0)
					{
						msgXml = (String) beans.get(0).get("msg_xml");
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过关键字获取msgXml值失败！");
		}
		return msgXml;
	}

	/**
	 * 关注事件消息回复msgXMl
	 * 
	 * @param acct
	 * @return
	 */
	@Override
    public String getSubscribeMsgXml(LfWcAccount acct)
	{
		String msgXml = "";
		try
		{
			List<LfWcRevent> reventList = findSubscribeByAccount(acct);
			if(reventList != null && reventList.size() > 0)
			{
				LfWcRevent revent = reventList.get(0);
//findbugs:		if(revent.getTId() != null && !"0".equals(String.valueOf(revent.getTId())) && !"".equals(revent.getTId()))
				if(revent.getTId() != null && !"0".equals(String.valueOf(revent.getTId())))
				{
					// 从模板里面读XML，实现同步
					LfWcTemplate template = empDao.findObjectByID(LfWcTemplate.class, revent.getTId());
					if(template instanceof LfWcTemplate)
					{
						msgXml = template.getMsgXml();
					}
				}
				else
				{
					msgXml = revent.getMsgXml();
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取关注时回复的msgXml值失败！");
		}
		return msgXml;
	}

	/**
	 * 查询公众帐号-默认回复msgXml
	 * 
	 * @param acct
	 * @return
	 */
	@Override
    public String getDefaultReplyMsgXml(LfWcAccount acct)
	{
		String msgXml = "";
		try
		{
			List<LfWcRtext> rtextList = findDefaultReplyByAccount(acct);
			if(rtextList != null && rtextList.size() > 0)
			{
				Random r = new Random();
				int n = r.nextInt(rtextList.size());
				LfWcRtext rtext = rtextList.get(n);
//findbugs:		if(rtext.getTId() != null && !"0".equals(String.valueOf(rtext.getTId())) && !"".equals(rtext.getTId()))
				if(rtext.getTId() != null && !"0".equals(String.valueOf(rtext.getTId())))
				{
					// 从模板里面读XML，实现同步
					LfWcTemplate template = empDao.findObjectByID(LfWcTemplate.class, rtext.getTId());
					if(template instanceof LfWcTemplate)
					{
						msgXml = template.getMsgXml();
					}
				}
				else
				{
					msgXml = rtext.getMsgXML();
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取默认回复msgXML值失败！");
		}
		return msgXml;
	}

	/**
	 * 查询公众帐号所有关联关键字，时间倒序排列
	 * 
	 * @param acct
	 * @return
	 */
	@Override
    public List<LfWcKeyword> findKeyWordsByAccount(LfWcAccount acct)
	{
		List<LfWcKeyword> keywordsList = null;
		try
		{
			return new KeywordDao().findKeyWordsByAccount(acct);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"查找公众帐号对应的所有关键字失败！");
		}
		return keywordsList;
	}

	/**
	 * 查询关键字所对于的模板
	 * 
	 * @param kId
	 * @return
	 */
	@Override
    public List<DynaBean> findTemplateByKeywordId(String kId)
	{
		List<DynaBean> beans = null;
		try
		{
			return new TempleDao().findTemplateByKeywordId(kId);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过关键字获取对应模板失败！");
		}
		return beans;
	}

	/**
	 * 查询公众帐号-关注事件回复List<LfWcRevent>对象列表
	 * 
	 * @param acct
	 * @return
	 */
	@Override
    public List<LfWcRevent> findSubscribeByAccount(LfWcAccount acct)
	{
		List<LfWcRevent> reventList = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();

		conditionMap.put("AId", String.valueOf(acct.getAId()));
		conditionMap.put("evtType", "1");
		orderMap.put("createtime", "DESC");
		try
		{
			reventList = empDao.findListByCondition(LfWcRevent.class, conditionMap, orderMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过公众帐号查询关注时回复失败！");
		}
		return reventList;
	}

	/**
	 * 查询公众帐号-默认回复List<LfWcRtext>列表
	 * 
	 * @param acct
	 * @return
	 */
	@Override
    public List<LfWcRtext> findDefaultReplyByAccount(LfWcAccount acct)
	{
		List<LfWcRtext> rtextList = null;
		try
		{
			return new RtextDao().findDefaultReplyByAccount(acct);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过公众帐号查询默认回复失败！");
		}
		return rtextList;
	}

	/**
	 * 创建返回的xml数据，将ToUserName和FromUserName加上
	 * 
	 * @param toUser
	 * @param fromUser
	 * @param msgXml
	 * @return
	 */
	@Override
    public String createResponseXml(HashMap<String, String> paramsXmlMap, String msgXml)
	{
		String responseXml = msgXml;
		String toUser = paramsXmlMap.get("FromUserName");
		String fromUser = paramsXmlMap.get("ToUserName");
		if(responseXml.contains("ToUserName") && responseXml.contains("FromUserName"))
		{
			Date time = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(time);
			long createTime = cal.getTimeInMillis() / 1000;

			responseXml = responseXml.replaceAll("<ToUserName>(.*?)</ToUserName>", "<ToUserName>" + toUser + "</ToUserName>");
			responseXml = responseXml.replaceAll("<FromUserName>(.*?)</FromUserName>", "<FromUserName>" + fromUser + "</FromUserName>");
			responseXml = responseXml.replaceAll("<CreateTime>(.*?)</CreateTime>", "<CreateTime>" + String.valueOf(createTime) + "</CreateTime>");
		}
		return responseXml;
	}

	/**
	 * 当文本回复的时候，在msgXml的content中添加会员邀请链接
	 * 
	 * @param msgXml
	 * @return
	 */
	@Override
    public String attachInviteMsgXml(LfWcAccount acct, Long wcId, String basePath, String msgXml)
	{
		String msg = getInviteMsg(acct.getCorpCode(), wcId, basePath);
		if(msgXml != null && !"".equals(msgXml))
		{
			HashMap<String, String> paramsXmlMap = new HashMap<String, String>();
			paramsXmlMap = getParamsXmlMap(msgXml);
			// 只有文本消息时，才在content上添加"邀请链接"
			if("text".equals(paramsXmlMap.get("MsgType")))
			{
				// String content = getMsgXmlContent(msgXml);
				msgXml = msgXml.replaceAll("<Content>(.*?)</Content>", "<Content><![CDATA[" + paramsXmlMap.get("Content") + " " + msg + "]]></Content>");
			}
		}
		else
		{
			msgXml = "";
		}
		return msgXml;
	}

	/**
	 * 系统无法识别时的回复
	 * 
	 * @param toUser
	 * @param fromUser
	 * @return
	 */
	@Override
    public String getInviteMemberMsgXml(LfWcAccount acct, Long wcId, String basePath)
	{
		String msg = getInviteMsg(acct.getCorpCode(), wcId, basePath);
		StringBuffer xml = new StringBuffer();
		xml.append("<xml>");
		xml.append("<ToUserName></ToUserName>");
		xml.append("<FromUserName></FromUserName>");
		xml.append("<CreateTime></CreateTime>");
		xml.append("<MsgType>text</MsgType>");
		xml.append("<Content><![CDATA[" + msg + "]]></Content>");
		xml.append("</xml>");
		return xml.toString();
	}

	/**
	 * 获取地理位置消息Msgxml
	 * @param paramsXmlMap
	 * @return1
	 */
	@Override
    public String getLocationMsgXml(HashMap<String, String> paramsXmlMap){
		StringBuffer msgXml = new StringBuffer();
		String label = paramsXmlMap.get("Label");
		String locationX = paramsXmlMap.get("Location_X");
		String locationY = paramsXmlMap.get("Location_Y");
		
		String picUrl = "http://api.map.baidu.com/staticimage?width=280&height=140&zoom=13&center="+locationY+","+locationX + "&markers="+locationY+","+locationX+"&markerStyles=l,A";
		String link = "http://map.baidu.com/mobile/?location="+locationX+","+locationY+"&title=我的位置&content="+label+"&output=html";
		String link2 = "{basePath}cwc_stationService.hts?pl=101&x=22.542669&y=113.935265";
		msgXml.append("<xml>");
		msgXml.append("<ToUserName></ToUserName>");
		msgXml.append("<FromUserName></FromUserName>");
		msgXml.append("<CreateTime></CreateTime>");
		msgXml.append("<MsgType>news</MsgType>");
		msgXml.append("<ArticleCount>2</ArticleCount>");
		msgXml.append("<Articles>");
		//添加一个图文
		msgXml.append("<item>");
		msgXml.append("<Title><![CDATA[").append("周边位置服务").append("]]></Title>");
		msgXml.append("<PicUrl><![CDATA[").append(initnullString(picUrl)).append("]]></PicUrl>");
		msgXml.append("<Description><![CDATA[").append("周边位置服务").append("]]></Description>");
		msgXml.append("<Url><![CDATA[").append(initnullString(link)).append("]]></Url>");
		msgXml.append("</item>");
		
		//添加一个图文
		msgXml.append("<item>");
		msgXml.append("<Title><![CDATA[").append("联想服务原型演示").append("]]></Title>");
		msgXml.append("<PicUrl><![CDATA[").append(initnullString(picUrl)).append("]]></PicUrl>");
		msgXml.append("<Description><![CDATA[").append("地理位置服务原型演示").append("]]></Description>");
		msgXml.append("<Url><![CDATA[").append(initnullString(link2)).append("]]></Url>");
		msgXml.append("</item>");
		
		msgXml.append("</Articles>");
		msgXml.append("</xml>");
		return msgXml.toString();
	}
	/**
	 * 通过EventKey获取msgXML
	 * 
	 * @param acct
	 * @param EventKey
	 * @return
	 */
	@Override
    public String getMenuMsgXml(LfWcAccount acct, String EventKey)
	{
		String msgXml = "";
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("mkey", EventKey);
		conditionMap.put("AId", String.valueOf(acct.getAId()));

		List<LfWcMenu> menuList = new ArrayList<LfWcMenu>();

		try
		{
			menuList = empDao.findListByCondition(LfWcMenu.class, conditionMap, null);
			if(menuList != null && menuList.size() > 0)
			{
				LfWcMenu menu = menuList.get(0);
//findbugs:		if(menu.getTId() != null && !"0".equals(String.valueOf(menu.getTId())) && !"".equals(menu.getTId()))
				if(menu.getTId() != null && !"0".equals(String.valueOf(menu.getTId())))
				{
					// 从模板里面读XML，实现同步
					LfWcTemplate template = empDao.findObjectByID(LfWcTemplate.class, menu.getTId());
					if(template instanceof LfWcTemplate)
					{
						msgXml = template.getMsgXml();	
					}
				}
				else
				{
					msgXml = menu.getMsgXml();
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过EventKey获取msgXML失败！");
		}

		return msgXml;
	}

	/**
	 * 发送会员邀请的文本和链接地址
	 * 
	 * @param basePath
	 * @param corpCode
	 * @param wcId
	 * @return
	 */
	private String getInviteMsg(String corpCode, Long wcId, String basePath)
	{
		String inviteUrl = basePath + "cwc_infoRelated.hts?lgcorpcode=" + corpCode + "&wcId=" + String.valueOf(wcId);
		String msg = "点击下面的链接，成为我们的会员:<a href=\"" + inviteUrl + "\">点击这里,加入会员</a>";
		return msg;
	}

	// 检查msgXMl格式是否正确(文本回复和图文回复检查)
	@Override
    public boolean checkMsgXml(String responseXml)
	{
		boolean flag = true;
		HashMap<String, String> paramsXmlMap = new HashMap<String, String>();
		if(responseXml == null || "".equals(responseXml))
		{
			flag = false;
		}
		else
		{
			paramsXmlMap = getParamsXmlMap(responseXml);

			// 回复的消息必须有ToUserName和FromUserName
			if(paramsXmlMap.get("ToUserName") == null || "".equals(paramsXmlMap.get("ToUserName")))
			{
				flag = false;
			}
			else if(paramsXmlMap.get("FromUserName") == null || "".equals(paramsXmlMap.get("FromUserName")))
			{
				flag = false;
			}
			else
			{
				// 回复的是文本消息，消息的内容不能为空
				if("text".equals(paramsXmlMap.get("MsgType")))
				{
					if(paramsXmlMap.get("Content") == null || "".equals(paramsXmlMap.get("Content")))
					{
						flag = false;
					}
				}
				else if("news".equals(paramsXmlMap.get("MsgType")))
				{
					// 回复 的是图文消息，消息的图文个数不能为空
					if(paramsXmlMap.get("ArticleCount") == null || "".equals(paramsXmlMap.get("ArticleCount")))
					{
						flag = false;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 系统无法识别时的回复
	 * 
	 * @param toUser
	 * @param fromUser
	 * @return
	 */
	@Override
    public String getSystemDefaultResponseXml(HashMap<String, String> paramsXmlMap)
	{
		String toUser = paramsXmlMap.get("FromUserName");
		String fromUser = paramsXmlMap.get("ToUserName");
		StringBuffer msgXml = new StringBuffer();
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		long createTime = cal.getTimeInMillis() / 1000;
		msgXml.append("<xml>");
		msgXml.append("<ToUserName><![CDATA[" + toUser + "]]></ToUserName>");
		msgXml.append("<FromUserName><![CDATA[" + fromUser + "]]></FromUserName>");
		msgXml.append("<CreateTime>" + String.valueOf(createTime) + "</CreateTime>");
		msgXml.append("<MsgType><![CDATA[text]]></MsgType>");
		msgXml.append("<Content><![CDATA[" + "尊敬的EMP用户，您的回复已收录，请下次询问！" + "]]></Content>");
		msgXml.append("</xml>");
		return msgXml.toString();
	}

	/**
	 * 系统关键字处理流程
	 */

	@Override
    public String handleSystemKeyword(String msg)
	{
		// TODO-处理系统关键字
		return msg;
	}

	/**
	 * 对msgXml中basePath的处理，形如： content =
	 * "...<PicUrl><![CDATA[{basePath}/login.png]]></PicUrl>...";
	 * 
	 * @param content
	 * @return
	 */
	@Override
    public String handleMsgXmlBasePath(String content, String weixFilePath)
	{
		List<String> items = new ArrayList<String>();
		items.add("<PicUrl>(.*?)</PicUrl>");
		items.add("<Url>(.*?)</Url>");
		items.add("<MusicUrl>(.*?)</MusicUrl>");
		items.add("<HQMusicUrl>(.*?)</HQMusicUrl>");
		for (String item : items)
		{
			Pattern p = Pattern.compile(item);
			Matcher m = p.matcher(content);
			boolean flag = m.find();
			if(flag)
			{
				if(content != null)
				{
					content = content.replace("{basePath}", weixFilePath);
					content = content.replace("{weixFilePath}", weixFilePath);
				}
				else
				{
					content = "";
				}
			}
		}
		return content;
	}

	/**
	 * 文本消息msgXml的创建
	 * 
	 * @param rtextMsg
	 * @return
	 * @throws Exception
	 */
	public static String createInitRtextMessage(String rtextMsg) throws Exception
	{
		StringBuffer msgXml = new StringBuffer();
		
		if(rtextMsg != null && !"".equals(rtextMsg))
		{
			msgXml.append("<xml>");
			msgXml.append("<ToUserName></ToUserName>");
			msgXml.append("<FromUserName></FromUserName>");
			msgXml.append("<CreateTime></CreateTime>");
			msgXml.append("<MsgType>text</MsgType>");
			msgXml.append("<Content><![CDATA[").append(initnullString(rtextMsg)).append("]]></Content>");
			msgXml.append("</xml>");
		}
		else
		{
			throw new Exception("参数rtextMsg不能为空!");
		}

		return msgXml.toString();
	}

	/**
	 * 图文消息msgXMl的创建
	 * 
	 * @param rimgItemList
	 * @return
	 * @throws Exception
	 */
	public static String createInitRimgMessage(List<LfWcRimg> rimgItemList) throws Exception
	{
		StringBuffer msgXml = new StringBuffer();
		int articleCount = rimgItemList.size();
		if(articleCount > 0)
		{
			msgXml.append("<xml>");
			msgXml.append("<ToUserName></ToUserName>");
			msgXml.append("<FromUserName></FromUserName>");
			msgXml.append("<CreateTime></CreateTime>");
			msgXml.append("<MsgType>news</MsgType>");
			msgXml.append("<ArticleCount>").append(articleCount).append("</ArticleCount>");
			msgXml.append("<Articles>");
			for (LfWcRimg item : rimgItemList)
			{
				msgXml.append("<item>");
				msgXml.append("<Title><![CDATA[").append(initnullString(item.getTitle())).append("]]></Title>");
				msgXml.append("<PicUrl><![CDATA[").append(initnullString(item.getPicurl())).append("]]></PicUrl>");
				msgXml.append("<Description><![CDATA[").append(initnullString(item.getDescription())).append("]]></Description>");
				msgXml.append("<Url><![CDATA[").append(initnullString(item.getLink())).append("]]></Url>");
				msgXml.append("</item>");
			}
			msgXml.append("</Articles>");
			msgXml.append("</xml>");
		}
		else
		{
			throw new Exception("文本消息的长度不能为空!");
		}

		return msgXml.toString();
	}

	/**
	 * 空字符的处理
	 * @param o
	 * @return1
	 */
	private static String initnullString(String o)
	{
		boolean b = GlobalMethods.isInvalidString(o);
		return b ? "" : o.toString();
	}

	/**
	 * 使用SHA算法加密
	 * 
	 * @param convertme
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String SHAsum(byte[] convertme) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		return byteArray2Hex(md.digest(convertme));
	}

	/**
	 * @param hash
	 * @return
	 */
	private String byteArray2Hex(final byte[] hash)
	{
		Formatter formatter = new Formatter();
		try {
			for (byte b : hash)
			{
				formatter.format("%02x", b);
			}
			return formatter.toString();
		} catch (Exception e) {
			return "";
		}finally{
			formatter.close();
		}
		
	}

	/**
	 * type(0:上行;1:下行)
	 * msgType(0：文本消息；1：图片消息；2：地理位置消息；3：链接消息；4：事件推送；5：回复文本；6：回复图文；7：回复语音消息)
	 * 
	 * @param type
	 */
	private Integer getMsgType(String msgType, String type)
	{
		Integer msgTp = null;
		LinkedHashMap<String, Integer> lMsgTypeMap = getLMsgTypeMap();
		LinkedHashMap<String, Integer> rMsgTypeMap = getRMsgTypeMap();

		if("0".equals(type))
		{
			msgTp = lMsgTypeMap.get(msgType);
		}
		else
			if("1".equals(type))
			{
				msgTp = rMsgTypeMap.get(msgType);
			}
		return msgTp;
	}

	/**
	 * 上行消息类型
	 * 
	 * @return
	 */
	private LinkedHashMap<String, Integer> getLMsgTypeMap()
	{
		LinkedHashMap<String, Integer> lMsgTypeMap = new LinkedHashMap<String, Integer>();
		lMsgTypeMap.put("text", 0);
		lMsgTypeMap.put("image", 1);
		lMsgTypeMap.put("location", 2);
		lMsgTypeMap.put("link", 3);
		lMsgTypeMap.put("event", 4);
		return lMsgTypeMap;
	}

	/**
	 * 下行消息类型
	 * 
	 * @return
	 */
	private LinkedHashMap<String, Integer> getRMsgTypeMap()
	{
		LinkedHashMap<String, Integer> lMsgTypeMap = new LinkedHashMap<String, Integer>();
		lMsgTypeMap.put("text", 5);
		lMsgTypeMap.put("music", 6);
		lMsgTypeMap.put("news", 7);
		return lMsgTypeMap;
	}
}
