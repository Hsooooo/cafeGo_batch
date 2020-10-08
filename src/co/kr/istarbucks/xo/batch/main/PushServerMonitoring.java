package co.kr.istarbucks.xo.batch.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import common.daemon.Daemon;

import co.kr.istarbucks.xo.batch.common.dto.xo.PushServerMonitoringDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.ServerConfiguration;
import co.kr.istarbucks.xo.batch.mgr.PushServerMonitoringMgr;
import co.kr.istarbucks.xo.batch.common.autopush.AutoPushMonitoring;

public class PushServerMonitoring {
	private static Logger logger      = Logger.getLogger("PushServerMonitoring");
//	private Configuration conf = null;
//	private int sleepTime;
	
	private final PushServerMonitoringMgr PushServerMonitoringMgr;
	
	public PushServerMonitoring() {		
		PushServerMonitoringMgr = new PushServerMonitoringMgr();
	}
	
	public static void main(String[] args) {
		/*if (args.length < 1) {
			System.out.println("usage :: PushServerMonitoring.sh [start | stop]");
			return;
		}*/
		new PushServerMonitoring().start();
	}
	
	@SuppressWarnings("deprecation")
	private void start(){
		logger.info("***** START PushServerMonitoring().start()");
		
		PushServerMonitoringDto dto = new PushServerMonitoringDto();
		List<PushServerMonitoringDto> pushUser = new ArrayList();
		List pushUsers = null;
//		AutoPushMonitoring autoPushMonitor = new AutoPushMonitoring(dto);
		AutoPushMonitoring autoPushMonitor = new AutoPushMonitoring();

		try {
			//Push ????? ???
			pushUsers = PushServerMonitoringMgr.getRepinfo();
			
			
			if (pushUsers == null || pushUsers.size() == 0) {
				logger.info("***** err *****" );
				logger.info("No Push targeted.");
			}else{
				logger.info("Total push count : " + pushUsers.size());
				
				for(int i=0; i < pushUsers.size() ; i++){
					String sUserId = pushUsers.get(i).toString();
					dto.setUser_id(sUserId);

					autoPushMonitor.run(dto);
					logger.info("****************" + dto.getUser_id() );

				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
