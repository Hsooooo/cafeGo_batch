package co.kr.istarbucks.xo.batch.mgr.batchResult;

import java.sql.SQLException;
import java.util.Map;

import co.kr.istarbucks.xo.batch.dao.batchResult.BatchResultDao;

public class BatchResultMgr {

	transient private final BatchResultDao brDao;
	public BatchResultMgr() {
		brDao = new BatchResultDao();
	}
	
	public int getBtjbSrnm(Map<String, Object> paramMap) throws SQLException{
		return brDao.getBtjbSrnm(paramMap);
	}
	
	public void insert(Map<String, Object> paramMap) throws SQLException{
		brDao.insert(paramMap);
	}
	
	public int update(Map<String, Object> paramMap) throws SQLException{
		return brDao.update(paramMap);
	}
}
