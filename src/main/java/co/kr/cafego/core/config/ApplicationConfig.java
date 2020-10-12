package co.kr.cafego.core.config;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.number.NumberFormatter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.config.Task;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import co.kr.cafego.BasePackageMarker;
import co.kr.cafego.common.interceptor.ApiAuthInterceptor;
import co.kr.cafego.common.interceptor.ReqJsonMappingInterceptor;
import co.kr.cafego.common.util.MessageUtil;
import co.kr.cafego.common.util.ReturnObject;
import co.kr.cafego.core.support.CustomDateFormatter;
import co.kr.cafego.core.support.XssConverter;


@Configuration
@PropertySource({
	"classpath:${spring.profiles.active:dev}/default.properties",
	"classpath:messages.properties",
	"classpath:${spring.profiles.active:dev}/xoDefault.properties"
})

@ImportResource({
	"classpath:etc-config.xml",
})
@Import({
	TraceConfig.class
})
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableScheduling
@ComponentScan(basePackageClasses = {BasePackageMarker.class}, nameGenerator = CafegoBeanNameGenerator.class)
public class ApplicationConfig extends AbstractApplicationConfig {
	 
	private final Logger logger = LoggerFactory.getLogger("INFO");
	
	// properties ?��?��
	@Inject
	private Environment env;
	
	@Inject
	private EnvironmentWrapper envw;
	
//	@Inject
//	private AuthMapper authMapper;
//	
//	@Inject
//	private HpOauthMapper hpOauthMapper;
	
	public ApplicationConfig() {
		logger.info("{} is initializing.", ApplicationConfig.class);
	}

	@PostConstruct
	public void postConstruct() {
		SystemEnviroment.setActiveProfile(env.getActiveProfiles()[0]);
		//DH keypair Error fix
//		Security.addProvider(new BouncyCastleProvider());
	}
	/**************************************** Bean definition ?��?�� ****************************************/
	@Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
	
	@Bean(name="multipartResolver")
	public MultipartResolver multipartResolver() {//throws IOException {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		return multipartResolver;
	}
	
	//json 처리?�� view
	@Bean
	public MappingJackson2JsonView jsonView(){
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setModelKey("app");
		return jsonView;
	}
	
	//jsp 처리?�� view
	@Bean
	public InternalResourceViewResolver viewResolver(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;		
	}
	
	@Bean
	public ReturnObject returnObject(){
		return new ReturnObject();
	}
	
	//Message.properties 처리?�� util
	@Bean(name="messageUtil")
	public MessageUtil messageUtil(){
		MessageUtil messageUtil =  new MessageUtil(env);
		return messageUtil;
	}
	/**************************************** Bean definition ?�� ****************************************/
	
	/****************************** WebMvcConfigurerAdapter overriding ?��?�� ******************************/
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver();
		pageableArgumentResolver.setFallbackPageable(new PageRequest(1, 10));
		pageableArgumentResolver.setMaxPageSize(100);
		argumentResolvers.add(pageableArgumentResolver);
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload_files/").addResourceLocations("/upload_files/**");
        registry.addResourceHandler("/css/").addResourceLocations("/css/**");
        registry.addResourceHandler("/images/").addResourceLocations("/images/**");
        registry.addResourceHandler("/js/").addResourceLocations("/js/**");
        registry.addResourceHandler("/resources/").addResourceLocations("/resources/**");
    }

	/**
	 * Interceptor
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ApiAuthInterceptor(env));			//api ?��증�??�� 체크
		registry.addInterceptor(new ReqJsonMappingInterceptor(env));	//json object mapping
//		registry.addInterceptor(new TokenKeyCheckInterceptor(env, authMapper, hpOauthMapper));
	}
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new CustomDateFormatter("yyyy-MM-dd"));
		registry.addFormatter(new NumberFormatter());
		registry.addConverter(new XssConverter());
	}
	
	/****************************** WebMvcConfigurerAdapter overriding ?�� ******************************/
	
	//LocalValidatorFactoryBean?? ?��?��?��?��?�� ?��?�� JSR-303 구현체�? �??��?�� ?��?��브러리�?? �??��?��?�� Validator�? ?��?��?���? �??��?��주는 ?��?��?�� ?��?��?��?��.
//	@Bean
//	public LocalValidatorFactoryBean localValidatorFactoryBean() {
//		return new LocalValidatorFactoryBean();
//	}
//	@Bean
//	public MethodValidationPostProcessor methodValidationPostProcessor(){
//		return new MethodValidationPostProcessor();
//	}
//	@Override
//	protected boolean getJsonPrettyPrint() {
//		return envw.getProperty("json.prettyPrint", Boolean.class, Boolean.TRUE);
//	}
}
