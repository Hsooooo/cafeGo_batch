package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;


/**
 * 선계산 주문 데이터 삭제
 * @author "yh.lee"
 *
 */
public class PreOrderDeleteDao {
	
	/**
	 * XO_PRE_ORDER# 테이블 데이터 삭제
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
	 * XO_PRE_ORDER_DETAIL 테이블 데이터 삭제
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
	 * XO_PRE_PAYMENT 테이블 데이터 삭제
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
