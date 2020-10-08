/*
 * @(#) $Id: Statistics.java,v 1.5 2019/06/04 01:04:17 leeminjung Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.main;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import co.kr.istarbucks.xo.batch.common.util.DateUtil;
import co.kr.istarbucks.xo.batch.exception.XOException;
import co.kr.istarbucks.xo.batch.mgr.StatisticsMgr;
import co.kr.istarbucks.xo.batch.mon.DataHelper;

/**
 * XO ��� Statistics.
 * @author eZEN ksy
 * @since 2014. 1. 22.
 * @version $Revision: 1.5 $
 */
public class Statistics {
	
	private static Log sLogger = LogFactory.getLog ("STATISTICS");
	
	private final StatisticsMgr statisticsMgr;
	private final String loggerTitle;
	private boolean isFirstDay = false;
	
    private static String bm_today = DateUtil.getToday("yyyyMMdd");
    private static boolean bm_is_insert_fail = false;
    private static int bm_start_cnt=0;
    private static int bm_error_cnt=0;
    private static int bm_success_cnt=0;
    private static String bm_error_target="";
    
	public Statistics () {
		this.statisticsMgr = new StatisticsMgr ();
		this.loggerTitle = "[statistics] ";
	}
	
	public void start () {
		StringBuffer logSb = new StringBuffer();
		
		long startTime = System.currentTimeMillis ();
		
		logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("START");
		sLogger.info (logSb.toString ());
		
		// ���� DB ��¥ ��ȸ
		getToday ();
		
		Map<String, String> paramMap = new HashMap<String, String> ();
		
		
		// �������ܺ� ���� ���
		paramMap.put ("type", "D");
		this.startPayMethod (paramMap);
		//if ( isFirstDay ) {
		//	paramMap.put ("type", "M");
		//	this.startPayMethod (paramMap);
		//}
		
		
		// ���� �ֹ� ���
		paramMap.put ("type", "D");
		this.startStoreOrder (paramMap);
		if ( isFirstDay ) {
			paramMap.put ("type", "M");
			this.startStoreOrder (paramMap);
		}
		
		// ���庰 �ֹ� �հ� ���
		paramMap.put ("type", "D");
		this.startStoreOrderSum (paramMap);
		if ( isFirstDay ) {
			paramMap.put ("type", "M");
			this.startStoreOrderSum (paramMap);
		}
		
		// SKU�� �ֹ� ���
		paramMap.put ("type", "D");
		this.startSkuOrder (paramMap);
		if ( isFirstDay ) {
			paramMap.put ("type", "M");
			this.startSkuOrder (paramMap);
		}
		
		// SKU�� �ֹ� �հ� ���
		paramMap.put ("type", "D");
		this.startSkuOrderSum (paramMap);
		if ( isFirstDay ) {
			paramMap.put ("type", "M");
			this.startSkuOrderSum (paramMap);
		}

		// �ֹ���ĺ� �ֹ� �����
		this.startStoreSelOrder ();
		
		// My Favorites ��� �����
		this.startMyFavoritesRegInfo ();
		
		// My Favorites �ֹ� �����
		this.startMyFavoritesOrderInfo ();
		
		// SSG PAY ���� �����
		this.startSsgpay ();
		
		// ���ɹ�ĺ� �ֹ� ��/�� ���
		paramMap.put ("type", "D");
		this.startReceiveOrder(paramMap);
		if ( isFirstDay ) {
			paramMap.put ("type", "M");
			this.startReceiveOrder(paramMap);
		}
		
		// ��纰 �ֹ� ��/�� ���
		paramMap.put ("type", "D");
		this.startEventOrder(paramMap);
		if ( isFirstDay ) {
			paramMap.put ("type", "M");
			this.startEventOrder(paramMap);
		}
		
		// ��纰 SKU�ֹ� ��/�� ���
		paramMap.put ("type", "D");
		this.startEventSkuOrder(paramMap);
		if ( isFirstDay ) {
			paramMap.put ("type", "M");
			this.startEventSkuOrder(paramMap);
		}
		
		// Ȧ���� �������ܺ� ���� �����
		this.startPayMethodWholecake();
		
		// Ȧ���� SSG PAY ���� �����
		this.startSsgpayWholecake();
		
		// �ֹ���ĺ� �ֹ� �����(OpenApi��)
		this.startStoreSelOrderOpenApi ();
		
		// ���庰 �ֹ� ���� ���
		this.startStoreOrderReservation();
		
		logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("END : ").append (System.currentTimeMillis () - startTime).append ("ms");
		sLogger.info (logSb.toString ());
	}
	
