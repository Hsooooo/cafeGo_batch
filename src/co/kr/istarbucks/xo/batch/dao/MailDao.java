/*
 * @(#) $Id: MailDao.java,v 1.1 2016/11/10 00:55:25 dev99 Exp $
 * Starbucks XO
 * Copyright 2014 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.dao;

import java.sql.SQLException;

import co.kr.istarbucks.xo.batch.common.dto.msr.SendMailQueueDto;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * ���� �߼� MailDao.
 * 
 * @author eZEN ksy
 * @since 2016. 10. 31.
 * @version $Revision: 1.1 $
 */
public class MailDao {

	/**
	 * ���� �߼� ��û ���
	 * @param sqlMap
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public int insertSendMailQueue(SqlMapClient sqlMap, SendMailQueueDto params) throws SQLException {
		return sqlMap.update("mail.insertSendMailQueue", params);
	}
}
