package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.dao.EGiftItemExpirationNoticeXoDao;
import co.kr.istarbucks.xo.batch.dao.PaymentCancelXoDao;

import com.ibatis.sqlmap.client.SqlMapClient;

public class EGiftItemFailPushSenderMgr {
	
	private final EGiftItemExpirationNoticeXoDao egiftitemexpirationnoticeXodao;
	private final PaymentCancelXoDao paymentCancelXoDao;
	
	public EGiftItemFailPushSenderMgr () {
		egiftitemexpirationnoticeXodao = new EGiftItemExpirationNoticeXoDao ();
		paymentCancelXoDao = new PaymentCancelXoDao ();
	}

	/**
	 * e-Gift Item LMS ���� ���� �� ��ȸ
	 * @param xoSqlMap
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, String>> getEGiftItemLmsList(String date) throws SQLException {
		return paymentCancelXoDao.getEGiftItemLmsList(date);
	}

	/**
	 * XO_TMS_QUEUE' ���̺� PUSH ���� Insert
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public void insertXoTmsQueue(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException {
		egiftitemexpirationnoticeXodao.insertXoTmsQueue(xoSqlMap, dbMap);
		
	}

	/**
	 * 'XO_TMS_QUEUE' ���̺� Ǫ�� ��� ���� Update
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public void updateXoTmsQueue(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException {
		egiftitemexpirationnoticeXodao.updateXoTmsQueue(xoSqlMap, dbMap);
		
	}

	/**
	 * 'MMS.EM_MMT_TRAN' ���̺� LMS ���� ���п� ���� �����ڿ��� Ǫ�� �˸� �߼ۿ���(Y : �߼�) Update
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public void updateEmMmtTran(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException {
		paymentCancelXoDao.updateEmMmtTran (xoSqlMap, dbMap);
		
	}

}
