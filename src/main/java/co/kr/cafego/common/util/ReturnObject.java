package co.kr.cafego.common.util;
 
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.View;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ReturnObject {
	private static final Logger LOGGER = LoggerFactory.getLogger("INFO");
	
	@Autowired
	private View jsonView;
	
	@Autowired
	private Environment env;
	
	
	/** 
	 * head의 Accept 값 획득 후 JSON 리턴
	 * 디폴트는 JSON을 사용
	 * @param request
	 * @param headers
	 * @param model
	 * @param obj
	 * @return
	 */
	public Object getObject(HttpServletRequest request, HttpHeaders headers, Model model, Object obj){
		try{
			String jsonDataStr = "";
			
			if(obj != null){
				ObjectWriter ow = new ObjectMapper().writer();
				//jsonDataStr = "{ \"app\": " + ow.writeValueAsString(obj) + " }";
				jsonDataStr = ow.writeValueAsString(obj);
			}
			
			request.setAttribute("jsonDataStr", jsonDataStr);
		}catch(JsonProcessingException jpe){
			LOGGER.error("JsonProcessingException >>> " + jpe.getMessage().replaceAll("\n|\r", ""));
			request.setAttribute("jsonDataStr", "");
		}
		if(obj== null) {
			return obj;
		} 
		return obj;
		
	}
	
	public Object getObject(HttpServletRequest request, HttpHeaders headers, Model model, String rootName, Object obj){
		try{
			ObjectMapper om = new ObjectMapper();
			om.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
			ObjectWriter ow = om.writer().withRootName(rootName);
			
			String jsonDataStr = ow.writeValueAsString(obj);
			request.setAttribute("jsonDataStr", jsonDataStr);
		}catch(JsonProcessingException jpe){
			LOGGER.error("JsonProcessingException >>> " + jpe.getMessage().replaceAll("\n|\r", ""));
			request.setAttribute("jsonDataStr", "");
		}
		model.addAttribute("app", obj);
		return jsonView;
	}	
	
	/**
	 * 결과 메세지 리턴
	 * @param gubun
	 * @param code
	 * @return
	 */
	public String getMessage(String code, String gubun){
		//return env.getProperty(code, "");
		return MessageUtil.getMessage(code);
	}
	
	/**
	 * 결과 코드/메세지 설정
	 * @param response
	 * @param result_code
	 * @param result_message
	 */
	public void setResult(HttpServletResponse response, String resultCode){
		try{
			setResultCode(response, resultCode);
			setResultMessage(response, getMessage(resultCode, ""));
		}catch(Exception e){
//			LOGGER.info("[결과 코드/메세지 설정] 오류 : " + e.getMessage().replaceAll("\n|\r", ""));
			LOGGER.info("Exception -> " , e);
			response.setHeader(env.getProperty("header.result.code", "resultCode").replaceAll("\n|\r", ""), "0099");
			
			try{
				response.setHeader(env.getProperty("header.result.message", "resultMessage").replaceAll("\n|\r", ""), URLEncoder.encode("시스템 오류", "UTF-8"));
			}catch(UnsupportedEncodingException e1){
				response.setHeader(env.getProperty("header.result.message", "resultMessage").replaceAll("\n|\r", ""), "");
			}
		}
	}
	
	/**
	 * 결과 코드/메세지 설정 <br>
	 * 		[0099:기타에러]인 경우 파라미터의 resultMessage를 사용 <br> 
	 * 		그외의 경우는 resultCode의 매핑된 메시지 사용
	 * @param response
	 * @param resultCode
	 * @param resultMessage
	 */
	public void setResult(HttpServletResponse response, String resultCode, String resultMessage){
		try{
			setResultCode(response, resultCode);
			
			//기타에러가 아닌  경우
			if(!StringUtils.equals(resultCode, "0099")){
				setResultMessage(response, getMessage(resultCode, ""));
			
			//기타에러인 경우
			}else{
				if(StringUtils.isNotBlank(resultMessage)){
					setResultMessage(response, resultMessage);
				}else{
					setResultMessage(response, getMessage(resultCode, ""));
				}
			}
		}catch(Exception e){
			LOGGER.info("[결과 코드/메세지 설정] 오류 : " + e.getMessage().replaceAll("\n|\r", ""));
			response.setHeader(env.getProperty("header.result.code", "resultCode").replaceAll("\n|\r", ""), "0099");
			
			try{
				response.setHeader(env.getProperty("header.result.message", "resultMessage").replaceAll("\n|\r", ""), URLEncoder.encode("시스템 오류", "UTF-8"));
			}catch(UnsupportedEncodingException e1){
				response.setHeader(env.getProperty("header.result.message", "resultMessage").replaceAll("\n|\r", ""), "");
			}
		}
	}
	
	/**
	 * 결과 코드/메세지 설정 V2
	 * resultData 값이 있으면 해당 값을 변환하여 반환, 없다면 resultCode값을 사용
	 * @param response
	 * @param resultCode
	 * @param resultData
	 */
	public void setResultV2(HttpServletResponse response, String resultCode, String[] resultData){
		String resultMessage = "";
		
		try{
			if(resultData != null && resultData.length > 0){
				String resultMsg = MessageUtil.getMessage(resultCode);
				StringBuffer buf = new StringBuffer();
				int cnt = 0;
				for(String str : resultData){
					buf.delete(0, buf.length()).append("\\{").append(cnt).append("\\}");
					resultMsg = resultMsg.replaceAll(buf.toString(), StringUtils.defaultString(str));
					cnt++;
				}
				resultMessage = resultMsg;
			}else{
				resultMessage = getMessage(resultCode, "");
			}
			
			setResultCode(response, resultCode);
			setResultMessage(response, resultMessage);
		}catch(Exception e){
			LOGGER.info("[결과 코드/메세지 설정] 오류 : " + e.getMessage().replaceAll("\n|\r", ""));
			response.setHeader(env.getProperty("header.result.code", "resultCode").replaceAll("\n|\r", ""), "0099");
			
			try{
				response.setHeader(env.getProperty("header.result.message", "resultMessage").replaceAll("\n|\r", ""), URLEncoder.encode("시스템 오류", "UTF-8"));
			}catch(UnsupportedEncodingException e1){
				response.setHeader(env.getProperty("header.result.message", "resultMessage").replaceAll("\n|\r", ""), "");
			}
		}
	}

	
	/**
	 * 결과 코드 설정
	 * @param response
	 * @param code
	 */
	public void setResultCode(HttpServletResponse response, String resultCode){
		response.setHeader(env.getProperty("header.result.code", "resultCode").replaceAll("\n|\r", ""), resultCode.replaceAll("\n|\r", ""));
	}
	
	/**
	 * 결과 메세지 설정
	 * @param response
	 * @param result_message
	 */
	public void setResultMessage(HttpServletResponse response, String resultMessage){
		try{
			LOGGER.info("############ TEST ###" + env.getProperty("header.result.message", "resultMessage"));
			LOGGER.info("############ TEST ###" + resultMessage);
			
			response.setHeader(env.getProperty("header.result.message", "resultMessage"), URLEncoder.encode(resultMessage, "UTF-8"));
		}catch(UnsupportedEncodingException e){
			LOGGER.error(e.getMessage().replaceAll("\n|\r", ""));
		}
	}
	
	public View getJsonView() { return jsonView; }
	public void setJsonView(View jsonView) { this.jsonView = jsonView; }
	public Environment getEnv() { return env; }
	public void setEnv(Environment env) { this.env = env; }
}
