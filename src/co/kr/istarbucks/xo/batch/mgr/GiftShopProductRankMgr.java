package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.dao.GiftShopProductRankDao;

public class GiftShopProductRankMgr {
	
	private static Log logger = LogFactory.getLog("giftshopProductSummary");
	
	final transient private GiftShopProductRankDao dao;
	
	public GiftShopProductRankMgr() {
		this.dao = new GiftShopProductRankDao();
	}
	
	public int makeRank() {
		int cnt = 0;
		
		try {
			cnt = dao.createProductRanking();
		} catch (SQLException e) {
			logger.error("ERROR! :", e);
		}
		
		logger.info("XO_GIFT_STAT_RANK :: Insert Count = " + cnt);
		
		return cnt;			
	}
	
	public int makeNew(){
		int cnt = 0;
		
		try {
			cnt = dao.createProductNewest();
		} catch (SQLException e) {
			logger.error("ERROR! :", e);
		}
		
		logger.info("XO_GIFT_STAT_NEW :: Insert Count = " + cnt);
		
		return cnt;
	}

}
