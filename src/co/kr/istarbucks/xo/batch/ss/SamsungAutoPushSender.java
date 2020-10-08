package co.kr.istarbucks.xo.batch.ss;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.xo.OrderAutoPushDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;

 
public class SamsungAutoPushSender {
	private static Logger logger      = Logger.getLogger("samsungAutoPushSender");
	private Configuration conf = null;
	private final int sleepTime;
	
	private final SamsungAutoPushMgr orderAutoPushMgr;
	
	public SamsungAutoPushSender() {
		sleepTime         = 1000;
		
		orderAutoPushMgr = new SamsungAutoPushMgr();
	}
	
	public static void main(String[] args) {
		if (args.length < 1) {
			logger.info("usage :: samsungAutoPushSender.sh [start | stop]");
			return;
		}
		new SamsungAutoPushSender().start();
	}
	
	@SuppressWarnings("deprecation")
	private void start(){
		logger.info("***** START samsungAutoPushSender().start()");
		
		List<OrderAutoPushDto> pushUsers = null;
		
		this.conf = CommPropertiesConfiguration.getConfiguration ("xoBatchSS.properties");
		String secondPush = this.conf.getString ("autopush.second");
		String thirdPush = this.conf.getString ("autopush.third");
		String fourthSms = this.conf.getString("autopush.fourth");
		
		Map<String, String> paramMap = new HashMap<String,String>();
		paramMap.put("second_push", secondPush);//첫푸시 후 1분
		paramMap.put("third_push", thirdPush);//첫푸시 후 3분
		paramMap.put("fourth_sms", fourthSms);//첫푸시 후 5분
		
		//스래드 생성
		int threadCount = this.conf.getInt("autopush.thread.count");//10개
		SamsungAutoPush autoPush[] = new SamsungAutoPush[threadCount];
		for(int i = 0; i < threadCount; i++){
			autoPush[i] = new SamsungAutoPush();
			autoPush[i].start();
			autoPush[i].stop();
		}
		
		while (true) {
			try {
				//Push 리스트 조회
				pushUsers = orderAutoPushMgr.getOrderAutoPush(paramMap);
				
				if (pushUsers == null || pushUsers.size() == 0) {
//					logger.info("No Push targeted.");
					Thread.sleep(sleepTime);					
					continue;
				}
				
				logger.info("Total push count : " + pushUsers.size());
				
				for (OrderAutoPushDto recvInfo : pushUsers) {
					
					for(int i = 0; i < threadCount; i++){
						//해당 스레드가 종료 상태인지 확인
						if(autoPush[i].getState().toString().equals("TERMINATED")){
							autoPush[i] = new SamsungAutoPush(recvInfo);
							autoPush[i].start();
							//다음 데이터로 넘어감.
							break;
						} else {
							if(i== threadCount - 1){
								i = -1;
							}
							//모든 스레드가 사용중이면 0번 쓰레드부터 다시 시작
							continue;
						}
					}
				}
				
				Thread.sleep(sleepTime);				
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
