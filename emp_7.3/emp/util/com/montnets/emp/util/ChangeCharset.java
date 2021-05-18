package com.montnets.emp.util;

import java.io.*;
import java.net.URLEncoder;
import java.util.BitSet;

import com.montnets.emp.common.context.EmpExecutionContext;


public class ChangeCharset {
	public static final String US_ASCII = "US-ASCII";
	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String UTF_8 = "UTF-8";
	public static final String UTF_16BE = "UTF-16BE";
	public static final String UTF_16LE = "UTF-16LE";
	public static final String UTF_16 = "UTF-16";
	public static final String GBK = "GBK";

	public static final String GB2312 = "GB2312";
	private static final String UTF_8_NO_BOM = "UTF-8_NO_BOM";

	public static String toASCII(String str) throws UnsupportedEncodingException {
		return changeCharset(str, US_ASCII);
	}

	public static String toISO_8859_1(String str) throws UnsupportedEncodingException {
		return changeCharset(str, ISO_8859_1);
	}

	public static String toUTF_8(String str) throws UnsupportedEncodingException {
		return changeCharset(str, UTF_8);
	}

	public static String toUTF_16BE(String str) throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16BE);
	}

	public static String toUTF_16LE(String str) throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16LE);
	}

	public static String toUTF_16(String str) throws UnsupportedEncodingException {
		return changeCharset(str, UTF_16);
	}

	public static String toGBK(String str) throws UnsupportedEncodingException {
		return changeCharset(str, GBK);
	}
	public static String toGB2312(String str) throws UnsupportedEncodingException {
		return changeCharset(str, GB2312);
	}

	/**
	 * 将字符串转换为newCharset编码格式的字符串
	 * @param str
	 * @param newCharset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCharset(String str, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			byte[] bs = str.getBytes();
			return new String(bs, newCharset);
		}
		return null;
	}

	/**
	 * 将oldCharset编码的字符串转换为newCharset编码的字符串
	 * @param str 需转换的字符串
	 * @param oldCharset 源字符串编码
	 * @param newCharset 转换后的字符串编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCharset(String str, String oldCharset, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			byte[] bs = str.getBytes(oldCharset);
			return new String(bs, newCharset);
		}
		return null;
	}

	/**
	 * 将字节数组串转换成Hex码
	 * @param bytes 自己数组
	 * @return
	 */
	public static final String encodeHex(byte[] bytes) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };

		char[] ob = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++)
		{
			ob[i * 2] = Digit[(bytes[i] >>> 4) & 0X0F];
			ob[i * 2 + 1] = Digit[bytes[i] & 0X0F];
		}
		return new String(ob);
	}
	
	/**
	 * 将字符串以URL编码返回值
	 * @param title
	 * @param charSet
	 * @return
	 * @throws Exception
	 */
	public static String urlEncode(String title,String charSet) throws Exception{
		
		return URLEncoder.encode(title, charSet);
		
	}

	
