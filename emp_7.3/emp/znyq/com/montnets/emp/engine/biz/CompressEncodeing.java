package com.montnets.emp.engine.biz;

import java.util.HashMap;
import java.util.Map;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 网讯编码处理
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-28 下午05:20:20
 * @description
 */
public class CompressEncodeing {
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
			'Z', '!', '=', };


	/**
	 * 把10进制的数字转换成64进制
	 * 
	 * @param number
	 * @param shift
	 * @return
	 */
	public static String CompressNumber(long number, int shift) {
		char[] buf = new char[64];
		int charPos = 64;
		int radix = 1 << shift;
		long mask = radix - 1L;
		do {
			buf[--charPos] = digits[(int) (number & mask)];
			number >>>= shift;
		} while (number != 0);
		return new String(buf, charPos, (64 - charPos));
	}

	/**
	 * 把64进制的字符串转换成10进制
	 * 
	 * @param decompStr
	 * @return
	 */
	public static long UnCompressNumber(String decompStr) {
		long result = 0;
		for (int i = decompStr.length() - 1; i >= 0; i--) {
			if (i == decompStr.length() - 1) {
				result += getCharIndexNum(decompStr.charAt(i));
				continue;
			}
			for (int j = 0; j < digits.length; j++) {
				if (decompStr.charAt(i) == digits[j]) {
					result += ((long) j) << 6 * (decompStr.length() - 1 - i);
				}
			}
		}
		return result;
	}
	
	public static String JieM(String pageid,String phone){
		String pdph = "";
		String h = CompressNumber(Long.valueOf(phone),6);
		
		String p = CompressNumber(Long.valueOf(pageid),6);
		pdph = p;
		if(h.length()==6){
			String ph =  h.substring(5,6)+h.substring(1,5)+ h.substring(0,1);
			pdph = pdph+ph;
		}
  		
		return pdph;
	}
	public static String JieMPhone(String phone){
		String pdph = "";
		if(phone!=null && !"".equals(phone)){
			String h = CompressNumber(Long.valueOf(phone),6);
			if(h.length()==6){
				String ph =  h.substring(5,6)+h.substring(1,5)+ h.substring(0,1);
				pdph = ph;
			}
		}
		return pdph;
	}
	public static Map<String,String> UnJieM(String m){
		//phone
		String h = "";
		//pageid
		String p = "";
		//taskid
		String t = "";
		Map<String,String>  pdph = new HashMap<String,String> ();
		try{
			if(m.length()>6){
				String ph = m.substring(m.length()-6, m.length());
				String phone = ph.substring(5,6)+ph.substring(1,5)+ ph.substring(0,1);
				h = UnCompressNumber(phone)+"";
				String pd = m.substring(0, m.length()-6);
				
				String pj = pd.substring(0,pd.indexOf("-"));
				p = UnCompressNumber(pj)+"";
				
				String td = pd.substring(pd.indexOf("-")+1,pd.length());
				t = UnCompressNumber(td)+"";
			}
			else
			{
				String[] mArray = m.split("-");
				p = UnCompressNumber(mArray[0])+"";
			}
			
			pdph.put("p", p);
			pdph.put("h", h);
			pdph.put("t", t);
			
		}catch(Exception e){
			EmpExecutionContext.error("pageid加phone解密出错：：" + e);
		} 
		return pdph;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

				
	}
	/**
	 * 
	 * @param ch
	 * @return
	 */
	private static long getCharIndexNum(char ch) { 
		int num = ((int) ch);
		if (num >= 48 && num <= 57) {
			return num - 48L;
		} else if (num >= 97 && num <= 122) {
			return num - 87L;
		} else if (num >= 65 && num <= 90) {
			return num - 29L;
		} else if (num == 33) {
			return 62;
		} else if (num == 61) {
			
			return 63;
		}
		return 0;
	}

}
