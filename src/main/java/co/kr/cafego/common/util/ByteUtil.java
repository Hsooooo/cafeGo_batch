package co.kr.cafego.common.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtil {	
	
	public static byte[] hexToByteArray(String hex) {
	    if (hex == null || hex.length() == 0) {
	        return null;
	    }
	 
	    byte[] ba = new byte[hex.length() / 2];
	    for (int i = 0; i < ba.length; i++) {
	        ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
	    }
	    return ba;
	}
	
	public static String byteArryToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder(ba.length * 2);
		String hexNumber;
		for (int x=0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
			sb.append(hexNumber.substring(hexNumber.length()-2));
		}
		return sb.toString();
	}
	
	/*public static byte[] intToBytes(int l) {
		byte[] returnBytes = new byte[(Integer.SIZE / 8)];

		for (int i=0; i < INT_SIZE; i++)
			returnBytes[i] = (byte)((l >>> (8 * ((Integer.SIZE / 8) - i - 1))) & 0xff);

		return returnBytes;
	}*/
	
	public static byte[] intToBytes(int k) {
		
		ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE / 8);
		buff.putInt(k);
		buff.order(ByteOrder.BIG_ENDIAN);
		byte[] result = buff.array();
		
		return result;
	}

	public static int bytesToInt(byte[] bytes) {
		int returnInt = 0;

		for(int i=0; i < (Integer.SIZE / 8); i++)
			returnInt += (int)unsign(bytes[i]) << ((8 * ((Integer.SIZE / 8) - i -1)));

		return returnInt;
	}
	
	public static long unsign(byte arg) {
		long value = (long)arg;
		return value < 0 ? 0x100 + value : value;
	}	
	
	public static byte[] addArray(byte[] data, byte[] s) {
		if(data == null) return s;

		byte[] tmp	=	new byte[data.length + s.length];

		System.arraycopy(data, 0,  tmp, 0, data.length);
		System.arraycopy(s, 0,  tmp, data.length, s.length);

		return tmp;
	}

	public static byte[] addArray(byte[] data, byte[] s, int start, int length) {
		if(data == null) {
			if(length - start == s.length) return s;

			byte[] tmp	=	new byte[length - start];
			System.arraycopy(s, start, tmp, 0, length);
			return tmp;
		}

		byte[] tmp	=	new byte[data.length + (length - start)];

		System.arraycopy(data, 0, tmp, 0, data.length);
		System.arraycopy(s, start, tmp, data.length, length);

		return tmp;
	}
}
