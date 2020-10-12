package co.kr.cafego.core.config;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

@Aspect
public class LoggingAspect{
	private Logger logger = LoggerFactory.getLogger(super.getClass());
	
	@Before("@within(org.springframework.stereotype.Controller) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void log(JoinPoint joinPoint) {
		if (this.logger.isDebugEnabled()) {
			Signature signature = joinPoint.getSignature();
		    MethodSignature methodSignature = (MethodSignature)signature;
		    String[] names = methodSignature.getParameterNames();
		    Class[] types = methodSignature.getParameterTypes();
		    Object[] args = joinPoint.getArgs();
		
		    for (int i = 0; i < args.length; i++) {
		    	Class type = types[i];
		        Object arg = args[i];
		        String str = null;
		
		        if (arg != null) {
		        	if ((HttpServletRequest.class.isAssignableFrom(type)) ||(HttpServletResponse.class.isAssignableFrom(type)) || 
		        			(HttpSession.class.isAssignableFrom(type)) || (BeanUtils.isSimpleValueType(type))) {
		        		str = arg.toString();
		        	}else if ((Map.class.isAssignableFrom(type)) || (ObjectUtils.isArray(type))) {
		        		str = ArrayUtils.toString(arg);
		        	}else {
		        		str = ReflectionToStringBuilder.toString(arg, ToStringStyle.MULTI_LINE_STYLE);
		        	}
		        }
		
		        this.logger.debug("{}. {} : {} = {}", new Object[] { Integer.valueOf(i), names[i], type.getName(), str });
		    }
		}
	}
}
