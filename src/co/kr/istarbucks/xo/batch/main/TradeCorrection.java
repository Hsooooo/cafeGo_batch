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
 * 1. 거래 대사 대상 조회 -  대사 처리
 * 1-1. 거래 대사 완료 처리
 * 2. 주문 취소 대상 조회
 * 2-2 주문 취소 처리
 * 
 * 홀케익
 * 3. 홀케익 수령완료 대사 대상 조회
 * 3.1. 홀케익 수령완료 대사 완료 처리
 * 4. 홀케익 미수령 폐기 대사 대상 조회
 * 4.1. 홀케익 미수령 폐기 대사 완료 처리
 * 		
 * 영업정보 거래 대사 TradeCorrection.
 * @author eZEN
 * @since 2014. 2. 5.
 * @version $Revision: 1.3 $
 */
public class TradeCorrection {
	
	private final Logger tLogger = Logger.getLogger ("TRADE");
	
	private final TradeCorrectionMgr tradeCorrectionMgr;
	
	private List<TradeCorrectionDto> tradeCorrectionList;
	private List<TradeCorrectionDto> orderCancelList;
	
	private List<TradeCorrectionDto> tradeWholecakeCorrectionList; //  홀케익 수령완료 대사 대상자 목록 
	private List<TradeCorrectionDto> receivedDisuseWholecakeList;  //  홀케익 미수령 폐기 대사 대상자 목록
		
	private final String loggerTitle;
	private final StringBuffer logSb;
 	
	/** 수령 완료 **/
	private static final String WHOLECAKE_ORDER_STATUS_RECEIVED_COMP   = "O30"; // 수령 완료
	/** 미수령 폐기 **/
	private static final String WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE = "O31"; // 미수령 폐기
	
	public TradeCorrection () {
		this.tradeCorrectionMgr = new TradeCorrectionMgr ();
		this.loggerTitle = "[TradeCorrection] ";
		this.logSb = new StringBuffer(); // log용 StringBuffer
	}
	
	public int tradeCorrectionTotalCnt = 0;
	public int orderCancelListTotalCnt = 0;
	
	public int tradeWholecakeCorrectionTotalCnt = 0; // 홀케익 수령완료 대사 대상 카운터
	public int receivedDisuseWholecakeTotalCnt = 0;  // 홀케익 미수령 폐기 대사 대상 카운터
	
	public void start () {
		long startTimeTotal = System.currentTimeMillis ();
		
		logSb.delete(0, logSb.length()).append(this.loggerTitle).append("START");
		tLogger.info(logSb.toString());
		
		Map<String, Object> dbMap = new HashMap<String, Object> ();
		
		// 1. 거래 대사 대상 조회
		this.getTradeCorrectionInfo();
		
		// 1-1. 거래 대사 완료 처리
		this.tradeComplete();
		
		// 2. 주문 취소 대상 조회
		this.getOrderCancelInfo();
		
		// 2-1. 주문 취소 처리
		this.orderCancel();
		
		// 3. 홀케익 수령완료 대사 대상 조회
		dbMap.put("status", WHOLECAKE_ORDER_STATUS_RECEIVED_COMP);
		// 1) 홀케익 O30 수령완료 대상자 조회
		this.getTradeWholecakeCorrectionInfo(dbMap);
		
		// 3.1. 홀케익 수령완료 대사 완료 처리
		 this.tradeWholecakeComplete();
		
		// 4. 홀케익 미수령 폐기 대사 대상 조회
		dbMap.clear();
		dbMap.put("status", WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE);
		// 1) 홀케익 O31 미수령 폐기 대상자 조회
		this.getReceivedDisuseWholecakeInfo(dbMap);
		
		// 4.1 홀케익 미수령 폐기 대사 완료 처리
		this.receivedDisuseWholecakeComplete();
		
		logSb.delete(0, logSb.length()).append(this.loggerTitle).append("END : ").append(System.currentTimeMillis () - startTimeTotal).append("ms");
		tLogger.info(logSb.toString());
	}


	public void start (String tradeDate) {
		long startTimeTotal = System.currentTimeMillis ();
		
		logSb.delete(0, logSb.length()).append(this.loggerTitle).append("START - ").append(tradeDate);
		tLogger.info(logSb.toString());
		
		Map<String, Object> dbMap = new HashMap<String, Object> ();
		
		// 1. 거래 대사 대상 조회
		this.getTradeCorrectionDateInfo(tradeDate);
		
		// 1-1. 거래 대사 완료 처리
		this.tradeComplete();
		
		// 2. 주문 취소 대상 조회
		this.getOrderCancelInfo(tradeDate);
		
		// 2-1. 주문 취소 처리
		this.orderCancel();
		
		// 3. 홀케익 수령완료 대사 대상 조회
		dbMap.put("status",    WHOLECAKE_ORDER_STATUS_RECEIVED_COMP);
		dbMap.put("tradeDate", tradeDate);
		// 1) 홀케익 O30 수령완료 대상자 조회
		this.getTradeWholecakeCorrectionInfo(dbMap);
		
		// 3.1. 홀케익 수령완료 대사 완료 처리
		 this.tradeWholecakeComplete();
		
		// 4. 홀케익 미수령 폐기 대사 대상 조회
		dbMap.clear();
		dbMap.put("status",    WHOLECAKE_ORDER_STATUS_RECEIVED_DISUSE);
		dbMap.put("tradeDate", tradeDate);
		// 1) 홀케익 O31 미수령 폐기 대상자 조회
		this.getReceivedDisuseWholecakeInfo(dbMap);
		
		// 4.1 홀케익 미수령 폐기 대사 완료 처리
		this.receivedDisuseWholecakeComplete();
		
		logSb.delete(0, logSb.length()).append(this.loggerTitle).append("END : ").append(System.currentTimeMillis () - startTimeTotal).append("ms");
		tLogger.info(logSb.toString());
	}
	
