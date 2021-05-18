package com.montnets.emp.shorturl.surlmanage.util;


import java.util.Stack;


public class GenUrl
 {
	private static char[] charSet = "frON2sA6XRKqc81pQCS7PuDE3lGviTaV9Wyegk0dJwbIZ5m4xUBFLhnzoYHjMt"
			.toCharArray();

	public static void main(String[] args) {
		// Long baseValue = 0L;
		// Long step = 1L;
		// //Long codeCount = 3521614606208L;
		// Long codeCount = 6000000L;
		//	
		// long count = codeCount/step;
		//	
		// String valCode ="";
		// long val=13396565346L;
		//	
		// String code = "";
		// //LoadingCache<String, LfSmsUrl> cache =
		// UrlCache.getInstance().getCache();
		// // for(long i=1000; i <= count; i++)
		// // {
		// System.out.println(val);
		// code = _10_to_62(val, 0);
		//		
		// System.out.println(code);
		// code ="100000";
		// valCode= _62_to_10(code);
		//		
		// System.out.println(valCode);
		//		
		// }
		// System.out.println(count+"��ݼ������");

		String shortUrl = "";
		long i = 56800200000L;
		while (true) {

			shortUrl = _10_to_62(i, 0);
			if (shortUrl.length() == 7) {
				System.out.println("十进制数：" + i);
				System.out.println("62进制数：" + shortUrl);
				break;
			}
			i++;
			if (i % 100000 == 0) {
				System.out.println(i);
			}
		}
		System.out.println(_10_to_62(56800235583L, 0));
	}

	public static String _10_to_62(long number, int length) {
		Long rest = number;
		Stack<Character> stack = new Stack<Character>();
		StringBuilder result = new StringBuilder(0);
		while (rest != 0) {
			Long temp=(rest - (rest / 62) * 62);
			stack.add(charSet[temp.intValue()]);
			rest = rest / 62;
		}
		for (; !stack.isEmpty();) {
			result.append(stack.pop());
		}
		int result_length = result.length();
		StringBuilder temp0 = new StringBuilder();
		for (int i = 0; i < length - result_length; i++) {
			temp0.append(charSet[0]);
		}

		return temp0.toString() + result.toString();

	}

	public static String _62_to_10(String code62) {
		int leng = code62.length();
		long val = 0L;
		char[] charStr = code62.toCharArray();

		long ss = 62L;

		for (int i = 0; i < leng; i++) {
			ss = getPow(leng - i - 1);
			if (i == leng - 1) {
				val += getIndex(charStr[i]);
			} else {

				val += getIndex(charStr[i]) * ss;
			}
		}

		return val + "";
	}

	static long getPow(int pow) {
		long val = 1;
		for (int i = 1; i <= pow; i++) {
			val = 62 * val;
		}

		return val;
	}

	static int getIndex(char charVal) {
		for (int i = 0; i < charSet.length; i++) {
			if (charVal == charSet[i]) {
				// /System.out.println(i);
				return i;
			}
		}
		return -1;
	}

}
