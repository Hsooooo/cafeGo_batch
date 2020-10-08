package co.kr.istarbucks.xo.batch.common.autopush;

import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.xo.PushServerMonitoringDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PushHistoryDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PushServerMonitoringDto;
import co.kr.istarbucks.xo.batch.mgr.PushServerMonitoringMgr;

public class AutoPushMonitoring {
	private static Logger logger      = Logger.getLogger("PushServerMonitoring");
	private final PushServerMonitoringMgr PushServerMonitoringMgr;
//	private PushServerMonitoringDto PushServerMonitoringDto;
	
//	public AutoPushMonitoring(PushServerMonitoringDto recvInfo){
	public AutoPushMonitoring(){
		PushServerMonitoringMgr = new PushServerMonitoringMgr();
	}
	
	
	public void run(PushServerMonitoringDto recvInfo){
		try{
			logger.info("here!!");
			PushServerMonitoringDto recvInfo2 = new PushServerMonitoringDto();
			recvInfo2 = recvInfo;
			
			PushServerMonitoringMgr.sendPush(recvInfo);

		} catch (Exception e){
			logger.error(e);
		} finally {
			logger.info("PushServerMonitoring END");
		}
	}
	
}
