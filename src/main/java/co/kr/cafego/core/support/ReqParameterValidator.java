package co.kr.cafego.core.support;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.ResultCode;

public class ReqParameterValidator {

	private static final Logger logger = LoggerFactory.getLogger("INFO");
	
	private static final int ARRAY_TYPE        = 0;
	private static final int INT_TYPE          = 1;
	private static final int CELLPHONE_NO_TYPE = 2;
	private static final int DATE_TYPE         = 3; // YYYYMMDD
	private static final int FLOAT_TYPE        = 4;
	private static final int STRING_TYPE       = 5;
	
	private static final int SPLIT_LIMIT       = 5;
	
	private static final String FIELD_SEPERATOR = ";";
	private static final String RULE_SEPERATOR  = "\\|"; 
	
	private final Environment env;

	// 결과 값
	private final String requestPath;
	private String requestKey  = "";
	private String resultCode  = "";
	private String resultMsg   = "";
	
	/**
	 * Constructor 
	 * @param requestPath
	 */
	public ReqParameterValidator(String requestPath, Environment env) {
		this.requestPath = requestPath;
		this.env = env;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	/**
	 * 
	 * @param requestURL
	 * @param paramMap
	 * @param isParamCheck
	 * @return
	 */
	public boolean check(Map<String, Object> paramMap) {

		try {
			//======================================================================================
			// 1. request URI Validation 
			//======================================================================================
			
			boolean isParamCheck = true;
			
			requestKey = "{" + requestPath + "}";
			logger.info("requestKey: {}",requestKey.replaceAll("\n|\r", ""));

			String requestValue = env.getProperty(requestKey + ".ENABLE","N");
			
			logger.info("requestValue: {}",requestValue.replaceAll("\n|\r", ""));
			
			if(StringUtils.isBlank(requestValue)) {
				throw new ApiException(ResultCode.INVALID_URL);
			}
			
			if ("N".equals(requestValue)) {
				isParamCheck = false;
			}
			//======================================================================================
			// 2. request parameters Validation 
			//======================================================================================
			if (isParamCheck) {
				String rules = env.getProperty(requestKey + ".PARAMETER");
			}
			
		} catch (ApiException e) {
			this.resultCode = e.getResultCode();
			this.resultMsg  = e.getResultMessage();
			return false;
		} catch (Exception e) {
			this.resultCode = ResultCode.SERVER_ERROR;
			this.resultMsg  = e.getMessage();
			logger.error(e.getMessage().replaceAll("\n|\r", ""), e);
			return false;
		}

		return true;
	}

}