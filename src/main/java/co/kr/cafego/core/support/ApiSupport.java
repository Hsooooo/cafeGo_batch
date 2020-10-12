package co.kr.cafego.core.support;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.ResultCode;


/**
 * API 공통 클래스
 *
 * @author sw.Lee
 * @version $Revision: 1.3 $
 */
public class ApiSupport {
	//Logger 설정
	
	/*
	 * logger.info() : info 로그(info 파일에 write)
	 * logger.error() : error 로그(info / error 파일에 write)
	 */
	protected final Logger logger = LoggerFactory.getLogger("INFO");
	
	/**
	 * Critical 로그
	 */
	protected final Logger criticalLogger = LoggerFactory.getLogger("CRITICAL");
	
	/**
	 * POS 로그
	 */
	protected final Logger posLogger = LoggerFactory.getLogger("POS");
	
	/*
	 * 재고조회 로그
	 */
	protected final Logger stockLogger = LoggerFactory.getLogger("STOCK");
	
	@SuppressWarnings("unchecked")
	protected Map<String, Object> fromBodyMap(HttpServletRequest req) {
		Map<String, Object> params = (Map<String, Object>) req.getAttribute("bodyMap");
		if (MapUtils.isEmpty(params)) {
			throw new ApiException(ResultCode.INVALID_PARAMETER,
					"요청 파라미터의 값이 잘못되었습니다.{NO REQUEST PARAMETERS}");
		}
		return params;
	}

	/**
	 * Object로 받은 값이 있는지 확인
	 * @param s
	 * @return
	 */
	public static boolean isEmptyOfObject(Object s) { 
		if (s == null) { 
			return true; 
		}
		
		if ((s instanceof String) && (((String)s).trim().length() == 0)) {
			return true; 
		} 
		
		if (s instanceof Map) { 
			return ((Map<?, ?>)s).isEmpty(); 
		} 
		
		if (s instanceof List) { 
			return ((List<?>)s).isEmpty(); 
		} 
		
		if (s instanceof Object[]) {
			return (((Object[])s).length == 0); 
		}
		
		return false; 
	}
}
