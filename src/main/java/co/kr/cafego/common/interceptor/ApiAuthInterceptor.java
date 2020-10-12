package co.kr.cafego.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import co.kr.cafego.common.exception.ApiException;
import co.kr.cafego.common.util.ResultCode;
import co.kr.cafego.core.config.SystemEnviroment;
import co.kr.cafego.core.support.MemberCheck;


public class ApiAuthInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger("INFO");
	
	private Environment env;
	public ApiAuthInterceptor() {}
	public ApiAuthInterceptor(Environment environment){
		this.env = environment;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		//======================================================================================
		// 2. Request Method 체크 => EXT 인터페이스는 모두 POST, GET, PATCH, DELETE 메소드만 사용
		//======================================================================================

		if(isRequiredMethod(request)) {
			throw new ApiException(ResultCode.INVALID_METHOD, "전문요청 메소드는 POST, GET, PATCH, DELETE 방식만 지원합니다.");
		}

		//======================================================================================
		// 3. Protocol 체크 => APP 인터페이스는 모두 HTTPS 프로토콜을 사용
		//======================================================================================
//		LOGGER.info("Api Operation Mode ::: " + SystemEnviroment.getActiveProfile().replaceAll("\n|\r", ""));
//		if(isRealModeAndNotSecured(request)) {
//			throw new ApiAuthException(ResultCode.INVALID_PROTOCOL, "전문요청 프로토콜은 HTTPS만 지원합니다.");
//		}
		
		MemberCheck memberCheck = ((HandlerMethod) handler).getMethodAnnotation(MemberCheck.class);
		
		return super.preHandle(request, response, handler);
	}

	private boolean isRealModeAndNotSecured(HttpServletRequest request) {
		return SystemEnviroment.getActiveProfile().equals("prod") && !request.isSecure();
	}

	private boolean isRequiredMethod(HttpServletRequest request) {
		return !"POST".equalsIgnoreCase(request.getMethod()) && !"PATCH".equalsIgnoreCase(request.getMethod()) 
				&& !"GET".equalsIgnoreCase(request.getMethod()) && !"DELETE".equalsIgnoreCase(request.getMethod());
	}
	public Environment getEnv() { return env; }
	public void setEnv(Environment env) { this.env = env; }
	
}
