/*
 * @(#) $Id: TradeCorrection.java,v 1.3 2016/12/01 05:23:08 namgu1 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import co.kr.istarbucks.xo.batch.common.dto.xo.TradeCorrectionDto;
import co.kr.istarbucks.xo.batch.mgr.TradeCorrectionMgr;

/**
 * 1. �ŷ� ��� ��� ��ȸ -  ��� ó��
 * 1-1. �ŷ� ��� �Ϸ� ó��
 * 2. �ֹ� ��� ��� ��ȸ
 * 2-2 �ֹ� ��� ó��
 * 
 * Ȧ����
 * 3. Ȧ���� ���ɿϷ� ��� ��� ��ȸ
 * 3.1. Ȧ���� ���ɿϷ� ��� �Ϸ� ó��
 * 4. Ȧ���� �̼��� ��� ��� ��� ��ȸ
 * 4.1. Ȧ���� �̼��� ��� ��� �Ϸ� ó��
 * 		
 * �������� �ŷ� ��� TradeCorrection.
 * @author eZEN
 * @since 2014. 2. 5.
 * @version $Revision: 1.3 $
 */
public class TradeCorrection {
	
	private final Logger tLogger = Logger.getLogger ("TRADE");
	
	private final TradeCorrectionMgr tradeCorrectionMgr;
	
	private List<TradeCorrectionDto> tradeCorrectionList;
	private List<TradeCorrectionDto> orderCancelList;
	
	private List<TradeCorrectionDto> tradeWholecakeCorrectionList; //  Ȧ���� ���ɿϷ� ��� ����� ��� 
	private List<TradeCorrectionDto> receivedDisuseWholecakeList;  //  Ȧ���� �̼��� ��� ��� ����� ���
		
	private final String loggerTitle;
	private final StringBuffer logSb;
 	
	/** ���� �Ϸ� **/
	private static final String WHOLECAKE_ORDER_STATUS_RECEIVED_COMP   = "O30"; // ���� �Ϸ�
	/** �̼��� ��� **/
	private static final String WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE = "O31"; // �̼��� ���
	
	public TradeCorrection () {
		this.tradeCorrectionMgr = new TradeCorrectionMgr ();
		this.loggerTitle = "[TradeCorrection] ";
		this.logSb = new StringBuffer(); // log�� StringBuffer
	}
	
	public int tradeCorrectionTotalCnt = 0;
	public int orderCancelListTotalCnt = 0;
	
	public int tradeWholecakeCorrectionTotalCnt = 0; // Ȧ���� ���ɿϷ� ��� ��� ī����
	public int receivedDisuseWholecakeTotalCnt = 0;  // Ȧ���� �̼��� ��� ��� ��� ī����
	
	public void start () {
		long startTimeTotal = System.currentTimeMillis ();
		
		logSb.delete(0, logSb.length()).append(this.loggerTitle).append("START");
		tLogger.info(logSb.toString());
		
		Map<String, Object> dbMap = new HashMap<String, Object> ();
		
		// 1. �ŷ� ��� ��� ��ȸ
		this.getTradeCorrectionInfo();
		
		// 1-1. �ŷ� ��� �Ϸ� ó��
		this.tradeComplete();
		
		// 2. �ֹ� ��� ��� ��ȸ
		this.getOrderCancelInfo();
		
		// 2-1. �ֹ� ��� ó��
		this.orderCancel();
		
		// 3. Ȧ���� ���ɿϷ� ��� ��� ��ȸ
		dbMap.put("status", WHOLECAKE_ORDER_STATUS_RECEIVED_COMP);
		// 1) Ȧ���� O30 ���ɿϷ� ����� ��ȸ
		this.getTradeWholecakeCorrectionInfo(dbMap);
		
		// 3.1. Ȧ���� ���ɿϷ� ��� �Ϸ� ó��
		 this.tradeWholecakeComplete();
		
		// 4. Ȧ���� �̼��� ��� ��� ��� ��ȸ
		dbMap.clear();
		dbMap.put("status", WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE);
		// 1) Ȧ���� O31 �̼��� ��� ����� ��ȸ
		this.getReceivedDisuseWholecakeInfo(dbMap);
		
		// 4.1 Ȧ���� �̼��� ��� ��� �Ϸ� ó��
		this.receivedDisuseWholecakeComplete();
		
		logSb.delete(0, logSb.length()).append(this.loggerTitle).append("END : ").append(System.currentTimeMillis () - startTimeTotal).append("ms");
		tLogger.info(logSb.toString());
	}


