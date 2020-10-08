package co.kr.istarbucks.xo.batch.ss;


import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;

import co.kr.istarbucks.xo.batch.common.util.ByteUtil;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;


public class XOUtilSS {

//	private static Log logger = LogFactory.getLog("pos.handler");
	protected static Configuration soConf = null; // soInfo.properties
	protected static String checkTime = "";
	public static final String charset = "UTF-8";
	
	public static byte[] setSize ( byte[] data ) throws UnsupportedEncodingException {
		int dataLength = 0;
		byte[] dataLengthByte;
		
		dataLength = data.length + 6;
		dataLengthByte = StringUtils.leftPad (String.valueOf (dataLength), 6, "0").getBytes (charset);
		
		byte[] result;
		result = ByteUtil.addArray (dataLengthByte, data);
		
		return result;
		
	}
	
	/**
	 * ������Ƽ���� String ��������
	 * @param name
	 * @return String
	 */
	public static String getPropertiesString ( String name ) {
		String value = "";
		if ( XOUtilSS.soConf == null ) {
			XOUtilSS.soConf = CommPropertiesConfiguration.getConfiguration ("xoBatchSS.properties");
		}
		value = StringUtils.defaultString (XOUtilSS.soConf.getString (name));
		value = StringUtils.replace(value, "^", ",");
		
		return value;
	}
	
	/**
	 * ������Ƽ���� String ��������
	 * @param name
	 * @param params
	 * @return
	 */
	public static String getPropertiesString(String name, Object[] params){
		String baseValue   = getPropertiesString(name);
		String returnValue = MessageFormat.format(baseValue, params);
		return returnValue; 
	}
	
	/**
	 * ������Ƽ���� �� ��������
	 * @param name
	 * @return int
	 */
	public static int getPropertiesInt ( String name, int dfValue ) {
		int value = dfValue;
		if ( XOUtilSS.soConf == null ) {
			XOUtilSS.soConf = CommPropertiesConfiguration.getConfiguration ("xoBatchSS.properties");
		}
		try {
			if ( XOUtilSS.soConf.getInt (name) > 0 ) {
				value = XOUtilSS.soConf.getInt (name);
			}
		} catch ( Exception e ) {
			value = dfValue;
		}
		return value;
	}
	
	/**
	 * �� ����Ʈ �迭�� �ϳ��� ��ħ
	 * @param data
	 * @param s
	 * @return
	 */
	public static byte[] addArray(byte[] data, byte[] s) {
		if (data == null) return s;

		byte[] tmp = new byte[data.length + s.length];

		System.arraycopy(data, 0, tmp, 0, data.length);
		System.arraycopy(s, 0, tmp, data.length, s.length);

		return tmp;
	}
}
