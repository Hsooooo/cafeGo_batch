package co.kr.cafego.common.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import co.kr.cafego.common.util.ReturnObject;

@ControllerAdvice
@RestController
public class DefaultControllerAdvice {
	private static final Logger logger = LoggerFactory.getLogger("INFO");
	
	@Autowired
	private ReturnObject ro;
	
	
	//인증관련 Exception 처리
//	@ExceptionHandler(ApiAuthException.class)
//	public Object apiAuthException(HttpServletRequest request, HttpServletResponse response, ApiAuthException apiAuthException){
//		logger.info("API 인증실패 : " + apiAuthException.getResultCode().replaceAll("\n|\r", "") + "/" + apiAuthException.getResultMessage().replaceAll("\n|\r", ""));
//		
//		ro.setResult(response, apiAuthException.getResultCode());
//		return null;
//	}
	
	//토큰체크 Exception 처리
//	@ExceptionHandler(TokenException.class)
//	public Object handleBusinessException(HttpServletResponse response, TokenException tokenException) {
//		logger.info("[토큰 체크] 실패 : " + tokenException.getResultCode().replaceAll("\n|\r", "") + " " + tokenException.getResultMessage().replaceAll("\n|\r", ""));
//		
//		ro.setResult(response, tokenException.getResultCode());
//		return null;
//	}
}
