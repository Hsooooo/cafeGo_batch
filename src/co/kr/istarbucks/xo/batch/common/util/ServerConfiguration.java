package co.kr.istarbucks.xo.batch.common.util;

import common.configuration.CommonConfiguration;

public final class ServerConfiguration extends CommonConfiguration {
	private static ServerConfiguration instance;
	
	private ServerConfiguration() {		
		super("xoBatch.properties");		
	}
	
	public static ServerConfiguration getInstance() {
		synchronized (ServerConfiguration.class) {
			if (instance == null) {
				instance = new ServerConfiguration();			
			}
			return instance;
		}
	}

}
