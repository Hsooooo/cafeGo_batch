package co.kr.cafego.common.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class ParamUtils {
	
	/**
	 * Json 파싱 결과로 전달된 Double 형 변수를 Integer로 변환
	 * @param parameters
	 * @return
	 */
	public static Map<String, Object> doubleToInt(Map<String, Object> parameters){
		Map<String, Object> returnObj = new HashMap<String, Object>();
		if(parameters != null) {
			returnObj.putAll(parameters);
			for(String key : parameters.keySet()) {
				Object obj = parameters.get(key);
				if(obj instanceof Double) {
					double doubleValue = (Double)obj;
					int intValue       = (int)doubleValue;
					returnObj.put(key, intValue);
				}
			}
		}
		return returnObj;
	}
	
	/**
	 * 접속 IP 획득
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = StringUtils.defaultString(request.getHeader("X-FORWARDED-FOR")); 
		 if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			 ip = StringUtils.defaultString(request.getHeader("Proxy-Client-IP"));
	     }
	     if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
	         ip = StringUtils.defaultString(request.getHeader("WL-Proxy-Client-IP"));  // 웹로직
	     }
	     if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
	         ip = StringUtils.defaultString(request.getRemoteAddr());
	     }
	     
	     if (StringUtils.defaultString(ip).length() > 1024) {
	    	 ip = "";
	     }
	     return ip;
	}
}
