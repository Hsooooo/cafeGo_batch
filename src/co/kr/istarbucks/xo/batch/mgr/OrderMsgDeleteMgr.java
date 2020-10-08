package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;
import java.util.Map;

import co.kr.istarbucks.xo.batch.dao.OrderMsgDeleteDao;

/**
 * 주문 메시지 데이터 삭제
 * @author "yh.lee"
 */
public class OrderMsgDeleteMgr {
	
	private final OrderMsgDeleteDao orderMsgDeleteDao;
	
	/**
	 * @return the preOrderDeleteDao
	 */
	public OrderMsgDeleteDao getOrderMsgDeleteDao() {
		return orderMsgDeleteDao;
	}

	public OrderMsgDeleteMgr () {
		this.orderMsgDeleteDao = new OrderMsgDeleteDao ();
	}
	
	/**
	 * XO_ORDER_MSG_M 테이블 데이터 삭제
	 * @return
	 * @throws SQLException 
	 * @throws Exception
	 */
	public int deleteOrderMsg (Map<String, Object> map) throws SQLException{
		return this.orderMsgDeleteDao.deleteOrderMsg(map);
	}
	
}
