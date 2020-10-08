package co.kr.istarbucks.xo.batch.common.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AES128 {	
	private static String KEY = "U2sdVkX1+NYhA9Oq";

	private static SecretKeySpec getKeySpec() throws IOException, NoSuchAlgorithmException {
		byte[] bytes = new byte[16];
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
//			String text = "62016111818391100252";
//
//			String enc = AES128.encrypt(text);
//			System.out.println("origin str = " + text);
//			System.out.println("encrypt str = " + enc);
//			System.out.println("decrypt str = " + AES128.decrypt(enc));
//			
//			String encText = AES128.encryptUrlEncode(text);
//			System.out.println("nor_Text -> " + text);
//			System.out.println("enc_Text -> " + encText);
//			System.out.println("dec_Text -> " + AES128.decryptUrlDecode(encText));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
