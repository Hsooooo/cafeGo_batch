package co.kr.istarbucks.xo.batch.ss;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.xo.OrderAutoPushDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PushHistoryDto;

public class SamsungAutoPush extends Thread {
	private final Logger logger      = Logger.getLogger("samsungAutoPushSender");
	private SamsungAutoPushMgr orderAutoPushMgr;
	PushHistoryDto pushDto = new PushHistoryDto();
	OrderAutoPushDto orderAutoPushDto = new OrderAutoPushDto();

	public SamsungAutoPush(){}
	
	public SamsungAutoPush(OrderAutoPushDto recvInfo) throws InterruptedException {
		orderAutoPushDto = recvInfo;
		orderAutoPushMgr = new SamsungAutoPushMgr();
	}
	
	@Override
	public void run() {
		try{
			//Ǫ�� �����忡 �ԷµǾ��ٴ� ���°����� ����
			orderAutoPushMgr.updateThreadInputYn(orderAutoPushDto, "Y");

			//Push �������� status �� ��ȸ
			String orderNo = orderAutoPushDto.getOrder_no();
			String status = orderAutoPushMgr.getOrderStatus(orderAutoPushDto);
			
			int pushCnt = Integer.parseInt(orderAutoPushDto.getPush_cnt());
			if(pushCnt < 3) { // ���������� PUSH�� ����
				if(status.equals("31")){
					//Push �߼�
					pushDto = orderAutoPushMgr.sendPush(orderAutoPushDto);
					
					if(pushDto != null) {
						//Push API ������� �������� ������� ����(2017-04-07)
						//Push �����丮 ���
						//orderAutoPushMgr.insertPushHistory(pushDto);

						//Push �߼۰� �߼� Ƚ�� ������Ʈ(����,���� ���� ������Ʈ)
						orderAutoPushMgr.updateOrderAutoPush(orderAutoPushDto);
					}
				} else {
					//stats ���� 31�� �ƴѰ��
					logger.info("Order Status is not '31'. order_no : " + orderAutoPushDto.getOrder_no());
				}
			} else { // �������� SMS�� ���� : OPEN_API(�Ｚ��ī��)�� ��� SMS��� PUSH �߼�
				if(status.equals("31")) {
					//�ֹ���ȣ�� 3 �Ǵ� P�� �����ϴ°Ǹ� �����.
					if(StringUtils.equals(StringUtils.substring(orderNo, 0, 1), "3") || StringUtils.equals(StringUtils.substring(orderNo, 0, 1), "P")) {
						//Push �߼�
						orderAutoPushMgr.sendPush(orderAutoPushDto);
						
						
						//�߼� Ƚ�� ������Ʈ
						orderAutoPushMgr.updateOrderAutoPush(orderAutoPushDto);
					}
				} else {
					//status ���� 31�� �ƴѰ��
					logger.info("Order Status is not '31'. order_no : " + orderNo);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			//e.printStackTrace();
		} finally {
			try {
				//Ǫ�� �����忡 �������Դٴ� ���°����� ����
				if(orderAutoPushDto.getOrder_no() != null) {
					orderAutoPushMgr.updateThreadInputYn(orderAutoPushDto, "N");	
				}
			} catch (Exception e) {
				logger.error(e);
				//e.printStackTrace();
			}
		}
	}
	
}
