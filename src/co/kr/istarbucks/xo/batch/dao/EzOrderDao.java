package co.kr.istarbucks.xo.batch.dao;

import java.util.Map;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

public class EzOrderDao {
	/**
	 * DB ���� ��¥ ��������
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public Map<String, String> getToday () throws Exception {
		String sqlId = "ezorder.getToday";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return (Map<String, String>) sqlMap.queryForObject (sqlId);
	}
	/**
	 * �������� ����� 
	 * @return
	 */
	public int ezOrder() throws Exception {
		String sqlId = "ezorder.ezOrderInfo";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}

}
