package com.montnets.emp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import com.montnets.emp.common.context.EmpExecutionContext;

public class UdpGetClientMacAddr {
	/*private static String sRemoteAddr;
	private static int iRemotePort = 137;
	private static byte[] buffer = new byte[1024];
	private static DatagramSocket ds = null;*/

	/*public UdpGetClientMacAddr(String strAddr) throws Exception {
		sRemoteAddr = strAddr;
		ds = new DatagramSocket();
	}

	public UdpGetClientMacAddr() {
	}
*/
	/*protected static final DatagramPacket send(final byte[] bytes)
			throws IOException {
		DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress
				.getByName(sRemoteAddr), iRemotePort);
		ds.send(dp);
		return dp;
	}

	protected static final DatagramPacket receive() {
		DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
		try {
			ds.receive(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dp;
	}*/

	 // 询问包结构:
    // Transaction ID 两字节（16位） 0x00 0x00
    // Flags 两字节（16位） 0x00 0x10
    // Questions 两字节（16位） 0x00 0x01
    // AnswerRRs 两字节（16位） 0x00 0x00
    // AuthorityRRs 两字节（16位） 0x00 0x00
    // AdditionalRRs 两字节（16位） 0x00 0x00
    // Name:array [1..34] 0x20 0x43 0x4B 0x41(30个) 0x00 ;
    // Type:NBSTAT 两字节 0x00 0x21
    // Class:INET 两字节（16位）0x00 0x01
	/*protected static byte[] GetQueryCmd() throws Exception {
		byte[] t_ns = new byte[50];
		t_ns[0] = 0x00;
		t_ns[1] = 0x00;
		t_ns[2] = 0x00;
		t_ns[3] = 0x10;
		t_ns[4] = 0x00;
		t_ns[5] = 0x01;
		t_ns[6] = 0x00;
		t_ns[7] = 0x00;
		t_ns[8] = 0x00;
		t_ns[9] = 0x00;
		t_ns[10] = 0x00;
		t_ns[11] = 0x00;
		t_ns[12] = 0x20;
		t_ns[13] = 0x43;
		t_ns[14] = 0x4B;

		for (int i = 15; i < 45; i++) {
			t_ns[i] = 0x41;
		}

		t_ns[45] = 0x00;
		t_ns[46] = 0x00;
		t_ns[47] = 0x21;
		t_ns[48] = 0x00;
		t_ns[49] = 0x01;
		return t_ns;
	}
*/
	// 表1 “UDP－NetBIOS－NS”应答包的结构及主要字段一览表
    // 序号 字段名 长度
    // 1 Transaction ID 两字节（16位）
    // 2 Flags 两字节（16位）
    // 3 Questions 两字节（16位）
    // 4 AnswerRRs 两字节（16位）
    // 5 AuthorityRRs 两字节（16位）
    // 6 AdditionalRRs 两字节（16位）
    // 7 Name<Workstation/Redirector> 34字节（272位）
    // 8 Type:NBSTAT 两字节（16位）
    // 9 Class:INET 两字节（16位）
    // 10 Time To Live 四字节（32位）
    // 11 Length 两字节（16位）
    // 12 Number of name 一个字节（8位）
    // NetBIOS Name Info 18×Number Of Name字节
    // Unit ID 6字节（48位
	/*protected static final String GetMacAddr(byte[] brevdata) {
		 // 获取计算机名
		int i = brevdata[56] * 18 + 56;
		String sAddr = "";
		StringBuffer sb = new StringBuffer(17);
		// 先从第56字节位置，读出Number Of Names（NetBIOS名字的个数，其中每个NetBIOS Names Info部分占18个字节）
        // 然后可计算出“Unit ID”字段的位置＝56＋Number Of Names×18，最后从该位置起连续读取6个字节，就是目的主机的MAC地址。
		for (int j = 1; j < 7; j++) {
			sAddr = Integer.toHexString(0xFF & brevdata[i + j]);
			if (sAddr.length() < 2) {
				sb.append(0);
			}
			sb.append(sAddr.toUpperCase());
			if (j < 6)
				sb.append('-');
		}
		return sb.toString();
	}*/

/*	public static final void close() {
		try {
			ds.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public  String GetRemoteMacAddr() throws Exception {
		byte[] bqcmd = GetQueryCmd();
		send(bqcmd);
		DatagramPacket dp = receive();
		String smac = GetMacAddr(dp.getData());
		close();

		return smac;
	}*/
	
	/**
	 * 获取客户端IP地址
	 * @param request
	 * @return
	 */
	public static  String getIpAddr(HttpServletRequest request) {
		
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
		ip = request.getRemoteAddr();
		}
		return ip;
		
