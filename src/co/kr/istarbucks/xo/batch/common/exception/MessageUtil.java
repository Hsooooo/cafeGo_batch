package co.kr.istarbucks.xo.batch.common.exception;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MessageUtil {

	public static Map<String, Object> messageMap = new HashMap<String, Object>();
	
	public MessageUtil(){ }

	public static String getMessage(String code){
		messageMap.put("0000", "{SUCCESS}��ġ�Ϸ�Ǿ����ϴ�.");
		messageMap.put("0001", "�Ķ������ ���� �߸��Ǿ����ϴ�.");
		messageMap.put("0002", "SQL ERROR");
		messageMap.put("0003", "{NO DATA}ó���� �����Ͱ� �����ϴ�.");
		messageMap.put("0004", "Util Error");
		messageMap.put("0005", "�ܺμ��� ���� ����");
		messageMap.put("0006", "wrong path");
		messageMap.put("0009", "ó�� ����� �ƴմϴ�.");
		return (String)messageMap.get(code);
	}
	
	public static String getMessage(String code, String[] data) {
		String message = getMessage(code);
		if (data != null) {
		    for (int idx = 0; idx < data.length ; idx++) {
		    	message = message.replaceAll("\\{" + idx + "}", StringUtils.defaultString(data[idx]));
			}
		}
		return message;
	}
	
	public static String encode(String code, String[] data, String enc) {
		String message = getMessage(code, data);
		try {
			return URLEncoder.encode(message, enc);
		}
		catch (UnsupportedEncodingException e) {
			return message;
		}
	}
}
