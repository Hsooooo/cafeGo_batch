package co.kr.cafego.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceConfig{
	  private final Logger logger = LoggerFactory.getLogger(super.getClass());
	
	  public TraceConfig() {
		  this.logger.info("{} is initializing.", TraceConfig.class);
	  }

	  @Bean
	  public CustomizableTraceInterceptor repositoryTraceInterceptor() {
		    CustomizableTraceInterceptor interceptor = new CustomizableTraceInterceptor();
		
		    interceptor.setEnterMessage("Entering $[methodName]($[arguments]).");
		
		    interceptor.setExitMessage("Leaving $[methodName](..), took $[invocationTime]ms.");
		
		    return interceptor;
	  }

	  @Bean
	  public Advisor classTraceAdvisor() {
		  return new DefaultPointcutAdvisor(new AnnotationMatchingPointcut(Trace.class, true),repositoryTraceInterceptor());
	  }

	  @Bean
	  public Advisor methodTraceAdvisor() {
		  return new DefaultPointcutAdvisor(new AnnotationMatchingPointcut(null, Trace.class),repositoryTraceInterceptor());
	  }
}
