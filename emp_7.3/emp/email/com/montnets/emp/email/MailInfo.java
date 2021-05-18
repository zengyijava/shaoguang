package com.montnets.emp.email;

public class MailInfo {	
	
	// 邮件发送协议 
	private String protocol ; 
     
    // SMTP邮件服务器 
    private String host ; 
     
    // SMTP邮件服务器默认端口 
    private String port ; 
     
    // 发件人 
    private String from ; 
 
    // 收件人 
    private String to ; 
     
    // 账号
    private String username ; 
    
    // 密码
    private String password ;
    
    //主题
    private String subject;

    //昵称
    private String name="";
    
    //内容 普通文本或者html文本
    private String content="";
    
    //优先级
    private String priority="1";
    
    //图片
    private String[] imagePaths;
    
    //附件
    private String[] attachPaths;
    
    //邮件头尊称
    private String respectName;
    
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

    /**
     * @param to 形如  a@qq.com,b@sina.cn
     * 多个收件人地址以','隔开
     */ 
	public void setTo(String to) {
		this.to = to;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * 设置账号
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getName() {
		return name;
	}

	/**
	 * 设置发件人昵称
	 * @return
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPriority() {
		return priority;
	}

	/**
	 * @param priority 优先级(1:紧急 3:普通 5:低)
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String[] getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(String[] imagePaths) {
		this.imagePaths = imagePaths;
	}

	public String[] getAttachPaths() {
		return attachPaths;
	}

	public void setAttachPaths(String[] attachPaths) {
		this.attachPaths = attachPaths;
	}

	public String getRespectName() {
		return respectName;
	}

	public void setRespectName(String respectName) {
		this.respectName = respectName;
	}

}
