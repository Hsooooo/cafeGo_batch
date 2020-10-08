package co.kr.istarbucks.xo.batch.common.autopush;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.xo.OrderAutoPushDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PushHistoryDto;
import co.kr.istarbucks.xo.batch.mgr.OrderAutoPushMgr;

public class AutoPush extends Thread {
	private static Logger logger      = Logger.getLogger("orderAutoPushSender");
	private OrderAutoPushMgr orderAutoPushMgr;
	PushHistoryDto pushDto = new PushHistoryDto();
	OrderAutoPushDto orderAutoPushDto = new OrderAutoPushDto();

	public AutoPush(){}
	
	public AutoPush(OrderAutoPushDto recvInfo) throws InterruptedException {
		orderAutoPushDto = recvInfo;
		orderAutoPushMgr = new OrderAutoPushMgr();
	}
	
	@Override
	public void run() {
		try{
			//푸시 스레드에 입력되었다는 상태값으로 변경
			orderAutoPushMgr.updateThreadInputYn(orderAutoPushDto, "Y");

			//Push 보내기전 status 값 조회
			String orderNo = orderAutoPushDto.getOrder_no();
			String status = orderAutoPushMgr.getOrderStatus(orderAutoPushDto);
			
			int pushCnt = Integer.parseInt(orderAutoPushDto.getPush_cnt());
			if(pushCnt < 3) { // 세번까지는 PUSH로 보냄
				if(status.equals("31")){
					//Push 발송
					pushDto = orderAutoPushMgr.sendPush(orderAutoPushDto);
					
					if(pushDto != null) {
						//Push API 연동방식 변경으로 사용하지 않음(2017-04-07)
						//Push 히스토리 등록
						//orderAutoPushMgr.insertPushHistory(pushDto);

						//Push 발송건 발송 횟수 업데이트(성공,실패 전부 업데이트)
						orderAutoPushMgr.updateOrderAutoPush(orderAutoPushDto);
					}
				} else {
					//stats 값이 31이 아닌경우
					logger.info("Order Status is not '31'. order_no : " + orderAutoPushDto.getOrder_no());
				}
			} else { // 세번이후 SMS로 보냄
				if(status.equals("31")) {
					String receiptOrderNo = orderAutoPushDto.getReceipt_order_no();
					//주문번호가 3 또는 P로 시작하는건만 대상임.
					if(StringUtils.equals(StringUtils.substring(orderNo, 0, 1), "3") || StringUtils.equals(StringUtils.substring(orderNo, 0, 1), "P")) {
						//SMS 발송
						orderAutoPushMgr.sendSms(orderAutoPushDto);
						
						//발송 횟수 업데이트
						orderAutoPushMgr.updateOrderAutoPush(orderAutoPushDto);
					}
				} else {
					//status 값이 31이 아닌경우
					logger.info("Order Status is not '31'. order_no : " + orderNo);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				//푸시 스레드에 빠져나왔다는 상태값으로 변경
				if(orderAutoPushDto.getOrder_no() != null) {
					orderAutoPushMgr.updateThreadInputYn(orderAutoPushDto, "N");	
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
	
}
