package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;
import java.util.Map;

import co.kr.istarbucks.xo.batch.dao.PreOrderDeleteDao;

/**
 * 선계산 주문 데이터 삭제
 * @author "yh.lee"
 */
public class PreOrderDeleteMgr {
	
	private final PreOrderDeleteDao preOrderDeleteDao;
	
	/**
	 * @return the preOrderDeleteDao
	 */
	public PreOrderDeleteDao getPreOrderDeleteDao() {
		return preOrderDeleteDao;
	}

	public PreOrderDeleteMgr () {
		this.preOrderDeleteDao = new PreOrderDeleteDao ();
	}
	
	/**
	 * XO_PRE_ORDER# 테이블 데이터 삭제
	 * @return
	 * @throws SQLException 
	 * @throws Exception
	 */
	public int deletePreOrder (Map<String, Object> map) throws SQLException{
		return this.preOrderDeleteDao.deletePreOrder(map);
	}
	
	/**
	 * XO_PRE_ORDER_DETAIL 테이블 데이터 삭제
	 * @return
	 * @throws Exception
	 */
	public int deletePreOrderDetail (Map<String, Object> map) throws SQLException {
		return this.preOrderDeleteDao.deletePreOrderDetail(map);
	}
	
	/**
	 * XO_PRE_PAYMENT 테이블 데이터 삭제
	 * @return
	 * @throws Exception
	 */
	public int deletePrePayment (Map<String, Object> map) throws SQLException {
		return this.preOrderDeleteDao.deletePrePayment(map);
	}
	
}
