package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;


/**
 * ����� �ֹ� ������ ����
 * @author "yh.lee"
 *
 */
public class PreOrderDeleteDao {
	
	/**
	 * XO_PRE_ORDER# ���̺� ������ ����
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public int deletePreOrder(Map<String, Object> dbMap ) throws SQLException {
		String sqlId = "preOrder.deletePreOrder";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.delete (sqlId, dbMap);
	}
	
	/**
	 * XO_PRE_ORDER_DETAIL ���̺� ������ ����
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public int deletePreOrderDetail(Map<String, Object> dbMap ) throws SQLException {
		String sqlId = "preOrder.deletePreOrderDetail";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.delete (sqlId, dbMap);
	}
	
	/**
	 * XO_PRE_PAYMENT ���̺� ������ ����
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public int deletePrePayment(Map<String, Object> dbMap ) throws SQLException {
		String sqlId = "preOrder.deletePrePayment";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.delete (sqlId, dbMap);
	}

}
