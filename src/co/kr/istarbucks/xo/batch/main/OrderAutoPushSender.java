package co.kr.istarbucks.xo.batch.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import common.daemon.Daemon;

import co.kr.istarbucks.xo.batch.common.dto.xo.OrderAutoPushDto;
import co.kr.istarbucks.xo.batch.common.util.CommPropertiesConfiguration;
import co.kr.istarbucks.xo.batch.common.util.ServerConfiguration;
import co.kr.istarbucks.xo.batch.mgr.OrderAutoPushMgr;
import co.kr.istarbucks.xo.batch.common.autopush.AutoPush;

public class OrderAutoPushSender extends Daemon {
	private static Logger logger      = Logger.getLogger("orderAutoPushSender");
	private Configuration conf = null;
	private final int sleepTime;
	
	private final OrderAutoPushMgr orderAutoPushMgr;
	
	public OrderAutoPushSender() {
		super(ServerConfiguration.getInstance().getConfiguration().getInt("watchdog.port"));
		sleepTime         = 1000;
		
		orderAutoPushMgr = new OrderAutoPushMgr();
	}
	
	public static void main(String[] args) {
		if (args.length < 1) {
			logger.info("usage :: orderAutoPushSender.sh [start | stop]");
			return;
		}
		new OrderAutoPushSender().start();
	}
	
	@SuppressWarnings("deprecation")
	private void start(){
		logger.info("***** START autoPushSender().start()");
		
		List<OrderAutoPushDto> pushUsers = null;
		
		this.conf = CommPropertiesConfiguration.getConfiguration ("xoBatch.properties");
		String secondPush = this.conf.getString ("autopush.second");
		String thirdPush = this.conf.getString ("autopush.third");
		String fourthSms = this.conf.getString("autopush.fourth");
		
		Map<String, String> paramMap = new HashMap<String,String>();
		paramMap.put("second_push", secondPush);//ùǪ�� �� 1��
		paramMap.put("third_push", thirdPush);//ùǪ�� �� 3��
		paramMap.put("fourth_sms", fourthSms);//ùǪ�� �� 5��
		
		//������ ����
		int threadCount = this.conf.getInt("autopush.thread.count");//10��
		AutoPush autoPush[] = new AutoPush[threadCount];
		for(int i = 0; i < threadCount; i++){
			autoPush[i] = new AutoPush();
			autoPush[i].start();
			autoPush[i].stop();
		}
		
		while (true) {
			try {
				//Push ����Ʈ ��ȸ
				pushUsers = orderAutoPushMgr.getOrderAutoPush(paramMap);
				
				if (pushUsers == null || pushUsers.size() == 0) {
//					logger.info("No Push targeted.");
					Thread.sleep(sleepTime);					
					continue;
				}
				
				logger.info("Total push count : " + pushUsers.size());
				
				for (OrderAutoPushDto recvInfo : pushUsers) {
					
					for(int i = 0; i < threadCount; i++){
						//�ش� �����尡 ���� �������� Ȯ��
						if(autoPush[i].getState().toString().equals("TERMINATED")){
							autoPush[i] = new AutoPush(recvInfo);
							autoPush[i].start();
							//���� �����ͷ� �Ѿ.
							break;
						} else {
							if(i== threadCount - 1){
								i = -1;
							}
							//��� �����尡 ������̸� 0�� ��������� �ٽ� ����
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
