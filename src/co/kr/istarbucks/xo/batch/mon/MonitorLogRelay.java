package co.kr.istarbucks.xo.batch.mon;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;

/**
 * 로그 감시 데몬
 * @author JUNO
 *
 */
public final class MonitorLogRelay {
	
	private static Log logger = LogFactory.getLog("monitor");
	private final Configuration conf;
	
	private MonitorLogRelay() {
//		super(0);
		conf = CommPropertiesConfiguration.getConfiguration("monitor.properties");
	}

	public void start() {
		
		logger.info("============MONITORLOG DAEMON START=============");
//		System.out.println("============MONITORLOG DAEMON START=============");
		while(true) {
			
			String path 		= conf.getString("monitor.path");
			String file 		= conf.getString("monitor.file");
			String posMsg		= conf.getString("monitor.pos.error");

			long timer 			= conf.getLong("monitor.timer");			
			String[] fileList 		= file.split("\\|");
			
			for (int i=0; i < fileList.length; i++) {
				MonitorPosLogProc mpl = new MonitorPosLogProc(fileList[i], path, posMsg);
				Thread thread = new Thread(mpl);
				
				thread.start();					
			}
			
			try {
				Thread.sleep(timer);
			} catch (Exception e) {
				logger.error(e,e);
			}
		}
	}
	
	public static void main(String[] args) {
		new MonitorLogRelay().start();
	}

}
