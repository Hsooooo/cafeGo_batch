/*
 * @(#) $Id: StatisticsMgr.java,v 1.5 2019/06/04 01:04:17 leeminjung Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.mgr;

import java.util.Map;

import co.kr.istarbucks.xo.batch.dao.StatisticsDao;

/**
 * XO 통계 StatisticsMgr.
 * @author eZEN ksy
 * @since 2014. 1. 22.
 * @version $Revision: 1.5 $
 */
public class StatisticsMgr {
	
	private final StatisticsDao statisticsDao;
	
	public StatisticsMgr () {
		this.statisticsDao = new StatisticsDao ();
	}
	
	/**
	 * DB 오늘 날짜 가져오기
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getToday () throws Exception {
		return this.statisticsDao.getToday ();
	}
	
	/**
	 * 결제수단별 매출 통계
	 * @return
	 * @throws Exception
	 */
	public int payMethod ( Map<String, String> paramMap ) throws Exception {
		return this.statisticsDao.payMethod (paramMap);
	}
	
	/**
	 * 매장별 주문 통계
	 * @return
	 * @throws Exception
	 */
	public int storeOrder ( Map<String, String> paramMap ) throws Exception {
		return this.statisticsDao.storeOrder (paramMap);
	}
	
	/**
	 * 매장별 주문 합계 통계
	 * @return
	 * @throws Exception
	 */
	public int storeOrderSum ( Map<String, String> paramMap ) throws Exception {
		return this.statisticsDao.storeOrderSum (paramMap);
	}
	
	/**
	 * SKU별 주문 통계
	 * @return
	 * @throws Exception
	 */
	public int skuOrder ( Map<String, String> paramMap ) throws Exception {
		return this.statisticsDao.skuOrder (paramMap);
	}
	
	/**
	 * SKU별 주문 합계 통계
	 * @return
	 * @throws Exception
	 */
	public int skuOrderSum ( Map<String, String> paramMap ) throws Exception {
		return this.statisticsDao.skuOrderSum (paramMap);
	}

	/**
	 * 주문방식별 주문 통계
	 * @return
	 * @throws Exception
	 */
	public int storeSelOrder() throws Exception {
		return this.statisticsDao.storeSelOrder ();
	}

	/**
	 * My Favorites 등록 통계
	 * @return
	 * @throws Exception
	 */
	public int myFavoritesRegInfo() throws Exception {
		return this.statisticsDao.myFavoritesRegInfo ();
	}

	/**
	 * SSG PAY 결제 통계
	 * @return
	 * @throws Exception
	 */
	public int ssgpay() throws Exception {
		return this.statisticsDao.ssgpay ();
	}

	/**
	 * My Favorites 주문 통계
	 * @return
	 * @throws Exception
	 */
	public int myFavoritesOrderInfo() throws Exception {
		return this.statisticsDao.myFavoritesOrderInfo ();
	}

	/**
	 * 수령방식별 통계
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int receiveOrder(Map<String, String> paramMap) throws Exception {
		return this.statisticsDao.receiveOrder (paramMap);
	}

	/**
	 * 행사별 주문 통계
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int eventOrder(Map<String, String> paramMap) throws Exception {
		return this.statisticsDao.eventOrder (paramMap);
	}

	/**
	 * 행사별 sku주문 통계
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int eventSkuOrder(Map<String, String> paramMap) throws Exception {
		return this.statisticsDao.eventSkuOrder (paramMap);
	}
	
	/**
	 * 홀케익 결제수단별 매출 일통계
	 * @return
	 * @throws Exception
	 */
	public int payMethodWholecake() throws Exception {
		return this.statisticsDao.payMethodWholecake();
	}
	
	/**
	 * 홀케익 SSG PAY 결제 일통계
	 * @return
	 * @throws Exception
	 */
	public int ssgpayWholecake() throws Exception {
		return this.statisticsDao.ssgpayWholecake();
	}

	/**
	 * 주문방식별 주문 일통계(OpenApi용)
	 * @return
	 * @throws Exception
	 */
	public int storeSelOrderOpenApi() throws Exception {
		return this.statisticsDao.storeSelOrderOpenApi ();
	}
	
	/**
	 * 매장별 주문 예약 통계
	 * @return
	 * @throws Exception
	 */
	public int storeOrderReservation() throws Exception {
		return this.statisticsDao.storeOrderReservation();
	}
}
