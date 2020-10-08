package co.kr.istarbucks.xo.batch.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.mgr.BeverageRankSyncMgr;

public class BeverageRankSynchronized {
	private static Log logger = LogFactory.getLog("beverageRankSynchronized");
	
	private final BeverageRankSyncMgr beverageRankSyncMgr;
	
	public static void main(String[] args) {
		BeverageRankSynchronized beverageRankSynchronized = new BeverageRankSynchronized();
		beverageRankSynchronized.process();
	}
	
	public BeverageRankSynchronized() {
		this.beverageRankSyncMgr = new BeverageRankSyncMgr();
	}
	
	public void process() {
		
		try {
			logger.info("process start!");
			logger.info("BeverageRankSynchronized process start ::: ");
			
			int totalCnt  = 0;
			
			totalCnt = beverageRankSyncMgr.getRankEdwCount();
			
			logger.info("XO_SELL_RANK_EDW_IF Count = " + totalCnt);
			
			if (totalCnt > 0) {				
				beverageRankSyncMgr.updateRankEdw();
			}
			logger.info("BeverageRankSynchronized process end ::: ");
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
