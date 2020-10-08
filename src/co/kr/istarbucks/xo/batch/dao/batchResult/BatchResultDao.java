package co.kr.istarbucks.xo.batch.dao.batchResult;

import java.sql.SQLException;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
/**
 * XO_BATCH ���� ����͸� ��� INSERT/UPDATE
 */
public class BatchResultDao {

	/**
	 * ��ġ�۾����� ��ȸ
	 */
	public int getBtjbSrnm(Map<String, Object> paramMap) throws SQLException{
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		return (Integer) sqlMap.queryForObject("batchResult.getBtjbSrnm", paramMap);
	}
	
	/**
	 * ��ġ ���� �� ����͸� insert
	 */
	public void insert(Map<String, Object> paramMap) throws SQLException{
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		sqlMap.insert("batchResult.insert", paramMap);
	}
	
	/**
	 * ��ġ ����͸� ��� update
	 */
	public int update(Map<String, Object> paramMap) throws SQLException{
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		return (Integer) sqlMap.update("batchResult.update", paramMap);
	}
	
	
}
