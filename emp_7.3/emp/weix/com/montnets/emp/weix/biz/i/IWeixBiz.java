package com.montnets.emp.weix.biz.i;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.montnets.emp.entity.weix.*;
import com.montnets.emp.weix.dao.MsgDao;
import org.apache.commons.beanutils.DynaBean;

public interface IWeixBiz
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
	public boolean verifySignature(String signature, String timestamp, String nonce, String token) throws Exception;

	/**
	 * 读取微信服务器返回的XML数据，并返回String类型数据
	 * 
	 * @param request
	 * @return
	 */
	public String readXMLFromRequestBody(HttpServletRequest request);

	/**
	 * 将请求参数转化成Map数据格式，方便读取
	 * 
	 * @param requestXml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getParamsXmlMap(String requestXml);

	/**
	 * 查找企业所有公众帐号
	 * @param corpCode
	 * @return1
	 */
	public List<LfWcAccount> findAllAccountByCorpcode(String corpCode);

	/**
	 * 创建消息历史记录
	 * weixBiz.createLMsg(acct,alink,"0",requestXml,paramsXmlMap);
	 * 
	 * @param paramsXmlMap
	 * @return
	 */
	public LfWcMsg createMsg(LfWcAccount acct, LfWcAlink alink, String type, String msgXml, HashMap<String, String> paramsXmlMap);

	/**
	 * 获取当前公众帐号
	 * 
	 * @param
	 * @return
	 */
	public LfWcAccount findAccountByOpenId(String toUserName);

	/**
	 * 获取当前微信帐号与当前公众帐号的关联关系
	 * 
	 * @param
	 * @return
	 */
	public LfWcAlink addAlink(HashMap<String, String> paramsXmlMap, LfWcAccount acct);

	/**
	 * 提取关键字，目前只处理了文本消息关键字提取
	 * 
	 * @param paramsXmlMap
	 * @return
	 */
	public String extractMsg(HashMap<String, String> paramsXmlMap);

	/**
	 *关键字匹配搜索
	 * 
	 * @param acct
	 * @param msg
	 * @return
	 */
	public String getKeywordMsgXml(LfWcAccount acct, String msg);

	/**
	 * 关注事件消息回复msgXMl
	 * 
	 * @param acct
	 * @return
	 */
	public String getSubscribeMsgXml(LfWcAccount acct);

	/**
	 * 查询公众帐号-默认回复msgXml
	 * 
	 * @param acct
	 * @return
	 */
	public String getDefaultReplyMsgXml(LfWcAccount acct);

	/**
	 * 查询公众帐号所有关联关键字，时间倒序排列
	 * 
	 * @param acct
	 * @return
	 */
	public List<LfWcKeyword> findKeyWordsByAccount(LfWcAccount acct);

	/**
	 * 查询关键字所对于的模板
	 * 
	 * @param kId
	 * @return
	 */
	public List<DynaBean> findTemplateByKeywordId(String kId);

	/**
	 * 查询公众帐号-关注事件回复List<LfWcRevent>对象列表
	 * 
	 * @param acct
	 * @return
	 */
	public List<LfWcRevent> findSubscribeByAccount(LfWcAccount acct);

	/**
	 * 查询公众帐号-默认回复List<LfWcRtext>列表
	 * 
	 * @param acct
	 * @return
	 */
	public List<LfWcRtext> findDefaultReplyByAccount(LfWcAccount acct);

	/**
	 * 创建返回的xml数据，将ToUserName和FromUserName加上
	 * 
	 * @param toUser
	 * @param fromUser
	 * @param msgXml
	 * @return
	 */
	public String createResponseXml(HashMap<String, String> paramsXmlMap, String msgXml);

	/**
	 * 当文本回复的时候，在msgXml的content中添加会员邀请链接
	 * 
	 * @param msgXml
	 * @return
	 */
	public String attachInviteMsgXml(LfWcAccount acct, Long wcId, String basePath, String msgXml);

	/**
	 * 系统无法识别时的回复
	 * 
	 * @param toUser
	 * @param fromUser
	 * @return
	 */
	public String getInviteMemberMsgXml(LfWcAccount acct, Long wcId, String basePath);

	/**
	 * 获取地理位置消息Msgxml
	 * @param paramsXmlMap
	 * @return1
	 */
	public String getLocationMsgXml(HashMap<String, String> paramsXmlMap);
	/**
	 * 通过EventKey获取msgXML
	 * 
	 * @param acct
	 * @param EventKey
	 * @return
	 */
	public String getMenuMsgXml(LfWcAccount acct, String EventKey);
	// 检查msgXMl格式是否正确(文本回复和图文回复检查)
	public boolean checkMsgXml(String responseXml);

	/**
	 * 系统无法识别时的回复
	 * 
	 * @param toUser
	 * @param fromUser
	 * @return
	 */
	public String getSystemDefaultResponseXml(HashMap<String, String> paramsXmlMap);

	/**
	 * 系统关键字处理流程
	 */

	public String handleSystemKeyword(String msg);

	/**
	 * 对msgXml中basePath的处理，形如： content =
	 * "...<PicUrl><![CDATA[{basePath}/login.png]]></PicUrl>...";
	 * 
	 * @param content
	 * @return
	 */
	public String handleMsgXmlBasePath(String content, String weixFilePath);
}
