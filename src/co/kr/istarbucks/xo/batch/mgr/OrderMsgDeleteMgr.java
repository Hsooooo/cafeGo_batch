package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;
import java.util.Map;

import co.kr.istarbucks.xo.batch.dao.OrderMsgDeleteDao;

/**
 * �ֹ� �޽��� ������ ����
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
	 * XO_ORDER_MSG_M ���̺� ������ ����
	 * @return
	 * @throws SQLException 
	 * @throws Exception
	 */
	public int deleteOrderMsg (Map<String, Object> map) throws SQLException{
		return this.orderMsgDeleteDao.deleteOrderMsg(map);
	}
	
}
