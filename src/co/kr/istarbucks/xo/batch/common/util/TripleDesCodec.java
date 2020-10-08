/*
 * @(#) $Id: TripleDesCodec.java,v 1.1 2014/03/03 04:50:53 alcyone Exp $
 * Guping Service
 * Copyright 2010 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * TripleDesCodec
 * @auther
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings("DES_USAGE") 
public class TripleDesCodec {
	private Cipher dcipher;
	private Cipher ecipher;
	private final byte buf[];
	
	/**
	 * @param rawKey
	 * @throws InvalidKeyException
	 */
	public TripleDesCodec ( byte rawKey[] ) throws InvalidKeyException {
		buf = new byte[1024];
		init (rawKey);
	}
	
	/**
	 * @param rawKey
	 * @throws InvalidKeyException
	 */
	public void init ( byte rawKey[] ) throws InvalidKeyException {
		try {
			SecretKeyFactory desEdeFactory = SecretKeyFactory.getInstance ("DESede");
			DESedeKeySpec keyspec = new DESedeKeySpec (rawKey);
			//SecretKeySpec keyspec = new SecretKeySpec(rawKey, "DESede");
			SecretKey k = desEdeFactory.generateSecret (keyspec);
			
			init (k);
		} catch ( NoSuchAlgorithmException nosuchalgorithmexception ) {
			nosuchalgorithmexception.printStackTrace ();
		} catch ( InvalidKeySpecException invalidkeyspecexception ) {
			invalidkeyspecexception.printStackTrace ();
		} catch ( Exception e ) {
			e.printStackTrace ();
		}
	}
	
	/**
	 * @param key
	 * @throws InvalidKeyException
	 */
	public void init ( SecretKey key ) throws InvalidKeyException {
		try {
			ecipher = Cipher.getInstance ("DESede/ECB/PKCS5Padding");
			dcipher = Cipher.getInstance ("DESede/ECB/PKCS5Padding");
			ecipher.init (Cipher.ENCRYPT_MODE, key);
			dcipher.init (Cipher.DECRYPT_MODE, key);
		} catch ( NoSuchPaddingException nosuchpaddingexception ) {
			nosuchpaddingexception.printStackTrace ();
		} catch ( NoSuchAlgorithmException nosuchalgorithmexception ) {
			nosuchalgorithmexception.printStackTrace ();
		}
	}
	
	/**
	 * @param in
	 * @param out
	 */
	public void decrypt ( InputStream in, OutputStream out ) {
		try {
			in = new CipherInputStream (in, dcipher);
			int numRead = 0;
			
			while ( ( numRead = in.read (buf) ) >= 0 ) {
				out.write (buf, 0, numRead);
			}
			
			out.close ();
		} catch ( Exception ioexception ) {
			ioexception.printStackTrace ();
		}
	}
	
	/**
	 * @param in
	 * @param out
	 */
	public void encrypt ( InputStream in, OutputStream out ) {
		try {
			out = new CipherOutputStream (out, ecipher);
			for ( int numRead = 0; ( numRead = in.read (buf) ) >= 0; ) {
				out.write (buf, 0, numRead);
			}
			
			out.close ();
		} catch ( IOException ioexception ) {
			ioexception.printStackTrace ();
		}
	}
	
	/**
	 * @param input
	 * @return
	 */
	public static byte[] decoderString ( String input ) {
		byte output[] = new byte[input.length () / 2];
		char cInput[] = new char[input.length ()];
		input.getChars (0, input.length (), cInput, 0);
		
		for ( int i = 0; i < input.length (); i++ ) {
			int num = 0;
			
			if ( '0' <= cInput[i] && cInput[i] <= '9' ) {
				num = cInput[i] - 48;
			} else if ( 'A' <= cInput[i] && cInput[i] <= 'F' ) {
				num = ( cInput[i] - 65 ) + 10;
			} else if ( 'a' <= cInput[i] && cInput[i] <= 'f' ) {
				num = ( cInput[i] - 97 ) + 10;
			} else {
				return new byte[input.length () / 2];
			}
			
			if ( 1 == i % 2 ) {
				output[i / 2] += num;
			} else {
				output[i / 2] += num * 16;
			}
		}
		
		return output;
	}
}
