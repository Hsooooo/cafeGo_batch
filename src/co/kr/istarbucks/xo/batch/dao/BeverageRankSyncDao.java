package co.kr.istarbucks.xo.batch.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

public class BeverageRankSyncDao {
	private static Log logger = LogFactory.getLog("beverageRankSynchronized");
	
	public int getRankEdwCount() throws Exception {
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
		return (Integer) sqlMap.queryForObject("rank.getRankEdwIfCnt");
	}

	public void updateRankEdw() throws Exception {
		SqlMapClient sqlMap = null;
		try{
			sqlMap = IBatisSqlConfig.getXoSqlMapInstance();
			sqlMap.startTransaction();
			
			int i = sqlMap.delete("rank.deleteRankEdw");
			logger.info("XO_SELL_RANK_EDW Delete success : " + i);
			sqlMap.insert("rank.insertRankEdw");			
			logger.info("XO_SELL_RANK_EDW Insert success");
			
			sqlMap.commitTransaction();
		} catch (Exception e) {
			logger.info("ERROR! :",e);
			throw e;
		} finally {
			sqlMap.endTransaction();
		}
	}
}
