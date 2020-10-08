package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;


/**
 * 주문 메시지 데이터 삭제
 * @author "yh.lee"
 *
 */
public class OrderMsgDeleteDao {
	
	/**
	 * XO_ORDER_MSG_M 테이블 데이터 삭제
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public int deleteOrderMsg(Map<String, Object> dbMap ) throws SQLException {
		String sqlId = "orderMsg.deleteOrderMsgBat";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.delete (sqlId, dbMap);
	}

}
