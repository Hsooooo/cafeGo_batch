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
 * �������� �ŷ� ��� TradeCorrectionMgr.
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
	 * �ŷ� ��� ��� ��ȸ
	 * @return TradeCorrectionDto
	 * @throws Exception
	 */
	public List<TradeCorrectionDto> getTradeCorrectionInfo () throws Exception {
		return this.tradeCorrectionDao.getTradeCorrectionInfo ();
	}
	
	/**
	 * �ŷ� ��� ��� ��ȸ - �ŷ� ��¥ �Է�
	 * @return TradeCorrectionDto��
	 * @throws Exception
	 */
	public List<TradeCorrectionDto> getTradeCorrectionDateInfo (String tradeDate) throws Exception {
		return this.tradeCorrectionDao.getTradeCorrectionDateInfo (tradeDate);
	}
	
	/**
	 * �ŷ� ��� �Ϸ� ó��
	 * @return int
	 * @throws Exception
	 */
	public int tradeComplete ( List<TradeCorrectionDto> tradeDtoInfo ) throws Exception {
		return this.tradeCorrectionDao.tradeComplete (tradeDtoInfo);
	}
	
	/**
	 * �ֹ� ��� ��� ��ȸ
	 * @return TradeCorrectionDto
	 * @throws Exception
	 */
	public List<TradeCorrectionDto> getOrderCancelInfo () throws Exception {
		return this.tradeCorrectionDao.getOrderCancelInfo ();
	}
	
	/**
	 * �ֹ� ��� ��� ��ȸ - �ŷ� ��¥ �Է�
	 * @return TradeCorrectionDto
	 * @throws Exception
	 */
	public List<TradeCorrectionDto> getOrderCancelDateInfo (String tradeDate) throws Exception {
		return this.tradeCorrectionDao.getOrderCancelDateInfo (tradeDate);
	}
	
	/**
	 * �ֹ� ��� �Ϸ� ó��
	 * @return int
	 * @throws Exception
	 */
	public int orderCancel ( List<TradeCorrectionDto> tradeDtoInfo ) throws Exception {
		return this.tradeCorrectionDao.orderCancel (tradeDtoInfo);
	}

	/**
	 *  Ȧ���� (���ɿϷ� : O30, �̼��� ��� : O31) ��� ��� ��ȸ
	 * @param dbMap 
	 * @return
	 */
	public List<TradeCorrectionDto> getWholecakeInfo(Map<String, Object> dbMap) throws Exception {
		return this.tradeCorrectionDao.getWholecakeInfo (dbMap);
	}

	/**
	 * Ȧ���� ���ɿϷ� ��� �Ϸ� ó��
	 * @param tradeWholecakeCorrectionList
	 * @return
	 * @throws Exception
	 */
	public int tradeWholecakeComplete(List<TradeCorrectionDto> tradeWholecakeCorrectionList) throws Exception {
		return this.tradeCorrectionDao.tradeWholecakeComplete (tradeWholecakeCorrectionList);
	}

	/**
	 * Ȧ���� �̼��� ��� ��� �Ϸ� ó��
	 * @param receivedDisuseWholecakeList
	 * @return
	 * @throws Exception
	 */
	public int receivedDisuseWholecakeComplete(List<TradeCorrectionDto> receivedDisuseWholecakeList) throws Exception {
		return this.tradeCorrectionDao.receivedDisuseWholecakeComplete (receivedDisuseWholecakeList);
	}
}
