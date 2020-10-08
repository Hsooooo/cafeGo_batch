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
public final class MonitorLog {
	
	private static Log logger = LogFactory.getLog("monitor");
	private final Configuration conf;
	
	private MonitorLog() {
//		super(0);
		conf = CommPropertiesConfiguration.getConfiguration("monitor.properties");
	}

	public void start() {
		
		logger.info("============MONITORLOG DAEMON START=============");
//		System.out.println("============MONITORLOG DAEMON START=============");
		while(true) {
			
			String path 		= conf.getString("monitor.path");
			String file 		= conf.getString("monitor.file");
			String appMsg		= conf.getString("monitor.app.error");
			String xoMsg		= conf.getString("monitor.xo.error");
			String posMsg		= conf.getString("monitor.pos.error");

			long timer 			= conf.getLong("monitor.timer");			
			String[] fileList 		= file.split("\\|");
			
			for (int i=0; i < fileList.length; i++) {
				if (fileList[i].indexOf("POS") > -1) {
					MonitorPosLogProc mpl = new MonitorPosLogProc(fileList[i], path, posMsg);
					Thread thread = new Thread(mpl);
					
					thread.start();					
				} else {
					MonitorLogProc mlp = new MonitorLogProc(fileList[i], path, appMsg, xoMsg);
					Thread thread = new Thread(mlp);
					
					thread.start();
				}
			}
			
			try {
				Thread.sleep(timer);
			} catch (Exception e) {
				logger.error(e,e);
			}
		}
	}
	
	public static void main(String[] args) {
		new MonitorLog().start();
	}

}
