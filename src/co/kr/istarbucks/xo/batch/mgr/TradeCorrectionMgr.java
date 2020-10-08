/*
 * @(#) $Id: TradeCorrectionMgr.java,v 1.2 2016/11/30 07:03:19 namgu1 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.mgr;

import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.dto.xo.TradeCorrectionDto;
import co.kr.istarbucks.xo.batch.dao.TradeCorrectionDao;

/**
 * 영업정보 거래 대사 TradeCorrectionMgr.
 * @author eZEN
 * @since 2014. 2. 6.
 * @version $Revision: 1.2 $
 */
public class TradeCorrectionMgr {
	
	private final TradeCorrectionDao tradeCorrectionDao;
	
	public TradeCorrectionMgr () {
		this.tradeCorrectionDao = new TradeCorrectionDao ();
	}
	
	/**
	 * 거래 대사 대상 조회
	 * @return TradeCorrectionDto
	 * @throws Exception
	 */
	public List<TradeCorrectionDto> getTradeCorrectionInfo () throws Exception {
		return this.tradeCorrectionDao.getTradeCorrectionInfo ();
	}
	
	/**
	 * 거래 대사 대상 조회 - 거래 날짜 입력
	 * @return TradeCorrectionDtoㄴ
	 * @throws Exception
	 */
	public List<TradeCorrectionDto> getTradeCorrectionDateInfo (String tradeDate) throws Exception {
		return this.tradeCorrectionDao.getTradeCorrectionDateInfo (tradeDate);
	}
	
	/**
	 * 거래 대사 완료 처리
	 * @return int
	 * @throws Exception
	 */
	public int tradeComplete ( List<TradeCorrectionDto> tradeDtoInfo ) throws Exception {
		return this.tradeCorrectionDao.tradeComplete (tradeDtoInfo);
	}
	
	/**
	 * 주문 취소 대상 조회
	 * @return TradeCorrectionDto
	 * @throws Exception
	 */
	public List<TradeCorrectionDto> getOrderCancelInfo () throws Exception {
		return this.tradeCorrectionDao.getOrderCancelInfo ();
	}
	
	/**
	 * 주문 취소 대상 조회 - 거래 날짜 입력
	 * @return TradeCorrectionDto
	 * @throws Exception
	 */
	public List<TradeCorrectionDto> getOrderCancelDateInfo (String tradeDate) throws Exception {
		return this.tradeCorrectionDao.getOrderCancelDateInfo (tradeDate);
	}
	
	/**
	 * 주문 취소 완료 처리
	 * @return int
	 * @throws Exception
	 */
	public int orderCancel ( List<TradeCorrectionDto> tradeDtoInfo ) throws Exception {
		return this.tradeCorrectionDao.orderCancel (tradeDtoInfo);
	}

	/**
	 *  홀케익 (수령완료 : O30, 미수령 폐기 : O31) 대사 대상 조회
	 * @param dbMap 
	 * @return
	 */
	public List<TradeCorrectionDto> getWholecakeInfo(Map<String, Object> dbMap) throws Exception {
		return this.tradeCorrectionDao.getWholecakeInfo (dbMap);
	}

	/**
	 * 홀케익 수령완료 대사 완료 처리
	 * @param tradeWholecakeCorrectionList
	 * @return
	 * @throws Exception
	 */
	public int tradeWholecakeComplete(List<TradeCorrectionDto> tradeWholecakeCorrectionList) throws Exception {
		return this.tradeCorrectionDao.tradeWholecakeComplete (tradeWholecakeCorrectionList);
	}

	/**
	 * 홀케익 미수령 폐기 대사 완료 처리
	 * @param receivedDisuseWholecakeList
	 * @return
	 * @throws Exception
	 */
	public int receivedDisuseWholecakeComplete(List<TradeCorrectionDto> receivedDisuseWholecakeList) throws Exception {
		return this.tradeCorrectionDao.receivedDisuseWholecakeComplete (receivedDisuseWholecakeList);
	}
}