		/*String sip = request.getHeader("x-forwarded-for");   
		if(sip == null || sip.length() == 0 || "unknown".equalsIgnoreCase(sip)) {   
		    sip = request.getHeader("Proxy-Client-IP");   
		}   
		if(sip == null || sip.length() == 0 || "unknown".equalsIgnoreCase(sip)) {   
		    sip = request.getHeader("WL-Proxy-Client-IP");   
		}   
		if(sip == null || sip.length() == 0 || "unknown".equalsIgnoreCase(sip)) {   
		    sip = request.getRemoteAddr();   
		} 
		return sip;*/
	}
	
	public List<String> getLocalIpAddrs() {
		List<String> ipList = new ArrayList<String>();
		 String ip = "";
        //枚举出本机所有的IP地址
        Enumeration<NetworkInterface> networkIterfaces = null;
		try {
			networkIterfaces = NetworkInterface
			        .getNetworkInterfaces();
		} catch (SocketException e) {
			EmpExecutionContext.error(e,"获取本机IP地址异常！");
			return null;
		}
        while (networkIterfaces.hasMoreElements()) {
            Enumeration<InetAddress> inetAddresses = networkIterfaces
                    .nextElement().getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                ip = inetAddresses.nextElement().getHostAddress();
                ipList.add(ip);
            }
        }
        return ipList;
	}
	
	/**
	 * 获取客户端MAC地址
	 * @param ip
	 * @return
	 * @throws IOException 
	 */
	public List<String> getMACAddress(String ip) throws IOException{ 
		
		String os = System.getProperty("os.name");
		try {
			if (os.startsWith("Windows")) {
				return this.getMACAddressWindows(ip);
			} else if (os.startsWith("Linux")) {
				return this.getMACAddressLinux(ip);
			} else {
				throw new IOException("unknown operating system: " + os);
			}
		} catch (Exception ex) {
			EmpExecutionContext.error(ex,"获取客户端MAC地址异常！");
			throw new IOException(ex.getMessage());
		}
    }
	
	private List<String> getMACAddressWindows(String ip){
		List<String> ips = this.getLocalIpAddrs();
		boolean isLocal = false;
		for (String str : ips) {
			if(str.equals(ip)){
				isLocal = true;
				break;
			}
		}
		if(isLocal){
			//如果是本机访问本机服务
			return this.getLocalMacWindows();
		}else{
			List<String> macs = new ArrayList<String>();
			//非本机访问
			String mac = this.getNonLacalMacAddr(ip).toUpperCase();
			if(mac != null && !"".equals(mac)){
				macs.add(mac);
			}
			return macs;
		}
	}
	
	private String getNonLacalMacAddr(String ip) {
		String mac = this.getMacByArp(ip);
		/*// 如果采用arp方式获取不到MAC地址，则采用nbtstat方式获取
		if (mac == null || "".equals(mac)) {
			mac = this.getMacByNbtstat(ip);
		}*/
		return mac;
	}
	
	private String getMacByArp(String ip){
		String str = "";
		String mac = "";
		Process p = null;
		LineNumberReader lnr = null;
		InputStreamReader isr = null;
		try {
			p = Runtime.getRuntime().exec("arp -A " + ip);
			isr = new InputStreamReader(p.getInputStream());
			lnr = new LineNumberReader(isr);
			for (int i = 1; i < 100; i++) {
				str = lnr.readLine();
				if (str != null) {
					if (/*str.contains("dynamic") && */str.contains(ip)) {
						String[] arr = str.split(" ");
						for (int j = 0; j < arr.length; j++) {
							if (arr[j].contains("-")) {
								mac = arr[j];
								break;
							}
						}
					}
					// 以上有个判断，不同系统cmd命令执行的返回结果展示方式不一样，我测试的win7是MAC 地址
					// 所以又第二个if判断 你可先在你机器上cmd测试下nbtstat -A 命令 当然得有一个你可以ping通的
					// 网络ip地址,然后根据你得到的结果中mac地址显示方式来确定这个循环取值

				}
			}
		} catch (IOException e) {
			mac = null;
			EmpExecutionContext.error(e,"通过ARP获取mac地址异常！");
		}finally{
			if(lnr != null){
				try {
					lnr.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"LineNumberReader流关闭异常！");
				}
			}
			if(isr != null){
				try {
					isr.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"InputStreamReader流关闭异常！");
				}
			}
			if(p != null){
				p.destroy();
			}
		}
		return mac;
	}
	
	/*private String getMacByNbtstat(String ip){
		String str = "";
		String mac = "";
		Process p = null;
		LineNumberReader lnr = null;
		InputStreamReader isr = null;
		try {
			p = Runtime.getRuntime().exec("nbtstat -A " + ip);
			isr = new InputStreamReader(p.getInputStream());
			lnr = new LineNumberReader(isr);
			 for (int i = 1; i < 100; i++) {  
				str = lnr.readLine();
				if (str != null) {
					if (str.indexOf("MAC Address") > 1) {
						mac = str.substring(str.indexOf("MAC Address") + 14);
						break;
					}else if(str.indexOf("MAC 地址") > 1){
						mac = str.substring(str.indexOf("MAC 地址") + 9);
						break;
					}
				}
			}
		} catch (IOException e) {
			mac = null;
			EmpExecutionContext.error(e);
		}finally{
			if(lnr != null){
				try {
					lnr.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e);
				}
			}
			if(isr != null){
				try {
					isr.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e);
				}
			}
			if(p != null){
				p.destroy();
			}
		}
		return mac;
	}*/
	
	private List<String> getLocalMacWindows(){
		List<String> macs = new ArrayList<String>();
		String mac = null;
		BufferedReader br = null;
		Process proc = null;
		InputStreamReader is = null;
		try {
			proc = Runtime.getRuntime().exec("cmd.exe /c ipconfig/all");
			is = new InputStreamReader(proc.getInputStream());
			br = new BufferedReader(is);
			String message = br.readLine();

			int index = -1;
			while (message != null) {
				if ((index = message.indexOf("Physical Address")) > 0) {
					mac = message.substring(index + 36).trim();
					if(mac != null && mac.length() == 17){
						macs.add(mac);
					}
				}else if((index = message.indexOf("物理地址")) > 0){
					mac = message.substring(message.lastIndexOf(" ")+1).trim();
					if(mac != null && mac.length() == 17){
						macs.add(mac);
					}
				}
				message = br.readLine();
			}
		} catch (IOException e) {
			EmpExecutionContext.error(e,"window获取本地mac地址异常！");
			return null;
		}finally{
			close(is, br, proc);
		}
		return macs;
	}
	
	private List<String> getMACAddressLinux(String ip){
		List<String> ips = this.getLocalIpAddrs();
		boolean isLocal = false;
		for (String str : ips) {
			if(str.equals(ip)){
				isLocal = true;
				break;
			}
		}
		if(isLocal){
			//如果是本机访问本机服务
			return this.getLocalMacLinux();
		}else{
			List<String> macs = new ArrayList<String>();
			//非本机访问
			String mac = this.getNonLacalMacLinux(ip).toUpperCase();
			if(mac != null && !"".equals(mac)){
				macs.add(mac);
			}
			return macs;
		}
	}
	
	private List<String> getLocalMacLinux(){
		String result = "";
		String line = "";
		String cmd = "ifconfig -a";
		String[] comands = new String[] { "/bin/sh", "-c", cmd };
		Process proc = null;
		BufferedReader br = null;
		InputStreamReader is = null;
		try {
			proc = Runtime.getRuntime().exec(comands);
			is = new InputStreamReader(proc.getInputStream());
			br = new BufferedReader(is);
			while ((line = br.readLine()) != null) {
				if (line.contains("HWaddr")) {
					result = line.substring(line.lastIndexOf("HWaddr")+7);
					break;
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取linux下的mac地址异常！");
		}finally{
			close(is, br, proc);
		}
		List<String> macs = new ArrayList<String>();
		if(result != null && !"".equals(result)){
			macs.add(result.trim().replaceAll(":", "-").toUpperCase());
		}
		return macs;
	}
	
	private  String getNonLacalMacLinux(final String ip) {
		String cmd = "ping "+ip+" -c 2 && arp -a "+ip;
		String[] comands = new String[] { "/bin/sh", "-c", cmd };
		String result = "";
		String line = "";
		Process proc = null;
		InputStreamReader is = null;
		BufferedReader br = null;
		try {
			proc = Runtime.getRuntime().exec(comands);
			is = new InputStreamReader(proc.getInputStream());
			br = new BufferedReader(is);
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取linux下的mac地址异常！");
		}finally{
			close(is, br, proc);
		}
		
		Pattern pattern = Pattern
				.compile("((([0-9,A-F,a-f]{1,2}:){1,5})[0-9,A-F,a-f]{1,2})");
		Matcher matcher = pattern.matcher(result);
		while (matcher.find()) {
			result = matcher.group(1);
			if (result.indexOf(ip) <= result.lastIndexOf(matcher.group(1))) {
				break;
			}
		}
		result = result.replaceAll(":", "-");
		return result;
	}
	
	private void close(InputStreamReader is,BufferedReader br,Process proc){
		try {
			if(is != null){
				is.close();
			}
			if(br != null){
				br.close();
			} 
		}catch (IOException e) {
			EmpExecutionContext.error(e, "关闭文件流异常！");
		}
		if(proc != null){
			proc.destroy();
		}
	}
}