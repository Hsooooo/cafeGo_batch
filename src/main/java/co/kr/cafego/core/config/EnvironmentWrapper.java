package co.kr.cafego.core.config;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

public class EnvironmentWrapper {
	private Environment env;
	
	public EnvironmentWrapper(Environment env) {
		this.env = env;
	}
	
	public String getProperty(String key, String defaultValue) {
		return StringUtils.defaultIfBlank(this.env.getProperty(key, defaultValue), defaultValue);
	}
	
	public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
		try {
			return ObjectUtils.defaultIfNull(this.env.getProperty(key, targetType, defaultValue), defaultValue);
		}catch(Exception e) {
			
		}
		return defaultValue;
	}
}
