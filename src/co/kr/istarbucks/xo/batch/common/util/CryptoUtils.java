package co.kr.istarbucks.xo.batch.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * ??/복�?��??
 */
public class CryptoUtils {

	/**
	 * ???��?? ??�?리�? : AES256<br/>
	 * ???��?? 모�?? : CBC<br/>
	 * Padding 방�?? : PKCS5<br/>
	 * ???�문 ?��??? 방�?? : Base64, URLEncoding?? ?��??<br/>
	 * 
	 * @param key ???��?? ??
	 * @param iv �?기�?? 벡�??(�? �?�??? ???��???? ?? ?��?��???? �?)
	 * @param str ???��?? ???? 문�????
	 * @param charset
	 * @param urlEncoding ???��?? ?? URLEncoding ???? ?��?
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String encryptAES256CBC(String key, String iv, String str, String charset, boolean urlEncoding)
			throws UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		
		byte[] keyData = key.getBytes();

		SecretKey secureKey = new SecretKeySpec(keyData, "AES");

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes(Charset.forName(charset))));
		
		byte[] encrypted = c.doFinal(str.getBytes(charset));
		String encStr = new String(Base64.encodeBase64(encrypted));
		
		if(urlEncoding) {
			return URLEncoder.encode(encStr, charset);
		} else {
			return encStr;
		}
	}
	
	/**
	 * ???��?? ??�?리�? : AES256<br/>
	 * ???��?? 모�?? : CBC<br/>
	 * Padding 방�?? : PKCS5<br/>
	 * ???�문 ?��??? 방�?? : Base64, URLEncoding?? ?��??<br/>
	 * 
	 * @param key ???��?? ??
	 * @param iv �?기�?? 벡�??(�? �?�??? ???��???? ?? ?��?��???? �?)
	 * @param str ???��?? ???? 문�????
	 * @param charset
	 * @param urlEncoding ???��?? ?? URLEncoding ???? ?��?
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String decryptAES256CBC(String key, String iv, String str, String charset, boolean urlEncoding)
			throws UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		
		String decStr = str;
		if(urlEncoding) {
			decStr = URLDecoder.decode(str, charset);
		}
		
		byte[] keyData = key.getBytes();
		
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");
		
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes(Charset.forName(charset))));

		byte[] byteStr = Base64.decodeBase64(decStr.getBytes());

		return new String(c.doFinal(byteStr), charset);
	}
	
	/**
	 * ???��?? ??�?리�? : AES256<br/>
	 * ???��?? 모�?? : ECB<br/>
	 * Padding 방�?? : PKCS5<br/>
	 * ???�문 ?��??? 방�?? : Base64, URLEncoding?? ?��??<br/>
	 * 
	 * @param key ???��?? ??
	 * @param str ???��?? ???? 문�????
	 * @param charset
	 * @param urlEncoding ???��?? ?? URLEncoding ???? ?��?
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String encryptAES256ECB(String key, String str, String charset, boolean urlEncoding)
			throws UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		
		byte[] keyData = key.getBytes();

		SecretKey secureKey = new SecretKeySpec(keyData, "AES");

		Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, secureKey);

		byte[] encrypted = c.doFinal(str.getBytes(charset));
		String encStr = new String(Base64.encodeBase64(encrypted));

		if(urlEncoding) {
			return URLEncoder.encode(encStr, charset);
		} else {
			return encStr;
		}
	}
	
	/**
	 * ???��?? ??�?리�? : AES256<br/>
	 * ???��?? 모�?? : ECB<br/>
	 * Padding 방�?? : PKCS5<br/>
	 * ???�문 ?��??? 방�?? : Base64, URLEncoding?? ?��??<br/>
	 * 
	 * @param key 복�?��?? ??
	 * @param str 복�?��?? ???? 문�????
	 * @param charset
	 * @param urlEncoding ???��?? ?? URLEncoding ???? ?��?
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String decryptAES256ECB(String key, String str, String charset, boolean urlEncoding)
			throws UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		
		String decStr = str;
		if(urlEncoding) {
			decStr = URLDecoder.decode(str, charset);
		}
		
		byte[] keyData = key.getBytes();
		
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");
		
		Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, secureKey);

		byte[] byteStr = Base64.decodeBase64(decStr.getBytes());

		return new String(c.doFinal(byteStr), charset);
	}
}
	
	