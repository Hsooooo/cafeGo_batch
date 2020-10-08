package co.kr.istarbucks.xo.batch.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.mgr.GiftShopProductRankMgr;

public class GiftShopProductRankMaker {
	
	private static Log logger = LogFactory.getLog("giftshopProductSummary");

	public static void main(String[] args) {
		GiftShopProductRankMaker processor = new GiftShopProductRankMaker();
		processor.execute();
	}
	
	private void execute() {
		logger.info("giftshopProductSummary process start ::: ");
		
		GiftShopProductRankMgr manager = new GiftShopProductRankMgr();			
			
		manager.makeRank();		
			
			
		manager.makeNew();		
			
		
		logger.info("giftshopProductSummary process end ::: ");
	}
	
}
