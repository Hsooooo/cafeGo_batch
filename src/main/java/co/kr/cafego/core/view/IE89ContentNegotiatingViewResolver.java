package co.kr.cafego.core.view;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class IE89ContentNegotiatingViewResolver extends ContentNegotiatingViewResolver{
	private MappingJackson2JsonView ie89JsonView = new MappingJackson2JsonView();

	public IE89ContentNegotiatingViewResolver() {
		this.ie89JsonView.setContentType("text/plain");
	}

	public View resolveViewName(String viewName, Locale locale) throws Exception {
		View view = super.resolveViewName(viewName, locale);

		if (("".equals(viewName)) && ((view instanceof JstlView))) {
			RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = ((ServletRequestAttributes)attrs).getRequest();
      
			if (request.getHeader("User-Agent").contains("MSIE")) {
				return this.ie89JsonView;
			}
		}

		return view;
	}
}