	/**
	 * 거래 대사 대상 조회
	 */
	private void getTradeCorrectionInfo() {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			this.tradeCorrectionList = this.tradeCorrectionMgr.getTradeCorrectionInfo ();
			
			this.tradeCorrectionTotalCnt = this.tradeCorrectionList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get TradeCorrection Info Count : (").append(this.tradeCorrectionList.size ()).append(")건 END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
			
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}
	
	/**
	 * 거래 대사 대상 조회 - 거래 날짜 입력
	 */
	private void getTradeCorrectionDateInfo(String tradeDate) {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			this.tradeCorrectionList = this.tradeCorrectionMgr.getTradeCorrectionDateInfo (tradeDate);
			
			this.tradeCorrectionTotalCnt = this.tradeCorrectionList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get TradeCorrection Info Count : (").append(this.tradeCorrectionList.size ()).append(")건 END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
			
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}
	
	/**
	 * 거래 대사 완료 처리
	 */
	private void tradeComplete() {
		try {
			long startTime = System.currentTimeMillis ();
			
			int resultCnt = this.tradeCorrectionMgr.tradeComplete (tradeCorrectionList);
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("tradeComplete Count : (").append(resultCnt).append(")건 / FAIL Count : (").append(tradeCorrectionTotalCnt - resultCnt).append(")건 END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}
	
	/**
	 * 주문 취소 대상 조회
	 */
	private void getOrderCancelInfo() {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			this.orderCancelList = this.tradeCorrectionMgr.getOrderCancelInfo ();
			
			this.orderCancelListTotalCnt = this.orderCancelList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get OrderCancel Info Count : (").append(this.orderCancelList.size ()).append(")건 END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}

	/**
	 * 주문 취소 대상 조회 - 거래 날짜 입력
	 */
	private void getOrderCancelInfo(String tradeDate) {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			this.orderCancelList = this.tradeCorrectionMgr.getOrderCancelDateInfo (tradeDate);
			
			this.orderCancelListTotalCnt = this.orderCancelList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get OrderCancel Info Count : (").append(this.orderCancelList.size ()).append(")건 END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}
	
	/**
	 * 주문 취소 처리
	 */
	private void orderCancel() {
		try {
			long startTime = System.currentTimeMillis ();
			
			int resultCnt = this.tradeCorrectionMgr.orderCancel (orderCancelList);

			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("orderCancel cnt : (").append(resultCnt).append(")건 / FAIL Count : (").append(orderCancelListTotalCnt - resultCnt).append(")건 END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
	}
	
	/**
	 * 홀케익 수령완료 대사 대상 조회
	 * @param dbMap 
	 */
	private void getTradeWholecakeCorrectionInfo(Map<String, Object> dbMap) {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			// 홀케익 수령완료 대사 대상 조회  
			this.tradeWholecakeCorrectionList = this.tradeCorrectionMgr.getWholecakeInfo (dbMap);
			
			this.tradeWholecakeCorrectionTotalCnt = this.tradeWholecakeCorrectionList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get TradeWholecakeCorrectionInfo Info Count : (").append(this.tradeWholecakeCorrectionList.size ()).append(")건 END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
			
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
		
	}
	
	/**
	 * 홀케익 미수령 폐기 대사 대상 조회
	 * @param dbMap
	 */
	private void getReceivedDisuseWholecakeInfo(Map<String, Object> dbMap) {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			// 홀케익 수령완료 대사 대상 조회  
			this.receivedDisuseWholecakeList = this.tradeCorrectionMgr.getWholecakeInfo (dbMap);
			
			this.receivedDisuseWholecakeTotalCnt = this.receivedDisuseWholecakeList.size ();
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("Get ReceivedDisuseWholecakeInfo Info Count : (").append(this.receivedDisuseWholecakeList.size ()).append(")건 END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
			
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}
		
	}
	
	/**
	 * 홀케익 수령완료 대사 완료 처리
	 */
	private void tradeWholecakeComplete() {
		
		try {
			long startTime = System.currentTimeMillis ();
			
			// 홀케익 수령완료 대사 완료 처리
			int resultCnt = this.tradeCorrectionMgr.tradeWholecakeComplete (tradeWholecakeCorrectionList);
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("tradeWholecakeComplete Count : (").append(resultCnt).append(")건 / FAIL Count : (").append(tradeWholecakeCorrectionTotalCnt - resultCnt).append(")건 END : ").append(System.currentTimeMillis () - startTime).append("ms");
			tLogger.info(logSb.toString());
		} catch ( Exception e ) {
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append(e.getMessage ());
			tLogger.error(logSb.toString(), e);
		}		
	}
	

	/**
	 * 홀케익 미수령 폐기 대사 완료 처리
	 */
	private void receivedDisuseWholecakeComplete() {
		
		try {
			long startTime = System.currentTimeMillis ();			
			// 홀케익 미수령 폐기 대사 완료 처리
			int resultCnt = this.tradeCorrectionMgr.receivedDisuseWholecakeComplete (receivedDisuseWholecakeList);
			
			logSb.delete(0, logSb.length()).append(this.loggerTitle).append("receivedDisuseWholecakeComplete Count : (").append(resultCnt).append(")건 / FAIL Count : (").append(receivedDisuseWholecakeTotalCnt - resultCnt).append(")건 END : ").append(System.currentTimeMillis () - startTime).append("ms");
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
