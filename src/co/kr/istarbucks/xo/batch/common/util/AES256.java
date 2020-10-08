package co.kr.istarbucks.xo.batch.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AES256 {	
	//private static String KEY = "9IoSejwH76A5X1wqnGpy9R1W68Xtu3it";		//dev
	private static String KEY = "U2sdVkX1+NYhA9Oq1dZGaYshRnQOrWsJ";		//real

	private static SecretKeySpec getKeySpec() throws IOException,
			NoSuchAlgorithmException {
		byte[] bytes = new byte[32];
		SecretKeySpec spec = null;
		bytes = KEY.getBytes();
		spec = new SecretKeySpec(bytes, "AES");
		return spec;
	}

	public static String encrypt(String text) throws Exception {
		SecretKeySpec spec = getKeySpec();
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, spec);
		return Base64Utils.base64Encode(cipher.doFinal(text.getBytes("UTF-8")));
	}

	public static String decrypt(String text) throws Exception {
		SecretKeySpec spec = getKeySpec();
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, spec);
		return new String(cipher.doFinal(Base64Utils.base64Decode(text)), "UTF-8");
	}
	public static String encrypt(String text, String key) throws Exception {
		KEY = key;
		return encrypt(text);
	}

	public static String decrypt(String text, String key) throws Exception {
		KEY = key;
		return decrypt(text);
	}

	//전자영수증 관련 AES256
	private static String RECEIPT_KEY = "$TaRBUCK$R2C2iP+";
	private static SecretKeySpec getKeySpecByReceipt() throws IOException, NoSuchAlgorithmException {
		byte[] bytes = new byte[32];
		SecretKeySpec spec = null;
		bytes = RECEIPT_KEY.getBytes();
		spec = new SecretKeySpec(bytes, "AES");
		return spec;
	}
	public static String receiptEncrypt(String text) throws Exception {
		SecretKeySpec spec = getKeySpecByReceipt();
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, spec);
		return Base64Utils.base64Encode(cipher.doFinal(text.getBytes("UTF-8")));
	}
	public static String receiptDecrypt(String text) throws Exception {
		SecretKeySpec spec = getKeySpecByReceipt();
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, spec);
		return new String(cipher.doFinal(Base64Utils.base64Decode(text)), "UTF-8");
	}
	public static String receiptEncrypt(String text, String key) throws Exception {
		RECEIPT_KEY = key;
		return receiptEncrypt(text);
	}
	
	public static String receiptDecrypt(String text, String key) throws Exception {
		RECEIPT_KEY = key;
		return receiptDecrypt(text);
	}
	
	public static String stringToHex(String text) throws UnsupportedEncodingException{
		StringBuilder sb = new StringBuilder();

		byte[] byteEncStr = text.getBytes("UTF-8");
		
		for(final byte b: byteEncStr){
			sb.append(String.format("%02x", b&0xff));
		}
		
		return sb.toString().toUpperCase();
	}
	
	public static String hexToString(String text) {
		byte [] hexBytes = new byte [text.length() / 2];  
		int j = 0;  
		for (int i = 0; i < text.length(); i += 2) {  
		    hexBytes[j++] = Byte.parseByte(text.substring(i, i + 2), 16);  
		}  
		String hr = new String(hexBytes);

		return hr;
	}

	public static String receiptEncryptByHex(String text) throws Exception {
		return stringToHex(receiptEncrypt(text));
	}
	
	public static String receiptDecryptByString(String text) throws Exception {
		return receiptDecrypt(hexToString(text));
	}
	
	
	/************************** 홀케익 히스토리 상세용 **************************/
	/**
	 * AES256 암호화 (암호화 후 URLEncode 처리)
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String encryptUrlEncode(String text) throws Exception {
		return URLEncoder.encode(encrypt(text), "UTF-8");
	}
	
	/**
	 * AES256 복호화 (URLDecode 처리 후 복호화)
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String decryptUrlDecode(String text) throws Exception {
		return decrypt(URLDecoder.decode(text, "UTF-8"));
	}
	/************************** 홀케익 히스토리 상세용 **************************/
	
	
	public static void main(String[] args) {    
//		try {
			
//			String text = "60876356829713674859";
//			String encText = AES256.encryptUrlEncode(text);
//			
//			System.out.println("nortext -> " + text);
//			System.out.println("encText -> " + encText);
//			System.out.println("decText -> " + AES256.decryptUrlDecode(encText));
			
//			String str = "201610249340990100";
//			String encStr = AES256.receiptEncrypt(str);
//			System.out.println("encStr : " + encStr);
//			
//			String hexEncStr = stringToHex(encStr);
//			System.out.println("hexEncStr : " + hexEncStr);
//			
//			String hexDesStr = hexToString(hexEncStr);
//			System.out.println("hexDesStr : " + hexDesStr);
//			
//			String desStr = AES256.receiptDecrypt(hexDesStr);
//			System.out.println("desStr : " + desStr);
//			System.out.println("==========================================");
//
//			System.out.println("totalEnc : " + receiptEncryptByHex(str));
//			System.out.println("totalDes : " + receiptDecryptByString(receiptEncryptByHex(str)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
