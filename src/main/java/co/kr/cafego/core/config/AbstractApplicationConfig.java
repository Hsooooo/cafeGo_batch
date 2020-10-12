package co.kr.cafego.core.config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import co.kr.cafego.core.view.FileDownloadView;
import co.kr.cafego.core.view.IE89ContentNegotiatingViewResolver;

public abstract class AbstractApplicationConfig extends WebMvcConfigurerAdapter{
	
	@Bean
	public ResourceBundleMessageSource messageSource(){
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames(new String[] { "messages" });

		return messageSource;
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();

		return multipartResolver;
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setViewClass(JstlView.class);
		internalResourceViewResolver.setPrefix("/WEB-INF/jsp/");
		internalResourceViewResolver.setSuffix(".jsp");
		internalResourceViewResolver.setRequestContextAttribute("rc");

		List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();

		viewResolvers.add(internalResourceViewResolver);

	    MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
	    jsonView.setPrettyPrint(getJsonPrettyPrint());

	    List<View> defaultViews = new ArrayList<View>();
	    defaultViews.add(jsonView);
	
	    ContentNegotiatingViewResolver viewResolver = new IE89ContentNegotiatingViewResolver();
	
	    viewResolver.setViewResolvers(viewResolvers);
	    viewResolver.setDefaultViews(defaultViews);
	
	    return viewResolver;
	  }
	
//	  @Bean
//	  public LocalValidatorFactoryBean validator() {
//		  return new LocalValidatorFactoryBean();
//	  }
	
	  @Bean
	  public FileDownloadView fileDownloadView() {
		  return new FileDownloadView();
	  }
	
	  @Bean
	  public LoggingAspect loggingAspect() {
		  return new LoggingAspect();
	  }
	  @Bean
	  @Inject
	  public EnvironmentWrapper environmentWrapper(Environment env) {
		  return new EnvironmentWrapper(env);
	  }
	
	  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		  configurer.enable();
	  }
	
	  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		  MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		  jsonConverter.setPrettyPrint(getJsonPrettyPrint());
	
		  converters.add(jsonConverter);
	  }
	
	  protected boolean getJsonPrettyPrint() {
		  return false;
	  }
}
