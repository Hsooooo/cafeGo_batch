/*
 * @(#) $Id: TripleDesEGift.java,v 1.1 2014/03/03 04:50:53 alcyone Exp $
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
 * ���ڿ� ��ȣȭ/��ȣȭ�� ó���ϴ� Ŭ����
 * Insert type comment for TripleDes
 * @auther ������
 */
public class TripleDesEGift {
	public static String keyData = "sbck832k87eh98jw68a32sr3";
	
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
	 * ���ڿ��� ��ȣȭ ��
	 * @param value : ��ȣȭ �� ���ڿ�
	 * @return : �Էµ� ���ڿ��� ���� ��ȣȭ�� ���ڿ�
	 */
	@SuppressWarnings ( "unused" )
	public static String encrypt ( String value ) {
		String result = null;
		
		BASE64Encoder encoder = new BASE64Encoder ();
		byte[] a = TripleDesEGift.encoder (value);
		result = encoder.encode (TripleDesEGift.encoder (value));
		
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
			result = TripleDesEGift.decoder (decoder.decodeBuffer (value));
		} catch ( IOException ioEx ) {
			ioEx.printStackTrace ();
		} finally {
			decoder = null;
		}
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
		} finally {
			decoder = null;
		}
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
	
//	/**
//	 * 1. ���� ��ȣȭ
//	 * 2. ��ȣȭ
//	 * @param args
//	 */
//	public static void main ( String[] args ) {
//
//		String encode = TripleDesEGift.encrypt ("87439824");
//		System .out .println ("encode :: " + encode);
//		String decode = TripleDesEGift.decrypt ("E0rmktSRu6QhwhUc+qw+Kw==");
//		System .out .println ("decode :: " + decode);
//	}
}
