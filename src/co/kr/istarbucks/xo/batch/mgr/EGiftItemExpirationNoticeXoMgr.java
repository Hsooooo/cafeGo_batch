/*
 * @(#) $Id: EGiftItemExpirationNoticeXoMgr.java,v 1.1 2017/08/07 09:02:38 shinepop Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2017년도 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import co.kr.istarbucks.xo.batch.dao.EGiftItemExpirationNoticeXoDao;

public class EGiftItemExpirationNoticeXoMgr { 

	private final EGiftItemExpirationNoticeXoDao egiftitemexpirationnoticeXodao;
	
	public EGiftItemExpirationNoticeXoMgr () {
		this.egiftitemexpirationnoticeXodao = new EGiftItemExpirationNoticeXoDao ();
	}
	
	/**
	 * 만기 알림 대상 조회
	 * @param dbMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getEGiftItemExpirationNoticeTarget(Map<String, Object> dbMap) throws Exception {
		return this.egiftitemexpirationnoticeXodao.getEGiftItemExpirationNoticeTarget(dbMap);
	}
	
	/**
	 * XO_TMS_QUEUE 등록
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public void insertXoTmsQueue(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException  {
		this.egiftitemexpirationnoticeXodao.insertXoTmsQueue(xoSqlMap, dbMap);
	}
	
	/**
	 * TMS_PERSON_QUEUE 테이블에 정상 등록 되었다면 XO_TMS_QUEUE테이블에 push_insert_yn 컬럼의 값을 'Y' 업데이트
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public void updateXoTmsQueue(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException  {
		this.egiftitemexpirationnoticeXodao.updateXoTmsQueue(xoSqlMap, dbMap);
	}
	
	/**
	 * LMS 히스토리 등록
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public void setSendHistory(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException  {
		this.egiftitemexpirationnoticeXodao.setSendHistory(xoSqlMap, dbMap);
	}
	
	/**
	 * 예약 발송 후 INBOX 전송 여부 등록
	 * @param xoSqlMap
	 * @param giftOrderNo
	 * @throws SQLException
	 */
	public void updateReserveInbox(SqlMapClient xoSqlMap, String giftOrderNo)throws SQLException  {
		this.egiftitemexpirationnoticeXodao.updateReserveInbox(xoSqlMap, giftOrderNo);
	}
}
