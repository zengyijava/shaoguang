package com.montnets.emp.email;

import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.sun.mail.util.MailSSLSocketFactory;

public class SendMail{

	private static MyAuthenticator authenticator;

	// 初始化连接邮件服务器的会话信息
	private static Properties props = null;

	// 是否要求身份认证
	private final static String IS_AUTH = "true";

	// 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
	private final static String IS_ENABLED_DEBUG_MOD = "true";

	private static MailInfo mailInfo;

	@SuppressWarnings("unused")
	private SendMail() {
	}

	public SendMail(MailInfo mi) {
		init(mi);
	}

	/**
	 * 初始化邮件系统所需参数
	 * 
	 * @param mi
	 *            邮件信息vo对象
	 */
	public static void init(MailInfo mi) {
		mailInfo = mi;
		props = new Properties();
		props.setProperty("mail.transport.protocol", mailInfo.getProtocol());
		props.setProperty("mail.smtp.host", mailInfo.getHost());
		props.setProperty("mail.smtp.port", mailInfo.getPort());
		props.setProperty("mail.smtp.auth", IS_AUTH);
	//	props.setProperty("mail.debug", IS_ENABLED_DEBUG_MOD);
		if(mi.getPort().equals("465")){
			MailSSLSocketFactory sf;
			try {
				sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);  
				props.put("mail.smtp.ssl.enable", "true");  
				props.put("mail.smtp.ssl.socketFactory", sf);  
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				EmpExecutionContext.error(e, "邮件服务器ssl验证失败");
			}  
		}else {
			props.put("mail.smtp.ssl.enable", "false");  
		}
		
		// 向邮件服务器验证账号密码
		authenticator = new MyAuthenticator(mailInfo.getUsername(),
				mailInfo.getPassword());
	}

	/**
	 * 发送带内嵌多图片、多附件、多收件人、邮件优先级、阅读回执的完整的HTML邮件
	 * 
	 * @param attachPaths
	 *            附件的路径,多份附件以数组形式传入
	 * @param imagePaths
	 *            图片路径,多份图片以数组形式传入
	 */
	public void sendMultipleEmail() throws Exception{

		String charset = "utf-8"; // 指定中文编码格式
		// 创建Session实例对象
		Session session = Session.getInstance(props, authenticator);

		// 创建MimeMessage实例对象
		MimeMessage message = new MimeMessage(session);
		// 设置主题

			message.setSubject(mailInfo.getSubject());
			// 设置发送人
			message.setFrom(new InternetAddress(mailInfo.getFrom(), mailInfo.getName(), charset));

			// 设置收件人
			message.setRecipients(RecipientType.TO,
					InternetAddress.parse(mailInfo.getTo()));
			// 设置发送时间
			message.setSentDate(new Date());
			// 设置回复人(收件人回复此邮件时,默认收件人)
			message.setReplyTo(InternetAddress.parse("\""
					+ MimeUtility.encodeText(mailInfo.getName()) + "\" <"
					+ mailInfo.getFrom() + ">"));
			// 设置优先级(1:紧急 3:普通 5:低)
			message.setHeader("X-Priority", mailInfo.getPriority());
			// 要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
			message.setHeader("Disposition-Notification-To", mailInfo.getFrom());

			// 创建一个MIME子类型为"mixed"的MimeMultipart对象，表示这是一封混合组合类型的邮件
			MimeMultipart mailContent = new MimeMultipart("mixed");
			message.setContent(mailContent);

			// 内容
			MimeBodyPart mailBody = new MimeBodyPart();
			// 将内容添加到邮件当中
			mailContent.addBodyPart(mailBody);

			String[] attachPaths = mailInfo.getAttachPaths();
			if (attachPaths != null ) {
				int len = attachPaths.length;
				for (int i = 0; i < len; i++) {
					// 附件
					MimeBodyPart attach = new MimeBodyPart();
					// 将附件添加到邮件当中
					mailContent.addBodyPart(attach);
					DataSource ds = new FileDataSource(getWebrootPath()+attachPaths[i]);
					DataHandler dh = new DataHandler(ds);
					attach.setDataHandler(dh);

					attach.setFileName(MimeUtility.encodeText(attachPaths[i]));
					EmpExecutionContext.error("邮件附件添加成功");
				}

			}

			// 邮件正文(内嵌图片+html文本)
			MimeMultipart body = new MimeMultipart("related");
			mailBody.setContent(body);

			// 邮件正文
			MimeBodyPart htmlPart = new MimeBodyPart();
			body.addBodyPart(htmlPart);
			
			String[] imagePaths = mailInfo.getImagePaths();
			if (imagePaths != null ) {
				int len = imagePaths.length;
				for (int i = 0; i < len; i++) {
					// 添加图片
					MimeBodyPart imgPart = new MimeBodyPart();
					body.addBodyPart(imgPart);
					// 正文图片
					DataSource ds2 = new FileDataSource(getWebrootPath()+imagePaths[i]);
					DataHandler dh2 = new DataHandler(ds2);
					imgPart.setDataHandler(dh2);
					imgPart.setContentID("sign" + i);
					
				}

			}

			// 邮件内容 html/text 文本
			MimeMultipart htmlMultipart = new MimeMultipart("alternative");
			htmlPart.setContent(htmlMultipart);
			MimeBodyPart htmlContent = new MimeBodyPart();
			StringBuffer contentBuffer = new StringBuffer();
			//正文头 收件人尊称
			if( mailInfo.getRespectName()!=null && !mailInfo.getRespectName().equals("")){
				contentBuffer.append("尊敬的"+mailInfo.getRespectName()+":"+"</br>");
			}
			contentBuffer.append(mailInfo.getContent());
			if(imagePaths != null ){
				for (int j = 0; j < imagePaths.length; j++) {
					contentBuffer.append("<img src='cid:sign" + j + "' />");
					EmpExecutionContext.error("邮件内嵌图片添加成功");
				}
			}
			
			htmlContent.setContent(contentBuffer.toString(),
					"text/html;charset=gbk");
			htmlMultipart.addBodyPart(htmlContent);

			// 保存邮件内容修改
			message.saveChanges();
			// 发送邮件
			long begin = System.currentTimeMillis();
			Transport.send(message);
			long end  = System.currentTimeMillis();	
			EmpExecutionContext.error("邮件发送成功,用时："+(end-begin)+"ms");
	}
	
	/**
	 *  获取文件在webroot下的路径
	 * @return 文件路径
	 */
	public String getWebrootPath(){
		String path="";
		try {
			path = ClassLoader.getSystemResource("").toURI().getPath();
			path = path.substring(0,path.lastIndexOf("WEB-INF"))+"email"+"/";
		} catch (URISyntaxException e) {
			EmpExecutionContext.error(e, "获取邮件资源文件路径异常");
		}
		return path;
	}
	
}
