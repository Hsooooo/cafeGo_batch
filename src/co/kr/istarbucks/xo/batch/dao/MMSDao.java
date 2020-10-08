/*
 * @(#) $Id: MMSDao.java,v 1.2 2016/12/14 05:52:51 namgu1 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;

import co.kr.istarbucks.xo.batch.common.dto.xo.EmMmtFileDto;
import co.kr.istarbucks.xo.batch.common.dto.xo.SmtTranDto;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * MMS 발송  MMSDao.
 * 
 * @author eZEN ksy
 * @since 2016. 11. 03.
 * @version $Revision: 1.2 $
 */
public class MMSDao {

	/**
	 * MMS MT 첨부 파일 등록  
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public long insertEmMmtFile(SqlMapClient sqlMap, EmMmtFileDto params) throws SQLException {
		return (Long) sqlMap.insert("mms.insertEmMmtFile", params);
	}
	
	/**
	 * MMS 발송 QUEUE 등록  
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public long insertEmMmtTran(SqlMapClient sqlMap, SmtTranDto params) throws SQLException {
		return (Long) sqlMap.insert("mms.insertEmMmtTran", params);
	}

	
	/**
	 * SMS 발송 요청(계정 : MMS)
	 * @param sqlMap
	 * @param smtTranDto
	 * @return
	 * @throws SQLException
	 */
	public long insertSmtTran(SqlMapClient sqlMap, SmtTranDto smtTranDto) throws SQLException {
		return (Long) sqlMap.insert("mms.insertSmtTran", smtTranDto);
	}
}
