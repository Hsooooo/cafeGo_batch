package co.kr.cafego.core.config;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.support.MultipartFilter;

public class ApplicationInitializer extends AbstractApplicationInitializer {

	private final Logger logger = LoggerFactory.getLogger("INFO");
	
	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		
		logger.info("ACTIVE_PROFILE = [{}]", getActiveProfile().replaceAll("\n|\r", ""));
		SystemEnviroment.setActiveProfile(getActiveProfile());
		servletContext.setAttribute("newLine", "\n");
	}
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{ApplicationConfig.class};
	}
	
	@Override
    protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        
        //HiddenHttpMethodFilter 보다 먼�? ?��?��?��?��.
        MultipartFilter multipartFilter = new MultipartFilter();
        multipartFilter.setMultipartResolverBeanName("multipartResolver");
        
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        
        // ?��로스 ?��?��?�� ?��?��
        //XssFilter xssFilter = new XssFilter();
        
       return new Filter[] {
    		   characterEncodingFilter,
    		   //xssFilter,
    		   multipartFilter, 
    		   hiddenHttpMethodFilter 
        };
    }
}
