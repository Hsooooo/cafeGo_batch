package co.kr.cafego.core.config;

import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

public abstract class AbstractApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{
	
	private String activeProfile;

	public void onStartup(ServletContext servletContext) throws ServletException{
	  
		this.activeProfile = servletContext.getInitParameter("spring.profiles.active");

		Assert.notNull(this.activeProfile);
		try {
			LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
			context.reset();

			URL url = ResourceUtils.getURL("classpath:" + this.activeProfile + "/logback.xml");

			JoranConfigurator jc = new JoranConfigurator();
			jc.setContext(context);
			jc.doConfigure(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onStartup(servletContext);

		servletContext.addListener(RequestContextListener.class);
	}

	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { EmptyServletConfig.class };
	}

	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	protected String getActiveProfile() {
		return this.activeProfile;
	}
}