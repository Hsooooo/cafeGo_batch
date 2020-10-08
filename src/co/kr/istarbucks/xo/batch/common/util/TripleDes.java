/*
 * @(#) $Id: TripleDes.java,v 1.1 2014/03/03 04:50:53 alcyone Exp $
 * Guping Service
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
 * Insert type comment for TripleDes
 * @auther 곽문식
 */
public class TripleDes {
	public static String keyData = "lgtkfig89rtg9845687323jr";
	
	/**
	 * @param value
	 * @return
	 */
	public static byte[] encoder ( String value ) {
		ByteArrayInputStream input = null;
		ByteArrayOutputStream output = null;
		byte[] result = null;
		
		try {
			input = new ByteArrayInputStream (value.getBytes ("EUC-KR"));
			output = new ByteArrayOutputStream ();
			
			TripleDesCodec codec = new TripleDesCodec (keyData.getBytes ());
			codec.encrypt (input, output);
			
			result = output.toByteArray ();
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		} catch ( InvalidKeyException e ) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 문자열을 암호화 함
	 * @param value : 암호화 할 문자열
	 * @return : 입력된 문자열에 대한 암호화된 문자열
	 */
	@SuppressWarnings ( "unused" )
	public static String encrypt ( String value ) {
		String result = null;
		
		BASE64Encoder encoder = new BASE64Encoder ();
		byte[] a = TripleDes.encoder (value);
		result = encoder.encode (TripleDes.encoder (value));
		
		return result;
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static String decoder ( byte[] value ) {
		String result = null;
		TripleDesCodec codec = null;
		ByteArrayInputStream input = null;
		ByteArrayOutputStream output = null;
		
		try {
			input = new ByteArrayInputStream (value);
			output = new ByteArrayOutputStream ();
			codec = new TripleDesCodec (keyData.getBytes ());
			
			codec.decrypt (input, output);
			result = output.toString ("EUC-KR");
		} catch ( InvalidKeyException e ) {
			e.printStackTrace();
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static String decrypt ( String value ) {
		BASE64Decoder decoder = null;
		String result = null;
		try {
			decoder = new BASE64Decoder ();
			result = TripleDes.decoder (decoder.decodeBuffer (value));
		} catch ( IOException ioEx ) {
			ioEx.printStackTrace ();
		}
		
		decoder = null;
		return result;
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static String decryptBase64 ( String value ) {
		BASE64Decoder decoder = null;
		String dec = null;
		try {
			decoder = new BASE64Decoder ();
			dec = new String (decoder.decodeBuffer (value), "utf-8");
		} catch ( IOException ioEx ) {
			ioEx.printStackTrace ();
		}
		
		decoder = null;
		return dec;
	}
	
	
	public static String pinEncode ( String pin ) {
		StringBuffer sb = new StringBuffer ();
		sb.append ("0");
		sb.append (pin.length ());
		sb.append (pin);
		for ( int i = 14; i > pin.length (); i-- ) {
			sb.append ("F");
		}
		byte[] pinByte = ByteUtil.hexToByteArray (sb.toString ());
		byte[] ciphertext = null;
		try {
			SecretKeyFactory desEdeFactory = SecretKeyFactory.getInstance ("DESede");
			
			DESedeKeySpec keyspec = new DESedeKeySpec (keyData.getBytes ());
			SecretKey key = desEdeFactory.generateSecret (keyspec);
			
			String algo = key.getAlgorithm ();
			
			algo = algo.concat ("/ECB/NoPadding");
			
			Cipher cipher = Cipher.getInstance (algo);
			cipher.init (Cipher.ENCRYPT_MODE, key);
			
			ciphertext = cipher.doFinal (pinByte);
			
		} catch ( Exception e ) {
			e.printStackTrace ();
		}

		return ByteUtil.byteArryToHex (ciphertext);
	}
}
