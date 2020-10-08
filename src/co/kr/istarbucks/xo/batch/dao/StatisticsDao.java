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
 * XO ��� StatisticsDao.
 * @author eZEN ksy
 * @since 2014. 1. 22.
 * @version $Revision: 1.5 $
 */
public class StatisticsDao {
	
	/**
	 * DB ���� ��¥ ��������
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
	 * �������ܺ� ���� ���
	 * @return
	 * @throws Exception
	 */
	public int payMethod ( Map<String, String> paramMap ) throws Exception {
		String sqlId = "statistics.payMethod";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId, paramMap);
	}
	
	/**
	 * ���庰 �ֹ� ���
	 * @return
	 * @throws Exception
	 */
	public int storeOrder ( Map<String, String> paramMap ) throws Exception {
		String sqlId = "statistics.storeOrder";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId, paramMap);
	}
	
	/**
	 * ���庰 �ֹ� �հ� ���
	 * @return
	 * @throws Exception
	 */
	public int storeOrderSum ( Map<String, String> paramMap ) throws Exception {
		String sqlId = "statistics.storeOrderSum";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId, paramMap);
	}
	
	/**
	 * SKU�� �ֹ� ���
	 * @return
	 * @throws Exception
	 */
	public int skuOrder ( Map<String, String> paramMap ) throws Exception {
		String sqlId = "statistics.skuOrder";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId, paramMap);
	}
	
	/**
	 * SKU�� �ֹ� �հ� ���
	 * @return
	 * @throws Exception
	 */
	public int skuOrderSum ( Map<String, String> paramMap ) throws Exception {
		String sqlId = "statistics.skuOrderSum";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId, paramMap);
	}

	/**
	 * �ֹ���ĺ� �ֹ� ���
	 * @return
	 * @throws Exception
	 */
	public int storeSelOrder() throws Exception {
		String sqlId = "statistics.storeSelOrder";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}

	/**
	 * My Favorites ��� ���
	 * @return
	 * @throws Exception
	 */
	public int myFavoritesRegInfo() throws Exception {
		String sqlId = "statistics.myFavoritesRegInfo";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}

	/**
	 * SSG PAY ���� ���
	 * @return
	 * @throws Exception
	 */
	public int ssgpay() throws Exception {
		String sqlId = "statistics.ssgpay";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}

	/**
	 * My Favorites �ֹ� ���
	 * @return
	 * @throws Exception
	 */
	public int myFavoritesOrderInfo() throws Exception {
		String sqlId = "statistics.myFavoritesOrderInfo";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}

	/**
	 * ���ɹ�ĺ� ���
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
	 * Ȧ���� �������ܺ� ���� �����
	 * @return
	 * @throws Exception
	 */
	public int payMethodWholecake() throws Exception {
		String sqlId = "statistics.payMethodWholecake";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}
	
	/**
	 * Ȧ���� SSG PAY ���� �����
	 * @return
	 * @throws Exception
	 */
	public int ssgpayWholecake() throws Exception {
		String sqlId = "statistics.ssgpayWholecake";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}

	/**
	 * �ֹ���ĺ� �ֹ� �����(OpenApi��)
	 * @return
	 */
	public int storeSelOrderOpenApi() throws Exception {
		String sqlId = "statistics.storeSelOrderOpenApi";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}
	
	/**
	 * ���庰 �ֹ� ���� ���
	 * @return
	 * @throws Exception
	 */
	public int storeOrderReservation() throws Exception {
		String sqlId = "statistics.storeOrderReservation";
		SqlMapClient sqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return sqlMap.update (sqlId);
	}
}
