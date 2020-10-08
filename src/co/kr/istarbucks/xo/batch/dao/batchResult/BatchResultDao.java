package co.kr.istarbucks.xo.batch.dao.batchResult;

import java.sql.SQLException;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;
/**
 * XO_BATCH 실행 모니터링 결과 INSERT/UPDATE
 */
public class BatchResultDao {

	/**
	 * 배치작업순번 조회
	 */
	public int getBtjbSrnm(Map<String, Object> paramMap) throws SQLException{
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		return (Integer) sqlMap.queryForObject("batchResult.getBtjbSrnm", paramMap);
	}
	
	/**
	 * 배치 실행 시 모니터링 insert
	 */
	public void insert(Map<String, Object> paramMap) throws SQLException{
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		sqlMap.insert("batchResult.insert", paramMap);
	}
	
	/**
	 * 배치 모니터링 결과 update
	 */
	public int update(Map<String, Object> paramMap) throws SQLException{
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		return (Integer) sqlMap.update("batchResult.update", paramMap);
	}
	
	
}
