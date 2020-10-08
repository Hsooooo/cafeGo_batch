package co.kr.istarbucks.xo.batch.common.util;

import org.apache.commons.configuration.AbstractFileConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommPropertiesConfiguration {
	private static Log log = LogFactory.getLog (CommPropertiesConfiguration.class);
	
	public static Configuration getConfiguration ( String fileName ) {
		Configuration config = null;
		try {
			config = new PropertiesConfiguration (fileName);
			( (AbstractFileConfiguration) config ).setReloadingStrategy (new FileChangedReloadingStrategy ());
		} catch ( ConfigurationException e ) {
			log.error(e.getMessage(), e);
		}
		return config;
	}
}
