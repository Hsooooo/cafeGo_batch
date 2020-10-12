package co.kr.cafego.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AES256 {	
//	private static final Logger logger = LoggerFactory.getLogger("INFO");
	
	private static String enckey = "U2sdVkX1+NYhA9Oq1dZGaYshRnQOrWsJ";		//real

	private static SecretKeySpec getKeySpec() throws IOException,
			NoSuchAlgorithmException {
		byte[] bytes = new byte[32];
		SecretKeySpec spec = null;
		bytes = enckey.getBytes();
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
		enckey = key;
		return encrypt(text);
	}

	public static String decrypt(String text, String key) throws Exception {
		enckey = key;
		return decrypt(text);
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
//			logger.error(e.getMessage(), e);
//		}
	}

}
