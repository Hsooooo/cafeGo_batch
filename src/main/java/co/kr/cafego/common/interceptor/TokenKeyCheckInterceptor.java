package co.kr.cafego.common.interceptor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Controller
public class TokenKeyCheckInterceptor extends HandlerInterceptorAdapter{
	
	private final Logger logger = LoggerFactory.getLogger("INFO");
	
//	private AuthMapper authMapper;
//	
//	private HpOauthMapper hpOauthMapper;
	
	private Environment env;
	
	public TokenKeyCheckInterceptor() {}
	
//	public TokenKeyCheckInterceptor(Environment environment, AuthMapper authMapper, HpOauthMapper hpOauthMapper){
//		this.env = environment;
////		this.authMapper = authMapper;
////		this.hpOauthMapper = hpOauthMapper;
//	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		SimpleDateFormat REQ_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
//		UrlPathHelper urlPathHelper = new UrlPathHelper(); 
//		String originalURL = urlPathHelper.getOriginatingRequestUri(request);
//		
//		String[] stockUrlChkArr = originalURL.split("/");
//		String stockUrlChk = stockUrlChkArr[stockUrlChkArr.length -1];
//		String tmp = "";
//		if(StringUtils.contains(stockUrlChk, "?")) {
//			String[] tmpArr = stockUrlChk.split("?");
//			tmp = tmpArr[0];
//		}else {
//			tmp = stockUrlChk;
//		}
//		if(StringUtils.equals(tmp, STOCK_CHK)) {
//			sLogger.info("[HEADER] : " + this.writeHeaderLog(request).replaceAll("\n|\r", ""));
//			sLogger.info("OriginalURL ==>" + originalURL.replaceAll("\n|\r", ""));
//		}else {
//			logger.info("[HEADER] : " + this.writeHeaderLog(request).replaceAll("\n|\r", ""));
//			logger.info("OriginalURL ==>" + originalURL.replaceAll("\n|\r", ""));
//		}
//		 
//		// 토큰체크 어노테이션 획득
//		NaverTokenCheck naverTokenCheck = ((HandlerMethod) handler).getMethodAnnotation(NaverTokenCheck.class);
//		
//		if(naverTokenCheck != null){
//
//			try {
//				String nmmServiceId = StringUtils.defaultString(request.getHeader("nmmServiceId"));
//				String nmmCheck     = StringUtils.defaultString(request.getHeader("nmmCheck"));
//				String model        = StringUtils.defaultString(request.getHeader("model"));
//				
//				if (StringUtils.isBlank(nmmServiceId) || StringUtils.isBlank(nmmCheck)) {
//					throw new TokenException(ResultCode.NO_TOKEN, "토큰키가 존재하지 않습니다.");
//				}
//				
//				if (nmmServiceId.length() != 7) {
//					throw new TokenException(ResultCode.NO_TOKEN, "유효한 요청값이 아닙니다.");
//				}
//				
//				if (model.length() != 2) {
//					throw new TokenException(ResultCode.NO_TOKEN, "유효한 요청값이 아닙니다.");
//				}
//				
//				if (nmmCheck.length() > 2048) {
//					throw new TokenException(ResultCode.NO_TOKEN, "유효한 요청값이 아닙니다.");
//				}
//	
//				String secretKey = hpOauthMapper.getSecretKey(nmmServiceId);
//				if (StringUtils.isBlank(secretKey)) {
//					throw new TokenException(ResultCode.NO_TOKEN, "토큰키의 Secret Key를 찾을 수 없습니다.");
//				}
//	
//				String plainCheckKey = "";
//				try {
//					NonMemberEncryptUtils encryptUtils = new NonMemberEncryptUtils(secretKey, env);
//					plainCheckKey = encryptUtils.decrypt(nmmCheck);
//				} catch (IllegalStateException e) {
//					throw new TokenException(ResultCode.NO_TOKEN, "토큰키가 유효하지 않습니다. " + e.getMessage());
//				}
//				
//				String requestTime    = plainCheckKey.substring(0, 14);
//				String affiliateCode2 = plainCheckKey.substring(14);
//				
//				if(StringUtils.isBlank(requestTime)) {
//					throw new TokenException(ResultCode.NO_TOKEN, "토큰키가 유효하지 않습니다.");
//				}
//				
//				if(!StringUtils.isNumeric(requestTime)) {
//					throw new TokenException(ResultCode.NO_TOKEN, "토큰키가 유효하지 않습니다.");
//				}
//				Date expiredTime  = DateUtils.addMinutes(REQ_DATE_FORMAT.parse(requestTime), 10);
//				Date currentTime  = new Date();
//				
//				if (currentTime.compareTo(expiredTime) > 0) {
//					throw new TokenException(ResultCode.NO_SIRN_TOKEN, "토큰키가 만료되었습니다.");
//				}
//				
//				if (! nmmServiceId.equals(affiliateCode2)) {
//					throw new TokenException(ResultCode.NO_TOKEN, "토큰키의 제휴사코드가 유효하지 않습니다.");
//				}
//				
//				Map<String, Object> affiliateCompany = authMapper.getaffiliateCompany(affiliateCode2);
//				
//				if (affiliateCompany == null) {
//					throw new TokenException(ResultCode.NO_TOKEN, "비회원 서비스가 인가된 제휴사가 아닙니다.");
//				}
//	
//				String affiliateCode  = (String) affiliateCompany.get("SRVIATHRTGROUPNO");
//				String nmmIdPrfxValue = (String) affiliateCompany.get("NMMIDPRFXVALUE");
//				String rrmmdd = DateFormatUtils.format(currentTime, "yyyyMMdd").substring(2);
//				String instantNonMemberUserId = "";
//	
//				if (naverTokenCheck.isCreateUserId()) {
//					String seq = authMapper.getInstantNonMemberUserSeq();
//					instantNonMemberUserId = "@" + nmmIdPrfxValue + rrmmdd + seq;
//				} else {
//					instantNonMemberUserId = "@" + nmmIdPrfxValue + rrmmdd + "0000000000";
//				}
////				
////				// add authorization info
////				if(isSsAppCard(affiliateCode)) {
////						ModelType.setAttributeToRequest(request, ModelType.SS_APPCARD);		// 삼성 앱카드
////				} else {
////	                ModelType.setAttributeToRequest(request, ModelType.AI_UNKNOWN);
////				}
//				 
//				request.setAttribute("userId",   instantNonMemberUserId);
//				request.setAttribute("userName", DataCode.NAVER_USER_NAME);
//				request.setAttribute("model",    model);
//				request.setAttribute("serviceId", nmmServiceId);
//				
//				if(StringUtils.equals(tmp, STOCK_CHK)){
//					sLogger.info("[토큰 체크] 정상 : " + ParamUtils.getIp(request).replaceAll("\n|\r", "") + " " + affiliateCode.replaceAll("\n|\r", "") + " " + instantNonMemberUserId.replaceAll("\n|\r", "") + " " + expiredTime);
//				}else {
//					logger.info("[토큰 체크] 정상 : " + ParamUtils.getIp(request).replaceAll("\n|\r", "") + " " + affiliateCode.replaceAll("\n|\r", "") + " " + instantNonMemberUserId.replaceAll("\n|\r", "") + " " + expiredTime);
//				}
//				
//			} catch(TokenException te){
//				logger.error(StringUtils.defaultIfBlank(te.getMessage(), "TokenKeyError").replaceAll("\n|\r", ""),te);
//				// NO_TOKEN, NO_SIRN_TOKEN 에러가 난 경우에는 Status 값을 401으로 변경하여 전송
//				// 표준이 토큰키가 없거나 만료인 경우 400 또는 401로 처리 한다고함
//				if(!ResultCode.INVALID_URL.equals(te.getResultCode())){
//					response.setStatus(401);
//				}
//				return false;
//	  		} catch(DataAccessException daEx) {
//	  			logger.error(daEx.getMessage().replaceAll("\n|\r", ""), daEx);
//	  			response.setStatus(500);
//	  			
//	  			return false;
//			} catch(Exception e) {
//				logger.error("Exception",e);
//				response.setStatus(500);
//				
//				return false;
//			}
////			logger.info("[토큰 체크] 정상 : " + remote_addr + " " + token + " " + user_id + " " + token_expire_date);
//		}
//		
		logger.info("response ==>" + response.getStatus());
	
		return super.preHandle(request, response, handler);
	}
	
	/**
	 * Header 정보 조회
	 * @param request
	 */
	private String writeHeaderLog(HttpServletRequest request){
		Enumeration<String> headerNames = request.getHeaderNames();
		List<String> headerList = new ArrayList<String>();
		while (headerNames.hasMoreElements()){
			String name  = headerNames.nextElement();
			String value = request.getHeader(name);
			headerList.add(name + ":" + value);
		}
		
		return StringUtils.join(headerList, ",");
	}
	
	public Environment getEnv() { return env; }
	public void setEnv(Environment env) { this.env = env; }
	public Logger getLogger() { return logger; }
	
	
}
