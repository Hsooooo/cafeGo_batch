package co.kr.cafego.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 회원 패스워드 단방향 암호화
 * @author rkrk6
 *
 */
public class SHA256 {
	
	public static String encrypt(String text) throws Exception{
		return getEncrypt(text);
	}
	
	/**
	 * 암호화
	 * @param text
	 * @param salt
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String getEncrypt(String text) throws Exception {
		String result = "";
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(text.getBytes("UTF-8"));
			
			byte[] byteData = md.digest();
			
			StringBuffer sb = new StringBuffer();
			
			for(int i=0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
			}
			
			result = sb.toString();
		}catch(NoSuchAlgorithmException nae) {
			throw nae;
		}catch(UnsupportedEncodingException uee) {
			throw uee;
		}
		
		return result;
	}
	
}