/*	public static void main(String[] args) throws UnsupportedEncodingException ,Exception{
		字符		    	        “A”		   “国”		   “國”
		US-ASCII				41			3f（错误）	3f
		ISO8859-1				41			3f	        3f
		GBK 					41			b9 fa		87 f8
		BIG5 					41			3f（错误）	b0 ea
		UTF-16BE				0041		56 fd		57 0b
		UTF-16LE				4100		fd 56		0b 57
		UTF-8					41			e5 9b bd	e5 9c 8b

		
		//EmpExecutionContext.debug("==="+ChangeCharset.urlEncode("你好，123", "GBK")+"===");
		String str = "你好，123" ;
		//EmpExecutionContext.debug("str：" + str);

		String gbk = ChangeCharset.toGBK(str);
		//EmpExecutionContext.debug("转换成GBK码：" + gbk);
		//EmpExecutionContext.debug("转换成Hex码==" + encodeHex(str.getBytes("GBK"))+"==");
		//EmpExecutionContext.debug();

		String ascii = ChangeCharset.toASCII(str);
		//EmpExecutionContext.debug("转换成US-ASCII：" + ascii);
		//EmpExecutionContext.debug("转换成Hex码==" + encodeHex(str.getBytes("US-ASCII"))+"==");
		//EmpExecutionContext.debug();

		String iso88591 = ChangeCharset.toISO_8859_1(str);
		//EmpExecutionContext.debug("转换成ISO-8859-1码：" + iso88591);
		//EmpExecutionContext.debug("转换成Hex码==" + encodeHex(str.getBytes("ISO-8859-1"))+"==");
		//EmpExecutionContext.debug();

		gbk = ChangeCharset.changeCharset(iso88591, ISO_8859_1, GBK);
		//EmpExecutionContext.debug("再把ISO-8859-1码的字符串转换成GBK码：" + gbk);
		//EmpExecutionContext.debug();

		String utf8 = ChangeCharset.toUTF_8(str);
		//EmpExecutionContext.debug();
		//EmpExecutionContext.debug("转换成UTF-8码：" + utf8);
		//EmpExecutionContext.debug("转换成Hex码==" + encodeHex(str.getBytes(UTF_8))+"==");
		String utf16be = ChangeCharset.toUTF_16BE(str);
		//EmpExecutionContext.debug("转换成UTF-16BE码：" + utf16be);
		//EmpExecutionContext.debug("转换成Hex码==" + encodeHex(str.getBytes(UTF_16BE))+"==");
		gbk = ChangeCharset.changeCharset(utf16be, UTF_16BE, GBK);
		//EmpExecutionContext.debug("再把UTF-16BE编码的字符转换成GBK码：" + gbk);
		//EmpExecutionContext.debug();

		String utf16le = ChangeCharset.toUTF_16LE(str);
		//EmpExecutionContext.debug("转换成UTF-16LE码：" + utf16le);
		//EmpExecutionContext.debug("转换成Hex码==" + encodeHex(str.getBytes(UTF_16LE))+"==");
		gbk = ChangeCharset.changeCharset(utf16le, UTF_16LE, GBK);
		//EmpExecutionContext.debug("再把UTF-16LE编码的字符串转换成GBK码：" + gbk);
		//EmpExecutionContext.debug();

		String utf16 = ChangeCharset.toUTF_16(str);
		//EmpExecutionContext.debug("转换成UTF-16码：" + utf16);
		//EmpExecutionContext.debug("转换成Hex码==" + encodeHex(str.getBytes(UTF_16))+"==");
		String gb2312 = ChangeCharset.changeCharset(utf16, UTF_16, GB2312);
		//EmpExecutionContext.debug("再把UTF-16编码的字符串转换成GB2312码：" + gb2312);
	}*/

	/**
	 * 将转码后的字符创还原为原先的字符创
	 */
	public static String toStringHex(String s)
	{
		try
		{
			if("0x".equals(s.substring(0, 2)))
			{
				s = s.substring(2);
			}
			byte[] baKeyword = new byte[s.length() / 2];
			for (int i = 0; i < baKeyword.length; i++)
			{
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			}

			s = new String(baKeyword, "gbk");//
			return s;
		}
		catch (Exception e)
		{
			//这里不打堆栈，太多了
			EmpExecutionContext.error("字符串转码异常。string="+s);
			return s;
		}

	}


	/**
	 * 检测单字节，判断是否为utf8
	 *
	 * @param b
	 * @return
	 */
	private static boolean checkUtf8Byte(byte b) throws Exception {
		BitSet bitSet = convert2BitSet(b);
		return bitSet.get(0) && !bitSet.get(1);
	}

	private static int BYTE_SIZE = 8;
	/**
	 * 检测bitSet中从开始有多少个连续的1
	 *
	 * @param bitSet
	 * @return
	 */
	private static int getCountOfSequential( BitSet bitSet) {
		int count = 0;
		for (int i = 0; i < BYTE_SIZE; i++) {
			if (bitSet.get(i)) {
				count++;
			} else {
				break;
			}
		}
		return count;
	}


	/**
	 * 将整形转为BitSet
	 *
	 * @param code
	 * @return
	 */
	private static BitSet convert2BitSet(int code) {
		BitSet bitSet = new BitSet(BYTE_SIZE);

		for (int i = 0; i < BYTE_SIZE; i++) {
			int tmp3 = code >> (BYTE_SIZE - i - 1);
			int tmp2 = 0x1 & tmp3;
			if (tmp2 == 1) {
				bitSet.set(i);
			}
		}
		return bitSet;
	}

	/**
	 * 检测多字节，判断是否为utf8，已经读取了一个字节
	 *
	 * @param bis
	 * @param bitSet
	 * @return
	 */
	private static boolean checkMultiByte( BufferedInputStream bis,  BitSet bitSet) throws Exception {
		int count = getCountOfSequential(bitSet);
		byte[] bytes = new byte[count - 1];//已经读取了一个字节，不能再读取
		bis.read(bytes);
		for (byte b : bytes) {
			if (!checkUtf8Byte(b)) {
				return false;
			}
		}
		return true;
	}



	/**
	 * 是否是无BOM的UTF8格式，不判断常规场景，只区分无BOM UTF8和GBK
	 * @param bis
	 * @return
	 */
	private static boolean isUTF8NoBom(BufferedInputStream bis) throws Exception {
		bis.reset();

		//读取第一个字节
		int code = bis.read();
		do {
			BitSet bitSet = convert2BitSet(code);
			//判断是否为单字节
			if (bitSet.get(0)) {//多字节时，再读取N个字节
				if (!checkMultiByte(bis, bitSet)) {//未检测通过,直接返回
					return false;
				}
			} else {
				//单字节时什么都不用做，再次读取字节
			}
			code = bis.read();
		} while (code != -1);
		return true;
	}



	/**
	 * 判断文本文件的编码格式
	 * @param inp 文本文件流
	 * @return
	 */
	public String get_charset( InputStream inp ) 
	{
		//默认返回GBK
        String charset = "GBK";   
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis=null;
        try 
        {   
            bis = new BufferedInputStream( inp );   
            bis.mark( 0 );   
            //通过读取文件流的开头标示判断格式
            int read = bis.read( first3Bytes, 0, 3 );   
            if ( read == -1 ) return charset; 
            if ( first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE ) {   
                charset = "Unicode";   
            }   
            else if ( first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF ) {   
                charset = "UTF-16BE";   
            }   
            else if ( first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF ) {   
                charset = "UTF-8";   
            }else if(isUTF8NoBom(bis)){
				// 判断是GBK还是UTF-8无BOM
				charset = UTF_8_NO_BOM;
			}
			bis.reset();
        }catch(Exception e)
        {
        	//add by denglj 2018.11.29
            if(bis!=null){
                try {
                    bis.close();
                }
                catch (IOException e1) {
                    EmpExecutionContext.error(e1,"文本文件关闭流异常！");
                } 
            }
        	EmpExecutionContext.error(e,"判断文本文件编码格式异常！");
        }finally {
            IOUtils.closeQuietly(bis);
        }
        return charset;
	}


    public BufferedReader getReader(InputStream in, InputStream inCopy) throws IOException {
        return getReader(in, inCopy, null);
    }

    /**
     * 获取文件字符读取流
     * @param in 输入字节流
     * @param inCopy 备份输入字节流，原先调用reset方法在读取超过5w行数据的txt进行复位报错，改为使用备份输入流进行读取
     * @param reader 字符读取流
     * @return 字符流
     * @throws IOException 异常
     */
    public BufferedReader getReader(InputStream in, InputStream inCopy, BufferedReader reader) throws IOException {
        String charSet = get_charset(in);
        boolean isUtf8NoBom = ChangeCharset.UTF_8_NO_BOM.equals(charSet);
        if(isUtf8NoBom){
            charSet = ChangeCharset.UTF_8;
        }
        if(reader == null){
            reader = new BufferedReader(new InputStreamReader(inCopy, charSet));
        }
        if(charSet.startsWith("UTF-") && !isUtf8NoBom){
            reader.read(new char[1]);
        }
        return reader;
    }

	/**
	 * 上传.rar文件时，archive.getInputStream(fileHeader) 多次获取输入流时会出现问题，
	 * 重载一个新方法，使用流复制来实现！
	 *
	 * @param in 输入流
	 * @return java.io.BufferedReader
	 */
	public BufferedReader getReader(InputStream in) throws IOException {

		InputStream stream1 = null;
		InputStream stream2 = null;
		try {
			ByteArrayOutputStream byteArrayOutputStream = cloneInputStream(in);
			stream1 = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			stream2 = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

			String charSet = get_charset(stream1);
			boolean isUtf8NoBom = ChangeCharset.UTF_8_NO_BOM.equals(charSet);
			if(isUtf8NoBom){
				charSet = ChangeCharset.UTF_8;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream2, charSet));
			if(charSet.startsWith("UTF-") && !isUtf8NoBom){
				reader.read(new char[1]);
			}
			return reader;
		} catch (IOException e) {
			throw e;
		} finally {
			if(stream1 != null) {
				stream1.close();
			}
			if(stream2 != null) {
				stream2.close();
			}
		}
	}

	/**
	 * InputStream转ByteArrayOutputStream
	 *
	 * @param input 输入流
	 */
	private static ByteArrayOutputStream cloneInputStream(InputStream input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = input.read(buffer)) > -1) {
			baos.write(buffer, 0, len);
		}
		baos.flush();
		return baos;
	}
}
