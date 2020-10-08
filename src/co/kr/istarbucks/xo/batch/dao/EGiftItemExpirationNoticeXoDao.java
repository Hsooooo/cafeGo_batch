/*
 * @(#) $Id: EGiftItemExpirationNoticeXoDao.java,v 1.1 2017/08/07 09:02:38 shinepop Exp $
 * 
 * Starbucks Service
 * 
 * Copyright 2017�⵵ eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */

package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import co.kr.istarbucks.xo.batch.common.util.IBatisSqlConfig;

import com.ibatis.sqlmap.client.SqlMapClient;

public class EGiftItemExpirationNoticeXoDao { 
	
	/**
	 * ���� �˸� ��� ��ȸ
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings ( "unchecked" )
	public List<Map<String, String>> getEGiftItemExpirationNoticeTarget ( Map<String, Object> dbMap ) throws Exception {
		String sqlId = "eGiftItemXo.getEGiftItemExpirationNoticeTarget";
		SqlMapClient xoSqlMap = IBatisSqlConfig.getXoSqlMapInstance ();
		return xoSqlMap.queryForList (sqlId, dbMap);
	}
	
	/**
	 * XO_TMS_QUEUE ���
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public void insertXoTmsQueue(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException {
		xoSqlMap.insert("eGiftItemXo.insertXoTmsQueue", dbMap);
	}
	
	/**
	 * TMS_PERSON_QUEUE ���̺� ���� ��� �Ǿ��ٸ� XO_TMS_QUEUE���̺� push_insert_yn �÷��� ���� 'Y' ������Ʈ
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public void updateXoTmsQueue(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException  {
		xoSqlMap.update("eGiftItemXo.updateXoTmsQueue", dbMap);
	}
	
	/**
	 * LMS �����丮 ���
	 * @param xoSqlMap
	 * @param dbMap
	 * @return
	 * @throws SQLException
	 */
	public void setSendHistory(SqlMapClient xoSqlMap, Map<String, Object> dbMap) throws SQLException  {
		xoSqlMap.insert("eGiftItemXo.setSendHistory", dbMap);
	}
	
	/**
	 * ���� �߼� �� INBOX ���� ���� ���
	 * @param xoSqlMap
	 * @param giftOrderNo
	 * @throws SQLException
	 */
	public void updateReserveInbox(SqlMapClient xoSqlMap, String giftOrderNo) throws SQLException  {
		xoSqlMap.update("eGiftItemXo.updateReserveInbox", giftOrderNo);
	}
}
