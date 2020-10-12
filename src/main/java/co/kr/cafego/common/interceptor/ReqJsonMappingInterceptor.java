package co.kr.cafego.common.interceptor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.lang.StringUtils;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import co.kr.cafego.common.util.ResultCode;
import co.kr.cafego.core.support.ReqParameterValidator;
import net.minidev.json.JSONObject;

@Controller
public class ReqJsonMappingInterceptor extends HandlerInterceptorAdapter {
	private final Logger logger = LoggerFactory.getLogger("INFO");
	
	private Environment env;
	
	public ReqJsonMappingInterceptor() {
		
	}
	
	public ReqJsonMappingInterceptor(Environment environment){
		this.env = environment;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Map<String, Object> bodyMap = null;
		StringBuffer sb             = new StringBuffer();
		BufferedReader br           = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
		PrintWriter out				= null;
		JSONObject json 			= new JSONObject();
		try {
			String line = null;
			String raw = null;
			//GET 메소드일 경우 Request Body 미존재,, 로깅 및 매핑하지않음
			if(!StringUtils.equals(request.getMethod(), "GET")) {
				//======================================================================================
				// 1. reading pay load[request body] 
				//======================================================================================
				line = br.readLine();
				
				while (StringUtils.isNotEmpty(line)){
					sb.append(line);
					line = br.readLine();
				}
				
				logger.info("==========================================================================");
				logger.info("Request jsonBody >>> " + sb.toString().replaceAll("\n|\r", ""));
				raw = this.XSScheck(sb.toString());
				// Request Body부분이 존재하는 경우만 매핑
				//======================================================================================
				// 2. population bodyMap
				//======================================================================================
				bodyMap = new Gson().fromJson(raw, Map.class);
				
				
				logger.info("Request bodyMap >>> " + bodyMap.toString().replaceAll("\n|\r", ""));
				
				logger.info("==========================================================================");
				
				//======================================================================================
				// 3. Validation Parameter-Type 
				//======================================================================================
				String requestPath = request.getRequestURI().replaceFirst(request.getContextPath(), "");
				ReqParameterValidator validator = new ReqParameterValidator(requestPath,env);
				
				
				validator.check(bodyMap);
			}
		} catch (JsonSyntaxException jse) {
//			ro.setResult(response, ResultCode.JSON_DATA_ERROR);
			json.put("errCode",URLEncoder.encode(ResultCode.JSON_DATA_ERROR, "utf-8") );
			json.put("errMessage", Encode.forHtml( env.getProperty(ResultCode.JSON_DATA_ERROR)));
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			response.setStatus(500);
			
			out = response.getWriter();
			out.print(json);
			out.flush();
			
			logger.error("JsonSyntaxException ", jse);
			return false;
		} catch (Exception e) {
//			ro.setResult(response, ResultCode.SERVER_ERROR);
			json.put("errCode",URLEncoder.encode(ResultCode.SERVER_ERROR, "utf-8") );
			json.put("errMessage", Encode.forHtml( env.getProperty(ResultCode.SERVER_ERROR)));
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			response.setStatus(500);
			
			out = response.getWriter();
			out.print(json);
			out.flush();
			
			logger.error("Exception ", e);
			return false;
		} finally {
			if (out != null) {
				out.close();
			}
			if (br != null) {
				br.close();
			}
		}
		
		request.setAttribute("bodyMap", bodyMap);
		
		return super.preHandle(request, response, handler);
	}
	
	private String XSScheck(String raw) {
		String tmpRaw = raw;
		
		tmpRaw = tmpRaw.replaceAll("(?i)<script[^>]*>[\\w|\\t|\\r|\\W]*</script>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<html[^>]*>[\\w|\\t|\\r|\\W]*</html>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<head[^>]*>[\\w|\\t|\\r|\\W]*</head>" , "");		
		tmpRaw = tmpRaw.replaceAll("(?i)<body[^>]*>[\\w|\\t|\\r|\\W]*</body>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<embed[^>]*>[\\w|\\t|\\r|\\W]*</embed>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<iframe[^>]*>[\\w|\\t|\\r|\\W]*</iframe>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<div[^>]*>[\\w|\\t|\\r|\\W]*</div>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<meta[^>]*>[\\w|\\t|\\r|\\W]*</meta>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<object[^>]*>[\\w|\\t|\\r|\\W]*</object>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<applet[^>]*>[\\w|\\t|\\r|\\W]*</applet>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<frameset[^>]*>[\\w|\\t|\\r|\\W]*</frameset>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<img[^>]*>[\\w|\\t|\\r|\\W]*</img>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<input[^>]*>[\\w|\\t|\\r|\\W]*</input>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<table[^>]*>[\\w|\\t|\\r|\\W]*</table>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<style[^>]*>[\\w|\\t|\\r|\\W]*</style>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<link[^>]*>[\\w|\\t|\\r|\\W]*</link>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)<base[^>]*>[\\w|\\t|\\r|\\W]*</base>" , "");
		tmpRaw = tmpRaw.replaceAll("(?i)alert([^>]*>[\\w|\\t|\\r|\\W]*)" , "");
		tmpRaw = tmpRaw.replaceAll("&", "");
		tmpRaw = tmpRaw.replaceAll("<", "");
		tmpRaw = tmpRaw.replaceAll(">", "");
		tmpRaw = tmpRaw.replaceAll("'", "");
		
		return tmpRaw;
	}
}
