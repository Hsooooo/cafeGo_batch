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
 * XO ��� StatisticsMgr.
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
	 * DB ���� ��¥ ��������
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getToday () throws Exception {
		return this.statisticsDao.getToday ();
	}
	
	/**
	 * �������ܺ� ���� ���
	 * @return
	 * @throws Exception
	 */
	public int payMethod ( Map<String, String> paramMap ) throws Exception {
		return this.statisticsDao.payMethod (paramMap);
	}
	
	/**
	 * ���庰 �ֹ� ���
	 * @return
	 * @throws Exception
	 */
	public int storeOrder ( Map<String, String> paramMap ) throws Exception {
		return this.statisticsDao.storeOrder (paramMap);
	}
	
	/**
	 * ���庰 �ֹ� �հ� ���
	 * @return
	 * @throws Exception
	 */
	public int storeOrderSum ( Map<String, String> paramMap ) throws Exception {
		return this.statisticsDao.storeOrderSum (paramMap);
	}
	
	/**
	 * SKU�� �ֹ� ���
	 * @return
	 * @throws Exception
	 */
	public int skuOrder ( Map<String, String> paramMap ) throws Exception {
		return this.statisticsDao.skuOrder (paramMap);
	}
	
	/**
	 * SKU�� �ֹ� �հ� ���
	 * @return
	 * @throws Exception
	 */
	public int skuOrderSum ( Map<String, String> paramMap ) throws Exception {
		return this.statisticsDao.skuOrderSum (paramMap);
	}

	/**
	 * �ֹ���ĺ� �ֹ� ���
	 * @return
	 * @throws Exception
	 */
	public int storeSelOrder() throws Exception {
		return this.statisticsDao.storeSelOrder ();
	}

	/**
	 * My Favorites ��� ���
	 * @return
	 * @throws Exception
	 */
	public int myFavoritesRegInfo() throws Exception {
		return this.statisticsDao.myFavoritesRegInfo ();
	}

	/**
	 * SSG PAY ���� ���
	 * @return
	 * @throws Exception
	 */
	public int ssgpay() throws Exception {
		return this.statisticsDao.ssgpay ();
	}

	/**
	 * My Favorites �ֹ� ���
	 * @return
	 * @throws Exception
	 */
	public int myFavoritesOrderInfo() throws Exception {
		return this.statisticsDao.myFavoritesOrderInfo ();
	}

	/**
	 * ���ɹ�ĺ� ���
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int receiveOrder(Map<String, String> paramMap) throws Exception {
		return this.statisticsDao.receiveOrder (paramMap);
	}

	/**
	 * ��纰 �ֹ� ���
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int eventOrder(Map<String, String> paramMap) throws Exception {
		return this.statisticsDao.eventOrder (paramMap);
	}

	/**
	 * ��纰 sku�ֹ� ���
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int eventSkuOrder(Map<String, String> paramMap) throws Exception {
		return this.statisticsDao.eventSkuOrder (paramMap);
	}
	
	/**
	 * Ȧ���� �������ܺ� ���� �����
	 * @return
	 * @throws Exception
	 */
	public int payMethodWholecake() throws Exception {
		return this.statisticsDao.payMethodWholecake();
	}
	
	/**
	 * Ȧ���� SSG PAY ���� �����
	 * @return
	 * @throws Exception
	 */
	public int ssgpayWholecake() throws Exception {
		return this.statisticsDao.ssgpayWholecake();
	}

	/**
	 * �ֹ���ĺ� �ֹ� �����(OpenApi��)
	 * @return
	 * @throws Exception
	 */
	public int storeSelOrderOpenApi() throws Exception {
		return this.statisticsDao.storeSelOrderOpenApi ();
	}
	
	/**
	 * ���庰 �ֹ� ���� ���
	 * @return
	 * @throws Exception
	 */
	public int storeOrderReservation() throws Exception {
		return this.statisticsDao.storeOrderReservation();
	}
}
