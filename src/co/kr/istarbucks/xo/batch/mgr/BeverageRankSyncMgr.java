package co.kr.istarbucks.xo.batch.mgr;

import co.kr.istarbucks.xo.batch.dao.BeverageRankSyncDao;

public class BeverageRankSyncMgr {
	
	private final BeverageRankSyncDao beverageRankSyncDao;
	
	public BeverageRankSyncMgr() {
		this.beverageRankSyncDao = new BeverageRankSyncDao();
	}
	
	public int getRankEdwCount() throws Exception {
		return beverageRankSyncDao.getRankEdwCount();
	}

	public void updateRankEdw() throws Exception{
		beverageRankSyncDao.updateRankEdw();
	}
}
