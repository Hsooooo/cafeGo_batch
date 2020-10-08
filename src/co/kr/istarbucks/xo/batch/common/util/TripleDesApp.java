/*
 * @(#) $Id: TripleDesApp.java,v 1.1 2016/11/10 10:37:31 dev99 Exp $
 * 
 * Guping Service
 * 
 * Copyright 2010 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 문자열 암호화/복호화를 처리하는 클래스 
 * TODO Insert type comment for TripleDes
 *
 * @auther 곽문식
 */
public class TripleDesApp {
	public static String keyData = "st5sp593l49sepk3076246rs";
	public String basicKey = "st5sp593l49sepk3076246rs";
	 

	public TripleDesApp() {
		keyData = basicKey;
		if (keyData.getBytes().length == 16) {
			byte[] temp = keyData.getBytes();
			byte[] key = new byte[24];
			System.arraycopy(temp, 0, key, 0, temp.length);
			System.arraycopy(temp, 0, key, 16, 8);
			keyData = new String (key);
			System.out.println("keyData1 :: " + keyData);
		}
	}
	
	public TripleDesApp(String hexKeyString) {
		byte[] keyByte = ByteUtil.hexToByteArray(hexKeyString);
		System.out.println("hexKeyString :: " + hexKeyString);
		System.out.println("keyByte.length :: " + keyByte.length);
		if (keyByte.length == 16) {
			byte[] temp = (byte[]) keyByte.clone();
			byte[] key = new byte[24];
			System.arraycopy(temp, 0, key, 0, temp.length);
			System.arraycopy(temp, 0, key, 16, 8);
			keyData = new String (key);
			System.out.println("keyData2 :: " + keyData);
		} else {
			keyData = new String (keyByte);
			System.out.println("keyData :: " + keyData);
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] encoder(String value) {
		ByteArrayInputStream input = null;
		ByteArrayOutputStream output = null;
		byte[] result = null;
		
		try {
			input = new ByteArrayInputStream(value.getBytes("EUC-KR"));
			output = new ByteArrayOutputStream();

			TripleDesCodec codec = new TripleDesCodec(keyData.getBytes());
			codec.encrypt(input, output);

			result = output.toByteArray();
		} catch (UnsupportedEncodingException e) {
		} catch (InvalidKeyException e) {
		} finally {
			return result;
		}
	}

	/**
	 * 문자열을 암호화 함
	 * @param value : 암호화 할 문자열
	 * @return : 입력된 문자열에 대한 암호화된 문자열 
	 */
	public static String encrypt(String value) {
		String result = null;

		BASE64Encoder encoder = new BASE64Encoder();
		byte[] a = TripleDesApp.encoder(value);
		result = encoder.encode(TripleDesApp.encoder(value));
		

		return result;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String decoder(byte[] value) {
		String result = null;
		TripleDesCodec codec = null;
		ByteArrayInputStream input = null;
		ByteArrayOutputStream output = null;

		try {
			input = new ByteArrayInputStream(value);
			output = new ByteArrayOutputStream();
			codec = new TripleDesCodec(keyData.getBytes());

			codec.decrypt(input, output);
			result = output.toString("EUC-KR");
		} catch (InvalidKeyException e) {
		} catch (UnsupportedEncodingException e) {
		} finally {
			return result;
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String decrypt(String value) {
		BASE64Decoder decoder = null;
		String result = null;
		try {
			decoder = new BASE64Decoder();
			result = TripleDesApp.decoder(decoder.decodeBuffer(value));
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			decoder = null;
			return result;
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String decryptBase64(String value) {
		BASE64Decoder decoder = null;
		String dec = null;
		try {
			decoder = new BASE64Decoder();
			dec = new String(decoder.decodeBuffer(value), "utf-8");
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			decoder = null;
			return dec;
		}
	}
	
	public static String pinEncode(String pin) {
		StringBuffer sb = new StringBuffer();
		sb.append("0");
		sb.append(""+pin.length());
		sb.append(pin);
		for (int i = 14; i > pin.length(); i--) {
			sb.append("F");
		}
		byte[] pinByte = ByteUtil.hexToByteArray(sb.toString());
		byte[] ciphertext = null;
		try {
			SecretKeyFactory desEdeFactory = SecretKeyFactory.getInstance("DESede");
        
			DESedeKeySpec keyspec = new DESedeKeySpec(keyData.getBytes());
			SecretKey key = desEdeFactory.generateSecret(keyspec);
                    
			String algo = key.getAlgorithm();
                    
			algo = algo.concat("/ECB/NoPadding");

			Cipher cipher = Cipher.getInstance(algo); 
			cipher.init(Cipher.ENCRYPT_MODE, key); 
                    
			ciphertext = cipher.doFinal(pinByte);
			
			System.out.println("Plaintext PIN Block : " + ByteUtil.byteArryToHex(pinByte));
			System.out.println("Ciphered PIN Block : " +  ByteUtil.byteArryToHex(ciphertext));

		} catch (Exception e) {
			e.printStackTrace();
		}


		return ByteUtil.byteArryToHex(ciphertext);
	}
	
	
	/*
	 * TEST
	 */
	public static void main(String[] args){
		String cardNumber = "7777111122223333";

		//암호화
		String encStr = TripleDesApp.encrypt(cardNumber);
		
		//복호화
		String decStr = TripleDesApp.decrypt(encStr);
		
		System.out.println("enc_cardNumber :: " + encStr);
		System.out.println("dec_cardNumber :: " + decStr);
	}
}
