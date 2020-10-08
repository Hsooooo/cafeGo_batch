/*
 * @(#) $Id: StatisticsDao.java,v 1.5 2019/06/04 01:04:17 leeminjung Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.dao;

import java.util.Map;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * XO 통계 StatisticsDao.
 * @author eZEN ksy
 * @since 2014. 1. 22.
 * @version $Revision: 1.5 $
 */
public class StatisticsDao {
	
	/**
	 * DB 오늘 날짜 가져오기
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public Map<String, String> getToday () throws Exception {
		String sqlId = "statistics.getToday";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return (Map<String, String>) sqlMap.queryForObject (sqlId);
	}
	
	/**
	 * 결제수단별 매출 통계
	 * @return
	 * @throws Exception
	 */
	public int payMethod ( Map<String, String> paramMap ) throws Exception {
		String sqlId = "statistics.payMethod";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId, paramMap);
	}
	
	/**
	 * 매장별 주문 통계
	 * @return
	 * @throws Exception
	 */
	public int storeOrder ( Map<String, String> paramMap ) throws Exception {
		String sqlId = "statistics.storeOrder";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId, paramMap);
	}
	
	/**
	 * 매장별 주문 합계 통계
	 * @return
	 * @throws Exception
	 */
	public int storeOrderSum ( Map<String, String> paramMap ) throws Exception {
		String sqlId = "statistics.storeOrderSum";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId, paramMap);
	}
	
	/**
	 * SKU별 주문 통계
	 * @return
	 * @throws Exception
	 */
	public int skuOrder ( Map<String, String> paramMap ) throws Exception {
		String sqlId = "statistics.skuOrder";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId, paramMap);
	}
	
	/**
	 * SKU별 주문 합계 통계
	 * @return
	 * @throws Exception
	 */
	public int skuOrderSum ( Map<String, String> paramMap ) throws Exception {
		String sqlId = "statistics.skuOrderSum";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId, paramMap);
	}

	/**
	 * 주문방식별 주문 통계
	 * @return
	 * @throws Exception
	 */
	public int storeSelOrder() throws Exception {
		String sqlId = "statistics.storeSelOrder";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}

	/**
	 * My Favorites 등록 통계
	 * @return
	 * @throws Exception
	 */
	public int myFavoritesRegInfo() throws Exception {
		String sqlId = "statistics.myFavoritesRegInfo";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}

	/**
	 * SSG PAY 결제 통계
	 * @return
	 * @throws Exception
	 */
	public int ssgpay() throws Exception {
		String sqlId = "statistics.ssgpay";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}

	/**
	 * My Favorites 주문 통계
	 * @return
	 * @throws Exception
	 */
	public int myFavoritesOrderInfo() throws Exception {
		String sqlId = "statistics.myFavoritesOrderInfo";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}

	/**
	 * 수령방식별 통계
	 * @return
	 * @throws Exception
	 */
	public int receiveOrder(Map<String, String> paramMap) throws Exception {
		String sqlId = "statistics.receiveOrder";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId, paramMap);
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public int eventOrder(Map<String, String> paramMap) throws Exception {
		String sqlId = "statistics.eventOrder";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId,paramMap);
	}

	public int eventSkuOrder(Map<String, String> paramMap) throws Exception {
		String sqlId = "statistics.eventSkuOrder";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId,paramMap);
	}
	
	/**
	 * 홀케익 결제수단별 매출 일통계
	 * @return
	 * @throws Exception
	 */
	public int payMethodWholecake() throws Exception {
		String sqlId = "statistics.payMethodWholecake";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}
	
	/**
	 * 홀케익 SSG PAY 결제 일통계
	 * @return
	 * @throws Exception
	 */
	public int ssgpayWholecake() throws Exception {
		String sqlId = "statistics.ssgpayWholecake";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}

	/**
	 * 주문방식별 주문 일통계(OpenApi용)
	 * @return
	 */
	public int storeSelOrderOpenApi() throws Exception {
		String sqlId = "statistics.storeSelOrderOpenApi";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}
	
	/**
	 * 매장별 주문 예약 통계
	 * @return
	 * @throws Exception
	 */
	public int storeOrderReservation() throws Exception {
		String sqlId = "statistics.storeOrderReservation";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}
}