	public void start (String tradeDate) {
		long startTimeTotal = System.currentTimeMillis ();
		
		logSb.delete(0, logSb.length()).append(this.loggerTitle).append("START - ").append(tradeDate);
		tLogger.info(logSb.toString());
		
		Map<String, Object> dbMap = new HashMap<String, Object> ();
		
		// 1. �ŷ� ��� ��� ��ȸ
		this.getTradeCorrectionDateInfo(tradeDate);
		
		// 1-1. �ŷ� ��� �Ϸ� ó��
		this.tradeComplete();
		
		// 2. �ֹ� ��� ��� ��ȸ
		this.getOrderCancelInfo(tradeDate);
		
		// 2-1. �ֹ� ��� ó��
		this.orderCancel();
		
		// 3. Ȧ���� ���ɿϷ� ��� ��� ��ȸ
		dbMap.put("status",    WHOLECAKE_ORDER_STATUS_RECEIVED_COMP);
		dbMap.put("tradeDate", tradeDate);
		// 1) Ȧ���� O30 ���ɿϷ� ����� ��ȸ
		this.getTradeWholecakeCorrectionInfo(dbMap);
		
		// 3.1. Ȧ���� ���ɿϷ� ��� �Ϸ� ó��
		 this.tradeWholecakeComplete();
		
		// 4. Ȧ���� �̼��� ��� ��� ��� ��ȸ
		dbMap.clear();
		dbMap.put("status",    WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE);
		dbMap.put("tradeDate", tradeDate);
		// 1) Ȧ���� O31 �̼��� ��� ����� ��ȸ
		this.getReceivedDisuseWholecakeInfo(dbMap);
		
		// 4.1 Ȧ���� �̼��� ��� ��� �Ϸ� ó��
		this.receivedDisuseWholecakeComplete();
		
		logSb.delete(0, logSb.length()).append(this.loggerTitle).append("END : ").append(System.currentTimeMillis () - startTimeTotal).append("ms");
		tLogger.info(logSb.toString());
	}
	
	/**
	 * �ŷ� ��� ��� ��ȸ
	 */
	private void getTradeCorrectionInfo() {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			this.tradeCorrectionList = this.tradeCorrectionMgr.getTradeCorrectionInfo ();
			
			this.tradeCorrectionTotalCnt = this.tradeCorrectionList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get TradeCorrection Info Count : (").append(this.tradeCorrectionList.size ()).append(")�� END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
			
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}
	
	/**
	 * �ŷ� ��� ��� ��ȸ - �ŷ� ��¥ �Է�
	 */
	private void getTradeCorrectionDateInfo(String tradeDate) {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			this.tradeCorrectionList = this.tradeCorrectionMgr.getTradeCorrectionDateInfo (tradeDate);
			
			this.tradeCorrectionTotalCnt = this.tradeCorrectionList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get TradeCorrection Info Count : (").append(this.tradeCorrectionList.size ()).append(")�� END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
			
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}
	