	/**
	 * DB�� ���� ��¥ ��������
	 */
	private void getToday () {
		StringBuffer logSb = new StringBuffer();
		
		try {
			Map<String, String> map = this.statisticsMgr.getToday ();
			
			String today = "";
			if ( map != null ) {
				isFirstDay = StringUtils.equals (map.get ("firstDayFlag"), "Y");
				today = map.get ("dbToday");
			}
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics TODAY : ").append (today);
			sLogger.info (logSb.toString ());
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics TODAY ").append (e);
			sLogger.error (logSb.toString (), e);
		}
	}
	
	/**
	 * �������ܺ� ���� ���
	 */
	private void startPayMethod ( Map<String, String> paramMap ) {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.payMethod (paramMap);
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics payMethodDay (").append (paramMap.get ("type")).append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics payMethodDay (").append (paramMap.get ("type")).append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startPayMethod ";
		}
	}
	
	/**
	 * ���� �ֹ� ���
	 */
	private void startStoreOrder ( Map<String, String> paramMap ) {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.storeOrder (paramMap);
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics storeOrder (").append (paramMap.get ("type")).append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics storeOrder (").append (paramMap.get ("type")).append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startStoreOrder ";
		}
	}
	
	/**
	 * ���庰 �ֹ� �հ� ���
	 */
	private void startStoreOrderSum ( Map<String, String> paramMap ) {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.storeOrderSum (paramMap);
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics storeOrderSum (").append (paramMap.get ("type")).append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics storeOrderSum (").append (paramMap.get ("type")).append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startStoreOrderSum ";
		}
	}
	
	/**
	 * SKU�� �ֹ� ���
	 */
	private void startSkuOrder ( Map<String, String> paramMap ) {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.skuOrder (paramMap);
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics skuOrder (").append (paramMap.get ("type")).append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics skuOrder (").append (paramMap.get ("type")).append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startSkuOrder ";
		}
	}
	
	/**
	 * SKU�� �ֹ� ���
	 */
	private void startSkuOrderSum ( Map<String, String> paramMap ) {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.skuOrderSum (paramMap);
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics skuOrderSum (").append (paramMap.get ("type")).append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics skuOrderSum (").append (paramMap.get ("type")).append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startSkuOrderSum ";
		}
	}

	/**
	 * �ֹ���ĺ� �ֹ� ���
	 */
	private void startStoreSelOrder () {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.storeSelOrder ();
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics storeSelOrder (").append ("D").append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics storeSelOrder (").append ("D").append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startStoreSelOrder ";
		}
	}
	
	/**
	 * My Favorites ��� ���
	 */
	private void startMyFavoritesRegInfo () {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.myFavoritesRegInfo ();
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics myFavoritesRegInfo (").append ("D").append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics myFavoritesRegInfo (").append ("D").append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startMyFavoritesRegInfo ";
		}
	}
	
	/**
	 * My Favorites �ֹ� ���
	 */
	private void startMyFavoritesOrderInfo() {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.myFavoritesOrderInfo ();
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics myFavoritesOrderInfo (").append ("D").append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics myFavoritesOrderInfo (").append ("D").append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startMyFavoritesOrderInfo ";
		}
	}
	
	/**
	 * SSG PAY �������
	 */
	private void startSsgpay () {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.ssgpay ();
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics ssgpay (").append ("D").append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics ssgpay (").append ("D").append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startSsgpay ";
		}
	}
	
	/**
	 * ���ɹ�ĺ� ���
	 * @param paramMap
	 */
	private void startReceiveOrder(Map<String, String> paramMap) {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.receiveOrder (paramMap);
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics receiveOrder (").append (paramMap.get ("type")).append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics receiveOrder (").append (paramMap.get ("type")).append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startSsgpay ";
		}
	}

	/**
	 * ��纰 �ֹ� ���
	 * @param paramMap
	 */
	private void startEventOrder(Map<String, String> paramMap) {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.eventOrder (paramMap);
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics eventOrder (").append (paramMap.get ("type")).append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics eventOrder (").append (paramMap.get ("type")).append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startEventOrder ";
		}
	}	
	
	/**
	 * ��纰 SKU�ֹ� ���
	 * @param paramMap
	 */
	private void startEventSkuOrder(Map<String, String> paramMap) {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.eventSkuOrder (paramMap);
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics eventSkuOrder (").append (paramMap.get ("type")).append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics eventSkuOrder (").append (paramMap.get ("type")).append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startEventSkuOrder ";
		}
		
	}
	
	/**
	 * Ȧ���� �������ܺ� ���� �����
	 */
	private void startPayMethodWholecake() {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.payMethodWholecake();
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics payMethodWholecake (D) : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics payMethodWholecake (D) : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startPayMethodWholecake ";
		}
	}
	
	/**
	 * Ȧ���� SSG PAY ���� �����
	 */
	private void startSsgpayWholecake() {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.ssgpayWholecake();
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics ssgpayWholecake (D) : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics ssgpayWholecake (D) : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startSsgpayWholecake ";
		}
	}
	
	
	/**
	 * �ֹ���ĺ� �ֹ� �����(OpenApi��)
	 */
	private void startStoreSelOrderOpenApi() {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resutCnt = this.statisticsMgr.storeSelOrderOpenApi ();
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics storeSelOrderOpenApi (").append ("D").append (") : ").append (resutCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics storeSelOrderOpenApi (").append ("D").append (") : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startStoreSelOrderOpenApi ";
		}
	}	
	
	/**
	 * ���庰 �ֹ� ���� ���
	 */
	private void startStoreOrderReservation() {
		StringBuffer logSb = new StringBuffer();
		
		try {
			long startTime = System.currentTimeMillis ();
			
			int resultCnt = this.statisticsMgr.storeOrderReservation();
			
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics storeOrderReservation : ").append (resultCnt).append ("�� END : ").append (System.currentTimeMillis () - startTime).append ("ms");
			sLogger.info (logSb.toString ());
			bm_success_cnt++;
		} catch ( Exception e ) {
			logSb.delete (0, logSb.length ()).append (this.loggerTitle).append ("Statistics storeOrderReservation : ").append (e);
			sLogger.error (logSb.toString (), e);
            bm_error_cnt++;
            bm_error_target+="startStoreOrderReservation ";
		}
	}
	
	public static void main ( String[] args ) {
		
        /********************************************
      	 BATCH MONITORING START
      	 ********************************************/
      	DataHelper helper = new DataHelper();
      	try {
      		bm_start_cnt = helper.startMonitor(bm_today, 1, "XO_STATISTICS_SERVICE", "XO_STATISTICS_DETAIL");
      	} catch (XOException e) {
      		bm_is_insert_fail = true;
      		sLogger.error(e.getMessage(), e);
      	}
      	
		Statistics statistics = new Statistics ();
		statistics.start ();
		
		/********************************************
		 BATCH MONITORING END
		 ********************************************/
		try {
			if (bm_is_insert_fail) {
				bm_is_insert_fail = false;
				
			} else {
				helper.endMonitor(bm_today, bm_start_cnt , "XO_STATISTICS_SERVICE", "XO_STATISTICS_DETAIL" ,16, bm_success_cnt, bm_error_cnt,bm_error_target);
			}
		} catch (XOException e) {
			sLogger.error(e.getMessage(), e);
		}
		
	}
	
}
