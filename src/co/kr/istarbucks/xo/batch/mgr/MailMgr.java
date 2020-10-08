/*
 * @(#) $Id: MailMgr.java,v 1.1 2016/11/10 00:55:27 dev99 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.mgr;

import java.sql.SQLException;

import co.kr.istarbucks.xo.batch.common.dto.msr.SendMailQueueDto;
import co.kr.istarbucks.xo.batch.dao.MailDao;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 메일 발송 MailMgr.
 * 
 * @author eZEN ksy
 * @since 2016. 10. 31.
 * @version $Revision: 1.1 $
 */
public class MailMgr {
	
	private final MailDao mailDao;
	
	public MailMgr() {
		this.mailDao = new MailDao();
	}

	/**
	 * 메일 발송 요청 등록
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public int insertSendMailQueue(SqlMapClient sqlMap, SendMailQueueDto params) throws SQLException {
		return this.mailDao.insertSendMailQueue(sqlMap, params);
	}
}
