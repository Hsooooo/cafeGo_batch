package co.kr.cafego.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

/**
 * @author sw.Lee
 * @version $Revision: 1.3 $
 */
public class MessageUtil{
	private static Map<String, Object> messageMap = new HashMap<String, Object>();
	

	public MessageUtil(){ }
	
	@SuppressWarnings("rawtypes")
	public MessageUtil(Environment env){
        for(Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
            PropertySource propertySource = (PropertySource) it.next();
			if (isMapPropertySource(propertySource)) {
				messageMap.putAll(((MapPropertySource) propertySource).getSource());
			}
        }
	}

	@SuppressWarnings("rawtypes")
	private boolean isMapPropertySource(PropertySource ps) {
		return (ps instanceof MapPropertySource) && (StringUtils.contains(ps.getName(), "messages.properties"));
	}

	public static String getMessage(String code){
		return (String)messageMap.get(code);
	}

	public static String getMessage(String code, String ... data) {
        String message = getMessage(code);
        if (data == null || data.length == 0) {
            return message;
        }

        for (int i = 0; i < data.length; i++) {
            String replacement = "\\{" + i + "\\}";
            message = message.replaceAll(replacement, StringUtils.defaultString(data[i]));
        }
        return message;
    }
}
