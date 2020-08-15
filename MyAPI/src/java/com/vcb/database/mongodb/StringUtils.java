package com.vcb.database.mongodb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;


import org.apache.commons.lang.StringEscapeUtils;

/**
 * 字符串工具集类
 * 
 * @author eric
 * 
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
	public static String toSetMethod(String s) {
		char[] ch = s.toCharArray();
		if (ch[0] >= 97)
			ch[0] = (char) (ch[0] - 32);
		String col = "set" + String.valueOf(ch);
		return col;
	}
	/**
	 * 加密当前时间
	 */
	private static String TIME_KEY = "current_time";

	public static String encryptTime() {
		return encrypt(String.valueOf(System.nanoTime()), TIME_KEY);
	}

	/**
	 * 解密当前时间
	 */
	public static long decryptTime(String timeStr) {
		if (isEmpty(timeStr))
			return 0;
		String time = decrypt(timeStr, TIME_KEY);
		try {
			if (StringUtils.isNumeric(time))
				return Long.parseLong(time);
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 如果系统中存在旧版本的数据，则此值不能修改，否则在进行密码解析的时候出错
	 */
	private static final String PASSWORD_CRYPT_KEY = "clickcom";

	private final static String DES = "DES";

	public final static String ISO8859_1 = "8859_1";

	/**
	 * 数据解密
	 * 
	 * @param data
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 */
	public final static String decrypt(String data, String key) {
		if (data == null || key == null)
			return null;
		try {
			return new String(decrypt(hex2byte(data.getBytes()), key.getBytes()));
		} catch (BadPaddingException e) {
			//e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 数据加密
	 * 
	 * @param data
	 * @param key 密钥
	 * @return
	 * @throws Exception
	 */
	public final static String encrypt(String data, String key) {
		if (data == null || key == null)
			return null;
		try {
			return byte2hex(encrypt(data.getBytes(), key.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * BASE64编码
	 * 
	 * @param s
	 * @return String
	 */
	public static byte[] enBASE64(byte[] bytes) {
		return Base64Code.encode(bytes);
	}

	/**
	 * BASE64反编码
	 * 
	 * @param bytes
	 * @return byte[]
	 */
	public static byte[] deBASE64(byte[] bytes) {
		return Base64Code.decode(bytes);
	}

	/**
	 * BASE64编码
	 * 
	 * @param s
	 * @return String
	 */
	public static String enBASE64(String s) {
		if (s != null) {
			byte abyte0[] = s.getBytes();
			abyte0 = Base64Code.encode(abyte0);
			s = new String(abyte0);
			return s;
		}
		return null;
	}

	/**
	 * BASE64反编码
	 * 
	 * @param s
	 * @return String
	 */
	public static String deBASE64(String s) {
		if (s != null) {
			byte abyte0[] = s.getBytes();
			abyte0 = Base64Code.decode(abyte0);
			s = new String(abyte0);
			abyte0 = null;
			return s;
		}
		return null;
	}

	public static String formatContent(String content) {
		return formatContent(content, true);
	}

	/**
	 * 得到文件的后缀
	 * 
	 * @date 2006-12-21
	 * @author eric.chen
	 * @param str
	 * @return
	 */
	public static String getFileExt(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		try {
			if (str.lastIndexOf(".") != -1)
				return str.substring(str.lastIndexOf(".") + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * HTML输出内容格式转换
	 * 
	 * @param content
	 * @param isBr 是否转换BR true 是,false否
	 * @return
	 */
	public static String formatContent(String content, boolean isBr) {
		if (content == null)
			return "";
		content = StringEscapeUtils.unescapeHtml(content);
		String randomStr = String.valueOf(System.currentTimeMillis());
		String html = StringUtils.replace(content, "&nbsp;", randomStr);
		html = StringUtils.replace(html, "&", "&amp;");
		html = StringUtils.replace(html, "&amp;amp;", "&amp;");
		html = StringUtils.replace(html, "'", "&apos;");
		html = StringUtils.replace(html, "\"", "&quot;");
		html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");// 替换跳格
		// html = StringUtils.replace(html, " ", "&nbsp;");// 替换空格
		html = StringUtils.replace(html, "<", "&lt;");
		html = StringUtils.replace(html, ">", "&gt;");
		html = StringUtils.replace(html, "\r\n", "<br/>");// 替换换行
		html = StringUtils.replace(html, "\n", "<br/>");// 替换换行
		html = StringUtils.replace(html, "$", "$$");
		html = StringUtils.replace(html, "/br", "<br/>");
		if (!isBr) {
			html = StringUtils.replace(html, "&lt;br/&gt;", "<br/>");
		}
		html = html.replaceAll("ko\\.cn|k o . c n", "****");
		// html = StringUtils.replace(html, "-", "&shy;");
		return StringUtils.replace(html, randomStr, "&nbsp;").trim();
	}

	private final static String regDate = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29))$";

	/**
	 * 判断是否日期 <code>
	 *   StringUtils.isDate(null)        = false
	 *   StringUtils.isDate("19830109")  = true
	 *   StringUtils.isDate("1983109")   = false
	 *   StringUtils.isDate("1983-01-09")= true
	 * </code>
	 * 
	 * @param d 日期字串，必需大于或等于8位格式是年月日
	 * @return
	 */
	public static boolean isDate(String d) {
		if (isEmpty(d) || d.length() < 8) {
			return false;
		}
		if (isNumeric(d)) {
			StringBuffer sb = new StringBuffer(d);
			sb.insert(4, "-").insert(7, "-");
			d = sb.toString();
		}

		Pattern pat = Pattern.compile(regDate);
		Matcher mat = pat.matcher(d);
		while (mat.find())
			return true;
		return false;
	}

	private final static String regDateTime = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";

	/**
	 * 判断是否日期 <code>
	 *   StringUtils.isDateTime(null)        = false
	 *   StringUtils.isDateTime("1983-01-09 12:22:00")  = true
	 *   StringUtils.isDateTime("1983-01-09 55:69:00")  = false
	 *   StringUtils.isDateTime("1983-01-09 12:22")  = false
	 * </code>
	 * 
	 * @param d 日期字串，必需是一个标准的日期和时间如1983-01-09 13:12:00
	 * @return
	 */
	public static boolean isDateTime(String d) {
		if (isEmpty(d)) {
			return false;
		}
		Pattern pat = Pattern.compile(regDateTime);
		Matcher mat = pat.matcher(d);
		while (mat.find())
			return true;
		return false;
	}

	/**
	 * 加密
	 * 
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 * 
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}

	/**
	 * 二行制转字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 该方法返回一个字符的DBCS编码值
	 * 
	 * @param cc
	 * @return int
	 */
	protected static int getCode(char cc) {
		byte[] bs = String.valueOf(cc).getBytes();
		int code = (bs[0] << 8) | (bs[1] & 0x00FF);
		if (bs.length < 2)
			code = (int) cc;
		bs = null;
		return code;
	}

	/**
	 * 全角转半角
	 * 
	 * @param s
	 * @return
	 */
	public static String change(String s) {
		String outStr = "";
		String Tstr = "";
		byte[] b = null;
		for (int i = 0; i < s.length(); i++) {
			try {
				Tstr = s.substring(i, i + 1);
				b = Tstr.getBytes("unicode");
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (b[3] == -1) {
				b[2] = (byte) (b[2] + 32);
				b[3] = 0;

				try {
					outStr = outStr + new String(b, "unicode");
				} catch (java.io.UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else
				outStr = outStr + Tstr;
		}
		return outStr;
	}

	private static char split = (char) 1;

	/**
	 * 把一个数组变成一个string 用(char)1做分割
	 * 
	 * @date 2007-9-11
	 * @author eric.chen
	 * @param objs
	 * @return
	 */
	public static String argToString(Object... objs) {
		if (objs == null || objs.length == 0)
			return "";

		StringBuffer sb = new StringBuffer();
		for (Object o : objs) {
			sb.append(o).append(split);
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 把一个用(char)1分割的字符串变成一个数组
	 * 
	 * @date 2007-9-11
	 * @author eric.chen
	 * @param str
	 * @return
	 */
	public static String[] stringToArg(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		} else {
			return str.split(String.valueOf(split));
		}
	}

	/**
	 * 反字符串变数组
	 * 
	 * @param str
	 * @param split
	 * @return
	 * @date 2008-11-25
	 * @author eric.chan
	 */
	public static int[] stringToIntArg(String str, String split) {
		if (isEmpty(str))
			return new int[0];
		String[] strs = str.split(split);
		int[] rt = new int[strs.length];
		for (int i = 0; i < rt.length; i++) {
			rt[i] = Integer.parseInt(strs[i]);
		}
		return rt;
	}
	public static String  arrayToString(String[] array,String split) {
		StringBuffer sb=new StringBuffer();
		for(String s:array) {
			sb.append(s).append(',');
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	/**
	 * 得到一个int数的二进制的某一位的数值 0/1
	 * 
	 * @param i
	 * @param bit
	 * @return 返回0 或者1
	 * @date 2007-12-26
	 * @author eric.chan
	 */
	public static int getIntBitValue(int i, int bit) {
		return (i & 1 << (bit - 1)) > 0 ? 1 : 0;
		// return (i & intTo16IntMap.get(bit)) >> (bit-1 );
	}

	/**
	 * 校验字符串长度
	 * 
	 * @param str
	 * @param max
	 * @param min
	 * @return
	 * @date 2009-6-12
	 * @author lyh
	 */
	public static boolean validateLength(String str, int max, int min) {
		int length = str.length();
		if (length < max + 1 && length > min - 1)
			return true;
		else
			return false;
	}

	/**
	 * 字符串正则表达式校验
	 * 
	 * @param reg
	 * @param str
	 * @return
	 * @date 2009-6-12
	 * @author lyh
	 */
	public static boolean startCheck(String reg, String str) {
		if (reg == null)
			reg = "^0{0,1}(13[0-9]?|15[0-9]|150)[0-9]{8}$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * java edition readUTF used by java to java
	 * 
	 * @param dos
	 * @param str
	 * @throws IOException
	 */
	public static final void writeUTF(DataOutputStream dos, String str) throws IOException {
		if (str == null)
			str = "null";
		int size = str.length();
		dos.writeShort(size);
		short c;
		for (int i = -1; ++i < size;) {
			c = (short) str.charAt(i);
			dos.writeShort(c);
		}
	}

	public static final String readUTF(DataInputStream dis) throws IOException {

		int utflen = dis.readShort();

		StringBuilder temp = new StringBuilder();
		char c;
		for (int i = -1; ++i < utflen;) {
			c = (char) dis.readShort();
			temp.append(c);
		}
		return temp.toString();
	}

	/**
	 * 过滤手机号码86
	 * 
	 * @param phone
	 * @return
	 * @date 2009-8-3
	 * @author lyh
	 */
	public static String filter_86(String phone) {
		if (phone == null)
			return null;
		String _86 = phone.substring(0, 2);
		if (_86.equals("86"))
			return phone.substring(2, phone.length());
		return phone;
	}

	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";
	
	/**
	 * sha1加密
	 * @param decript
	 * @return
	 */
	public final static String SHA1(String decript) {
	    try {
	        MessageDigest digest = java.security.MessageDigest
	                .getInstance("SHA-1");
	        digest.update(decript.getBytes());
	        byte messageDigest[] = digest.digest();
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        // 字节数组转换为 十六进制 数
	        for (int i = 0; i < messageDigest.length; i++) {
	            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
	            if (shaHex.length() < 2) {
	                hexString.append(0);
	            }
	            hexString.append(shaHex);
	        }
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}

	/**
	 * * 使用 HMAC-SHA1 签名方法对对encryptText进行签名 * @param @param @param @param
	 * encryptText 被签名的字符串 * @param @param @param @param encryptKey 密钥 * @return @return @return @return
	 * 返回被加密后的字符串 * @throws @throws @throws @throws Exception
	 */
	public static String HmacSHA1Encrypt(String encryptText, String encryptKey) {
		try {
			byte[] data = encryptKey.getBytes(ENCODING);
			// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
			SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
			// 生成一个指定 Mac 算法 的 Mac 对象
			Mac mac = Mac.getInstance(MAC_NAME); // 用给定密钥初始化 Mac 对象
			mac.init(secretKey);
			byte[] text = encryptText.getBytes(ENCODING);
			// 完成 Mac 操作 byte byte byte
			byte[] digest = mac.doFinal(text);
			StringBuilder sBuilder = bytesToHexString(digest);
			return sBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/** * 转换成Hex * * @param @param @param @param bytesArray */
	public static StringBuilder bytesToHexString(byte[] bytesArray) {
		if (bytesArray == null) {
			return null;
		}
		StringBuilder sBuilder = new StringBuilder();
		for (byte b : bytesArray) {
			String hv = String.format("%02x", b);
			sBuilder.append(hv);
		}
		return sBuilder;
	}
	
	public static long _61to10(String s) {
		long i = 0;
		for (char c : s.toCharArray()) {
			if (i > 0) {
				i *= 61;
			}
			i += _61to10_(c);
		}
		return i;
	}

	/**
	 * 将单位的0-9a-zA-Y按顺序转换成数字<br>
	 * Z保留用作字符串分隔
	 */
	private static int _61to10_(char c) {
		int t = 0;
		if (c >= '0' && c <= '9') {
			t = c - 48;
		} else if (c >= 'a' && c <= 'z') {
			t = c - 87;
		} else if (c >= 'A' && c <= 'Y') {
			t = c - 29;
		}
		return t;
	}
	
	public static String _10to61(long i) {
		if(i == 0){
			return "0";
		}
		StringBuilder buffer = new StringBuilder();
		while (i > 0) {
			buffer.insert(0, _10to61_(i % 61));
			i /= 61;
		}
		return buffer.toString();
	}

	private static char _10to61_(long i) {
		if (i >= 0 && i <= 9) {
			i = (i + 48);
		} else if (i >= 10 && i <= 35) {
			i = (i + 87);
		} else if (i >= 36 && i <= 60) {
			i = (i + 29);
		}
		return (char) (i);
	}
	

}

/**
 * BASE64编码解码实现类
 * 
 * @author liusoft created on 2002-12-19
 */
class Base64Code {

	protected static byte[] _encode_map = { (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F',
			(byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O',
			(byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X',
			(byte) 'Y', (byte) 'Z',

			(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i',
			(byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r',
			(byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',

			(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8',
			(byte) '9',

			(byte) '+', (byte) '/' };

	protected static byte _decode_map[] = new byte[128];
	static {
		/*
		 * Fill in the decode map
		 */
		for (int i = 0; i < _encode_map.length; i++) {
			_decode_map[_encode_map[i]] = (byte) i;
		}
	}

	/**
	 * This class isn't meant to be instantiated.
	 */
	private Base64Code() {

	}

	/**
	 * This method encodes the given byte[] using the Base64 encoding
	 * 
	 * 
	 * @param data the data to encode.
	 * @return the Base64 encoded <var>data</var>
	 */
	public final static byte[] encode(byte[] data) {

		if (data == null) {
			return (null);
		}

		/*
		 * Craete a buffer to hold the results
		 */
		byte dest[] = new byte[((data.length + 2) / 3) * 4];

		/*
		 * 3-byte to 4-byte conversion and 0-63 to ascii printable conversion
		 */
		int i, j;
		int data_len = data.length - 2;
		for (i = 0, j = 0; i < data_len; i += 3) {

			dest[j++] = _encode_map[(data[i] >>> 2) & 077];
			dest[j++] = _encode_map[(data[i + 1] >>> 4) & 017 | (data[i] << 4) & 077];
			dest[j++] = _encode_map[(data[i + 2] >>> 6) & 003 | (data[i + 1] << 2) & 077];
			dest[j++] = _encode_map[data[i + 2] & 077];
		}

		if (i < data.length) {
			dest[j++] = _encode_map[(data[i] >>> 2) & 077];

			if (i < data.length - 1) {
				dest[j++] = _encode_map[(data[i + 1] >>> 4) & 017 | (data[i] << 4) & 077];
				dest[j++] = _encode_map[(data[i + 1] << 2) & 077];
			} else {
				dest[j++] = _encode_map[(data[i] << 4) & 077];
			}
		}

		/*
		 * Pad with "=" characters
		 */
		for (; j < dest.length; j++) {
			dest[j] = (byte) '=';
		}

		return (dest);
	}

	/**
	 * This method decodes the given byte[] using the Base64 encoding
	 * 
	 * 
	 * @param data the Base64 encoded data to decode.
	 * @return the decoded <var>data</var>.
	 */
	public final static byte[] decode(byte[] data) {

		if (data == null)
			return (null);

		/*
		 * Remove the padding on the end
		 */
		int ending = data.length;
		if (ending < 1) {
			return (null);
		}
		while (data[ending - 1] == '=')
			ending--;

		/*
		 * Create a buffer to hold the results
		 */
		byte dest[] = new byte[ending - data.length / 4];

		/*
		 * ASCII printable to 0-63 conversion
		 */
		for (int i = 0; i < data.length; i++) {
			data[i] = _decode_map[data[i]];
		}

		/*
		 * 4-byte to 3-byte conversion
		 */
		int i, j;
		int dest_len = dest.length - 2;
		for (i = 0, j = 0; j < dest_len; i += 4, j += 3) {
			dest[j] = (byte) (((data[i] << 2) & 255) | ((data[i + 1] >>> 4) & 003));
			dest[j + 1] = (byte) (((data[i + 1] << 4) & 255) | ((data[i + 2] >>> 2) & 017));
			dest[j + 2] = (byte) (((data[i + 2] << 6) & 255) | (data[i + 3] & 077));
		}

		if (j < dest.length) {
			dest[j] = (byte) (((data[i] << 2) & 255) | ((data[i + 1] >>> 4) & 003));
		}

		j++;
		if (j < dest.length) {
			dest[j] = (byte) (((data[i + 1] << 4) & 255) | ((data[i + 2] >>> 2) & 017));
		}

		return (dest);
	}
	
	


}