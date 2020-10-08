package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.dto.xo.OrderAutoPushDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.PushHistoryDto;
import co.kr.istarbucks.xo.batch.dao.OrderAutoPushDao;

public class OrderAutoPushMgr {
	private final OrderAutoPushDao orderAutoPushDao;
	
	public OrderAutoPushMgr() {
		orderAutoPushDao  = new OrderAutoPushDao();
	}

	public List<OrderAutoPushDto> getOrderAutoPush(Map<String, String> paramMap) throws SQLException {
		return orderAutoPushDao.getOrderAutoPush(paramMap);
	}

	public String getOrderStatus(OrderAutoPushDto recvInfo) throws SQLException {
		return orderAutoPushDao.getOrderStatus(recvInfo);
		
	}

	public PushHistoryDto sendPush(OrderAutoPushDto recvInfo) {
		return orderAutoPushDao.sendPush(recvInfo);
	}

	public void updateOrderAutoPush(OrderAutoPushDto recvInfo) throws Exception {
		orderAutoPushDao.updateOrderAutoPush(recvInfo);
	}

	public void insertPushHistory(PushHistoryDto pushDto) throws Exception {
		orderAutoPushDao.insertPushHistory(pushDto);
	}

	public void updateThreadInputYn(OrderAutoPushDto recvInfo, String input_yn) throws Exception{
		orderAutoPushDao.updateThreadInputYn(recvInfo, input_yn);
	}
	
	/**
	 * SMS �߼�
	 * @param orderNo
	 * @param receiptOrderNo
	 * @return
	 * @throws Exception
	 */
	public void sendSms(OrderAutoPushDto recvInfo) throws Exception{
		orderAutoPushDao.sendSms(recvInfo);
	}
}
