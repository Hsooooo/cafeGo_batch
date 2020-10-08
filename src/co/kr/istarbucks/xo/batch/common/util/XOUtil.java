package co.kr.istarbucks.xo.batch.common.util;


import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class XOUtil {
	private static Log logger = LogFactory.getLog(XOUtil.class);
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
	 * 프로퍼티에서 String 가져오기
	 * @param name
	 * @return String
	 */
	public static String getPropertiesString ( String name ) {
		String value = "";
		if ( XOUtil.soConf == null ) {
			XOUtil.soConf = CommPropertiesConfiguration.getConfiguration ("xoBatch.properties");
		}
		try {
			value = StringUtils.defaultString (XOUtil.soConf.getString (name));
			value = StringUtils.replace(value, "^", ",");
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		return value;
	}
	
	/**
	 * 프로퍼티에서 String 가져오기
	 * @param name
	 * @param params
	 * @return
	 */
	public static String getPropertiesString(String name, Object[] params){
		String baseValue   = getPropertiesString(name);
		String returnValue = MessageFormat.format(baseValue, params);
		return returnValue; 
	}
}
