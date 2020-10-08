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
 * ??/ë³µí?¸í??
 */
public class CryptoUtils {

	/**
	 * ???¸í?? ??ê³?ë¦¬ì? : AES256<br/>
	 * ???¸í?? ëª¨ë?? : CBC<br/>
	 * Padding ë°©ì?? : PKCS5<br/>
	 * ???¸ë¬¸ ?¸ì??? ë°©ì?? : Base64, URLEncoding?? ?µì??<br/>
	 * 
	 * @param key ???¸í?? ??
	 * @param iv ì´?ê¸°í?? ë²¡í??(ì²? ë¸?ë¡??? ???¸í???? ?? ?¬ì?©ë???? ê°?)
	 * @param str ???¸í?? ???? ë¬¸ì????
	 * @param charset
	 * @param urlEncoding ???¸í?? ?? URLEncoding ???? ?¬ë?
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
	 * ???¸í?? ??ê³?ë¦¬ì? : AES256<br/>
	 * ???¸í?? ëª¨ë?? : CBC<br/>
	 * Padding ë°©ì?? : PKCS5<br/>
	 * ???¸ë¬¸ ?¸ì??? ë°©ì?? : Base64, URLEncoding?? ?µì??<br/>
	 * 
	 * @param key ???¸í?? ??
	 * @param iv ì´?ê¸°í?? ë²¡í??(ì²? ë¸?ë¡??? ???¸í???? ?? ?¬ì?©ë???? ê°?)
	 * @param str ???¸í?? ???? ë¬¸ì????
	 * @param charset
	 * @param urlEncoding ???¸í?? ?? URLEncoding ???? ?¬ë?
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
	 * ???¸í?? ??ê³?ë¦¬ì? : AES256<br/>
	 * ???¸í?? ëª¨ë?? : ECB<br/>
	 * Padding ë°©ì?? : PKCS5<br/>
	 * ???¸ë¬¸ ?¸ì??? ë°©ì?? : Base64, URLEncoding?? ?µì??<br/>
	 * 
	 * @param key ???¸í?? ??
	 * @param str ???¸í?? ???? ë¬¸ì????
	 * @param charset
	 * @param urlEncoding ???¸í?? ?? URLEncoding ???? ?¬ë?
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
	 * ???¸í?? ??ê³?ë¦¬ì? : AES256<br/>
	 * ???¸í?? ëª¨ë?? : ECB<br/>
	 * Padding ë°©ì?? : PKCS5<br/>
	 * ???¸ë¬¸ ?¸ì??? ë°©ì?? : Base64, URLEncoding?? ?µì??<br/>
	 * 
	 * @param key ë³µí?¸í?? ??
	 * @param str ë³µí?¸í?? ???? ë¬¸ì????
	 * @param charset
	 * @param urlEncoding ???¸í?? ?? URLEncoding ???? ?¬ë?
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
	
	