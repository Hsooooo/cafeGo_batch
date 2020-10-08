package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;


/**
 * �ֹ� �޽��� ������ ����
 * @author "yh.lee"
 *
 */
public class OrderMsgDeleteDao {
	
	/**
	 * XO_ORDER_MSG_M ���̺� ������ ����
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
