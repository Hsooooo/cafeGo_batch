package co.kr.cafego.common.aspect;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.kr.cafego.common.util.ParamUtils;

@Component
@Aspect
public class CommonLogAspect {
	private static final Logger LOGGER  = LoggerFactory.getLogger("INFO");
	private static final Logger RLOGGER = LoggerFactory.getLogger("RESULT");
	
	@Around("execution(* co.kr.cafego..*Controller.*(..))")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
		Object obj = null;
		HttpServletRequest request = getHttpServletRequest(joinPoint);
		
		StopWatch watch   = new StopWatch();
		String className  = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		String fullPkgName = joinPoint.getTarget().getClass().getPackage().getName();
		String[] fullPkgArr = StringUtils.split(fullPkgName,'.');
		String pkgName = fullPkgArr[fullPkgArr.length-1];
		
		try{
			watch.start();

			this.logPreWrite(request, pkgName);
			LOGGER.info("[{}] {}", className+":"+methodName, "########## START ##########");
			
			obj = joinPoint.proceed();
		}finally{
			LOGGER.info("[{}] {}", className+":"+methodName, "########## END ##########");
			
			watch.stop();
			this.logCompletedWrite(request, watch, pkgName);
		}
		
		return obj;
	}
	
	
	@Around("execution(* co.kr.cafego..*Service.*(..))")
	public Object servecieAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object obj = null;

		StopWatch watch   = new StopWatch();
		String className  = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		
		try{
			watch.start();
			LOGGER.info("[{}] {}", className, "----- START ["+methodName+"] -----");
			

			obj = joinPoint.proceed();
		}finally{
			watch.stop();
			LOGGER.info("[{}] {}", className, "----- END ["+methodName+"](" + watch.toString() + ") -----");

		}
		
		return obj;
	}
	
	
	@AfterReturning(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) && execution(public Object *(..))", returning = "jsonView")
	public void responseMessage(JoinPoint joinPoint, Object jsonView) {
		boolean logWrite = true;
		StringBuffer log = new StringBuffer();
		
		String userId 	   = "";
		String servletPath = "";
		
		//결과코드/메시지
		String resultCode 	 = "";
		String resultMessage = "";
		HttpServletRequest request   = null;
		HttpServletResponse response = null;
		
		
		if(logWrite){
			try{
				//request, response 획득
				request  = this.getHttpServletRequest(joinPoint);
				response = this.getHttpServletResponse(joinPoint);
				
				//결과 메시지 획득
				if(response != null){
					resultCode 	  = response.getHeader("resultCode");
					resultMessage = URLDecoder.decode(response.getHeader("resultMessage"), "UTF-8");
				}
				
				userId = (String) request.getAttribute("userId");
				servletPath  = StringUtils.defaultIfEmpty(request.getServletPath(), "");
				
				log.append("[").append(userId).append("][").append(servletPath).append("](").append(ParamUtils.getIp(request)).append(")").append("\n");
				log.append("[").append(userId).append("][HEADER] ").append(this.writeHeaderLog(request)).append("\n");
				log.append("[").append(userId).append("][PARAMS] ").append(request.getAttribute("bodyMap")).append("\n");
				
				log.append("[").append(userId).append("][RESULT_HEADER] ").append(resultCode).append(" : ").append(resultMessage).append("\n");
				log.append("[").append(userId).append("][RESULT_BODY] ").append(StringUtils.defaultString((String)request.getAttribute("jsonDataStr"))).append("\n");
			}catch(Exception ex){
				RLOGGER.info("[" + userId + "][" + servletPath + "] Exception -> " + ex.getMessage());
			}finally{
				RLOGGER.info(log.toString());
			}
		}
	}
	
	private void logPreWrite(HttpServletRequest request, String pkgName) {
		LOGGER.info("Connected: [{}][{}][{}|{}][{}]", 
				request.getRemoteAddr(),
				(String) request.getAttribute("userId"),
				request.getScheme(), 
				getMethod(request),				
				request.getRequestURI()
		);
		
	}
	private void logCompletedWrite(HttpServletRequest request, StopWatch watch, String pkgName) {
		LOGGER.info("Completed: [{}][{}][{}|{}][{}]({})", 
				request.getRemoteAddr(),
				(String) request.getAttribute("userId"),
				request.getScheme(), 
				getMethod(request),				
				request.getRequestURI(),
				watch.toString()
		);
	}
	
	private String getMethod(HttpServletRequest request) {
		String method = request.getMethod();
		String paramValue = request.getParameter("_method");
		
		if ("POST".equals(method) && org.springframework.util.StringUtils.hasLength(paramValue)) {
			method = paramValue.toUpperCase(Locale.KOREA);
		}
		
		return method;
	}
	
	/**
	 * Header 정보 조회
	 * @param request
	 */
	public String writeHeaderLog(HttpServletRequest request){
		Enumeration<String> headerNames = request.getHeaderNames();
		ArrayList<String> headerList    = new ArrayList<String>();
		while(headerNames.hasMoreElements()){
			String name  = (String)headerNames.nextElement();
			String value = request.getHeader(name);
			headerList.add(name + ":" + value);
		}
		
		return StringUtils.join(headerList, ",");
	}
	
	/**
	 * 파라미터에서 response 객체 획득
	 * @param joinPoint
	 * @return
	 */
	public HttpServletResponse getHttpServletResponse(JoinPoint joinPoint){
		HttpServletResponse response = null;
		Object[] args = joinPoint.getArgs();
		for(Object param : args){
			if(param instanceof HttpServletResponse){
				response = (HttpServletResponse)param;
				break;
			}
		}
		
		return response;
	}
	
	/**
	 * 파라미터에서 request 객체 획득
	 * @param joinPoint
	 * @return
	 */
	public HttpServletRequest getHttpServletRequest(JoinPoint joinPoint){
		HttpServletRequest request = null;
		Object[] args = joinPoint.getArgs();
		for(Object obj : args){
			if(obj instanceof HttpServletRequest){
				request = (HttpServletRequest)obj;
				break;
			}
		}
		
		return request;
	}
}