	/**
	 * �ŷ� ��� �Ϸ� ó��
	 */
	private void tradeComplete() {
		try {
			long startTime = System.currentTimeMillis ();
			
			int resultCnt = this.tradeCorrectionMgr.tradeComplete (tradeCorrectionList);
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("tradeComplete Count : (").append(resultCnt).append(")�� / FAIL Count : (").append(tradeCorrectionTotalCnt - resultCnt).append(")�� END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}
	
	/**
	 * �ֹ� ��� ��� ��ȸ
	 */
	private void getOrderCancelInfo() {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			this.orderCancelList = this.tradeCorrectionMgr.getOrderCancelInfo ();
			
			this.orderCancelListTotalCnt = this.orderCancelList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get OrderCancel Info Count : (").append(this.orderCancelList.size ()).append(")�� END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}

	/**
	 * �ֹ� ��� ��� ��ȸ - �ŷ� ��¥ �Է�
	 */
	private void getOrderCancelInfo(String tradeDate) {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			this.orderCancelList = this.tradeCorrectionMgr.getOrderCancelDateInfo (tradeDate);
			
			this.orderCancelListTotalCnt = this.orderCancelList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get OrderCancel Info Count : (").append(this.orderCancelList.size ()).append(")�� END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}
	
	/**
	 * �ֹ� ��� ó��
	 */
	private void orderCancel() {
		try {
			long startTime = System.currentTimeMillis ();
			
			int resultCnt = this.tradeCorrectionMgr.orderCancel (orderCancelList);

			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("orderCancel cnt : (").append(resultCnt).append(")�� / FAIL Count : (").append(orderCancelListTotalCnt - resultCnt).append(")�� END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}
	
	/**
	 * Ȧ���� ���ɿϷ� ��� ��� ��ȸ
	 * @param dbMap 
	 */
	private void getTradeWholecakeCorrectionInfo(Map<String, Object> dbMap) {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			// Ȧ���� ���ɿϷ� ��� ��� ��ȸ  
			this.tradeWholecakeCorrectionList = this.tradeCorrectionMgr.getWholecakeInfo (dbMap);
			
			this.tradeWholecakeCorrectionTotalCnt = this.tradeWholecakeCorrectionList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get TradeWholecakeCorrectionInfo Info Count : (").append(this.tradeWholecakeCorrectionList.size ()).append(")�� END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
			
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
		
	}
	
	/**
	 * Ȧ���� �̼��� ��� ��� ��� ��ȸ
	 * @param dbMap
	 */
	private void getReceivedDisuseWholecakeInfo(Map<String, Object> dbMap) {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			// Ȧ���� ���ɿϷ� ��� ��� ��ȸ  
			this.receivedDisuseWholecakeList = this.tradeCorrectionMgr.getWholecakeInfo (dbMap);
			
			this.receivedDisuseWholecakeTotalCnt = this.receivedDisuseWholecakeList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get ReceivedDisuseWholecakeInfo Info Count : (").append(this.receivedDisuseWholecakeList.size ()).append(")�� END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
			
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
		
	}
	
	/**
	 * Ȧ���� ���ɿϷ� ��� �Ϸ� ó��
	 */
	private void tradeWholecakeComplete() {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			// Ȧ���� ���ɿϷ� ��� �Ϸ� ó��
			int resultCnt = this.tradeCorrectionMgr.tradeWholecakeComplete (tradeWholecakeCorrectionList);
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("tradeWholecakeComplete Count : (").append(resultCnt).append(")�� / FAIL Count : (").append(tradeWholecakeCorrectionTotalCnt - resultCnt).append(")�� END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}		
	}
	

	/**
	 * Ȧ���� �̼��� ��� ��� �Ϸ� ó��
	 */
	private void receivedDisuseWholecakeComplete() {
		
		try {
			long startTime = System.currentTimeMillis ();			
			// Ȧ���� �̼��� ��� ��� �Ϸ� ó��
			int resultCnt = this.tradeCorrectionMgr.receivedDisuseWholecakeComplete (receivedDisuseWholecakeList);
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("receivedDisuseWholecakeComplete Count : (").append(resultCnt).append(")�� / FAIL Count : (").append(receivedDisuseWholecakeTotalCnt - resultCnt).append(")�� END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
		
	}

	public static void main ( String[] args ) {
		TradeCorrection tc = new TradeCorrection();
		if (args.length > 1) {
			tc.start(args[1]);
		} else {
			tc.start();
		}
	}
}
